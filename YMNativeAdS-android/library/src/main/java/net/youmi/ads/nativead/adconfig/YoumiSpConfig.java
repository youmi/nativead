package net.youmi.ads.nativead.adconfig;

import android.content.Context;

import net.youmi.ads.base.utils.SPUtils;

/**
 * @author zhitao
 * @since 2017-05-31 17:08
 */
public class YoumiSpConfig {
	
	private final static String KEY_APPID = "appid";
	
	public static String getAppId(Context context) {
		return SPUtils.getString(context, KEY_APPID);
	}
	
	static void setAppId(Context context, String appId) {
		SPUtils.putString(context, KEY_APPID, appId);
	}
	
}
