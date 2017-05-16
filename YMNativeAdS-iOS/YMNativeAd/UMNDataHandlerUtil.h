//
//  DataHandler.h
//  SDK
//
//  Created by ENZO YANG on 13-2-22.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//
//  作用：加密或者数据摘要

#import <Foundation/Foundation.h>

extern char *SPT_64dic;

int SPT_hexdec(const char *shex);
int SPT_64dec(const char *s64);
char *SPT_substr(char *dest, const char*src  ,size_t index, size_t len);
char *SPT_hex_to_64(char *dest, const char*src, size_t index, size_t len);



// NSString or NSData for Base64  把8位一字节转化为6位一字节
NSData *base64DataFromNString(NSString *string);
NSData *base64DataFromCString(const char *string,int length);
NSString  *base64StringFromData(NSData  *data);
NSString  *base64StringFromBytes(const uint8_t *bytes ,int length);

// Encrypted string 给请求参数加密
NSString  *encryptedSignatureWithNSString(NSString  *encryptString ,NSString  *encryptKey);  // Deprecated
NSString  *spotencryptedWithNSString(NSString  *encryptString ,NSString  *encryptKey);  // Deprecated

NSString  *spotencryptedWithNSString_prime(NSString  *encryptString ,NSString  *encryptKey ,int prime);
NSString  *encryptedWithNSData(NSData  *encryptData ,NSString  *encryptKey ,int prime);

// md5
NSString  *md5HexDigest(NSString *input);
NSString  *md5HexDigest_length(const void  *input ,int length);

// Push DeviceToken
NSString  *decodeDeviceToken(NSData  *deviceTokenData);
NSString  *decodeAES256(NSString  *base64EncodedString ,NSString *password);

