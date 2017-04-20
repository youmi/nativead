package net.youmi.ads.base.utils;

import net.youmi.ads.base.log.DLog;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author zhitao
 * @since 2017-04-13 16:14
 */
public class JSONUtils {
	
	public static boolean isKeyNotNull(JSONObject jsonObject, String key) {
		return jsonObject != null && !jsonObject.isNull(key);
	}
	
	public static JSONObject toJsonObject(String json) {
		try {
			if (json == null) {
				return null;
			}
			return new JSONObject(json);
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
	public static JSONArray toJsonArray(String json) {
		try {
			if (json == null) {
				return null;
			}
			return new JSONArray(json);
		} catch (Throwable e) {
			DLog.e(e);
		}
		return null;
	}
	
	public static String getString(JSONObject obj, String key, String defaultValue) {
		try {
			if (isKeyNotNull(obj, key)) {
				String str = obj.getString(key);
				if (str != null) {
					str = str.trim();
					if (str.length() > 0) {
						return str;
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static boolean getBoolean(JSONObject obj, String key, boolean defaultValue) {
		try {
			if (isKeyNotNull(obj, key)) {
				return obj.getBoolean(key);
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static String getString(JSONArray ary, int index, String defaultValue) {
		try {
			if (ary != null) {
				if (ary.length() > index && index > -1) {
					String str = ary.getString(index);
					if (str != null) {
						str = str.trim();
						
						if (str.length() > 0) {
							return str;
						}
					}
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static int getInt(JSONObject obj, String key, int defaultValue) {
		try {
			if (isKeyNotNull(obj, key)) {
				return obj.getInt(key);
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static int getInt(JSONArray ary, int index, int defaultValue) {
		try {
			if (ary != null) {
				if (ary.length() > index && index > -1) {
					return ary.getInt(index);
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static long getLong(JSONObject obj, String key, long defaultValue) {
		try {
			if (isKeyNotNull(obj, key)) {
				return obj.getLong(key);
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static double getDouble(JSONObject obj, String key, double defaultValue) {
		try {
			if (isKeyNotNull(obj, key)) {
				return obj.getDouble(key);
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static float getFloat(JSONObject obj, String key, float defaultValue) {
		try {
			if (isKeyNotNull(obj, key)) {
				return (float) obj.getDouble(key);
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static long getLong(JSONArray ary, int index, long defaultValue) {
		try {
			if (ary != null) {
				if (ary.length() > index && index > -1) {
					return ary.getLong(index);
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static JSONObject getJsonObject(JSONObject obj, String key, JSONObject defaultValue) {
		try {
			if (isKeyNotNull(obj, key)) {
				return obj.getJSONObject(key);
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static JSONObject getJsonObject(JSONArray ary, int index, JSONObject defaultValue) {
		try {
			if (ary != null) {
				if (ary.length() > index && index > -1) {
					return ary.getJSONObject(index);
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static JSONArray getJsonArray(JSONObject obj, String key, JSONArray defaultValue) {
		try {
			if (isKeyNotNull(obj, key)) {
				return obj.getJSONArray(key);
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static JSONArray getJsonArray(JSONArray ary, int index, JSONArray defaultValue) {
		try {
			if (ary != null) {
				if (ary.length() > index && index > -1) {
					return ary.getJSONArray(index);
				}
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
		return defaultValue;
	}
	
	public static void putObject(JSONObject jo, String key, Object value) {
		try {
			if (jo != null) {
				jo.put(key, value);
			}
		} catch (Throwable e) {
			DLog.e(e);
		}
	}
}
