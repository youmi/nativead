package net.youmi.ads.base.download.base;

import android.content.Context;

import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.network.HttpRequesterFactory;
import net.youmi.ads.base.utils.FileUtils;
import net.youmi.ads.base.utils.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @author zhitao
 * @since 2017-04-14 15:53
 */
public class HttpURLConnectionDownloader implements IDownloader {
	
	/**
	 * 每次读入网络流写入到文件的长度(字节)
	 */
	private final static int BUFFER_SIZE = 2048;
	
	/**
	 * 文件的最终长度
	 */
	private long mDownloadFileFinalLength = 0;
	
	/**
	 * 下载任务是否正在进行中
	 */
	private boolean mIsRunning;
	
	/**
	 * 是否被主动停止了
	 */
	private boolean mIsStop;
	
	private Context mContext;
	
	private FileDownloadTask mFileDownloadTask;
	
	public HttpURLConnectionDownloader(Context context, FileDownloadTask fileDownloadTask) {
		mContext = context;
		mFileDownloadTask = fileDownloadTask;
		mIsRunning = false;
		mIsStop = false;
	}
	
	/**
	 * 检查参数是否有效
	 *
	 * @return
	 */
	private boolean isParamsVaild() {
		try {
			mContext = mContext.getApplicationContext();
		} catch (Throwable e) {
			return false;
		}
		if (!mFileDownloadTask.isValid()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 开始下载
	 *
	 * @return 下载的最终状态
	 */
	@Override
	public DownloadStatus download() {
		// 开始之前先检查任务状态
		if (!isParamsVaild()) {
			return new DownloadStatus(DownloadStatus.Code.ERROR_PARAMS);
		}
		
		RandomAccessFile randomAccessFile = null;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream;
		
		// 标记为开始
		mIsRunning = true;
		mIsStop = false;
		
		try {
			
			// 创建默认的HttpURLConnection
			httpURLConnection = HttpRequesterFactory.newHttpURLConnection(mContext, mFileDownloadTask.getRawDownloadUrl());
			if (httpURLConnection == null) {
				throw new NullPointerException("connection is null");
			}
			
			// 设置请求方式 GET
			httpURLConnection.setRequestMethod("GET");
			
			// 是否使用断点续传
			// 如果起始点大于0，即指定了下载起始点，需要配置块下载——起始至文件结束
			// 如果起始点为0,而结束点没有指定，则说明是从文件头部开始下载整个文件，因此不指定RANGE
			long start = mFileDownloadTask.getTempFile().length();
			if (start > 0) {
				httpURLConnection.setRequestProperty("RANGE", String.format("bytes=%d-", start));
			}
			
			if (httpURLConnection.getResponseCode() >= 200 && httpURLConnection.getResponseCode() < 300) {
				inputStream = httpURLConnection.getInputStream();
			} else {
				//				inputStream = httpURLConnection.getErrorStream();
				return new DownloadStatus(DownloadStatus.Code.ERROR_HTTP_CODE);
			}
			
			if (inputStream == null) {
				throw new NullPointerException("InputStream is null");
			}
			mFileDownloadTask.setDestDownloadUrl(httpURLConnection.getURL().toURI().toString());
			// 这里特别解释一下：
			// httpURLConnection.getContentLength()拿到的是本次http请求的文件部分长度。
			// 而可能本次http请求之前就已经下载了其他一部分数据(mFileDownloadTask.getTempFile.length())
			// 因此mDownloadFileFinalLength是两者相加的。
			long contentLength = httpURLConnection.getContentLength();
			if (contentLength == -1) {
				return new DownloadStatus(DownloadStatus.Code.ERROR_HTTP_RESPONSE);
			}
			
			// 文件最终的长度= 当前文件长度 + 本次下载长度
			mDownloadFileFinalLength = start + contentLength;
			
			if (DLog.isDownloadLog) {
				StringBuilder sb = new StringBuilder(256);
				sb.append("本次下载信息:");
				sb.append("\n * 原始下载url:").append(mFileDownloadTask.getRawDownloadUrl());
				sb.append("\n * 最终下载url:").append(mFileDownloadTask.getDestDownloadUrl());
				sb.append("\n * http返回状态码:").append(httpURLConnection.getResponseCode());
				sb.append("\n * 本次下载的长度(ContentLength):").append(contentLength);
				sb.append("\n * 目标下载文件路径:").append(mFileDownloadTask.getTempFile().getAbsolutePath());
				sb.append("\n * 起始下载位置:").append(start);
				DLog.i(sb.toString());
			}
			
			// 设置文件的写入起点
			File tempFile = FileUtils.getValidFile(mFileDownloadTask.getTempFile());
			if (tempFile == null) {
				return new DownloadStatus(DownloadStatus.Code.ERROR_UNKNOWN);
			}
			randomAccessFile = new RandomAccessFile(tempFile, "rw");
			randomAccessFile.seek(start);
			
			byte[] buff = new byte[BUFFER_SIZE];
			int length;
			while (mIsRunning && ((length = inputStream.read(buff)) > 0)) {
				randomAccessFile.write(buff, 0, length);
			}
			
			// 下载成功之后，这里做一个简单的判断，判断文件当前长度是否等于已经完成的长度
			if (mDownloadFileFinalLength == mFileDownloadTask.getTempFile().length()) {
				
				// 这里需要将缓存文件重命名为最终文件
				if (mFileDownloadTask.getTempFile().renameTo(mFileDownloadTask.getStoreFile())) {
					return new DownloadStatus(DownloadStatus.Code.SUCCESS);
				} else {
					return new DownloadStatus(DownloadStatus.Code.EXCEPTION_RENAME_TEMPFILE_TO_STOREFILE);
				}
			}
			
			// 到这里就表示mContentLength != mFileDownloadTask.getTempFile().length()
			// 那么出现上面的结论是有两种情况的
			// 1. mIsRunning依旧为true 即用户并没有终止下载，那么mContentLength 还是不等于下载缓存文件长度的话，
			//    那么可能就是被注入了额外的内容，这个时候需要表示为下载失败，而不是下载停止
			// 2. mIsRunning为fasle 即用户主动终止了下载，那么肯定是不相等的
			if (mIsRunning) {
				return new DownloadStatus(DownloadStatus.Code.EXCEPTION_DOWNLOADFLIE_CHANGE);
			} else {
				if (DLog.isDownloadLog) {
					StringBuilder sb = new StringBuilder(256);
					sb.append("下载被停止:");
					sb.append("\n * 原始下载url:").append(mFileDownloadTask.getRawDownloadUrl());
					sb.append("\n * 最终下载url:").append(mFileDownloadTask.getDestDownloadUrl());
					sb.append("\n * 目标文件路径:").append(mFileDownloadTask.getTempFile().getAbsolutePath());
					sb.append("\n * 文件当前长度:").append(mFileDownloadTask.getTempFile().length());
					sb.append("\n * 文件下载完的预计长度:").append(mDownloadFileFinalLength);
					sb.append("\n * 停止时，已完成的进度百分比:").append(getDownloadPercent());
					DLog.i(sb.toString());
				}
				// 这里后续可以考虑在更早的地方判断mIsRunning来判断是否被用户停止了下载任务
				return new DownloadStatus(DownloadStatus.Code.STOP);
			}
		} catch (ConnectException e) {
			return new DownloadStatus(DownloadStatus.Code.EXCEPTION_UNKNOWN, e);
		} catch (SocketTimeoutException e) {
			// 请求超时
			return new DownloadStatus(DownloadStatus.Code.EXCEPTION_UNKNOWN, e);
		} catch (UnknownHostException e) {
			// 网络没有打开或者没有配置网络权限的时候会抛出这个异常
			return new DownloadStatus(DownloadStatus.Code.EXCEPTION_UNKNOWN, e);
		} catch (SocketException e) {
			// 请求被关闭
			return new DownloadStatus(DownloadStatus.Code.EXCEPTION_UNKNOWN, e);
		} catch (NullPointerException e) {
			// 空指针异常
			return new DownloadStatus(DownloadStatus.Code.ERROR_UNKNOWN, e);
		} catch (Exception e) {
			// 未知异常
			return new DownloadStatus(DownloadStatus.Code.EXCEPTION_UNKNOWN, e);
		} finally {
			// 标记下载停止
			mIsRunning = false;
			try {
				if (httpURLConnection != null) {
					httpURLConnection.disconnect();
				}
			} catch (Throwable e) {
				DLog.e(e);
			}
			IOUtils.close(randomAccessFile);
		}
	}
	
	/**
	 * @return 获取本次下载的任务模型
	 */
	@Override
	public FileDownloadTask getFileDownloadTask() {
		return mFileDownloadTask;
	}
	
	/**
	 * @return 获取本次下载文件的总长度
	 */
	@Override
	public long getDownloadFileFinalLength() {
		return mDownloadFileFinalLength;
	}
	
	/**
	 * @return 获取已经完成的长度
	 */
	@Override
	public long getCompleteLength() {
		return mFileDownloadTask.getTempFile().length();
	}
	
	/**
	 * @return 获取下载进度的百分比
	 */
	@Override
	public int getDownloadPercent() {
		long contentLength = mDownloadFileFinalLength;
		if (contentLength > 0) {
			long completeLength = mFileDownloadTask.getTempFile().length();
			return (int) ((completeLength * 100) / contentLength);
		}
		return 0;
	}
	
	/**
	 * 停止下载过程
	 * <p>
	 * 调用下载停止之后，并不会立即停止下载的，而是程序跑到判断是否需要停止下载的地方才会停止的，
	 */
	@Override
	public void stop() {
		mIsRunning = false;
		mIsStop = true;
	}
	
	/**
	 * 是否被停止，只有调用了{@link #stop()}才会变为true，而调用了{@link #download()}之后就会变为false
	 *
	 * @return 是否被停止
	 *
	 * @see #isRunning()
	 */
	@Override
	public boolean isStop() {
		return mIsStop;
	}
	
	/**
	 * 是否正在下载中，调用了{@link #download()}之后就会变为true，下载结束或者调用了{@link #stop()}都会变为false
	 * <p>
	 * 和 {@link #isStop()}的区别
	 * <ol>
	 * <li>任务下载结束(成功或者失败)，或者被用户主动停止下载任务，isRunning都为false，</li>
	 * <li>只有用户主动停止下载任务（即调用{@link #stop()}，isStop才会是true，当再次调用{@link #download()}方法之后，isStop就会变为false</li>
	 * </ol>
	 *
	 * @return 是否在正下载中
	 *
	 * @see #isStop()
	 */
	@Override
	public boolean isRunning() {
		return mIsRunning;
	}
}
