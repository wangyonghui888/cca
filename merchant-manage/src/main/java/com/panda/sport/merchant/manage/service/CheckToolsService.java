package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.vo.CheckToolsEditReqVO;
import com.panda.sport.merchant.common.vo.CheckToolsEditRespVO;
import com.panda.sport.merchant.common.vo.CheckToolsQueryReqVO;
import com.panda.sport.merchant.common.vo.CheckToolsQueryRespVO;

/**
 * @author :  ives
 * @Description :  财务中心-对账工具 服务
 * @Date: 2022-02-08 20:14
 */
public interface CheckToolsService {

    /**
     * 开始对账
     * @param queryReqVO
     * @return CheckToolsQueryRespVO
     */
    CheckToolsQueryRespVO checkFinance(CheckToolsQueryReqVO queryReqVO);

    /**
     * 校正对账数据
     * @param editReqVO
     * @return CheckToolsEditRespVO
     */
    CheckToolsEditRespVO editFinance(CheckToolsEditReqVO editReqVO);

    /**
     * 用户级别开始对账
     * @param queryReqVO
     * @return CheckToolsQueryRespVO
     */
    CheckToolsQueryRespVO checkUserFinance(CheckToolsQueryReqVO queryReqVO) throws Exception;

    /**
     * 用户级别校正对账数据
     * @param editReqVO
     * @return CheckToolsEditRespVO
     */
    CheckToolsEditRespVO editUserFinance(CheckToolsEditReqVO editReqVO);
}
