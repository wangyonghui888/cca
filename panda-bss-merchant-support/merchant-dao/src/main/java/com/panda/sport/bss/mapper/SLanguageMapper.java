package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SLanguagePO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SLanguageMapper extends BaseMapper<SLanguagePO> {


    List<SLanguagePO> getLanguageInNameCode(@Param(value = "nameCode") List<Long> nameCodes);
}
