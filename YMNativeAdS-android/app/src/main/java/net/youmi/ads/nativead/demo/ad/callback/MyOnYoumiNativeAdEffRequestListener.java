package net.youmi.ads.nativead.demo.ad.callback;

import android.content.Context;
import android.widget.Toast;

import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;
import net.youmi.ads.nativead.effrequest.OnYoumiNativeAdEffRequestListener;

import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-07-06 17:11
 */
public class MyOnYoumiNativeAdEffRequestListener implements OnYoumiNativeAdEffRequestListener {
	
	private Context mContext;
	
	private String mEffDesc;
	
	public MyOnYoumiNativeAdEffRequestListener(Context context, String effDesc) {
		mContext = context.getApplicationContext();
		mEffDesc = effDesc;
	}
	
	/**
	 * 效果记录发送完毕时回调
	 *
	 * @param isSuccess 是否所有对应的效果记录都发送成功
	 * @param adModel   效果记录对应的广告信息对象
	 */
	@Override
	public void onEffRequestFinish(boolean isSuccess, YoumiNativeAdModel adModel) {
		Toast.makeText(
				mContext,
				String.format(Locale.getDefault(), "发送广告位 %s 的 %s %s", adModel.getSlotId(), mEffDesc, isSuccess ? "成功" : "失败"),
				Toast.LENGTH_SHORT
		).show();
	}
}
