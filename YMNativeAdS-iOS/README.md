# 有米iOS原生广告开源API接口使用说明

## 1. 前言

欢迎您使用有米iOS原生广告开源API接口，请您参照此文档步骤来使用开源API接口，或可以参考我们的Demo程序源码来使用开源API接口。

以下几点注意事项，请仔细阅读：

1. 一个应用APPID只能使用在单个APP内，不能多个APP重复使用。
2. 嵌入完成，将获取到测试广告，如果测试没问题，需要到有米开发者后台审核上传应用进行审核，审核通过后方可获得正式广告，并且产生正常收益。
3. 每个应用可以创建多个广告位，广告位可以设定不同的广告图片尺寸，具体可以登录有米开发者后台http://app.youmi.net查看。
4. 在使用开源API接口的同时，需要您在自己的应用中开辟出适合的位置，并设置好正确的广告逻辑。iOS版的API接口已经封装好了相应的逻辑，您也可以直接使用。


## 2. 使用说明

### 2.1 导入项目

支持下面两种项目导入方式，建议使用CocoaPod导入项目

#### 2.1.1 拷贝源码

项目下的libCode目录就是需要使用的源码，开发者将libCode目录拷贝到自己的项目中即可使用。

#### 2.1.2 在CocoaPod中安装有米原生广告

项目中的代码已经上传到CocoaPod中，你可以直接直接在CocoaPod中安装使用

1. 在你的项目的根目录中新建一个 `Podfile` 文件，添加内容如下：

```
pod 'nativead'
```

2. 终端命令行（使用 `cd` 命令）进入到您的iOS项目的根目录中，执行如下命令安装：

```
pod install
```

3. 安装完成之后，点击目录中的.xcworkplace文件来打开项目，接下来你就可以在项目中使用有米原生广告了。


### 2.2 代码使用

#### 2.2.1 初始化

```
//在interface头部设定属性并添加头部代理<YMNativeAdDelegate>
YMNativeAd *_nativeAd;//广告实例 （YMNativeAd是libCode源码包的头文件，整个交互基本都是由本类来处理）
NSArray *_adArray;//广告数组（UMNSataModel实例集合）
UMNDataModel *_currentAd;//当前展示的ad（方便接口调用）

//在初始化函数中实例广告
if (!_nativeAd) {
    _nativeAd = [[YMNativeAd alloc]initWithAppId:@"320a6cd3b8d2c4e0" slotId:@"7746"];
    [_nativeAd setContTitle:@"广告位的标题" contKw:@"广告位的关键字"];//可选填写
    _nativeAd.delegate = self; 
}
[_nativeAd loadAd];//拉取广告
```

#### 2.2.2 请求反馈

```
#pragma mark -YMNvativeDelegate
//拉取成功
-(void)ymNativeAdSuccessToLoad:(NSArray *)nativeAdArray{
 
    _adArray = nativeAdArray;//返回广告array,默认只返回一个
    
    UMNDataModel *dataModel = [_adArray objectAtIndex:0]; //获取广告model，然后自定义view展示model属性
    [self showAdView];
}
//拉取失败
-(void)ymNativeAdFailedToLoad:(NSError *)error{
NSLog(@"%@",error);
} 
```

#### 2.2.3 展示广告（展示广告时必须调用展示效果接口）
```
//展示原生广告，开发者获取广告model，自定义view样式并展示
-(void)showAdView{
    if (_adArray) {
        UMNDataModel *dataModel = [_adArray objectAtIndex:0];//获取model
        _currentAd = dataModel;
		
		//开发者可以在这里自定义UI展示广告
        NSLog(@"adname:%@",_currentAd.name);//广告名

		/*展示广告 调用展示接口*/
		[_nativeAd showAd:_currentAd callBackBlock:^(NSError *error) {
    		if (error) {
        		NSLog(@"%@",error);
   		 }else{
        		NSLog(@"展示有效");
    		}
		}];
	}
}
```

#### 2.2.4 点击跳转和效果上报
```
- (void)viewTapped:(UITapGestureRecognizer *)gr {
    /*点击发生，调用点击接口*/
    [_nativeAd clickAd:_currentAd callBackBlock:^(NSError *error) {
        if (error) {
            NSLog(@"%@",error);
        }else{
            NSLog(@"点击有效");
        }
    }];

    /*跳转到广告对应的AppStore页面*/
    [_nativeAd clickAdOpenAppStoreVC:_currentAd];
}
```

#### 2.2.5 到此接入完毕，有任何问题请联系有米客服。

