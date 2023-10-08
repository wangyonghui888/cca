package com.panda.sport.merchant.manage.util;

import com.panda.sport.merchant.common.utils.SpringUtil;


public class IDUtils {
	
    private static IdGeneratorFactory idGeneratorFactory = SpringUtil.getBean(IdGeneratorFactory.class) ;
	
	public static long getId(String key) {
		
		String generateIdByBussiness = idGeneratorFactory.generateIdByBussiness(key, 5);
		
		return Long.valueOf(generateIdByBussiness);
	 
	}
	
}
