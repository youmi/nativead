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


-(void)startRedirectRequest:(NSString *)urlStr{
    
    urlStr = [urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSURL *url = [NSURL URLWithString:urlStr];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    NSString *str = [NSString stringWithFormat:@"Bearer %@", [UMNSDKConfig sharedInstanceSDKConfig].appid];
    [request addValue:str forHTTPHeaderField:@"Authorization"];
    
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
