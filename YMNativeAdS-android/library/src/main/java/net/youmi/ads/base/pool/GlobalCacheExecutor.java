package net.youmi.ads.base.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhitao
 * @since 2017-04-13 18:35
 */
public class GlobalCacheExecutor {
	
	private static ExecutorService sCachedThreadTool;
	
	public static void execute(Runnable runnable) {
		if (sCachedThreadTool == null) {
			sCachedThreadTool = Executors.newCachedThreadPool();
		}
		sCachedThreadTool.execute(runnable);
	}
}
