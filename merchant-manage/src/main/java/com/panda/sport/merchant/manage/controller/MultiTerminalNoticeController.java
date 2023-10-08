package com.panda.sport.merchant.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.ESportsNoticeAddReqVO;
import com.panda.sport.merchant.common.vo.ESportsNoticeDelReqVO;
import com.panda.sport.merchant.common.vo.ESportsNoticeEditReqVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.MultiTerminalNoticeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author :  ives
 * @Description :  内部交互多端公告处理类
 * @Date: 2022-03-08 16:58
 */
@Slf4j
@RestController
@RequestMapping("/manage/notice")
public class MultiTerminalNoticeController {

    @Resource
    private MultiTerminalNoticeService multiTerminalNoticeService;

    @PostMapping(value = "/addESportsNotice")
    @ApiOperation(value = "/manage/notice/addESportsNotice", notes = "供电竞部门调用，添加到本地公告信息")
    public Response addESportsNotice(HttpServletRequest request, @RequestBody @Valid ESportsNoticeAddReqVO addReqVO){
        log.info("添加到本地公告信息:param:【{}】", JSONObject.toJSONString(addReqVO));
        return multiTerminalNoticeService.addESportsNotice(addReqVO,IPUtils.getIpAddr(request));
    }

    @PostMapping(value = "/editESportsNotice")
    @ApiOperation(value = "/manage/notice/editESportsNotice", notes = "供电竞部门调用，修改录入本地的公告信息")
    public Response editESportsNotice(@RequestBody @Valid ESportsNoticeEditReqVO editReqVO ,HttpServletRequest  request){
        log.info("修改录入本地的公告信息:param:【{}】", JSONObject.toJSONString(editReqVO));
        return multiTerminalNoticeService.editESportsNotice(editReqVO, IPUtils.getIpAddr(request));
    }

    @PostMapping(value = "/delESportsNotice")
    @ApiOperation(value = "/manage/notice/delESportsNotice", notes = "供电竞部门调用，删除电竞写入的本地公告信息")
    public Response delESportsNotice(@RequestBody @Valid ESportsNoticeDelReqVO delReqVO){
        log.info("删除电竞写入的本地公告信息:param:【{}】", JSONObject.toJSONString(delReqVO));
        return multiTerminalNoticeService.delESportsNotice(delReqVO);
    }

}
