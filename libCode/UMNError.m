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

+(void)initialize{
    if (self == [UMNError class]) {
        errorDictionary = \
        @{
          /* code        :        errorWithDomain */
          /* ==================================== */
          @(e110)         :        @"请求失败",
          @(e0)         : @"请求成功，返回正确",
          
          @(e1000) : @"app不存在",
          @(e1001) : @"appid不为空",
          @(e1002) : @"appid为空",
          @(e1003) : @"appid非法平台id",
          @(e1004) : @"appid不对应平台id",
          @(e1100) : @"包名未绑定",
          @(e1200) : @"app状态异常",
          @(e1201) : @"app未提交",
          @(e1202) : @"app封杀",
          @(e1203) : @"app非通过状态",
          @(e1205) : @"app版本号太低",
          
          @(e1300) : @"app secret 解密失败",
          @(e1301) : @"非合法app状态",
          @(e1400) : @"app业务不对",
          @(e1401) : @"",
          @(e1402) : @"",
          @(e1404) : @"包名不匹配",
          @(e1405) : @"地理位置不允许投放",
          @(e1406) : @"应用不在白名单中",
          @(e1407) : @"应用在黑名单中",
          
          @(e2000) : @"无广告 ",
          @(e2006) : @"无版本sdk广告",
          @(e2007) : @"没有广告",
          @(e2009) : @"app不匹配",
          @(e2012) : @"",
          @(e2100) : @"无此广告",
          
          @(e3000) : @"参数缺失",
          @(e3001) : @"GET参数缺失",
          @(e3002) : @"POST参数缺失",
          @(e3003) : @"url第一级参数缺失",
          @(e3004) : @"第二级参数缺失",
          @(e3005) : @"LOG参数缺失",
          
          @(e3101) : @"参数无法解析",
          @(e3106) : @"S参数无法解析",
          @(e3107) : @"E参数无法解析",
          
          @(e3210) : @"非法Ip",
          @(e3217) : @"sdk版本号太低",
          @(e3218) : @"非法请求操作",
          
          @(e3312) : @"设备可安装的广告数达到上限,或视频播放已到媒介限定次数和广告限定次数",
          
          
          /* ==================================== */
          };
    }
}

+ (NSError *)errorCode:(UMNErrorCode)code userInfo:(NSDictionary *)dic
{
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
