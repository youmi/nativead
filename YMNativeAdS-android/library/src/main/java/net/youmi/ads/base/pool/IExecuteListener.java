package net.youmi.ads.base.pool;

/**
 * @author zhitao
 * @since 2017-04-14 20:16
 */
public interface IExecuteListener {
	
	/**
	 * 在线程池执行每个任务之前的额外操作
	 */
	void beforeExecute(Thread t, Runnable r);
	
	/**
	 * 在线程池执行完每个任务之后的额外操作
	 */
	void afterExecute(Runnable r, Throwable t);
	
}