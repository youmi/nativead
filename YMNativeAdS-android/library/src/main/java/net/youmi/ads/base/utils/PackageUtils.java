package net.youmi.ads.base.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * @author zhitao
 * @since 2017-04-19 17:56
 */
public class PackageUtils {
	
	public static boolean isPakcageInstall(Context context, String packageName) {
		try {
			return getPackageInfo(context, packageName) != null;
		} catch (Throwable e) {
		}
		return false;
		
	}
	
	public static PackageInfo getPackageInfo(Context context, String packageName) {
		try {
			return context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (Throwable e) {
		}
		return null;
	}
	
}
