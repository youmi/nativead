package net.youmi.ads.base.pool;

/**
 * @author zhitao
 * @since 2017-04-13 18:35
 */
public class GlobalCacheExecutor {
	
	public static BaseCacheExecutorService sExecutorService = new BaseCacheExecutorService();
	
	public static void execute(Runnable runnable) {
		sExecutorService.execute(runnable);
	}
}
