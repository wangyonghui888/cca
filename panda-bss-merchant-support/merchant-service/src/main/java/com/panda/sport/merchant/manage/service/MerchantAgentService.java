package com.panda.sport.merchant.manage.service;

import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantAgentPageVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantAgentVO;

/**
 * @author javier
 * @date 2020/12/15
 * @description 代理商设置
 */
public interface MerchantAgentService {
    /**
     *
     * todo 根据代理商Id查询下级渠道商户
     * @param merchantAgentPageVO
     *  1、如果没有传代理商id（merchantId），则查询所有未绑定代理商的渠道商户
     *  2、如果传代理商id（merchantId），则查询代理商下绑定了的渠道商户
     * @return Response<PageInfo<MerchantPO>>
     *     返回分页的对象集合
     */
    Response<PageInfo<MerchantPO>> listMerchant(MerchantAgentPageVO merchantAgentPageVO);

    /**
     * todo 批量绑定代理商下渠道商户
     *
     * @param merchantAgentVO 对象中的merchantIds字段为渠道商户ids，用逗号相隔
     * @param language
     * @param ipAddr
     * @return Response<ResponseEnum> 结果枚举
     */
    Response<ResponseEnum> bindMerchantByAgent(MerchantAgentVO merchantAgentVO, String language, String ipAddr);
}
