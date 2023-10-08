package com.panda.sport.merchant.manage.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.BonusRecordForm;
import com.panda.sport.merchant.manage.feign.BonusRecordClient.BonusRecordClientHystrix;

import lombok.extern.slf4j.Slf4j;

@FeignClient(value = "panda-bss-usercenter", fallback = BonusRecordClientHystrix.class)
@SuppressWarnings("all")
public interface BonusRecordClient {

	/**
     * 财务中心/对账工具-开始对账
     * @param queryReqVO
     * @return CheckToolsQueryRespVO
     */
	@PostMapping(value = "/manage/bonusRecord/reissueBonus")
	Response reissueBonus(@RequestBody BonusRecordForm bonusRecordForm,@RequestParam(value = "merchantCode") String merchantCode);

    @Slf4j
    @Component
    public class BonusRecordClientHystrix implements BonusRecordClient{

    	@Override
		public Response reissueBonus(BonusRecordForm bonusRecordForm,String merchantCode) {
			 log.error(" 奖券补发异常 ");
			return Response.returnFail("奖券补发异常  请求超时了");
		}


    }
}
