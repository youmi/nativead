# 有米Android原生广告开源SDK使用说明

## 1. 前言

欢迎您使用有米Android原生广告开源SDK接口，请您参照此文档步骤来使用开源SDK，或可以参考我们的Demo程序源码来使用开源SDK。

以下几点注意事项，请仔细阅读：

1. 一个应用APPID只能使用在单个APP内，不能多个APP重复使用。
2. 嵌入完成，将获取到测试广告，如果测试没问题，需要到有米开发者后台审核上传应用进行审核，审核通过后方可获得正式广告，并且产生正常收益。
3. 每个应用可以创建多个广告位，广告位可以设定不同的广告图片尺寸，具体可以登录 [有米开发者后台](http://app.youmi.net) 查看。
4. 在使用开源API接口的同时，需要您在自己的应用中开辟出适合的位置，并设置好正确的广告逻辑。

## 2. 使用说明

有米Android原生广告开源SDK实质上是对我们的 [API协议文档](https://github.com/youmi/nativead/blob/master/docs/%E6%9C%89%E7%B1%B3%E5%8E%9F%E7%94%9F%E5%B9%BF%E5%91%8AAPI%E6%96%87%E6%A1%A3.md) 进行了一系列封装，开发者可以自行根据协议编写实际逻辑，但是我们更加建议你使用我们的SDK进行快速嵌入。

在使用我们的SDK时，你可以

1. 下载本目录下SDK源码以进行引用，或者在此基础上进行修改至适合项目使用。
2. 我们更建议你直接使用本SDK，按照下面方法进行快速嵌入

### 2.1 下载并导入sdk

``` gradle
// 依赖release版本的aar [开发者正式版本的apk引用]
compile 'net.youmi.ads:nativead:1.2.0:release@aar'

// 依赖debug版本的aar [开发者需要调试时可以引用]
//
// debug版本aar相比release的aar:
//
// 1. 能输出网络、下载相关的相关log
// 2. 对外提供的数据模型，都重写了toString方法，使其会详细打印出各个成员变量的值
// 3. 部分错误将会捕捉并输出
// 4. Logcat中过滤Tag为 Sdk ，即为本Sdk输出的Log
//
// 基于上面两点，建议开发者在发现疑点时才使用这个版本的aar进行更多的log输出，正式发布时强烈建议使用release版本
// compile 'net.youmi.ads:nativead:1.2.0:debug@aar'
```

### 2.2 快速使用

#### 2.2.1 初始化

```java
YoumiNativeAdHelper

	// 初始化配置原则上应用生命周期中调用一次则可
	.initConfig(Context context)
	
	// 初始化appId
	.withAppId(String appId);
```

#### 2.2.2 发起一个广告位请求

``` java
YoumiNativeAdHelper
	
	// 创建一个原生广告请求
	.newAdRequest(Context context)
	
	// （必须）设置应用APPID
	.withAppId(String appId)
	
	// （必须）设置请求广告位Id
	.withSlotId(String slotId)
	
	// （可选）设置请求广告数量，默认为1（实际返回的广告数量小于等于设置的请求广告数量）
	.withRequestCount(int adCount)
	
	// （可选）设置性别（M：男性; F：女性）
	.withGender(String gender)
	
	// （可选）设置年龄
	.withAge(String age)
	
	// （可选）设置内容标题
	.withContentTitle(String contTitle)
	
	// （可选）设置内容关键词
	.withContentKeyword(String contKeyword)
	
	// （可选）设置请求唯一标识
	.withReqId(String reqId)
	
	// （可选）设置UserAgent
	.withUserAgent(String userAgent)
	
	// 发起同步请求（无需传入参数）
	// .request();
	
	// 发起异步请求（需要传入 OnYoumiNativeAdRequestListener 对象）
	.request(OnYoumiNativeAdRequestListener listener);
```

**e.g.** 

``` java
// 发起一个同步请求 
YoumiNativeAdResposeModel model = YoumiNativeAdHelper
				.newAdRequest(context)
				.withSlotId(slotId)
				.request();

// 获取到返回结果后，判断其是否有效
if (model == null) {
	Toast.makeText(adLargeFragment.getActivity(), "请求失败，返回结果：null", Toast.LENGTH_SHORT).show();
	return;
}

// 判断返回状态码，不为0都是有问题的
if (respModel.getCode() != 0) {
	Toast.makeText(
		this,
		String.format(Locale.getDefault(), "请求失败，错误代码：%d", respModel.getCode()),
		Toast.LENGTH_SHORT
	).show();
	return;
}

// 获取返回的请求广告列表
ArrayList<YoumiNativeAdModel> adModels = respModel.getAdModels();
if (adModels == null || adModels.isEmpty()) {
	return;
}

// 因为默认只请求一个广告，所以这里就直接取0下标
YoumiNativeAdModel adModel = adModels.get(0);

// 下面可以进行图片加载等布局相关操作
// ... 
```

#### 2.2.3 发送广告曝光效果记录

``` java
YoumiNativeAdHelper

	// 创建一个广告效果记录请求
	.newAdEffRequest(Context context)
	
	// （必须）设置要发送的广告
	.withYoumiNativeAdModel(YoumiNativeAdModel adModel)
	
	// （可选）设置效果记录发送失败时的重试次数，默认为5次
	.withMaxRetryCount(5)
	
	// 同步发送曝光记录记录
	//.syncSendShowEff();
	
	// 异步发送曝光效果记录
	.asyncSendShowEff();
```

#### 2.2.4 发送广告点击效果记录

``` java
YoumiNativeAdHelper

	// 创建一个广告效果记录请求
	.newAdEffRequest(Context context)
	
	// （必须）设置要发送的广告
	.withYoumiNativeAdModel(YoumiNativeAdModel adModel)
	
	// （可选）设置效果记录发送失败时的重试次数，默认为5次
	.withMaxRetryCount(5)
	
	// 同步发送曝光记录记录
	//.syncSendClickEff();
	
	// 异步发送曝光效果记录
	.asyncSendClickEff();
```

### 2.2.5 发送下载完成效果记录

**如果使用sdk自带下载管理器，sdk自带下载管理器就会自动在下载完成时发送**

``` java
YoumiNativeAdHelper

	// 创建一个广告效果记录请求
	.newAdEffRequest(Context context)
	
	// （必须）设置要发送的广告
	.withYoumiNativeAdModel(YoumiNativeAdModel adModel)
	
	// （可选）设置效果记录发送失败时的重试次数，默认为5次
	.withMaxRetryCount(5)
	
	// 同步发送下载完成效果记录
	//.syncSendDownloadSuccessEff();
	
	// 异步发送下载完成效果记录
	.asyncSendDownloadSuccessEff();
```

### 2.2.6 发送安装完成效果记录

**如果使用sdk自带下载管理器，sdk自带下载管理器就会自动在安装完成时发送**

``` java
YoumiNativeAdHelper

	// 创建一个广告效果记录请求
	.newAdEffRequest(Context context)
	
	// （必须）设置要发送的广告
	.withYoumiNativeAdModel(YoumiNativeAdModel adModel)
	
	// （可选）设置效果记录发送失败时的重试次数，默认为5次
	.withMaxRetryCount(5)
	
	// 同步发送安装完成效果记录
	//.syncSendInstallSuccessEff();
	
	// 异步发送安装完成效果记录
	.asyncSendInstallSuccessEff();
```

#### 2.2.7 下载或打开广告

须知：

1. 广告类型目前有 **APP类型** 和 **WAP类型** 广告，两者在点击后的流程存在不同
2. 根据 ``YoumiNativeAdModel#getAdType()`` 方法可以获取广告当前的类型

##### 2.2.7.1 APP类型广告下载打开

流程：

1. 如果app还没有安装，则使用 ``YoumiNativeAdModel#getUrl()`` 进行下载安装，安装完毕后，检查 ``YoumiNativeAdModel#getUri()`` 
是否存在，存在就打开 ``YoumiNativeAdModel#getUri()`` ，不存在就直接根据包名打开app，跳转逻辑结束
2. 如果app已经安装，``YoumiNativeAdModel#getUri()`` 存在，则把uri解析为Intent，然后打开Intent，跳转逻辑结束
3. 如果app已经安装，``YoumiNativeAdModel#getUri()`` 不存在，这根据包名打开app，跳转逻辑结束

开发者可以自行实现上述流程，也可以使用sdk已经封装好的API，直接使用下面代码即可实现

<a name="app-download" id="app-download"></a>

###### 1. 下载或者打开广告

**注意：**

1. 如果需要在Android 7.0系统以上能打开下载这个安装通知，需要配置FileProvider，可以参考demo配置

``` java
if (!PackageUtils.isPakcageInstall(this, mYoumiNativeAdModel.getAppModel().getPackageName())) {
			
	// 如果广告还没有安装的话，就创建一个广告下载任务
	YoumiNativeAdHelper.newAdDownload(this)
	
		// （必须）指定下载的广告
		.withYoumiNativeAdModel(mYoumiNativeAdModel)
		
		// （可选）是否显示下载过程中的通知栏提示（默认为true：显示）
		.showDownloadNotification(true)
		
		// （可选）下载成功后是否打开安装界面（默认为false：不打开）
		.installApkAfterDownloadSuccess(true)
		
		// （可选）安装成功后是否打开应用（默认为false：不打开）
		.startAppAfterInstalled(true)
		
		// （可选）安装成功后是否删除对应的APK文件（默认为true：立即删除）
		// 此方法需要设置安装成功后打开广告应用的方法才生效，即调用了 startAppAfterInstalled(true) 才生效
		.deleteApkAfterInstalled(true)
		
		// （可选）广告下载成功之后是否自动发送下载成功效果记录（默认为true：发送）
		.sendDownloadSuccessEff(true)
		
		// （可选）广告安装成功之后是否自动发送安装成功效果记录（默认为true：发送）
		// 此方法需要设置下载成功之后打开应用安装界面之后才可能生效，即调用了 installApkAfterDownloadSuccess（true)
		.sendInstallSuccessEff(true)
		
		// 开始下载
		.download();
} else {
	
	// 如果广告已经安装的话就直接打开
	YoumiNativeAdHelper.openApp(this, mYoumiNativeAdModel);
}
```

###### 2. 停止下载

``` java
YoumiNativeAdHelper.stopDownload(YoumiNativeAdModel adModel);
```

###### 3. 添加下载监听器

``` java
YoumiNativeAdHelper.addOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener listener);
```

###### 4. 移除下载监听器

``` java
YoumiNativeAdHelper.removeOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener listener);
```

#### 2.2.7.2 WAP类型广告相关处理

采用外部浏览器或者内部WebView打开 ``YoumiNativeAdModel#getUrl()`` 即可，更多实现可以参考demo中实现。

e.g.

```
public class Utils {
	
	public static void startActivity2OpenUrl(Context context, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		if (!(context instanceof Activity)) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);
	}
}


Utils.startActivity2OpenUrl(this, adModel.getUrl);
```


### 2.3 更多使用

更多使用请参考demo示例