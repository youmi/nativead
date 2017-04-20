package net.youmi.ads.nativead.adinstall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import net.youmi.ads.base.hash.MD5;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.utils.FileUtils;
import net.youmi.ads.base.utils.JSONUtils;
import net.youmi.ads.base.utils.SPUtils;

import org.json.JSONObject;

/**
 * @author zhitao
 * @since 2017-04-19 17:20
 */
class ApkInstallBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public final void onReceive(Context context, Intent intent) {
		try {
			if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
				String pkgName = intent.getData().getSchemeSpecificPart();
				if (TextUtils.isEmpty(pkgName)) {
					return;
				}
				
				String data = SPUtils.getString(context, MD5.md5(pkgName), null);
				JSONObject jo = JSONUtils.toJsonObject(data);
				if (jo == null) {
					return;
				}
				
				// 还原数据
				String uri = JSONUtils.getString(jo, "a", null);
				long startInstallTime = JSONUtils.getLong(jo, "b", -1);
				boolean isNeed2OpenApp = JSONUtils.getBoolean(jo, "c", false);
				boolean isNeed2DeleteApkAfterInstalled = JSONUtils.getBoolean(jo, "d", true);
				String apkFileAbsPath = JSONUtils.getString(jo, "e", null);
				
				// 如果需要删除apk的话
				if (isNeed2DeleteApkAfterInstalled && !TextUtils.isEmpty(apkFileAbsPath)) {
					FileUtils.delete(apkFileAbsPath);
				}
				
				// 如果不需要打开app的话就不继续了
				if (!isNeed2OpenApp) {
					return;
				}
				
				// 如果不是我们安装的包名
				// 或者是我们安装的包名，但是已经过时了
				// 就不处理
				if (startInstallTime == -1 || (System.currentTimeMillis() - startInstallTime) > 30 * 60 * 1000) {
					return;
				}
				
				AdInstallUtils.startAd(context, pkgName, uri);
				
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		
	}
}
