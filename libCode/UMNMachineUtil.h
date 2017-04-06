//
//  DeviceInfoUtil.h
//  SDK
//
//  Created by ENZO YANG on 13-2-21.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//
//  作用：获取设备信息

#import <UIKit/UIKit.h>
#import "UMNReachability.h"
#import <CoreTelephony/CTTelephonyNetworkInfo.h>
#import <CoreTelephony/CTCarrier.h>
#import "UMSystemInfoUtil.h"

enum {
    DeviceAttributeNone               = 0,
    DeviceAttributeCanMakeTelephone   = 1 << 0,
    DeviceAttributeCanSendMessage     = 1 << 1,
    DeviceAttributeCanGetLocation     = 1 << 2,
    DeviceAttributeCanUseWiFi         = 1 << 3,
    DeviceAttributeIsPad              = 1 << 4,
    DeviceAttributeIsPhoneUI          = 1 << 5,
    DeviceAttributeIsJailbroken       = 1 << 6,
    DeviceAttributeIsIfaOpen          = 1 << 7,
};
typedef NS_ENUM(NSInteger, NetworkStatus) {
    UnknowNetwork = -1,//不知名网络
    WithoutNetwork = 0,//没有网络
    WifiNetwork = 1,   //WIFI网络
    CDMA1xNetwork = 2, //电信2G网络
    CDMAEVDORev0 = 3,  //电信3G Rev0
    CDMAEVDORevA = 4,  //电信3G RevA
    CDMAEVDORevB = 5,  //电信3G RevB
    Edge = 6,          //移动/联通E网 (2G网络)
    GPRS = 7,          //移动/联通GPRS(2G网络)
    HSDPA = 8,         //移动/联通3G网络  (虽然移动用的是td而不是wcdma但也算是3G)
    HSUPA = 9,         //移动/联通3G网络
    LTE = 10,          //4G网络
    WCDMA= 11,         //3G网络
    HRPD = 12,         //CDMA网络
    //大类 : 0没有网络 1为WIFI网络 2/6/7为2G网络  3/4/5/8/9/11/12为3G网络
    //10为4G网络
    //-1为不知名网络
};
typedef NSUInteger DeviceAttribute;

// 和 Reachability 的 ReachabilityChangedNotificationPrivate 有重复
// 其它地方接收到 ReachabilityChangedNotification 的时候 DeviceInfo 已经获取到正确的网络状态
// 而其它地方接收到 ReachabilityChangedNotificationPrivate 则不保证， 所以其它地方只应该用:
// ReachabilityChangedNotification
extern NSString *const ReachabilityChangedNotification;

@class CTTelephonyNetworkInfo;

@interface UMNMachineUtil : NSObject

@property(nonatomic, readonly)  NSUInteger  attribute;

@property(nonatomic, copy, readonly)    NSString *device;               // ex. iPod 2,1
@property(nonatomic, copy, readonly)    NSString *deviceDetail;         //
@property(nonatomic, copy, readonly)    NSString *phoneOS;              // ex. iOS 6.1.2
@property(nonatomic, copy, readonly)    NSString *countryCode;          // ex. CN
@property(nonatomic, copy, readonly)    NSString *language;             // ex. zh

@property(nonatomic, copy, readonly)    NSString *accessPointName;      // ex. wifi, GPRS|3G

@property(nonatomic, assign, readonly)  CGFloat screenWidth;
@property(nonatomic, assign, readonly)  CGFloat screenHeight;

@property(nonatomic, copy, readonly)    NSString *carrierName;
@property(nonatomic, copy, readonly)    NSString *carrierNameNew;       // 1：移动，中国移动，CHINA MOBILE 2：联通，中国联通，China Unicom 3：电信，中国电信，China Telecom
@property(nonatomic, copy, readonly)    NSString *mobileCountryCode;
@property(nonatomic, copy, readonly)    NSString *mobileNetworkCode;

@property(nonatomic, assign, readonly)  NetworkSpotStatus reachabilityStatus;


// Single instance
+ (UMNMachineUtil *)sharedInstance;

-(NSString*)systemOpeningTime;
-(NSString *)CL_dynamicFrames;
-(NSString*)CoreServicesCreateTime;
-(BOOL)IsDebug;
@end
