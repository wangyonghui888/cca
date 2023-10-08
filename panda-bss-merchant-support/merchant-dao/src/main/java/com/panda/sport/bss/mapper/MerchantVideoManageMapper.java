package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.vo.MerchantVideoManageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MerchantVideoManageMapper extends BaseMapper<MerchantVideoManageVo> {

    /**
     * 修改视频流量管控配置
     * @param videoManageVo
     * @return
     */
    int updateMerchantVideoManage (MerchantVideoManageVo videoManageVo);

    /**
     * 查询视频流量管控配置
     * @param merchantCode
     * @return
     */
    MerchantVideoManageVo queryMerchantVideoManage(@Param(value = "merchantCode")String merchantCode);

    /**
     * 列表查询
     * @return
     */
    List<MerchantVideoManageVo> getList(MerchantVideoManageVo videoManageVo);

    /**
     * 批量更新配置
     * @param videoManageVo
     * @return
     */
    int batchUpdateMerchantVideoManage(MerchantVideoManageVo videoManageVo);

    /**
     * 查询全部配置
     * @return
     */
    List<MerchantVideoManageVo> getVideoManageList();
}
