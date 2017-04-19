package net.youmi.ads.nativead.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author zhitao
 * @since 2017-04-19 10:38
 */
public class Utils {
	
	public static void startActivity2OpenUrl(Context context, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		if (!(context instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}
	
	public static void startActivity2OpenUrlWithChooser(Context context, String url) {
		Intent intent = Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), "choose browser to open");
		if (!(context instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}
	
}
