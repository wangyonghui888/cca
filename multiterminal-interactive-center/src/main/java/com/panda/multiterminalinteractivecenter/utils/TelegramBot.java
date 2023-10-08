package com.panda.multiterminalinteractivecenter.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 小飞机消息
 */
@Slf4j
@Repository
@RefreshScope
public class TelegramBot extends TelegramLongPollingBot  {
    //机器人token，BotFather获取
    @Value("${bot.bot-token:5670612588:AAEZ_FCD-p1R7-zvDQH-k0nPj2DC9uDYX5I}")
    private String botToken;

    //机器人昵称
    @Value("${bot.user-name:tylab_testbot}")
    private String userName;

    //群id
    @Value("${bot.chat-id:-1001834006811}")
    private String chatId;

    @Autowired
    private MultiterminalConfig config;

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public TelegramBot(){super();}

    public TelegramBot(String userName, String token){
        this.userName = userName;
        this.botToken = token;
    }


    /**
     * 收到消息更新时
     * */
    @Override
    public void onUpdateReceived(Update update) {
        try{
            //检查更新是否有消息
            if(update.hasMessage()){
                Message message = update.getMessage();
                log.info("...Telegram机器人监听到...更新的数据是 {}", JSON.toJSONString(message));
                if(message.hasText()){
                    //收到信息内容
                    String messageText = message.getText().trim();
                    //直接和机器人的聊天
                    if(message.getFrom().getId().toString().equals(message.getChatId().toString())){
//                        msgLogicDealWith(message,messageText);
                        sendReplyMessage(message,"异常告警机器人不支持私聊哈！");
                        //检查消息中是否包含特定文本
                        if(messageText.contains("@Test_")){
                            String dataSourceCode = messageText.replace("@Test_", "");
                            Long modifyTime = System.currentTimeMillis();
                            Map<String,Object> map = new HashMap<>();
                            map.put("linkId",dataSourceCode+"_"+modifyTime);
                            map.put("dataSourceCode",dataSourceCode);
                            map.put("level","2");
                            map.put("message","测试信息");
                            map.put("modifyTime",modifyTime);
                        }
                        return;
                    }
                    //检查消息中是否包含特定文本
                    if(messageText.contains("@"+userName)){
                        sendReplyMessage(message,"群ID: "+message.getChatId());
//                        sendGroupMessage(messageText,String.valueOf(message.getChatId()));
                        return;
                    }
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 发送回复消息
     * @param  resMsg  回复信息内容
     */
    public void sendReplyMessage(Message message,String resMsg) {
        try{
            //返回信息对象
            SendMessage sendMessageRequest = new SendMessage();
            //谁应该收到消息？ 我们收到消息的发件人...
            sendMessageRequest.setChatId(message.getChatId().toString());
            //回复信息ID
            sendMessageRequest.setReplyToMessageId(message.getMessageId());
            //回复信息内容
            sendMessageRequest.setText(resMsg);
            execute(sendMessageRequest);
        }catch (Exception e){
            log.error("multiterminal:sendReplyMessage小飞机回复消息异常error：{}"+e.getMessage()+",Exception:", e);
        }
    }


    /**
     * 发送群消息
     * @param  resMsg  发送信息内容
     */
    public void sendGroupMessage(String resMsg) {
        try{
            //返回信息对象
            SendMessage sendMessageRequest = new SendMessage();
            //收到消息的发件人...
            sendMessageRequest.setChatId(chatId);
            //发送信息内容
            sendMessageRequest.setText(resMsg);
            //execute(sendMessageRequest);
            String url =  "https://api.telegram.org/bot"+botToken+"/sendmessage?chat_id="+chatId;
            httPost(url,resMsg);
        }catch (Exception e){
            log.error("multiterminal:sendGroupMessage小飞机发送消息异常error：{}"+e.getMessage()+",Exception:", e);
        }
    }

    public void httPost(String url,String resMsg) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("text", resMsg);
        try{
            ResponseEntity<String>  response = HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(params, headers), String.class);
            HttpStatus httpStatus = response.getStatusCode();
            if (200 == httpStatus.OK.value()) {
                log.info("multiterminal-httPost:sendGroupMessage小飞机发送消息成功:{}",response);
            }
        } catch (Exception e) {
            log.error("multiterminal-httPost:sendGroupMessage小飞机发送消息请求异常:{},param={}",e.getMessage(), url,e);
        }
    }
}
