package net.youmi.ads.nativead.addownload;

import android.content.Context;

import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

/**
 * @author zhitao
 * @since 2017-04-19 17:27
 */
public class YoumiNativeAdDownloadBuilder {
	
	private Context applicationContext;
	
	private YoumiNativeAdModel adModel;
	
	private DownloadTaskConfig downloadTaskConfig = new DownloadTaskConfig();
	
	public YoumiNativeAdDownloadBuilder(Context context) {
		applicationContext = context.getApplicationContext();
	}
	
	/**
	 * 要下载的广告
	 *
	 * @param adModel 要下载的广告
	 *
	 * @return this
	 */
	public YoumiNativeAdDownloadBuilder withYoumiNativeAdModel(YoumiNativeAdModel adModel) {
		this.adModel = adModel;
		return this;
	}
	
	/**
	 * 下载过程中是否显示默认的通知栏提示
	 *
	 * @param isShowDefaultNotification <ul>
	 *                                  <li>true（默认）：下载过程中将显示sdk自带默认的通知栏提示</li>
	 *                                  <li>false：下载过程中将不显示sdk自带默认的通知栏提示</li>
	 *                                  </ul>
	 *
	 * @return this
	 */
	public YoumiNativeAdDownloadBuilder withDefaultDownloadNotification(boolean isShowDefaultNotification) {
		downloadTaskConfig.setShowDefaultNotification(isShowDefaultNotification);
		return this;
	}
	
	/**
	 * 下载完毕后是否需要立即安装广告
	 *
	 * @param isNeed2StartInstallAfterDownloadSuccess <ul>
	 *                                                <li>true：下载完毕后立即打开广告</li>
	 *                                                <li>false（默认）：下载完毕后不做处理</li>
	 *                                                </ul>
	 *
	 * @return this
	 */
	public YoumiNativeAdDownloadBuilder installAfterDownloadSuccess(boolean isNeed2StartInstallAfterDownloadSuccess) {
		downloadTaskConfig.setNeed2StartInstallAfterDownloadSuccess(isNeed2StartInstallAfterDownloadSuccess);
		return this;
	}
	
	/**
	 * 安装广告完毕之后是否需要立即打开广告应用
	 *
	 * @param isNeed2StartAppAfterInstalled <ul>
	 *                                      <li>true：安装完毕后立即打开广告</li>
	 *                                      <li>false（默认）：安装完毕后不做处理</li>
	 *                                      </ul>
	 *
	 * @return this
	 */
	public YoumiNativeAdDownloadBuilder startAppAfterInstalled(boolean isNeed2StartAppAfterInstalled) {
		downloadTaskConfig.setNeed2OpenAppAfterInstalled(isNeed2StartAppAfterInstalled);
		return this;
	}
	
	/**
	 * 下载广告
	 */
	public void download() {
		YoumiNativeAdDownloadManager.getInstance().download(applicationContext, adModel, downloadTaskConfig);
	}
	
}
