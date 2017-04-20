package net.youmi.ads.base.download.listener;

import net.youmi.ads.base.download.model.FileDownloadTask;

/**
 * @author zhitao
 * @since 2017-04-14 16:19
 */
public interface IFileAvailableChecker {
	
	/**
	 * 下载完成时，检查文件是否可以用
	 * <p>
	 * 使用场合：
	 * 如果你觉得你下载的文件被劫持为另一个url，那么就可以实现一下下面的接口
	 * <p>
	 * 如：
	 * <ul>
	 * <li>重新计算文件的md5和task中的md5(服务器那边返回的比较一下)</li>
	 * <li>或者重新向服务器请求这个文件的md5然后做对比</li>
	 * </ul>
	 *
	 * @param fileDownloadTask 下载任务
	 *
	 * @return true： 有效； false： 无效
	 */
	boolean isStoreFileAvailable(FileDownloadTask fileDownloadTask);
}
