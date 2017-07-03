//
//  SystemInfo.h
//  
//
//  Created by 陈建峰 on 15/11/21.
//  Copyright © 2015年 yuxuhong. All rights reserved.
//

#import <Foundation/Foundation.h>

NSString *name();
NSString *version();
NSString *identifier();

NSString *Openudid();
NSString *MAC_ADDR();
BOOL isIfaOpen();

NSString *OriginIFA();
NSString *IFA_function();
BOOL isIfaOpen();

BOOL spotisSimulator();
BOOL spotisIPhoneSimulator();
BOOL spotisIPadSimulator();
BOOL spotisDevice();
BOOL spotisIPhoneOrIPodTouch();
BOOL spotisIPhone();
BOOL spotisIPodTouch();
BOOL spotisIPad();
BOOL spotisPadUI();    // 包括iPad 和 iPad模拟器
BOOL spotisPhoneUI();  // 包括iPhone， iPode 以及 iPhone模拟器
BOOL spotisRetina();
BOOL spotisMultitaskingSupported();
BOOL spotisJailbroken();
int wwanNetwork();
NSString *countryCode();
NSString *language();
NSString * systemMainVersion();
NSString *joyplatform();

id fetchSSIDInfo();
BOOL CanOpenURl(NSString *urlstr);
//static BOOL CanOpenURl;
