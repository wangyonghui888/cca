package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SOlympicLuckyboxRecords;
import com.panda.sport.merchant.common.vo.activity.LuckyboxRecordsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LuckyboxHisMapper extends BaseMapper<SOlympicLuckyboxRecords> {

    List<LuckyboxRecordsVO> queryLuckyBoxHistory(@Param("parm") LuckyboxRecordsVO vo);
}