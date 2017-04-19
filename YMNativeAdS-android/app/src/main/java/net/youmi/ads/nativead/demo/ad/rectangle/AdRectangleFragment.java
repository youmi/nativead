package net.youmi.ads.nativead.demo.ad.rectangle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdResposeModel;
import net.youmi.ads.nativead.demo.BuildConfig;
import net.youmi.ads.nativead.demo.R;
import net.youmi.ads.nativead.demo.ad.BaseFragment;
import net.youmi.ads.nativead.demo.ad.SlotIdConfig;
import net.youmi.ads.nativead.demo.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-17 18:06
 */
public class AdRectangleFragment extends BaseFragment implements View.OnClickListener {
	
	private ImageView mPicView;
	
	private TextView mTitleView;
	
	private TextView mSubTitleView;
	
	private CardView mCardView;
	
	private YoumiNativeAdModel mYoumiNativeAdModel;
	
	private MyAsyncTask mMyAsyncTask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_ad_rectangle, container, false);
		mCardView = (CardView) view.findViewById(R.id.ad_rectangle_cardview);
		mPicView = (ImageView) view.findViewById(R.id.ad_rectangle_iv_large_pic);
		mTitleView = (TextView) view.findViewById(R.id.ad_rectangle_tv_title);
		mSubTitleView = (TextView) view.findViewById(R.id.ad_rectangle_tv_sub_title);
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mCardView.setOnClickListener(this);
		mMyAsyncTask = new MyAsyncTask(this);
		mMyAsyncTask.execute();
	}
	
	@Override
	public void onClick(View v) {
		// 点击了图片之后需要发送点击记录
		YoumiNativeAdHelper.newAdEffRequest(getActivity())
		                   .withAppId(BuildConfig.APPID)
		                   .withYoumiNativeAdModel(mYoumiNativeAdModel)
		                   .asyncSendClickEff();
		
		Toast.makeText(
				getActivity(),
				String.format(Locale.getDefault(), "发送广告位 %s 的点击记录", mYoumiNativeAdModel.getSlotId()),
				Toast.LENGTH_SHORT
		).show();
		
		// 如果为app广告类型
		// 可以进入自定义的详情页，也可以直接下载
		if (mYoumiNativeAdModel.getAdType() == 0) {
			YoumiNativeAdHelper.download(getActivity(), mYoumiNativeAdModel);
		}
		
		// 如果为wap广告类型
		// 可以打开外部浏览器或者采用内部浏览器（Webview）加载
		// 这里演示为打开外部浏览器
		else if (mYoumiNativeAdModel.getAdType() == 1) {
			Utils.startActivity2OpenUrlWithChooser(getActivity(), mYoumiNativeAdModel.getUrl());
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mMyAsyncTask.cancel(true);
	}
	
	private static class MyAsyncTask extends android.os.AsyncTask<Void, Void, YoumiNativeAdResposeModel> {
		
		AdRectangleFragment mAdRectangleFragment;
		
		MyAsyncTask(AdRectangleFragment adRectangleFragment) {
			mAdRectangleFragment = adRectangleFragment;
		}
		
		@Override
		protected YoumiNativeAdResposeModel doInBackground(Void... params) {
			if (mAdRectangleFragment == null || mAdRectangleFragment.getActivity() == null) {
				return null;
			}
			// 发起一个原生广告位广告请求
			return YoumiNativeAdHelper
					
					// 创建一个原生广告请求
					.newAdRequest(mAdRectangleFragment.getActivity())
					
					// （必须）指定appId
					.withAppId(BuildConfig.APPID)
					
					// （必须）指定请求广告位
					.withSlotId(SlotIdConfig.RECTANGLE_SLOIID)
					
					// 发起同步请求
					.request();
			
		}
		
		@Override
		protected void onPostExecute(YoumiNativeAdResposeModel respModel) {
			super.onPostExecute(respModel);
			if (mAdRectangleFragment == null || mAdRectangleFragment.getActivity() == null) {
				return;
			}
			if (respModel == null) {
				Toast.makeText(mAdRectangleFragment.getActivity(), "请求失败，返回结果：null", Toast.LENGTH_SHORT).show();
				return;
			}
			if (respModel.getCode() != 0) {
				Toast.makeText(
						mAdRectangleFragment.getActivity(),
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
			mAdRectangleFragment.mTitleView.setText(adModel.getSlogan());
			mAdRectangleFragment.mSubTitleView.setText(adModel.getSubSlogan());
			
			// 加载大图
			for (YoumiNativeAdModel.YoumiNativeAdPicObject pic : pics) {
				if (pic == null) {
					continue;
				}
				
				// 直接加载第一张图片
				// 实际使用时，可根据具体要求进行处理
				// 比如：有多张图片返回时，采用合适分辨率的图片，而不是第一张
				Glide.with(mAdRectangleFragment).load(pic.getUrl()).listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
							isFirstResource) {
						return false;
					}
					
					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
							boolean isFromMemoryCache, boolean isFirstResource) {
						mAdRectangleFragment.mYoumiNativeAdModel = adModel;
						// 发送曝光记录
						YoumiNativeAdHelper.newAdEffRequest(mAdRectangleFragment.getActivity())
						                   .withAppId(BuildConfig.APPID)
						                   .withYoumiNativeAdModel(adModel)
						                   .asyncSendShowEff();
						Toast.makeText(
								mAdRectangleFragment.getActivity(),
								String.format(Locale.getDefault(), "发送广告位 %s 的曝光记录", adModel.getSlotId()),
								Toast.LENGTH_SHORT
						).show();
						return false;
						
					}
				}).into(mAdRectangleFragment.mPicView);
			}
		}
	}
}