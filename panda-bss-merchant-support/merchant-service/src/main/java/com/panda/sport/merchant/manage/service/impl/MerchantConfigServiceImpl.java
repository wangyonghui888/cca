package com.panda.sport.merchant.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.backup.mapper.SportMapper;
import com.panda.sport.bss.mapper.MerchantConfigMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.SystemSwitchMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.utils.MerchantUtil;
import com.panda.sport.merchant.common.vo.MerchantConfigReqVO;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.TournamentVo;
import com.panda.sport.merchant.common.vo.merchant.MerchantConfigEditReqVO;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.service.impl
 * @Description :  商户配置服务类
 * @Date: 2021-02-04 10:44:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service
@Slf4j
public class MerchantConfigServiceImpl extends ServiceImpl<MerchantConfigMapper, MerchantConfig> {

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private MerchantConfigMapper merchantConfigMapper;

    @Autowired
    protected MerchantMapper merchantMapper;

    @Autowired
    private BackupTUserMapper tUserMapper;


    @Autowired
    private SportMapper sportMapper;

    @Autowired
    private SystemSwitchMapper systemSwitchMapper;

    @Autowired
    private LocalCacheService localCacheService;

    /**
     * 足球盘口开关
     */
    private final static String SOCCER_SWITCH = "1";

    /**
     * 蓝球盘口开关
     */
    private final static String BASKETBALL_SWITCH = "2";

    /**
     * 查询商户配置
     * @return
     */
    public MerchantConfig findByMerchantCode(String merchantCode){
        MerchantConfig merchantConfig = merchantConfigMapper.queryByMerchantCode(merchantCode);
        if (merchantConfig == null){
            merchantConfig = new MerchantConfig();
            // 设置默认值
            merchantConfig.setMinSeriesNum(CommonDefaultValue.SeriesNumSetting.MIN);
            merchantConfig.setMaxSeriesNum(CommonDefaultValue.SeriesNumSetting.MAX);
        }
        return merchantConfig;
    }

    /**
     * 更新图片url
     * @param tag
     * @param logoUrl
     */
    public void updateLogoUrl(String merchantCode,Integer tag,String logoUrl){
        MerchantConfig config = merchantConfigMapper.queryByMerchantCode(merchantCode);
        config = getMerchantConfig(merchantCode, config);
        switch (tag){
            case 1:
                //深色
                config.setDarkLogoUrl(logoUrl);
                break;
            case 2:
                //白色
                config.setWhiteLogoUrl(logoUrl);
                break;
            case 3:
                //pc
                config.setPcLogoUrl(logoUrl);
                break;
            case 4:
                //兼容
                config.setCompatLogoUrl(logoUrl);
                break;
            case 5:
                //首页
                config.setLoadLogoUrl(logoUrl);
                break;
            case 6:
                //联赛
                config.setLeagueLogoUrl(logoUrl);
                break;
            case 7:
                //banner
                config.setBannerUrl(logoUrl);
                break;
            case 8:
                config.setVideoLogoUrl(logoUrl);
                break;
            default:
                throw new RuntimeException("参数异常！");
        }
        merchantConfigMapper.update(config);
    }

    /**
     * 保存商户配置
     * @param merchantCode
     * @param config
     * @return
     */
    private MerchantConfig getMerchantConfig(String merchantCode, MerchantConfig config) {
        if (config == null){
            config = new MerchantConfig();
            config.setMerchantCode(merchantCode);
            merchantConfigMapper.insert(config);
            config = merchantConfigMapper.queryByMerchantCode(merchantCode);
        }
        return config;
    }

    /**
     * 保存或者更新数据
     * @param merchantConfig
     * @param userName 修改人
     */
    @Transactional
    public void updateConfig(MerchantConfigEditReqVO merchantConfig, String userId, String userName, String language,String ip){

        // 获取商户配置
        MerchantConfig config = merchantConfigMapper.queryByMerchantCode(merchantConfig.getMerchantCode());
        String merchantName = localCacheService.getMerchantCode(merchantConfig.getMerchantCode());

        Date localDateTime = new Date();
        if (config == null){
            config = new MerchantConfig();
            BeanUtils.copyProperties(merchantConfig,config);
            merchantConfig.setId(null);
            config.setCreateTime(localDateTime);
            config.setUpdateTime(localDateTime);
            config.setCreateUser(userName);
            config.setUpdateUser(userName);
            merchantConfigMapper.insert(config);
        } else {

            if (merchantConfig.getBookBet() != null && !config.getBookBet().equals(merchantConfig.getBookBet())){
                //  预约投注开关
                MerchantLogFiledVO vo = new MerchantLogFiledVO();

                vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("bookBetSwitch"));

                // 获取设置前数据
                vo.getBeforeValues().add(SwitchEnum.getInstance(config.getBookBet()).getLabel());

                // 获取设置后数据
                vo.getAfterValues().add(SwitchEnum.getInstance(merchantConfig.getBookBet()).getLabel());

                // 保存变更日志
                merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.TO_ORDER.getCode(),  MerchantLogTypeEnum.FUNCTION_SWITCH.getRemark()
                        , MerchantUtil.getModeInfo(merchantConfig.getAgentLevel(),language),
                        MerchantLogTypeEnum.TO_ORDER.getPageCode().get(0), config.getMerchantCode(), merchantName, config.getId().toString(),
                        vo.getFieldName(), vo.getBeforeValues(), vo.getAfterValues(),userName,userId,ip);
                // 打印日志
                log.info("后台用户{}修改商户{}, 预约投注开关{} 更新为{}", userId, merchantConfig.getId(), merchantConfig.getBookBet(), config.getBookBet());
            } else if (merchantConfig.getVideoSwitch() != null && !config.getVideoSwitch().equals(merchantConfig.getVideoSwitch())) {
                //视频流量管控开关
                MerchantLogFiledVO filedVO = new MerchantLogFiledVO();

                filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("videoSwitch"));

                // 获取设置前数据
                filedVO.getBeforeValues().add(SwitchEnum.getInstance(config.getVideoSwitch()).getLabel());

                // 获取设置后数据
                filedVO.getAfterValues().add(SwitchEnum.getInstance(merchantConfig.getVideoSwitch()).getLabel());

               // 保存变更日志
                merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.VIDEO_TRAFFIC.getCode(),  MerchantLogTypeEnum.FUNCTION_SWITCH.getRemark()
                        , MerchantUtil.getModeInfo(merchantConfig.getAgentLevel(),language) + "-" +  (Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language)? MerchantLogTypeEnum.VIDEO_TRAFFIC.getRemark():MerchantLogTypeEnum.VIDEO_TRAFFIC.getRemarkEn()),
                        MerchantLogTypeEnum.VIDEO_TRAFFIC.getPageCode().get(0), config.getMerchantCode(), merchantName, config.getId().toString(),
                        filedVO.getFieldName(), filedVO.getBeforeValues(), filedVO.getAfterValues(),userName,userId,ip);
                // 打印日志
                log.info("后台用户{}修改商户{}, 预约投注开关{} 更新为{}", userId, merchantConfig.getId(), merchantConfig.getVideoSwitch(), config.getVideoSwitch());
            } else{
                    MerchantUtil<MerchantConfigEditReqVO> fieldList = new MerchantUtil<MerchantConfigEditReqVO>();
                    MerchantLogFiledVO vo1 = fieldList.compareObject(config, merchantConfig, MerchantUtil.filterFieldNames, MerchantUtil.FIELD_MAPPING);
                    merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.CHAT_COMPREHENSIVE.getCode(), MerchantLogTypeEnum.EDIT_INFO.getRemark()
                            , MerchantUtil.getModeInfo(merchantConfig.getAgentLevel(), language) + "-" + (Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantLogTypeEnum.CHAT_COMPREHENSIVE.getRemark() : MerchantLogTypeEnum.CHAT_COMPREHENSIVE.getRemarkEn()),
                            MerchantLogTypeEnum.CHAT_COMPREHENSIVE.getPageCode().get(0), config.getMerchantCode(), merchantName, config.getId().toString(),
                            vo1.getFieldName(), vo1.getBeforeValues(), vo1.getAfterValues(), userName, userId, ip);
            }

            // 排除id字段不复制
            BeanUtils.copyProperties(merchantConfig,config, "id");
            config.setUpdateTime(localDateTime);
            config.setUpdateUser(userName);
            merchantConfigMapper.update(config);
        }


    }


    /**
     * 保存或者更新数据
     * @param userId
     * @param userName
     * @param language
     */
    public void updateConfigFilter(@Valid MerchantConfigReqVO reqVO, String userId, String userName, String language, String ip ){

            // 获取商户配置
            MerchantConfig config = merchantConfigMapper.queryByMerchantCode(reqVO.getMerchantCode());

            // 保存商户配置
            config = getMerchantConfig(reqVO.getMerchantCode(), config);
            String merchantName = localCacheService.getMerchantCode(reqVO.getMerchantCode());

            // 获取赛种列表
            List<SportFilterVo> sportList = sportMapper.getAllSportListByFilter(Constant.LANGUAGE_CHINESE_SIMPLIFIED);
            Map<Long, SportFilterVo> sportMap = sportList.stream().collect(Collectors.toMap(SportFilterVo::getId, a -> a, (k1, k2) -> k1));
            // 球类开关
            if (reqVO.getType().equals(Constant.MATCH_BALL)) {

                // 更新后的赛种
                String filterString = "";
                // 赛种空判断
                    // 此处1代表关  过滤的联赛
                if (StringUtils.isEmpty(config.getFilterSport())) {
                    if (reqVO.getTag() == 1) {
                        config.setFilterSport(reqVO.getIdList().stream().map(Object::toString).collect(Collectors.joining(",")));
                    }
                }else {
                    if (reqVO.getTag() == 1) {
                        filterString = addByList(reqVO.getIdList(), config.getFilterSport());
                        config.setFilterSport(filterString);
                    } else {
                        // 移除数据
                        filterString = removeByList(reqVO.getIdList(), config.getFilterSport());
                        config.setFilterSport(filterString);
                    }
                }
                MerchantConfig finalConfig1 = config;
                String finalFilterString = filterString;


                if (CollectionUtils.isEmpty(sportList)){
                    log.error("赛种列表为空");
                }


                reqVO.getIdList().forEach(id ->{
                    // 增加球赛操作日志
                    MerchantLogFiledVO logFileVo = new MerchantLogFiledVO();

                    // 获取设置前数据
                    String value = sportMap.get(id) == null ? "" :sportMap.get(id).getName();

                    logFileVo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("ballSwitch")+"-"+value);

                    logFileVo.getBeforeValues().add((reqVO.getTag() == 1 ? "开" : "关"));

                    // 获取设置后数据
                    logFileVo.getAfterValues().add((reqVO.getTag() == 1 ? "关" : "开"));

                    // 保存变更日志
                    merchantLogService.saveLogNew(MerchantUtil.getModeInfo(reqVO.getAgentLevel(),language)+"-商户球种设置", MerchantLogTypeEnum.FUNCTION_SWITCH, logFileVo,
                            MerchantLogConstants.MERCHANT_IN, userId, userName, reqVO.getMerchantCode(), merchantName, id.toString()
                            , language, ip);
                    log.info("后台用户{}修改商户{},FilterSport字段{}更新为{}", userId, id, finalConfig1.getFilterSport(), finalFilterString);

                });
            } else {
                // 联赛
                if (StringUtils.isEmpty(config.getFilterLeague())) {
                    if (reqVO.getTag() == 1) {
                        config.setFilterLeague(reqVO.getIdList().stream().map(Object::toString).collect(Collectors.joining(",")));
                    }
                }else {
                    if (reqVO.getTag() == Constant.SWITCH_ON) {
                        config.setFilterLeague(addByList(reqVO.getIdList(), config.getFilterLeague()));
                    } else {
                        config.setFilterLeague(removeByList(reqVO.getIdList(), config.getFilterLeague()));
                    }
                }
                MerchantConfig finalConfig = config;
                reqVO.getIdList().forEach(id->{
                    // 增加球赛操作日志
                    MerchantLogFiledVO logFileVo = new MerchantLogFiledVO();
                    // 获取联赛相关信息
                    TournamentVo tournamentVo = sportMapper.queryFilterTournamentById(id, language);
                    // 获取设置前数据
                    String value = sportMap.get(tournamentVo.getSportId()) == null ? "" :sportMap.get(tournamentVo.getSportId()).getName();

                    logFileVo.getFieldName().add(value+"-"+MerchantFieldUtil.FIELD_MAPPING.get("matchSwitch") +"-"+tournamentVo.getName());


                    logFileVo.getBeforeValues().add((reqVO.getTag() == 1 ? "开" : "关"));

                    // 获取设置后数据
                    logFileVo.getAfterValues().add((reqVO.getTag() == 1 ? "关" : "开"));
                    // 保存变更日志
                    merchantLogService.saveLogNew(MerchantUtil.getModeInfo(reqVO.getAgentLevel(),language)+"-商户球种设置", MerchantLogTypeEnum.FUNCTION_SWITCH, logFileVo,
                            MerchantLogConstants.MERCHANT_IN, userId, userName, reqVO.getMerchantCode(), merchantName, id.toString()
                            , language, ip);
                    log.info("后台用户{}修改商户{},FilterLeague字段{}更新为{}", userId, id, finalConfig.getFilterLeague(), language);
                });

            }

        // 批量更新
//        updateById(config);
        merchantConfigMapper.update(config);
    }

    private String addByList(List<Long> idList, String filterSport) {

        List<String> ids = new ArrayList<>(Arrays.asList(filterSport.split(",")));

        ids.addAll(idList.stream().map(Objects::toString).collect(Collectors.toList()));

        // 去重
        return ids.stream().distinct().collect(Collectors.joining(","));
    }

    /**
     *  移除数据
     * @return
     */
    private String removeByList(List<Long> idList, String filterParam) {

        // 分割字符串
        List<String> ids = new ArrayList<>(Arrays.asList(filterParam.split(",")));

        ids.removeAll(idList.stream().map(Objects::toString).collect(Collectors.toList()));

        // 去重
        return ids.stream().distinct().collect(Collectors.joining(","));
    }


    public List<MerchantCodeConfig> queryCodeConfigList() {
        return  merchantConfigMapper.queryCodeConfigList();
    }

    public PageInfo<MerchantCodeConfigLog>queryCodeConfigLogList(String merchantCode, Integer pageSize, Integer pageNum) {
        Integer pageIndex = pageNum;
        pageSize = (pageSize == null || pageSize == 0) ? 20 : pageSize;
        pageIndex = pageIndex == null ? 1 : pageIndex;
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<MerchantCodeConfigLog> pageInfo = new PageInfo<>(merchantConfigMapper.queryCodeConfigLogList(merchantCode));
        return  pageInfo;
    }

    public int updateCodeConfig(List<MerchantCodeConfig> list, HttpServletRequest request) {
        int num =0;
        for(MerchantCodeConfig config:list){
            MerchantCodeConfig merchantCodeConfig = merchantConfigMapper.queryCodeConfigById(config);
            num+= merchantConfigMapper.updateCodeConfig(config);

            MerchantUtil filedUtil = new MerchantUtil<MerchantCodeConfig>();
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            MerchantLogFiledVO filedVO = filedUtil.compareObject(merchantCodeConfig, config,MerchantUtil.filterField,MerchantUtil.FIELD_MERCHNTLEVEL_MAPPING);

                    merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_LEVEL_SET, MerchantLogTypeEnum.EDIT_INFO, filedVO,
                            MerchantLogConstants.MERCHANT_IN, request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"),request.getHeader("language") , IPUtils.getIpAddr(request));
        }
        return  num;
    }

    public int insertMerchantCodeConfigLog(MerchantCodeConfigLog config) {
        return  merchantConfigMapper.insertMerchantCodeConfigLog(config);
    }

    public void updateMarketSwitch(MerchantConfigEditReqVO merchantConfig, String userName){
        if(merchantConfig.getBookMarketSwitch() != null || merchantConfig.getBookMarketSwitchBasketball() != null){
            MerchantConfig config = merchantConfigMapper.queryByMerchantCode(merchantConfig.getMerchantCode());
            Date localDateTime = new Date();
            String str = "{" + SOCCER_SWITCH + ":" + merchantConfig.getBookMarketSwitch() + ","
                    + BASKETBALL_SWITCH + ":" + merchantConfig.getBookMarketSwitchBasketball() + "}";
            config.setCoilSwitch(str);
            config.setUpdateUser(userName);
            config.setUpdateTime(localDateTime);
            merchantConfigMapper.update(config);
        }
    }

    public MerchantConfig queryMarketSwitch(String merchantCode) {
        MerchantConfig config = merchantConfigMapper.queryByMerchantCode(merchantCode);
        if (null != config) {
            try {
                String coilSwitch = config.getCoilSwitch();
                List<String> switchList = Arrays.asList(coilSwitch.substring(1, coilSwitch.length() - 1).split(","));
                for (String item : switchList) {
                    String[] str = item.split(":");
                    if (str[0].equalsIgnoreCase("1")) {
                        config.setBookMarketSwitch(str[1]);
                    } else if (str[0].equalsIgnoreCase("2")) {
                        config.setBookMarketSwitchBasketball(Integer.valueOf(str[1]));
                    }
                }
            } catch (Exception e) {
                log.error("queryMarketSwitch;", e);
                config.setBookMarketSwitch("1");
                config.setBookMarketSwitchBasketball(1);
            }
        }
        return config;
    }

}
