package net.youmi.ads.base.download.store;

import android.text.TextUtils;

import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.hash.MD5;

import java.io.File;

/**
 * @author zhitao
 * @since 2017-04-14 20:54
 */
public class DefaultDownloadDir extends AbsDownloadDir {
	
	/**
	 * @param dir                    目录位置
	 * @param dirMaxCacheSize_KB     全部文件的最大限制大小 (KB) 传入{@link #UN_LIMT_STORE_SIZE} 表示不限制目录缓存总体积
	 * @param dirPerFileCacheTime_ms 每个文件的缓存时间 (ms) 传入{@link #UN_LIMT_STORE_TIME} 表示不限制每个文件的缓存时间
	 */
	public DefaultDownloadDir(File dir, long dirMaxCacheSize_KB, long dirPerFileCacheTime_ms) {
		super(dir, dirMaxCacheSize_KB, dirPerFileCacheTime_ms);
	}
	
	/**
	 * @param dirPath                目录路径地址
	 * @param dirMaxCacheSize_KB     全部文件的最大限制大小 (KB) 传入{@link #UN_LIMT_STORE_SIZE} 标识不限制目录缓存总体积
	 * @param dirPerFileCacheTime_ms 每个文件的缓存时间 (ms) 传入{@link #UN_LIMT_STORE_TIME} 标识不限制每个文件的缓存时间
	 */
	public DefaultDownloadDir(String dirPath, long dirMaxCacheSize_KB, long dirPerFileCacheTime_ms) {
		super(dirPath, dirMaxCacheSize_KB, dirPerFileCacheTime_ms);
	}
	
	/**
	 * 根据下载任务对象来生成缓存文件的文件名，规则可随意
	 * <p>
	 * e.g.
	 * <p>
	 * new File(md5(url)+".cache");
	 *
	 * @param fileDownloadTask 下载任务
	 *
	 * @return 缓存文件
	 */
	@Override
	public File newDownloadTempFile(FileDownloadTask fileDownloadTask) {
		String srcFileName = fileDownloadTask.getIdentify();
		if (TextUtils.isEmpty(srcFileName)) {
			srcFileName = fileDownloadTask.getRawDownloadUrl();
		}
		return new File(getDir(), MD5.md5(srcFileName) + ".y".trim() + "mt".trim() + "f");
	}
	
	/**
	 * 根据下载任务对象来生成最终下载完成的文件的文件名，规则可随意
	 * <p>
	 * e.g.
	 * <p>
	 * new File(md5(url));
	 *
	 * @param fileDownloadTask 下载任务
	 *
	 * @return 最终存储的下载文件
	 */
	@Override
	public File newDownloadStoreFile(FileDownloadTask fileDownloadTask) {
		String srcFileName = fileDownloadTask.getIdentify();
		if (TextUtils.isEmpty(srcFileName)) {
			srcFileName = fileDownloadTask.getRawDownloadUrl();
		}
		return new File(getDir(), MD5.md5(srcFileName));
	}
	
}