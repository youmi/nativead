//
//  DevConfiguration.m
//  SDK
//
//  Created by ENZO YANG on 13-2-26.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import "UMNSDKConfig.h"
#import "Macro.h"

@implementation UMNSDKConfig

+ (UMNSDKConfig *)sharedInstanceSDKConfig {
    static UMNSDKConfig *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [self new];
    });
    return instance;
}

- (void)dealloc {
    YM_RELEASE_SAFELY(_appid);
    YM_RELEASE_SAFELY(_slotid);
    YM_RELEASE_SAFELY(_cont_kw);
    YM_RELEASE_SAFELY(_cont_title);
}

@end
