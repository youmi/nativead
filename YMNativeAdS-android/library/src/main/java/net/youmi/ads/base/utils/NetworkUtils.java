package net.youmi.ads.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.network.BaseHttpRequesterModel;
import net.youmi.ads.base.network.HttpRequesterFactory;

import java.net.HttpURLConnection;

/**
 * @author zhitao
 * @since 2017-04-13 14:44
 */
public class NetworkUtils {
	
	/**
	 * 获取最终的url地址(循环遍历301/302)
	 *
	 * @param rawUrl
	 * @param loopMaxTimes 最大遍历次数，不然坑爹一点的话会无限循环重定向 (A->B->A)
	 *
	 * @return
	 */
	public static String getFinalDestUrlByHttpURLConnection(Context context, String rawUrl, int loopMaxTimes) {
		if (TextUtils.isEmpty(rawUrl)) {
			return null;
		}
		int count = 0;
		DLog.i("原始url:%s", rawUrl);
		String currentUrl = rawUrl;
		while (count < loopMaxTimes) {
			HttpURLConnection httpURLConnection = null;
			try {
				httpURLConnection = HttpRequesterFactory.newHttpURLConnection(context, currentUrl);
				httpURLConnection.setRequestMethod(BaseHttpRequesterModel.REQUEST_TYPE_GET);
				
				// 关闭自动跟踪重定向
				httpURLConnection.setInstanceFollowRedirects(false);
				
				// 只要connection就可以获取到头部信息，而不用getInputStream(发送请求)
				httpURLConnection.connect();
				
				switch (httpURLConnection.getResponseCode()) {
				case HttpURLConnection.HTTP_MULT_CHOICE://300
				case HttpURLConnection.HTTP_MOVED_TEMP: //301
				case HttpURLConnection.HTTP_MOVED_PERM: //302
				case HttpURLConnection.HTTP_SEE_OTHER:  //303
				case 307:                               //307
					currentUrl = httpURLConnection.getHeaderField("Location");
					DLog.i("中途url:%s", currentUrl);
					break;
				default:
					httpURLConnection.disconnect();
					DLog.i("最终url:%s", currentUrl);
					return currentUrl;
				}
			} catch (Exception e) {
				DLog.e(e);
			} finally {
				try {
					if (httpURLConnection != null) {
						httpURLConnection.disconnect();
					}
				} catch (Exception e) {
					DLog.e(e);
				}
			}
			
		}
		return rawUrl;
	}
	
	/**
	 * 检查当前网络是否可用 1.检查联网权限 2.检查网络状态
	 *
	 * @param context 上下文
	 *
	 * @return 当前网络是否可以使用
	 */
	public static boolean isNetworkAvailable(Context context) {
		
		//		if (!PermissionUtils.isPermissionGranted(context, Manifest.permission.INTERNET)) {
		//			return false;
		//		}
		
		try {
			ConnectivityManager connectivityManager =
					(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				if (activeNetworkInfo.isAvailable()) {
					return true;
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		
		return false;
	}
	
	/**
	 * 获取当前移动网络APN名字
	 *
	 * @param context
	 *
	 * @return
	 */
	public static String getApn(Context context) {
		try {
			//			if (!PermissionUtils.isPermissionGranted(context, Manifest.permission.ACCESS_NETWORK_STATE)) {
			//				return APN.APN_UNKNOW;
			//			}
			ConnectivityManager connectivityManager =
					(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				if (activeNetworkInfo.isAvailable()) {
					switch (activeNetworkInfo.getType()) {
					case ConnectivityManager.TYPE_WIFI: // wifi网络
						return APN.APN_WIFI;
					case ConnectivityManager.TYPE_MOBILE: // 手机网络
						// 判断接入点
						String apn = activeNetworkInfo.getExtraInfo();
						if (apn != null) {
							apn = apn.trim().toLowerCase();
							if (apn.length() > 25) {
								return apn.substring(0, 25);
							} else {
								return apn;
							}
						} else {
							// 未知接入点，返回 APN_UNKNOW
							return APN.APN_UNKNOW;
						}
					default:
						break;
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return APN.APN_UNKNOW;
	}
	
	/**
	 * 获取当前网络类型
	 *
	 * @param context 上下文
	 *
	 * @return <ul>
	 * <li>{@link NetworkType#TYPE_UNKNOWN}</li>
	 * <li>{@link NetworkType#TYPE_2G}</li>
	 * <li>{@link NetworkType#TYPE_3G}</li>
	 * <li>{@link NetworkType#TYPE_4G}</li>
	 * <li>{@link NetworkType#TYPE_WIFI}</li>
	 * </ul>
	 */
	public static int getNetworkType(Context context) {
		//		if (!PermissionUtils.isPermissionGranted(
		//				context,
		//				Manifest.permission.INTERNET,
		//				Manifest.permission.ACCESS_NETWORK_STATE,
		//				Manifest.permission.ACCESS_WIFI_STATE
		//		)) {
		//			return NetworkType.TYPE_UNKNOWN;
		//		}
		try {
			ConnectivityManager connectivityManager =
					(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				// 网络不可用
				if (!activeNetworkInfo.isAvailable()) {
					return NetworkType.TYPE_UNKNOWN;
				}
				// 网络可用
				else {
					
					// 判断当前网络类型
					switch (activeNetworkInfo.getType()) {
					case ConnectivityManager.TYPE_WIFI: // wifi网络
						return NetworkType.TYPE_WIFI;
					case ConnectivityManager.TYPE_MOBILE: // 手机网络
						try {
							// 判断手机网络类型
							switch (activeNetworkInfo.getSubtype()) {
							case TelephonyManager.NETWORK_TYPE_CDMA: // 14-64kbps,电信2G网络
							case TelephonyManager.NETWORK_TYPE_IDEN: // 25kbps,
							case TelephonyManager.NETWORK_TYPE_1xRTT: // 50-100kbps,
							case TelephonyManager.NETWORK_TYPE_EDGE: // 50-100kbps,移动2G网络，基于gprs的，2.75代
							case TelephonyManager.NETWORK_TYPE_GPRS: // 100kbps,联通2G网络，基于gsm，2.5代
								return NetworkType.TYPE_2G;
							
							case TelephonyManager.NETWORK_TYPE_EVDO_0: // 400-1000kbps,电信3G网络
							case TelephonyManager.NETWORK_TYPE_EVDO_A: // 600-1400kbps,电信3G网络
							case TelephonyManager.NETWORK_TYPE_EVDO_B: // 5Mbps,电信3G网络
							case TelephonyManager.NETWORK_TYPE_UMTS: // 400-7000kbps
							case TelephonyManager.NETWORK_TYPE_HSPA: // 700-1700kbps,WCDMA，应用于R99,R4，
							case TelephonyManager.NETWORK_TYPE_HSDPA: // 2-14Mbps,基于WCDMA联通3G网络（3.5G）——高速下行，一般部署在其他城市，应用于R5
							case TelephonyManager.NETWORK_TYPE_HSUPA: // 1-23Mbps,基于WCDMA联通3G网络（3.5G）——高速上行，一般部署在重要城市，应用于R6
							case TelephonyManager.NETWORK_TYPE_EHRPD: // 1-2Mbps,电信3G网络
							case TelephonyManager.NETWORK_TYPE_HSPAP: // 10-20Mbps,也称HSPA+，目前全球最快的WCDMA商用网络
								return NetworkType.TYPE_3G;
							
							case TelephonyManager.NETWORK_TYPE_LTE: // 10+Mbps,3G到4G的一个过渡，准4G网络
								return NetworkType.TYPE_4G;
							
							case TelephonyManager.NETWORK_TYPE_UNKNOWN: // 未知网络
								return NetworkType.TYPE_UNKNOWN;
							default:
								return NetworkType.TYPE_3G;
							}
							
						} catch (Exception e) {
							DLog.e(e);
						}
						return NetworkType.TYPE_3G;
					default:
						break;
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return NetworkType.TYPE_UNKNOWN;
		
	}
	
	public static long getContentLength(Context context, String url) {
		return getContentLengthByHttpURLConnection(context, url, 5);
	}
	
	/**
	 * 获取服务器上目标文件的长度(支持传入从重定向地址)
	 *
	 * @param rawUrl       原始url
	 * @param loopMaxTimes 最大遍历次数，不然坑爹一点的话会无限循环重定向 (A->B->A)
	 *
	 * @return contentLength
	 */
	public static long getContentLengthByHttpURLConnection(Context context, String rawUrl, int loopMaxTimes) {
		if (TextUtils.isEmpty(rawUrl)) {
			return -1;
		}
		int count = 0;
		String currentUrl = rawUrl;
		while (count < loopMaxTimes) {
			HttpURLConnection httpURLConnection = null;
			try {
				httpURLConnection = HttpRequesterFactory.newHttpURLConnection(context, currentUrl);
				httpURLConnection.setRequestMethod(BaseHttpRequesterModel.REQUEST_TYPE_GET);
				
				// 关闭自动跟踪重定向
				httpURLConnection.setInstanceFollowRedirects(false);
				
				// 只要connection就可以获取到头部信息，而不用getInputStream(发送请求)
				httpURLConnection.connect();
				
				switch (httpURLConnection.getResponseCode()) {
				case HttpURLConnection.HTTP_MULT_CHOICE://300
				case HttpURLConnection.HTTP_MOVED_TEMP: //301
				case HttpURLConnection.HTTP_MOVED_PERM: //302
				case HttpURLConnection.HTTP_SEE_OTHER:  //303
				case 307:                               //307
					currentUrl = httpURLConnection.getHeaderField("Location");
					DLog.i("中途url:%s", currentUrl);
					break;
				default:
					long contentLength = httpURLConnection.getContentLength();
					httpURLConnection.disconnect();
					DLog.i("最终url:%s", currentUrl);
					DLog.i("最终ContentLength:%d", contentLength);
					return contentLength;
				}
			} catch (Exception e) {
				DLog.e(e);
			} finally {
				try {
					if (httpURLConnection != null) {
						httpURLConnection.disconnect();
					}
				} catch (Exception e) {
					DLog.e(e);
				}
			}
			
		}
		return -1;
	}
	
	public static class NetworkType {
		
		public final static int TYPE_UNKNOWN = 0;
		
		public final static int TYPE_WIFI = 1;
		
		public final static int TYPE_2G = 2;
		
		public final static int TYPE_3G = 3;
		
		public final static int TYPE_4G = 4;
	}
	
	public static class APN {
		
		/**
		 * apn wifi
		 */
		public final static String APN_WIFI = "wifi";
		
		// -------------------------------------------------------------------------------------
		// 移动apn
		
		/**
		 * apn cmnet 移动 2G 3G 4G通用
		 */
		public final static String APN_CMNET = "cmnet";
		
		/**
		 * apn cmwap 移动 2G 3G 4G通用 ，但是好像不适用4G
		 */
		public final static String APN_CMWAP = "cmwap";
		
		// -------------------------------------------------------------------------------------
		// 联通apn
		
		/**
		 * apn uninet 联通 2G
		 */
		public final static String APN_UNINET = "uninet";
		
		/**
		 * apn uniwap 联通 2G
		 */
		public final static String APN_UNIWAP = "uniwap";
		
		/**
		 * apn 3gnet 联通3G
		 */
		public final static String APN_3GNET = "3gnet";
		
		/**
		 * apn 3gwap 联通3G
		 */
		public final static String APN_3GWAP = "3gwap";
		
		/**
		 * 联通4G 现阶段中国联通4G业务APN接入点可以设置为3gnet。以后待数据改造完成且通过验证后，中国联通LTE以及3G定制终端接入点会启用wonet接入点。
		 */
		public final static String APN_WONET = "wonet";
		
		// -------------------------------------------------------------------------------------
		// 电信apn
		
		/**
		 * apn #777 ctwap 电信2G
		 */
		public final static String APN_CTWAP = "ctwap";
		
		/**
		 * apn #777 ctnet 电信3G 理论速率 3.1M
		 */
		public final static String APN_CTNET = "ctnet";
		
		/**
		 * 电信4G 理论速率100M
		 */
		public final static String APN_CTLTE = "ctlte";
		
		// -------------------------------------------------------------------------------------
		
		/**
		 * apn internet
		 */
		public final static String APN_INTERNET = "internet";
		
		/**
		 * apn unknow
		 */
		public final static String APN_UNKNOW = "";
		
	}
	
}
