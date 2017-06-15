package net.youmi.ads.nativead.demo;

import android.app.Application;

import net.youmi.ads.nativead.YoumiNativeAdHelper;

/**
 * @author zhitao
 * @since 2017-04-14 11:33
 */
public class MainApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// 初始化sdk的一些配置，如：appId
		YoumiNativeAdHelper.initConfig(this).withAppId(BuildConfig.APPID);
	}
}
