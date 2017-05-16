//
//  NetworkToolkit.h
//  SDK
//
//  Created by ENZO YANG on 13-2-25.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//
//  作用：网络相关，更多是网络地址相关的操作

#import <Foundation/Foundation.h>


void pingURL(NSString *url);

BOOL isAppStoreLink(NSString *link);

NSNumber *extractAppStoreID(NSString *link);

NSDictionary *parseURLQuery(NSString *query);

NSString *generateQueryString(NSDictionary*query);

NSString *encodeURLStringSavely(NSString *url);

