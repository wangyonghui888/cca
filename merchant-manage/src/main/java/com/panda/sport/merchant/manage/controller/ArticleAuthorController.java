package com.panda.sport.merchant.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.TArticleAuthor;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.ITArticleAuthorService;
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
@RequestMapping("/manage/articleAuthor")
public class ArticleAuthorController {

    private final ITArticleAuthorService itArticleAuthorService;

    @Autowired
     private MerchantLogService merchantLogService;

    public ArticleAuthorController(ITArticleAuthorService itArticleAuthorService) {
        this.itArticleAuthorService = itArticleAuthorService;
    }

    /**
     * 采集管理-作者下拉选
     *
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @GetMapping("/authorList")
    public Response<?> authorList() {
        QueryWrapper<TArticleAuthor> wrapper = new QueryWrapper<>();
        wrapper.select("id", "author_name")
                .eq("author_status", 1)
                .orderByDesc("create_time");
        return Response.returnSuccess(itArticleAuthorService.list(wrapper));
    }

    /**
     * 作者管理-作者列表
     *
     * @param authorParams authorParams
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/list")
    public Response<?> list(@RequestBody ArticleAuthorParams authorParams) {
        QueryWrapper<TArticleAuthor> wrapper = new QueryWrapper<>();
        wrapper.select("id", "author_name", "avatar_img", "introduction", "author_status");
        String authorName = authorParams.getAuthorName();
        if (StringUtils.isNotBlank(authorName)) {
            wrapper.eq("author_name", authorName);
        }
        wrapper.orderByDesc("create_time");
        PageHelper.startPage(authorParams.getPageNum(), authorParams.getPageSize());
        List<TArticleAuthor> list = itArticleAuthorService.list(wrapper);
        PageInfo<TArticleAuthor> pageInfo = new PageInfo<>(list);
        return Response.returnSuccess(pageInfo);
    }

    /**
     * 作者管理-新增作者
     *
     * @param authorForm authorForm
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/add")
    public Response<?> addAuthor(HttpServletRequest request, @RequestBody @Validated({AddGroup.class, Default.class}) ArticleAuthorForm authorForm) {
        TArticleAuthor articleAuthor = new TArticleAuthor();
        BeanUtils.copyProperties(authorForm, articleAuthor);
        long curTime = System.currentTimeMillis();
        articleAuthor.setCreateTime(curTime);
        articleAuthor.setUpdateTime(curTime);
        itArticleAuthorService.save(articleAuthor);

        /**
         *  添加系统日志
         * */
        List<String> names = new ArrayList<>();
        List<String> after = new ArrayList<>();
        names.add("昵称");
        names.add("头像");
        names.add("发布文章");
        names.add("简介");
        after.add(null== articleAuthor?"-":articleAuthor.getAuthorName());
        after.add(null== articleAuthor?"-":articleAuthor.getAvatarImg());
        after.add(null== articleAuthor?"-":0==articleAuthor.getAuthorStatus()?"禁用":"启用" );
        after.add(null== articleAuthor?"-":articleAuthor.getIntroduction());
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.setFieldName(names);
        vo.getBeforeValues().add("-");;
        vo.setAfterValues(after);
        merchantLogService.saveLog(MerchantLogPageEnum.AUTHOR_MANAGE, MerchantLogTypeEnum.AUTHOR_ADD, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, "", null==articleAuthor?"-":articleAuthor.getAuthorName(), language , ip);
        return Response.returnSuccess();
    }

    /**
     * 作者管理-编辑作者
     *
     * @param authorForm authorForm
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/edit")
    public Response<?> editAuthor(HttpServletRequest request,@RequestBody @Validated({UpdateGroup.class, Default.class}) ArticleAuthorForm authorForm) {
        TArticleAuthor articleAuthor = new TArticleAuthor();
        TArticleAuthor oldInfo = new TArticleAuthor();
        BeanUtils.copyProperties(authorForm, articleAuthor);
        //查询原来的数据
        itArticleAuthorService.getById(articleAuthor.getId());
        BeanUtils.copyProperties(authorForm, oldInfo);
        long curTime = System.currentTimeMillis();
        articleAuthor.setUpdateTime(curTime);
        itArticleAuthorService.editAuthor(articleAuthor);
        /**
         *  添加系统日志
         * */
        List<String> names = new ArrayList<>();
        List<String> before = new ArrayList<>();
        List<String> after = new ArrayList<>();
        names.add("昵称");
        names.add("头像");
        names.add("发布文章");
        names.add("简介");
        before.add(null== oldInfo?"-":articleAuthor.getAuthorName());
        before.add(null== oldInfo?"-":articleAuthor.getAvatarImg());
        before.add(null== oldInfo?"-":0==articleAuthor.getAuthorStatus()?"禁用":"启用" );
        before.add(null== oldInfo?"-":articleAuthor.getIntroduction());
        after.add(null== articleAuthor?"-":articleAuthor.getAuthorName());
        after.add(null== articleAuthor?"-":articleAuthor.getAvatarImg());
        after.add(null== articleAuthor?"-":0==articleAuthor.getAuthorStatus()?"禁用":"启用" );
        after.add(null== articleAuthor?"-":articleAuthor.getIntroduction());
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.setFieldName(names);
        vo.setBeforeValues(before);
        vo.setAfterValues(after);
        merchantLogService.saveLog(MerchantLogPageEnum.AUTHOR_MANAGE, MerchantLogTypeEnum.EDIT_INFO, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, "", null==articleAuthor?"-":articleAuthor.getAuthorName(), language , ip);
        return Response.returnSuccess();
    }

    /**
     * 作者管理-状态禁用/启用
     *
     * @param authorStatus authorStatus
     * @param id           id
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/changeStatus")
    public Response<?> changeStatus(HttpServletRequest request,@RequestParam(name = "authorStatus") Integer authorStatus,
                                    @RequestParam(name = "id") Long id) {
        if (authorStatus != 0 && authorStatus != 1) {
            return Response.returnFail("参数错误!");
        }
        TArticleAuthor articleAuthor = itArticleAuthorService.getById(id);
        if (articleAuthor == null) {
            return Response.returnFail("查询作者失败!");
        }
        articleAuthor.setAuthorStatus(authorStatus);
        itArticleAuthorService.updateById(articleAuthor);
        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("postArticle"));
        vo.getBeforeValues().add(0==authorStatus ?"开":"关");
        vo.getAfterValues().add(0==authorStatus ?"关":"开");
        merchantLogService.saveLog(MerchantLogPageEnum.AUTHOR_MANAGE, MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                MerchantLogConstants.MERCHANT_OUT, userId, username, null, username, id.toString(), language , ip);
        return Response.returnSuccess();
    }

    @Getter
    @Setter
    @ToString
    public static class ArticleAuthorParams {
        private String authorName;
        private int pageNum = 1;
        private int pageSize = 20;
    }

    @Getter
    @Setter
    @ToString
    public static class ArticleAuthorForm {
        @Null(message = "id 应该为空", groups = AddGroup.class)
        @NotNull(message = "id 不能为空", groups = UpdateGroup.class)
        private Long id;
        @NotBlank(message = "authorName 不能为空")
        private String authorName;
        private String avatarImg;
        private String introduction;
        @NotNull(message = "authorStatus 不能为 null")
        private Integer authorStatus;
        private Long createTime;
        private Long updateTime;
    }
}
