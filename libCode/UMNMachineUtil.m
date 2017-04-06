//
//  DeviceInfoUtil.m
//  SDK
//
//  Created by ENZO YANG on 13-2-21.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import "UMNMachineUtil.h"

#import <CoreTelephony/CTTelephonyNetworkInfo.h>
#import <CoreTelephony/CTCarrier.h>
#import <sys/socket.h>
#import <sys/utsname.h>
#import <sys/sysctl.h>
#include <net/if.h>
#include <net/if_dl.h>
#import "UMNReachability.h"
#import "UMNDataHandlerUtil.h"
#import "Macro.h"
#import "UMNSDKKeyValue.h"
#import <AdSupport/AdSupport.h>
#import "UMNFileUtil.h"
#import <mach-o/loader.h>
#import <mach-o/dyld.h>
#import <mach-o/arch.h>
#ifndef AppListFunc
#define AppListFunc
#include <dlfcn.h>
#include <stdlib.h>
typedef NSObject *(*SBSCopyApplicationDisplayIdentifiersFunc)(BOOL onlyActive, BOOL unkown);
#endif

//extern NSString *const ReachabilityChangedNotificationPrivate;
//NSString *const ReachabilityChangedNotification              = @"kReachabilityChangedNotification";
NSString*const JoykReachabilityChangedNotification(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'k'),(XOR_KEY ^ 'R'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'c'),(XOR_KEY ^ 'h'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'b'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'y'),(XOR_KEY ^ 'C'),(XOR_KEY ^ 'h'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'n'),(XOR_KEY ^ 'g'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'N'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'f'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'c'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 'n'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"JoykReachabilityChangedNotification";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}

//static NSString *const kSPTReachabilityHostName             = @"www.baidu.com";
static NSString*const www_baidu_com(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'w'),(XOR_KEY ^ 'w'),(XOR_KEY ^ 'w'),(XOR_KEY ^ '.'),(XOR_KEY ^ 'b'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'u'),(XOR_KEY ^ '.'),(XOR_KEY ^ 'c'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 'm'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"www.baidu.com";///[NSString stringWithFormat:@"%s", spotShareDirectory];
}

static NSString* CFBundleDisplayName(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'C'),(XOR_KEY ^ 'F'),(XOR_KEY ^ 'B'),(XOR_KEY ^ 'u'),(XOR_KEY ^ 'n'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'D'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 's'),(XOR_KEY ^ 'p'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'y'),(XOR_KEY ^ 'N'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'm'),(XOR_KEY ^ 'e'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"CFBundleDisplayName";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}

static NSString *const kCellularProviderDidUpdateNotification(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'k'),(XOR_KEY ^ 'C'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'u'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'r'),(XOR_KEY ^ 'P'),(XOR_KEY ^ 'r'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 'v'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'r'),(XOR_KEY ^ 'D'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'U'),(XOR_KEY ^ 'p'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'N'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'f'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'c'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 'n'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"kCellularProviderDidUpdateNotification";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}

//(lldb) po OPSLogFoundtion(@"wifi")
static NSString* wifi(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'w'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'f'),(XOR_KEY ^ 'i'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"wifi";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}


//(lldb) po OPSLogFoundtion(@"None")
static NSString* None(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'N'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 'n'),(XOR_KEY ^ 'e'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"None";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}


//(lldb) po OPSLogFoundtion(@"Device")
static NSString* Device(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'D'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'v'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'c'),(XOR_KEY ^ 'e'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"Divece";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}


//(lldb) po OPSLogFoundtion(@"Jailbreak")
static NSString* Jailbreak(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'J'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'b'),(XOR_KEY ^ 'r'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'k'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"Jailbreak";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}


//(lldb) po OPSLogFoundtion(@"Version")
static NSString* Version(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'V'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'r'),(XOR_KEY ^ 's'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 'n'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"Version";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}


//(lldb) po OPSLogFoundtion(@"Model")
static NSString* Model(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'M'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'l'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"Model";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}


//(lldb) po OPSLogFoundtion(@"GPRS|3G")
static  NSString* GPRS_3G(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'G'),(XOR_KEY ^ 'P'),(XOR_KEY ^ 'R'),(XOR_KEY ^ 'S'),(XOR_KEY ^ '|'),(XOR_KEY ^ '3'),(XOR_KEY ^ 'G'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"GPRS|3G";//[NSString stringWithFormat:@"%s", spotShareDirectory];
} 


// 获得对应包名的应用程序名, 用于检测程序是否安装
//static NSString *AppNameOfBID(NSString *bid);

@interface UMNMachineUtil()
@property (nonatomic, retain) UMNReachability   *reachability;
@property (nonatomic, retain) CTTelephonyNetworkInfo *networkInfo;
@end

@implementation UMNMachineUtil

@dynamic attribute;

NSString *name() {
    NSString *result = [[[NSBundle mainBundle] localizedInfoDictionary] objectForKey: CFBundleDisplayName()];
    if (!result)
        result = [[NSBundle mainBundle] objectForInfoDictionaryKey:CFBundleDisplayName()];
    if (!result)
        result = [[NSBundle mainBundle] objectForInfoDictionaryKey:(NSString *)kCFBundleNameKey];
    return result;
}

NSString *version() {
    return [[NSBundle mainBundle] objectForInfoDictionaryKey:(NSString *)kCFBundleVersionKey];
}

NSString *identifier() {
    return [[NSBundle mainBundle] bundleIdentifier];
}


- (NSUInteger)attribute {
    NSUInteger result = DeviceAttributeNone;
    
    result |= DeviceAttributeNone;
    result |= DeviceAttributeNone;
    result |= DeviceAttributeNone;
    result |= ([_reachability currentReachabilityStatus] == ReachableViaWiFi)        ? DeviceAttributeCanUseWiFi       : DeviceAttributeNone;
    result |= spotisIPad()                                                    ? DeviceAttributeIsPad            : DeviceAttributeNone;
    result |= spotisPadUI()                                                   ? DeviceAttributeIsPhoneUI        : DeviceAttributeNone;
    result |= spotisJailbroken()                                                            ? DeviceAttributeIsJailbroken     : DeviceAttributeNone;
    result |= isIfaOpen()                                                               ? DeviceAttributeIsIfaOpen        : DeviceAttributeNone;
    
    return result;
}

+ (UMNMachineUtil *)sharedInstance {
    static UMNMachineUtil *shareDeviceInfo = nil;
    static dispatch_once_t predicate;
    dispatch_once(&predicate, ^{
        shareDeviceInfo = [[UMNMachineUtil alloc] init];
        [shareDeviceInfo _fillInfos];
    });
    return shareDeviceInfo;
}

#pragma mark -
#pragma mark 初始化
// 为了防止别处手滑init了DeviceInfoUtil，所以不在init里初始化
- (void)_fillInfos {
    
    _device         = [joyplatform() copy];
    _deviceDetail       = [[self generateDevDetail] copy];
    _phoneOS        = [[NSString alloc] initWithFormat:@"%@ %@", [[UIDevice currentDevice] systemName], [[UIDevice currentDevice] systemVersion]];
    _countryCode    = [countryCode() copy];
    _language       = [language() copy];
    
    // 网络信息
    _reachability   = [UMNReachability reachabilityWithHostName:www_baidu_com()];
    [_reachability startNotifier];
    _accessPointName = [[self accessPointNameForStatus:[_reachability currentReachabilityStatus]] copy];
    _reachabilityStatus = [_reachability currentReachabilityStatus];
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(checkNetChanged:)
                                                 name: JoykReachabilityChangedNotification()
                                               object: nil];
    
    // 屏幕信息
    CGRect bounds   = [UIScreen mainScreen].bounds;
    CGFloat factor  = spotisRetina() ? 2 : 1;
    _screenWidth    = CGRectGetWidth(bounds)*factor;
    _screenHeight   = CGRectGetHeight(bounds)*factor;
    
    // 运营商信息
    _networkInfo        = [[CTTelephonyNetworkInfo alloc] init];
    _carrierName        = [@"" copy];
    _carrierNameNew     = [@"" copy];
    _mobileCountryCode  = [@"" copy];
    _mobileNetworkCode  = [@"" copy];
    [self getTTInfo];
    [self subscribeCellularProviderUpdateMessage];
}

int wwanNetwork(){
//    NSLog(@"%@",[DeviceInfoUtil sharedInstance].accessPointName);
    if ([[UMNMachineUtil sharedInstance].accessPointName isEqualToString:wifi()]) {
        return 4;
    }else if ([[UMNMachineUtil sharedInstance].accessPointName isEqualToString:None()]){
        
        return 0;
    }
    
    CTTelephonyNetworkInfo *networkStatus = [[CTTelephonyNetworkInfo alloc]init];
    NSString *currentStatus  = networkStatus.currentRadioAccessTechnology;
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyGPRS"]){
//        self.callBackBlock(GPRS);
        //GPRS网络
        return 1;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyEdge"]){
    //    self.callBackBlock(Edge);
        //2.75G的EDGE网络
        return 1;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyWCDMA"]){
      //  self.callBackBlock(WCDMA);
        //3G WCDMA网络
        return 2 ;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyHSDPA"]){
       // self.callBackBlock(HSDPA);
        //3.5G网络
        return 2;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyHSUPA"]){
       // self.callBackBlock(HSUPA);
        //3.5G网络
        return 2;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyCDMA1x"]){
       // self.callBackBlock(CDMA1xNetwork);
        //CDMA2G网络
        return 1;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyCDMAEVDORev0"]){
       // self.callBackBlock(CDMAEVDORev0);
        //CDMA的EVDORev0(应该算3G吧?)
        return 2;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyCDMAEVDORevA"]){
       // self.callBackBlock(CDMAEVDORevA);
        //CDMA的EVDORevA(应该也算3G吧?)
        return 2;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyCDMAEVDORevB"]){
       // self.callBackBlock(CDMAEVDORevB);
        //CDMA的EVDORev0(应该还是算3G吧?)
        return 2;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyeHRPD"]){
       // self.callBackBlock(HRPD);
        //HRPD网络
        return 3;
    }
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyLTE"]){
       // self.callBackBlock(LTE);
        //LTE4G网络
        return 3;
    }
    return 0;
    /*==
     取运营商名字  Objective.subscriberCellularProvider.carrierName
     */
}

#pragma mark -
#pragma mark Generated Infomations
- (NSString *)generateDevDetail {
    NSMutableString *detail = [NSMutableString string];
    [detail appendFormat:@"%@:%@",Device(),joyplatform()];
    [detail appendFormat:@"  %@:%d",Jailbreak(),(spotisJailbroken() ? 1 : 0)];
    [detail appendFormat:@"  OS:%@",[[UIDevice currentDevice] systemName]];
    [detail appendFormat:@"  %@:%@",Version(),[[UIDevice currentDevice] systemVersion]];
    [detail appendFormat:@"  Name:%@",[[UIDevice currentDevice] name]];
    [detail appendFormat:@"  %@:%@",Model(),[[UIDevice currentDevice] model]];
    return detail;
}

#pragma mark -
#pragma mark Reachability Relate
- (NSString *)accessPointNameForStatus:(NetworkSpotStatus)status {
    NSString *name = nil;
    switch (status) {
        case NotReachable:
            name = [NSString stringWithFormat:@"%@", None()];
            break;
        case ReachableViaWWAN:
            name = [NSString stringWithFormat:@"%@", GPRS_3G()];
            break;
        case ReachableViaWiFi:
            name = [NSString stringWithFormat:@"%@", wifi()];
            break;
        default: 
            name = [NSString stringWithFormat:@"%@", None()];
            break;
    }
    return name;
}



- (void)removeObservers {
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:JoykReachabilityChangedNotification()
                                                  object:nil];
}

- (void)checkNetChanged:(NSNotification *)note {
    UMNReachability* curReach = [note object];
    // 基本没可能
    if(![curReach isKindOfClass: [UMNReachability class]]) return;

    YM_RELEASE_SAFELY(_accessPointName);
    _accessPointName = [[self accessPointNameForStatus:[curReach currentReachabilityStatus]] copy];
    
    _reachabilityStatus = [curReach currentReachabilityStatus];
    
    OGINFO(@"网络发生改变:%@", self.accessPointName);
    
    //[[NSNotificationCenter defaultCenter] postNotificationName:ReachabilityChangedNotification object:self];
}

#pragma mark -
#pragma mark Telephony Relate
- (void)getTTInfo {
    if (!_networkInfo) return;
    
    YM_RELEASE_SAFELY(_carrierName);
    YM_RELEASE_SAFELY(_carrierNameNew);
    YM_RELEASE_SAFELY(_mobileCountryCode);
    YM_RELEASE_SAFELY(_mobileNetworkCode)
    
    CTCarrier *carrier = [_networkInfo subscriberCellularProvider];
    if (!carrier) {
        _carrierName        = [@"" copy];
        _carrierNameNew     = [@"" copy];
        _mobileCountryCode  = [@"" copy];
        _mobileNetworkCode  = [@"" copy];
    } else {
        _carrierName        = [[carrier carrierName] copy];
        _carrierNameNew     = [[self convertCarrierName:_carrierName] copy];
        _mobileCountryCode  = [[carrier mobileCountryCode] copy];
        _mobileNetworkCode  = [[carrier mobileNetworkCode] copy];
    }
}

- (NSString *)convertCarrierName:(NSString *)carrierName {
    NSString *result = carrierName;
    if ([carrierName isEqualToString:@"移动"] ||
        [carrierName isEqualToString:@"中国移动"] ||
        [carrierName isEqualToString:@"CHINA MOBILE"]) {
        result = @"1";
    } else if ([carrierName isEqualToString:@"联通"] ||
               [carrierName isEqualToString:@"中国联通"] ||
               [carrierName isEqualToString:@"China Unicom"]) {
        result = @"2";
    } else if ([carrierName isEqualToString:@"电信"] ||
               [carrierName isEqualToString:@"中国电信"] ||
               [carrierName isEqualToString:@"China Telecom"]) {
        result = @"3";
    }
    return result;
}

- (void)subscribeCellularProviderUpdateMessage {
    if (!_networkInfo) return;
    [_networkInfo setSubscriberCellularProviderDidUpdateNotifier:^(CTCarrier *carrier) {
        [[NSNotificationCenter defaultCenter] postNotificationName:kCellularProviderDidUpdateNotification() object:nil];
    }];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(getTTInfo) name:kCellularProviderDidUpdateNotification() object:nil];
}

- (void)unsubscribeCellularProviderUpdateMessage {
    if (!_networkInfo) return;
    [_networkInfo setSubscriberCellularProviderDidUpdateNotifier:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kCellularProviderDidUpdateNotification() object:nil];
}


-(NSString*)CoreServicesCreateTime{
    NSFileManager *file = [NSFileManager defaultManager];
    NSDictionary *dic= [file attributesOfItemAtPath:@"System/Library/CoreServices" error:nil];
    //    NSLog(@"dic %@",dic);
    //    NSLog(@"createTime:%@",[dic objectForKey:@"NSFileCreationDate"]);
    //    NSLog(@"modifiTime:%@",[dic objectForKey:@"NSFileModificationDate"]);
    return [NSString stringWithFormat:@"CT:%@--MT:%@",[dic objectForKey:@"NSFileCreationDate"],[dic objectForKey:@"NSFileModificationDate"]];
}

-(BOOL)IsDebug{
    
    struct kinfo_proc infos_process;
    size_t size_info_proc = sizeof(infos_process);
    pid_t pid_process = getpid(); // pid of the current process
    int mib[] = {CTL_KERN,        // Kernel infos
        KERN_PROC,       // Search in process table
        KERN_PROC_PID,   // the process with pid =
        pid_process};    // pid_process
    
    //Retrieve infos for current process in infos_process
    int ret = sysctl(mib, 4, &infos_process, &size_info_proc, NULL, 0);
    if (ret) return 0;             // sysctl failed
    
    struct extern_proc process = infos_process.kp_proc;
    int flags_process = process.p_flag;
    
    return flags_process & P_TRACED;        // value of the debug flag
}
-(NSString *)CL_dynamicFrames{
    //检查动态库
    uint32_t count = _dyld_image_count();
    NSMutableArray *mutableArray = [NSMutableArray new];
    for (uint32_t i = 0 ; i < count; ++i) {
        NSString *name = [[NSString alloc]initWithUTF8String:_dyld_get_image_name(i)];
        if ([name hasPrefix:@"/Library/"]) {
            [mutableArray addObject:name];
        }
    }
    if (mutableArray.count <= 0) {
        return @"";
    }
    return [mutableArray componentsJoinedByString:@"|"];
}

-(NSString*)systemOpeningTime{
    
    //    struct timeval boottime;
    //    size_t len = sizeof(boottime);
    //    int mib[2] = { CTL_KERN, KERN_BOOTTIME };
    //    if( sysctl(mib, 2, &boottime, &len, NULL, 0) < 0 )
    //    {
    //        return -1.0;
    //    }
    //    time_t bsec = boottime.tv_sec, csec = time(NULL);
    //
    //    return difftime(csec, bsec);
    struct timeval boottime;
    size_t len = sizeof(boottime);
    int mib[2] = { CTL_KERN, KERN_BOOTTIME };
    if( sysctl(mib, 2, &boottime, &len, NULL, 0) < 0 )
    {
        return @"";
    }
    time_t bsec = boottime.tv_sec;
    
    NSTimeInterval time= bsec;//因为时差问题要加8小时 == 28800 sec
    NSDate *detaildate=[NSDate dateWithTimeIntervalSince1970:time];
    //    NSLog(@"date:%@",[detaildate description]);
    //实例化一个NSDateFormatter对象
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    //设定时间格式,这里可以设置成自己需要的格式
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    
    NSString *currentDateStr = [dateFormatter stringFromDate: detaildate];
    dateFormatter = nil;
    return currentDateStr;
}
@end
