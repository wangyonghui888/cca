package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.vo.MerchantChatRoomSwitchVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantChatRoomSwitchMapper {

    int updateChatRoomSwitch(MerchantChatRoomSwitchVO chatRoomSwitchVO);

    MerchantChatRoomSwitchVO queryMerchantChatRoomSwitch(@Param(value = "merchantCode")String merchantCode);

    int updateMerchantChat(MerchantChatRoomSwitchVO chatRoomSwitchVO);
}
