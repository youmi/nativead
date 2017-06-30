//
//  StoreKitUtil.m
//  SDK
//
//  Created by YANG ENZO on 12-11-20.
//  Copyright (c) 2012年  Mobile Co. Ltd. All rights reserved.
//

#import "UMStoreKitUtil.h"
#import <StoreKit/StoreKit.h>
#import "UMNMachineUtil.h"
#import "UMNSDKConfig.h"
#import "Macro.h"

@interface JoyStoreViewController : SKStoreProductViewController
@end
@implementation JoyStoreViewController
- (BOOL)shouldAutorotate {
    return YES;
}
#if __IPHONE_OS_VERSION_MAX_ALLOWED < 90000
- (NSUInteger)supportedInterfaceOrientations {
#else
- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
#endif
    //-(unsigned int)supportedInterfaceOrientations{
    return UIInterfaceOrientationMaskAll;
}
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation {
    return YES;
}
@end

//static NSString * const kSKStoreProductParameterITunesItemIdentifier = @"id";
static NSString *ZYin_id() {
    return @"id";
}

@class SKStoreProductViewController;

@interface UMStoreKitUtil () < SKStoreProductViewControllerDelegate >

@property (nonatomic, retain) UIViewController *middleController;
@property (nonatomic, retain) UIViewController *storeProductController;

@end

@implementation UMStoreKitUtil

+ (UMStoreKitUtil *)defaultStoreKitHelper {
    static UMStoreKitUtil *helper = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        helper = [UMStoreKitUtil new];
    });
    return helper;
}

- (void)dealloc {
    self.middleController = nil;
    self.storeProductController = nil;
}

- (BOOL)showAppInAppStore:(NSNumber *)appid {
    //    if (![UMNSDKConfig sharedInstanceSDKConfig]->showInAppStore()) return NO;

    UIWindow *window = [[UIApplication sharedApplication].delegate window]; //applicationFrontNormalWindow();
    @synchronized(self) {
        if (!window)
            return NO;

        if (self.storeProductController != nil) {
            //            [self.storeProductController dismissModalViewControllerAnimated:NO];
            [self.storeProductController dismissViewControllerAnimated:NO
                                                            completion:^{

                                                            }];
            self.storeProductController = nil;
        }

        if (self.middleController != nil) {
            [self.middleController.view removeFromSuperview];
            self.middleController = nil;
        }

        BOOL statusBarHidden = [UIApplication sharedApplication].statusBarHidden;

        self.middleController = [UIViewController new];

        JoyStoreViewController *skvc = [JoyStoreViewController new];
        skvc.delegate = self;
// 如果window的level比statusbar的高，则全屏否则不全屏
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"

        //      #pragma clang diagnostic pop
        if (window.windowLevel >= UIWindowLevelStatusBar || statusBarHidden == YES) {
            skvc.wantsFullScreenLayout = YES;
        }
#pragma clang diagnostic pop
        NSDictionary *dict = [NSDictionary dictionaryWithObject:[NSString stringWithFormat:@"%@", appid] forKey:ZYin_id()];
        [skvc loadProductWithParameters:dict completionBlock:nil];

        self.storeProductController = skvc;

        [window addSubview:self.middleController.view];
        [self.middleController presentViewController:skvc animated:YES completion:nil];
    }

    return YES;
}

- (void)productViewControllerDidFinish:(SKStoreProductViewController *)viewController {
    [viewController dismissViewControllerAnimated:YES
                                       completion:^(void) {
                                           [self.middleController.view removeFromSuperview];
                                           self.middleController = nil;
                                           self.storeProductController = nil;
                                       }];
}

@end
