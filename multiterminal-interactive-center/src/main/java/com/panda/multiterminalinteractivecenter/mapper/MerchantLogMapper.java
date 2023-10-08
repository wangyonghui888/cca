package com.panda.multiterminalinteractivecenter.mapper;

import com.panda.multiterminalinteractivecenter.dto.MerchantLogDTO;
import com.panda.multiterminalinteractivecenter.po.MerchantLogPO;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogQueryVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author :  ifan
 * @Description :  商户日志orm接口
 * @Date: 2022-07-11
 */
@Repository
public interface MerchantLogMapper {

    /**
     * 新增
     * @param merchantLog
     * @return
     */
    int insert(MerchantLogPO merchantLog);

    /**
     * [查询] 分页查询 count
     **/
    int pageListCount(MerchantLogQueryVO queryVO);

    /**
     * [查询] 分页查询
     **/
    List<MerchantLogDTO> pageList(MerchantLogQueryVO queryVO);

}
