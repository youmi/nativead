# 有米Android原生广告开源SDK使用说明

## 1. 前言

欢迎您使用有米Android原生广告开源SDK接口，请您参照此文档步骤来使用开源SDK，或可以参考我们的Demo程序源码来使用开源SDK。

以下几点注意事项，请仔细阅读：

1. 一个应用APPID只能使用在单个APP内，不能多个APP重复使用。
2. 嵌入完成，将获取到测试广告，如果测试没问题，需要到有米开发者后台审核上传应用进行审核，审核通过后方可获得正式广告，并且产生正常收益。
3. 每个应用可以创建多个广告位，广告位可以设定不同的广告图片尺寸，具体可以登录 [有米开发者后台](http://app.youmi.net) 查看。
4. 在使用开源API接口的同时，需要您在自己的应用中开辟出适合的位置，并设置好正确的广告逻辑。

## 2. 使用说明

有米Android原生广告开源SDK实质上是对我们的 [API协议文档](https://github.com/youmi/nativead/blob/master/docs/%E6%9C%89%E7%B1%B3%E5%8E%9F%E7%94%9F%E5%B9%BF%E5%91%8AAPI%E6%96%87%E6%A1%A3.md) 进行了一系列封装，开发者可以自行根据协议编写实际逻辑，但是我们更加建议你使用我们的SDK进行快速嵌入

在使用我们的SDK时，你可以

1. 下载SDK源码进行引用，或者在此基础上进行修改至适合项目使用。
2. 我们更建议你直接使用本SDK，按照下面方法进行快速嵌入

### 2.1 下载并导入sdk

### 2.2 快速使用

#### 2.2.1 发起一个广告位请求

``` java
YoumiNativeAdHelper
	
	// 创建一个原生广告请求
	.newAdRequest(Context context)
	
	// （必须）设置应用APPID
	.withAppId(String appId)
	
	// （必须）设置请求广告位Id
	.withSlotId(String slotId)
	
	// （可选）设置请求广告数量，默认为1
	.withRequestCount(int adCount)
	
	// （可选）设置性别（M：男性; F：女性）
	.withRequestCount(int adCount)
	
	// （可选）设置年龄
	.withAge(String age)
	
	// （可选）设置内容标题
	.withContentTitle(int adCount)
	
	// （可选）设置内容关键词
	.withContentKeyword(int adCount)
	
	// （可选）设置请求唯一标识
	.withReqId(int adCount)
	
	// （可选）设置UserAgent
	.withUserAgent(int adCount)
	
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
									.withAppId(BuildConfig.APPID)
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

#### 2.2.2 发送广告曝光效果记录

``` java
YoumiNativeAdHelper

	// 创建一个广告效果记录请求
	.newAdEffRequest(Context context)
	
	// （必须）指定APPID
	.withAppId("写入你在后台为应用创建的APPID")
	
	// （必须）指定对应的广告模型
	.withYoumiNativeAdModel(YoumiNativeAdModel adModel)
	
	// （可选）设置效果记录发送失败时的重试次数，默认为5次
	.withMaxRetryCount(5)
	
	// 同步发送曝光记录记录
	//.syncSendShowEff();
	
	// 异步发送曝光效果记录
	.asyncSendShowEff();
```

#### 2.2.3 发送广告点击效果记录

``` java
YoumiNativeAdHelper

	// 创建一个广告效果记录请求
	.newAdEffRequest(Context context)
	
	// （必须）指定APPID
	.withAppId(String appId")
	
	// （必须）指定对应的广告模型
	.withYoumiNativeAdModel(YoumiNativeAdModel adModel)
	
	// （可选）设置效果记录发送失败时的重试次数，默认为5次
	.withMaxRetryCount(5)
	
	// 同步发送曝光记录记录
	//.syncSendClickEff();
	
	// 异步发送曝光效果记录
	.asyncSendClickEff();
```

#### 2.2.4 下载广告

**注意：**

1. 只有广告类型 *(YoumiNativeAdModel#getAdType() 方法可以获知广告类型)* 为APP类型，广告才可以下载
2. 下载时会默认发送下载通知到通知栏中以显示进度，下载完成时，存在下载成功的通知，如果需要在Android 7.0系统以上能打开下载这个安装通知，需要配置FileProvider，可以参考demo配置

``` java
YoumiNativeAdHelper.download(Context context, YoumiNativeAdModel adModel);
```

#### 2.2.5 停止下载广告

``` java
YoumiNativeAdHelper.stopDownload(YoumiNativeAdModel adModel);
```

#### 2.2.6 添加下载监听器

``` java
YoumiNativeAdHelper.addOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener listener);
```

#### 2.2.6 移除下载监听器

``` java
YoumiNativeAdHelper.removeOnYoumiNativeAdDownloadListener(OnYoumiNativeAdDownloadListener listener);
```