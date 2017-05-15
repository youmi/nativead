//
//  AdTableViewController.h
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/8.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YMNativeAd.h" //原生广告头文件

@interface AdTableViewController : UIViewController<YMNativeAdDelegate,UITableViewDelegate,UITableViewDataSource>//代理

-(void)loadAd:(NSString *)slotId;
@end
