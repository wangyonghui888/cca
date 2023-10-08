package com.panda.sport.order.service.expot;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.backup.mapper.BackupOrderMixMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.BizTypeEnum;
import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  注单账变导出实现类
 * @Date: 2020-12-11 13:49:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Service("abstractOrderAccountExportServiceImpl")
public abstract class AbstractOrderAccountExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    public BackupOrderMixMapper orderMixMapper;

    public void execute(MerchantFile merchantFile) {
        InputStream inputStream = null;
        try {
            if (super.checkTask(merchantFile.getId())) {
                log.info("当前任务被删除！");
                return;
            }
            long startTime = System.currentTimeMillis();
            log.info("大文件开始导出注单账变 param = {}", merchantFile.getExportParam());
            BetOrderVO param = JSON.parseObject(merchantFile.getExportParam(), BetOrderVO.class);
            int size = 1000;
            List<Map<String, Object>> resultMap = new ArrayList<>();
            int i = 0;
            int start = 0;
            super.updateFileStart(merchantFile.getId());
            super.updateRate(merchantFile.getId(), 40L);
            List<Map<String, Object>> result = null;
            do {
                param.setSize(size);
                param.setStart(start);
                result = this.abstractQueryUserOrderAccountHistoryList(param);
                start = start + size;
                i++;
                if (result != null && result.size() > 0) {
                    resultMap.addAll(result);
                }
                log.info("注单导出执行中 start = {} id= {}", start, merchantFile.getId());
            } while (result != null && result.size() > 0);
            super.updateRate(merchantFile.getId(), 80L);
            if (CollectionUtils.isEmpty(resultMap)) {
                throw new Exception("未查询到数据");
            }
            inputStream = new ByteArrayInputStream(exportOrderAccountHistoryToCsv(resultMap, param.getLanguage()));

            super.uploadFile(merchantFile, inputStream);

            //更新状态
            log.info("结束导出注单 param = {} 耗时 {}", merchantFile.getExportParam(), System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("订单账变记录导出异常！", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("流关闭异常！");
                }
            }
        }
    }

    protected byte[] exportOrderAccountHistoryToCsv(List<Map<String, Object>> mapList, String language) {
        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(mapList.size());
        //序号
        int index = 0;
        for (Map<String, Object> vo : mapList) {
            index++;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", "\t" + index + "\t");
            rowData.put("2", "\t" + DateUtil.format(new Date(Long.parseLong(String.valueOf(vo.get("createTime")))), "yyyy-MM-dd HH:mm:ss") + "\t");
            rowData.put("3", "\t" + vo.get("username") + "\t");
            rowData.put("4", "\t" + vo.get("uid") + "\t");
            rowData.put("5", "\t" + vo.get("merchantName") + "\t");
            rowData.put("6", "\t" + (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ?
                    BizTypeEnum.getDescByCode(Integer.valueOf(String.valueOf(vo.get("bizType")))) :
                    BizTypeEnum.getEnByCode(Integer.valueOf(String.valueOf(vo.get("bizType")))))
                    + "\t");
            rowData.put("7", "\t" + (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ?
                    vo.get("remark") : BizTypeEnum.getEnByCode(Integer.valueOf(String.valueOf(vo.get("bizType"))))) + "\t");
            rowData.put("8", ("0".equals(String.valueOf(vo.get("changeType"))) ? "+" : "-") + vo.get("changeAmount"));
            rowData.put("9", vo.get("beforeTransfer"));
            rowData.put("10", vo.get("afterTransfer"));
            rowData.put("11", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "成功" : "Done");
            rowData.put("12", "\t" + ("null".equals(String.valueOf(vo.get("orderNo"))) ? "" : String.valueOf(vo.get("orderNo"))) + "\t");
            exportData.add(rowData);
        }

        return CsvUtil.exportCSV(getHeader(language), exportData);
    }

    private LinkedHashMap<String, String> getHeader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "账变时间");
            header.put("3", "用户名");
            header.put("4", "用户ID");
            header.put("5", "所属商户");
            header.put("6", "账变类型");
            header.put("7", "账变来源");
            header.put("8", "账变金额");
            header.put("9", "账变前余额");
            header.put("10", "账变后余额");
            header.put("11", "账变结果");
            header.put("12", "注单号");
        } else {
            header.put("1", "NO");
            header.put("2", "Time");
            header.put("3", "User");
            header.put("4", "UserID");
            header.put("5", "Merchant");
            header.put("6", "Type");
            header.put("7", "Source");
            header.put("8", "ChangeAmount");
            header.put("9", "Before");
            header.put("10", "After");
            header.put("11", "Result");
            header.put("12", "Order");
        }

        return header;
    }

    /**
     * 查询注单具体实现,投注时间,结算时间,比赛时间
     *
     * @param betOrderVO
     * @return
     * @throws Exception
     */
    protected List<Map<String, Object>> abstractQueryUserOrderAccountHistoryList(BetOrderVO betOrderVO) throws Exception {
        List<Map<String, Object>> reuslt;
        assemblyQueryTime(betOrderVO);
        String filter = StringUtils.isEmpty(betOrderVO.getFilter()) ? "1" : betOrderVO.getFilter();
        log.info("betOrderVO:" + betOrderVO.getStartTimeL() + "," + betOrderVO.getEndTimeL() + "," + betOrderVO.getFilter() + ",outcomeList=" + betOrderVO.getOutComeList());
        if ("1".equals(filter)) {
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder()) == null ?
                    Constant.ID : Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
            betOrderVO.setSqlOrder(orderBy);
            betOrderVO.setSortby(sort);
            reuslt = orderMixMapper.queryBetOrderAccountHistoryList(betOrderVO);
        } else if ("3".equals(filter)) {
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder()) == null ?
                    "s1.id" : Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
            betOrderVO.setSqlOrder(orderBy);
            betOrderVO.setSortby(sort);
            reuslt = orderMixMapper.querySettleOrderAccountHistoryList(betOrderVO);
        } else {
            String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder()) == null ?
                    "s1.id" : Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder());
            String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
            betOrderVO.setSqlOrder(orderBy);
            betOrderVO.setSortby(sort);
            reuslt = orderMixMapper.queryLiveOrderAccountHistoryList(betOrderVO);
        }
        return reuslt;
    }

    /**
     * 注单查询 限制查询90天
     *
     * @Param: [betOrderVO]
     * @date: 2020/8/23 15:40
     */
    protected void assemblyQueryTime(BetOrderVO betOrderVO) throws Exception {
        Integer matchType = betOrderVO.getMatchType();
        Long matchId = betOrderVO.getMatchId();
        String startTime = betOrderVO.getStartTime();
        String endTime = betOrderVO.getEndTime();
        Long userId = betOrderVO.getUserId();
        String userName = betOrderVO.getUserName();
        Date startDate, endDate, before, now = new Date();
        String orderNo = betOrderVO.getOrderNo();
        if ("2".equals(betOrderVO.getFilter())) {
            if (matchId == null && (StringUtils.isEmpty(userName)) && userId == null && StringUtils.isEmpty(orderNo)) {
                before = DateUtils.addDays(now, -30);
            } else {
                before = DateUtils.addDays(now, -360);
            }
            betOrderVO.setBetStartTimeL(before.getTime());
            if (StringUtils.isNotEmpty(startTime)) {
                startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss");
            } else {
                startDate = DateUtils.addDays(now, -14);
            }
        } else {
            before = (matchId == null && (StringUtils.isEmpty(userName)) && userId == null && StringUtils.isEmpty(orderNo)) ?
                    DateUtils.addDays(now, -35) : DateUtils.addDays(now, -360);
            if (StringUtils.isNotEmpty(startTime)) {
                startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss");
                if (startDate.before(before)) {
                    startDate = before;
                }
            } else {
                startDate = before;
            }
        }


        endDate = StringUtils.isEmpty(endTime) ? now : DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss");
        betOrderVO.setStartTimeL(startDate.getTime());
        betOrderVO.setEndTimeL(endDate.getTime() + 999);
        if (betOrderVO.getMinBetAmount() != null) {
            betOrderVO.setMinBetAmount(BigDecimal.valueOf(betOrderVO.getMinBetAmount().doubleValue() * 100));
        }
        if (betOrderVO.getMaxBetAmount() != null) {
            betOrderVO.setMaxBetAmount(BigDecimal.valueOf(betOrderVO.getMaxBetAmount().doubleValue() * 100));
        }
        if (betOrderVO.getMinProfit() != null) {
            betOrderVO.setMinProfit(BigDecimal.valueOf(betOrderVO.getMinProfit().doubleValue() * 100));
        }
        if (betOrderVO.getMaxProfit() != null) {
            betOrderVO.setMaxProfit(BigDecimal.valueOf(betOrderVO.getMaxProfit().doubleValue() * 100));
        }
        betOrderVO.setLanguage(betOrderVO.getLanguage() == null ? LanguageEnum.ZS.getCode() : betOrderVO.getLanguage());
        if (CollectionUtils.isNotEmpty(betOrderVO.getOutComeList())) {
            betOrderVO.setOrderStatus(1);
        }
        log.info("betOrderVO:" + betOrderVO);
    }

    /**
     * 组装注单查询参数,自然日
     *
     * @param vo
     * @return
     * @throws Exception
     */
    protected BetOrderVO assemblyQueryOrderParamUTC8(UserOrderVO vo) {
        BetOrderVO betOrderVO = new BetOrderVO();
        BeanUtils.copyProperties(vo, betOrderVO);
        int pageSize = vo.getPageSize() == null || vo.getPageSize() > 100 ? 20 : vo.getPageSize();
        int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        betOrderVO.setPageNo(pageNum);
        betOrderVO.setSize(pageSize);
        betOrderVO.setStart((pageNum - 1) * pageSize);
        betOrderVO.setEnd(pageNum * pageSize);
        betOrderVO.setSortby(vo.getSort());
        betOrderVO.setSqlOrder(vo.getOrderBy());
        return betOrderVO;
    }
}
