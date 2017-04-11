//
//  AdTableViewCell.m
//  YMNativeAdS
//
//  Created by linxiaolong on 2017/3/9.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "AdTableViewCell.h"
#import "UMNDataModel.h"
@interface AdTableViewCell()
@property (nonatomic,strong)UIImageView *adImgV; //
@property (nonatomic,strong)UILabel *adNameLb; //
@property (nonatomic,strong)UILabel *adSloganLb; //

@property (nonatomic,strong)UILabel *adTip;
@end


@implementation AdTableViewCell


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        self.adNameLb = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
//        self.adNameLb.text = @"信息流游戏名称";
        self.adNameLb.font = [UIFont systemFontOfSize:12];
        self.adNameLb.textColor = [UIColor colorWithRed:1/255.0 green:1/255.0 blue:1/255.0 alpha:0.54];;
        [self.contentView addSubview:self.adNameLb];
        
        self.adSloganLb = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
//        self.adSloganLb.text = @"广告语";
        self.adSloganLb.numberOfLines = 0;
        self.adSloganLb.backgroundColor = [UIColor redColor];
        self.adSloganLb.font = [UIFont systemFontOfSize:15];
        [self.contentView addSubview:self.adSloganLb];
        
        self.adImgV = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, self.contentView.frame.size.width, self.contentView.frame.size.height)];
        self.adImgV.backgroundColor = [UIColor orangeColor];
        [self.contentView addSubview:self.adImgV];
//        self.adImgV.layer.cornerRadius = 3;
//        self.adImgV.layer.masksToBounds = YES;
        
        

        self.adTip = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
        self.adTip.text = @"广告";
        self.adTip.font = [UIFont systemFontOfSize:10];
        self.adTip.textColor = [UIColor colorWithRed:43/255.0 green:144/255.0 blue:215/255.0 alpha:0.8];
        self.adTip.textAlignment = NSTextAlignmentCenter;
        self.adTip.backgroundColor = [UIColor clearColor];
        self.adTip.layer.cornerRadius = 3;
        self.adTip.layer.masksToBounds = YES;
        self.adTip.layer.borderWidth = 0.8;
        self.adTip.layer.borderColor = [UIColor colorWithRed:43/255.0 green:144/255.0 blue:215/255.0 alpha:0.8].CGColor;
        [self.contentView addSubview:self.adTip];
        
        
        self.adNameLb.translatesAutoresizingMaskIntoConstraints = NO;
        self.adSloganLb.translatesAutoresizingMaskIntoConstraints = NO;
        
        [self.contentView addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-15-[_adSloganLb]-15-|"
                                                                                 options:0
                                                                                 metrics:@{@"width":@(0)}
                                                                                   views:NSDictionaryOfVariableBindings(_adSloganLb)]];
        
        
        self.adImgV.translatesAutoresizingMaskIntoConstraints = NO;
        [self.contentView addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-8-[_adSloganLb(38)]-4-[_adImgV(imgvheight)]-8-[_adNameLb(12)]-14-|"
                                                                                 options:0
                                                                                 metrics:@{@"imgvheight":@(188)}
                                                                                   views:NSDictionaryOfVariableBindings(_adImgV,_adNameLb,_adSloganLb)]];
        
        [self.contentView addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-15-[_adImgV]-15-|"
                                                                                 options:0
                                                                                 metrics:@{@"width":@(0)}
                                                                                   views:NSDictionaryOfVariableBindings(_adImgV)]];
        
        self.adTip.translatesAutoresizingMaskIntoConstraints = NO;
        [self.contentView addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:[_adTip(height)]-13-|"
                                                                                 options:0
                                                                                 metrics:@{@"height":@(14)}
                                                                                   views:NSDictionaryOfVariableBindings(_adTip)]];
        [self.contentView addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-15-[_adNameLb]-15-[_adTip(width)]-15-|"
                                                                                 options:0
                                                                                 metrics:@{@"width":@(27)}
                                                                                   views:NSDictionaryOfVariableBindings(_adTip,_adNameLb)]];
        
    }
    return self;
}

-(void)setCellInfo:(UMNDataModel *)addata{
    
    self.adNameLb.text = addata.name;
    self.adSloganLb.text = addata.slogan;
    
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
