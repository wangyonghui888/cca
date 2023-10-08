package com.panda.center.controller;

import com.panda.center.entity.AcTask;
import com.panda.center.entity.Games;
import com.panda.center.service.IAcTaskService;
import com.panda.center.service.IGamesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/24 12:20:54
 */
@RequestMapping("/test")
@RestController
public class TestController {
    @Resource
    private IAcTaskService acTaskService;
    @Resource
    private IGamesService gamesService;

    @GetMapping("/start")
    public void start() {
        List<AcTask> list = acTaskService.list();
        List<Games> games = gamesService.list();
    }
}
