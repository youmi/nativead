package net.youmi.ads.nativead.demo.ad.large;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import net.youmi.ads.nativead.demo.R;
import net.youmi.ads.nativead.demo.ad.BaseFragment;
import net.youmi.ads.nativead.demo.ad.callback.MyOnYoumiNativeAdEffRequestListener;
import net.youmi.ads.nativead.demo.ad.SlotIdConfig;
import net.youmi.ads.nativead.demo.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-17 17:20
 */
public class AdLargeFragment extends BaseFragment implements View.OnClickListener {
	
	private ImageView mIconView;
	
	private TextView mTitleView;
	
	private TextView mSubTitleView;
	
	private ImageView mPicView;
	
	private CardView mCardView;
	
	private YoumiNativeAdModel mYoumiNativeAdModel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_ad_large, container, false);
		mCardView = (CardView) view.findViewById(R.id.ad_large_cardview);
		mIconView = (ImageView) view.findViewById(R.id.ad_large_iv_icon);
		mTitleView = (TextView) view.findViewById(R.id.ad_large_tv_title);
		mSubTitleView = (TextView) view.findViewById(R.id.ad_large_tv_sub_title);
		mPicView = (ImageView) view.findViewById(R.id.ad_large_iv_large_pic);
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mCardView.setOnClickListener(this);
		
		// 发起一个原生广告位广告请求
		YoumiNativeAdHelper
				
				// 创建一个原生广告请求
				.newAdRequest(getActivity())
				
				// （必须）指定请求广告位
				.withSlotId(SlotIdConfig.LARGE_SLOIID)
				
				// 发起异步请求
				.request(new MyOnYoumiNativeAdRequestListener(this));
		
	}
	
	@Override
	public void onClick(View v) {
		// 如果还没有请求到数据就不处理
		if (mYoumiNativeAdModel == null) {
			return;
		}
		
		// 点击了图片之后需要发送点击记录
		YoumiNativeAdHelper.newAdEffRequest(getActivity())
		                   .withYoumiNativeAdModel(mYoumiNativeAdModel)
		                   .asyncSendClickEff(new MyOnYoumiNativeAdEffRequestListener(getActivity(), "点击效果记录"));
		
		// 如果为app广告类型
		// 可以进入自定义的详情页，也可以直接下载
		if (mYoumiNativeAdModel.getAdType() == 0) {
			
			if (!PackageUtils.isPakcageInstall(getActivity(), mYoumiNativeAdModel.getAppModel().getPackageName())) {
				
				// 如果广告还没有安装的话，就创建一个广告下载任务
				YoumiNativeAdHelper.newAdDownload(getActivity())
				
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
				YoumiNativeAdHelper.openApp(getActivity(), mYoumiNativeAdModel);
			}
		}
		
		// 如果为wap广告类型
		// 可以打开外部浏览器或者采用内部浏览器（Webview）加载
		// 这里演示为打开外部浏览器
		else if (mYoumiNativeAdModel.getAdType() == 1) {
			Utils.startActivity2OpenUrlWithChooser(getActivity(), mYoumiNativeAdModel.getUrl());
		}
	}
	
	private static class MyOnYoumiNativeAdRequestListener implements OnYoumiNativeAdRequestListener {
		
		private WeakReference<AdLargeFragment> mReference;
		
		MyOnYoumiNativeAdRequestListener(AdLargeFragment adBannerFragment) {
			mReference = new WeakReference<>(adBannerFragment);
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
			if (mReference == null) {
				return;
			}
			final AdLargeFragment adLargeFragment = mReference.get();
			if (adLargeFragment == null) {
				return;
			}
			if (adLargeFragment.getActivity() == null) {
				return;
			}
			
			if (respModel == null) {
				Toast.makeText(adLargeFragment.getActivity(), "请求失败，返回结果：null", Toast.LENGTH_SHORT).show();
				return;
			}
			if (respModel.getCode() != 0) {
				Toast.makeText(adLargeFragment.getActivity(),
						String.format(Locale.getDefault(), "请求失败，错误代码：%d", respModel.getCode()),
						Toast.LENGTH_SHORT
				).show();
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
			
			// 显示文字
			adLargeFragment.mTitleView.setText(adModel.getSlogan());
			adLargeFragment.mSubTitleView.setText(adModel.getSubSlogan());
			
			// 加载图标
			if (!TextUtils.isEmpty(adModel.getAdIconUrl())) {
				Glide.with(adLargeFragment).load(adModel.getAdIconUrl()).into(adLargeFragment.mIconView);
			}
			
			// 加载大图
			for (YoumiNativeAdModel.YoumiNativeAdPicObject pic : pics) {
				if (pic == null) {
					continue;
				}
				
				// 直接加载第一张图片
				// 实际使用时，可根据具体要求进行处理
				// 比如：有多张图片返回时，采用合适分辨率的图片，而不是第一张
				Glide.with(adLargeFragment).load(pic.getUrl()).listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
							isFirstResource) {
						return false;
					}
					
					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
							boolean isFromMemoryCache, boolean isFirstResource) {
						adLargeFragment.mYoumiNativeAdModel = adModel;
						// 发送曝光记录
						YoumiNativeAdHelper.newAdEffRequest(adLargeFragment.getActivity())
						                   .withYoumiNativeAdModel(adModel)
						                   .asyncSendShowEff(new MyOnYoumiNativeAdEffRequestListener(adLargeFragment
								                   .getActivity(),
								                   "曝光效果记录"
						                   ));
						return false;
						
					}
				}).into(adLargeFragment.mPicView);
			}
		}
	}
}
