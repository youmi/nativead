package net.youmi.ads.nativead.demo.ad.banner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.youmi.ads.nativead.YoumiNativeAdHelper;
import net.youmi.ads.nativead.adrequest.OnYoumiNativeAdRequestListener;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdResposeModel;
import net.youmi.ads.nativead.demo.BuildConfig;
import net.youmi.ads.nativead.demo.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-17 16:35
 */
public class AdBannerFragment extends Fragment implements OnYoumiNativeAdRequestListener, View.OnClickListener {
	
	private ImageView mImageView;
	
	private YoumiNativeAdModel mYoumiNativeAdModel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ad_banner, container, false);
		mImageView = (ImageView) view.findViewById(R.id.ad_banner_iv);
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mImageView.setOnClickListener(this);
		
		// 发起一个原生广告位广告请求
		YoumiNativeAdHelper
				
				// 创建一个原生广告请求
				.newAdRequest(getActivity())
				
				// （必须）指定appId
				.withAppId(BuildConfig.APPID)
				
				// （必须）指定请求广告位
				.withSlotId("7949")
				
				// 发起异步请求
				.requestWithListener(this);
		
	}
	
	
	@Override
	public void onClick(View v) {
		// 点击了图片之后需要发送点击记录
		YoumiNativeAdHelper.newAdEffRequest(getActivity())
		                   .withAppId(BuildConfig.APPID)
		                   .withYoumiNativeAdModel(mYoumiNativeAdModel)
		                   .asyncSendClickeff();
		
		Toast.makeText(
				getActivity(),
				String.format(Locale.getDefault(), "发送广告位 %s 的点击记录", mYoumiNativeAdModel.getSlotId()),
				Toast.LENGTH_SHORT
		).show();
		
		// 可以进入自定义的详情页，也可以直接下载
		YoumiNativeAdHelper.download(getActivity(), mYoumiNativeAdModel);
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
		//		DLog.i("请求成功");
		//		final SplashActivity activity = (SplashActivity) mReference.get();
		//		if (activity == null) {
		//			DLog.i("但是activity已经被回收，不展示");
		//			return;
		//		}
		//		// activity还没有被回收，检查是否已经被销毁，销毁了的话就不加载
		//		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
		//			if (activity.mIsDestroy) {
		//				DLog.i("activity已经销毁，不再加载图片");
		//				return;
		//			}
		//		} else {
		//			if (activity.isDestroyed()) {
		//				DLog.i("activity已经销毁，不再加载图片");
		//				return;
		//			}
		//		}
		
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
			// 直接加载第一张图片
			// 实际使用时，可根据具体要求进行处理
			// 比如：有多张图片返回时，采用合适分辨率的图片，而不是第一张
			Glide.with(this).load(pic.getUrl()).listener(new RequestListener<String, GlideDrawable>() {
				@Override
				public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
					return false;
				}
				
				@Override
				public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
						boolean isFromMemoryCache, boolean isFirstResource) {
					mYoumiNativeAdModel = adModel;
					// 发送曝光记录
					YoumiNativeAdHelper.newAdEffRequest(getActivity())
					                   .withAppId(BuildConfig.APPID)
					                   .withYoumiNativeAdModel(adModel)
					                   .asyncSendShowEff();
					Toast.makeText(
							getActivity(),
							String.format(Locale.getDefault(), "发送广告位 %s 的曝光记录", adModel.getSlotId()),
							Toast.LENGTH_SHORT
					).show();
					return false;
					
				}
			}).into(mImageView);
		}
	}
}