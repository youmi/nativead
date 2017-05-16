//
//  YMNativeAds.m
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/6.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "YMNativeAd.h"
#import "UMAdManager.h"
@implementation YMNativeAd

-(instancetype)initWithAppId:(NSString *)appid slotId:(NSString *)slotid{
    
    self = [super init];
    if (self) {
        [[UMAdManager sharedInstanceUMAdManager]initWithAppId:appid slotId:slotid];
    }
    return self;
}

//设置广告位内容
-(void)setContTitle:(NSString *)title contKw:(NSString *)kw{
    [[UMAdManager sharedInstanceUMAdManager]setContTitle:title contKw:kw];
}


-(void)loadAd{
    
    __weak YMNativeAd *weakSelf = self;
    [[UMAdManager sharedInstanceUMAdManager]requestLoadAds:1 successCallBackBlock:^(NSArray *adArray) {
        
        if (weakSelf.delegate) {
            [weakSelf.delegate ymNativeAdSuccessToLoad:adArray];
        }
    } fialedCallBackBlock:^(NSError *error) {
        
        if (weakSelf.delegate) {
            [weakSelf.delegate ymNativeAdFailedToLoad:error];
        }
    }];
}


///**
// * 获取广告
// * adcount  :想要获取的广告个数
// * 注：返回的广告数量小于等于adcount
// */
//-(void)loadAd:(int)adcount;
//请求广告
-(void)loadAd:(int)adcount{

    __weak YMNativeAd *weakSelf = self;
    [[UMAdManager sharedInstanceUMAdManager]requestLoadAds:adcount successCallBackBlock:^(NSArray *adArray) {
        
        if (weakSelf.delegate) {
            [weakSelf.delegate ymNativeAdSuccessToLoad:adArray];
        }
    } fialedCallBackBlock:^(NSError *error) {
        
        if (weakSelf.delegate) {
            [weakSelf.delegate ymNativeAdFailedToLoad:error];
        }
    }];
}



-(void)clickAd:(UMNDataModel *)adData callBackBlock:(void(^)(NSError *error))callBackBlock{
    [[UMAdManager sharedInstanceUMAdManager]requestClickAd:adData callBackBlock:^(NSError *error) {
        if (callBackBlock) {
            callBackBlock(error);
        }
    }];
}

-(void)showAd:(UMNDataModel *)adData callBackBlock:(void(^)(NSError *error))callBackBlock{
    [[UMAdManager sharedInstanceUMAdManager]requestShowAd:adData callBackBlock:^(NSError *error) {
        if (callBackBlock) {
            callBackBlock(error);
        }
    }];
}

-(void)clickAdOpenAppStoreVC:(UMNDataModel*)adData{
    
    [[UMAdManager sharedInstanceUMAdManager]clickAdOpenAppStoreVC:adData];
}

@end
