package com.panda.sport.merchant.api.aop;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.common.utils.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestLimitHand {

    public static Environment environment = SpringUtil.getBean(Environment.class);

    public static final boolean pass = false;
    public static final boolean forbid = true;
    
    private static Cache<String, Integer> localCache = build(1);
	 
    private static Cache<String, Integer> build(long expire) {
        return Caffeine.newBuilder()
                //.maximumSize(capacity)
                .expireAfterWrite(expire, TimeUnit.SECONDS)
                .recordStats()
                .build();
    }
    
    /**
     * 判断是否已经登录
     */
    public static boolean requestLimit(String requestUrl,String merchantCode,String userName) {
        try {
        	
        	String limitOpen = environment.getProperty("rate.limit.open.switch");
        	
        	if("off".equals(limitOpen)) {
        		log.info(" 已经关闭接口限频：{} ",limitOpen);
        		return pass;
        	}
        	
        	requestUrl = requestUrl.replace("/", "_").replaceFirst("_", "");
            String property = environment.getProperty("rate.limit."+requestUrl+"_switch");

            if(!"on".equals(property))  return pass;
            
            Boolean openLocalLimit = environment.getProperty("rate.limit.openLocalLimit",Boolean.class,false);
            
            if(openLocalLimit) {
            	log.info(" 开启本地限频  {}",requestUrl);
            	if(localLimit(requestUrl,merchantCode)) return forbid;
            	return pass;
            }
            
            if(StringUtils.isNotBlank(userName)) {
            	
            	int limit = environment.getProperty("rate.limit."+requestUrl+"_rate",Integer.class,3);
                
                int range = environment.getProperty("rate.limit."+requestUrl+"_range",Integer.class,1);

                String userRateRange = environment.getProperty("rate.limit."+requestUrl+"_user_rate_range");
                //用户级特殊限频
                Map<String, Object> map = getMerthodLimit(userRateRange, merchantCode);

                if(map != null) {
                	limit = (int) map.get("limit");
                	range = (int) map.get("range");
                }
                
                String apiKey = "userLevel-"+requestUrl + "-"+merchantCode +"_"+userName;
                
                if(limit(limit, range, apiKey,merchantCode)) return forbid;
                //用户登录过期检测，不过期就不检测商户和接口限频
                if(UserCheckHand.checkOK(requestUrl,merchantCode +"_"+userName)) return pass;
            }
            
            //触发商户限频
            if(merchantLimit(requestUrl,merchantCode)) return forbid;  

            //触发接口限频
            if(interfaceLimit(requestUrl,merchantCode)) return forbid;
            
            return pass;
        } catch (Exception e) {
        	log.error("限流异常，执行本地限流策略", e);
        	if(localLimit(requestUrl,merchantCode)) return forbid;
        }
        return pass;
    }
    /**
     * 商户限频
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param requestUrl
    * @param merchantCode
    * @return
    * @author: Star
    * @Date: 2022-11-30 3:11:57
     */
	private static boolean merchantLimit(String requestUrl, String merchantCode) {
		int limit = 0,range = 0;
		
		Integer rate = environment.getProperty("rate.limit."+requestUrl+"_merchant_rate",Integer.class);

		Integer rangeStr = environment.getProperty("rate.limit."+requestUrl+"_merchant_range",Integer.class);

		//商户通用配置
		if( rate != null && rangeStr != null) {
			limit = rate;
			range = rangeStr;
		}
		
		String merchantConfig = environment.getProperty("rate.limit."+requestUrl+"_merchant_rate_range");
		//商户独立配置
		Map<String, Object> merthodLimit = getMerthodLimit(merchantConfig, merchantCode);
		if(merthodLimit != null) {
			 limit = (int) merthodLimit.get("limit");
			 range = (int) merthodLimit.get("range");
			 
		}
		String apiKey = "merchantLevel-"+requestUrl +"_"+ merchantCode;
		
		if(limit(limit, range, apiKey,merchantCode)) return forbid;
		
		return pass;
	}
	 
	/**
	 * 本地缓存限频
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param requestUrl
	* @return
	* @author: Star
	* @Date: 2022-11-5 16:31:03
	*/
	private static boolean localLimit(String requestUrl,String merchantCode) {
		 
		int limit = environment.getProperty("rate.limit.all.interface.rate",Integer.class,50);
		
		Integer rate = environment.getProperty("rate.limit."+requestUrl+"_local_rate",Integer.class);

		Integer rangeStr = environment.getProperty("rate.limit."+requestUrl+"_local_range",Integer.class);
		
		if(rangeStr != null && rate != null) {
			limit = rate;
			if(rangeStr > 1) limit = limit / rangeStr;
		}
		
		Integer count = localCache.get(requestUrl, (key)->{
			return 0;
		});

		if(count + 1 >= limit) {
	    	log.info("rateLimitHit-localLimit-{} 触发本地限频，到达限流次数:{},限制频次为:{}, 商户:{},",requestUrl,count,limit,merchantCode);
			 return true;
		}
		
		localCache.put(requestUrl, count + 1);
		
		return false;
	}

	/**
	 * 接口级别限频
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param requestUrl
	* @param userPO
	* @return
	* @author: Star
	* @Date: 2022-11-5 16:02:43
	*/
	private static  boolean interfaceLimit(String requestUrl,String merchantCode) {
		 
		int limit = 0,range = 0;
		
		Integer rate = environment.getProperty("rate.limit."+requestUrl+"_interface_rate",Integer.class);

		Integer rangeStr = environment.getProperty("rate.limit."+requestUrl+"_interface_range",Integer.class);

		//商户通用配置
		if(rate != null && rangeStr != null) {
			limit = rate;
			range = rangeStr;
		}
        return limit(limit, range, "interfaceLevel-"+requestUrl,merchantCode);
	}

    
	public static boolean limit( int limit, int range, String apiKey,String merchantCode) {
		
		if(limit == 0 && range == 0) return false;
		
		RedisTemp RedisLimitUtil = SpringUtil.getBean(RedisTemp.class);
		
		String c = RedisLimitUtil.get(apiKey);
		
		String script = "local count = redis.call('incr',KEYS[1]) if count == 1 then  redis.call('expire',KEYS[1] , " +
		        "ARGV[1]) end  local ttlTime = redis.call('ttl',KEYS[1]) if ttlTime == -1 then  redis.call('expire'," +
		        "KEYS[1] , ARGV[1]) end  return count ";
		if (c == null) c = "0";
		
		int currentCount = Integer.valueOf(c);
		
		log.info("rateLimitStart:{},currentCount:{},limit:{},商户:{} ",apiKey, (currentCount + 1), limit,merchantCode);
		if ((currentCount + 1) > limit) {
		    
			Long expire = RedisLimitUtil.getExpire(apiKey, TimeUnit.SECONDS);
		   
		    if(expire <= range) {
		    	log.info("rateLimitHit{} 到达限流次数:{},限制频次为:{},剩余时间:{}秒,商户:{} ",apiKey,currentCount,limit,expire,merchantCode);
		    	return true;
		    }else {
		    	//配置时间修改变短后
		    	script = " redis.call('incr',KEYS[1])  redis.call('expire',KEYS[1] , " +
		                "ARGV[1])  local ttlTime = redis.call('ttl',KEYS[1]) if ttlTime == -1 then  redis.call('expire'," +
		                "KEYS[1] , ARGV[1]) end  ";
		    	
		    }
		}
		RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
		RedisLimitUtil.execute(redisScript, apiKey, range+"");
		return false;
	}
  
	private static Map<String,Object> getMerthodLimit(String merchantConfig, String merchantCode){
    	Map<String,Object> map = null;
    	if(StringUtils.isNotBlank(merchantConfig) && merchantConfig.contains(merchantCode)) {
	       	 String rateRang = "";
	       	 String[] split = merchantConfig.split(";");
	       	 for (String s : split) {
					 if(s.contains(merchantCode)) {
						 rateRang = s;
						 break;
					 }
				}
	       	map = new HashMap<>();
	       	String[] split2 = rateRang.split("_");
	       	map.put("limiTarget", split2[0]);
	       	map.put("limit", Integer.valueOf(split2[1]));
	       	map.put("range", Integer.valueOf(split2[2]));
	       
        }
    	return map;
    }
    
}



 


