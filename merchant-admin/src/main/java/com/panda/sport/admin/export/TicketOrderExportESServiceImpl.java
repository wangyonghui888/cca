package com.panda.sport.admin.export;


import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import com.panda.sport.merchant.common.vo.merchant.TicketOrderSettle;
import com.panda.sport.merchant.common.vo.merchant.TicketResponseVO;
import com.panda.sport.order.service.expot.AbstractTicketOrderExportESServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.REALTIME_TABLE;


/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  注单导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("ticketOrderExportESServiceImpl")
@Slf4j
public  class TicketOrderExportESServiceImpl extends AbstractTicketOrderExportESServiceImpl{

    @Autowired
    private MerchantReportClient merchantReportClient;

    @Override
    @Async()
    public void export(MerchantFile merchantFile) {
        this.execute(merchantFile);
    }


    @Override
    public List<TicketOrderSettle> processQuery(String filter, BetOrderVO betOrderVO) {
        Long beginTime = System.currentTimeMillis();
        TicketResponseVO result = merchantReportClient.queryTicketList(betOrderVO);

        List<TicketOrderSettle> betOrderPOList =  result.getList();
        List<OrderSettle> userMerchantPOList = null;
        if(betOrderVO.getDatabaseSwitch().equals(REALTIME_TABLE) &&  CollectionUtils.isNotEmpty(betOrderPOList)){
            Set<Long> uidList = betOrderPOList.stream().map(e -> Long.parseLong(e.getUid())).collect(Collectors.toSet());
            userMerchantPOList = mergeOrderMixMapper.queryUserMerchantPOList(uidList);
        }
        Long emdTimed= System.currentTimeMillis();
        log.info("导出查询sql耗时：{}" + (emdTimed-beginTime));
        //组装betOrderPOList
        if(CollectionUtils.isNotEmpty(userMerchantPOList)){
            betOrderPOList= assembleBetOrderList(betOrderPOList,userMerchantPOList);
        }
        return betOrderPOList;
    }
}
