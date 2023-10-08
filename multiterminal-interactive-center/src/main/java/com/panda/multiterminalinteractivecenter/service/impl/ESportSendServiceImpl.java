package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import com.panda.multiterminalinteractivecenter.dto.MaintainRecordDto;
import com.panda.multiterminalinteractivecenter.dto.MaintenanceRecordDTO;
import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import com.panda.multiterminalinteractivecenter.enums.TabEnum;
import com.panda.multiterminalinteractivecenter.mapper.MaintenancePlatformMapper;
import com.panda.multiterminalinteractivecenter.mapper.MatchInfoMapper;
import com.panda.multiterminalinteractivecenter.utils.DjUtil;
import com.panda.multiterminalinteractivecenter.utils.HttpConnectionPool;
import com.panda.multiterminalinteractivecenter.vo.DjKickUserVo;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceRecordEditVo;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceRecordVo;
import com.panda.multiterminalinteractivecenter.vo.SportMaintainRemindToESportSendReqVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  test
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-29 13:06:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
@Slf4j
@RefreshScope
public class ESportSendServiceImpl {

    @Value("${dj.merchant.id:31433517168705439}")
    private String djMerchantId;
    @Value("${dj.merchant.key:5a49502d69594196622c5b3a981b5208}")
    private String djMerchantKey;
    @Value("${dj.merchant.url:http://www.phiqui.com}")
    private String baseUrl;
    @Autowired
    private MultiterminalConfig config;

    @Resource
    private MaintenanceRecordServiceImpl maintenanceRecordService;

    @Resource
    private MaintenancePlatformServiceImpl maintenancePlatformService;

    @Resource
    private MaintenancePlatformMapper maintenancePlatformMapper;

    @Autowired
    private LotterySendServiceImpl lotterySendService;

    @Autowired
    private MatchInfoMapper matchInfoMapper;

    public void send(DjKickUserVo djKickUserVo, String url) throws IOException {
        djKickUserVo.setMerchant(djMerchantId);
        String reqUrl = baseUrl + url ;
        log.info("ty ： handleCloseSport：发送消息关闭DJ请求，URL：{}，param：{}",reqUrl,JSON.toJSONString(djKickUserVo));
        Map<String,Object> sourceMap = BeanUtil.beanToMap(djKickUserVo, false, true);
        String sign = DjUtil.eSportEncrypt(sourceMap, djMerchantKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("clientId", "internal service");
        headers.add("sign", sign);

        MultiValueMap<String, Object> params= new LinkedMultiValueMap<String, Object>();
        params.add("is_open_match", djKickUserVo.getIs_open_match());//支持中文
        params.add("merchant", djMerchantId);

        ResponseEntity<String> response = HttpConnectionPool.restTemplate.postForEntity(reqUrl, new HttpEntity<>(params,headers),String.class);

        HttpStatus httpStatus = response.getStatusCode();

        log.info("handleCloseSport: 电竞服务调用关闭赛种  {}--> result:【{}】",reqUrl,response.getBody());
        if (httpStatus.value() == HttpStatus.OK.value()) {
            log.info("ty ： handleCloseSport: 电竞服务调用关闭赛种成功！result= {}", response.getBody());
            // 修改维护状态
            matchInfoMapper.updateByTab(TabEnum.DJ.getName(), djKickUserVo.getIs_open_match());
        }else{
            log.info("ty ： handleCloseSport: 电竞服务调用关闭赛种失败！result= {}", response.getBody());
        }
    }

    public void checkSendEndMaintainRemindAndNoticeToESport(MaintenanceRecordEditVo maintenanceRecordEditVo) {
        String title = "维护平台电竞结束维护时发送公告至电竞";
        try {
            final String eSportFlag = "dj";
            if (StringUtils.isNoneBlank(maintenanceRecordEditVo.getDataCode()) && Objects.equals(eSportFlag,maintenanceRecordEditVo.getDataCode())){
                List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordEditVo.getDataCode());
                if (Objects.equals(records.get(0).getIsSendNotice(), 0)){
                    return;
                }
                if (ObjectUtil.isNotEmpty(records)){
                    MaintenanceRecordDTO dto = new MaintenanceRecordDTO();
                    BeanUtils.copyProperties(records.get(0),dto);
                    sendMaintainRemindAndNoticeToESport(dto, 2,title);
                }
            } else if (ObjectUtil.isNotNull(maintenanceRecordEditVo.getRecordId())){
                MaintenanceRecord maintenanceRecord = maintenanceRecordService.getById(maintenanceRecordEditVo.getRecordId());
                if (ObjectUtil.isNull(maintenanceRecord)){
                    return;
                }
                if (Objects.equals(maintenanceRecord.getIsSendNotice(), 0)){
                    return;
                }
                List<MaintenancePlatform> maintenancePlatformList = maintenancePlatformMapper.selectList(new QueryWrapper<>());
                Map<Long,String> platformList = maintenancePlatformList.stream().collect(Collectors.toMap(MaintenancePlatform::getId, MaintenancePlatform::getDataCode, (k1, k2) -> k1));
                if (eSportFlag.equals(platformList.get(maintenanceRecord.getMaintenancePlatformId()))){
                    MaintenanceRecordDTO dto = new MaintenanceRecordDTO();
                    BeanUtils.copyProperties(maintenanceRecord,dto);
                    sendMaintainRemindAndNoticeToESport(dto, 2,title);
                }
            }
        } catch (BeansException e) {
            log.error("{}异常，原因：{}",title,e);
        }
    }

    public void checkSendStartMaintainRemindAndNoticeToESport(MaintenanceRecordVo maintenanceRecordVo) {
        String title = "维护平台电竞开始维护时发送公告至电竞";
        log.info("{}, 进入方法：{}",title,maintenanceRecordVo);
        try {
            final String eSportFlag = "dj";
            if (Objects.equals(eSportFlag,maintenanceRecordVo.getDataCode()) && Objects.equals(maintenanceRecordVo.getIsSendNotice(), 1)){
                List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordVo.getDataCode());
                if (ObjectUtil.isNotEmpty(records)){
                    MaintenanceRecordDTO dto = new MaintenanceRecordDTO();
                    BeanUtils.copyProperties(records.get(0),dto);
                    sendMaintainRemindAndNoticeToESport(dto, 1,title);
                }
            }
        } catch (BeansException e) {
            log.error("{}异常，原因：{}",title, e);
        }
    }

    /**
     * 维护平台开始维护时发送提醒及维护公告至电竞
     * @param maintenanceRecordDTO
     */
    private void sendMaintainRemindAndNoticeToESport(MaintenanceRecordDTO maintenanceRecordDTO, Integer status, String title) {
        String url = baseUrl + "/v1/notice/maintain";
        String key = djMerchantKey;
        Long merchantId = Long.parseLong(djMerchantId);

        SportMaintainRemindToESportSendReqVO sendReqVO = new SportMaintainRemindToESportSendReqVO();
        sendReqVO.setMerchant(merchantId);
        sendReqVO.setMaintenance_start_time(TimeUnit.MILLISECONDS.toSeconds(maintenanceRecordDTO.getMaintenanceStartTime()));
        sendReqVO.setMaintenance_end_time(TimeUnit.MILLISECONDS.toSeconds(maintenanceRecordDTO.getMaintenanceEndTime()));
        sendReqVO.setReminder_time(maintenanceRecordDTO.getRemindTime());
        sendReqVO.setStatus(status);

        // log.info("{}, 参数：{},url:{},key:{},merchantId:{}",title,sendReqVO,url,key,merchantId);
        Map<String, Object> sourceMap = BeanUtil.beanToMap(sendReqVO, false, true);

//        JSONObject param = new JSONObject();
//
//        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
//            param.put(entry.getKey(), entry.getValue().toString());
//        }

        try {
//            CloseableHttpClient httpClient = HttpClientUtil.getSystemHttpClient(config.getHttpProxySwitch());
//            HttpPost httpPost = new HttpPost(url);
//            StringEntity paramEntity = new StringEntity(JSON.toJSONString(param),"UTF-8");
//            httpPost.setEntity(paramEntity);
//            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//            log.info("{}调用{}--> result:【{}】",title,url,response.toString());
//            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
//            JSONObject jsonObject = JSON.parseObject(msg);
//            String successStatus = "true";
//            String successCode = "200";
//            if (successStatus.equals(jsonObject.get("status")) && successCode.equals(jsonObject.get("code"))) {
//                log.error("{}失败,result={}", title, msg);
//            } else {
//                log.error("{}失败,result={}", title, msg);
//            }

            HttpHeaders headers = new HttpHeaders();
            String sign = DjUtil.eSportEncrypt(sourceMap, djMerchantKey);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("sign", sign);
            MultiValueMap<String, Object> param= new LinkedMultiValueMap<>();
            sourceMap.forEach(param::add);
            log.info("{}, 参数：{},url:{},key:{},merchantId:{},sign:{}", title,param,url,key,merchantId,sign);
            ResponseEntity<String> response = HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(param, headers),String.class);
            HttpStatus httpStatus = response.getStatusCode();

            if (200 == httpStatus.OK.value()) {
                log.error("{}成功,result={}", title, response.getBody());
            } else {
                log.error("{}失败,result={}", title, response.getBody());
            }
        } catch (Exception e) {
            log.error("{}异常,param={},异常信息：{}", title,sourceMap, e);
        }
    }

    public void checkSendStartMaintainRemindAndNoticeToLottery(MaintenanceRecordVo maintenanceRecordVo) {
        String title = "维护平台彩票开始维护时发送公告至彩票";
        try {
            final String eSportFlag = "cp";
            if (Objects.equals(eSportFlag,maintenanceRecordVo.getDataCode()) && Objects.equals(maintenanceRecordVo.getIsSendNotice(), 1)){
                List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordVo.getDataCode());
                if (ObjectUtil.isNotEmpty(records)){
                    try {
                        MaintainRecordDto dto = new MaintainRecordDto();
                        dto.setMaintenanceStartTime(maintenanceRecordVo.getMaintenanceStartTime());
                        dto.setMaintenanceEndTime(maintenanceRecordVo.getMaintenanceEndTime());
                        dto.setStatus(0);
                        lotterySendService.MaintainRemindAndNotice(dto, "/sabang/pulic/maintainRecord");
                    }catch (Exception e){
                        log.error("彩票公告发送异常！", e);
                    }
                }
            }
        } catch (BeansException e) {
            log.error("{}异常，原因：{}",title, e);
        }
    }

    public void checkSendStartMaintainRemindAndNoticeToLottery(MaintenanceRecordEditVo maintenanceRecordEditVo) {
        String title = "维护平台彩票开始维护时发送公告至彩票";
        try {
            final String eSportFlag = "cp";
            if (Objects.equals(eSportFlag,maintenanceRecordEditVo.getDataCode())){
                List<MaintenanceRecord> records = maintenancePlatformService.getListByDataCode(maintenanceRecordEditVo.getDataCode());
                if (ObjectUtil.isNotEmpty(records)){
                    for (MaintenanceRecord maintenanceRecord : records) {
                        MaintenancePlatform maintenancePlatform = maintenancePlatformMapper.selectById(maintenanceRecord.getMaintenancePlatformId());
                        if (maintenancePlatform != null && "1".equals(maintenancePlatform.getServeCode())){
                            try {
                                MaintainRecordDto dto = new MaintainRecordDto();
                                dto.setMaintenanceStartTime(maintenanceRecord.getMaintenanceStartTime());
                                dto.setMaintenanceEndTime(maintenanceRecord.getMaintenanceEndTime());
                                dto.setStatus(1);
                                lotterySendService.MaintainRemindAndNotice(dto, "/sabang/pulic/maintainRecord");
                            }catch (Exception e){
                                log.error("彩票公告发送异常！", e);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (BeansException e) {
            log.error("{}异常，原因：{}",title, e);
        }
    }
}
