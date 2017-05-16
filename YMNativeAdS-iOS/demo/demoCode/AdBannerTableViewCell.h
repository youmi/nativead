//
//  AdBannerTableViewCell.h
//  YMNativeAdS
//
//  Created by linxiaolong on 2017/3/22.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import <UIKit/UIKit.h>
 
@class UMNDataModel;
@interface AdBannerTableViewCell : UITableViewCell
-(void)setCellInfo:(UMNDataModel *)addata;
@end
