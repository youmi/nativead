package net.youmi.ads.nativead.demo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.youmi.ads.nativead.demo.ad.BasePagerAdapter;
import net.youmi.ads.nativead.demo.ad.banner.AdBannerFragment;
import net.youmi.ads.nativead.demo.ad.infoflow.AdInfoFlowFragment;
import net.youmi.ads.nativead.demo.ad.large.AdLargeFragment;
import net.youmi.ads.nativead.demo.ad.rectangle.AdRectangleFragment;

import java.util.ArrayList;

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
		mLists.add(new BasePagerAdapter.FragmentModel("横幅", mAdBannerFragment));
		mLists.add(new BasePagerAdapter.FragmentModel("大图", mAdLargeFragment));
		mLists.add(new BasePagerAdapter.FragmentModel("方图", mAdRectangleFragment));
		mLists.add(new BasePagerAdapter.FragmentModel("信息流", mAdInfoFlowFragment));
		
		mViewPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), mLists);
		mViewpager.setAdapter(mViewPagerAdapter);
		mViewpager.setCurrentItem(0);
		mTabLayout.setupWithViewPager(mViewpager);
	}
}
