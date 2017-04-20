package net.youmi.ads.nativead.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import net.youmi.ads.nativead.YoumiNativeAdHelper;
import net.youmi.ads.nativead.addownload.OnYoumiNativeAdDownloadListener;
import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;
import net.youmi.ads.nativead.demo.ad.BasePagerAdapter;
import net.youmi.ads.nativead.demo.ad.banner.AdBannerFragment;
import net.youmi.ads.nativead.demo.ad.infoflow.AdInfoFlowFragment;
import net.youmi.ads.nativead.demo.ad.large.AdLargeFragment;
import net.youmi.ads.nativead.demo.ad.rectangle.AdRectangleFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-18 11:54
 */
public class MainActivity extends AppCompatActivity {
	
	AdBannerFragment mAdBannerFragment;
	
	AdLargeFragment mAdLargeFragment;
	
	AdRectangleFragment mAdRectangleFragment;
	
	AdInfoFlowFragment mAdInfoFlowFragment;
	
	BasePagerAdapter mViewPagerAdapter;
	
	TabLayout mTabLayout;
	
	ViewPager mViewpager;
	
	private MyOnYoumiNativeAdDownloadListener mMyOnYoumiNativeAdDownloadListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		mViewpager = (ViewPager) findViewById(R.id.viewpager);
		mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
		
		mAdBannerFragment = new AdBannerFragment();
		mAdLargeFragment = new AdLargeFragment();
		mAdRectangleFragment = new AdRectangleFragment();
		mAdInfoFlowFragment = new AdInfoFlowFragment();
		ArrayList<BasePagerAdapter.FragmentModel> mLists = new ArrayList<>();
		mLists.add(new BasePagerAdapter.FragmentModel("信息流", mAdInfoFlowFragment));
		mLists.add(new BasePagerAdapter.FragmentModel("大图", mAdLargeFragment));
		mLists.add(new BasePagerAdapter.FragmentModel("方图", mAdRectangleFragment));
		mLists.add(new BasePagerAdapter.FragmentModel("横幅", mAdBannerFragment));
		
		mViewPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), mLists);
		mViewpager.setAdapter(mViewPagerAdapter);
		mViewpager.setCurrentItem(0);
		mTabLayout.setupWithViewPager(mViewpager);
		
		// 添加一个广告下载监听器
		mMyOnYoumiNativeAdDownloadListener = new MyOnYoumiNativeAdDownloadListener(this);
		YoumiNativeAdHelper.addOnYoumiNativeAdDownloadListener(mMyOnYoumiNativeAdDownloadListener);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 移除一个广告下载监听器
		YoumiNativeAdHelper.removeOnYoumiNativeAdDownloadListener(mMyOnYoumiNativeAdDownloadListener);
	}
	
	private static class MyOnYoumiNativeAdDownloadListener implements OnYoumiNativeAdDownloadListener {
		
		private WeakReference<Activity> mReference;
		
		MyOnYoumiNativeAdDownloadListener(Activity context) {
			mReference = new WeakReference<>(context);
		}
		
		/**
		 * 下载开始通知，在UI线程中执行
		 *
		 * @param adModel 下载任务对象
		 */
		@Override
		public void onDownloadStart(YoumiNativeAdModel adModel) {
			Activity activity = mReference.get();
			if (activity == null) {
				return;
			}
			Toast.makeText(activity, String.format(Locale.getDefault(), "开始下载广告 %s", adModel.getAdName()), Toast.LENGTH_SHORT)
			     .show();
		}
		
		/**
		 * 下载进度变更通知，在UI线程中执行
		 *
		 * @param adModel           下载任务对象
		 * @param totalLength       下载总长度
		 * @param completeLength    当前已经下载完成的长度
		 * @param downloadPercent   下载百分比
		 * @param downloadBytesPerS 下载速度(B/s)
		 */
		@Override
		public void onDownloadProgressUpdate(YoumiNativeAdModel adModel, long totalLength, long completeLength,
				int downloadPercent, long downloadBytesPerS) {
			
		}
		
		/**
		 * 下载成功通知，在UI线程中执行
		 *
		 * @param adModel 下载任务对象
		 */
		@Override
		public void onDownloadSuccess(YoumiNativeAdModel adModel) {
			Activity activity = mReference.get();
			if (activity == null) {
				return;
			}
			Toast.makeText(activity, String.format(Locale.getDefault(), "广告 %s 下载成功", adModel.getAdName()), Toast.LENGTH_SHORT)
			     .show();
		}
		
		/**
		 * 下载失败通知，在UI线程中执行
		 *
		 * @param adModel 下载任务对象
		 */
		@Override
		public void onDownloadFailed(YoumiNativeAdModel adModel) {
			Activity activity = mReference.get();
			if (activity == null) {
				return;
			}
			Toast.makeText(activity, String.format(Locale.getDefault(), "广告 %s 下载失败", adModel.getAdName()), Toast.LENGTH_SHORT)
			     .show();
		}
		
		/**
		 * 下载停止通知，在UI线程中执行
		 *
		 * @param adModel 下载任务对象
		 */
		@Override
		public void onDownloadStop(YoumiNativeAdModel adModel) {
			Activity activity = mReference.get();
			if (activity == null) {
				return;
			}
			Toast.makeText(activity, String.format(Locale.getDefault(), "广告 %s 下载已被停止", adModel.getAdName()), Toast.LENGTH_SHORT)
			     .show();
		}
	}
}
