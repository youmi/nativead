package net.youmi.ads.base.download.listener;

import android.util.SparseIntArray;

import net.youmi.ads.base.download.base.DownloadStatus;
import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.template.TListenerManager;

import java.util.List;

/**
 * @author zhitao
 * @since 2017-04-14 16:31
 */
public class DownloadPublisher extends TListenerManager<OnDownloadListener> {
	
	/**
	 * 利用一个稀疏数组记录每个下载任务当前的下载状态，方便一些特殊场合即时获取任务的状态
	 */
	private SparseIntArray mSparseIntArray = new SparseIntArray();
	
	/**
	 * @param fileDownloadTask 下载任务对象
	 *
	 * @return <ul>
	 * <li>-1:没有这个任务的下载</li>
	 * <li>{@link OnDownloadListener} 中的下载状态</li>
	 * </ul>
	 */
	public int getDownloadStats(FileDownloadTask fileDownloadTask) {
		if (fileDownloadTask == null) {
			return -1;
		}
		return mSparseIntArray.get(fileDownloadTask.hashCode(), -1);
	}
	
	/**
	 * 通知任务当前处于文件锁
	 * <p/>
	 * 一般需要重写的场合为SDK下载，APP应用类不需要重写这个方法:
	 * <p/>
	 * 因为sdk一般是很多个app同时使用的，所以下载的文件基本是公用的，
	 * 这个时候，在下载之前就需要检查下下载文件是否已经被其他进程读取中，
	 * 如果是的话，这里要通知一下
	 *
	 * @param fileDownloadTask
	 */
	public void onNotifyDownloadBeforeStart_FileLock(FileDownloadTask fileDownloadTask) {
		
		try {
			mSparseIntArray.put(fileDownloadTask.hashCode(), OnDownloadListener.DOWNLOAD_BEFORE_START_FILE_LOCK);
			
			final List<OnDownloadListener> list = getListeners();
			if (list != null && !list.isEmpty()) {
				if (DLog.isDownloadLog) {
					DLog.i("下载前，文件处于文件锁中：当前共有[%d]个监听者要处理", list.size());
				}
				for (int i = 0; i < list.size(); ++i) {
					try {
						list.get(i).onDownloadBeforeStartWithFileLock(fileDownloadTask);
					} catch (Throwable e) {
						DLog.e(e);
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
	
	/**
	 * 通知下载开始：子类需要实现具体业务逻辑
	 *
	 * @param fileDownloadTask 下载任务对象
	 */
	public void onNotifyDownloadStart(FileDownloadTask fileDownloadTask) {
		try {
			mSparseIntArray.put(fileDownloadTask.hashCode(), OnDownloadListener.DOWNLOAD_START);
			
			final List<OnDownloadListener> list = getListeners();
			if (list != null && !list.isEmpty()) {
				if (DLog.isDownloadLog) {
					DLog.i("下载开始：当前共有[%d]个监听者要处理", list.size());
				}
				for (int i = 0; i < list.size(); ++i) {
					try {
						list.get(i).onDownloadStart(fileDownloadTask);
					} catch (Throwable e) {
						DLog.e(e);
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
	
	/**
	 * 通知下载进度回调：之类需要实现具体业务逻辑
	 *
	 * @param fileDownloadTask 下载任务对象
	 * @param totalLength      本次下载文件的总长度
	 * @param completeLength   已下载的长度
	 * @param percent          当前完成百分比
	 * @param speedBytes       当前下载速度:每intervalTime_ms毫秒下载的长度(单位:bytes)
	 * @param intervalTime_ms  当前下载速度时间单位:每intervalTime_ms毫秒回回调一次本方法(单位:bytes)
	 */
	public void onNotifyDownloadProgressUpdate(FileDownloadTask fileDownloadTask, long totalLength, long completeLength,
			int percent, long speedBytes, long intervalTime_ms) {
		try {
			mSparseIntArray.put(fileDownloadTask.hashCode(), OnDownloadListener.DOWNLOAD_ING);
			
			final List<OnDownloadListener> list = getListeners();
			if (list != null && !list.isEmpty()) {
				if (DLog.isDownloadLog) {
					DLog.i("下载中：当前共有[%d]个监听者要处理", list.size());
				}
				for (int i = 0; i < list.size(); ++i) {
					try {
						list.get(i)
						    .onDownloadProgressUpdate(fileDownloadTask,
								    totalLength,
								    completeLength,
								    percent,
								    speedBytes,
								    intervalTime_ms
						    );
					} catch (Throwable e) {
						DLog.e(e);
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
	
	/**
	 * 通知下载成功：子类需要实现具体业务逻辑
	 *
	 * @param fileDownloadTask 下载任务对象
	 */
	public void onNotifyDownloadSuccess(FileDownloadTask fileDownloadTask) {
		try {
			mSparseIntArray.put(fileDownloadTask.hashCode(), OnDownloadListener.DOWNLOAD_SUCCESS);
			
			final List<OnDownloadListener> list = getListeners();
			if (list != null && !list.isEmpty()) {
				if (DLog.isDownloadLog) {
					DLog.i("下载成功：当前共有[%d]个监听者要处理", list.size());
				}
				for (int i = 0; i < list.size(); ++i) {
					try {
						list.get(i).onDownloadSuccess(fileDownloadTask);
					} catch (Throwable e) {
						DLog.e(e);
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
	
	/**
	 * 通知下载成功（文件本来已经存在）：子类需要实现具体业务逻辑
	 *
	 * @param fileDownloadTask 下载任务对象
	 */
	public void onNotifyFileAlreadyExist(FileDownloadTask fileDownloadTask) {
		try {
			mSparseIntArray.put(fileDownloadTask.hashCode(), OnDownloadListener.DOWNLOAD_SUCCESS_FILE_ALREADY_EXIST);
			
			final List<OnDownloadListener> list = getListeners();
			if (list != null && !list.isEmpty()) {
				if (DLog.isDownloadLog) {
					DLog.i("下载文件已经存在于本地中：当前共有[%d]个监听者要处理", list.size());
				}
				for (int i = 0; i < list.size(); ++i) {
					try {
						list.get(i).onFileAlreadyExist(fileDownloadTask);
					} catch (Throwable e) {
						DLog.e(e);
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
	
	/**
	 * 通知下载失败：子类需要实现具体业务逻辑
	 *
	 * @param fileDownloadTask 下载任务对象
	 * @param downloadStatus   下载任务失败数据模型
	 */
	public void onNotifyDownloadFailed(FileDownloadTask fileDownloadTask, DownloadStatus downloadStatus) {
		try {
			mSparseIntArray.put(fileDownloadTask.hashCode(), OnDownloadListener.DOWNLOAD_FAILED);
			
			final List<OnDownloadListener> list = getListeners();
			if (list != null && !list.isEmpty()) {
				if (DLog.isDownloadLog) {
					DLog.i("下载失败：当前共有[%d]个监听者要处理", list.size());
				}
				for (int i = 0; i < list.size(); ++i) {
					try {
						list.get(i).onDownloadFailed(fileDownloadTask, downloadStatus);
					} catch (Throwable e) {
						DLog.e(e);
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
	
	/**
	 * 通知下载停止：子类需要实现具体业务逻辑
	 *
	 * @param fileDownloadTask 下载任务对象
	 * @param totalLength      本次下载的总长度
	 * @param completeLength   已下载的长度
	 * @param percent          下载停止时，已经完成的百分比
	 */
	public void onNotifyDownloadStop(FileDownloadTask fileDownloadTask, long totalLength, long completeLength, int percent) {
		try {
			mSparseIntArray.put(fileDownloadTask.hashCode(), OnDownloadListener.DOWNLOAD_STOP);
			
			final List<OnDownloadListener> list = getListeners();
			if (list != null && !list.isEmpty()) {
				if (DLog.isDownloadLog) {
					DLog.i("下载暂停：当前共有[%d]个监听者要处理", list.size());
				}
				for (int i = 0; i < list.size(); ++i) {
					try {
						list.get(i).onDownloadStop(fileDownloadTask, totalLength, completeLength, percent);
					} catch (Throwable e) {
						DLog.e(e);
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
}
