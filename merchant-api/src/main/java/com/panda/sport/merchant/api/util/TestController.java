package com.panda.sport.merchant.api.util;


import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panda.sport.merchant.common.utils.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {

	@RequestMapping(value = "/get")
    public Object upsertUserBonus(String url) {

		Environment environment = SpringUtil.getBean(Environment.class);
		
		Map<String,Object> map = new HashMap<>();
        int limit = environment.getProperty(url+"_rate",Integer.class);
        int range = environment.getProperty(url+"_range",Integer.class);
        map.put("用户通用频率(频次/时间)	", limit+"/"+range);
        
        String _user_rate_range = environment.getProperty(url+"_user_rate_range",String.class);
        map.put("用户级特殊配置", _user_rate_range);
       
        String _merchant_rate = environment.getProperty(url+"_merchant_rate",String.class);
        
        String _merchant_range = environment.getProperty(url+"_merchant_range",String.class);

        map.put("商户通用配置(频次/时间)", _merchant_rate+"/"+_merchant_range);
        
        String _merchant_rate_range = environment.getProperty(url+"_merchant_rate_range",String.class);
        map.put("商户总量特殊配置", _merchant_rate_range);
        
        String _interface_rate = environment.getProperty(url+"_interface_rate",String.class);

        String _interface_range = environment.getProperty(url+"_interface_range",String.class);
        map.put("接口总量配置(频次/时间)", _interface_rate+"/"+_interface_range);
        
        return map;
        
	}

    

}
