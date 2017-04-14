package net.youmi.ads.nativead.adrequest;

/**
 * 原生广告请求监听器
 *
 * @author zhitao
 * @since 2017-04-13 19:12
 */
public interface OnYoumiNativeAdRequestListener {
	
	/**
	 * 请求结束（本回调执行在UI线程中）
	 * <p>
	 * 注意：这里只是请求结束，是否请求成功，返回数据是否正常还需要开发者针对返回结果做判断
	 *
	 * @param respModel 返回的请求结果
	 */
	void onRequestFinish(YoumiNativeAdResposeModel respModel);
	
}
