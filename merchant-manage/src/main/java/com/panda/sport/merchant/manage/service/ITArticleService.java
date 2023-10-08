package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.SArticle;
import com.panda.sport.merchant.common.po.bss.TArticle;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 赛事文章表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
public interface ITArticleService extends IService<TArticle> {

    void insert(TArticle article);

    List<TArticle> selectDelay();

    void edit(TArticle article, TArticle oldArticle);

    void showBatch(HttpServletRequest request,List<Long> idList);

    void offline(HttpServletRequest request,List<Long> idList);

    void batchExport(List<TArticle> tArticleList, List<SArticle> sArticleList);
}
