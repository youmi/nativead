package net.youmi.ads.base.download.core;

import android.content.Context;

import net.youmi.ads.base.download.base.AutoRetryDownloader;
import net.youmi.ads.base.download.base.DownloadStatus;
import net.youmi.ads.base.download.base.IDownloader;
import net.youmi.ads.base.download.listener.DownloadPublisher;
import net.youmi.ads.base.download.listener.IFileAvailableChecker;
import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.download.store.AbsDownloadDir;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.pool.GlobalCacheExecutor;
import net.youmi.ads.base.utils.FileUtils;
import net.youmi.ads.base.utils.NetworkUtils;

/**
 * @author zhitao
 * @since 2017-04-14 16:18
 */
public class DownloadHandler implements Runnable {
	
	private Context mApplicationContext;
	
	/**
	 * 下载任务数据模型
	 */
	private final FileDownloadTask mFileDownloadTask;
	
	/**
	 * 下载目录
	 */
	private AbsDownloadDir mAbsDownloadDir;
	
	/**
	 * 下载核心实现类
	 */
	private final IDownloader mIDownloader;
	
	/**
	 * 下载状态观察者管理器：用于通知旗下所有观察者最新的下载状态
	 */
	private final DownloadPublisher mBaseDownloadNotifier;
	
	/**
	 * 下载完成后的文件校验处理：交由各个下载管理器实现，外部传入
	 */
	private final IFileAvailableChecker mIFileAvailableChecker;
	
	/**
	 * 下载最终状态
	 */
	private DownloadStatus mDownloadStatus;
	
	/**
	 * 下载之前是否需要优化下载目录
	 */
	private boolean mIsNeed2OptCacheDir = false;
	
	/**
	 * 是否正在运行中
	 */
	private boolean mIsRunning = false;
	
	/**
	 * 是否支持多进程下载
	 */
	private boolean mIsSupportMultiProcessDownload;
	
	/**
	 * 上一次的增长量
	 */
	private long mLastRecordCompleteLength = 0;
	
	/**
	 * 上一次回调的时间
	 */
	private long mLastRecordNotifyTime_ms = 0;
	
	/**
	 * @param context                       上下文
	 * @param fileDownloadTask              下载任务描述数据模型
	 * @param absDownloadDir                一个带有自定义规则的下载目录(如：目录是否会自动清理，目录下的文件命名规范等)
	 * @param baseDownloadNotifier          下载状态监听观察者管理器
	 * @param iFileAvailableChecker         任务下载完成后的检查器，主要用于检查下载完成的文件是否有效
	 * @param isSupportMultiProcessDownload 是否支持多进程下载
	 */
	public DownloadHandler(Context context, FileDownloadTask fileDownloadTask, AbsDownloadDir absDownloadDir,
			DownloadPublisher baseDownloadNotifier, IFileAvailableChecker iFileAvailableChecker,
			boolean isSupportMultiProcessDownload) {
		mApplicationContext = context.getApplicationContext();
		mFileDownloadTask = fileDownloadTask;
		mAbsDownloadDir = absDownloadDir;
		mFileDownloadTask.setTempFile(mAbsDownloadDir.newDownloadTempFile(mFileDownloadTask));
		mFileDownloadTask.setStoreFile(mAbsDownloadDir.newDownloadStoreFile(mFileDownloadTask));
		mBaseDownloadNotifier = baseDownloadNotifier;
		mIFileAvailableChecker = iFileAvailableChecker;
		mIsSupportMultiProcessDownload = isSupportMultiProcessDownload;
		
		// 实质下载器
		mIDownloader = new AutoRetryDownloader(mApplicationContext, mFileDownloadTask);
	}
	
	@Override
	public void run() {
		download();
	}
	
	public FileDownloadTask getFileDownloadTask() {
		return mFileDownloadTask;
	}
	
	/**
	 * 设置需要在执行下载逻辑之前进行下载目录的优化
	 */
	public void setNeed2OptCacheDir(boolean isNeed2OptCacheDir) {
		mIsNeed2OptCacheDir = isNeed2OptCacheDir;
	}
	
	/**
	 * 调用这个方法之后会阻塞线程进行下载，之后可以通过{@link #getFinalDownlaodStatus()}方法获取下载结果
	 */
	public void download() {
		mIsRunning = true;
		// 如果下载目录无效，直接结束
		if (!mAbsDownloadDir.isDirValid()) {
			mIsRunning = false;
			mDownloadStatus = new DownloadStatus(DownloadStatus.Code.ERROR_DOWNLOAD_DIR_SETTINGS_INVALID);
			return;
		}
		if (mIsNeed2OptCacheDir) {
			// 这里可以考虑是否针对返回值做一下处理
			mAbsDownloadDir.optDir();
		}
		change2DownloadInit();
		mIsRunning = false;
	}
	
	/**
	 * 调用了这个方法之后，尝试进行停止下载，可能会有一小段时间之后才停止下载
	 */
	public void stopDownload() {
		mIsRunning = false;
		mIDownloader.stop();
	}
	
	/**
	 * @return 获取最终下载状态，需要在{@link #download()}之后调用
	 */
	public DownloadStatus getFinalDownlaodStatus() {
		return mDownloadStatus;
	}
	
	/**
	 * 下载初始化，主要在下载之前做一些校验
	 */
	private void change2DownloadInit() {
		
		if (DLog.isDownloadLog) {
			DLog.i("===DownloadInit===");
		}
		try {
			// 1.检查参数是否有效
			if (mApplicationContext == null) {
				change2DownloadFailed(new DownloadStatus(DownloadStatus.Code.ERROR_PARAMS));
			}
			
			if (!mFileDownloadTask.isValid()) {
				change2DownloadFailed(new DownloadStatus(DownloadStatus.Code.ERROR_PARAMS));
			}
			
			// 2. 检查目标存储文件是否存在
			if (mFileDownloadTask.getStoreFile().exists()) {
				change2DestFileAlreadyExist();
				return;
			}
			
			// 3. 检查下载缓存文件有效性
			if (mFileDownloadTask.getTempFile().exists()) {
				
				// 如果下载缓存文件路径是一个目录，返回失败
				if (mFileDownloadTask.getTempFile().isDirectory()) {
					change2DownloadFailed(new DownloadStatus(DownloadStatus.Code.ERROR_LOCAL_FILE_TYPE));
					return;
				}
				
			}
			
			// 4. 下载之前还应该检查下载目录的有效性
			// （
			//      如是否可写，空间是否可够之类的，
			//      但是因为最终是在线程池中执行下载线程的，
			//      所以应该在下载线程池启动第一个下载任务的时候检查.
			//
			//      如果在每次下载线程之前都检查的话，
			//      那么考虑一种情况，如果设置该目录的最大空间为10M，
			//      那么如果第一个线程下载20M，然后有加入了一个新的下载任务，
			//      那么这个时候如果在这个新的下载任务开始之前，优化下载目录的话，就会把你正在下载的这个20M的任务的文件给删除掉了
			//
			//      所以为了保险起见，仅在线程池加入执行第一个任务的时候进行下载目录优化，
			// ）
			
			// 到这里基本处理好下载前的一些参数判断了
			
			change2DownloadBeforeStart_FileLock();
			
		} catch (Throwable e) {
			if (DLog.isDownloadLog) {
				DLog.e(e);
			}
			change2DownloadFailed(new DownloadStatus(DownloadStatus.Code.ERROR_UNKNOWN, e));
		}
	}
	
	private void change2DestFileAlreadyExist() {
		
		if (DLog.isDownloadLog) {
			DLog.i("===DestFileAlreadyExist===");
		}
		
		// 如果目标存储文件是一个目录，返回失败
		if (mFileDownloadTask.getStoreFile().isDirectory()) {
			change2DownloadFailed(new DownloadStatus(DownloadStatus.Code.ERROR_LOCAL_FILE_TYPE));
			return;
		}
		
		// 接口为空，直接回调下载成功
		// 接口不为空的时候，通过下面接口检查存储文件是否有效
		if (mIFileAvailableChecker == null || mIFileAvailableChecker.isStoreFileAvailable(mFileDownloadTask)) {
			
			// 这里创建一个结果，用于同步调用download方法时回调的结果
			mDownloadStatus = new DownloadStatus(DownloadStatus.Code.SUCCESS);
			
			//　通知监听者们下载文件本来就存在
			if (mBaseDownloadNotifier != null) {
				mBaseDownloadNotifier.onNotifyFileAlreadyExist(mFileDownloadTask);
			}
			return;
		}
		
		//　如果不通过校验的话就删除已有的文件，然后重新开始下载
		if (FileUtils.delete(mFileDownloadTask.getStoreFile())) {
			change2DownloadInit();
		} else {
			change2DownloadFailed(new DownloadStatus(DownloadStatus.Code.ERROR_DEST_FILE_CANNOT_DEL));
		}
	}
	
	private void change2DownloadBeforeStart_FileLock() {
		// 如果支持多进程下载
		if (mIsSupportMultiProcessDownload) {
			
			// 检查下载缓存文件是否被其他进程使用中，是的话就不采用网络下载，
			// 而是监听文件的长度来实现共享下载进度
			if (DownloadUtils.isFileUsingByOtherProgress(mFileDownloadTask.getTempFile())) {
				if (DLog.isDownloadLog) {
					DLog.i("===DownloadBeforeStart_FileLock===");
				}
				
				// 通知一下当前任务可能被其他进程下载中，稍后将开始
				if (mBaseDownloadNotifier != null) {
					mBaseDownloadNotifier.onNotifyDownloadBeforeStart_FileLock(mFileDownloadTask);
				}
				
				// 这里坐等一段时间，然后在检查文件是否还在被其他进程使用
				// 主要是为了区分究竟是正的被其他进程使用，还是任务刚刚被停止
				if (DLog.isDownloadLog) {
					DLog.i("文件处于文件锁中 %d s 之后进行下载逻辑", DownloadUtils.LOCK_INTERVAL_ms / 1000);
				}
				try {
					Thread.sleep(DownloadUtils.LOCK_INTERVAL_ms);
				} catch (Throwable e) {
					if (DLog.isDownloadLog) {
						DLog.e(e);
					}
				}
				if (DLog.isDownloadLog) {
					DLog.i("已过 %d s", DownloadUtils.LOCK_INTERVAL_ms / 1000);
				}
				
				// 如果过了一段时间之后还是处于文件锁，就表示该文件被其他进程读写或者下载中，因此通过共享文件大小来模拟下载，而不在请求网络下载
				if (DownloadUtils.isFileUsingByOtherProgress(mFileDownloadTask.getTempFile())) {
					change2ObserveOtherProgressDownloading();
					return;
				}
				
				// 因为这里是已经阻塞了一段时间了，所以需要判断一下用户在这个时间段里面有没有停止下载，如果有的话，就没必要开始下载
				else {
					if (!mIsRunning) {
						change2DownloadStop();
						return;
					}
				}
			}
		}
		
		change2DownloadStart();
	}
	
	private void change2DownloadStart() {
		
		if (DLog.isDownloadLog) {
			DLog.i("===DownloadStart===");
		}
		
		// 通知监听者们下载开始了
		if (mBaseDownloadNotifier != null) {
			mBaseDownloadNotifier.onNotifyDownloadStart(mFileDownloadTask);
		}
		
		change2Downloading();
	}
	
	private void change2Downloading() {
		
		if (DLog.isDownloadLog) {
			DLog.i("===Downloading===");
		}
		
		// 如果有传入监听者的话
		// 因为download方法是一个同步方法的，所以为了获取下载进度，这里采用了在全局线程池中添加一个监听线程，定时访问下载逻辑处理对象的下载进度线程池中
		if (mBaseDownloadNotifier != null) {
			
			GlobalCacheExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					
					// 如果下载任务还在继续执行中
					while (mIsRunning) {
						
						try {
							// 因为这个监听线程是在下载开始之前就启动的
							// 所以需要判断一下任务是否启动了，任务启动之后在进行监听
							if (!mIDownloader.isRunning()) {
								continue;
							}
							
							// 每隔指定的间隔时间之后就进行一次下载进度广播通知
							if (System.currentTimeMillis() - mLastRecordNotifyTime_ms < mFileDownloadTask.getIntervalTime_ms()) {
								continue;
							}
							
							// 必须要按照下面的写法，因为get方法获取到的值是时刻在在变的，所以我们要先获取出来用另外一个变量存起来
							// 而且下面方法的调用顺序不能随便改动
							
							// 总长度
							long downloadFileFinalLength = mIDownloader.getDownloadFileFinalLength();
							
							// 已经完成的长度
							long completeLength = mIDownloader.getCompleteLength();
							
							// 已完成的百分比
							int percent = mIDownloader.getDownloadPercent();
							
							// 算出间隔时间内的增加字节数
							long increase = completeLength - mLastRecordCompleteLength;
							
							// 更新上一次已完成的长度
							mLastRecordCompleteLength = completeLength;
							
							// 更新上一次的回调时间
							mLastRecordNotifyTime_ms = System.currentTimeMillis();
							
							// 进行回调通知各个监听者之前，需要在判断一次是否还在运行中，有可能其实已经不在运行中了
							// 如果不在这次循环中判断多一次，那么只有等到下一次循环才知道不在运行中，那么这样子就会触发本次循环的下载中的回调，不合逻辑
							// 所以这里的判断很重要
							if (!mIsRunning) {
								break;
							}
							if (DLog.isDownloadLog) {
								DLog.i(
										"下载中:%d%% 速度: %d KB/s 已下载: %d KB",
										percent,
										increase / mFileDownloadTask.getIntervalTime_ms(),
										completeLength / 1024
								);
							}
							
							// 通知监听者下载进行中
							mBaseDownloadNotifier.onNotifyDownloadProgressUpdate(
									mFileDownloadTask,
									downloadFileFinalLength,
									completeLength,
									percent,
									increase,
									mFileDownloadTask.getIntervalTime_ms()
							);
						} catch (Throwable e) {
							if (DLog.isDownloadLog) {
								DLog.i(e);
							}
						}
					}
				}
			});
		}
		
		// 调用这里的download之后会阻塞
		DownloadStatus downloadStatus = mIDownloader.download();
		
		// 下载结束后（不管成功与否），还是提前一下置为不在运行中（虽然最后回调完毕之后会置为不在运行中，见本类的download方法）
		// 防止下载成功之后，还可能会继续回掉下载中的情况
		mIsRunning = false;
		
		// 重置参数
		mLastRecordCompleteLength = 0;
		mLastRecordNotifyTime_ms = 0;
		
		// 根据下载结果切换到相应的状态中
		if (DownloadStatus.Code.SUCCESS == downloadStatus.getDownloadStatusCode()) {
			change2DownloadSuccessed();
		} else if (DownloadStatus.Code.STOP == downloadStatus.getDownloadStatusCode()) {
			change2DownloadStop();
		} else if (downloadStatus.getDownloadStatusCode() >= 100 && downloadStatus.getDownloadStatusCode() <= 199) {
			// 下载失败的状态码在[100,199]之间
			change2DownloadFailed(downloadStatus);
		}
	}
	
	private void change2DownloadSuccessed() {
		
		if (DLog.isDownloadLog) {
			DLog.i("===DownloadSuccess===");
		}
		
		// 如果下载成功之后，检查最终文件有效性
		// 如果文件存在并且通过外部验证方法的话，就返回下载成功
		if (mFileDownloadTask.getStoreFile().exists() && mFileDownloadTask.getStoreFile().isFile()) {
			
			if (mIFileAvailableChecker == null || mIFileAvailableChecker.isStoreFileAvailable(mFileDownloadTask)) {
				
				// 这里创建一个结果，用于同步调用download方法时回调的结果
				mDownloadStatus = new DownloadStatus(DownloadStatus.Code.SUCCESS);
				
				// 通知监听者们下载成功
				if (mBaseDownloadNotifier != null) {
					mBaseDownloadNotifier.onNotifyDownloadSuccess(mFileDownloadTask);
				}
				return;
			}
		}
		change2DownloadFailed(new DownloadStatus(DownloadStatus.Code.ERROR_DEST_FILE_INVALID));
		
	}
	
	private void change2DownloadFailed(DownloadStatus DownloadStatus) {
		
		if (DLog.isDownloadLog) {
			DLog.i("===DownloadFailed===\n \n%s", DownloadStatus.toString());
		}
		
		// 这里后续可以实现异常上报
		// ....
		
		// 这里创建一个结果，用于同步调用download方法时回调的结果
		mDownloadStatus = DownloadStatus;
		
		// 通知监听者们下载失败
		if (mBaseDownloadNotifier != null) {
			mBaseDownloadNotifier.onNotifyDownloadFailed(mFileDownloadTask, DownloadStatus);
		}
	}
	
	private void change2DownloadStop() {
		
		if (DLog.isDownloadLog) {
			DLog.i("===DownloadStop===");
		}
		
		// 这里创建一个结果，用于同步调用download方法时回调的结果
		mDownloadStatus = new DownloadStatus(DownloadStatus.Code.STOP);
		
		//　通知监听者们下载停止
		if (mBaseDownloadNotifier != null) {
			try {
				mBaseDownloadNotifier.onNotifyDownloadStop(
						mFileDownloadTask,
						mIDownloader.getDownloadFileFinalLength(),
						mIDownloader.getCompleteLength(),
						mIDownloader.getDownloadPercent()
				);
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.i(e);
				}
			}
		}
	}
	
	/**
	 * 多进程下载监听原理:通过观察文件的状态来监控(共享)其下载进度。
	 */
	private void change2ObserveOtherProgressDownloading() {
		if (DLog.isDownloadLog) {
			DLog.i("===ObserveOtherProgressDownloading===");
		}
		
		int maxTimes = 3;
		int count = 0;
		
		// 总长度
		long downloadFileFinalLength = mIDownloader.getDownloadFileFinalLength();
		
		// 如果下载缓存文件一直被锁住，并且下载任务没有被停止，就需要跟进里面的下载状态
		while (DownloadUtils.isFileUsingByOtherProgress(mFileDownloadTask.getTempFile()) && mIsRunning) {
			
			try {
				// 每隔指定的间隔时间之后就进行一次下载进度广播通知
				if (System.currentTimeMillis() - mLastRecordNotifyTime_ms < mFileDownloadTask.getIntervalTime_ms()) {
					continue;
				}
				
				// 如果总长度小于0，就需要从网络获取文件总长度, 并缓存起来
				// 因为多进程下载的时候，监听进程是不知道要下载多长的长度，所以这里需要联网请求一下 ContentLength得出本次下载长度
				// 然后才好回到进度通知
				if (downloadFileFinalLength <= 0) {
					
					// 这里加计时器主要是为了不重复请求无限次ContentLength
					++count;
					if (count <= maxTimes) {
						downloadFileFinalLength =
								NetworkUtils.getContentLength(mApplicationContext, mFileDownloadTask.getRawDownloadUrl());
						if (DLog.isDownloadLog) {
							DLog.i("第%d次从网络中获取文件长度 = %d", count, downloadFileFinalLength);
						}
						if (downloadFileFinalLength <= 0) {
							continue;
						}
					} else {
						
						// 如果超过最大请求次数都还没有能获取到 ContentLength的话就切换到下载失败
						DownloadStatus downloadStatus =
								new DownloadStatus(DownloadStatus.Code.ERROR_REACH_MAX_GET_CONTENT_LENGTH_TIMES_INMULTPROCESSES);
						change2DownloadFailed(downloadStatus);
						return;
					}
				}
				
				if (mBaseDownloadNotifier != null && mFileDownloadTask.getTempFile().exists() &&
				    mFileDownloadTask.getTempFile().isFile()) {
					
					// 已经完成长度
					long completeLength = mFileDownloadTask.getTempFile().length();
					
					// 已经完成百分比
					int percent = (int) ((completeLength * 100) / downloadFileFinalLength);
					
					// 算出间隔时间内的增加字节数
					long increase = completeLength - mLastRecordCompleteLength;
					
					// 更新上一次已完成的长度
					mLastRecordCompleteLength = completeLength;
					
					// 更新上一次的回调时间
					mLastRecordNotifyTime_ms = System.currentTimeMillis();
					
					if (DLog.isDownloadLog) {
						DLog.i(
								"多进程下载中:%d%% 速度: %d KB/s 已下载: %d KB",
								percent,
								increase / mFileDownloadTask.getIntervalTime_ms(),
								completeLength / 1024
						);
					}
					
					// 通知监听者下载进行中
					mBaseDownloadNotifier.onNotifyDownloadProgressUpdate(
							mFileDownloadTask,
							downloadFileFinalLength,
							completeLength,
							percent,
							increase,
							mFileDownloadTask.getIntervalTime_ms()
					);
					
				}
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.e(e);
				}
			}
		}
		
		// 跳出while可能是文件锁结束了
		// 也可能是被用户主动暂停了
		if (!mIsRunning) {
			change2DownloadStop();
			return;
		}
		
		// 到这里表示已经结束文件锁了
		//
		// 因为其他进程如果采用同一套的下载方案的话，那么其他进程下载成功之后
		// 1. 缓存文件会不见了，因为会重命名为最终存储文件
		// 2. 多出了最终存储文件
		
		if (mFileDownloadTask.getStoreFile().exists()) {
			change2DownloadSuccessed();
			return;
		}
		
		// 到这里就表示其他进程可能下载没完成，因此这里需要继续自己请求网络进行下载
		// 因为可能会init之后到达downloading 而downloading里面又用到这个参数
		// 所以需要重置参数
		mLastRecordCompleteLength = 0;
		mLastRecordNotifyTime_ms = 0;
		
		// 如果文件不再被锁着，就再重新跑一边流程，确认下，因为一些状态是不能从其他进程上回馈的，比如下载失败之类的，所以需要重新跑一遍流程
		change2DownloadInit();
	}
}
