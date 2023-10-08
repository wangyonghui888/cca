package com.panda.sport.merchant.common.utils;

import org.apache.http.impl.client.BasicCookieStore;

public class CookieHolder {

	private Long addTime;

	private BasicCookieStore cookies;

	public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	public BasicCookieStore getCookies() {
		return cookies;
	}

	public void setCookies(BasicCookieStore cookies) {
		this.cookies = cookies;
	}

}
