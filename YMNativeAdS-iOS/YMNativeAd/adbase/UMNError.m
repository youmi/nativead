//
//  UMNError.m
//  YMNativeAdS
//
//  Created by linxiaolong on 2017/3/8.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "UMNError.h"

static NSDictionary *errorDictionary = nil;

@implementation UMNError

+ (void)initialize {
    if (self == [UMNError class]) {
        errorDictionary =
            @{
                @(e110) : @"请求失败",
                @(e0) : @"请求成功，返回正确",

                @(e1002) : @"appid为空",
                @(e1012) : @"Header错误",

                @(e2007) : @"没有广告",
                @(e2222) : @"请求广告超时",

                @(e3003) : @"缺少参数",
                @(e3208) : @"设备参数非法或者缺失",
                @(e3312) : @"用户今天的广告展示已经达到上限",
            };
    }
}

+ (NSError *)errorCode:(UMNErrorCode)code userInfo:(NSDictionary *)dic {
    NSErrorDomain domain = errorDictionary[@(code)];
    if (!domain) {
        domain = @"请求出错，请联系客服";
    }
    return [NSError errorWithDomain:domain
                               code:code
                           userInfo:dic];
}

+ (NSString *)transformCodeToStringInfo:(UMNErrorCode)code
{
    return errorDictionary[@(code)];
}
@end
