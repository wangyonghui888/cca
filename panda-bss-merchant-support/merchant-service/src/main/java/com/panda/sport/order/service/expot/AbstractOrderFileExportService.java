package com.panda.sport.order.service.expot;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.enums.UserAllowListSourceEnum;
import com.panda.sport.merchant.common.enums.UserLimitEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.po.merchant.MerchantOrderDayPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.utils.FtpUtil;
import com.panda.sport.merchant.common.utils.ZipFilesUtils;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetInfoDto;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  商户订单接口
 * @Date: 2020-12-11 10:40:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
public abstract class AbstractOrderFileExportService implements OrderFileExportService {

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    protected FtpProperties ftpProperties;

    public static final Map<Integer, Object> matchENMap = new HashMap<>();

    public static final Map<Integer, Object> matchMap = new HashMap<>();
    public static final Map<Integer, Object> tournamentLevelMap = new HashMap<>();

    static {
        matchMap.put(0, "赛事未开始");
        matchMap.put(1, "进行中(滚球)");
        matchMap.put(2, "暂停");
        matchMap.put(3, "结束");
        matchMap.put(4, "关闭");
        matchMap.put(5, "取消");
        matchMap.put(6, "比赛放弃");
        matchMap.put(7, "延迟");
        matchMap.put(8, "未知");
        matchMap.put(9, "延期");
        matchMap.put(10, "比赛中断");
        matchENMap.put(0, "PreMatch");
        matchENMap.put(1, "Ongoing");
        matchENMap.put(2, "Paused");
        matchENMap.put(3, "Over");
        matchENMap.put(4, "Closed");
        matchENMap.put(5, "Cancel");
        matchENMap.put(6, "GivenUP");
        matchENMap.put(7, "Delayed");
        matchENMap.put(8, "Unknown");
        matchENMap.put(9, "Delayed");
        matchENMap.put(10, "Paused");
        tournamentLevelMap.put(0, "");
        tournamentLevelMap.put(1, "一级联赛");
        tournamentLevelMap.put(2, "二级联赛");
        tournamentLevelMap.put(3, "三级联赛");
        tournamentLevelMap.put(4, "四级联赛");
        tournamentLevelMap.put(5, "五级联赛");
        tournamentLevelMap.put(6, "六级联赛");
        tournamentLevelMap.put(7, "七级联赛");
        tournamentLevelMap.put(8, "八级联赛");
        tournamentLevelMap.put(9, "九级联赛");
        tournamentLevelMap.put(10, "十级联赛");
        tournamentLevelMap.put(11, "十一级联赛");
        tournamentLevelMap.put(12, "十二级联赛");
        tournamentLevelMap.put(13, "十三级联赛");
        tournamentLevelMap.put(14, "十四级联赛");
        tournamentLevelMap.put(15, "十五级联赛");
        tournamentLevelMap.put(16, "十六级联赛");
        tournamentLevelMap.put(17, "十七级联赛");
        tournamentLevelMap.put(18, "十八级联赛");
        tournamentLevelMap.put(19, "十九级联赛");
        tournamentLevelMap.put(20, "二十级联赛");
    }

    public void updateRate(Long fileId, Long rate) {
        merchantFileService.updateFileRate(fileId, rate);
    }

    public void updateFileStart(Long fileId) {
        merchantFileService.updateFileStatus(fileId);
    }

    public void updateFileStatus(Long fileId, Integer size) {
        merchantFileService.updateFileSizeAndStatus(fileId, size);
    }

    public boolean checkTask(Long id) {
        if (merchantFileService.loadById(id) == null) {
            return true;
        }
        return false;
    }

    public void updateFileStatusEnd(Long fileId) {
        merchantFileService.updateFileEnd(fileId);
    }

    public void exportFail(Long fileId, String remark) {
        merchantFileService.updateFileFail(fileId, remark);
    }

    public void uploadFile(MerchantFile merchantFile, InputStream inputStream) throws IOException {
        InputStream zip = null;
        try {
            zip = ZipFilesUtils.zip(inputStream, merchantFile.getFileName());
            updateFileStatus(merchantFile.getId(), zip.available());
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", merchantFile.getFilePath(), merchantFile.getFtpFileName(), zip);

            updateFileStatusEnd(merchantFile.getId());
        } catch (Exception e) {
            log.error("导出上传异常！", e);
            throw e;
        } finally {
            if (zip != null) {
                zip.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public void uploadFileMax(MerchantFile merchantFile, InputStream inputStream, String fileName) throws IOException {
        InputStream zip = null;
        try {
            zip = ZipFilesUtils.zip(inputStream, fileName);
            if (merchantFile.getDataSize() == null) {
                merchantFile.setDataSize(0);
            }
            merchantFile.setDataSize(merchantFile.getDataSize() + zip.available());

            updateFileStatus(merchantFile.getId(), merchantFile.getDataSize());
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", merchantFile.getFilePath(), fileName, zip);
            //updateFileStatusEnd(merchantFile.getId());
        } catch (Exception e) {
            log.error("导出上传异常！", e);
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (zip != null) {
                zip.close();
            }
        }
    }

    public void uploadFile(MerchantFile merchantFile, InputStream inputStream, int j) throws IOException {
        InputStream zip = null;
        try {
            String[] fileNames = merchantFile.getFileName().split(",");
            zip = ZipFilesUtils.zip(inputStream, fileNames[j]);
            updateFileStatus(merchantFile.getId(), zip.available() * (j + 1));
            // ftp上传
            String[] ftpFileNames = merchantFile.getFtpFileName().split(",");
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", merchantFile.getFilePath(), ftpFileNames[j], zip);

            updateFileStatusEnd(merchantFile.getId());
        } catch (Exception e) {
            log.error("导出上传异常！", e);
            throw e;
        } finally {
            if (zip != null) {
                zip.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 导出用户到csv文件
     *
     * @param resultList 导出的数据（用户）
     */
    protected static byte[] groupByExportUsersToCsv(List<?> resultList, UserOrderVO vo) {
        String filter = vo.getFilter();
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        ObjectMapper mapper = new ObjectMapper();
        List<UserOrderDayPO> filterList = mapper.convertValue(resultList, new TypeReference<List<UserOrderDayPO>>() {
        });
        int i = 0;
        for (UserOrderDayPO orderPO : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", "\t" + orderPO.getUserId());
            String name = "*********";
            try {
                name = "****" + orderPO.getUserName().substring(3);
            } catch (Exception e) {
                log.error("数据截取", e);
            }
            rowData.put("3", name);
            rowData.put("4", vo.getStartTime() + "至" + vo.getEndTime());
            rowData.put("5", orderPO.getMerchantCode());
            if ("1".equals(filter)) {
                rowData.put("6", orderPO.getBetNum());
                rowData.put("7", orderPO.getBetAmount());
                rowData.put("8", orderPO.getProfit());
                rowData.put("9", orderPO.getProfitRate() + "%");
            } else if ("2".equals(filter)) {
                rowData.put("6", orderPO.getLiveOrderNum());
                rowData.put("7", orderPO.getLiveOrderAmount());
                rowData.put("8", orderPO.getLiveProfit());
                rowData.put("9", orderPO.getLiveProfitRate() + "%");
            } else {
                rowData.put("6", orderPO.getSettleOrderNum());
                rowData.put("7", orderPO.getSettleOrderAmount());
                rowData.put("8", orderPO.getSettleProfit());
                rowData.put("9", orderPO.getSettleProfitRate() + "%");
            }
            rowData.put("10", orderPO.getActiveDays());
            rowData.put("11", "3".equals(filter) ? orderPO.getSettleValidBetMoney() : orderPO.getOrderValidBetMoney());
            rowData.put("12", orderPO.getOrderValidBetCount());
            //修改用户投注统计有效投注笔数取值
            //rowData.put("12", "1".equals(filter) ? orderPO.getValidTickets() : orderPO.getTicketSettled());
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = getUserHeader(vo.getLanguage());

        return CsvUtil.exportCSV(header, exportData);
    }


    protected static LinkedHashMap<String, String> getUserHeader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "用户ID");
            header.put("3", "用户名");
            header.put("4", "日期");
            header.put("5", "所属商户");
            header.put("6", "投注笔数");
            header.put("7", "投注金额");
            header.put("8", "盈亏金额");
            header.put("9", "盈亏比例");
            header.put("10", "活跃天数");
            header.put("11", "有效投注额");
            header.put("12", "有效投注笔数");
        } else {
            header.put("1", "NO");
            header.put("2", "UserID");
            header.put("3", "User");
            header.put("4", "Date");
            header.put("5", "Merchant");
            header.put("6", "Tickets");
            header.put("7", "BetAmount");
            header.put("8", "Profit");
            header.put("9", "ProfitRate");
            header.put("10", "ActiveDays");
            header.put("11", "OrderValidBetMoney");
            header.put("12", "validTickets");
        }
        return header;
    }



    /**
     * 导出用户到csv文件
     *
     * @param resultList 导出的数据（用户）
     */
    protected static byte[] betUserReportExportToCsvToCsv(List<?> resultList, UserOrderVO vo) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        ObjectMapper mapper = new ObjectMapper();
        String language = vo.getLanguage();
        List<UserOrderAllPO> filterList = mapper.convertValue(resultList, new TypeReference<List<UserOrderAllPO>>() {
        });
        int i = 0;
        for (UserOrderAllPO orderPO : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", orderPO.getUserId() + "\t");
            rowData.put("3", orderPO.getUserName());
            rowData.put("4",language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? 1==orderPO.getDisabled()? "禁用" : "启用" : 1==orderPO.getDisabled()? "Disable" : "Enable" ) ;
            rowData.put("5", orderPO.getMerchantName());
            rowData.put("6", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? (2==(null==orderPO.getIsvip() ? 0 :orderPO.getIsvip()) ? "是" : "否") : (2==(null==orderPO.getIsvip() ? 0 :orderPO.getIsvip()) ? "Yes" : "No"));
            rowData.put("7", null==orderPO.getAmount()? "0" : orderPO.getAmount() + "\t");
            rowData.put("8", null== orderPO.getBetAmount() ? "0" : orderPO.getBetAmount()  + "\t");
            rowData.put("9", null== orderPO.getProfit() ? "0" : orderPO.getProfit() + "\t");
            rowData.put("10", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? CurrencyTypeEnum.optGetDescription(orderPO.getCurrencyCode()) :CurrencyTypeEnum.optGetCurrency(orderPO.getCurrencyCode()));
            rowData.put("11", null== orderPO.getProfit() ? "0" : orderPO.getBetNum() + "\t");
            rowData.put("12", orderPO.getLastBetStr());
            rowData.put("13", "");
            rowData.put("14", orderPO.getLastLoginStr());
            rowData.put("15",language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? 0==orderPO.getMarketLevel()?"标准赔率":orderPO.getMarketLevel()+"\t":0==orderPO.getMarketLevel()?"The standard price":orderPO.getMarketLevel()+"\t");
            rowData.put("16", orderPO.getSpecialBettingLimitDelayTime());
            rowData.put("17", dataInfo(orderPO.getSpecialBettingLimitType(), vo.getLanguage()));
            rowData.put("18", "\t" + (null == orderPO.getSpecialBettingLimitTime() ? "" :DateUtil.format(new Date(orderPO.getSpecialBettingLimitTime()), "yyyy-MM-dd HH:mm:ss" + "\t")));
            rowData.put("19", orderPO.getVipUpdateTime());
            rowData.put("20", "\t" + (null == orderPO.getCreateTime() ? "" : DateUtil.format(new Date(orderPO.getCreateTime()), "yyyy-MM-dd HH:mm:ss" + "\t")));

            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = getBetUserHeader(vo.getLanguage());

        return CsvUtil.exportCSV(header, exportData);
    }

    /**
     * 导出用户到csv文件
     *
     * @param resultList 导出的数据（用户）
     */
    protected static byte[] betAllowListUserReportExportToCsvToCsv(List<?> resultList, UserOrderVO vo) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        ObjectMapper mapper = new ObjectMapper();
        String language = vo.getLanguage();
        List<UserOrderAllPO> filterList = mapper.convertValue(resultList, new TypeReference<List<UserOrderAllPO>>() {
        });
        int i = 0;
        for (UserOrderAllPO orderPO : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", orderPO.getUserId() + "\t");
            rowData.put("3", orderPO.getUserName());
            rowData.put("4", UserAllowListSourceEnum.getSourceNameByCode(orderPO.getDisabled()));
            rowData.put("5",language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? 1==orderPO.getDisabled()? "禁用" : "启用" : 1==orderPO.getDisabled()? "Disable" : "Enable" ) ;
            rowData.put("6", orderPO.getMerchantName());
            rowData.put("7", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? (2==(null==orderPO.getIsvip() ? 0 :orderPO.getIsvip()) ? "是" : "否") : (2==(null==orderPO.getIsvip() ? 0 :orderPO.getIsvip()) ? "Yes" : "No"));
            rowData.put("8", null==orderPO.getAmount()? "0" : orderPO.getAmount() + "\t");
            rowData.put("9", null== orderPO.getBetAmount() ? "0" : orderPO.getBetAmount()  + "\t");
            rowData.put("10", null== orderPO.getProfit() ? "0" : orderPO.getProfit() + "\t");
            rowData.put("11", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? CurrencyTypeEnum.optGetDescription(orderPO.getCurrencyCode()) :CurrencyTypeEnum.optGetCurrency(orderPO.getCurrencyCode()));
            rowData.put("12", null== orderPO.getProfit() ? "0" : orderPO.getBetNum() + "\t");
            rowData.put("13", orderPO.getLastBetStr());
            rowData.put("14", "");
            rowData.put("15", orderPO.getLastLoginStr());
            rowData.put("16",language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? 0==orderPO.getMarketLevel()?"标准赔率":orderPO.getMarketLevel()+"\t":0==orderPO.getMarketLevel()?"The standard price":orderPO.getMarketLevel()+"\t");
            rowData.put("17", orderPO.getSpecialBettingLimitDelayTime());
            rowData.put("18", dataInfo(orderPO.getSpecialBettingLimitType(), vo.getLanguage()));
            rowData.put("19", "\t" + (null == orderPO.getSpecialBettingLimitTime() ? "" :DateUtil.format(new Date(orderPO.getSpecialBettingLimitTime()), "yyyy-MM-dd HH:mm:ss" + "\t")));
            rowData.put("20", orderPO.getVipUpdateTime());
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = getAllowListUserHeader(vo.getLanguage());

        return CsvUtil.exportCSV(header, exportData);
    }


    /**
     * 投注用户管理headers设置文件
     *
     * @param language 导出的数据（用户）
     */
    protected static LinkedHashMap<String, String> getBetUserHeader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "用户ID");
            header.put("3", "用户名");
            header.put("4", "启&禁用");
            header.put("5", "所属商户");
            header.put("6", "线路vip");
            header.put("7", "可用余额");
            header.put("8", "累计投注额");
            header.put("9", "累计盈利");
            header.put("10", "用户币种");
            header.put("11", "注单数量");
            header.put("12", "最后投注时间");
            header.put("13", "在线状态");
            header.put("14", "最后登录时间");
            header.put("15", "赔率分组");
            header.put("16", "投注延时");
            header.put("17", "投注特殊限额");
            header.put("18", "特殊管控设置时间");
            header.put("19", "VIP升级时间");
            header.put("20", "注册时间");
        } else {
            header.put("1", "NO");
            header.put("2", "User ID");
            header.put("3", "Username");
            header.put("4", "Disable");
            header.put("5", "Owned Merchant");
            header.put("6", "Line vip");
            header.put("7", "Available Balance");
            header.put("8", "Cumulative bet amount");
            header.put("9", "Cumulative profit");
            header.put("10", "User currency");
            header.put("11", "Number of bets");
            header.put("12", "Last bet time");
            header.put("13", "online status");
            header.put("14", "Last login time");
            header.put("15", "Odds division");
            header.put("16", "Devote time delay");
            header.put("17", "Special betting limit");
            header.put("18", "Special control setting time");
            header.put("19", "VIP Upgrade Time");
            header.put("20", "Registration time");
        }
        return header;
    }
    protected static LinkedHashMap<String, String> getAllowListUserHeader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "用户ID");
            header.put("3", "用户名");
            header.put("4", "来源");
            header.put("5", "启&禁用");
            header.put("6", "所属商户");
            header.put("7", "线路vip");
            header.put("8", "可用余额");
            header.put("9", "累计投注额");
            header.put("10", "累计盈利");
            header.put("11", "用户币种");
            header.put("12", "注单数量");
            header.put("13", "最后投注时间");
            header.put("14", "在线状态");
            header.put("15", "最后登录时间");
            header.put("16", "赔率分组");
            header.put("17", "投注延时");
            header.put("18", "投注特殊限额");
            header.put("19", "特殊管控设置时间");
            header.put("20", "VIP升级时间");
        } else {
            header.put("1", "NO");
            header.put("2", "User ID");
            header.put("3", "Username");
            header.put("4", "Source");
            header.put("5", "Disable");
            header.put("6", "Owned Merchant");
            header.put("7", "Line vip");
            header.put("8", "Available Balance");
            header.put("9", "Cumulative bet amount");
            header.put("10", "Cumulative profit");
            header.put("11", "User currency");
            header.put("12", "Number of bets");
            header.put("13", "Last bet time");
            header.put("14", "online status");
            header.put("15", "Last login time");
            header.put("16", "Odds division");
            header.put("17", "Devote time delay");
            header.put("18", "Special betting limit");
            header.put("19", "Special control setting time");
            header.put("20", "VIP Upgrade Time");
        }
        return header;
    }

    protected static byte[] groupByExportMerchantReportToCsv(List<MerchantOrderDayPO> filterList, MerchantOrderVO merchantOrderVO) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(filterList) ? 0 : filterList.size());
        int i = 0;
        for (MerchantOrderDayPO order : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", StringUtils.isNotBlank(merchantOrderVO.getStartTime()) && StringUtils.isNotBlank(merchantOrderVO.getEndTime())
                    ? merchantOrderVO.getStartTime() + "to" + merchantOrderVO.getEndTime() : "");
            rowData.put("3", " " + order.getMerchantCode() + " ");
            rowData.put("4", order.getMerchantLevel() == null ? "" : order.getMerchantLevel() + " ");
            rowData.put("5", order.getBetAmount());
            rowData.put("6", order.getOrderSum());
            rowData.put("7", order.getProfit());
            rowData.put("8", order.getProfitRate() == null ? "0.00" : order.getProfitRate().multiply(new BigDecimal(100)) + "%");
            rowData.put("9", order.getBetUserSum());
            rowData.put("10", order.getAddUser());
            rowData.put("11", order.getRegisterTotalUserSum());
            rowData.put("12", order.getBetUserRate() == null ? "0.00" : order.getBetUserRate() + "%");
            rowData.put("13", order.getOrderValidBetMoney());
            exportData.add(rowData);
        }
        return CsvUtil.exportCSV(getHeader(merchantOrderVO.getLanguage()), exportData);
    }

    protected static byte[] groupByExportMerchantReportToCsv(List<Map<String, Object>> filterList, MerchantFinanceDayVo merchantOrderVO) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(filterList) ? 0 : filterList.size());
        int i = 0;
        for (Map map : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", StringUtils.isNotBlank(merchantOrderVO.getStartTime()) && StringUtils.isNotBlank(merchantOrderVO.getEndTime())
                    ? merchantOrderVO.getStartTime() + "to" + merchantOrderVO.getEndTime() : "");
            rowData.put("3", " " + map.get("merchantCode") + " ");
            rowData.put("4", map.get("merchantLevel") == null ? "" : map.get("merchantLevel") + " ");
            rowData.put("5", map.get("orderAmountTotal"));
            rowData.put("6", map.get("orderValidNum"));
            rowData.put("7", map.get("platformProfit"));
            rowData.put("8", map.get("platformProfitRate") == null ? "0.00" : map.get("platformProfitRate") + "%");
            rowData.put("9", map.get("orderUserNum"));
            rowData.put("10", map.get("newUserSum"));
            rowData.put("11", map.get("registerUserSum"));
            rowData.put("12", map.get("betsOnRate") == null ? "0.00" : map.get("betsOnRate") + "%");
            rowData.put("13", map.get("orderValidBetMoney"));
            exportData.add(rowData);
        }
        return CsvUtil.exportCSV(getHeader(merchantOrderVO.getLanguage()), exportData);
    }

    private static LinkedHashMap<String, String> getHeader(String language) {

        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "日期");
            header.put("3", "商户编码");
            header.put("4", "商户等级");
            header.put("5", "投注金额");
            header.put("6", "投注笔数");
            header.put("7", "平台盈利金额");
            header.put("8", "盈利率");
            header.put("9", "投注用户数");
            header.put("10", "新增用户数");
            header.put("11", "注册用户数");
            header.put("12", "投注率");
            header.put("13", "有效投注额");
        } else {
            header.put("1", "NO");
            header.put("2", "Date");
            header.put("3", "Merchant");
            header.put("4", "Level");
            header.put("5", "BetAmount");
            header.put("6", "Tickets");
            header.put("7", "Profit");
            header.put("8", "ProfitRate");
            header.put("9", "BetUsers");
            header.put("10", "NewUsers");
            header.put("11", "Register");
            header.put("12", "BetRate");
            header.put("13", "OrderValidBetMoney");
        }

        return header;
    }

    protected LinkedHashMap<String, String> getPlayHeader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "商户名称");
            header.put("3", "赛事对阵");
            header.put("4", "赛事状态");
            header.put("5", "开赛时间");
            header.put("6", "玩法名称");
            header.put("7", "盘口");
            header.put("8", "投注金额");
            header.put("9", "投注笔数");
            header.put("10", "投注用户数");
            header.put("11", "未结算笔数");
            header.put("12", "未结算金额");
            header.put("13", "派彩金额");
            header.put("14", "盈利金额");
            header.put("15", "盈利率");
            header.put("16", "赛事id");
        } else {
            header.put("1", "NO");
            header.put("2", "Merchant");
            header.put("3", "VS");
            header.put("4", "Status");
            header.put("5", "BeginTime");
            header.put("6", "PlayName");
            header.put("7", "Market");
            header.put("8", "BetAmount");
            header.put("9", "Tickets");
            header.put("10", "BetUsers");
            header.put("11", "UnsettledTickets");
            header.put("12", "UnsettledAmount");
            header.put("13", "Return");
            header.put("14", "Profit");
            header.put("15", "ProfitRate");
            header.put("16", "matchId");
        }


        return header;
    }

    /**
     * 设置赛事导出报表格式
     *
     * @param resultList
     * @param language
     * @return
     */
    protected byte[] exportMatchToCsv(List<?> resultList, String language) {
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        ObjectMapper mapper = new ObjectMapper();
        List<MerchantMatchBetInfoDto> filterList = mapper.convertValue(resultList, new TypeReference<List<MerchantMatchBetInfoDto>>() {
        });
        int i = 0;
        Map<Integer, String> sportMap = localCacheService.getSportMap(language);
        for (MerchantMatchBetInfoDto order : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", order.getMerchantCode());
            rowData.put("3", order.getMatchInfo());
            rowData.put("4", order.getTournamentName());
            if (sportMap != null && order.getSportId() != null) {
                order.setSportName(sportMap.get(order.getSportId()));
            }
            rowData.put("5", order.getSportName());
            rowData.put("6", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? tournamentLevelMap.get(order.getTournamentLevel()) : order.getTournamentLevel());
            rowData.put("7", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? matchMap.get(order.getMatchStatus()) :
                    matchENMap.get(order.getMatchStatus()));
            rowData.put("8", DateFormatUtils.format(new Date(order.getBeginTime()), "yyyy-MM-dd HH:mm:ss"));
            rowData.put("9", order.getBetAmount());
            rowData.put("10", order.getOrderAmount());
            rowData.put("11", order.getUserAmount());
            rowData.put("12", order.getUnSettleOrder());
            rowData.put("13", order.getUnSettleAmount());
            rowData.put("14", order.getSettleAmount());
            rowData.put("15", order.getProfit());
            rowData.put("16", order.getProfitRate() + "%");
            rowData.put("17", order.getPlayAmount());
            rowData.put("18", order.getParlayVaildBetAmount());
            rowData.put("19", order.getParlayValidTickets());
            rowData.put("20", order.getParlayProfit());
            rowData.put("21", order.getParlayProfitRate());
            rowData.put("22", order.getSettleValidBetMoney());
            rowData.put("23", order.getSettleValidOrderCount());
            rowData.put("24", order.getMatchId());
            exportData.add(rowData);
        }
        return com.panda.sport.merchant.common.utils.CsvUtil.exportCSV(getheader(language), exportData);
    }

    private LinkedHashMap<String, String> getheader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "商户名称");
            header.put("3", "赛事对阵");
            header.put("4", "联赛名");
            header.put("5", "赛种");
            header.put("6", "赛事等级");
            header.put("7", "赛事状态");
            header.put("8", "开赛时间");
            header.put("9", "投注金额");
            header.put("10", "投注笔数");
            header.put("11", "投注用户数");
            header.put("12", "未结算笔数");
            header.put("13", "未结算金额");
            header.put("14", "派彩金额");
            header.put("15", "盈利金额");
            header.put("16", "盈利率");
            header.put("17", "玩法数量");
            header.put("18", "串关投注额");
            header.put("19", "串关注单数");
            header.put("20", "串关盈利额");
            header.put("21", "串关盈利率");
            header.put("22", "有效投注额");
            header.put("23", "有效投注笔数");
            header.put("24", "赛事id");
        } else {
            header.put("1", "NO");
            header.put("2", "merchant");
            header.put("3", "MatchInfo");
            header.put("4", "Tournament");
            header.put("5", "Sport");
            header.put("6", "MatchLevel");
            header.put("7", "Status");
            header.put("8", "BeginTime");
            header.put("9", "BetAmount");
            header.put("10", "Tickets");
            header.put("11", "BetUsers");
            header.put("12", "UnsettleTickets");
            header.put("13", "UnsettleAmount");
            header.put("14", "Return");
            header.put("15", "Profit");
            header.put("16", "ProfitRate");
            header.put("17", "PlayNum");
            header.put("18", "SeriesAmount");
            header.put("19", "SeriesTickets");
            header.put("20", "SeriesProfit");
            header.put("21", "SeriesProfitRate");
            header.put("22", "SettleValidBetMoney");
            header.put("23", "SettleValidOrderCount");
            header.put("24", "matchId");
        }

        return header;
    }

    /**
     * 导出用户到csv文件
     * String.valueOf不能删除
     *
     * @param mapList  导出的数据
     * @param language
     */
    protected byte[] exportFinanceDayCsv(List<?> mapList, String language) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> filterList = mapper.convertValue(mapList, new TypeReference<List<Map<String, String>>>() {
        });
        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(mapList.size());
        for (Map<String, String> map : filterList) {
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", map.get("username"));
            rowData.put("2", map.get("platform_name"));
            rowData.put("3", map.get("country_zh"));
            rowData.put("4", map.get("sport_name"));
            rowData.put("5", map.get("profit_amount"));
            rowData.put("6", map.get("order_amount_total"));
            rowData.put("7", map.get("market_type"));
            rowData.put("8", map.get("market_value"));
            rowData.put("9", map.get("odds_value"));
            rowData.put("10", map.get("order_status") + "\t");
            rowData.put("11", map.get("create_time") + "\t");

            rowData.put("12", map.get("begin_time") == null || StringUtils.isEmpty(map.get("begin_time")) ? map.get("create_time") : map.get("begin_time") + "\t");
            rowData.put("13", map.get("settle_time") + "\t");
            rowData.put("14", map.get("order_no") + "\t");
            rowData.put("15", map.get("series_value"));
            rowData.put("16", map.get("settle_status"));
            rowData.put("17", map.get("match_id"));
            rowData.put("18", map.get("match_name"));
            rowData.put("19", map.get("match_info"));
            rowData.put("20", map.get("match_type"));
            rowData.put("21", map.get("play_option_name") + "\t");
            rowData.put("22", map.get("play_name"));
            rowData.put("23", map.get("uid") + "\t");
            BigDecimal orderValidBetMoney = new BigDecimal(0);
            try {
                Integer come = Integer.valueOf(map.get("out_come"));
                BigDecimal amount = BigDecimal.valueOf(Float.parseFloat(map.get("profit_amount")));
                BigDecimal toal = BigDecimal.valueOf(Float.parseFloat(map.get("order_amount_total")));
                if (come == 4 || come == 5) {
                    if (amount != null) {
                        if (toal.compareTo(amount) < 0) {
                            orderValidBetMoney = toal.abs();
                        } else {
                            orderValidBetMoney = amount.abs();
                        }
                    }
                } else {
                    if (amount != null) {
                        orderValidBetMoney = amount.abs();
                    }
                }
            } catch (Exception e) {
                log.error("有效投注额计算错误！", e);
            }
            rowData.put("24", orderValidBetMoney.setScale(2, BigDecimal.ROUND_DOWN) + "\t");
            rowData.put("25", map.get("vip_update_time") + "\t");
//            rowData.put("1", new BigDecimal(dayVo.getOrderNo()).toPlainString() + "\t");
            exportData.add(rowData);
        }


        return CsvUtil.exportCSV(getFinnceHeader(language), exportData);
    }

    private LinkedHashMap<String, String> getFinnceHeader(String language) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "用户名称");
            header.put("2", "平台名称");
            header.put("3", "用户币种");
            header.put("4", "赛种");
            header.put("5", "盈亏");
            header.put("6", "下注金额");
            header.put("7", "盘口类型");
            header.put("8", "盘口值");
            header.put("9", "赔率");
            header.put("10", "注单状态");
            header.put("11", "下注时间");
            header.put("12", "开赛时间");
            header.put("13", "结算时间");
            header.put("14", "注单号");
            header.put("15", "串关值");
            header.put("16", "结算状态");
            header.put("17", "赛事ID");
            header.put("18", "联赛名称");
            header.put("19", "比赛对阵");
            header.put("20", "注单类型");
            header.put("21", "投注项名称");
            header.put("22", "玩法名称");
            header.put("23", "用户ID");
            header.put("24", "有效投注额");
        } else {
            header.put("1", "Player");
            header.put("2", "Platform");
            header.put("3", "Currency");
            header.put("4", "Sport");
            header.put("5", "Profit");
            header.put("6", "BetAmount");
            header.put("7", "MarketType");
            header.put("8", "Market");
            header.put("9", "Odds");
            header.put("10", "BetStatus");
            header.put("11", "BetTime");
            header.put("12", "BeginTime");
            header.put("13", "SettleTime");
            header.put("14", "BetNo");
            header.put("15", "Series");
            header.put("16", "SettleResult");
            header.put("17", "MatchId");
            header.put("18", "Tournament");
            header.put("19", "MatchInfo");
            header.put("20", "TicketType");
            header.put("21", "PlayOptions");
            header.put("22", "PlayName");
            header.put("23", "PlayerId");
            header.put("24", "OrderValidBetMoney");
        }

        header.put("25", "vip升级时间");

        return header;
    }

    private static  String dataInfo(Integer newData, String language) {
        String neData = "";
        neData = String.valueOf(newData);
        if (StringUtils.isNotEmpty(neData)) {
            switch (neData) {
                case "1":
                    neData = LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ? UserLimitEnum.LIMIT_TYPE_1.getRemark() : UserLimitEnum.LIMIT_TYPE_1.getEnRemark();
                    break;
                case "2":
                    neData = LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ? UserLimitEnum.LIMIT_TYPE_2.getRemark() : UserLimitEnum.LIMIT_TYPE_2.getEnRemark();
                    break;
                case "3":
                    neData = LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ? UserLimitEnum.LIMIT_TYPE_3.getRemark() : UserLimitEnum.LIMIT_TYPE_3.getEnRemark();
                    break;
                case "4":
                    neData = LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ? UserLimitEnum.LIMIT_TYPE_4.getRemark() : UserLimitEnum.LIMIT_TYPE_4.getEnRemark();
                    break;
                case "5":
                    neData = LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ? UserLimitEnum.LIMIT_TYPE_5.getRemark() : UserLimitEnum.LIMIT_TYPE_5.getEnRemark();
                    break;
            }
        }
        return neData;
    }

}
