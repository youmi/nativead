package net.youmi.ads.base.log;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-13 10:50
 */
public class DLog {
	
	/**
	 * 是否开启下载相关的log，打包aar时需要设置为false
	 */
	public static final boolean isDownloadLog = false;
	
	/**
	 * 是否开启网络相关的log，打包aar时需要设置为false
	 */
	public static final boolean isNetLog = false;
	
	// ------------------------------------------------------------------
	
	private static String sTag = "Sdk";
	
	private static boolean sIsDebug = false;
	
	private static boolean sIsShowClassNameInTag = false;
	
	public static String getTag() {
		return sTag;
	}
	
	public static void setTag(String tag) {
		sTag = tag;
	}
	
	public static boolean isDebug() {
		return sIsDebug;
	}
	
	public static void setIsDebug(boolean isDebug) {
		sIsDebug = isDebug;
	}
	
	public static boolean isShowClassNameInTag() {
		return sIsShowClassNameInTag;
	}
	
	public static void setIsShowClassNameInTag(boolean isShowClassNameInTag) {
		sIsShowClassNameInTag = isShowClassNameInTag;
	}
	
	// INFO
	public static void i(String format, Object... args) {
		log(Log.INFO, null, format, args);
	}
	
	public static void i(Throwable throwable) {
		log(Log.INFO, throwable, null);
	}
	
	public static void i(Throwable throwable, String format, Object... args) {
		log(Log.INFO, throwable, format, args);
	}
	
	// ERROR
	public static void e(String format, Object... args) {
		log(Log.ERROR, null, format, args);
	}
	
	public static void e(Throwable throwable) {
		log(Log.ERROR, throwable, null);
	}
	
	public static void e(Throwable throwable, String format, Object... args) {
		log(Log.ERROR, throwable, format, args);
	}
	
	// DEBUG
	public static void d(String format, Object... args) {
		log(Log.DEBUG, null, format, args);
	}
	
	public static void d(Throwable throwable) {
		log(Log.DEBUG, throwable, null);
	}
	
	public static void d(Throwable throwable, String format, Object... args) {
		log(Log.DEBUG, throwable, format, args);
	}
	
	// WARN
	public static void w(String format, Object... args) {
		log(Log.WARN, null, format, args);
	}
	
	public static void w(Throwable throwable) {
		log(Log.WARN, throwable, null);
	}
	
	public static void w(Throwable throwable, String format, Object... args) {
		log(Log.WARN, throwable, format, args);
	}
	
	// VERBOSE
	public static void v(String format, Object... args) {
		log(Log.VERBOSE, null, format, args);
	}
	
	public static void v(Throwable throwable) {
		log(Log.VERBOSE, throwable, null);
	}
	
	public static void v(Throwable throwable, String format, Object... args) {
		log(Log.VERBOSE, throwable, format, args);
	}
	
	private static void log(int level, Throwable throwable, String format, Object... args) {
		if (!isDebug()) {
			return;
		}
		try {
			String msg = "";
			if (!TextUtils.isEmpty(format)) {
				msg = String.format(Locale.getDefault(), format, args);
			}
			
			String tag = getTag();
			
			if (isShowClassNameInTag()) {
				StackTraceElement[] elements = Thread.currentThread().getStackTrace();
				String classPackageName = elements[4].getClassName();
				tag += "_" + classPackageName.substring(classPackageName.lastIndexOf(".") + 1);
			}
			
			if (throwable == null) {
				switch (level) {
				case Log.DEBUG:
					Log.d(tag, msg);
					break;
				case Log.INFO:
					Log.i(tag, msg);
					break;
				case Log.WARN:
					Log.w(tag, msg);
					break;
				case Log.ERROR:
					Log.e(tag, msg);
					break;
				default:
					Log.v(tag, msg);
					break;
				}
			} else {
				switch (level) {
				case Log.DEBUG:
					Log.d(tag, msg, throwable);
					break;
				case Log.INFO:
					Log.i(tag, msg, throwable);
					break;
				case Log.WARN:
					Log.w(tag, msg, throwable);
					break;
				case Log.ERROR:
					Log.e(tag, msg, throwable);
					break;
				default:
					Log.v(tag, msg, throwable);
					break;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
