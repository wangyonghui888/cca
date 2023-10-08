package com.panda.multiterminalinteractivecenter.controller;


import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.DomainArea;
import com.panda.multiterminalinteractivecenter.service.impl.DomainAreaServiceImpl;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/domain/area")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DomainAreaController {

    private final DomainAreaServiceImpl domainAreaService;

    @GetMapping("list")
    public APIResponse<?> list(@RequestParam(value = "page",required = false)Integer page,
                               @RequestParam(value = "size",required = false)Integer size,
                               @RequestParam(value = "name",required = false)String name
                               ) {
        return domainAreaService.pageList(page,size,name);
    }

    @GetMapping("list/simple")
    public APIResponse<?> simpleList(@RequestParam(value = "name",required = false)String name) {
        return domainAreaService.simpleList(name);
    }


    @PostMapping(value = "create")
    public APIResponse<Object> create(HttpServletRequest request, @RequestBody DomainArea domainArea){
        if (StringUtils.isEmpty(domainArea.getName())){
            return APIResponse.returnFail("参数异常！");
        }
        String currentUser = JWTUtil.getUsername(request.getHeader("token"));
        domainArea.setCreateUser(currentUser);
        domainArea.setUpdateUser(currentUser);
        domainAreaService.create(domainArea, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "edit")
    public APIResponse<Object> edit(HttpServletRequest request,@RequestBody DomainArea domainArea){
        if (domainArea.getId() == null){
            return APIResponse.returnFail("参数异常！");
        }
        domainArea.setUpdateUser(JWTUtil.getUsername(request.getHeader("token")));
        domainAreaService.edit(domainArea, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "del")
    public APIResponse<Object> updateMenu(HttpServletRequest request, @RequestBody Map<String,Long> requestMap){
        Long id = requestMap.get("id");
        if (id == null){
            return APIResponse.returnFail("参数异常！");
        }
        domainAreaService.delete(id, request);
        return APIResponse.returnSuccess();
    }

    /**
     * 获取区域数据
     * @return
     */
    @GetMapping("/getDomainAreaList")
    public APIResponse getDomainAreaList() {

        return domainAreaService.getDomainAreaList();
    }
}
