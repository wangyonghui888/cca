package com.panda.sport.merchant.manage.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.po.bss.TArticleStatistics;
import com.panda.sport.merchant.common.dto.AricleStatisticsDto;
import com.panda.sport.merchant.common.vo.AricleStatisticsVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.ITArticleStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manage/articleStatistics")
public class ArticleStatisticsController {

    private final ITArticleStatisticsService itArticleStatisticsService;

    public ArticleStatisticsController(ITArticleStatisticsService itArticleStatisticsService) {
        this.itArticleStatisticsService = itArticleStatisticsService;
    }

    @PostMapping("/list")
    public Response<?> statisticsList(@RequestBody AricleStatisticsDto req) {
        log.info("ArticleStatisticsController.statisticsList | req={}", JSON.toJSONString(req));
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<AricleStatisticsVO> list = itArticleStatisticsService.statisticsList(req);
        PageInfo<AricleStatisticsVO> pageInfo = new PageInfo<>(list);
        return Response.returnSuccess(pageInfo);
    }

}
