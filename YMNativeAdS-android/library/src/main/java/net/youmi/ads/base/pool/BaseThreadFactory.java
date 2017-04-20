package net.youmi.ads.base.pool;

import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhitao
 * @since 2017-04-14 20:17
 */
public class BaseThreadFactory implements ThreadFactory {
	
	/**
	 * 用来输出一下当前究竟用了多少个线程工厂
	 */
	protected static AtomicInteger mPoolCounter = new AtomicInteger(0);
	
	/**
	 * 用来输出一下当前线程工厂已经创建了的线程数
	 */
	protected AtomicInteger mThreadCounter = new AtomicInteger(0);
	
	protected ThreadGroup mThreadGroup;
	
	protected String mPoolName;
	
	protected int mThreadPriority;
	
	/**
	 * @param threadPriority 线程优先级 一般为 Thread.NORM_PRIORITY - 1 就可以
	 * @param poolName       线程工厂名字，如ImageDownload
	 */
	public BaseThreadFactory(int threadPriority, String poolName) {
		if (TextUtils.isEmpty(poolName)) {
			mPoolName = "default" + mPoolCounter.get();
		} else {
			mPoolName = poolName;
		}
		mThreadPriority = threadPriority;
		mThreadGroup = new ThreadGroup(mPoolName);
		mPoolCounter.getAndIncrement();
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread thread = null;
		String threadName = null;
		try {
			
			threadName = String.format(Locale.getDefault(), "%s-%d", mPoolName, mThreadCounter.incrementAndGet());
			
			// 线程所在组，runable，线程名字，stacksize
			thread = new Thread(mThreadGroup, r, threadName, 0);
			if (thread.isDaemon()) {
				thread.setDaemon(false);
			}
			thread.setPriority(mThreadPriority);
		} catch (Exception e) {
			DLog.e(e);
		}
		return thread;
	}
	
	/**
	 * 只能将阻塞中的线程中断，并不能停止线程组中的所有线程的运行
	 */
	public void interrupt() {
		mThreadGroup.interrupt();
	}
	
	/**
	 * @return 线程池名字
	 */
	public String getPoolName() {
		return mPoolName;
	}
	
	/**
	 * @return 当前已经创建了多少个线程工厂
	 */
	public static int getTotalPoolNumber() {
		return mPoolCounter.get();
	}
}