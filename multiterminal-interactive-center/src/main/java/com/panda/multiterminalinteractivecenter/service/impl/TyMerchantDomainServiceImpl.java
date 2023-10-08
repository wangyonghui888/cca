package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import com.panda.multiterminalinteractivecenter.config.NacosThirdEnableConfig;
import com.panda.multiterminalinteractivecenter.dto.DomainDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainImportReqDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamDto;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamTYDTO;
import com.panda.multiterminalinteractivecenter.entity.Domain2DomainGroup;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.exception.BusinessException;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.feign.MerchantManageClient;
import com.panda.multiterminalinteractivecenter.mapper.Domain2DomainGroupMapper;
import com.panda.multiterminalinteractivecenter.mapper.DomainGroupMapper;
import com.panda.multiterminalinteractivecenter.mapper.MerchantGroupMapper;
import com.panda.multiterminalinteractivecenter.mapper.TyDomainMapper;
import com.panda.multiterminalinteractivecenter.po.MerchantPO;
import com.panda.multiterminalinteractivecenter.service.MerchantDomainService;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.utils.HttpClient;
import com.panda.multiterminalinteractivecenter.vo.DomainGroupVO;
import com.panda.multiterminalinteractivecenter.vo.DomainVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.api.DomainApiVO;
import com.panda.multiterminalinteractivecenter.vo.api.DomainGroupApiVO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 *  ty 体育service实现
 */
@Service(value = "tyMerchantDomainServiceImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RefreshScope
public class TyMerchantDomainServiceImpl extends ServiceImpl<TyDomainMapper, TyDomain> implements MerchantDomainService {

    @Value("${nacos.api.url:null}")
    private String nacosUrl;

    @Value("${nacos.api.namespace:null}")
    private String nacosNameSpace;

    private final NacosThirdEnableConfig nacosThirdEnableConfig;

    private final MultiterminalConfig multiterminalConfig;

    private final MerchantApiClient merchantApiClient;

    private final MerchantManageClient merchantManageClient;

    private final TyDomainMapper tyDomainMapper;

    private final DomainGroupMapper domainGroupMapper;

    private final Domain2DomainGroupMapper domain2DomainGroupMapper;

    private final MerchantGroupMapper merchantGroupMapper;

    private final MerchantLogService merchantLogService;

    public APIResponse<?> cleanMerchant(String merchantCode) {

        Object obj = merchantApiClient.kickoutMerchant(merchantCode);
        log.info("踢出商户:" + obj);
        return APIResponse.returnSuccess();
    }



    @Override
    public APIResponse<?> pageList(Integer page, Integer size, String domainName, Integer domainType, Long domainGroupId,
                                   String domainGroupName, Long lineCarrierId, Integer groupType, Boolean used, String tab) {
        return this.pageListCore(page,size,domainName,domainType,domainGroupId,domainGroupName,lineCarrierId,groupType,used,tab,null);
    }

    public APIResponse<?> pageListCore(Integer page, Integer size, String domainName, Integer domainType, Long domainGroupId,
                                   String domainGroupName, Long lineCarrierId, Integer groupType, Boolean used, String tab,List<Integer> domainTypes) {
        PageHelper.startPage(page, size, true);
        List<TyDomain> list = tyDomainMapper.pageList(page, size, domainName, domainType, domainGroupId, domainGroupName, lineCarrierId, groupType, used, tab,domainTypes);

        if (CollectionUtil.isNotEmpty(list)) {
            List<TyDomain.DomainGroupDTO> domainGroupDetailList;
            for (TyDomain tyDomain : list) {
                domainGroupDetailList = Lists.newArrayList();
                String domainGroupDetail = tyDomain.getDomainGroupDetail();
                if (StringUtils.isNotBlank(domainGroupDetail)) {
                    List<TyDomain.DomainGroupDTO> finalDomainGroupDetailList = domainGroupDetailList;
                    Arrays.stream(domainGroupDetail.split(",")).forEach(
                            domainGroupDetailO -> {
                                String[] domainGroupStr = domainGroupDetailO.split(":");
                                finalDomainGroupDetailList.add(TyDomain.DomainGroupDTO.builder().domainGroupId(domainGroupStr[0]).domainGroupName(domainGroupStr[1]).build());
                            }
                    );
                }
                tyDomain.setDomainGroupDTOList(domainGroupDetailList);
            }
        }

        PageInfo<TyDomain> poList = new PageInfo<>(list);
        return APIResponse.returnSuccess(poList);
    }


    @Override
    public void create(TyDomain tyDomain) {
        Integer groupType = DomainTypeEnum.paramCanInDifferentGroupType(tyDomain.getDomainType())?tyDomain.getGroupType():null;
        int count = tyDomainMapper.countByName(tyDomain.getDomainName(),groupType, null);
        if (count != 0) {
            throw new BusinessException("该域名已存在!");
        }
        long now = System.currentTimeMillis();
        tyDomain.setCreateTime(now);
        tyDomain.setUpdateTime(now);
        tyDomain.setStatus(0);
        tyDomain.setSelfTestTag(1);
        tyDomain.setEnable(2);
        baseMapper.insert(tyDomain);
    }

    @Override
    public void edit(TyDomain tyDomain, HttpServletRequest request) {
        Integer groupType = DomainTypeEnum.paramCanInDifferentGroupType(tyDomain.getDomainType())?tyDomain.getGroupType():null;
        int count = tyDomainMapper.countByName(tyDomain.getDomainName(),groupType, tyDomain.getId());
        if (count != 0) {
            throw new BusinessException("该域名已存在");
        }
        TyDomain oldDomain = baseMapper.selectById(tyDomain.getId());
        tyDomain.setUpdateTime(System.currentTimeMillis());
        tyDomain.setUpdateUser(tyDomain.getUpdateUser());
        baseMapper.updateById(tyDomain);
        if(request != null) {
            MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(BeanUtil.toBean(oldDomain, DomainVO.class), BeanUtil.toBean(tyDomain, DomainVO.class));
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.EDIT, filedVO,  null,
                    tyDomain.getDomainName() + StringPool.AMPERSAND + DomainTypeEnum.getNameByCode(tyDomain.getDomainType()) + StringPool.AMPERSAND +
                            MerchantGroupEnum.getNameByKey(tyDomain.getGroupType())+ StringPool.AMPERSAND + tyDomain.getId(), request);
        }
    }

    public void delete(Long id,String tab, HttpServletRequest request) {
        TyDomain oldDomain = baseMapper.selectById(id);
        validate(id, "删除",tab);
        tyDomainMapper.deleteById(id);

        tyDomainMapper.deleteByDomainId(id);
        MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(BeanUtil.toBean(oldDomain, DomainDTO.class), new DomainDTO());
        filedVO.setAfterValues(Collections.singletonList("-"));
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.DEL, filedVO,  null,
                oldDomain.getDomainName() + StringPool.AMPERSAND + DomainTypeEnum.getNameByCode(oldDomain.getDomainType()) + StringPool.AMPERSAND +
                        MerchantGroupEnum.getNameByKey(oldDomain.getGroupType())+ StringPool.AMPERSAND + oldDomain.getId(), request);
    }

    public void off(List<Long> id, String tab, HttpServletRequest request, String dataId){
        tyDomainMapper.offDomain(id);
        //操作日志
        List<TyDomain> list = tyDomainMapper.getDomainListByIds(id, tab);
        String beforeValue = com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(list) ? list.stream().map(TyDomain::getDomainName).collect(Collectors.joining(",")) : null;
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("soldOut"), beforeValue,null );
        String[] data = dataId.split(StringPool.AMPERSAND);
        filedVO.setMerchantName(data[0]);
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_SOLD_OUT, MerchantLogTypeEnum.DOMAIN_SOLD_OUT, filedVO,  null,
                dataId, request);
    }


    @Override
    public void importDomains(DomainImportReqDTO domainDTO) {
        List<String> domains = domainDTO.getDomainName();

        String msg = validateDomains(domainDTO.getDomainType(), domainDTO.getGroupType(), domains);
        if (StringUtils.isNotBlank(msg)) {
            throw new BusinessException(msg);
        }

        long now = System.currentTimeMillis();
        TyDomain insertTyDomain = new TyDomain();

        // 这里产品要求默认开启
        insertTyDomain.setStatus(1);
        insertTyDomain.setEnable(DomainEnableEnum.WAIT_USE.getCode());
        insertTyDomain.setCreateTime(now);
        insertTyDomain.setUpdateTime(now);
        insertTyDomain.setSelfTestTag(1);

        insertTyDomain.setCreateUser(domainDTO.getOperator());
        insertTyDomain.setUpdateUser(domainDTO.getOperator());
        insertTyDomain.setDomainType(domainDTO.getDomainType());
        insertTyDomain.setGroupType(domainDTO.getGroupType());
        insertTyDomain.setLineCarrierId(domainDTO.getLineCarrierId());
        insertTyDomain.setTab(StringUtils.defaultIfBlank(domainDTO.getTab(), "ty"));

        domains.forEach(
                domainName -> {
                    domainName = domainName.trim();
                    if (StringUtils.isNotBlank(domainName)) {
                        insertTyDomain.setDomainName(domainName);
                        baseMapper.insert(insertTyDomain);
                    }
                }
        );
    }

    private String validateDomains(Integer domainType, Integer groupType, List<String> domainList) {
        if (CollectionUtil.isEmpty(domainList)) {
            throw new BusinessException("域名数据不能为空！");
        }
        List<String> domains = domainList.stream().distinct().collect(Collectors.toList());
        String groupName = GroupTypeEnum.getStrByType(groupType);
        StringBuilder resultsb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        StringBuilder sb4 = new StringBuilder();
        for (String domain : domains) {
            if (StringUtils.isBlank(domain)) {
                continue;
            }
            if (domainType == 3 && multiterminalConfig.getApiDomainValidate()) {
                // API域名需要校验
                String result;
                try {
                    result = HttpClient.restTemplate.getForObject(domain,String.class);
                } catch (Exception e) {
                    sb1.append(domain).append(",");
                    continue;
                }
                if (StringUtils.isBlank(result) || !"ok".equals(result.substring(0, 2))) {
                    sb2.append(domain).append(",");
                    continue;
                }
                String currentGroupCode = result.substring(2, 3);
                currentGroupCode = StringUtils.isBlank(currentGroupCode) || "\n".equals(currentGroupCode) ?
                        "common" : currentGroupCode.toLowerCase();
                if (!Objects.equals(groupType, GroupTypeEnum.getTypeIntByCode(currentGroupCode))) {
                    sb3.append(domain).append(",");
                    continue;
                }
                // 添加t_merchant表校验
                List<?> merchants = merchantManageClient.getMerchantByDomains(domain);
                if (CollectionUtils.isNotEmpty(merchants)) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<MerchantPO> merchantPOList = mapper.convertValue(merchants, new TypeReference<List<MerchantPO>>(){});
                    List<Long> merchantGroupCodeList = merchantPOList.stream().map(MerchantPO::getMerchantGroupId)
                            .filter(Objects::nonNull).distinct().collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(merchantGroupCodeList)){
                        List<Long> merchantGroupIdList = merchantGroupMapper.getMerchantNotInParamGroup(merchantGroupCodeList,groupType);
                        if(CollectionUtils.isNotEmpty(merchantGroupIdList)){
                            String merchantCodeStr = merchantPOList
                                    .stream()
                                    .filter(m->merchantGroupIdList.contains(m.getMerchantGroupId()))
                                    .map(MerchantPO::getMerchantCode)
                                    .collect(Collectors.joining(","));
                            return String.format("域名【%s】已在商户【%s】存在，且商户不是%s组，请检查!",domain,merchantCodeStr,currentGroupCode);
                        }
                    }
                }
            }
            // 查重复
            Integer groupTypeParam =  DomainTypeEnum.paramCanInDifferentGroupType(domainType)?groupType:null;
            int count = tyDomainMapper.countByName(domain, groupTypeParam,null);
            if (count != 0) {
                sb4.append(domain).append(",");
            }
        }
        if (StringUtils.isNotBlank(sb1.toString()) && sb1.toString().length() > 1) {
            sb1.deleteCharAt(sb1.length()-1);
            resultsb.append("【").append(sb1).append("】此域名不存在，无法导入！\n");
        }
        if (StringUtils.isNotBlank(sb2.toString()) && sb2.toString().length() > 1){
            sb2.deleteCharAt(sb1.length()-1);
            resultsb.append("【").append(sb2).append("】此域名不是API域名，无法导入！\n");
        }
        if (StringUtils.isNotBlank(sb3.toString()) && sb3.toString().length() > 1) {
            sb3.deleteCharAt(sb3.length()-1);
            resultsb.append("【").append(sb3).append("】此域名不属于").append(groupName).append(",无法导入！\n");
        }
        if (StringUtils.isNotBlank(sb4.toString()) && sb4.toString().length() > 1) {
            sb4.deleteCharAt(sb4.length()-1);
            resultsb.append("【").append(sb4).append("】此域名已存在，无法导入！\n");
        }

        return resultsb.toString();
    }

    @Transactional
    @Override
    public void replace(Long oldDomainId, Long domainId,String tab, HttpServletRequest request) {
        TyDomain oldTyDomain = tyDomainMapper.selectById(oldDomainId);
        TyDomain tyDomain = tyDomainMapper.selectById(domainId);
        if (Objects.isNull(oldTyDomain)) {
            throw new BusinessException("域名不存在或已删除");
        }
        List<Domain2DomainGroup> domain2DomainGroup = domain2DomainGroupMapper.selectByDomainId(oldDomainId,tab);

        if (CollectionUtil.isEmpty(domain2DomainGroup)) {
            throw new BusinessException("当前域名【" + oldTyDomain.getDomainName() + "】没有域名组使用");
        }

        domain2DomainGroupMapper.replaceByDomainId(oldDomainId, domainId,tab);
        //记录操作日志
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("replaceDomain"), oldTyDomain.getDomainName(), tyDomain.getDomainName());
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.DOMAIN_SELECT, filedVO,  null,
                tyDomain.getDomainName() + StringPool.AMPERSAND + DomainTypeEnum.getNameByCode(tyDomain.getDomainType()) + StringPool.AMPERSAND +
                        MerchantGroupEnum.getNameByKey(tyDomain.getGroupType())+ StringPool.AMPERSAND + tyDomain.getId(), request);

        /**
         * 处理域名状态  ：  这里产品要求
         * a是待使用的   b过来也是待使用
         * a是使用中的   b过来也是使用中
         * a是其他状态的时候  b过来 还是 b的状态,但是不可用要改为可用
         */
        if(oldTyDomain.getEnable().equals(DomainEnableEnum.USED.getCode())){
            oldTyDomain.setEnable(DomainEnableEnum.WAIT_USE.getCode());
            tyDomainMapper.updateById(oldTyDomain);
            tyDomain.setEnable(DomainEnableEnum.USED.getCode());
            tyDomainMapper.updateById(tyDomain);
        }else {
            tyDomain.setEnable(DomainEnableEnum.WAIT_USE.getCode());
            tyDomainMapper.updateById(tyDomain);
        }

        // 替换掉common库在使用此域名的商户为target
        Integer domainType = oldTyDomain.getDomainType();
        int[] needEditTypes = {1, 2, 3};
        if (Arrays.stream(needEditTypes).anyMatch(n -> n == domainType)) {
            // 异步替换商户正在使用的域名
            CompletableFuture.runAsync(() -> {
                String source = oldTyDomain.getDomainName();
                String target = tyDomain.getDomainName();
                log.info("异步替换域名开始：{},source:{},target:{}", DateUtil.now(), source, target);
                JSONObject param = new JSONObject();
                param.put("source", source);
                param.put("target", target);
                param.put("domainType", domainType);
                merchantApiClient.replace(param);
            });
        }
    }

    @Override
    public void switchStatus(TyDomain tyDomain) {
        long id = tyDomain.getId();
        int status = tyDomain.getStatus();
        int enable = tyDomain.getEnable();
        if (status == 0) {
            if(enable == DomainEnableEnum.USED.getCode()){
                throw new BusinessException("正在使用的域名不可关闭！");
            }
            validate(id, "关闭",tyDomain.getTab());
        } else {
            if (enable != DomainEnableEnum.USED.getCode()) enable = DomainEnableEnum.WAIT_USE.getCode();
        }
        tyDomainMapper.switchStatus(id, status, enable, System.currentTimeMillis(),tyDomain.getUpdateUser());

    }

    @Override
    public void switchSelfTestTag(TyDomain tyDomain) {
        tyDomain.setUpdateTime(System.currentTimeMillis());
        tyDomainMapper.switchSelfTestTag(tyDomain);
    }

    private void validate(Long domainId, String msg,String tab) {
        if (Objects.isNull(domainId)) {
            throw new BusinessException("域名不存在或已删除");
        }

        TyDomain tyDomain = tyDomainMapper.selectById(domainId);

        if(Objects.equals(tyDomain.getEnable(), DomainEnableEnum.USED.getCode())){
            throw new BusinessException("正在使用的域名不可"+msg);
        }

        List<Domain2DomainGroup> domain2DomainGroups = domain2DomainGroupMapper.selectByDomainId(domainId,tab);

        if (CollectionUtil.isNotEmpty(domain2DomainGroups)) {

            List<Long> ids = domain2DomainGroups.stream().map(Domain2DomainGroup::getDomainGroupId).collect(Collectors.toList());

            List<DomainGroupVO> domainGroupVOList = domainGroupMapper.getDomainGroupByIds(ids, tab);

            for (DomainGroupVO domainGroupVO : domainGroupVOList) {

                int domainCount = domain2DomainGroupMapper.countByGroupId(domainGroupVO.getId(),tab);
                long alarmThreshold = domainGroupVO.getThreshold(tyDomain.getDomainType());
                if (domainCount - 1 < alarmThreshold) {
                    throw new BusinessException("此域名无法" + msg + "，" + msg + "后此域名所在域名组【" + domainGroupVO.getDomainGroupName() + "】将低于预警值");
                }

            }
        }
    }

    @Override
    public List<?> getDomainByMerchantAndArea(Long merchantGroupId, String domainGroupCode) {
        List<DomainGroupApiVO> result = Lists.newArrayList();
        String tab = TabEnum.TY.getName();
        try {
            List<DomainGroupApiVO> dglist = tyDomainMapper.getDomainGroupByMerchant(merchantGroupId, GroupTypeEnum.getTypeIntByCode(domainGroupCode),tab);
            if (CollectionUtil.isNotEmpty(dglist)) {
                for (DomainGroupApiVO dg : dglist) {
                    List<DomainApiVO> dls = tyDomainMapper.getDomainByDomainGroupId(dg.getId(), GroupTypeEnum.getTypeIntByCode(domainGroupCode));
                    if (CollectionUtil.isNotEmpty(dls)) {
                        dg.setDomainls(dls);
                    }
                }
            }
            result.addAll(dglist);
            log.info("getDomainByMerchantAndArea, merchantgroupid:{}, domaingroupcode:{}, result:{}", merchantGroupId, domainGroupCode, result);
            return result;
        } catch (Exception e) {
            log.error(merchantGroupId + "getDomainByMerchantAndArea异常：" + domainGroupCode, e);
            return result;
        }
    }

    @Override
    public List<TyDomain> getNewDomainByGroupId(DomainSqlParamTYDTO domainSqlParamTYDTO) {
        return tyDomainMapper.getNewDomainByGroupId(domainSqlParamTYDTO);
    }

    @Override
    public List<TyDomain> getDomainListByLineId(Long id, String tab) {
        return tyDomainMapper.getDomainListByLineId(id,tab);
    }

    @Override
    public List<TyDomain> getDomainListByNames(List<String> domainNameList,String tab) {
        return tyDomainMapper.getDomainListByNames(domainNameList,tab);
    }

    @Override
    public List<TyDomain> getDomainListByIds(List<Long> domainIdList,String tab) {
        return tyDomainMapper.getDomainListByIds(domainIdList,tab);
    }

    public Boolean getThirdEnable() {
        return "true".equalsIgnoreCase(nacosThirdEnableConfig.getThirdEnable());
    }

    public void editThirdEnable(Boolean enable, HttpServletRequest request) {
        log.info("publish: thirdEnable:17ce：【{}】", enable);
        List<String> lines = getNacosConfig();
        if (CollectionUtil.isEmpty(lines)) {
            throw new BusinessException("17ce配置读取错误！");
        }
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            String[] tempSTrAr = line.split("=");
            String key = tempSTrAr[0];
            if (key.equalsIgnoreCase("websocket.17ce.enable")) {
                sb.append("websocket.17ce.enable=").append(enable).append("\r\n\r\n");
            } else {
                sb.append(line).append("\r\n\r\n");
            }
        }
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("dataId", "merchant-config.properties");
        params.add("tenant", nacosNameSpace);
        params.add("group", "merchant");
        params.add("type", "properties");
        params.add("content", sb.toString());
        Mono<String> result = WebClient.create().post()
                .uri(nacosUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .retrieve().bodyToMono(String.class);
        log.info("publish:{}", result.block());
        String beforeValue = enable ? "关" : "开";
        String afterValue = enable ? "开" : "关";
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("17ce"), beforeValue, afterValue);
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_MANAGE, MerchantLogTypeEnum.EDIT_INFO_STATUS, filedVO,  null,
                "", request);
    }

    private List<String> getNacosConfig() {
        String url = nacosUrl + "?tenant=" + nacosNameSpace + "&dataId=merchant-config.properties&group=merchant&type=properties";
        Mono<?> mono = WebClient.create().get().uri(url).retrieve().bodyToMono(String.class);
        log.info("result:" + mono.block());
        String configStr = (String) mono.block();
        if (StringUtils.isBlank(configStr)) {
            return Lists.newArrayList();
        }
        return Arrays.stream(configStr.split("\\r?\\n")).collect(Collectors.toList());
    }

    public APIResponse getNewH5PcDomain() {
        try {
            List<String> newDomainList = tyDomainMapper.getNewH5PcDomain();
            if (CollectionUtil.isEmpty(newDomainList)) {
                return APIResponse.returnFail("暂无新域名数据!");
            }
            return APIResponse.returnSuccess(newDomainList) ;
        } catch (Exception ex) {
            log.error( "getNewH5PcDomain:" + ex);
            return APIResponse.returnFail("getNewH5PcDomain获取新域名数据失败!");
        }
    }

    @Override
    public List<TyDomain> getDomainByDomainByParam(DomainSqlParamDto domainSqlParamDto, TyDomain domain){
        return tyDomainMapper.getDomainByDomainByParam(domainSqlParamDto,domain);
    }

    @Override
    public TyDomain selectById(Long id){
        return tyDomainMapper.selectById(id);
    }
}