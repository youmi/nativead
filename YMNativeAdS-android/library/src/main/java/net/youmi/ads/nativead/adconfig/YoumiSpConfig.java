package net.youmi.ads.nativead.adconfig;

import android.content.Context;
import android.content.SharedPreferences;

import net.youmi.ads.nativead.BuildConfig;

/**
 * @author zhitao
 * @since 2017-05-31 17:08
 */
public class YoumiSpConfig {
	
	private final static String KEY_APPID = "appid";
	
	public static String getAppId(Context context) {
		SharedPreferences sharedPreferences =
				context.getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
		return sharedPreferences.getString(KEY_APPID, null);
	}
	
	static void setAppId(Context context, String appId) {
		SharedPreferences sharedPreferences =
				context.getApplicationContext().getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(KEY_APPID, appId);
		editor.apply();
	}
	
}
