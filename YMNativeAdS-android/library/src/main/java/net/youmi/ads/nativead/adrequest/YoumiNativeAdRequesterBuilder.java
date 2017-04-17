package net.youmi.ads.nativead.adrequest;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import net.youmi.ads.base.deviceinfos.DeviceInfoUtils;
import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.network.BaseHttpRequesterModel;
import net.youmi.ads.base.network.YoumiHttpRequester;
import net.youmi.ads.base.pool.GlobalCacheExecutor;
import net.youmi.ads.base.utils.JSONUtils;
import net.youmi.ads.base.utils.NetworkUtils;
import net.youmi.ads.base.utils.UIHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 有米原生广告请求
 *
 * @author zhitao
 * @since 2017-04-13 14:17
 */
public class YoumiNativeAdRequesterBuilder {
	
	private final static String REQ_URL = "https://native.umapi.cn/aos/v1/oreq";
	
	private Context applicationContext;
	
	/**
	 * appId
	 */
	private String appId;
	
	/**
	 * （必须）请求广告位Id
	 */
	private String slotId;
	
	/**
	 * （必须）请求广告数量
	 */
	private int adCount = 1;
	
	/**
	 * （可选）性别 M=男性，F=女性
	 */
	private String gender;
	
	/**
	 * （可选）年龄
	 */
	private String age;
	
	/**
	 * （可选）内容标题
	 */
	private String contTitle;
	
	/**
	 * （可选）内容关键词
	 */
	private String contKeyword;
	
	/**
	 * （可选）请求唯一标识
	 */
	private String reqId;
	
	/**
	 * （可选）UserAgent
	 */
	private String userAgent;
	
	public YoumiNativeAdRequesterBuilder(Context context) {
		applicationContext = context.getApplicationContext();
	}
	
	/**
	 * （必须）设置应用APPID
	 *
	 * @param appId （必须）应用APPID
	 *
	 * @return this
	 */
	public YoumiNativeAdRequesterBuilder withAppId(String appId) {
		this.appId = appId;
		return this;
	}
	
	/**
	 * （必须）设置请求广告位Id
	 *
	 * @param slotId 广告位Id
	 *
	 * @return this
	 */
	public YoumiNativeAdRequesterBuilder withSlotId(String slotId) {
		this.slotId = slotId;
		return this;
	}
	
	/**
	 * （可选）设置请求广告数量，默认为1
	 *
	 * @param adCount 请求广告数量
	 *
	 * @return this
	 */
	public YoumiNativeAdRequesterBuilder withRequestCount(int adCount) {
		this.adCount = adCount;
		return this;
	}
	
	/**
	 * （可选）设置性别
	 *
	 * @param gender M=男性，F=女性
	 *
	 * @return this
	 */
	public YoumiNativeAdRequesterBuilder withGender(String gender) {
		this.gender = gender;
		return this;
	}
	
	/**
	 * （可选）设置年龄
	 *
	 * @param age 年龄
	 *
	 * @return this
	 */
	public YoumiNativeAdRequesterBuilder withAge(String age) {
		this.age = age;
		return this;
	}
	
	/**
	 * （可选）设置内容标题
	 *
	 * @param contTitle 内容标题
	 *
	 * @return this
	 */
	public YoumiNativeAdRequesterBuilder withContentTitle(String contTitle) {
		this.contTitle = contTitle;
		return this;
	}
	
	/**
	 * （可选）设置内容关键词
	 *
	 * @param contKeyword 内容关键词
	 *
	 * @return this
	 */
	public YoumiNativeAdRequesterBuilder withContentKeyword(String contKeyword) {
		this.contKeyword = contKeyword;
		return this;
	}
	
	/**
	 * （可选）设置请求唯一标识
	 *
	 * @param reqId （请求唯一标识
	 *
	 * @return this
	 */
	public YoumiNativeAdRequesterBuilder withReqId(String reqId) {
		this.reqId = reqId;
		return this;
	}
	
	/**
	 * （可选）设置UserAgent
	 *
	 * @param userAgent UserAgent
	 *
	 * @return this
	 */
	public YoumiNativeAdRequesterBuilder withUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}
	
	/**
	 * 异步请求
	 *
	 * @param listener 监听器
	 */
	public void requestWithListener(final OnYoumiNativeAdRequestListener listener) {
		GlobalCacheExecutor.execute(new Runnable() {
			@Override
			public void run() {
				final YoumiNativeAdResposeModel respModel = request();
				if (listener != null) {
					UIHandler.runInUIThread(new Runnable() {
						@Override
						public void run() {
							listener.onRequestFinish(respModel);
						}
					});
				}
			}
		});
	}
	
	/**
	 * 同步请求
	 *
	 * @return YoumiNativeAdResposeModel 对象
	 */
	public YoumiNativeAdResposeModel request() {
		if (TextUtils.isEmpty(appId)) {
			throw new IllegalArgumentException("can not request without appId");
		}
		if (TextUtils.isEmpty(slotId)) {
			throw new IllegalArgumentException("can not request without slotId");
		}
		
		try {
			StringBuilder sb = new StringBuilder(512);
			sb.append(REQ_URL);
			sb.append("?reqtime=").append(System.currentTimeMillis() / 1000);
			sb.append("&slotid=").append(slotId);
			sb.append("&adcount=").append(adCount);
			if (!TextUtils.isEmpty(gender)) {
				sb.append("&gender=").append(base64Encode(gender));
			}
			if (!TextUtils.isEmpty(age)) {
				sb.append("&age=").append(age);
			}
			if (!TextUtils.isEmpty(contTitle)) {
				sb.append("&cont_title=").append(base64Encode(contTitle));
			}
			if (!TextUtils.isEmpty(contKeyword)) {
				sb.append("&cont_kw=").append(base64Encode(contKeyword));
			}
			if (!TextUtils.isEmpty(reqId)) {
				sb.append("&reqid=").append(base64Encode(reqId));
			}
			sb.append("&brand=").append(base64Encode(DeviceInfoUtils.getBrand()));
			sb.append("&model=").append(base64Encode(DeviceInfoUtils.getModel()));
			sb.append("&mac=").append(DeviceInfoUtils.getMacAddress(applicationContext));
			sb.append("&imei=").append(DeviceInfoUtils.getDeviceId(applicationContext));
			sb.append("&androidid=").append(DeviceInfoUtils.getAndroidID(applicationContext));
			if (!TextUtils.isEmpty(userAgent)) {
				sb.append("&ua=").append(base64Encode(userAgent));
			}
			sb.append("&os=").append("Android");
			sb.append("&osv=").append(DeviceInfoUtils.getAndroidVersionName());
			sb.append("&conntype=").append(NetworkUtils.getNetworkType(applicationContext));
			sb.append("&carrier=").append(base64Encode(DeviceInfoUtils.getOperatorName(applicationContext)));
			sb.append("&pk=").append(applicationContext.getPackageName());
			sb.append("&language=").append(base64Encode(DeviceInfoUtils.getLanguage()));
			sb.append("&countrycode=").append(base64Encode(DeviceInfoUtils.getCountry()));
			
			// 添加指定的header
			ArrayList<BaseHttpRequesterModel.Header> headers = new ArrayList<>();
			headers.add(new BaseHttpRequesterModel.Header("Authorization", "Bearer " + appId));
			
			// 发起请求
			String respJsonStr = YoumiHttpRequester.httpGetForString(applicationContext, sb.toString(), headers);
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
				
				String adId = JSONUtils.getString(adJson, "id", null);
				String slotId = JSONUtils.getString(adJson, "slotid", null);
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
	private String base64Encode(String value) {
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
}
