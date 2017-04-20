package net.youmi.ads.nativead.demo.ad;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * @author zhitao
 * @since 2017-04-17 16:35
 */
public class BasePagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<FragmentModel> mFragments;
	
	public BasePagerAdapter(FragmentManager fm, ArrayList<FragmentModel> fragments) {
		super(fm);
		mFragments = fragments;
	}
	
	@Override
	public Fragment getItem(int position) {
		return mFragments == null ? null : mFragments.get(position).getFragment();
	}
	
	@Override
	public int getCount() {
		return mFragments == null ? 0 : mFragments.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mFragments.get(position).getTitle();
	}
	
	public static class FragmentModel {
		
		String mTitle;
		
		Fragment mFragment;
		
		public FragmentModel(String title, Fragment fragment) {
			mTitle = title;
			mFragment = fragment;
		}
		
		String getTitle() {
			return mTitle;
		}
		
		Fragment getFragment() {
			return mFragment;
		}
	}
}
