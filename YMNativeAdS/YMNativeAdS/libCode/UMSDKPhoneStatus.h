//
//  MobileStatus.h
//  testDeviceInfo
//
//  Created by  on 14/12/25.
//  Copyright (c) 2014年 yuxuhong. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface UMSDKPhoneStatus : NSObject

@property (nonatomic, assign) CGFloat MotionX;//运动状态监测 x,y,z
@property (nonatomic, assign) CGFloat MotionY;
@property (nonatomic, assign) CGFloat MotionZ;
@property (nonatomic, assign) CGFloat AccelerometerX;//加速度监测 x,y,z
@property (nonatomic, assign) CGFloat AccelerometerY;
@property (nonatomic, assign) CGFloat AccelerometerZ;
@property (nonatomic, assign) CGFloat GyroGraphX;//陀螺仪坐标 x,y,z
@property (nonatomic, assign) CGFloat GyroGraphY;
@property (nonatomic, assign) CGFloat GyroGraphZ;

+ (UMSDKPhoneStatus *)sharedInstanceMS;

@end


void initSetup(UMSDKPhoneStatus *status);

NSString *getOnLineNetFlowNumber();

//CGFloat getBatterySurplus();

BOOL isOnlineVPN();
