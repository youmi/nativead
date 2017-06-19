//
//  UMNDataModel.m
//  YMNativeAdS
//
//  Created by wengxianxun on 2017/3/6.
//  Copyright © 2017年 wengxianxun. All rights reserved.
//

#import "UMNDataModel.h"
#import "UMNDataHandlerUtil.h"
#import "UMNSDKConstants.h"
#import "UMNSDKConfig.h" 
#import "Macro.h"
@implementation UMNDataModel
-(id)initWithDiction:(NSDictionary *)preDic dic:(NSDictionary*)dic open:(BOOL)ynOpen{
    self = [super init];
    if (self) {
        if (dic) {
 
            
            self.rsd = [preDic objectForKey:@"rsd"];
            self.spotid = [dic objectForKey:@"id"];
            self.slotid = [dic objectForKey:@"slotid"];
            
            
            self.name =[dic objectForKey:@"name"];
            self.icon = [dic objectForKey:@"icon"];
            self.picArr = [dic objectForKey:@"pic"];
            self.slogan = [dic objectForKey:@"slogan"];
            self.subslogan = [dic objectForKey:@"subslogan"];
            self.url = [dic objectForKey:@"url"];
            self.uri = [dic objectForKey:@"uri"];
            self.cpt = [[dic objectForKey:@"cpt"] intValue];
            
            self.track = [dic objectForKey:@"track"];
            
            NSDictionary *appDic = [dic objectForKey:@"app"];
            if (appDic && [appDic isKindOfClass:[NSDictionary class]]) {
                NSLog(@"%@",appDic);
                self.bid = [appDic objectForKey:@"bid"];
                self.asid = [[appDic objectForKey:@"storeid"] intValue];
                self.appSize = [appDic objectForKey:@"size"];
                self.appScore = [appDic objectForKey:@"score"];
                self.appCategory = [appDic objectForKey:@"category"];
                self.appDescription = [appDic objectForKey:@"description"];
                self.appScreenshotArr = [appDic objectForKey:@"screenshot"];
            }
            
            NSDictionary *ext = [dic objectForKey:@"ext"];
            if (ext && [ext isKindOfClass:[NSDictionary class]]) {
                self.io = [[ext objectForKey:@"io"] intValue];
                self.jm = [[ext objectForKey:@"jm"] intValue];
                self.sal = [[ext objectForKey:@"sal"] intValue];
                self.pl = [[ext objectForKey:@"pl"] intValue];
                self.delay = [[ext objectForKey:@"delay"] intValue];
            }
            
            
            
            
            if (ynOpen) {
                self.showTrack = [[dic objectForKey:@"track"] objectForKey:@"show"];
                self.clickTrack = [[dic objectForKey:@"track"] objectForKey:@"click"];
            }
        }
    }
    return self;
}

/*
 * 图片数量 - 一个广告存在1或多张图片
 */
-(int)picCount{
    if (self.picArr) {
        return (int)self.picArr.count;
    }
    return 0;
}
/*
 * 获取指定下标的图片
 * index: picArr(NSArray)的下标
 */
-(NSString *)getPicURL4PicArr:(int)index{
    
    if (self.picArr) {
        NSDictionary *dic = [self.picArr objectAtIndex:index];
        if (dic && [dic isKindOfClass:[NSDictionary class]]) {
            NSString* url = [dic objectForKey:@"url"];
            return url;
        }
    }
    return @"";
}

/*
 * 获取指定下标图片的width
 */
-(float)getPicWidth4PicArr:(int)index{
    if (self.picArr) {
        NSDictionary *dic = [self.picArr objectAtIndex:index];
        if (dic && [dic isKindOfClass:[NSDictionary class]]) {
            float width = [[dic objectForKey:@"w"] floatValue];
            return width;
        }
    }
    return 0;
}

/*
 * 获取指定下标图片的height
 */
-(float)getPicHeight4PicArr:(int)index{
    if (self.picArr) {
        NSDictionary *dic = [self.picArr objectAtIndex:index];
        if (dic && [dic isKindOfClass:[NSDictionary class]]) {
            float height = [[dic objectForKey:@"h"] floatValue];
            return height;
        }
    }
    return 0;
}


-(BOOL)isHttpsUrl{
    
    NSString *substr = [self.url substringToIndex:5];
    if ([substr isEqualToString:@"https"]) {
        return YES;
    }
    return NO;
}

-(id)copyWithZone:(NSZone *)zone{
    UMNDataModel *dataStructure = [[self class] new];
    dataStructure.jm = self.jm;
    dataStructure.rsd = self.rsd;
    dataStructure.sal = self.sal;
    dataStructure.pl = self.pl;
    dataStructure.spotid = self.spotid;
    dataStructure.slotid = self.slotid;
    dataStructure.asid = self.asid;
    dataStructure.bid = self.bid;
    dataStructure.name = self.name;
    dataStructure.icon = self.icon;
    dataStructure.picArr = self.picArr;
    dataStructure.slogan = self.slogan;
    dataStructure.subslogan = self.subslogan;
    dataStructure.url = self.url;
    dataStructure.uri = self.uri;
    dataStructure.cpt = self.cpt;
    dataStructure.io = self.io;
    dataStructure.delay = self.delay;
    dataStructure.track = self.track;
    dataStructure.showTrack = self.showTrack;
    dataStructure.clickTrack = self.clickTrack;
    dataStructure.appSize = self.appSize;
    dataStructure.appScore = self.appScore;
    dataStructure.appCategory = self.appCategory;
    dataStructure.appDescription = self.appDescription;
    dataStructure.appScreenshotArr  =self.appScreenshotArr;
    return dataStructure;
}

-(void)dealloc{
    self.jm = 0;
    self.rsd = nil;
    self.sal = 0;
    self.pl = 0;
    self.spotid = 0;
    self.slotid = nil;
    self.asid = 0;
    self.bid = nil;
    self.name = nil;
    self.icon = nil;
    self.picArr = nil;
    self.slogan = nil;
    self.subslogan = nil;
    self.url = nil;
    self.uri = nil;
    self.cpt = 0;
    self.io = 0;
    self.delay = 0;
    self.track = nil;
    self.appScreenshotArr = nil;
    self.appDescription = nil;
    self.appCategory = nil;
    self.appScore = nil;
    self.appSize = nil;
    self.showTrack = nil;
    self.clickTrack = nil;
}
@end
