//
//  YMAdRequest.m
//  SDK
//
//  Created by Layne on 11-11-18.
//  Copyright (c) 2012年  Mobile Co. Ltd. All rights reserved.
//

#import "UMRequest.h"
#import <UIKit/UIKit.h>
#import "Macro.h"
#import "UMNMachineUtil.h"
#import "UMURLPathUtil.h"
#import "UMNSDKConfig.h"
//static NSString *const kRequestMethodGET   = @"GET";
static NSString*const JY_GET(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'G'),(XOR_KEY ^ 'E'),(XOR_KEY ^ 'T'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"GET";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}
//static NSString *const kRequestMethodPOST  = @"POST";
static NSString *const JY_POST(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'P'),(XOR_KEY ^ 'O'),(XOR_KEY ^ 'S'),(XOR_KEY ^ 'T'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"POST";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}
#define kRequestTimeOut 60.0f


@interface UMRequest ()
- (void)_requestAsynchronously;
- (void)_requestFinished:(BOOL)success;
@end


@implementation UMRequest
#pragma mark -
#pragma mark private Methods

- (void)_requestAsynchronously {
    @try {
        // reset
        // Init the data to handle the response coming back from the server.
        if (!_REdata) {
            _REdata = [[NSMutableData alloc] init];
        }
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wnonnull"
        // clear content
        [_REdata setData:nil];
        
        YM_RELEASE_SAFELY(_REresponse);
        YM_RELEASE_SAFELY(_REerror);
        _hasRequested = NO;
        self.statusCode = 200;
        self.errorCode  = 0;
        self.requestTimestamp       = nil;
        self.firstResponseTimestamp = nil;
        self.finishTimestamp        = nil;
        
		NSMutableURLRequest *urlRequest = [NSMutableURLRequest requestWithURL:_reqURL
                                                                  cachePolicy: NSURLRequestReloadIgnoringCacheData
                                                              timeoutInterval:_timeout];
        
        NSString *str = [NSString stringWithFormat:@"Bearer %@",[UMNSDKConfig sharedInstanceSDKConfig].appid];
        //用作负载均衡
        [urlRequest addValue:GetCID() forHTTPHeaderField:getLoadBalancing()];
        [urlRequest addValue:str forHTTPHeaderField:@"Authorization"];
		[urlRequest setHTTPMethod:_reqMethod];
        if ([_reqMethod isEqualToString:JY_POST()] && _reqBody) {
            [urlRequest setHTTPBody:_reqBody];
        }
        
//        YM_RELEASE_SAFELY(_connection);
        
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-value"
        

        dispatch_async(dispatch_get_main_queue(), ^{
            [[NSURLConnection alloc] initWithRequest:urlRequest delegate:self];
            self.requestTimestamp = [NSDate date];
        });
	}
	@catch (NSException * exception) {
        OGERROR(@"%@", exception);
	}
    #pragma clang diagnostic pop
}

- (void)_requestFinished:(BOOL)success {
    // call back if use block
    if (_completion_block) {
        _completion_block(success, self);
    }
}

#pragma mark -
#pragma mark public Methods

+ (UMRequest *)request {
    return [[UMRequest alloc] init];
}

+ (UMRequest *)requestWithURL:(NSURL *)url {
    UMRequest *request = [UMRequest request];
    request.reqURL = url;
    return request;
}

- (id)init {
    self = [super init];
	if (self) {
        _reqTag = 0;
        _timeout = kRequestTimeOut;
        _reqMethod = [[NSString alloc] initWithString:JY_GET()];
    }
	
	return self;
}

- (void)requestAsynchronouslyWithCompletionUsingBlock:(void (^)(BOOL finished, UMRequest *request))completion {
    YM_RELEASE_SAFELY(_completion_block);
    _completion_block = [completion copy];
    [self _requestAsynchronously];
}

- (BOOL)hasError {
	return _REerror ? YES : NO;
}

- (BOOL)shouldReport {
    if (self.statusCode > 399 || self.errorCode != 0) return YES;
    return NO;
}

- (void)dealloc {
//    [_reqMethod release];
//    [_reqURL release];
//    [_REdata release];
//    [_REresponse release];
//    [_REerror release];
//    [_completion_block release];
//    [_requestTimestamp release];
//    [_finishTimestamp release];
//    [_firstResponseTimestamp release];
//	[super dealloc];
}

#pragma mark - delegate methods for asynchronous requests

- (NSCachedURLResponse*)connection:(NSURLConnection*)connection willCacheResponse:(NSCachedURLResponse*)cachedResponse {
	return nil;
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response {
    // set network activity visible
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
    
    YM_RELEASE_SAFELY(_REresponse);
    _REresponse = response;
    
    NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
    self.statusCode = [httpResponse statusCode];
    
    self.firstResponseTimestamp = [NSDate date];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [_REdata appendData:data];
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    // set network activity no visible
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    
	_hasRequested = YES;
    _REerror = error;
    self.errorCode = [error code];
    NSLog(@"%@",error);
    self.finishTimestamp = [NSDate date];
    if (!self.firstResponseTimestamp) {
        self.firstResponseTimestamp = self.finishTimestamp;
    }
    
    // finish
    [self _requestFinished:NO];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
	// set network activity no visible
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    
	_hasRequested = YES;
    _REerror = nil;
    self.finishTimestamp = [NSDate date];
    if (!self.firstResponseTimestamp) {
        self.firstResponseTimestamp = self.finishTimestamp;
    }
    
    // finish
    [self _requestFinished:YES];
}
#pragma clang diagnostic pop
@end
