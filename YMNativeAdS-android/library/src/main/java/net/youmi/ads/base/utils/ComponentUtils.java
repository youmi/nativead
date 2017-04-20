package net.youmi.ads.base.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;

/**
 * @author zhitao
 * @since 2017-04-17 14:49
 */
public class ComponentUtils {
	
	/**
	 * 获取ProviderInfo。
	 *
	 * @param context           上下文
	 * @param providerClassName 指定的providerName
	 *
	 * @return ProviderInfo
	 */
	public static ProviderInfo getProviderInfo(Context context, String providerClassName) {
		try {
			if (context == null || TextUtils.isEmpty(providerClassName)) {
				return null;
			}
			
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS);
			if (pi == null) {
				return null;
			}
			
			ProviderInfo[] providerInfos = pi.providers;
			if (providerInfos == null) {
				return null;
			}
			
			for (ProviderInfo providerInfo : providerInfos) {
				if (providerInfo.name.equals(providerClassName)) {
					return providerInfo;
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
}
