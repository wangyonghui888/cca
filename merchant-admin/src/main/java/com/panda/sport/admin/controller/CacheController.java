package com.panda.sport.admin.controller;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/admin/cache")
public class CacheController {

    @Autowired
    private LocalCacheService localCacheService;

    @GetMapping(value = "/getLocalCacheInfo")
    public Response getLocalCacheInfo(HttpServletRequest request) {
        return localCacheService.getLocalCacheInfo();
    }

    @GetMapping(value = "/invalidateCache")
    public Response invalidateCache(HttpServletRequest request, @RequestParam(value = "key") String key) {
        return localCacheService.invalidateCache(key);
    }

    @GetMapping(value = "/invalidateAll")
    public Response invalidateAll(HttpServletRequest request) {
        return localCacheService.invalidateAll();
    }
}
