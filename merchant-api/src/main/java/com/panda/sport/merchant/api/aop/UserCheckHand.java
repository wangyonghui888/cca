

package com.panda.sport.merchant.api.aop;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.env.Environment;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.common.utils.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserCheckHand { 

    public static Environment environment = SpringUtil.getBean(Environment.class);

    private static Cache<String, Long> localCache = build(2);
	 
    private static Cache<String, Long> build(long expire) {
        return Caffeine.newBuilder()
                .expireAfterWrite(expire, TimeUnit.HOURS)
                .recordStats()
                .build();
    }
    
    
    
    /**
     * 检查用户登录是否过时
     * @param requestUrl
     * @param userPO
     * @return
     * @throws Exception 
     */
	public static boolean checkOK(String requestUrl, String userId) throws Exception {

		String configDate = environment.getProperty("rate.limit."+requestUrl+"_config_date",String.class);
		
		Boolean checkLoginTime = environment.getProperty("rate.limit.user.checkLoginTime", Boolean.class,false);
		//默认不检测用户登录时间
		if(StringUtils.isNotBlank(configDate) && checkLoginTime) {
			
			log.info("configDate:{} {}",requestUrl,configDate);
			
			String userLoginKey = "rate.limit.user.login.check:"+userId;
			Long userLoginTime = localCache.getIfPresent(userLoginKey);
			if(userLoginTime == null) {
				RedisTemp RedisLimitUtil = SpringUtil.getBean(RedisTemp.class);
				String userLoginTime1 = RedisLimitUtil.get(userLoginKey);
				if(userLoginTime1 == null) {
					userLoginTime1 = System.currentTimeMillis()+"";
					//缓存两个小时
					RedisLimitUtil.setKey(userLoginKey, userLoginTime1, 60 * 60);
				}
				userLoginTime = Long.valueOf(userLoginTime1.toString());
				localCache.put(userLoginKey, userLoginTime);
			}
			
			Date parseDate = DateUtils.parseDate(configDate, "yyyy-MM-dd HH:mm:ss");
			 
			if(parseDate != null && userLoginTime < parseDate.getTime())  return true;
			
		}
		
		return false;
	}
    
}



 


