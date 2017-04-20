package net.youmi.ads.base.download.base;

import android.content.Context;

import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.utils.NetworkUtils;

/**
 * @author zhitao
 * @since 2017-04-14 16:08
 */
public class AutoRetryDownloader implements IDownloader {
	
	/**
	 * 默认下一次重试的间隔时间:10秒
	 */
	private final static int RETRY_INTERVAL_TIME_MS = 10000;
	
	/**
	 * 默认最大重试次数:8次
	 */
	private final static int MAX_RETRY_TIMES = 8;
	
	private Context mApplicationContext;
	
	/**
	 * 下一次重试的时间间隔
	 */
	private int mRetryIntervalTime_ms;
	
	/**
	 * 最大重试次数
	 */
	private int mMaxRetryTimes;
	
	/**
	 * 是否正在运行
	 */
	private boolean mIsRunning;
	
	/**
	 * 是否被主动停止了本次下载任务
	 */
	private boolean mIsStop;
	
	/**
	 * 抽象的基本下载逻辑类
	 */
	private IDownloader mDownloader;
	
	/**
	 * 下载任务数据模型
	 */
	private FileDownloadTask mFileDownloadTask;
	
	/**
	 * @param context             上下文
	 * @param fileDownloadTask    下载任务对象
	 * @param maxRetryTimes       最大重试次数
	 * @param retryIntervalTimeMS 下一次重试的时间间隔(ms)
	 */
	public AutoRetryDownloader(Context context, FileDownloadTask fileDownloadTask, int maxRetryTimes, int retryIntervalTimeMS) {
		mApplicationContext = context.getApplicationContext();
		mFileDownloadTask = fileDownloadTask;
		
		mMaxRetryTimes = maxRetryTimes > 0 ? maxRetryTimes : MAX_RETRY_TIMES;
		mRetryIntervalTime_ms = retryIntervalTimeMS > 0 ? retryIntervalTimeMS : RETRY_INTERVAL_TIME_MS;
		
		mDownloader = new HttpURLConnectionDownloader(mApplicationContext, mFileDownloadTask);
		mIsRunning = false;
		mIsStop = false;
	}
	
	/**
	 * @param context          上下文
	 * @param fileDownloadTask 下载任务对象
	 */
	public AutoRetryDownloader(Context context, FileDownloadTask fileDownloadTask) {
		this(context, fileDownloadTask, 0, 0);
	}
	
	/**
	 * 开始下载
	 *
	 * @return 下载的最终状态
	 */
	@Override
	public DownloadStatus download() {
		mIsRunning = true;
		mIsStop = false;
		try {
			int mRunCounter = 0;
			while (mIsRunning) {
				++mRunCounter;
				if (DLog.isDownloadLog) {
					DLog.i("===第[%d]次下载===", mRunCounter);
				}
				
				// 从第二次可重试下载开始，要检查一下网络情况
				if (mRunCounter >= 2) {
					
					if (!NetworkUtils.isNetworkAvailable(mApplicationContext)) {
						
						if (DLog.isDownloadLog) {
							DLog.i("当前网络不可用，等待[%d]毫秒后再次检查网络状态", mRetryIntervalTime_ms);
						}
						try {
							Thread.sleep(mRetryIntervalTime_ms);
						} catch (Throwable e) {
							DLog.e(e);
						}
						
						// 如果之前检查到网络不行的话，那么一段时间之后，进行下载之前还需要检查一遍网络
						// 如果这个时候网络还是不行的话，就没必要进行下载了
						if (!NetworkUtils.isNetworkAvailable(mApplicationContext)) {
							
							// 如果网络还是不行，则判断是否是否已经达到重试上限
							if (mRunCounter >= mMaxRetryTimes) {
								if (DLog.isDownloadLog) {
									DLog.e("当前网络不可用，重试次数已经达到上限[%d]，结束下载", mMaxRetryTimes);
								}
								// 由于网络不成功导致的重试，达到最大限定次数后取消，同时标记为下载失败
								return new DownloadStatus(DownloadStatus.Code.ERROR_REACH_MAX_DOWNLOAD_TIMES);
							}
							if (DLog.isDownloadLog) {
								DLog.i("当前网络不可用");
							}
							
							// 如果还没有达最大次数就进行下一次循环
							continue;
						}
					}
				}
				
				DownloadStatus finalDownloadStatus = mDownloader.download();
				if (finalDownloadStatus == null) {
					return new DownloadStatus(DownloadStatus.Code.ERROR_UNKNOWN, new Exception("null status 1"));
				}
				
				if (DownloadStatus.Code.SUCCESS == finalDownloadStatus.getDownloadStatusCode()) {
					return finalDownloadStatus;
					
				} else if (DownloadStatus.Code.STOP == finalDownloadStatus.getDownloadStatusCode()) {
					return finalDownloadStatus;
					
				} else if (finalDownloadStatus.getDownloadStatusCode() >= 150 &&
				           finalDownloadStatus.getDownloadStatusCode() <= 199) {
					// 不可重试的下载失败类型
					if (DLog.isDownloadLog) {
						DLog.e("不可重试下载失败\n%s", finalDownloadStatus.toString());
					}
					return finalDownloadStatus;
					
				} else if (finalDownloadStatus.getDownloadStatusCode() >= 100 &&
				           finalDownloadStatus.getDownloadStatusCode() <= 149) {
					// 可重试的下载失败类型
					if (DLog.isDownloadLog) {
						DLog.e("可重试下载失败\n%s", finalDownloadStatus.toString());
					}
					
					// 如果网络还是不行，则判断是否是否已经达到重试上限
					if (mRunCounter >= mMaxRetryTimes) {
						if (DLog.isDownloadLog) {
							DLog.e("下载失败，属于不可重试类型失败，重试次数已经达到上限[%d]，结束下载", mMaxRetryTimes);
						}
						return finalDownloadStatus;
					}
				}
			}
			
			// 到这里就表示循环重试过程中，下载任务被停止聊
			// 那么这里就返回任务被停止而不是下载失败
			if (isStop()) {
				return new DownloadStatus(DownloadStatus.Code.STOP);
			}
			
		} catch (Throwable e) {
			DLog.e(e);
			return new DownloadStatus(DownloadStatus.Code.ERROR_UNKNOWN, e);
		} finally {
			mIsRunning = false;
		}
		return new DownloadStatus(DownloadStatus.Code.ERROR_UNKNOWN, new Exception("null status 2"));
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
		return mDownloader.getDownloadFileFinalLength();
	}
	
	/**
	 * @return 获取已经完成的长度
	 */
	@Override
	public long getCompleteLength() {
		return mDownloader.getCompleteLength();
	}
	
	/**
	 * @return 获取下载进度的百分比
	 */
	@Override
	public int getDownloadPercent() {
		return mDownloader.getDownloadPercent();
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
		if (mDownloader != null) {
			mDownloader.stop();
		}
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
