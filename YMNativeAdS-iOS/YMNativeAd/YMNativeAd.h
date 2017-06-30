//
//  YMNativeAds.h
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/6.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UMNDataModel.h"

/**
 广告请求结果代理
 */
@protocol YMNativeAdDelegate < NSObject >

@required

/**
 获取广告成功后的回调

 @param nativeAdArray 返回的广告数组UMNDataModel
 */
- (void)ymNativeAdSuccessToLoad:(NSArray *)nativeAdArray;

/**
 获取广告失败后的回调

 @param error 对应的错误代码
 */
- (void)ymNativeAdFailedToLoad:(NSError *)error;

@end

@interface YMNativeAd : NSObject

/**
 委托对象
 */
@property (nonatomic, weak) id< YMNativeAdDelegate > delegate;

/**
 初始化广告请求对象
 
 @param appid 开发者应用的APPID
 @param slotid 广告位
 @return 广告请求对象
 */
- (instancetype)initWithAppId:(NSString *)appid slotId:(NSString *)slotid;

/**
 （可选）设置请求广告时附带的可选参数

 @param title 内容标题
 @param kw 内容关键词，逗号分隔
 */
- (void)setContTitle:(NSString *)title contKw:(NSString *)kw;

/**
 异步请求广告，一次返回一个
 */
- (void)loadAd;

/**
 处理广告点击事件，在用户点击广告时，需要调用本方法，同一个广告不要重复调用

 @param adData 待点击的广告数据
 @param callBackBlock 完成有米广告点击逻辑后的回调block，开发者可以在block里处理自己的逻辑
 */
- (void)clickAd:(UMNDataModel *)adData callBackBlock:(void (^)(NSError *error))callBackBlock;

/**
 处理广告展示事件，在准备展示广告时，需要调用本方法，每个广告展示之前都必须调用本方法，但同一个广告不要重复调用本方法

 @param adData 待展示的广告数据
 @param callBackBlock 完成有米广告展示逻辑后的回调block，开发者可以在block里处理自己的逻辑
 */
-(void)showAd:(UMNDataModel *)adData callBackBlock:(void(^)(NSError *error))callBackBlock;

/**
 执行广告的点击逻辑：具体一般为打开广告所对应的APPStore

 @param adData 被点击的广告数据
 */
-(void)clickAdOpenAppStoreVC:(UMNDataModel*)adData;
@end
