//
//  UMAdManager.m
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/8.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "UMAdManager.h"
#import "UMNSDKConfig.h"
#import "UMOpenApiRequest.h"
#import "UMNError.h"
#import "UMStoreKitUtil.h"
@interface UMAdManager(){
}

@end

@implementation UMAdManager

+ (UMAdManager *)sharedInstanceUMAdManager {
    static UMAdManager *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [self new];
        
        [UMOpenApiRequest shareInstanceRef];
    });
    return instance;
}

-(void)initWithAppId:(NSString *)appid slotId:(NSString *)slotid{
    
    [UMNSDKConfig sharedInstanceSDKConfig].slotid = slotid;
    [UMNSDKConfig sharedInstanceSDKConfig].appid = appid; 
}

-(void)setContTitle:(NSString *)title contKw:(NSString *)kw{
    [UMNSDKConfig sharedInstanceSDKConfig].cont_title = title;
//    [UMNSDKConfig sharedInstanceSDKConfig].cont_cat = cat;
//    [UMNSDKConfig sharedInstanceSDKConfig].cont_url = url;
    [UMNSDKConfig sharedInstanceSDKConfig].cont_kw = kw;
}


-(void)requestLoadAds:(int)adcount successCallBackBlock:(void(^)(NSArray *adArray))successblock fialedCallBackBlock:(void(^)(NSError *error))errorblock{
    
    [UMNSDKConfig sharedInstanceSDKConfig].adcount = adcount;
    
    sendSpotURLRequestWithBlock(^(NSArray *adArray,NSError *error) {
        if (adArray) {
            if (successblock) {
                successblock(adArray);
            }
        }else{
            if (errorblock) {
                errorblock(error);
            }
        }
    });
}

-(void)requestClickAd:(UMNDataModel*)dataModel callBackBlock:(void(^)(NSError *error))callBackBlock{
    
    sendSpotEffURLRequestWithBlock(1, dataModel, ^(NSError *error) {
        if (callBackBlock) {
            callBackBlock(error);
        }
    });
}

-(void)requestShowAd:(UMNDataModel*)dataModel callBackBlock:(void(^)(NSError *error))callBackBlock{
    
    sendSpotEffURLRequestWithBlock(0, dataModel, ^(NSError *error) {
        if (callBackBlock) {
            callBackBlock(error);
        }
    });
}

-(void)clickAdOpenAppStoreVC:(UMNDataModel*)adData{
    //跳转模式有两种， 第一种使用url跳转，第二种使用appid打开内部appstore
    if ([adData.url length]>0) {
        if ([[[UIDevice currentDevice] systemVersion] doubleValue]<10.0) {
            [[UIApplication sharedApplication]openURL:[NSURL URLWithString:adData.url]];
        }else{
            [[UIApplication sharedApplication]openURL:[NSURL URLWithString:adData.url] options:@{UIApplicationOpenURLOptionsSourceApplicationKey : @YES} completionHandler:^(BOOL success) {
                
            }];
        }
        
//        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:adData.url] options:nil completionHandler:^(BOOL success) {
//            if (success) {
//                
//            }else{
//                [[UMStoreKitUtil defaultStoreKitHelper]showAppInAppStore:[NSNumber numberWithInteger:adData.asid]];
//            }
//        }];
    }else{
        [[UMStoreKitUtil defaultStoreKitHelper]showAppInAppStore:[NSNumber numberWithInteger:adData.asid]];
    }
    
}
@end
