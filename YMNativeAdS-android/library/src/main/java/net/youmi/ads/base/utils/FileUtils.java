package net.youmi.ads.base.utils;

import android.os.Build;
import android.os.StatFs;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;

import java.io.File;

/**
 * @author zhitao
 * @since 2017-04-14 15:23
 */
public class FileUtils {
	
	/**
	 * 获取指定的路径的文件，如果没有会创建(支持自动补全所有父目录)
	 *
	 * @param path 文件路径
	 */
	public static File getValidFile(String path) {
		return getValidFile(new File(path));
	}
	
	/**
	 * 获取指定的路径的文件，如果没有会创建(支持自动补全所有父目录)
	 *
	 * @param file 文件
	 */
	public static File getValidFile(File file) {
		try {
			if (file == null) {
				return null;
			}
			if (file.exists()) {
				//  如果文件存在
				if (file.isFile()) {
					return file;
				}
				if (file.isDirectory()) {
					return null;
				}
			} else {
				// 如果文件不存在，则创建
				
				// 检查是否存在父目录
				// 如果不存在父目录的话, 自动补全所有的根目录,然后创建
				if (!file.getParentFile().exists()) {
					
					DLog.w("当前文件[%s]不存在父目录[%s]，将补全", file.getAbsolutePath(), file.getParent());
					boolean isMkdirsSuccess = file.getParentFile().mkdirs();
					
					if (!isMkdirsSuccess) {
						DLog.w("补全父目录[%s]失败", file.getParent());
						return null;
					} else {
						if (!file.getParentFile().exists()) {
							DLog.w("补全父目录[%s]失败", file.getParent());
							return null;
						}
						DLog.i("补全父目录[%s]成功", file.getParent());
					}
				}
				boolean isSuccess = file.createNewFile();
				if (isSuccess) {
					return file;
				} else {
					return null;
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
	/**
	 * 删除文件/目录
	 *
	 * @param file 待删除文件/目录
	 *
	 * @return true： 删除成功； false： 删除失败
	 */
	public static boolean delete(File file) {
		try {
			if (file == null) {
				return false;
			}
			if (file.exists()) {
				if (file.isFile()) {
					boolean isSuccess = file.delete();
					if (isSuccess) {
						DLog.i("文件删除成功： %s", file.getAbsolutePath());
					} else {
						DLog.e("文件删除失败： %s", file.getAbsolutePath());
					}
					return isSuccess;
				} else if (file.isDirectory()) {
					for (File f : file.listFiles()) {
						if (!delete(f)) {
							return false;
						}
					}
					boolean isSuccess = file.delete();
					if (isSuccess) {
						DLog.i("文件删除成功： %s", file.getAbsolutePath());
					} else {
						DLog.e("文件删除失败： %s", file.getAbsolutePath());
					}
					return isSuccess;
				}
			} else {
				// 因为最终目的是令该文件不存在，所以如果文件一开始就不存在，那么也就意味着删除成功
				return true;
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return false;
	}
	
	/**
	 * 获取目录空间总容量
	 *
	 * @return 容量(Byte)
	 */
	public static long getDirTotalSize(String dirPath) {
		try {
			if (TextUtils.isEmpty(dirPath)) {
				return 0;
			}
			StatFs statFs = new StatFs(dirPath);
			
			// 获取单个数据块的大小（Byte）
			long blocSize;
			if (Build.VERSION.SDK_INT < 18) {
				blocSize = statFs.getBlockSize();
			} else {
				blocSize = statFs.getBlockSizeLong();
			}
			
			// 获取所有数据块的数量
			long countBlock;
			if (Build.VERSION.SDK_INT < 18) {
				countBlock = statFs.getBlockCount();
			} else {
				countBlock = statFs.getBlockCountLong();
			}
			
			return Math.abs(countBlock * blocSize);
		} catch (Throwable e) {
			DLog.e(e);
		}
		return 0;
	}
	
	/**
	 * 获取目录空间已用容量
	 *
	 * @return 容量(Byte)
	 */
	public static long getDirUsedSize(String dirPath) {
		try {
			
			if (TextUtils.isEmpty(dirPath)) {
				return 0;
			}
			
			StatFs statFs = new StatFs(dirPath);
			
			// 获取单个数据块的大小（Byte）
			long blocSize;
			if (Build.VERSION.SDK_INT < 18) {
				blocSize = statFs.getBlockSize();
			} else {
				blocSize = statFs.getBlockSizeLong();
			}
			
			// 获取所有数据块的数量
			long totalBlocks;
			if (Build.VERSION.SDK_INT < 18) {
				totalBlocks = statFs.getBlockCount();
			} else {
				totalBlocks = statFs.getBlockCountLong();
			}
			
			// 获取空闲数据块的数量
			long availableBlocks;
			if (Build.VERSION.SDK_INT < 18) {
				availableBlocks = statFs.getAvailableBlocks();
			} else {
				availableBlocks = statFs.getAvailableBlocksLong();
			}
			
			return Math.abs((totalBlocks - availableBlocks) * blocSize);
		} catch (Throwable e) {
			DLog.e(e);
		}
		return 0;
	}
	
	/**
	 * 获取目录空间剩余容量
	 *
	 * @return 容量(Byte)
	 */
	public static long getDirFreeSize(String dirPath) {
		try {
			if (TextUtils.isEmpty(dirPath)) {
				return 0;
			}
			
			StatFs statFs = new StatFs(dirPath);
			
			// 获取单个数据块的大小
			long blocSize;
			if (Build.VERSION.SDK_INT < 18) {
				blocSize = statFs.getBlockSize();
			} else {
				blocSize = statFs.getBlockSizeLong();
			}
			
			// 获取空闲数据块的数量
			long countBlock;
			if (Build.VERSION.SDK_INT < 18) {
				countBlock = statFs.getAvailableBlocks();
			} else {
				countBlock = statFs.getAvailableBlocksLong();
			}
			
			return Math.abs(countBlock * blocSize);
		} catch (Throwable e) {
			DLog.e(e);
		}
		return 0;
	}
	
}
