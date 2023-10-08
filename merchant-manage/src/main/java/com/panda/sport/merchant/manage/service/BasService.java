package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.po.merchant.BasePO;
import com.panda.sport.merchant.common.vo.merchant.BaseVO;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author : Jeffrey
 * @Date: 2020-01-27 17:21
 * @Description :
 */
public interface BasService {
    /**
    * @Description: 修改/新增方法 统一参数设置
    * @Author: Jeffrey
    * @Date: 2020/1/27
    */
    default void convertVOByPO(BaseVO baseVO, BasePO tempPo){
        BeanUtils.copyProperties(baseVO,tempPo);
        tempPo.setCreateTime(System.currentTimeMillis());
        tempPo.setCreateUser(baseVO.getModifyUser());
        tempPo.setModifyUser(baseVO.getModifyUser());
        tempPo.setModifyTime(System.currentTimeMillis());
    }
}
