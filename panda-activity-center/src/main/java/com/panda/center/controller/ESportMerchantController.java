package com.panda.center.controller;

import com.panda.center.param.MerchantTreeForm;
import com.panda.center.result.Response;
import com.panda.center.service.IMerchantService;
import com.panda.center.vo.MerchantTree;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/27 15:49:09
 */
@RestController
@RequestMapping("/manage/dj/notice")
public class ESportMerchantController {

    @Resource
    private IMerchantService merchantService;

    @PostMapping("/getMerchantTree")
    public Response<?> getMerchantTree(@RequestBody MerchantTreeForm merchantTreeForm) {
        List<MerchantTree> merchantTree = merchantService.getMerchantTree(merchantTreeForm);
        return Response.returnSuccess(merchantTree);
    }
}
