package com.panda.sport.merchant.api.controller;


import com.panda.sport.merchant.api.service.ActivityService;
import com.panda.sport.merchant.common.po.bss.AcBonusPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avtivity")
@Slf4j
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping(value = "/upsertUserBonus")
    public void upsertUserBonus(@RequestParam(value = "merchantCode") String merchantCode, @RequestBody List<AcBonusPO> bonusList) {
        log.info(merchantCode + "/api/avtivity/upsertUserBonus:" + bonusList.size());
        activityService.updateUserBonus(bonusList);
    }

    @GetMapping(value = "/clearTicketsOfTask")
    public void clearTicketsOfTask(@RequestParam(value = "merchantCode") String merchantCode) {
        log.info(merchantCode + "/api/avtivity/clearTicketsOfTask:" + merchantCode);
        activityService.clearTicketsOfTask(merchantCode);
    }

    @GetMapping(value = "/clearTicketsOfMardigraTask")
    public void clearTicketsOfMardigraTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "conditionId") Integer conditionId) {
        log.info(merchantCode + "/api/avtivity/clearTicketsOfMardigraTask:" + merchantCode);
        activityService.clearTicketsOfMardigraTask(merchantCode, conditionId);
    }

    @GetMapping("/executeSumTask")
    public void executeSumTask(@RequestParam(value = "merchantCode") String merchantCode){
        log.info(merchantCode + "/api/avtivity/executeSumTask:" + merchantCode);
        activityService.executeSumTask();
    }

    @GetMapping("/executeDailyTask")
    public void executeDailyTask(@RequestParam(value = "merchantCode") String merchantCode,@RequestParam(value = "dateStr") String dateStr){
        log.info( merchantCode + "/api/avtivity/executeDailyTask,merchantCode:{},dateStr:{}" ,merchantCode, dateStr);
        activityService.executeDailyTask(dateStr);
    }

}
