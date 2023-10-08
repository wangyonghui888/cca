package com.panda.sport.order.controller;


import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.dto.*;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.utils.UUIDUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.order.feign.BssBackendClient;
import com.panda.sport.order.feign.PandaRcsCreditClient;
import com.panda.sport.order.feign.PandaRcsUserLimitClient;
import com.panda.sport.order.service.FinanceService;
import com.panda.sport.order.service.MerchantOrderService;
import com.panda.sport.order.service.impl.OrderSearchServiceImpl;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order/merchant")
@Slf4j
public class MerchantOrderController {
    @Autowired
    private MerchantOrderService merchantOrderService;

    @Autowired
    private PandaRcsCreditClient pandaRcsCreditClient;


    @Autowired
    private BssBackendClient bssBackendClient;

    @Autowired
    private PandaRcsUserLimitClient pandaRcsUserLimitClient;

    /**
     * 分库以后 所有用户的级别都要去汇总库（备份库查询）
     *  @Autowired
     *  private TUserMapper tUserMapper;
     */
    @Autowired
    private BackupTUserMapper tUserMapper;


    @Autowired
    public OrderSearchServiceImpl orderSearchService;

    @Resource
    private FinanceService financeService;

    /**
     * 商户报表查询
     */
    @PostMapping("/merchantList")
    @AuthRequiredPermission("Merchant:Report:merchant:list")
    public Response<?> merchantList(@RequestBody MerchantOrderVO pageVO) {
        log.info("/order/merchant/merchantList:" + pageVO);
        if (pageVO != null && pageVO.getCurrency() != null &&
                !pageVO.getCurrency().equals(CurrencyTypeEnum.RMB.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        return merchantOrderService.listGroupByMerchant(pageVO);
    }

    /**
     * 商户报表查询
     */
    @RequestMapping("/merchantFileExport")
    @AuthRequiredPermission("Merchant:Report:merchant:list:export")
    public Response merchantFileExport(HttpServletRequest request, @RequestBody MerchantOrderVO merchantOrderVO) {
        log.info("/order/merchant/merchantFileExport:" + merchantOrderVO);
        String username = request.getHeader("merchantName");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        merchantOrderVO.setLanguage(language);
        return Response.returnSuccess(merchantOrderService.reportDownload(username, merchantOrderVO));
    }

    @PostMapping("/queryCreditLimitConfig")
    public Response queryCreditLimitConfig(HttpServletRequest request, @RequestParam(value = "merchantId", required = false) Long merchantId,
                                           @RequestParam(value = "creditId", required = false) String creditId) {
        String globalId = UUIDUtils.createId();
        log.info("/order/merchant/queryCreditLimitConfig  merchantId:" + merchantId + ",creditId:" + creditId + ",globalId:" + globalId);
        try {
            CreditConfigDto reqData = new CreditConfigDto();
            reqData.setCreditId(creditId);
            reqData.setMerchantId(merchantId);
            Request req = new Request();
            req.setGlobalId(globalId);
            req.setData(reqData);
            return Response.returnSuccess(pandaRcsCreditClient.queryCreditLimitConfig(req).getData());
        } catch (Exception e) {
            log.error("MerchantController.queryCreditLimitConfig,exception:", e);
            return Response.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }

    }

    @PostMapping("/queryMechantAgentList")
    public Response queryMechantAgentList(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode) {
        log.info("/order/merchant/queryMechantAgentList  merchantCode:" + merchantCode );
        try {
            return merchantOrderService.queryMechantAgentList(merchantCode);
        } catch (Exception e) {
            log.error("MerchantController.queryCreditLimitConfig,exception:", e);
            return Response.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }

    }

    @RequestMapping(value = "/getMerchantMatchCreditConfig", method = RequestMethod.POST)
    public Response<CreditConfigHttpQueryDto> getMerchantMatchCreditConfig(@RequestBody CreditConfigHttpQueryDto configDto) {
        log.info("获取获取信用限额配置查询参数查询参数： {}", configDto );
        return Response.returnSuccess(pandaRcsCreditClient.getMerchantMatchCreditConfig(configDto).getData());
    }

    @RequestMapping(value = "/saveCreditLimitTemplate", method = RequestMethod.POST)
    public Response<String> saveCreditLimitTemplate(@RequestBody CreditConfigHttpQueryDto configDto) {
        log.info("保存获取信用限额配置查询参数数据： {}", configDto );
        try {
            return  Response.returnSuccess(pandaRcsCreditClient.saveCreditLimitTemplate(configDto).getData());
        } catch (Exception e) {
            return Response.returnFail("商户信用额度配置保存失败！");
        }
    }

    @RequestMapping(value = "/listAlbPayoutLimit", method = RequestMethod.GET)
    public Object queryListAlbPayoutLimit(@RequestParam(value ="uid") Long uid) {
        log.info("派彩可用额度查询：uid ", uid );
        try {
            Long startTime = System.currentTimeMillis();
            Object object= bssBackendClient.queryListAlbPayoutLimit(uid);
            Long diffSeconds = (System.currentTimeMillis()-startTime)/1000;
            log.info("MerchantOrderController.queryListAlbPayoutLimit,userId:{} time:{}", uid,diffSeconds);
             return object;
        } catch (Exception e) {
            return Response.returnFail("派彩可用额度查询失败！");
        }
    }

    @RequestMapping(value = "/userSeriesAvailableLimit", method = RequestMethod.POST)
    public Response<UserSeriesAvailableLimitResDto> userSeriesAvailableLimit(@RequestBody UserSingleLimitVO vo) {
        log.info("获取用户串关限额查询参数数据： {}", vo );
        try {
            String globalId = UUIDUtils.createId();
            Request req = new Request();
            req.setGlobalId(globalId);

            UserSeriesAvailableLimitReqDto configDto = new UserSeriesAvailableLimitReqDto();
            configDto.setMerchantId(tUserMapper.getUserMerchantId(vo.getUserId()));
            configDto.setUserId(Long.valueOf(vo.getUserId()));
            req.setData(configDto);
            log.info("/order/merchant/userSeriesAvailableLimit  globalId:" + globalId);
            UserSeriesAvailableLimitResDto dto = (UserSeriesAvailableLimitResDto) pandaRcsUserLimitClient.userSeriesAvailableLimit(req).getData();
            return  Response.returnSuccess(dto);
        } catch (Exception e) {
            log.error("获取用户串关限额查询参数数据",e);
            return Response.returnFail("获取用户串关限额查询参数数据！");
        }
    }

    @GetMapping("/getSportIdByMatchManageId")
    public Response<?> getSportIdByMatchManageId(@RequestParam(value = "matchId") String matchId) {
        return orderSearchService.getSportIdByMatchManageId(matchId);
    }


    @RequestMapping(value = "/userSingleAvailableLimit", method = RequestMethod.POST)
    public Response<UserSingleAvailableLimitResDto> userSingleAvailableLimit(@RequestBody UserSingleLimitVO vo) {
        log.info("获取用户单关限额配置查询参数数据： {}", vo );
        try {
            String globalId = UUIDUtils.createId();
            Request req = new Request();
            req.setGlobalId(globalId);

            UserSingleAvailableLimitReqDto configDto = new UserSingleAvailableLimitReqDto();
            configDto.setMerchantId(tUserMapper.getUserMerchantId(vo.getUserId()));
            configDto.setUserId(Long.valueOf(vo.getUserId()));
            String matchId;
            if(StringUtils.isNotEmpty(vo.getMatchId()) && vo.getMatchId().length()>7){
                matchId= vo.getMatchId().substring(vo.getMatchId().length()-7);
            } else{
                matchId = vo.getMatchId();
            }
            configDto.setMatchId(Long.valueOf(matchId));
            configDto.setPlayId(vo.getPlayId());
            req.setData(configDto);
            log.info("/order/merchant/userSingleAvailableLimit  globalId:" + globalId);
            UserSingleAvailableLimitResDto dto = (UserSingleAvailableLimitResDto) pandaRcsUserLimitClient.userSingleAvailableLimit(req).getData();
            return  Response.returnSuccess(dto);
        } catch (Exception e) {
            log.error("获取用户单关限额配置查询参数数据",e);
            return Response.returnFail("获取用户单关限额配置查询参数数据！");
        }
    }

    /**
     * 商户注单统计列表 ----》实际调用的是 order/financeMonth/queryFinanceayTotalList 接口
     */
    @PostMapping(value = "/queryMerchantNoteStatisticsList")
    @ApiOperation(value = "/order/merchant/queryMerchantNoteStatisticsList",notes = "商户后台-数据中心-商户注单统计-列表")
    public Response queryMerchantNoteStatisticsList(@RequestBody MerchantFinanceDayVo dayVo) {
        if (dayVo.getPageNum() == null) {
            dayVo.setPageNum(1);
        }
        if (dayVo.getPageSize() == null) {
            dayVo.setPageSize(10);
        }
        return financeService.queryFinanceayTotalList(dayVo);
    }

    /**
     * 商户注单统计汇总 ----》实际调用的是 order/financeMonth/queryFinanceDayTotal 接口
     */
    @PostMapping(value = "/getMerchantNoteStatisticsSummary")
    @ApiOperation(value = "/order/merchant/getMerchantNoteStatisticsSummary",notes = "商户后台-数据中心-商户注单统计-汇总")
    public Response getMerchantNoteStatisticsSummary(@RequestBody MerchantFinanceDayVo dayVo) {
        return financeService.queryFinanceDayTotal(dayVo);
    }

}
