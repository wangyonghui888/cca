package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.TArticleTag;

/**
 * <p>
 * 赛事文章标签表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
public interface ITArticleTagService extends IService<TArticleTag> {

    void editTag(TArticleTag articleTag);
}
