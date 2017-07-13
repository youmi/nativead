package net.youmi.ads.base.deviceinfos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import net.youmi.ads.base.log.DLog;
import net.youmi.ads.base.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;

/**
 * @author zhitao
 * @since 2017-04-13 11:46
 */
@SuppressLint("HardwareIds")
public class DeviceInfoUtils {
	
	private static String sMacAddress;
	
	public static String getDeviceId(Context context) {
		try {
			TelephonyManager telephonyManager =
					(TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager != null) {
				String deviceId = telephonyManager.getDeviceId();
				if (!TextUtils.isEmpty(deviceId)) {
					return deviceId;
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return "";
	}
	
	public static String getIMSI(Context context) {
		try {
			TelephonyManager telephonyManager =
					(TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager != null) {
				String imsi = telephonyManager.getSubscriberId();
				if (!TextUtils.isEmpty(imsi)) {
					return imsi;
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return "";
	}
	
	/**
	 * 获取网络运营商并且转换为有米原生广告请求协议中的网络运营商标识号
	 *
	 * @param context 上下文，会自动去ApplicationContext
	 *
	 * @return <ul>
	 * <li>空：无</li>
	 * <li>0: 未知/其他</li>
	 * <li>2: 中国移动</li>
	 * <li>3: 中国联通</li>
	 * <li>4: 中国电信</li>
	 * </ul>
	 */
	public static String getOperatorNameCode(Context context) {
		try {
			TelephonyManager telephonyManager =
					(TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager != null) {
				String operatorName = telephonyManager.getNetworkOperator();
				if (!TextUtils.isEmpty(operatorName)) {
					if (operatorName.equals("46000") || operatorName.equals("46002") || operatorName.equals("46007")) {
						return "2";
					}
					if (operatorName.equals("46001") || operatorName.equals("46006")) {
						return "3";
					}
					if (operatorName.equals("46003") || operatorName.equals("46005")) {
						return "4";
					}
					return "0";
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return "";
	}
	
	public static String getMacAddress(Context context) {
		if (!TextUtils.isEmpty(sMacAddress)) {
			return sMacAddress;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			LineNumberReader reader = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(new File("/sys/class/net/wlan0/address"));
				reader = new LineNumberReader(new InputStreamReader(fis));
				String lineStr;
				while (!TextUtils.isEmpty(lineStr = reader.readLine())) {
					sMacAddress = lineStr.trim().toLowerCase();
					if (!TextUtils.isEmpty(sMacAddress)) {
						break;
					}
				}
			} catch (Throwable e) {
				DLog.e(e);
			} finally {
				IOUtils.close(reader);
				IOUtils.close(fis);
			}
		} else {
			try {
				WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifi.getConnectionInfo();
				String macAddress = wifiInfo.getMacAddress();
				if (macAddress != null) {
					sMacAddress = macAddress.trim().toLowerCase();
				}
			} catch (Throwable e) {
				DLog.e(e);
			}
		}
		
		if (!TextUtils.isEmpty(sMacAddress)) {
			return sMacAddress;
		} else {
			return "";
		}
	}
	
	public static String getAndroidID(Context context) {
		try {
			return android.provider.Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
					android.provider.Settings.Secure.ANDROID_ID
			);
		} catch (Throwable e) {
			DLog.e(e);
		}
		return "";
	}
	
	public static String getBrand() {
		return Build.BRAND;
	}
	
	public static String getModel() {
		return Build.MODEL;
	}
	
	public static String getAndroidVersionName() {
		return Build.VERSION.RELEASE;
	}
	
	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}
	
	public static String getCountry() {
		return Locale.getDefault().getCountry();
	}
}
