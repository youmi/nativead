//
//  YMAdRequest.h
//  SDK
//
//  Created by Layne on 11-11-18.
//  Copyright (c) 2012年  Mobile Co. Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

@class UMRequest;

@interface UMRequest : NSObject {
    void (^_completion_block)(BOOL finished, UMRequest *request);
}

@property                       int             reqTag;
@property(nonatomic)            NSTimeInterval  timeout;
@property(nonatomic, retain)    NSString        *reqMethod;
@property(nonatomic, retain)    NSData          *reqBody; // use when in POST method
@property(nonatomic, retain)    NSURL           *reqURL;

@property(nonatomic, retain)    NSMutableData* REdata;
@property(nonatomic, retain)    NSURLResponse   *REresponse;
@property(nonatomic, retain)    NSError         *REerror;

@property(nonatomic)            BOOL            hasRequested;

@property(nonatomic, retain)    NSDate          *requestTimestamp;
@property(nonatomic, retain)    NSDate          *firstResponseTimestamp;
@property(nonatomic, retain)    NSDate          *finishTimestamp;
@property(nonatomic, assign)    NSInteger       statusCode;
@property(nonatomic, assign)    NSInteger       errorCode;

+ (UMRequest *)request;
+ (UMRequest *)requestWithURL:(NSURL *)url;

// asynchronous
- (void)requestAsynchronouslyWithCompletionUsingBlock:(void (^)(BOOL finished, UMRequest *request))completion;

- (BOOL)hasError;

- (BOOL)shouldReport;

@end


