package net.youmi.ads.base.download;

import android.content.Context;
import android.text.TextUtils;

import net.youmi.ads.base.download.core.DownloadHandler;
import net.youmi.ads.base.download.listener.DownloadPublisher;
import net.youmi.ads.base.download.listener.IFileAvailableChecker;
import net.youmi.ads.base.download.listener.OnDownloadListener;
import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.download.store.AbsDownloadDir;
import net.youmi.ads.base.hash.MD5;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.pool.BaseCacheExecutorService;
import net.youmi.ads.base.pool.IExecuteListener;

import java.util.HashMap;

/**
 * @author zhitao
 * @since 2017-04-14 16:40
 */
public abstract class AbsCachedDownloadManager implements IFileAvailableChecker, IExecuteListener {
	
	/**
	 * 是否为首次下载，如果是首次下载的话，那么需要优化一下下载目录空间
	 */
	private boolean isFirstDownload = true;
	
	/**
	 * 记录正在执行的下载实体类
	 */
	private HashMap<FileDownloadTask, DownloadHandler> mFileDownloadTaskDownloadHandlerHashMap = new HashMap<>();
	
	/**
	 * 本下载管理器锁采用的下载线程池
	 */
	private BaseCacheExecutorService mBaseCacheExecutorService;
	
	/**
	 * 一个带有自定义规则的下载目录(如：目录是否会自动清理，目录下的文件命名规范等)
	 */
	private AbsDownloadDir mAbsDownloadDir;
	
	/**
	 * 所有下载任务对象共用的下载监听观察者管理器
	 */
	private DownloadPublisher mDownloadPublisher;
	
	protected AbsCachedDownloadManager() {
		
		// 设置下载线程池
		mBaseCacheExecutorService = new BaseCacheExecutorService("Y-M-Cached-Download").withIExecuteListener(this);
		
		//　设置下载任务的观察者监听管理器
		mDownloadPublisher = new DownloadPublisher();
	}
	
	/**
	 * @param context 上下文
	 *
	 * @return 一个下载的最终目录,{@link AbsDownloadDir}是一个带有自定义规则的下载目录(如：目录是否会自动清理，目录下的文件命名规范等)
	 */
	public abstract AbsDownloadDir newDownloadDir(Context context);
	
	public boolean download(Context context, String rawUrl) {
		return download(context, rawUrl, null, -1);
	}
	
	public boolean download(Context context, String rawUrl, String md5SumString) {
		return download(context, rawUrl, md5SumString, -1);
	}
	
	public boolean download(Context context, String rawUrl, String md5SumString, long contentLength) {
		return download(context, new FileDownloadTask(rawUrl, md5SumString, contentLength, 500), true);
	}
	
	public boolean download(Context context, FileDownloadTask fileDownloadTask) {
		return download(context, fileDownloadTask, true);
	}
	
	/**
	 * 是否能成功提交下载任务到线程池中异步执行
	 *
	 * @param context          上下文
	 * @param fileDownloadTask 下载任务描述
	 * @param isCallBack       是否回调下载过程中相关的内容，如：是否回调下载中，回调下载成功等
	 *
	 * @return <ul>
	 * <li>true：成功提交下载任务到线程池中异步执行</li>
	 * <li>false：提交下载任务到线程池中异步执行失败</li>
	 * </ul>
	 */
	public boolean download(Context context, FileDownloadTask fileDownloadTask, boolean isCallBack) {
		// 检查任务是否在执行中
		try {
			if (mAbsDownloadDir == null) {
				mAbsDownloadDir = newDownloadDir(context.getApplicationContext());
			}
			
			if (mFileDownloadTaskDownloadHandlerHashMap.keySet().contains(fileDownloadTask)) {
				if (DLog.isDownloadLog) {
					DLog.i("下载管理器[%s]:当前任务[%d]已经在执行中", this.getClass().getSimpleName(), fileDownloadTask.hashCode());
				}
				return true;
			} else {
				
				DownloadHandler mAbsDownloader = new DownloadHandler(
						context.getApplicationContext(),
						fileDownloadTask,
						mAbsDownloadDir,
						isCallBack ? mDownloadPublisher : null,
						this,
						true
				);
				
				// 如果任务没有在执行中的话，就创建一个下载任务并加入到线程池中执行
				// 仅仅在线程池执行第一个任务的时候采进行下载目录优化
				if (isFirstDownload) {
					mAbsDownloader.setNeed2OptCacheDir(isFirstDownload);
					isFirstDownload = false;
				}
				
				mBaseCacheExecutorService.execute(mAbsDownloader);
				return true;
			}
		} catch (Throwable e) {
			DLog.e(e);
			return false;
		}
	}
	
	public void stopDownload(String rawUrl) {
		stopDownload(new FileDownloadTask(rawUrl));
	}
	
	public void stopDownload(FileDownloadTask fileDownloadTask) {
		// 检查任务是否处于缓存中
		if (mFileDownloadTaskDownloadHandlerHashMap.keySet().contains(fileDownloadTask)) {
			
			// 检查任务是否处于队列中，如果是的话就只需要从缓冲队列中移除该任务即可，而不用stopdownload
			// 因为缓冲队列中的任务是还没有执行的
			DownloadHandler downloader = mFileDownloadTaskDownloadHandlerHashMap.get(fileDownloadTask);
			if (DLog.isDownloadLog) {
				DLog.i("任务正在执行中，准备停止任务");
			}
			downloader.stopDownload();
			mFileDownloadTaskDownloadHandlerHashMap.remove(fileDownloadTask);
		}
	}
	
	/**
	 * 即时获取某个任务的下载状态
	 *
	 * @param fileDownloadTask 下载任务对象
	 *
	 * @return <ul>
	 * <li>-1:没有这个任务的下载</li>
	 * <li>{@link OnDownloadListener} 中的下载状态</li>
	 * </ul>
	 */
	public int getDownloadStats(FileDownloadTask fileDownloadTask) {
		return mDownloadPublisher.getDownloadStats(fileDownloadTask);
	}
	
	/**
	 * 即时获取某个任务的下载状态
	 *
	 * @param rawUrl 原始下载url
	 *
	 * @return <ul>
	 * <li>-1:没有这个任务的下载</li>
	 * <li>{@link OnDownloadListener} 中的下载状态</li>
	 * </ul>
	 */
	public int getDownloadStats(String rawUrl) {
		FileDownloadTask fileDownloadTask = new FileDownloadTask(rawUrl);
		return getDownloadStats(fileDownloadTask);
	}
	
	/**
	 * 即时获取某个任务的下载状态
	 *
	 * @param rawUrl   原始下载url
	 * @param identify 任务唯一标识
	 *
	 * @return <ul>
	 * <li>-1:没有这个任务的下载</li>
	 * <li>{@link OnDownloadListener} 中的下载状态</li>
	 * </ul>
	 */
	public int getDownloadStats(String rawUrl, String identify) {
		FileDownloadTask fileDownloadTask = new FileDownloadTask(rawUrl);
		fileDownloadTask.setIdentify(identify);
		return getDownloadStats(fileDownloadTask);
	}
	
	/**
	 * 注册下载监听观察者
	 *
	 * @param onDownloadListener 下载监听者对象
	 *
	 * @return 是否注册成功
	 */
	public boolean addOnDownloadListener(OnDownloadListener onDownloadListener) {
		return mDownloadPublisher.addListener(onDownloadListener);
	}
	
	/**
	 * 移除下载监听观察者
	 *
	 * @param onDownloadListener 下载监听者对象
	 *
	 * @return 是否移除成功
	 */
	public boolean removeOnDownloadListener(OnDownloadListener onDownloadListener) {
		return mDownloadPublisher.removeListener(onDownloadListener);
	}
	
	/**
	 * 使用场合：
	 * 如果你觉得你下载的文件被劫持为另一个url，那么就可以实现一下下面的接口
	 * <p>
	 * 如：
	 * <ul>
	 * <li>检查文件长度是否等于服务器三的长度</li>
	 * <li>重新计算文件的md5和task中的md5(服务器那边返回的比较一下)</li>
	 * <li>或者重新向服务器请求这个文件的md5然后做对比</li>
	 * </ul>
	 * <p>
	 * 子类可以重写，但是重写时，建议在最后 return super.isStoreFileAvailable
	 *
	 * @param fileDownloadTask 下载任务
	 *
	 * @return true：有效；false：无效
	 */
	@Override
	public boolean isStoreFileAvailable(FileDownloadTask fileDownloadTask) {
		try {
			if (!fileDownloadTask.getStoreFile().exists() || !fileDownloadTask.getStoreFile().isFile()) {
				return false;
			}
			
			// 如果这个任务在下载之前有设置下载文件最后的长度的话，那么在下载完毕之后，比较一下最终下载完毕的文件长度是否和一开始设置的一致
			if (fileDownloadTask.getTotalLength() > 0) {
				if (fileDownloadTask.getStoreFile().length() != fileDownloadTask.getTotalLength()) {
					return false;
				}
			}
			
			// 如果这个任务有传入MD5的话
			if (!TextUtils.isEmpty(fileDownloadTask.getDownloadFileMd5sum())) {
				String destFileMd5 = MD5.getFileMd5(fileDownloadTask.getStoreFile());
				if (!fileDownloadTask.getDownloadFileMd5sum().equals(destFileMd5)) {
					return false;
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return true;
	}
	
	/**
	 * 在线程池执行每个任务之前的额外操作
	 */
	@Override
	public void beforeExecute(Thread t, Runnable r) {
		try {
			DownloadHandler downloadHandler = (DownloadHandler) r;
			FileDownloadTask absFileDownloadTask = downloadHandler.getFileDownloadTask();
			if (absFileDownloadTask != null) {
				mFileDownloadTaskDownloadHandlerHashMap.put(absFileDownloadTask, downloadHandler);
				if (DLog.isDownloadLog) {
					DLog.i(
							"下载管理器[%s]:添加下载任务[%d] %s",
							this.getClass().getSimpleName(),
							absFileDownloadTask.hashCode(),
							absFileDownloadTask.getRawDownloadUrl()
					);
				}
				
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
	
	/**
	 * 在线程池执行完每个任务之后的额外操作
	 */
	@Override
	public void afterExecute(Runnable r, Throwable t) {
		try {
			DownloadHandler downloadHandler = (DownloadHandler) r;
			FileDownloadTask fileDownloadTask = downloadHandler.getFileDownloadTask();
			if (fileDownloadTask != null) {
				mFileDownloadTaskDownloadHandlerHashMap.remove(fileDownloadTask);
				if (DLog.isDownloadLog) {
					DLog.i(
							"下载管理器[%s]:移除下载任务[%d] %s",
							this.getClass().getSimpleName(),
							fileDownloadTask.hashCode(),
							fileDownloadTask.getRawDownloadUrl()
					);
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
}