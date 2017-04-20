package net.youmi.ads.base.pool;

import net.youmi.ads.base.log.DLog;

import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zhitao
 * @since 2017-04-14 20:14
 */
public class BaseCacheExecutorService {
	
	private BaseThreadPoolExecutor mExecutorService;
	
	private BaseThreadFactory mBaseThreadFactory;
	
	public BaseCacheExecutorService(BaseThreadPoolExecutor executorService, BaseThreadFactory baseThreadFactory) {
		mExecutorService = executorService;
		mBaseThreadFactory = baseThreadFactory;
	}
	
	public BaseCacheExecutorService(String poolName) {
		mBaseThreadFactory = new BaseThreadFactory(Thread.NORM_PRIORITY, poolName);
		mExecutorService = new BaseThreadPoolExecutor(0,
				Integer.MAX_VALUE,
				60L,
				TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(),
				mBaseThreadFactory
		);
	}
	
	public BaseCacheExecutorService() {
		this("Global");
	}
	
	/**
	 * 设置每个任务执行前后需要做的逻辑
	 *
	 * @param iExecuteListener 具体逻辑器
	 *
	 * @return this
	 */
	public BaseCacheExecutorService withIExecuteListener(IExecuteListener iExecuteListener) {
		if (mExecutorService != null) {
			mExecutorService.setIExecuteListener(iExecuteListener);
		}
		return this;
	}
	
	public void execute(Runnable runnable) {
		try {
			mExecutorService.execute(runnable);
		} catch (Exception e) {
			DLog.e(e);
		}
	}
	
	/**
	 * shutdownNow会停止我们继续向线程池添加任务，同时会将还没有执行的任务列表抛出来不予以执行，会尝试终止正在执行的任务，但是不会等待起终止了才返回
	 *
	 * @return 还没有执行的任务
	 */
	public List<Runnable> shutdownNow() {
		try {
			return mExecutorService.shutdownNow();
		} catch (Exception e) {
			DLog.e(e);
			return null;
		}
	}
	
	/**
	 * 中断所有阻塞中的线程
	 */
	public void interrupt() {
		try {
			mBaseThreadFactory.interrupt();
		} catch (Exception e) {
			DLog.e(e);
		}
	}
}