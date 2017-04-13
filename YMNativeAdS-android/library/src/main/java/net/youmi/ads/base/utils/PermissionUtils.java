package net.youmi.ads.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import net.youmi.ads.base.log.DLog;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zhitao
 * @since 2017-04-13 14:45
 */
public class PermissionUtils {
	
	/**
	 * 获取开发者在AndroidManifest.xml文件中配置的所有权限信息，注意：仅仅是获取是否有没有在AndroidManifest.xml中配置，并不是是否已经被允许了
	 * {@link #isPermissionGranted(Context, String, String...)} } 方法是不能判断到下面这种权限的存在的
	 * <pre>
	 * <uses-permission
	 *     android:name="android.permission.PACKAGE_USAGE_STATS"
	 *     tools:ignore="ProtectedPermissions" />
	 * </pre>
	 * 因此就存在了这种一次性获取所有权限的，然后进行contains的方法来进行判断是否拥有某个权限的方法
	 *
	 * @param context
	 *
	 * @return
	 */
	public static List<String> getPkgNamePermissions(Context context, String pkgName) {
		try {
			return Arrays.asList(context.getPackageManager()
			                            .getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS).requestedPermissions);
		} catch (Throwable e) {
			DLog.e(e);
		}
		return Collections.emptyList();
	}
	
	/**
	 * 检查自身应用是否已经被授权指定权限
	 *
	 * @param context     上下文
	 * @param pkgName     目标应用
	 * @param permissions 指定权限
	 *
	 * @return <ul>
	 * <li>{@code true}： 目标应用已被授权本权限</li>
	 * <li>{@code false}： 目标应用没有被授权本权限</li>
	 * </ul>
	 */
	public static boolean isPermissionGranted(Context context, String pkgName, String[] permissions) {
		try {
			if (permissions == null || permissions.length == 0) {
				return false;
			}
			for (String permission : permissions) {
				if (PackageManager.PERMISSION_GRANTED != context.getPackageManager().checkPermission(permission, pkgName)) {
					return false;
				}
			}
			return true;
		} catch (Throwable e) {
			DLog.e(e);
		}
		//		try {
		//			if (context.checkCallingOrSelfPermission(permissionName) == PackageManager.PERMISSION_DENIED) {
		//				return false;
		//			}
		//		} catch (Throwable e) {
		//			DLog.e(e);
		//		}
		return false;
	}
	
	/**
	 * 检查自身应用是否已经被授权指定权限
	 *
	 * @param context     上下文
	 * @param permissions 指定权限
	 *
	 * @return <ul>
	 * <li>{@code true}： 目标应用已被授权本权限</li>
	 * <li>{@code false}： 目标应用没有被授权本权限</li>
	 * </ul>
	 */
	public static boolean isPermissionGranted(Context context, String... permissions) {
		return isPermissionGranted(context, context.getPackageName(), permissions);
	}
	
	/**
	 * 为用户导航到指定应用的详情页面，让用户自己主动开启权限
	 * <p/>
	 * 目前只能导航到最近一级的界面，没法直接到达指定应用的权限设置界面
	 * <p/>
	 * <b>需要注意Intent不过去的时候会抛异常的情况，如一些厂商去掉了这个界面之类的</b>
	 *
	 * @return <ul>
	 * <li>{@code true}： 能成功导航到应用详情页面 </li>
	 * <li>{@code false}： 不能成功导航到应用详情页面 </li>
	 * </ul>
	 *
	 * @since 2015-12-09
	 */
	public static boolean openAppPermissionSetting(Context context, String pkgName) {
		try {
			Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + pkgName));
			appSettingsIntent.addCategory(Intent.CATEGORY_DEFAULT);
			appSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(appSettingsIntent);
			return true;
		} catch (Throwable e) {
			DLog.e(e);
		}
		return false;
	}
}
