//
//  NetworkToolkit.m
//  SDK
//
//  Created by ENZO YANG on 13-2-25.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import "UMNNetworkUtil.h"
#import "Macro.h"
#import "UMNBackgroundQueue.h"
#import "UMNMachineUtil.h"
#import "UMURLPathUtil.h"


//link存放url，判断url是否是链接去appStore的连接
BOOL isAppStoreLink(NSString *link){
    // if the lins has prefix "itms-apps://"
    if ([link hasPrefix:@"itms-apps://"] || [link hasPrefix:@"itms://"]) {
        return YES;
    }
    
	// if the lins has "itunes.apple.com/"
	static NSString *kAppStoreIdentifier = @"itunes.apple.com/";
	if (CFStringFind((CFStringRef)link, (CFStringRef)kAppStoreIdentifier, kCFCompareCaseInsensitive).length > 0) {
		return YES;
	}
    return NO;
}

NSNumber *extractAppStoreID(NSString *link) {
    static NSRegularExpression *regex = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        regex = [NSRegularExpression regularExpressionWithPattern:@"/id(\\d*)" options:NSRegularExpressionCaseInsensitive error:nil];
    });
    NSTextCheckingResult *match = [regex firstMatchInString:link options:0 range:NSMakeRange(0, [link length])];
    
    NSNumber *result = nil;
    if (match) {
        NSRange firstRange = [match rangeAtIndex:1];
        NSString *resultString = [link substringWithRange:firstRange];
        result = [NSNumber numberWithInt:[resultString intValue]];
    }
    return result;
}

static NSString *const kURLQuerySeparatorCharacter  = @"&";
static NSString *const kURLQueryEqualCharacter      = @"=";


//解析url请求
//用到了NSCharcterSet集合跟NSScanner扫描器来解析NSString类型的query
//参数query是类似于log=JS%3A%20Init%201307091618.这样的字符串
//返回key value对比如这里key是log value是JS%3A%20Init%201307091618
NSDictionary *parseURLQuery(NSString *query) {
    if (!YM_STRING_IS_NOT_VOID(query)) {
        return nil;
    }
    NSCharacterSet *firstSet = [NSCharacterSet characterSetWithCharactersInString:[NSString stringWithFormat:@"%@%@", kURLQueryEqualCharacter, kURLQuerySeparatorCharacter]];
    NSCharacterSet *secondSet = [NSCharacterSet characterSetWithCharactersInString:kURLQuerySeparatorCharacter];
    NSScanner *scanner = [NSScanner scannerWithString:query];
    if (!scanner) return nil;
    
    NSString *key = nil;
    NSString *value = nil;
    NSInteger len = [query length];
    NSMutableDictionary *info = [NSMutableDictionary dictionary];
    while (![scanner isAtEnd]) {
        // reset
        key = nil;
        value = nil;
        
        //Scans the string until a character from a given character set is encountered, accumulating characters into a string that’s returned by reference.
        [scanner scanUpToCharactersFromSet:firstSet intoString:&key];
        // skip "="
        if ([scanner scanLocation] + 1 >= len) {
            if (YM_STRING_IS_NOT_VOID(key)) [info setValue:YM_ASSIGN_STRING_SAFELY(nil) forKey:key];
            break;
        }
        
        NSString *next = [[scanner string] substringWithRange:NSMakeRange([scanner scanLocation], [kURLQuerySeparatorCharacter length])];
        [scanner setScanLocation:[scanner scanLocation] + 1];
        if ([next isEqualToString:kURLQuerySeparatorCharacter]) {
            if (YM_STRING_IS_NOT_VOID(key)) [info setValue:YM_ASSIGN_STRING_SAFELY(nil) forKey:key];
            continue;
        }
        
        // scan value
        [scanner scanUpToCharactersFromSet:secondSet intoString:&value];
        if (YM_STRING_IS_NOT_VOID(key)) {
            value = YM_ASSIGN_STRING_SAFELY(value);
            //编码这里与stringByAddingPercentEscapesUsingEncoding对应
            [info setValue:[value stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding] forKey:key];
        }
        // skip "&"
        if ([scanner scanLocation] + 1 >= len) break;
        [scanner setScanLocation:[scanner scanLocation] + 1];
    }
    
    if ([info count] > 0) return [NSDictionary dictionaryWithDictionary:info];
    return nil;
}

NSString *encodeURLStringSavely(NSString *url) {
    url = [url stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    url = [url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    return url;
}

NSString *generateQueryString(NSDictionary *query) {
    if (!query) return @"";
    
    NSMutableString *result = [NSMutableString new];
    
    [query enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
        NSString *keyString = [NSString stringWithFormat:@"%@",key];
        NSString *valueString = [NSString stringWithFormat:@"%@",obj];
        valueString = encodeURLStringSavely(valueString);
        
        [result appendFormat:@"&%@=%@", keyString, valueString];
    }];
    
    
    if ([result length] == 0) {
        return @"";
    } else {
        return [result substringFromIndex:1];
    }
}



