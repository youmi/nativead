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
    
    e1000 = -1000,
    e1001 = -1001,
    e1002 = -1002,
    e1003 = -1003,
    e1004 = -1004,
    e1100 = -1100,
    e1200 = -1200,
    e1201 = -1201,
    e1202 = -1202,
    e1203 = -1203,
    e1205 = -1205,
    
    e1300 = -1300,
    e1301 = -1301,
    e1400 = -1400,
    e1401 = -1401,
    e1402 = -1402,
    e1404 = -1404,
    e1405 = -1405,
    e1406 = -1406,
    e1407 = -1407,
    
    e2000 = -2000,
    e2006 = -2006,
    e2007 = -2007,
    e2009 = -2009,
    e2012 = -2012,
    e2015 = -2015,
    e2100 = -2100,
    
    e3000 = -3000,
    e3001 = -3001,
    e3002 = -3002,
    e3003 = -3003,
    e3004 = -3004,
    e3005 = -3005,
    
    e3101 = -3101,
    e3106 = -3106,
    e3107 = -3107,
    
    e3210 = -3210,
    e3217 = -3217,
    e3218 = -3218,
    
    e3312 = -3312,
    e3401 = -3401,
    e3402 = -3402,
    e3403 = -3403,
    e3404 = -3404,
    e3405 = -3405,
    
    
}UMNErrorCode;

@interface UMNError : NSObject

+(NSError *)errorCode:(UMNErrorCode)code userInfo:(NSDictionary *)dic;
+(NSString *)transformCodeToStringInfo:(UMNErrorCode)code;

@end
