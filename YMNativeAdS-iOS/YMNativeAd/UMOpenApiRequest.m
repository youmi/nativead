//
//  UMApiRequest.m
//  YMNativeAdS
//
//  Created by linxiaolong on 2017/3/24.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "UMOpenApiRequest.h"

#import "UMNMachineUtil.h"
#import "UMNSDKConfig.h"
#import "Macro.h"
#import "UMNSDKKeyValue.h"
#import "UMNSDKConstants.h"
#import "UMNDataHandlerUtil.h"
#import <zlib.h>
#import "UMRequest.h"
#import "UMNDataModel.h"
#import "UMNBackgroundQueue.h"
#import "UMNCommonUseUtil.h"
#import "UMURLPathUtil.h"
#import "UMSDKPhoneStatus.h"
#import "UMNSPOpenIDFA.h"
#import "UMNError.h"


extern NSString *const SDK_VERSION();

@interface UMOpenApiRequest()
@end

@implementation UMOpenApiRequest
+(id)shareInstanceRef{
    static UMOpenApiRequest *spotWeb= nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        spotWeb = [UMOpenApiRequest new];
        spotWeb.trackQueue = [UMNBackgroundQueue new];
    });
    return spotWeb;
}


#pragma mark -
#pragma mark 网络请求
//发送请求链接
void sendSpotURLRequestWithBlock(ListBlock block){
    NSString *urlString = spotRequestURL(@"request");
 
    NSURL *url = [NSURL URLWithString:urlString];
    UMRequest *request = [UMRequest requestWithURL:url];
    [request requestAsynchronouslyWithCompletionUsingBlock:^(BOOL isfinish,UMRequest *request){
        if (isfinish) {
            NSError *error = nil;
            
        
            NSDictionary *jsonDic = [NSJSONSerialization JSONObjectWithData:request.REdata options:NSJSONReadingAllowFragments error:&error];
            OGINFO(@"%@",jsonDic);
            //            NSString *jm = [jsonDic objectForKey:@"jm"];
            if (error) {
                NSLog(@"插播数据json解析失败<error>:%@\n\n",error);
                NSString *backUpString = [[NSString alloc] initWithData:request.REdata encoding:NSUTF8StringEncoding];
                NSLog(@"解不开的数据为：%@",backUpString);
                block(nil,error);
            }
            else{
                
                int code = [[jsonDic objectForKey:@"c"] intValue];
                
                if (code == 0) {
                    
                    NSMutableDictionary *preDic = [NSMutableDictionary dictionaryWithObjectsAndKeys:
                                                   YM_ASSIGN_STRING_SAFELY([jsonDic objectForKey:@"rsd"]),@"rsd",
//                                                   [jsonDic objectForKey:@"jm"],@"jm",
//                                                   [jsonDic objectForKey:@"sal"],@"sal",
//                                                   [jsonDic objectForKey:@"pl"],@"pl",
                                                   nil];
                    
                    NSArray *ad = [jsonDic objectForKey:@"ad"];
                    NSMutableArray *mutabArray = [[NSMutableArray alloc] initWithCapacity:ad.count];
                    for (NSDictionary *adDic in ad) {
                        UMNDataModel *spotDataStructure = [[UMNDataModel alloc] initWithDiction:preDic dic:adDic open:YES];
                        [mutabArray addObject:spotDataStructure];
                    }
                    block(mutabArray,nil);
                }else{
                    NSError *error = [UMNError errorCode:code userInfo:nil];
                    NSLog(@"%@",error);
                    //                    NSLog(@"插播数据返回码code不对：code:=%d",code);
                    block(nil,error);
                }
                
            }
        }else{
            block(nil,nil);
        }
    }];
}

//效果链接
void sendSpotEffURLRequestWithBlock(long effType ,UMNDataModel *spotDataStructure ,FinishBlock block){
    if (effType == 0) {
        [[[UMOpenApiRequest shareInstanceRef] trackQueue] addOperationWithBlock:^{
            [UMOpenApiRequest sendTrackURL:spotDataStructure.showTrack];
        }];
    }else if (effType == 1){
        [[[UMOpenApiRequest shareInstanceRef] trackQueue] addOperationWithBlock:^{
            [UMOpenApiRequest sendTrackURL:spotDataStructure.clickTrack];
        }];
    }
    
}


//发送链接
+(void)sendTrackURL:(NSArray *)trackArray{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        if (!trackArray) {
            return;
        }
        
        for (NSString *urlString in trackArray) {
            
            NSURL *url = [NSURL URLWithString:urlString];
            NSMutableURLRequest *request    = [[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:30];
            //用作负载均衡
            [request addValue:GetCID() forHTTPHeaderField:getLoadBalancing()];
            NSString *str = [NSString stringWithFormat:@"Bearer %@",[UMNSDKConfig sharedInstanceSDKConfig].appid];
            
            [request addValue:str forHTTPHeaderField:@"Authorization"];
            
            NSURLResponse *response             = nil;
            NSError *error                                      = nil;
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-variable"
            NSData *data = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
            
            if(error || ![response isKindOfClass:[NSHTTPURLResponse class]]){
                OGINFO(@"发送http：url %@",url);
            }else{
                OGINFO(@"发送成功,返回的数据%@",[[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]);
            }
        }
    });
}
#pragma mark -
#pragma mark url的生成

//插播请求链接
NSString *spotRequestURL(NSString *type){
    
    NSString *urlArg = getUrlArg();
    
    NSString *requestHead = @"https://native.umapi.cn/ios/v1/oreq";//请求广告的url
    
    NSString *requestURL = [NSString stringWithFormat:@"%@?%@",requestHead,urlArg];
    requestURL = [requestURL  stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
    if ([type isEqualToString:@"showerror"])
        OGINFO(@"发送显示失败效果：%@",requestURL);
    else
        OGINFO(@"新插播请求接口：%@",requestURL);
    
    return requestURL;
}


NSString *getUrlArg(){
    NSTimeInterval reqtime = [[NSDate date] timeIntervalSince1970] * 1000; //时间戳
    NSString *slotid = [UMNSDKConfig sharedInstanceSDKConfig].slotid;//广告位id
    int ad_count = [UMNSDKConfig sharedInstanceSDKConfig].adcount;//自定义获取广告数，但暂时每个广告位只给一个广告
    NSString *gender = @"";//性别，M=男性，F=女性
    NSString *age = @"";//年龄
    NSString *cont_title = YM_ASSIGN_STRING_SAFELY([UMNSDKConfig sharedInstanceSDKConfig].cont_title);//内容的标题
    NSString *cont_kw = YM_ASSIGN_STRING_SAFELY([UMNSDKConfig sharedInstanceSDKConfig].cont_kw);//内容的关键词，逗号分隔
    
    NSString *reqid = @"";//这次请求的唯一id，可不填写
    NSString *idfa = YM_ASSIGN_STRING_SAFELY(IFA_function());//
    NSString *brand = @"apple";//制造厂商,如“apple”“Samsung”“Huawei“，默认为空字符串
    NSString *model = YM_ASSIGN_STRING_SAFELY([UMNMachineUtil sharedInstance].phoneOS);//型号, 如”iphoneA1530”，默认为空字符串
    NSString *mac = @""; //设备的mac地址，明文不加密
    NSString *imei = @"";//设备的imei码，明文不加密
    NSString *ip = @"";//ip
    NSString *ua = @"";//useragent
    NSString *os = @"iOS";
    NSString *osv = [NSString stringWithFormat:@"%@",systemMainVersion()];
    NSString *appversion = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];
    NSString *conntype = YM_ASSIGN_STRING_SAFELY([UMNMachineUtil sharedInstance].accessPointName);//网络类型，空=无，0=未知/其他，1=wifi，2=2g，3=3g，4=4g，5=5g
    NSString *carrier = YM_ASSIGN_STRING_SAFELY([UMNMachineUtil sharedInstance].mobileNetworkCode);//网络运营商，空=无，0=未知/其他，1=wifi，2=移动，3=联通，4=电信
    NSString *pk = [[NSBundle mainBundle] bundleIdentifier];//iOS为App的BundleIdentifier
    NSString *language = YM_ASSIGN_STRING_SAFELY([UMNMachineUtil sharedInstance].language);//用户设置的语言，如zh
    NSString *countrycode = YM_ASSIGN_STRING_SAFELY([UMNMachineUtil sharedInstance].countryCode);//用户设置的国家编码，如CN
    
    
    //设置参数集合
    NSString *str = [NSString stringWithFormat:@"%f",reqtime];
    int time = [str intValue];
    
    NSString *urlArg = [NSString stringWithFormat:@"reqtime=%d&slotid=%@&ad_count=%d&gender=%@&age=%@&cont_title=%@&cont_kw=%@&reqid=%@&idfa=%@&brand=%@&model=%@&mac=%@&imei=%@&ip=%@&ua=%@&os=%@&osv=%@&appversion=%@&conntype=%@&carrier=%@&pk=%@&language=%@&countrycode=%@",time,slotid,ad_count,gender,age,cont_title,cont_kw,reqid,idfa,brand,model,mac,imei,ip,ua,os,osv,appversion,conntype,carrier,pk,language,countrycode];
    
    return urlArg;
}


+(NSString*)DataTOjsonString:(id)object
{
    NSString *jsonString = nil;
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:object
                                                       options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                         error:&error];
    if (! jsonData) {
        NSLog(@"Got an error: %@", error);
    } else {
        jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    }
    return jsonString;
}

#pragma clang diagnostic pop
@end
