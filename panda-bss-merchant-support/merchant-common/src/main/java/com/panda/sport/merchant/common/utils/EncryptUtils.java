package com.panda.sport.merchant.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description : 加密解密工具类
 * @Date: 2019-08-29 下午2:11:47
 */
public class EncryptUtils {

	public static String hash(String algorithm, String origin) {
		if (origin == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(origin.getBytes());
			return StringUtil.byteArrayToHexString(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * MD5加密
	 * @param origin
	 * @return
	 */
	public static String hashByMD5(String origin) {
		return hash("MD5", origin);
	}

	/**
	 * SHA1加密
	 * @param origin
	 * @return
	 */
	public static String hashBySHA1(String origin) {
		return hash("SHA-1", origin);
	}

	/**
	 * BASE64加密
	 * @param origin  明文
	 * @param charset 字符编码
	 * @return
	 */
	public static String encodeByBase64(String origin, String charset) {
		return Base64.encodeBase64String(origin.getBytes(Charset.forName(charset)));
	}

	/**
	 * BASE64解密
	 * @param encrypt  密文
	 * @param charset  字符编码
	 * @return
	 */
	public static String decodeFromBase64(String encrypt, String charset) {
		return new String(Base64.decodeBase64(encrypt), Charset.forName(charset));
	}
	
	public static String encodeByBase64(String origin) {
		return encodeByBase64(origin, "UTF-8");
	}
	
	public static String decodeFromBase64(String encrypt) {
		return decodeFromBase64(encrypt, "UTF-8");
	}


	/**
	 * 密码加密
	 * @param password
	 * @return
	 */
	public static String encryptMd5(String password){
		return  DigestUtils.md5DigestAsHex(password.getBytes());
	}
}
