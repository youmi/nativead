package net.youmi.ads.base.download;

import android.content.Context;

import net.youmi.ads.base.download.store.AbsDownloadDir;
import net.youmi.ads.base.download.store.DefaultDownloadDir;
import net.youmi.ads.base.utils.SdCardUtils;

/**
 * @author zhitao
 * @since 2017-04-14 20:24
 */
public class BaseApkFileDownloadManager extends AbsCachedDownloadManager {

	/**
	 * 下载目录上限大小（1GB）
	 */
	private final static long MAX_LIMIT_MB = 1024 * 1024 * 1024;

	/**
	 * 每个文件缓存上限时间（5天）
	 */
	private final static long MAX_LIMIT_TIME = 5 * 24 * 60 * 60 * 1000;

	private static volatile BaseApkFileDownloadManager sInstance;

	private BaseApkFileDownloadManager() {
		super();
	}

	public static BaseApkFileDownloadManager getInstance() {
		if (sInstance == null) {
			synchronized (BaseApkFileDownloadManager.class) {
				if (sInstance == null) {
					sInstance = new BaseApkFileDownloadManager();
				}
			}
		}
		return sInstance;
	}

	/**
	 * @param context ApplicationContext
	 *
	 * @return 一个下载的最终目录,{@link AbsDownloadDir}是一个带有自定义规则的下载目录(如：目录是否会自动清理，目录下的文件命名规范等)
	 */
	@Override
	public AbsDownloadDir newDownloadDir(Context context) {
		if (SdCardUtils.isSdCardHaveEnoughSpace(context, MAX_LIMIT_MB)) {
			return new DefaultDownloadDir(SdCardUtils.getSdcardRootPath() + "/Android/data/.cache/.apk",
					MAX_LIMIT_MB,
					MAX_LIMIT_TIME
			);
		} else {
			return new DefaultDownloadDir(context.getCacheDir() + "/.apk", MAX_LIMIT_MB, MAX_LIMIT_TIME);
		}
	}

}