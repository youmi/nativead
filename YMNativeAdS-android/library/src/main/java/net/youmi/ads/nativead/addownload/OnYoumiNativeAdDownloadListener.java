package net.youmi.ads.nativead.addownload;

import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

/**
 * @author zhitao
 * @since 2017-04-14 14:59
 */
public interface OnYoumiNativeAdDownloadListener {
	
	/**
	 * 下载开始通知，在UI线程中执行
	 *
	 * @param adModel 下载任务对象
	 */
	void onDownloadStart(YoumiNativeAdModel adModel);
	
	/**
	 * 下载进度变更通知，在UI线程中执行
	 *
	 * @param adModel           下载任务对象
	 * @param totalLength       下载总长度
	 * @param completeLength    当前已经下载完成的长度
	 * @param downloadPercent   下载百分比
	 * @param downloadBytesPerS 下载速度(B/s)
	 */
	void onDownloadProgressUpdate(YoumiNativeAdModel adModel, long totalLength, long completeLength, int downloadPercent,
			long downloadBytesPerS);
	
	/**
	 * 下载成功通知，在UI线程中执行
	 *
	 * @param adModel 下载任务对象
	 */
	void onDownloadSuccess(YoumiNativeAdModel adModel);
	
	/**
	 * 下载失败通知，在UI线程中执行
	 *
	 * @param adModel 下载任务对象
	 */
	void onDownloadFailed(YoumiNativeAdModel adModel);
	
	/**
	 * 下载停止通知，在UI线程中执行
	 *
	 * @param adModel 下载任务对象
	 */
	void onDownloadStop(YoumiNativeAdModel adModel);
}
