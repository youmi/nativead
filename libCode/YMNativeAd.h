//
//  YMNativeAds.h
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/6.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UMNDataModel.h"


@protocol YMNativeAdDelegate <NSObject>

@required

/**
 * 获取广告成功，返回UMNDataModel对象的数组
 */
-(void)ymNativeAdSuccessToLoad:(NSArray *)nativeAdArray;

/**
 * 获取广告失败
 */
-(void)ymNativeAdFailedToLoad:(NSError *)error;

@end


@interface YMNativeAd : NSObject

/**
 *  委托对象
 */
@property (nonatomic, weak) id<YMNativeAdDelegate> delegate;

/**
 *  初始化方法
 *  appid   :应用id
 *  slotid  :广告位id
 */
-(instancetype)initWithAppId:(NSString *)appid slotId:(NSString *)slotid;

/**
 *  设置广告位对应的内容
 *  title   :内容的标题
 *  kw      :内容的关键词，逗号分隔 
 */
-(void)setContTitle:(NSString *)title contKw:(NSString *)kw;

/**
 * 获取广告
 * 一次返回一个
 */
-(void)loadAd;



/**
 *  广告点击
 * 【必选】：用户点击广告调用本方法
 *  注：同一个广告请不要重复调用
 */
-(void)clickAd:(UMNDataModel *)adData callBackBlock:(void(^)(NSError *error))callBackBlock;

/**
 *  广告展示
 * 【必选】：展示广告调用本方法
 *  注：每个广告展示之前都必须调用本方法一次，请不要重复调用
 */
-(void)showAd:(UMNDataModel *)adData callBackBlock:(void(^)(NSError *error))callBackBlock;

/**
 * 点击广告打开AppStore
 * adData   :被点击的广告
 */
-(void)clickAdOpenAppStoreVC:(UMNDataModel*)adData;
@end
