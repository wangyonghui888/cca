package com.panda.multiterminalinteractivecenter.feign;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.dto.MaintenanceRecordDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroup;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroupDomainPO;
import com.panda.multiterminalinteractivecenter.vo.MerchantVO;
import com.panda.multiterminalinteractivecenter.vo.OperationLogVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantSimpleVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MerchantManageHystrix implements FallbackFactory<MerchantManageClient>   {

    @Override
    public MerchantManageClient create(Throwable cause) {

        log.error("MerchantManageHystrix error",cause);
        return new MerchantManageFallBack() {
            @Override
            public Object sendStartMaintainRemindAndNoticeToSport(MaintenanceRecordDTO maintenanceRecordDTO) {
                log.error("维护平台体育开始维护时发送公告至体育,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public void checkInDomainEnable() {
                log.error("检测域名是否可用,调用接口异常，原因：{}",cause.getMessage());
            }

            @Override
            public int updateApiDomainByMerchantCode(String newDomain, String merchantCode) {
                log.error("updateApiDomainByMerchantCode,调用接口异常，原因：{}",cause.getMessage());
                return 0;
            }

            @Override
            public int updateMerchantDomainByMerchantCode(Integer domainType,String newDomain,String oldDomain, String merchantCode) {
                log.error("updateMerchantDomainByMerchantCode,调用接口异常，原因：{}",cause.getMessage());
                return 0;
            }

            @Override
            public List<?> queryMerchantListByGroup(String merchantGroupId,Integer status) {
                log.error("queryMerchantListByGroup,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public void updateMerchantGroupIdDefult(String id) {
                log.error("updateMerchantGroupIdDefult,调用接口异常，原因：{}",cause.getMessage());
            }

            @Override
            public int updateMerchantGroupId(String id, String groupCode, List<String> merchantCodes) {
                log.error("updateMerchantGroupId,调用接口异常，原因：{}",cause.getMessage());
                return 0;
            }

            @Override
            public List<?> selectList(MerchantVO merchantVO) {
                log.error("selectList,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public APIResponse merchantGroupInfoByGroupId(Long merchantGroupId) {
                log.error("merchantGroupInfoByGroupId,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public APIResponse load(Integer id) {
                log.error("load,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public int checkMerchantDomainExistByDomainType(String domain,Integer domainType) {
                log.error("checkMerchantDomainExistByDomainType,调用接口异常，原因：{}",cause.getMessage());
                return 0;
            }

            @Override
            public List<?> getMerchantList() {
                log.error("getMerchantList,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public List<?> getMerchantByCodes(List<String> merchantCode) {
                log.error("getMerchantByCodes,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public List<String> getMerchantByName(String merchantName) {
                log.error("getMerchantByName,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public List<?> getTblMerchantList() {
                log.error("getTblMerchantList,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public List<?> getTblMerchantByCodes(List<String> merchantCode) {
                log.error("getTblMerchantByCodes,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public int queryMerchantCountByGroup(String merchantGroupId, int status) {
                log.error("queryMerchantCountByGroup,调用接口异常，原因：{}",cause.getMessage());
                return 0;
            }

            @Override
            public int updateMerchantDomainByMerchantCodes(JSONObject param) {
                log.error("queryMerchantCountByGroup,调用接口异常，原因：{}",cause.getMessage());
                return 0;
            }

            @Override
            public List<?> getMerchantByDomains(String domain) {
                log.error("getMerchantByDomains,调用接口异常，原因：{}",cause.getMessage());
                return Lists.newArrayList();
            }

            @Override
            public String getAnimationURL() {
                log.error("getAnimationURL,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public int updateAnimationURL(String url) {
                log.error("updateAnimationURL,调用接口异常，原因：{}",cause.getMessage());
                return 0;
            }

            @Override
            public APIResponse queryMerchantDomain(FrontendMerchantDomain merchantDomain) {
                log.error("queryMerchantDomain,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public APIResponse changeMerchantDomain(DomainChangeDTO domainChangeDto) {
                log.error("changeMerchantDomain,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public APIResponse saveOperationLog(OperationLogVO operationLogVO) {
                log.error("saveOperationLog,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public APIResponse createFrontendMerchantGroup(FrontendMerchantGroup merchantGroup) {
                log.error("createFrontendMerchantGroup,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public APIResponse updateFrontendMerchantGroup(FrontendMerchantGroup merchantGroup) {
                log.error("updateFrontendMerchantGroup,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public APIResponse createFrontendDomain(FrontendMerchantDomain merchantDomain) {
                log.error("createFrontendDomain,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public APIResponse delFrontendDomain(FrontendMerchantDomain merchantDomain) {
                log.error("delFrontendDomain,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public APIResponse queryFrontendMerchantGroup(FrontendMerchantGroupDomainPO groupDomainPo) {
                log.error("queryFrontendMerchantGroup,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }

            @Override
            public List<com.panda.sport.merchant.common.vo.merchant.MerchantVO> queryMerchantListByParams(JSONObject param) {
                log.error("queryMerchantListByParam,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }
            @Override
            public List<MerchantSimpleVO> queryMerchantSimpleListByParams(JSONObject param) {
                log.error("queryMerchantListByParam,调用接口异常，原因：{}",cause.getMessage());
                return null;
            }
        };
    }
}

