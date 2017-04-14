package net.youmi.ads.nativead.effrequest;

import android.content.Context;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.network.BaseHttpRequesterModel;
import net.youmi.ads.base.network.BaseHttpResponseModel;
import net.youmi.ads.base.network.YoumiHttpRequester;
import net.youmi.ads.base.pool.GlobalCacheExecutor;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

import java.util.ArrayList;

/**
 * 发送效果记录
 * <p>
 * 支持一定程度上重发，重发过程：
 * <pre>
 *  第1次 第2次 第3次    第4次     第5次
 *     |-->|---->|------>|-------->|
 * </pre>
 *
 * @author zhitao
 * @since 2017-04-13 18:28
 */
public class YoumiNativeAdEffBuilder {
	
	/**
	 * 每个请求最大重试请求次数
	 */
	private final static int MAX_RETRY_TIMES = 5;
	
	private Context applicationContext;
	
	private YoumiNativeAdModel adModel;
	
	private String appId;
	
	private int maxRetryTimes = MAX_RETRY_TIMES;
	
	public YoumiNativeAdEffBuilder(Context context) {
		applicationContext = context.getApplicationContext();
	}
	
	/**
	 * 设置AppId
	 *
	 * @param appId appId
	 *
	 * @return this
	 */
	public YoumiNativeAdEffBuilder withAppId(String appId) {
		this.appId = appId;
		return this;
	}
	
	/**
	 * 设置发送失败时，重试次数，目前重发过程为
	 * <pre>
	 *  第1次 第2次 第3次    第4次     第5次
	 *     |-->|---->|------>|-------->|
	 * </pre>
	 *
	 * @param maxReryTimes 最大重试次数
	 *
	 * @return this
	 */
	public YoumiNativeAdEffBuilder withMaxRetryCount(int maxReryTimes) {
		this.maxRetryTimes = maxReryTimes;
		return this;
	}
	
	/**
	 * 要发送的广告
	 *
	 * @param adModel 要发送的广告
	 *
	 * @return this
	 */
	public YoumiNativeAdEffBuilder withYoumiNativeAdModel(YoumiNativeAdModel adModel) {
		this.adModel = adModel;
		return this;
	}
	
	/**
	 * [异步]发送曝光效果记录
	 */
	public void asyncSendShowEff() {
		GlobalCacheExecutor.execute(new Runnable() {
			@Override
			public void run() {
				syncSendShowEff();
			}
		});
	}
	
	/**
	 * [异步]发送点击记录效果
	 */
	public void asyncSendClickeff() {
		GlobalCacheExecutor.execute(new Runnable() {
			@Override
			public void run() {
				asyncSendClickeff();
			}
		});
	}
	
	/**
	 * [同步]发送曝光效果记录
	 */
	public void syncSendShowEff() {
		if (applicationContext == null || adModel == null || adModel.getShowUrls() == null) {
			return;
		}
		sendEff(applicationContext, adModel.getShowUrls());
	}
	
	/**
	 * [同步]发送点击记录效果
	 */
	public void syncSendClickeff() {
		if (applicationContext == null || adModel == null || adModel.getClickUrls() == null) {
			return;
		}
		sendEff(applicationContext, adModel.getClickUrls());
	}
	
	/**
	 * 发送效果记录
	 * <p>
	 * 支持一定程度上重发，重发过程：
	 * <pre>
	 *  第1次 第2次 第3次    第4次     第5次
	 *     |-->|---->|------>|-------->|
	 * </pre>
	 *
	 * @param context 上下文
	 * @param urls    效果记录
	 */
	private void sendEff(Context context, final ArrayList<String> urls) {
		if (TextUtils.isEmpty(appId)) {
			throw new IllegalArgumentException("can not request without appId");
		}
		
		for (String url : urls) {
			try {
				int count = 0;
				while (count < maxRetryTimes) {
					
					ArrayList<BaseHttpRequesterModel.Header> headers = new ArrayList<>();
					headers.add(new BaseHttpRequesterModel.Header("Authorization", "Bearer " + appId));
					
					BaseHttpResponseModel resp = YoumiHttpRequester.httpGet(context.getApplicationContext(), url, headers);
					if (resp.getHttpCode() >= 200 && resp.getHttpCode() < 300) {
						break;
					}
					count++;
					// 如果发送失败，等候一段时间后重新发送
					Thread.sleep(count * 2000);
				}
			} catch (InterruptedException e) {
				DLog.e(e);
			}
		}
	}
}
