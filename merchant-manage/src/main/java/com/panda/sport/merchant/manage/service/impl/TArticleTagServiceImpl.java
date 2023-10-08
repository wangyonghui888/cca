package com.panda.sport.merchant.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.match.mapper.TArticleTagMapper;
import com.panda.sport.merchant.common.po.bss.TArticle;
import com.panda.sport.merchant.common.po.bss.TArticleTag;
import com.panda.sport.merchant.manage.service.ITArticleService;
import com.panda.sport.merchant.manage.service.ITArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 赛事文章标签表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
@Service
public class TArticleTagServiceImpl extends ServiceImpl<TArticleTagMapper, TArticleTag> implements ITArticleTagService {
    @Autowired
    private ITArticleService itArticleService;


    @Transactional
    @Override
    public void editTag(TArticleTag articleTag) {
        boolean flag = updateById(articleTag);
        if(flag) {
            UpdateWrapper<TArticle> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("tag_id",articleTag.getId());
            updateWrapper.set("tag_name",articleTag.getTagName());
            updateWrapper.set("tag_color",articleTag.getTagColor());
            itArticleService.update(updateWrapper);
        }
    }
}
