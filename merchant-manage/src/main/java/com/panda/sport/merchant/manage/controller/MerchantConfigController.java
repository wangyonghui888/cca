package com.panda.sport.merchant.manage.controller;

import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.LogTagEnum;
import com.panda.sport.merchant.common.permission.AuthSeaMoonKey;
import com.panda.sport.merchant.common.po.bss.MerchantCodeConfig;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.MerchantConfigReqVO;
import com.panda.sport.merchant.common.vo.MerchantConfigVo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantConfigEditReqVO;
import com.panda.sport.merchant.manage.config.ExecutorInstance;
import com.panda.sport.merchant.manage.feign.MerchantApiClient;
import com.panda.sport.merchant.manage.service.MerchantService;
import com.panda.sport.merchant.manage.service.impl.LoginUserService;
import com.panda.sport.merchant.manage.service.impl.MerchantConfigServiceImpl;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.controller
 * @Description :  TODO
 * @Date: 2021-02-04 13:43:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@RestController
@RequestMapping("/manage/config")
@Slf4j
public class MerchantConfigController {

    @Autowired
    private MerchantConfigServiceImpl merchantConfigService;


    @Autowired
    private MerchantService merchantService;

    @Resource
    private MerchantApiClient merchantApiClient;

    @Autowired
    private LoginUserService loginUserService;

    @GetMapping("/getConfig")
    public Response queryByMerchantCode(@Param("merchantCode") String merchantCode){
        return Response.returnSuccess(merchantConfigService.findByMerchantCode(merchantCode));
    }

    @PostMapping("/uploadImgUrl")
    public Response uploadImgUrl(@RequestBody MerchantConfigVo merchantConfigVo){
        merchantConfigService.updateLogoUrl(merchantConfigVo.getMerchantCode(),merchantConfigVo.getTag(),merchantConfigVo.getUrl());
        return Response.returnSuccess();
    }

    @PostMapping("/updateConfig")
    public Response updateConfig(HttpServletRequest  request ,@RequestBody @Valid MerchantConfigEditReqVO merchantConfig,
                                 @RequestHeader(value = "language",defaultValue = Constant.LANGUAGE_CHINESE_SIMPLIFIED)  String  language){
        try {
            String userName = loginUserService.getLoginUser(SsoUtil.getUserId(request));

            merchantConfig.setLogTag(LogTagEnum.BACK_END.getCode());

            merchantConfigService.updateConfig(merchantConfig, SsoUtil.getUserId(request).toString(), userName, language, IPUtils.getIpAddr(request));

            asyncUpdateMerchantCache(merchantConfig.getMerchantCode());


        }catch (Exception e ){
            e.printStackTrace();
            return Response.returnFail(e.getMessage());
        }
        return Response.returnSuccess();
    }

    @PostMapping("/updateConfigFilter")
    public Response updateConfigFilter(HttpServletRequest  request ,@RequestBody @Valid MerchantConfigEditReqVO merchantConfig){
        if (merchantConfig.getFilterSport() == null){
            return Response.returnFail("必须添加过滤赛种信息！");
        }

        String userName = loginUserService.getLoginUser(SsoUtil.getUserId(request));

        // 获取系统语言
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        merchantConfigService.updateConfig(merchantConfig,SsoUtil.getUserId(request).toString(), userName, language,IPUtils.getIpAddr(request));
        // 设置日志标识为后端
        merchantConfig.setLogTag(LogTagEnum.BACK_END.getCode());
        merchantApiClient.updateMerchantUserCache(merchantConfig.getMerchantCode());
        return Response.returnSuccess();
    }

    private void asyncUpdateMerchantCache(String merchantCode) {
        ExecutorInstance.executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                log.error("asyncUpdateMerchantCache.sleep=", e);
            }
            merchantApiClient.updateMerchantUserCache(merchantCode);
            log.info("updateMerchantUserCache,更新商户缓存成功");
        });
    }

    /**
     * 球类开关 和联赛开关 批量修改
     * @param request
     * @param reqVO
     * @return
     */
    @PostMapping("/updateConfigFilterV2")
    public Response updateConfigFilterV2( @RequestHeader(value = "language",defaultValue = Constant.LANGUAGE_CHINESE_SIMPLIFIED)  String  language,
                                          HttpServletRequest request,
                                          @RequestBody  @Valid   MerchantConfigReqVO reqVO){
        try {
            log.info("url:/manage/config/updateConfigFilterV2;param:{}", reqVO.toString());

            // 用户名信息
            String userName = loginUserService.getLoginUser(SsoUtil.getUserId(request));

            // 更新配置
            merchantConfigService.updateConfigFilter(reqVO, SsoUtil.getUserId(request).toString(), userName, language, IPUtils.getIpAddr(request));

            // 更新缓存
            merchantApiClient.kickoutMerchant(reqVO.getMerchantCode());
            Thread.sleep(1_000);
            merchantApiClient.updateMerchantUserCache(reqVO.getMerchantCode());

        }catch (Exception e){
            log.error("批量修改异常",e);
            return Response.returnFail(e.getMessage());
        }

        return Response.returnSuccess();
    }


    /**
     * 商户等级列表
     * @return
     */
    @GetMapping("/queryCodeConfigList")
    public Response queryCodeConfigList(){
        return Response.returnSuccess(merchantConfigService.queryCodeConfigList());
    }


    /**
     * 修改商户等级配置
     *
     * @return
     */
    @AuthSeaMoonKey("修改商户等级配置")
    @PostMapping("/updateCodeConfig")
    public Response updateCodeConfig(String tokenCode, @RequestBody List<MerchantCodeConfig> list, HttpServletRequest request) {
        if (list == null) {
            return Response.returnFail("参数异常！");
        }
        int num = merchantConfigService.updateCodeConfig(list, request);
        return Response.returnSuccess(num > 0 ? true : false);
    }

    /**
     * 商户等级列表
     * @return
     */
    @GetMapping("/queryCodeConfigLogList")
    public Response queryCodeConfigLogList(@Param("merchantCode") String merchantCode,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                           @RequestParam(value = "pageNum") Integer pageNum){
        return Response.returnSuccess(merchantConfigService.queryCodeConfigLogList(merchantCode,pageSize,pageNum));
    }

    /**
     * 编辑商户盘口开关
     * @param request
     * @param merchantConfig
     * @param language
     * @return
     */
    @PostMapping("/updateMarketSwitch")
    public Response updateMarketSwitch(HttpServletRequest  request ,@RequestBody @Valid MerchantConfigEditReqVO merchantConfig,
                                       @RequestHeader(value = "language",defaultValue = Constant.LANGUAGE_CHINESE_SIMPLIFIED)  String  language){
        try {
            String userName = loginUserService.getLoginUser(SsoUtil.getUserId(request));
            merchantConfigService.updateMarketSwitch(merchantConfig, userName);
            asyncUpdateMerchantCache(merchantConfig.getMerchantCode());
        }catch (Exception e){
            e.printStackTrace();
            return Response.returnFail(e.getMessage());
        }
        return Response.returnSuccess();
    }

    /**
     * 预约盘口开关查询
     * @param request
     * @param merchantConfig
     * @return
     */
    @PostMapping(value = "/queryMarketSwitch")
    public Response queryMarketSwitch(HttpServletRequest  request ,@RequestBody @Valid MerchantConfigEditReqVO merchantConfig){
        if(StringUtils.isEmpty(merchantConfig.getMerchantCode())){
            return Response.returnFail("参数异常！");
        }
        MerchantConfig config = merchantConfigService.queryMarketSwitch(merchantConfig.getMerchantCode());
        return Response.returnSuccess(config);
    }

}
