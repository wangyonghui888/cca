package com.panda.sport.merchant.manage.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;

import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.TMerchantKey;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import com.panda.sport.merchant.common.po.merchant.MerchantNotice;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantLigthVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantNewsVO;
import com.panda.sport.merchant.manage.entity.form.MerchantNewsForm;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.impl.MerchantNewsServiceImpl;
import com.panda.sport.merchant.manage.service.impl.MerchantNoticeServiceImpl;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import com.panda.sports.auth.util.SsoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author YK
 * @Description: 我的消息
 * @date 2020/3/12 15:04
 */
@RestController
@RequestMapping(value = "/manage/news")
public class MerchantNewsController {

    @Autowired
    private MerchantNewsServiceImpl merchantNewsService;

    @Autowired
    private MerchantNoticeServiceImpl merchantNoticeService;

    @Autowired
    private MerchantMapper merchantMapper;

    @PostMapping(value = "/list")
    @AuthRequiredPermission("Merchant:Manage:news:list")
    public Response list(
            HttpServletRequest request,
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "pgNum", defaultValue = "1") Integer pgNum,
            @RequestParam(name = "pgSize", defaultValue = "10") Integer pgSize) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        PageHelper.startPage(pgNum, pgSize, true);
        QueryWrapper<MerchantNews> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        queryWrapper.orderByDesc("create_time");
        List<MerchantNews> merchantNewsPoList = merchantNewsService.list(queryWrapper);
        /**
         * 查询总数量
         */
        long count =  merchantNewsService.count(queryWrapper);
        Set<String> merchantCodeList = merchantNewsPoList.stream().map(m -> m.getMerchantCode()).collect(Collectors.toSet());
        Map<String, TMerchantKey> tMerchantKeyMap = new HashMap<>();

        if (merchantCodeList.size() > 0) {
            List<TMerchantKey> tMerchantKeyList = merchantMapper.getKeyInMerchantCode(merchantCodeList);
            tMerchantKeyMap = tMerchantKeyList.stream().collect(Collectors.toMap(TMerchantKey::getMerchantCode, tMerchantKey -> tMerchantKey));
        }

        List<MerchantNewsVO> merchantNewsVOList = new ArrayList<>();
        for (MerchantNews merchantNews : merchantNewsPoList) {
            if (!language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
                String titles = merchantNews.getTitle();
                String temptitle = titles.replaceAll("平台对用户", "Game Developer has set some bet amount limit to user ").replaceAll("设置了特殊限额", "")
                        .replaceAll("平台于", "Game Developer has set some bet amount limit to user since ")
                        .replaceAll("对用户", "").replaceAll("点击", "click here ").replaceAll("查看详情", "to check the details!");
                merchantNews.setTitle(temptitle);
                String content = merchantNews.getContext();
                String tempContent = content.replaceAll("平台对用户", "Game Developer has set some bet amount limit to user ").replaceAll("设置了特殊限额", "")
                        .replaceAll("平台于", "Game Developer has set some bet amount limit since ")
                        .replaceAll("对用户", "to user").replaceAll(",点击", "click here ").replaceAll("查看详情", "to check the details!");
                merchantNews.setTitle(tempContent);
            }
            MerchantNewsVO merchantNewsVO = new MerchantNewsVO();
            BeanUtils.copyProperties(merchantNews, merchantNewsVO);
            if (tMerchantKeyMap.containsKey(merchantNews.getMerchantCode())) {
                merchantNewsVO.setChildren(tMerchantKeyMap.get(merchantNews.getMerchantCode()));
            }
            merchantNewsVOList.add(merchantNewsVO);
        }

        // 输出key
        PageInfo<MerchantNewsVO> pageInfo = new PageInfo<>(merchantNewsVOList);
        pageInfo.setTotal(count);
        return Response.returnSuccess(pageInfo);
    }

    @PostMapping("/add")
    @AuthRequiredPermission("Merchant:Manage:news:add")
    public Response add(@Valid MerchantNewsForm merchantNewsForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(merchantNewsForm)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        MerchantNews merchantNewsPo = new MerchantNews();
        BeanUtils.copyProperties(merchantNewsForm, merchantNewsPo);
        merchantNewsPo.setCreateTime(System.currentTimeMillis());
        merchantNewsPo.setSendTime(System.currentTimeMillis());
        try {
            merchantNewsService.save(merchantNewsPo);
            return Response.returnSuccess();
        } catch (Exception e) {
            return Response.returnFail(e.getMessage());
        }
    }


    /**
     * 查找
     *
     * @return
     */
    @PostMapping(value = "/findById")
    @AuthRequiredPermission("Merchant:Manage:news:detail")
    public Response findById(Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        MerchantNews merchantNewsPo = merchantNewsService.getById(id);

        MerchantNews merchantNewsUpdate = new MerchantNews();
        merchantNewsUpdate.setId(Long.valueOf(id));
        merchantNewsUpdate.setIsRead("1");
        merchantNewsUpdate.setVisitNumber(merchantNewsPo.getVisitNumber() + 1);
        merchantNewsService.updateById(merchantNewsUpdate);

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

    @Autowired
    private MerchantLogService merchantLogService;

    /**
     * 删除
     *
     * @return
     */
    @PostMapping(value = "/delete")
    @AuthRequiredPermission("Merchant:Manage:news:delete")
    public Response delete(HttpServletRequest request, Integer id) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        MerchantNews merchantNewsPo = merchantNewsService.getById(id);
        boolean isDelete = merchantNewsService.removeById(id);
        if (isDelete) {
            //记录日志
            String username = request.getHeader("merchantName");
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add("删除消息");
            vo.getBeforeValues().add(id+"-"+merchantNewsPo.getTitle());
            vo.getAfterValues().add("-");
            merchantLogService.saveLog(MerchantLogPageEnum.MES_IN_MY, MerchantLogTypeEnum.MSG_DELETE, vo, MerchantLogConstants.MERCHANT_IN,
                    SsoUtil.getUserId(request).toString(), username, null, null, id.toString(), language, IPUtils.getIpAddr(request));
            return Response.returnSuccess();
        }
        return Response.returnFail("删除失败");
    }


    /**
     * 获取跑马灯的消息
     *
     * @return
     */
    @PostMapping("/getLightNews")
    public Response getLightNews() {

        // 消息的未读
        QueryWrapper<MerchantNews> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_read", 0);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 10");
        List<MerchantNews> merchantNewsList = merchantNewsService.list(queryWrapper);

        List<MerchantLigthVO> merchantLigthVOList = new ArrayList<>();

        if (merchantNewsList.size() > 0) {
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
        QueryWrapper<MerchantNotice> merchantNoticeQueryWrapper = new QueryWrapper<>();
        merchantNoticeQueryWrapper.eq("status", 1);
        merchantNoticeQueryWrapper.ge("send_time", time2);
        merchantNoticeQueryWrapper.orderByDesc("id");
        List<MerchantNotice> merchantNoticeList = merchantNoticeService.list(merchantNoticeQueryWrapper);

        if (merchantNoticeList.size() > 0) {
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
        return Response.returnSuccess(outMap);
    }
}
