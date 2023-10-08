package com.panda.sport.order.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import java.util.*;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import com.panda.sport.merchant.common.vo.merchant.AbnormalVo;
import com.panda.sport.merchant.common.vo.user.MerchantComboVO;
import com.panda.sport.order.feign.AbnormalApiClient;
import com.panda.sport.order.feign.AbnormalUserApiClient;
import com.panda.sport.order.service.AbnormalService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@Service
public class AbnormalServiceImpl implements AbnormalService {

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private AbnormalApiClient abnormalApiClient;

    @Autowired
    private AbnormalUserApiClient abnormalUserApiClient;

    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 异常会员名单查询
     */
    @Override
    public Response<?> queryAbnormalList(AbnormalVo abnormalVo, String language) {
        try {
            Map<String, Object> resultMap = new HashMap();
            Map<String, Object> result = new HashMap<>();
            assemblyMatchCriteria(abnormalVo, abnormalVo.getStartDate(), abnormalVo.getEndDate());
                   long beginTime = System.currentTimeMillis();
                   log.info("queryAbnormalList: 参数:{}:" + abnormalVo);
                   result= abnormalApiClient.queryAbnormalList(abnormalVo);
                    long endTime = System.currentTimeMillis();
            log.info("queryAbnormalList.cast: " + (endTime - beginTime) / 1000 + " second!");
            if (null != result  && null != result.get("data")) {
                Map<String, Object> thisMap = (Map<String, Object>) result.get("data");
                log.info("abnormalApiClient.queryAbnormalList: result:{}:" + result);
                resultMap.put("total", null != thisMap ? thisMap.get("total") : 0);
                resultMap.put("list", null != thisMap ? thisMap.get("userExceptionResVoList") : null);
                return Response.returnSuccess(resultMap);
            } else {
                return Response.returnSuccess("无数据!");
            }
        } catch (Exception e) {
            log.error("异常会员名单查询==queryAbnormalList:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 异常会员名单导出
     */
    @Override
    public void exportAbnormalStatistic(HttpServletResponse response, HttpServletRequest request, AbnormalVo vo, String language) throws Exception {
        vo.setPageNum(1);
        if (vo.getPageSize() > 50000) {
            log.error("导出数据记录大于50000条，无法导出记录为{}", vo.getPageSize());
            throw new RuntimeException("导出失败导出数据过多");
        }
        assemblyMatchCriteria(vo, vo.getStartDate(), vo.getEndDate());
        log.info("导出参数{}", JSON.toJSONString(vo));
        merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "异常会员名单导出_" : "AbnormalReport_"), null, request.getHeader("merchantName"), JSON.toJSONString(vo),
                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "异常会员名单导出" : "Report Center-Match Report-matchExport", "abnormalReportExportServiceImpl");
    }

    /**
     * 初始化查询参数，帐务日或自然日的时间
     *
     * @param vo
     * @param
     * @param
     * @throws Exception
     */
    public void assemblyMatchCriteria(AbnormalVo vo, String startTime1, String endTime1) throws Exception {
        String startTime2 = startTime1 + " 00:00:00";
        String endTime2 = endTime1 + " 23:59:59";
        Date endTime = StringUtils.isNotBlank(endTime1) ? DateUtil.parse(endTime2, DatePattern.NORM_DATETIME_PATTERN) : null;
        Date startTime = StringUtils.isNotBlank(startTime1) ? DateUtil.parse(startTime2, DatePattern.NORM_DATETIME_PATTERN) : null;
        vo.setEndTime(null != endTime ? endTime.getTime() : null);
        vo.setStartTime(null != startTime ? startTime.getTime() : null);
    }

    public void assemblyUserMatchCriteria(AbnormalUserVo vo, String startTime1, String endTime1) throws Exception {
        String startTime2 = startTime1 + " 00:00:00";
        String endTime2 = endTime1 + " 23:59:59";
        Date endTime = StringUtils.isNotBlank(endTime1) ? DateUtil.parse(endTime2, DatePattern.NORM_DATETIME_PATTERN) : null;
        Date startTime = StringUtils.isNotBlank(startTime1) ? DateUtil.parse(startTime2, DatePattern.NORM_DATETIME_PATTERN) : null;
        vo.setEndTime(null != endTime ? endTime.getTime() : null);
        vo.setStartTime(null != startTime ? startTime.getTime() : null);
    }

    /**
     * 初始化查询参数，帐务日或自然日的时间
     *
     * @param params
     * @param startTime
     * @param endTime
     * @throws Exception
     */
    public void assemblyPlayMatchCriteria(Map<String, Object> params, String startTime, String endTime) throws Exception {
        Date endTime1 = DateUtil.parse(startTime, DatePattern.NORM_DATETIME_PATTERN);
        Date startTime1 = DateUtil.parse(endTime, DatePattern.NORM_DATETIME_PATTERN);
        params.put("startTime", startTime1.getTime());
        params.put("endTime", endTime1.getTime());
    }

    /**
     * 异常会员名单查询
     */
    @Override
    public Response<?> queryAbnormalUserList(AbnormalUserVo abnormalUserVo, String language) {

        try {
            Map<String, Object> endResult = new HashMap();
            if (StringUtils.isNotEmpty(abnormalUserVo.getStartDate()) && StringUtils.isNotEmpty(abnormalUserVo.getEndDate())) {
                assemblyUserMatchCriteria(abnormalUserVo, abnormalUserVo.getStartDate(), abnormalUserVo.getEndDate());
            }
            //定义feign接口调用
            abnormalUserVo.setCategory(0);
            //判断如果是渠道用户就查询他下面所有的二级
            setMerchantCodeList(abnormalUserVo);
            endResult = abnormalUserApiClient.queryAbnormalUserList(abnormalUserVo);
            if (endResult.get("code").toString().equals("200")) {
                log.info("风控异常用户查询服务调用成功！result= {}", endResult);
                return Response.returnSuccess(endResult.get("data"));
            } else {
                log.error("风控异常用户查询服务调用失败！");
                return Response.returnFail("异常用户查询!");
            }

        } catch (Exception e) {
            log.error("异常用户查询==queryAbnormalUserList:", e);
            return Response.returnFail("异常用户查询!");
        }
    }

    /**
     * 异常用户导出
     */
    @Override
    public void exportAbnormalUserStatistic(HttpServletResponse response, HttpServletRequest request, AbnormalUserVo vo, String language) throws Exception {
        vo.setPageNum(0);
        if (vo.getPageSize() > 50000) {
            log.error("导出数据记录大于50000条，无法导出记录为{}", vo.getPageSize());
            throw new RuntimeException("导出失败导出数据过多");
        }
        if (StringUtils.isNotEmpty(vo.getStartDate()) && StringUtils.isNotEmpty(vo.getEndDate())) {
            assemblyUserMatchCriteria(vo, vo.getStartDate(), vo.getEndDate());
        }
        vo.setCategory(1);
        setMerchantCodeList(vo);
        log.info("导出参数{}", JSON.toJSONString(vo));
        merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "特殊限额用户名单导出_" : "SpecialQuotaUserListReport_"), null, request.getHeader("merchantName"), JSON.toJSONString(vo),
                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "特殊限额用户名单导出" : "Report Center-Match Report-matchExport", "abnormalUserReportExportServiceImpl");
    }

    private void setMerchantCodeList(AbnormalUserVo vo) {
        String level = null;
        String merchantId = null;
        List<String> codes = new ArrayList<>();
        if (StringUtils.isNotEmpty(vo.getMerchantCode())) {
            Map<String, String> map = merchantMapper.findLevelByMerchantCode(vo.getMerchantCode());
            if (null != map && map.size() > 0) {
                level = String.valueOf(ObjectUtils.isNotEmpty(map.get("agent_level")) ? map.get("agent_level") : "");
                merchantId = map.get("id");
            }
            if (level.equals("1")) {
                vo.setMerchantCode(null);
                List<MerchantComboVO> vos = merchantMapper.selectMerChantList(merchantId);
                for (MerchantComboVO mvo : vos) {
                    codes.add(mvo.getMerchantCode());
                }
                vo.setMerchantCodes(codes);
            }
        }
    }

    @Override
    public Response<?> queryUserComboList() {
        try {
            return Response.returnSuccess(merchantMapper.selectMerChantList(null));
        } catch (Exception e) {
            log.info("queryUserComboList 查询sql==error==" + e.getMessage());
            return Response.returnSuccess("下拉列表查询异常！");
        }
    }
}
