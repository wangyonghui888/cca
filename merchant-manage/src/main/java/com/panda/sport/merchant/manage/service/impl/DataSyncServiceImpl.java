package com.panda.sport.merchant.manage.service.impl;


import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.feign.PandaReportTaskClient;
import com.panda.sport.merchant.manage.service.DataSyncService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RefreshScope
@Service("dataSyncService")
public class DataSyncServiceImpl implements DataSyncService {

    @Autowired
    private PandaReportTaskClient reportTaskClient;

    @Autowired
    private MerchantLogService merchantLogService;

    /**
     * 对账单数据同步
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
   public Response<?> callExecuteFinance(HttpServletRequest request, String startDate, String endDate) {
        try {
            Response resp = reportTaskClient.executeFinance(startDate,endDate);
            MerchantLogPageEnum pageEnum = MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_55;
            MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT_INFO;
            String name = MerchantFieldUtil.FIELD_MAPPING.get("synchronizationBill");
            extractedSaveLog(request,name,pageEnum,typeEnum);
            return resp;
        } catch (Exception e) {
            log.error("DataSyncController.callExecuteFinance,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    private void extractedSaveLog(HttpServletRequest request,String name,MerchantLogPageEnum pageEnum,MerchantLogTypeEnum typeEnum) {
        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(name);
        vo.getBeforeValues().add("-");
        vo.getAfterValues().add("-");
        merchantLogService.saveLog(pageEnum, typeEnum, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, username, userId, language, ip);
    }

    /**
     * 用户投注统计数据同步
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Response<?> callExecuteUserBet(HttpServletRequest request,String startDate, String endDate) {
        try {
            Response resp = reportTaskClient.executeUserBet(startDate, endDate);

            MerchantLogPageEnum pageEnum = MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_56;
            MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT_INFO;
            String name = MerchantFieldUtil.FIELD_MAPPING.get("synchronizationOrder");
            extractedSaveLog(request, name, pageEnum, typeEnum);
            return resp;

        } catch (Exception e) {
            log.error("DataSyncController.callExecuteUserBet,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 赛事投注统计数据同步
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Response<?> callExecuteMatchBet(HttpServletRequest request,String startDate, String endDate) {
        try {

            Response resp =reportTaskClient.executeMatchBet(startDate,endDate);
            MerchantLogPageEnum pageEnum = MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_57;
            MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT_INFO;
            String name = MerchantFieldUtil.FIELD_MAPPING.get("synchronizationMatch");
            extractedSaveLog(request,name,pageEnum,typeEnum);
            return resp;
        } catch (Exception e) {
            log.error("DataSyncController.callExecuteMatchBet,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }

    }

}
