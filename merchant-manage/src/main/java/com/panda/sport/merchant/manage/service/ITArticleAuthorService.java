package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.TArticleAuthor;

/**
 * <p>
 * 赛事文章作者表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
public interface ITArticleAuthorService extends IService<TArticleAuthor> {

    void editAuthor(TArticleAuthor articleAuthor);
}
