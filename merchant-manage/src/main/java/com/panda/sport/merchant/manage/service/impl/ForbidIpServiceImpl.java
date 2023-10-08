package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.bss.mapper.TForbidIpMapper;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.TForbidIp;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;

import com.panda.sport.merchant.manage.service.IForbidIpService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.service.impl
 * @Description :  TODO
 * @Date: 2021-09-08 11:51:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
@Slf4j
public class ForbidIpServiceImpl implements IForbidIpService {

    @Autowired
    private TForbidIpMapper tForbidIpMapper;

    @Autowired
    private MerchantLogService merchantLogService;

    @Override
    public Response queryList(DomainVo domainVo) {
        Map<String, Object> resultMap = new HashMap();
        int count = tForbidIpMapper.pageListCount(domainVo);
        resultMap.put("total", count);
        if (count == 0) {
            return Response.returnSuccess(resultMap);
        }
        domainVo.setStarNum((domainVo.getPageNum() - 1) * domainVo.getPageSize());
        List<TForbidIp> list = tForbidIpMapper.pageList(domainVo);
        resultMap.put("list", list);
        return Response.returnSuccess(resultMap);
    }

    @Override
    public void deleteIp(HttpServletRequest request,Long id) {
        int id1 =id.intValue();
        //查询ip信息
        TForbidIp ipVo = tForbidIpMapper.load(id1);
        if(null==ipVo){
            return;
        }
        tForbidIpMapper.delete(id);
        /**
         *  添加系统日志
         * */

        String userId = request.getHeader("user-id");
        String ip = IPUtils.getIpAddr(request);
        String username = request.getHeader("merchantName");
        if(StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"))) {
            username = JWTUtil.getUsername(request.getHeader("token"));
        }
        String language = request.getHeader("language") == null ? LanguageEnum.ZS.getCode() : request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("deleteAllIp"));
        vo.getBeforeValues().add(ipVo.getIpName());
        vo.getAfterValues().add("-");
        merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_59, MerchantLogTypeEnum.ABNORMAL_IP_DEL, vo,
                MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId, username, null, null, userId, language, ip);
    }

    @Override
    public void deleteIpByName(HttpServletRequest request,String ipName) {
       List<String> before =tForbidIpMapper.ListNames();
        tForbidIpMapper.deleteByIpName(ipName);
        /**
         *  添加系统日志
         * */

        String userId = request.getHeader("user-id");
        String ip = IPUtils.getIpAddr(request);
        String username = request.getHeader("merchantName");
        if(StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"))) {
            username = JWTUtil.getUsername(request.getHeader("token"));
        }
        String language = request.getHeader("language") == null ? LanguageEnum.ZS.getCode() : request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("deleteAllIp"));
        vo.setBeforeValues(before);
        vo.getAfterValues().add("-");
        merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_58, MerchantLogTypeEnum.ABNORMAL_IP_ALL_DEL, vo,
                MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId, username, null, null, userId, language, ip);
    }

    @Override
    public Response saveIp(HttpServletRequest request,DomainVo domainVo, String username) {

        String[] strArray = domainVo.getIpName().split(",");
        Set<String> set = Arrays.stream(strArray).collect(Collectors.toSet());
        if (StringUtils.isEmpty(username)){
            username = "三端用户导入";
        }
        for (String value : set) {
            try {
                TForbidIp data = new TForbidIp();
                data.setIpName(value.trim());
                data.setCreateTime(System.currentTimeMillis());
                data.setCreateUser(username);
                tForbidIpMapper.insert(data);
            }catch (Exception e){
                log.warn("插入失败！ {}", value);
            }
        }

        /**
         *  添加系统日志
         * */

        String userId = request.getHeader("user-id");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language") == null ? LanguageEnum.ZS.getCode() : request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("deleteAllIp"));
        vo.getBeforeValues().add("-");
        vo.setAfterValues(new ArrayList(set));
        merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_45, MerchantLogTypeEnum.ABNORMAL_IP_ADD, vo,
                MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId, username, null, null, userId, language, ip);
        return Response.returnSuccess();
    }

}
