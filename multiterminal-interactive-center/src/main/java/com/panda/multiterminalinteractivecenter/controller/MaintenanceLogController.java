package com.panda.multiterminalinteractivecenter.controller;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.service.impl.MaintenanceLogServiceImpl;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceLogPageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.controller
 * @Description :  TODO
 * @Date: 2022-03-11 15:14:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/maintenanceLog")
public class MaintenanceLogController {
    @Autowired
    MaintenanceLogServiceImpl maintenanceServiceLog;
    @PostMapping("/list")
    public APIResponse<?> list(@RequestBody MaintenanceLogPageVo param) {
        return maintenanceServiceLog.pageList(param);
    }

}
