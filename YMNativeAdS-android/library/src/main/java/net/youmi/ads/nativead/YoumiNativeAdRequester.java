package net.youmi.ads.nativead;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import net.youmi.ads.base.deviceinfos.DeviceInfoUtils;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.network.BaseHttpRequesterModel;
import net.youmi.ads.base.network.YoumiHttpRequester;
import net.youmi.ads.base.utils.JSONUtils;
import net.youmi.ads.base.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author zhitao
 * @since 2017-04-13 14:17
 */
public class YoumiNativeAdRequester {
	
	private final static String REQ_URL = "https://native.umapi.cn/aos/v1/oreq";
	
	private Context mApplicationContext;
	
	/**
	 * appId
	 */
	private String mAppId;
	
	/**
	 * （必须）请求广告位Id
	 */
	public String slotId;
	
	/**
	 * （必须）请求广告数量
	 */
	public int adCount = 1;
	
	/**
	 * （可选）性别 M=男性，F=女性
	 */
	public String gender;
	
	/**
	 * （可选）年龄
	 */
	public String age;
	
	/**
	 * （可选）内容标题
	 */
	public String contTitle;
	
	/**
	 * （可选）内容关键词
	 */
	public String contKeyword;
	
	/**
	 * （可选）请求唯一标识
	 */
	public String reqId;
	
	/**
	 * （可选）UserAgent
	 */
	public String userAgent;
	
	private YoumiNativeAdRequester() {
	}
	
	public YoumiNativeAdResposeModel request() {
		try {
			StringBuilder sb = new StringBuilder(512);
			sb.append(REQ_URL);
			sb.append("?reqtime=").append(System.currentTimeMillis() / 1000);
			sb.append("&slotid=").append(slotId);
			sb.append("&adcount=").append(adCount);
			if (!TextUtils.isEmpty(gender)) {
				sb.append("&gender=").append(getEncodeValue(gender));
			}
			if (!TextUtils.isEmpty(age)) {
				sb.append("&age=").append(age);
			}
			if (!TextUtils.isEmpty(contTitle)) {
				sb.append("&cont_title=").append(getEncodeValue(contTitle));
			}
			if (!TextUtils.isEmpty(contKeyword)) {
				sb.append("&cont_kw=").append(getEncodeValue(contKeyword));
			}
			if (!TextUtils.isEmpty(reqId)) {
				sb.append("&reqid=").append(getEncodeValue(reqId));
			}
			sb.append("&brand=").append(getEncodeValue(DeviceInfoUtils.getBrand()));
			sb.append("&model=").append(getEncodeValue(DeviceInfoUtils.getModel()));
			sb.append("&mac=").append(DeviceInfoUtils.getMacAddress(mApplicationContext));
			sb.append("&imei=").append(DeviceInfoUtils.getDeviceId(mApplicationContext));
			sb.append("&androidid=").append(DeviceInfoUtils.getAndroidID(mApplicationContext));
			if (!TextUtils.isEmpty(userAgent)) {
				sb.append("&ua=").append(getEncodeValue(userAgent));
			}
			sb.append("&os=").append("Android");
			sb.append("&osv=").append(DeviceInfoUtils.getAndroidVersionName());
			sb.append("&conntype=").append(NetworkUtils.getNetworkType(mApplicationContext));
			sb.append("&carrier=").append(getEncodeValue(DeviceInfoUtils.getOperatorName(mApplicationContext)));
			sb.append("&pk=").append(mApplicationContext.getPackageName());
			sb.append("&language=").append(getEncodeValue(DeviceInfoUtils.getLanguage()));
			sb.append("&countrycode=").append(getEncodeValue(DeviceInfoUtils.getCountry()));
			
			// 添加指定的header
			ArrayList<BaseHttpRequesterModel.Header> headers = new ArrayList<>();
			headers.add(new BaseHttpRequesterModel.Header("Authorization", "Bearer " + mAppId));
			
			// 发起请求
			String respJsonStr = YoumiHttpRequester.requestGet(mApplicationContext, sb.toString(), headers);
			if (TextUtils.isEmpty(respJsonStr)) {
				return null;
			}
			JSONObject respJson = JSONUtils.toJsonObject(respJsonStr.trim());
			if (respJson == null) {
				return null;
			}
			
			int code = JSONUtils.getInt(respJson, "c", -1);
			String rsd = JSONUtils.getString(respJson, "rsd", null);
			
			YoumiNativeAdResposeModel adResp = new YoumiNativeAdResposeModel();
			adResp.setCode(code);
			adResp.setRsd(rsd);
			
			// 状态码不正常就不用继续解析
			if (code < 0) {
				return adResp;
			}
			
			JSONArray adsJsonArray = JSONUtils.getJsonArray(respJson, "ad", null);
			if (adsJsonArray == null || adsJsonArray.length() == 0) {
				return adResp;
			}
			
			ArrayList<YoumiNativeAdModel> adModels = null;
			for (int i = 0; i < adsJsonArray.length(); i++) {
				JSONObject adJson = JSONUtils.getJsonObject(adsJsonArray, i, null);
				if (adJson == null) {
					continue;
				}
				
				int adId = JSONUtils.getInt(adJson, "id", -1);
				int slotId = JSONUtils.getInt(adJson, "slotid", -1);
				String name = JSONUtils.getString(adJson, "name", null);
				String iconUrl = JSONUtils.getString(adJson, "icon", null);
				
				JSONArray picJsonArray = JSONUtils.getJsonArray(adJson, "pic", null);
				ArrayList<YoumiNativeAdModel.YoumiNativeAdPicObject> pics = null;
				if (picJsonArray != null || picJsonArray.length() > 0) {
					for (int j = 0; j < picJsonArray.length(); j++) {
						JSONObject picJson = JSONUtils.getJsonObject(picJsonArray, j, null);
						if (picJson == null) {
							continue;
						}
						String picUrl = JSONUtils.getString(picJson, "url", null);
						int width = JSONUtils.getInt(picJson, "w", 0);
						int height = JSONUtils.getInt(picJson, "h", 0);
						
						YoumiNativeAdModel.YoumiNativeAdPicObject pic = new YoumiNativeAdModel.YoumiNativeAdPicObject();
						pic.setUrl(picUrl);
						pic.setWidth(width);
						pic.setHeight(height);
						if (pics == null) {
							pics = new ArrayList<>();
						}
						pics.add(pic);
					}
				}
				
				String slogan = JSONUtils.getString(adJson, "slogan", null);
				String subSlogan = JSONUtils.getString(adJson, "subslogan", null);
				String url = JSONUtils.getString(adJson, "url", null);
				String uri = JSONUtils.getString(adJson, "uri", null);
				int pt = JSONUtils.getInt(adJson, "pt", 0);
				
				JSONObject trackJson = JSONUtils.getJsonObject(adJson, "track", null);
				ArrayList<String> showUrls = null;
				ArrayList<String> clickUrls = null;
				if (trackJson != null) {
					
					JSONArray showUrlsJsonArray = JSONUtils.getJsonArray(trackJson, "show", null);
					if (showUrlsJsonArray != null && showUrlsJsonArray.length() > 0) {
						for (int j = 0; j < showUrlsJsonArray.length(); j++) {
							String showUrl = JSONUtils.getString(showUrlsJsonArray, j, null);
							if (TextUtils.isEmpty(showUrl)) {
								continue;
							}
							if (showUrls == null) {
								showUrls = new ArrayList<>();
							}
							showUrls.add(showUrl);
						}
					}
					
					JSONArray clickUrlsJsonArray = JSONUtils.getJsonArray(trackJson, "click", null);
					if (clickUrlsJsonArray != null && clickUrlsJsonArray.length() > 0) {
						for (int j = 0; j < clickUrlsJsonArray.length(); j++) {
							String clickUrl = JSONUtils.getString(clickUrlsJsonArray, j, null);
							if (TextUtils.isEmpty(clickUrl)) {
								continue;
							}
							if (clickUrls == null) {
								clickUrls = new ArrayList<>();
							}
							clickUrls.add(clickUrl);
						}
					}
				}
				
				// 只有广告类型为APP广告才解析 "app" 字段
				YoumiNativeAdModel.YoumiNativeAdAppModel appModel = null;
				if (pt == 0) {
					JSONObject appJson = JSONUtils.getJsonObject(adJson, "app", null);
					if (appJson != null) {
						String packageName = JSONUtils.getString(appJson, "bid", null);
						String desc = JSONUtils.getString(appJson, "description", null);
						String size = JSONUtils.getString(appJson, "size", null);
						
						JSONArray screenShotJsonArray = JSONUtils.getJsonArray(appJson, "screenshot", null);
						ArrayList<String> screenShotUrls = null;
						if (screenShotJsonArray != null && screenShotJsonArray.length() > 0) {
							for (int j = 0; j < screenShotJsonArray.length(); j++) {
								String screenShotUrl = JSONUtils.getString(screenShotJsonArray, j, null);
								if (TextUtils.isEmpty(screenShotUrl)) {
									continue;
								}
								if (screenShotUrls == null) {
									screenShotUrls = new ArrayList<>();
								}
								screenShotUrls.add(screenShotUrl);
							}
						}
						
						float score = JSONUtils.getFloat(appJson, "score", 0f);
						String category = JSONUtils.getString(appJson, "category", null);
						
						appModel = new YoumiNativeAdModel.YoumiNativeAdAppModel();
						appModel.setPackageName(packageName);
						appModel.setDescription(desc);
						appModel.setSize(size);
						appModel.setScreenShots(screenShotUrls);
						appModel.setScore(score);
						appModel.setCategory(category);
					}
				}
				
				JSONObject extJson = JSONUtils.getJsonObject(adJson, "ext", null);
				YoumiNativeAdModel.YoumiNativeAdExtModel extModel = null;
				if (extJson != null) {
					
					int io = JSONUtils.getInt(extJson, "io", 0);
					int delay = JSONUtils.getInt(extJson, "delay", 3);
					int sal = JSONUtils.getInt(extJson, "sal", 1);
					int pl = JSONUtils.getInt(extJson, "pl", 1);
					
					extModel = new YoumiNativeAdModel.YoumiNativeAdExtModel();
					extModel.setIo(io);
					extModel.setDelay(delay);
					extModel.setSal(sal);
					extModel.setPl(pl);
				}
				
				YoumiNativeAdModel adModel = new YoumiNativeAdModel();
				adModel.setAdId(adId);
				adModel.setSlotId(slotId);
				adModel.setAdName(name);
				adModel.setAdIconUrl(iconUrl);
				adModel.setAdPics(pics);
				adModel.setSlogan(slogan);
				adModel.setSubSlogan(subSlogan);
				adModel.setUrl(url);
				adModel.setUri(uri);
				adModel.setAdType(pt);
				adModel.setShowUrls(showUrls);
				adModel.setClickUrls(clickUrls);
				adModel.setAppModel(appModel);
				adModel.setExtModel(extModel);
				
				if (adModels == null) {
					adModels = new ArrayList<>();
				}
				adModels.add(adModel);
				
			}
			adResp.setAdModels(adModels);
			return adResp;
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
	/**
	 * 将部分参数进行Base64加密，并替换部分特殊字符串
	 *
	 * @param value 原始参数
	 *
	 * @return 处理后参数
	 */
	private String getEncodeValue(String value) {
		if (TextUtils.isEmpty(value)) {
			return null;
		}
		try {
			String result = Base64.encodeToString(value.getBytes("UTF-8"), Base64.NO_WRAP);
			if (result != null) {
				return result.replace("+", "-").replace("=", "_").replace("/", ".");
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
	public static class Builder {
		
		private YoumiNativeAdRequester mRequester = new YoumiNativeAdRequester();
		
		/**
		 * @param context 上下文
		 *
		 * @return this
		 */
		public Builder withContext(Context context) {
			mRequester.mApplicationContext = context.getApplicationContext();
			return this;
		}
		
		/**
		 * （必须）应用APPID
		 *
		 * @param appId （必须）应用APPID
		 *
		 * @return this
		 */
		public Builder withAppId(String appId) {
			mRequester.mAppId = appId;
			return this;
		}
		
		/**
		 * （必须）请求广告位Id
		 *
		 * @param slotId 广告位Id
		 *
		 * @return this
		 */
		public Builder withSlotId(String slotId) {
			mRequester.slotId = slotId;
			return this;
		}
		
		/**
		 * （可选）请求广告数量，默认为1
		 *
		 * @param adCount 请求广告数量
		 *
		 * @return this
		 */
		public Builder withRequestCount(int adCount) {
			mRequester.adCount = adCount;
			return this;
		}
		
		/**
		 * （可选）性别
		 *
		 * @param gender M=男性，F=女性
		 *
		 * @return this
		 */
		public Builder withGender(String gender) {
			mRequester.gender = gender;
			return this;
		}
		
		/**
		 * （可选）年龄
		 *
		 * @param age 年龄
		 *
		 * @return this
		 */
		public Builder withAge(String age) {
			mRequester.age = age;
			return this;
		}
		
		/**
		 * （可选）内容标题
		 *
		 * @param contTitle 内容标题
		 *
		 * @return this
		 */
		public Builder withContentTitle(String contTitle) {
			mRequester.contTitle = contTitle;
			return this;
		}
		
		/**
		 * （可选）内容关键词
		 *
		 * @param contKeyword 内容关键词
		 *
		 * @return this
		 */
		public Builder withContentKeyword(String contKeyword) {
			mRequester.contKeyword = contKeyword;
			return this;
		}
		
		/**
		 * （可选）请求唯一标识
		 *
		 * @param reqId （请求唯一标识
		 *
		 * @return this
		 */
		public Builder withReqId(String reqId) {
			mRequester.reqId = reqId;
			return this;
		}
		
		/**
		 * （可选）UserAgent
		 *
		 * @param userAgent UserAgent
		 *
		 * @return this
		 */
		public Builder withUserAgent(String userAgent) {
			mRequester.userAgent = userAgent;
			return this;
		}
		
		public YoumiNativeAdRequester build() {
			return mRequester;
		}
	}
	
}
