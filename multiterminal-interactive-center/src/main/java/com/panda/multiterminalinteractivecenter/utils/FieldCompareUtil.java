package com.panda.multiterminalinteractivecenter.utils;

import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;


/**
 * @author Z9-velpro
 */
@Slf4j
public class FieldCompareUtil<T> {

    /**
     *  对象字段比较
     * @param oldBean
     * @param newBean
     * @return
     * @param <T>
     */
    public static  <T> com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO compareObject(T oldBean, T newBean) {
        MerchantFieldUtil<T> fieldUtil = new MerchantFieldUtil<>();
        com.panda.sport.merchant.common.vo.MerchantLogFiledVO filedVO = fieldUtil.compareObject(oldBean, newBean);
        com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO vo = new com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO();
        BeanUtils.copyProperties(filedVO, vo);
        return vo;
    }

}
