package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.bss.mapper.SystemSwitchMapper;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.SystemSwitchVO;
import com.panda.sport.merchant.manage.feign.MerchantApiClient;
import com.panda.sport.merchant.manage.service.ISystemSwitchService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SystemSwitchServiceImpl implements ISystemSwitchService {

    @Autowired
    private SystemSwitchMapper systemSwitchMapper;

    @Autowired
    public MerchantApiClient merchantApiClient;

    @Autowired
    private MerchantLogService merchantLogService;

    /**
     * 系统配置key值
     */
    private static final String SWITCH_KEY = "chatRoomSwitch";
    private static final String CONFIG_KEY = "pullMsgRate";
    private static final String MERCHANT_EVENT_KEY = "merchantEventSwitch";

    @Override
    public int updateSystemSwitch(SystemSwitchVO systemSwitchVO, HttpServletRequest request) {
        Long currentTime = System.currentTimeMillis();
        systemSwitchVO.setUpdateTime(currentTime);
        int num = systemSwitchMapper.updateSystemSwitch(systemSwitchVO);
        if(systemSwitchVO.getPullMsgRate() != null
                && StringUtils.isNotEmpty(systemSwitchVO.getConfigKey())
                && systemSwitchVO.getConfigKey().equals(SWITCH_KEY)){
            SystemSwitchVO switchVO = new SystemSwitchVO();
            switchVO.setConfigKey(CONFIG_KEY);
            switchVO.setConfigValue(String.valueOf(systemSwitchVO.getPullMsgRate()));
            switchVO.setPullMsgRate(systemSwitchVO.getPullMsgRate());
            switchVO.setUpdateTime(currentTime);
            switchVO.setUpdateBy(systemSwitchVO.getUpdateBy());
            systemSwitchMapper.updateConfigValue(switchVO);
            merchantApiClient.updateMerchantChatSwitch(switchVO);
        }
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        if(systemSwitchVO.getId() ==4L ){
            vo.getFieldName().add("聊天室状态");
            vo.getBeforeValues().add(systemSwitchVO.getConfigValue().equals("0") ? "开" : "关");
            vo.getAfterValues().add(systemSwitchVO.getConfigValue().equals("0") ? "关" : "开");
            merchantLogService.saveLog(MerchantLogPageEnum.CHATROOM_SWITCH, MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                    MerchantLogConstants.MERCHANT_IN,request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"), "", systemSwitchVO.getId().toString(),request.getHeader("language") , IPUtils.getIpAddr(request));
        }
        if(systemSwitchVO.getId() ==3L ){
            vo.getFieldName().add("视频流量管控状态");
            vo.getBeforeValues().add(systemSwitchVO.getConfigValue().equals("0") ? "开" : "关");
            vo.getAfterValues().add(systemSwitchVO.getConfigValue().equals("0") ? "关" : "开");
            merchantLogService.saveLog(MerchantLogPageEnum.SYSTEM_SWITCH, MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                    MerchantLogConstants.MERCHANT_IN,request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"), "", systemSwitchVO.getId().toString(),request.getHeader("language") , IPUtils.getIpAddr(request));

        }
        if (systemSwitchVO.getId() == 15L) {
            SystemSwitchVO switchVO = new SystemSwitchVO();
            switchVO.setConfigKey(MERCHANT_EVENT_KEY);
            switchVO.setConfigValue(systemSwitchVO.getConfigValue());
            switchVO.setUpdateTime(currentTime);
            switchVO.setUpdateBy(systemSwitchVO.getUpdateBy());
            systemSwitchMapper.updateConfigValue(switchVO);
            merchantApiClient.updateMerchantChatSwitch(switchVO);
        }
       return num;
    }

    @Override
    public List<SystemSwitchVO> querySystemSwitch() {
        List<SystemSwitchVO> list = systemSwitchMapper.querySystemSwitch();
        Map<String, SystemSwitchVO> switchMap = list.stream().collect(Collectors.toMap(SystemSwitchVO::getConfigKey, v ->v,(k1,k2)->k1));
        List<SystemSwitchVO> resultList = Lists.newArrayList();
        for (SystemSwitchVO vo : list ){
            if(vo.getConfigKey().equals(SWITCH_KEY)){
                switchMap.forEach((key, value)->{
                    if(key.equals(CONFIG_KEY)){
                        vo.setPullMsgRate(Integer.valueOf(value.getConfigValue()));
                    }
                });
            }
            if(!vo.getConfigKey().equals(CONFIG_KEY)){
                resultList.add(vo);
            }
        }
        return resultList;
    }
}
