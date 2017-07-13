package net.youmi.ads.nativead.adrequest;

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
	 * 返回结果状态码
	 *
	 * @return 0为成功，负数错误代码则存在问题，常见错误代码列表：
	 * <ul>
	 * <li> {@code -1012}: Header 错误 </li>
	 * <li> {@code -2007}: 当前没有广告 </li>
	 * <li> {@code -2222}: 请求广告超时 </li>
	 * <li> {@code -3003}: 缺少请求参数 </li>
	 * <li> {@code -3208}: 设备参数非法或者缺失，如必须要有IMEI参数，也就是说不支持大部分没有电话功能的平板 </li>
	 * <li> {@code -3312}: 用户今天的广告展示已经达到上限 </li>
	 * </ul>
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
		//		final StringBuilder sb = new StringBuilder("YoumiNativeAdResposeModel{");
		//		sb.append("\n  code=").append(code);
		//		sb.append("\n  rsd='").append(rsd).append('\'');
		//		sb.append("\n  adModels=").append(adModels);
		//		sb.append("\n}");
		//		return sb.toString();
		return super.toString();
	}
}
