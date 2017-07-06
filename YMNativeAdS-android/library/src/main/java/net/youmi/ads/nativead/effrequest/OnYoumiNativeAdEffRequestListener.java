package net.youmi.ads.nativead.effrequest;

import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

/**
 * 效果记录发送结果回调监听接口
 *
 * @author zhitao
 * @since 2017-07-06 15:49
 */
public interface OnYoumiNativeAdEffRequestListener {
	
	/**
	 * 效果记录发送完毕时回调(本回调方法执行在UI线程中)
	 *
	 * @param isSuccess 是否所有对应的效果记录都发送成功
	 * @param adModel   效果记录对应的广告信息对象
	 */
	void onEffRequestFinish(boolean isSuccess, YoumiNativeAdModel adModel);
	
}
