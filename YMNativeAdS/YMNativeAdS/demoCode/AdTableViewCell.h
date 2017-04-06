//
//  AdTableViewCell.h
//  YMNativeAdS
//
//  Created by linxiaolong on 2017/3/9.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import <UIKit/UIKit.h>
@class UMNDataModel;
@interface AdTableViewCell : UITableViewCell
-(void)setCellInfo:(UMNDataModel *)addata;
@end
