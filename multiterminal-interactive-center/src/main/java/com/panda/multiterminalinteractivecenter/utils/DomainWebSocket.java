package com.panda.multiterminalinteractivecenter.utils;

import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.entity.WSResponse;
import com.panda.multiterminalinteractivecenter.service.impl.DomainWebSocketServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@ConditionalOnProperty(name = "websocket.17ce.enable", havingValue = "true")
public class DomainWebSocket extends WebSocketClient {


    public DomainWebSocketServiceImpl domainWebSocketService;
    /**
     * 线程安全的Boolean -是否受到消息
     */
    public AtomicBoolean hasMessage = new AtomicBoolean(false);

    /**
     * 线程安全的Boolean -是否已经连接
     */
    private AtomicBoolean hasConnection = new AtomicBoolean(false);

    /**
     * 构造方法
     *
     * @param uri
     */
    public DomainWebSocket(URI uri, DomainWebSocketServiceImpl domainWebSocketService) {
        super(uri);
        this.domainWebSocketService = domainWebSocketService;
        log.info("DomainWebSocket init:" + uri);
    }

    /**
     * 打开连接是方法
     *
     * @param serverHandshake
     */
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("DomainWebSocket onOpen");
    }

    /**
     * 收到消息时
     *
     * @param s
     */
    @Override
    public void onMessage(String s) {
        hasMessage.set(true);
        log.info("收到DomainWebSocket onMessage:" + s);
        try {
            WSResponse wsResponse = JSONObject.parseObject(s, WSResponse.class);
            domainWebSocketService.processWSMessage(wsResponse);
        } catch (Exception e) {
            log.error("DomainWebSocket消息处理失败!!", e);
        }
    }

    /**
     * 当连接关闭时
     *
     * @param i
     * @param s
     * @param b
     */
    @Override
    public void onClose(int i, String s, boolean b) {
        this.hasConnection.set(false);
        this.hasMessage.set(false);
        log.info("DomainWebSocket onClose:" + s);
    }

    /**
     * 发生error时
     *
     * @param e
     */
    @Override
    public void onError(Exception e) {
        log.info("DomainWebSocket onError:" + e);
    }

    @Override
    public void connect() {
        if (!this.hasConnection.get()) {
            super.connect();
            hasConnection.set(true);
        }
    }
}
