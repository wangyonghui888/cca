package com.panda.sport.merchant.manage.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.utils.ExtractUserInfoUtil;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.JsonUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.activity.LuckyboxRecordsVO;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.impl.SLuckyboxRecordsServiceImpl;
import com.panda.sport.order.service.MerchantFileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/manage/box")
public class LuckyBoxHisContorller {

    private SLuckyboxRecordsServiceImpl luckyboxRecordsService;

    @Autowired
    private MerchantLogService merchantLogService;
    @Resource
    private MerchantFileService merchantFileService;

    /**
     * 盲合领取数量查询
     * @param luckyboxRecordsVO
     * @return
     */
    @PostMapping("/queryLuckyBoxHistory")
    public Response queryLuckyBoxHistory(@RequestBody LuckyboxRecordsVO luckyboxRecordsVO) {
        if (luckyboxRecordsVO == null) {
            return Response.returnFail("parameter  is null");
        }
        try {
            Page data = luckyboxRecordsService.queryLuckyBoxHistory(luckyboxRecordsVO);
            return Response.returnSuccess(data);
        } catch (Exception e) {
            log.error("queryLuckyBoxHistory error: ", e);
            return Response.returnFail("queryLuckyBoxHistory 查询报错");
        }
    }

    /**
     * 盲合领取数量查询
     * @param luckyboxRecordsVO
     * @return
     */
    @RequestMapping("/queryLuckyBoxHistoryExcel")
    public Response<?> queryLuckyBoxHistoryExcel(HttpServletRequest request, @RequestBody LuckyboxRecordsVO luckyboxRecordsVO) {
        //写入商户文件记录表
        String lang = request.getHeader("language");
        String operator = request.getHeader("merchantName");
        String msg = lang.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!";
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        resultMap.put("msg", msg);
        try {
            String asyncFileName = "LuckyBoxHistory-" + new Date().getTime() + ".csv.zip";
            JSONObject obj = JSONUtil.createObj();
            obj.put("startTime", luckyboxRecordsVO.getStartTime());
            obj.put("endTime", luckyboxRecordsVO.getEndTime());
            obj.put("merchantId", luckyboxRecordsVO.getMerchantId());
            obj.put("userName", luckyboxRecordsVO.getUserName());
            JSONObject page = JSONUtil.createObj();
            page.put("current", luckyboxRecordsVO.getPage().getCurrent());
            page.put("size", luckyboxRecordsVO.getPage().getSize());
            obj.put("page", page);
            MerchantFile merchantFile = merchantFileService.saveExportTask(operator, asyncFileName, obj.toString(),
                    "SLuckyBoxRecordsServiceImpl");
            //异步执行导出任务
            luckyboxRecordsService.asyncExport(merchantFile);
            //写入日志
            MerchantLogPO logPO = new MerchantLogPO();
            Map<String, String> userInfo = ExtractUserInfoUtil.extractUserInfo(request);
            String userId = userInfo.get("userId");
            logPO.setUserId(userId);
            logPO.setUserName(userInfo.get("userName"));
            logPO.setOperatType(118);
            logPO.setTypeName("excel报表导出");
            logPO.setPageName("运营管理-盲盒领取记录");
            logPO.setPageCode("");
            logPO.setMerchantName("excel报表导出");
            logPO.setOperatField(JsonUtils.listToJson(Collections.singletonList("excel报表导出")));
            logPO.setDataId("118");
            logPO.setLogTag(1);
            logPO.setOperatTime(System.currentTimeMillis());
            logPO.setIp(IPUtils.getIpAddr(request));
            merchantLogService.saveLog(logPO);
        } catch (Exception e) {
            log.error("queryLuckyBoxHistoryExcel error: ", e);
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
        }
        return Response.returnSuccess(resultMap);
    }
}