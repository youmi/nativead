package net.youmi.ads.base.pool;

/**
 * @author zhitao
 * @since 2017-04-14 20:16
 */
public interface IExecuteListener {
	
	void beforeExecute(Thread t, Runnable r);
	
	void afterExecute(Runnable r, Throwable t);
	
}