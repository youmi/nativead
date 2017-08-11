//
//  AppDelegate.m
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/6.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "AppDelegate.h"
#import "YMNativeAd.h"
@interface AppDelegate ()<YMNativeAdDelegate>{
    YMNativeAd *_nativeAd;//广告实例
    NSArray *_adArray;//广告数组（UMNSataModel实例集合）
    UMNDataModel *_currentAd;//当前展示的ad
    
    UIView *adView;//广告View
}

@end

@implementation AppDelegate

//加载原生广告
-(void)loadAd{
    
    if (!_nativeAd) {
//        _nativeAd = [[YMNativeAd alloc]initWithAppId:@"1d5f537002646414" slotId:@"8009"];
        _nativeAd = [[YMNativeAd alloc]initWithAppId:@"320a6cd3b8d2c4e0" slotId:@"7748"];
        [_nativeAd setContTitle:@"新闻页面" contKw:@"国内新闻，头条"];
        _nativeAd.delegate = self;
    }
    [_nativeAd loadAd];//拉取广告，3为数量,
}

//展示原生广告
-(void)showAdView{
    if (_adArray) {
        UMNDataModel *dataModel = [_adArray objectAtIndex:0];
        _currentAd = dataModel;
        
        adView = [[UIView alloc]initWithFrame:CGRectMake(0,0,[[UIScreen mainScreen] bounds].size.width,[[UIScreen mainScreen] bounds].size.height)];
        adView.layer.borderWidth = 3;
        adView.layer.cornerRadius = 5;
        adView.layer.masksToBounds = YES;
        adView.backgroundColor = [UIColor greenColor];
        [self.window.rootViewController.view addSubview:adView];
       
        UIImageView *imgV = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, adView.frame.size.width, adView.frame.size.height - 100)];
        imgV.backgroundColor = [UIColor orangeColor];
        [adView addSubview:imgV];
        
        NSURL *iconURL = [NSURL URLWithString:[_currentAd getPicURL4PicArr:0]];
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            NSData *imageData = [NSData dataWithContentsOfURL:iconURL];
            dispatch_async(dispatch_get_main_queue(), ^{
                imgV.image = [UIImage imageWithData:imageData];
            });
        });
        
        
        
        UILabel *adTip = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
        adTip.text = @"广告";
        adTip.font = [UIFont systemFontOfSize:10];
        adTip.textColor = [UIColor whiteColor];
        adTip.textAlignment = NSTextAlignmentCenter;
        adTip.backgroundColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.3];
        adTip.layer.cornerRadius = 7.5;
        adTip.layer.masksToBounds = YES;
//        adTip.layer.borderWidth = 0.8;
//        adTip.layer.borderColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:0.8].CGColor;
        [imgV addSubview:adTip];
        
        adTip.translatesAutoresizingMaskIntoConstraints = NO;
        [imgV addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:[adTip(height)]-10-|"
                                                                                 options:0
                                                                                 metrics:@{@"height":@(15)}
                                                                                   views:NSDictionaryOfVariableBindings(adTip)]];
        
        [imgV addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:[adTip(width)]-10-|"
                                                                                 options:0
                                                                                 metrics:@{@"width":@(30)}
                                                                                   views:NSDictionaryOfVariableBindings(adTip)]];
        
        
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(viewTapped:)];
        [adView addGestureRecognizer:tap];
        
        /*展示广告 调用展示接口*/
        [_nativeAd showAd:_currentAd callBackBlock:^(NSError *error) {
            if (error) {
                NSLog(@"%@",error);
            }else{
                NSLog(@"展示有效");
            }
        }];
        
        [self performSelector:@selector(closeAd) withObject:nil afterDelay:4];
    }
}

-(void)closeAd{
    
    [adView removeFromSuperview];
    adView = nil;
}

- (void)viewTapped:(UITapGestureRecognizer *)gr {
    /*点击发生，调用点击接口*/
    [_nativeAd clickAd:_currentAd callBackBlock:^(NSError *error) {
        if (error) {
            NSLog(@"%@",error);
        }else{
            NSLog(@"点击有效");
        }
    }];
    
    /*跳转到广告对应的AppStore页面*/
    [_nativeAd clickAdOpenAppStoreVC:_currentAd];
}

#pragma mark -YMNvativeDelegate
//拉取成功
-(void)ymNativeAdSuccessToLoad:(NSArray *)nativeAdArray{
    NSLog(@"返回成功%@",nativeAdArray);
    _adArray = nativeAdArray;
    [self showAdView];
}
//拉取失败
-(void)ymNativeAdFailedToLoad:(NSError *)error{
    NSLog(@"%@",error);
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    [self loadAd];
//    [self showAdView];
    return YES;
}


- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}


- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}


@end
