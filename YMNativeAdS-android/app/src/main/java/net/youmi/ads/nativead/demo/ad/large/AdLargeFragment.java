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

import net.youmi.ads.nativead.YoumiNativeAdHelper;
import net.youmi.ads.nativead.adrequest.OnYoumiNativeAdRequestListener;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdResposeModel;
import net.youmi.ads.nativead.demo.BuildConfig;
import net.youmi.ads.nativead.demo.R;
import net.youmi.ads.nativead.demo.ad.BaseFragment;
import net.youmi.ads.nativead.demo.ad.SlotIdConfig;

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
				
				// （必须）指定appId
				.withAppId(BuildConfig.APPID)
				
				// （必须）指定请求广告位
				.withSlotId(SlotIdConfig.LARGE_SLOIID)
				
				// 发起异步请求
				.requestWithListener(new MyOnYoumiNativeAdRequestListener(this));
		
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
				Toast.makeText(
						adLargeFragment.getActivity(),
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
				Glide.with(adLargeFragment)
				     .load(pic.getUrl())
				     .listener(new RequestListener<String, GlideDrawable>() {
					     @Override
					     public boolean onException(Exception e, String model, Target<GlideDrawable> target,
							     boolean isFirstResource) {
						     return false;
					     }
					
					     @Override
					     public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
							     boolean isFromMemoryCache, boolean isFirstResource) {
						     adLargeFragment.mYoumiNativeAdModel = adModel;
						     // 发送曝光记录
						     YoumiNativeAdHelper.newAdEffRequest(adLargeFragment.getActivity())
						                        .withAppId(BuildConfig.APPID)
						                        .withYoumiNativeAdModel(adModel)
						                        .asyncSendShowEff();
						     Toast.makeText(
								     adLargeFragment.getActivity(),
								     String.format(Locale.getDefault(), "发送广告位 %s 的曝光记录", adModel.getSlotId()),
								     Toast.LENGTH_SHORT
						     ).show();
						     return false;
						
					     }
				     })
				     .into(adLargeFragment.mPicView);
			}
		}
	}
}
