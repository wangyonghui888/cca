package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.backup.mapper.BackupRcsUserSpecialBetLimitConfigMapper;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.merchant.common.enums.UserLimitEnum;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.vo.RcsUserConfigDetailVO;
import com.panda.sport.merchant.manage.service.RcsUserConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author javier
 * @date 2021/2/9
 *  TODO 用户限额服务
 */
@Service
@Slf4j
public class RcsUserConfigServiceImpl implements RcsUserConfigService {
    @Resource
    private BackupTUserMapper tUserMapper;
    @Resource
    private BackupRcsUserSpecialBetLimitConfigMapper backupRcsUserSpecialBetLimitConfigMapper;

    @Override
    public UserPO getUserLimit(String userId) {
        return tUserMapper.getUserLimitInfo(userId);
    }


    @Override
    public RcsUserConfigDetailVO detail(UserPO userInfo) {
        RcsUserConfigDetailVO result = new RcsUserConfigDetailVO();
        result.setUserId(String.valueOf(userInfo.getUserId()));
        result.setSpecialBettingLimitType(userInfo.getSpecialBettingLimitType());
        result.setSpecialBettingLimitTime(userInfo.getSpecialBettingLimitTime());
        result.setSpecialBettingLimitRemark(userInfo.getSpecialBettingLimitRemark());
        if(null==userInfo.getSpecialBettingLimitType() || UserLimitEnum.LIMIT_TYPE_1.getCode().equals(userInfo.getSpecialBettingLimitType())){
            return result;
        }
        result.setRcsUserSpecialBetLimitConfigList(backupRcsUserSpecialBetLimitConfigMapper.selectListByUserIdAndType(userInfo.getUserId().toString(),userInfo.getSpecialBettingLimitType()));
        return result;
    }
}
