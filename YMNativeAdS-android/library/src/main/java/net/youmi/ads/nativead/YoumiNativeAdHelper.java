package net.youmi.ads.nativead;

import android.content.Context;

import net.youmi.ads.nativead.adconfig.YoumiNativeAdConfigBuilder;
import net.youmi.ads.nativead.addownload.OnYoumiNativeAdDownloadListener;
import net.youmi.ads.nativead.addownload.YoumiNativeAdDownloadBuilder;
import net.youmi.ads.nativead.addownload.YoumiNativeAdDownloadManager;
import net.youmi.ads.nativead.adinstall.AdInstallUtils;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdRequesterBuilder;
import net.youmi.ads.nativead.effrequest.YoumiNativeAdEffBuilder;

/**
 * @author zhitao
 * @since 2017-04-14 09:36
 */
public class YoumiNativeAdHelper {
	
	/**
	 * 设置sdk的一些配置
	 *
	 * @param context 上下文，会自动取ApplicationContext
	 *
	 * @return sdk配置对象
	 */
	public static YoumiNativeAdConfigBuilder initConfig(Context context) {
		return new YoumiNativeAdConfigBuilder(context.getApplicationContext());
	}
	
	/**
	 * 创建一个广告请求
	 *
	 * @param context 上下文，会自动取ApplicationContext
	 *
	 * @return 广告请求对象
	 */
	public static YoumiNativeAdRequesterBuilder newAdRequest(Context context) {
		return new YoumiNativeAdRequesterBuilder(context.getApplicationContext());
	}
	
	/**
	 * 创建一个效果记录发送请求
	 *
	 * @param context 上下文，会自动取ApplicationContext
	 *
	 * @return 效果记录发送对象
	 */
	public static YoumiNativeAdEffBuilder newAdEffRequest(Context context) {
		return new YoumiNativeAdEffBuilder(context.getApplicationContext());
	}
	
	/**
	 * 创建一个广告下载任务
	 *
	 * @param context 上下文，会自动取ApplicationContext
	 *
	 * @return 广告下载任务对象
	 */
	public static YoumiNativeAdDownloadBuilder newAdDownload(Context context) {
		return new YoumiNativeAdDownloadBuilder(context.getApplicationContext());
	}
	
	/**
	 * 打开APP类型的广告
	 *
	 * @param context 上下文，会自动取ApplicationContext
	 * @param adModel 要打开的广告
	 */
	public static void openApp(Context context, YoumiNativeAdModel adModel) {
		Context applicationContext = context.getApplicationContext();
		if (adModel == null) {
			return;
		}
		
		if (adModel.getAdType() != 0) {
			return;
		}
		
		AdInstallUtils.startAd(applicationContext, adModel.getAppModel().getPackageName(), adModel.getUri());
		
	}
	
	/**
	 * 停止下载
	 *
	 * @param adModel 要停止下载的广告对象
	 */
	public static void stopDownload(YoumiNativeAdModel adModel) {
		YoumiNativeAdDownloadManager.getInstance().stopDownload(adModel);
	}
	
	/**
	 * 注册下载监听器，注意配对使用，有注册就得有注销
	 * <p>
	 * e.g.
	 * <pre>
	 *     protected void onCreate(Bundle savedInstanceState) {
	 *         YoumiNativeAdHelper.registerOnYoumiNativeAdDownloadListener(this);
	 *     }
	 *
	 *     protected void onDestroy() {
	 *         YoumiNativeAdHelper.removeOnYoumiNativeAdDownloadListener(this);
	 *     }
	 * </pre>
	 *
	 * @param listener 下载监听器
	 *
	 * @see #removeOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener)
	 */
	public static void addOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener listener) {
		YoumiNativeAdDownloadManager.getInstance().addOnYoumiNativeAdDownloadListener(listener);
	}
	
	/**
	 * 注销下载监听器，注意配对使用，有注册就得有注销
	 * <p>
	 * e.g.
	 * <pre>
	 *     protected void onCreate(Bundle savedInstanceState) {
	 *         YoumiNativeAdHelper.registerOnYoumiNativeAdDownloadListener(this);
	 *     }
	 *
	 *     protected void onDestroy() {
	 *         YoumiNativeAdHelper.removeOnYoumiNativeAdDownloadListener(this);
	 *     }
	 * </pre>
	 *
	 * @param listener 下载监听器
	 *
	 * @see #addOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener)
	 */
	public static void removeOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener listener) {
		YoumiNativeAdDownloadManager.getInstance().removeOnYoumiNativeAdDownloadListener(listener);
	}
	
}
