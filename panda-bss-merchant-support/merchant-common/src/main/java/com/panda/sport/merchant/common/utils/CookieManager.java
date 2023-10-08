package com.panda.sport.merchant.common.utils;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieManager {

	private static Map<String, CookieHolder> storeMap = new HashMap<String, CookieHolder>();

	public static BasicCookieStore getCookieStore(String shipcomCode) {
		CookieHolder ch = storeMap.get(shipcomCode);
		if (ch != null) {
			// 定义小于一小时之内的Cookie可重用
			if (System.currentTimeMillis() - ch.getAddTime() <= 3600000L) {
				return ch.getCookies();
			}
		}
		return null;
	}

	public static List<Cookie> getCookies(String shipcomCode) {
		BasicCookieStore bcs = getCookieStore(shipcomCode);
		if (bcs != null) {
			return bcs.getCookies();
		}
		return null;
	}

	public static void setCookieStore(String shipcomCode, BasicCookieStore bcs) {
		if (bcs != null) {
			CookieHolder ch = new CookieHolder();
			ch.setAddTime(System.currentTimeMillis());
			ch.setCookies(bcs);
			storeMap.put(shipcomCode, ch);
		}
	}

	public static void setCookies(String shipcomCode, List<Cookie> cookies) {
		if (cookies != null) {
			BasicCookieStore bcs = new BasicCookieStore();
			bcs.addCookies((Cookie[]) cookies.toArray());
			setCookieStore(shipcomCode, bcs);
		}
	}

}
