package net.youmi.ads.nativead.adconfig;

import android.content.Context;

import net.youmi.ads.base.log.DLog;

/**
 * @author zhitao
 * @since 2017-04-14 09:59
 */
public class YoumiNativeAdConfigBuilder {
	
	private Context applicationContext;
	
	public YoumiNativeAdConfigBuilder(Context context) {
		applicationContext = context.getApplicationContext();
	}
	
	/**
	 * 设置APPID
	 *
	 * @param appId APPID
	 *
	 * @return this
	 */
	public YoumiNativeAdConfigBuilder withAppId(String appId) {
		YoumiSpConfig.setAppId(applicationContext, appId);
		return this;
	}
	
	/**
	 * 是否显示调试log
	 *
	 * @param isShowDebugLog 是否显示调试log
	 *
	 * @return this
	 */
	@Deprecated
	YoumiNativeAdConfigBuilder showDebugLog(boolean isShowDebugLog) {
		DLog.setIsDebug(isShowDebugLog);
		return this;
	}
	
	/**
	 * 设置调试Log的Tag
	 *
	 * @param tag Log的Tag
	 *
	 * @return this
	 */
	@Deprecated
	YoumiNativeAdConfigBuilder withTag(String tag) {
		DLog.setTag(tag);
		return this;
	}
	
	/**
	 * 打印调试Log的时候是否在TAG中打印LOG所在类
	 *
	 * @param isWithClassNameInTag 打印调试Log的时候是否在TAG中打印LOG所在类
	 *
	 * @return this
	 *
	 * @see #showDebugLog(boolean)
	 */
	@Deprecated
	YoumiNativeAdConfigBuilder withClassNameInTag(boolean isWithClassNameInTag) {
		DLog.setIsShowClassNameInTag(isWithClassNameInTag);
		return this;
	}
	
}
