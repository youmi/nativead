//
//  UMNCommonUseUtil.h
//  SDK
//
//  Created by ENZO YANG on 13-2-25.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//
//  各种不好归类的工具


#import <UIKit/UIKit.h>

// 上层可能会设置好UIWindow，但不能直接依赖上层，于是添加个接口。
@protocol WindowGetterProtocol <NSObject>
+ (UIWindow *)window;
@end

/// conver appid from 16 to hex 11
NSString *ConverAppIDToHex11(NSString *appID);

NSString *CreateRandStrinWithLength(NSUInteger length);

// Date formatter
NSString *fixStringForDate(NSDate *date);
NSString *fixStringForGMTFromDate(NSDate *date);
NSInteger secondsForDate(NSDate *date);

// Color
// #99000000 -- 处理最后8个字符
NSString *ARGBStringFromColor(UIColor *color);

NSInteger integerFromHexChar(char c);

UIWindow *applicationFrontNormalWindow();

NSString*urlencode(NSString*unescapedString);
// base64后,替换特别的三个字符就行,不需要再做urlencode('+'->'-', '='->'_', '/'->'.')
NSString*replaceSpecialChar(NSString*unescapedString);
NSString *generateSign(NSDictionary *params,NSString *access_key);
 
