package com.panda.sport.merchant.manage.service.impl;

import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.MerchantChatRoomSwitchMapper;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.SwitchEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.utils.MerchantUtil;
import com.panda.sport.merchant.common.vo.MerchantChatRoomSwitchVO;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.manage.config.ExecutorInstance;
import com.panda.sport.merchant.manage.feign.MerchantApiClient;
import com.panda.sport.merchant.manage.service.IMerchantChatRoomSwitchService;
import com.panda.sport.merchant.manage.service.ISlotTicketDictService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Slf4j
@Service
@RefreshScope
public class MerchantChatRoomSwitchServiceImpl implements IMerchantChatRoomSwitchService {

    /**
     * 是否默认(0默认 1自定义)
     */
    private final static Integer IS_DEFAULT = 0;

    @Autowired
    private MerchantChatRoomSwitchMapper chatRoomSwitchMapper;

    @Resource
    private MerchantApiClient merchantApiClient;

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private MerchantLogService merchantLogService;
    @Resource
    private ISlotTicketDictService slotTicketDictService;

    @Override
    public int updateChatRoomSwitch(HttpServletRequest request,MerchantChatRoomSwitchVO chatRoomSwitchVO) {
        if(chatRoomSwitchVO.getIsDefault() != null && chatRoomSwitchVO.getIsDefault().equals(IS_DEFAULT)){
            chatRoomSwitchVO.setThreeDayAmount(Double.valueOf(500));
            chatRoomSwitchVO.setSevenDayAmount(Double.valueOf(1500));
        }
        Integer userId = SsoUtil.getUserId(request);
        String username = loginUserService.getLoginUser(userId);
        String ip = IPUtils.getIpAddr(request);
        MerchantChatRoomSwitchVO beforeValue = chatRoomSwitchMapper.queryMerchantChatRoomSwitch(chatRoomSwitchVO.getMerchantCode());
        int num = chatRoomSwitchMapper.updateChatRoomSwitch(chatRoomSwitchVO);

        merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.CHAT_ROOM.getCode(),  MerchantLogTypeEnum.EDIT_INFO.getRemark()
                , MerchantUtil.getModeInfo(chatRoomSwitchVO.getAgentLevel(),request.getHeader("language")),
                MerchantLogTypeEnum.AGGREGATE_RESOURCE_MANAGEMENT.getPageCode().get(0), "", beforeValue.getMerchantName(), beforeValue.getMerchantId(),
                convertSlotMachineLog(beforeValue), convertSlotMachineRoomLog(beforeValue), convertSlotMachineRoomLog(chatRoomSwitchVO),username,userId.toString(),ip);

        if(num > 0){
            this.asyncUpdateMerchantCache(chatRoomSwitchVO.getMerchantCode());
        }
        this.kickOutMerchant(chatRoomSwitchVO.getMerchantCode());
        return num;
    }

    private List<String> convertSlotMachineLog(MerchantChatRoomSwitchVO beforeValue) {
        List<String> list = Lists.newArrayList();
        if (beforeValue == null) {
            list.add("--");
            return list;
        }
        list.add("聊天室");
        list.add("发言最低累计投注额状态");
        list.add("設置");
        list.add("前3天累计投注额");
        list.add("前7天累计投注额");
        return list;
    }

    private List<String> convertSlotMachineRoomLog(MerchantChatRoomSwitchVO beforeValue) {
        List<String> list = Lists.newArrayList();
        if (beforeValue == null) {
            list.add("--");
            return list;
        }
        list.add(SwitchEnum.getInstance(beforeValue.getChatMinBetAmount()).getLabel());
        list.add(beforeValue.getIsDefault() == 0 ? "默认系统设置" : "自定义设置");
        list.add(beforeValue.getThreeDayAmount().toString());
        list.add(beforeValue.getSevenDayAmount().toString());
        return list;
    }

    @Override
    public MerchantChatRoomSwitchVO getChatRoomSwitch(MerchantChatRoomSwitchVO chatRoomSwitchVO) {
        MerchantChatRoomSwitchVO vo = chatRoomSwitchMapper.queryMerchantChatRoomSwitch(chatRoomSwitchVO.getMerchantCode());
        return vo;
    }

    @Override
    public int updateMerchantChatSwitch(MerchantChatRoomSwitchVO chatRoomSwitchVO, Integer userId, String language, String ip) throws Exception{
        MerchantChatRoomSwitchVO chatSwitchVO = chatRoomSwitchMapper.queryMerchantChatRoomSwitch(chatRoomSwitchVO.getMerchantCode());
        if(chatSwitchVO == null){
            throw new Exception("商户code查询对象为空！");
        }
        int num = chatRoomSwitchMapper.updateMerchantChat(chatRoomSwitchVO);
        if(num > 0){
            this.asyncUpdateMerchantCache(chatRoomSwitchVO.getMerchantCode());
            // 记录操作日志 后期添加操作日志功能
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            String beforeOpenVideos = "";
            String afterOpenVideos = "";
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("chatRoomSwitch"));
            if (chatRoomSwitchVO.getChatRoomSwitch() != null) {
                beforeOpenVideos = SwitchEnum.getInstance(chatSwitchVO.getChatRoomSwitch()).getLabel();
            }
            afterOpenVideos = SwitchEnum.getInstance(chatRoomSwitchVO.getChatRoomSwitch()).getLabel();
            vo.getBeforeValues().add(beforeOpenVideos);
            vo.getAfterValues().add(afterOpenVideos);
            String username = loginUserService.getLoginUser(userId);
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_0, MerchantLogTypeEnum.FUNCTION_SWITCH, vo,
                    MerchantLogConstants.MERCHANT_IN, userId.toString(), username, chatSwitchVO.getMerchantCode(), chatSwitchVO.getMerchantName(), chatSwitchVO.getMerchantId(), language, ip);
            log.info("后台用户{}修改商户{},chatRoomSwitch字段{}更新为{}", userId, chatSwitchVO.getMerchantId(), chatSwitchVO.getChatRoomSwitch(), chatRoomSwitchVO.getChatRoomSwitch());
        }
        this.kickOutMerchant(chatRoomSwitchVO.getMerchantCode());
        return num;
    }

    private void asyncUpdateMerchantCache(String merchantCode) {
        ExecutorInstance.executorService.submit(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error("asyncUpdateMerchantCache.sleep=", e);
            }
            merchantApiClient.updateMerchantUserCache(merchantCode);
            log.info("updateMerchantUserCache,更新商户缓存成功");
        });
    }

    private void kickOutMerchant(String merchantCode) {
        try {
            merchantApiClient.kickoutMerchant(merchantCode);
        } catch (Exception e) {
            log.error("踢出商户失败!", e);
        }
    }
}
