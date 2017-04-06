//
//  NativeViewController.m
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/8.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "NativeViewController.h"
#import "YMNativeAd.h"

@interface NativeViewController ()<YMNativeAdDelegate>{
    YMNativeAd *_nativeAd;//广告实例
    NSArray *_adArray;//广告数组（UMNSataModel实例集合）
    UMNDataModel *_currentAd;//当前展示的ad
}

@end

@implementation NativeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationItem.title = @"原生广告";
    UIBarButtonItem *rightBtn = [[UIBarButtonItem alloc]
                                 initWithTitle:@"返回"
                                 style:UIBarButtonItemStyleDone
                                 target:self
                                 action:@selector(rightBarItemClick)];
    
    self.navigationItem.leftBarButtonItem = rightBtn;
  
    self.view.backgroundColor = [UIColor whiteColor];
//    [self showAdView];
    [self loadAd];
    
    
    // Do any additional setup after loading the view.
}

-(void)rightBarItemClick{
    [self dismissViewControllerAnimated:YES completion:^{
        
    }];
}

//加载原生广告
-(void)loadAd{

    if (!_nativeAd) {
        _nativeAd = [[YMNativeAd alloc]initWithAppId:@"320a6cd3b8d2c4e0" slotId:@"7746"];
        [_nativeAd setContTitle:@"新闻页面" contKw:@"国内新闻，头条"];
        _nativeAd.delegate = self;
    }
    [_nativeAd loadAd];//拉取广告
}

//展示原生广告
-(void)showAdView{
    if (_adArray) {
        UMNDataModel *dataModel = [_adArray objectAtIndex:0];
        _currentAd = dataModel;
        
        UIView *adView = [[UIView alloc]initWithFrame:CGRectMake((self.view.frame.size.width - 200)/2, (self.view.frame.size.height - 300)/2, 200, 300)];
        adView.layer.borderWidth = 3;
        adView.layer.cornerRadius = 5;
        adView.layer.masksToBounds = YES;
        adView.backgroundColor = [UIColor greenColor];
        [self.view addSubview:adView];
        
        UILabel *adnameLb = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, adView.frame.size.width, 30)];
        adnameLb.text = dataModel.name;
        adnameLb.textAlignment = NSTextAlignmentCenter;
        [adView addSubview:adnameLb];
        
        UILabel *adsloganLb = [[UILabel alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(adnameLb.frame), adView.frame.size.width, 30)];
        adsloganLb.text = dataModel.slogan;
        adsloganLb.textAlignment = NSTextAlignmentCenter;
        [adView addSubview:adsloganLb];
        
        UILabel *adsubsloganLb = [[UILabel alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(adsloganLb.frame), adView.frame.size.width, 30)];
        adsubsloganLb.text = dataModel.subslogan;
        adsubsloganLb.textAlignment = NSTextAlignmentCenter;
        [adView addSubview:adsubsloganLb];
        
        UIImageView *imgV = [[UIImageView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(adsubsloganLb.frame)+2, adView.frame.size.width, adView.frame.size.height - CGRectGetMaxY(adsubsloganLb.frame)-2)];
        imgV.backgroundColor = [UIColor orangeColor];
        [adView addSubview:imgV];
        
        
        NSURL *iconURL = [NSURL URLWithString:[_currentAd getPicURL4PicArr:0]];
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            NSData *imageData = [NSData dataWithContentsOfURL:iconURL];
            dispatch_async(dispatch_get_main_queue(), ^{
                imgV.image = [UIImage imageWithData:imageData];
            });
        });

        
        
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
    }
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

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
