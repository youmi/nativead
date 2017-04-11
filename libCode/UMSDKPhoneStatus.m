//
//  MobileStatus.m
//  testDeviceInfo
//
//  Created by  on 14/12/25.
//  Copyright (c) 2014年 yuxuhong. All rights reserved.
//

#import "UMSDKPhoneStatus.h"
#include <ifaddrs.h>
#include <net/if.h>
#import <CoreMotion/CoreMotion.h>
#import "Macro.h"

@interface UMSDKPhoneStatus()
@property (nonatomic, retain) CMMotionManager *mManager;
@end

@implementation UMSDKPhoneStatus

+ (UMSDKPhoneStatus *)sharedInstanceMS {
    static UMSDKPhoneStatus *shareManage = nil;
    static dispatch_once_t predicate;
    dispatch_once(&predicate, ^{
        shareManage = [[UMSDKPhoneStatus alloc] init];
    });
    return shareManage;
}

- (void)dealloc {
    _mManager = nil;
}
void initSetup(UMSDKPhoneStatus *status)
{
    status.mManager = [[CMMotionManager alloc] init];
    getMotionGraph(status);
}

#pragma mark -
#pragma mark motiongraph
void getMotionGraph(UMSDKPhoneStatus *status)
{
//    NSTimeInterval updateInterval = 10.0;
    status.MotionX = 0.0;//deviceMotion.attitude.roll;
    status.MotionY = 0.0;//deviceMotion.attitude.pitch;
    status.MotionZ = 0.0;//deviceMotion.attitude.yaw;
    //rotationRate陀螺仪
    status.GyroGraphX = 0.0;//deviceMotion.rotationRate.x;
    status.GyroGraphY = 0.0;//deviceMotion.rotationRate.y;
    status.GyroGraphZ = 0.0;//deviceMotion.rotationRate.z;
    // userAcceleration加速度
    status.AccelerometerX = 0.0;//deviceMotion.userAcceleration.x;
    status.AccelerometerY = 0.0;//deviceMotion.userAcceleration.y;
    status.AccelerometerZ = 0.0;//deviceMotion.userAcceleration.z;
//    if ([status.mManager isDeviceMotionAvailable] == YES) {
//        [status.mManager setDeviceMotionUpdateInterval:updateInterval];
//        [status.mManager startDeviceMotionUpdatesToQueue:[NSOperationQueue mainQueue] withHandler:^(CMDeviceMotion *deviceMotion, NSError *error) {
//            //attitude属性
//            status.MotionX = 0.0;//deviceMotion.attitude.roll;
//            status.MotionY = 0.0;//deviceMotion.attitude.pitch;
//            status.MotionZ = 0.0;//deviceMotion.attitude.yaw;
//            //rotationRate陀螺仪
//            status.GyroGraphX = 0.0;//deviceMotion.rotationRate.x;
//            status.GyroGraphY = 0.0;//deviceMotion.rotationRate.y;
//            status.GyroGraphZ = 0.0;//deviceMotion.rotationRate.z;
//            // userAcceleration加速度
//            status.AccelerometerX = 0.0;//deviceMotion.userAcceleration.x;
//            status.AccelerometerY = 0.0;//deviceMotion.userAcceleration.y;
//            status.AccelerometerZ = 0.0;//deviceMotion.userAcceleration.z;
////            OGINFO(@"MotionGraph: %f %f %f", status.MotionX,status.MotionY, status.MotionZ);
//        }];
//    }
}

#pragma mark -
#pragma mark getDataCounters
// iOS获取设备流量使用情况，iPhone Data Usage Tracking/Monitoring，获取的是开机后WIFI,3G/GPRS网络下使用流量(仅仅只能获取开机后的)。
NSString *getOnLineNetFlowNumber()
{
    BOOL   success;
    struct ifaddrs *addrs;
    const struct ifaddrs *cursor;
    const struct if_data *networkStatisc;
    
    int WiFiSent = 0;
    int WiFiReceived = 0;
    int WWANSent = 0;
    int WWANReceived = 0;
    
    NSString *name=[[NSString alloc]init];
    
    success = getifaddrs(&addrs) == 0;
    if (success)
    {
        cursor = addrs;
        while (cursor != NULL)
        {
            name=[NSString stringWithFormat:@"%s",cursor->ifa_name];
            //NSLog(@"ifa_name %s == %@\n", cursor->ifa_name,name);
            // names of interfaces: en0 is WiFi ,pdp_ip0 is WWAN
            if (cursor->ifa_addr->sa_family == AF_LINK)
            {
                if ([name hasPrefix:@"en"])
                {
                    networkStatisc = (const struct if_data *) cursor->ifa_data;
                    WiFiSent+=networkStatisc->ifi_obytes;
                    WiFiReceived+=networkStatisc->ifi_ibytes;
                    //NSLog(@"WiFiSent %d ==%d",WiFiSent,networkStatisc->ifi_obytes);
                    //NSLog(@"WiFiReceived %d ==%d",WiFiReceived,networkStatisc->ifi_ibytes);
                }
                if ([name hasPrefix:@"pdp_ip"])
                {
                    networkStatisc = (const struct if_data *) cursor->ifa_data;
                    WWANSent+=networkStatisc->ifi_obytes;
                    WWANReceived+=networkStatisc->ifi_ibytes;
                    //NSLog(@"WWANSent %d ==%d",WWANSent,networkStatisc->ifi_obytes);
                    //NSLog(@"WWANReceived %d ==%d",WWANReceived,networkStatisc->ifi_ibytes);
                }
            }
            cursor = cursor->ifa_next;
        }
        freeifaddrs(addrs);
    }
    
    return [NSString stringWithFormat:@"%d",(WiFiReceived + WWANReceived)];//只关心手机下载的流量,不关心上传的流量.
}


#pragma mark -
#pragma mark battery level
//CGFloat getBatterySurplus()
//{
//    return [UIDevice currentDevice].batteryLevel;
//}

#pragma mark -
#pragma mark vpn
BOOL isOnlineVPN()
{
    struct ifaddrs *interfaces = NULL;
    struct ifaddrs *temp_addr = NULL;
    int success = 0;
    
    // retrieve the current interfaces - returns 0 on success
    success = getifaddrs(&interfaces);
    
    if (success == 0) {
        // Loop through linked list of interfaces
        temp_addr = interfaces;
        
        while (temp_addr != NULL) {
            NSString *string = [NSString stringWithFormat:@"%s" , temp_addr->ifa_name];
            if ([string rangeOfString:@"tap"].location != NSNotFound ||
                [string rangeOfString:@"tun"].location != NSNotFound ||  //OpenVPN
                [string rangeOfString:@"ppp"].location != NSNotFound){   //L || P
                return YES;
            }
            
            temp_addr = temp_addr->ifa_next;
        }
    }
    
    // Free memory
    
    freeifaddrs(interfaces);
    return NO;
}

@end
