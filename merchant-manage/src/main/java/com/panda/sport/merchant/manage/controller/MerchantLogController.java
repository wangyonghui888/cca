package com.panda.sport.merchant.manage.controller;

import com.google.common.collect.Lists;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.utils.JsonUtils;
import com.panda.sport.merchant.common.vo.MerchantLogFindVO;
import com.panda.sport.merchant.common.vo.PageVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.admin.controller
 * @Description :  商户端日志接口
 * @Date: 2020-09-04 15:34
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/manage/log")
public class MerchantLogController {

    @Autowired
    private MerchantLogService merchantLogService;

    /**
     * 查询栏目
     *
     * @return
     */
    @GetMapping("/getLogPages")
    public Response getLogPages(HttpServletRequest request) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return Response.returnSuccess(merchantLogService.loadLogPages(MerchantLogConstants.MERCHANT_IN, language));
    }

    /**
     * 查询日志操作类型
     *
     * @return
     */
    @GetMapping("/getLogTypes")
    public Response getLogTypes() {
        return Response.returnSuccess(merchantLogService.loadLogType());
    }

    /**
     * 查询日志
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findLog")
    public Response queryLog(HttpServletRequest request, @RequestBody MerchantLogFindVO findVO) {
        log.info("manage/log/findLog");
        //tob 后台不查三端的
        findVO.setTags(Arrays.asList(MerchantLogConstants.MERCHANT_IN, MerchantLogConstants.MERCHANT_OUT, MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN));
        PageVO<MerchantLogPO>  pageVO=merchantLogService.queryLog(findVO);
        for(MerchantLogPO po:pageVO.getRecords()){
            if((MerchantLogPageEnum.MERCHANT_INFO_EDITKEY.getCode().equals(po.getPageCode()) && MerchantLogTypeEnum.EDIT_INFO.getCode().equals(po.getOperatType())) || MerchantLogTypeEnum.EDIT_MERCHANT_INFO_KEY.getCode().equals(po.getOperatType())){
                po.setBeforeValues(JsonUtils.listToJson(Collections.singletonList("***")));
                po.setAfterValues(JsonUtils.listToJson(Collections.singletonList("***")));
            }
        }
        return Response.returnSuccess(pageVO);
    }

    /**
     * 查询域名切换异常日志
     *
     * @param request
     * @param findVO
     * @return
     */
    @PostMapping("/findApiDomainLog")
    public Response findApiDomainLog(HttpServletRequest request, @RequestBody MerchantLogFindVO findVO) {
        List<Integer> useTypes = Lists.newArrayList();
        if (findVO.getOperatSourceType() == null) {
            useTypes.add(MerchantLogTypeEnum.CHANGE_MERCHANT_Domian.getCode());
            useTypes.add(MerchantLogTypeEnum.MANUAL_CHANGE_MERCHANT_Domian.getCode());
        } else if (findVO.getOperatSourceType() == 1) {
            useTypes.add(MerchantLogTypeEnum.CHANGE_MERCHANT_Domian.getCode());
        } else if (findVO.getOperatSourceType() == 2) {
            useTypes.add(MerchantLogTypeEnum.MANUAL_CHANGE_MERCHANT_Domian.getCode());
        }
        findVO.setOperatTypes(useTypes);
        return Response.returnSuccess(merchantLogService.queryLog(findVO));
    }
}
