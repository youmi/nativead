package net.youmi.ads.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zhitao
 * @since 2017-04-17 11:31
 */
public class IntentUtils {
	
	/**
	 * 获取打开应用安装界面的Intent
	 *
	 * @param context     上下文
	 * @param fileAbsPath 需要安装的apk文件路径
	 *
	 * @return Intent
	 */
	public static Intent getIntentForInstallApk(Context context, String fileAbsPath) {
		return getIntentForInstallApk(context, new File(fileAbsPath));
	}
	
	/**
	 * 获取打开应用安装界面的Intent
	 *
	 * @param context 上下文
	 * @param file    需要安装的apk文件
	 *
	 * @return Intent
	 */
	public static Intent getIntentForInstallApk(Context context, File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri uri;
			if (Build.VERSION.SDK_INT >= 24) {
				
				String FileProviderClassName = "android.support.v4.content.FileProvider";
				ProviderInfo providerInfo = ComponentUtils.getProviderInfo(context, FileProviderClassName);
				if (providerInfo == null) {
					return null;
				}
				
				if (!providerInfo.grantUriPermissions) {
					return null;
				}
				
				if (TextUtils.isEmpty(providerInfo.authority)) {
					return null;
				}
				
				Method method = Class.forName(FileProviderClassName)
				                     .getDeclaredMethod("getUriForFile", Context.class, String.class, File.class);
				method.setAccessible(true);
				Object obj = method.invoke(null, context, providerInfo.authority, file);
				if (obj == null) {
					return null;
				}
				uri = (Uri) obj;
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			} else {
				uri = Uri.fromFile(file);
			}
			
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			return intent;
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
	/**
	 * 从uri中解析出Intent
	 *
	 * @param context 上下文
	 * @param uri     uri
	 * @param flags   intentFlags
	 *
	 * @return Intent
	 */
	public static Intent getIntentFromUri(Context context, String uri, int flags) {
		try {
			Intent intent = Intent.parseUri(uri, flags);
			if (intent == null) {
				return null;
			}
			List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
			if (list == null || list.isEmpty()) {
				return null;
			}
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			return intent;
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
	/**
	 * 从uri中解析出Intent
	 *
	 * @param context 上下文
	 * @param uri     uri
	 *
	 * @return Intent
	 */
	public static Intent getIntentFromUri(Context context, String uri) {
		return getIntentFromUri(context, uri, 0);
	}
	
	public static boolean startActivity(Context context, String packageName) {
		return startActivity(context, packageName, 0);
	}
	
	public static boolean startActivity(Context context, String packageName, int flags) {
		try {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				Intent intent = pm.getLaunchIntentForPackage(packageName);
				if (intent != null) {
					if (!(context instanceof Activity)) {
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					}
					intent.addFlags(flags);
					context.startActivity(intent);
					return true;
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return false;
	}
	
}
