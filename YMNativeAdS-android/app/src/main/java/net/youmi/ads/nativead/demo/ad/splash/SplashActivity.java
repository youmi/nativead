package net.youmi.ads.nativead.demo.ad.splash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.youmi.ads.base.utils.PackageUtils;
import net.youmi.ads.nativead.YoumiNativeAdHelper;
import net.youmi.ads.nativead.adrequest.OnYoumiNativeAdRequestListener;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdResposeModel;
import net.youmi.ads.nativead.demo.MainActivity;
import net.youmi.ads.nativead.demo.R;
import net.youmi.ads.nativead.demo.ad.SlotIdConfig;
import net.youmi.ads.nativead.demo.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 延迟5秒钟打开demo主页，5秒钟内尝试加载原生开屏广告
 *
 * @author zhitao
 * @since 2017-04-14 10:32
 */
public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
	
	private final static int MSG_UPDATE_REMAINING_TIME = 1;
	
	private ImageView mImageView;
	
	private TextView mTipsTextView;
	
	private TextView mRemainingTextView;
	
	private boolean mIsDestroy;
	
	private MyHandler mHandler;
	
	/**
	 * 剩余倒计时，起始位5秒
	 */
	private int mRemainingTimeS = 5;
	
	/**
	 * 广告返回数据模型
	 */
	private YoumiNativeAdModel mYoumiNativeAdModel;
	
	private static class MyHandler extends Handler {
		
		private WeakReference<Activity> mReference;
		
		MyHandler(Activity context) {
			mReference = new WeakReference<>(context);
		}
		
		@Override
		public void handleMessage(Message msg) {
			SplashActivity activity = (SplashActivity) mReference.get();
			if (activity == null) {
				return;
			}
			switch (msg.what) {
			case MSG_UPDATE_REMAINING_TIME:
				activity.mRemainingTimeS -= 1;
				if (activity.mRemainingTimeS <= 0) {
					// 倒计时结束就打开主界面
					activity.startActivity(new Intent(activity, MainActivity.class));
					activity.finish();
				} else {
					// 倒计时没有结束就更新倒计时
					activity.updateRemainTime();
					sendEmptyMessageDelayed(MSG_UPDATE_REMAINING_TIME, 1000);
				}
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash);
		mRemainingTextView = (TextView) findViewById(R.id.tv_splash_remaining_time);
		mRemainingTextView.setOnClickListener(this);
		mTipsTextView = (TextView) findViewById(R.id.tv_splash_msg);
		mImageView = (ImageView) findViewById(R.id.iv_splash_ad);
		mImageView.setOnClickListener(this);
		
		// 延迟一定时间后打开主界面
		mHandler = new MyHandler(this);
		mHandler.sendEmptyMessageDelayed(MSG_UPDATE_REMAINING_TIME, 1000);
		
		// 发起一个原生开屏广告位广告请求
		YoumiNativeAdHelper
				
				// 创建一个原生广告请求
				.newAdRequest(this)
				
				// （必须）指定请求广告位
				.withSlotId(SlotIdConfig.SPLASH_SLOIID)
				
				// （可选）指定请求广告数量，默认为1
				.withRequestCount(1)
				
				// （可选）其他可选配置，具体可以看代码注释
				//	.withAge("18")
				//	.withContentKeyword("游戏")
				//	.withContentTitle("好玩的游戏")
				//	.withGender("M")
				//	.withReqId("123456")
				//	.withUserAgent("username")
				
				// 发起同步请求
				// .request();
				
				// 发起异步请求
				.request(new MyOnYoumiNativeAdRequesterListener(this));
		
	}
	
	/**
	 * 更新倒计时
	 */
	private void updateRemainTime() {
		if (mRemainingTextView.getVisibility() != View.VISIBLE) {
			mRemainingTextView.setVisibility(View.VISIBLE);
		}
		SpannableString ss = new SpannableString(String.format(Locale.getDefault(), "%ds 跳过广告", mRemainingTimeS));
		ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ffa012")), 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		mRemainingTextView.setText(ss);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeMessages(MSG_UPDATE_REMAINING_TIME);
		mIsDestroy = true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_splash_remaining_time:
			mHandler.removeMessages(MSG_UPDATE_REMAINING_TIME);
			startActivity(new Intent(this, MainActivity.class));
			this.finish();
			break;
		case R.id.iv_splash_ad:
			if (mYoumiNativeAdModel == null) {
				break;
			}
			// 点击了图片之后需要发送点击记录
			YoumiNativeAdHelper.newAdEffRequest(this).withYoumiNativeAdModel(mYoumiNativeAdModel).asyncSendClickEff();
			
			Toast.makeText(
					this,
					String.format(Locale.getDefault(), "发送广告位 %s 的点击记录", mYoumiNativeAdModel.getSlotId()),
					Toast.LENGTH_SHORT
			).show();
			
			// 如果为app广告类型
			// 可以进入自定义的详情页，也可以直接下载
			if (mYoumiNativeAdModel.getAdType() == 0) {
				
				if (!PackageUtils.isPakcageInstall(this, mYoumiNativeAdModel.getAppModel().getPackageName())) {
					
					// 如果广告还没有安装的话，就创建一个广告下载任务
					YoumiNativeAdHelper.newAdDownload(this)
					
					                   // （必须）指定下载的广告
					                   .withYoumiNativeAdModel(mYoumiNativeAdModel)
					
					                   // （可选）是否显示下载过程中的通知栏提示（默认为true：显示）
					                   .showDownloadNotification(true)
					
					                   // （可选）下载成功后是否打开安装界面（默认为false：不打开）
					                   .installApkAfterDownloadSuccess(true)
					
					                   // （可选）安装成功后是否打开应用（默认为false：不打开）
					                   .startAppAfterInstalled(true)
					
					                   // （可选）安装成功后是否删除对应的APK文件（默认为true：立即删除）
					                   // 此方法需要设置安装成功后打开广告应用的方法才生效，即调用了 startAppAfterInstalled(true) 才生效
					                   .deleteApkAfterInstalled(true)
					
					                   // （可选）广告下载成功之后是否自动发送下载成功效果记录（默认为true：发送）
					                   .sendDownloadSuccessEff(true)
					
					                   // （可选）广告安装成功之后是否自动发送安装成功效果记录（默认为true：发送）
					                   // 此方法需要设置下载成功之后打开应用安装界面之后才可能生效，即调用了 installApkAfterDownloadSuccess（true)
					                   .sendInstallSuccessEff(true)
					
					                   // 开始下载
					                   .download();
				} else {
					
					// 如果广告已经安装的话就直接打开
					YoumiNativeAdHelper.openApp(this, mYoumiNativeAdModel);
				}
			}
			
			// 如果为wap广告类型
			// 可以打开外部浏览器或者采用内部浏览器（Webview）加载
			// 这里演示为打开外部浏览器
			else if (mYoumiNativeAdModel.getAdType() == 1) {
				Utils.startActivity2OpenUrlWithChooser(this.getApplicationContext(), mYoumiNativeAdModel.getUrl());
			}
			break;
		default:
			break;
		}
		
	}
	
	private static class MyOnYoumiNativeAdRequesterListener implements OnYoumiNativeAdRequestListener {
		
		private WeakReference<Activity> mReference;
		
		MyOnYoumiNativeAdRequesterListener(Activity context) {
			mReference = new WeakReference<>(context);
		}
		
		/**
		 * 请求结束（本回调执行在UI线程中）
		 * <p>
		 * 注意：这里只是请求结束，是否请求成功，返回数据是否正常还需要开发者针对返回结果做判断
		 *
		 * @param respModel 返回的请求结果
		 */
		@Override
		public void onRequestFinish(YoumiNativeAdResposeModel respModel) {
			final SplashActivity activity = (SplashActivity) mReference.get();
			if (activity == null) {
				return;
			}
			// activity还没有被回收，检查是否已经被销毁，销毁了的话就不加载
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
				if (activity.mIsDestroy) {
					return;
				}
			} else {
				if (activity.isDestroyed()) {
					return;
				}
			}
			
			if (respModel == null || respModel.getCode() != 0) {
				return;
			}
			ArrayList<YoumiNativeAdModel> adModels = respModel.getAdModels();
			if (adModels == null || adModels.isEmpty()) {
				return;
			}
			
			// 因为只请求一个广告，所以这里就直接取0index
			final YoumiNativeAdModel adModel = adModels.get(0);
			ArrayList<YoumiNativeAdModel.YoumiNativeAdPicObject> pics = adModel.getAdPics();
			if (pics == null || pics.isEmpty()) {
				return;
			}
			
			for (YoumiNativeAdModel.YoumiNativeAdPicObject pic : pics) {
				if (pic == null) {
					continue;
				}
				activity.mTipsTextView.setText("请求广告成功，开始加载图片");
				// 直接加载第一张图片
				// 实际使用时，可根据具体要求进行处理
				// 比如：有多张图片返回时，采用合适分辨率的图片，而不是第一张
				Glide.with(activity).load(pic.getUrl()).listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
							isFirstResource) {
						return false;
					}
					
					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
							boolean isFromMemoryCache, boolean isFirstResource) {
						activity.mYoumiNativeAdModel = adModel;
						// 发送曝光记录
						YoumiNativeAdHelper.newAdEffRequest(activity).withYoumiNativeAdModel(adModel).asyncSendShowEff();
						Toast.makeText(
								activity,
								String.format(Locale.getDefault(), "发送广告位 %s 的曝光记录", adModel.getSlotId()),
								Toast.LENGTH_SHORT
						).show();
						return false;
						
					}
				}).into(activity.mImageView);
			}
		}
	}
}
