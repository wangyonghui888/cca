package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.po.merchant.MerchantRatePO;
import com.panda.sport.merchant.common.vo.merchant.MerchantRateVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : Jeffrey
 * @Date: 2020-01-23 18:37
 * @Description :费率表服务类
 */
public interface MerchantRateService extends BasService {

    /**
     * 查询费率列表
     */
    List<MerchantRateVO> queryList(MerchantRateVO merchantRateVO);

    /**
     * 新增修改
     */
    boolean add(MerchantRateVO merchantRateVO, HttpServletRequest request);

    /**
     * 新增修改
     */
    boolean update(MerchantRateVO merchantRateVO, Integer userId, String language ,String ip);

    List<MerchantRatePO> queryRateList();
}
