package com.panda.sport.merchant.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.bss.mapper.SLanguageMapper;
import com.panda.sport.merchant.common.po.bss.SLanguagePO;
import com.panda.sport.merchant.manage.service.ISLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auth: YK
 * @Description: 多语言
 * @Date:2020/6/28 17:20
 */
@Service
public class SLanguageServiceImpl extends ServiceImpl<SLanguageMapper, SLanguagePO> implements ISLanguageService {

    @Autowired
    SLanguageMapper sLanguageMapper;

    /**
     * 多语言
     *
     * @param nameCodes
     * @return
     */
    @Override
    public List<SLanguagePO> getLangList(List<Long> nameCodes) {

        return sLanguageMapper.getLanguageInNameCode(nameCodes);
    }
}
