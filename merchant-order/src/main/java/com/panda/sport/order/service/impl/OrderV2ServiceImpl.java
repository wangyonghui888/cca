package com.panda.sport.order.service.impl;

import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.merchant.common.vo.UserOrderV2VO;
import com.panda.sport.order.service.AbstractOrderV2Service;
import com.panda.sport.order.service.OrderV2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RefreshScope
public class OrderV2ServiceImpl extends AbstractOrderV2Service implements OrderV2Service {

    private final int serviceType = 1;

    @Autowired
    private BackupTUserMapper tUserMapper;

    @Override
    public Map<String, Object> queryTicketList(UserOrderV2VO vo) throws Exception {
        vo.setServiceType(serviceType);
        return this.abstractQueryUserOrderList(vo);
    }

    @Override
    public List<Map<String, Object>> queryUserIdList(String merchantCode) {
        return tUserMapper.queryUserlogHistoryIDList(merchantCode);
    }
}
