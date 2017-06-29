//
//  DevConfiguration.h
//  SDK
//
//  Created by ENZO YANG on 13-2-26.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UMNCommonUseUtil.h"
#import "UMNSDKPicConstants.h"

@interface UMNSDKConfig : NSObject

@property (nonatomic, copy) NSString *appid;      // appid
@property (nonatomic, copy) NSString *slotid;     // 广告位id
@property (nonatomic, copy) NSString *cont_title; // 内容的标题
@property (nonatomic, copy) NSString *cont_kw;    // 内容的关键词，逗号分隔
@property (nonatomic, assign) int adcount;        // 广告数量

+ (UMNSDKConfig *)sharedInstanceSDKConfig;

@end
