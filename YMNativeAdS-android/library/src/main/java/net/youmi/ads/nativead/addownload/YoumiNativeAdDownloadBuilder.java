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
	public YoumiNativeAdDownloadBuilder showDownloadNotification(boolean isShowDefaultNotification) {
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
	public YoumiNativeAdDownloadBuilder installApkAfterDownloadSuccess(boolean isNeed2StartInstallAfterDownloadSuccess) {
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
	 * 安装广告完毕之后是否立即删除广告APk文件
	 * <p>
	 * 注意：
	 * <p>
	 * 此方法需要设置安装成功后打开广告应用的方法才生效，即 {@link #startAppAfterInstalled(boolean)} 方法被调用了且传入 {@code true}才生效
	 *
	 * @param isNeed2DeleteApkAfterInstalled <ul>
	 *                                       <li>true（默认）：立即删除</li>
	 *                                       <li>false：不删除，一段时候后，sdk也会自动删除</li>
	 *                                       </ul>
	 *
	 * @return this
	 *
	 * @see #startAppAfterInstalled(boolean)
	 */
	public YoumiNativeAdDownloadBuilder deleteApkAfterInstalled(boolean isNeed2DeleteApkAfterInstalled) {
		downloadTaskConfig.setNeed2DeleteApkAfterInstalled(isNeed2DeleteApkAfterInstalled);
		return this;
	}
	
	/**
	 * 广告下载成功之后是否自动发送下载成功效果记录
	 *
	 * @param isNeed2SendDownloadSuccessEff <ul>
	 *                                      <li>true（默认）：是</li>
	 *                                      <li>false：否</li>
	 *                                      </ul>
	 *
	 * @return this
	 */
	public YoumiNativeAdDownloadBuilder sendDownloadSuccessEff(boolean isNeed2SendDownloadSuccessEff) {
		downloadTaskConfig.setNeed2SendDownloadSuccessEff(isNeed2SendDownloadSuccessEff);
		return this;
	}
	
	/**
	 * 广告安装成功之后是否自动发送安装成功效果记录
	 *
	 * @param isNeed2SendInstallSuccessEff <ul>
	 *                                     <li>true（默认）：是</li>
	 *                                     <li>false：否</li>
	 *                                     </ul>
	 *
	 * @return this
	 */
	public YoumiNativeAdDownloadBuilder sendInstallSuccessEff(boolean isNeed2SendInstallSuccessEff) {
		downloadTaskConfig.setNeed2SendInstallSuccessEff(isNeed2SendInstallSuccessEff);
		return this;
	}
	
	/**
	 * 下载广告
	 */
	public void download() {
		YoumiNativeAdDownloadManager.getInstance().download(applicationContext, adModel, downloadTaskConfig);
	}
	
}
