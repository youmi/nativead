package net.youmi.ads.base.download.model;

import android.text.TextUtils;
import android.util.SparseArray;

import net.youmi.ads.base.hash.MD5;
import net.youmi.ads.base.log.DLog;

import java.io.File;

/**
 * 下载任务模型
 * <p>
 * 默认情况下：本类会以原始下载url的md5后32位字符串的hashcode作为本类示例的hashcode
 * <p>
 * <b>But</b>
 * 有时候，同一个资源下载，可能不同时刻创建的下载url会不同，如url中加入了请求时间戳，那么每次的下载url都会不同
 * 但是其实还是同一个下载任务，那么这个时候需要调用 {@link #setIdentify(String)} 进行自定义本类的hashcode，标记上面话说的这种url其实都是同一个对象
 *
 * @author zhitao
 * @since 2017-04-14 15:32
 */
public final class FileDownloadTask {
	
	/**
	 * 原始文件下载链接(可能为302/301的url) 也有可能直接等于 {@link #mDestDownloadUrl}
	 */
	private String mRawDownloadUrl;
	
	/**
	 * 文件最终下载地址(最后的下载地址,如:***.apk)
	 * <p/>
	 * 默认是等于原始地址
	 * <p/>
	 * 如果下载的URL是重定向类型，那么在重定向到最终地址时，这个值需要通过{@link #setDestDownloadUrl(String)} 进行修改
	 */
	private String mDestDownloadUrl;
	
	/**
	 * 缓存文件位置，进入下载之前创建，不在创建对象的时候传入
	 * <p/>
	 * 下载时会先下载在缓存文件的位置上,下载成功之后会重命名缓存文件为最终文件的名字（缓存文件会被删除）
	 */
	private File mTempFile;
	
	/**
	 * 文件的最终保存位置，创建对象的时候传入
	 */
	private File mStoreFile;
	
	/**
	 * 本次下载文件在服务器中的md5校验码
	 */
	private String mDownloadFileMd5sum;
	
	/**
	 * 缓存文件的下载总长度
	 */
	private long mTotalLength = -1;
	
	/**
	 * 定义本次任务的下载进度回调时间间隔为每1秒回调一次
	 */
	private int mIntervalTime_ms = 1000;
	
	/**
	 * 额外业务对象的存储模型
	 */
	private SparseArray<IFileDownloadTask> mIFileDownloadTaskSparseArray;
	
	private String mIdentify = null;
	
	/**
	 * @param rawDownloadUrl     原始下载url
	 * @param downloadFileMd5Sum 服务器上本次下载文件的md5值，用于下载完毕后的校验，可以不传入来
	 * @param totalLength        下载文件的总长度，用于下载完毕后的校验，可以不传入来(即传入-1)
	 * @param intervalTime_ms    本次任务的下载进度回调时间间隔(默认为1000ms)，可以不传入来(即传入-1)
	 */
	public FileDownloadTask(String rawDownloadUrl, String downloadFileMd5Sum, long totalLength, int intervalTime_ms) {
		mRawDownloadUrl = rawDownloadUrl;
		mDestDownloadUrl = mRawDownloadUrl;
		mDownloadFileMd5sum = downloadFileMd5Sum;
		if (totalLength > 0) {
			mTotalLength = totalLength;
		}
		if (intervalTime_ms > 0) {
			mIntervalTime_ms = intervalTime_ms;
		}
	}
	
	public FileDownloadTask(String rawDownloadUrl, String downloadFileMd5sum) {
		this(rawDownloadUrl, downloadFileMd5sum, -1, -1);
	}
	
	public FileDownloadTask(String rawDownloadUrl) {
		this(rawDownloadUrl, null);
	}
	
	/**
	 * 获取传入来的标识，不等于本类的hashcode
	 *
	 * @return 下载任务标识
	 */
	public String getIdentify() {
		return mIdentify;
	}
	
	/**
	 * 设置本类的唯一标示，不掉用的话，本类会默认用原始下载地址的url的hashcode作为本类的hashcode
	 * <p>
	 * 本方法的使用场合：如果同一个下载资源，可能会有多个原始下载地址的url（如url中加入了请求时间戳），那么建议用本方法设置一下新的标识(hashcode)
	 *
	 * @param identify 任务标识
	 */
	public void setIdentify(String identify) {
		mIdentify = identify;
	}
	
	/**
	 * @return 检查当前下载任务是否可用
	 */
	public boolean isValid() {
		if (TextUtils.isEmpty(mRawDownloadUrl)) {
			return false;
		}
		if (hashCode() == 0) {
			return false;
		}
		if (mTempFile == null) {
			return false;
		}
		
		if (mStoreFile == null) {
			return false;
		}
		return true;
	}
	
	public SparseArray<IFileDownloadTask> getIFileDownloadTaskSparseArray() {
		return mIFileDownloadTaskSparseArray;
	}
	
	public void setIFileDownloadTaskSparseArray(SparseArray<IFileDownloadTask> IFileDownloadTaskSparseArray) {
		mIFileDownloadTaskSparseArray = IFileDownloadTaskSparseArray;
	}
	
	public void addIFileDownloadTask(int key, IFileDownloadTask iFileDownloadTask) {
		if (mIFileDownloadTaskSparseArray == null) {
			mIFileDownloadTaskSparseArray = new SparseArray<>();
		}
		mIFileDownloadTaskSparseArray.put(key, iFileDownloadTask);
	}
	
	/**
	 * @param destDownloadUrl 设置文件的最终下载地址
	 */
	public void setDestDownloadUrl(String destDownloadUrl) {
		mDestDownloadUrl = destDownloadUrl;
	}
	
	/**
	 * @return 获取文件下载地址(最终下载地址)
	 */
	public String getDestDownloadUrl() {
		return mDestDownloadUrl;
	}
	
	/**
	 * @return 获取原始的url
	 */
	public String getRawDownloadUrl() {
		return mRawDownloadUrl;
	}
	
	/**
	 * @return 获取长度
	 */
	public long getTotalLength() {
		return mTotalLength;
	}
	
	/**
	 * @return 获取下载文件的md5校验码(等于最终下载url的md5)
	 */
	public String getDownloadFileMd5sum() {
		return mDownloadFileMd5sum;
	}
	
	/**
	 * @return 获取最终存储的文件地址
	 */
	public File getStoreFile() {
		return mStoreFile;
	}
	
	/**
	 * 设置最终存储的文件地址
	 *
	 * @param storeFile 最终存储的文件地址
	 */
	public void setStoreFile(File storeFile) {
		mStoreFile = storeFile;
	}
	
	/**
	 * @return 获取缓存文件的地址，可能为空，只有在开始下载和下载过程中，才会可能获取到真实的缓存地址
	 */
	public File getTempFile() {
		return mTempFile;
	}
	
	/**
	 * 设置缓存文件的地址，在下载开始的时候才设置
	 *
	 * @param mTempFile 缓存文件
	 */
	public void setTempFile(File mTempFile) {
		this.mTempFile = mTempFile;
	}
	
	/**
	 * @return 本次下载任务的回调进度时间间隔
	 */
	public int getIntervalTime_ms() {
		return mIntervalTime_ms;
	}
	
	/**
	 * 如果有调用 {@link #setIdentify(String)} 方法设置本类的唯一标示
	 * 那么hashcode优先使用传入来的标识的md5字符串的hashcode作为本类的hashcode
	 * 否则就用原始url的md5字符串的hashcode作为本类的hashcode
	 *
	 * @return hashcode
	 */
	@Override
	public int hashCode() {
		if (mIdentify != null) {
			mIdentify = mIdentify.trim();
		}
		if (!TextUtils.isEmpty(mIdentify)) {
			String identifyMd5 = MD5.md5(mIdentify);
			if (!TextUtils.isEmpty(identifyMd5)) {
				return identifyMd5.hashCode();
			}
		}
		String temp = MD5.md5(mRawDownloadUrl);
		if (TextUtils.isEmpty(temp)) {
			return 0;
		} else {
			return temp.hashCode();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null && o.hashCode() == this.hashCode();
	}
	
	@Override
	public String toString() {
		if (DLog.isDownloadLog) {
			final StringBuilder sb = new StringBuilder("FileDownloadTask{");
			sb.append("\n  mRawDownloadUrl='").append(mRawDownloadUrl).append('\'');
			sb.append("\n  mDestDownloadUrl='").append(mDestDownloadUrl).append('\'');
			sb.append("\n  mTempFile=").append(mTempFile);
			sb.append("\n  mStoreFile=").append(mStoreFile);
			sb.append("\n  mDownloadFileMd5sum='").append(mDownloadFileMd5sum).append('\'');
			sb.append("\n  mTotalLength=").append(mTotalLength);
			sb.append("\n  mIntervalTime_ms=").append(mIntervalTime_ms);
			sb.append("\n  mIFileDownloadTaskSparseArray=").append(mIFileDownloadTaskSparseArray);
			sb.append("\n  mIdentify='").append(mIdentify).append('\'');
			sb.append("\n}");
			return sb.toString();
		}
		return super.toString();
	}
}