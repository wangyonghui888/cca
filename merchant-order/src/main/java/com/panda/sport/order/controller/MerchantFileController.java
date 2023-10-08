package com.panda.sport.order.controller;

import cn.hutool.core.collection.CollectionUtil;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/order/file")
public class MerchantFileController {

    @Autowired
    private MerchantFileService merchantFileService;

    /**
     * 查询列表
     *
     * @param request
     * @param merchantFileVo
     * @return
     */
    @PostMapping("/findList")
    public Response findList(HttpServletRequest request, @RequestBody MerchantFileVo merchantFileVo) {
        log.info("order/file/findList:" + merchantFileVo);
        merchantFileVo.setOperatName(request.getHeader("merchantName"));
        return Response.returnSuccess(merchantFileService.queryList(merchantFileVo));
    }

    /**
     * 查询当前执行文件
     *
     * @param request
     * @return
     */
    @RequestMapping("/queryExecuteFile")
    public Response queryExecuteFile(HttpServletRequest request) {
        MerchantFileVo merchantFileVo = new MerchantFileVo();
        merchantFileVo.setOperatName(request.getHeader("merchantName"));
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
        log.info("order/file/deleteFile:" + merchantFileVo);
        if (merchantFileVo == null || merchantFileVo.getId() == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        return merchantFileService.deleteNewFileById(request,merchantFileVo.getId());
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
        log.info("order/file/queryFileRate:" + merchantFileVo);
        merchantFileVo.setOperatName(request.getHeader("merchantName"));
        return merchantFileService.queryFileRate(merchantFileVo);
/*        if (merchantFileVo == null || merchantFileVo.getId() == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        return merchantFileService.queryFileRate(merchantFileVo.getId());*/
    }

    /**
     * 导出
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletResponse response, HttpServletRequest request, @RequestBody MerchantFile merchantFile) {
        log.info("order/file/export:" + merchantFile);
        merchantFileService.downLoadFile(merchantFile.getId(), response);
    }

    /**
     * 导出
     */
    @RequestMapping(value = "/exportTemplate")
    public void exportTemplate(HttpServletResponse response, HttpServletRequest request, @RequestBody MerchantFile merchantFile) {
        log.info("order/file/export:" + merchantFile);
        String fileName =  merchantFile.getFileName();
        if (StringUtils.isEmpty(fileName)){
            return;
        }
        List<MerchantFile> list = merchantFileService.queryFileByFileName(fileName);
        if (CollectionUtil.isEmpty(list)){
            return;
        }
        merchantFile = list.get(0);
        merchantFileService.downLoadFile(merchantFile.getId(), response);
    }

    @RequestMapping(value = "/execute")
    public Response execute(@RequestBody MerchantFile param) {
        log.info("order/file/execute:" + param);
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

    @GetMapping("delete/{id}")
    public void deleteFileById(@PathVariable("id")Long id){
        merchantFileService.deleteFileById(id);
    }
}
