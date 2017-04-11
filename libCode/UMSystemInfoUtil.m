//
//  SystemInfo.m
//  
//
//  Created by 陈建峰 on 15/11/21.
//  Copyright © 2015年 yuxuhong. All rights reserved.
//

#import "UMSystemInfoUtil.h"
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
#import <SystemConfiguration/CaptiveNetwork.h>
#import <objc/message.h>
#include <ifaddrs.h>
#import <sys/stat.h>
#import <dlfcn.h>
#import <mach-o/dyld.h>
#import <AdSupport/AdSupport.h>
#import "UMNFileUtil.h"

//static NSString *const CellularProviderDidUpdateNotification = @"kCellularProviderDidUpdateNotification";
static NSString *const ZYin_kCellularProviderDidUpdateNotification(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'k'),(XOR_KEY ^ 'C'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'u'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'r'),(XOR_KEY ^ 'P'),(XOR_KEY ^ 'r'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 'v'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'r'),(XOR_KEY ^ 'D'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'U'),(XOR_KEY ^ 'p'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'N'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'f'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'c'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'o'),(XOR_KEY ^ 'n'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"kCellularProviderDidUpdateNotification";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}

static NSString* GetSysInfoByName(char *typeSpecifier) {
    size_t size;
    sysctlbyname(typeSpecifier, NULL, &size, NULL, 0);
    char *answer = malloc(size);
    sysctlbyname(typeSpecifier, answer, &size, NULL, 0);
    NSString *results = [NSString stringWithUTF8String:answer];
    free(answer);
    return results;
}

static NSUInteger GetSysInfo(uint typeSpecifier) {
    size_t size = sizeof(int);
    int results;
    int mib[2] = {CTL_HW, typeSpecifier};
    sysctl(mib, 2, &results, &size, NULL, 0);
    return (NSUInteger) results;
}

//static NSString *const kClientIDSpecialCharacteristic       = @"MGnEt6aj6ZXRNwZ4";
static NSString*const ZYin_MGnEt6aj6ZXRNwZ4(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'M'),(XOR_KEY ^ 'G'),(XOR_KEY ^ 'n'),(XOR_KEY ^ 'E'),(XOR_KEY ^ 't'),(XOR_KEY ^ '6'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'j'),(XOR_KEY ^ '6'),(XOR_KEY ^ 'Z'),(XOR_KEY ^ 'X'),(XOR_KEY ^ 'R'),(XOR_KEY ^ 'N'),(XOR_KEY ^ 'w'),(XOR_KEY ^ 'Z'),(XOR_KEY ^ '4'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"MGnEt6aj6ZXRNwZ4";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}

//static NSString *const kSPTOriginIFAKey                     = @"oriifa";
static NSString*const ZYin_oriifa(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'o'),(XOR_KEY ^ 'r'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'f'),(XOR_KEY ^ 'a'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"oriifa";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}

#pragma mark -
#pragma mark Static Methods
NSString *GetCID(){
   static NSString *cid = nil;
    if (cid) {
        return cid;
    }
    
    // permanent
    NSString *theID = MAC_ADDR();
    if (!YM_STRING_IS_NOT_VOID(theID)) {
        theID = IFA_function();
    }
    NSString *permanent = theID;
    NSString *cid_cat = [[NSString alloc] initWithFormat:@"%@%@", permanent, ZYin_MGnEt6aj6ZXRNwZ4()];
    
    NSString *cid_str = md5HexDigest(cid_cat);
    const char *cid_cstr = [cid_str UTF8String];
    char *cid_64_cstr = (char *)malloc(sizeof(char) * (12 + 2));
    cid_64_cstr[13] = '\0';
    SPT_hex_to_64(cid_64_cstr, cid_cstr, 7, 18);
    
    // add Check bit
    int total = 0;
    for (int i = 0; i < 12; i++) {
        char *dec_sub_cstr = (char *)malloc(sizeof(char) * (1 + 1));
        int dec = SPT_64dec(SPT_substr(dec_sub_cstr, cid_64_cstr, i, 1));
        free(dec_sub_cstr);
        
        if (i == 2 || i == 3 || i == 5 || i == 7 || i == 11) {
            dec *= i;
        } else {
            dec = pow(dec, 2);
        }
        total += (dec < 64) ? 0 : dec >> 6;
        total += dec & 63;
    }
    
    total &= 63;
    total = (64 - total)%64;
    cid_64_cstr[12] = SPT_64dic[total];
    
    //不能用autorelease 这里需要的cid指向的内存别释放，因为用了static
    cid = [[NSString stringWithUTF8String:cid_64_cstr] copy];
    // release
    free(cid_64_cstr);
    
    return cid;
}

NSString *Openudid(){
    NSString* data = load(ZYin_kCellularProviderDidUpdateNotification());
    if (!data && [data isEqualToString:@""]) {
        // IDs
        data           = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
        data = [[[data stringByReplacingOccurrencesOfString:@"-" withString:@""] lowercaseString] copy];
        save(ZYin_kCellularProviderDidUpdateNotification(), data);
    }
    
    return data;
}

NSString *MAC_ADDR() {
//    if ([[[UIDevice currentDevice] systemVersion] integerValue] >= 7) {
//        return @"";
//    }
//    
//    int                 mib[6];
//    size_t              len;
//    char                *buf;
//    unsigned char       *ptr;
//    struct if_msghdr    *ifm;
//    struct sockaddr_dl  *sdl;
//    
//    mib[0] = CTL_NET;
//    mib[1] = AF_ROUTE;
//    mib[2] = 0;
//    mib[3] = AF_LINK;
//    mib[4] = NET_RT_IFLIST;
//    
//    if ((mib[5] = if_nametoindex("en0")) == 0) {
//        return NULL;
//    }
//    
//    if (sysctl(mib, 6, NULL, &len, NULL, 0) < 0) {
//        return NULL;
//    }
//    
//    if ((buf = malloc(len)) == NULL) {
//        return NULL;
//    }
//    
//    if (sysctl(mib, 6, buf, &len, NULL, 0) < 0) {
//        free(buf);
//        return NULL;
//    }
//    
//    ifm = (struct if_msghdr *)buf;
//    sdl = (struct sockaddr_dl *)(ifm + 1);
//    ptr = (unsigned char *)LLADDR(sdl);
//    NSString *macAddress = [NSString stringWithFormat:@"%02X%02X%02X%02X%02X%02X",
//                            *ptr, *(ptr+1), *(ptr+2), *(ptr+3), *(ptr+4), *(ptr+5)];
//    macAddress = [macAddress lowercaseString];
//    free(buf);
//    
//    return macAddress;
    return @"";
}

BOOL isIfaOpen() {
    BOOL result = NO;
    result = [[ASIdentifierManager sharedManager] isAdvertisingTrackingEnabled];
    return result;
}

NSString *OriginIFA(){
    NSString *ifa = [UMNSDKKeyValue shareKeyValue]->stringForKey(ZYin_oriifa());
    return ifa;
}

NSString *IFA_function() {
    NSString *result = @"";
    NSUUID *ifa = [[ASIdentifierManager sharedManager] advertisingIdentifier];
    result = [[[ifa UUIDString] stringByReplacingOccurrencesOfString:@"-" withString:@""] lowercaseString];
    return result;
}

BOOL spotisSimulator() {
#if TARGET_IPHONE_SIMULATOR
    return YES;
#else
    return NO;
#endif
}

BOOL spotisDevice() {
    return spotisSimulator();
}

BOOL spotisIPhoneSimulator() {
    return spotisSimulator() && spotisPhoneUI();
}

BOOL spotisIPadSimulator() {
    return spotisSimulator() && spotisPadUI();
}

BOOL spotisIPhoneOrIPodTouch() {
    return spotisIPhone() || spotisIPodTouch();
}

BOOL spotisIPhone() {
    NSString *platform = joyplatform();
    if (CFStringFind((CFStringRef)platform, (CFStringRef)@"iPhone", kCFCompareCaseInsensitive).length > 0) {
        return YES;
    }
    
    return NO;
}

BOOL spotisIPodTouch() {
    NSString *platform = joyplatform();
    if (CFStringFind((CFStringRef)platform, (CFStringRef)@"iPod", kCFCompareCaseInsensitive).length > 0) {
        return YES;
    }
    
    return NO;
}

BOOL spotisIPad() {
    NSString *platform = joyplatform();
    if (CFStringFind((CFStringRef)platform, (CFStringRef)@"iPad", kCFCompareCaseInsensitive).length > 0) {
        return YES;
    }
    
    return NO;
}

BOOL spotisPadUI() {
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        return YES;
    }
    return NO;
}

BOOL spotisPhoneUI() {
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
        return YES;
    } else {
        return NO;
    }
}

BOOL spotisRetina() {
    return ([[UIScreen mainScreen] respondsToSelector:@selector(scale)] && [[UIScreen mainScreen] scale] == 2);
}

BOOL spotisMultitaskingSupported() {
    BOOL multiTaskingSupported = NO; // 没必要判断的了，留着吧
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wundeclared-selector"
#pragma clang diagnostic ignored "-Wobjc-method-access"
#pragma clang diagnostic ignored "-Wint-conversion"
    if ([[UIDevice currentDevice] respondsToSelector:@selector(isMultitaskingSupported)]) {
        multiTaskingSupported = [(id)[UIDevice currentDevice] isMultitaskingSupported];
    }
#pragma clang diagnostic pop
    return multiTaskingSupported;
}

BOOL CanOpenURl(NSString *urlstr){
    double version = [[UIDevice currentDevice].systemVersion doubleValue];//判定系统版本。
    if (version>=9.0) {
        return NO;
    }
    if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:urlstr]]) {
        return YES;
    }
    return NO;
}

BOOL spotisJailbroken() {
#if TARGET_IPHONE_SIMULATOR
    return NO;
    
#else
    if (CanOpenURl(@"cydia:")) {
        return YES;
    }
    
    BOOL isJailbroken = NO;
    
    FILE *f = fopen("/bin/bash", "r");
    
    if (!(errno == ENOENT)) {
        isJailbroken = YES;
    }
    fclose(f);
    return isJailbroken;
#endif
}

NSString *countryCode() {
    NSLocale *locale = [NSLocale currentLocale];
    NSString *countryCode = [locale objectForKey:NSLocaleCountryCode];
    return countryCode;
}

NSString *language() {
    NSString *language;
    NSLocale *locale = [NSLocale currentLocale];
    if ([[NSLocale preferredLanguages] count] > 0) {
        language = [[NSLocale preferredLanguages]objectAtIndex:0];
    } else {
        language = [locale objectForKey:NSLocaleLanguageCode];
    }
    
    return language;
}

NSString* systemMainVersion() {
    return [[UIDevice currentDevice] systemVersion];
}

NSString *joyplatform() {
    return GetSysInfoByName("hw.machine");
}

NSString *SBhwmodel() {
    return GetSysInfoByName("hw.model");
}

NSUInteger cpuFrequency() {
    return GetSysInfo(HW_CPU_FREQ);
}

NSUInteger busFrequency() {
    return GetSysInfo(HW_BUS_FREQ);
}

NSUInteger totalMemory() {
    return GetSysInfo(HW_PHYSMEM);
}

NSUInteger userMemory() {
    return GetSysInfo(HW_USERMEM);
}

NSUInteger maxSocketBufferSize() {
    return GetSysInfo(KIPC_MAXSOCKBUF);
}

NSNumber *totalDiskSpace() {
    NSDictionary *fattributes = [[NSFileManager defaultManager] attributesOfFileSystemForPath:NSHomeDirectory() error:nil];
    return [fattributes objectForKey:NSFileSystemSize];
}

NSNumber *freeDiskSpace() {
    NSDictionary *fattributes = [[NSFileManager defaultManager] attributesOfFileSystemForPath:NSHomeDirectory() error:nil];
    return [fattributes objectForKey:NSFileSystemFreeSize];
}

id fetchSSIDInfo()
{
    NSArray *ifs = (id)CFBridgingRelease(CNCopySupportedInterfaces());
    id info = nil;
    for (NSString *ifnam in ifs) {
        info = (id)CFBridgingRelease(CNCopyCurrentNetworkInfo((CFStringRef)ifnam));
        if (info && [info count]) {

            break;
        }
    }
    return info;
}


