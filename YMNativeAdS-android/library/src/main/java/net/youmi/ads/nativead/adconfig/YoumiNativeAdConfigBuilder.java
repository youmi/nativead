package net.youmi.ads.nativead.adconfig;

import net.youmi.ads.base.log.DLog;

/**
 * @author zhitao
 * @since 2017-04-14 09:59
 */
public class YoumiNativeAdConfigBuilder {
	
	public YoumiNativeAdConfigBuilder() {
		
	}
	
	/**
	 * 是否显示调试log
	 *
	 * @param isShowDebugLog 是否显示调试log
	 *
	 * @return this
	 */
	public YoumiNativeAdConfigBuilder showDebugLog(boolean isShowDebugLog) {
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
	public YoumiNativeAdConfigBuilder withTag(String tag) {
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
	public YoumiNativeAdConfigBuilder withClassNameInTag(boolean isWithClassNameInTag) {
		DLog.setIsShowClassNameInTag(isWithClassNameInTag);
		return this;
	}
	
}
