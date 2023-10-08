package com.panda.sport.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.OutMerchantService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.MerchantFileVo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.order.service.MerchantFileService;
import com.panda.sport.order.service.expot.OrderFileExportService;
import com.panda.sport.order.service.expot.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
@RequestMapping("/admin/file")
public class MerchantFileController {

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private OutMerchantService merchantService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询列表
     *
     * @param request
     * @param merchantFileVo
     * @return
     */
    @PostMapping("/findList")
    public Response findList(HttpServletRequest request, @RequestBody MerchantFileVo merchantFileVo) {
        log.info("admin/file/findList:" + merchantFileVo);
        List<String> codes = new ArrayList<>();
        JwtUser user = SecurityUtils.getUser();
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(user.getAgentLevel())) {
            codes = merchantService.queryMerchantList();
            if (CollectionUtil.isNotEmpty(codes)) {
                codes.add(user.getMerchantCode());
            } else {
                codes.add(user.getMerchantCode());
            }
        } else {
            codes.add(user.getMerchantCode());
        }
        merchantFileVo.setMerchantCodes(codes);
        merchantFileVo.setOperatName(user.getUsername());
        return Response.returnSuccess(merchantFileService.queryList(merchantFileVo));
    }

    /**
     * 查询当前执行文件
     *
     * @param request
     * @return
     */
    @PostMapping("/queryExecuteFile")
    public Response queryExecuteFile(HttpServletRequest request) {
        MerchantFileVo merchantFileVo = new MerchantFileVo();
        List<String> codes = new ArrayList<>();
        JwtUser user = SecurityUtils.getUser();
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(user.getAgentLevel())) {
            codes = merchantService.queryMerchantList();
            if (CollectionUtil.isNotEmpty(codes)) {
                codes.add(user.getMerchantCode());
            } else {
                codes.add(user.getMerchantCode());
            }
        } else {
            codes.add(user.getMerchantCode());
        }
        merchantFileVo.setMerchantCodes(codes);
        return Response.returnSuccess(merchantFileService.queryExecuteFile(merchantFileVo));
    }

    /**
     * 删除记录
     *
     * @param request
     * @param merchantFileVo
     * @return
     */
    @PostMapping("/deleteFile")
    public Response deleteFile(HttpServletRequest request, @RequestBody MerchantFileVo merchantFileVo) {
        log.info("admin/file/deleteFile:" + merchantFileVo);
        return merchantFileVo == null || merchantFileVo.getId() == null ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                merchantFileService.deleteFileById(merchantFileVo.getId());
    }

    /**
     * 查询进度
     *
     * @param request
     * @param merchantFileVo
     * @return
     */
    @PostMapping("/queryFileRate")
    public Response queryFileRateRate(HttpServletRequest request, @RequestBody MerchantFileVo merchantFileVo) {
        log.info("admin/file/queryFileRate:" + merchantFileVo);
        JwtUser user = SecurityUtils.getUser();
        merchantFileVo.setOperatName(user.getUsername());
        return merchantFileService.queryFileRate(merchantFileVo);
/*        return merchantFileVo == null || merchantFileVo.getId() == null ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                merchantFileService.queryFileRate(merchantFileVo.getId());*/
    }

    /**
     * 导出
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletResponse response, HttpServletRequest request, @RequestBody MerchantFile merchantFile) {
        log.info("admin/file/export:" + merchantFile.getId());
        merchantFileService.downLoadFile(merchantFile.getId(), response);
    }

    /**
     * 导出
     */
    @RequestMapping(value = "/execute")
    public Response execute(HttpServletRequest request, @RequestBody MerchantFile param) {
        log.info("admin/file/execute:" + param);
        try {
            MerchantFile merchantFile = merchantFileService.loadById(param.getId());
            OrderFileExportService exportService = SpringUtils.getBean(merchantFile.getExportBean());
            assert exportService != null;

            merchantFileService.updateFileStatusInit(merchantFile.getId());
            exportService.export(merchantFile);
        } catch (Exception e) {
            log.error("MerchantFileController.execute error!", e);
            return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
        return Response.returnSuccess();
    }
}
