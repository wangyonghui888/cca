package com.panda.sport.merchant.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.match.mapper.TArticleAuthorMapper;
import com.panda.sport.merchant.common.po.bss.TArticle;
import com.panda.sport.merchant.common.po.bss.TArticleAuthor;
import com.panda.sport.merchant.manage.service.ITArticleAuthorService;
import com.panda.sport.merchant.manage.service.ITArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 赛事文章作者表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
@Service
public class TArticleAuthorServiceImpl extends ServiceImpl<TArticleAuthorMapper, TArticleAuthor> implements ITArticleAuthorService {
    @Autowired
    private ITArticleService itArticleService;

    @Transactional
    @Override
    public void editAuthor(TArticleAuthor articleAuthor) {
        boolean flag = updateById(articleAuthor);
        if(flag) {
            UpdateWrapper<TArticle> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("author_id",articleAuthor.getId());
            updateWrapper.set("author_name",articleAuthor.getAuthorName());
            itArticleService.update(updateWrapper);
        }
    }
}
