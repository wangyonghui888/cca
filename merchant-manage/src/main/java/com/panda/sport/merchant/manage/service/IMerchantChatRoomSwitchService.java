package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.vo.MerchantChatRoomSwitchVO;

import javax.servlet.http.HttpServletRequest;

public interface IMerchantChatRoomSwitchService {

    int updateChatRoomSwitch(HttpServletRequest request,MerchantChatRoomSwitchVO chatRoomSwitchVO);

    MerchantChatRoomSwitchVO getChatRoomSwitch(MerchantChatRoomSwitchVO chatRoomSwitchVO);

    int updateMerchantChatSwitch(MerchantChatRoomSwitchVO chatRoomSwitchVO, Integer userId, String language, String ip) throws Exception;
}
