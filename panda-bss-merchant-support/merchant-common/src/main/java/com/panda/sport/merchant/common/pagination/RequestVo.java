package com.panda.sport.merchant.common.pagination;

import java.io.Serializable;

/**
 */
public class RequestVo<T> implements Serializable {


	private static final long serialVersionUID = 3966683051054436720L;

	private T data;


	public T getData() {
		return data;
	}


	public void setData(T data) {
		this.data = data;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
