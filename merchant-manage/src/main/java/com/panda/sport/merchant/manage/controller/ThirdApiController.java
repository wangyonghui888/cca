package com.panda.sport.merchant.manage.controller;

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.impl.DomainAbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.controller
 * @Description :  TODO
 * @Date: 2022-01-05 11:29:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@RestController
@RequestMapping("/manage/api")
@Slf4j
@Validated
public class ThirdApiController {

    @Autowired()
    @Qualifier("DjDomainServiceImpl")
    DomainAbstractService djDomainAbstractService;

    @GetMapping("/getMerchantInfo")
    public Response getMerchantGroup(HttpServletRequest request, @RequestParam(value = "account",required = false) String account,@RequestParam(value = "code",required = false) String code) {
        log.info("第三方获取商户组 /getMerchantGroup param = {}", account);
        return djDomainAbstractService.getMerchantGroupInfoByThirdCode(code, account);
    }
}
