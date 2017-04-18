package net.youmi.ads.base.network;

import net.youmi.ads.base.log.DLog;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @author zhitao
 * @since 2017-04-13 15:35
 */
public class BaseHttpRequesterModel {
	
	public final static String REQUEST_TYPE_GET = "GET";
	
	public final static String REQUEST_TYPE_POST = "POST";
	
	/**
	 * 请求url
	 */
	protected String mRequestUrl;
	
	/**
	 * 请求类型，post和get
	 */
	protected String mRequestType;
	
	/**
	 * 请求头
	 */
	protected ArrayList<Header> mHeaders;
	
	/**
	 * post data , 如果为空，看看是不是需要二进制post，如果都不用就，就使用get方法请求
	 */
	protected Map<String, String> mPostDataMap;
	
	/**
	 * post data , post二进制数据
	 */
	protected byte[] mPostDataByteArray;
	
	/**
	 * 编码格式
	 */
	private String mEncodingCharset;
	
	public void setRequestUrl(String requestUrl) {
		mRequestUrl = requestUrl;
	}
	
	public String getRequestUrl() {
		return mRequestUrl;
	}
	
	/**
	 * 设置请求类型
	 *
	 * @param mRequestType {@link #REQUEST_TYPE_GET} or {@link #REQUEST_TYPE_POST} or "delete" or ....
	 */
	public void setRequsetType(String mRequestType) {
		this.mRequestType = mRequestType;
	}
	
	/**
	 * @return 如果没有调用set方法，那么默认是get 请求
	 */
	public String getRequestType() {
		if (mRequestType == null) {
			return REQUEST_TYPE_GET;
		}
		return mRequestType;
	}
	
	/**
	 * 设置请求头
	 *
	 * @param headers 请求头
	 */
	public void setHeaders(ArrayList<Header> headers) {
		mHeaders = headers;
	}
	
	public ArrayList<Header> getHeaders() {
		return mHeaders;
	}
	
	/**
	 * 设置post 二进制数据
	 *
	 * @param bytes 二进制数据
	 */
	public void setPostDataByteArray(byte[] bytes) {
		mPostDataByteArray = bytes;
	}
	
	public byte[] getPostDataByteArray() {
		return mPostDataByteArray;
	}
	
	/**
	 * 设置post NameValuePair 数据
	 *
	 * @param postDataMap NameValuePair
	 */
	public void setPostDataMap(Map<String, String> postDataMap) {
		mPostDataMap = postDataMap;
	}
	
	public Map<String, String> getPostDataMap() {
		return mPostDataMap;
	}
	
	/**
	 * 设置请求编码
	 *
	 * @param encoding 请求编码
	 */
	public void setEncodingCharset(String encoding) {
		mEncodingCharset = encoding;
	}
	
	public String getEncodingCharset() {
		if (mEncodingCharset == null) {
			return "UTF-8";
		}
		return mEncodingCharset;
	}
	
	//	/**
	//	 * 通过请求获取服务器IP[这是一个耗时的方法]
	//	 *
	//	 * @return 形如下面的格式 "127.0.0.1;...,...,..." or null
	//	 */
	//	private String request4HostIp() {
	//		return NetworkUtil.request4HostIp(mRequestUrl);
	//	}
	
	public String getHostString() {
		try {
			return new URI(mRequestUrl).getHost();
		} catch (Exception e) {
			DLog.e(e);
		}
		return "";
	}
	
	public String getPathString() {
		try {
			return new URI(mRequestUrl).getRawPath();
		} catch (Exception e) {
			DLog.e(e);
		}
		return "";
		
	}
	
	public String getQueryString() {
		try {
			return new URI(mRequestUrl).getRawQuery();
		} catch (Exception e) {
			DLog.e(e);
		}
		return "";
	}
	
	@Override
	public String toString() {
		
		try {
			final StringBuilder sb = new StringBuilder("BaseHttpRequesterModel {\n");
			sb.append("  mRequestUrl=\"").append(mRequestUrl).append('\"').append("\n");
			sb.append("  mRequestType=\"").append(mRequestType).append('\"').append("\n");
			sb.append("  mHeaders=").append(mHeaders).append("\n");
			sb.append("  mPostDataMap=").append(mPostDataMap).append("\n");
			sb.append("  mPostDataByteArray=").append(Arrays.toString(mPostDataByteArray)).append("\n");
			sb.append("  mEncodingCharset=\"").append(mEncodingCharset).append('\"').append("\n");
			sb.append("  getHostString()=\"").append(getHostString()).append('\"').append("\n");
			sb.append("  getPathString()=\"").append(getPathString()).append('\"').append("\n");
			sb.append("  getQueryString()=\"").append(getQueryString()).append('\"').append("\n");
			sb.append('}');
			return sb.toString();
		} catch (Exception e) {
			DLog.e(e);
		}
		
		return super.toString();
	}
	
	public static class Header {
		
		public String key;
		
		public String value;
		
		public Header(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("Header{");
			sb.append("\n  key='").append(key).append('\'');
			sb.append("\n  value='").append(value).append('\'');
			sb.append("\n}");
			return sb.toString();
		}
	}
}
