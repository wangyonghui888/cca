package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.constant.CPDomain;
import com.panda.multiterminalinteractivecenter.constant.DomainConstants;
import com.panda.multiterminalinteractivecenter.dto.DomainReqDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainResDTO;
import com.panda.multiterminalinteractivecenter.dto.DomainResDTO4DJ;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.enums.TabEnum;
import com.panda.multiterminalinteractivecenter.exception.BusinessException;
import com.panda.multiterminalinteractivecenter.feign.MerchantManageClient;
import com.panda.multiterminalinteractivecenter.mapper.*;
import com.panda.multiterminalinteractivecenter.po.MerchantGroupPO;
import com.panda.multiterminalinteractivecenter.po.MerchantPO;
import com.panda.multiterminalinteractivecenter.service.IDomainService;
import com.panda.multiterminalinteractivecenter.utils.RedisTemp;
import com.panda.multiterminalinteractivecenter.vo.TMerchantGroupInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 域名 服务实现类
 * </p>
 *
 * @author ifan
 * @since 2022-07-11
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DomainServiceImpl extends ServiceImpl<TyDomainMapper, TyDomain> implements IDomainService {

    /**新版本商户组页面 上方域名的KEY*/
    private static final String Live_H5_URL_KEY = "live_h5";
    private static final String Live_PC_URL_KEY = "live_pc";
    private static final String CHATROOM_API_URL_KEY = "chatroom_api_url";


    private static final String DOMAIN_SUFFIX = "\\|";
    private static final String DOMAIN_SUFFIX2 = "|";



    private final MerchantGroupMapper merchantGroupMapper;
    private final CpDjMerchantGroupMapper cpDjMerchantGroupMapper;
    private final CpDjMerchantGroupInfoMapper cpDjMerchantGroupInfoMapper;

    private final MerchantManageClient merchantManageClient;

    @Override
    public APIResponse queryChatroomDomain() {
        String chatRoomUrl = RedisTemp.get(CHATROOM_API_URL_KEY);
        return APIResponse.returnSuccess(chatRoomUrl);
    }

    @Override
    public APIResponse updateChatroomDomain(String url) {
        if (StrUtil.isBlank(url)) {
            return APIResponse.returnFail("聊天室链接不能为空");
        }
        try {
            RedisTemp.delete(CHATROOM_API_URL_KEY);
            RedisTemp.set(CHATROOM_API_URL_KEY, url);
            log.info("的缓存值:{}", RedisTemp.getString(CHATROOM_API_URL_KEY));
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("聊天室链接域名更新异常！", e);
            return APIResponse.returnFail("聊天室链接域名更新异常！");
        }
    }

    @Override
    public APIResponse deleteChatRoomCache() {
        try {
            String livePcUrl = RedisTemp.get(CHATROOM_API_URL_KEY);
            RedisTemp.delete(CHATROOM_API_URL_KEY);
            RedisTemp.set(CHATROOM_API_URL_KEY, livePcUrl);
            log.info("{}的缓存值:{}",CHATROOM_API_URL_KEY, RedisTemp.getString(CHATROOM_API_URL_KEY));
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("清除缓存异常！", e);
            return APIResponse.returnFail("清除缓存异常！");
        }
    }

    @Override
    public APIResponse updateAnimation(String url) {
        if (StrUtil.isBlank(url)) {
            return APIResponse.returnFail("动画的链接不能为空");
        }
        try {
//            RedisTemp.delete("Animation3Url_hdpn");
            //tDomainMapper.updateAnimation(url);
//            RedisTemp.set("Animation3Url_hdpn", url);
            merchantManageClient.updateAnimationURL(url);
            log.info("updateAnimationDomain:{}", url);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("动画更新异常！", e);
            return APIResponse.returnFail("动画更新异常！");
        }
    }

    /**
     * 清除缓
     *
     * @return
     */
    @Override
    public APIResponse deleteAniCache() {
        try {

            String animationUrl = RedisTemp.get("Animation3Url_hdpn");
            RedisTemp.delete("Animation3Url_hdpn");
            //String animationUrl = tDomainMapper.selectAnimation3Url();
            RedisTemp.set("Animation3Url_hdpn", animationUrl);
            log.info("Animation3Url_hdpn的缓存值:{}", RedisTemp.getString("Animation3Url_hdpn"));
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("清除缓存异常！", e);
            return APIResponse.returnFail("清除缓存异常！");
        }
    }


    /**
     * 清除缓
     *
     * @return
     */
    @Override
    public APIResponse deleteH5Cache() {
        try {

            String liveH5Url = RedisTemp.get(Live_H5_URL_KEY);
            RedisTemp.delete(Live_H5_URL_KEY);
            RedisTemp.set(Live_H5_URL_KEY, liveH5Url);
            log.info("视频播放H5的缓存值:{}", RedisTemp.getString(Live_H5_URL_KEY));
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("视频播放H5清除缓存异常！", e);
            return APIResponse.returnFail("视频播放H5清除缓存异常！");
        }
    }

    /**
     * 清除缓
     *
     * @return
     */
    @Override
    public APIResponse deletePcCache() {
        try {

            String livePcUrl = RedisTemp.get(Live_PC_URL_KEY);
            RedisTemp.delete(Live_PC_URL_KEY);
            RedisTemp.set(Live_PC_URL_KEY, livePcUrl);
            log.info("live_pc的缓存值:{}", RedisTemp.getString(Live_PC_URL_KEY));
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("清除缓存异常！", e);
            return APIResponse.returnFail("清除缓存异常！");
        }
    }

    /**
     * 查询域名
     *
     * @return
     */
    @Override
    public APIResponse queryAnimation() {

        String animationUrl = merchantManageClient.getAnimationURL();

        return APIResponse.returnSuccess(animationUrl);
    }

    /**
     * 查询域名
     *
     * @return
     */
    @Override
    public APIResponse queryLiveH5Domain() {

        String liveH5Url = RedisTemp.get(Live_H5_URL_KEY);
        return APIResponse.returnSuccess(liveH5Url);
    }

    @Override
    public APIResponse updateLiveH5Domain(String url) {
        if (StrUtil.isBlank(url)) {
            return APIResponse.returnFail("H5域名链接不能为空");
        }
        try {
            RedisTemp.delete(Live_H5_URL_KEY);
            RedisTemp.set(Live_H5_URL_KEY, url);
            log.info("的缓存值:{}", RedisTemp.getString(Live_H5_URL_KEY));
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("视频播放H5域名更新异常！", e);
            return APIResponse.returnFail("视频播放H5域名更新异常！");
        }
    }

    /**
     * 查询域名
     *
     * @return
     */
    @Override
    public APIResponse queryLivePcDomain() {

        String livePcUrl = RedisTemp.get(Live_PC_URL_KEY);
        return APIResponse.returnSuccess(livePcUrl);
    }

    @Override
    public APIResponse updateLivePcDomain(String url) {
        if (StrUtil.isBlank(url)) {
            return APIResponse.returnFail("PC域名链接不能为空");
        }
        try {
            RedisTemp.delete(Live_PC_URL_KEY);
            //tDomainMapper.updateAnimation(url);
            RedisTemp.set(Live_PC_URL_KEY, url);
            log.info("live_pc的缓存值:{}", RedisTemp.getString(Live_PC_URL_KEY));
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("PC域名更新异常！", e);
            return APIResponse.returnFail("PC域名更新异常！");
        }
    }

    @Override
    public APIResponse getMerchantGroupInfoByThirdCode(String code, String account) {
        Integer groupCode = 0;
        if ("cp".equalsIgnoreCase(code)){
            groupCode = 2;
        }else if ("dj".equalsIgnoreCase(code)){
            groupCode = 1;
        }else {
            return APIResponse.returnFail("input code fail!");
        }
        List<TMerchantGroupInfoVo> merchantGroups = merchantGroupMapper.getMerchantGroupInfoByThirdCode(groupCode,account);
        return APIResponse.returnSuccess(merchantGroups);
    }

    @Override
    public DomainResDTO getCPDomainByMerchantGroupV2(DomainReqDTO domainReqDTO) {
        domainReqDTO.setTab(TabEnum.CP.getName());
        MerchantPO merchantPO = cpDjMerchantGroupInfoMapper.getMerchantInfoByAccount(domainReqDTO);

        if(merchantPO==null){
            throw new BusinessException("此商户不存在！");
        }

        if(domainReqDTO.getIsVip()==null){
            domainReqDTO.setIsVip(false);
        }

        if(domainReqDTO.getIsVip()){
            return assemblyDomainDetail(2,domainReqDTO.getMerchantAccount(),null,null);
        }
        if(StringUtils.isNotBlank(domainReqDTO.getIpArea()) || StringUtils.isNotBlank(domainReqDTO.getAreaCode())){
            return assemblyDomainDetail(1,domainReqDTO.getMerchantAccount(),domainReqDTO.getIpArea(),domainReqDTO.getAreaCode());
        }
        return assemblyDomainDetail(0,domainReqDTO.getMerchantAccount(),null,null);
    }


    /**
     * 根据相关优先级获取域名信息
     * @param type 类型 0默认  1区域  2vip
     * @param merchantAccount 顶级商户name
     * @param ipArea ip解析区域
     */
    private DomainResDTO assemblyDomainDetail(int type, String merchantAccount,String ipArea,String areaCode) {
        String tab = "cp";
        DomainResDTO domainResDTO = null;
        if(type==2){
            domainResDTO = cpDjMerchantGroupMapper.getDomainDetails4AccountId(tab,type,merchantAccount,ipArea,areaCode);
            if(domainResDTO==null) type = 1;
        }
        if(type==1 && (StringUtils.isNotBlank(ipArea) || StringUtils.isNotBlank(areaCode))){
            domainResDTO = cpDjMerchantGroupMapper.getDomainDetails4AccountId(tab,type,merchantAccount,ipArea,areaCode);
            if(domainResDTO==null) type = 0;
        }
        if(domainResDTO!=null)  return subDomainDetail(domainResDTO,tab);
        return subDomainDetail(cpDjMerchantGroupMapper.getDomainDetails4AccountId(tab,type,merchantAccount,ipArea,areaCode),tab);
    }

    /**截取要求的域名数量*/
    private DomainResDTO subDomainDetail(DomainResDTO domainResDTO,String tab) {
        if(!tab.equalsIgnoreCase("cp")){
          return domainResDTO;
        }
        if(domainResDTO==null){
            return null;
        }
        domainResDTO.setPcDomain(subString(domainResDTO.getPcDomain(), DomainConstants.CP_PC_DOMAIN_COUNT));
        domainResDTO.setH5Domain(subString(domainResDTO.getH5Domain(),DomainConstants.CP_H5_DOMAIN_COUNT));
        domainResDTO.setApiDomain(subString(domainResDTO.getApiDomain(),DomainConstants.CP_API_DOMAIN_COUNT));
        domainResDTO.setOssDomain(subString(domainResDTO.getOssDomain(),DomainConstants.CP_OSS_DOMAIN_COUNT));
        domainResDTO.setOtherDomain(subString(domainResDTO.getOtherDomain(),DomainConstants.CP_OTHER_DOMAIN_COUNT));
        return domainResDTO;
    }

    /**删除domain到固定长度*/
    private String subString(String domain, int count) {
        if(StringUtils.isBlank(domain)) return "";

        List<String> domains = Arrays.stream(domain.split(DOMAIN_SUFFIX)).collect(Collectors.toList());

        if(domains.size() <= count) return domain;

        for (int i = domains.size()-1; i >= count; i--) {
            domains.remove(i);
        }

        return String.join(DOMAIN_SUFFIX2, domains);
    }


    @Override
    public DomainResDTO getCPDomainByMerchantGroupV2Test(DomainReqDTO domainReqDTO) {
        if(domainReqDTO.getIsVip()==null){
            domainReqDTO.setIsVip(false);
        }

        String merchantAccount = domainReqDTO.getMerchantAccount();
        if (domainReqDTO.getIsVip()) {
            return assemblyDomains("vip",merchantAccount);
        }
        if (domainReqDTO.getIpArea().equals("北京")) {
            return assemblyDomains("bj",merchantAccount);
        }
        return assemblyDomains("def",merchantAccount);
    }

    @Override
    public List<DomainResDTO4DJ> getDJDomainByMerchantGroupV2(DomainReqDTO domainReqDTO) {
        domainReqDTO.setTab(TabEnum.DJ.getName());
        List<MerchantGroupPO> merchantGroups = cpDjMerchantGroupMapper.selectMerchantGroup(MerchantGroupPO.builder().tab(domainReqDTO.getTab()).groupName(domainReqDTO.getMerchantAccount()).build());
        if(CollectionUtils.isEmpty(merchantGroups)){
            throw new BusinessException("无商户数据！");
        }

        List<DomainResDTO4DJ> domainResDTO4DJS = cpDjMerchantGroupMapper.getDomainIdsByTab(domainReqDTO.getTab(),domainReqDTO.getMerchantAccount());

        return buildDomains(domainResDTO4DJS,domainReqDTO.getTab());

    }

    private List<DomainResDTO4DJ> buildDomains(List<DomainResDTO4DJ> domainResDTO4DJS,String tab) {

        domainResDTO4DJS.forEach(d->{
            if(StringUtils.isNotBlank(d.getDomains())){
                d.setDomains(subString(d.getDomains(),DomainConstants.getSuffixByDomainType(d.getDomainType(),tab)));
            }
        });

        return domainResDTO4DJS;
    }

    private DomainResDTO assemblyDomains(String key,String merchantAccount) {
        Map<String,List<String>> useMap;
        Map<String,List<String>> dataMap;
        if (merchantAccount.equals("bw") || merchantAccount.equals("bob")) {
            useMap = CPDomain.bwUseDomains;
            dataMap = CPDomain.bwDomains;
        }
        else if (merchantAccount.equals("topcp") || merchantAccount.equals("ob02")) {
            useMap = CPDomain.topcpUseDomains;
            dataMap = CPDomain.topcpDomains;
        }else {
            throw new BusinessException("此商户未配置，届时会返回默认");
        }
        if (CollectionUtil.isEmpty(useMap) || CollectionUtil.isEmpty(useMap.get(key.concat("1")))) {
                useMap.put(key.concat("1"), new ArrayList<String>() {{
                add(dataMap.get(key.concat("1")).stream().findAny().orElse(""));
            }});
                useMap.put(key.concat("2"), new ArrayList<String>() {{
                add(dataMap.get(key.concat("2")).stream().findAny().orElse(""));
            }});
                useMap.put(key.concat("3"), new ArrayList<String>() {{
                add(dataMap.get(key.concat("3")).get(0));
                add(dataMap.get(key.concat("3")).get(1));
            }});
                useMap.put(key.concat("4"), new ArrayList<String>() {{
                add(dataMap.get(key.concat("4")).get(0));
                add(dataMap.get(key.concat("4")).get(1));
            }});
        }
        return DomainResDTO.builder()
                .pcDomain(String.join("|", useMap.get(key.concat("1"))))
                .h5Domain(String.join("|", useMap.get(key.concat("2"))))
                .apiDomain(String.join("|", useMap.get(key.concat("3"))))
                .ossDomain(String.join("|", useMap.get(key.concat("4"))))
                .build();
    }

}
