package net.youmi.ads.nativead.addownload;

import net.youmi.ads.base.download.base.DownloadStatus;
import net.youmi.ads.base.download.listener.OnDownloadListener;
import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.download.model.IFileDownloadTask;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.template.TListenerManager;
import net.youmi.ads.base.utils.UIHandler;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

import java.util.List;

/**
 * @author zhitao
 * @since 2017-04-17 15:30
 */
class OnYoumiNativeAdDownloadListenerTransform extends TListenerManager<OnYoumiNativeAdDownloadListener>
		implements OnDownloadListener {
	
	private YoumiNativeAdModel getYoumiNativeAdModel(FileDownloadTask fileDownloadTask) {
		try {
			if (fileDownloadTask == null) {
				return null;
			}
			IFileDownloadTask iFileDownloadTask = fileDownloadTask.getIFileDownloadTask();
			if (iFileDownloadTask == null || !(iFileDownloadTask instanceof YoumiNativeAdModel)) {
				return null;
			}
			return (YoumiNativeAdModel) iFileDownloadTask;
		} catch (Throwable e) {
			DLog.e(e);
			return null;
		}
	}
	
	/**
	 * 如果支持多进程下载，那么就会回调这里
	 * <p/>
	 * 如果刚刚调用停止下载的代码之后，立即点击再次重新下载的话，可能就会到这里）
	 * 添加下载开始之前，文件处于文件锁的回调
	 * <p/>
	 * 通知任务当前处于文件锁
	 * <p/>
	 * 一般需要重写的场合为SDK:
	 * <p/>
	 * 因为sdk一般是很多个app同时使用的，所以下载的文件基本是公用的，
	 * 这个时候，在下载之前就需要检查下下载文件是否已经被其他进程读取中，
	 * 如果是的话，这里要通知一下
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	@Override
	public void onDownloadBeforeStartWithFileLock(FileDownloadTask fileDownloadTask) {
		
	}
	
	/**
	 * 下载开始
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	@Override
	public void onDownloadStart(FileDownloadTask fileDownloadTask) {
		final YoumiNativeAdModel adModel = getYoumiNativeAdModel(fileDownloadTask);
		if (adModel == null) {
			return;
		}
		
		final List<OnYoumiNativeAdDownloadListener> list = getListeners();
		if (list == null || list.isEmpty()) {
			return;
		}
		
		for (int i = 0; i < list.size(); ++i) {
			final OnYoumiNativeAdDownloadListener listener = list.get(i);
			if (listener == null) {
				continue;
			}
			UIHandler.runInUIThread(new Runnable() {
				@Override
				public void run() {
					listener.onDownloadStart(adModel);
				}
			});
		}
	}
	
	/**
	 * 下载进度回调：之类需要实现具体业务逻辑
	 *
	 * @param fileDownloadTask 下载任务模型
	 * @param totalLength      本次下载文件的总长度
	 * @param completeLength   已经完成的长度
	 * @param percent          当前完成百分比
	 * @param speedBytes       当前下载速度:每 intervalTimeMS 毫秒下载的长度(单位:bytes)
	 * @param intervalTimeMS   当前下载速度时间单位:每 intervalTimeMS 毫秒回回调一次本方法(单位:bytes)
	 */
	@Override
	public void onDownloadProgressUpdate(FileDownloadTask fileDownloadTask, final long totalLength, final long completeLength,
			final int percent, final long speedBytes, final long intervalTimeMS) {
		final YoumiNativeAdModel adModel = getYoumiNativeAdModel(fileDownloadTask);
		if (adModel == null) {
			return;
		}
		
		final List<OnYoumiNativeAdDownloadListener> list = getListeners();
		if (list == null || list.isEmpty()) {
			return;
		}
		
		for (int i = 0; i < list.size(); ++i) {
			final OnYoumiNativeAdDownloadListener listener = list.get(i);
			if (listener == null) {
				continue;
			}
			UIHandler.runInUIThread(new Runnable() {
				@Override
				public void run() {
					listener.onDownloadProgressUpdate(adModel,
							totalLength,
							completeLength,
							percent,
							(speedBytes * 1000) / intervalTimeMS
					);
				}
			});
		}
	}
	
	/**
	 * 下载成功
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	@Override
	public void onDownloadSuccess(FileDownloadTask fileDownloadTask) {
		final YoumiNativeAdModel adModel = getYoumiNativeAdModel(fileDownloadTask);
		if (adModel == null) {
			return;
		}
		
		final List<OnYoumiNativeAdDownloadListener> list = getListeners();
		if (list == null || list.isEmpty()) {
			return;
		}
		
		for (int i = 0; i < list.size(); ++i) {
			final OnYoumiNativeAdDownloadListener listener = list.get(i);
			if (listener == null) {
				continue;
			}
			UIHandler.runInUIThread(new Runnable() {
				@Override
				public void run() {
					listener.onDownloadSuccess(adModel);
				}
			});
		}
	}
	
	/**
	 * 下载成功，文件本来就存在于本地
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	@Override
	public void onFileAlreadyExist(FileDownloadTask fileDownloadTask) {
		onDownloadSuccess(fileDownloadTask);
	}
	
	/**
	 * 下载失败
	 *
	 * @param fileDownloadTask 下载任务模型
	 * @param downloadStatus   下载任务失败数据模型
	 */
	@Override
	public void onDownloadFailed(FileDownloadTask fileDownloadTask, DownloadStatus downloadStatus) {
		final YoumiNativeAdModel adModel = getYoumiNativeAdModel(fileDownloadTask);
		if (adModel == null) {
			return;
		}
		
		final List<OnYoumiNativeAdDownloadListener> list = getListeners();
		if (list == null || list.isEmpty()) {
			return;
		}
		
		for (int i = 0; i < list.size(); ++i) {
			final OnYoumiNativeAdDownloadListener listener = list.get(i);
			if (listener == null) {
				continue;
			}
			UIHandler.runInUIThread(new Runnable() {
				@Override
				public void run() {
					listener.onDownloadFailed(adModel);
				}
			});
		}
	}
	
	/**
	 * 下载暂停
	 *
	 * @param fileDownloadTask 下载任务模型
	 * @param totalLength      本次下载的总长度
	 * @param completeLength   已下载的长度
	 * @param percent          下载停止时，已经完成的百分比
	 */
	@Override
	public void onDownloadStop(FileDownloadTask fileDownloadTask, long totalLength, long completeLength, int percent) {
		final YoumiNativeAdModel adModel = getYoumiNativeAdModel(fileDownloadTask);
		if (adModel == null) {
			return;
		}
		
		final List<OnYoumiNativeAdDownloadListener> list = getListeners();
		if (list == null || list.isEmpty()) {
			return;
		}
		
		for (int i = 0; i < list.size(); ++i) {
			final OnYoumiNativeAdDownloadListener listener = list.get(i);
			if (listener == null) {
				continue;
			}
			UIHandler.runInUIThread(new Runnable() {
				@Override
				public void run() {
					listener.onDownloadStop(adModel);
				}
			});
		}
	}
}
