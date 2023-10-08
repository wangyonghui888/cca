package com.panda.sport.admin.service;

import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.RcsUserConfigDetailVO;
import com.panda.sport.merchant.common.vo.RcsUserConfigParamVO;
import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javier
 * @date 2021/2/9
 * TODO 用户限额服务
 */
public interface RcsUserConfigLimitService {

    /**
     * 保存操盘后台设置的用户限额数据信息
     */
    void saveUserLimit(RcsUserConfigParamVO rcsUserConfigParamVo, String language, JwtUser user ,String ip) throws Exception;


    /**
     * 保存操盘后台设置的用户限额数据信息
     */
    RcsUserConfigDetailVO detail(UserPO userLimit);

    UserPO getUserLimit(String userId);

    Response addChangeRecordHistory(AccountChangeHistoryFindVO vo, HttpServletRequest request);
}
