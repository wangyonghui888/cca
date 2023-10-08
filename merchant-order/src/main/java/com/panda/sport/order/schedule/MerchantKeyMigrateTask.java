package com.panda.sport.order.schedule;

import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.vo.merchant.MerchantKeyVO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@JobHandler(value = "MerchantKeyMigrateTask")
public class MerchantKeyMigrateTask extends IJobHandler {

    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            List<MerchantKeyVO> list = merchantMapper.selectAllMerchantKey();
            if (CollectionUtils.isNotEmpty(list)){
                for (MerchantKeyVO item : list){
                    item.setEnableTime(StringUtils.isNotEmpty(item.getCreateTime()) ? Long.valueOf(item.getCreateTime()) : System.currentTimeMillis());
                    item.setKeyStatus(Constant.INT_1);
                    item.setCreateTime(StringUtils.isNotEmpty(item.getCreateTime()) ? item.getCreateTime() : String.valueOf(System.currentTimeMillis()));
                    item.setUpdateTime(StringUtils.isNotEmpty(item.getUpdateTime()) ? item.getUpdateTime() : String.valueOf(System.currentTimeMillis()));
                    merchantMapper.addMerchantKey(item);
                }
            }
        }catch (Exception e){
            log.error("MerchantKeyMigrateTask异常！", e);
            return FAIL;
        }
        return SUCCESS;
    }
}
