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

import net.youmi.ads.nativead.YoumiNativeAdHelper;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdResposeModel;
import net.youmi.ads.nativead.demo.BuildConfig;
import net.youmi.ads.nativead.demo.R;
import net.youmi.ads.nativead.demo.ad.BaseFragment;
import net.youmi.ads.nativead.demo.ad.SlotIdConfig;

import java.util.ArrayList;

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
		
		Toast.makeText(AdInfoFlowFragment.this.getActivity(), "click position " + position, Toast.LENGTH_SHORT).show();
		
		AdInfoFlowModel model = mAdapter.getItem(position);
		if (model.getType() == AdInfoFlowModel.TYPE_AD_BANNER || model.getType() == AdInfoFlowModel.TYPE_AD_LARGE ||
		    model.getType() == AdInfoFlowModel.TYPE_AD_RECTANGLE) {
			
			// 异步发送点击记录
			YoumiNativeAdHelper.newAdEffRequest(this.getActivity())
			                   .withAppId(BuildConfig.APPID)
			                   .withYoumiNativeAdModel(model.getAdModel())
			                   .asyncSendClickEff();
			
			// 可以进入自定义的详情页，也可以直接下载
			// 如果是应用广告，这里演示为直接下载
			if (model.getAdModel().getAdType() == 0) {
				YoumiNativeAdHelper.download(this.getActivity(), model.getAdModel());
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
			
			// 在最后插入banner
			YoumiNativeAdModel adBanner = getYoumiNativeAdModel(SlotIdConfig.BANNER_SLOIID);
			if (adBanner != null) {
				models.add(15, new AdInfoFlowModel(AdInfoFlowModel.TYPE_AD_BANNER, adBanner));
			}
			
			// 在中间插入方图广告
			YoumiNativeAdModel adRectangle = getYoumiNativeAdModel(SlotIdConfig.RECTANGLE_SLOIID);
			if (adRectangle != null) {
				models.add(10, new AdInfoFlowModel(AdInfoFlowModel.TYPE_AD_RECTANGLE, adRectangle));
			}
			
			// 在开头插入大图广告
			YoumiNativeAdModel adLarge = getYoumiNativeAdModel(SlotIdConfig.LARGE_SLOIID);
			if (adLarge != null) {
				models.add(5, new AdInfoFlowModel(AdInfoFlowModel.TYPE_AD_LARGE, adLarge));
			}
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
			//  这里演示为：只取第一个对象，实际逻辑可由根据业务优化
			return adModels.get(0);
		}
	}
}
