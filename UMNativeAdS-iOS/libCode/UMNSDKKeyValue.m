//
//  KeyValue.m
//  SDK
//
//  Created by ENZO YANG on 13-3-5.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import "UMNSDKKeyValue.h"
#import "Macro.h"
#import "UMURLPathUtil.h"
#import "UMNFileUtil.h"

//static NSString *const kSPTKeyValueFileName = @"keyvalue_data.dict";
static NSString* ZYin_keyvalue_data_dict(){
//    unsigned char spotShareDirectory[] = {(XOR_KEY ^ 'k'),(XOR_KEY ^ 'e'),(XOR_KEY ^ 'y'),(XOR_KEY ^ 'v'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 'l'),(XOR_KEY ^ 'u'),(XOR_KEY ^ 'e'),(XOR_KEY ^ '_'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'a'),(XOR_KEY ^ 't'),(XOR_KEY ^ 'a'),(XOR_KEY ^ '.'),(XOR_KEY ^ 'd'),(XOR_KEY ^ 'i'),(XOR_KEY ^ 'c'),(XOR_KEY ^ 't'),(XOR_KEY ^ '\0')};
//    OPSmixString(spotShareDirectory, XOR_KEY);
    return @"keyvalue_data.dict";//[NSString stringWithFormat:@"%s", spotShareDirectory];
}
@interface UMNSDKKeyValue()

@property (nonatomic, retain) NSMutableDictionary *dict;

@end
static KeyValueStruct *util = NULL;
@implementation UMNSDKKeyValue

+ (KeyValueStruct *)shareKeyValue{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        util = malloc(sizeof(KeyValueStruct));
        util->existValueOfKey = existValueOfKey;
//        util->objectForKey = objectForKey;
        util->stringForKey = stringForKey;
        util->arrayForKey = arrayForKey;
        util->dictionaryForKey = dictionaryForKey;
        util->floatForKey = floatForKey;
        util->integerForKey = integerForKey;
        util->boolForKey = boolForKey;
        util->doubleForKey = doubleForKey;
//        util->setObjectforKey = setObjectforKey;
        util->setStringforKey = setStringforKey;
        util->setArrayforKey = setArrayforKey;
        util->setDictionaryforKey = setDictionaryforKey;
        util->setFloatforKey = setFloatforKey;
        util->setIntegerForKeyforKey = setIntegerForKeyforKey;
        util->setBoolForKeyforKey = setBoolForKeyforKey;
        util->setDoubleforKey = setDoubleforKey;
//        util->removeObjectForKey = removeObjectForKey;
//        util->synchronize = synchronize;
       
    });
    return util;
}



+ (UMNSDKKeyValue *)sharedInstance {
    static UMNSDKKeyValue *keyValue = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        keyValue = [UMNSDKKeyValue new];
    });
    return keyValue;
}

- (id)init {
    self = [super init];
    if (self) {
        self.dict = (NSMutableDictionary *)deserializeObjectAtPath([self filePath]);
        if (!self.dict) self.dict = [NSMutableDictionary dictionaryWithCapacity:5];
        [self _addObservers];
    }
    return self;
}

- (void)dealloc {
    [self _removeObservers];
    self.dict = nil; 
}

- (NSString *)filePath {
    NSString *folder = [libraryPath() stringByAppendingPathComponent:getSpotShareDirectory()];
    createFolderAtPath(folder);
    return [folder stringByAppendingPathComponent:ZYin_keyvalue_data_dict()];
}

- (void)_addObservers {
    // 添加观察者，若程序将要退出则保存数据
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(_applicationWillTerminate:)
												 name:UIApplicationWillTerminateNotification
											   object:nil];

	
	
    
    // 进入后台
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(_applicationWillResignActive:)
                                                 name:UIApplicationWillResignActiveNotification
                                               object:nil];
}

- (void)_removeObservers {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)_applicationWillTerminate:(NSNotification *)notification {
	[UMNSDKKeyValue synchronize];
}

- (void)_applicationWillResignActive:(NSNotification *)note {
	[UMNSDKKeyValue synchronize];
}


 static BOOL existValueOfKey(NSString *key) {
    return ([UMNSDKKeyValue objectForKey:key] != nil);
}

+(id)objectForKey:(NSString *)key {
    @synchronized(self) {
        return [[self sharedInstance].dict objectForKey:key];
    }
}

static NSString * stringForKey(NSString *key) {
    NSString *str = [UMNSDKKeyValue objectForKey:key];
    if (![str isKindOfClass:[NSString class]]) return nil;
    return str;
}

static NSArray * arrayForKey(NSString * key) {
    NSArray *arr = [UMNSDKKeyValue objectForKey:key];
    if (![arr isKindOfClass:[NSArray class]]) return nil;
    return arr;
}

static NSDictionary * dictionaryForKey(NSString *key) {
    NSDictionary *dict = [UMNSDKKeyValue objectForKey:key];
    if (![dict isKindOfClass:[NSDictionary class]]) return nil;
    return dict;
}

static CGFloat floatForKey(NSString *key) {
    NSNumber *num = [UMNSDKKeyValue objectForKey:key];
    if (![num isKindOfClass:[NSNumber class]]) return 0.0f;
    return [num floatValue];
}

static double doubleForKey(NSString *key) {
    NSNumber *num = [UMNSDKKeyValue objectForKey:key];
    if (![num isKindOfClass:[NSNumber class]]) return 0.0f;
    return [num doubleValue];
}

static NSInteger integerForKey(NSString *key) {
    NSNumber *num = [UMNSDKKeyValue objectForKey:key];
    if (![num isKindOfClass:[NSNumber class]]) return 0;
    return [num integerValue];
}

static BOOL boolForKey(NSString *key) {
    NSNumber *num = [UMNSDKKeyValue objectForKey:key];
    if (![num isKindOfClass:[NSNumber class]]) return NO;
    return [num boolValue];
}

+(void)setObject:(id) object forKey:(NSString *)key {
    if (object == nil) {
        OGERROR(@"[KeyValue] set nil to key(%@)", key);
        return;
    }
    @synchronized(self) {
        [[self sharedInstance].dict setObject:object forKey:key];
    }
}

static void setStringforKey(NSString *string,NSString *key) {
    if (![string isKindOfClass:[NSString class]]) {
        OGERROR(@"[KeyValue] value is not string for key(%@)", key);
        return;
    }
    [UMNSDKKeyValue setObject:string forKey:key];
}

static void setArrayforKey(NSArray *array ,NSString *key) {
    if (![array isKindOfClass:[NSArray class]]) {
        OGERROR(@"[KeyValue] value is not array for key(%@)", key);
        return;
    }
    [UMNSDKKeyValue setObject:array forKey:key];
}

static void setDictionaryforKey(NSDictionary *dictionary,NSString *key) {
    if (![dictionary isKindOfClass:[NSDictionary class]]) {
        OGERROR(@"[KeyValue] value is not array for key(%@)", key);
        return;
    }
    [UMNSDKKeyValue setObject:dictionary forKey:key];
}

static void setFloatforKey(CGFloat number,NSString *key) {
    NSNumber *n = [NSNumber numberWithFloat:number];
    [UMNSDKKeyValue setObject:n forKey:key];
}

static void setDoubleforKey(double number,NSString *key){
    NSNumber *n = [NSNumber numberWithDouble:number];
    [UMNSDKKeyValue setObject:n forKey:key];
}

static void setIntegerForKeyforKey(NSInteger number,NSString *key) {
    NSNumber *n = [NSNumber numberWithInteger:number];
    [UMNSDKKeyValue setObject:n forKey:key];
}

static void setBoolForKeyforKey(BOOL number,NSString *key) {
    NSNumber *n = [NSNumber numberWithBool:number];
    [UMNSDKKeyValue setObject:n forKey:key];
}

+(void)removeObjectForKey:(NSString *)key {
    @synchronized(self) {
        [[self sharedInstance].dict removeObjectForKey:key];
    }
}

+(void)synchronize{
    @synchronized(self) {
        serializeObject([self sharedInstance].dict ,[[self sharedInstance] filePath]);
    }
}
@end
