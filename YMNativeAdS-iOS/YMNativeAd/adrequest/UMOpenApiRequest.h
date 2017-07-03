//
//  UMApiRequest.h
//  YMNativeAdS
//
//  Created by linxiaolong on 2017/3/24.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "UMNDataModel.h"
#import "UMNBackgroundQueue.h"

typedef void (^CompletionBlock)(NSInteger code);
typedef void (^FinishBlock)(NSError *error);
typedef void (^ListBlock)(NSArray *sList, NSError *error);

@interface UMOpenApiRequest : NSObject

+ (id)shareInstanceRef;

@property (nonatomic, retain) UMNBackgroundQueue *trackQueue;

@end

void sendSpotURLRequestWithBlock(ListBlock block);

//发送显示成功效果链接
void sendSpotEffURLRequestWithBlock(long effType, UMNDataModel *spotDataStructure, FinishBlock block);
