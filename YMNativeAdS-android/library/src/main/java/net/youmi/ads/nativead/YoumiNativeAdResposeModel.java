package net.youmi.ads.nativead;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 原始返回结果
 *
 * @author zhitao
 * @since 2017-04-13 10:59
 */
public class YoumiNativeAdResposeModel implements Serializable {
	
	private int code;
	
	private String rsd;
	
	private ArrayList<YoumiNativeAdModel> adModels;
	
	/**
	 * @return 返回结果状态码
	 */
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * @return 本次回话的唯一码
	 */
	public String getRsd() {
		return rsd;
	}
	
	public void setRsd(String rsd) {
		this.rsd = rsd;
	}
	
	/**
	 * @return 返回的广告列表
	 *
	 * @see YoumiNativeAdModel
	 */
	public ArrayList<YoumiNativeAdModel> getAdModels() {
		return adModels;
	}
	
	public void setAdModels(ArrayList<YoumiNativeAdModel> adModels) {
		this.adModels = adModels;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("YoumiNativeAdResposeModel{");
		sb.append("\n  code=").append(code);
		sb.append("\n  rsd='").append(rsd).append('\'');
		sb.append("\n  adModels=").append(adModels);
		sb.append("\n}");
		return sb.toString();
	}
}
