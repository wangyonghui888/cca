package com.panda.multiterminalinteractivecenter.feign;


import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.DomainChangeDTO;
import com.panda.multiterminalinteractivecenter.dto.MaintenanceRecordDTO;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantDomain;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroup;
import com.panda.multiterminalinteractivecenter.entity.FrontendMerchantGroupDomainPO;
import com.panda.multiterminalinteractivecenter.vo.MerchantVO;
import com.panda.multiterminalinteractivecenter.vo.OperationLogVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantSimpleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "merchant-manage", fallbackFactory = MerchantManageHystrix.class)
public interface MerchantManageClient {

    @PostMapping("/manage/notice/sendStartMaintainRemindAndNoticeToSport")
    Object sendStartMaintainRemindAndNoticeToSport(@RequestBody MaintenanceRecordDTO maintenanceRecordDTO);

    @GetMapping("/manage/task/checkInDomainEnable")
    void checkInDomainEnable()  throws InterruptedException;

    @GetMapping(value ="/manage/merchant/updateApiDomainByMerchantCode")
    int updateApiDomainByMerchantCode(@RequestParam(value = "newDomain") String newDomain, @RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping(value ="/manage/merchant/updateMerchantDomainByMerchantCode")
    int updateMerchantDomainByMerchantCode( @RequestParam(value = "domainType") Integer domainType, @RequestParam(value = "newDomain") String newDomain,@RequestParam(value = "oldDomain")String oldDomain, @RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping("/manage/merchant/queryMerchantListByGroup")
    List<?> queryMerchantListByGroup(@RequestParam(value = "merchantGroupId") String merchantGroupId,@RequestParam(value = "status") Integer status);

    @GetMapping(value = "/manage/merchant/updateMerchantGroupIdDefult")
    void updateMerchantGroupIdDefult(@RequestParam(value = "id") String id);

    @GetMapping(value = "/manage/merchant/updateMerchantGroupId")
    int updateMerchantGroupId(@RequestParam(value = "id") String id, @RequestParam(value = "groupCode") String groupCode,@RequestParam(value = "merchantCodes") List<String> merchantCodes);


    @PostMapping(value = "/manage/merchant/selectList")
    List<?> selectList(@RequestBody MerchantVO merchantVO);


    @GetMapping(value = "/manage/merchant/merchantGroupInfoByGroupId")
    APIResponse merchantGroupInfoByGroupId(@RequestParam(value = "merchantGroupId") Long merchantGroupId);

    @GetMapping(value = "/manage/merchant/load")
    APIResponse load(@RequestParam(value = "id")  Integer id);

    @GetMapping(value = "/manage/merchant/checkMerchantDomainExistByDomainType")
    int checkMerchantDomainExistByDomainType(@RequestParam(value = "domain") String domain,@RequestParam(value = "domainType") Integer domainType);

    @GetMapping(value = "/manage/merchant/getMerchantList")
    List<?> getMerchantList();

    @GetMapping(value = "/manage/merchant/getMerchantByCodes")
    List<?> getMerchantByCodes(@RequestParam(value = "merchantCode") List<String> merchantCode);

    @GetMapping(value = "/manage/merchant/getMerchantByName")
    List<String> getMerchantByName(@RequestParam(value = "merchantName") String merchantName);

    @GetMapping(value = "/manage/merchant/getTblMerchantList")
    List<?> getTblMerchantList();

    @GetMapping(value = "/manage/merchant/getTblMerchantByCodes")
    List<?> getTblMerchantByCodes(@RequestParam(value = "merchantCode") List<String> merchantCode);


    /**
     * @describe 根据商户组id和状态，获取商户组下商户的数量
     * @function queryMerchantCountByGroup
     */
    @GetMapping("/manage/merchant/queryMerchantCountByGroup")
    int queryMerchantCountByGroup(@RequestParam(value = "merchantGroupId")String merchantGroupId,@RequestParam(value = "status") int status);

    /**
     * @describe 调用manage服务，根据商户组id 更新所有商户的域名，同时清除缓存
     * @function updateMerchantDomainByMerchantCodes
     */
    @PostMapping(value = "/manage/merchant/updateMerchantDomainByMerchantCodes")
    int updateMerchantDomainByMerchantCodes(@RequestBody JSONObject param);

    @GetMapping(value = "/manage/merchant/getMerchantByDomains")
    List<?> getMerchantByDomains(@RequestParam(value = "domain") String domain);

    @GetMapping(value = "/manage/merchant/getAnimationURL")
    String getAnimationURL();

    @GetMapping(value = "/manage/merchant/updateAnimationURL")
    int updateAnimationURL(@RequestParam(value = "url") String url);

    @PostMapping(value = "/manage/merchant/queryMerchantDomain")
    APIResponse queryMerchantDomain(@RequestBody FrontendMerchantDomain merchantDomain);

    @PostMapping(value = "/manage/merchant/changeMerchantDomain")
    APIResponse changeMerchantDomain(@RequestBody DomainChangeDTO domainChangeDto);

    @PostMapping(value = "/manage/merchant/saveOperationLog")
    APIResponse saveOperationLog(@RequestBody OperationLogVO operationLogVO);

    @PostMapping(value = "/manage/merchant/createFrontendMerchantGroup")
    APIResponse createFrontendMerchantGroup(@RequestBody FrontendMerchantGroup merchantGroup);

    @PostMapping(value = "/manage/merchant/updateFrontendMerchantGroup")
    APIResponse updateFrontendMerchantGroup(@RequestBody FrontendMerchantGroup merchantGroup);

    @PostMapping(value = "/manage/merchant/createFrontendDomain")
    APIResponse createFrontendDomain(@RequestBody FrontendMerchantDomain merchantDomain);

    @PostMapping(value = "/manage/merchant/delFrontendDomain")
    APIResponse delFrontendDomain(@RequestBody FrontendMerchantDomain merchantDomain);

    @PostMapping(value = "/manage/merchant/queryFrontendMerchantGroup")
    APIResponse queryFrontendMerchantGroup(@RequestBody FrontendMerchantGroupDomainPO groupDomainPo);

    @PostMapping(value = "/manage/merchant/queryMerchantListByParams")
    List<com.panda.sport.merchant.common.vo.merchant.MerchantVO> queryMerchantListByParams(@RequestBody JSONObject param);

    @PostMapping(value = "/manage/merchant/queryMerchantSimpleListByParams")
    List<MerchantSimpleVO> queryMerchantSimpleListByParams(@RequestBody JSONObject param);
}

