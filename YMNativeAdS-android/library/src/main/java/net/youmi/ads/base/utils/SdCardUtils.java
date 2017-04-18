package net.youmi.ads.base.utils;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

import net.youmi.ads.base.log.DLog;

/**
 * @author zhitao
 * @since 2017-04-14 21:00
 */
public class SdCardUtils {
	
	/**
	 * 判断sd卡是否可读
	 *
	 * @param context 上下文
	 *
	 * @return true：可读；false： 不可读
	 */
	public static boolean IsSdCardCanRead(Context context) {
		try {
			if (PermissionUtils.isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
					return true;
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return false;
	}
	
	/**
	 * 判断sd卡是否可写
	 *
	 * @param context 上下文
	 *
	 * @return true： 可写；false： 不可写
	 */
	public static boolean IsSdCardCanWrite(Context context) {
		try {
			if (PermissionUtils.isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					return true;
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return false;
	}
	
	/**
	 * @return sd卡是否存在
	 */
	public static boolean isSdCardExist() {
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				return true;
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return false;
	}
	
	/**
	 * @return sd卡根目录
	 */
	public static String getSdcardRootPath() {
		try {
			if (isSdCardExist()) {
				return Environment.getExternalStorageDirectory().getPath();
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
	/**
	 * @return sd卡总空间容量
	 */
	public static long getSdCardTotalSize() {
		return FileUtils.getDirTotalSize(getSdcardRootPath());
	}
	
	/**
	 * @return sd卡剩余并且app可用使用的空间容量
	 */
	public static long getSdCardFreeSize() {
		return FileUtils.getDirFreeSize(getSdcardRootPath());
	}
	
	/**
	 * @return sd卡已用空间容量
	 */
	public static long getSdCardUsedSize() {
		return FileUtils.getDirUsedSize(getSdcardRootPath());
	}
	
	/**
	 * @param context      上下文
	 * @param requireBytes 所需求的空间大小
	 *
	 * @return sd卡是否存在所需求的空间大小
	 */
	public static boolean isSdCardHaveEnoughSpace(Context context, long requireBytes) {
		return IsSdCardCanWrite(context) && getSdCardFreeSize() > requireBytes;
	}
	
}
