package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.backup.mapper.BackupTAccountChangeHistoryMapper;
import com.panda.sport.backup.mapper.BackupTransferRecordErrorMapper;
import com.panda.sport.backup.mapper.BackupTransferRecordMapper;
import com.panda.sport.backup83.mapper.Backup83TAccountChangeHistoryMapper;
import com.panda.sport.backup83.mapper.Backup83TransferRecordMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.bss.TransferRecordPO;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.PageVO;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;
import com.panda.sport.merchant.manage.service.UserAccountService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.manage.service.impl
 * @Description :  账变与交易记录查询接口实现类
 * @Date: 2020-09-11 15:41
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired(required = false)
    private Backup83TransferRecordMapper transfer83RecordMapper;
    @Autowired
    private BackupTransferRecordMapper transferRecordMapper;
    @Autowired
    private BackupTransferRecordErrorMapper transferRecordErrorMapper;

    @Autowired
    private BackupTAccountChangeHistoryMapper accountChangeHistoryMapper;

    @Autowired(required = false)
    private Backup83TAccountChangeHistoryMapper account83ChangeHistoryMapper;

    @Autowired
    private MerchantFileService merchantFileService;

    /**
     * 查询交易记录
     *
     * @param findVO
     * @return
     */
    @Override

    public PageVO queryTransferRecord(UserAccountFindVO findVO) {
        try {
            if (StringUtils.isNotBlank(findVO.getMerchantCode())) {
                findVO.setMerchantCodes(Arrays.asList(findVO.getMerchantCode()));
            }
            if (StringUtils.isBlank(findVO.getSort())) {
                findVO.setSort("desc");
            }
            if (findVO.getStartTime() != null && findVO.getEndTime() == null) {
                findVO.setEndTime(System.currentTimeMillis());
            }
            if(findVO.getEndTime() != null){
                Long longTime = 999L;
                findVO.setEndTime(findVO.getEndTime() + longTime);
            }
            String userId = findVO.getUserId();
            if (StringUtils.isNotEmpty(userId)) {
                findVO.setUid(Long.parseLong(userId));
            }
            String userName = findVO.getUsername();
            String transferId = findVO.getTransferId();
            Long startTimeL = findVO.getStartTime();
            Long endTimeL = findVO.getEndTime();
            if (StringUtils.isAllEmpty(userName, userId, transferId) && endTimeL - startTimeL > 1000 * 60 * 60 * 24) {
                startTimeL = endTimeL - 1000 * 60 * 60 * 24;
                findVO.setStartTime(startTimeL);
            }
            Integer count = transfer83RecordMapper.findTransferRecordCount(findVO);
            PageVO poPageVO = new PageVO<TransferRecordPO>(count, findVO.getPageSize(), findVO.getPageNum());
            if (count == 0) {
                return poPageVO;
            }
            Integer star = (findVO.getPageNum() - 1) * findVO.getPageSize();
            if (star > count) {
                return poPageVO;
            }
            findVO.setStart(star);
            poPageVO.setRecords(transfer83RecordMapper.findTransferRecord(findVO));
            return poPageVO;
        } catch (Exception e) {
            log.error("查询账变记录异常!", e);
            return null;
        }
    }

    @Override
    public PageVO queryRetryTransferRecord(UserAccountFindVO findVO) {
        try {
            if (StringUtils.isNotBlank(findVO.getMerchantCode())) {
                if(findVO.getMerchantCode()!=null) {
                    findVO.setMerchantCodes(Collections.singletonList(findVO.getMerchantCode()));
                }
            }
            if (StringUtils.isBlank(findVO.getSort())) {
                findVO.setSort("desc");
            }
            /*查询七天内的免转失败记录 交易类弄为 结算、退款、礼金、拒单（目前下注先扣钱）、结算回滚、下注取消、下注取消回滚；*/
            findVO.setStatus(0);
            findVO.setTransferMode(1);
            Calendar calendar = Calendar.getInstance();
            long createTime = calendar.getTimeInMillis();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
            if ((findVO.getStartTime() == null || findVO.getStartTime() < calendar.getTimeInMillis()) && StringUtils.isEmpty(findVO.getTransferId())) {
                findVO.setStartTime(calendar.getTimeInMillis());
            }
            if (findVO.getEndTime() == null) {
                findVO.setEndTime(createTime);
            }
            String userId = findVO.getUserId();
            if (StringUtils.isNotEmpty(userId)) {
                findVO.setUid(Long.parseLong(userId));
            }
            String userName = findVO.getUsername();
            String transferId = findVO.getTransferId();
           /* Long startTimeL = findVO.getStartTime();
            Long endTimeL = findVO.getEndTime();
            if (StringUtils.isAnyEmpty(userName, userId, transferId) && endTimeL - startTimeL > 1000 * 60 * 60 * 24) {
                startTimeL = endTimeL - 1000 * 60 * 60 * 24;
                findVO.setStartTime(startTimeL);
            }*/
            Integer count = transferRecordErrorMapper.findTransferRecordCount(findVO);
            PageVO poPageVO = new PageVO<TransferRecordPO>(count, findVO.getPageSize(), findVO.getPageNum());
            if (count == 0) {
                return poPageVO;
            }
            Integer start = (findVO.getPageNum() - 1) * findVO.getPageSize();
            if (start > count) {
                return poPageVO;
            }
            findVO.setStart(start);
            poPageVO.setRecords(transferRecordErrorMapper.findTransferRecord(findVO));
            return poPageVO;
        } catch (Exception e) {
            log.error("queryRetryTransferRecord查询账变记录异常!", e);
            return null;
        }
    }

    /**
     * 交易记录导出
     *
     * @param findVO
     */
    @Override
    public Map transferRecordExport(String username, String merchantCode, UserAccountFindVO findVO, String language) throws IOException {
        if (StringUtils.isNotBlank(findVO.getMerchantCode())) {
            findVO.setMerchantCodes(Arrays.asList(findVO.getMerchantCode()));
        }
        if (StringUtils.isBlank(findVO.getSort())) {
            findVO.setSort("desc");
        }
        Map<String, Object> resultMap = new HashMap<>();
        Long time  = findVO.getEndTime() - findVO.getStartTime();
        Long dayMillisecond = 86400000L;
        if (time > (dayMillisecond * 16)){
            resultMap.put("code", "0002");
            resultMap.put("msg", "最多导16天的数据");
            return resultMap;
        }
        Integer dataSize = 0;
        resultMap.put("code", "0000");
        resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        findVO.setLanguage(language);
        if (time > dayMillisecond){
            Long endTime = findVO.getEndTime();
            Integer num = 1;
            while (true) {
                Long maxTime = findVO.getStartTime() + dayMillisecond;
                if (maxTime >= endTime){
                    maxTime = endTime;
                }
                findVO.setEndTime(maxTime);
                dataSize = transferRecordMapper.findTransferRecordExportListCount(findVO);
                try {
                    if (dataSize > 0) {
                        Date date = new Date(findVO.getStartTime());
                        merchantFileService.saveFileTask2((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "交易记录导出_"+ DateUtil.format(date, "yyyyMMdd") +"_" + num +"_" : "TransactionExport_" + num), merchantCode, username, JSON.toJSONString(findVO),
                                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-交易记录" : "Report Center-TransactionRecord", "transferRecordExportServiceImpl", dataSize);
                        num++;
                    }
                } catch (RuntimeException e) {
                    resultMap.put("code", "0002");
                    resultMap.put("msg", e.getMessage());
                }
                if (maxTime >= endTime){
                    break;
                }else {
                    findVO.setStartTime(findVO.getEndTime());
                }
            }
            return resultMap;
        }else {
            dataSize = transferRecordMapper.findTransferRecordExportListCount(findVO);
            findVO.setLanguage(language);
            try {
                Date date = new Date(findVO.getStartTime());
                merchantFileService.saveFileTask2((language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "交易记录导出_"+ DateUtil.format(date, "yyyyMMdd")  +"_" : "TransactionExport_"), merchantCode, username, JSON.toJSONString(findVO),
                        language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-交易记录" : "Report Center-TransactionRecord", "transferRecordExportServiceImpl", dataSize);
            } catch (RuntimeException e) {
                resultMap.put("code", "0002");
                resultMap.put("msg", e.getMessage());
            }
            return resultMap;
        }
    }
    /**
     * 账变记录导出
     *
     * @param findVO
     */
    @Override
    public Map<String, Object> accountHistoryExport(String username, String merchantCode, UserAccountFindVO findVO) throws IOException {

        Map<String, Object> resultMap = new HashMap<>();

        if (StringUtils.isNotBlank(findVO.getMerchantCode())) {
            findVO.setMerchantCodes(Collections.singletonList(findVO.getMerchantCode()));
        }
        if (StringUtils.isBlank(findVO.getSort())) {
            findVO.setSort("desc");
        }
       /*Integer total = accountChangeHistoryMapper.queryChangeHistoryListNewExportListCount(findVO);
        if (total > 1000000){
            resultMap.put("code","0001");
            resultMap.put("msg","导出数据超过最大限制一百万条");
            return resultMap;
        }*/
        Long time  = findVO.getEndTime() - findVO.getStartTime();
        Long dayMillisecond = 86400000L;
        if (time > (dayMillisecond * 16)){
            resultMap.put("code", "0002");
            resultMap.put("msg", "最多导16天的数据");
            return resultMap;
        }
        Integer dataSize = 0;
        String language = findVO.getLanguage();
        if (StringUtils.isEmpty(language)){
            language = LANGUAGE_CHINESE_SIMPLIFIED;
        }
        findVO.setLanguage(language);
        resultMap.put("code", "0000");
        resultMap.put("msg", LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ? "导出任务创建成功,请在文件列表等候下载！"
                : "The exporting task has been created,please click at the Download Task menu to check!");
        if (time > dayMillisecond){
            Long endTime = findVO.getEndTime();
            Integer num = 1;
            while (true) {
                Long maxTime = findVO.getStartTime() + dayMillisecond;
                if (maxTime >= endTime){
                    maxTime = endTime;
                }
                findVO.setEndTime(maxTime);
                dataSize =  accountChangeHistoryMapper.queryChangeHistoryListNewExportListCount(findVO);
                try {
                    if (dataSize > 0) {
                        Date date = new Date(findVO.getStartTime());
                        merchantFileService.saveFileTask2(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "账变记录导出_"+ DateUtil.format(date, "yyyyMMdd") +"_" + num : "Report Center-AccountRecord" + num
                                , merchantCode, username, JSON.toJSONString(findVO),
                                language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-账变记录" : "Report Center-AccountRecord", "accountHistoryExportServiceImpl", dataSize);
                        num++;
                    }
                } catch (RuntimeException e) {
                    resultMap.put("code", "0002");
                    resultMap.put("msg", e.getMessage());
                }
                if (maxTime >= endTime){
                    break;
                }else {
                    findVO.setStartTime(findVO.getEndTime());
                }
            }
            return resultMap;
        }else {
            dataSize = accountChangeHistoryMapper.queryChangeHistoryListNewExportListCount(findVO);
            try {
                Date date = new Date(findVO.getStartTime());
                merchantFileService.saveFileTask2(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "账变记录导出_"+ DateUtil.format(date, "yyyyMMdd") : "Report Center-AccountRecord"
                        , merchantCode, username, JSON.toJSONString(findVO),
                        language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-账变记录" : "Report Center-AccountRecord", "accountHistoryExportServiceImpl", dataSize);
            } catch (RuntimeException e) {
                resultMap.put("code", "0002");
                resultMap.put("msg", e.getMessage());
            }
            return resultMap;
        }
    }
/*    
    public Integer transferRecordExportCount( UserAccountFindVO findVO){
        return transferRecordMapper.findTransferRecordExportListCount(findVO);
    }*/

    /**
     * 查询账变记录
     *
     * @param findVO
     * @param language
     * @return
     */
    @Override

    public PageVO queryAccountHistory(UserAccountFindVO findVO, String language) {
        try {
            if (StringUtils.isNotBlank(findVO.getMerchantCode())) {
                findVO.setMerchantCodes(Arrays.asList(findVO.getMerchantCode()));
            }
            if (StringUtils.isBlank(findVO.getSort())) {
                findVO.setSort("desc");
            }
            int count = account83ChangeHistoryMapper.countChangeHistoryListNew(findVO);
            PageVO poPageVO = new PageVO<TransferRecordPO>(count, findVO.getPageSize(), findVO.getPageNum());
            if (count == 0) {
                return poPageVO;
            }
            Integer star = (findVO.getPageNum() - 1) * findVO.getPageSize();
            if (star > count) {
                return poPageVO;
            }
            findVO.setStart(star);
            poPageVO.setRecords(assemblyLanguage(account83ChangeHistoryMapper.queryChangeHistoryListNew(findVO), language));
            return poPageVO;
        } catch (Exception e) {
            log.error("queryAccountHistory查询用户账变记录异常!", e);
            return null;
        }
    }

    private List assemblyLanguage(List<AccountChangeHistoryFindVO> queryChangeHistoryListNew, String language) {

        if (!Constant.LANGUAGE_CHINESE_SIMPLIFIED.equalsIgnoreCase(language)) {
            if (CollectionUtils.isNotEmpty(queryChangeHistoryListNew)) {
                for (AccountChangeHistoryFindVO vo : queryChangeHistoryListNew) {
                    Integer bizType = vo.getBizType();
                    if (bizType == 1) {
                        vo.setRemark("Transfer In");
                    } else if (bizType == 2) {
                        vo.setRemark("Transfer OUT");
                    } else if (bizType == 3) {
                        vo.setRemark("Bet");
                    } else if (bizType == 4) {
                        vo.setRemark("Settle");
                    } else if (bizType == 7) {
                        vo.setRemark("Promotion");
                    } else if (bizType == 8) {
                        vo.setRemark("Refused");
                    } else if (bizType == 9) {
                        vo.setRemark("SettleRollback");
                    } else if (bizType == 10) {
                        vo.setRemark("BetCancel");
                    } else if (bizType == 11) {
                        vo.setRemark("BetCancelRollback");
                    }
                }
            }
        }
        return queryChangeHistoryListNew;
    }

}
