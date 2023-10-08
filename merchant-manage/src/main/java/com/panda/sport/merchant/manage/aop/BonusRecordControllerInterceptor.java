/*
package com.panda.sport.merchant.manage.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.panda.sport.merchant.manage.entity.form.BonusRecordForm;
import com.panda.sport.merchant.manage.feign.BonusRecordClient;

*//**
	* 拦截器：验签 会话管理
	*
	* @author valar so handsome
	*/
/*

@Aspect
@Component
public class BonusRecordControllerInterceptor {


@Autowired
BonusRecordClient bonusRecordClient;

 *//**
	* 定义拦截规则：拦截controller包下面的所有类。
	*//*
		@Pointcut("execution(* com.panda.sport.merchant.manage.controller.BonusRecordController.reissueBonus(..))")
		public void controllerMethodPointcut() {
		}
		
		@Around("controllerMethodPointcut()") //指定拦截器规则
		public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
		BonusRecordForm bonusRecordForm = null;
		Object[] args = pjp.getArgs();
		for (Object object : args) {
			if (object instanceof BonusRecordForm) {
				bonusRecordForm = (BonusRecordForm) object;
			}
		}
		if(bonusRecordForm != null) {
			 HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
			 bonusRecordForm.setAdminName(request.getHeader("merchantName"));
			 
			 Object reissueBonus = bonusRecordClient.reissueBonus(bonusRecordForm,bonusRecordForm.getMerchantCode());
			  
			 return  reissueBonus;
		}
		 return pjp.proceed();
		}
		
		
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		*/