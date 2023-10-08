package com.panda.sport.order.service;


import com.panda.sport.merchant.common.bo.UserInfoBO;
import com.panda.sport.merchant.common.vo.user.UserLogHistoryVO;

import java.util.List;

/**
 * 
 */
public interface UserLogHistoryService {

    List<UserLogHistoryVO> queryHistory(UserLogHistoryVO historyVO);

    void collectIp();

    void preLoadUserLogin(String s);

    void modifyUserProperties(String merchantCode, List<UserInfoBO> uList);
}
