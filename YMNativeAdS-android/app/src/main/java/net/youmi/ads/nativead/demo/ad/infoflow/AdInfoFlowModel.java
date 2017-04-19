package net.youmi.ads.nativead.demo.ad.infoflow;

import android.support.annotation.IntDef;

import net.youmi.ads.nativead.adrequest.YoumiNativeAdModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author zhitao
 * @since 2017-04-17 18:36
 */
public class AdInfoFlowModel {
	
	public final static int TYPE_NORMAL = 0;
	
	public final static int TYPE_AD_BANNER = 1;
	
	public final static int TYPE_AD_LARGE = 1 << 1;
	
	public final static int TYPE_AD_RECTANGLE = 1 << 2;
	
	@Retention(RetentionPolicy.SOURCE)
	@IntDef({ TYPE_NORMAL, TYPE_AD_BANNER, TYPE_AD_LARGE, TYPE_AD_RECTANGLE })
	public @interface TYPE {
		
	}
	
	/**
	 * item类型
	 */
	@TYPE private int type;
	
	private YoumiNativeAdModel mAdModel;
	
	public AdInfoFlowModel(@TYPE int type, YoumiNativeAdModel adModel) {
		this.type = type;
		mAdModel = adModel;
	}
	
	public AdInfoFlowModel(@TYPE int type) {
		this.type = type;
	}
	
	@TYPE
	public int getType() {
		return type;
	}
	
	public void setType(@TYPE int type) {
		this.type = type;
	}
	
	public YoumiNativeAdModel getAdModel() {
		return mAdModel;
	}
	
	public void setAdModel(YoumiNativeAdModel adModel) {
		mAdModel = adModel;
	}
}
