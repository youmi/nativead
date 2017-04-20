package net.youmi.ads.nativead.demo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.youmi.ads.nativead.demo.ad.splash.SplashActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author zhitao
 * @since 2017-04-20 15:28
 */
@RuntimePermissions
public class PermissionCheckActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PermissionCheckActivityPermissionsDispatcher.startSplashActivityWithCheck(this);
	}
	
	@NeedsPermission({ Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE })
	void startSplashActivity() {
		startActivity(new Intent(this, SplashActivity.class));
		finish();
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		PermissionCheckActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
	}
	
	@OnPermissionDenied({ Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE })
	void onPermissionDenied() {
		Toast.makeText(this, "缺少必要权限，将自动退出", Toast.LENGTH_SHORT).show();
		finish();
	}
	
	@OnNeverAskAgain({ Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE })
	void onNeverAskAgain() {
		Toast.makeText(this, "缺少必要权限，无法打开，请进入系统设置将权限开启", Toast.LENGTH_SHORT).show();
		finish();
	}
}
