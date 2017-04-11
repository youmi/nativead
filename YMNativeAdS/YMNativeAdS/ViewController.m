//
//  ViewController.m
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/6.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "ViewController.h"
#import "NativeViewController.h"
#import "AdTableViewController.h"
@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    UIButton *nativeVC = [[UIButton alloc]initWithFrame:CGRectMake(40, 100, 80, 50)];
    nativeVC.backgroundColor = [UIColor blackColor];
    nativeVC.titleLabel.textColor = [UIColor whiteColor];
    [nativeVC setTitle:@"原生广告" forState:UIControlStateNormal];
    [self.view addSubview:nativeVC];
    
    [nativeVC addTarget:self action:@selector(showNativeVC) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton *feedVC = [[UIButton alloc]initWithFrame:CGRectMake(40, 190, 150, 50)];
    feedVC.backgroundColor = [UIColor blackColor];
    feedVC.titleLabel.textColor = [UIColor whiteColor];
    [feedVC setTitle:@"信息流7749" forState:UIControlStateNormal];
    [self.view addSubview:feedVC];
    
    [feedVC addTarget:self action:@selector(showFeedVC) forControlEvents:UIControlEventTouchUpInside];
    
    
    
    UIButton *feedVC2 = [[UIButton alloc]initWithFrame:CGRectMake(40, 250, 150, 50)];
    feedVC2.backgroundColor = [UIColor blackColor];
    feedVC2.titleLabel.textColor = [UIColor whiteColor];
    [feedVC2 setTitle:@"信息流7747" forState:UIControlStateNormal];
    [self.view addSubview:feedVC2];
    [feedVC2 addTarget:self action:@selector(showFeedVC2) forControlEvents:UIControlEventTouchUpInside];
    
    
    UIButton *feedVC3 = [[UIButton alloc]initWithFrame:CGRectMake(40, 330, 150, 50)];
    feedVC3.backgroundColor = [UIColor blackColor];
    feedVC3.titleLabel.textColor = [UIColor whiteColor];
    [feedVC3 setTitle:@"信息流7745" forState:UIControlStateNormal];
    [self.view addSubview:feedVC3];
    
    [feedVC3 addTarget:self action:@selector(showFeedVC3) forControlEvents:UIControlEventTouchUpInside];
}

-(void)showFeedVC{
    
    AdTableViewController  *nv = [[AdTableViewController alloc]init];
    UINavigationController *nav = [[UINavigationController alloc]initWithRootViewController:nv];
    [self presentViewController:nav animated:YES completion:^{
    }];
    
    [nv loadAd:@"7749"];
}

-(void)showFeedVC2{
    
    AdTableViewController  *nv = [[AdTableViewController alloc]init];
    UINavigationController *nav = [[UINavigationController alloc]initWithRootViewController:nv];
    [self presentViewController:nav animated:YES completion:^{
    }];
    
    [nv loadAd:@"7747"];
}

-(void)showFeedVC3{
    
    AdTableViewController  *nv = [[AdTableViewController alloc]init];
    UINavigationController *nav = [[UINavigationController alloc]initWithRootViewController:nv];
    [self presentViewController:nav animated:YES completion:^{
    }];
    
    [nv loadAd:@"7745"];
}

-(void)showNativeVC{
    NativeViewController *nv = [[NativeViewController alloc]init];
    UINavigationController *nav = [[UINavigationController alloc]initWithRootViewController:nv];
    [self presentViewController:nav animated:YES completion:^{
        
    }];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
