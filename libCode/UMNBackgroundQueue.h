//
//  UMNBackgroundQueue.h
//  SDK
//
//  Created by ENZO YANG on 13-2-25.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//
//  名字：顺序操作执行队列
//  作用：按顺序另开线程执行操作，并且可以让操作在APP resignActive 后继续执行10分钟。

#import <UIKit/UIKit.h>

@interface UMNBackgroundQueue : NSOperationQueue {
    UIBackgroundTaskIdentifier backgroundTask;
}

@property (nonatomic, assign) BOOL shouldContinueWhenAppEnterBackground;

- (void)SBSaddOperation:(NSOperation *)op;

- (void)addOperationWithBlock:(void (^)(void))block NS_AVAILABLE(10_6, 4_0);

@end
