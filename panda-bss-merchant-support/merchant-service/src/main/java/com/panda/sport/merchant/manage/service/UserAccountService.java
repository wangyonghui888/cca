package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.vo.PageVO;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;

import java.io.IOException;
import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.manage.service
 * @Description :  用户账户变更与账变记录接口
 * @Date: 2020-09-11 15:24
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface UserAccountService {


    /**
     * 交易记录查询
     *
     * @param findVO
     * @return
     */
    PageVO queryTransferRecord(UserAccountFindVO findVO);

    /**
     * 需重试交易记录查询
     *
     * @param findVO
     * @return
     */
    PageVO queryRetryTransferRecord(UserAccountFindVO findVO);

    /**
     * 交易记录导出
     *
     * @param findVO
     */
    Map transferRecordExport(String username, String merchantCode, UserAccountFindVO findVO, String language) throws IOException;

    /**
     * 账变记录查询
     *
     * @param findVO
     * @param language
     * @return
     */
    PageVO queryAccountHistory(UserAccountFindVO findVO, String language);

    /**
     * 账变记录导出
     *
     * @param findVO
     */
    Map accountHistoryExport(String username, String merchantCode, UserAccountFindVO findVO) throws IOException;
}
