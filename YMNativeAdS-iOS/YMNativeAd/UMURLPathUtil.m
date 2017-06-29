//
//  URLPathConstants.m
//  SDK
//
//  Created by yuxuhong on 14-10-30.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
// 用来混淆字符串

#import "UMURLPathUtil.h"

#define XOR_KEY 0xBB

//取公共的存取目录
NSString* getSpotShareDirectory() {
    return @".homeCache";
}


////取广告的请求路径
NSString* getSpotRequestHead() {
    return @"https://native.umapi.cn/ios/v1/req";
}


//广告效果路径
NSString* getSpotEffectURLHead() {
    
    return @"https://native.umapi.cn/ios/v1/eff";
}


