package com.panda.sport.merchant.manage.controller;

import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.MerchantChatRoomSwitchVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.IMerchantChatRoomSwitchService;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/merchantChatRoomSwitch")
public class MerchantChatRoomSwitchController {

    @Autowired
    private IMerchantChatRoomSwitchService chatRoomSwitchService;

    @PostMapping("/updateChatRoomSwitch")
    public Response<MerchantChatRoomSwitchVO> updateChatRoomSwitch(HttpServletRequest request,@RequestBody MerchantChatRoomSwitchVO chantChatRoomSwitchVO){
        if(StringUtils.isEmpty(chantChatRoomSwitchVO.getMerchantCode())){
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        chatRoomSwitchService.updateChatRoomSwitch(request,chantChatRoomSwitchVO);
        return Response.returnSuccess();
    }

    @PostMapping("/getChatRoomSwitch")
    public Response<MerchantChatRoomSwitchVO> getChatRoomSwitch(@RequestBody MerchantChatRoomSwitchVO chantChatRoomSwitchVO){
        if(StringUtils.isEmpty(chantChatRoomSwitchVO.getMerchantCode())){
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        return Response.returnSuccess(chatRoomSwitchService.getChatRoomSwitch(chantChatRoomSwitchVO));
    }

    @PostMapping("/updateMerchantChatSwitch")
    public Response updateMerchantChatSwitch(HttpServletRequest request, @RequestBody MerchantChatRoomSwitchVO chantChatRoomSwitchVO){
        if(chantChatRoomSwitchVO == null){
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        if(StringUtils.isEmpty(chantChatRoomSwitchVO.getMerchantCode())){
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        if(chantChatRoomSwitchVO.getChatRoomSwitch() == null){
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        try {
            String language = request.getHeader("language");
            chatRoomSwitchService.updateMerchantChatSwitch(chantChatRoomSwitchVO, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
        }catch (Exception e){
            log.error("MerchantChatRoomSwitchController.updateMerchantChatSwitch,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        return Response.returnSuccess();
    }
}
