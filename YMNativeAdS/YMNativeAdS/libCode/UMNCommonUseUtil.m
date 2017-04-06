//
//  UMNCommonUseUtil.m
//  SDK
//
//  Created by ENZO YANG on 13-2-25.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import "UMNCommonUseUtil.h"
#import "UMNDataHandlerUtil.h"
#import "Macro.h"
#import "UMNSDKConfig.h"

static char kRandomCharTable[62] = {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
    'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
};

NSString *ConverAppIDToHex11(NSString *appID) {
    NSString *newAppID = nil;
    if (YM_STRING_IS_NOT_VOID(appID)) {
        // 根据算法转换为11个字符串长度的APPID
        const char *appid_cstr = [appID UTF8String];
        if (strlen(appid_cstr) == 16) {
            char *appid_sub_cstr = (char *)malloc(sizeof(char) * (11 + 1));
            appid_sub_cstr[11] = '\0';
            appid_sub_cstr[0] = appid_cstr[0];
            SPT_hex_to_64((appid_sub_cstr + 1), appid_cstr, 1, 15);
            // create appid
            newAppID = [NSString stringWithUTF8String:appid_sub_cstr];
            // release
            free(appid_sub_cstr);
        }
    }
    return newAppID;
}

NSString *CreateRandStrinWithLength(NSUInteger length) {
    char rand_chars[length];
    for (int i = 0; i < length; i++) {
        u_int32_t randChar = arc4random() % 62;
        rand_chars[i] = kRandomCharTable[randChar];
    }
    
    NSString *randString = [[NSString alloc] initWithBytes:rand_chars length:length encoding:NSUTF8StringEncoding];
    return randString;
}

#pragma mark - - Date Formatter

NSString *fixStringForDate(NSDate *date) {
	NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateStyle:NSDateFormatterFullStyle];
	[dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSString *fixString = [dateFormatter stringFromDate:date];
    
	return fixString;
}

NSString *fixStringForGMTFromDate(NSDate *date) {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"EEE, dd MMM yyyy HH:mm:ss z"];
    [dateFormatter setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"GMT"]];
    NSString *fixString = [dateFormatter stringFromDate:date];
    
    return fixString;
}

NSInteger secondsForDate(NSDate *date) {
	NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateStyle:NSDateFormatterFullStyle];
	[dateFormatter setDateFormat:@"ss"];
    NSString *secondsStr = [dateFormatter stringFromDate:date];
	NSInteger seconds = [secondsStr intValue];
    
	return seconds;
}
 

NSString *ARGBStringFromColor(UIColor *color) {
    CGFloat a = 0.0, r = 0.0, g = 0.0, b = 0.0;
    [color getRed:&r green:&g blue:&b alpha:&a];
    unsigned int ia = (int)(a * 255);
    unsigned int ir = (int)(r * 255);
    unsigned int ig = (int)(g * 255);
    unsigned int ib = (int)(b * 255);
    
    
    int64_t argb = ia;
    argb = argb << 24;
    argb = argb | (ir << 16) | (ig << 8) | ib;
    
    return [NSString stringWithFormat:@"#%08llx", argb];
}

NSInteger integerFromHexChar(char c){
    c = 0x20 | c; // lowercase
    int res = c - '0';
    if (res < 10) return res;
    return ((c - 'a') + 10);
}


// window 的level
// window 的frame
// window 的hidden
// window 的alpha
// window 的位置
// window 的exclusive touch
// window 的userInteraction
//获取当前应用中适合用的window
UIWindow *applicationFrontNormalWindow() {
    UIWindow *window = nil;
    
    // 规定上层提供window的类为DevConfiguration
    Class<WindowGetterProtocol> WindowGetter = [UMNSDKConfig class];
    if (WindowGetter) {
        window = [WindowGetter window];
    }
    
    if (window) return window;
    
    NSEnumerator *frontToBackWindows = [[[UIApplication sharedApplication]windows]reverseObjectEnumerator];
    
    for (UIWindow *aWindow in frontToBackWindows) {
        if (aWindow.windowLevel == UIWindowLevelNormal) {
            BOOL isIt = YES;
            
            CGSize screenSize = [UIScreen mainScreen].bounds.size;
            CGSize windowSize = aWindow.bounds.size;
            
            if (!(screenSize.width * screenSize.height == windowSize.width * windowSize.height)) {
                isIt = NO;
            }
            
            if ((aWindow.frame.origin.x != 0.0) ||
                (aWindow.frame.origin.y != 0.0)) {
                isIt = NO;
            }
            
            if (aWindow.isHidden) {
                isIt = NO;
            }
            
            if (aWindow.alpha == 0.0f) {
                isIt = NO;
            }
            
            if (aWindow.exclusiveTouch) {
                isIt = NO;
            }
            
            if (!aWindow.userInteractionEnabled) {
                isIt = NO;
            }
            
            if (isIt) {
                window = aWindow;
                break;
            }
        }
    }
    
    return window;
}


//urlencode空格变+号得规范
NSString*urlencode(NSString*unescapedString)
{
    NSString *resultStr = unescapedString;
    
    CFStringRef originalString = (__bridge CFStringRef) unescapedString;
    CFStringRef leaveUnescaped = CFSTR(" ");
    CFStringRef forceEscaped = CFSTR("!*'();:@&=+$,/?%#[]");
    
    CFStringRef escapedStr;
    escapedStr = CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
                                                         originalString,
                                                         leaveUnescaped,
                                                         forceEscaped,
                                                         kCFStringEncodingUTF8);
    
    if( escapedStr )
    {
        NSMutableString *mutableStr = [NSMutableString stringWithString:(__bridge NSString *)escapedStr];
        CFRelease(escapedStr);
        
        // replace spaces with plusses
        [mutableStr replaceOccurrencesOfString:@" "
                                    withString:@"+"
                                       options:0
                                         range:NSMakeRange(0, [mutableStr length])];
        resultStr = mutableStr;
    }
    return resultStr;
}

//
NSString*replaceSpecialChar(NSString*unescapedString)
{
    unescapedString = [unescapedString stringByReplacingOccurrencesOfString:@"+" withString:@"-"];
    unescapedString = [unescapedString stringByReplacingOccurrencesOfString:@"=" withString:@"_"];
    unescapedString = [unescapedString stringByReplacingOccurrencesOfString:@"/" withString:@"."];
    
    return unescapedString;
}

NSString *generateSign(NSDictionary *params,NSString *access_key)
{
    NSArray *keys = [params allKeys];
    NSArray *sortedArray = [keys sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
        return [obj1 compare:obj2 options:NSLiteralSearch];
    }];
    NSMutableString *str = [[NSMutableString alloc]init];
    for (NSString *categoryId in sortedArray) {
        [str appendString:[NSString stringWithFormat:@"%@=%@", categoryId, [params objectForKey:categoryId]]];
    }
    [str appendString:access_key];
    
    return md5HexDigest(str);
}
 
