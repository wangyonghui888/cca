package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import com.panda.multiterminalinteractivecenter.constant.DomainConstants;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.dto.DomainResDTO;
import com.panda.multiterminalinteractivecenter.entity.*;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.feign.MerchantManageClient;
import com.panda.multiterminalinteractivecenter.mapper.*;
import com.panda.multiterminalinteractivecenter.po.MerchantGroupPO;
import com.panda.multiterminalinteractivecenter.po.MerchantPO;
import com.panda.multiterminalinteractivecenter.service.IMongoService;
import com.panda.multiterminalinteractivecenter.service.MerchantGroupServiceTransfer;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.BeanCopierUtils;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.utils.HttpConnectionPool;
import com.panda.multiterminalinteractivecenter.utils.TelegramBot;
import com.panda.multiterminalinteractivecenter.vo.*;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RefreshScope
@Service("dj_cp")
public class CpDjMerchantGroupServiceImpl implements MerchantGroupServiceTransfer {
    @Autowired
    private IMongoService mongoService;

    @Autowired
    private MerchantManageClient merchantManageClient;

    @Autowired
    private CpDjMerchantGroupInfoMapper cpDjMerchantGroupInfoMapper;

    @Autowired
    private DomainGroupRelationMapper domainGroupRelationMapper;

    @Autowired
    private DomainGroupMapper domainGroupMapper;

    @Autowired
    private CpDjMerchantGroupMapper cpDjMerchantGroupMapper;

    @Autowired
    private CpDjDomainMapper cpDomainMapper;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private DomainProgramMapper domainProgramMapper;

    @Autowired
    private CpDjMerchantGroupServiceImpl merchantGroupService;

    @Autowired
    private MultiterminalConfig config;

    @Autowired
    private MerchantGroupMapper merchantGroupMapper;

    private static final String CP_PATH = "/sabang/pulic/replaceStaticSite";
    private static final String DJ_PATH = "/v1/domain/update";


    @Value("${cp.merchant.url:http://test-admin.emkcp.com}")
    private String cpConfigUrl;

    @Value("${dj.merchant.url:http://www.phiqui.com}")
    private String djConfigUrl;

    //所属环境
    @Value("${env.title:test}")
    private String envTitle;

    @Autowired
    private TelegramBot telegramBot;


    @Override
    public List<ThirdMerchantVo> getMerchantList() {
        List<?> list = merchantManageClient.getMerchantList();
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<ThirdMerchantVo> merchantList = mapper.convertValue(list, new TypeReference<List<ThirdMerchantVo>>() {
        });
        for (ThirdMerchantVo vo : merchantList){
            vo.setCreatTime(vo.getCreatedAt().getTime() / 1000);
        }
        return merchantList;
    }

    /**
     * @param  merchantCode 商户名称
     * @param  domainType   域名类型
     * @param  domainUrl          域名地址
     * @param  isVip          0否1是
     * @param  ipArea        ip地址
     * @param  changeType     1自检切换2手动切换
     * @see com.panda.multiterminalinteractivecenter.constant.DomainConstants
     */
    @Override
    public void sendCPMsg(String merchantCode, Integer domainType, String domainUrl,Boolean isVip ,String ipArea,int changeType) {
        // 单独处理H5，PC域名下发只下发一个
        if(domainType.equals(DomainTypeEnum.H5.getCode())||domainType.equals(DomainTypeEnum.PC.getCode())){
            if(domainUrl.contains("|")) domainUrl= domainUrl.split("\\|")[0];
        }
        int vip = isVip ? 1 : 0;
        String city = ipArea.equals("默认") ? "" : ipArea;
        String mongoText = "【" + envTitle + "】\n"  + " 彩票域名切换%s！\n merchantCode:【%s】\n type:【%s】\n domainUrl:【%s】\n isVip:【%s】\n ipArea:【%s】\n msg:【%s】\n 请求地址:【%s】";

        try {
            city = URLEncoder.encode(city,"UTF-8");
            domainUrl = URLEncoder.encode(domainUrl,"UTF-8");

            String params = "?merchantAccount="+merchantCode+"&type="+domainType+"&url="+domainUrl+"&isVip="+vip+"&ipArea="+city;
            String url = cpConfigUrl + CP_PATH + params;
            log.info("调用彩票接口，通知CP切换域名，请求路径：【{}】,请求参数：【merchantCode:{}】,【domainType:{}】," +
                            "【domainUrl:{}】,【isVip:{}】,【ipArea:{}】",
                    url,merchantCode,domainType,domainUrl,vip,city);

            HttpHeaders headers = new HttpHeaders();
            headers.set("clientId", "internal service");
            MultiValueMap<HttpEntity, Object> param = new LinkedMultiValueMap<>();

            ResponseEntity<String> response = HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(param, headers),String.class);

            HttpStatus httpStatus = response.getStatusCode();

            if (200 == httpStatus.value()) {
                String successMsg = String.format(mongoText, "成功", merchantCode, domainType, domainUrl, vip, city, response.getBody(), cpConfigUrl + CP_PATH);
                mongoService.send(successMsg);
                telegramBot.sendGroupMessage(successMsg);
            }else {
                String failMsg = String.format(mongoText, "失败", merchantCode, domainType, domainUrl, vip, city, response, cpConfigUrl + CP_PATH);
                mongoService.send(failMsg);
                telegramBot.sendGroupMessage(failMsg);
            }
        } catch (Exception e) {
            log.error("请求异常{}，请求路径{}",e.getMessage(),cpConfigUrl,e);
            String exceptionMsg = String.format(mongoText, "失败", merchantCode, domainType, domainUrl, vip, city, e.getMessage(), cpConfigUrl + CP_PATH);
            mongoService.send(exceptionMsg);
            telegramBot.sendGroupMessage(exceptionMsg);
        }
    }

    /**
     * @param  merchantCode 商户名称
     * @param  domainType   域名类型
     * @param  domainUrl    域名地址
     * @param  changeType   1自检切换2手动切换
     * @see com.panda.multiterminalinteractivecenter.constant.DomainConstants
     */
    @Override
    public void sendDJMsg(String merchantCode, Integer domainType, String domainUrl,Integer changeType) {
        String params = "?merchantAccount="+merchantCode+"&type="+domainType+"&changeType="+changeType+"&url="+domainUrl;
        String url = djConfigUrl + DJ_PATH + params;
        log.info("调用电竞接口，通知DJ切换域名，请求路径：【{}】,请求参数：【merchantCode:{}】【changeType:{}：{}】",
                "GET: "+url,merchantCode,changeType,changeType==1?"自动切换":"手动切换");

        String mongoText = "【" + envTitle + "】\n"  + " 电竞域名切换%s!\n merchantCode:【%s】\n domainType:【%s】\n domainUrl:【%s】\n changeType:【%s】\n msg:【%s】\n 请求地址:【%s】";

        try {
//            CloseableHttpClient httpClient = HttpClientUtil.getSystemHttpClient(config.getHttpProxySwitch());
//
//            HttpGet httpGet = new HttpGet(url);
//
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//
//            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//            if (response.getStatusLine().getStatusCode() == 200) {
//                mongoService.send(String.format(mongoText,"成功",merchantCode,domainType,domainUrl,changeType,msg,url));
//                telegramBot.sendGroupMessage(String.format(mongoText,"成功",merchantCode,domainType,domainUrl,changeType,msg,url));
//            }else {
//                mongoService.send(String.format(mongoText,"失败",merchantCode,domainType,domainUrl,changeType,msg,url));
//                telegramBot.sendGroupMessage(String.format(mongoText,"失败",merchantCode,domainType,domainUrl,changeType,msg,url));
//            }

            ResponseEntity<String> response = HttpConnectionPool.restTemplate.getForEntity(url,String.class);

            HttpStatus httpStatus = response.getStatusCode();

            if (200 == httpStatus.OK.value()) {
                mongoService.send(String.format(mongoText,"成功",merchantCode,domainType,domainUrl,changeType,response.getBody(),url));
                telegramBot.sendGroupMessage(String.format(mongoText,"成功",merchantCode,domainType,domainUrl,changeType,response.getBody(),url));

            }else {
                mongoService.send(String.format(mongoText,"失败",merchantCode,domainType,domainUrl,changeType,response,url));
                telegramBot.sendGroupMessage(String.format(mongoText,"失败",merchantCode,domainType,domainUrl,changeType,response,url));
            }

        } catch (Exception e) {
            log.error("请求异常"+e.getMessage() +"param=" + url,e);
            mongoService.send(String.format(mongoText,"异常",merchantCode,domainType,domainUrl,changeType,e.getMessage(),url));
            telegramBot.sendGroupMessage(String.format(mongoText,"异常",merchantCode,domainType,domainUrl,changeType,e.getMessage(),url));
        }
    }


    @Override
    public List<ThirdMerchantVo> getTblMerchantList(Integer code){
        List<?> merchants =  merchantManageClient.getTblMerchantList();
        if (CollectionUtil.isEmpty(merchants)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<ThirdMerchantVo> merchantList = mapper.convertValue(merchants, new TypeReference<List<ThirdMerchantVo>>() {
        });

        return  getThirdMerchantVos(code, merchantList);
    }

    private List<ThirdMerchantVo> getThirdMerchantVos(Integer code, List<ThirdMerchantVo> merchantList) {
        List<ThirdMerchantVo> result = new ArrayList<>();
        if (code != null) {
            List<MerchantPO> self = cpDjMerchantGroupInfoMapper.getAllByCode(code);
            List<TMerchantGroupInfo> merchantGroupInfos = new ArrayList<>();
            self.forEach(merchantPO -> {
                TMerchantGroupInfo tMerchantGroupInfo = new TMerchantGroupInfo();
                BeanCopierUtils.copyProperties(merchantPO,tMerchantGroupInfo);
                merchantGroupInfos.add(tMerchantGroupInfo);
            });
            if (CollectionUtil.isNotEmpty(merchantGroupInfos)) {
                List<String> names = merchantGroupInfos.stream().map(TMerchantGroupInfo::getMerchantName).collect(Collectors.toList());
                for (ThirdMerchantVo vo : merchantList) {
                    if (!names.contains(vo.getMerchantName())) {
                        result.add(vo);
                    }
                }
            } else {
                result.addAll(merchantList);
            }
        }else {
            result.addAll(merchantList);
        }
        return result;
    }

    /**
     * 获取分组域名
     * @param merchantGroupCode
     * @return
     */
    public APIResponse getMerchantGroup(Integer merchantGroupCode){
        List<TMerchantGroup> merchantGroups = cpDjMerchantGroupMapper.selectAll(merchantGroupCode);
        if (CollectionUtil.isNotEmpty(merchantGroups)){
            for (TMerchantGroup merchantGroup : merchantGroups) {
                merchantGroup.setMerchantList(cpDjMerchantGroupInfoMapper.tMerchantGroupInfoByGroupId(merchantGroup.getId()));
            }
        }
        return APIResponse.returnSuccess(merchantGroups);
    }

    @Override
    public List<MerchantGroupVO> selectMerchantGroup(MerchantGroupVO merchantGroupPO) {
        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);
        if (!StrUtil.isBlank(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        List<MerchantGroupPO> list = cpDjMerchantGroupMapper.selectMerchantGroup(groupPO);
        List<MerchantGroupVO> groupList = Lists.newArrayList();
        list.stream().forEach(po -> {
            MerchantGroupVO vo = new MerchantGroupVO();
            vo.setId(String.valueOf(po.getId()));
            BeanUtils.copyProperties(po, vo);
            if (!Objects.equals(vo.getId(), "0")) {
                List<TMerchantGroupInfo> merchantPOs = cpDjMerchantGroupInfoMapper.tMerchantGroupInfoByGroupId(Long.valueOf(vo.getId()));
                if (CollectionUtil.isNotEmpty(merchantPOs)) {
                    List<MerchantResultVO> merchantList = merchantPOs.stream().map(mp -> {
                        MerchantResultVO resultVO = new MerchantResultVO();
                        BeanUtils.copyProperties(mp, resultVO);
                        resultVO.setId(String.valueOf(mp.getId()));
                        resultVO.setMerchantGroupId(Long.valueOf(mp.getMerchantGroupId()));
                        return resultVO;
                    }).collect(Collectors.toList());
                    vo.setMerchantList(merchantList);
                }
            }

            if (Objects.isNull(vo.getProgramId())) {
                //查询商户组默认方案
                vo.setTab(merchantGroupPO.getTab());
                DomainProgramVO domainProgramVO = cpDjMerchantGroupMapper.findProgram(vo);
                vo.setProgramId(domainProgramVO.getId());
                vo.setProgramName(domainProgramVO.getProgramName());

                //修改商户组默认方案初始化
                MerchantGroupPO merchantGroupPO1 = new MerchantGroupPO();
                merchantGroupPO1.setId(Long.valueOf(po.getId()));
                merchantGroupPO1.setProgramId(domainProgramVO.getId());
                merchantGroupPO1.setTab(merchantGroupPO.getTab());
                cpDjMerchantGroupMapper.updateMerchantGroup(merchantGroupPO1);
            }

            DomainVO domainVo = new DomainVO();
            domainVo.setMerchantGroupId(po.getId().toString());
            domainVo.setEnable(1);
            domainVo.setTab(merchantGroupPO.getTab());
            List<TDomain> list1 = cpDomainMapper.selectAll(domainVo);
            if (CollectionUtil.isNotEmpty(list1)) {
                vo.setDomain(list1.get(0).getDomainName());
            }
            groupList.add(vo);
        });

        return groupList;
    }

    @Override
    public APIResponse updateMerchantDomain(DomainVO domainVo, String userName, String ip) {
        try {
            //获取新的域名信息
            List<DomainVO> domainVOS = domainVo.getConfig();

            String merchantName = "SYSTEM";
            MerchantGroupPO merchantGroupPO = cpDjMerchantGroupInfoMapper.selectMerchantGroupById(Long.valueOf(domainVo.getMerchantGroupId()),domainVo.getTab());
            if(merchantGroupPO != null){
                merchantName = merchantGroupPO.getGroupName();
            }
            for (DomainVO domainVO1 : domainVOS) {
                if (StrUtil.isBlank(domainVO1.getNewDomainName())) {
                    continue;
                }
                //重置原域名enable =2, status =1
                cpDomainMapper.reset2Domain(domainVO1.getOldDomainId(), domainVo.getTab());
                String[] newDomainIds = domainVO1.getNewDomainId().split(",");
                    Arrays.stream(newDomainIds).forEach(newDomainId -> {
                        //更新新域名 enable = 1, status = 1
                        cpDomainMapper.updateDomainEnableTimeById(Long.valueOf(newDomainId), System.currentTimeMillis(), domainVo.getTab());


                    });

                    String[] newDomainNames = domainVO1.getNewDomainName().split(",");
                String finalMerchantName = merchantName;
                Arrays.stream(newDomainNames).forEach(newDomainName -> {
                        // 记录手动切换域名日志
                        MerchantLogFiledVO vo = new MerchantLogFiledVO();
                        List<String> fieldName = new ArrayList<>();
                        fieldName.add("domain check");
                        vo.setFieldName(fieldName);
                        List<String> beforeValues = new ArrayList<>();
                        beforeValues.add(JSON.toJSONString(domainVO1.getOldDomainName()));
                        vo.setBeforeValues(beforeValues);
                        List<String> afterValues = new ArrayList<>();
                        afterValues.add(JSON.toJSONString(newDomainName));
                        vo.setAfterValues(afterValues);
                        vo.setDomainType(domainVO1.getDomainType());

                        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP,
                                MerchantLogTypeEnum.MANUAL_SWITCH, vo,
                                MerchantLogConstants.MERCHANT_IN, "0",
                                userName, finalMerchantName,
                                finalMerchantName, finalMerchantName + StringPool.AMPERSAND + domainVo.getMerchantGroupId(), "zs", ip,
                                null, null, domainVO1.getDomainType());
                    });

                    if(domainVo.getTab().equalsIgnoreCase(TabEnum.DJ.getName())){
                        sendDJMsg(merchantName,domainVO1.getDomainType(),domainVO1.getNewDomainName(),DomainConstants.DOMAIN_CHANGE_MANUAL);
                    }else{
                        this.clearCPCache(Long.valueOf(domainVo.getMerchantGroupId()),null);
                    }
                }
            return APIResponse.returnSuccess();
        } catch (Exception ex) {
            log.error("手动切换域名失败:", ex);
            return APIResponse.returnFail("手动切换域名失败!");
        }
    }

    private int getTypeByArea(String areaName) {
        if(areaName.equalsIgnoreCase("VIP")){
            return  2;
        }else if(areaName.equalsIgnoreCase("默认")){
            return 0;
        }
            return 1;
    }


    @Override
    public APIResponse getMerchantGroupDomainRelationDataList(MerchantDomainVO merchantDomainVO) {
        try {
            List<MerchantDomainVO> merchantDomainVOS = cpDomainMapper.getMerchantGroupDomainRelationDataList(merchantDomainVO);

            //初始化没有手动切换域名数据
            if (CollectionUtil.isEmpty(merchantDomainVOS)) {
                // 域名组为空
                merchantDomainVOS = cpDomainMapper.getDomainGroupByProgramId(merchantDomainVO.getProgramId(),merchantDomainVO.getTab());
            }else{
                // 查询包含域名组 但数据为空的数据
                MerchantDomainVO merchantDomainVOTemplate = merchantDomainVOS.get(0);
               List<Long> domainGroupIdList= merchantDomainVOS.stream().map(MerchantDomainVO::getDomainGroupId).distinct().collect(Collectors.toList());
                List<Map<String,Object>> domainGroupIdList1 = cpDomainMapper.getDomainGroupListByMerchantCode(merchantDomainVO,domainGroupIdList);
                if(CollectionUtils.isNotEmpty(domainGroupIdList1)){
                    for (Map<String,Object> domainGroup : domainGroupIdList1) {
                        merchantDomainVOS.add(
                                MerchantDomainVO.builder()
                                        .domainGroupId( Objects.isNull(domainGroup.get("domainGroupId")) ? null :Long.valueOf(domainGroup.get("domainGroupId").toString()))
                                        .areaName(Objects.isNull(domainGroup.get("areaName")) ? "" : domainGroup.get("areaName").toString())
                                        .programId(merchantDomainVO.getProgramId())
                                        .merchantGroupId(merchantDomainVOTemplate.getMerchantGroupId())
                                        .groupCode(merchantDomainVOTemplate.getGroupCode())
                                        .groupName(merchantDomainVOTemplate.getGroupName())
                                        .groupType(merchantDomainVOTemplate.getGroupType())
                                        .tab(merchantDomainVOTemplate.getTab())
                                        .build());
                    }
                }
            }

            //去重
            merchantDomainVOS = merchantDomainVOS.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(c ->
                            String.format("%s_%s_%s", c.getAreaName(), c.getDomainType(), c.getDomainName())))), ArrayList::new));
            List<MerchantDomainVO> result = extractedMerchantDomainList(merchantDomainVOS,merchantDomainVO.getTab());
            //手动切换选择域名时过滤域名类型为空的数据
            List<MerchantDomainVO>  merchantDomainVOList = result.stream().filter(e-> Objects.nonNull(e.getDomainType()))
                    .collect(Collectors.toList());

            return APIResponse.returnSuccess(merchantDomainVOList);
        } catch (Exception ex) {
            log.error("获取商户组下的域名，失败:", ex);
            return APIResponse.returnFail("获取商户组下的域名失败");
        }
    }

    /**
     * 提取商户组关联域名数据
     * @param merchantDomainVOS
     */
    private List<MerchantDomainVO> extractedMerchantDomainList(List<MerchantDomainVO> merchantDomainVOS,String tab) {
        List<MerchantDomainVO> result = Lists.newArrayList();

        Map<String,List<MerchantDomainVO>> domainVOMap = merchantDomainVOS.stream().filter(m->StringUtils.isNotBlank(m.getAreaName())).collect(Collectors.groupingBy(MerchantDomainVO::getAreaName));

        int threshold = tab.equalsIgnoreCase(TabEnum.DJ.getName())
                ?DomainConstants.DJ_DOMAINS_THRESHOLD
                :DomainConstants.CP_DOMAINS_THRESHOLD;

        for(Map.Entry<String,List<MerchantDomainVO>> entry : domainVOMap.entrySet()){
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.H5.getCode(),tab);
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.PC.getCode(),tab);
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.APP.getCode(),tab);
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.IMAGE.getCode(),tab);
            buildDomainVOS(entry.getKey(),entry.getValue(),DomainTypeEnum.OTHER.getCode(),tab);
            result.addAll(entry.getValue());
        }
        return result;
    }

    /**
     * 初始化没有默认数据无法手动切换数据
     * @param areaName
     * @param domainVOS
     * @param domainType
     */
    private void buildDomainVOS(String areaName, List<MerchantDomainVO> domainVOS, int domainType,String tab) {
        int suffix =  DomainConstants.getSuffixByDomainType(domainType,tab);

        List<MerchantDomainVO> result = domainVOS.stream().filter(domainVo-> Objects.equals(domainVo.getDomainType(), domainType)).collect(Collectors.toList());
        if(result.size()== suffix){}
        else if(result.size() > suffix){
            // 删
            domainVOS.removeIf(domainVo-> Objects.equals(domainVo.getDomainType(), domainType));
            for (int i = 0; i < suffix; i++) {
                domainVOS.add(result.get(i));
            }
        }else{
            // 补
            MerchantDomainVO domainVOTemp = domainVOS.get(0);
            MerchantDomainVO domainVO =
                    MerchantDomainVO
                            .builder()
                            .merchantGroupId(domainVOTemp.getMerchantGroupId())
                            .domainType(domainType)
                            .domainGroupId(domainVOTemp.getDomainGroupId())
                            .groupCode(domainVOTemp.getGroupCode())
                            .groupName(domainVOTemp.getGroupName())
                            .programId(domainVOTemp.getProgramId())
                            .areaName(areaName)
                            .build();
//            MerchantDomainVO domainVOTemp = new MerchantDomainVO();
//            BeanUtils.copyProperties(result.get(0),domainVOTemp);
//            domainVOTemp.setDomainId(null);
//            domainVOTemp.setDomainName(null);
//            domainVOS.add(domainVOTemp);
            for (int i = result.size(); i < suffix; i++) {
                domainVOS.add(domainVO);
            }
        }
    }


    @Override
    public APIResponse getDomainNameList(DomainGroupVO domainGroupVO) {
        try {
            return APIResponse.returnSuccess(cpDomainMapper.getDomainNameList(domainGroupVO));
        } catch (Exception ex) {
            log.error("获取域名名称，失败:", ex);
            return APIResponse.returnFail("获取域名名称失败");
        }
    }


    @Override
    public APIResponse<?> selectDomain(Integer page, Integer size, String domainName, Integer domainType, Long domainGroupId,
                                       String domainGroupName, Long lineCarrierId, Integer groupType, Boolean used, String tab,Long programId,String areaName) {
        try {
            // 限制域名组
            if(StringUtils.isNotBlank(areaName) && (domainGroupId == null || domainGroupId==0)){
                if(areaName.equalsIgnoreCase("VIP")){
                    domainGroupId = cpDomainMapper.getVIPDomainGroupIdByProgramId(programId);
                }else if(areaName.equalsIgnoreCase("默认")){
                    domainGroupId = cpDomainMapper.getDefaultDomainGroupIdByProgramId(programId);
                }else{
                    domainGroupId = cpDomainMapper.getDomainGroupIdByProgramId(programId,areaName);
                }
            }
            if (page == null) page = 1;
            if (size == null) size = 200;
            PageHelper.startPage(page, size, true);

            List<TyDomain> list = cpDomainMapper.selectDomainList(page, size, domainName, domainType, domainGroupId,
                    domainGroupName, lineCarrierId, groupType, used, tab,programId);

            if (CollectionUtil.isNotEmpty(list)) {
                List<TyDomain.DomainGroupDTO> domainGroupDetailList;
                for (TyDomain tyDomain : list) {
                    domainGroupDetailList = Lists.newArrayList();
                    String domainGroupDetail = tyDomain.getDomainGroupDetail();
//                    String allDomainGroupDetail = tyDomain.getAllDomainGroupDetail();
                    if (org.apache.commons.lang.StringUtils.isNotBlank(domainGroupDetail)) {
                        List<TyDomain.DomainGroupDTO> finalDomainGroupDetailList = domainGroupDetailList;
                        Arrays.stream(domainGroupDetail.split(",")).forEach(
                                domainGroupDetailO -> {
                                    String[] domainGroupStr = domainGroupDetailO.split(":");
                                    finalDomainGroupDetailList.add(TyDomain.DomainGroupDTO.builder().domainGroupId(domainGroupStr[0]).domainGroupName(domainGroupStr[1]).build());
                                }
                        );
                    }
                    tyDomain.setDomainGroupDTOList(domainGroupDetailList);
//                    if (StringUtils.isNotBlank(allDomainGroupDetail)) {
//                        String [] arr = allDomainGroupDetail.split(",");
//                        tyDomain.setNotRecommended(arr.length != 1);
//                    }else{
//                        tyDomain.setNotRecommended(false);
//                    }
                }
            }

            PageInfo<TyDomain> poList = new PageInfo<>(list);
            return APIResponse.returnSuccess(poList);
        } catch (Exception ex) {
            log.error("选择域名，失败:", ex);
            return APIResponse.returnFail("选择域名失败");
        }
    }

    @Override
    public APIResponse updateMerchantGroup(MerchantGroupVO merchantGroupPO, HttpServletRequest request) {
        TMerchantGroup groupPO = new TMerchantGroup();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);
        if (!StrUtil.isBlank(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        // 已经被选择的方案，不能被其他商户组选
        int selectCount = cpDjMerchantGroupMapper.getCountByProgramId(merchantGroupPO);
        if(selectCount > 0){
            return APIResponse.returnFail("该方案已经被其他商户组选择");
        }
        if(merchantGroupPO.getTab().equalsIgnoreCase(TabEnum.DJ.getName())){
            merchantGroupService.sendDJMsg("",0,"",DomainConstants.DOMAIN_CHANGE_MANUAL);
        }else{
            this.clearCPCache(null,merchantGroupPO.getProgramId());
        }

        return this.createMerchantGroup(groupPO, request);
    }

    @Override
    public APIResponse deleteMerchantGroup(MerchantGroupVO merchantGroupPO, HttpServletRequest request) {
        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);
        if (StringUtils.isNotEmpty(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        MerchantGroupPO oldPO = cpDjMerchantGroupInfoMapper.selectMerchantGroupById(groupPO.getId(),null);
        int num = cpDjMerchantGroupMapper.deleteMerchantGroup(groupPO);
        if (num > 0) {
            cpDomainMapper.updateDomainByMerchantGroupId(groupPO.getId().toString(),merchantGroupPO.getTab());
            cpDjMerchantGroupInfoMapper.deleteByGroupId(groupPO.getId());

            MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(oldPO, new MerchantGroupPO());
            filedVO.setMerchantName(oldPO.getGroupName());
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP, MerchantLogTypeEnum.DEL, filedVO,  null,
                    oldPO.getGroupName() + StringPool.AMPERSAND + oldPO.getId(), request);
            return APIResponse.returnSuccess();
        }
        log.info("deleteMerchantGroup:" + num);
        return APIResponse.returnFail("删除商户组失败！");
    }

    @Override
    public APIResponse createMerchantGroup(TMerchantGroup tMerchantGroup, HttpServletRequest request) {

        Map result  = new HashMap();
        if (tMerchantGroup.getId() != null){
            result.put("id",tMerchantGroup.getId());
            //修改禁用状态和其他信息

            if(tMerchantGroup.getProgramId()!=null){
                int resultCode = domainProgramMapper.hasDefaultDomainGroup(tMerchantGroup.getProgramId(),tMerchantGroup.getTab());
                if(resultCode==0){
                    return APIResponse.returnFail("该域名方案没有默认域名组，不可选则");
                }
            }
            MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT;
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            Integer type = tMerchantGroup.getType();
            List<String> afters = new ArrayList<>();
            List <String> before = new ArrayList<>();
            MerchantGroupPO oldPO = cpDjMerchantGroupMapper.selectMerchantGroupById(tMerchantGroup.getId(), tMerchantGroup.getTab());
            cpDjMerchantGroupMapper.update(tMerchantGroup);

            // 0 选择商户 1 切换方案 2 启用/禁用 3切换频率
             type = Objects.isNull(type) ? 3 : type;
            if(type == 0 ) {
                filedVO.setFieldName(Arrays.asList("商户编码","商户名称","商户类型"));
                List<TMerchantGroupInfo> merchantPOs = cpDjMerchantGroupInfoMapper.tMerchantGroupInfoByGroupId(tMerchantGroup.getId());
                if (CollectionUtil.isNotEmpty(merchantPOs)) {
                    before.add(merchantPOs.stream().map(m -> m.getMerchantCode() + StringPool.AMPERSAND + m.getMerchantName()).collect(Collectors.joining(",")));
                }
            }else if(type == 1 && tMerchantGroup.getProgramId() != null) {
                filedVO.getFieldName().add(MerchantLogTypeEnum.CUT_PROGRAM.getRemark());
                typeEnum = MerchantLogTypeEnum.CUT_PROGRAM;
                DomainProgram old = domainProgramMapper.selectById(oldPO.getProgramId());
                DomainProgram newPO = domainProgramMapper.selectById(tMerchantGroup.getProgramId());
                before.add(old != null ? old.getProgramName() : null);
                afters.add(newPO != null ? newPO.getProgramName() : null);
            }else if(type == 2) {
                filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("status"));
                typeEnum = MerchantLogTypeEnum.EDIT_INFO_STATUS;
                before.add(1==oldPO.getStatus()?"开":"关");
                afters.add(1==tMerchantGroup.getStatus()?"开":"关");
            }else if(type == 3) {
                if(!oldPO.getTimes().equals(tMerchantGroup.getTimes()) || !oldPO.getTimeType().equals(tMerchantGroup.getTimeType())) {
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("changeNum"));
                    before.add("每" + oldPO.getTimes() + getAfterOp(oldPO.getTimeType()));
                    afters.add("每" + tMerchantGroup.getTimes() + getAfterOp(tMerchantGroup.getTimeType()));
                }
                if(!oldPO.getGroupName().equals(tMerchantGroup.getGroupName())) {
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("groupName"));
                    before.add(oldPO.getGroupName());
                    afters.add(tMerchantGroup.getGroupName());
                }
            }

            //修改商户信息
            if (CollectionUtil.isNotEmpty(tMerchantGroup.getMerchantCodes())){
                List<ThirdMerchantVo> merchantList = new ArrayList<>();
                if(Objects.equals(TransferEnum.CP.getCode(),tMerchantGroup.getTab())) {
                    List<?> merchants = merchantManageClient.getMerchantByCodes(tMerchantGroup.getMerchantCodes());
                    if (CollectionUtil.isEmpty(merchants)) {
                        return null;
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    merchantList = mapper.convertValue(merchants, new TypeReference<List<ThirdMerchantVo>>() {
                    });
                }

                if(Objects.equals(TransferEnum.DJ.getCode(),tMerchantGroup.getTab())) {
                    List<?> merchants = merchantManageClient.getTblMerchantByCodes(tMerchantGroup.getMerchantCodes());
                    if (CollectionUtil.isEmpty(merchants)) {
                        return null;
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    merchantList = mapper.convertValue(merchants, new TypeReference<List<ThirdMerchantVo>>() {
                    });
                }

                log.info("有详细数据开始新增! size ={}", tMerchantGroup.getMerchantCodes().size());
                cpDjMerchantGroupInfoMapper.deleteByGroupId(tMerchantGroup.getId());
                for (ThirdMerchantVo vo : merchantList) {
                    TMerchantGroupInfo merchantGroup = new TMerchantGroupInfo();
                    merchantGroup.setCreatTime(vo.getCreatTime());
                    merchantGroup.setMerchantCode(vo.getId());
                    merchantGroup.setMerchantName(vo.getMerchantName());
                    merchantGroup.setMerchantGroupId(tMerchantGroup.getId().intValue());
                    merchantGroup.setOperator("SYS");
                    merchantGroup.setUpdateTime(System.currentTimeMillis());
                    cpDjMerchantGroupInfoMapper.insert(merchantGroup);
                }
                before.add(merchantList.stream().map(m -> m.getMerchantCode() + StringPool.AMPERSAND + m.getMerchantName()).collect(Collectors.joining(",")));
            }
            filedVO.setBeforeValues(before);
            filedVO.setAfterValues(afters);
            filedVO.setMerchantName(tMerchantGroup.getGroupName());
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP, typeEnum, filedVO,  null,
                    tMerchantGroup.getGroupName() + StringPool.AMPERSAND + tMerchantGroup.getId(), request);
            return APIResponse.returnSuccess(result);
        }


        //创建商户组信息
        tMerchantGroup.setUpdateTime(System.currentTimeMillis());
        TMerchantGroup m = cpDjMerchantGroupMapper.loadIdByGroupName(tMerchantGroup.getGroupName(),tMerchantGroup.getGroupCode());
        if (m !=null){
            return APIResponse.returnFail("存在相同分组名称！");
        }
        Integer count  = cpDjMerchantGroupMapper.insert(tMerchantGroup);
        MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(new TMerchantGroup(), tMerchantGroup);
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_GROUP, MerchantLogTypeEnum.SAVE, filedVO,  null,
                tMerchantGroup.getGroupName(), request);

        if (count > 0){
            result.put("id", cpDjMerchantGroupMapper.loadIdByGroupName(tMerchantGroup.getGroupName(),tMerchantGroup.getGroupCode()).getId());
            if (CollectionUtil.isNotEmpty(tMerchantGroup.getMerchantGroupInfos())){
                log.info("初始新增 有详细数据开始新增! size ={}", tMerchantGroup.getMerchantGroupInfos().size());
                for (TMerchantGroupInfo vo : tMerchantGroup.getMerchantGroupInfos()) {
                    TMerchantGroupInfo merchantGroup = new TMerchantGroupInfo();
                    merchantGroup.setCreatTime(vo.getCreatTime());
                    merchantGroup.setMerchantCode(vo.getId());
                    merchantGroup.setMerchantName(vo.getMerchantName());
                    merchantGroup.setMerchantGroupId(tMerchantGroup.getId().intValue());
                    merchantGroup.setOperator("SYS");
                    merchantGroup.setUpdateTime(System.currentTimeMillis());
                    cpDjMerchantGroupInfoMapper.insert(merchantGroup);
                }
            }
        }
        return APIResponse.returnSuccess(result);
    }

    private String getAfterOp(Integer timeType){
        String afterOp="";
        switch(timeType){
            case 1 :
                afterOp ="分钟";
                break;
            case 2:
                afterOp ="小时";
                break;
            case 3 :
                afterOp ="日";
                break;
            case 4:
                afterOp ="月";
                break;
        }

        return afterOp;
    }
    @Override
    public APIResponse delProgramRelationByDomainGroupId(MerchantGroupVO merchantGroupVO, HttpServletRequest request) {
        int num = domainGroupRelationMapper.delProgramRelationByDomainGroupId(merchantGroupVO.getProgramId(),merchantGroupVO.getDomainGroupId());
        if (num > 0) {
            DomainGroup domainGroup = domainGroupMapper.selectById(merchantGroupVO.getDomainGroupId());
            DomainProgram program = domainProgramMapper.selectById(merchantGroupVO.getProgramId());
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            filedVO.getFieldName().add("删除");
            filedVO.getBeforeValues().add(domainGroup.getDomainGroupName());
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_PROGRAM_INFO, MerchantLogTypeEnum.DEL, filedVO,  null,
                    program.getProgramName() + StringPool.AMPERSAND + merchantGroupVO.getProgramId(), request);
            return APIResponse.returnSuccess();
        }
        log.info("delProgramRelationByDomainGroupId:" + num);
        return APIResponse.returnFail("删除方案关系数据失败！");
    }

    @Override
    public List<ThirdMerchantVo> getMerchantList(Integer code){
        List<ThirdMerchantVo> merchantList = getMerchantList();
        return  getThirdMerchantVos(code, merchantList);
    }

    /**商户组id或者方案id二者传一即可*/
    public void clearCPCache(Long merchantGroupId,Long programId) {

        List<MerchantDomainVO> merchantDomainVOS = cpDomainMapper.getMerchantGroupDomainRelationDataList(
                MerchantDomainVO
                        .builder()
                        .merchantGroupId(merchantGroupId)
                        .programId(programId==null?"":programId.toString())
                        .tab(TabEnum.CP.getName())
                        .build());
        // 手动切换
        if(CollectionUtils.isNotEmpty(merchantDomainVOS)){
            for (MerchantDomainVO domainVO : merchantDomainVOS) {
                int type = getTypeByArea(domainVO.getAreaName());
                DomainResDTO domainResDTO = cpDjMerchantGroupMapper
                        .getDomainDetails4AccountId(TabEnum.CP.getName(),type, domainVO.getMerchantAccount(),domainVO.getAreaName(),null);
                if(domainResDTO!=null){
                    sendCPMsg(domainVO.getGroupName(),domainVO.getDomainType(),domainResDTO.getDomainByType(domainVO.getDomainType()),
                            domainVO.getAreaName().equalsIgnoreCase("VIP"),
                            domainVO.getAreaName(), DomainConstants.DOMAIN_CHANGE_MANUAL);
                }
            }
        }
    }

    @Override
    public APIResponse getMerchantGroupDomain(MerchantGroupDomainVO req) {
        List<MerchantGroupDomainVO> result = Lists.newArrayList();
        List<MerchantGroupDomainVO> list = merchantGroupMapper.getMerchantGroupDomain(req);
        if(CollectionUtils.isNotEmpty(list)){
            Set<String> merchantGroupList = list.stream().map(MerchantGroupDomainVO::getGroupName).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(merchantGroupList)){
                for (String item : merchantGroupList){
                    MerchantGroupDomainVO resultVO = new MerchantGroupDomainVO();
                    resultVO.setGroupName(item);
                    result.add(resultVO);
                }
                for (MerchantGroupDomainVO itemResult : result){
                    Set<String> merchantCodeSet = new HashSet<>();
                    Set<DomainInfoVO> domainList = new HashSet<>();
                    for (MerchantGroupDomainVO itemList : list){
                        if(itemResult.getGroupName().equals(itemList.getGroupName())){
                            DomainInfoVO domainInfo = new DomainInfoVO();
                            itemResult.setGroupType(itemList.getGroupType());
                            merchantCodeSet.add(itemList.getMerchantCode());
                            domainInfo.setAreaName(itemList.getAreaName());
                            domainInfo.setDomainType(itemList.getDomainType());
                            domainInfo.setDomainName(itemList.getDomainName());
                            domainInfo.setId(itemList.getDomainId());
                            domainList.add(domainInfo);
                        }
                    }
                    itemResult.setMerchantCodeSet(merchantCodeSet);
                    itemResult.setDomainInfo(new ArrayList<>(domainList));
                }
            }
        }
        return APIResponse.returnSuccess(result);
    }

    @Override
    public APIResponse editThreshold(MerchantGroupVO req, HttpServletRequest request) {
        return null;
    }

    @Override
    public APIResponse getThreshold(MerchantGroupVO req) {
        return null;
    }

}