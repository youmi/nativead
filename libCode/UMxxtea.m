//
//  xxtea.m
//  SDK
//
//  Created by 陈建峰 on 14-7-4.
//  Copyright (c) 2014年  Mobile Co. Ltd. All rights reserved.
//

#import "UMxxtea.h"
#import "UMZiP.h"
#import "UMNDataHandlerUtil.h"

typedef uint32_t xxtea_long;

#define XXTEA_MX (z >> 5 ^ y << 2) + (y >> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z)
#define XXTEA_DELTA 0x9e3779b9

void xxtea_long_encrypt(xxtea_long *v, xxtea_long len, xxtea_long *k) {
    xxtea_long n = len - 1;
    xxtea_long z = v[n], y = v[0], p, q = 6 + 52 / (n + 1), sum = 0, e;
    if (n < 1) {
        return;
    }
    while (0 < q--) {
        sum += XXTEA_DELTA;
        e = sum >> 2 & 3;
        for (p = 0; p < n; p++) {
            y = v[p + 1];
            z = v[p] += XXTEA_MX;
        }
        y = v[0];
        z = v[n] += XXTEA_MX;
    }
}

void xxtea_long_decrypt(xxtea_long *v, xxtea_long len, xxtea_long *k) {
    xxtea_long n = len - 1;
    xxtea_long z = v[n], y = v[0], p, q = 6 + 52 / (n + 1), sum = q * XXTEA_DELTA, e;
    if (n < 1) {
        return;
    }
    while (sum != 0) {
        e = sum >> 2 & 3;
        for (p = n; p > 0; p--) {
            z = v[p - 1];
            y = v[p] -= XXTEA_MX;
        }
        z = v[n];
        y = v[0] -= XXTEA_MX;
        sum -= XXTEA_DELTA;
    }
}

static xxtea_long *xxtea_to_long_array(const unsigned char *data, xxtea_long len, int include_length, xxtea_long *ret_len) {
    xxtea_long i, n, *result;
    n = len >> 2;
    n = (((len & 3) == 0) ? n : n + 1);
    if (include_length) {
        result = (xxtea_long *)malloc((n + 1) << 2);
        result[n] = len;
        *ret_len = n + 1;
    } else {
        result = (xxtea_long *)malloc(n << 2);
        *ret_len = n;
    }
    memset(result, 0, n << 2);
    for (i = 0; i < len; i++) {
        result[i >> 2] |= (xxtea_long)data[i] << ((i & 3) << 3);
    }
    return result;
}

static char *xxtea_to_byte_array(xxtea_long *data, xxtea_long len, int include_length, xxtea_long *ret_len) {
    xxtea_long i, n, m;
    char *result;
    n = len << 2;
    if (include_length) {
        m = data[len - 1];
        if ((m < n - 7) || (m > n - 4)){
            //            NSLog(@"m:%i n:%i ",m,n);
            return NULL;
        }
        n = m;
    }
    result = (char *)malloc(n + 1);
    for (i = 0; i < n; i++) {
        result[i] = (char)((data[i >> 2] >> ((i & 3) << 3)) & 0xff);
    }
    result[n] = '\0';
    *ret_len = n;
    return result;
}

NSString *encryptXXTEA(NSString *key,NSData *dataa){
    const unsigned char *data = (const unsigned char *)[dataa bytes];
    const unsigned char *strkey = (const unsigned char *)[key UTF8String];
    
    xxtea_long len = (xxtea_long)dataa.length;
    xxtea_long ret_len;
    
    const char *result;
    xxtea_long *v, *k, v_len, k_len;
    v = xxtea_to_long_array(data, len, 1, &v_len);
    k = xxtea_to_long_array(strkey, 16, 0, &k_len);
    xxtea_long_encrypt(v, v_len, k);
    result = xxtea_to_byte_array(v, v_len, 0, &ret_len);
    free(v);
    free(k);
    
    NSData *dataResult = [NSData dataWithBytes:result length:ret_len];
    NSString *newstr = base64StringFromData(dataResult);
    //    NSLog(@"ecncryped neew str is %@",newstr);
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wincompatible-pointer-types-discards-qualifiers"
    
    free(result);
    return newstr;
}

NSString *decryptXXTEA(NSString *key,NSString *string){
    NSData *_data = base64DataFromNString(string);
    const unsigned char *data = (const unsigned char *)[_data bytes];
    const unsigned char *strkey = (const unsigned char *)[key UTF8String];
    xxtea_long len = (xxtea_long)_data.length;
    
    xxtea_long ret_len;
    const unsigned char *result;
    xxtea_long *v, *k, v_len, k_len;
    v = xxtea_to_long_array(data, len, 0, &v_len);
    k = xxtea_to_long_array(strkey, 16, 0, &k_len);
    xxtea_long_decrypt(v, v_len, k);
    
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wpointer-sign"
    result = xxtea_to_byte_array(v, v_len, 1, &ret_len);
    free(v);
    free(k);
    
    if (result == NULL) {
        return nil;
    }
    
    NSData *resultData = [NSData dataWithBytes:result length:ret_len];
    NSData* inflateData = zlibInflate(resultData);
    NSString * newstr = [[NSString alloc] initWithData:inflateData encoding:NSUTF8StringEncoding];
    
    free(result);
    return newstr;
}

#pragma clang diagnostic pop
