//
//  FileHandler.h
//  SDK
//
//  Created by ENZO YANG on 13-3-6.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//
//  作用：文件操作

#import <Foundation/Foundation.h>


NSString *documentPath();
NSString *libraryPath();

// 序列化对象到文件中
void serializeObject(NSObject *obj ,NSString *path);

// 从文件中加载对象
NSObject *deserializeObjectAtPath(NSString *path);

// 文件夹
BOOL isFolderAtPath(NSString  *path);
BOOL createFolderAtPath(NSString  *path); // WithIntermediateDirectory = YES

// 不让icloud备份
void addSkipBackupAttributeToFile(NSString *path);
void addSkipBackupAttributeToFileUrl(NSURL *url);
void addSkipBackupAttributeToFilesUnderFolder(NSString *folderPath);

// 取得文件的大小
int fileSizeAtPath(NSString *path);

BOOL deleteFile(NSString  *path);

///keychain save
void save(NSString *service, id data);

///keychainload
id load(NSString *service);

///keychain数据delete
void delete(NSString *service);