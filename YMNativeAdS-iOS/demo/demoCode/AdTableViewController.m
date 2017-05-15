//
//  AdTableViewController.m
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/8.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "AdTableViewController.h"
#import "AdTableViewCell.h"
#import <objc/runtime.h>
#import "HeadView.h"
#import "AdLittleTableViewCell.h"
#import "AdBannerTableViewCell.h"

@interface UMNDataModel (ExtendedProperties)

@property (nonatomic,assign) id cellRow;

@end

static void *propertyKey = (void *)@"propertyKey";

@implementation UMNDataModel (ExtendedProperties)

- (id) cellRow {
    return objc_getAssociatedObject(self, propertyKey);
}

- (void) setCellRow:(id)myCustomProperty {
    objc_setAssociatedObject(self, propertyKey, myCustomProperty, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

@end

@interface AdTableViewController () {
    
    YMNativeAd *_nativeAd;//广告实例
    NSArray *_adArray;//广告数组（UMNSataModel实例集合）
    int _currentNum;//顺序拿广告，从0开始
    
}

@property (nonatomic,strong) UITableView *feedtableview;

@end

@implementation AdTableViewController

-(void)dealloc{
    self.feedtableview.delegate = nil;
    self.feedtableview.dataSource = nil;
    self.feedtableview = nil;
    _nativeAd.delegate = nil;
    _nativeAd = nil;
    _adArray = nil;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationItem.title = @"国际新闻";
    UIBarButtonItem *rightBtn = [[UIBarButtonItem alloc]
                                 initWithTitle:@"返回"
                                 style:UIBarButtonItemStyleDone
                                 target:self
                                 action:@selector(rightBarItemClick)];
    
    self.navigationItem.leftBarButtonItem = rightBtn;
    
    _currentNum = 0;//默认为0
    // Do any additional setup after loading the view.
    self.view.backgroundColor = [UIColor whiteColor];
    self.feedtableview = [[UITableView alloc]initWithFrame:CGRectMake(0,0,self.view.frame.size.width,self.view.frame.size.height)];
    self.feedtableview.dataSource = self;
    self.feedtableview.delegate = self;
    self.feedtableview.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.feedtableview];
    
    HeadView *headView = [[HeadView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 150)];
    //    [headView showAdView];
    [self.feedtableview setTableHeaderView:headView];
    
}
-(void)rightBarItemClick{
    [self dismissViewControllerAnimated:YES completion:^{
        
    }];
}

/************  广告 **********************/
//加载原生广告
-(void)loadAd:(NSString *)slotId{
    
    if (!_nativeAd) {
        _nativeAd = [[YMNativeAd alloc]initWithAppId:@"320a6cd3b8d2c4e0" slotId:slotId];
        _nativeAd.delegate = self;
    }
    [_nativeAd loadAd];//
}

#pragma mark -YMNvativeDelegate
//拉取成功
-(void)ymNativeAdSuccessToLoad:(NSArray *)nativeAdArray{
    if (_adArray) {
        _adArray = nil;//清空
    }
    _adArray = nativeAdArray;
}
//拉取失败
-(void)ymNativeAdFailedToLoad:(NSError *)error{
    if (_adArray) {
        _adArray = nil;//清空
    }
}

#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    return 50;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.row == 10  ) {
        if (_adArray && _adArray.count > 0) {
            UMNDataModel *addata = [_adArray objectAtIndex:0];
            
            if ([addata.slotid isEqualToString:@"7748"]) {
                return 278;
            }else if ([addata.slotid isEqualToString:@"7744"]) {
                return 84;
            }else if ([addata.slotid isEqualToString:@"7746"]) {
                return 98;
            }
        }
    }
    
    return 90;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.row == 10  ) {
        UMNDataModel *addata = [_adArray objectAtIndex:0];
        [addata setCellRow:indexPath];
        [_nativeAd showAd:addata callBackBlock:^(NSError *error) {
            
        }];
        
        if ([addata.slotid isEqualToString:@"7748"]) {
            if (_adArray && _adArray.count > 0) {
                static NSString *CellIdentifier = @"adcell";
                AdTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
                
                if (cell == nil) {
                    cell = [[AdTableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle
                                                  reuseIdentifier:CellIdentifier];
                }
                [cell setCellInfo:addata];
                return cell;
            }
        }else if ([addata.slotid isEqualToString:@"7746"]){
            
            if (_adArray && _adArray.count > 0) {
                static NSString *CellIdentifier = @"adlittlecell";
                AdLittleTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
                
                if (cell == nil) {
                    cell = [[AdLittleTableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle
                                                        reuseIdentifier:CellIdentifier];
                }
                [cell setCellInfo:addata];
                return cell;
            }
        }else if ([addata.slotid isEqualToString:@"7744"]){
            
            if (_adArray && _adArray.count > 0) {
                static NSString *CellIdentifier = @"adbannercell";
                AdBannerTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
                
                if (cell == nil) {
                    cell = [[AdBannerTableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle
                                                        reuseIdentifier:CellIdentifier];
                }
                [cell setCellInfo:addata];
                return cell;
            }
        }
    }
    
    
    
    static NSString *CellIdentifier = @"cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle
                                      reuseIdentifier:CellIdentifier];
    }
    cell.textLabel.text = @"这是一条和谐的新闻";
    return cell;
    
    
}

//
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    //        for (int i=0; i<_adArray.count; i++) {
    UMNDataModel *addata = [_adArray objectAtIndex:0];
    //    NSIndexPath *path = [addata cellRow];
    //            if (path.row == indexPath.row) {
    //                NSLog(@"点击了%@广告",addata.name);
    /* 点击广告，调用点击接口*/
    [_nativeAd clickAd:addata callBackBlock:^(NSError *error) {
        
    }];
    
    /*跳转到广告对应的AppStore页面*/
    [_nativeAd clickAdOpenAppStoreVC:addata];
    //            }
    
    //        }
    
    
    
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
