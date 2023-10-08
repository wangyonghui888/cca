package com.panda.sport.admin.service;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import com.panda.sport.merchant.common.vo.merchant.AbnormalVo;
import com.panda.sport.merchant.common.vo.user.MerchantComboVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AbnormalOutService {

    /**
     * 异常会员名单查询
     */
    Response<?> queryAbnormalList(AbnormalVo abnormalVo, String language);

    /**
     * 异常会员名单icon统计查询
     */
    Response<?> queryAbnormalCount(AbnormalVo abnormalVo, String language);

    /**
     * 异常会员名单icon点击更新新时间
     */
    Response<?> updateAbnormalClickTime(String merchantCode,Long abnormalClickTime);

    /**
     * 异常会员名单导出
     */
    void exportAbnormalStatistic(HttpServletResponse response, HttpServletRequest request, AbnormalVo vo, String language) throws Exception;

    /**
     * 异常用户查询
     */
    Response<?> queryAbnormalUserList(AbnormalUserVo abnormalUserVo, String language);

    /**
     * 异常用户查询导出
     */
    void exportAbnormalUserStatistic(HttpServletResponse response, HttpServletRequest request, AbnormalUserVo vo, String language) throws Exception;

    List<MerchantComboVO> findUserComboList();
}
