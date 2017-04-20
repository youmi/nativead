package net.youmi.ads.base.network;

import android.content.Context;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author zhitao
 * @since 2017-04-13 15:42
 */
class HttpURLConnectionRequester extends AbsHttpRequester {
	
	protected HttpURLConnection mHttpURLConnection;
	
	protected boolean mIsRunning;
	
	public HttpURLConnectionRequester(Context context, BaseHttpRequesterModel baseHttpRequesterModel)
			throws NullPointerException {
		super(context, baseHttpRequesterModel);
	}
	
	/**
	 * 舍弃当前的请求，比如如果正在读取InputStream的话，调用本方法之后就会停止
	 * <p/>
	 * 使用场合：
	 * 如果短时间内发起很多次相同的请求，那么我们可以在发起一个新的请求的时候舍弃当前的请求
	 */
	@Override
	public void abort() {
		//		mIsRunning = false;
		//		//　标识请求已经舍弃了
		//		mBaseHttpResponseModel.setIsFinishResponse(mIsRunning);
		//
		//		try {
		//			mHttpURLConnection.disconnect();
		//		} catch (Throwable e) {
		//			DLog.e(e);
		//		}
		
	}
	
	@Override
	protected void newHttpRequest() {
		
		InputStream inputStream;
		ByteArrayOutputStream baos = null;
		mIsRunning = true;
		//　一开始就标识请求是能正常跑的，而不是被主动打断的
		mBaseHttpResponseModel.setIsFinishResponse(mIsRunning);
		try {
			
			// 创建默认的HttpURLConnection
			mHttpURLConnection =
					HttpRequesterFactory.newHttpURLConnection(mApplicationContext, mBaseHttpRequesterModel.getRequestUrl());
			
			if (mHttpURLConnection == null) {
				throw new NullPointerException();
			}
			
			// 添加额外的http请求头部数据
			try {
				ArrayList<BaseHttpRequesterModel.Header> headers = mBaseHttpRequesterModel.getHeaders();
				if (headers != null && !headers.isEmpty()) {
					for (BaseHttpRequesterModel.Header header : headers) {
						mHttpURLConnection.addRequestProperty(header.key, header.value);
					}
				}
			} catch (Throwable e) {
				DLog.e(e);
			}
			
			// 设置请求方式 GET OR POST OR OTHER
			mHttpURLConnection.setRequestMethod(mBaseHttpRequesterModel.getRequestType());
			
			// 根据请求参数设置与请求类型相关的其他参数
			// get
			if (BaseHttpRequesterModel.REQUEST_TYPE_GET.equals(mBaseHttpRequesterModel.getRequestType())) {
				
			}
			// post
			else if (BaseHttpRequesterModel.REQUEST_TYPE_POST.equals(mBaseHttpRequesterModel.getRequestType())) {
				
				// 设置是否向httpUrlConnection输出，默认情况下是false。使用httpUrlConnection.getOutputStream()，把内容输出到远程服务器上。
				mHttpURLConnection.setDoOutput(true);
				
				OutputStream os = mHttpURLConnection.getOutputStream();
				
				// 优先以NameValuePair的post请求
				if (mBaseHttpRequesterModel.getPostDataMap() != null && !mBaseHttpRequesterModel.getPostDataMap().isEmpty()) {
					
					StringBuilder sb = new StringBuilder();
					for (Map.Entry<String, String> entry : mBaseHttpRequesterModel.getPostDataMap().entrySet()) {
						sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
					}
					String params = sb.toString().substring(0, sb.length() - 1);
					String afterURLEncode = URLEncoder.encode(params, "UTF-8");
					if (DLog.isNetLog) {
						DLog.i("[POST]原始请求参数:%s urlencode后:%s", params, afterURLEncode);
					}
					os.write(afterURLEncode.getBytes(mBaseHttpRequesterModel.getEncodingCharset()));
					os.flush();
				}
				
				// 然后才是二进制的post data
				else if (mBaseHttpRequesterModel.getPostDataByteArray() != null &&
				         mBaseHttpRequesterModel.getPostDataByteArray().length > 0) {
					os.write(mBaseHttpRequesterModel.getPostDataByteArray());
					os.flush();
				}
				
				if (os != null) {
					os.close();
				}
			}
			
			// 如果在发起请求之前就舍弃的话就不发起请求
			if (!mIsRunning) {
				return;
			}
			if (mHttpURLConnection.getResponseCode() >= 200 && mHttpURLConnection.getResponseCode() < 300) {
				inputStream = mHttpURLConnection.getInputStream();
			} else {
				inputStream = mHttpURLConnection.getErrorStream();
			}
			
			try {
				// 设置httpcode
				mBaseHttpResponseModel.setHttpCode(mHttpURLConnection.getResponseCode());
			} catch (Throwable e) {
				DLog.e(e);
			}
			try {
				// 设置状态信息
				mBaseHttpResponseModel.setHttpReasonPhrase(mHttpURLConnection.getResponseMessage());
			} catch (Throwable e) {
				DLog.e(e);
			}
			try {
				// 设置返回头部信息
				mBaseHttpResponseModel.setHeaders(mHttpURLConnection.getHeaderFields());
			} catch (Throwable e) {
				DLog.e(e);
			}
			
			try {
				// 设置返回结果的contentlength
				mBaseHttpResponseModel.setContentLength(mHttpURLConnection.getContentLength());
			} catch (Throwable e) {
				DLog.e(e);
			}
			
			try {
				// 获取contentEncoding来获取inputStream
				String contentEncoding = mHttpURLConnection.getContentEncoding();
				if (!TextUtils.isEmpty(contentEncoding) && contentEncoding.toLowerCase(Locale.US).contains("gzip") &&
				    inputStream != null) {
					inputStream = new GZIPInputStream(inputStream);
				}
			} catch (Throwable e) {
				DLog.e(e);
			}
			
			if (inputStream == null) {
				throw new NullPointerException();
			}
			
			baos = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int len = 0;
			
			while ((len = inputStream.read(buff)) > 0) {
				if (mIsRunning) {
					baos.write(buff, 0, len);
				} else {
					return;
				}
			}
			baos.flush();
			
			byte[] buffer = baos.toByteArray();
			mBaseHttpResponseModel.setBodyLength(buffer.length);
			
			String rspString = new String(buffer, mBaseHttpRequesterModel.getEncodingCharset());
			mBaseHttpResponseModel.setResponseString(rspString);
			
		} catch (SocketTimeoutException e) {
			// 请求超时
			setException(BaseHttpErrorCode.SocketTimeoutException, e);
			
		} catch (UnknownHostException e) {
			// 网络没有打开或者没有配置网络权限的时候会1抛出这个异常
			setException(BaseHttpErrorCode.UnknownHostException, e);
			
		} catch (SocketException e) {
			// 请求被关闭
			setException(BaseHttpErrorCode.SocketException, e);
			
		} catch (Exception e) {
			// 这里如果有其他的异常的话先统一标记为未知
			setException(BaseHttpErrorCode.UnknownException, e);
		} finally {
			try {
				if (mHttpURLConnection != null) {
					mHttpURLConnection.disconnect();
				}
			} catch (Throwable e) {
				DLog.e(e);
			}
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (Throwable e) {
				DLog.e(e);
			}
		}
		
	}
}
