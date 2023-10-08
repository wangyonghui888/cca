package com.panda.sport.order.controller;

import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import com.panda.sport.merchant.common.vo.merchant.AbnormalVo;
import com.panda.sport.order.service.AbnormalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@RestController
@RequestMapping("/order/abnormal")
public class AbnormalController {

    @Autowired
    private AbnormalService abnormalService;


    /**
     * 异常会员名单查询
     */
    @PostMapping(value = "/queryAbnormalList")
    //@AuthRequiredPermission("Merchant:Report:play:list")
    public Response queryAbnormalList(HttpServletRequest request, @RequestBody AbnormalVo abnormalVo) {
        log.info("/order/abnormal/queryAbnormalList:" + abnormalVo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        abnormalVo.setLanguage(language);
        return abnormalService.queryAbnormalList(abnormalVo, language);
    }




    /**
     * 异常会员名单导出
     */
    @RequestMapping("/exportAbnormalStatistic")
    //@AuthRequiredPermission("Merchant:Report:match:export")
    public Response exportAbnormalStatistic(HttpServletResponse response, HttpServletRequest request, @RequestBody AbnormalVo vo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        log.info("/order/abnormal/exportAbnormalStatistic:" + vo);
        try {
            vo.setLanguage(language);
           abnormalService.exportAbnormalStatistic(response, request, vo, language);
        } catch (Exception e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
            log.error("AbnormalController.exportAbnormalStatistic,exception:", e);
            return Response.returnSuccess(resultMap);
        }
        return Response.returnSuccess(resultMap);
    }

    /**
     * 异常用户查询
     */
    @PostMapping(value = "/queryAbnormalUserList")
    //@AuthRequiredPermission("Merchant:Report:play:list")
    public Response queryAbnormalUserList(HttpServletRequest request, @RequestBody AbnormalUserVo abnormalUserVo) {
        log.info("/order/abnormal/queryAbnormalUserList:" + abnormalUserVo);
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        abnormalUserVo.setLanguage(language);
        abnormalUserVo.setPageNum(abnormalUserVo.getPageNum()-1);
        return abnormalService.queryAbnormalUserList(abnormalUserVo, language);
    }

    /**
     * 异常用户导出
     */
    @RequestMapping("/exportAbnormalUserStatistic")
    //@AuthRequiredPermission("Merchant:Report:match:export")
    public Response exportAbnormalUserStatistic(HttpServletResponse response, HttpServletRequest request, @RequestBody AbnormalUserVo vo) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        log.info("/order/abnormal/exportAbnormalUserStatistic:" + vo);
        try {
            vo.setLanguage(language);
            abnormalService.exportAbnormalUserStatistic(response, request, vo, language);
        } catch (Exception e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
            log.error("AbnormalController.exportAbnormalUserStatistic,exception:", e);
            return Response.returnSuccess(resultMap);
        }
        return Response.returnSuccess(resultMap);
    }

    /**
     * 商户下拉查询
     */
    @PostMapping(value = "/queryUserComboList")
    //@AuthRequiredPermission("Merchant:Report:play:list")
    public Response queryUserComboList(HttpServletRequest request) {
        log.info("/order/abnormal/queryUserComboList:" );
        return abnormalService.queryUserComboList();
    }
}
