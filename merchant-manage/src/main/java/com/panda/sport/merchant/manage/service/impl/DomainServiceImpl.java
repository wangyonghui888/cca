package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.TDomainMapper;
import com.panda.sport.bss.mapper.TMerchantGroupMapper;
import com.panda.sport.match.mapper.MatchInfoCurMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.dto.ApiMerchantGroupVO;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.TDomainVo;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.manage.feign.MerchantApiClient;
import com.panda.sport.merchant.manage.feign.MultiterminalClient;
import com.panda.sport.merchant.manage.service.IDomainService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.util.JWTUtil;
import com.panda.sport.merchant.manage.util.RedisTemp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * oss域名表 服务实现类
 * </p>
 *
 * @author Baylee
 * @since 2021-05-28
 */
@Service
@Slf4j
public class DomainServiceImpl extends ServiceImpl<TDomainMapper, TDomain> implements IDomainService {
    private static final String  defaultMerchantGroupName = "系统预设商户组";
    private static final String  defaultMerchantGroupCode = "common";

    @Value("${merchant.group.default.id:0}")
    private Long defaultMerchantGroupId;

    @Autowired
    private TDomainMapper tDomainMapper;
    @Autowired
    private MultiterminalClient multiterminalClient;

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private TMerchantGroupMapper merchantGroupMapper;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Autowired
    private MatchInfoCurMapper matchInfoCurMapper;

    @Autowired
    private HttpServletRequest request;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDomain(Integer userId, Long id, String ip, HttpServletRequest request) {
        String username = loginUserService.getLoginUser(userId);
        //逻辑删除
        TDomain tDomain = getById(id);
        if (tDomain == null) {
            return;
        }
        if (tDomainMapper.delete(id) > 0) {
            //删除成功 记日志
            try {
                MerchantGroupPO po = merchantGroupMapper.selectMerchantGroupById(tDomain.getMerchantGroupId());
                MerchantLogPageEnum pageEnum = MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_84;
                MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT_MERCHANT_Domian;
                if(StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"))){
                    // 三端入口来的
                    pageEnum = MerchantLogPageEnum.API_DOMAIN_POOL_SET;
                    typeEnum = MerchantLogTypeEnum.DEL_INFO;
                    username = JWTUtil.getUsername(request.getHeader("token"));
                }
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().addAll(Arrays.asList(MerchantFieldUtil.FIELD_MAPPING.get("domainType"),MerchantFieldUtil.FIELD_MAPPING.get("name"), MerchantFieldUtil.FIELD_MAPPING.get("status")));
                vo.getBeforeValues().addAll(Arrays.asList(DomainTypeEnum.getNameByCode(tDomain.getDomainType()),tDomain.getDomainName(), DomainEnableEnum.getValueByCode(tDomain.getEnable())));
                merchantLogService.saveLog(pageEnum, typeEnum, vo,
                        MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, String.valueOf(userId), username, null, po.getGroupName(), null, Constant.LANGUAGE_CHINESE_SIMPLIFIED, ip);
            } catch (Exception e) {
                log.error("记录日志出错", e);
            }
        }

    }

    @Override
    public void resetDomain(Integer userId, Long id) {
        tDomainMapper.resetDomain(id);
        log.info("恢复成功！");
    }

    @Override
    public void deleteDomainAll(Integer userId, DomainVo domainVo, String ip, HttpServletRequest request) {
        if (domainVo.getMerchantGroupId() == null) {
            return;
        }
        String username = loginUserService.getLoginUser(userId);
        List<TDomain> list = tDomainMapper.selectAll(domainVo);
        if (tDomainMapper.deleteAll(domainVo) > 0) {
            //删除成功 记日志
            try {
                MerchantGroupPO po = merchantGroupMapper.selectMerchantGroupById(domainVo.getMerchantGroupId());
                MerchantLogPageEnum pageEnum = MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_84;
                MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT_MERCHANT_Domian;
                if(StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"))){
                    // 三端入口来的
                    pageEnum = MerchantLogPageEnum.API_DOMAIN_POOL_SET;
                    typeEnum = MerchantLogTypeEnum.ABNORMAL_IP_ALL_DEL;
                    username = JWTUtil.getUsername(request.getHeader("token"));
                }
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().addAll(Arrays.asList("域名类型","地址","使用状态"));
                list.forEach( td -> vo.getBeforeValues().addAll(Arrays.asList(DomainTypeEnum.getNameByCode(td.getDomainType()),td.getDomainName(), DomainEnableEnum.getValueByCode(td.getEnable()))));
                merchantLogService.saveLog(pageEnum, typeEnum, vo,
                        MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, String.valueOf(userId), username, null, po.getGroupName(), null, Constant.LANGUAGE_CHINESE_SIMPLIFIED, ip);
            } catch (Exception e) {
                log.error("记录日志出错", e);
            }
        }
    }

    @Override
    public boolean checkDomainByGroup(String api, String groupCode) {
        int count = tDomainMapper.checkDomainByGroup(api, groupCode);
        return count > 0;
    }

    @Override
    public APIResponse<Object> queryFrontendMerchantDomain(FrontendMerchantGroupDomainPO frontendMerchantGroup) {
        return multiterminalClient.queryFrontendMerchantDomain(frontendMerchantGroup);
    }
    @Override
    public APIResponse<Object> queryVideoMerchantDomain(VideoMerchantGroupDomainPO frontendMerchantGroup) {
        return multiterminalClient.queryVideoMerchantDomain(frontendMerchantGroup);
    }

    @Override
    public Response queryList(DomainVo domainVo) {
        Map<String, Object> resultMap = new HashMap();
        int count = tDomainMapper.pageListCount(domainVo);
        resultMap.put("total", count);
        if (count == 0) {
            return Response.returnSuccess(resultMap);
        }
        domainVo.setStarNum((domainVo.getPageNum() - 1) * domainVo.getPageSize());
        List<TDomainVo> list = tDomainMapper.pageList(domainVo);
        resultMap.put("list", list);
        return Response.returnSuccess(resultMap);
    }

    @Override
    public Response importDomain(Integer userId, MultipartFile file) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            HSSFSheet sheet = workbook.getSheetAt(0);
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            HSSFRow row = null;
            List<TDomain> importList = new ArrayList<>();
            String username = loginUserService.getLoginUser(userId);
            for (int i = 0; i < physicalNumberOfRows; i++) {
                if (i == 0) {
                    continue;//标题行
                }
                row = sheet.getRow(i);
                TDomain data = new TDomain();
                data.setDomainType((int) row.getCell(0).getNumericCellValue());
                data.setDomainName(row.getCell(1).getStringCellValue());
                data.setCreateTime(new Date());
                data.setCreateUser(username);
                importList.add(data);
            }
            if (CollectionUtil.isNotEmpty(importList)) {
                log.info("开始批量插入==={}", importList.size());
                saveBatch(importList);
                log.info("结束批量插入");
            }
        } catch (IOException e) {
            log.error("导入异常！", e);
            return Response.returnFail("导入异常！");
        }
        return Response.returnSuccess();
    }

    @Override
    public Response saveDomain(Integer userId, DomainVo domainVo ,HttpServletRequest request) {
        if (domainVo.getMerchantGroupId() == null) {
            return Response.returnFail("商户分组id未携带！");
        }
        String[] strArray = domainVo.getDomainName().split(",");
        Set<String> set = Arrays.stream(strArray).collect(Collectors.toSet());
        String username = loginUserService.getLoginUser(userId);
        Integer count = tDomainMapper.checkDomianByEnable(domainVo.getMerchantGroupId());
        List<JSONObject> merchantList = merchantMapper.getMerchantByApiDomains(new ArrayList<>(set), domainVo.getMerchantGroupCode());
        if(CollectionUtils.isNotEmpty(merchantList)){
            return Response.returnFail(
                    "【" + merchantList.stream().map(j->j.getString("code")).collect(Collectors.joining(","))
                    +"】以上商户不是" + domainVo.getMerchantGroupType()+"组,但是包含此次导入域名，请检验！");
        }
        for (String value : set) {
            TDomain data = new TDomain();
            data.setDomainType(2);
            data.setDomainName(value.trim());
            data.setCreateTime(new Date());
            data.setCreateUser(username);
            data.setMerchantGroupId(domainVo.getMerchantGroupId());
            if (count == 0) {
                data.setEnable(2);
                count = 1;
            }
            try {

                log.info("data = {}", JSON.toJSONString(data));

                tDomainMapper.saveDomian(data);
            } catch (Exception e) {
                log.error("数据重复保存失败！data ={}", JSON.toJSONString(data));
            }
        }
        MerchantGroupPO po = merchantGroupMapper.selectMerchantGroupById(domainVo.getMerchantGroupId());
        MerchantLogPageEnum pageEnum = MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_84;
        if(StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"))){
            // 三端入口来的
            pageEnum = MerchantLogPageEnum.API_DOMAIN_POOL_SET;
            username = JWTUtil.getUsername(request.getHeader("token"));
        }
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("domainImport"));
        vo.getBeforeValues().add("-");
        vo.setAfterValues(new ArrayList<>(set));
        merchantLogService.saveLog(pageEnum, MerchantLogTypeEnum.DOMAIN_IMPORT, vo,
                MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, String.valueOf(userId), username, null, po.getGroupName(), DomainTypeEnum.getNameByCode(domainVo.getDomainType()),  Constant.LANGUAGE_CHINESE_SIMPLIFIED, IPUtils.getIpAddr(request));
        return Response.returnSuccess();
    }

    /**
     * 查询域名
     *
     * @return
     */
    @Override
    public Response queryAnimation() {

        String animationUrl = RedisTemp.get("Animation3Url_hdpn");
        return Response.returnSuccess(animationUrl);
    }


    /**
     * 更新域名
     *
     * @param url
     * @return
     */
    @Override
    public Response updateAnimation(HttpServletRequest request,String url) {

        if (StringUtil.isBlankOrNull(url)) {
            return Response.returnFail("动画的链接不能为空");
        }
        try {
            String oldUrl = RedisTemp.get("Animation3Url_hdpn");
            RedisTemp.delete("Animation3Url_hdpn");
            //tDomainMapper.updateAnimation(url);
            RedisTemp.set("Animation3Url_hdpn", url);
            log.info("Animation3Url_hdpn的缓存值:{}", RedisTemp.getString("Animation3Url_hdpn"));
            matchInfoCurMapper.aniUpdate(url);
            /**
            *  添加系统日志
            * */
            String userId = request.getHeader("user-id");
            String username = request.getHeader("merchantName");
            String ip = IPUtils.getIpAddr(request);
            String language = request.getHeader("language");
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("domainUrl"));
            vo.getBeforeValues().add(oldUrl);
            vo.getAfterValues().add(url);
            MerchantLogPageEnum pageEnum = MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_43;
            MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.EDIT_INFO;
            if(StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"))){
                // 三端入口来的
                typeEnum = MerchantLogTypeEnum.EDIT;
                username = JWTUtil.getUsername(request.getHeader("token"));
            }
            merchantLogService.saveLog(pageEnum, typeEnum, vo,
                    MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId, username, null, null, userId, language, ip);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("动画更新异常！", e);
            return Response.returnFail("动画更新异常！");
        }
    }


    /**
     * deleteAniCache
     *
     * @return
     */
    @Override
    public Response deleteAniCache(HttpServletRequest request) {

        try {

            String animationUrl = RedisTemp.get("Animation3Url_hdpn");
            RedisTemp.delete("Animation3Url_hdpn");
            //String animationUrl = tDomainMapper.selectAnimation3Url();
            RedisTemp.set("Animation3Url_hdpn", animationUrl);
            log.info("Animation3Url_hdpn的缓存值:{}", RedisTemp.getString("Animation3Url_hdpn"));
            /**
             *  添加系统日志
             * */
            String username = request.getHeader("merchantName");
            String ip = IPUtils.getIpAddr(request);
            String language = request.getHeader("language");
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("clearCache"));

            if(StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"))){
                username = JWTUtil.getUsername(request.getHeader("token"));
            }
            merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_43, MerchantLogTypeEnum.CLEAR_CACHE, vo,
                    MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, null, username, null, null, null, language, ip);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("清除缓存异常！", e);
            return Response.returnFail("清除缓存异常！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateMerchantDomain(Integer userId, String oldDomain, String domain, Long groupId, String ip,Integer domainType) {

        // 校验merchant_group_id 是否存在
        MerchantGroupPO merchantGroupPO = merchantGroupMapper.selectMerchantGroupById(groupId);
        if(merchantGroupPO == null){
            return Response.returnFail("商户组不存在");
        }
        final String merchantGroupName = merchantGroupPO.getGroupName();
        Map<String, Object> param = Maps.newHashMap();
        param.put("merchantGroupId", groupId);
        param.put("status", 1);
        List<MerchantPO> merchantPoS = merchantMapper.queryMerchantListByGroup(param);
        if (CollectionUtil.isEmpty(merchantPoS)) {
            return Response.returnFail("商户组未查询到商户信息1");
        }
        // fix 42327 这里系统预设商户组 只操作c组商户
        if(Objects.equals(groupId, defaultMerchantGroupId) || merchantGroupName.equals(defaultMerchantGroupName)){
            merchantPoS = merchantPoS.stream().filter(m->m.getDomainGroupCode() != null && m.getDomainGroupCode().equals(defaultMerchantGroupCode)).collect(Collectors.toList());
        }
        if (CollectionUtil.isEmpty(merchantPoS)) {
            return Response.returnFail("商户组未查询到商户信息2");
        }
        String username = "";
        MerchantLogPageEnum pageEnum = MerchantLogPageEnum.DOMAIN_NAME_SETTINGS;
        MerchantLogTypeEnum typeEnum = MerchantLogTypeEnum.DO_DOMAIN;
        if(StringUtils.isNotBlank(request.getHeader("token"))){
            pageEnum = MerchantLogPageEnum.API_DOMAIN_SET;
            typeEnum = MerchantLogTypeEnum.MANUAL_SWITCH;
            // 三端入口来的
            try {
                username = JWTUtil.getUsername(request.getHeader("token"));
            }catch (Exception e){
                log.error("三端服务手动切换旧域名，获取用户名失败！，{}",e.getMessage(),e);
            }
        }
        if (userId != null) {
            // manage入口来的
            username = loginUserService.getLoginUser(userId);
        }
        log.info("手动切换域名操作人：{}，更新商户数量：{},",username,merchantPoS.size());
        DomainVo domainVo2 = new DomainVo();
        domainVo2.setDomainName(domain);
        domainVo2.setMerchantGroupId(groupId);
        List<TDomain> list2 = tDomainMapper.selectAll(domainVo2);
        // 2022 1123 这里产品要求不存在此商户组的域名不可以切换
        if (CollectionUtil.isEmpty(list2)) {
            return Response.returnFail("新域名【" + domain + "】不在此商户组内，请重新选择");
        }

        DomainVo domainVo = new DomainVo();
        domainVo.setMerchantGroupId(groupId);
        domainVo.setEnable(1);
        domainVo.setDomainType(2);
        List<TDomain> list = tDomainMapper.selectAll(domainVo);
        if (CollectionUtil.isNotEmpty(list)) {
            for (TDomain tDomain : list) {
                tDomainMapper.resetDomain(tDomain.getId());
            }
        }
        tDomainMapper.updateDomianEnableTimeByid(list2.get(0).getId(), new Date());
        if(CollectionUtils.isNotEmpty(merchantPoS)){
                merchantMapper.updateApiDomainByMerchantCodes(domain, merchantPoS.stream().map(MerchantPO::getMerchantCode).collect(Collectors.toList()));
                merchantApiClient.kickoutMerchants(null);
        }
        log.info("旧版本域名池手动切换接口：批量更新商户信息结束：{}", DateUtils.getCurrentTime());
        // 记录日志
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        List<String> fieldName = new ArrayList<>();
        fieldName.add(getDomain(domainType));
        vo.setFieldName(fieldName);
        List<String> beforeValues = new ArrayList<>();
        beforeValues.add(JSON.toJSONString(oldDomain));
        vo.setBeforeValues(beforeValues);
        List<String> afterValues = new ArrayList<>();
        afterValues.add(JSON.toJSONString(domain));
        vo.setAfterValues(afterValues);
        String merchantName = "SYSTEM";
        String merchantCode = "0";
        if (groupId != null) {
            merchantName = merchantGroupPO.getGroupName();
            merchantCode = merchantGroupPO.getGroupCode();
        }
        if (userId == null) {
            userId = 0;
        }
        merchantLogService.saveLog(pageEnum, typeEnum, vo,
                MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId.toString(),
                username, merchantCode,
                merchantName + "商户组", groupId == null ? "0" : merchantName + StringPool.AMPERSAND + groupId , "zs", ip);
        return Response.returnSuccess();
    }

    private String  getDomain(Integer domainType) {
        String name = "";
        switch (domainType){
            case 1 :
                name = DomainTypeEnum.H5.getName() + "域名";
                break;
            case 2:
                name = DomainTypeEnum.PC.getName() + "域名";
                break;
            case 3 :
                name = DomainTypeEnum.APP.getName() + "域名";
                break;
            case 4:
                name = DomainTypeEnum.IMAGE.getName() + "域名";
                break;
            case 5 :
                name = DomainTypeEnum.OTHER.getName() + "域名";
                break;
        }
        return name;
    }

    /**
     * 获取api商户分组
     *
     * @return
     */
    @Override
    public List<ApiMerchantGroupVO> apiMerchantGroupList() {

        List<TMerchantDomainGroupPo> tMerchantDomainGroupPoList = tDomainMapper.tMerchantDomainGroupList();

        // 根据domainGroupCode分组
        Map<String, List<TMerchantDomainGroupPo>> groupBy = tMerchantDomainGroupPoList.stream().collect(Collectors.groupingBy(TMerchantDomainGroupPo::getGroupCode));

        List<ApiMerchantGroupVO> groupList = Lists.newArrayList();

        Map<String, Object> param = Maps.newHashMap();
        for (Map.Entry<String, List<TMerchantDomainGroupPo>> map : groupBy.entrySet()) {

            ApiMerchantGroupVO apiMerchantGroupVO = new ApiMerchantGroupVO();
            apiMerchantGroupVO.setGroupCode(map.getKey());
            apiMerchantGroupVO.setGroupName(map.getValue().get(0).getGroupName());
            apiMerchantGroupVO.setId(map.getValue().get(0).getId());
            apiMerchantGroupVO.setStatus(map.getValue().get(0).getStatus());
            apiMerchantGroupVO.setCreateTime(map.getValue().get(0).getCreateTime());
            apiMerchantGroupVO.setUpdateTime(map.getValue().get(0).getUpdateTime());
            List<String> domainNameList = Lists.newArrayList();
            for (TMerchantDomainGroupPo groupPos : map.getValue()) {
                domainNameList.add(groupPos.getDomainName());
            }
            apiMerchantGroupVO.setDomainName(domainNameList);
            param.put("domainGroupCode", map.getKey());
            List<MerchantPO> merchantList = merchantMapper.queryMerchantListByGroup(param);
            apiMerchantGroupVO.setMerchantList(merchantList);
            groupList.add(apiMerchantGroupVO);
        }

        return groupList;
    }

    /**
     * 添加api分组
     *
     * @param apiMerchantGroupVO
     * @return
     */
    @Override
    public Response addApiMerchantGroup(ApiMerchantGroupVO apiMerchantGroupVO) {


        TMerchantDomainGroupPo deletePo = new TMerchantDomainGroupPo();
        BeanUtils.copyProperties(apiMerchantGroupVO, deletePo);

        tDomainMapper.deleteApiMerchantGroup(deletePo);
        merchantMapper.updateDomainGroupCodeDefult("A", apiMerchantGroupVO.getGroupCode());

        List<TMerchantDomainGroupPo> tMerchantDomainGroupPoList = new ArrayList<>();
        for (String domaiName : apiMerchantGroupVO.getDomainName()) {

            TMerchantDomainGroupPo groupPO = new TMerchantDomainGroupPo();
            BeanUtils.copyProperties(apiMerchantGroupVO, groupPO);
            groupPO.setDomainName(domaiName);
            groupPO.setCreateTime(System.currentTimeMillis());
            groupPO.setUpdateTime(System.currentTimeMillis());
            tMerchantDomainGroupPoList.add(groupPO);
        }

        if (tMerchantDomainGroupPoList.size() > 0) {
            tDomainMapper.addBatchMerchantDomainGroup(tMerchantDomainGroupPoList);
        }

        if (apiMerchantGroupVO.getMerchantCodes() != null) {
            merchantMapper.updateDomainGroupCode(apiMerchantGroupVO.getGroupCode(), apiMerchantGroupVO.getMerchantCodes());
            log.info("apiMerchantGroupVO.getGroupCode() " + " + apiMerchantGroupVO.getMerchantCodes()");
        }

        return Response.returnSuccess();
    }

    /**
     * 更新api分组
     *
     * @param apiMerchantGroupVO
     * @return
     */
    @Override
    public Response updateApiMerchantGroup(ApiMerchantGroupVO apiMerchantGroupVO) {

        if (apiMerchantGroupVO.getId() == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        if (apiMerchantGroupVO.getGroupName() == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        if (apiMerchantGroupVO.getGroupCode() == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        if (apiMerchantGroupVO.getDomainName() == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        if (apiMerchantGroupVO.getStatus() == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }

        TMerchantDomainGroupPo deletePo = new TMerchantDomainGroupPo();
        BeanUtils.copyProperties(apiMerchantGroupVO, deletePo);

        tDomainMapper.deleteApiMerchantGroup(deletePo);
        merchantMapper.updateDomainGroupCodeDefult("A", apiMerchantGroupVO.getGroupCode());

        // 更新
        TMerchantDomainGroupPo groupPO = new TMerchantDomainGroupPo();
        BeanUtils.copyProperties(apiMerchantGroupVO, groupPO);
        tDomainMapper.updateApiMerchantGroup(groupPO);

        try {
            if (apiMerchantGroupVO.getMerchantCodes() != null) {
                merchantMapper.updateDomainGroupCodeDefult("A", groupPO.getGroupCode());
                merchantMapper.updateDomainGroupCode(groupPO.getGroupCode(), apiMerchantGroupVO.getMerchantCodes());
            }

            log.info("updateApiMerchantGroup:end ");
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("updateApiMerchantGroup error:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 删除api分组
     *
     * @param apiMerchantGroupVO
     * @return
     */
    @Override
    public Response deleteApiMerchantGroup(ApiMerchantGroupVO apiMerchantGroupVO) {

        TMerchantDomainGroupPo groupPO = new TMerchantDomainGroupPo();
        BeanUtils.copyProperties(apiMerchantGroupVO, groupPO);

        List<TMerchantDomainGroupPo> resultPO = tDomainMapper.selectTmerchantDomainGroup(apiMerchantGroupVO.getGroupCode());

        if (resultPO == null || resultPO.size() == 0) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            int num = tDomainMapper.deleteApiMerchantGroup(groupPO);
            if (num > 0) {
                merchantMapper.updateDomainGroupCodeDefult("A", apiMerchantGroupVO.getGroupCode());
            }
            log.info("deleteApiMerchantGroup:" + num);
            return Response.returnSuccess(num > 0);
        } catch (Exception e) {
            log.error("deleteApiMerchantGroup error:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }
}
