//
//  UMNSDKConstants.h
//  SDK
//
//  Created by ENZO YANG on 13-2-26.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
// Communication Protocol Version
//extern NSString *const kSPTDefaultProtocolVersion;
//extern NSString *const kSPTBannerProtocolVersion;
//extern NSString *const ZYin_3_0();

// app info
extern NSString *const ZYin__Simulator_();
extern NSString *const ZYin__Simulator_();
extern NSString *const UM_privacyURL();

// SDK Version
extern NSString *const SDK_VERSION();
#define kSPTBannerSDKVersion SDK_VERSION()
#define kSPTWaSDKVersion SDK_VERSION()

//extern NSString *const  kSPTWaSDKSubversion;

// App Promote Channel
extern NSInteger const kSPTDefaultAppChannel;
extern NSString *const ZYin_Default();

// special character for signature and encrypt
//extern NSString *const kSpotStatEncryptSpecialCharacter;
//extern NSString *const kSpotBannerEncryptSpecialCharacter;
extern NSString *const ZYin_NPHTxSQmAA4ASmwu();

// encrypt special prime
//extern NSInteger const kSpotStatEncryptSpecialPrime;
//extern NSInteger const kSpotBannerEncryptSpecialPrime;
//extern NSInteger const kSptEncryptSpecialPrime;

#define kSPTSDKPromoteChannel 1

#define kSPTProductIDBanner  1
#define kSPTProductIDSpot    2
#define kSPTProductIDWa    3


// 请求的输出格式 0:JSON，1:XML，2:HTML
enum {
	OutputFormatJSONType		= 0,	//JSON
	OutputFormatXMLType		= 1,	//XML
	OutputFormatHTMLType		= 2		//HTML
};
typedef NSUInteger OutputFormatType;

// 平台类型 0:java SDK，1:symbian sdk, 2:WM sdk, 3:android sdk, 4:other, 5:iphone, 6:black brrey【新增iphone和黑莓平台】
enum {
	SourcePlatformJava			= 0,
	SourcePlatformSymbian			= 1,
	SourcePlatformWindowsMobile	= 2,
	SourcePlatformAndroid         = 3,
	SourcePlatformOther           = 4,
	SourcePlatformiOS             = 5,
	SourcePlatformBlackBrrey      = 6
};
typedef NSUInteger SourcePlatform;


#define kIllegitimateSpecialCharacterTarget          @"&"
#define kIllegitimateSpecialCharacterReplacement	 @"_"

#define kSPTRandomStringLength                     4

// Notifications
//extern NSString *const kSPTaunchInitRequestFinishedNotification;

extern NSString *const ZYin_SpotUniversalTid();

@interface UIImage (bundleimg)
+(UIImage *)imagesNamedFromCustomBundle:(NSString *)imgName;
@end
@implementation UIImage(bundleimg)
+ (UIImage *)imagesNamedFromCustomBundle:(NSString *)imgName
{
    NSString *bundlePath = [[NSBundle mainBundle].resourcePath stringByAppendingPathComponent:@"umspot.bundle"];
    NSBundle *bundle = [NSBundle bundleWithPath:bundlePath];
    NSString *img_path = [bundle pathForResource:imgName ofType:@"png"];
    return [UIImage imageWithContentsOfFile:img_path];
}
@end
