package net.youmi.ads.base.network;

import android.content.Context;

import net.youmi.ads.base.log.DLog;

import java.util.ArrayList;

/**
 * @author zhitao
 * @since 2017-04-13 15:37
 */
public class YoumiHttpRequester {
	
	/**
	 * 发起Get请求
	 *
	 * @param context 上下文
	 * @param url     请求url 如果是带有中文信息的url，请先urlencode
	 *
	 * @return 请求返回的原始字符串
	 */
	public static String httpGetForString(Context context, String url) {
		return httpGetForString(context, url, null);
	}
	
	/**
	 * 发起Get请求
	 *
	 * @param context 上下文
	 * @param url     请求url 如果是带有中文信息的url，请先urlencode
	 * @param headers Header
	 *
	 * @return 请求返回的原始字符串
	 */
	public static String httpGetForString(Context context, String url, ArrayList<BaseHttpRequesterModel.Header> headers) {
		try {
			BaseHttpResponseModel resp = httpGet(context, url, headers);
			if (resp == null) {
				return null;
			}
			// 判断请求结果
			if (resp.getResponseString() != null) {
				return resp.getResponseString();
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		
		return null;
	}
	
	/**
	 * 发起Get请求
	 *
	 * @param context 上下文
	 * @param url     请求url 如果是带有中文信息的url，请先urlencode
	 *
	 * @return 返回详细信息
	 */
	public static BaseHttpResponseModel httpGet(Context context, String url) {
		return httpGet(context, url, null);
	}
	
	/**
	 * 发起Get请求
	 *
	 * @param context 上下文
	 * @param url     请求url 如果是带有中文信息的url，请先urlencode
	 * @param headers Header
	 *
	 * @return 返回详细信息
	 */
	public static BaseHttpResponseModel httpGet(Context context, String url, ArrayList<BaseHttpRequesterModel.Header> headers) {
		try {
			
			BaseHttpRequesterModel baseHttpRequesterModel = new BaseHttpRequesterModel();
			baseHttpRequesterModel.setRequestUrl(url);
			baseHttpRequesterModel.setRequsetType(BaseHttpRequesterModel.REQUEST_TYPE_GET);
			baseHttpRequesterModel.setHeaders(headers);
			baseHttpRequesterModel.setEncodingCharset("UTF-8");
			
			AbsHttpRequester requester = new HttpURLConnectionRequester(context, baseHttpRequesterModel);
			requester.request();
			return requester.getBaseHttpResponseModel();
			
		} catch (Throwable e) {
			DLog.e(e);
		}
		
		return null;
	}
}
