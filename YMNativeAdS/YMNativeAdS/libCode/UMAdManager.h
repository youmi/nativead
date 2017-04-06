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

-(void)initWithAppId:(NSString *)appid slotId:(NSString *)slotid;

/**
 * 请求广告列表
 * adcount      :要请求的广告数量
 * successblock :请求成功block
 * errorblock   :请求失败block
 */
-(void)requestLoadAds:(int)adcount successCallBackBlock:(void(^)(NSArray *adArray))successblock fialedCallBackBlock:(void(^)(NSError *error))errorblock;

-(void)setContTitle:(NSString *)title contKw:(NSString *)kw;

/**
 * 点击广告时调用
 * dataModel     :被点击的广告实例
 */
-(void)requestClickAd:(UMNDataModel*)dataModel callBackBlock:(void(^)(NSError *error))callBackBlock;

/**
 * 展示广告时调用
 * dataModel     :被展示的广告实例
 */
-(void)requestShowAd:(UMNDataModel*)dataModel callBackBlock:(void(^)(NSError *error))callBackBlock;

/**
 * 点击广告打开AppStore
 *
 */
-(void)clickAdOpenAppStoreVC:(UMNDataModel*)adData;
@end
