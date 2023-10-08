package com.panda.sport.admin.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.admin.feign.AbnormalOutApiClient;
import com.panda.sport.admin.feign.PandaRcsTradeClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.AbnormalOutService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.admin.service.ExternalMerchantConfigService;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import com.panda.sport.merchant.common.vo.merchant.AbnormalVo;
import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingEditReqVO;
import com.panda.sport.merchant.common.vo.user.MerchantComboVO;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@Service
public class AbnormalOutServiceImpl implements AbnormalOutService {

    @Autowired
    private AbnormalOutApiClient abnormalOutApiClient;

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private PandaRcsTradeClient abnormalUserApiClient;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private BackupTUserMapper tUserMapper;

    @Autowired
    private ExternalMerchantConfigService externalMerchantConfigService;


    /**
     * 异常会员名单查询
     */
    @Override
    public Response<?> queryAbnormalList(AbnormalVo abnormalVo, String language) {
        try {
               Map<String, Object> resultMap = new HashMap();
               Map<String, Object> result = new HashMap();
                abnormalVo.setAbnormalClickTime(abnormalVo.getAbnormalClickTime());
                assemblyMatchCriteria(abnormalVo,abnormalVo.getStartDate(),abnormalVo.getEndDate());
                long beginTime = System.currentTimeMillis();
                log.info("queryAbnormalList: 参数:{}:" + abnormalVo);
                result= abnormalOutApiClient.queryAbnormalList(abnormalVo);
                long endTime = System.currentTimeMillis();
                log.info("queryAbnormalList.cast: " + (endTime - beginTime) / 1000 + " second!");
                if (null != result  && null != result.get("data")) {
                    Map<String, Object> thisMap = (Map<String, Object>) result.get("data");
                    log.info("abnormalOutApiClient.queryAbnormalList: result:{}:" + result);
                    resultMap.put("total", null != thisMap ? thisMap.get("total") : 0);

                   List<Map<String,Object>>  userVoList  = (List<Map<String, Object>>) thisMap.get("userExceptionResVoList");
                   if(userVoList!=null && userVoList.size()>0) {
                       UserOrderVO userOrderVO = new UserOrderVO();
                       List<String> userIdList = Lists.newArrayList();
                       for (Map<String, Object> map : userVoList) {
                           userIdList.add((String) map.get("userId"));
                       }
                       userOrderVO.setUserIdList(userIdList);
                       userOrderVO.setOrderBy("u.modify_time");
                       userOrderVO.setSort(Constant.DESC);
                       userOrderVO.setStart(0);
                       userOrderVO.setPageSize(100);
                       List<UserOrderAllPO> list = tUserMapper.queryAllUserList(userOrderVO);
                       for (UserOrderAllPO userAllPo : list) {
                           for (Map<String, Object> map : userVoList) {
                               String userId = (String) map.get("userId");
                               if (userId.equals(userAllPo.getUserId())) {
                                   map.put("marketLevel", userAllPo.getMarketLevel());
                                   map.put("specialBettingLimitType", userAllPo.getSpecialBettingLimitType());
                                   map.put("specialBettingLimitDelayTime", userAllPo.getSpecialBettingLimitDelayTime());
                                   map.put("specialBettingLimitTime", userAllPo.getSpecialBettingLimitTime());
                               }
                           }
                       }
                   }
                    resultMap.put("list", null != userVoList ? userVoList : null);
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
     * 异常会员名单icon统计查询
     */
    @Override
    public Response<?> queryAbnormalCount(AbnormalVo abnormalVo, String language) {
        try {
            Map<String, Object> resultMap = new HashMap();
            Map<String, Object> result = new HashMap();
            //查询异常用户名单点击时间
            Long abnormalClickTime = abnormalVo.getAbnormalClickTime();
            Map<String, Object> getQueryAbnormalMap = externalMerchantConfigService.getQueryConditionMap(abnormalVo.getMerchantCode());
            log.info(abnormalVo.getMerchantCode(), "getQueryAbnormalMap", getQueryAbnormalMap);
            if (getQueryAbnormalMap != null && getQueryAbnormalMap.containsKey("abnormal_click_time")) {
                abnormalClickTime = Long.parseLong(getQueryAbnormalMap.get("abnormal_click_time").toString());
                String startTime2 = abnormalVo.getEndDate() + " 00:00:00";
                Date startTime = StringUtils.isNotBlank(startTime2)?DateUtil.parse(startTime2, DatePattern.NORM_DATETIME_PATTERN):null;
                Long startTimeLong = null != startTime ? startTime.getTime() : null;
                Long current = System.currentTimeMillis();
                if (abnormalClickTime.compareTo(startTimeLong) < 0 && startTimeLong.compareTo(current) < 0) {
                    abnormalClickTime = startTimeLong;
                }
            }
            abnormalVo.setAbnormalClickTime(abnormalClickTime);
            assemblyMatchCriteria(abnormalVo,abnormalVo.getStartDate(),abnormalVo.getEndDate());
            long beginTime = System.currentTimeMillis();
            log.info("queryAbnormalCount: 参数:{}:" + abnormalVo);
            result= abnormalOutApiClient.queryAbnormalList(abnormalVo);
            long endTime = System.currentTimeMillis();
            log.info("queryAbnormalCount.cast: " + (endTime - beginTime) / 1000 + " second!");
            if (null != result  && null != result.get("data")) {
                Map<String, Object> thisMap = (Map<String, Object>) result.get("data");
                log.info("abnormalOutApiClient.queryAbnormalCount: result:{}:" + result);
                Integer total = null != thisMap ? Integer.valueOf(thisMap.get("total").toString()) : 0;
                resultMap.put("total", total);
                //isIcon:1  点亮并显示数量， isIcon:0 置灰
                resultMap.put("isIcon", total>0 ? 1 : 0);
                return Response.returnSuccess(resultMap);
            } else {
                return Response.returnSuccess("无数据!");
            }
        } catch (Exception e) {
            log.error("异常会员名单icon统计查询==queryAbnormalCount:", e);
            return Response.returnFail("查询异常!");
        }
    }

    /**
     * 异常会员名单icon点击更新时间
     */
    @Override
    public Response<?> updateAbnormalClickTime(String merchantCode,Long abnormalClickTime) {
        try {
            Map<String, Object> resultMap = new HashMap();
            //查询上次异常用户名单点击时间
            Long abnormalLastClickTime = 0L;
            Map<String, Object> getQueryAbnormalMap = externalMerchantConfigService.getQueryConditionMap(merchantCode);
            if (getQueryAbnormalMap != null && getQueryAbnormalMap.containsKey("abnormal_click_time")) {
                abnormalLastClickTime = Long.parseLong(getQueryAbnormalMap.get("abnormal_click_time").toString());
                /*Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                Long startTimeLong = calendar.getTime().getTime();
                Long current = System.currentTimeMillis();
                if (abnormalClickTime.compareTo(startTimeLong) < 0 && startTimeLong.compareTo(current) < 0) {
                    abnormalClickTime = startTimeLong;
                }*/
            }
            //记录异常用户名单点击当前时间
            QueryConditionSettingEditReqVO queryConditionVO = new QueryConditionSettingEditReqVO();
            queryConditionVO.setMerchantCode(merchantCode);
            queryConditionVO.setAbnormalClickTime(abnormalClickTime);
            externalMerchantConfigService.editQueryConditionSetting(queryConditionVO);
            //更新时间成功 isIcon:0 置灰
            resultMap.put("isIcon",0);
            resultMap.put("abnormalClickTime",abnormalLastClickTime);
            return Response.returnSuccess(resultMap);
        } catch (Exception e) {
            return Response.returnFail("异常会员名单icon点击更新时间异常!");
        }
    }

    /**
     * 异常会员名单导出
     */
    @Override
    public void exportAbnormalStatistic(HttpServletResponse response, HttpServletRequest request,AbnormalVo vo, String language) throws Exception {
        vo.setPageNum(1);
        if(vo.getPageSize() > 50000){
            log.error("导出数据记录大于50000条，无法导出记录为{}",vo.getPageSize());
            throw new RuntimeException("导出失败导出数据过多");
        }
        assemblyMatchCriteria(vo, vo.getStartDate(), vo.getEndDate());
        log.info("导出参数{}", JSON.toJSONString(vo));
        JwtUser user = SecurityUtils.getUser();
        merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "异常会员名单导出_" : "AbnormalReport_"), user.getMerchantCode(), user.getUsername(), JSON.toJSONString(vo),
                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "异常会员名单导出" : "Report Center-Match Report-matchExport", "abnormalOutReportExportServiceImpl");
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
        Date endTime = StringUtils.isNotBlank(endTime1)?DateUtil.parse(endTime2, DatePattern.NORM_DATETIME_PATTERN):null;
        Date startTime = StringUtils.isNotBlank(startTime1)?DateUtil.parse(startTime2, DatePattern.NORM_DATETIME_PATTERN):null;
        vo.setEndTime(null!=endTime?endTime.getTime():null);
        if (null != vo.getAbnormalClickTime()) {
            vo.setStartTime(vo.getAbnormalClickTime());
        } else {
            vo.setStartTime(null != startTime ? startTime.getTime() : null);
        }
    }

    /**
     * 异常会员名单查询
     */
    @Override
    public Response<?> queryAbnormalUserList(AbnormalUserVo abnormalUserVo, String language) {

        try {
            Map<String, Object> endResult = new HashMap();
            if(StringUtils.isNotEmpty(abnormalUserVo.getStartDate()) && StringUtils.isNotEmpty(abnormalUserVo.getEndDate())) {
                assemblyUserMatchCriteria(abnormalUserVo, abnormalUserVo.getStartDate(), abnormalUserVo.getEndDate());
            }
            //定义feign接口调用
            abnormalUserVo.setCategory(0);
            endResult= abnormalUserApiClient.queryAbnormalUserList(abnormalUserVo);
            if(endResult.get("code").toString().equals("200")){
                log.info("风控异常用户查询服务调用成功！result= {}",endResult);
                return Response.returnSuccess(endResult.get("data"));
            }else{
                log.error("风控异常用户查询服务调用失败！");
                return Response.returnFail("异常用户查询!");
            }

        }catch (Exception e) {
            log.error("异常用户查询==queryAbnormalUserList:", e);
            return Response.returnFail("异常用户查询数据异常!");
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
        if(StringUtils.isNotEmpty(vo.getStartDate()) && StringUtils.isNotEmpty(vo.getEndDate())) {
            assemblyUserMatchCriteria(vo, vo.getStartDate(), vo.getEndDate());
        }
        vo.setUserId(null);
        vo.setAppId(request.getHeader("app-id"));
        vo.setCategory(1);
        log.info("导出参数{}", JSON.toJSONString(vo));
        JwtUser user = SecurityUtils.getUser();
        merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "特殊限额用户名单导出_" : "SpecialQuotaUserListReport_"), user.getMerchantCode(), user.getUsername(), JSON.toJSONString(vo),
                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "特殊限额用户名单导出" : "Report Center-Match Report-matchExport", "abnormalUserOutReportExportServiceImpl");
    }

    @Override
    public List<MerchantComboVO> findUserComboList() {
        JwtUser user = SecurityUtils.getUser();
        String merchantId = user.getMerchantId();
        return merchantMapper.selectMerChantList(merchantId);
    }

    public void assemblyUserMatchCriteria(AbnormalUserVo vo, String startTime1, String endTime1) throws Exception {
        String startTime2 = startTime1 + " 00:00:00";
        String endTime2 = endTime1 + " 23:59:59";
        Date endTime = StringUtils.isNotBlank(endTime1) ? DateUtil.parse(endTime2, DatePattern.NORM_DATETIME_PATTERN) : null;
        Date startTime = StringUtils.isNotBlank(startTime1) ? DateUtil.parse(startTime2, DatePattern.NORM_DATETIME_PATTERN) : null;
        vo.setEndTime(null != endTime ? endTime.getTime() : null);
        vo.setStartTime(null != startTime ? startTime.getTime() : null);
    }
}
