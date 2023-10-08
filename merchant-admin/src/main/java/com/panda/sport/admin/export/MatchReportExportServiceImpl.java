package com.panda.sport.admin.export;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetInfoDto;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetVo;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  赛事报表导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("matchReportExportServiceImpl")
@Slf4j
public class MatchReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private MerchantReportClient rpcClient;
    @Autowired
    private LocalCacheService localCacheService;
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

    @Override
    @Async()
    public void export(MerchantFile merchantFile) {

        long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
                if (super.checkTask(merchantFile.getId())) {
                    log.info("当前任务被删除！");
                    return;
                }
                super.updateFileStart(merchantFile.getId());

                log.info("开始赛事投注报表导出 param = {}", merchantFile.getExportParam());
                JSONObject jsonObject = JSON.parseObject(merchantFile.getExportParam());
                String language = jsonObject.getString("language");
                if (StringUtils.isEmpty(language)){
                    language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
                }
                MerchantMatchBetVo sportVO = JSON.parseObject(merchantFile.getExportParam(), MerchantMatchBetVo.class);
                List<?> resultList = null;
                if (CollectionUtil.isEmpty(sportVO.getMerchantCodeList())) {
                    Map<String, Object> result = rpcClient.queryMatchStatisticListNew(sportVO);
                    if (result != null) {
                        resultList = (List<?>) result.get("list");
                    }
                } else {
                    Map<String, Object> result = rpcClient.queryMatchStatisticListNew(sportVO);
                    if (result != null) {
                        resultList = (List<?>) result.get("list");
                    }
                }
                if (CollectionUtils.isEmpty(resultList)) {
                    throw new Exception("未查询到数据");
                }

                inputStream = new ByteArrayInputStream(exportMatchToCsv(resultList, language));

                super.uploadFile(merchantFile, inputStream);

                log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("赛事投注统计报表异常!", e);
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

}
