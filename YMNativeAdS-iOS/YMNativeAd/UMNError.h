//
//  UMNError.h
//  YMNativeAdS
//
//  Created by linxiaolong on 2017/3/8.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum :NSUInteger{
    
    e0   = 0,
    e110 = 110,
    
    e1002 = -1002,
    e1012 = -1012,
    
    e2007 = -2007,
    e2222 = -2222,
    
    e3003 = -3003,
    e3208 = -3208,
    e3312 = -3312,
    
}UMNErrorCode;

@interface UMNError : NSObject

+(NSError *)errorCode:(UMNErrorCode)code userInfo:(NSDictionary *)dic;
+(NSString *)transformCodeToStringInfo:(UMNErrorCode)code;

@end
