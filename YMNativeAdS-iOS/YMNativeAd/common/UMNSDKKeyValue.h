//
//  KeyValue.h
//  SDK
//
//  Created by ENZO YANG on 13-3-5.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//
//  作用：和NSUserDefault功能相似，为了不让开发者轻易看到SDK保存了什么

#import <UIKit/UIKit.h>

typedef struct keyValue {
    BOOL (*existValueOfKey)
    (NSString *key);

    //     id (*objectForKey)(NSString *key);
    // 类型不对会返回空或者 0
    NSString *(*stringForKey)(NSString *key);
    NSArray *(*arrayForKey)(NSString *key);
    NSDictionary *(*dictionaryForKey)(NSString *key);
    CGFloat (*floatForKey)(NSString *key);
    NSInteger (*integerForKey)(NSString *key);
    BOOL (*boolForKey)
    (NSString *key);
    double (*doubleForKey)(NSString *key);
    // 当setObject:nil forKey: 的时候不会改变原值
    //     void (*setObjectforKey)(id object,NSString *key);
    // 类型不对不会改变原值
    void (*setStringforKey)(NSString *string, NSString *key);
    void (*setArrayforKey)(NSArray *array, NSString *key);
    void (*setDictionaryforKey)(NSDictionary *dictionary, NSString *key);
    void (*setFloatforKey)(CGFloat number, NSString *key);
    void (*setIntegerForKeyforKey)(NSInteger, NSString *key);
    void (*setBoolForKeyforKey)(BOOL number, NSString *key);
    void (*setDoubleforKey)(double number, NSString *key);
    //     void (*removeObjectForKey)(NSString * key);
    //     void (*synchronize)();
} KeyValueStruct;

// 只支持能够序列化的类型
@interface UMNSDKKeyValue : NSObject

+ (KeyValueStruct *)shareKeyValue;

+ (id)objectForKey:(NSString *)key;

+(void)setObject:(id) object forKey:(NSString *)key;

+(void)removeObjectForKey:(NSString *)key;

+(void)synchronize;

@end
