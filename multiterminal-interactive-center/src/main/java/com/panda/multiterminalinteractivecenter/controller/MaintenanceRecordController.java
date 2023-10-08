package com.panda.multiterminalinteractivecenter.controller;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.dto.RecordListDTO;
import com.panda.multiterminalinteractivecenter.service.impl.MaintenanceRecordServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping("/maintenanceRecord")
public class MaintenanceRecordController {

    @Resource
    MaintenanceRecordServiceImpl maintenanceRecordService;

    @GetMapping("/queryList")
    @ApiModelProperty(value = "/queryList", notes = "电竞查询维护系统得状态")
    public APIResponse<RecordListDTO> queryList() {
        return APIResponse.returnSuccess(maintenanceRecordService.findServerStatus());
    }

}
