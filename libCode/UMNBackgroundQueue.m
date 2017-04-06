//
//  UMNBackgroundQueue.m
//  SDK
//
//  Created by ENZO YANG on 13-2-25.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import "UMNBackgroundQueue.h"
#import "UMNMachineUtil.h"

@interface UMNBackgroundQueue()

- (void)_appDidEnterBackground:(NSNotification *)note;

@end

@implementation UMNBackgroundQueue

@synthesize shouldContinueWhenAppEnterBackground = _shouldContinueWhenAppEnterBackground;

- (id)init {
    self = [super init];
    if (self) {
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(_appDidEnterBackground:)
                                                     name:UIApplicationDidEnterBackgroundNotification
                                                   object:nil];
        self.shouldContinueWhenAppEnterBackground = YES;
    }
    return self;
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
 
}

/*
  检测到UIApplicationDidEnterBackgroundNotification消息，继续执行后台任务。
 backgroundTask是UIBackgroundTaskIdentifier类型，beginBackgroundTaskWithExpirationHandler应该跟endBackgroundTask对应出现，在
 beginBackgroundTaskWithExpirationHandler的block在后台运行的线程就要挂起的时候调用。
 */
- (void)_appDidEnterBackground:(NSNotification *)note {
    if (spotisMultitaskingSupported() && self.shouldContinueWhenAppEnterBackground) {
        backgroundTask = [[UIApplication sharedApplication] beginBackgroundTaskWithExpirationHandler:^{
            [[UIApplication sharedApplication] endBackgroundTask:backgroundTask];
        }];
    }
    [self performSelectorInBackground:@selector(waitUntilAllOperationsAreFinished) withObject:nil];
}

- (void)waitUntilAllOperationsAreFinished {
    [super waitUntilAllOperationsAreFinished];
    [[UIApplication sharedApplication] endBackgroundTask:backgroundTask];
    backgroundTask = UIBackgroundTaskInvalid;
}

/*
  先进先执行
 */
- (void)SBSaddOperation:(NSOperation *)op {
    NSArray *operations = [self operations];
    if ([operations count] > 0) {
        NSOperation *pre = [operations lastObject];
        [op addDependency:pre];
    }
    [super addOperation:op];
}

- (void)addOperationWithBlock:(void (^)(void))block {
    NSBlockOperation *blockOperation = [NSBlockOperation blockOperationWithBlock:block];
    [self addOperation:blockOperation];
}

@end
