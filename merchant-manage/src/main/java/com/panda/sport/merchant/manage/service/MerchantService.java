package com.panda.sport.merchant.manage.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.dto.DomainChangeDTO;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.vo.MerchantInfoVo;
import com.panda.sport.merchant.common.vo.OperationLogVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.manage.entity.vo.MerchantGroupDomainVO;
import com.panda.sport.merchant.manage.mq.vo.TagMarketMsg;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface MerchantService {

    Response selectList(MerchantVO merchantVO);

    Response selectMonthList(Integer agentLevel);

    Response merchantDetail(String id);

    Response getMerchantLanguageList();

    Response getMerchantRiskList();

    Response updateMerchant(MerchantVO merchantVO, Integer userId, String language,String ip,String agentLevel) throws Exception;

    Response updateMerchantWhiteIp(MerchantVO merchantVO, Integer userId, String language,String ip) throws Exception;

    Response updateMerchantVrSport(MerchantVO merchantVO, Integer userId, String language,String ip) throws Exception;
    Response updateMerchantESport(MerchantVO merchantVO, Integer userId, String language,String ip) throws Exception;
    Response updateMerchantOpenVideo(MerchantVO merchantVO, Integer userId, String language,String ip) throws Exception;

    Response createMerchant(MerchantVO merchantVO, Integer userId, String language,String ip) throws Exception;

    Response<PageInfo<TMerchantKey>> queryKeyList(String merchantName, String merchantCode, String parentId, Integer pageSize, Integer pageIndex, String language);

    Response<Object> generateKey();

    Response<Object> updateKey(String code, String key, String keyLabel, String startTime, String merchantCode, Integer userId, String language,String ip) throws Exception;

    Response<Object> updateMerchantStatus(String merchantCode, String status, Integer userId, String language,String ip);

    Response<Object> updateMerchantBackendStatus(String merchantCode, String status, Integer userId, String language,String ip);

    Response<Object> importMerchantStatus(MerchantInfoVo merchantInfoVo,Integer userId, String language,String ip);

    Response<Object> findMerchantInfo(MerchantInfoVo merchantInfoVo,Integer userId, String language,String ip);

    Response<Object> updateMerchantRiskStatus(List<MerchantConfig> configList, String userId, String ip);

    Response<Object> transfer(String merchantCode, String username, Integer transferType, Double amount);

    Response<Object> queryTransferList(String username, String merchantCode, Integer pageSize, Integer pageIndex);


    Response<Object> adminList();

    Response<Object> queryAgentCount();


    Response createAdmin(String id, String adminName, String adminPassword, Integer userId, String language, String ip,String agentLevel);

    Response queryMerchantListByParam();

    Response queryMerchantTree();

    Response getMerchantListTree();

    void updateMerchantStatus() throws Exception;

    boolean updateMerchantFTPInfo(String merchantCode, String ftpUser, String ftpPassword);

    Response<Object> deleteSubAgent(String merchantCode, String parentId, Integer userId, String language ,String ip);

    MerchantPO getMerchantById(String id);

    Response getMerchantCodeListTree();

    Response getMerchantNameListTree();

    Response merchantDomainList(Integer merchantTag,Integer containsType,String containsStr,String parentCode,Integer pageSize,Integer pageNum);

    Response merchantAppDomainList(Integer merchantTag,Integer containsType,String containsStr,String parentCode,String merchantCode,Integer pageSize,Integer pageNum);

    Response updateDomainList(Integer userid, Integer merchantTag, Integer containsType, String containsStr, String newDomainStr, String merchantCode,String parentCode, String language,String ip);

    Response updateKanaCode(Integer userid, String kanaCode,  String merchantCode, String language ,String ip);
    Response updateSerialNumber(Integer userid, Integer serialNumber,  String merchantCode, String language,String ip);

    Response updateAppDomainList(Integer userid, Integer merchantTag, Integer containsType, String containsStr, String newDomainStr, String merchantCode, String parentCode,String language,String ip);

    Response cleanMerchant(Integer merchantTag,Integer containsType,String containsStr,String merchantCode,String parentCode);

    /**
     * 更新商户提前结算开关
     */
    Response<ResponseEnum> updateMerchantSettleSwitchAdvance(MerchantVO merchantVO, Integer userId, String language,String ip,String sportId);

    /**
     * 更新商户提前结算开关
     */
    Response<ResponseEnum> isTestOrExternal(MerchantVO merchantVO, Integer userId, String language ,String ip);
    Response<ResponseEnum> isApp(MerchantVO merchantVO, Integer userId, String language ,String ip);

    void updateMerchantMarketData(TagMarketMsg map);

    /**
     * 对内商户后台-商户中心-商户管理-修改超级管理员名称
     * @param adminUserNameUpdateReqVO
     * @param language
     * @return Response
     */
    Response updateAdminUserName(HttpServletRequest request,AdminUserNameUpdateReqVO adminUserNameUpdateReqVO, String language, String ip,String agentLevel);

    void merchantInfoExport(HttpServletRequest request, MerchantVO merchantVO, String language);
    void merchantInfoKanaCodeExport(HttpServletRequest request, MerchantVO merchantVO, String language);

    List<MerchantVO> queryListByParam(MerchantVO merchantVO);

    int updateApiDomainByMerchantCode( String newDomain, String merchantCode);

    int updateMerchantDomainByMerchantCode(Integer domainType,String newDomain,String oldDomain, String merchantCode);

    List<?> queryMerchantListByGroup( String merchantGroupId,Integer status);

    void updateMerchantGroupIdDefult(String id);

    int updateMerchantGroupId(String id, String groupCode, List<String> merchantCodes);

    List<?> list(MerchantVO merchantVO);

    APIResponse merchantGroupInfoByGroupId(Long merchantGroupId);

    APIResponse load(Integer id);

    int checkMerchantDomainExistByDomainType(String domain,Integer domainType);


    Response updateOpenBill(MerchantVO merchantVO, String language, String ip);

    /**
     * 获取商户信息
     * @return
     */
    List<?> getMerchantList();

    List<?> getMerchantByCodes(List<String> merchantCode);

    /**
     * 获取商户信息
     * @return
     */
    List<?> getTblMerchantList();

    List<?> getTblMerchantByCodes(List<String> merchantCode);

    int queryMerchantCountByGroup(String merchantGroupId, Integer status);

    int updateMerchantDomainByMerchantCodes(String merchantGroupId, JSONArray domainVos);

    APIResponse updateLanguage(MerchantVO merchantVO);

    APIResponse getLanguageById(String id);

    APIResponse getMerchantGroupDomain(MerchantGroupDomainVO req);

    APIResponse queryAnimation();

    List<?> getMerchantByDomains(String domain);

    String getAnimationURL();

    int updateAnimationURL(String url);

    APIResponse updateDefaultVideoDomain(HttpServletRequest request, MerchantConfig merchantConfig);

    APIResponse queryDefaultVideoDomain();

    APIResponse deleteVideoDomainCache();

    APIResponse saveOperationLog(OperationLogVO operationLogVO);

    APIResponse executeMerchantCurrency();

    APIResponse changeMerchantName(MultipartFile file,HttpServletRequest request) throws IOException;

    APIResponse changeMerchantDomain(DomainChangeDTO domainChangeDto);

    APIResponse<Object> queryMerchantDomain(FrontendMerchantDomain merchantDomain);

    APIResponse createFrontendMerchantGroup(FrontendMerchantGroup merchantGroup);

    APIResponse updateFrontendMerchantGroup(HttpServletRequest request, FrontendMerchantGroup merchantGroup);

    APIResponse queryFrontendMerchantGroup(FrontendMerchantGroupDomainPO groupDomainPo);

    APIResponse createFrontendDomain(FrontendMerchantDomain merchantDomain);

    APIResponse delFrontendDomain(FrontendMerchantDomain merchantDomain);

    Response updateMerchantEvent(MerchantEventVO merchantEventVO);

    Response updateMerchantEventSwitch(MerchantEventVO merchantEventVO);

    MerchantEventVO getMerchantEventSwitch(MerchantEventVO merchantEventVO);

    List<?> getMerchantByName(String merchantName);

    List<MerchantVO> queryMerchantListByParams(JSONObject param);

    List<MerchantSimpleVO> queryMerchantSimpleListByParams(JSONObject param);

    void exportMerchantKey(List<String> codeList);

    APIResponse addMerchantKey(HttpServletRequest request, MerchantKeyVO merchantKeyVO);

    APIResponse enableMerchantKey(HttpServletRequest request, MerchantKeyVO merchantKeyVO);

    APIResponse sendMongoMsg(MerchantKeyVO merchantKeyVO);
}
