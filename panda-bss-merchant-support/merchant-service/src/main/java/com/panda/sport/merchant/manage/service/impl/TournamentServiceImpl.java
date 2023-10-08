package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.panda.sport.backup.mapper.SportMapper;
import com.panda.sport.bss.mapper.MerchantConfigMapper;
import com.panda.sport.merchant.common.bo.sport.SportFilterBO;
import com.panda.sport.merchant.common.bo.tournament.DataProviderBO;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.po.bss.PullDownParamPO;
import com.panda.sport.merchant.common.po.bss.PullDownResultPO;
import com.panda.sport.merchant.common.po.bss.SportFilterVo;
import com.panda.sport.merchant.common.utils.DateTimeUtils;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.TournamentVo;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import com.panda.sport.merchant.manage.service.TournamentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.service
 * @Description :  联赛下拉接口
 * @Date: 2020-11-06 13:10:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service
@Slf4j
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private LocalCacheService localCacheService;
    @Autowired
    private SportMapper sportMapper;
    @Autowired
    private MerchantConfigMapper merchantConfigMapper;

    @Autowired
    DataProviderBO dataProviderBO;

    @Autowired
    SportFilterBO sportFilterBO;

    /**
     * 获取赛事统计下拉列表信息
     *
     * @param vo       只需要startTime(必选)  endTime(必选)  sportId(可选)  三个参数
     * @param language
     * @return 对象list
     */
    @Override
    public List<PullDownResultPO> pullDownTournament(SportVO vo, String language) {
        PullDownParamPO param = getPullDownParamBySportVO(vo, language);
        return sportMapper.listTournament(param);
    }

    @Override

    public Response getSportList(String language) {
        try {
            List<Map<String, Object>> result = localCacheService.getSportList(language);

            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("getSportList:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response getSportListByFilter(String merchantCode) {
        try {
            List<SportFilterVo> result = sportMapper.getAllSportListByFilter(Constant.LANGUAGE_CHINESE_SIMPLIFIED);
            //增加球种
            List<SportFilterVo> sportList = sportFilterBO.getSportFilterList();
            List<SportFilterVo> finalSportList = new ArrayList<>();
            for (SportFilterVo sportFilterVo : sportList) {
                SportFilterVo finalSportFilterVo = new SportFilterVo();
                finalSportFilterVo.setId(sportFilterVo.getId());
                finalSportFilterVo.setNameCode(sportFilterVo.getNameCode());
                finalSportFilterVo.setName(sportFilterVo.getName());
                finalSportList.add(finalSportFilterVo);
            }
            for (SportFilterVo sportFilterVo : finalSportList
            ) {
                result.add(sportFilterVo);
            }
            MerchantConfig merchantConfig = merchantConfigMapper.getConfigByMerchantCode(merchantCode);
            if (merchantConfig != null && StringUtils.isNotEmpty(merchantConfig.getFilterSport())) {
                String[] ids = merchantConfig.getFilterSport().split(",");
                for (SportFilterVo vo : result) {
                    for (String id : ids) {
                        if (vo.getId().toString().equals(id)) {
                            vo.setTag(0);
                            break;
                        }
                    }
                }
            }
            return Response.returnSuccess(result);
        } catch (Exception e) {
            log.error("getSportList:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response getLocalCacheInfo() {
        return localCacheService.getLocalCacheInfo();
    }

    private PullDownParamPO getPullDownParamBySportVO(SportVO sportVO, String language) {
        PullDownParamPO paramPO = new PullDownParamPO();
        sportVO.setPageNum(1);
        sportVO.setPageSize(1000);
        paramPO.setPageNum((sportVO.getPageNum() - 1) * sportVO.getPageSize());
        paramPO.setPageSize(sportVO.getPageSize());
        Date startDate = DateTimeUtils.engStringToDate(sportVO.getStartTime() + " 00:00:00", DateUtils.DATE_YYYY_MM_DD_HH_MM_SS);
        Date endDate = DateTimeUtils.engStringToDate(sportVO.getEndTime() + " 23:59:59", DateUtils.DATE_YYYY_MM_DD_HH_MM_SS);
        paramPO.setStartTime(DateTimeUtils.dateToTimestamp(startDate));
        paramPO.setEndTime(DateTimeUtils.dateToTimestamp(endDate));
        paramPO.setSportId(sportVO.getSportId());
        paramPO.setLanguage(language);
        return paramPO;
    }

    /**
     * 查询联赛集合根据球类和等级排序
     *
     * @param args
     * @param language
     * @return
     */
    @Override
    public List<TournamentVo> findTournamentListBySportsLevel(String args, String language, Integer level, Long sportId) {
        /*if (level == null) {
            if (StringUtils.isNotEmpty(args)) {
                if (RegexUtils.isInteger(args)) {
                    level = Integer.parseInt(args);
                    args = null;
                } else if ("未定级".equals(args)) {
                    level = 0;
                }
            } else {
                args = null;
            }
            return sportMapper.queryTournament(level, args, args, language);
        }else {*/
        return sportMapper.queryTournamentBySportId(level, sportId, args, language);
        // }
    }

    /**
     * 查询联赛开关
     *
     * @param args
     * @param language     语言
     * @param level        等级
     * @param sportId      联赛ID
     * @param merchantCode 商户编号
     * @return
     * @Param dataSourceCode 联赛数据商
     */
    @Override
    public List<TournamentVo> findFilterTournamentListBySportsLevel(String args, String language, Integer level, Long sportId, String dataSourceCode, String merchantCode) {
        String finalDataSourceCode = dataProviderBO.getDataProviderMap().get(dataSourceCode);
        List<TournamentVo> list = sportMapper.queryFilterTournamentBySportId(level, sportId, args, language, finalDataSourceCode);
        if (CollectionUtil.isNotEmpty(list)) {
            list.stream().forEach(tournamentVo -> tournamentVo.setDataSourceCode(convertDataProvider(tournamentVo.getDataSourceCode())));
            MerchantConfig config = merchantConfigMapper.getConfigByMerchantCode(merchantCode);
            if (config == null || StringUtils.isEmpty(config.getFilterLeague())) {
                return list;
            }
            List<String> ids = Arrays.asList(config.getFilterLeague().split(","));
            for (TournamentVo vo : list) {
                if (ids.contains(vo.getId().toString())) {
                    vo.setTag(0);
                }
            }
            return list;
        }
        return null;
    }

    /**
     * 转换数据商
     *
     * @param providerCode
     * @return 转换后的数据商
     */
    private String convertDataProvider(String providerCode) {
        String nickCode = dataProviderBO.getReverseDataProviderMap().get(providerCode);
        return Strings.isBlank(nickCode) ? "暂无" : nickCode;
    }

}
