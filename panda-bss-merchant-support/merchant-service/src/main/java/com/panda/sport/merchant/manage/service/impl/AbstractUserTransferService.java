package com.panda.sport.merchant.manage.service.impl;


import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.panda.sport.backup83.mapper.Backup83TAccountChangeHistoryMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.bss.AccountChangeHistoryPO;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryVO;
import com.panda.sport.merchant.common.vo.UserTransferVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("abstractUserTransferService")
public abstract class AbstractUserTransferService {

    @Autowired
    public Backup83TAccountChangeHistoryMapper accountChangeHistoryMapper;

    /**
     * 查询账变记录(90天内)
     *
     * @Param: [vo]
     * @return: java.lang.Object
     * @date: 2020/8/23 15:43
     */
    protected Object abstractQueryUserTransferList(UserTransferVO vo, String language) throws Exception {
        Integer pageSize = vo.getPageSize() == null || vo.getPageSize() > 100 ? 20 : vo.getPageSize();
        Integer pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
        vo.setPageNo(pageNum);
        vo.setSize(pageSize);
        vo.setStart((pageNum - 1) * pageSize);
        vo.setEnd(pageNum * pageSize);
        String startTime = vo.getStartTime();
        String endTime = vo.getEndTime();
        Date startDate, endDate;
        Date before90 = DateUtils.addDays(new Date(), -90);
        if (StringUtils.isNotEmpty(startTime)) {
            startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss");
            if (startDate.before(before90)) {
                startDate = before90;
            }
        } else {
            startDate = before90;
        }
        endDate = StringUtils.isEmpty(endTime) ? new Date() : DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss");
        vo.setStartTimeL(startDate.getTime());
        vo.setEndTimeL(endDate.getTime());
        List<AccountChangeHistoryPO> userTransferPOList;
        int totCnt = accountChangeHistoryMapper.countChangeHistoryList(vo);
        String orderBy = "s.create_time";
        String sort = StringUtils.isEmpty(vo.getSort()) ? Constant.DESC : vo.getSort();
        vo.setSort(sort);
        vo.setOrderBy(orderBy);
        PageHelper.startPage(pageNum, pageSize, orderBy + " " + sort);
        Map<String, Object> param = new HashMap<>();
        if (totCnt > 0) {
            log.info("账变明细查询:" + vo);
            userTransferPOList = accountChangeHistoryMapper.queryChangeHistoryList(vo);
            if (userTransferPOList != null) {
                List<AccountChangeHistoryVO> list = Lists.newArrayList();
                userTransferPOList.forEach(e -> {
                    AccountChangeHistoryVO accountVo = new AccountChangeHistoryVO();
                    BeanUtils.copyProperties(e, accountVo);
                    accountVo.setAfterTransfer(null==e.getAfterTransfer()? new BigDecimal(0) : new BigDecimal(e.getAfterTransfer()).divide(new BigDecimal(100), 2, RoundingMode.FLOOR));
                    accountVo.setBeforeTransfer(null==e.getBeforeTransfer()? new BigDecimal(0) : new BigDecimal(e.getBeforeTransfer()).divide(new BigDecimal(100), 2, RoundingMode.FLOOR));
                    accountVo.setCurrentBalance(null==e.getAfterTransfer()? new BigDecimal(0) :new BigDecimal(e.getCurrentBalance()).divide(new BigDecimal(100), 2, RoundingMode.FLOOR));
                    accountVo.setChangeAmount(null==e.getAfterTransfer()? new BigDecimal(0) : BigDecimal.valueOf(e.getChangeAmount()).divide(new BigDecimal(100), 2, RoundingMode.FLOOR));
                    Integer bizType = accountVo.getBizType();
                    //处理手动加扣款ip
                    if(StringUtils.isNotEmpty(e.getRemark()) && e.getRemark().contains("手动加扣款操作人IP:")){
                        accountVo.setIpAddress(e.getRemark().substring(e.getRemark().indexOf("IP:")+3).trim());
                    }
                    if (language.equalsIgnoreCase(Constant.LANGUAGE_ENGLISH)) {
                        if (bizType == 1) {
                            accountVo.setRemark("Transfer In");
                        } else if (bizType == 2) {
                            accountVo.setRemark("Transfer OUT");
                        } else if (bizType == 3) {
                            accountVo.setRemark("Bet");
                        } else if (bizType == 4) {
                            accountVo.setRemark("Settle");
                        } else if (bizType == 7) {
                            accountVo.setRemark("Promotion");
                        } else if (bizType == 8) {
                            accountVo.setRemark("Refused");
                        } else if (bizType == 9) {
                            accountVo.setRemark("SettleRollback");
                        } else if (bizType == 10) {
                            accountVo.setRemark("BetCancel");
                        } else if (bizType == 11) {
                            accountVo.setRemark("BetCancelRollback");
                        }
                    }
                    list.add(accountVo);
                });
                param.put("list", list);
            }
        }
        param.put("pageNum", pageNum);
        param.put("pageSize", pageSize);
        param.put("total", totCnt);
        return param;
    }
}
