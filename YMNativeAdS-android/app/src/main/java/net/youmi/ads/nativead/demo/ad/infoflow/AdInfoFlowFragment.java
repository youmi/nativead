package net.youmi.ads.nativead.demo.ad.infoflow;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.utils.PackageUtils;
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
 * @since 2017-04-17 18:27
 */
public class AdInfoFlowFragment extends BaseFragment
		implements AdInfoFlowAdapter.OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
	
	private RecyclerView mRecyclerView;
	
	private SwipeRefreshLayout mSwipeRefreshLayout;
	
	private AdInfoFlowAdapter mAdapter;
	
	private MyAsyncTask mMyAsyncTask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_ad_info_flow, container, false);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_ad_info_flow_recycler_view);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_ad_info_flow_swipe);
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new AdInfoFlowAdapter(this.getActivity(), null);
		mAdapter.setOnRecyclerViewItemClickListener(this);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		mRecyclerView.setAdapter(mAdapter);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mMyAsyncTask = new MyAsyncTask(this);
		mMyAsyncTask.execute();
	}
	
	@Override
	public void onItemClick(int position) {
		AdInfoFlowModel model = mAdapter.getItem(position);
		if (model.getAdModel() == null) {
			return;
		}
		if (model.getType() == AdInfoFlowModel.TYPE_AD_BANNER || model.getType() == AdInfoFlowModel.TYPE_AD_LARGE ||
		    model.getType() == AdInfoFlowModel.TYPE_AD_RECTANGLE) {
			
			// 点击了图片之后需要发送点击记录
			YoumiNativeAdHelper.newAdEffRequest(getActivity())
			                   .withAppId(BuildConfig.APPID)
			                   .withYoumiNativeAdModel(model.getAdModel())
			                   .asyncSendClickEff();
			
			Toast.makeText(
					getActivity(),
					String.format(Locale.getDefault(), "发送广告位 %s 的点击记录", model.getAdModel().getSlotId()),
					Toast.LENGTH_SHORT
			).show();
			
			// 如果为app广告类型
			// 可以进入自定义的详情页，也可以直接下载
			if (model.getAdModel().getAdType() == 0) {
				
				if (!PackageUtils.isPakcageInstall(getActivity(), model.getAdModel().getAppModel().getPackageName())) {
					
					// 如果广告还没有安装的话，就创建一个广告下载任务
					YoumiNativeAdHelper.newAdDownload(getActivity())
					
					                   // （必须）指定下载的广告
					                   .withYoumiNativeAdModel(model.getAdModel())
					
					                   // （可选）是否显示下载过程中的通知栏提示（默认为true：显示）
					                   .withDefaultDownloadNotification(true)
					
					                   // （可选）下载成功后是否打开安装界面（默认为false：不打开）
					                   .installAfterDownloadSuccess(true)
					
					                   // （可选）安装成功后是否打开应用（默认为false：不打开）
					                   .startAppAfterInstalled(true)
					
					                   // 开始下载
					                   .download();
				} else {
					
					// 如果广告已经安装的话就直接打开
					YoumiNativeAdHelper.openApp(getActivity(), model.getAdModel());
				}
			}
			
			// 如果为wap广告类型
			// 可以打开外部浏览器或者采用内部浏览器（Webview）加载
			// 这里演示为打开外部浏览器
			else if (model.getAdModel().getAdType() == 1) {
				Utils.startActivity2OpenUrl(getActivity(), model.getAdModel().getUrl());
			}
		}
	}
	
	@Override
	public void onRefresh() {
		if (mMyAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
			return;
		}
		mMyAsyncTask = new MyAsyncTask(this);
		mMyAsyncTask.execute();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mMyAsyncTask.cancel(true);
	}
	
	private static class MyAsyncTask extends android.os.AsyncTask<Void, Void, ArrayList<AdInfoFlowModel>> {
		
		AdInfoFlowFragment mFragment;
		
		MyAsyncTask(AdInfoFlowFragment fragment) {
			mFragment = fragment;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!mFragment.mSwipeRefreshLayout.isRefreshing()) {
				mFragment.mSwipeRefreshLayout.setRefreshing(true);
			}
		}
		
		@Override
		protected ArrayList<AdInfoFlowModel> doInBackground(Void... params) {
			ArrayList<AdInfoFlowModel> models = new ArrayList<>();
			for (int i = 0; i < 15; i++) {
				models.add(new AdInfoFlowModel(AdInfoFlowModel.TYPE_NORMAL));
			}
			
			// 在最后插入横幅广告
			DLog.i("请求横幅广告");
			YoumiNativeAdModel adBanner = getYoumiNativeAdModel(SlotIdConfig.BANNER_SLOIID);
			if (adBanner != null) {
				models.add(15, new AdInfoFlowModel(AdInfoFlowModel.TYPE_AD_BANNER, adBanner));
			}
			
			// 在中间插入方图广告
			DLog.i("请求方图广告");
			YoumiNativeAdModel adRectangle = getYoumiNativeAdModel(SlotIdConfig.RECTANGLE_SLOIID);
			if (adRectangle != null) {
				models.add(10, new AdInfoFlowModel(AdInfoFlowModel.TYPE_AD_RECTANGLE, adRectangle));
			}
			
			// 在前面插入大图广告
			DLog.i("请求大图广告");
			YoumiNativeAdModel adLarge = getYoumiNativeAdModel(SlotIdConfig.LARGE_SLOIID);
			if (adLarge != null) {
				models.add(5, new AdInfoFlowModel(AdInfoFlowModel.TYPE_AD_LARGE, adLarge));
			}
			DLog.i("请求结束");
			return models;
		}
		
		@Override
		protected void onPostExecute(ArrayList<AdInfoFlowModel> models) {
			super.onPostExecute(models);
			mFragment.mAdapter.setData(models);
			mFragment.mSwipeRefreshLayout.setRefreshing(false);
		}
		
		private YoumiNativeAdModel getYoumiNativeAdModel(String slotId) {
			YoumiNativeAdResposeModel adResp = YoumiNativeAdHelper.newAdRequest(mFragment.getActivity())
			                                                      .withAppId(BuildConfig.APPID)
			                                                      .withSlotId(slotId)
			                                                      .request();
			
			if (adResp == null || adResp.getCode() != 0) {
				return null;
			}
			ArrayList<YoumiNativeAdModel> adModels = adResp.getAdModels();
			if (adModels == null || adModels.isEmpty()) {
				return null;
			}
			// 暂时只要第一个广告
			return adModels.get(0);
		}
		
	}
}
