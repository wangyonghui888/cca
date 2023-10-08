package com.panda.sport.merchant.api.service.impl;

import com.google.common.collect.Sets;
import com.panda.sport.bss.mapper.OrderMapper;
import com.panda.sport.merchant.api.service.BetService;
import com.panda.sport.merchant.common.enums.SeriesTypesEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.api.BetApiVo;
import com.panda.sport.merchant.common.vo.api.BetDetailApiVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@RefreshScope
@Service("betService")
public class BetServiceImpl extends AbstractMerchantService implements BetService {

    @Autowired
    private OrderMapper orderMapper;

    @Value("${queryBetList.size:100}")
    private String queryBetSize;

    @Value("${queryPreBetOrderList.size:10}")
    private String queryPreBetOrderSize;

    @Value("${queryBetList.switch:on}")
    private String queryBetListSwitch;

    @Value("${queryPreBetOrderList.switch:on}")
    private String queryPreBetOrderListSwitch;

    private final static Set<Integer> HANDICAP_PLAY = Sets.newHashSet(20003, 20004, 20015, 20045, 39, 46, 19, 58, 52, 64, 4, 3, 69, 71, 121, 143,
            172, 113, 163, 176, 155, 154, 181, 185, 243, 249, 253, 268, 278, 128, 130, 306, 308, 324, 327, 334, 33, 232, 269, 270, 280, 294);

    private final static Set<Integer> ORDERBY_LIST = Sets.newHashSet(1, 2);

    /**
     * 查询单条投注记录
     * a验签解密b查询
     *
     * @param merchantCode
     * @param orderId
     * @param timestamp
     * @param signature
     * @return BetApiVo
     */
    @Override
    public APIResponse<BetApiVo> getBetDetail(HttpServletRequest request, String merchantCode, String orderId, Long timestamp, String signature) {
        try {
            String signStr = merchantCode + "&" + orderId + "&" + timestamp;
            APIResponse errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                log.error("getBetDetail:" + errorResult);
                return errorResult;
            }
            return APIResponse.returnSuccess(assemblyHandicap(orderMapper.getBetDetail(merchantCode, orderId)));
        } catch (Exception e) {
            log.error("BetController.getBetDetail!", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * @description:查询列表 a验签解密b查询
     * @Param: [request, userName, startTime, endTime, merchantCode, sportId, tournamentId, settleStatus, pageNum, pageSize, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<java.lang.Object>
     * @date: 2020/8/23 11:27
     */
    @Override
    public APIResponse<Object> queryBetList(HttpServletRequest request, String userName, Long startTime, Long endTime, String merchantCode, Integer sportId,
                                            Long tournamentId, Integer settleStatus, Integer pageNum, Integer pageSize, Long timestamp, String signature, Integer orderBy, String language) {
        try {
            if (queryBetListSwitch.equals("off")) {
                return APIResponse.returnFail(ApiResponseEnum.RATE_LIMIT_ERROR);
            }
            String signStr = merchantCode + "&" + startTime + "&" + endTime + "&" + timestamp;
            APIResponse errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                log.error("queryBetList:" + errorResult);
                return errorResult;
            }
            Long start7 = DateUtils.getStartTime();
            if (start7 > startTime) {
                startTime = start7;
            }
            int size = 100;
            if (StringUtils.isNotEmpty(queryBetSize) && NumberUtils.isDigits(queryBetSize)) {
                size = Integer.parseInt(queryBetSize);
            }
            if (pageSize == null || pageSize > size) {
                pageSize = size;
            }
            if (pageNum == null) {
                pageNum = 1;
            }
            if (null == orderBy || !ORDERBY_LIST.contains(orderBy)) {
                orderBy = 1;
            }
            orderBy = 2;
            Long cur = System.currentTimeMillis();
            log.info(signature + ",countBetList:param=" + ((pageNum - 1) * pageSize) + "," + pageSize);
            String key = userName + startTime + endTime + merchantCode + sportId + tournamentId + settleStatus + orderBy;
            String betCount = redisTemp.get(key);
            log.info(key + ",redis.countBetListBackUp:" + betCount);
            int count;
            if (StringUtils.isNotEmpty(betCount) && !betCount.equals("null")) {
                count = Integer.parseInt(betCount);
            } else {
                count = orderMapper.countBetList(userName, startTime, endTime, merchantCode, sportId, tournamentId, settleStatus, orderBy);
                if (endTime <= cur) {
                    log.info("queryBetList.endTime:" + endTime + ",cur=" + cur);
                    redisTemp.setWithExpireTime(key, Integer.toString(count), 3600);
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", count);
            if (count > 0) {
                List<BetApiVo> betList;
                if (StringUtils.isNotEmpty(language)) {
                    betList = orderMapper.queryBetListBackUpByLg(userName, startTime, endTime, merchantCode, sportId, tournamentId,
                            settleStatus, (pageNum - 1) * pageSize, pageSize, orderBy, language);

                    // seriesValue中英文翻译
                    betList.forEach(bet->{
                        if (bet.getSeriesType() != null){
                            SeriesTypesEnum scoreEnumByKey = SeriesTypesEnum.getSeriesTypesEnumByCode(bet.getSeriesType());
                            if (scoreEnumByKey != null) {
                                bet.setSeriesValue(language.equalsIgnoreCase("en") ? scoreEnumByKey.getEnDesc() : scoreEnumByKey.getDescribe());
                            }
                        }
                    });
                } else {
                    betList = orderMapper.queryBetList(userName, startTime, endTime, merchantCode, sportId, tournamentId,
                            settleStatus, (pageNum - 1) * pageSize, pageSize, orderBy);
                }
                for (BetApiVo vo : betList) {
                    assemblyHandicap(vo);
                }
                result.put("list", betList);
            }
            result.put("pageSize", pageSize);
            result.put("pageNum", pageNum);
            return APIResponse.returnSuccess(result);
        } catch (Exception e) {
            log.error("BetController.queryBetList,exception!", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    private BetApiVo assemblyHandicap(BetApiVo bet) {
        try {
            if (bet == null) {
                return bet;
            }
            List<BetDetailApiVo> betDetailApiVoList = bet.getDetailList();
            for (BetDetailApiVo betDetailApiVo : betDetailApiVoList) {
                String marketValue = betDetailApiVo.getMarketValue();
                Integer playId = betDetailApiVo.getPlayId();
                String playName = betDetailApiVo.getPlayName();
                String playOptionName = betDetailApiVo.getPlayOptionName();
                String playOptions = betDetailApiVo.getPlayOptions();
                if (HANDICAP_PLAY.contains(playId) || (StringUtils.isNotEmpty(playName) && playName.contains("让"))) {
                    marketValue = StringUtils.isEmpty(marketValue) ? playOptionName : marketValue;
                    if ("1".equals(playOptions) && !marketValue.equals("0")) {
                        marketValue = !marketValue.contains("-") ? "+" + marketValue : marketValue;
                    } else if ("2".equals(playOptions) && !marketValue.equals("0")) {
                        marketValue = !marketValue.contains("-") ? "-" + marketValue : marketValue.replace("-", "+");
                    } else if ("X".equalsIgnoreCase(playOptions)) {
                        marketValue = !marketValue.contains("-") ? "+" + marketValue : marketValue;
                    }
                }
                if (StringUtils.isNotEmpty(marketValue)) {
                    BigDecimal handicap = null;
                    if (marketValue.contains("/")) {
                        double a = Double.parseDouble(marketValue.split("/")[0]);
                        double b = Double.parseDouble(marketValue.split("/")[1]);
                        double newValue = (Math.abs(a) + Math.abs(b)) / 2;
                        handicap = (a < 0 || marketValue.startsWith("-")) ? BigDecimal.valueOf((-1) * newValue) : BigDecimal.valueOf(newValue);
                    } else if (marketValue.endsWith("+")) {
                        handicap = BigDecimal.valueOf(Double.parseDouble(marketValue.replaceAll("\\+", "")));
                    } else if (NumberUtils.isParsable(marketValue)) {
                        handicap = BigDecimal.valueOf(Double.parseDouble(marketValue));
                    }
                    betDetailApiVo.setHandicap(handicap);
                }
            }
        } catch (Exception e) {
            log.error("组装盘口值异常!" + bet.getOrderNo(), e);
        }
        return bet;
    }

    /*@Override
    public APIResponse<Object> queryPreBetOrderList(HttpServletRequest request, Long startTime, Long endTime, String merchantCode,
                                                    Integer pageNum, Integer pageSize, String language, Long timestamp, String signature) {
        if(queryPreBetOrderListSwitch.equalsIgnoreCase("off")){
            return APIResponse.returnFail(ApiResponseEnum.RATE_LIMIT_ERROR);
        }
        APIResponse errorResult = checkParam(request, timestamp, merchantCode, merchantCode + "&" + startTime + "&" + endTime + "&" + timestamp, signature);
        if (errorResult != null && !errorResult.getStatus()) {
            log.error("queryPreBetOrderList:" + errorResult);
            return errorResult;
        }
        //限制最大查询时间范围7天
        Long dayOfSeven = DateUtils.getStartTime();
        if (DateUtils.getStartTime() > startTime) {
            startTime = dayOfSeven;
        }
        int size = 50;
        if (StringUtils.isNotEmpty(queryPreBetOrderSize) && NumberUtils.isDigits(queryPreBetOrderSize)) {
            size = Integer.parseInt(queryPreBetOrderSize);
        }
        if (pageSize == null || pageSize > size) {
            pageSize = size;
        }
        if (pageNum == null) {
            pageNum = 1;
        }

        String key = startTime + endTime + merchantCode;
        String preBetCount = redisTemp.get(key);
        log.info(key + ",redis.countPreBetOrderList:" + preBetCount);
        int betCount;
        Map<String, Object> paramMap = getParamMap(startTime, endTime, pageSize, pageNum, merchantCode);
        if (StringUtils.isNotEmpty(preBetCount) && !preBetCount.equals("null")) {
            betCount = Integer.parseInt(preBetCount);
        } else {
            StatisticsVO vo = orderMapper.countPreBetOrderSize(paramMap);
            betCount= vo.getTotalOrder();
            Long cur = System.currentTimeMillis();
            if (endTime <= cur) {
                log.info("queryBetList.endTime:" + endTime + ",cur=" + cur);
                redisTemp.setWithExpireTime(key, Integer.toString(betCount), 3600);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", betCount);
        if (betCount > 0) {
            List<BetApiVo> betList;
            List<PreBetOrderVo> preBetList = Lists.newArrayList();
            if (StringUtils.isNotEmpty(language)) {
                betList = orderMapper.getPreBetOrderList(paramMap);
                // seriesValue中英文翻译
                betList.forEach(bet->{
                    if (bet.getSeriesType() != null){
                        SeriesTypesEnum scoreEnumByKey = SeriesTypesEnum.getSeriesTypesEnumByCode(bet.getSeriesType());
                        if (scoreEnumByKey != null) {
                            bet.setSeriesValue(language.equalsIgnoreCase("en") ? scoreEnumByKey.getEnDesc() : scoreEnumByKey.getDescribe());
                        }
                    }
                });
            } else {
                betList = orderMapper.getPreBetOrderList(paramMap);
            }
            for (BetApiVo vo : betList) {
                assemblyHandicap(vo);
            }
            for (BetApiVo vo : betList){
                PreBetOrderVo preBetOrderVo = new PreBetOrderVo();
                BeanCopierUtils.copyProperties(vo, preBetOrderVo);
                preBetList.add(preBetOrderVo);
            }
            result.put("list", preBetList);
        }
        result.put("pageSize", pageSize);
        result.put("pageNum", pageNum);
        return APIResponse.returnSuccess(result);
    }*/

    /*public Map<String, Object> getParamMap(Long startTime, Long endTime, Integer pageSize, Integer pageNum, String merchantCode) {
        // 限制查询时间范围
        Map<String, Object> paramMap = new HashMap<>();
        if (startTime != null) {
            paramMap.put("startTime", startTime);
        }
        if (endTime != null) {
            paramMap.put("endTime", endTime);
        }
        // 组合分页参数
        int page = pageNum == null ? 1 : pageNum;
        int size = pageSize == null || pageSize > 1000 ? 500 : pageSize;
        Integer startRecord = page == 1 ? 0 : size * (page - 1);
        paramMap.put("startRecord", startRecord);
        paramMap.put("endRecord", size);
        paramMap.put("merchantCode", merchantCode);
        return paramMap;
    }*/

}
