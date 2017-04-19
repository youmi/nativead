package net.youmi.ads.base.download.core;

import net.youmi.ads.base.log.DLog;

import java.io.File;

/**
 * @author zhitao
 * @since 2017-04-14 16:27
 */
class DownloadUtils {
	
	/**
	 * 文件被其他进程使用的判断依据
	 */
	final static int LOCK_INTERVAL_ms = 12000;
	
	/**
	 * 检查文件是否被其他进程使用中
	 * <p/>
	 * 这里的原理为:通过检查文件12秒内是否被修改过
	 *
	 * @return true or false
	 */
	static boolean isFileUsingByOtherProgress(File file) {
		return isFileUsingByOtherProgress(file, LOCK_INTERVAL_ms);
	}
	
	/**
	 * 检查文件是否被其他进程使用中
	 * <p/>
	 * 这里的原理为:通过检查文件最近一段时间内是否被修改过
	 *
	 * @return true or false
	 */
	private static boolean isFileUsingByOtherProgress(File file, int lockTime) {
		if (file == null) {
			return false;
		}
		
		try {
			// 如果文件存在
			if (file.exists()) {
				
				// 则检查当前时间和最后一次编辑时间的差值
				long currentTime = System.currentTimeMillis();
				long lastEditTime = file.lastModified();
				long intervalTime = currentTime - lastEditTime;
				
				if (intervalTime < lockTime) {
					// 判定为其他进程正在占用
					return true;
					
				} else {
					// 已超过限定值，判定为未被占用。
					return false;
				}
			}
		} catch (Throwable e) {
			if (DLog.isDownloadLog) {
				DLog.e(e);
			}
		}
		return false;
	}
}
