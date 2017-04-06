//
//  AdBannerTableViewCell.m
//  YMNativeAdS
//
//  Created by linxiaolong on 2017/3/22.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "AdBannerTableViewCell.h"

#import "UMNDataModel.h"
@interface AdBannerTableViewCell()
@property (nonatomic,strong)UIImageView *adImgV; //
@property (nonatomic,strong)UILabel *adTip;
@end


@implementation AdBannerTableViewCell


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        
        
        self.adImgV = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, self.contentView.frame.size.width, self.contentView.frame.size.height)];
        self.adImgV.backgroundColor = [UIColor orangeColor];
        [self.contentView addSubview:self.adImgV];
        self.adImgV.layer.cornerRadius = 3;
        self.adImgV.layer.masksToBounds = YES;
        
        
        
        self.adTip = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
        self.adTip.text = @"广告";
        self.adTip.font = [UIFont systemFontOfSize:6];
        self.adTip.textColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1];
        self.adTip.textAlignment = NSTextAlignmentCenter;
        self.adTip.backgroundColor = [UIColor blackColor];
        self.adTip.alpha = 0.5;
        [self.adImgV addSubview:self.adTip];
        
        
        [self.contentView addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-15-[_adImgV]-15-|"
                                                                                 options:0
                                                                                 metrics:@{@"width":@(0)}
                                                                                   views:NSDictionaryOfVariableBindings(_adImgV)]];
        
        
        self.adImgV.translatesAutoresizingMaskIntoConstraints = NO;
        [self.contentView addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-15-[_adImgV]-15-|"
                                                                                 options:0
                                                                                 metrics:@{@"imgvheight":@(83)}
                                                                                   views:NSDictionaryOfVariableBindings(_adImgV)]];
        
        
        self.adTip.translatesAutoresizingMaskIntoConstraints = NO;
        [self.adImgV addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:[_adTip(height)]-0-|"
                                                                                 options:0
                                                                                 metrics:@{@"height":@(10)}
                                                                                   views:NSDictionaryOfVariableBindings(_adTip)]];
        [self.adImgV addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:[_adTip(width)]-0-|"
                                                                                 options:0
                                                                                 metrics:@{@"width":@(20)}
                                                                                   views:NSDictionaryOfVariableBindings(_adTip)]];
        
    }
    return self;
}

-(void)setCellInfo:(UMNDataModel *)addata{
     
    NSURL *iconURL = [NSURL URLWithString:[addata getPicURL4PicArr:0]];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
        NSData *imageData = [NSData dataWithContentsOfURL:iconURL];
        dispatch_async(dispatch_get_main_queue(), ^{
            self.adImgV.image = [UIImage imageWithData:imageData];
        });
    });
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

@end
