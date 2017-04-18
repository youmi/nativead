package net.youmi.ads.base.download.listener;

import net.youmi.ads.base.download.base.DownloadStatus;
import net.youmi.ads.base.download.model.FileDownloadTask;

/**
 * @author zhitao
 * @since 2017-04-14 15:47
 */
public interface OnDownloadListener {
	
	int DOWNLOAD_BEFORE_START_FILE_LOCK = 1;
	
	int DOWNLOAD_START = 2;
	
	int DOWNLOAD_ING = 3;
	
	int DOWNLOAD_SUCCESS = 4;
	
	int DOWNLOAD_SUCCESS_FILE_ALREADY_EXIST = 5;
	
	int DOWNLOAD_FAILED = 6;
	
	int DOWNLOAD_STOP = 7;
	
	/**
	 * 如果支持多进程下载，那么就会回调这里
	 * <p>
	 * 如果刚刚调用停止下载的代码之后，立即点击再次重新下载的话，可能就会到这里）
	 * 添加下载开始之前，文件处于文件锁的回调
	 * <p>
	 * 通知任务当前处于文件锁
	 * <p>
	 * 一般需要重写的场合为SDK:
	 * <p>
	 * 因为sdk一般是很多个app同时使用的，所以下载的文件基本是公用的，
	 * 这个时候，在下载之前就需要检查下下载文件是否已经被其他进程读取中，
	 * 如果是的话，这里要通知一下
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	void onDownloadBeforeStartWithFileLock(FileDownloadTask fileDownloadTask);
	
	/**
	 * 下载开始
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	void onDownloadStart(FileDownloadTask fileDownloadTask);
	
	/**
	 * 下载进度回调：之类需要实现具体业务逻辑
	 *
	 * @param fileDownloadTask 下载任务模型
	 * @param totalLength      本次下载文件的总长度
	 * @param completeLength   已经完成的长度
	 * @param percent          当前完成百分比
	 * @param speedBytes       当前下载速度:每intervalTime_ms毫秒下载的长度(单位:bytes)
	 * @param intervalTime_ms  当前下载速度时间单位:每intervalTime_ms毫秒回回调一次本方法(单位:bytes)
	 */
	void onDownloadProgressUpdate(FileDownloadTask fileDownloadTask, long totalLength, long completeLength, int percent,
			long speedBytes, long intervalTime_ms);
	
	/**
	 * 下载成功
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	void onDownloadSuccess(FileDownloadTask fileDownloadTask);
	
	/**
	 * 下载成功，文件本来就存在于本地
	 *
	 * @param fileDownloadTask 下载任务模型
	 */
	void onFileAlreadyExist(FileDownloadTask fileDownloadTask);
	
	/**
	 * 下载失败
	 *
	 * @param fileDownloadTask 下载任务模型
	 * @param downloadStatus   下载任务失败数据模型
	 */
	void onDownloadFailed(FileDownloadTask fileDownloadTask, DownloadStatus downloadStatus);
	
	/**
	 * 下载暂停
	 *
	 * @param fileDownloadTask 下载任务模型
	 * @param totalLength      本次下载的总长度
	 * @param completeLength   已下载的长度
	 * @param percent          下载停止时，已经完成的百分比
	 */
	void onDownloadStop(FileDownloadTask fileDownloadTask, long totalLength, long completeLength, int percent);
	
}
