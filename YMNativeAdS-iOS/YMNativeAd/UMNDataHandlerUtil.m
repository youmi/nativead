//
//  DataHandler.m
//  SDK
//
//  Created by ENZO YANG on 13-2-22.
//  Copyright (c) 2013年  Mobile Co. Ltd. All rights reserved.
//

#import "UMNDataHandlerUtil.h"
#import <CommonCrypto/CommonHMAC.h>
#import <CommonCrypto/CommonDigest.h>
#import <CommonCrypto/CommonCryptor.h>

#define ArrayLength(x) (sizeof(x)/sizeof(*(x)))

char *SPT_64dic = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-";

static char base64EncodingTable[64] = {
	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
	'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
	'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
	'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
};
static char base64base64DecodingTable[128];


static void FixKeyLengths( CCAlgorithm algorithm, NSMutableData * keyData, NSMutableData * ivData )
{
    NSUInteger keyLength = [keyData length];
    switch ( algorithm )
    {
        case kCCAlgorithmAES128:
        {
            if ( keyLength < 16 )
            {
                [keyData setLength: 16];
            }
            else if ( keyLength < 24 )
            {
                [keyData setLength: 24];
            }
            else
            {
                [keyData setLength: 32];
            }
            
            break;
        }
            
        case kCCAlgorithmDES:
        {
            [keyData setLength: 8];
            break;
        }
            
        case kCCAlgorithm3DES:
        {
            [keyData setLength: 24];
            break;
        }
            
        case kCCAlgorithmCAST:
        {
            if ( keyLength < 5 )
            {
                [keyData setLength: 5];
            }
            else if ( keyLength > 16 )
            {
                [keyData setLength: 16];
            }
            
            break;
        }
            
        case kCCAlgorithmRC4:
        {
            if ( keyLength > 512 )
                [keyData setLength: 512];
            break;
        }
            
        default:
            break;
    }
    
    [ivData setLength: [keyData length]];
}

int SPT_hexdec(const char *shex) {
	int result = 0, mid = 0;
	int len = (int)strlen(shex);
	for (int i = 0; i < len; i++) {
		if (shex[i] >= '0' && shex[i] <= '9') {
			mid = shex[i] -'0';
		} else if (shex[i] >= 'a' && shex[i] <= 'f') {
			mid = shex[i] - 'a' + 10;
		} else if (shex[i] >= 'A' && shex[i] <= 'F') {
			mid = shex[i] - 'A' + 10;
		} else {
			return -1;
		}
		
		mid <<= ((len - i - 1) << 2);
		result |= mid;
	}
	
	return result;
}

int SPT_64dec(const char *s64) {
    int result = 0, mid = 0;
    int len = (int)strlen(s64);
    for (int i = 0; i < len; i++) {
        if (s64[i] >= '0' && s64[i] <= '9') {
            mid = s64[i] - '0';
        } else if (s64[i] >= 'a' && s64[i] <= 'z') {
            mid = s64[i] - 'a' + 10;
        } else if (s64[i] >= 'A' && s64[i] <= 'Z') {
            mid = s64[i] - 'A' + 10 + 26;
        } else if (s64[i] == '_') {
            mid = 62;
        } else if (s64[i] == '-') {
            mid = 63;
        } else {
            return -1;
        }
        
        mid <<= ((len - i - 1) * 6);
		result |= mid;
    }
    
    return result;
}

char *SPT_substr(char *dest, const char*src  ,size_t index, size_t len) {
	assert(dest != NULL && src != NULL && index >= 0 && index < strlen(src) && len <= (strlen(src) - index));
	char *substr = strncpy(dest, src + index, len);
	substr[len] = '\0';
	return substr;
}

char *SPT_hex_to_64(char *dest, const char*src, size_t index, size_t len) {
	assert(dest != NULL && src != NULL && index >= 0 && index < strlen(src) && len <= (strlen(src) - index));
	
	char *src_cstr = (char *)(src + index);
	char *dec_sub_cstr = (char *)malloc(sizeof(char) * (3 + 1));
	int j = 0;
	for (int i = 0; i < len; i += 3) {
		int sub_len = ((len - i) > 3) ? 3 : (int)(len - i);
		// hexdec:十六进制转十进制
		int dec = SPT_hexdec(SPT_substr(dec_sub_cstr, src_cstr, i, sub_len));
		
		// >>:右移运算
		int high_index = (dec < 64) ? 0 : dec >> 6;
		int low_index = dec & 63;
		
		dest[j++] = SPT_64dic[high_index];
		dest[j++] = SPT_64dic[low_index];
	}
	dest[j++] = '\0';
	
	// 释放
	free(dec_sub_cstr);
	
	return dest;
}


NSData*base64DataFromNString(NSString*string) {
    return base64DataFromCString([string cStringUsingEncoding:NSASCIIStringEncoding] ,(int)string.length);
}

NSData*base64DataFromCString(const char*string ,int length) {
    if ((string == NULL) || (length % 4 != 0)) {
		return nil;
	}
	while (length > 0 && string[length - 1] == '=') {
		length--;
	}
	NSInteger outputLength = length * 3 / 4;
	NSMutableData* data = [NSMutableData dataWithLength:outputLength];
    

        memset(base64base64DecodingTable, 0, ArrayLength(base64EncodingTable));
        for (NSInteger i = 0; i < ArrayLength(base64EncodingTable); i++) {
            base64base64DecodingTable[base64EncodingTable[i]] = i;
        }
        
        uint8_t* output = data.mutableBytes;
        
        NSInteger inputPoint = 0;
        NSInteger outputPoint = 0;
        while (inputPoint < length) {
            char i0 = string[inputPoint++];
            char i1 = string[inputPoint++];
            char i2 = inputPoint < length ? string[inputPoint++] : 'A'; /* 'A' will decode to \0 */
            char i3 = inputPoint < length ? string[inputPoint++] : 'A';
            
            output[outputPoint++] = (base64base64DecodingTable[i0] << 2) | (base64base64DecodingTable[i1] >> 4);
            if (outputPoint < outputLength) {
                output[outputPoint++] = ((base64base64DecodingTable[i1] & 0xf) << 4) | (base64base64DecodingTable[i2] >> 2);
            }
            if (outputPoint < outputLength) {
                output[outputPoint++] = ((base64base64DecodingTable[i2] & 0x3) << 6) | base64base64DecodingTable[i3];
            }
        }

	
	return data;
}

NSString *base64StringFromData(NSData *data) {
    return base64StringFromBytes((const uint8_t*) data.bytes ,(int)data.length);
}

NSString *base64StringFromBytes(const uint8_t*bytes ,int length) {
    NSMutableData* data = [NSMutableData dataWithLength:((length + 2) / 3) * 4];
    uint8_t* output = (uint8_t*)data.mutableBytes;
	
    for (NSInteger i = 0; i < length; i += 3) {
        NSInteger value = 0;
        for (NSInteger j = i; j < (i + 3); j++) {
            value <<= 8;
			
            if (j < length) {
                value |= (0xFF & bytes[j]);
            }
        }
		
        NSInteger index = (i / 3) * 4;
        output[index + 0] =                    base64EncodingTable[(value >> 18) & 0x3F];
        output[index + 1] =                    base64EncodingTable[(value >> 12) & 0x3F];
        output[index + 2] = (i + 1) < length ? base64EncodingTable[(value >> 6)  & 0x3F] : '=';
        output[index + 3] = (i + 2) < length ? base64EncodingTable[(value >> 0)  & 0x3F] : '=';
    }
	
    return [[NSString alloc] initWithData:data
                                  encoding:NSASCIIStringEncoding];
}

#pragma mark - Encrypted String

NSString *encryptedSignatureWithNSString(NSString *encryptString ,NSString *encryptKey) {
	// HMAC-加密过程
	NSString *key = encryptKey;
	NSString *data= encryptString;
	
	const char *cKey  = [key cStringUsingEncoding:NSASCIIStringEncoding];
	const char *cData = [data cStringUsingEncoding:NSASCIIStringEncoding];
	
	unsigned char cHMAC[CC_SHA1_DIGEST_LENGTH];
	
	CCHmac(kCCHmacAlgSHA1, cKey, strlen(cKey), cData, strlen(cData), cHMAC);
	
	NSData *HMAC = [[NSData alloc] initWithBytes:cHMAC
										  length:sizeof(cHMAC)];
	
	NSString *encryptedString  = base64StringFromData(HMAC);
	
	// 释放
//	[HMAC release];
	
	return encryptedString;
}

NSString *spotencryptedWithNSString(NSString *encryptString ,NSString *encryptKey) {
	static NSString * dic = @"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-";
	static NSString * hexchars = @"0123456789ABCDEF";
	
	NSMutableString *strBuffer = [[NSMutableString alloc] init];
	NSMutableString *hexBuffer = [[NSMutableString alloc] init];
	int key_len = (int)encryptKey.length;
	int str_len = (int)encryptString.length;
	int nums = ((str_len - 1) / key_len) + 1;
	int start = 0;
	int end = 0;
	int tmp = 0;
	NSString *subStr = nil;
	for (int i = 0; i < nums; i++) {
		end = start + key_len;
		if (end > str_len) {
			end = str_len;
		}
		NSRange range;
		range.location = start;
		range.length = end - start;
		subStr = [encryptString substringWithRange:range];
		// 对subStr异或加密
		NSData *subData = [subStr dataUsingEncoding:NSUTF8StringEncoding];
		NSUInteger sub_len = [subData length];
		unsigned char* subBytes;
		unsigned char* keyBytes;
		unsigned char datas[sub_len];
        memset(datas, 0, sub_len);
        
		subBytes = (unsigned char *)[subData bytes];
		keyBytes = (unsigned char *)[[encryptKey dataUsingEncoding:NSUTF8StringEncoding] bytes];
		int step = key_len;
		int sub_nums = (int)((sub_len - 1) / key_len) + 1;
		int sub_start = 0;
		int index = 0;
		for (int m = 0; m < sub_nums; m++) {
			for (int n = 0; n < step; n++) {
				index = sub_start + n;
				if (index < sub_len) {
					// 异或
					datas[index] = (unsigned char)(subBytes[index] ^ keyBytes[n]);
				}else{
					break;
				}
			}
			sub_start += step;
		}
		
		// 获取datas字节数组的16进制表示
		for (int l = 0; l < sub_len; l++) {
			NSRange rang;
			tmp = (datas[l] >> 4) & 0x0f;
			rang.location = tmp;
			rang.length = 1;
			[strBuffer appendString:[hexchars substringWithRange:rang]];
			tmp = datas[l] & 0x0f;
			rang.location = tmp;
			rang.length = 1;
			[strBuffer appendString:[hexchars substringWithRange:rang]];
		}
		start += key_len;
	}
	
	// 把16进制数转化为密文
	int hex_lens = (int)[strBuffer length];
	int hex_index = 0;
	int hex_dec = 0;
	unsigned char hex_chr = 0;
	int hex_end = 0;
	
	NSRange rang;
	rang.location = hex_lens % 3;
	rang.length = 1;
	[hexBuffer appendString:[dic substringWithRange:rang]];
	NSInteger MAX_LEN = [dic length];
	
	for (int z = 0; z < hex_lens; z  += 3) {
		hex_end = ((z + 3) < hex_lens ? (z + 3) : hex_lens);
		rang.location = z;
		rang.length = hex_end - z;
		NSString *tmp = [strBuffer substringWithRange:rang];
		hex_dec = (int)strtoul([tmp UTF8String], 0, 16);
		hex_chr = (unsigned char)((hex_dec < MAX_LEN) ? 0 : (hex_dec >> 6));
		rang.location = (hex_chr + hex_index) % MAX_LEN;
		rang.length = 1;
		[hexBuffer appendString:[dic substringWithRange:rang]];
		hex_index++;
		rang.location = (hex_dec + hex_index) % MAX_LEN;
		rang.length = 1;
		[hexBuffer appendString:[dic substringWithRange:rang]];
		hex_index++;
	}
	// 释放
//	[strBuffer release];
//	[hexBuffer autorelease];
	return hexBuffer;
}

NSString *spotencryptedWithNSString_prime(NSString *encryptString ,NSString *encryptKey ,int prime) {
	return encryptedWithNSData([encryptString dataUsingEncoding:NSUTF8StringEncoding]
                                  ,encryptKey ,prime);
}

NSString *encryptedWithNSData(NSData *encryptData ,NSString *encryptKey ,int prime) {
	static char *dic = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-";
	static char *hexchars = "0123456789ABCDEF";
	static int	primes[] = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31};
	
	// 定义变量
	NSMutableString *plain_key = [[NSMutableString alloc] init];
	NSString *key;
	const char *key_cstr;
	const char *src_cstr;
	int random_x0 = 0;          // 初始值
	int random_a	= 0;		// 随机序列计算的参数(不大于x0的最大伪质数（不是的概率大概是34%，不过并不影响结果）)
	int random_b	= 0;		// 随机序列计算的另一个参数，由中间层指定（各个平台不同）,主要就是加密方法传入的那个素数
	
	NSMutableString *xorBuffer		= [[NSMutableString alloc] init];		// 保存异或结果
	NSMutableString *cipherBuffer	= [[NSMutableString alloc] init];		// 转换64进制的结果
	NSMutableString *result			= [[NSMutableString alloc] init];		// 加密结果
	
	// 1.初始化明文密钥
	// NSString *src_key = [[NSString alloc] initWithFormat:@"%@%@", encryptKey, encryptString];
	NSMutableData *src_key_data = [[NSMutableData alloc] init];
	[src_key_data appendData:[encryptKey dataUsingEncoding:NSUTF8StringEncoding]];
	[src_key_data appendData:encryptData];
	const char *src_key_cstr = [md5HexDigest_length([src_key_data bytes] ,(int)[src_key_data length]) UTF8String];
	char *sub_src_key_cstr = (char *)malloc(sizeof(char) * (9 + 1));
	char *dec_src_key_cstr = (char *)malloc(sizeof(char) * (3 + 1));
	char *dec_res_cstr = (char *)malloc(sizeof(char) * 6);
	SPT_substr(sub_src_key_cstr, src_key_cstr, 9, 9);
	for (int i = 0, j = 0; i < 9; i += 3) {
		// hexdec:十六进制转十进制
		int dec = SPT_hexdec(SPT_substr(dec_src_key_cstr, sub_src_key_cstr, i, 3));
		
		// >>:右移运算
		int high_index = (dec < 64) ? 0 : dec >> 6;
		int low_index = dec & 63;
		
		dec_res_cstr[j++] = dic[high_index];
		dec_res_cstr[j++] = dic[low_index];
	}
	// 生成plain_key
	NSString *plain_key_str = [[NSString alloc] initWithBytes:dec_res_cstr length:6 encoding:NSUTF8StringEncoding];
	[plain_key appendString:plain_key_str];
//	[plain_key_str release];
//	
//	// 释放
//	[src_key_data release];
	free(sub_src_key_cstr);
	free(dec_src_key_cstr);
	free(dec_res_cstr);
	
	
	// 2.初始化
	NSString *cat_key = [[NSString alloc] initWithFormat:@"%@%@", plain_key, encryptKey];
	key = md5HexDigest(cat_key);		// 密钥根据明文密钥和参数密钥生成
	key_cstr = [key UTF8String];
	random_b = prime;							// 参数传递的素数
	
	// 计算初始值（将密钥中的前26位字符转换成26位二进制数）
	random_x0 = 0;
	for (int i = 0; i < 26; i++) {
		random_x0 <<= 1; // 左移一位
		if (key_cstr[i] > '7') {
			random_x0++;
		}
	}
	
	// 最小值
	if (random_x0 < 67) {
		random_x0 = 67;
	}
	
	// 计算参数 random_a
	int random_ax = (0 == (random_x0 & 1)) ? (random_x0 - 1) : (random_x0 - 2);
	while (random_ax > 0) {
		BOOL flag = YES;
		for (int i = 0; i < 11 ; i++) {
			if (0 == (random_ax % primes[i])) {
				// 如果可以被整除，则说明不是素数
				flag = NO;
				break;
			}
		}
		// 如果都不能被整除，则是我们要找的数
		if (flag) {
			break;
		}
		random_ax -= 2;
	}
	random_a = random_ax;
	
	// 释放
//	[cat_key release];
	
	// 3.加密运算[异或运算]
	src_cstr = [encryptData bytes];
	int key_len = (int)[key lengthOfBytesUsingEncoding:NSUTF8StringEncoding];
	int src_len = (int)[encryptData length];
	int nums = ceil((src_len * 1.0) / key_len);
	int start = 0, end = 0, tmp = 0;
	for (int i = 0; i < nums; i++) {
		end = start + key_len;
		if (end > src_len) {
			end = src_len;
		}
		
		// 子字符串与key的“按位异或”运算
		int sub_len = end - start;
		char datas[sub_len];
		for (int j = 0; j < sub_len; j++) {
			datas[j] = src_cstr[start + j] ^ key_cstr[j];
		}
		
		// 将二进制数据转换成十六进制表示
		for (int l = 0; l < sub_len; l++) {
			char hex_chs[2];
			tmp = (datas[l] >> 4) & 0x0f;
			hex_chs[0] = hexchars[tmp];
			tmp = datas[l] & 0x0f;
			hex_chs[1] = hexchars[tmp];
			NSString *hex_str = [[NSString alloc] initWithBytes:hex_chs length:2 encoding:NSUTF8StringEncoding];
			[xorBuffer appendString:hex_str];
//			[hex_str release];
		}
		start += key_len;
	}
	
	// 4.把16进制数转化为密文
	int xor_len = (int)[xorBuffer lengthOfBytesUsingEncoding:NSUTF8StringEncoding];
	int index = random_x0;
	int dec_index = 0;
	
	// 获取第一个标志字符
	NSString *first_ch = [[NSString alloc] initWithBytes:(dic + xor_len % 3) length:1 encoding:NSUTF8StringEncoding];
	[cipherBuffer appendString:first_ch];
//	[first_ch release];
	
	// 十六进制转换为64进制
	const char *xor_cstr = [xorBuffer UTF8String];
	char *dec_xor_cstr = malloc(sizeof(char) * (3 + 1));
	for (int i = 0; i < xor_len; i += 3) {
		// hexdec:十六进制转十进制
		int sub_len = ((xor_len - i) > 3) ? 3 : (xor_len - i);
		int dec = SPT_hexdec(SPT_substr(dec_xor_cstr, xor_cstr, i, sub_len));
		
		// >>: 右移运算符
		dec_index = (dec < 64) ? 0 : dec >> 6;
		
		// 第一个64进制度字符
		index = (random_a * index + random_b) & 63;
		NSString *cipher1 = [[NSString alloc] initWithBytes:(dic +  ((dec_index + index) & 63))
													 length:1 encoding:NSUTF8StringEncoding];
		[cipherBuffer appendString:cipher1];
//		[cipher1 release];
		
		// 第二个64进制字符
		index = (random_a * index + random_b) & 63;
		NSString *cipher2 = [[NSString alloc] initWithBytes:(dic +  ((dec + index) & 63))
													 length:1 encoding:NSUTF8StringEncoding];
		[cipherBuffer appendString:cipher2];
//		[cipher2 release];
		
	}
	// 释放
	free(dec_xor_cstr);
	
	// 5.组合加密结果
	[result appendFormat:@"%@%@", plain_key, cipherBuffer];
	
	// 最后释放
//	[plain_key release];
//	[xorBuffer release];
//	[cipherBuffer release];
	
	return result;
}
//生成一串16进制的摘要
NSString*md5HexDigest(NSString* input){
    const char* str = [input UTF8String];
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_MD5(str, (CC_LONG)strlen(str), result);
	
    NSMutableString *ret = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH*2];
    for(int i = 0; i < CC_MD5_DIGEST_LENGTH; i++) {
        [ret appendFormat:@"%02x",result[i]];
    }
    return ret;
}

NSString *md5HexDigest_length(const void *input ,int length) {
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_MD5(input, length, result);
	
    NSMutableString *ret = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH*2];
    for(int i = 0; i < CC_MD5_DIGEST_LENGTH; i++) {
        [ret appendFormat:@"%02x",result[i]];
    }
    return ret;
}

#pragma mark - Push DeviceToken

NSString *decodeDeviceToken(NSData *deviceTokenData) {
    const unsigned *tokenBytes = [deviceTokenData bytes];
    NSString *hexToken = [[NSString alloc] initWithFormat:@"%08x%08x%08x%08x%08x%08x%08x%08x",
                          ntohl(tokenBytes[0]), ntohl(tokenBytes[1]), ntohl(tokenBytes[2]),
                          ntohl(tokenBytes[3]), ntohl(tokenBytes[4]), ntohl(tokenBytes[5]),
                          ntohl(tokenBytes[6]), ntohl(tokenBytes[7])];
    
    return hexToken;
}


NSData * _runCryptor(CCCryptorRef cryptor ,CCCryptorStatus *status ,NSData* data)
{
    size_t bufsize = CCCryptorGetOutputLength( cryptor, (size_t)[data length], true );
    void * buf = malloc( bufsize );
    size_t bufused = 0;
    size_t bytesTotal = 0;
    *status = CCCryptorUpdate( cryptor, [data bytes], (size_t)[data length],
                              buf, bufsize, &bufused );
    if ( *status != kCCSuccess )
    {
        free( buf );
        return ( nil );
    }
    
    bytesTotal += bufused;
    
    // From Brent Royal-Gordon (Twitter: architechies):
    //  Need to update buf ptr past used bytes when calling CCCryptorFinal()
    *status = CCCryptorFinal( cryptor, buf + bufused, bufsize - bufused, &bufused );
    if ( *status != kCCSuccess )
    {
        free( buf );
        return ( nil );
    }
    
    bytesTotal += bufused;
    
    return ( [NSData dataWithBytesNoCopy: buf length: bytesTotal] );
}
            
static NSData *decryptedDataUsingAlgorithm_f(CCAlgorithm algorithm
                                      , id key		// data or string
                                      , id iv		// data or string
                                      , CCOptions options
                                      , CCCryptorStatus * error
                                      ,NSData *data)
{
	CCCryptorRef cryptor = NULL;
	CCCryptorStatus status = kCCSuccess;
	
	//NSParameterAssert([key isKindOfClass: [NSData class]] || [key isKindOfClass: [NSString class]]);
	//NSParameterAssert(iv == nil || [iv isKindOfClass: [NSData class]] || [iv isKindOfClass: [NSString class]]);
	
	NSMutableData * keyData, * ivData;
	if ( [key isKindOfClass: [NSData class]] )
		keyData = (NSMutableData *) [key mutableCopy];
	else
		keyData = [[key dataUsingEncoding: NSUTF8StringEncoding] mutableCopy];
	
	if ( [iv isKindOfClass: [NSString class]] )
		ivData = [[iv dataUsingEncoding: NSUTF8StringEncoding] mutableCopy];
	else
		ivData = (NSMutableData *) [iv mutableCopy];	// data or nil
	
//	[keyData autorelease];
//	[ivData autorelease];
	
	// ensure correct lengths for key and iv data, based on algorithms
	FixKeyLengths( algorithm, keyData, ivData );
	
	status = CCCryptorCreate( kCCDecrypt, algorithm, options,
                             [keyData bytes], [keyData length], [ivData bytes],
                             &cryptor );
	
	if ( status != kCCSuccess )
	{
		if ( error != NULL )
			*error = status;
		return ( nil );
	}
	
	NSData * result = _runCryptor(cryptor , &status ,data);
	if ( (result == nil) && (error != NULL) )
		*error = status;
	
	CCCryptorRelease( cryptor );
	
	return ( result );
}



NSError * errorWithCCCryptorStatus(CCCryptorStatus status)
{
    NSString * description = nil, * reason = nil;
    
    switch ( status )
    {
        case kCCSuccess:
            description = NSLocalizedString(@"Success", @"Error description");
            break;
            
        case kCCParamError:
            description = NSLocalizedString(@"Parameter Error", @"Error description");
            reason = NSLocalizedString(@"Illegal parameter supplied to encryption/decryption algorithm", @"Error reason");
            break;
            
        case kCCBufferTooSmall:
            description = NSLocalizedString(@"Buffer Too Small", @"Error description");
            reason = NSLocalizedString(@"Insufficient buffer provided for specified operation", @"Error reason");
            break;
            
        case kCCMemoryFailure:
            description = NSLocalizedString(@"Memory Failure", @"Error description");
            reason = NSLocalizedString(@"Failed to allocate memory", @"Error reason");
            break;
            
        case kCCAlignmentError:
            description = NSLocalizedString(@"Alignment Error", @"Error description");
            reason = NSLocalizedString(@"Input size to encryption algorithm was not aligned correctly", @"Error reason");
            break;
            
        case kCCDecodeError:
            description = NSLocalizedString(@"Decode Error", @"Error description");
            reason = NSLocalizedString(@"Input data did not decode or decrypt correctly", @"Error reason");
            break;
            
        case kCCUnimplemented:
            description = NSLocalizedString(@"Unimplemented Function", @"Error description");
            reason = NSLocalizedString(@"Function not implemented for the current algorithm", @"Error reason");
            break;
            
        default:
            description = NSLocalizedString(@"Unknown Error", @"Error description");
            break;
    }
    
    NSMutableDictionary * userInfo = [[NSMutableDictionary alloc] init];
    [userInfo setObject: description forKey: NSLocalizedDescriptionKey];
    
    if ( reason != nil )
        [userInfo setObject: reason forKey: NSLocalizedFailureReasonErrorKey];
    
    NSError * result = [NSError errorWithDomain: @"CommonCryptoErrorDomain" code: status userInfo: userInfo];
//    [userInfo release];
    
    return ( result );
}
NSData *decryptedDataUsingAlgorithm( CCAlgorithm algorithm ,id key ,CCOptions options ,CCCryptorStatus * error ,NSData *data)
{
    return ( decryptedDataUsingAlgorithm_f( algorithm , key , nil ,options , error ,data));
}
NSData *decryptedAES256DataUsingKey(id key ,NSError **  error,NSData *data)
{
            CCCryptorStatus status = kCCSuccess;
            NSData * result = decryptedDataUsingAlgorithm( kCCAlgorithmAES128 , key , kCCOptionPKCS7Padding , &status ,data);
                
            if ( result != nil )
                    return ( result );
                
            if ( error != NULL )
                    *error = errorWithCCCryptorStatus(status);
                
            return ( nil );
        }
#pragma mark decodeAES256
NSString *decodeAES256(NSString *base64EncodedString ,NSString* password){
    NSData *encryptedData = base64DataFromNString(base64EncodedString);
    NSData *decryptedData = decryptedAES256DataUsingKey([password dataUsingEncoding:NSUTF8StringEncoding] ,nil ,encryptedData);
    return [[NSString alloc] initWithData:decryptedData encoding:NSUTF8StringEncoding];
}
