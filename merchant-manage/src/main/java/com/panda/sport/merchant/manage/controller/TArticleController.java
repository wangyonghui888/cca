package com.panda.sport.merchant.manage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.dto.*;
import com.panda.sport.merchant.common.dto.TArticleVO;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.SportEnum;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.utils.BeanCopierUtils;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.*;
import com.panda.sport.merchant.manage.util.RedisConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/manage/article")
public class TArticleController {
    private static final String TAB = "文章管理->";
    private static final Pattern SEARCH_PARAM_PATTERN = Pattern.compile("^\\d+$");

    @Autowired
    private ITArticleService tArticleService;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private IMatchInfoCurService matchInfoCurService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ISArticleService isArticleService;

    @Autowired
    private ITArticleTagService itArticleTagService;

    @Autowired
    private ITArticleStatisticsService itArticleStatisticsService;

    /**
     * 文章管理-文章列表
     *
     * @param articleParams params
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @PostMapping("/list")
    public Response<?> articleList(@RequestBody TArticleParams articleParams) {
        log.info("{}文章列表 | req={}",TAB, JSON.toJSONString(articleParams));
        QueryWrapper<TArticle> wrapper = new QueryWrapper<>();
        wrapper.select("id", "article_tittle", "factor", "category_name", "author_name","update_time","is_show","match_id");
        Integer id = articleParams.getArticleId();
        if (id != null) {
            wrapper.eq("id", id);
        }
        String articleTittle = articleParams.getArticleTittle();
        if (StringUtils.isNotBlank(articleTittle)) {
            wrapper.like("article_tittle", articleTittle);
        }
        if(ObjectUtils.isNotEmpty(articleParams.getAuthorName())) {
            wrapper.like("author_name",articleParams.getAuthorName());
        }
        if(ObjectUtils.isNotEmpty(articleParams.getCategoryId())) {
            wrapper.eq("category_id",articleParams.getCategoryId());
        }
        if(ObjectUtils.isNotEmpty(articleParams.getIsHaveMatch())) {
            Integer isHaveMatch = articleParams.getIsHaveMatch();
            if(isHaveMatch == 0) {
                wrapper.isNull("match_id");
            }else {
                wrapper.isNotNull("match_id");
            }
        }
        if(ObjectUtils.isNotEmpty(articleParams.getIsShow())) {
            wrapper.eq("is_show",articleParams.getIsShow());
        }
        wrapper.orderByDesc("create_time");
        PageHelper.startPage(articleParams.getPageNum(), articleParams.getPageSize(),true);
        List<TArticle> list = tArticleService.list(wrapper);
        PageInfo<?> pageInfo = new PageInfo<>(list);
        List resp = list.stream().map(a -> {
            TArticleVO ArticleVO = new TArticleVO();
            BeanCopierUtils.copyProperties(a, ArticleVO);
            if (ObjectUtils.isNotEmpty(a.getMatchId())) {
                ArticleVO.setIsHaveMatch(1);
            } else {
                ArticleVO.setIsHaveMatch(0);
            }
            return ArticleVO;
        }).collect(Collectors.toList());
        pageInfo.setList(resp);
        return Response.returnSuccess(pageInfo);
    }

    /**
     * 文章管理-文章详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public Response<TArticleVO> articleDetail(@RequestParam(name = "id") Long id) {
        log.info("{}文章详情 | id={}",TAB,id);
        if(null == id) {
            Response.returnFail("缺少参数");
        }
        QueryWrapper<TArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        TArticle article = tArticleService.getOne(wrapper);
        TArticleVO vo = new TArticleVO();
        BeanUtils.copyProperties(article,vo);
        TArticleStatistics statistics = itArticleStatisticsService.getById(article.getId());
        if(null != statistics) {
            vo.setReadCounts(statistics.getRealReadCounts() * article.getFactor());
        }
        return Response.returnSuccess(vo);
    }

    /**
     * 获取所有球种
     * @return
     */
    @GetMapping("/sprots")
    public Response<?> articleSportType() {
        SportEnum[] values = SportEnum.values();
        List<JSONObject> sportTypes = Arrays.stream(values).sorted(Comparator.comparingInt(o -> Integer.valueOf(o.getKey().toString()))).map(sportEnum -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sportId", sportEnum.getKey());
            jsonObject.put("sportName", sportEnum.getValue());
            return jsonObject;
        }).collect(Collectors.toList());
        return Response.returnSuccess(sportTypes);
    }

    @PostMapping("/add")
    public Response articleAdd(HttpServletRequest request, @RequestBody @Valid TArticleReq req) {
        //不打印内容，减少日志
        String content = req.getArticleContent();
        req.setArticleContent(null);
        log.info("{}新增文章 | req={}",TAB,JSON.toJSONString(req));
        if(req.getFactor() < 1) {
            return Response.returnFail("权重必须是大于0的整数！");
        }
        try {
            req.setArticleContent(content);
            TArticle article = new TArticle();
            BeanUtils.copyProperties(req,article);
            long curTime = System.currentTimeMillis();
            if(article.getIsShow() == 1) {
                article.setOnlineTime(null);
                article.setShowTime(curTime);
                //如果是普通置顶
                if(article.getIsTop() == 1) {
                    article.setSortPriority(1);
                }
                //如果是永久置顶
                if(article.getPermanentTop() == 1) {
                    article.setSortPriority(2);
                }
            }
            if(article.getIsTop() == 1) {
                article.setTopStartTime(null);
            }
            article.setUpdateTime(curTime);
            article.setCreateTime(curTime);
            tryInitId();
            Long id = redisTemplate.opsForValue().increment(RedisConstants.MATCH_ARTICLE_ID);
            article.setId(id);
            tArticleService.insert(article);
            /**
             *  添加系统日志
             * */
            String username = request.getHeader("merchantName");
            String userId = request.getHeader("user-id");
            String ip = IPUtils.getIpAddr(request);
            MerchantUtil util = new MerchantUtil();
            MerchantLogFiledVO vo1 = util.compareObject(null,req,MerchantUtil.filterArticelAddNames,MerchantUtil.FIELD_ARTICLE_MAPPING);
            merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.ARTICLE_ADD.getCode(),  MerchantLogTypeEnum.ARTICLE_ADD.getRemark()
                    , MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_74.getRemark(),
                    MerchantLogTypeEnum.ARTICLE_ADD.getPageCode().get(0), userId, null, "活动入口",
                    vo1.getFieldName(), vo1.getBeforeValues(), vo1.getAfterValues(),username,userId,ip);

            return Response.returnSuccess();
        }catch (Exception e) {
            log.error("{}新增文章异常 | e={}",TAB,e);
        }
        return Response.returnFail("新增文章异常");
    }

    @PostMapping("/update")
    public Response articleUpdate(HttpServletRequest request,@RequestBody @Valid TArticleReq req) {
        //不打印内容，减少日志
        String content = req.getArticleContent();
        req.setArticleContent(null);
        log.info("{}编辑文章 | req={}",TAB,JSON.toJSONString(req));
        if(req.getFactor() < 1) {
            return Response.returnFail("权重必须是大于0的整数！");
        }
        try {
            req.setArticleContent(content);
            TArticle article = new TArticle();
            BeanUtils.copyProperties(req,article);
            long curTime = System.currentTimeMillis();
            if(article.getIsShow() == 1) {
                article.setOnlineTime(null);
                article.setShowTime(curTime);
                article.setSortPriority(0);
                //如果是普通置顶
                if(article.getIsTop() == 1) {
                    article.setSortPriority(1);
                }
                //如果是永久置顶
                if(article.getPermanentTop() == 1) {
                    article.setSortPriority(2);
                }
            }
            if(article.getIsTop() == 1) {
                article.setTopStartTime(null);
            }
            QueryWrapper<TArticle> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",article.getId());
            TArticle oldArticle = tArticleService.getOne(queryWrapper);
            article.setUpdateTime(curTime);

            TArticleVO vo = new TArticleVO();
            TArticleVO newVo = new TArticleVO();
            BeanUtils.copyProperties(oldArticle,vo);
            BeanUtils.copyProperties(req,newVo);
            TArticleStatistics statistics = itArticleStatisticsService.getById(article.getId());
            if(null != statistics) {
                vo.setReadCounts(statistics.getRealReadCounts() * article.getFactor());
            }
            tArticleService.edit(article,oldArticle);

            /**
             *  添加系统日志
             * */
            String username = request.getHeader("merchantName");
            String userId = request.getHeader("user-id");
            String ip = IPUtils.getIpAddr(request);
            MerchantUtil util = new MerchantUtil();
            MerchantLogFiledVO vo1 = util.compareObject(vo,newVo,MerchantUtil.filterArticelAddNames,MerchantUtil.FIELD_ARTICLE_MAPPING);
            merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.ARTICLE_EDIT.getCode(),  MerchantLogTypeEnum.ARTICLE_EDIT.getRemark()
                    , MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_74.getRemark(),
                    MerchantLogTypeEnum.ARTICLE_EDIT.getPageCode().get(0), userId, null, article.getId()+"",
                    vo1.getFieldName(), vo1.getBeforeValues(), vo1.getAfterValues(),username,userId,ip);
            return Response.returnSuccess();
        }catch (Exception e) {
            log.error("{}编辑文章异常 | e=",TAB,e);
        }
        return Response.returnFail("编辑文章异常");
    }

    @GetMapping("/show")
    public Response articleShow(HttpServletRequest request,@RequestParam(name="ids") String ids) {
        log.info("{}上线文章 | ids={}",TAB,ids);
        if(ObjectUtils.isEmpty(ids)) {
            return Response.returnFail("请选择要上线的文章");
        }
        String[] idsArr = ids.split("[;|；]");
        List<Long> idList = Arrays.stream(idsArr).map(Long::valueOf).collect(Collectors.toList());
        tArticleService.showBatch(request,idList);
        return Response.returnSuccess();
    }

    @GetMapping("/hide")
    public Response articleHide(HttpServletRequest request,@RequestParam(name="ids") String ids) {
        log.info("{}上线下线 | ids={}",TAB,ids);
        if(ObjectUtils.isEmpty(ids)) {
            return Response.returnFail("请选择要下线的文章");
        }
        String[] idsArr = ids.split("[;|；]");
        List<Long> idList = Arrays.stream(idsArr).map(Long::valueOf).collect(Collectors.toList());
        tArticleService.offline(request,idList);
        return Response.returnSuccess();
    }

    @GetMapping("/leagues")
    public Response articleLeagues(@RequestParam(name = "sportId") Integer sportId) {
        log.info("{}查询联赛列表 | sportId={}",TAB,sportId);
        List<LeagueDto> leagueDtos = matchInfoCurService.selectLeagues(sportId);
        return Response.returnSuccess(leagueDtos);
    }

    /**
     * 1期暂时不做
     * @param req
     * @return
     */
//    @GetMapping("/plays")
//    public Response articlePlays(@RequestParam(name = "id") Long id) {
//        log.info("{}根据赛事id：{}查询玩法",TAB,id);
//        List<MatchPalyDto> plays = matchInfoCurService.selectPlaysByMatchId(id);
//        return Response.returnSuccess(plays);
//    }

    @PostMapping("/searchMatchs")
    public Response searchMatchs(@RequestBody @Valid SearchDto req) {
        log.info("{}搜索赛事 | req={}",TAB,JSON.toJSONString(req));
        String keyWord = req.getKeyword().trim();
        Matcher matcher = SEARCH_PARAM_PATTERN.matcher(keyWord);
        try {
            List<SearchMatchDto> matchList = null;
            if(matcher.matches()) {
                //根据赛事id查询
                matchList = matchInfoCurService.searchById(req);
            }else{
                matchList = matchInfoCurService.searchByKeyword(req);
            }
            if(ObjectUtils.isNotEmpty(matchList)) {
                //根据nameCode查找本地名称
                Set<Long> nameCodes = new HashSet<>();
                matchList.stream().forEach(m ->{
                    nameCodes.add(m.getAwayNameCode());
                    nameCodes.add(m.getHomeNameCode());
                    nameCodes.add(m.getLeagueNameCode());
                });
                List<SLanguagePO> languages = matchInfoCurService.selectNameByIds(nameCodes);
                Map<Long, SLanguagePO> nameCodeMap = languages.stream().collect(Collectors.toMap(SLanguagePO::getNameCode, Function.identity()));
                matchList.stream().forEach(m ->{
                    SLanguagePO sLanguagePO = nameCodeMap.get(m.getAwayNameCode());
                    if(null != sLanguagePO) {
                        m.setAwayName(sLanguagePO.getZs());
                    }
                    sLanguagePO = nameCodeMap.get(m.getHomeNameCode());
                    if(null != sLanguagePO) {
                        m.setHomeName(sLanguagePO.getZs());
                    }
                    sLanguagePO = nameCodeMap.get(m.getLeagueNameCode());
                    if(null != sLanguagePO) {
                        m.setLeagueName(sLanguagePO.getZs());
                    }
                });
            }
            return Response.returnSuccess(matchList);
        }catch (Exception e) {
            log.error("{}搜索赛事异常 | e=",TAB,e);
        }
        return Response.returnFail("服务器异常");
    }

    private void tryInitId() {
        //为了和t_article表的主键保持一致，主键id由redis来生成
        if(!redisTemplate.hasKey(RedisConstants.MATCH_ARTICLE_ID)) {
            synchronized(this) {
                if(!redisTemplate.hasKey(RedisConstants.MATCH_ARTICLE_ID)) {
                    QueryWrapper<SArticle> queryWrapper = new QueryWrapper<>();
                    queryWrapper.select("id").orderByDesc("id").last("LIMIT 1");
                    SArticle oneArticle = isArticleService.getOne(queryWrapper);
                    if(null == oneArticle) {
                        redisTemplate.opsForValue().set(RedisConstants.MATCH_ARTICLE_ID,"0",30L, TimeUnit.DAYS);
                    }else {
                        redisTemplate.opsForValue().set(RedisConstants.MATCH_ARTICLE_ID,oneArticle.getId().toString(),30L, TimeUnit.DAYS);
                    }
                }
            }
        }
    }

    @Getter
    @Setter
    private static class TArticleParams {
        private Integer articleId;
        private String articleTittle;
        private int pageNum = 1;
        private int pageSize = 20;
        private Integer categoryId;
        private String authorName;
        private Integer isHaveMatch;
        private Integer isShow;
    }

    @Getter
    @Setter
    private static class TArticleReq {

        private Long id;

        /**
         * 赛种ID
         */
        @NotNull(message = "请选择比赛类型")
        private Long sportId;

        /**
         * 赛种名称
         */
        private String sportName;

        /**
         * 赛事ID，如果是冠军则是盘口ID
         */
        private Long matchId;

        /**
         * 赛事详情
         */
        private String matchDetail;

        /**
         * 主队名称
         */
        private String homeName;

        /**
         * 客队名称
         */
        private String awayName;

        /**
         * 玩法ID
         */
        private Long playId;

        /**
         * 联赛ID
         */
        private Long leagueId;

        /**
         * 联赛名称
         */
        private String leagueName;

        /**
         * 联赛排序
         */
        private Long leagueSort;

        /**
         * 栏目ID
         */
        @NotNull(message = "请选择栏目")
        private Long categoryId;

        /**
         * 栏目名称
         */
        private String categoryName;

        /**
         * 作者ID
         */
        @NotNull(message = "请选择作者")
        private Long authorId;

        /**
         * 作者名称
         */
        private String authorName;

        /**
         * 文章ID
         */
        private Integer articleId;

        /**
         * 文章标题
         */
        @NotBlank(message = "文章标题不能为空")
        private String articleTittle;

        /**
         * 封面图，数组，使用;分割
         */
        private String thumbnails;

        /**
         * 摘要
         */
        private String summary;

        /**
         * 展示状态	0：下架	1：上架
         */
        private Integer isShow;

        /**
         * 是否置顶	0：否	1：是
         */
        private Integer isTop;

        /**
         * 是否冠军	0：否	1：是
         */
        private Integer isChampion;

        /**
         * 置顶上线时间
         */
        private Long topStartTime;

        /**
         * 置顶下线时间
         */
        private Long topEndTime;

        /**
         * 上线时间
         */
        private Long onlineTime;

        /**
         * 权重（阅读量=真实点击数*权重）
         */
        @NotNull(message = "权重不能为null")
        private Integer factor;

        /**
         * 永久置顶	0：否	1：是
         */
        @NotNull(message = "永久置顶不能为null")
        private Integer permanentTop;

        /**
         * 标签ID
         */
        private Long tagId;

        /**
         * 标签名称
         */
        private String tagName;

        /**
         * 标签颜色
         */
        private String tagColor;

        /**
         * 文章内容
         */
        @NotBlank(message = "文章内容不能为空")
        private String articleContent;

        /**
         * 创建时间
         */
        private Long createTime;

        /**
         * 更新时间
         */
        private Long updateTime;

    }

}
