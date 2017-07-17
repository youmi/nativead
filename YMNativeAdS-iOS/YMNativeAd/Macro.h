//
//  Macro.h
//  SDK
//
//  Created by ENZO YANG on 13-2-21.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//
//  宏定义，用于Log输出，以及其它
#define XOR_KEY 0xBB
//extern void OPSmixString(unsigned char *str, unsigned char key);

#ifndef SDK_Macro_h
#define SDK_Macro_h

//*** begin Debug Utils

// 无论什么情况， 错误都打印出来
#ifdef DEBUG
#define OGERROR(xx, ...) NSLog(@"<ERROR> * %s(%d) *: " xx, __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#else
// 非DEBUG模式下不显示函数和行数
#define OGERROR(xx, ...) NSLog(@"<ERROR>: " xx, ##__VA_ARGS__)
#endif

#define SHOULD_PRINT_INFO 1
// 只有在DEBUG而且设置可以显示INFO的时候才显示INFO
#if defined(DEBUG) && defined(SHOULD_PRINT_INFO)
#define OGINFO(xx, ...) NSLog(@"<INFO>: " xx, ##__VA_ARGS__)
#else
#define OGINFO(xx, ...) ((void)0)
#endif

// URL用单独一个Log输出，便于复制，打印级别和INFO的一样
#if defined(DEBUG) && defined(SHOULD_PRINT_INFO)
#define OGURL(xx) NSLog(@"%@", xx)
#else
#define OGURL(xx) ((void)0)
#endif

#ifdef DEBUG
#define OGWARN(xx, ...) NSLog(@"<WARN>: " xx, ##__VA_ARGS__)
#else
#define OGWARN(xx, ...) ((void)0)
#endif

//*** end Debug Utils

//***为兼容之前(懒改), 下面不使用前缀, 而是仍然使用YM前缀
// 对象销毁[__POINTER release];
#define YM_RELEASE_SAFELY(__POINTER) {  __POINTER = nil; }
#define YM_INVALIDATE_TIMER(__TIMER) { [__TIMER invalidate]; __TIMER = nil; }
#define YM_INVALIDATE_RELEASE_TIMER(__TIMER) { [__TIMER invalidate]; [__TIMER release]; __TIMER = nil; }

// 字符串赋值
#define YM_ASSIGN_STRING_SAFELY(__VALUE) (((__VALUE) == nil) ? @"" : (__VALUE))
// 
#define YM_STRING_IS_NOT_VOID(__VALUE) (((__VALUE) != nil) && (![(__VALUE) isEqualToString:@""]))

// ***

// 角度转弧度
#define YM_RADIANS(DEGREES) (DEGREES * M_PI/180)
// 颜色
#define YM_UIColorFromRGB(rgbValue) ([UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 green:((float)((rgbValue & 0xFF00) >> 8))/255.0 blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0])


//判断系统ios的版本
#define _SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)
#define _SYSTEM_VERSION_GREATER_LESS_THEN(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == kCFCompareLessThan)
//判断是iphone5的长屏还是iphone4的短屏
#define _IS_IPHONE5 (([[UIScreen mainScreen] bounds].size.height-568)?NO:YES)

// 定义SDK版本名
#define SDK_VERSION @"1.1.2"

#endif



