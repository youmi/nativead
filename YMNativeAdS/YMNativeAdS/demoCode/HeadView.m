//
//  HeadView.m
//  YMNativeAdS
//
//  Created by linxiaolong on 2017/3/9.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "HeadView.h"
#import "YMNativeAd.h"
#import "ImageCarouselView.h"
#import "CarouselSubview.h"
#import "CarouselCellInfo.h"

@interface HeadView ()<YMNativeAdDelegate,ImageCarouselViewDelegate, ImageCarouselViewDataSource>{
    YMNativeAd *_nativeAd;//广告实例
    NSArray *_adArray;//广告数组（UMNSataModel实例集合）
    UMNDataModel *_currentAd;//当前展示的ad
}
@property (nonatomic, strong) NSArray *cellInfoArray;
@property (nonatomic, strong) ImageCarouselView *imageCarouselView;
@property (nonatomic, assign) NSUInteger pageWidth;
@property (nonatomic, assign) NSUInteger pageHeight;
@end

@implementation HeadView

-(void)dealloc{
    
    _imageCarouselView.delegate = nil;
    _imageCarouselView.dataSource = nil;
    _imageCarouselView = nil;
    _cellInfoArray = nil;
    _nativeAd.delegate = nil;
    _nativeAd = nil;
    _adArray = nil;
    _currentAd = nil;
}

-(id)initWithFrame:(CGRect)frame{

    self = [super initWithFrame:frame];
    if (self) {
        [self loadAd];
        _imageCarouselView = [[ImageCarouselView alloc] initWithFrame:CGRectMake(0, 5, self.bounds.size.width, self.pageHeight) withDataSource:self withDelegate:self];
        [self addSubview:_imageCarouselView];
        
        
    }
    return self;
}


- (CGSize)sizeForPageInCarouselView:(ImageCarouselView *)carouselView {
    return CGSizeMake(self.pageWidth, self.pageHeight);
}

- (NSInteger)numberOfPagesInCarouselView:(ImageCarouselView *)carouselView {
    return self.cellInfoArray.count;
}

- (CarouselCell *)carouselView:(ImageCarouselView *)carouselView cellForPageAtIndex:(NSUInteger)index {
    
    if (index == 1 && _adArray) {
        UMNDataModel *dataModel = [_adArray objectAtIndex:0];
        _currentAd = dataModel;
        
        CarouselCellAdView *cell = [[CarouselCellAdView alloc] initWithFrame:CGRectMake(0, 0, self.pageWidth - 4, self.pageHeight)];
        cell.showTime = 3;
        cell.adNameLb.text = _currentAd.name;
        
        NSURL *iconURL = [NSURL URLWithString:[_currentAd getPicURL4PicArr:0]];
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0), ^{
            NSData *imageData = [NSData dataWithContentsOfURL:iconURL];
            dispatch_async(dispatch_get_main_queue(), ^{
                cell.imageView.image = [UIImage imageWithData:imageData];
            });
        });
        
        return cell;
    }else{
        CarouselCellImageView *cell = [[CarouselCellImageView alloc] initWithFrame:CGRectMake(0, 0, self.pageWidth - 4, self.pageHeight)];
        CarouselCellInfo *cellInfo = self.cellInfoArray[index];
        cell.imageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@", cellInfo.imageName]];
        cell.showTime = cellInfo.showTime;
        return cell;
    }
}

- (void)carouselView:(ImageCarouselView *)carouselView didScrollToPage:(NSInteger)pageNumber {

}

- (void)carouselView:(ImageCarouselView *)carouselView didSelectPageAtIndex:(NSInteger)index {
    
}

- (NSUInteger)pageWidth {
    return self.bounds.size.width * 0.84;
}

- (NSUInteger)pageHeight {
    return self.frame.size.height-10;
}

- (NSArray *)cellInfoArray {
    if (_cellInfoArray == nil) {
        
        NSArray *dictArr = [NSArray arrayWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"imageInfos.plist" ofType:nil]];
        // 2.创建一个可变数据
        NSMutableArray *arrM = [NSMutableArray arrayWithCapacity:dictArr.count];
        // 3.遍历字典数组,来做字典转模型
        for (NSDictionary *dict in dictArr) {
            // 把字典转换成模型同时添加到可变数组中
            [arrM addObject:[CarouselCellInfo cellInfoWithDict:dict]];
        }
        _cellInfoArray = arrM;
    }
    return _cellInfoArray;
}


//加载原生广告
-(void)loadAd{
    
//    if (!_nativeAd) {
//        _nativeAd = [[YMNativeAd alloc]initWithAppId:@"320a6cd3b8d2c4e0" secret:@"1c2d8c5b3ffe9129" slotId:@"7745"];
//        [_nativeAd setContTitle:@"新闻页面" contKw:@"国内新闻，头条" contUrl:@"http://www.baidu.com" contCat:@""];
//        _nativeAd.delegate = self;
//    }
//    [_nativeAd loadAd];//拉取广告，3为数量,
}



- (void)viewTapped:(UITapGestureRecognizer *)gr {
    /*点击发生，调用点击接口*/
    [_nativeAd clickAd:_currentAd callBackBlock:^(NSError *error) {
        
    }];
}

#pragma mark -YMNvativeDelegate
//拉取成功
-(void)ymNativeAdSuccessToLoad:(NSArray *)nativeAdArray{
    _adArray = nativeAdArray;
}
//拉取失败
-(void)ymNativeAdFailedToLoad:(NSError *)error{
    
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
