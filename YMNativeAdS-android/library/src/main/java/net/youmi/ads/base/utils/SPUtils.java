package net.youmi.ads.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zhitao
 * @since 2017-04-20 10:34
 */
public class SPUtils {
	
	private final static String PREFERENCE_NAME = "cp";
	
	public static boolean putString(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	public static String getString(Context context, String key) {
		return getString(context, key, null);
	}
	
	public static String getString(Context context, String key, String defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_MULTI_PROCESS);
		return settings.getString(key, defaultValue);
	}
}
