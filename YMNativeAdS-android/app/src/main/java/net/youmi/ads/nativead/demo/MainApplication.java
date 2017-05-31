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
		
		// [可选]初始化sdk的一些配置
		// 默认情况下是不会开启调试log的，因为是demo，所以我们开一下
		// 1. 开启调试Log
		// 2. 设置调试Log的Tag为YoumiSdk
		//
		YoumiNativeAdHelper.initConfig(this).withAppId(BuildConfig.APPID);
	}
}
