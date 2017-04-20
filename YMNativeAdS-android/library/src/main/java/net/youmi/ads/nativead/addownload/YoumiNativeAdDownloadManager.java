package net.youmi.ads.nativead.addownload;

import android.content.Context;

import net.youmi.ads.base.download.AbsCachedDownloadManager;
import net.youmi.ads.base.download.model.FileDownloadTask;
import net.youmi.ads.base.download.store.AbsDownloadDir;
import net.youmi.ads.base.download.store.DefaultDownloadDir;
import net.youmi.ads.base.utils.SdCardUtils;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

/**
 * @author zhitao
 * @since 2017-04-17 15:41
 */
public class YoumiNativeAdDownloadManager extends AbsCachedDownloadManager {
	
	/**
	 * 下载目录上限大小（1GB）
	 */
	private final static long MAX_LIMIT_MB = 1024 * 1024 * 1024;
	
	/**
	 * 每个文件缓存上限时间（5天）
	 */
	private final static long MAX_LIMIT_TIME = 5 * 24 * 60 * 60 * 1000;
	
	private static volatile YoumiNativeAdDownloadManager sInstance;
	
	private OnYoumiNativeAdDownloadListenerTransform mListenerTransform = new OnYoumiNativeAdDownloadListenerTransform();
	
	private DefaultDownloadListener mPublisher;
	
	private YoumiNativeAdDownloadManager() {
		super();
	}
	
	public static YoumiNativeAdDownloadManager getInstance() {
		if (sInstance == null) {
			synchronized (YoumiNativeAdDownloadManager.class) {
				if (sInstance == null) {
					sInstance = new YoumiNativeAdDownloadManager();
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
	
	/**
	 * 下载广告
	 *
	 * @param context            上下文
	 * @param adModel            要下载的广告对象
	 * @param downloadTaskConfig 本次下载的一些配置信息，如是否显示通知栏等
	 *
	 * @return <ul>
	 * <li>true：成功提交下载任务到线程池中异步执行</li>
	 * <li>false：提交下载任务到线程池中异步执行失败</li>
	 * </ul>
	 */
	public boolean download(Context context, YoumiNativeAdModel adModel, DownloadTaskConfig downloadTaskConfig) {
		if (adModel.getAdType() != 0) {
			return false;
		}
		FileDownloadTask fileDownloadTask = new FileDownloadTask(adModel.getUrl());
		fileDownloadTask.addIFileDownloadTask(YoumiNativeAdModel.class.hashCode(), adModel);
		fileDownloadTask.addIFileDownloadTask(DownloadTaskConfig.class.hashCode(), downloadTaskConfig);
		if (downloadTaskConfig.isShowDefaultNotification()) {
			if (mPublisher == null) {
				mPublisher = new DefaultDownloadListener(context.getApplicationContext());
				addOnDownloadListener(mPublisher);
			}
		}
		
		return download(context.getApplicationContext(), fileDownloadTask);
	}
	
	/**
	 * 停止下载
	 *
	 * @param adModel 要停止下载的广告对象
	 */
	public void stopDownload(YoumiNativeAdModel adModel) {
		if (adModel.getAdType() != 0) {
			return;
		}
		FileDownloadTask fileDownloadTask = new FileDownloadTask(adModel.getUrl());
		fileDownloadTask.addIFileDownloadTask(YoumiNativeAdModel.class.hashCode(), adModel);
		stopDownload(fileDownloadTask);
	}
	
	/**
	 * 添加下载监听器
	 *
	 * @param listener 下载监听器对象
	 */
	public void addOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener listener) {
		mListenerTransform.addListener(listener);
		addOnDownloadListener(mListenerTransform);
	}
	
	/**
	 * 移除下载监听器
	 *
	 * @param listener 下载监听器对象
	 */
	public void removeOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener listener) {
		mListenerTransform.removeListener(listener);
		if (mListenerTransform.isEmpty()) {
			removeOnDownloadListener(mListenerTransform);
		}
	}
	
}