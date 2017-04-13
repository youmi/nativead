package net.youmi.ads.nativead;

import android.content.Context;

import net.youmi.ads.base.log.DLog;

/**
 * @author zhitao
 * @since 2017-04-13 10:52
 */
public class YoumiNativeAdManager {
	
	private static volatile YoumiNativeAdManager sInstance;
	
	private Context mApplicationContext;
	
	private YoumiNativeAdManager(Context context) {
		mApplicationContext = context.getApplicationContext();
	}
	
	public static YoumiNativeAdManager getInstance(Context context) {
		if (sInstance == null) {
			synchronized (YoumiNativeAdManager.class) {
				if (sInstance == null) {
					sInstance = new YoumiNativeAdManager(context);
				}
			}
		}
		return sInstance;
	}
	
	/**
	 * 是否显示调试log
	 *
	 * @param isShowDebugLog true or false
	 */
	public static void setDebug(boolean isShowDebugLog) {
		DLog.setIsDebug(isShowDebugLog);
	}
}
