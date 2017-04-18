package net.youmi.ads.nativead.demo.ad;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.youmi.ads.base.log.DLog;

/**
 * @author zhitao
 * @since 2017-04-18 14:40
 */
public class BaseFragment extends Fragment {
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		DLog.i("%s : onAttach", getClass().getSimpleName());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DLog.i("%s : onCreate", getClass().getSimpleName());
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		DLog.i("%s : onCreateView", getClass().getSimpleName());
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		DLog.i("%s : onActivityCreated", getClass().getSimpleName());
	}
	
	@Override
	public void onStart() {
		super.onStart();
		DLog.i("%s : onStart", getClass().getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		DLog.i("%s : onResume", getClass().getSimpleName());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		DLog.i("%s : onPause", getClass().getSimpleName());
	}
	
	@Override
	public void onStop() {
		super.onStop();
		DLog.i("%s : onStop", getClass().getSimpleName());
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		DLog.i("%s : onDestroyView", getClass().getSimpleName());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		DLog.i("%s : onDestroy", getClass().getSimpleName());
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		DLog.i("%s : onDetach", getClass().getSimpleName());
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		DLog.i("%s : onSaveInstanceState", getClass().getSimpleName());
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		DLog.i("%s : onConfigurationChanged", getClass().getSimpleName());
	}
	
}
