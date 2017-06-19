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
    if ([adData.uri length]>0) {
        //deeplink
        //        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"fruitday://"]];
        //        [self openScheme:adData.uri];
        
        UIApplication *application = [UIApplication sharedApplication];
        NSURL *URL = [NSURL URLWithString:adData.uri];
        
        if ([application respondsToSelector:@selector(openURL:options:completionHandler:)]) {
            [application openURL:URL options:@{}
               completionHandler:^(BOOL success) {
                   NSLog(@"Open %@: %d",adData.uri,success);
                   if (success) {
                       
                   }else{
                       if (adData.asid) {
                           [[UMStoreKitUtil defaultStoreKitHelper]showAppInAppStore:[NSNumber numberWithInteger:adData.asid]];
                       }else{
                           [self openAppStoreUrl:adData.url];
                       }
                       
                   }
               }];
        } else {
            BOOL success = [application openURL:URL];
            NSLog(@"Open %@: %d",adData.uri,success);
            if (success) {
                
            }else{
                if (adData.asid) {
                    [[UMStoreKitUtil defaultStoreKitHelper]showAppInAppStore:[NSNumber numberWithInteger:adData.asid]];
                }else{
                    [self openAppStoreUrl:adData.url];
                }
            }
        }
    }
    else if ([adData.url length]>0) {
        
        [self openAppStoreUrl:adData.url];

    }else{
        [[UMStoreKitUtil defaultStoreKitHelper]showAppInAppStore:[NSNumber numberWithInteger:adData.asid]];
    }
}

-(void)openAppStoreUrl:(NSString *)url{
    
    if ([[[UIDevice currentDevice] systemVersion] doubleValue]<10.0) {
        [[UIApplication sharedApplication]openURL:[NSURL URLWithString:url]];
    }else{
        [[UIApplication sharedApplication]openURL:[NSURL URLWithString:url] options:@{UIApplicationOpenURLOptionsSourceApplicationKey : @YES} completionHandler:^(BOOL success) {
            
        }];
    }
}
@end
