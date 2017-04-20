package net.youmi.ads.nativead.addownload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import net.youmi.ads.base.download.base.DownloadStatus;
import net.youmi.ads.base.download.listener.OnDownloadListener;
import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.hash.MD5;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.utils.IntentUtils;
import net.youmi.ads.base.utils.JSONUtils;
import net.youmi.ads.base.utils.NetworkUtils;
import net.youmi.ads.base.utils.SPUtils;
import net.youmi.ads.base.utils.SdCardUtils;
import net.youmi.ads.nativead.adinstall.AdInstallUtils;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-17 11:23
 */
class DefaultDownloadListener implements OnDownloadListener {
	
	private HashMap<FileDownloadTask, DownloadNotification> mFileDownloadTaskDownloadNotificationHashMap = new HashMap<>();
	
	private Context mApplicationContext;
	
	DefaultDownloadListener(Context context) {
		mApplicationContext = context.getApplicationContext();
	}
	
	private DownloadNotification getNotification(FileDownloadTask fileDownloadTask) {
		try {
			if (fileDownloadTask == null || fileDownloadTask.getIFileDownloadTaskSparseArray() == null) {
				return null;
			}
			
			YoumiNativeAdModel adModel = (YoumiNativeAdModel) fileDownloadTask.getIFileDownloadTaskSparseArray()
			                                                                  .get(YoumiNativeAdModel.class.hashCode());
			if (adModel == null) {
				return null;
			}
			
			DownloadNotification notification = mFileDownloadTaskDownloadNotificationHashMap.get(fileDownloadTask);
			if (notification == null) {
				notification = new DownloadNotification(mApplicationContext,
						fileDownloadTask.hashCode(),
						String.format(Locale.getDefault(), "正在下载「%s」", adModel.getAdName()),
						String.format(Locale.getDefault(), "下载「%s」成功", adModel.getAdName()),
						String.format(Locale.getDefault(), "下载「%s」失败", adModel.getAdName()),
						String.format(Locale.getDefault(), "已停止下载「%s」", adModel.getAdName())
				);
				mFileDownloadTaskDownloadNotificationHashMap.put(fileDownloadTask, notification);
			}
			return notification;
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
	private DownloadTaskConfig getDownloadTaskConfig(FileDownloadTask fileDownloadTask) {
		DownloadTaskConfig downloadTaskConfig = null;
		try {
			if (fileDownloadTask != null && fileDownloadTask.getIFileDownloadTaskSparseArray() != null) {
				downloadTaskConfig = (DownloadTaskConfig) fileDownloadTask.getIFileDownloadTaskSparseArray()
				                                                          .get(DownloadTaskConfig.class.hashCode());
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		if (downloadTaskConfig == null) {
			downloadTaskConfig = new DownloadTaskConfig();
		}
		return downloadTaskConfig;
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
		
		DownloadTaskConfig downloadTaskConfig = getDownloadTaskConfig(fileDownloadTask);
		
		// 发送下载进度到通知栏
		if (downloadTaskConfig.isShowDefaultNotification()) {
			DownloadNotification notification = getNotification(fileDownloadTask);
			if (notification == null) {
				return;
			}
			notification.notifyDownloadStart();
		}
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
		
		DownloadTaskConfig downloadTaskConfig = getDownloadTaskConfig(fileDownloadTask);
		
		// 发送下载进度到通知栏
		if (downloadTaskConfig.isShowDefaultNotification()) {
			DownloadNotification notification = getNotification(fileDownloadTask);
			if (notification == null) {
				return;
			}
			notification.notifyDownloading(percent, (speedBytes * 1000) / intervalTime_ms);
		}
		
	}
	
	/**
	 * 下载成功
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	@Override
	public void onDownloadSuccess(FileDownloadTask fileDownloadTask) {
		
		DownloadTaskConfig downloadTaskConfig = getDownloadTaskConfig(fileDownloadTask);
		
		// 只有在下载成功的时候不自动发起安装，才发送通知栏通知
		// 发送下载进度到通知栏
		if (!downloadTaskConfig.isNeed2StartInstallAfterDownloadSuccess()) {
			if (downloadTaskConfig.isShowDefaultNotification()) {
				DownloadNotification notification = getNotification(fileDownloadTask);
				if (notification == null || fileDownloadTask.getStoreFile() == null ||
				    !fileDownloadTask.getStoreFile().exists()) {
					return;
				}
				notification.notifyDownloadSuccess(fileDownloadTask.getStoreFile().getPath());
			}
		}
		
		// 处理下载成功后打开apk安装的逻辑
		if (downloadTaskConfig.isNeed2StartInstallAfterDownloadSuccess()) {
			if (fileDownloadTask.getStoreFile() != null && fileDownloadTask.getStoreFile().exists()) {
				
				YoumiNativeAdModel adModel = null;
				if (fileDownloadTask.getIFileDownloadTaskSparseArray() != null) {
					adModel = (YoumiNativeAdModel) fileDownloadTask.getIFileDownloadTaskSparseArray()
					                                               .get(YoumiNativeAdModel.class.hashCode());
				}
				if (adModel != null) {
					Intent intent = IntentUtils.getIntentForInstallApk(mApplicationContext, fileDownloadTask.getStoreFile());
					if (intent != null) {
						
						// 打开安装之前，标记是来自我们的安装，令我们的安装广播器只处理来自我们发起安装的应用的信息
						JSONObject jo = new JSONObject();
						JSONUtils.putObject(jo, "a", adModel.getUri());
						JSONUtils.putObject(jo, "b", System.currentTimeMillis());
						JSONUtils.putObject(jo, "c", downloadTaskConfig.isNeed2OpenAppAfterInstalled());
						JSONUtils.putObject(jo, "d", downloadTaskConfig.isNeed2DeleteApkAfterInstalled());
						JSONUtils.putObject(jo, "e", fileDownloadTask.getStoreFile().getAbsolutePath());
						if (SPUtils.putString(mApplicationContext,
								MD5.md5(adModel.getAppModel().getPackageName()),
								jo.toString()
						)) {
							
							// 注册监听器后在启动安装
							AdInstallUtils.registerApkInstallReceiver(mApplicationContext);
							
							mApplicationContext.startActivity(intent);
						}
					}
				}
			}
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
		DownloadTaskConfig downloadTaskConfig = getDownloadTaskConfig(fileDownloadTask);
		
		// 发送下载进度到通知栏
		if (downloadTaskConfig.isShowDefaultNotification()) {
			
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
		DownloadTaskConfig downloadTaskConfig = getDownloadTaskConfig(fileDownloadTask);
		
		// 发送下载进度到通知栏
		if (downloadTaskConfig.isShowDefaultNotification()) {
			DownloadNotification notification = getNotification(fileDownloadTask);
			if (notification == null) {
				return;
			}
			
			notification.onDownloadStop();
		}
	}
	
	/**
	 * @author zhitao
	 * @since 2017-04-17 11:22
	 */
	private static class DownloadNotification {
		
		private Context mApplicationContext;
		
		private int mNotificationId;
		
		private PendingIntent mDownloadingPendingIntent;
		
		private PendingIntent mDownloadedPi;
		
		private NotificationManager mNotificationManager;
		
		private long mDownloadStartTime;
		
		private String mDownloadingTitle;
		
		private String mDownloadSuccessTitle;
		
		private String mDownloadFailedTitle;
		
		private String mDownloadStopTitle;
		
		/**
		 * @param context              上下文
		 * @param notificationID       通知栏id
		 * @param downloadingTitle     下载中标题
		 * @param downloadSuccessTitle 下载成功标题
		 * @param downloadFailedTitle  下载失败标题
		 * @param downloadStopTitle    下载停止标题
		 */
		DownloadNotification(Context context, int notificationID, String downloadingTitle, String downloadSuccessTitle,
				String downloadFailedTitle, String downloadStopTitle) {
			mApplicationContext = context.getApplicationContext();
			mNotificationId = notificationID;
			mDownloadingTitle = downloadingTitle;
			mDownloadSuccessTitle = downloadSuccessTitle;
			mDownloadFailedTitle = downloadFailedTitle;
			mDownloadStopTitle = downloadStopTitle;
		}
		
		private boolean isDownloadingPendingIntentValid() {
			try {
				if (mDownloadingPendingIntent == null) {
					Intent intent = new Intent();
					mDownloadingPendingIntent = PendingIntent.getActivity(mApplicationContext,
							mNotificationId,
							intent,
							PendingIntent.FLAG_UPDATE_CURRENT
					);
				}
				return mDownloadingPendingIntent != null;
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.e(e);
				}
			}
			return false;
		}
		
		private boolean isDownloadSuccessPendingIntentValid(String filePath) {
			try {
				if (filePath == null || filePath.trim().length() == 0) {
					return false;
				}
				if (TextUtils.isEmpty(filePath)) {
					return false;
				}
				Intent intent = IntentUtils.getIntentForInstallApk(mApplicationContext, filePath);
				mDownloadedPi = PendingIntent.getActivity(mApplicationContext,
						mNotificationId,
						intent,
						PendingIntent.FLAG_UPDATE_CURRENT
				);
				return mDownloadedPi != null;
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.e(e);
				}
			}
			return false;
		}
		
		void notifyDownloadStart() {
			try {
				if (!isDownloadingPendingIntentValid()) {
					return;
				}
				mDownloadStartTime = System.currentTimeMillis();
				showNotification(mApplicationContext,
						mNotificationId,
						null,
						mDownloadingTitle,
						"连接服务器中",
						mDownloadingPendingIntent,
						android.R.drawable.stat_sys_download,
						mDownloadStartTime
				);
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.e(e);
				}
			}
		}
		
		void notifyDownloading(int percent, long speed) {
			try {
				if (!isDownloadingPendingIntentValid()) {
					return;
				}
				
				showNotification(mApplicationContext,
						mNotificationId,
						null,
						mDownloadingTitle,
						String.format(Locale.getDefault(), "已完成:%d%%. 下载速度:%d KB/s", percent, (speed / 1024)),
						mDownloadingPendingIntent,
						android.R.drawable.stat_sys_download,
						mDownloadStartTime
				);
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.e(e);
				}
			}
		}
		
		void notifyDownloadSuccess(String filePath) {
			try {
				if (!isDownloadSuccessPendingIntentValid(filePath)) {
					return;
				}
				showNotification(mApplicationContext,
						mNotificationId,
						null,
						mDownloadSuccessTitle,
						"点击安装",
						mDownloadedPi,
						android.R.drawable.stat_sys_download_done,
						System.currentTimeMillis()
				);
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.e(e);
				}
			}
		}
		
		void notifyDownloadFailed(String errorContent) {
			try {
				if (!isDownloadingPendingIntentValid()) {
					return;
				}
				showNotification(mApplicationContext,
						mNotificationId,
						null,
						mDownloadFailedTitle,
						errorContent,
						mDownloadingPendingIntent,
						android.R.drawable.stat_sys_download_done,
						System.currentTimeMillis()
				);
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.e(e);
				}
			}
		}
		
		void onDownloadStop() {
			try {
				if (!isDownloadingPendingIntentValid()) {
					return;
				}
				showNotification(mApplicationContext,
						mNotificationId,
						null,
						mDownloadStopTitle,
						"下载任务已停止",
						mDownloadingPendingIntent,
						android.R.drawable.stat_sys_download_done,
						System.currentTimeMillis()
				);
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.e(e);
				}
			}
		}
		
		private void showNotification(Context context, int id, String tickerText, String title, String contentText,
				PendingIntent pi, int smallIconResId, long startTime) {
			try {
				if (mNotificationManager == null) {
					mNotificationManager =
							(NotificationManager) mApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
				}
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					Notification.Builder builder = new Notification.Builder(mApplicationContext);
					builder.setAutoCancel(true);
					builder.setOnlyAlertOnce(true);
					if (!TextUtils.isEmpty(tickerText)) {
						builder.setTicker(tickerText);
					}
					builder.setWhen(startTime);
					builder.setSmallIcon(smallIconResId);
					builder.setContentTitle(title);
					builder.setContentText(contentText);
					builder.setContentIntent(pi);
					mNotificationManager.notify(id, builder.build());
				} else {
					Context applicationContext = context.getApplicationContext();
					Notification notification = new Notification();
					notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
					if (!TextUtils.isEmpty(tickerText)) {
						notification.tickerText = tickerText;
					}
					notification.when = startTime;
					notification.icon = smallIconResId;
					
					// 在android 6.0 sdk上已经没有这个方法，因此如果需要兼容11以下，需要采用反射实现这个方法
					// notification.setLatestEventInfo(applicationContext, title, contentText, pi);
					
					// 反射实现
					Method method = Class.forName(Notification.class.getName())
					                     .getMethod("setLatestEventInfo",
							                     Context.class,
							                     CharSequence.class,
							                     CharSequence.class,
							                     PendingIntent.class
					                     );
					method.invoke(notification, applicationContext, title, contentText, pi);
					mNotificationManager.notify(id, notification);
				}
			} catch (Throwable e) {
				if (DLog.isDownloadLog) {
					DLog.e(e);
				}
			}
		}
		
	}
}
