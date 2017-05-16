//
//  CarouselSubview.m
//  YHImageCarousel
//
//  Created by zyh on 2016/12/8.
//  Copyright © 2016年 zyh. All rights reserved.
//

#import "CarouselSubview.h"

@implementation CarouselSubview

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.containerView];
    }
    return self;
}

- (UIView *)containerView {
    if (_containerView == nil) {
        _containerView = [[UIView alloc] initWithFrame:CGRectMake(2, 0, self.bounds.size.width - 4, self.bounds.size.height)];
        _containerView.layer.cornerRadius = 5;
        _containerView.layer.masksToBounds = YES;
    }
    return _containerView;
}

@end

@implementation CarouselCell


@end


@implementation CarouselCellImageView

- (instancetype)initWithFrame:(CGRect)frame {
    
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.imageView];
    }
    return self;
}

- (UIImageView *)imageView {
    if (_imageView == nil) {
        _imageView = [[UIImageView alloc] initWithFrame:self.bounds];
    }
    return _imageView;
}

@end

@implementation CarouselCellAdView

- (instancetype)initWithFrame:(CGRect)frame {
    
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.imageView];
        [self initAdTip];
        
        self.adNameLb = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
        self.adNameLb.text = @"信息流游戏名称";
        [self addSubview:self.adNameLb];
        
        self.adNameLb.translatesAutoresizingMaskIntoConstraints = NO;
        [self addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|-10-[_adNameLb(height)]"
                                                                                 options:0
                                                                                 metrics:@{@"height":@(30)}
                                                                                   views:NSDictionaryOfVariableBindings(_adNameLb)]];
        [self addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|-10-[_adNameLb]-10-|"
                                                                                 options:0
                                                                                 metrics:@{@"width":@(0)}
                                                                                   views:NSDictionaryOfVariableBindings(_adNameLb)]];
    }
    return self;
}

-(void)initAdTip{
    self.adTip = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
    self.adTip.text = @"广告";
    self.adTip.font = [UIFont systemFontOfSize:12];
    self.adTip.textColor = [UIColor whiteColor];
    self.adTip.textAlignment = NSTextAlignmentCenter;
    self.adTip.backgroundColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.4];
    self.adTip.layer.cornerRadius = 5;
    self.adTip.layer.masksToBounds = YES;
    self.adTip.layer.borderWidth = 0.8;
    self.adTip.layer.borderColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:0.8].CGColor;
    [self addSubview:self.adTip];
    
    self.adTip.translatesAutoresizingMaskIntoConstraints = NO;
    [self addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:[_adTip(height)]-10-|"
                                                                             options:0
                                                                             metrics:@{@"height":@(20)}
                                                                               views:NSDictionaryOfVariableBindings(_adTip)]];
    [self addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:[_adTip(width)]-10-|"
                                                                             options:0
                                                                             metrics:@{@"width":@(40)}
                                                                               views:NSDictionaryOfVariableBindings(_adTip)]];
}

- (UIImageView *)imageView {
    if (_imageView == nil) {
        _imageView = [[UIImageView alloc] initWithFrame:self.bounds];
    }
    return _imageView;
}

@end


