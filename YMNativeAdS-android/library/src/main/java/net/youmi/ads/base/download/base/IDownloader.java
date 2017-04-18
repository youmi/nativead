package net.youmi.ads.base.download.base;

import net.youmi.ads.base.download.model.FileDownloadTask;

/**
 * @author zhitao
 * @since 2017-04-14 15:50
 */
public interface IDownloader {
	
	/**
	 * 开始下载
	 *
	 * @return 下载的最终状态
	 */
	DownloadStatus download();
	
	/**
	 * @return 获取本次下载的任务模型
	 */
	FileDownloadTask getFileDownloadTask();
	
	/**
	 * @return 获取本次下载文件的总长度
	 */
	long getDownloadFileFinalLength();
	
	/**
	 * @return 获取已经完成的长度
	 */
	long getCompleteLength();
	
	/**
	 * @return 获取下载进度的百分比
	 */
	int getDownloadPercent();
	
	/**
	 * 停止下载过程
	 * <p>
	 * 调用下载停止之后，并不会立即停止下载的，而是程序跑到判断是否需要停止下载的地方才会停止的，
	 */
	void stop();
	
	/**
	 * 是否被停止，只有调用了{@link #stop()}才会变为true，而调用了{@link #download()}之后就会变为false
	 *
	 * @return 是否被停止
	 *
	 * @see #isRunning()
	 */
	boolean isStop();
	
	/**
	 * 是否正在下载中，调用了{@link #download()}之后就会变为true，下载结束或者调用了{@link #stop()}都会变为false
	 * <p>
	 * 和 {@link #isStop()}的区别
	 * <ol>
	 * <li>任务下载结束(成功或者失败)，或者被用户主动停止下载任务，isRunning都为false，</li>
	 * <li>只有用户主动停止下载任务（即调用{@link #stop()}，isStop才会是true，当再次调用{@link #download()}方法之后，isStop就会变为false</li>
	 * </ol>
	 *
	 * @return 是否在正下载中
	 *
	 * @see #isStop()
	 */
	boolean isRunning();
}
