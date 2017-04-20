package net.youmi.ads.nativead.adinstall;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.utils.IntentUtils;
import net.youmi.ads.base.utils.PackageUtils;

/**
 * @author zhitao
 * @since 2017-04-20 11:01
 */
public class AdInstallUtils {
	
	private static ApkInstallBroadcastReceiver mReceiver;
	
	public synchronized static void registerApkInstallReceiver(Context context) {
		
		try {
			if (context == null) {
				return;
			}
			Context applicationContext = context.getApplicationContext();
			if (applicationContext == null) {
				return;
			}
			if (mReceiver != null) {
				return;
			}
			ApkInstallBroadcastReceiver receiver = new ApkInstallBroadcastReceiver();
			IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
			filter.addDataScheme("package");
			applicationContext.registerReceiver(receiver, filter);
			mReceiver = receiver;
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
	
	public static void startAd(Context context, String pkgName, String uri) {
		Context applicationContext = context.getApplicationContext();
		if (PackageUtils.isPakcageInstall(applicationContext, pkgName)) {
			
			// 先尝试用uri的方法打开app
			if (uri != null) {
				uri = uri.trim();
				if (!TextUtils.isEmpty(uri)) {
					Intent intent = IntentUtils.getIntentFromUri(applicationContext, uri);
					if (intent != null) {
						applicationContext.startActivity(intent);
						return;
					}
				}
			}
			
			// 不能用uri打开的话就用url打开
			IntentUtils.startActivity(applicationContext, pkgName);
		}
	}
	
}
