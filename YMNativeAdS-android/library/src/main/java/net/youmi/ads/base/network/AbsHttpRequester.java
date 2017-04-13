package net.youmi.ads.base.network;

import android.content.Context;

import net.youmi.ads.base.log.DLog;

/**
 * @author zhitao
 * @since 2017-04-13 15:35
 */
abstract class AbsHttpRequester {
	
	protected Context mApplicationContext;
	
	/**
	 * 原始请求数据模型
	 */
	protected BaseHttpResponseModel mBaseHttpResponseModel;
	
	/**
	 * 请求结果数据模型
	 */
	protected BaseHttpRequesterModel mBaseHttpRequesterModel;
	
	/**
	 * @param context                上下文
	 * @param baseHttpRequesterModel 本次请求的相关参数的自定义数据模型
	 *
	 * @throws NullPointerException
	 */
	public AbsHttpRequester(Context context, BaseHttpRequesterModel baseHttpRequesterModel) throws NullPointerException {
		if (context == null || baseHttpRequesterModel == null) {
			throw new NullPointerException();
		}
		mApplicationContext = context.getApplicationContext();
		mBaseHttpRequesterModel = baseHttpRequesterModel;
		mBaseHttpResponseModel = new BaseHttpResponseModel();
	}
	
	/**
	 * 获取请求数据
	 *
	 * @return
	 */
	public BaseHttpRequesterModel getBaseHttpRequesterModel() {
		return mBaseHttpRequesterModel;
	}
	
	/**
	 * 获取请求完毕之后的相关数据
	 *
	 * @return 返回的对象是绝对不为空的，所以判断本对象是否为空是没用的
	 */
	public BaseHttpResponseModel getBaseHttpResponseModel() {
		return mBaseHttpResponseModel;
	}
	
	/**
	 * 执行网络请求
	 */
	public synchronized void request() {
		
		DLog.i("======请求信息======\n%s", mBaseHttpRequesterModel.toString());
		long startTime = System.currentTimeMillis();
		mBaseHttpResponseModel.setStartRequestTimestamp_ms(startTime);
		
		newHttpRequest();   // 执行http请求，子类具体实现
		
		long endTime = System.currentTimeMillis();
		mBaseHttpResponseModel.setResponseTimestamp_ms(endTime);
		mBaseHttpResponseModel.setTotalTimes_ms(endTime - startTime);
		
		DLog.i("======返回信息======\n%s", mBaseHttpResponseModel.toString());
	}
	
	/**
	 * 执行http请求
	 */
	protected abstract void newHttpRequest();
	
	/**
	 * 舍弃当前的请求，比如如果正在读取InputStream的话，调用本方法之后就会停止
	 * <p/>
	 * 使用场合：
	 * 如果短时间内发起很多次相同的请求，那么我们可以在发起一个新的请求的时候舍弃当前的请求
	 */
	@Deprecated
	public abstract void abort();
	
	/**
	 * 将http请求中的异常赋值到请求结果数据模型中
	 *
	 * @param exceptionCode 异常错误代表码 {@link BaseHttpErrorCode}
	 * @param e             异常错误详细信息
	 */
	protected void setException(int exceptionCode, Exception e) {
		if (mBaseHttpResponseModel != null) {
			mBaseHttpResponseModel.setClientException(exceptionCode);
			mBaseHttpResponseModel.setException(e);
		}
	}
}
