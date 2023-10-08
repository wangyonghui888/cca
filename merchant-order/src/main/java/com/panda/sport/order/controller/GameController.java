package com.panda.sport.order.controller;

import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.vo.Response;

import com.panda.sport.merchant.manage.service.TournamentService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/order/game")
public class GameController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping(value = "/getSportList")
    public Response getSportList(HttpServletRequest request) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return tournamentService.getSportList(language);
    }

    @GetMapping(value = "/getSportListByFilter")
    public Response getSportListByFilter(HttpServletRequest request,@RequestParam(value = "merchantCode")String merchantCode) {
        return tournamentService.getSportListByFilter(merchantCode);
    }

    @GetMapping(value = "/getLocalCacheInfo")
    public Response getLocalCacheInfo(HttpServletRequest request) {
        return tournamentService.getLocalCacheInfo();
    }

}
