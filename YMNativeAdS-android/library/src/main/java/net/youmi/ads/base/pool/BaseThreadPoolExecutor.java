package net.youmi.ads.base.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhitao
 * @since 2017-04-14 20:15
 */
public class BaseThreadPoolExecutor extends ThreadPoolExecutor {
	
	private IExecuteListener mIExecuteListener;
	
	public BaseThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}
	
	public void setIExecuteListener(IExecuteListener iExecuteListener) {
		mIExecuteListener = iExecuteListener;
	}
	
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		if (mIExecuteListener != null) {
			mIExecuteListener.beforeExecute(t, r);
		}
		super.beforeExecute(t, r);
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		if (mIExecuteListener != null) {
			mIExecuteListener.afterExecute(r, t);
		}
	}
}