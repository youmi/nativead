//
//  StoreKitUtil.h
//  SDK
//
//  Created by YANG ENZO on 12-11-20.
//  Copyright (c) 2012年  Mobile Co. Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UMStoreKitUtil : NSObject {
}

+ (UMStoreKitUtil *)defaultStoreKitHelper;

- (BOOL)showAppInAppStore:(NSNumber *)appid;

@end
