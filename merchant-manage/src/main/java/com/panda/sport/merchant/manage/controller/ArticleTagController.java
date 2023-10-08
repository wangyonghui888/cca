package com.panda.sport.merchant.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.TArticleTag;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.ITArticleTagService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.validation.AddGroup;
import com.panda.sport.merchant.manage.validation.UpdateGroup;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manage/articleTag")
public class ArticleTagController {

    private final ITArticleTagService itArticleTagService;

    @Autowired
   private MerchantLogService merchantLogService;

    public ArticleTagController(ITArticleTagService itArticleTagService) {
        this.itArticleTagService = itArticleTagService;
    }

    /**
     * 文章管理-标签下拉选
     *
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @GetMapping("/tagList")
    public Response<?> tagList() {
        QueryWrapper<TArticleTag> wrapper = new QueryWrapper<>();
        wrapper.select("id", "tag_name","tag_color").orderByDesc("create_time");
        return Response.returnSuccess(itArticleTagService.list(wrapper));
    }

    @GetMapping("/leagueList")
    public Response<?> leagueList() {
        QueryWrapper<TArticleTag> wrapper = new QueryWrapper<>();
        wrapper.select("distinct league_name").orderByDesc("create_time");
        return Response.returnSuccess(itArticleTagService.list(wrapper));
    }


    /**
     * 标签管理-列表
     *
     * @param tagParams tagParams
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/list")
    public Response<?> tagList(@RequestBody ArticleTagParams tagParams) {
        QueryWrapper<TArticleTag> wrapper = new QueryWrapper<>();
        wrapper.select("id", "league_name", "tag_color", "tag_name");
        String leagueName = tagParams.getLeagueName();
        if (StringUtils.isNotBlank(leagueName)) {
            wrapper.eq("league_name", leagueName);
        }
        String tagName = tagParams.getTagName();
        if (StringUtils.isNotBlank(tagName)) {
            wrapper.like("tag_name", tagName);
        }
        wrapper.orderByDesc("create_time");
        PageHelper.startPage(tagParams.getPageNum(), tagParams.getPageSize());
        List<TArticleTag> list = itArticleTagService.list(wrapper);
        PageInfo<TArticleTag> pageInfo = new PageInfo<>(list);
        return Response.returnSuccess(pageInfo);
    }

    /**
     * 标签管理-新增标签
     *
     * @param articleTagForm articleTagForm
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/add")
    public Response<?> addTag(HttpServletRequest request, @RequestBody @Validated({AddGroup.class, Default.class}) ArticleTagForm articleTagForm) {
        TArticleTag articleTag = new TArticleTag();
        BeanUtils.copyProperties(articleTagForm, articleTag);
        long curTime = System.currentTimeMillis();
        articleTag.setCreateTime(curTime);
        articleTag.setUpdateTime(curTime);
        itArticleTagService.save(articleTag);
        /**
         *  添加系统日志
         * */
        List<String> names = new ArrayList<>();
        List<String> after = new ArrayList<>();
        names.add("联赛");
        names.add("标签底色");
        names.add("标签名称");
        after.add(articleTag.getLeagueName());
        after.add(articleTag.getTagColor());
        after.add(articleTag.getTagName());
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.setFieldName(names);
        vo.getBeforeValues().add("-");
        vo.setAfterValues(after);
        merchantLogService.saveLog(MerchantLogPageEnum.TAG_MANAGE, MerchantLogTypeEnum.ADD_TAG, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, null, articleTag.getTagName(), language , ip);
        return Response.returnSuccess();
    }

    /**
     * 标签管理-编辑标签
     *
     * @param articleTagForm articleTagForm
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/edit")
    public Response<?> editTag(HttpServletRequest request,@RequestBody @Validated({UpdateGroup.class, Default.class}) ArticleTagForm articleTagForm) {
        TArticleTag articleTag = new TArticleTag();
        //查询原来的数据
        TArticleTag oldInfo =itArticleTagService.getById(articleTagForm.getId());

        BeanUtils.copyProperties(articleTagForm, articleTag);
        long curTime = System.currentTimeMillis();
        articleTag.setUpdateTime(curTime);
        itArticleTagService.editTag(articleTag);

        /**
         *  添加系统日志
         * */
        List<String> names = new ArrayList<>();
        List<String> after = new ArrayList<>();
        List<String> before = new ArrayList<>();
        names.add("联赛");
        names.add("标签底色");
        names.add("标签名称");
        before.add(oldInfo.getLeagueName());
        before.add(oldInfo.getTagColor());
        before.add(oldInfo.getTagName());
        after.add(articleTag.getLeagueName());
        after.add(articleTag.getTagColor());
        after.add(articleTag.getTagName());
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.setFieldName(names);
        vo.setBeforeValues(before);
        vo.setAfterValues(after);
        merchantLogService.saveLog(MerchantLogPageEnum.TAG_MANAGE, MerchantLogTypeEnum.EDIT_INFO, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, null, articleTag.getTagName(), language , ip);
        return Response.returnSuccess();
    }

    /**
     * 标签管理-删除标签
     *
     * @param id 主键ID
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/delete")
    public Response<?> deleteTag(HttpServletRequest request,@RequestParam(name = "id") Long id) {
        //查询原来的数据
        TArticleTag oldInfo =itArticleTagService.getById(id);
        itArticleTagService.removeById(id);
        /**
         *  添加系统日志
         * */
        List<String> names = new ArrayList<>();
        List<String> before = new ArrayList<>();
        names.add("联赛");
        names.add("标签底色");
        names.add("标签名称");
        before.add(null ==oldInfo ? "-":oldInfo.getLeagueName());
        before.add(null ==oldInfo ? "-":oldInfo.getTagColor());
        before.add(null ==oldInfo ? "-":oldInfo.getTagName());
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.setFieldName(names);
        vo.setBeforeValues(before);
        vo.getAfterValues().add("-");
        merchantLogService.saveLog(MerchantLogPageEnum.TAG_MANAGE, MerchantLogTypeEnum.DEL_INFO, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, "", null==oldInfo?"":oldInfo.getTagName(), language , ip);
        return Response.returnSuccess();
    }

    @Getter
    @Setter
    @ToString
    public static class ArticleTagParams {
        private String leagueName;
        private String tagName;
        private int pageNum = 1;
        private int pageSize = 20;
    }

    @Getter
    @Setter
    @ToString
    public static class ArticleTagForm {
        @Null(message = "id 应该为空", groups = AddGroup.class)
        @NotNull(message = "id 不能为空", groups = UpdateGroup.class)
        private Long id;
        @NotBlank(message = "leagueName 不能为空")
        private String leagueName;
        @NotBlank(message = "tagColor 不能为空")
        private String tagColor;
        @NotBlank(message = "tagName 不能为空")
        private String tagName;
    }
}
