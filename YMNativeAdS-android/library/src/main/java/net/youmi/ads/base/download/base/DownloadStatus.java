package net.youmi.ads.base.download.base;

/**
 * @author zhitao
 * @since 2017-04-14 15:48
 */
public class DownloadStatus {
	
	private Throwable mThrowable;
	
	private int mDownloadStatusCode;
	
	/**
	 * @param downloadStatusCode 下载最终状态码{@link DownloadStatus.Code}
	 * @param throwable          下载失败时的异常信息，可以用于异常上报
	 */
	public DownloadStatus(int downloadStatusCode, Throwable throwable) {
		mThrowable = throwable;
		mDownloadStatusCode = downloadStatusCode;
	}
	
	/**
	 * @param downloadStatusCode 下载最终状态码{@link DownloadStatus.Code}
	 */
	public DownloadStatus(int downloadStatusCode) {
		mDownloadStatusCode = downloadStatusCode;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("FinalDownloadStatus {\n");
		sb.append("  mThrowable=").append(mThrowable).append("\n");
		sb.append("  mDownloadStatusCode=").append(mDownloadStatusCode).append("\n");
		sb.append('}');
		return sb.toString();
	}
	
	/**
	 * 获取下载失败时说抛出来的异常
	 *
	 * @return
	 */
	public Throwable getThrowable() {
		return mThrowable;
	}
	
	/**
	 * 获取下载最终状态码
	 *
	 * @return {@link DownloadStatus.Code}
	 */
	public int getDownloadStatusCode() {
		return mDownloadStatusCode;
	}
	
	/**
	 * 下载最终状态码
	 * <p/>
	 * 定义下载失败的代码常量范围为在[100, 199]不能超过
	 * <p/>
	 * 可重试失败区间[100, 149]
	 * <p/>
	 * 不可重试失败区间[150, 199]
	 */
	public static class Code {
		
		/**
		 * 下载成功
		 */
		public final static int SUCCESS = 0;
		
		/**
		 * 下载停止：用户终止下载流程，不可重试下载
		 */
		public final static int STOP = 1;
		
		// --------------- 可以重试的失败类型区间[100, 149] ---------------
		
		/**
		 * 下载失败：其他已知不影响重要流程的异常，比如网络问题等，可以重试下载
		 */
		public final static int EXCEPTION_UNKNOWN = 100;
		
		//		/**
		//		 * 下载失败：网络异常
		//		 */
		//		public final static int EXCEPTION_NETWORK = 101;
		
		/**
		 * 下载失败：下载过程中，下载文件可能被改动了，可以重试下载，不过需要在重新下载的时候删除文件
		 */
		public final static int EXCEPTION_DOWNLOADFLIE_CHANGE = 102;
		
		/**
		 * 下载失败：下载的缓存文件重命名为最终文件失败，可以重试下载
		 */
		public final static int EXCEPTION_RENAME_TEMPFILE_TO_STOREFILE = 103;
		
		// --------------- 不可以重试的失败类型区间[150, 199] ---------------
		
		/**
		 * 下载失败，未知异常，不可重试下载
		 */
		public final static int ERROR_UNKNOWN = 150;
		
		/**
		 * 下载失败：错误参数，不可重试下载
		 */
		public final static int ERROR_PARAMS = 151;
		
		/**
		 * 下载失败：遇到http错误码，不可重试下载
		 */
		public final static int ERROR_HTTP_CODE = 152;
		
		/**
		 * 下载失败：返回的http参数有误，如ContentLength 无法获取到，不可以重试下载
		 */
		public final static int ERROR_HTTP_RESPONSE = 153;
		//
		//		/**
		//		 * 下载失败：下载的文件目录出现问题，不可重试下载
		//		 */
		//		public final static int FAILED_ERROR_DIR = 154;
		
		/**
		 * 下载失败：重试几次之后还是下载失败就会回调这个，不可继续重试下载
		 */
		public final static int ERROR_REACH_MAX_DOWNLOAD_TIMES = 155;
		
		/**
		 * 下载失败：下载的缓存文件路径或者最终存放路径有一个为目录，不可重试下载
		 */
		public final static int ERROR_LOCAL_FILE_TYPE = 156;
		
		/**
		 * 下载失败：下载成功时，检查下载结果时发现最终下载文件无效时的错误，不可重试下载
		 */
		public final static int ERROR_DEST_FILE_INVALID = 157;
		
		/**
		 * 下载失败：下载之前发现最终存储文件存在，但是不通过检验，尝试删除该文件以重新下载时，发现该文件无法被删除，因此归类为不可重试下载
		 */
		public final static int ERROR_DEST_FILE_CANNOT_DEL = 158;
		
		/**
		 * 下载失败：多进程下载时，达到获取conent-length最大上线数 不可重试下载
		 */
		public final static int ERROR_REACH_MAX_GET_CONTENT_LENGTH_TIMES_INMULTPROCESSES = 159;
		
		/**
		 * 下载失败：下载目录相关参数无效，如目录不可写之类
		 */
		public final static int ERROR_DOWNLOAD_DIR_SETTINGS_INVALID = 160;
		
	}
}
