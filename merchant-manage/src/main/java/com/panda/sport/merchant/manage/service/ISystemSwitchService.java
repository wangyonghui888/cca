package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.vo.SystemSwitchVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ISystemSwitchService {

    int updateSystemSwitch(SystemSwitchVO systemSwitchVO, HttpServletRequest request);

    List<SystemSwitchVO> querySystemSwitch();
}
