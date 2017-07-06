package net.youmi.ads.nativead.effrequest;

import android.content.Context;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.network.BaseHttpResponseModel;
import net.youmi.ads.base.network.YoumiHttpRequester;
import net.youmi.ads.base.pool.GlobalCacheExecutor;
import net.youmi.ads.base.utils.UIHandler;
import net.youmi.ads.nativead.adconfig.YoumiSpConfig;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

import java.util.ArrayList;

/**
 * 发送效果记录
 * <p>
 * 支持一定程度上重发，重发过程：
 * <pre>
 *  第1次 第2次 第3次 第4次   第5次
 *     |--|----|------|--------|
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
	
	private int maxRetryTimes = MAX_RETRY_TIMES;
	
	public YoumiNativeAdEffBuilder(Context context) {
		applicationContext = context.getApplicationContext();
	}
	
	/**
	 * 设置发送失败时，重试次数，默认为5次，目前重发过程为
	 * <pre>
	 *  第1次 第2次 第3次 第4次   第5次
	 *     |--|----|------|--------|
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
	@Deprecated
	public void asyncSendShowEff() {
		asyncSendShowEff(null);
	}
	
	/**
	 * [异步]发送曝光效果记录
	 *
	 * @param youmiNativeAdEffRequestListener 效果记录发送结果回调监听接口
	 *
	 * @since 1.4.0
	 */
	public void asyncSendShowEff(final OnYoumiNativeAdEffRequestListener youmiNativeAdEffRequestListener) {
		GlobalCacheExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final boolean isSuccess = syncSendShowEff();
				if (youmiNativeAdEffRequestListener != null) {
					UIHandler.runInUIThread(new Runnable() {
						@Override
						public void run() {
							youmiNativeAdEffRequestListener.onEffRequestFinish(isSuccess, adModel);
						}
					});
				}
			}
		});
	}
	
	/**
	 * [同步]发送曝光效果记录
	 *
	 * @return <ul>
	 * <li>{@code true} : 发送成功</li>
	 * <li>{@code false} : 发送失败</li>
	 * </ul>
	 */
	public boolean syncSendShowEff() {
		if (applicationContext == null || adModel == null || adModel.getShowUrls() == null) {
			return false;
		}
		return sendEff(applicationContext, adModel.getShowUrls());
	}
	
	/**
	 * [异步]发送点击记录效果
	 */
	@Deprecated
	public void asyncSendClickEff() {
		asyncSendClickEff(null);
	}
	
	/**
	 * [异步]发送点击记录效果
	 *
	 * @param youmiNativeAdEffRequestListener 效果记录发送结果回调监听接口
	 *
	 * @since 1.4.0
	 */
	public void asyncSendClickEff(final OnYoumiNativeAdEffRequestListener youmiNativeAdEffRequestListener) {
		GlobalCacheExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final boolean isSuccess = syncSendClickEff();
				if (youmiNativeAdEffRequestListener != null) {
					UIHandler.runInUIThread(new Runnable() {
						@Override
						public void run() {
							youmiNativeAdEffRequestListener.onEffRequestFinish(isSuccess, adModel);
						}
					});
				}
			}
		});
	}
	
	/**
	 * [同步]发送点击记录效果
	 *
	 * @return <ul>
	 * <li>{@code true} : 发送成功</li>
	 * <li>{@code false} : 发送失败</li>
	 * </ul>
	 */
	public boolean syncSendClickEff() {
		if (applicationContext == null || adModel == null || adModel.getClickUrls() == null) {
			return false;
		}
		return sendEff(applicationContext, adModel.getClickUrls());
	}
	
	/**
	 * [异步]发送下载完成记录效果
	 */
	@Deprecated
	public void asyncSendDownloadSuccessEff() {
		asyncSendDownloadSuccessEff(null);
	}
	
	/**
	 * [异步]发送下载完成记录效果
	 *
	 * @param youmiNativeAdEffRequestListener 效果记录发送结果回调监听接口
	 *
	 * @since 1.4.0
	 */
	public void asyncSendDownloadSuccessEff(final OnYoumiNativeAdEffRequestListener youmiNativeAdEffRequestListener) {
		GlobalCacheExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final boolean isSuccess = syncSendDownloadSuccessEff();
				if (youmiNativeAdEffRequestListener != null) {
					UIHandler.runInUIThread(new Runnable() {
						@Override
						public void run() {
							youmiNativeAdEffRequestListener.onEffRequestFinish(isSuccess, adModel);
						}
					});
				}
			}
		});
	}
	
	/**
	 * [同步]发送下载完成效果记录
	 * <p>
	 *
	 * @return <ul>
	 * <li>{@code true} : 发送成功</li>
	 * <li>{@code false} : 发送失败</li>
	 * </ul>
	 */
	public boolean syncSendDownloadSuccessEff() {
		if (applicationContext == null || adModel == null || adModel.getDownloadUrls() == null) {
			return false;
		}
		return sendEff(applicationContext, adModel.getDownloadUrls());
	}
	
	/**
	 * [异步]发送安装完成记录效果
	 */
	@Deprecated
	public void asyncSendInstallSuccessEff() {
		asyncSendInstallSuccessEff(null);
	}
	
	/**
	 * [异步]发送安装完成记录效果
	 *
	 * @param youmiNativeAdEffRequestListener 效果记录发送结果回调监听接口
	 *
	 * @since 1.4.0
	 */
	public void asyncSendInstallSuccessEff(final OnYoumiNativeAdEffRequestListener youmiNativeAdEffRequestListener) {
		GlobalCacheExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final boolean isSuccess = syncSendInstallSuccessEff();
				if (youmiNativeAdEffRequestListener != null) {
					UIHandler.runInUIThread(new Runnable() {
						@Override
						public void run() {
							youmiNativeAdEffRequestListener.onEffRequestFinish(isSuccess, adModel);
						}
					});
				}
			}
		});
	}
	
	/**
	 * [同步]发送安装完成效果记录
	 * <p>
	 *
	 * @return <ul>
	 * <li>{@code true} : 发送成功</li>
	 * <li>{@code false} : 发送失败</li>
	 * </ul>
	 */
	public boolean syncSendInstallSuccessEff() {
		if (applicationContext == null || adModel == null || adModel.getInstallUrls() == null) {
			return false;
		}
		return sendEff(applicationContext, adModel.getInstallUrls());
	}
	
	/**
	 * 发送效果记录
	 * <p>
	 * 支持一定程度上重发，重发过程：
	 * <pre>
	 *  第1次 第2次 第3次 第4次   第5次
	 *     |--|----|------|--------|
	 * </pre>
	 *
	 * @param context 上下文
	 * @param urls    效果记录
	 */
	@Deprecated
	public boolean sendEff(Context context, final ArrayList<String> urls) {
		if (TextUtils.isEmpty(YoumiSpConfig.getAppId(context))) {
			throw new IllegalArgumentException("can not request without appId");
		}
		
		// 是否所有效果就都发送成功
		boolean isAllEffSendSuccess = true;
		
		for (String url : urls) {
			
			// 本条效果记录是否发送成功
			boolean isThisEffSendSuccess = false;
			
			for (int count = 0; count < maxRetryTimes; count++) {
				BaseHttpResponseModel resp = YoumiHttpRequester.httpGet(context.getApplicationContext(), url);
				
				// 可能是网络导致的发送失败，跳过
				if (resp == null) {
					continue;
				}
				
				// 发送成功，跳出
				if (resp.getHttpCode() >= 200 && resp.getHttpCode() < 300) {
					isThisEffSendSuccess = true;
					break;
				}
				
				// 到这里就为发送失败，遇到错误Http Code，静候一段时间后进行下一次重发
				try {
					Thread.sleep(count * 2000);
				} catch (InterruptedException e) {
					DLog.e(e);
				}
			}
			
			// 如果存在一条效果记录发送失败，那么就标识整个效果记录数组发送失败，但是还是会继续发送剩下的记录
			if (!isThisEffSendSuccess) {
				isAllEffSendSuccess = false;
			}
		}
		
		return isAllEffSendSuccess;
	}
}
