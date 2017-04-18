package net.youmi.ads.nativead.addownload;

import android.content.Context;

import net.youmi.ads.base.download.base.DownloadStatus;
import net.youmi.ads.base.download.listener.OnDownloadListener;
import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.download.model.IFileDownloadTask;
import net.youmi.ads.base.utils.NetworkUtils;
import net.youmi.ads.base.utils.SdCardUtils;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-17 11:23
 */
class OnDownloadNotificationPublisher implements OnDownloadListener {
	
	private HashMap<FileDownloadTask, DownloadNotification> mFileDownloadTaskDownloadNotificationHashMap = new HashMap<>();
	
	private Context mApplicationContext;
	
	OnDownloadNotificationPublisher(Context context) {
		mApplicationContext = context.getApplicationContext();
	}
	
	private DownloadNotification getNotification(FileDownloadTask fileDownloadTask) {
		if (fileDownloadTask == null) {
			return null;
		}
		
		IFileDownloadTask iFileDownloadTask = fileDownloadTask.getIFileDownloadTask();
		if (iFileDownloadTask == null || !(iFileDownloadTask instanceof YoumiNativeAdModel)) {
			return null;
		}
		YoumiNativeAdModel adModel = (YoumiNativeAdModel) iFileDownloadTask;
		DownloadNotification notification = mFileDownloadTaskDownloadNotificationHashMap.get(fileDownloadTask);
		if (notification != null) {
			return notification;
		} else {
			notification = new DownloadNotification(
					mApplicationContext,
					fileDownloadTask.hashCode(),
					String.format(Locale.getDefault(), "正在下载「%s」", adModel.getAdName()),
					String.format(Locale.getDefault(), "下载「%s」成功", adModel.getAdName()),
					String.format(Locale.getDefault(), "下载「%s」失败", adModel.getAdName()),
					String.format(Locale.getDefault(), "已停止下载「%s」", adModel.getAdName())
			);
			mFileDownloadTaskDownloadNotificationHashMap.put(fileDownloadTask, notification);
			return notification;
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
		DownloadNotification notification = getNotification(fileDownloadTask);
		if (notification == null) {
			return;
		}
		notification.notifyDownloadStart();
	}
	
	/**
	 * 下载进度回调：之类需要实现具体业务逻辑
	 *
	 * @param fileDownloadTask 下载任务模型
	 * @param totalLength      本次下载文件的总长度
	 * @param completeLength   已经完成的长度
	 * @param percent          当前完成百分比
	 * @param speedBytes       当前下载速度:每intervalTime_ms毫秒下载的长度(单位:bytes)
	 * @param intervalTime_ms  当前下载速度时间单位:每intervalTime_ms毫秒回回调一次本方法(单位:bytes)
	 */
	@Override
	public void onDownloadProgressUpdate(FileDownloadTask fileDownloadTask, long totalLength, long completeLength, int percent,
			long speedBytes, long intervalTime_ms) {
		DownloadNotification notification = getNotification(fileDownloadTask);
		if (notification == null) {
			return;
		}
		notification.notifyDownloading(percent, (speedBytes * 1000) / intervalTime_ms);
	}
	
	/**
	 * 下载成功
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	@Override
	public void onDownloadSuccess(FileDownloadTask fileDownloadTask) {
		DownloadNotification notification = getNotification(fileDownloadTask);
		if (notification == null || fileDownloadTask.getStoreFile() == null || !fileDownloadTask.getStoreFile().exists()) {
			return;
		}
		
		// 通知栏:下载成功
		notification.notifyDownloadSuccess(fileDownloadTask.getStoreFile().getPath());
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
		DownloadNotification notification = getNotification(fileDownloadTask);
		if (notification == null) {
			return;
		}
		
		// 通知栏:下载失败
		String tips;
		if (NetworkUtils.isNetworkAvailable(mApplicationContext)) {
			// 检查存储空间是否足够
			long contentLength = fileDownloadTask.getTotalLength();
			if (contentLength <= 0) {
				contentLength = 10 * 1024 * 1024;// 是否不足10MB
			}
			
			if (SdCardUtils.isSdCardHaveEnoughSpace(mApplicationContext, contentLength)) {
				// sd 卡可用，而且够用，自然会选择sdcard,那么失败的原因应该跟网络有关
				tips = "下载失败，无法连接服务器";
			} else {
				// sd 卡不可用
				// 这里就直接提示存储空间不足吧
				tips = "下载失败，请检查存储空间是否足够";
			}
			
		} else {
			// 判断为网络异常
			tips = "下载失败，请检查网络设置";
		}
		tips += " code " + downloadStatus.getDownloadStatusCode();
		notification.notifyDownloadFailed(tips);
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
		DownloadNotification notification = getNotification(fileDownloadTask);
		if (notification == null) {
			return;
		}
		
		notification.onDownloadStop();
	}
}
