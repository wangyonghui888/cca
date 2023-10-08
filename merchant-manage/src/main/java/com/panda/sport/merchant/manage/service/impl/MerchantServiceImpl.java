package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.bss.mapper.MerchantConfigMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.TMerchantGroupMapper;
import com.panda.sport.cp.mapper.TCpMerchantMapper;
import com.panda.sport.match.mapper.MatchInfoCurMapper;
import com.panda.sport.merchant.common.base.MQMsgBody;
import com.panda.sport.merchant.common.constant.*;
import com.panda.sport.merchant.common.dto.DomainChangeDTO;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.po.merchant.AdminUser;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import com.panda.sport.merchant.common.po.merchant.TMerchantGroupInfo;
import com.panda.sport.merchant.common.utils.*;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.manage.config.ExecutorInstance;
import com.panda.sport.merchant.manage.entity.vo.MerchantGroupDomainVO;
import com.panda.sport.merchant.manage.feign.MerchantApiClient;
import com.panda.sport.merchant.manage.feign.MerchantReportClient;
import com.panda.sport.merchant.manage.feign.MultiterminalClient;
import com.panda.sport.merchant.manage.mq.MQProducer;
import com.panda.sport.merchant.manage.mq.PubMQProducer;
import com.panda.sport.merchant.manage.mq.vo.TagMarketMsg;
import com.panda.sport.merchant.manage.service.*;
import com.panda.sport.merchant.manage.util.RedisConstants;
import com.panda.sport.merchant.manage.util.RedisTemp;
import com.panda.sport.merchant.mapper.AdminUserMapper;
import com.panda.sport.merchant.mapper.MerchantLogMapper;
import com.panda.sport.merchant.mapper.MerchantNewsMapper;
import com.panda.sport.merchant.mapper.TMerchantGroupInfoMapper;
import com.panda.sport.order.service.MerchantFileService;
import com.panda.sport.trader.mapper.ThirdMerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;
import static com.panda.sport.merchant.manage.service.impl.LocalCacheService.TREE_LIST;

/**
 * @author valar
 * @Description:商户管理
 * @date 2020/3/12 17:07
 */
@Service("merchantService")
@Slf4j
@RefreshScope
public class MerchantServiceImpl extends AbstractMerchantService implements MerchantService {

    @Autowired
    private MerchantNewsMapper merchantNewsMapper;

    @Autowired
    private MerchantLogMapper merchantLogMapper;

    @Autowired
    private MatchInfoCurMapper matchInfoCurMapper;

    @Autowired
    private ThirdMerchantMapper thirdMerchantMapper;

    @Autowired
    private TMerchantGroupMapper tMerchantGroupMapper;

    @Value("${transfer_url:null}")
    private String transfer_url;

    @Value("${query_transfer_list_url:null}")
    private String query_transfer_list_url;

    @Value("${special_merchant_list:null}")
    private String special_merchant;

    /**新建二级商户特殊域名组赋值*/
    @Value("${merchant.group.special.group:null}")
    private String specialMerchantGroupList;

    @Value("${manage_mongo_env:test}")
    private String env;

    @Value("${manage_mongo_target_name:null}")
    private String target_name;

    @Value("${file.path:/opt/oss/}")
    private String filePath;

    @Value("${manage.mongo.tranfer.userid}")
    private String userId;

    @Value("${manage.mongo.tranfer.usertoken}")
    private String userToken;

    @Value("${manage.mongo.tranfer.targetname}")
    private String targetName;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private MerchantAdminUserServiceImpl merchantAdminUserService;

    @Autowired
    private MerchantConfigMapper merchantConfigMapper;

    @Resource
    private MerchantApiClient merchantApiClient;

    @Autowired
    private IMongoService mongoService;

    @Autowired
    private MerchantFileService merchantFileService;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Autowired
    private MQProducer mqProducer;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private TMerchantGroupInfoMapper merchantGroupInfoMapper;

    @Autowired
    private TCpMerchantMapper cpMerchantMapper;

    @Autowired
    private MultiterminalClient multiterminalClient;

    @Autowired
    private MerchantReportClient merchantReportClient;

    private final static Integer AGENT_LEVEL = 1;

    @Autowired
    private PubMQProducer producer;

    private static final String topic = "MERCHANT_KEY_ALARM";

    private static final String OPERATION_TYPE_ADD = "add";
    private static final String OPERATION_TYPE_ENABLE = "enable";

    @Autowired
    private MerchantNewsService merchantNewsService;

    /**
     * 创建商户
     *
     * @param merchantVO
     * @param language
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response createMerchant(MerchantVO merchantVO, Integer userId, String language, String ip) throws Exception {

        //设置status状态的默认值
        if (null != merchantVO && null == merchantVO.getStatus()) {
            merchantVO.setStatus(1);
        }
        //设置open_video默认值为开启
        if (null != merchantVO) {
            merchantVO.setOpenVideo(1);
        }

        // fix 42322 新商户域名组设置
        if(merchantVO.getAgentLevel() != null && merchantVO.getAgentLevel() == 2){
            buildMerchantGroupId(merchantVO);
        }

        ResponseEnum responseEnum = this.abstractCreateMerchant(merchantVO);
        if (responseEnum == ResponseEnum.SUCCESS) {

            // 通知 merchant-api，刷新缓存
            merchantApiClient.kickoutMerchant(merchantVO.getMerchantCode());

            //新增操作日志
            String username = loginUserService.getLoginUser(userId);
            String name = null;
            if (merchantVO.getAgentLevel() == 1) {
                name = AgentLevelEnum.AGENT_LEVEL_1.getRemark();
            }
            if (merchantVO.getAgentLevel() == 0) {
                name = AgentLevelEnum.AGENT_LEVEL_0.getRemark();
            }
            if (merchantVO.getAgentLevel() == 2) {
                name = AgentLevelEnum.AGENT_LEVEL_2.getRemark();
            }
            if (StringUtils.isNotEmpty(name)) {
                String text = "【" + name + "】 【" + merchantVO.getMerchantName() + "】已入驻平台，请检查是否需要进行域名切换商户组设置！";
                mongoService.send(text, null);
            }
            if (merchantVO.getPageMode() == null || merchantVO.getPageMode() != 1) {
                MerchantUtil util = new MerchantUtil();
                MerchantLogFiledVO vo1 = util.compareObject(null, merchantVO, MerchantUtil.filterFieldAddNames, MerchantUtil.FIELD_CHANNEL_MAPPING);
                merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.CHAT_CHANNEL.getCode(), MerchantLogTypeEnum.SAVE_INFO.getRemark()
                        , MerchantUtil.getModeInfo(merchantVO.getAgentLevel().toString(), language) + "-" + MerchantUtil.getModeEndInfo(merchantVO.getAgentLevel().toString(), language),
                        MerchantLogTypeEnum.CHAT_CHANNEL.getPageCode().get(0), merchantVO.getMerchantCode(), username, merchantVO.getId(),
                        vo1.getFieldName(), vo1.getBeforeValues(), vo1.getAfterValues(), username, userId.toString(), ip);

            } else {
                merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_16, MerchantLogTypeEnum.SAVE_INFO, null,
                        MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantVO.getMerchantCode(), merchantVO.getMerchantName(), merchantVO.getId(), language, ip);
            }
        }
        return responseEnum == ResponseEnum.SUCCESS ? Response.returnSuccess(ResponseEnum.SUCCESS) : Response.returnFail(responseEnum);
    }

    /**
     *  S组特殊渠道商户配置
     *  渠道商户符合NACOS 配置  则设置merchantGroupId
     *  若merchantGroupId 则随机区任意S组商户的组id
     *  若不存在S组的商户组  则走系统预设组
     * @param merchantVO 商户VO
     */
    private void buildMerchantGroupId(MerchantVO merchantVO) {
        try{
            if(StringUtils.isBlank(specialMerchantGroupList)){
                return;
            }
            if(StringUtils.isEmpty(merchantVO.getParentId())){
                return;
            }
            List<String> specialMerchantGroupKVList = Arrays.stream(specialMerchantGroupList.split(",")).collect(Collectors.toList());
            for (String str : specialMerchantGroupKVList) {
                String [] setupStr = str.split("\\|");
                if(StringUtils.isEmpty(setupStr[0])){
                    continue;
                }
                Long merchantId = merchantMapper.getMerchantId(setupStr[0]);
                if(merchantId == null || !merchantId.equals(Long.valueOf(merchantVO.getParentId()))) {
                    continue;
                }
                Long merchantGroupId = Long.valueOf(setupStr[1]);
                MerchantGroupPO merchantGroup = tMerchantGroupMapper.selectMerchantGroupById(merchantGroupId);
                if(merchantGroup != null){
                    merchantVO.setMerchantGroupId(merchantGroupId);
                    continue;
                }
                log.error("createMerchant.buildMerchantGroupId.warning,NACOS配置ID不存在 ： {}", merchantGroupId);
                MerchantGroupPO param = new MerchantGroupPO();
                param.setGroupCode("s");
                List<MerchantGroupPO> merchantGroupPOList = tMerchantGroupMapper.selectMerchantGroup(param);
                if(CollectionUtils.isNotEmpty(merchantGroupPOList)){
                    merchantVO.setMerchantGroupId(merchantGroupPOList.get(0).getId());
                }
                log.error("createMerchant.buildMerchantGroupId.warning,S组商户组不存在，请及时补充 " );
            }
        }catch (Exception e){
            log.error("createMerchant.buildMerchantGroupId error",e);
        }
    }

    /**
     * 新增商户公告
     *
     * @param merchantNewsPo
     */
    @Override
    protected void insertMerchantNews(MerchantNews merchantNewsPo) {
        merchantNewsMapper.insert(merchantNewsPo);
    }

    /**
     * 修改商户
     *
     * @param merchantVO
     * @param language
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateMerchant(MerchantVO merchantVO, Integer userId, String language, String ip, String agentLevel) throws Exception {

        MerchantPO merchantPO = merchantMapper.selectById(merchantVO.getId());
        // 更新逻辑
        Response response = abstractUpdateMerchant(merchantVO);

        // 踢出商户
        if (response.getStatus()) {
            if (StringUtils.isNotEmpty(merchantPO.getMerchantCode()))
                kickoutMerchant(merchantPO.getMerchantCode());
        }

        // 日志更新
        if (response.getStatus()) {
            String username = loginUserService.getLoginUser(userId);
            if (StringUtils.isNotBlank(merchantVO.getAdminPassword())) {
                //发送mango告警
                String test = String.format("环境【%s】\n商户【%s】%s，请悉知！",env, merchantPO.getMerchantName() + StringPool.AMPERSAND + merchantPO.getMerchantCode(),
                        StringUtils.isNotBlank(merchantPO.getMerchantAdmin()) ? "修改商户后台密码" : "创建商户管理员");
                mongoService.send(test, target_name);
                // 设置 管理员 记录操作日志
                MerchantLogFiledVO filedVO = null;
                filedVO = new MerchantLogFiledVO();
                filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("admin_password"));
                filedVO.getBeforeValues().add("*");
                filedVO.getAfterValues().add("*");
                merchantLogService.saveLogNew(MerchantUtil.getModeInfo(null == merchantPO.getAgentLevel() ? "" : merchantPO.getAgentLevel().toString(), language) + "-重置密码", MerchantLogTypeEnum.CHAT_PASSWORD, filedVO,
                        MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language, ip);
            } else {
                //更新商户
                MerchantPO merchantNew = convertToPO(merchantVO);
                MerchantFieldUtil filedUtil = new MerchantFieldUtil<MerchantPO>();

                // 打印日志
                log.info("o1:{},o2,{}", JSON.toJSONString(merchantPO), JSON.toJSONString(merchantNew));

                MerchantLogFiledVO filedVO = filedUtil.compareObject(merchantPO, merchantNew);
                //新增操作日志
                if (filedVO != null && CollectionUtil.isNotEmpty(filedVO.getFieldName())) {
                    for (int i = 0; i < filedVO.getFieldName().size(); i++) {
                        if ("币种".equals(filedVO.getFieldName().get(i))) {
                            filedVO.getBeforeValues().set(i, MerchantFieldUtil.currencyMap.get(filedVO.getBeforeValues().get(i)));
                            filedVO.getAfterValues().set(i, MerchantFieldUtil.currencyMap.get(filedVO.getAfterValues().get(i)));
                        }
                        if ("缴纳周期".equals(filedVO.getFieldName().get(i))) {
                            filedVO.getBeforeValues().set(i, MerchantFieldUtil.paymentCycle.get(filedVO.getBeforeValues().get(i)));
                            filedVO.getAfterValues().set(i, MerchantFieldUtil.paymentCycle.get(filedVO.getAfterValues().get(i)));
                        }
                        if ("VIP费用缴纳周期".equals(filedVO.getFieldName().get(i))) {
                            filedVO.getBeforeValues().set(i, MerchantFieldUtil.vipPaymentCycle.get(filedVO.getBeforeValues().get(i)));
                            filedVO.getAfterValues().set(i, MerchantFieldUtil.vipPaymentCycle.get(filedVO.getAfterValues().get(i)));
                        }
                        if ("技术服务费缴纳周期".equals(filedVO.getFieldName().get(i))) {
                            filedVO.getBeforeValues().set(i, MerchantFieldUtil.techniquePaymentCycleMap.get(filedVO.getBeforeValues().get(i)));
                            filedVO.getAfterValues().set(i, MerchantFieldUtil.techniquePaymentCycleMap.get(filedVO.getAfterValues().get(i)));
                        }
                        if ("转账类型".equals(filedVO.getFieldName().get(i))) {
                            filedVO.getBeforeValues().set(i, MerchantFieldUtil.transferModeMap.get(filedVO.getBeforeValues().get(i)));
                            filedVO.getAfterValues().set(i, MerchantFieldUtil.transferModeMap.get(filedVO.getAfterValues().get(i)));
                        }
                        if ("计算模式".equals(filedVO.getFieldName().get(i))) {
                            filedVO.getBeforeValues().set(i, MerchantFieldUtil.computingStandardMap.get(filedVO.getBeforeValues().get(i)));
                            filedVO.getAfterValues().set(i, MerchantFieldUtil.computingStandardMap.get(filedVO.getAfterValues().get(i)));
                        }
                        if ("C端默认语言".equals(filedVO.getFieldName().get(i))) {
                            merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.CHAT_LANGUAGE.getCode(), MerchantLogTypeEnum.EDIT_INFO.getRemark()
                                    , MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_11.getRemark(),
                                    MerchantLogTypeEnum.CHAT_LANGUAGE.getPageCode().get(0), merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(),
                                    convertSlotMachineLog(merchantPO), convertSlotMachineRoomLog(merchantPO), convertSlotMachineRoomLog(merchantNew), username, userId.toString(), ip);
                            return response;
                        }
                    }
                }
                merchantLogService.saveLog(MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchantPO.getAgentLevel()), MerchantLogTypeEnum.EDIT_INFO, filedVO,
                        MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language, ip);
            }
        }
        return response;
    }
    private List<String> convertSlotMachineLog(MerchantPO beforeValue) {
        List<String> list = Lists.newArrayList();
        if (beforeValue == null) {
            list.add("--");
            return list;
        }
        list.add("默认语言");
        list.add("中文繁体开关");
        list.add("越南语开关");
        list.add("泰语开关");
        list.add("马来语开关");
        list.add("印尼语开关");
        return list;
    }

    private List<String> convertSlotMachineRoomLog(MerchantPO beforeValue) {
        List<String> list = Lists.newArrayList();
        if (beforeValue == null) {
            list.add("--");
            return list;
        }
        beforeValue.getDefaultLanguage();
        beforeValue.getLanguageList();
        List<String> listNew = Arrays.asList(beforeValue.getLanguageList().split(","));
        list.add(beforeValue.getDefaultLanguage().equals("zh") ? "中文简体" : "英文");
        if (listNew.contains("tw")) {
            list.add("开");
        } else {
            list.add("关");
        }
        if (listNew.contains("vi")) {
            list.add("开");
        } else {
            list.add("关");
        }

        if (listNew.contains("th")) {
            list.add("开");
        } else {
            list.add("关");
        }

        if (listNew.contains("ms")) {
            list.add("开");
        } else {
            list.add("关");
        }

        if (listNew.contains("ad")) {
            list.add("开");
        } else {
            list.add("关");
        }
        return list;
    }

    /**
     * 修改商户预留IP地址
     *
     * @param merchantVO
     * @param userId
     * @param language
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateMerchantWhiteIp(MerchantVO merchantVO, Integer userId, String language, String ip) throws Exception {
        MerchantPO merchant = merchantMapper.selectById(merchantVO.getId());
        if (merchant == null) {
            throw new Exception("商户id查询对象为空！");
        }
        if (AgentLevelEnum.AGENT_LEVEL_0.getCode().equals(merchant.getAgentLevel())
                || AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(merchant.getAgentLevel())) {
            String oldIp = merchant.getWhiteIp();
            MerchantPO updateMerchant = new MerchantPO();
            updateMerchant.setId(merchant.getId());
            updateMerchant.setWhiteIp(merchantVO.getWhiteIp());
            int updateNum = merchantMapper.updateMerchant(updateMerchant);
            if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(merchant.getAgentLevel())) {
                merchantMapper.updateChildrenIp(merchant.getId(), merchantVO.getWhiteIp());
            }
            if (updateNum > 0) {
                //发送mango告警
                String test = String.format("环境【%s】\n商户【%s】变更白名单，请悉知！", env, merchant.getMerchantName() + StringPool.AMPERSAND + merchant.getMerchantCode());
                mongoService.send(test, target_name);
                // 记录操作日志 后期添加操作日志功能
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("whiteIp"));
                vo.getBeforeValues().add(merchant.getWhiteIp());
                vo.getAfterValues().add(merchantVO.getWhiteIp());
                String username = loginUserService.getLoginUser(userId);
                merchantLogService.saveLogNew(MerchantUtil.getModeInfo(merchant.getAgentLevel().toString(), language) + "-" + MerchantFieldUtil.FIELD_MAPPING.get("whiteIp"), MerchantLogTypeEnum.EDIT_INFO, vo,
                        MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language, ip);
                log.info("后台用户{}修改商户{},WhiteIp字段{}更新为{}", userId, merchantVO.getId(), oldIp, merchantVO.getWhiteIp());
            }
        } else {
            throw new Exception("非直营或渠道商户无法修改IP！");
        }
        return Response.returnSuccess(ResponseEnum.SUCCESS);
    }


    /**
     * 修改商户预留IP地址
     *
     * @param merchantVO
     * @param userId
     * @param language
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateMerchantVrSport(MerchantVO merchantVO, Integer userId, String language, String ip) throws Exception {
        MerchantPO merchant = merchantMapper.selectById(merchantVO.getId());
        if (merchant == null) {
            throw new Exception("商户id查询对象为空！");
        }
        String merchantCode = merchant.getMerchantCode();
        Integer OldOpenVrSport = merchant.getOpenVrSport();
        Integer openVrSport = merchantVO.getOpenVrSport();
        MerchantPO updateMerchant = new MerchantPO();
        updateMerchant.setId(merchant.getId());
        updateMerchant.setOpenVrSport(openVrSport);
        int updateNum = merchantMapper.updateMerchant(updateMerchant);
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(merchant.getAgentLevel())) {
            List<MerchantPO> list = merchantMapper.queryChildPoList(merchant.getId());
            for (MerchantPO po : list) {
                MerchantPO updatePo = new MerchantPO();
                updatePo.setId(po.getId());
                updatePo.setOpenVrSport(openVrSport);
                updateNum = merchantMapper.updateMerchant(updatePo);
            }
            //merchantMapper.updateChildrenOpenVrSport(merchant.getId(), merchantVO.getOpenVrSport());
        }
        if (updateNum > 0) {
            // 记录操作日志 后期添加操作日志功能
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("openVrSport"));
            // 设置前数值
            vo.getBeforeValues().add(SwitchEnum.getInstance(merchant.getOpenVrSport()).getLabel());
            vo.getAfterValues().add(SwitchEnum.getInstance(merchantVO.getOpenVrSport()).getLabel());
            String username = loginUserService.getLoginUser(userId);
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_0, MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language, ip);
            log.info("后台用户{}修改商户{},OpenVrSport字段{}更新为{}", userId, merchantVO.getId(), OldOpenVrSport, merchantVO.getOpenVrSport());
        }
        kickoutMerchant(merchantCode);
        return Response.returnSuccess(ResponseEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateMerchantESport(MerchantVO merchantVO, Integer userId, String language, String ip) throws Exception {
        MerchantPO merchant = merchantMapper.selectById(merchantVO.getId());
        if (merchant == null) {
            throw new Exception("商户id查询对象为空！");
        }

        String merchantCode = merchant.getMerchantCode();
        Integer OldOpenESport = merchant.getOpenEsport();
        Integer openESport = merchantVO.getOpenEsport();
        MerchantPO updateMerchant = new MerchantPO();
        updateMerchant.setId(merchant.getId());
        updateMerchant.setOpenEsport(openESport);
        int updateNum = merchantMapper.updateMerchant(updateMerchant);
        if (AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(merchant.getAgentLevel())) {
            List<MerchantPO> list = merchantMapper.queryChildPoList(merchant.getId());
            for (MerchantPO po : list) {
                MerchantPO updatePo = new MerchantPO();
                updatePo.setId(po.getId());
                updatePo.setOpenEsport(openESport);
                updateNum = merchantMapper.updateMerchant(updatePo);
            }
            //merchantMapper.updateChildrenOpenVrSport(merchant.getId(), merchantVO.getOpenVrSport());
        }
        if (updateNum > 0) {
            merchantApiClient.kickoutMerchantUser(merchant.getMerchantCode());
            // 记录操作日志 后期添加操作日志功能
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("openEsport"));
            // 查询商户的电子竞技开关
            String openEsport = "";
            if (merchant.getOpenEsport() != null) {
                openEsport = merchant.getOpenEsport() == 0 ? "关" : "开";
            }
            // 获取设置前数据
            vo.getBeforeValues().add(openEsport);
            // 获取设置后数据
            vo.getAfterValues().add(merchantVO.getOpenEsport() == 0 ? "关" : "开");
            String username = loginUserService.getLoginUser(userId);
            // 保存变更日志
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_0, MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language, ip);
            log.info("后台用户{}修改商户{},OpenESport字段{}更新为{}", userId, merchantVO.getId(), OldOpenESport, merchantVO.getOpenEsport());
        }
        kickoutMerchant(merchantCode);
        return Response.returnSuccess(ResponseEnum.SUCCESS);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateMerchantOpenVideo(MerchantVO merchantVO, Integer userId, String language, String ip) throws Exception {
        MerchantPO merchant = merchantMapper.selectById(merchantVO.getId());
        if (merchant == null) {
            throw new Exception("商户id查询对象为空！");
        }
        String merchantCode = merchant.getMerchantCode();
        Integer OldOpenVideo = merchant.getOpenVideo();
        Integer openVideo = merchantVO.getOpenVideo();
        MerchantPO updateMerchant = new MerchantPO();
        updateMerchant.setId(merchant.getId());
        updateMerchant.setOpenVideo(openVideo);
        int updateNum = merchantMapper.updateMerchant(updateMerchant);
        if (updateNum > 0) {
            merchantApiClient.updateMerchantUserCache(merchant.getMerchantCode());
            // 记录操作日志 后期添加操作日志功能
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("openVideos"));
            String openVideos = "";
            if (merchant.getOpenVideo() != null) {
                openVideos = SwitchEnum.getInstance(merchant.getOpenVideo()).getLabel();
            }
            vo.getBeforeValues().add(openVideos);
            vo.getAfterValues().add(SwitchEnum.getInstance(merchantVO.getOpenVideo()).getLabel());
            String username = loginUserService.getLoginUser(userId);
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_0, MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language, ip);
            log.info("后台用户{}修改商户{},openVideos字段{}更新为{}", userId, merchantVO.getId(), OldOpenVideo, merchantVO.getOpenVideo());
        }
        kickoutMerchant(merchantCode);
        return Response.returnSuccess(ResponseEnum.SUCCESS);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<ResponseEnum> updateMerchantSettleSwitchAdvance(MerchantVO merchantVO, Integer userId, String language, String ip,String sportId) {
        MerchantPO merchant = merchantMapper.selectById(merchantVO.getId());
        //验证商户信息
        verifyMerchant(merchantVO, merchant);
        //更新商户提前结算开关
        int updateNum = updateMerchantSettleSwitchAdvance(merchant.getMerchantCode(), merchantVO,sportId);
        if (updateNum <= 0) {
            return Response.returnFail(ResponseEnum.FAIL);
        }
        //更新缓存
        kickoutMerchant(merchant.getMerchantCode());
        log.info("清里商户缓存完成，" + merchant.getMerchantCode());
        merchantApiClient.kickoutMerchant(merchant.getMerchantCode());
        this.asyncUpdateMerchantCache(merchant.getMerchantCode());
        //添加操作日志
        addUpdateMerchantSettleSwitchAdvanceLog(merchantVO, merchant, userId,
                language, ip,sportId);
        return Response.returnSuccess(ResponseEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<ResponseEnum> isTestOrExternal(MerchantVO merchantVO, Integer userId, String language, String ip) {
        MerchantPO newMerchant = new MerchantPO();
        MerchantPO oldMerchant = merchantMapper.selectById(merchantVO.getId());
        // 记录操作日志 后期添加操作日志功能
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        MerchantLogTypeEnum functionSwitch = MerchantLogTypeEnum.EDIT_INFO;
        if (merchantVO.getIsExternal() != null) {
            newMerchant.setIsExternal(merchantVO.getIsExternal());
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("isExternal"));
            vo.getBeforeValues().add(oldMerchant.getIsExternal() == 0 ? "外部" : "内部");
            vo.getAfterValues().add(newMerchant.getIsExternal() == 0 ? "外部" : "内部");
            functionSwitch = MerchantLogTypeEnum.FUNCTION_SWITCH;
        }
        if (merchantVO.getIsTest() != null) {
            newMerchant.setIsTest(merchantVO.getIsTest());
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("isTest"));
            vo.getBeforeValues().add(oldMerchant.getIsTest() == 0 ? "否" : "是");
            vo.getAfterValues().add(newMerchant.getIsTest() == 0 ? "否" : "是");
            functionSwitch = MerchantLogTypeEnum.FUNCTION_SWITCH;
        }
        newMerchant.setId(merchantVO.getId());
        //更新商户提前结算开关
        int updateNum = merchantMapper.updateMerchant(newMerchant);
        if (updateNum <= 0) {
            return Response.returnFail(ResponseEnum.FAIL);
        }
        String username = loginUserService.getLoginUser(userId);
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER, functionSwitch, vo,
                MerchantLogConstants.MERCHANT_IN, userId.toString(), username, oldMerchant.getMerchantCode(), oldMerchant.getMerchantName(), oldMerchant.getId(), language, ip);
        return Response.returnSuccess(ResponseEnum.SUCCESS);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<ResponseEnum> isApp(MerchantVO merchantVO, Integer userId, String language, String ip) {
        MerchantPO newMerchant = new MerchantPO();
        MerchantPO oldMerchant = merchantMapper.selectById(merchantVO.getId());
        if (oldMerchant.getAgentLevel() == AgentLevelEnum.AGENT_LEVEL_1.getCode()) {
            return Response.returnFail("渠道商户不可修改此选项");
        }

        // 记录操作日志 后期添加操作日志功能
        MerchantLogFiledVO vo = new MerchantLogFiledVO();

        if (merchantVO.getIsApp() == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }

        newMerchant.setIsApp(merchantVO.getIsApp());
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("isApp"));
        vo.getBeforeValues().add(oldMerchant.getIsApp() == null ? "" : oldMerchant.getIsApp() == 0 ? "关" : "开");
        vo.getAfterValues().add(newMerchant.getIsApp() == 0 ? "关" : "开");
        //更新商户是否对接app
        MerchantConfig merchantConfig = new MerchantConfig();
        merchantConfig.setMerchantCode(oldMerchant.getMerchantCode());
        merchantConfig.setIsApp(merchantVO.getIsApp());
        int updateNum1 = merchantConfigMapper.updateIsAppMerchantConfig(merchantConfig);
        if (updateNum1 <= 0) {
            return Response.returnFail("渠道商户不可修改此选项");
        }
        String username = loginUserService.getLoginUser(userId);
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER, MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                MerchantLogConstants.MERCHANT_IN, userId.toString(), username, oldMerchant.getMerchantCode(), oldMerchant.getMerchantName(), oldMerchant.getId(), language, ip);
        return Response.returnSuccess();
    }

    private void asyncUpdateMerchantCache(String merchantCode) {
        ExecutorInstance.executorService.submit(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("asyncUpdateMerchantCache.sleep=", e);
            }
            merchantApiClient.updateMerchantUserCache(merchantCode);
            log.info("updateMerchantUserCache,更新商户缓存成功");
        });
    }

    private Integer updateMerchantSettleSwitchAdvance(String merchantCode, MerchantVO merchantVO,String sportId) {
        MerchantConfig updateMerchant = new MerchantConfig();
        updateMerchant.setMerchantCode(merchantCode);
        if("1".equals(sportId)) {
            updateMerchant.setSettleSwitchAdvance(merchantVO.getSettleSwitchAdvance());
            return merchantConfigMapper.updateMerchantSettleSwitchAdvance(updateMerchant);
        }else {
            updateMerchant.setSettleSwitchBasket(merchantVO.getSettleSwitchBasket());
            return merchantConfigMapper.updateMerchantSettleSwitchBasket(updateMerchant);
        }

    }

    private void addUpdateMerchantSettleSwitchAdvanceLog(MerchantVO newMerchant, MerchantPO oldMerchant, Integer userId, String language, String ip,String sportId) {
        // 记录操作日志 后期添加操作日志功能
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("settleSwitchAdvance"));
        if("1".equals(sportId)) {
            //调整足球提前结算开关
            vo.getBeforeValues().add(SwitchEnum.getInstance(oldMerchant.getSettleSwitchAdvance()).getLabel());
            vo.getAfterValues().add(SwitchEnum.getInstance(newMerchant.getSettleSwitchAdvance()).getLabel());
        }else{
            //调整篮球提前结算开关
            vo.getBeforeValues().add(SwitchEnum.getInstance(oldMerchant.getSettleSwitchBasket()).getLabel());
            vo.getAfterValues().add(SwitchEnum.getInstance(newMerchant.getSettleSwitchBasket()).getLabel());
        }
        String username = loginUserService.getLoginUser(userId);
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_0,
                MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                MerchantLogConstants.MERCHANT_IN, userId.toString(),
                username, oldMerchant.getMerchantCode(),
                oldMerchant.getMerchantName(), oldMerchant.getId(), language, ip);
        log.info("后台用户{}修改商户{},OpenVrSport字段{}更新为{}", userId, newMerchant.getId(), oldMerchant.getSettleSwitchAdvance(), newMerchant.getSettleSwitchAdvance());
    }

    private void verifyMerchant(MerchantVO merchantVO, MerchantPO merchant) {
        Assert.notNull(merchant, "商户ID未查询到对象");
        Assert.isTrue(merchant.getStatus().equals(1), "商户状态为禁用，请开启重试");
        Assert.isTrue(AgentLevelEnum.AGENT_LEVEL_0.getCode().equals(merchant.getAgentLevel()) || AgentLevelEnum.AGENT_LEVEL_2.getCode().equals(merchant.getAgentLevel()), "只能二级代理或直营商户才有提前结算开关");
        //Assert.isFalse(merchant.getSettleSwitchAdvance().equals(merchantVO.getSettleSwitchAdvance()), "开关值和原来一样，无需修改");
    }

    /**
     * 创建商户管理员
     *
     * @param merchantPO
     */
    @Override
    public void createAdminUser(MerchantPO merchantPO) {
        merchantAdminUserService.createAdminUser(merchantPO);
    }

    /**
     * 分页查询商户列表
     *
     * @param merchantVO
     * @return
     */
    @Override
    public Response selectList(MerchantVO merchantVO) {
        return Response.returnSuccess(this.queryMerchantList(merchantVO));
    }

    /**
     * 跟俊参数查询商户列表
     *
     * @param merchantVO
     * @return
     */
    @Override
    public List<MerchantVO> queryListByParam(MerchantVO merchantVO) {
        return this.queryMerchantList(merchantVO).getList();
    }

    @Override
    public int updateApiDomainByMerchantCode(String newDomain, String merchantCode) {
        return merchantMapper.updateApiDomainByMerchantCode(newDomain, merchantCode);
    }


    @Override
    public int updateMerchantDomainByMerchantCode(Integer domainType, String newDomain, String oldDomain, String merchantCode) {
        return merchantMapper.updateMerchantDomainByMerchantCode(domainType, newDomain, oldDomain, merchantCode);
    }

    @Override
    public List<?> queryMerchantListByGroup(String merchantGroupId, Integer status) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("merchantGroupId", merchantGroupId);
        param.put("status", status);
        return merchantMapper.queryMerchantListByGroup(param);
    }

    @Override
    public int queryMerchantCountByGroup(String merchantGroupId, Integer status) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("merchantGroupId", merchantGroupId);
        param.put("status", status);
        return merchantMapper.queryMerchantCountByGroup(param);
    }

    @Override
    public int updateMerchantDomainByMerchantCodes(String merchantGroupId, JSONArray domainVos) {

        Map<String, Object> param = Maps.newHashMap();
        param.put("merchantGroupId", merchantGroupId);
        param.put("status", 1);
        List<?> resultList = merchantMapper.queryMerchantListByGroup(param);
        if (CollectionUtils.isEmpty(resultList)) {
            log.info("商户组ID：{}，此商户组下没有商户需要清除缓存!", merchantGroupId);
        }

        ObjectMapper mapper = new ObjectMapper();
        final List<MerchantPO> merchantList = mapper.convertValue(resultList, new TypeReference<List<MerchantPO>>() {
        });

        CompletableFuture.runAsync(() -> {
            int count = 0;

            List<Integer> domainTypeList = Arrays.asList(1, 2, 3);
            log.info("新版本域名池手动切换接口：异步批量更新商户信息开始：{}", com.panda.sport.merchant.common.utils.DateUtils.getCurrentTime());
            if (CollectionUtils.isNotEmpty(merchantList)) {
                List<String> merchantCodeList = merchantList.stream().map(MerchantPO::getMerchantCode).collect(Collectors.toList());
                for (int i = 0; i < domainVos.size(); i++) {
                    JSONObject domainVO = domainVos.getJSONObject(i);
                    if (domainVO.getInteger("domainType") == null
                            || !domainTypeList.contains(domainVO.getInteger("domainType"))) {
                        continue;
                    }
                    int updateCount = merchantMapper.updateMerchantDomainByMerchantCodes(
                            domainVO.getInteger("domainType"),
                            domainVO.getString("domainName"),
                            domainVO.getString("oldDomain"),
                            merchantCodeList, null
                    );
                    count += updateCount;
                }
            }
            log.info("新版本域名池手动切换接口：异步批量更新商户信息结束：{},被更新信息商户数量为{}",
                    com.panda.sport.merchant.common.utils.DateUtils.getCurrentTime(), count);
        });

        CompletableFuture.runAsync(() -> {
            log.info("新版本域名池手动切换接口：异步清除商户缓存开始：{}", com.panda.sport.merchant.common.utils.DateUtils.getCurrentTime());
            if (CollectionUtils.isNotEmpty(merchantList)) {
                merchantApiClient.kickoutMerchants(
                        merchantList
                                .stream()
                                .map(MerchantPO::getMerchantCode)
                                .collect(Collectors.toList()));
            }
            log.info("新版本域名池手动切换接口：异步批量更新商户信息结束：{}", com.panda.sport.merchant.common.utils.DateUtils.getCurrentTime());
        });
        return 1;
    }

    @Override
    public void updateMerchantGroupIdDefult(String id) {
        merchantMapper.updateMerchantGroupIdDefult(id);
    }

    @Override
    public int updateMerchantGroupId(String id, String groupCode, List<String> merchantCodes) {
        return merchantMapper.updateMerchantGroupId(id, groupCode, merchantCodes);
    }

    /**
     * 修改商户状态
     *
     * @param merchantCode
     * @param status
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Object> updateMerchantStatus(String merchantCode, String status, Integer userId, String language, String ip) {
        String username = loginUserService.getLoginUser(userId);
        MerchantPO merchant = abstractUpdateMerchantStatus(merchantCode, status, null);
        //修改渠道商状态同步修改下级商户
        this.updateChildrenMerchantStatus(merchantCode, status, userId, language, ip, username, merchant);
        //如果是禁用商户则同步踢出商户和商户下对应的用户
        if (status.equals("0")) {
            CompletableFuture.runAsync(() -> {
                merchantApiClient.kickoutMerchant(merchantCode);
                merchantApiClient.kickoutMerchantUser(merchantCode);
                log.info("merchantCode:{}踢出商户用户成功", merchantCode);

            });
        }
        if (merchant != null) {
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("client"));
            vo.getBeforeValues().add(merchant.getStatus() == 1 ? "关" : "开");
            vo.getAfterValues().add("1".equals(status) ? "开" : "关");
            merchantLogService.saveLog(MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchant.getAgentLevel()), MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language, ip);
        }
        return Response.returnSuccess(true);
    }

    /**
     * 修改商户状态
     *
     * @param merchantCode
     * @param status
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Object> updateMerchantBackendStatus(String merchantCode, String status, Integer userId, String language, String ip) {
        String username = loginUserService.getLoginUser(userId);
        MerchantPO merchant = abstractUpdateMerchantBackendStatus(merchantCode, status);
        // 修改渠道商登录状态同步修改下级商户
        this.updateChildrenMerchantBackendStatus(merchantCode, status, userId, language, ip, username, merchant);
        if (merchant != null) {
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
           /* vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("backendSwitch"));
            vo.getBeforeValues().add(merchant.getBackendSwitch() != null && merchant.getBackendSwitch() == 1 ? "启用" : "禁用");
            vo.getAfterValues().add("1".equals(status) ? "启用" : "禁用");
            merchantLogService.saveLog(MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchant.getAgentLevel()), MerchantLogTypeEnum.EDIT_INFO_STATUS, vo,*/
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("merchantBackground"));
            vo.getBeforeValues().add(merchant.getBackendSwitch() != null && merchant.getBackendSwitch() == 1 ? "开" : "关");
            vo.getAfterValues().add("1".equals(status) ? "开" : "关");
            merchantLogService.saveLog(MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchant.getAgentLevel()), MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language, ip);
        }
        return Response.returnSuccess(true);
    }

    /**
     * 批量禁用商户
     *
     * @param merchantInfoVo
     * @param userId
     * @param language
     * @param ip
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Object> importMerchantStatus(MerchantInfoVo merchantInfoVo, Integer userId, String language, String ip) {
        String username = loginUserService.getLoginUser(userId);
        List<String> merchantCodes = Arrays.asList(merchantInfoVo.getMerchantCodes().split(","));
        String status = String.valueOf(merchantInfoVo.getStatus());
        String remark = merchantInfoVo.getRemark();
        Map<String, List<String>> map = new HashMap<>();
        List<String> errorCodes = new ArrayList<>();
        //修改渠道商状态同步修改下级商户
        for (String merchantCode : merchantCodes) {
            MerchantPO merchant = abstractUpdateMerchantStatus(merchantCode, status, remark);
            if (merchant != null) {
                if (status.equals("0")) {
                    CompletableFuture.runAsync(() -> {
                        this.updateChildrenMerchantStatus(merchantCode, status, userId, language, ip, username, merchant);
                        //修改商户状态同步清理商户缓存
                        merchantApiClient.kickoutMerchant(merchantCode);
                        log.info("merchantCode:{}踢出商户用户成功", merchantCode);
                    });
                }
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("status"));
                vo.getBeforeValues().add(merchant.getStatus() == 1 ? "启用" : "禁用");
                vo.getAfterValues().add("1".equals(status) ? "启用" : "禁用");
                merchantLogService.saveLog(MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchant.getAgentLevel()), MerchantLogTypeEnum.EDIT_INFO_STATUS, vo,
                        MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language, ip);
            } else {
                errorCodes.add(merchantCode);
            }
        }
        map.put("errorCodes", errorCodes);
        return Response.returnSuccess(map);
    }

    /**
     * 查询商户信息
     *
     * @param merchantInfoVo
     * @param userId
     * @param language
     * @param ip
     * @return
     */
    @Override
    public Response<Object> findMerchantInfo(MerchantInfoVo merchantInfoVo, Integer userId, String language, String ip) {
        merchantInfoVo.setIp(ip);
        merchantInfoVo.setLanguage(language);
        merchantInfoVo.setUserId(userId);
        try {
            if (StringUtils.isNotEmpty(merchantInfoVo.getMerchantCodes())) {
                List<String> merchantCodes = Arrays.asList(merchantInfoVo.getMerchantCodes().split(","));
                return Response.returnSuccess(merchantMapper.findMerchantInfo(merchantCodes));
            } else {
                log.info("findMerchantInfo ==>merchantInfoVo:{}", merchantInfoVo);
                return Response.returnFail("merchantCodes没有传参");
            }
        } catch (Exception e) {
            log.error("查询商户信息异常!", e);
            return Response.returnFail("查询商户信息失败!");
        }
    }

    /**
     * 修改渠道商状态同步修改下级商户
     *
     * @param merchantCode
     * @param status
     * @param userId
     * @param language
     * @param ip
     * @param username
     * @param merchant
     */
    private void updateChildrenMerchantStatus(String merchantCode, String status, Integer userId, String language, String ip, String username, MerchantPO merchant) {
        if (merchant.getAgentLevel() != null && merchant.getAgentLevel().equals(AGENT_LEVEL)) {
            List<MerchantPO> childrenMerchantList = this.abstractUpdateChildrenMerchantStatus(merchantCode, status);
            if (CollectionUtils.isNotEmpty(childrenMerchantList)) {
                //如果是禁用商户则同步踢出商户和商户下对应的用户
                List<String> merchantCodeList = childrenMerchantList.stream().map(MerchantPO::getMerchantCode).collect(Collectors.toList());
                if (status.equals("0")) {
                    CompletableFuture.runAsync(() -> {
                        merchantApiClient.kickoutMerchants(merchantCodeList);
                        //去掉踢出用户
//                        this.asyncKickOutMerchantUser(merchantCodeList);
                        log.info("merchantCodeList:{}踢出商户用户成功", merchantCodeList);
                    });
                }
                for (MerchantPO merchantPO : childrenMerchantList) {
                    MerchantLogFiledVO logFiledVO = new MerchantLogFiledVO();
                    logFiledVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("status"));
                    logFiledVO.getBeforeValues().add(merchantPO.getStatus() == 1 ? "启用" : "禁用");
                    logFiledVO.getAfterValues().add("1".equals(status) ? "启用" : "禁用");
                    merchantLogService.saveLog(MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchantPO.getAgentLevel()), MerchantLogTypeEnum.EDIT_INFO_STATUS, logFiledVO,
                            MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language, ip);
                }
            }
        }
    }

    private void updateChildrenMerchantBackendStatus(String merchantCode, String status, Integer userId, String language, String ip, String username, MerchantPO merchant) {
        if (merchant.getAgentLevel() != null && merchant.getAgentLevel().equals(AGENT_LEVEL)) {
            List<MerchantPO> childrenMerchantList = this.abstractUpdateChildrenMerchantBackendStatus(merchantCode, status);
            if (CollectionUtils.isNotEmpty(childrenMerchantList)) {
                //如果是禁用商户后台则同步踢出商户  无需清除缓存
//                List<String> merchantCodeList = childrenMerchantList.stream().map(MerchantPO::getMerchantCode).collect(Collectors.toList());
//                if(status.equals("0")){
//                    CompletableFuture.runAsync(()->{
//                        merchantApiClient.kickoutMerchants(merchantCodeList);
//                        log.info("merchantCodeList:{}踢出商户成功",merchantCodeList);
//                    });
//                }
                for (MerchantPO merchantPO : childrenMerchantList) {
                    MerchantLogFiledVO logFiledVO = new MerchantLogFiledVO();
                    logFiledVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("backendSwitch"));
                    logFiledVO.getBeforeValues().add(merchant.getBackendSwitch() != null && merchant.getBackendSwitch() == 1 ? "启用" : "禁用");
                    logFiledVO.getAfterValues().add("1".equals(status) ? "启用" : "禁用");
                    merchantLogService.saveLog(MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchantPO.getAgentLevel()), MerchantLogTypeEnum.EDIT_INFO_STATUS, logFiledVO,
                            MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language, ip);
                }
            }
        }
    }


    private void asyncKickOutMerchantUser(List<String> merchantCodeList) {
        ExecutorInstance.executorService.submit(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("asyncKickOutMerchantUser.sleep=", e);
            }
            merchantCodeList.forEach(item -> merchantApiClient.kickoutMerchantUser(item));
            log.info("asyncKickOutMerchantUser,更新商户用户缓存成功");
        });
    }

    /**
     * 修改是否风险商户状态
     *
     * @param configList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Object> updateMerchantRiskStatus(List<MerchantConfig> configList, String userId, String ip) {
        log.info("configList" + configList.size());
        if (CollectionUtils.isNotEmpty(configList)) {
            RcsBusinessLogVO logVO = new RcsBusinessLogVO();
            StringBuffer beforeBuffer = new StringBuffer();
            StringBuffer afterBuffer = new StringBuffer();
            for (MerchantConfig cf : configList) {
                MerchantConfig beforeConfig = new MerchantConfig();
                if (null != cf && !StringUtils.isEmpty(cf.getMerchantCode()) && (cf.getIsRisk() == 0 || cf.getIsRisk() == 1)) {
                    MerchantConfig config = new MerchantConfig();
                    config.setMerchantCode(cf.getMerchantCode());
                    config.setIsRisk(cf.getIsRisk());
                    beforeConfig = merchantConfigMapper.selectMerchantByCode(cf.getMerchantCode());
                    if (cf.getIsRisk().equals(0)) {
                        beforeBuffer.append(beforeConfig.getMerchantName() + ";");
                        afterBuffer.append("");
                    } else {
                        beforeBuffer.append("");
                        afterBuffer.append(beforeConfig.getMerchantName() + ";");
                    }
                    int result = merchantConfigMapper.updateMerchantConfig(config);
                    log.info("修改是否风险商户状态:" + config + ",result:" + result);
                }
            }
            if (beforeBuffer.toString().length() > 0) {
                logVO.setBeforeVal(beforeBuffer.toString().substring(0, beforeBuffer.toString().length() - 1));
            }
            if (afterBuffer.toString().length() > 0) {
                logVO.setAfterVal(afterBuffer.toString().substring(0, afterBuffer.toString().length() - 1));
            }
            //同步所有风险用户到MQ
            List<MerchantConfig> riskList = merchantConfigMapper.queryMerchantRiskList();
            if (riskList != null && !riskList.isEmpty()) {
                StringBuilder keys = new StringBuilder();
                for (MerchantConfig con : riskList) {
                    keys.append(con.getMerchantCode()).append(con.getIsRisk());
                }
                MQMsgBody body = new MQMsgBody();
                body.setKey(keys.toString());
                body.setTag("merchant_risk_status_info");
                body.setTopic("MERCHANT_RISK_STATUS_INFO");
                body.setObj(riskList);
                mqProducer.sendMessage(body);
            }
            //添加风控日志
            logVO.setOperateCategory("风控措施管理");
            logVO.setObjectId("234");
            logVO.setObjectName("风险商户新用户");
            logVO.setOperateType("10030");
            logVO.setParamName("1756特殊脚本商户");
            logVO.setUserId(userId);
            logVO.setIp(ip);
            List<RcsBusinessLogVO> logList = Lists.newArrayList();
            logList.add(logVO);
            MQMsgBody body = new MQMsgBody();
            body.setKey("");
            body.setTag("merchant_risk_status_log");
            body.setTopic("rcs_business_log_save");
            body.setObj(logList);
            mqProducer.sendMessage(body);
        }
        return Response.returnSuccess(true);
    }

    @Override
    public void updateMerchantMarketData(TagMarketMsg msg) {
        MerchantPO merchantPO = getMerchantById(msg.getBusinessId());
        Integer marketLevel = msg.getTagMarketLevelId();
        Integer marketLevelIdPc = msg.getTagMarketLevelIdPc();
        if (marketLevel == null) {
            log.error("参数错误");
            return;
        }
        if (marketLevel != null || marketLevelIdPc != null) {
            updateMerchantTagMarketStatus(merchantPO.getMerchantCode(), msg);
            log.info(merchantPO.getMerchantCode() + "修改Tag信息num:" + marketLevel);
            merchantApiClient.kickoutMerchant(merchantPO.getMerchantCode());
            log.info("商户踢出成功!" + merchantPO.getMerchantCode());
        } else {
            log.error("商户模式和行情等级不一致");
        }
    }

    /**
     * 渠道商户独立出来
     *
     * @param merchantCode
     * @param parentId
     * @param userId
     * @param language
     * @return
     */
    @Override
    public Response<Object> deleteSubAgent(String merchantCode, String parentId, Integer userId, String language, String ip) {
        MerchantPO merchant = abstractDeleteSubAgent(merchantCode, parentId);
        if (merchant != null) {
            String username = loginUserService.getLoginUser(userId);
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("parentId"));
            List<String> before = new ArrayList();
            before.add("序号  商户编码  商户名称  商户等级");
            before.add(1 + "    " + merchant.getMerchantCode() + "      " + merchant.getMerchantName() + "        " + "Lv" + merchant.getLevel());
            vo.setBeforeValues(before);
            vo.getAfterValues().add("");
            merchantLogService.saveLog(MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchant.getAgentLevel()), MerchantLogTypeEnum.EDIT_SUB_INFO_STATUS, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language, ip);
        }
        return Response.returnSuccess(true);
    }

    @Override
    public MerchantPO getMerchantById(String id) {
        return merchantMapper.selectById(id);
    }

    /**
     * 获取商户详情
     *
     * @param id
     * @return
     */
    @Override
    public Response merchantDetail(String id) {

        return Response.returnSuccess(this.abstractMerchantDetail(id));
    }

    /**
     * 根据商户类型号查询本月商户列表
     *
     * @param agentLevel
     * @return
     */
    @Override
    public Response selectMonthList(Integer agentLevel) {
        MerchantVO requestVO = new MerchantVO();
        requestVO.setAgentLevel(agentLevel == null ? 0 : agentLevel);
        requestVO.setCreateTime(getMonthFirDay());
        return Response.returnSuccess(merchantMapper.selectMonthList(requestVO));
    }

    /**
     * 创建商户管理员
     *
     * @param id
     * @param adminName
     * @param adminPassword
     * @param language
     * @return
     */
    @Override
    public Response createAdmin(String id, String adminName, String adminPassword, Integer userId, String language, String ip, String agentLevel) {
        try {
            MerchantPO merchantPO = this.selectById(id);
            if (merchantPO == null) {
                return Response.returnFail(ResponseEnum.DELETE_FAILS);
            }
            Map<String, String> map = Maps.newHashMap();
            map.put("merchantAdmin", adminName);
            int count = this.countByParam(map);
            if (count > 0) {
                return Response.returnFail(ResponseEnum.MARCHANT_ADMIN_NAME_IS_EXIST);
            }
            MerchantVO merchantVO = new MerchantVO();
            merchantVO.setId(id);
            BeanUtils.copyProperties(merchantPO, merchantVO);
            //2058需求，增加密码复杂程度，随机生成至少12位且必须含特殊字符+大小字母+数字
//        adminUserForm.setPassword(adminUserForm.getUsername() + user.getMerchantCode());
            String psw = CreatPswUtil.getPsw(12);//生成随机密码
            merchantVO.setAdminPassword(psw);
            merchantVO.setPswCode(psw);
            merchantVO.setMerchantAdmin(adminName);
            return this.updateMerchant(merchantVO, userId, language, ip, agentLevel);
        } catch (Exception e) {
            log.error("MerchantController.update,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 根据商户CODE查询商户名称
     *
     * @return
     */
    @Override
    public Response queryMerchantListByParam() {
        Map<String, Object> param = Maps.newHashMap();
        return Response.returnSuccess(merchantMapper.queryMerchantListByParam(param));
    }

    @Override
    public Response queryMerchantTree() {
        List<MerchantPO> list = merchantMapper.queryMerchantTree();
        List<MerchantTree> treeList = new ArrayList<>(list.size());
        for (MerchantPO po : list) {
            MerchantTree merchantTree = new MerchantTree();
            merchantTree.setId(po.getId());
            merchantTree.setParentId(po.getParentId());
            merchantTree.setMerchantCode(po.getMerchantCode());
            merchantTree.setMerchantName(po.getMerchantName());
            treeList.add(merchantTree);
        }
        List<MerchantTree> tree = buildTree(treeList);
        return Response.returnSuccess(tree);
    }

    @Override
    public Response getMerchantListTree() {
        try {
            return Response.returnSuccess(abstractGetMerchantTree(TREE_LIST));
        } catch (Exception e) {
            log.error("获取商户树失败!", e);
            return Response.returnFail("获取商户树失败");
        }
    }

    @Override
    public Response getMerchantCodeListTree() {
        try {
            List<MerchantTree> treeList = abstractGetMerchantTree(TREE_LIST);
            for (MerchantTree tree : treeList) {
                tree.setMerchantName(tree.getMerchantCode());
            }
            return Response.returnSuccess(treeList);
        } catch (Exception e) {
            log.error("获取商户树失败!", e);
            return Response.returnFail("获取商户树失败");
        }
    }

    @Override
    public Response getMerchantNameListTree() {
        try {
            List<MerchantTree> treeList = abstractGetMerchantTree(TREE_LIST);
            return Response.returnSuccess(treeList);
        } catch (Exception e) {
            log.error("获取商户树失败!", e);
            return Response.returnFail("获取商户树失败");
        }
    }

    @Override
    public Response merchantDomainList(Integer merchantTag, Integer containsType, String containsStr, String parentCode, Integer pageSize, Integer pageIndex) {
        try {
            if (containsType == null) {
                containsType = 1;
            }
            pageSize = (pageSize == null || pageSize == 0) ? 20 : pageSize;
            pageIndex = pageIndex == null ? 1 : pageIndex;
            PageHelper.startPage(pageIndex, pageSize);
            PageInfo<MerchantPO> pageInfo = new PageInfo<>(merchantMapper.merchantDomainList(merchantTag, containsType, containsStr, parentCode));
            return Response.returnSuccess(pageInfo);
        } catch (Exception e) {
            log.error("获取商户域名失败!", e);
            return Response.returnFail("获取商户域名失败");
        }
    }

    @Override
    public Response merchantAppDomainList(Integer merchantTag, Integer containsType, String containsStr, String parentCode, String merchantCode, Integer pageSize, Integer pageIndex) {
        try {
            if (containsType == null) {
                containsType = 1;
            }
            pageSize = (pageSize == null || pageSize == 0) ? 20 : pageSize;
            pageIndex = pageIndex == null ? 1 : pageIndex;
            PageHelper.startPage(pageIndex, pageSize);
            PageInfo<MerchantPO> pageInfo = new PageInfo<>(merchantMapper.merchantAppDomainList(merchantTag, containsType, containsStr, parentCode, merchantCode));
            return Response.returnSuccess(pageInfo);
        } catch (Exception e) {
            log.error("获取商户域名失败!", e);
            return Response.returnFail("获取商户域名失败");
        }
    }

    @Override
    public Response updateDomainList(Integer userId, Integer merchantTag, Integer containsType, String containsStr, String newDomainStr, String merchantCode, String parentCode, String language, String ip) {
        int num = 0;
        if (containsStr != null) {
            containsStr = containsStr.trim();
        } else {
            containsType = null;
        }
        if (newDomainStr != null) {
            newDomainStr = newDomainStr.trim();
        }

        if (StringUtils.isBlank(merchantCode)) {
            merchantCode = null;
        } else {
            containsType = null;
        }
        if (StringUtils.isBlank(parentCode)) {
            parentCode = null;
        }
        List<MerchantPO> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(merchantCode)) {
            MerchantPO merchantPO = merchantMapper.getMerchant(merchantCode);
            list.add(merchantPO);
        } else {
            list = merchantMapper.merchantDomainList(merchantTag, containsType, containsStr, parentCode);
        }
        log.info("updateDomainList list:" + list.size());
        if (merchantCode == null && parentCode == null && (containsType != null && containsType != 1)) {
            num += merchantMapper.updateDomainList(merchantTag, containsType, containsStr, newDomainStr, merchantCode, parentCode, "pc");
            num += merchantMapper.updateDomainList(merchantTag, containsType, containsStr, newDomainStr, merchantCode, parentCode, "h5");
            log.info("updateDomainList containsType1");
        } else {
            num += merchantMapper.updateDomainList(merchantTag, containsType, containsStr, newDomainStr, merchantCode, parentCode, "");
            log.info("updateDomainList containsType0");
        }
        try {
            for (MerchantPO merchantPO : list) {
                if (merchantPO == null || num == 0) {
                    return Response.returnSuccess(num);
                }
                //记录日志
                String username = loginUserService.getLoginUser(userId);
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("domain"));
                vo.getBeforeValues().add("pc:" + merchantPO.getPcDomain() == null ? "" : merchantPO.getPcDomain());
                vo.getBeforeValues().add(" h5:" + merchantPO.getH5Domain() == null ? "" : merchantPO.getH5Domain());
                vo.getAfterValues().add(newDomainStr);
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_domain_edit, MerchantLogTypeEnum.EDIT_MERCHANT__DOMAIN, vo,
                        MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language, ip);
            }
        } catch (Exception e) {
            log.error("记录日志出错", e);
        }
        return Response.returnSuccess(num);
    }

    @Override
    public Response updateAppDomainList(Integer userId, Integer merchantTag, Integer containsType, String containsStr, String newDomainStr, String merchantCode, String parentCode, String language, String ip) {
        int num = 0;
        if (containsStr != null) {
            containsStr = containsStr.trim();
        } else {
            containsType = null;
        }
        if (newDomainStr != null) {
            newDomainStr = newDomainStr.trim();
        }

        if (StringUtils.isBlank(merchantCode)) {
            merchantCode = null;
        } else {
            containsType = null;
        }
        List<MerchantPO> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(merchantCode)) {
            MerchantPO merchantPO = merchantMapper.getMerchant(merchantCode);
            list.add(merchantPO);
        } else {
            list = merchantMapper.merchantAppDomainList(merchantTag, containsType, containsStr, parentCode, null);
        }
        log.info("updateAppDomainList list:" + list.size());
        num += merchantMapper.updateAppDomainList(merchantTag, containsType, containsStr, newDomainStr, merchantCode, parentCode);
        try {
            for (MerchantPO merchantPO : list) {
                if (merchantPO == null || num == 0) {
                    return Response.returnSuccess(num);
                }
                //记录日志
                String username = loginUserService.getLoginUser(userId);
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                MerchantLogPageEnum pageEnum = null;
                if (StringUtils.isNotBlank(merchantCode)) { //批量
                    vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("domains"));
                    pageEnum = MerchantLogPageEnum.SET_MERCHANT_APPDOMAIN_ALL_edit;
                } else {
                    vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("domain"));
                    pageEnum = MerchantLogPageEnum.SET_MERCHANT_Appdomain_edit;
                }

                vo.getBeforeValues().add("app:" + merchantPO.getAppDomain() == null ? "" : merchantPO.getAppDomain());
                vo.getAfterValues().add(newDomainStr);
                merchantLogService.saveLog(pageEnum, MerchantLogTypeEnum.EDIT_MERCHANT__DOMAIN, vo,
                        MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language, ip);
            }
        } catch (Exception e) {
            log.error("记录日志出错", e);
        }
        return Response.returnSuccess(num);
    }

    @Override
    public Response cleanMerchant(Integer merchantTag, Integer containsType, String containsStr, String merchantCode, String parentCode) {
        Object obj = merchantApiClient.kickoutMerchant(merchantCode);
        log.info("踢出商户:" + obj);
        return Response.returnSuccess();
    }

    private void kickoutMerchant(String merchantCode) {
        try {
            merchantApiClient.kickoutMerchant(merchantCode);
        } catch (Exception e) {
            log.error("踢出商户失败!", e);
        }
    }


    private List<MerchantTree> buildTree(List<MerchantTree> list) {
        Map<String, Object> newMap = new HashMap<>();
        List<MerchantTree> newList = new ArrayList<>();
        for (MerchantTree treeDto : list) {
            newMap.put(treeDto.getId(), treeDto);
        }
        for (MerchantTree treeDto : list) {
            MerchantTree parent = (MerchantTree) newMap.get(treeDto.getParentId());
            if (parent != null) {
                if (parent.getTrees() == null) {
                    List<MerchantTree> ch = new ArrayList<>();
                    ch.add(treeDto);
                    parent.setTrees(ch);
                } else {
                    List<MerchantTree> ch = parent.getTrees();
                    ch.add(treeDto);
                    parent.setTrees(ch);
                }
            } else {
                newList.add(treeDto);
            }
        }
        return newList;
    }

    /**
     * 查询证书列表
     *
     * @param merchantName
     * @param parentId     上级商户ID
     * @param pageSize
     * @param pageNum
     * @param language
     * @return
     */
    @Override
    public Response<PageInfo<TMerchantKey>> queryKeyList(String merchantName, String merchantCode, String parentId, Integer
            pageSize, Integer pageNum, String language) {
        pageSize = pageSize == null ? 20 : pageSize;
        pageNum = pageNum == null ? 1 : pageNum;
        PageHelper.startPage(pageNum, pageSize, true);
        MerchantVO merchantVO = new MerchantVO();
        merchantVO.setParentId(parentId);
        merchantVO.setMerchantName(merchantName);
        merchantVO.setMerchantCode(merchantCode);
        return Response.returnSuccess(new PageInfo<>(assemblykeyList(merchantMapper.queryKeyList(merchantVO), language, Constant.INTERNAL_MERCHANT)));
    }

    /**
     * 重新生成KEY
     *
     * @return
     */
    @Override
    public Response<Object> generateKey() {
        return Response.returnSuccess(CreateSecretKey.keyCreate());
    }

    /**
     * 修改商户的KEY
     *
     * @param merchantCode
     * @param key
     * @param startTime
     * @param endTime
     * @param language
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Object> updateKey(String merchantCode, String key, String keyLabel, String startTime, String endTime, Integer userId, String language, String ip) throws Exception {
        MerchantPO merchant = merchantMapper.getMerchantInfo(merchantCode);
        String keys = CreateSecretKey.keyCreate();
        keys =AESUtils.aesEncode(keys);
        boolean result = this.abstractUpdateKey(merchantCode, keys, keyLabel, startTime, endTime, "");
        if (result) {
            //发送mango告警
            String test = String.format("环境【%s】\n商户【%s】更换密钥，请悉知！",env, merchant.getMerchantName() + StringPool.AMPERSAND + merchant.getMerchantCode());
            mongoService.send(test, target_name);
            //记录日志
            String username = loginUserService.getLoginUser(userId);
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("key"));
            vo.getBeforeValues().add("***");
            vo.getAfterValues().add("***");
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_EDITKEY, MerchantLogTypeEnum.EDIT_INFO, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language, ip);
        }
        return result ? Response.returnSuccess(true)
                : Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     * 获取本地管理员列表
     *
     * @return
     */
    @Override
    public Response<Object> adminList() {
        return Response.returnSuccess(merchantMapper.getAdminList());
    }

    /**
     * 根据商户类型分组统计商户总数
     *
     * @return
     */
    @Override
    public Response<Object> queryAgentCount() {
        return Response.returnSuccess(merchantMapper.queryAgentCount());
    }

    /**
     * 上下分
     *
     * @param merchantCode
     * @param username
     * @param transferType
     * @param amount
     * @return
     */
    @Override
    public Response<Object> transfer(String merchantCode, String username, Integer transferType, Double amount) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userName", username);
        params.add("merchantCode", merchantCode);
        params.add("transferType", transferType);
        params.add("amount", amount);
        Long transferId = System.currentTimeMillis();
        params.add("transferId", transferId);
        Long timestamp = System.currentTimeMillis();
        String merchantKey = merchantMapper.getKey(merchantCode, Constant.REALTIME_TABLE);
        params.add("timestamp", timestamp);
        String signature = Md5Util.getMD5(merchantCode + PandaConstant.and + username + PandaConstant.and + transferType + PandaConstant.and + amount + PandaConstant.and + transferId + PandaConstant.and + timestamp, merchantKey);
        params.add("signature", signature);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("transfer_url:" + transfer_url);
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(transfer_url, new HttpEntity<>(params, headers), String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        JSONObject inner = (JSONObject) jsonObject.get("data");
        return Response.returnSuccess(inner);
    }

    /**
     * 查询上下分记录列表
     *
     * @param username
     * @param merchantCode
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @Override
    public Response<Object> queryTransferList(String username, String merchantCode, Integer pageSize, Integer
            pageIndex) {
        if (StringUtils.isAnyEmpty(username, merchantCode)) {
            return Response.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userName", username);
        params.add("merchantCode", merchantCode);
        Long timestamp = System.currentTimeMillis();
        params.add("timestamp", timestamp);
        String merchantKey = merchantMapper.getKey(merchantCode, Constant.REALTIME_TABLE);
        params.add("pageNum", 1);
        params.add("pageSize", 100);
        Date d = new Date();
        String startTime = DateUtils.addDays(d, -7).getTime() + "";
        String endTime = d.getTime() + "";
        params.add("startTime", startTime);
        params.add("endTime", endTime);
        String signature = Md5Util.getMD5(merchantCode + PandaConstant.and + username + PandaConstant.and + startTime + PandaConstant.and + endTime + PandaConstant.and + timestamp, merchantKey);
        params.add("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("query_transfer_list_url:" + query_transfer_list_url);
        ResponseEntity<String> response =
                HttpConnectionPool.restTemplate.postForEntity(query_transfer_list_url, new HttpEntity<>(params, headers), String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        JSONObject inner = (JSONObject) jsonObject.get("data");
        log.info(inner.toJSONString());
        return Response.returnSuccess(inner);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMerchantStatus() throws Exception {
        try {
            MerchantVO merchantVO = new MerchantVO();
            merchantVO.setStatus(1);
            Date now = getStartTime(new Date());
            String nowStr = DateFormatUtils.format(now, "yyyyMMddHHmmss");
            long nowL = Long.parseLong(nowStr);
            List<MerchantPO> merchantPOList = merchantMapper.queryMerchantList(ImmutableMap.of("status", 1));
            for (MerchantPO po : merchantPOList) {
                String end = po.getEndTime();
                if (StringUtils.isEmpty(end)) {
                    continue;
                }
                Date endDate = DateUtils.parseDate(end, "yyyy-MM-dd");
                String endStr = DateFormatUtils.format(endDate, "yyyyMMddHHmmss");
                long endL = Long.parseLong(endStr);
                if (endL < nowL) {
                    String merchantCode = po.getMerchantCode();
                    log.info("开始失效商户:" + merchantCode);
                    merchantMapper.updateMerchantStatus(merchantCode, 0,null);
                    tUserMapper.updateAllMerchantUser(merchantCode, 1);
                    log.info(now + "失效商户" + po.getMerchantCode());
                }
            }
        } catch (Exception e) {
            log.error("更新商户状态异常!", e);
            throw new Exception("更新商户状态异常!");
        }
    }

    @Override
    public boolean updateMerchantFTPInfo(String merchantCode, String user, String pwd) {
        try {
            merchantMapper.updateMerchantFTPInfo(merchantCode, user, pwd);
            return true;
        } catch (Exception e) {
            log.error("修改FTP异常!", e);
            return false;
        }
    }

    /**
     * 获取每天的开始时间 00:00:00:00
     *
     * @param date
     * @return
     */
    private Date getStartTime(Date date) {
        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(date);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        return dateStart.getTime();
    }

    @Override
    public Response getMerchantLanguageList() {
        return Response.returnSuccess(merchantMapper.getMerchantLanguageList());
    }

    @Override
    public Response getMerchantRiskList() {
        List<MerchantPO> result = Lists.newArrayList();
        List<MerchantPO> agent = Lists.newArrayList();
        //获取直营（一级）和渠道商户
        MerchantPO po = new MerchantPO();
        po.setStatus(1);
        po.setAgentLevel(0);
        List<MerchantPO> agent0 = merchantMapper.selectRiskList(po);
        po.setAgentLevel(1);
        List<MerchantPO> agent1 = merchantMapper.selectRiskList(po);
        agent.addAll(agent0);
        agent.addAll(agent1);
        for (MerchantPO p1 : agent) {
            MerchantPO temp = new MerchantPO();
            temp.setStatus(1);
            temp.setAgentLevel(2);
            temp.setParentId(p1.getId());
            List<MerchantPO> childls = merchantMapper.selectRiskList(temp);
            p1.setChildList(childls);
        }
        //需要把风险商户排序在前
        List<MerchantPO> fulllist = Lists.newArrayList();
        List<MerchantPO> emptylist = Lists.newArrayList();
        for (MerchantPO p11 : agent) {
            Boolean flag = false;
            List<MerchantPO> childl = p11.getChildList();
            if (childl != null) {
                for (MerchantPO c1 : childl) {
                    if (c1 != null && c1.getIsRisk() != null && c1.getIsRisk() == 1) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                fulllist.add(p11);
            } else {
                emptylist.add(p11);
            }
        }
        result.addAll(fulllist);
        result.addAll(emptylist);
        return Response.returnSuccess(result);
    }

    private Integer updateMerchantTagMarketStatus(String merchantCode, TagMarketMsg tagMarketMsg) {
        MerchantConfig config = new MerchantConfig();
        config.setMerchantCode(merchantCode);
        config.setTagMarketStatus(tagMarketMsg.getTagMarketStatus());
        config.setTagMarketLevel(tagMarketMsg.getTagMarketLevelId());
        config.setMarketLevelIdPc(tagMarketMsg.getTagMarketLevelIdPc());
        merchantConfigMapper.updateMerchantConfig(config);
        return 0;
    }

    @Override
    public Response updateKanaCode(Integer userId, String kanaCode, String merchantCode, String language, String ip) {
        int num = 0;
        MerchantPO merchantPO = merchantMapper.getMerchantNotStatus(merchantCode);
        num = merchantMapper.updateKanaCode(kanaCode, new Date(), merchantCode);
        MerchantCodeConfigLog configLog = new MerchantCodeConfigLog();
        configLog.setMerchantCode(merchantCode);
        configLog.setCreateTime(System.currentTimeMillis());
        String month = org.apache.http.client.utils.DateUtils.formatDate(new Date(), "yyyy-MM");
        configLog.setMonth(month);
        configLog.setCode(kanaCode);
        merchantConfigMapper.insertMerchantCodeConfigLog(configLog);
        if (merchantPO.getAgentLevel() == 1) {
            String serNum = "";
            if (merchantPO.getSerialNumber() != null) {
                serNum = merchantPO.getSerialNumber().toString();
            }
            merchantMapper.updateKanaCodeByParentId(kanaCode + serNum, new Date(), merchantPO.getId());
            List<String> codelist = merchantMapper.getMerhcantCodeList(merchantPO.getId());
            for (String codeStr : codelist) {
                configLog.setMerchantCode(codeStr);
                merchantConfigMapper.insertMerchantCodeConfigLog(configLog);
            }
        }

        if (num > 0) {
            log.info("更新成功 merchantCode = {} , oldvalue = {} new = {}", merchantCode, merchantPO.getKanaCode() == null ? "-" : merchantPO.getKanaCode(), kanaCode);
        }
        try {
            //记录日志
            String username = loginUserService.getLoginUser(userId);
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("kanaCode"));
            vo.getBeforeValues().add(merchantPO.getKanaCode() == null ? "-" : merchantPO.getKanaCode());
            vo.getAfterValues().add(kanaCode);
            merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_kanacode, MerchantLogTypeEnum.EDIT_MERCHANT_KANACODE, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language, ip);
        } catch (Exception e) {
            log.error("记录日志出错", e);
        }
        return Response.returnSuccess(num);
    }


    @Override
    public Response updateSerialNumber(Integer userId, Integer serialNumber, String merchantCode, String language, String ip) {
        int num = 0;
        MerchantPO merchantPO = merchantMapper.getMerchantNotStatus(merchantCode);
        if (serialNumber != null) {
            int count = merchantMapper.checkSerialNumber(serialNumber, merchantPO.getParentId());
            if (count > 0) {
                return Response.returnFail(ResponseEnum.SERIAL_NUMBER_EXIST);
            }
        }
        num = merchantMapper.updateSerialNumber(serialNumber, merchantCode);
        if (num > 0) {
            log.info("更新成功 merchantCode = {} , oldvalue = {} new = {}", merchantCode, merchantPO.getKanaCode() == null ? "-" : merchantPO.getKanaCode(), serialNumber);
        }
        try {
            //记录日志
            String username = loginUserService.getLoginUser(userId);
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("SerialNumber"));
            vo.getBeforeValues().add(merchantPO.getKanaCode() == null ? "-" : merchantPO.getKanaCode());
            vo.getAfterValues().add("" + serialNumber);
            merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_kanacode, MerchantLogTypeEnum.EDIT_MERCHANT_KANACODE, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language, ip);
        } catch (Exception e) {
            log.error("记录日志出错", e);
        }
        return Response.returnSuccess(num);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateAdminUserName(HttpServletRequest request, AdminUserNameUpdateReqVO adminUserNameUpdateReqVO, String language, String ip, String agentLevel) {
        try {
            MerchantPO merchantPO = merchantMapper.selectById(adminUserNameUpdateReqVO.getId());
            if (Objects.isNull(merchantPO)) {
                return Response.returnFail(ResponseEnum.USER_MISS);
            }
            String beforeValues = merchantPO.getMerchantAdmin();
            merchantPO.setMerchantAdmin(adminUserNameUpdateReqVO.getUserName());
            merchantMapper.updateMerchantAdminById(merchantPO);

            QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseCommonColumnToStr.MERCHANT_ID, merchantPO.getId());
            queryWrapper.eq(DatabaseCommonColumnToStr.IS_ADMIN, CommonDefaultValue.DifferentiateStatus.YES);
            List<AdminUser> adminUserList = adminUserMapper.selectList(queryWrapper);
            if (adminUserList.size() == CommonDefaultValue.ONE) {
                AdminUser adminUser = adminUserList.get(0);
                AdminUser adminUserAnother = adminUserMapper.selectOne(new QueryWrapper<AdminUser>().eq("username", adminUserNameUpdateReqVO.getUserName()));
                if (adminUserAnother != null && !Objects.equals(adminUser.getId(), adminUserAnother.getId())) {
                    return Response.returnFail(ResponseEnum.MARCHANT_ADMIN_NAME_IS_EXIST);
                }
                AdminUser adminUserUpd = new AdminUser();
                adminUserUpd.setUsername(adminUserNameUpdateReqVO.getUserName());
                adminUserUpd.setId(adminUser.getId());
                adminUserMapper.updateById(adminUserUpd);
            }


            MerchantLogPO merchantLogPO = new MerchantLogPO();
            merchantLogPO.setMerchantCode(merchantPO.getMerchantCode());
            merchantLogPO.setUserId(request.getHeader("user-id"));
            merchantLogPO.setDataId(merchantPO.getId());
            merchantLogPO.setUserName(request.getHeader("merchantName"));
            merchantLogPO.setLogTag(MerchantLogConstants.MERCHANT_IN);
            merchantLogPO.setMerchantName(merchantPO.getMerchantName());
            merchantLogPO.setOperatType(MerchantLogTypeEnum.SET_MANAGER.getCode());
            merchantLogPO.setOperatField(JsonUtils.listToJson(Collections.singletonList(Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantLogTypeEnum.SET_MANAGER.getRemark() : MerchantLogTypeEnum.SET_MANAGER.getRemarkEn())));
            merchantLogPO.setBeforeValues(JsonUtils.listToJson(Collections.singletonList(beforeValues)));
            merchantLogPO.setAfterValues(JsonUtils.listToJson(Collections.singletonList(adminUserNameUpdateReqVO.getUserName())));
            merchantLogPO.setPageCode(MerchantLogPageEnum.MERCHANT_INFO_MANAGER.getCode());
            merchantLogPO.setPageName(Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantUtil.getModeInfo(agentLevel, language) + "-" + MerchantLogTypeEnum.CHAT_MANAGER.getRemark() : MerchantUtil.getModeInfo(agentLevel, language) + "-" + MerchantLogTypeEnum.CHAT_MANAGER.getRemarkEn());
            merchantLogPO.setTypeName(Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantLogTypeEnum.SET_MANAGER.getRemark() : MerchantLogTypeEnum.SET_MANAGER.getRemarkEn());
            merchantLogPO.setOperatTime(System.currentTimeMillis());
            merchantLogPO.setIp(ip);
            merchantLogService.saveLog(merchantLogPO);

            // 踢商户
            Object obj = merchantApiClient.kickoutMerchant(merchantPO.getMerchantCode());
            log.info("踢出商户:" + obj);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("对内商户后台-商户中心-商户管理-修改超级管理员名称异常：", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateOpenBill(MerchantVO merchantVO, String language, String ip) {
        try {
            MerchantPO merchantPO = merchantMapper.selectById(merchantVO.getId());
            if (Objects.isNull(merchantPO)) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }

            merchantMapper.updateOpenBill(merchantVO.getId(),merchantVO.getIsOpenBill());

            MerchantLogPO merchantLogPO = new MerchantLogPO();
            merchantLogPO.setMerchantCode(merchantPO.getMerchantCode());
            merchantLogPO.setMerchantName(merchantPO.getMerchantName());
            merchantLogPO.setOperatType(MerchantLogTypeEnum.SET_MANAGER.getCode());
            merchantLogPO.setOperatField(JsonUtils.listToJson(Collections.singletonList(DatabaseCommonColumnToStr.OPEN_BILL)));
            merchantLogPO.setBeforeValues(JsonUtils.listToJson(Collections.singletonList(merchantPO.getIsOpenBill())));
            merchantLogPO.setAfterValues(JsonUtils.listToJson(Collections.singletonList(merchantVO.getIsOpenBill())));
            merchantLogPO.setPageCode(MerchantLogPageEnum.MERCHANT_INFO_MANAGER.getCode());
            merchantLogPO.setPageName(Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantLogPageEnum.MERCHANT_INFO_MANAGER.getRemark() : MerchantLogPageEnum.MERCHANT_INFO_MANAGER.getEn());
            merchantLogPO.setTypeName(Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantLogTypeEnum.SET_MANAGER.getRemark() : MerchantLogTypeEnum.SET_MANAGER.getRemarkEn());
            merchantLogPO.setIp(ip);
            merchantLogService.saveLog(merchantLogPO);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("对内商户后台-商户中心-商户管理-修改开启对帐异常：", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    @Override
    public List<?> getMerchantList() {
        List<ThirdMerchantVo> list = cpMerchantMapper.getMerchantList();
        for (ThirdMerchantVo vo : list){
            vo.setCreatTime(vo.getCreatedAt().getTime() / 1000);
        }
        return list;
    }

    @Override
    public List<?> getTblMerchantList() {
        return thirdMerchantMapper.getMerchantList();
    }

    public List<?> getMerchantByCodes(List<String> merchantCode) {
        return cpMerchantMapper.getMerchantByCodes(merchantCode);
    }

    public List<?> getMerchantByName(String merchantName) {
        return merchantMapper.getMerchantByName(merchantName);
    }

    @Override
    public List<MerchantVO> queryMerchantListByParams(JSONObject param) {
        MerchantVO merchantVO = new MerchantVO();
        merchantVO.setPageIndex(1);
        merchantVO.setPageSize(10000);
        if (param != null) {
            String merchantCodesStr = param.getString("merchantCodes");
            if (StringUtils.isNotEmpty(merchantCodesStr)) {
                Set<String> set = Arrays.stream(merchantCodesStr.split(",")).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(set)) {
                    merchantVO.setMerchantCodes(Lists.newArrayList(set));
                }
            }
            if (StringUtils.isNotEmpty(param.getString("merchantName"))) {
                merchantVO.setMerchantName(param.getString("merchantName"));
            }
        }

        return this.queryMerchantList(merchantVO).getList();

    }

    /**
     * merchantName:商户name
     * merchantCodes:商户code  ，号分割
     */
    @Override
    public List<MerchantSimpleVO> queryMerchantSimpleListByParams(JSONObject param) {
        if (param == null) {
            param = new JSONObject();
        }
        param.put("pageIndex", 0);
        param.put("pageSize", 10000);
        String merchantCodesStr = param.getString("merchantCodes");
        if (StringUtils.isNotEmpty(merchantCodesStr)) {
            Set<String> set = Arrays.stream(merchantCodesStr.split(",")).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(set)) {
                param.put("merchantCodes", set);
            }
        }
        return merchantMapper.queryMerchantSimpleListByParams(param);
    }

    public List<?> getTblMerchantByCodes(List<String> merchantCode) {
        return thirdMerchantMapper.getTblMerchantByCodes(merchantCode);
    }

    @Override
    public void merchantInfoExport(HttpServletRequest request, MerchantVO merchantVO, String language) {
        MerchantLogPageEnum pageEnum = MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchantVO.getAgentLevel());
        merchantVO.setPageIndex(1);
        merchantVO.setPageSize(1);
        merchantVO.setLanguage(language);

        List<MerchantVO> resultList = this.queryListByParam(merchantVO);
        if (CollectionUtil.isEmpty(resultList)) {
            throw new RuntimeException("暂无数据！");
        }
        merchantVO.setPageSize(100000);
        log.info("导出参数{}", JSON.toJSONString(merchantVO));
        merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "商户信息_" : "MerchantInfo_"),
                null, request.getHeader("merchantName"), JSON.toJSONString(merchantVO),
                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? String.format("%s-导出商户信息", pageEnum.getRemark()) : String.format("%s-MerchantInfoExport", pageEnum.getEn()),
                "merchantInfoExportServiceImpl");


    }


    @Override
    public void merchantInfoKanaCodeExport(HttpServletRequest request, MerchantVO merchantVO, String language) {
        MerchantLogPageEnum pageEnum = MerchantLogPageEnum.getMerchantLogPageEnumByAgentLevel(merchantVO.getAgentLevel());
        merchantVO.setPageIndex(1);
        merchantVO.setPageSize(1);
        merchantVO.setLanguage(language);

        List<MerchantVO> resultList = this.queryListByParam(merchantVO);
        if (CollectionUtil.isEmpty(resultList)) {
            throw new RuntimeException("暂无数据！");
        }
        merchantVO.setPageSize(100000);
        log.info("导出参数{}", JSON.toJSONString(merchantVO));
        merchantFileService.saveFileTask((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "公司代码_" : "MerchantInfo_"),
                null, request.getHeader("merchantName"), JSON.toJSONString(merchantVO),
                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? String.format("%s-导出公司代码信息", pageEnum.getRemark()) : String.format("%s-MerchantInfoKanaCodeExport", pageEnum.getEn()),
                "merchantInfoKanaCodeExportServiceImpl");


    }

    /**
     * 分页查询商户列表
     *
     * @param merchantVO
     * @return
     */
    @Override
    public List<?> list(MerchantVO merchantVO) {
        return this.queryMerchants(merchantVO);
    }

    @Override
    public APIResponse merchantGroupInfoByGroupId(Long merchantGroupId) {
        try {
            List<TMerchantGroupInfo> merchantGroupInfos = merchantGroupInfoMapper.tMerchantGroupInfoByGroupId(merchantGroupId);
            return APIResponse.returnSuccess(merchantGroupInfos);
        } catch (Exception ex) {
            log.error("merchantGroupInfoByGroupId:", ex);
            return APIResponse.returnFail("merchantGroupInfoByGroupId 查询失败!");
        }
    }

    @Override
    public APIResponse load(Integer id) {
        try {
            TMerchantGroupInfo merchantGroupInfo = merchantGroupInfoMapper.load(id);
            return APIResponse.returnSuccess(merchantGroupInfo);
        } catch (Exception ex) {
            log.error("load:", ex);
            return APIResponse.returnFail("load 查询失败!");
        }
    }

    @Override
    public int checkMerchantDomainExistByDomainType(String domain, Integer domainType) {
        try {
            return merchantMapper.checkMerchantDomainExistByDomainType(domain, domainType);
        } catch (Exception ex) {
            log.error("checkMerchantDomainExist:", ex);
            return 0;
        }
    }

    public APIResponse updateLanguage(MerchantVO merchantVO) {

        return APIResponse.returnSuccess(merchantMapper.updateLanguage(merchantVO.getId(), merchantVO.getLanguageList()));
    }

    public APIResponse getLanguageById(String id) {

        return APIResponse.returnSuccess(merchantMapper.getLanguageById(id));
    }

    @Override
    public APIResponse getMerchantGroupDomain(MerchantGroupDomainVO req) {
        return APIResponse.returnSuccess(multiterminalClient.getMerchantGroupDomain(req).getData());
    }

    @Override
    public APIResponse queryAnimation() {
        return APIResponse.returnSuccess(multiterminalClient.queryAnimation().getData());
    }

    @Override
    public List<?> getMerchantByDomains(String domain) {
        return merchantMapper.getMerchantByDomains(domain);
    }

    public String getAnimationURL() {
        return matchInfoCurMapper.getAnimationURL();
    }

    public int updateAnimationURL(String url) {
        return matchInfoCurMapper.aniUpdate(url);
    }

    /**
     * 更新商户默认视频配置
     *
     * @param merchantConfig
     * @return
     */
    @Override
    public APIResponse updateDefaultVideoDomain(HttpServletRequest request, MerchantConfig merchantConfig) {
        if (StringUtils.isEmpty(merchantConfig.getDefaultVideoDomain())) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        try {
            log.info("special_merchant" + special_merchant);
            List<String> merchantCodeList = Arrays.asList(special_merchant.split(","));
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                List<String> codeList = this.checkMerchant(merchantConfig, merchantCodeList);
                merchantConfig.setMerchantCodeList(codeList);
                String oldVideoDomain = merchantConfigMapper.queryDefaultVideoDomain(merchantCodeList);
                merchantConfigMapper.updateDefaultVideoDomain(merchantConfig);
                ExecutorInstance.executorService.submit(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        log.error("asyncUpdateMerchantCache.sleep=", e);
                    }
                    merchantApiClient.updateMerchantCache(merchantConfig);
                    log.info("updateMerchantUserCache,更新商户缓存成功");
                });
                /**
                 *  添加系统日志
                 * */
                String userId = request.getHeader("user-id");
                String username = request.getHeader("merchantName");
                String ip = IPUtils.getIpAddr(request);
                String language = request.getHeader("language");
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("defaultVideoDomain"));
                vo.getBeforeValues().add(oldVideoDomain);
                vo.getAfterValues().add(merchantConfig.getDefaultVideoDomain());
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_72, MerchantLogTypeEnum.EDIT_INFO, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, null, null, null, null, language, ip);
            }
        } catch (Exception e) {
            log.error("更新商户默认视频配置失败!" + e);
        }
        return APIResponse.returnSuccess();
    }

    private List<String> checkMerchant(MerchantConfig merchantConfig, List<String> merchantCodeList) {
        merchantConfig.setUpdateTime(new Date());
        merchantConfig.setMerchantCodeList(merchantCodeList);
        List<MerchantPO> merchantList = merchantConfigMapper.queryMerchantInfo(merchantCodeList);
        List<String> codeList = Lists.newArrayList();
        for (MerchantPO item : merchantList) {
            if (item.getAgentLevel().equals(AGENT_LEVEL)) {
                List<MerchantPO> ChildrenMerchant = merchantConfigMapper.selectChildrenMerchant(item.getId());
                for (MerchantPO merchantPo : ChildrenMerchant) {
                    codeList.add(merchantPo.getMerchantCode());
                }
            } else {
                codeList.add(item.getMerchantCode());
            }
        }
        return codeList;
    }

    /**
     * 查询商户默认视频域名配置
     *
     * @return
     */
    @Override
    public APIResponse queryDefaultVideoDomain() {
        MerchantConfig merchantConfig = new MerchantConfig();
        List<String> merchantCodeList = Arrays.asList(special_merchant.split(","));
        String defaultVideoDomain = "";
        if (CollectionUtils.isNotEmpty(merchantCodeList)) {
            List<String> codeList = this.checkMerchant(merchantConfig, merchantCodeList);
            merchantConfig.setMerchantCodeList(codeList);
            defaultVideoDomain = merchantConfigMapper.queryDefaultVideoDomain(merchantCodeList);
        }
        return APIResponse.returnSuccess(defaultVideoDomain);
    }

    @Override
    public APIResponse deleteVideoDomainCache() {
        try {
            MerchantConfig merchantConfig = new MerchantConfig();
            List<String> merchantCodeList = Arrays.asList(special_merchant.split(","));
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                List<String> codeList = this.checkMerchant(merchantConfig, merchantCodeList);
                merchantConfig.setMerchantCodeList(codeList);
            }
            ExecutorInstance.executorService.submit(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.error("asyncUpdateMerchantCache.sleep=", e);
                }
                merchantApiClient.updateMerchantCache(merchantConfig);
                log.info("updateMerchantUserCache,更新商户缓存成功");
            });
        } catch (Exception e) {
            log.error("deleteVideoDomainCache:", e);
            return APIResponse.returnFail("清除商户默认视频域名缓存失败!");
        }
        return APIResponse.returnSuccess("清除商户默认视频域名缓存成功!");
    }

    /**
     * 保存操作日志
     *
     * @return
     */
    @Override
    public APIResponse saveOperationLog(OperationLogVO operationLogVO) {
        if (null == operationLogVO) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        String operationPage = operationLogVO.getOperationPage();
        String operationType = operationLogVO.getOperationType();
        String userId = operationLogVO.getUserId();
        String username = operationLogVO.getUserName();
        String ip = operationLogVO.getIp();
        String language = operationLogVO.getLanguage();
        String merchantCode = operationLogVO.getMerchantCode();
        String merchantName = operationLogVO.getMerchantName();
        String dataId = operationLogVO.getDataId();
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.setFieldName(operationLogVO.getOperationColumns());
        vo.setBeforeValues(operationLogVO.getBeforeValues());
        vo.setAfterValues(operationLogVO.getAfterValues());
        assemblyOperationType(operationPage, operationType, userId, username, ip, language, merchantCode, merchantName, dataId, vo);
        return APIResponse.returnSuccess("日志保存成功!");
    }

    private void assemblyOperationType(String operationPage, String operationType, String userId, String username, String ip, String language, String merchantCode, String merchantName, String dataId, MerchantLogFiledVO vo) {
        if (Integer.valueOf(operationPage).equals(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_73.getId())) {
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ADD_ANCHOR.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_73, MerchantLogTypeEnum.ADD_ANCHOR, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.EDIT_ANCHOR.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_73, MerchantLogTypeEnum.EDIT_ANCHOR, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.APPLY_ANCHOR.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_73, MerchantLogTypeEnum.APPLY_ANCHOR, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.DISABLE_ANCHOR.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_73, MerchantLogTypeEnum.DISABLE_ANCHOR, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
        } else if (Integer.valueOf(operationPage).equals(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_74.getId())) {
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.SCHEDULING_LIVE.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_74, MerchantLogTypeEnum.SCHEDULING_LIVE, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.EDIT_LIVE.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_74, MerchantLogTypeEnum.EDIT_LIVE, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.START_LIVE.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_74, MerchantLogTypeEnum.START_LIVE, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.FINISH_LIVE.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_74, MerchantLogTypeEnum.FINISH_LIVE, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.DEL_LIVE.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_74, MerchantLogTypeEnum.DEL_LIVE, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
        } else if (Integer.valueOf(operationPage).equals(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_75.getId())) {
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ON_PREGAME_SHOW.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_75, MerchantLogTypeEnum.ON_PREGAME_SHOW, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.OFF_PREGAME_SHOW.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_75, MerchantLogTypeEnum.OFF_PREGAME_SHOW, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.EDIT_PREGAME_SHOW.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_75, MerchantLogTypeEnum.EDIT_PREGAME_SHOW, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.DEL_PREGAME_SHOW.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_75, MerchantLogTypeEnum.DEL_PREGAME_SHOW, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ADD_PREGAME_SHOW.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_75, MerchantLogTypeEnum.ADD_PREGAME_SHOW, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
        } else if (Integer.valueOf(operationPage).equals(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_76.getId())) {
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ADD_CHAT_ROOM.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_76, MerchantLogTypeEnum.ADD_CHAT_ROOM, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ON_CHAT_ROOM.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_76, MerchantLogTypeEnum.ON_CHAT_ROOM, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.OFF_CHAT_ROOM.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_76, MerchantLogTypeEnum.OFF_CHAT_ROOM, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.EXPORT_CHAT_CONTENT.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_76, MerchantLogTypeEnum.EXPORT_CHAT_CONTENT, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
        } else if (Integer.valueOf(operationPage).equals(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_77.getId())) {
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ADD_CHAT_ROOM_ANNOUNCEMENT.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_77, MerchantLogTypeEnum.ADD_CHAT_ROOM_ANNOUNCEMENT, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.EDIT_CHAT_ROOM_ANNOUNCEMENT.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_77, MerchantLogTypeEnum.EDIT_CHAT_ROOM_ANNOUNCEMENT, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ON_CHAT_ROOM_ANNOUNCEMENT.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_77, MerchantLogTypeEnum.ON_CHAT_ROOM_ANNOUNCEMENT, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.OFF_CHAT_ROOM_ANNOUNCEMENT.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_77, MerchantLogTypeEnum.OFF_CHAT_ROOM_ANNOUNCEMENT, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.DEL_CHAT_ROOM_ANNOUNCEMENT.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_77, MerchantLogTypeEnum.DEL_CHAT_ROOM_ANNOUNCEMENT, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.DEL_CHAT_ROOM_ANNOUNCEMENT.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_77, MerchantLogTypeEnum.ANNOUNCEMENT_PUSH_AND_DROP_SORT, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
        } else if (Integer.valueOf(operationPage).equals(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_78.getId())) {
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.USER_CLEAR_SCREEN.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_78, MerchantLogTypeEnum.USER_CLEAR_SCREEN, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.MERCHANT_CLEAR_SCREEN.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_78, MerchantLogTypeEnum.MERCHANT_CLEAR_SCREEN, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.CONTENT_AUDIT_RECALL_MESSAGE.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_78, MerchantLogTypeEnum.CONTENT_AUDIT_RECALL_MESSAGE, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.CHAT_ROOM_CLEAR_SCREEN.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_78, MerchantLogTypeEnum.CHAT_ROOM_CLEAR_SCREEN, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.CHAT_ROOM_DISABLE_SEND_MSG.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_78, MerchantLogTypeEnum.CHAT_ROOM_DISABLE_SEND_MSG, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.CHAT_ROOM_DISABLE_BASK_ORDER.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_78, MerchantLogTypeEnum.CHAT_ROOM_DISABLE_BASK_ORDER, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.Enable_Send_Msg.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_78, MerchantLogTypeEnum.Enable_Send_Msg, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ENABLE_BASK_ORDER.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_78, MerchantLogTypeEnum.ENABLE_BASK_ORDER, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
        } else if (Integer.valueOf(operationPage).equals(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_79.getId())) {
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ADD_SENSITIVE_WORDS.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_79, MerchantLogTypeEnum.ADD_SENSITIVE_WORDS, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.EDIT_SENSITIVE_WORDS.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_79, MerchantLogTypeEnum.EDIT_SENSITIVE_WORDS, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ON_SENSITIVE_WORDS.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_79, MerchantLogTypeEnum.ON_SENSITIVE_WORDS, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.OFF_SENSITIVE_WORDS.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_79, MerchantLogTypeEnum.OFF_SENSITIVE_WORDS, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
        } else if (Integer.valueOf(operationPage).equals(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_80.getId())) {
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.PROHIBITED_MEMBER_NOTES.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_80, MerchantLogTypeEnum.PROHIBITED_MEMBER_NOTES, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.PROHIBIT_BOOKLET_MEMBER_NOTES.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_80, MerchantLogTypeEnum.PROHIBIT_BOOKLET_MEMBER_NOTES, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.DISABLE_SEND_MSG_OPERATION.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_80, MerchantLogTypeEnum.DISABLE_SEND_MSG_OPERATION, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.DISABLE_BASK_ORDER_OPERATION.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_80, MerchantLogTypeEnum.DISABLE_BASK_ORDER_OPERATION, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
        } else if (Integer.valueOf(operationPage).equals(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_81.getId())) {
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.ADD_SUPER_MEMBER.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_81, MerchantLogTypeEnum.ADD_SUPER_MEMBER, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.EDIT_SUPER_MEMBER.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_81, MerchantLogTypeEnum.EDIT_SUPER_MEMBER, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
            if (Integer.valueOf(operationType).equals(MerchantLogTypeEnum.DEL_SUPER_MEMBER.getCode())) {
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_81, MerchantLogTypeEnum.DEL_SUPER_MEMBER, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantCode, merchantName, dataId, language, ip);
            }
        }
    }

    @Override
    public APIResponse executeMerchantCurrency() {
        long start = System.currentTimeMillis();
        try {
            //直营商户
            List<MerchantPO> merchantCurrencyList = merchantMapper.queryMerchantCurrency(0);
            if (CollectionUtils.isNotEmpty(merchantCurrencyList)) {
                Map<String, List<MerchantPO>> map = merchantCurrencyList.stream().collect(Collectors.groupingBy(MerchantPO::getMerchantCode));
                if (map != null) {
                    for (String key : map.keySet()) {
//                        List<String> currencyList = map.get(key).stream().map(MerchantPO::getCurrencyCode).collect(Collectors.toList());
                        Set<String> currencySet = map.get(key).stream().map(MerchantPO::getCurrencyCode).collect(Collectors.toSet());
                        log.info("直营商户:" + key + "多币种" + currencySet.toString());
                        MerchantPO merchantPO = merchantMapper.selectMerchantCurrency(key);
                        if (StringUtils.isNotEmpty(merchantPO.getCurrencyCode())) {
                            List<String> currency = Arrays.asList(merchantPO.getCurrencyCode());
                            List<String> resultList = Lists.newArrayList(currency);
                            for (String item : currencySet) {
                                if (!merchantPO.getCurrencyCode().contains(item)) {
                                    resultList.add(item);
                                }
                            }
                            merchantMapper.updateMerchantCurrency(StringUtils.join(resultList.toArray(), ","), key);
                        } else {
                            List<String> currency = Lists.newArrayList(currencySet);
                            merchantMapper.updateMerchantCurrency(StringUtils.join(currency.toArray(), ","), key);
                        }
                    }
                }
            }
            //渠道商户/二级商户
            List<MerchantPO> channelMerchantList = merchantMapper.queryChannelMerchant();
            if (CollectionUtils.isNotEmpty(channelMerchantList)) {
                for (MerchantPO po : channelMerchantList) {
                    //查询渠道商所属二级商户多币种
                    List<MerchantPO> secondMerchantCurrencyList = merchantMapper.querySecondMerchantCurrency(po.getId());
                    if (CollectionUtils.isNotEmpty(secondMerchantCurrencyList)) {
                        Map<String, List<MerchantPO>> map = secondMerchantCurrencyList.stream().collect(Collectors.groupingBy(MerchantPO::getMerchantCode));
                        if (map != null) {
                            for (String key : map.keySet()) {
//                                List<String> currencyList = map.get(key).stream().map(MerchantPO::getCurrencyCode).collect(Collectors.toList());
                                Set<String> currencySet = map.get(key).stream().map(MerchantPO::getCurrencyCode).collect(Collectors.toSet());
                                log.info("二级商户:" + key + "多币种" + currencySet.toString());
                                MerchantPO merchantPO = merchantMapper.selectMerchantCurrency(key);
                                if (StringUtils.isNotEmpty(merchantPO.getCurrencyCode())) {
                                    List<String> currency = Arrays.asList(merchantPO.getCurrencyCode());
                                    List<String> resultList = Lists.newArrayList(currency);
                                    for (String item : currencySet) {
                                        if (!merchantPO.getCurrencyCode().contains(item)) {
                                            resultList.add(item);
                                        }
                                    }
                                    merchantMapper.updateMerchantCurrency(StringUtils.join(resultList.toArray(), ","), key);
                                } else {
                                    List<String> currency = Lists.newArrayList(currencySet);
                                    merchantMapper.updateMerchantCurrency(StringUtils.join(currency.toArray(), ","), key);
                                }
                            }
                        }
                        //渠道商多币种
//                        List<String> currencyList = secondMerchantCurrencyList.stream().map(MerchantPO::getCurrencyCode).collect(Collectors.toList());
                        Set<String> currencySet = secondMerchantCurrencyList.stream().map(MerchantPO::getCurrencyCode).collect(Collectors.toSet());
                        log.info("渠道商户:" + po.getMerchantCode() + "多币种" + currencySet.toString());
                        MerchantPO merchantPO = merchantMapper.selectMerchantCurrency(po.getMerchantCode());
                        if (StringUtils.isNotEmpty(merchantPO.getCurrencyCode())) {
                            List<String> currency = Arrays.asList(merchantPO.getCurrencyCode());
                            List<String> resultList = Lists.newArrayList(currency);
                            for (String item : currencySet) {
                                if (!merchantPO.getCurrencyCode().contains(item)) {
                                    resultList.add(item);
                                }
                            }
                            merchantMapper.updateMerchantCurrency(StringUtils.join(resultList.toArray(), ","), po.getMerchantCode());
                        } else {
                            List<String> currency = Lists.newArrayList(currencySet);
                            merchantMapper.updateMerchantCurrency(StringUtils.join(currency.toArray(), ","), po.getMerchantCode());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("MerchantCurrencyTask 任务执行error:" + e);
        }
        log.info("MerchantCurrencyTask任务执行结束:" + (System.currentTimeMillis() - start));
        return APIResponse.returnSuccess();
    }

    @Override
    public APIResponse changeMerchantName(MultipartFile file, HttpServletRequest request) throws IOException {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 设置文件存储路径
        StringBuffer msg = new StringBuffer();
        InputStream fs = file.getInputStream();
        Workbook ws = null;
        if (fileName.endsWith("xlsx")) {
            ws = new XSSFWorkbook(fs);
        } else {
            ws = new HSSFWorkbook(fs);
        }
        Sheet sheet = ws.getSheetAt(0);
        log.info("changeMerchantName" + sheet.getLastRowNum());
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell cell1 = row.getCell(0);
            Cell cell3 = row.getCell(3);
            if (cell1 == null || cell3 == null) {
                msg.append("ROWS:" + i + 1).append("数据有误/\n");
            }
            cell1.setCellType(CellType.STRING);
            String merchantCode = cell1.getStringCellValue();
            String newMerchantName = cell3.getStringCellValue();
            int num0 = merchantNewsMapper.updateMerchantName(merchantCode, newMerchantName);
            int num = merchantLogMapper.updateMerchantName(merchantCode, newMerchantName);
            int num1 = merchantMapper.updateMerchantName(merchantCode, newMerchantName);
            ExecutorInstance.executorService.submit(() -> {
                int num2 = merchantReportClient.updateMerchantName(merchantCode,newMerchantName);
                log.info(num2 + "merchantReportClient updateMerchantName,更新商户成功");
            });

            msg.append("ROWS:" + i + ";merchantCode=" + merchantCode + ";merchantName:" + newMerchantName).append("/\n");
            log.info("changeMerchantName" + "ROWS:" + i + ";merchantCode=" + merchantCode + ";merchantName:" + newMerchantName);
        }
        try {
            /**
             *  添加系统日志
             * */
            String userId = request.getHeader("user-id");
            String username = request.getHeader("merchantName");
            String ip = IPUtils.getIpAddr(request);
            String language = request.getHeader("language");
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("uploadChangeMerchantName"));
            vo.getBeforeValues().add(null);
            vo.getAfterValues().add(fileName);
            merchantLogService.saveLog(MerchantLogPageEnum.UPLOAD_CHANGE_MERCHANT_NAME, MerchantLogTypeEnum.EDIT_INFO, vo,
                    MerchantLogConstants.MERCHANT_IN, userId, username, null, null, null, language, ip);
        } catch (Exception e) {
            log.error("changeMerchantNam添加系统日志", e);
        }

        return APIResponse.returnSuccess(msg.toString());

    }

    @Override
    public APIResponse changeMerchantDomain(DomainChangeDTO domainChangeDto) {
        //域名切换时,若分组ID不为空则按域名+分组去切换,若分组ID为空则直接用域名替换域名
        DomainChangeDTO dto = new DomainChangeDTO();
        BeanUtils.copyProperties(domainChangeDto, dto);
        dto.setUpdateUser(domainChangeDto.getUpdateUser());
        dto.setUpdateTime(System.currentTimeMillis());
        List<String> merchantCodeList = Lists.newArrayList();
        try {
            if (null != dto.getGroupId()) {
                merchantCodeList = merchantMapper.selectMerchantCode(dto.getGroupId());
            }
            merchantMapper.changeMerchantDomain(dto);
            String domainType;
            if (StringUtils.isNotEmpty(dto.getDomainType()) && "1".equals(dto.getDomainType())) {
                domainType = "PC";
            } else {
                domainType = "H5";
            }
            merchantMapper.updateMerchantDomain(dto.getNewDomain(), dto.getOldDomain(), domainType, merchantCodeList);
            String key = RedisConstants.MERCHANT_FAMILY + RedisConstants.MERCHANT_DOMAIN + domainType;
            RedisTemp.setObject(key, dto.getNewDomain(), RedisConstants.EXPIRE_TIME_FIVE_MIN);
            merchantApiClient.kickoutMerchants(merchantCodeList);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("changeMerchantDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse<Object> queryMerchantDomain(FrontendMerchantDomain merchantDomain) {
        Integer pageIndex = merchantDomain.getPageIndex();
        Integer pageSize = merchantDomain.getPageSize();
        pageSize = (pageSize == null || pageSize == 0) ? 100 : pageSize;
        pageIndex = pageIndex == null ? 1 : pageIndex;
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<FrontendMerchantDomain> pageInfo = new PageInfo<>(merchantMapper.queryMerchantDomain(merchantDomain));
        return APIResponse.returnSuccess(pageInfo);
    }

    @Override
    public APIResponse createFrontendMerchantGroup(FrontendMerchantGroup merchantGroup) {
        int count = merchantMapper.selectFrontendMerchantGroup(merchantGroup);
        if (count > 0) {
            return APIResponse.returnFail("商户组已存在!");
        }
        try {
            merchantMapper.createFrontendMerchantGroup(merchantGroup);
            //新增关联关系表数据
            if (CollectionUtils.isNotEmpty(merchantGroup.getMerchantCodeList())) {
                List<String> list = merchantGroup.getMerchantCodeList();
                List<FrontendMerchantRelation> relationList = Lists.newArrayList();
                list.forEach(item -> {
                    FrontendMerchantRelation relation = new FrontendMerchantRelation();
                    relation.setFrontendMerchantGroupId(merchantGroup.getGroupId());
                    relation.setMerchantCode(item);
                    relation.setCreateTime(System.currentTimeMillis());
                    relation.setCreateUser(merchantGroup.getCreateUser());
                    relation.setUpdateTime(System.currentTimeMillis());
                    relation.setUpdateUser(merchantGroup.getUpdateUser());
                    relationList.add(relation);
                });
                merchantMapper.batchSaveFrontendMerchantRelation(relationList);
            }
            return APIResponse.returnSuccess();
        }catch (Exception e){
            log.error("createFrontendMerchantGroup, exception:",e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse updateFrontendMerchantGroup(HttpServletRequest request, FrontendMerchantGroup merchantGroup) {
        if (merchantGroup.getGroupId() == null) {
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        FrontendMerchantGroup group = new FrontendMerchantGroup();
        BeanUtils.copyProperties(merchantGroup, group);
        group.setUpdateTime(System.currentTimeMillis());
        group.setUpdateUser(merchantGroup.getUpdateUser());
        try {
            int num = merchantMapper.updateFrontendMerchantGroup(group);
            //修改关联关系表数据
            if (CollectionUtils.isNotEmpty(merchantGroup.getMerchantCodeList())) {
                //删除原有关联关系数据
                merchantMapper.delFrontendMerchantRelation(merchantGroup.getGroupId());
                List<String> list = merchantGroup.getMerchantCodeList();
                List<FrontendMerchantRelation> relationList = Lists.newArrayList();
                list.forEach(item -> {
                    FrontendMerchantRelation relation = new FrontendMerchantRelation();
                    relation.setFrontendMerchantGroupId(merchantGroup.getGroupId());
                    relation.setMerchantCode(item);
                    relation.setCreateTime(System.currentTimeMillis());
                    relation.setCreateUser(merchantGroup.getCreateUser());
                    relation.setUpdateTime(System.currentTimeMillis());
                    relation.setUpdateUser(merchantGroup.getUpdateUser());
                    relationList.add(relation);
                });
                merchantMapper.batchSaveFrontendMerchantRelation(relationList);
                /**
                 *  添加系统日志
                 * */
                String username = merchantGroup.getUpdateUser();
                String ip = IPUtils.getIpAddr(request);
                String language = null != request.getHeader("language") ? request.getHeader("language") : Constant.LANGUAGE_CHINESE_SIMPLIFIED;
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("updateFrontendMerchantGroup"));
                vo.getBeforeValues().add(null);
                vo.getAfterValues().add(merchantGroup.getGroupName());
                vo.getAfterValues().add(merchantGroup.getGroupType());
                vo.getAfterValues().add(String.valueOf(merchantGroup.getTimes()));
                vo.getAfterValues().add(String.valueOf(merchantGroup.getStatus()));
                vo.getAfterValues().add(String.valueOf(merchantGroup.getTimeType()));
                vo.getAfterValues().add(String.valueOf(merchantGroup.getAlarmNum()));
                vo.getAfterValues().add(StringUtils.join(merchantGroup.getMerchantCodeList().toArray(), ","));
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_82, MerchantLogTypeEnum.EDIT_INFO, vo,
                        MerchantLogConstants.MERCHANT_IN, null, username, null, null, null, language, ip);
            }
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("createFrontendMerchantGroup, exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse queryFrontendMerchantGroup(FrontendMerchantGroupDomainPO groupDomainPo) {
        List<FrontendMerchantGroupDomainPO> list = merchantMapper.queryFrontendMerchantGroup(groupDomainPo);
        List<FrontendMerchantGroupDomainPO> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> groupSet = list.stream().map(FrontendMerchantGroupDomainPO::getGroupName).collect(Collectors.toSet());
            for (String item : groupSet) {
                FrontendMerchantGroupDomainPO po = new FrontendMerchantGroupDomainPO();
                po.setGroupName(item);
                result.add(po);
            }
            result.forEach(resultItem -> {
                Map<String, String> map = new HashMap<>();
                Set<String> merchantNameSet = new HashSet<>();
                Set<String> merchantCodeSet = new HashSet<>();
                list.forEach(item -> {
                    FrontendMerchantGroupDomainPO po = new FrontendMerchantGroupDomainPO();
                    if (resultItem.getGroupName().equalsIgnoreCase(item.getGroupName())) {
                        if (item.getDomainType().equals(1)) {
                            map.put("PC", item.getDomainName());
                        } else {
                            map.put("H5", item.getDomainName());
                        }
                        merchantNameSet.add(item.getMerchantName());
                        merchantCodeSet.add(item.getMerchantCode());
                        resultItem.setDomainMap(map);
                        resultItem.setMerchantNameSet(merchantNameSet);
                        resultItem.setMerchantCodeSet(merchantCodeSet);
                        resultItem.setTimes(item.getTimes());
                        resultItem.setTimeType(item.getTimeType());
                        resultItem.setAlarmNum(item.getAlarmNum());
                        resultItem.setStatus(item.getStatus());
                    }
                });
            });
        }
        return APIResponse.returnSuccess(result);
    }

    @Override
    public APIResponse createFrontendDomain(FrontendMerchantDomain merchantDomain) {
        FrontendMerchantDomain domain = new FrontendMerchantDomain();
        BeanUtils.copyProperties(merchantDomain, domain);
        int count = merchantMapper.selectFrontendDomain(domain);
        if (count > 0) {
            return APIResponse.returnFail("域名已存在!");
        }
        try {
            merchantMapper.createFrontendDomain(domain);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("createFrontendDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse delFrontendDomain(FrontendMerchantDomain merchantDomain) {
        FrontendMerchantDomain domain = new FrontendMerchantDomain();
        BeanUtils.copyProperties(merchantDomain, domain);
        domain.setUpdateTime(System.currentTimeMillis());
        domain.setUpdateUser(merchantDomain.getUpdateUser());
        try {
            merchantMapper.delFrontendDomain(domain);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("delFrontendDomain, exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response updateMerchantEvent(MerchantEventVO merchantEventVO) {
        try {
            String merchantEventJson = merchantMapper.selectMerchantEventByCode(merchantEventVO.getMerchantCode());
            Map<String, Object> enevtMap = new HashMap<>();
            String enevtJson = "";
            if (StringUtils.isNotEmpty(merchantEventJson)) {
                Map<String, Object> erchantEventMap = JsonUtils.jsonToMap(merchantEventJson);
                enevtMap.put("cornerEvent", merchantEventVO.getCornerEvent());
                enevtMap.put("penaltyEvent", merchantEventVO.getPenaltyEvent());
                enevtMap.put("eventSwitch", Integer.valueOf(erchantEventMap.get("eventSwitch").toString()));
                enevtJson = JsonUtils.objectToJson(enevtMap);
            }
            int updateNum = merchantMapper.updateMerchantEvent(enevtJson, merchantEventVO.getMerchantCode());
        /*    if (updateNum > 0) {
                merchantApiClient.updateMerchantUserCache(merchantEventVO.getMerchantCode());
            }*/
            ExecutorInstance.executorService.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("asyncUpdateMerchantCache.sleep=", e);
                }
                merchantApiClient.updateMerchantUserCache(merchantEventVO.getMerchantCode());
                log.info("updateMerchantUserCache,更新商户缓存成功");
            });
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("updateMerchantEvent, exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    @Override
    public Response updateMerchantEventSwitch(MerchantEventVO merchantEventVO) {
        try {
            String merchantEventJson = merchantMapper.selectMerchantEventByCode(merchantEventVO.getMerchantCode());
            Map<String, Object> enevtMap = new HashMap<>();
            String enevtJson = "";
            if (StringUtils.isNotEmpty(merchantEventJson)) {
                Map<String, Object> erchantEventMap = JsonUtils.jsonToMap(merchantEventJson);
                enevtMap.put("cornerEvent", erchantEventMap.containsKey("cornerEvent") ? Integer.valueOf(erchantEventMap.get(
                        "cornerEvent").toString()) : 0);
                enevtMap.put("penaltyEvent", erchantEventMap.containsKey("penaltyEvent") ?
                        Integer.valueOf(erchantEventMap.get("penaltyEvent").toString()) : 0);
                enevtMap.put("eventSwitch", merchantEventVO.getEventSwitch());
                enevtJson = JsonUtils.objectToJson(enevtMap);
            } else {
                enevtMap.put("cornerEvent", 0);
                enevtMap.put("penaltyEvent", 0);
                enevtMap.put("eventSwitch", merchantEventVO.getEventSwitch());
                enevtJson = JsonUtils.objectToJson(enevtMap);
            }
            int updateNum = merchantMapper.updateMerchantEvent(enevtJson, merchantEventVO.getMerchantCode());
            if (updateNum > 0) {
                log.info("merchantApiClient.updateMerchantUserCache RPC 更新用户缓存开始");
                merchantApiClient.updateMerchantUserCache(merchantEventVO.getMerchantCode());
            }
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("updateMerchantEvent, exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public MerchantEventVO getMerchantEventSwitch(MerchantEventVO merchantEventVO) {
        String merchantEventJson = merchantMapper.selectMerchantEventByCode(merchantEventVO.getMerchantCode());
        MerchantEventVO vo = new MerchantEventVO();
        if (StringUtils.isNotEmpty(merchantEventJson)) {
            Map<String, Object> erchantEventMap = JsonUtils.jsonToMap(merchantEventJson);
            vo.setMerchantCode(merchantEventVO.getMerchantCode());
          /*  vo.setGoalEvent(erchantEventMap.containsKey("goalEvent") ?
                    Integer.valueOf(erchantEventMap.get("goalEvent").toString()) : 0);*/
            vo.setCornerEvent(erchantEventMap.containsKey("cornerEvent") ? Integer.valueOf(erchantEventMap.get(
                    "cornerEvent").toString()) : 0);
            vo.setPenaltyEvent(erchantEventMap.containsKey("penaltyEvent") ? Integer.valueOf(erchantEventMap.get(
                    "penaltyEvent").toString()) : 0);
        } else {
            vo.setPenaltyEvent(0);
            vo.setCornerEvent(0);
        }
        return vo;
    }

    @Override
    public void exportMerchantKey(List<String> codeList) {
        //批量重新生成商户密钥
        String startTime = "1970-01-05";
        String endTime = "1970-01-15";
        for (String item : codeList){
            try {
                this.updateKey(item, null, startTime, endTime, null, null,"zs", null);
            }catch (Exception e){
                log.error("重新生成商户密钥失败!");
            }
        }
        List<MerchantPO> merchantPOList = merchantMapper.queryMerchantKey(codeList);
        List<MerchantPO> exportList = Lists.newArrayList();
        for (MerchantPO po : merchantPOList){
            MerchantPO merchantPO = new MerchantPO();
            BeanUtils.copyProperties(po, merchantPO);
            merchantPO.setMerchantKey(AESUtils.aesDecode(po.getMerchantKey()));
            exportList.add(merchantPO);
        }
        this.writeIntoCSVFile(exportList);
    }

    private void writeIntoCSVFile(List<MerchantPO> exportList){
        Date date = new Date();
        String str = DateFormatUtils.format(date, "yyMMddHHmm");
        String csvName = str + ".csv";
        try {
            byte[] bytes = exportTicketToFtp(exportList);
            File tempFile = new File(filePath + csvName);
            FileOutputStream fos = new FileOutputStream(tempFile);
            try {
                fos.write(bytes);
            } finally {
                try {
                    fos.close();
                } catch (IOException var8) {
                    log.error("CSV文件写入异常!", var8);
                }
            }
        }catch (Exception e){
            log.error("文件写入异常!", e);
        }
    }

    private byte[] exportTicketToFtp(List<MerchantPO> exportList){
        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(CollectionUtils.isEmpty(exportList) ? 0 : exportList.size());
        int i = 0;
        for (MerchantPO po : exportList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", po.getMerchantName());
            rowData.put("3", po.getMerchantCode());
            rowData.put("4", po.getMerchantKey());
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", "序号");
        header.put("2", "商户简称");
        header.put("3", "商户编码");
        header.put("4", "商户密钥");
        return CsvUtil.exportCSV(header, exportData);
    }

    @Override
    public APIResponse addMerchantKey(HttpServletRequest request, MerchantKeyVO merchantKeyVO) {
        //若传入的商户在表里面不存在需要insert数据
        try {
            MerchantKeyVO keyInfo = merchantMapper.queryMerchantKeyInfo(merchantKeyVO);
            MerchantKeyVO keyVO = merchantMapper.selectMerchantKey(merchantKeyVO);
            String merchantKey = CreateSecretKey.keyCreate();
            String userId = request.getHeader("user-id");
            int count;
            if(null == keyInfo){
                merchantKeyVO.setSecondMerchantKey(AESUtils.aesEncode(merchantKey));
                merchantKeyVO.setKeyStatus(Constant.INT_1);
                merchantKeyVO.setSecondStatus(Constant.INT_1);
                merchantKeyVO.setSecondEnableTime(System.currentTimeMillis());
                merchantKeyVO.setCreatedBy(userId);
                merchantKeyVO.setCreateTime(String.valueOf(System.currentTimeMillis()));
                merchantKeyVO.setUpdatedBy(userId);
                merchantKeyVO.setUpdateTime(String.valueOf(System.currentTimeMillis()));
                count = merchantMapper.addMerchantKey(merchantKeyVO);
            }else{
                if(StringUtils.isNotEmpty(keyVO.getMerchantKey()) && StringUtils.isNotEmpty(keyVO.getSecondMerchantKey())){
                    return APIResponse.returnFail("商户证书超过上限,无法新增!");
                }else {
                    merchantKeyVO.setSecondMerchantKey(AESUtils.aesEncode(merchantKey));
                    merchantKeyVO.setSecondStatus(Constant.INT_1);
                    merchantKeyVO.setSecondEnableTime(System.currentTimeMillis());
                    merchantKeyVO.setUpdatedBy(userId);
                    merchantKeyVO.setUpdateTime(String.valueOf(System.currentTimeMillis()));
                    count = merchantMapper.updateMerchantKey(merchantKeyVO);
                }
            }
            if(count > 0){
                //记录日志
                String language = request.getHeader("language");
                String username = loginUserService.getLoginUser(Integer.valueOf(userId));
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("key"));
                vo.getBeforeValues().add("***");
                vo.getAfterValues().add("***");
                merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER, MerchantLogTypeEnum.ADD_MERCHANT_KEY, vo,
                        MerchantLogConstants.MERCHANT_IN, userId, username, merchantKeyVO.getMerchantCode(), merchantKeyVO.getMerchantName(), merchantKeyVO.getId(), language, IPUtils.getIpAddr(request));
                //发送定时MQ消息
                String json = JSON.toJSONString(merchantKeyVO);
                producer.sendDelayed(json, topic,OPERATION_TYPE_ADD, merchantKeyVO.getMerchantCode());
                log.info("发送MQ消息时间，{}",System.currentTimeMillis());
                //异步清理商户缓存
                CompletableFuture.runAsync(()->{
                    merchantApiClient.kickoutMerchant(merchantKeyVO.getMerchantCode());
                });
            }
            return APIResponse.returnSuccess();
        }catch (Exception e){
            log.error("addMerchantKey, exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse enableMerchantKey(HttpServletRequest request, MerchantKeyVO merchantKeyVO) {
        try {
            int count;
            MerchantKeyVO keyVO = merchantMapper.selectMerchantKey(merchantKeyVO);
            if ((keyVO.getKeyStatus() == Constant.INT_0 && merchantKeyVO.getSecondStatus() == Constant.INT_0)
                    || keyVO.getSecondStatus() == Constant.INT_0 && merchantKeyVO.getKeyStatus() == Constant.INT_0){
                return APIResponse.returnFail("商户必须有一个证书有效,当前无法禁用!");
            }
            if (StringUtils.isNotEmpty(merchantKeyVO.getMerchantKey()) && null != merchantKeyVO.getKeyStatus()
                    && merchantKeyVO.getKeyStatus() == Constant.INT_1){
                merchantKeyVO.setEnableTime(System.currentTimeMillis());
            }
            if (StringUtils.isNotEmpty(merchantKeyVO.getSecondMerchantKey()) && null != merchantKeyVO.getSecondStatus()
                    && merchantKeyVO.getSecondStatus() == Constant.INT_1){
                merchantKeyVO.setSecondEnableTime((System.currentTimeMillis()));
            }
            merchantKeyVO.setUpdateTime(String.valueOf(System.currentTimeMillis()));
            merchantKeyVO.setUpdatedBy(request.getHeader("user-id"));
            count = merchantMapper.enableMerchantKey(merchantKeyVO);
            if(count > 0){
                //记录日志
                Integer userId = Integer.valueOf(request.getHeader("user-id"));
                String language = request.getHeader("language");
                String username = loginUserService.getLoginUser(userId);
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("key"));
                vo.getBeforeValues().add("***");
                vo.getAfterValues().add("***");
                merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_EDITKEY, MerchantLogTypeEnum.EDIT_INFO, vo,
                        MerchantLogConstants.MERCHANT_IN, userId.toString(), username, merchantKeyVO.getMerchantCode(), merchantKeyVO.getMerchantName(), merchantKeyVO.getId(), language, IPUtils.getIpAddr(request));
                //校验是否需要发送定时MQ消息
                if(StringUtils.isNotEmpty(merchantKeyVO.getMerchantKey()) && Constant.INT_1 == merchantKeyVO.getKeyStatus()){
                    String json = JSON.toJSONString(merchantKeyVO);
                    producer.sendDelayed(json, topic, OPERATION_TYPE_ENABLE, merchantKeyVO.getMerchantCode());
                    log.info("发送MQ消息时间，{}",System.currentTimeMillis());
                }
                if(StringUtils.isNotEmpty(merchantKeyVO.getSecondMerchantKey()) && Constant.INT_1 == merchantKeyVO.getSecondStatus()){
                    String json = JSON.toJSONString(merchantKeyVO);
                    producer.sendDelayed(json, topic, OPERATION_TYPE_ENABLE, merchantKeyVO.getMerchantCode());
                    log.info("发送MQ消息时间，{}",System.currentTimeMillis());
                }
                //异步清理商户缓存
                CompletableFuture.runAsync(()->{
                    merchantApiClient.kickoutMerchant(merchantKeyVO.getMerchantCode());
                });
            }
            return APIResponse.returnSuccess();
        }catch (Exception e){
            log.error("enableMerchantKey, exception:", e);
            return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse sendMongoMsg(MerchantKeyVO merchantKeyVO) {
        //发送告警消息前去重新查询证书的状态，若状态已发生变化则不发送消息
        MerchantKeyVO keyInfo = merchantMapper.queryMerchantKeyInfo(merchantKeyVO);
        if((StringUtils.isNotEmpty(merchantKeyVO.getMerchantKey()) && merchantKeyVO.getKeyStatus() == keyInfo.getKeyStatus())
                || (StringUtils.isNotEmpty(merchantKeyVO.getSecondMerchantKey()) && merchantKeyVO.getSecondStatus() == keyInfo.getSecondStatus())){
            MerchantPO merchantPO = merchantMapper.getMerchantInfo(merchantKeyVO.getMerchantCode());
            try {
                String str = "";
                if(StringUtils.isNotEmpty(merchantKeyVO.getMerchantKey())){
                    str = String.valueOf(Constant.INT_1);
                }else if(StringUtils.isNotEmpty(merchantKeyVO.getSecondMerchantKey())){
                    str = String.valueOf(Constant.INT_2);
                }
                MerchantNews merchantNewsPo = new MerchantNews();
                merchantNewsPo.setMerchantCode(merchantPO.getMerchantCode());
                merchantNewsPo.setMerchantName(merchantPO.getMerchantName());
                merchantNewsPo.setTitle(merchantPO.getMerchantName() + "的两个有效证书并行达30分钟");
                merchantNewsPo.setContext("【" + merchantPO.getMerchantName() + "&" + merchantPO.getMerchantCode() + "】两个有效证书并行已达30分钟，请点击此处禁用证书" + str);
                merchantNewsPo.setCreateTime(System.currentTimeMillis());
                merchantNewsPo.setSendTime(System.currentTimeMillis());
                merchantNewsService.save(merchantNewsPo);
            }catch (Exception e){
                log.error("执行禁用商户证书我的消息通知异常!", e);
                return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR);
            }
            //异步发送mango告警
            CompletableFuture.runAsync(()->{
                try {
                    String str = "";
                    if(StringUtils.isNotEmpty(merchantKeyVO.getMerchantKey())){
                        str = String.valueOf(Constant.INT_1);
                    }else if(StringUtils.isNotEmpty(merchantKeyVO.getSecondMerchantKey())){
                        str = String.valueOf(Constant.INT_2);
                    }
                    String text = String.format("环境："+ "【" + env + "】" + "\n商户：" +
                            "【" + merchantPO.getMerchantName()+ "&" + merchantPO.getMerchantCode() + "】" + "两个有效证书并行已达30分钟，请禁用证书" + str);
                    mongoService.send(text, targetName, userId, userToken);
                }catch (Exception e){
                    log.error("执行禁用商户证书芒果告警通知异常!", e);
                }

            });
        }
        return APIResponse.returnSuccess();
    }
}
