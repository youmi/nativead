//
//  UMNDataModel.h
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/6.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface UMNDataModel : NSObject

@property (nonatomic, retain) NSString *rsd;       // 请求唯一码
@property (nonatomic, assign) NSString *spotid;    // 广告id
@property (nonatomic, retain) NSString *slotid;    // 广告位id
@property (nonatomic, retain) NSString *name;      // 广告名
@property (nonatomic, retain) NSString *icon;      // 广告icon
@property (nonatomic, retain) NSArray *picArr;     // 广告图片素材列表图片集合 ,一个广告可能存在多张或者一张  (url,w,h)
@property (nonatomic, retain) NSString *slogan;    // 广告标题，一般字数比较很少，部分广告位可能没有广告标题，而只有广告语
@property (nonatomic, retain) NSString *subslogan; // 广告语，一般字数比较多
@property (nonatomic, retain) NSString *url;       // 点击跳转的落地页
@property (nonatomic, retain) NSString *uri;       // 应用的URLScheme
@property (nonatomic, assign) int cpt;             // 广告类型 0：app广告  1：wap页广告

@property (nonatomic, retain) NSString *track;     // 如果需要第三方tracking介入，则在这里；
@property (nonatomic, retain) NSArray *showTrack;  // 展示的时候需要track的链接数组
@property (nonatomic, retain) NSArray *clickTrack; // 点击的时候需要track的链接数组

@property (nonatomic, assign) int asid;                  // appstore id
@property (nonatomic, retain) NSString *bid;             // bundle id
@property (nonatomic, retain) NSString *appDescription;  // 应用描述
@property (nonatomic, retain) NSString *appSize;         // 应用大小
@property (nonatomic, retain) NSArray *appScreenshotArr; // 应用截图 (截图url集合)
@property (nonatomic, retain) NSString *appScore;        // 应用评分
@property (nonatomic, retain) NSString *appCategory;     // 应用类别

@property (nonatomic, assign) int io;    // 浏览器打开方式 0：内部浏览器 1：外部浏览器
@property (nonatomic, assign) int delay; // 关闭浏览器延迟显示时间 单位：秒
@property (nonatomic, assign) int sal;   // show_ad_logo的缩写，控制显示"广告"标识，1:显示"广告"标识(默认), 0:不显示"广告"标识
@property (nonatomic, assign) int pl;    // platform_logo的缩写，控制显示平台logo，0为不显示平台logo(默认)

@property (nonatomic, retain) NSDate *showTime; // 记录下这个广告刚刚显示的时间，用作算点击的耗费时间

- (id)initWithDiction:(NSDictionary *)preDic dic:(NSDictionary *)dic;


/**
 判断落地页URL是否为HTTPS链接

 @return true or false
 */
- (BOOL)isHttpsUrl;

/**
 图片数量，一个广告存在1或多张图片

 @return 图片数量
 */
- (int)picCount;

/**
 获取指定下标的图片

 @param index 下标
 @return 该下标的图片
 */
- (NSString *)getPicURL4PicArr:(int)index;

/**
 获取指定下标图片的宽度

 @param index 下标
 @return 图片宽度
 */
- (float)getPicWidth4PicArr:(int)index;

/**
 获取指定下标图片的高度

 @param index 下标
 @return 图片高度
 */
- (float)getPicHeight4PicArr:(int)index;

@end
