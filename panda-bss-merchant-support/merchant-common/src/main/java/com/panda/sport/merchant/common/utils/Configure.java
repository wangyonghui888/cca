package com.panda.sport.merchant.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @ClassName: Configure
 * @Description: 读取properties文件的工具类
 * @author chuntu tuchun168@163.com
 * @date 2016年5月3日 上午11:22:57
 *
 */
public class Configure {
	private Configure() {

	}

	private static Configure configure;

	public synchronized static Configure getInstance() {
		if (configure == null) {
			configure = new Configure();
		}
		return configure;
	}

	public String getProperties(String sign) {
		InputStream in = getClass().getResourceAsStream(
				"/application.properties");
		Properties prop = new Properties();
		String properties = "";
		try {
			prop.load(in);
			properties = prop.getProperty(sign).toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return properties;
	}

	public static String getPropertyBykey(String key) {
		Configure configure = Configure.getInstance();
		return configure.getProperties(key);
	}

}