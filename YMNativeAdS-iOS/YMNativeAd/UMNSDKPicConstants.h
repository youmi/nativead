//
//  PicConstants.h
//  SDK
//
//  Created by ENZO YANG on 13-3-7.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Macro.h"
//广告大小
#define YM_SPOTS_WIDTH_300  300
#define YM_SPOTS_HEIGHT_250 251

#define kSpotBorderWidth    2.0f
#define kSpotCornerRadius   1.0f
#define kSpotInerCornerRadius  1.0f

#define kSpotIpadFactor     2.0f

#define kSpotSlideSourceHeight 9.0f


typedef enum {
    kSPTSptTypePortrait = 0,//竖屏
    kSPTSptTypeLandscape = 1,//横屏
    kSPTSptTypeBoth = 2,//支持横竖屏的广告.
} SptType;

 
//notification
extern NSString *const ZYin_kSPTSptTappedNotification();
extern NSString *const UM_kSPTSptCloseAdNotification();
extern NSString *const ZYin_spot();
extern NSString *const ZYin_kSPTSptShowActionNotification();
//extern NSString *const kSPTSptTappedNotificationSpotDataKey;
//extern NSString *const kSPTSptShowActionNotification;
//extern NSString *const kSPTSptShowActionNotificationSpotDataKey;
//extern NSString *const kSPTSptMoreButtonPressedNotification;

//imageString
extern NSString *const kSPTSptCloseButtonImageString;//关闭按钮
extern NSString *const kSPTSptconfirmButtonImageString;//二次确认按钮
extern NSString *const kSPTogoImageString;//右下角的logo
extern NSString *const kSPTogoImageString_ipad;//右下角的logo ipad

extern NSString *const kSPWebCloseBtnString;
extern NSString *const kSPWebMoreBtnString;


