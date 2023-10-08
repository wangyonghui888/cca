package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.SLanguagePO;

import java.util.List;

public interface ISLanguageService extends IService<SLanguagePO> {

    List<SLanguagePO> getLangList(List<Long> nameCodes);
}
