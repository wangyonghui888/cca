package com.panda.sport.order.controller;

import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import com.panda.sport.merchant.manage.service.UserAccountService;
import com.panda.sport.order.service.OrderV2Service;
import com.panda.sport.order.service.UserTransferService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.manage.controller
 * @Description :  账户交易查询
 * @Date: 2020-09-11 16:19
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/order/account")
public class UserAccountController {
    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserTransferService userTransferService;

    @Autowired
    private OrderV2Service orderV2Service;

    /**
     * 通过商户编码查询用户名称，uid
     */
    @PostMapping(value = "/queryUserIdList")
    public List<Map<String, Object>> queryUserIdList(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode) {
        log.info("order/user/queryUserIdList,通过商户编码查询用户名称，merchantCode:" + merchantCode);
        return orderV2Service.queryUserIdList(merchantCode);
    }

    /**
     * 查询交易记录
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findRecord")
    public Response findRecord(HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        findVO.setUsername(null);
        findVO.setMerchantName(null);
        PageVO pageVO = userAccountService.queryTransferRecord(findVO);
        if (pageVO != null && pageVO.getRecords() != null) {
            for (Object obj : pageVO.getRecords()) {
                TransferRecordVO vo = (TransferRecordVO) obj;
                vo.setMerchantName(vo.getMerchantCode());
                vo.setUserName(StringUtils.isNotEmpty(vo.getFakeName()) ? vo.getFakeName() : vo.getUserName());
            }
        }
        return Response.returnSuccess(pageVO);
    }


    /**
     * 查询失败交易记录
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findReTryRecord")
    public Response findReTryRecord(HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        findVO.setMerchantName(null);
        findVO.setUsername(null);
        PageVO pageVO = userAccountService.queryRetryTransferRecord(findVO);
        if (pageVO != null && pageVO.getRecords() != null) {
            for (Object obj : pageVO.getRecords()) {
                TransferRecordVO vo = (TransferRecordVO) obj;
                vo.setMerchantName(vo.getMerchantCode());
                vo.setUserName(StringUtils.isNotEmpty(vo.getFakeName()) ? vo.getFakeName() : vo.getUserName());
            }
        }
        return Response.returnSuccess(pageVO);
    }


    /**
     * 重新调用交易接口
     *
     * @param request
     * @param vo
     * @return
     */
    @PostMapping(value = "/retryTransfer")
    public Response retryTransfer(HttpServletRequest request, @RequestBody UserRetryTransferVO vo) {
        log.info("/order/account/retryTransfer:" + vo);
        return userTransferService.retryTransfer(vo);
    }

    /**
     * 交易记录导出
     */
    @RequestMapping(value = "/transferRecordExport")
    public Response transferRecordExport(HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        try {
            long time = findVO.getEndTime() - findVO.getStartTime();
            long dayMillisecond = 86400000L;
            if (time > dayMillisecond) {
                Map<String, Object> resultMap = new HashMap<>(2);
                resultMap.put("code", "0002");
                resultMap.put("msg", "请查询时间范围一天内的数据");
                return Response.returnSuccess(resultMap);
            }
            String name = request.getHeader("merchantName");
            if (StringUtils.isEmpty(name)) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return Response.returnSuccess(userAccountService.transferRecordExport(name, null, findVO, language));
        } catch (Exception e) {
            log.error("UserAccountController.transferRecordExport error!", e);
            return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 查询账变记录
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findAccountHistory")
    public Response findAccountHistory(HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        PageVO pageVO = userAccountService.queryAccountHistory(findVO, language);
        if (pageVO != null && pageVO.getRecords() != null) {
            for (Object obj : pageVO.getRecords()) {
                AccountChangeHistoryFindVO vo = (AccountChangeHistoryFindVO) obj;
                vo.setMerchantName(vo.getMerchantCode());
                vo.setFakeName(StringUtils.isNotEmpty(vo.getFakeName()) ? vo.getFakeName() : vo.getUsername());
                //处理手动加款ip
                if (StringUtils.isNotEmpty(vo.getRemark()) && vo.getRemark().contains("手动加扣款操作人IP:")) {
                    vo.setIpAddress(vo.getRemark().substring(vo.getRemark().indexOf("IP:") + 3).trim());
                }
            }
        }
        return Response.returnSuccess(pageVO);
    }

    /**
     * 账变记录导出
     */
    @RequestMapping(value = "/accountHistoryExport")
    public Response accountHistoryExport(HttpServletRequest request, @RequestBody UserAccountFindVO findVO) {
        try {
            long time = findVO.getEndTime() - findVO.getStartTime();
            long dayMillisecond = 86400000L;
            if (time > dayMillisecond) {
                Map<String, Object> resultMap = new HashMap<>(2);
                resultMap.put("code", "0002");
                resultMap.put("msg", "请查询时间范围一天内的数据");
                return Response.returnSuccess(resultMap);
            }
            String name = request.getHeader("merchantName");
            if (StringUtils.isEmpty(name)) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            findVO.setLanguage(language);
            return Response.returnSuccess(userAccountService.accountHistoryExport(name, null, findVO));
        } catch (Exception e) {
            log.error("UserAccountController.accountHistoryExport error!", e);
        }
        return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
    }

    /**
     * 增加帐变记录信息
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/addChangeRecordHistory")
    public Response addChangeRecordHistory(HttpServletRequest request, @RequestBody AccountChangeHistoryFindVO findVO) {
        return Response.returnSuccess(userTransferService.addChangeRecordHistory(findVO, request));
    }
}
