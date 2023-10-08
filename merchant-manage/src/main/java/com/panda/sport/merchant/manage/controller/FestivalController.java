package com.panda.sport.merchant.manage.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.panda.sport.merchant.common.po.bss.FestivalResourceConfig;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.IFestivalResourceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author baylee
 * @version 1.0
 * @date 2022/1/29 14:10:47
 */
@RestController
@RequestMapping("/manage/festival")
@Slf4j
public class FestivalController {

    @Resource
    private IFestivalResourceConfigService festivalResourceConfigService;

    @GetMapping("/selectList")
    public Response<?> selectList() {
        return Response.returnSuccess(festivalResourceConfigService.list());
    }

    @PostMapping("/save")
    public Response<?> save(@RequestBody FestivalResourceConfig festival) {
        long currentTimeMillis = System.currentTimeMillis();
        festival.setModifyTime(currentTimeMillis);
        if (festival.getId() == null) {
            festival.setCreateTime(currentTimeMillis);
        }
        return Response.returnSuccess(festivalResourceConfigService.saveOrUpdate(festival));
    }

    @PostMapping("/enable")
    public Response<?> save(@RequestParam(value = "enabled") Integer enabled) {
        UpdateWrapper<FestivalResourceConfig> wrapper = new UpdateWrapper<>();
        wrapper.set("enabled", enabled)
                .set("modify_time", System.currentTimeMillis());
        return Response.returnSuccess(festivalResourceConfigService.update(wrapper));
    }
}
