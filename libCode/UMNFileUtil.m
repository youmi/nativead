//
//  FileHandler.m
//  SDK
//
//  Created by ENZO YANG on 13-3-6.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import "UMNFileUtil.h"
#import <sys/xattr.h>

static NSMutableDictionary *HNgetKeychainQuery(NSString *service) {
    return [NSMutableDictionary dictionaryWithObjectsAndKeys:
            (id)kSecClassGenericPassword,(id)kSecClass,
            service, (id)kSecAttrService,
            service, (id)kSecAttrAccount,
            (id)kSecAttrAccessibleAfterFirstUnlock,(id)kSecAttrAccessible,
            nil];
}

void save(NSString *service, id data) {
    //save in keychain
    //Get search dictionary
    NSMutableDictionary *keychainQuery = HNgetKeychainQuery(service);
    //Delete old item before add new item
    SecItemDelete((CFDictionaryRef)keychainQuery);
    //Add new object to search dictionary(Attention:the data format)
    [keychainQuery setObject:[NSKeyedArchiver archivedDataWithRootObject:data] forKey:(id)kSecValueData];
    //Add item to keychain with the search dictionary
    SecItemAdd((CFDictionaryRef)keychainQuery, NULL);
}

id load(NSString *service) {
    //keychain
    id ret = nil;
    NSMutableDictionary *keychainQuery = HNgetKeychainQuery(service);
    //Configure the search setting
    //Since in our simple case we are expecting only a single attribute to be returned (the password) we can set the attribute kSecReturnData to kCFBooleanTrue
    [keychainQuery setObject:(id)kCFBooleanTrue forKey:(id)kSecReturnData];
    [keychainQuery setObject:(id)kSecMatchLimitOne forKey:(id)kSecMatchLimit];
    CFDataRef keyData = NULL;
    if (SecItemCopyMatching((CFDictionaryRef)keychainQuery, (CFTypeRef *)&keyData) == noErr) {
        @try {
            ret = [NSKeyedUnarchiver unarchiveObjectWithData:(__bridge NSData *)keyData];
        } @catch (NSException *e) {
            NSLog(@"Unarchive of %@ failed: %@", service, e);
        } @finally {
        }
    }
    if (keyData)
        CFRelease(keyData);
    return ret;
}

void delete(NSString *service) {
    //keychain
    NSMutableDictionary *keychainQuery = HNgetKeychainQuery(service);
    SecItemDelete((CFDictionaryRef)keychainQuery);
}

NSString *documentPath() {
    static NSString *path = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        NSArray *pathArray =  nil;
        pathArray = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        path = [pathArray objectAtIndex:0];
        
    });
    return path;
}

NSString *libraryPath() {
    static NSString *path = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        NSArray *pathArray =  nil;
        pathArray = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);
        path = [pathArray objectAtIndex:0];
        
    });
    return path;
}

BOOL deleteFile(NSString *path) {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    return [fileManager removeItemAtPath:path error:nil];
}
void serializeObject(NSObject *obj ,NSString *path) {
    if (obj == nil) {
        deleteFile(path);
    }
    NSData *data = [NSKeyedArchiver archivedDataWithRootObject:obj];
    [data writeToFile:path atomically:YES];
}

NSObject *deserializeObjectAtPath(NSString*path) {
    // 如果文件不存在 则 返回nil
    if (![[NSFileManager defaultManager] fileExistsAtPath:path]) return nil;
    
    NSData *data = [NSData dataWithContentsOfFile:path];
    NSObject *obj = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    
    return obj;
}

BOOL isFolderAtPath(NSString *path) {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError *error = nil;
    NSDictionary *attrDict = [fileManager attributesOfItemAtPath:path error:&error];
     if (!attrDict || ![[attrDict objectForKey:NSFileType] isEqualToString:NSFileTypeDirectory]) {
        return NO;
    }
    return YES;
}

BOOL createFolderAtPath(NSString *path) {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if (![fileManager fileExistsAtPath:path]) {
        return [fileManager createDirectoryAtPath:path
               withIntermediateDirectories:YES
                                attributes:nil
                                     error:NULL];
    }
    return YES;
}

void addSkipBackupAttributeToFile(NSString*path) {
    NSURL *url = [NSURL fileURLWithPath:path];
    addSkipBackupAttributeToFileUrl(url);
}

void addSkipBackupAttributeToFileUrl(NSURL*url) {
    u_int8_t b = 1;
    setxattr([[url path] fileSystemRepresentation], "com.apple.MobileBackup", &b, 1, 0, 0);
}

void addSkipBackupAttributeToFilesUnderFolder(NSString *folderPath) {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSArray *elementArray = [fileManager contentsOfDirectoryAtPath:folderPath error:nil];
    for (NSString* elementName in elementArray) {
        
        NSString *aPath = [folderPath stringByAppendingPathComponent:elementName];
        if (isFolderAtPath(aPath)) {
            addSkipBackupAttributeToFilesUnderFolder(aPath);
        } else {
            addSkipBackupAttributeToFile(aPath);
        }
    }
}

int fileSizeAtPath(NSString *path) {
    NSDictionary *fileAttributes = [[NSFileManager defaultManager] attributesOfItemAtPath:path error:nil];
    if (fileAttributes == nil) return 0;
    NSNumber *nFileSize = [fileAttributes objectForKey:NSFileSize];
    return (int)[nFileSize longLongValue];
}


 
