package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.dto.DomainGroupThresholdDTO;
import com.panda.multiterminalinteractivecenter.dto.LineCarrierDTO;
import com.panda.multiterminalinteractivecenter.entity.DomainGroup;
import com.panda.multiterminalinteractivecenter.entity.LineCarrier;
import com.panda.multiterminalinteractivecenter.vo.LineCarrierVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LineCarrierMapper extends BaseMapper<LineCarrier> {

    int insertLineCarrier(LineCarrier lineCarrier);

    /**
     * 编辑线路商名称
     * @param lineCarrier
     * @return
     */
    int updateLineCarrier(LineCarrier lineCarrier);

    /**
     * 编辑线路商状态
     * @param lineCarrier
     * @return
     */
    int updateLineCarrierStatus(LineCarrier lineCarrier);

    /**
     * 分页查询
     * @param lineCarrier
     * @return
     */
    List<LineCarrier> getLineCarrier(LineCarrier lineCarrier);

    /**
     * 新增线路商校验名称是否重复
     * @param lineCarrier
     * @return
     */
    int selectByName(LineCarrier lineCarrier);

    /**
     * 编辑线路商校验名称是否重复
     * @param lineCarrierName
     * @return
     */
    LineCarrier editCheckName(@Param("lineCarrierName") String lineCarrierName,@Param("tab") String tab);

    /**
     * 单条查询
     * @param id
     * @return
     */
    LineCarrier getLineCarrierById(@Param("id") Long id);

    /**
     * 线路商删除
     * @param id
     * @return
     */
    void delLineCarrierById(@Param("id") Long id);

    /**
     * 列表查询
     * @return
     */
    List<LineCarrier> queryLineCarrierList(@Param("tab") String tab, @Param("name") String name);

    List<LineCarrierVo> getDomainNum(@Param("id") Long id);

    List<DomainGroupThresholdDTO> getDomainGroupByLineId(@Param("param") LineCarrierDTO lineCarrierDTO);

    List<Long> getDomainListByLineId(@Param("id") Long id,@Param("tab") String tab);

    List<DomainGroupThresholdDTO> getUsedDomainByLineId(@Param("param") LineCarrierDTO lineCarrierDTO);

    List<DomainGroupThresholdDTO> getWaitUsedDomainByLineId(@Param("param") LineCarrierDTO lineCarrierDTO);

}
