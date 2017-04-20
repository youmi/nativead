package net.youmi.ads.base.network;

import net.youmi.ads.base.log.DLog;

import java.util.List;
import java.util.Map;

/**
 * @author zhitao
 * @since 2017-04-13 15:35
 */
public class BaseHttpResponseModel {
	
	/**
	 * Http码
	 */
	private int mHttpCode;
	
	/**
	 * 请求开始时间戳
	 */
	private long mStartRequestTimestamp_ms = -1;
	
	/**
	 * 结果响应时间戳
	 */
	private long mResponseTimestamp_ms = -1;
	
	/**
	 * 本次请求总耗时
	 */
	private long mTotalTimes_ms = -1;
	
	/**
	 * http返回的ReasonPhrase
	 */
	private String mHttpReasonPhrase;
	
	/**
	 * 结果的Header
	 */
	private Map<String, List<String>> mHeaders;
	
	/**
	 * 客户端异常码
	 */
	private int mClientException = BaseHttpErrorCode.NoException;
	
	/**
	 * 详细异常信息
	 */
	private Exception mException;
	
	/**
	 * bodylength长度
	 */
	private long mBodyLength = -1;
	
	/**
	 * contentLength
	 */
	private long mContentLength = -1;
	
	/**
	 * 返回结果字符串
	 */
	private String mResponseString = null;
	
	/**
	 * 是否完成请求结果的接收，因为网络请求支持舍弃，所以可能会有请求结果没有接受完毕的情况
	 */
	private boolean mIsFinishResponse = false;
	
	public int getHttpCode() {
		return mHttpCode;
	}
	
	public void setHttpCode(int httpCode) {
		mHttpCode = httpCode;
	}
	
	public int getClientException() {
		return mClientException;
	}
	
	public void setClientException(int clientException) {
		mClientException = clientException;
	}
	
	public String getHttpReasonPhrase() {
		return mHttpReasonPhrase;
	}
	
	public Map<String, List<String>> getHeaders() {
		return mHeaders;
	}
	
	public void setHeaders(Map<String, List<String>> headers) {
		mHeaders = headers;
	}
	
	public void setHttpReasonPhrase(String httpReasonPhrase) {
		mHttpReasonPhrase = httpReasonPhrase;
	}
	
	public Exception getException() {
		return mException;
	}
	
	public void setException(Exception a) {
		this.mException = a;
	}
	
	public long getTotalTimes_ms() {
		return mTotalTimes_ms;
	}
	
	public void setTotalTimes_ms(long totalTimes_ms) {
		mTotalTimes_ms = totalTimes_ms;
	}
	
	public long getStartRequestTimestamp_ms() {
		return mStartRequestTimestamp_ms;
	}
	
	public void setStartRequestTimestamp_ms(long startRequestTimestamp_ms) {
		mStartRequestTimestamp_ms = startRequestTimestamp_ms;
	}
	
	public long getResponseTimestamp_ms() {
		return mResponseTimestamp_ms;
	}
	
	public void setResponseTimestamp_ms(long responseTimestamp_ms) {
		mResponseTimestamp_ms = responseTimestamp_ms;
	}
	
	public long getBodyLength() {
		return mBodyLength;
	}
	
	public void setBodyLength(long bodyLength) {
		mBodyLength = bodyLength;
	}
	
	public long getContentLength() {
		return mContentLength;
	}
	
	public void setContentLength(long contentLength) {
		mContentLength = contentLength;
	}
	
	public String getResponseString() {
		return mResponseString;
	}
	
	public void setResponseString(String responseString) {
		mResponseString = responseString;
	}
	
	public boolean isFinishResponse() {
		return mIsFinishResponse;
	}
	
	public void setIsFinishResponse(boolean isFinishResponse) {
		mIsFinishResponse = isFinishResponse;
	}
	
	@Override
	public String toString() {
		if (DLog.isNetLog) {
			final StringBuilder sb = new StringBuilder("BaseHttpResponseModel {\n");
			sb.append("  mIsFinishResponse=").append(mIsFinishResponse).append("\n");
			sb.append("  mHttpCode=").append(mHttpCode).append("\n");
			sb.append("  mStartRequestTimestamp_ms=").append(mStartRequestTimestamp_ms).append("\n");
			sb.append("  mResponseTimestamp_ms=").append(mResponseTimestamp_ms).append("\n");
			sb.append("  mTotalTimes_ms=").append(mTotalTimes_ms).append("\n");
			sb.append("  mHttpReasonPhrase=\"").append(mHttpReasonPhrase).append('\"').append("\n");
			sb.append("  mHeaders=").append(mHeaders).append("\n");
			sb.append("  mClientException=").append(mClientException).append("\n");
			sb.append("  mException=").append(mException).append("\n");
			sb.append("  mBodyLength=").append(mBodyLength).append("\n");
			sb.append("  mContentLength=").append(mContentLength).append("\n");
			sb.append("  mResponseString=").append(mResponseString).append("\n");
			sb.append('}');
			return sb.toString();
		}
		return super.toString();
	}
	
}
