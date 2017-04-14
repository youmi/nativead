package net.youmi.ads.nativead;

import android.content.Context;

import net.youmi.ads.nativead.adconfig.YoumiNativeAdConfigBuilder;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdRequesterBuilder;
import net.youmi.ads.nativead.effrequest.YoumiNativeAdEffBuilder;

/**
 * @author zhitao
 * @since 2017-04-14 09:36
 */
public class YoumiNativeAdHelper {
	
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
	 * 设置sdk的一些配置
	 *
	 * @return sdk配置对象
	 */
	public static YoumiNativeAdConfigBuilder initConfig() {
		return new YoumiNativeAdConfigBuilder();
	}
	
}
