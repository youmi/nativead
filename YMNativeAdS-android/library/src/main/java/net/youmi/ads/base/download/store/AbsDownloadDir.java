package net.youmi.ads.base.download.store;

import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhitao
 * @since 2017-04-14 15:21
 */
public abstract class AbsDownloadDir {
	
	/**
	 * 不限制所有文件的缓存 总体积
	 */
	public static final long UN_LIMT_STORE_SIZE = -1;
	
	/**
	 * 不限制每个文件的缓存时间
	 */
	public static final long UN_LIMT_STORE_TIME = -1;
	
	/**
	 * 每个文件缓存的时间
	 */
	private long mDirPerFileCacheTime_ms = UN_LIMT_STORE_TIME;
	
	/**
	 * 所有缓存文件的总体积
	 */
	private long mDirMaxCacheSize_KB = UN_LIMT_STORE_SIZE;
	
	/**
	 * 文件缓存目录
	 */
	private File mCacheDir;
	
	/**
	 * @param dir                    目录位置
	 * @param dirMaxCacheSize_KB     全部文件的最大限制大小 (KB) 传入{@link #UN_LIMT_STORE_SIZE} 表示不限制目录缓存总体积
	 * @param dirPerFileCacheTime_ms 每个文件的缓存时间 (ms) 传入{@link #UN_LIMT_STORE_TIME} 表示不限制每个文件的缓存时间
	 */
	public AbsDownloadDir(File dir, long dirMaxCacheSize_KB, long dirPerFileCacheTime_ms) {
		mCacheDir = dir;
		mDirMaxCacheSize_KB = dirMaxCacheSize_KB;
		mDirPerFileCacheTime_ms = dirPerFileCacheTime_ms;
	}
	
	/**
	 * @param dirPath                目录路径地址
	 * @param dirMaxCacheSize_KB     全部文件的最大限制大小 (KB) 传入{@link #UN_LIMT_STORE_SIZE} 标识不限制目录缓存总体积
	 * @param dirPerFileCacheTime_ms 每个文件的缓存时间 (ms) 传入{@link #UN_LIMT_STORE_TIME} 标识不限制每个文件的缓存时间
	 */
	public AbsDownloadDir(String dirPath, long dirMaxCacheSize_KB, long dirPerFileCacheTime_ms) {
		this(new File(dirPath), dirMaxCacheSize_KB, dirPerFileCacheTime_ms);
	}
	
	/**
	 * @return 检查存放目录以及传入参数是否有效
	 */
	public boolean isDirValid() {
		if (mCacheDir == null) {
			return false;
		}
		if (mCacheDir.exists() && !mCacheDir.isDirectory()) {
			return false;
		}
		// 检查文件夹是否存在，如果不存在，重新建立文件夹
		if (!mCacheDir.exists() && !mCacheDir.mkdirs()) {
			return false;
		}
		if (mDirMaxCacheSize_KB <= 0 && mDirMaxCacheSize_KB != UN_LIMT_STORE_SIZE) {
			return false;
		}
		if (mDirPerFileCacheTime_ms <= 0 && mDirPerFileCacheTime_ms != UN_LIMT_STORE_SIZE) {
			return false;
		}
		return true;
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
	public abstract File newDownloadTempFile(FileDownloadTask fileDownloadTask);
	
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
	public abstract File newDownloadStoreFile(FileDownloadTask fileDownloadTask);
	
	/**
	 * @return 下载文件目录
	 */
	public File getDir() {
		return mCacheDir;
	}
	
	/**
	 * 优化传入来的目录
	 * <ol>
	 * <li>根据缓存时间，将已经超过缓存时间的文件进行删除</li>
	 * <li>检查当前目录的总体积是否还是超过最大限制大小，如果是的话，在根据文件的最后编辑时间排序，将最久的文件删除，直至当前目录总体积小于限制体积</li>
	 * </ol>
	 * 目前有个取舍的问题:
	 * 为了防止删除太久，会设置一个定时器10000，也就是说如果目录里面有超过10000个文件的话，那么最多可能删除10000个文件，
	 * 如果删除了10000个文件还是不能满足最大限制要求，那么也会停止
	 *
	 * @return <ul>
	 * <li>true：优化成功</li>
	 * <li>false：优化失败</li>
	 * </ul>
	 */
	public boolean optDir() {
		// 如果不限制的话，那么就返回已经优化成功
		if (mDirMaxCacheSize_KB == UN_LIMT_STORE_SIZE && mDirPerFileCacheTime_ms == UN_LIMT_STORE_TIME) {
			return true;
		}
		try {
			// 获取当前目录的所有文件列表
			File[] files = mCacheDir.listFiles();
			if (files == null || files.length == 0) {
				return true;
			}
			
			// 所有文件的总长度
			long countLen = 0;
			
			// 用于收集还没有超过缓存时间的文件待排序列表
			List<File> fileList = new ArrayList<>();
			
			// 将已经超过缓存时间的文件删除，同时收集剩下的文件
			for (File file : files) {
				if (file == null || !file.exists()) {
					continue;
				}
				
				// 1. 先检查有没有超期了，文件超期就删除
				if (isFileTimeOut(file)) {
					FileUtils.delete(file);
					continue;
				}
				
				// 2. 如果这个目录设置了总体积限制，那么就要收集没有超时的文件
				// 只有需要检查目录大小的情况下才需要计算文件大小，以及把文件放入待排序列表中
				if (mDirMaxCacheSize_KB != UN_LIMT_STORE_SIZE) {
					countLen += file.length();// 文件未超期或未被删除，就用来检查总容量
					fileList.add(file);// 加入到待排序列表中
				}
			}
			
			// 按lastModify进行，从旧到新
			Collections.sort(fileList, new FileLastModifyCom());// 这里需要添加排序算法
			
			// 使用链接将文件进行排序，文件比较旧的排在前面，如果超过目录缓存的总大小，删除排在前面的文件。
			Iterator<File> iterator = fileList.iterator();
			DLog.i("准备删除旧的但未过时的文件");
			
			int a = 10000;
			// 如果剩余文件长度大于限制长度，就需要不断循环删除
			while (countLen > mDirMaxCacheSize_KB && iterator.hasNext()) {
				try {
					File gfFile = iterator.next();
					iterator.remove();
					countLen -= gfFile.length();
					FileUtils.delete(gfFile);
				} catch (Throwable e) {
					DLog.e(e);
				}
				--a;
				if (a < 0) {
					// 防止死循环
					break;
				}
			}
			return true;
		} catch (Throwable e) {
			DLog.e(e);
		}
		return false;
	}
	
	/**
	 * 检查当前目录的指定文件是否已经超过缓存时间
	 *
	 * @param file 待检测文件
	 *
	 * @return true： 超时； false：还没有超时
	 */
	private boolean isFileTimeOut(File file) {
		if (file == null) {
			return false;
		}
		
		if (mDirPerFileCacheTime_ms == UN_LIMT_STORE_TIME) {
			return false;
		}
		
		if (System.currentTimeMillis() - file.lastModified() > mDirPerFileCacheTime_ms) {
			return true;
		}
		
		return false;
	}
	
	private static class FileLastModifyCom implements Comparator<File> {
		
		@Override
		public int compare(File lhs, File rhs) {
			try {
				if (lhs.lastModified() < rhs.lastModified()) {
					return -1;
				}
				return 1;
			} catch (Throwable e) {
				DLog.e(e);
			}
			return 0;
		}
	}
	
}
