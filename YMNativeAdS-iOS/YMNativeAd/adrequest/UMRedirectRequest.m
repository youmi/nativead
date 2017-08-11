//
//  UMRedirectRequest.m
//  YMNativeAdS
//
//  Created by game just on 2017/8/8.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "UMRedirectRequest.h"
#import "UMNSDKConfig.h"
#import "Macro.h"
@implementation UMRedirectRequest


#define USERAGENT @"Mozilla/5.0 (iPhone; CPU iPhone OS 5_1_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B206 Safari/7534.48.3"

-(void)startRedirectRequest:(NSString *)urlStr{
    
    urlStr = [urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSURL *url = [NSURL URLWithString:urlStr];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    NSString *str = [NSString stringWithFormat:@"Bearer %@", [UMNSDKConfig sharedInstanceSDKConfig].appid];
    [request addValue:str forHTTPHeaderField:@"Authorization"];
    [request setValue:USERAGENT forHTTPHeaderField:@"User-Agent"];
    
    NSURLConnection *connection = [[NSURLConnection alloc] initWithRequest:request delegate:self];
    
    if (!connection) {
        OGERROR(@"NSURLConnection 空");
    }
}

- (NSURLRequest *)connection:(NSURLConnection *)connection willSendRequest:(NSURLRequest *)request redirectResponse:(NSURLResponse *)response {
    
    [NSString stringWithContentsOfURL:[request URL] encoding:NSUTF8StringEncoding error:nil];
//    NSLog(@"will send request\n%@", [request URL]);
//    NSLog(@"redirect response\n%@", [response URL]);
    
    return request;
}
@end
