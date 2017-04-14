package net.youmi.ads.base.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @author zhitao
 * @since 2017-04-14 11:15
 */
public class UIHandler {
	
	private static Handler sUIHandler = new Handler(Looper.getMainLooper());
	
	public static void runInUIThread(Runnable runnable) {
		if (runnable != null) {
			sUIHandler.post(runnable);
		}
	}
	
}
