package com.panda.sport.admin.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.admin.service.impl.AdminMenuServiceImpl;
import com.panda.sport.admin.utils.MenuUtils;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.MerchantLocalCacheEnum;
import com.panda.sport.merchant.common.enums.NoticeLanguageEnum;
import com.panda.sport.merchant.common.enums.NoticeTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.TMerchantKey;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import com.panda.sport.merchant.common.po.merchant.MerchantNotice;
import com.panda.sport.merchant.common.po.merchant.MerchantNoticeType;
import com.panda.sport.merchant.common.po.merchant.NoticeLangContextPo;
import com.panda.sport.merchant.common.utils.RegexUtils;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.BackendNoticeVo;
import com.panda.sport.merchant.common.vo.merchant.MerchantLigthVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantNewsVO;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.mapper.MerchantNewsMapper;
import com.panda.sport.merchant.mapper.MerchantNoticeMapper;
import com.panda.sport.merchant.mapper.MerchantNoticeTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

/**
 * @auth: YK
 * @Description:公共栏 和 消息栏
 * @Date:2020/5/19 15:01
 */
@RestController
@RequestMapping("/admin/noticeNew")
@Slf4j
public class NoticeNewController {

    @Autowired
    MerchantNoticeMapper merchantNoticeMapper;

    @Autowired
    MerchantNewsMapper merchantNewsMapper;

    @Autowired
    MerchantMapper merchantMapper;

    @Autowired
    MerchantNoticeTypeMapper merchantNoticeTypeMapper;

    @Autowired
    MerchantLogService merchantLogService;

    @Autowired
    AdminMenuServiceImpl adminMenuService;

    static Cache<String,Map<String,Object>> getLightNewsListCache = (Cache<String,Map<String,Object>>) MerchantLocalCacheEnum.getLocalCache(MerchantLocalCacheEnum.MERCHANT_LOCAL_CACHE_GET_LIGHT_NEWS);

    /**
     * 公告栏
     *
     * @param title
     * @param pgNum
     * @param pgSize
     * @return
     */
    @PostMapping("/notice")
    @PreAuthorize("hasAnyRole('bulletin')")

    public Response notice(HttpServletRequest request, @RequestParam(name = "title", defaultValue = "") String title,
                           @RequestParam(name = "mid", defaultValue = "0") Integer mid,
                           @RequestParam(name = "nid", defaultValue = "0") Integer nid,
                           @RequestParam(name = "pgNum", defaultValue = "1") Integer pgNum,
                           @RequestParam(name = "pgSize", defaultValue = "10") Integer pgSize) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        String merchantCode = SecurityUtils.getUser().getMerchantCode();
        String merchantCreditCode = adminMenuService.getMerchantCredit(merchantCode);
        int releaseRange = MenuUtils.getReleaseRange(merchantCreditCode);
        PageHelper.startPage(pgNum, pgSize, true);
        QueryWrapper<MerchantNotice> queryWrapper = new QueryWrapper<>();
        if (mid > 0) {
            queryWrapper.eq("standard_match_id", mid);
        }
        if (nid > 0) {
            queryWrapper.eq("notice_type_id", nid);
        }
        //queryWrapper.like("release_range", releaseRange);
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        queryWrapper.eq("status", 1);
        String prefix = merchantCode + StringPool.UNDERSCORE;
        queryWrapper.and( q -> q.ne("notice_type_id", NoticeTypeEnum.ABNORMAL_EVENT_USER.getCode()).like("release_range", releaseRange)
                .or( q1 -> q1.eq("notice_type_id", NoticeTypeEnum.ABNORMAL_EVENT_USER.getCode()).like("abnormal_user_ids", prefix)));
        queryWrapper.orderByDesc("send_time");
        List<MerchantNotice> merchantNoticeList = merchantNoticeMapper.selectList(queryWrapper);
        for (MerchantNotice merchantNotice : merchantNoticeList) {
            String context = RegexUtils.delImgTag(merchantNotice.getContext());
            if( NoticeTypeEnum.ABNORMAL_EVENT_USER.getCode().equals(merchantNotice.getNoticeTypeId()) && StringUtil.isNotBlank(merchantNotice.getAbnormalUserIds())) {
                //过滤其他商户的异常用户
                List<String> list = Arrays.stream(merchantNotice.getAbnormalUserIds().split(StringPool.COMMA)).filter(s -> s.contains(prefix)).collect(Collectors.toList());
                if(list.size() > 0) {
                    String abnormalUser =  list.stream().map(String::trim).collect(Collectors.joining("\n"));
                    context = context + "\n异常用户：\n" + abnormalUser;
                    merchantNotice.setAbnormalUserIds(abnormalUser);
                }
            }
            merchantNotice.setContext(context);
        }
        PageInfo<MerchantNotice> pageInfo = new PageInfo<>(merchantNoticeList);
        return Response.returnSuccess(pageInfo);
    }

    /**
     * 公告类型
     *
     * @return
     */
    @PostMapping(value = "/noticeType")
    public Response noticeType() {

        List<MerchantNoticeType> merchantNoticePoList = merchantNoticeTypeMapper.selectList(new QueryWrapper<>());
        return Response.returnSuccess(merchantNoticePoList);
    }


    /**
     * 详情
     *
     * @param id
     * @return
     */
    @PostMapping("/noticeDetail")
    public Response noticeDetail(HttpServletRequest request, Integer id) {

        // 判断id空
        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }

        // 查询公告明细
        MerchantNotice merchantNoticePo = merchantNoticeMapper.selectById(id);
        if (StringUtils.isEmpty(merchantNoticePo)) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }

        // 设置访问记录
        MerchantNotice merchantNoticePoUpdate = new MerchantNotice();
        merchantNoticePoUpdate.setId(merchantNoticePo.getId());
        merchantNoticePoUpdate.setVisitNumber(merchantNoticePo.getVisitNumber() + 1);
        merchantNoticePoUpdate.setModifyTime(System.currentTimeMillis());

        // 定义返回的公告视图
        BackendNoticeVo backendNoticeVo = new BackendNoticeVo();
        BeanUtils.copyProperties(merchantNoticePo, backendNoticeVo);

        // 设置商户编码
        if (!StringUtils.isEmpty(merchantNoticePo.getMerchantCodes())) {
            backendNoticeVo.setMerchantCodes(merchantNoticePo.getMerchantCodes().split(","));
        }
        if( NoticeTypeEnum.ABNORMAL_EVENT_USER.getCode().equals(merchantNoticePo.getNoticeTypeId()) && StringUtil.isNotBlank(merchantNoticePo.getAbnormalUserIds())) {
            //过滤其他商户的异常用户
            String merchantCode = SecurityUtils.getUser().getMerchantCode();
            String abnormalUser = Arrays.stream(merchantNoticePo.getAbnormalUserIds().split(StringPool.COMMA))
                    .filter(s -> s.contains(merchantCode + StringPool.UNDERSCORE))
                    .map(String::trim).collect(Collectors.joining("<br/>"));
            if(StringUtil.isNotBlank(abnormalUser)) {
                merchantNoticePo.setContext(merchantNoticePo.getContext().replaceAll("\n", "<br/>") + "<br/>异常用户：<br/>" + abnormalUser);
                merchantNoticePo.setEnContext(merchantNoticePo.getEnContext().replaceAll("\n", "<br/>") + "<br/>Username：<br/>" + abnormalUser);
                backendNoticeVo.setContext(merchantNoticePo.getContext());
                backendNoticeVo.setAbnormalUserIds(abnormalUser);
            }
        }
        // 多语言转换
        backendNoticeVo = getNoticeList(backendNoticeVo,merchantNoticePo);

        try {
            // 更新访问记录
            merchantNoticeMapper.updateById(merchantNoticePoUpdate);

            // 返回视图
            return Response.returnSuccess(backendNoticeVo);
        } catch (Exception e) {
            return Response.returnFail(e.getMessage());
        }
    }

    // 多语言转换为list列表
    public BackendNoticeVo getNoticeList(BackendNoticeVo backendNoticeVo,MerchantNotice merchantNotice) {

        List<NoticeLangContextPo> list = new ArrayList<>();
        for (NoticeLanguageEnum noticeLanguageEnum : NoticeLanguageEnum.values()) {
            NoticeLangContextPo noticeLangContextPo = new NoticeLangContextPo();
            if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.ZS.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.ZS.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getTitle());
                noticeLangContextPo.setContext(merchantNotice.getContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.EN.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.EN.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getEnTitle());
                noticeLangContextPo.setContext(merchantNotice.getEnContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.ZH.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.ZH.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getZhTitle());
                noticeLangContextPo.setContext(merchantNotice.getZhContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.VI.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.VI.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getViTitle());
                noticeLangContextPo.setContext(merchantNotice.getViContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.JP.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.JP.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getJpTitle())?merchantNotice.getEnTitle():merchantNotice.getJpTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getJpContext())?merchantNotice.getEnContext():merchantNotice.getJpContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.TH.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.TH.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getThTitle())?merchantNotice.getEnTitle():merchantNotice.getThTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getThContext())?merchantNotice.getEnContext():merchantNotice.getThContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.MA.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.MA.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getMaTitle())?merchantNotice.getEnTitle():merchantNotice.getMaTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getMaContext())?merchantNotice.getEnContext():merchantNotice.getMaContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.KO.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.KO.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getKoTitle())?merchantNotice.getEnTitle():merchantNotice.getKoTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getKoContext())?merchantNotice.getEnContext():merchantNotice.getKoContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.PT.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.PT.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getPtTitle())?merchantNotice.getEnTitle():merchantNotice.getPtTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getPtContext())?merchantNotice.getEnContext():merchantNotice.getPtContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.ES.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.ES.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getEsTitle())?merchantNotice.getEnTitle():merchantNotice.getEsTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getEsContext())?merchantNotice.getEnContext():merchantNotice.getEsContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.MYA.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.MYA.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getMyaTitle())?merchantNotice.getEnTitle():merchantNotice.getMyaTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getMyaContext())?merchantNotice.getEnContext():merchantNotice.getMyaContext());
            }
            list.add(noticeLangContextPo);
        }
        backendNoticeVo.setList(list);

        // 返回
        return backendNoticeVo;
    }



    /**
     * 消息中心
     *
     * @param title
     * @param pgNum
     * @param pgSize
     * @return
     */
    @PostMapping("/news")
    @PreAuthorize("hasAnyRole('mymessage')")
    public Response news(@RequestParam(name = "title", defaultValue = "") String title,
                         @RequestParam(name = "pgNum", defaultValue = "1") Integer pgNum,
                         @RequestParam(name = "pgSize", defaultValue = "10") Integer pgSize) {

        PageHelper.startPage(pgNum, pgSize, true);
        QueryWrapper<MerchantNews> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }
        queryWrapper.eq("merchant_code", SecurityUtils.getUser().getMerchantCode());
        queryWrapper.orderByDesc("send_time");
        List<MerchantNews> merchantNewsList = merchantNewsMapper.selectList(queryWrapper);
        PageInfo<MerchantNews> pageInfo = new PageInfo<>(merchantNewsList);
        return Response.returnSuccess(pageInfo);
    }


    /**
     * 已读消息
     *
     * @param id
     * @return
     */
    @PostMapping("/newsDetail")
    public Response newsDetail(Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        MerchantNews merchantNewsPo = merchantNewsMapper.selectById(id);

        MerchantNews merchantNewsUpdate = new MerchantNews();
        merchantNewsUpdate.setId(Long.valueOf(id));
        merchantNewsUpdate.setSelfIsRead("1");
        merchantNewsUpdate.setVisitNumber(merchantNewsPo.getVisitNumber() + 1);
        merchantNewsMapper.updateById(merchantNewsUpdate);

        MerchantNewsVO merchantNewsVO = new MerchantNewsVO();
        BeanUtils.copyProperties(merchantNewsPo, merchantNewsVO);

        if (merchantNewsPo.getType().equals(0)) {
            // 商户过期消息
            TMerchantKey tMerchantKey = merchantMapper.getMerchantKeyPo(merchantNewsPo.getMerchantCode());
            tMerchantKey.setMerchantCode(merchantNewsPo.getMerchantCode());
            tMerchantKey.setMerchantName(merchantNewsPo.getMerchantName());
            merchantNewsVO.setChildren(tMerchantKey);
        } else if (merchantNewsPo.getType().equals(1)) {
            // 商户新注册详情
            MerchantPO po = merchantMapper.getMerchantByMerchantCode(merchantNewsPo.getMerchantCode());
            merchantNewsVO.setChildren(po);
        }
        return Response.returnSuccess(merchantNewsVO);
    }

    /**
     * 已读消息
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Response delete(Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        MerchantNews merchantNewsPo = merchantNewsMapper.selectById(id);
        if (merchantNewsPo != null) {
            merchantNewsMapper.deleteById(id);
            //记录日志
            /*JwtUser user = SecurityUtils.getUser();
            merchantLogService.saveLog(MerchantLogPageEnum.MY_MESSAGE, MerchantLogTypeEnum.MSG_DELETE,null,
                    MerchantLogConstants.MERCHANT_OUT,user.getId().longValue(),user.getUsername(),user.getMerchantCode(),user.getMerchantName(),id.toString());*/
            return Response.returnSuccess(true);
        }
        return Response.returnFail("删除失败，请联系管理员！");
    }

    /**
     * 跑马灯
     *
     * @return
     */
    @PostMapping("/getLightNews")
    public Response getLightNews() {
        String merchantCode = SecurityUtils.getUser().getMerchantCode();
        //根据merchantCode作为缓存key
        Map<String,Object> localCacheMaps = getLightNewsListCache.getIfPresent(merchantCode + MerchantLocalCacheEnum.MERCHANT_LOCAL_CACHE_GET_LIGHT_NEWS.getKey());

        if (localCacheMaps != null && !localCacheMaps.isEmpty()) {
            return Response.returnSuccess(localCacheMaps);
        }

        // 消息的未读
        QueryWrapper<MerchantNews> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("merchant_code", merchantCode);
        queryWrapper.eq("self_is_read", 0);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 10");
        List<MerchantNews> merchantNewsList = merchantNewsMapper.selectList(queryWrapper);

        List<MerchantLigthVO> merchantLigthVOList = new ArrayList<>();

        if (merchantNewsList != null && merchantNewsList.size() > 0) {
            merchantNewsList.forEach(news -> {
                MerchantLigthVO merchantLigthVO = new MerchantLigthVO();
                merchantLigthVO.setId(news.getId());
                merchantLigthVO.setTitle(news.getTitle());
                merchantLigthVO.setCreateTime(news.getCreateTime());
                merchantLigthVO.setType(2);
                merchantLigthVOList.add(merchantLigthVO);
            });
        }

        // 三天前公告
        String dateStr = DateUtil.now();
        Date date = DateUtil.parse(dateStr);
        DateTime newDate2 = DateUtil.offsetDay(date, -3);
        Long time2 = newDate2.getTime();

        String merchantCreditCode = adminMenuService.getMerchantCredit(merchantCode);
        int releaseRange = MenuUtils.getReleaseRange(merchantCreditCode);

        QueryWrapper<MerchantNotice> merchantNoticeQueryWrapper = new QueryWrapper<>();
        merchantNoticeQueryWrapper.like("release_range", releaseRange);
        merchantNoticeQueryWrapper.eq("status", 1);
        merchantNoticeQueryWrapper.ge("send_time", time2);
        merchantNoticeQueryWrapper.orderByDesc("id");
        List<MerchantNotice> merchantNoticeList = merchantNoticeMapper.selectList(merchantNoticeQueryWrapper);

        if (merchantNoticeList != null && merchantNoticeList.size() > 0) {
            merchantNoticeList.forEach(notice -> {
                MerchantLigthVO merchantLigthVO = new MerchantLigthVO();
                merchantLigthVO.setCreateTime(notice.getSendTime());
                merchantLigthVO.setTitle(notice.getTitle());
                merchantLigthVO.setId(notice.getId());
                merchantLigthVO.setType(1);
                merchantLigthVOList.add(merchantLigthVO);
            });
        }

        Map<String, Object> outMap = new HashMap<>();
        outMap.put("non", merchantNoticeList.size());
        outMap.put("nen", merchantNewsList.size());
        outMap.put("list", merchantLigthVOList);
        //put缓存数据
        getLightNewsListCache.put(merchantCode + MerchantLocalCacheEnum.MERCHANT_LOCAL_CACHE_GET_LIGHT_NEWS.getKey(),outMap);

        return Response.returnSuccess(outMap);
    }


    /**
     * 获取首页
     *
     * @return
     */
    @PostMapping("/getHomeNews")
    public Response getHomeNews(HttpServletRequest request) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        //消息
        QueryWrapper<MerchantNews> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("self_is_read", 0);
        queryWrapper.eq("merchant_code", SecurityUtils.getUser().getMerchantCode());
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 10");
        List<MerchantNews> merchantNewsList = merchantNewsMapper.selectList(queryWrapper);

        List<MerchantLigthVO> merchantLigtNewVO = new ArrayList<>();
        String finalLanguage = language;
        merchantNewsList.forEach(news -> {
            MerchantLigthVO merchantLigthVO = new MerchantLigthVO();
            BeanUtils.copyProperties(news, merchantLigthVO);
            merchantLigthVO.setType(2);
            merchantLigthVO.setSendTimeStr(getSendTimeStr(news.getSendTime(), finalLanguage));
            merchantLigtNewVO.add(merchantLigthVO);
        });

        String merchantCreditCode = adminMenuService.getMerchantCredit(SecurityUtils.getUser().getMerchantCode());
        //公告
        int releaseRange = MenuUtils.getReleaseRange(merchantCreditCode);
        QueryWrapper<MerchantNotice> merchantNoticeQueryWrapper = new QueryWrapper<>();
        merchantNoticeQueryWrapper.eq("status", 1);
        merchantNoticeQueryWrapper.like("release_range", releaseRange);
        merchantNoticeQueryWrapper.orderByDesc("id");
        merchantNoticeQueryWrapper.last("limit 10");
        List<MerchantNotice> merchantNoticeList = merchantNoticeMapper.selectList(merchantNoticeQueryWrapper);
        List<MerchantLigthVO> merchantLigtNoticeVO = new ArrayList<>();
        String finalLanguage1 = language;
        merchantNoticeList.forEach(notice -> {
            MerchantLigthVO merchantLigthVO = new MerchantLigthVO();
            BeanUtils.copyProperties(notice, merchantLigthVO);
            merchantLigthVO.setHeadType(1);
            merchantLigthVO.setIsUpload(StringUtils.isEmpty(notice.getUploadName()) ? 0 : 1);
            merchantLigthVO.setType(1);
            merchantLigthVO.setSendTimeStr(getSendTimeStr(notice.getSendTime(), finalLanguage1));
            merchantLigtNoticeVO.add(merchantLigthVO);
        });

        Map<String, Object> outMap = new HashMap<String, Object>(2) {{
            put("news", merchantLigtNewVO);
            put("notice", merchantLigtNoticeVO);
        }};

        return Response.returnSuccess(outMap);
    }

    public String getSendTimeStr(Long sendTime, String language) {
        String lastSendStr;
        Date now = new Date();
        long min = (now.getTime() - sendTime) / (1000 * 60);

        if (min <= 1) {
            lastSendStr = language.equals(LANGUAGE_CHINESE_SIMPLIFIED) ? "刚刚" : "Just a moment";
        } else if (min <= 60) {
            lastSendStr = min + (language.equals(LANGUAGE_CHINESE_SIMPLIFIED) ? "分钟" : "min");
        } else if (min <= 60 * 24) {
            lastSendStr = min / 60 + (language.equals(LANGUAGE_CHINESE_SIMPLIFIED) ? "小时" : "hour");
        } else {
            lastSendStr = min / (60 * 24) + (language.equals(LANGUAGE_CHINESE_SIMPLIFIED) ? "天" : "day");
        }
        return lastSendStr;
    }

}
