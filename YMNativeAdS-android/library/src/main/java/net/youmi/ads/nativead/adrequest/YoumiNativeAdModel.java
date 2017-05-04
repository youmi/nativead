package net.youmi.ads.nativead.adrequest;

import net.youmi.ads.base.download.model.IFileDownloadTask;
import net.youmi.ads.nativead.BuildConfig;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 原生广告数据模型
 *
 * @author zhitao
 * @see YoumiNativeAdPicObject
 * @see YoumiNativeAdAppModel
 * @see YoumiNativeAdExtModel
 * @since 2017-04-13 10:58
 */
public class YoumiNativeAdModel implements IFileDownloadTask {
	
	private String adId;
	
	private String slotId;
	
	private String adName;
	
	private String adIconUrl;
	
	private ArrayList<YoumiNativeAdPicObject> adPics;
	
	private String slogan;
	
	private String subSlogan;
	
	private String url;
	
	private String uri;
	
	private int pt;
	
	private ArrayList<String> showUrls;
	
	private ArrayList<String> clickUrls;
	
	private YoumiNativeAdAppModel appModel;
	
	private YoumiNativeAdExtModel extModel;
	
	/**
	 * @return 广告ID
	 */
	public String getAdId() {
		return adId;
	}
	
	public void setAdId(String adId) {
		this.adId = adId;
	}
	
	/**
	 * @return 对应的广告位ID
	 */
	public String getSlotId() {
		return slotId;
	}
	
	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}
	
	/**
	 * @return 广告名字
	 */
	public String getAdName() {
		return adName;
	}
	
	public void setAdName(String adName) {
		this.adName = adName;
	}
	
	/**
	 * 获取广告图标地址
	 * <ul>
	 * <li>APP类广告一般会提供APP的ICON图标</li>
	 * <li>非APP类广告可能会没有提供广告ICON图标</li>
	 * </ul>
	 *
	 * @return 广告图标地址
	 *
	 * @see #getAdType()
	 */
	public String getAdIconUrl() {
		return adIconUrl;
	}
	
	public void setAdIconUrl(String adIconUrl) {
		this.adIconUrl = adIconUrl;
	}
	
	/**
	 * @return 广告展示的图片列表
	 *
	 * @see YoumiNativeAdPicObject
	 */
	public ArrayList<YoumiNativeAdPicObject> getAdPics() {
		return adPics;
	}
	
	public void setAdPics(ArrayList<YoumiNativeAdPicObject> adPics) {
		this.adPics = adPics;
	}
	
	/**
	 * @return 主广告标题
	 */
	public String getSlogan() {
		return slogan;
	}
	
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	
	/**
	 * @return 副广告标题
	 */
	public String getSubSlogan() {
		return subSlogan;
	}
	
	public void setSubSlogan(String subSlogan) {
		this.subSlogan = subSlogan;
	}
	
	/**
	 * 需要根据 {@link #getAdType()} 来判断广告类型，不同广告类型，此参数返回不同含义的地址
	 *
	 * @return <ul>
	 * <li>如果为APP广告类型，则返回APP广告的下载地址</li>
	 * <li>如果为WAP广告类型，则返回WAP广告的推广网页地址</li>
	 * </ul>
	 *
	 * @see #getAdType()
	 */
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * 需要根据 {@link #getAdType()} 来判断广告类型，不同广告类型，此参数返回不同含义的地址
	 *
	 * @return <ul>
	 * <li>如果为APP广告类型，则返回APP广告的某一指定页面的地址uri，需要将此uri通过 {@link android.content.Intent#parseUri(String, int)}方法转换为对应的Intent然后跳转</li>
	 * <li>如果为WAP广告类型，则返回null</li>
	 * </ul>
	 *
	 * @see #getAdType()
	 */
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	/**
	 * 广告类型
	 *
	 * @return <ul>
	 * <li>{@code 0}： APP广告</li>
	 * <li>{@code 1}： WAP网页推广告广告</li>
	 * </ul>
	 */
	public int getAdType() {
		return pt;
	}
	
	public void setAdType(int pt) {
		this.pt = pt;
	}
	
	/**
	 * @return <strong>重要：</strong> 广告曝光检测URL列表，开发者需要将此列表的所有URL都发送才算是完成广告曝光统计
	 */
	public ArrayList<String> getShowUrls() {
		return showUrls;
	}
	
	public void setShowUrls(ArrayList<String> showUrls) {
		this.showUrls = showUrls;
	}
	
	/**
	 * @return <strong>重要：</strong> 广告点击检测URL列表，开发者需要将此列表的所有URL都发送才算是完成广告点击统计
	 */
	public ArrayList<String> getClickUrls() {
		return clickUrls;
	}
	
	public void setClickUrls(ArrayList<String> clickUrls) {
		this.clickUrls = clickUrls;
	}
	
	/**
	 * 需要根据 {@link #getAdType()} 来判断广告类型，只有广告类型为APP广告，此参数才可能存在实例，否则为null
	 *
	 * @return APP广告的信息
	 *
	 * @see #getAdType()
	 * @see YoumiNativeAdAppModel
	 */
	public YoumiNativeAdAppModel getAppModel() {
		return appModel;
	}
	
	public void setAppModel(YoumiNativeAdAppModel appModel) {
		this.appModel = appModel;
	}
	
	/**
	 * 获取扩展参数信息
	 *
	 * @return 扩展参数信息对象
	 *
	 * @see YoumiNativeAdExtModel
	 */
	public YoumiNativeAdExtModel getExtModel() {
		return extModel;
	}
	
	public void setExtModel(YoumiNativeAdExtModel extModel) {
		this.extModel = extModel;
	}
	
	@Override
	public String toString() {
		if (BuildConfig.DEBUG) {
			final StringBuilder sb = new StringBuilder("YoumiNativeAdModel{");
			sb.append("\n  adId=").append(adId);
			sb.append("\n  slotId=").append(slotId);
			sb.append("\n  adName='").append(adName).append('\'');
			sb.append("\n  adIconUrl='").append(adIconUrl).append('\'');
			sb.append("\n  adPics=").append(adPics);
			sb.append("\n  slogan='").append(slogan).append('\'');
			sb.append("\n  subSlogan='").append(subSlogan).append('\'');
			sb.append("\n  url='").append(url).append('\'');
			sb.append("\n  uri='").append(uri).append('\'');
			sb.append("\n  pt=").append(pt);
			sb.append("\n  showUrls=").append(showUrls);
			sb.append("\n  clickUrls=").append(clickUrls);
			sb.append("\n  appModel=").append(appModel);
			sb.append("\n  extModel=").append(extModel);
			sb.append("\n}");
			return sb.toString();
		}
		return super.toString();
	}
	
	/**
	 * 原生广告图片数据模型
	 */
	public static class YoumiNativeAdPicObject implements Serializable {
		
		private String url;
		
		private int width;
		
		private int height;
		
		/**
		 * @return 图片地址
		 */
		public String getUrl() {
			return url;
		}
		
		public void setUrl(String url) {
			this.url = url;
		}
		
		/**
		 * @return 图片宽度（px）
		 */
		public int getWidth() {
			return width;
		}
		
		public void setWidth(int width) {
			this.width = width;
		}
		
		/**
		 * @return 图片高度（px）
		 */
		public int getHeight() {
			return height;
		}
		
		public void setHeight(int height) {
			this.height = height;
		}
		
		@Override
		public String toString() {
			if (BuildConfig.DEBUG) {
				final StringBuilder sb = new StringBuilder("YoumiNativeAdPicObject{");
				sb.append("\n  url='").append(url).append('\'');
				sb.append("\n  width=").append(width);
				sb.append("\n  height=").append(height);
				sb.append("\n}");
				return sb.toString();
			}
			return super.toString();
		}
	}
	
	/**
	 * APP类型原生广告的APP数据模型
	 */
	public static class YoumiNativeAdAppModel implements Serializable {
		
		private String packageName;
		
		private String description;
		
		private String size;
		
		private ArrayList<String> screenShots;
		
		private float score;
		
		private String category;
		
		/**
		 * @return 应用包名
		 */
		public String getPackageName() {
			return packageName;
		}
		
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}
		
		/**
		 * @return 应用描述
		 */
		public String getDescription() {
			return description;
		}
		
		public void setDescription(String description) {
			this.description = description;
		}
		
		/**
		 * @return 应用大小，结果为处理后的字符串, 格式形如"200M"
		 */
		public String getSize() {
			return size;
		}
		
		public void setSize(String size) {
			this.size = size;
		}
		
		/**
		 * @return 应用截图地址列表
		 */
		public ArrayList<String> getScreenShots() {
			return screenShots;
		}
		
		public void setScreenShots(ArrayList<String> screenShots) {
			this.screenShots = screenShots;
		}
		
		/**
		 * @return 应用评分，范围在{@code [1.0, 5.0]}
		 */
		public float getScore() {
			return score;
		}
		
		public void setScore(float score) {
			this.score = score;
		}
		
		/**
		 * @return 应用分类，如："游戏"
		 */
		public String getCategory() {
			return category;
		}
		
		public void setCategory(String category) {
			this.category = category;
		}
		
		@Override
		public String toString() {
			if (BuildConfig.DEBUG) {
				final StringBuilder sb = new StringBuilder("YoumiNativeAdAppModel{");
				sb.append("\n  packageName='").append(packageName).append('\'');
				sb.append("\n  description='").append(description).append('\'');
				sb.append("\n  size='").append(size).append('\'');
				sb.append("\n  screenShots=").append(screenShots);
				sb.append("\n  score=").append(score);
				sb.append("\n  category='").append(category).append('\'');
				sb.append("\n}");
				return sb.toString();
			}
			return super.toString();
		}
	}
	
	/**
	 * 原生广告扩展参数
	 */
	public static class YoumiNativeAdExtModel implements Serializable {
		
		private int io;
		
		private int delay;
		
		private int sal;
		
		private int pl;
		
		/**
		 * 浏览器打开方式
		 *
		 * @return <ul>
		 * <li>{@code 0}： 应该采用内部浏览器打开(默认)</li>
		 * <li>{@code 1}： 应该采用外部浏览器打开</li>
		 * </ul>
		 */
		public int getIo() {
			return io;
		}
		
		public void setIo(int io) {
			this.io = io;
		}
		
		/**
		 * @return 关闭按钮延迟显示的时间（单位：秒）
		 */
		public int getDelay() {
			return delay;
		}
		
		public void setDelay(int delay) {
			this.delay = delay;
		}
		
		/**
		 * 是否需要显示《广告》标识
		 *
		 * @return <ul>
		 * <li>{@code 0}： 不显示</li>
		 * <li>{@code 1}： 显示</li>
		 * </ul>
		 */
		public int getSal() {
			return sal;
		}
		
		public void setSal(int sal) {
			this.sal = sal;
		}
		
		/**
		 * 是否需要显示平台标识
		 *
		 * @return <ul>
		 * <li>{@code 0}： 不显示</li>
		 * <li>{@code 1}： 显示</li>
		 * </ul>
		 */
		public int getPl() {
			return pl;
		}
		
		public void setPl(int pl) {
			this.pl = pl;
		}
		
		@Override
		public String toString() {
			if (BuildConfig.DEBUG) {
				final StringBuilder sb = new StringBuilder("YoumiNativeAdExtModel{");
				sb.append("\n  io=").append(io);
				sb.append("\n  delay=").append(delay);
				sb.append("\n  sal=").append(sal);
				sb.append("\n  pl=").append(pl);
				sb.append("\n}");
				return sb.toString();
			}
			return super.toString();
		}
	}
}
