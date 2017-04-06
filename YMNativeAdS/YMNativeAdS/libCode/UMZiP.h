//
//  ZiP.h
//  SDK
//
//  Created by 陈建峰 on 14-7-9.
//  Copyright (c) 2014年  Mobile Co. Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>


//解压
NSData *zlibInflate(NSData *data);
//压缩
NSData *zlibDeflate(NSData *data);

//解压
NSData *gzipInflate(NSData *data);
//压缩
NSData *gzipDeflate(NSData *data);

