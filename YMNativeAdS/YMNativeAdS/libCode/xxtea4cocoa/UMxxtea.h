//
//  xxtea.h
//  SDK
//
//  Created by 陈建峰 on 14-7-4.
//  Copyright (c) 2014年  Mobile Co. Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>


NSString *encryptXXTEA(NSString *key,NSData *dataa);

//里面伴随了zip解压
NSString *decryptXXTEA(NSString *key,NSString *string);


