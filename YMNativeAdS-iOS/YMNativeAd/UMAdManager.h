//
//  UMAdManager.h
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/8.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import <Foundation/Foundation.h>
@class UMNDataModel;

@interface UMAdManager : NSObject

+ (UMAdManager *)sharedInstanceUMAdManager;

/**
 初始化appid和广告位id

 @param appid   APPID
 @param slotid  广告位ID
 */
- (void)initWithAppId:(NSString *)appid slotId:(NSString *)slotid;

/**
 请求广告列表

 @param adcount         广告请求数量
 @param successblock    请求成功block
 @param errorblock      请求失败block
 */
- (void)requestLoadAds:(int)adcount successCallBackBlock:(void (^)(NSArray *adArray))successblock fialedCallBackBlock:(void (^)(NSError *error))errorblock;

/**
 （可选）设置请求时附带的内容标题和内容关键词参数，用于完善广告筛选，提高广告填充

 @param title   内容的标题
 @param kw      内容的关键词
 */
- (void)setContTitle:(NSString *)title contKw:(NSString *)kw;

/**
 点击广告时调用

 @param dataModel       广告实例
 @param callBackBlock   错误block
 */
- (void)requestClickAd:(UMNDataModel *)dataModel callBackBlock:(void (^)(NSError *error))callBackBlock;

/**
 展示广告时调用

 @param dataModel       广告实例
 @param callBackBlock   错误block
 */
- (void)requestShowAd:(UMNDataModel*)dataModel callBackBlock:(void(^)(NSError *error))callBackBlock;

/**
 打开指定广告在APPStore中的页面

 @param adData      广告实例
 */
- (void)clickAdOpenAppStoreVC:(UMNDataModel*)adData;

@end
