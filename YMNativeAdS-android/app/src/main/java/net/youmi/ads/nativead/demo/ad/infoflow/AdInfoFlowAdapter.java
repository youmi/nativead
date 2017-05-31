package net.youmi.ads.nativead.demo.ad.infoflow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;
import net.youmi.ads.nativead.demo.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-17 18:32
 */
class AdInfoFlowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
	
	private Context mContext;
	
	private LayoutInflater mLayoutInflater;
	
	private ArrayList<AdInfoFlowModel> mModels;
	
	private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
	
	AdInfoFlowAdapter(Context context, ArrayList<AdInfoFlowModel> models) {
		super();
		mContext = context;
		mModels = models;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	public void setData(ArrayList<AdInfoFlowModel> sysInfoModels) {
		mModels = sysInfoModels;
		notifyDataSetChanged();
	}
	
	void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
		mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
	}
	
	AdInfoFlowModel getItem(int position) {
		return mModels == null ? null : mModels.get(position);
	}
	
	@Override
	public int getItemCount() {
		return mModels == null ? 0 : mModels.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		return getItem(position).getType();
	}
	
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		RecyclerView.ViewHolder viewHolder;
		switch (viewType) {
		case AdInfoFlowModel.TYPE_AD_BANNER:
			viewHolder = new AdBannerHolder(mLayoutInflater.inflate(R.layout.recycler_view_item_ad_banner, parent, false));
			break;
		case AdInfoFlowModel.TYPE_AD_LARGE:
			viewHolder = new AdLargeHolder(mLayoutInflater.inflate(R.layout.recycler_view_item_ad_large, parent, false));
			break;
		case AdInfoFlowModel.TYPE_AD_RECTANGLE:
			viewHolder = new AdRectangleHolder(mLayoutInflater.inflate(R.layout.recycler_view_item_ad_rectangle, parent, false));
			break;
		default:
			viewHolder = new NormalItemHolder(mLayoutInflater.inflate(R.layout.recycler_view_item_normal, parent, false));
			break;
		}
		viewHolder.itemView.setOnClickListener(this);
		return viewHolder;
	}
	
	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
		if (holder instanceof NormalItemHolder) {
			onBindNormalItemViewHolder((NormalItemHolder) holder, position);
		} else if (holder instanceof AdBannerHolder) {
			onBindAdBannerViewHolder((AdBannerHolder) holder, position);
		} else if (holder instanceof AdLargeHolder) {
			onBindAdLargeViewHolder((AdLargeHolder) holder, position);
		} else if (holder instanceof AdRectangleHolder) {
			onBindAdRectangleViewHolder((AdRectangleHolder) holder, position);
		}
		holder.itemView.setTag(R.id.recycleview_tag_position, position);
		holder.itemView.setTag(R.id.recycleview_tag_model, getItem(position));
	}
	
	private void onBindNormalItemViewHolder(NormalItemHolder holder, int position) {
		holder.mTitleView.setText(String.format(Locale.getDefault(), "第%d条新闻", position + 1));
	}
	
	private void onBindAdBannerViewHolder(AdBannerHolder holder, int position) {
		YoumiNativeAdModel adModel = getItem(position).getAdModel();
		if (adModel == null) {
			return;
		}
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
			Glide.with(mContext).load(pic.getUrl()).listener(new OnRequestListener(mContext, adModel)).into(holder.mImageView);
		}
	}
	
	private void onBindAdLargeViewHolder(AdLargeHolder holder, int position) {
		// 因为只请求一个广告，所以这里就直接取0index
		YoumiNativeAdModel adModel = getItem(position).getAdModel();
		if (adModel == null) {
			return;
		}
		
		ArrayList<YoumiNativeAdModel.YoumiNativeAdPicObject> pics = adModel.getAdPics();
		if (pics == null || pics.isEmpty()) {
			return;
		}
		
		// 显示文字
		holder.mTitleView.setText(adModel.getSlogan());
		holder.mSubTitleView.setText(adModel.getSubSlogan());
		
		// 加载图标
		if (!TextUtils.isEmpty(adModel.getAdIconUrl())) {
			Glide.with(mContext).load(adModel.getAdIconUrl()).into(holder.mIconView);
		}
		
		// 加载大图
		for (YoumiNativeAdModel.YoumiNativeAdPicObject pic : pics) {
			if (pic == null) {
				continue;
			}
			
			// 直接加载第一张图片
			// 实际使用时，可根据具体要求进行处理
			// 比如：有多张图片返回时，采用合适分辨率的图片，而不是第一张
			Glide.with(mContext).load(pic.getUrl()).listener(new OnRequestListener(mContext, adModel)).into(holder.mPicView);
		}
	}
	
	private void onBindAdRectangleViewHolder(AdRectangleHolder holder, int position) {
		// 因为只请求一个广告，所以这里就直接取0index
		YoumiNativeAdModel adModel = getItem(position).getAdModel();
		if (adModel == null) {
			return;
		}
		
		ArrayList<YoumiNativeAdModel.YoumiNativeAdPicObject> pics = adModel.getAdPics();
		if (pics == null || pics.isEmpty()) {
			return;
		}
		
		// 显示文字
		holder.mTitleView.setText(adModel.getSlogan());
		holder.mSubTitleView.setText(adModel.getSubSlogan());
		
		// 加载大图
		for (YoumiNativeAdModel.YoumiNativeAdPicObject pic : pics) {
			if (pic == null) {
				continue;
			}
			
			// 直接加载第一张图片
			// 实际使用时，可根据具体要求进行处理
			// 比如：有多张图片返回时，采用合适分辨率的图片，而不是第一张
			Glide.with(mContext).load(pic.getUrl()).listener(new OnRequestListener(mContext, adModel)).into(holder.mPicView);
		}
	}
	
	@Override
	public void onClick(View v) {
		if (mOnRecyclerViewItemClickListener != null) {
			int position = Integer.valueOf(v.getTag(R.id.recycleview_tag_position).toString());
			mOnRecyclerViewItemClickListener.onItemClick(position);
		}
	}
	
	private static class OnRequestListener implements RequestListener<String, GlideDrawable> {
		
		private Context mContext;
		
		private YoumiNativeAdModel mAdModel;
		
		OnRequestListener(Context context, YoumiNativeAdModel adModel) {
			mContext = context;
			mAdModel = adModel;
		}
		
		@Override
		public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
			return false;
		}
		
		@Override
		public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
				boolean isFromMemoryCache, boolean isFirstResource) {
			// 发送曝光记录
			YoumiNativeAdHelper.newAdEffRequest(mContext).withYoumiNativeAdModel(mAdModel).asyncSendShowEff();
			Toast.makeText(mContext,
					String.format(Locale.getDefault(), "发送广告位 %s 的曝光记录", mAdModel.getSlotId()),
					Toast.LENGTH_SHORT
			)
			     .show();
			
			return false;
		}
	}
	
	private static class NormalItemHolder extends RecyclerView.ViewHolder {
		
		TextView mTitleView;
		
		NormalItemHolder(View itemView) {
			super(itemView);
			mTitleView = (TextView) itemView.findViewById(R.id.recycler_view_normal_item_tv_title);
		}
	}
	
	private static class AdBannerHolder extends RecyclerView.ViewHolder {
		
		ImageView mImageView;
		
		AdBannerHolder(View itemView) {
			super(itemView);
			mImageView = (ImageView) itemView.findViewById(R.id.ad_banner_iv);
		}
	}
	
	private static class AdLargeHolder extends RecyclerView.ViewHolder {
		
		ImageView mIconView;
		
		TextView mTitleView;
		
		TextView mSubTitleView;
		
		ImageView mPicView;
		
		AdLargeHolder(View itemView) {
			super(itemView);
			mIconView = (ImageView) itemView.findViewById(R.id.ad_large_iv_icon);
			mTitleView = (TextView) itemView.findViewById(R.id.ad_large_tv_title);
			mSubTitleView = (TextView) itemView.findViewById(R.id.ad_large_tv_sub_title);
			mPicView = (ImageView) itemView.findViewById(R.id.ad_large_iv_large_pic);
		}
	}
	
	private static class AdRectangleHolder extends RecyclerView.ViewHolder {
		
		ImageView mPicView;
		
		TextView mTitleView;
		
		TextView mSubTitleView;
		
		AdRectangleHolder(View itemView) {
			super(itemView);
			
			mPicView = (ImageView) itemView.findViewById(R.id.ad_rectangle_iv_large_pic);
			mTitleView = (TextView) itemView.findViewById(R.id.ad_rectangle_tv_title);
			mSubTitleView = (TextView) itemView.findViewById(R.id.ad_rectangle_tv_sub_title);
		}
	}
	
	interface OnRecyclerViewItemClickListener {
		
		void onItemClick(int position);
	}
}