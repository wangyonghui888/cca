package com.panda.sport.merchant.api.service.impl;


import com.panda.sport.merchant.api.util.ExecutorInstance;
import com.panda.sport.merchant.common.utils.HttpConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author :  alic
 * @version v11.25
 * @Description : 小飞机业务处理接口 实现层
 * @Date: 2020-11-14 13:21
 */
@Slf4j
@RefreshScope
@Service("telegramService")
public class TelegramService {

    private static final String telegramUrl = "https://api.telegram.org";

    private static final String chatId = "chat_id";

    private static final String text = "text";

    private static final String bot = "/bot";

    private static final String sendMessage = "/sendMessage";

    @Value("${telegram.token:0}")
    private String tokenTelegram;

    @Value("${telegram.userName:0}")
    private String userName;

    @Value("${telegram.group.id:0}")
    private String groupId;

    @Value("${telegram.switch:false}")
    private boolean useFlag;


    public void sendTelegram(StringBuilder content) {
        if (!useFlag) {
            return;
        }
        ExecutorInstance.executorService.submit(() -> {
            sendTelegramMessage("merchant-api", content.toString());
        });
    }

    /**
     * 小飞机消息发送
     *
     * @param moduleName  模块名称
     * @param sendContent 发送内容
     * @return void
     * @author alic
     * @date 22020-11-14
     * @version v11.25
     */
    public void sendTelegramMessage(String moduleName, String sendContent) {
        if (StringUtils.isEmpty(moduleName) || StringUtils.isEmpty(sendContent)) {
            return;
        }
        log.error(":::::TelegramSendWarningMessage::::模块名称:{}::::发送预警内容:{}", moduleName, sendContent);
        String sendMsg = this.toStringForParams("模块名称:", moduleName, "\n",
                "预警内容:", sendContent);
        telegramMessage(sendMsg);
    }

    public String toStringForParams(Object... params) {
        StringBuilder sb = new StringBuilder();
        if (params.length > 0) {
            for (Object param : params) {
                sb.append(param);
            }
        }
        return sb.toString();
    }

    /**
     * 小飞机消息发送
     *
     * @param msg 具体发送消息
     * @return void
     * @author alic
     * @date 22020-11-14
     * @version v11.25
     */
    private void telegramMessage(String msg) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add(chatId, Long.parseLong(groupId));
        map.add(text, msg);
        try {
            ResponseEntity<String> response = HttpConnectionPool.restTemplate.postForEntity(telegramUrl + bot + tokenTelegram + sendMessage, map, String.class);
            log.info("telegram调用返回结果:" + response);
            // webClient.post().uri(bot + tokenTelegram + sendMessage).syncBody(map).retrieve().bodyToMono(JSON.class).block();
        } catch (Exception e) {
            log.error("小飞机发送失败!" + msg, e);
        }
    }

}
