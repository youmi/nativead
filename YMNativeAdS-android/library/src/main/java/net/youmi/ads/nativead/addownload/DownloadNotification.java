package net.youmi.ads.nativead.addownload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.utils.IntentUtils;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-17 11:22
 */
class DownloadNotification {
	
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
			mDownloadedPi =
					PendingIntent.getActivity(mApplicationContext, mNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
	
	private void showNotification(Context context, int id, String tickerText, String title, String contentText, PendingIntent pi,
			int smallIconResId, long startTime) {
		try {
			if (mNotificationManager == null) {
				mNotificationManager = (NotificationManager) mApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
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
