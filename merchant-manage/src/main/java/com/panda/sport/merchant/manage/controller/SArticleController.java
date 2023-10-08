package com.panda.sport.merchant.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.SArticle;
import com.panda.sport.merchant.common.po.bss.TArticle;
import com.panda.sport.merchant.common.po.bss.TArticleCategory;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.utils.MerchantUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.ISArticleService;
import com.panda.sport.merchant.manage.service.ITArticleCategoryService;
import com.panda.sport.merchant.manage.service.ITArticleService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/manage/sArticle")
public class SArticleController {

    private final ISArticleService isArticleService;
    private final ITArticleCategoryService articleCategoryService;
    private final ITArticleService itArticleService;

    @Autowired
    private MerchantLogService merchantLogService;

    public SArticleController(ISArticleService isArticleService,
                              ITArticleCategoryService articleCategoryService,
                              ITArticleService itArticleService) {
        this.isArticleService = isArticleService;
        this.articleCategoryService = articleCategoryService;
        this.itArticleService = itArticleService;
    }

    /**
     * 采集管理-文章列表
     *
     * @param articleParams params
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/list")
    public Response<?> articleList(@RequestBody SArticleParams articleParams) {
        QueryWrapper<SArticle> wrapper = new QueryWrapper<>();
        wrapper.select("id", "article_id", "article_tittle", "create_time", "modify_time", "article_status");
        Integer articleId = articleParams.getArticleId();
        if (articleId != null) {
            wrapper.eq("id", articleId);
        }
        String articleTittle = articleParams.getArticleTittle();
        if (StringUtils.isNotBlank(articleTittle)) {
            wrapper.like("article_tittle", articleTittle);
        }
        wrapper.orderByDesc("id");
        PageHelper.startPage(articleParams.getPageNum(), articleParams.getPageSize());
        List<SArticle> list = isArticleService.list(wrapper);
        PageInfo<SArticle> pageInfo = new PageInfo<>(list);
        return Response.returnSuccess(pageInfo);
    }

    /**
     * 采集管理-文章导出栏目下拉选接口
     *
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @GetMapping("/categoryList")
    public Response<?> categoryList() {
        List<TArticleCategory> categoryList =
                articleCategoryService.list(new QueryWrapper<TArticleCategory>().orderByDesc("id"));
        return Response.returnSuccess(categoryList);
    }

    /**
     * 采集管理-导出/批量导出
     *
     * @param batchExportParams batchExportParams
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/batchExport")
    public Response<?> batchExport(HttpServletRequest request, @RequestBody @Valid BatchExportParams batchExportParams) {
        List<Integer> articleIds = batchExportParams.getArticleIds();
        List<String> after = new ArrayList<>();
        if (CollectionUtils.isEmpty(articleIds)) {
            return Response.returnFail("勾选文章错误，请检查！");
        }
        List<SArticle> sArticleList = isArticleService.listByIds(articleIds);
        if (sArticleList.isEmpty()) {
            return Response.returnFail("db中文章数据为空，请联系开发人员！");
        }
        sArticleList = sArticleList.stream().filter(a -> a.getArticleStatus() == 0).collect(Collectors.toList());
        if (sArticleList.isEmpty()) {
            return Response.returnFail("请勾选没有导出过的文章数据！");
        }
        List<TArticle> tArticleList = Lists.newArrayList();
        for (SArticle sArticle : sArticleList) {
            TArticle tArticle = new TArticle();
            tArticle.setId(sArticle.getId());
            tArticle.setCategoryId(batchExportParams.getCategoryId());
            tArticle.setCategoryName(batchExportParams.getCategoryName());
            tArticle.setAuthorId(batchExportParams.getAuthorId());
            tArticle.setAuthorName(batchExportParams.getAuthorName());
            tArticle.setArticleId(sArticle.getArticleId());
            tArticle.setArticleTittle(sArticle.getArticleTittle());
            tArticle.setFactor(batchExportParams.getFactor());
            tArticle.setArticleContent(sArticle.getContent());
            long curTime = System.currentTimeMillis();
            tArticle.setCreateTime(curTime);
            tArticle.setUpdateTime(curTime);
            tArticleList.add(tArticle);
        }
        itArticleService.batchExport(tArticleList,sArticleList);

        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        List<String> names = new ArrayList<>();
        names.add("导出栏目");
        names.add("选择作者");
        names.add("权重");
        after.add(batchExportParams.getCategoryName());
        after.add(batchExportParams.getAuthorName());
        after.add(batchExportParams.getFactor().toString());
        vo.setFieldName(names);
        vo.getBeforeValues().add("-");
        vo.setAfterValues(after);
        merchantLogService.saveLog(MerchantLogPageEnum.MATCH_FILE_MANAGE, MerchantLogTypeEnum.BATCH_EXPORT, vo,
                MerchantLogConstants.MERCHANT_OUT, userId, username, null, username, userId, language , ip);

        return Response.returnSuccess();
    }

    @PostMapping("/batchDel")
    public Response<?> batchDel(HttpServletRequest request,@RequestBody List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Response.returnFail("ids 不能为空");
        }
        isArticleService.removeBatchByIds(ids);

        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("-"));
        vo.setBeforeValues(ids.stream().map(String::valueOf).collect(Collectors.toList()));
        vo.getAfterValues().add("-");
        merchantLogService.saveLog(MerchantLogPageEnum.BOOK_DELETE, MerchantLogTypeEnum.ABNORMAL_IP_DEL, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, username, userId, language , ip);

        return Response.returnSuccess();
    }

    @Getter
    @Setter
    @ToString
    public static class SArticleParams {
        private Integer articleId;
        private String articleTittle;
        private int pageNum = 1;
        private int pageSize = 20;
    }

    @Getter
    @Setter
    @ToString
    public static class BatchExportParams {
        @NotNull(message = "categoryId 不能为 null")
        private Long categoryId;
        @NotBlank(message = "categoryName 不能为空")
        private String categoryName;
        @NotNull(message = "authorId 不能为 null")
        private Long authorId;
        @NotBlank(message = "authorName 不能为空")
        private String authorName;
        @NotNull(message = "factor 不能为空")
        private Integer factor;
        private List<Integer> articleIds;
    }
}
