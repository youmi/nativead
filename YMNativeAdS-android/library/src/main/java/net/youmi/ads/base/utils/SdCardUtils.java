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
	 * sd卡是否存在
	 *
	 * @return
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
	 * 获取sdcard根目录
	 *
	 * @return
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
	 * 获取sd总空间容量
	 *
	 * @return
	 */
	public static long getSdCardTotalSize() {
		return FileUtils.getDirTotalSize(getSdcardRootPath());
	}
	
	/**
	 * 获取sd剩余并且app可用使用的空间容量
	 *
	 * @return
	 */
	public static long getSdCardFreeSize() {
		return FileUtils.getDirFreeSize(getSdcardRootPath());
	}
	
	/**
	 * 获取sd已用空间容量
	 *
	 * @return
	 */
	public static long getSdCardUsedSize() {
		return FileUtils.getDirUsedSize(getSdcardRootPath());
	}
	
	/**
	 * sd卡是否存在所需求的空间大小
	 *
	 * @param context
	 * @param requireBytes 所需求的空间大小
	 *
	 * @return
	 */
	public static boolean isSdCardHaveEnoughSpace(Context context, long requireBytes) {
		return IsSdCardCanWrite(context) && getSdCardFreeSize() > requireBytes;
	}
	//
	//	// -------------------------------------------------------------------------------------
	//	// 内部存储卡使用原来的方式获取
	//	//	/**
	//	//	 * 获取内部存储卡路径
	//	//	 *
	//	//	 * @param context
	//	//	 *
	//	//	 * @return
	//	//	 */
	//	//	public static String getInternalSdCardPath(Context context) {
	//	//		String path = null;
	//	//		try {
	//	//			Object[] storageVolumes = getStorageVolumeList(context);
	//	//			if (storageVolumes != null) {
	//	//				for (Object storageVolume : storageVolumes) {
	//	//					String tmpPath = getStorageVolumePath(storageVolume);
	//	//					if (tmpPath != null && !isStorageVolumeRemovable(storageVolume)) {
	//	//						path = tmpPath;
	//	//						break;
	//	//					}
	//	//				}
	//	//			}
	//	//			if (path == null) {
	//	//				path = Environment.getExternalStorageDirectory().getAbsolutePath();
	//	//			}
	//	//		} catch (Exception e) {
	//	//			if (DLog.isSysInfoLog) {
	//	//				DLog.te(DLog.mSysInfoTag, SdCardStats.class, e);
	//	//			}
	//	//		}
	//	//		return path;
	//	//	}
	//	//
	//	//	/**
	//	//	 * 判断内部储存卡是否存在
	//	//	 *
	//	//	 * @param context
	//	//	 *
	//	//	 * @return
	//	//	 */
	//	//	public static boolean isInternalSdCardExist(Context context) {
	//	//		String path = getInternalSdCardPath(context);
	//	//		if (path != null && getStorageVolumeState(context, path).equals(Environment.MEDIA_MOUNTED)) {
	//	//			return true;
	//	//		}
	//	//		return false;
	//	//	}
	//	//
	//	//	/**
	//	//	 * 判断内部储存卡是否可读
	//	//	 *
	//	//	 * @return
	//	//	 */
	//	//	public static boolean isInternalSdCardCanRead(Context context) {
	//	//		try {
	//	//			if (Util_System_Permission.isPermissionGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
	//	//				String state = getStorageVolumeState(context, getInternalSdCardPath(context));
	//	//				if (state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
	//	//					return true;
	//	//				}
	//	//			}
	//	//		} catch (Throwable e) {
	//	//			if (DLog.isSysInfoLog) {
	//	//				DLog.te(DLog.mSysInfoTag, SdCardStats.class, e);
	//	//			}
	//	//		}
	//	//		return false;
	//	//	}
	//	//
	//	//	/**
	//	//	 * 判断内部储存卡是否可写
	//	//	 *
	//	//	 * @return
	//	//	 */
	//	//	public static boolean isInternalSdCardCanWrite(Context context) {
	//	//		try {
	//	//			if (Util_System_Permission.isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
	//	//				if (isInternalSdCardExist(context)) {
	//	//					return true;
	//	//				}
	//	//			}
	//	//		} catch (Throwable e) {
	//	//			if (DLog.isSysInfoLog) {
	//	//				DLog.te(DLog.mSysInfoTag, SdCardStats.class, e);
	//	//			}
	//	//		}
	//	//		return false;
	//	//	}
	//	//
	//	//	/**
	//	//	 * 获取内部储存卡总容量
	//	//	 *
	//	//	 * @param context
	//	//	 *
	//	//	 * @return
	//	//	 */
	//	//	public static long getInternalSdCardTotalSize(Context context) {
	//	//		return Util_System_File.getDirTotalSize(getInternalSdCardPath(context));
	//	//	}
	//	//
	//	//	/**
	//	//	 * 获取内部储存卡空闲容量
	//	//	 *
	//	//	 * @param context
	//	//	 *
	//	//	 * @return
	//	//	 */
	//	//	public static long getInternalSdCardFreeSize(Context context) {
	//	//		return Util_System_File.getDirFreeSize(getInternalSdCardPath(context));
	//	//	}
	//	//
	//	//	/**
	//	//	 * 获取内部储存卡已用容量
	//	//	 *
	//	//	 * @param context
	//	//	 *
	//	//	 * @return
	//	//	 */
	//	//	public static long getInternalSdCardUsedSize(Context context) {
	//	//		return Util_System_File.getDirUsedSize(getInternalSdCardPath(context));
	//	//	}
	//
	//	/**
	//	 * 获取外置SDCard路径
	//	 * <p>
	//	 * 如果路径不为null，可以说明存在外置SDCard卡槽
	//	 * </p>
	//	 *
	//	 * @param context
	//	 *
	//	 * @return
	//	 */
	//	public static String getExternalSdCardPath(Context context) {
	//		String path = null;
	//		try {
	//			Object[] storageVolumes = getStorageVolumeList(context);
	//			if (storageVolumes != null) {
	//				for (Object storageVolume : storageVolumes) {
	//					String tmpPath = getStorageVolumePath(storageVolume);
	//					if (tmpPath != null && isStorageVolumeRemovable(storageVolume)) {
	//						path = tmpPath;
	//						break;
	//					}
	//				}
	//			}
	//		} catch (Exception e) {
	//			if (DLog.isSysInfoLog) {
	//				DLog.te(DLog.mSysInfoTag, SdCardStats.class, e);
	//			}
	//		}
	//		return path;
	//	}
	//
	//	/**
	//	 * 判断设备是否存在外置SDCard卡槽
	//	 *
	//	 * @param context
	//	 *
	//	 * @return
	//	 */
	//	public static boolean isExternalSdCardSlotExist(Context context) {
	//		return getExternalSdCardPath(context) != null && getExternalSdCardPath(context).length() > 0;
	//	}
	//
	//	/**
	//	 * 判断外置SDCard是否存在
	//	 *
	//	 * @param context
	//	 *
	//	 * @return
	//	 */
	//	public static boolean isExternalSdCardExist(Context context) {
	//		String path = getExternalSdCardPath(context);
	//		if (path != null && getStorageVolumeState(context, path).equals(Environment.MEDIA_MOUNTED)) {
	//			return true;
	//		}
	//		return false;
	//	}
	//
	//	/**
	//	 * 获取外置SDCard总容量
	//	 *
	//	 * @param context
	//	 *
	//	 * @return
	//	 */
	//	public static long getExternalSdCardTotalSize(Context context) {
	//		return Util_System_File.getDirTotalSize(getExternalSdCardPath(context));
	//	}
	//
	//	/**
	//	 * 获取外置SDCard空闲容量
	//	 *
	//	 * @param context
	//	 *
	//	 * @return
	//	 */
	//	public static long getExternalSdCardFreeSize(Context context) {
	//		return Util_System_File.getDirFreeSize(getExternalSdCardPath(context));
	//	}
	//
	//	/**
	//	 * 获取外置SDCard已用容量
	//	 *
	//	 * @param context
	//	 *
	//	 * @return
	//	 */
	//	public static long getExternalSdCardUsedSize(Context context) {
	//		return Util_System_File.getDirUsedSize(getExternalSdCardPath(context));
	//	}
	//
	//	/**
	//	 * 通过反射执行{@link StorageVolume#getPath()}方法，获取存储卷的路径
	//	 *
	//	 * @param storageVolume
	//	 *
	//	 * @return
	//	 */
	//	private static String getStorageVolumePath(Object storageVolume) {
	//		String path = null;
	//		try {
	//			Class<?> storageVolumeClass = getStorageVolumeClass();
	//			if (storageVolumeClass == null || storageVolume == null) {
	//				return path;
	//			}
	//			Method getPathMethod = storageVolumeClass.getMethod("getPath");
	//			path = (String) getPathMethod.invoke(storageVolume);
	//		} catch (Exception e) {
	//			if (DLog.isSysInfoLog) {
	//				DLog.te(DLog.mSysInfoTag, SdCardStats.class, e);
	//			}
	//		}
	//		return path;
	//	}
	//
	//	/**
	//	 * 通过反射执行{@link StorageVolume#isRemovable()}方法，判断储存卷是否可移动
	//	 *
	//	 * @param storageVolume
	//	 *
	//	 * @return
	//	 */
	//	private static boolean isStorageVolumeRemovable(Object storageVolume) {
	//		boolean result = false;
	//		try {
	//			Class<?> storageVolumeClass = getStorageVolumeClass();
	//			if (storageVolumeClass == null || storageVolume == null) {
	//				return result;
	//			}
	//			Method isRemovableMethod = storageVolumeClass.getMethod("isRemovable");
	//			result = (Boolean) isRemovableMethod.invoke(storageVolume);
	//		} catch (Exception e) {
	//			if (DLog.isSysInfoLog) {
	//				DLog.te(DLog.mSysInfoTag, SdCardStats.class, e);
	//			}
	//		}
	//		return result;
	//	}
	//
	//	/**
	//	 * 获取存储卷类
	//	 * <p>
	//	 * 由于{@code StorageVolume}在Android 7.0才对外开放，但其实在Android 3.0左右就存在了，所以通过反射方式获取以兼容使用Android低版本编译
	//	 * </p>
	//	 *
	//	 * @return
	//	 */
	//	private static Class<?> getStorageVolumeClass() {
	//		try {
	//			return SdCardStats.class.getClassLoader().loadClass("android.os.storage.StorageVolume");
	//		} catch (ClassNotFoundException e) {
	//			if (DLog.isSysInfoLog) {
	//				DLog.te(DLog.mSysInfoTag, SdCardStats.class, e);
	//			}
	//		}
	//		return null;
	//	}
	//
	//	/**
	//	 * 获取存储卷列表
	//	 * <p>
	//	 * 由于{@link StorageManager#getVolumeList()}不对外开放，所以使用反射方式获取
	//	 * </p>
	//	 *
	//	 * @param context
	//	 *
	//	 * @return
	//	 */
	//	private static Object[] getStorageVolumeList(Context context) {
	//		Object[] objects = null;
	//		try {
	//			StorageManager storageManager = getStorageManager(context);
	//			if (storageManager == null) {
	//				return objects;
	//			}
	//			Method getVolumeListMethod = StorageManager.class.getMethod("getVolumeList");
	//			objects = (Object[]) getVolumeListMethod.invoke(storageManager);
	//		} catch (Exception e) {
	//			if (DLog.isSysInfoLog) {
	//				DLog.te(DLog.mSysInfoTag, SdCardStats.class, e);
	//			}
	//		}
	//		return objects;
	//	}
	//
	//	/**
	//	 * 获取存储卷的状态
	//	 * <p>
	//	 * 由于{@link StorageManager#getVolumeState()}不对外开放，所以使用反射方式获取
	//	 * </p>
	//	 *
	//	 * @param context
	//	 * @param mountPoint
	//	 *
	//	 * @return
	//	 */
	//	private static String getStorageVolumeState(Context context, String mountPoint) {
	//		String state = "unknown";
	//		try {
	//			StorageManager storageManager = getStorageManager(context);
	//			if (storageManager == null || Basic_StringUtil.isNullOrEmpty(mountPoint)) {
	//				return state;
	//			}
	//			Method getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", String.class);
	//			state = (String) getVolumeStateMethod.invoke(storageManager, mountPoint);
	//		} catch (Exception e) {
	//			if (DLog.isSysInfoLog) {
	//				DLog.te(DLog.mSysInfoTag, SdCardStats.class, e);
	//			}
	//		}
	//		return state;
	//	}
	//
	//	/**
	//	 * 获取存储管理者
	//	 *
	//	 * @param context
	//	 *
	//	 * @return
	//	 */
	//	private static StorageManager getStorageManager(Context context) {
	//		if (context == null) {
	//			return null;
	//		}
	//		return (StorageManager) context.getApplicationContext().getSystemService(Activity.STORAGE_SERVICE);
	//	}
	
}
