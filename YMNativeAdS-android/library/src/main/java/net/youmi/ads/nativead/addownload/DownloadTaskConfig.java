package net.youmi.ads.nativead.addownload;

import net.youmi.ads.base.download.model.IFileDownloadTask;

/**
 * @author zhitao
 * @since 2017-04-20 09:46
 */
class DownloadTaskConfig implements IFileDownloadTask {
	
	private boolean isShowDefaultNotification = true;
	
	private boolean isNeed2StartInstallAfterDownloadSuccess = false;
	
	private boolean isNeed2OpenAppAfterInstalled = false;
	
	private boolean isNeed2DeleteApkAfterInstalled = true;
	
	DownloadTaskConfig() {
	}
	
	boolean isShowDefaultNotification() {
		return isShowDefaultNotification;
	}
	
	void setShowDefaultNotification(boolean showDefaultNotification) {
		isShowDefaultNotification = showDefaultNotification;
	}
	
	boolean isNeed2StartInstallAfterDownloadSuccess() {
		return isNeed2StartInstallAfterDownloadSuccess;
	}
	
	void setNeed2StartInstallAfterDownloadSuccess(boolean need2StartInstallAfterDownloadSuccess) {
		isNeed2StartInstallAfterDownloadSuccess = need2StartInstallAfterDownloadSuccess;
	}
	
	boolean isNeed2OpenAppAfterInstalled() {
		return isNeed2OpenAppAfterInstalled;
	}
	
	void setNeed2OpenAppAfterInstalled(boolean need2OpenAppAfterInstalled) {
		isNeed2OpenAppAfterInstalled = need2OpenAppAfterInstalled;
	}
	
	boolean isNeed2DeleteApkAfterInstalled() {
		return isNeed2DeleteApkAfterInstalled;
	}
	
	void setNeed2DeleteApkAfterInstalled(boolean need2DeleteApkAfterInstalled) {
		isNeed2DeleteApkAfterInstalled = need2DeleteApkAfterInstalled;
	}
}
