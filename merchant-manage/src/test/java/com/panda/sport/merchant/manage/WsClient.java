//package com.panda.sport.merchant.manage;
//
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//
//public class WsClient extends WebSocketClient {
//    public WsClient(URI serverUri) {
//        super(serverUri);
//    }
//
//    @Override
//    public void onOpen(ServerHandshake serverHandshake) {
//        System.out.println("握手成功");
//    }
//
//    @Override
//    public void onMessage(String s) {
//        System.out.println(System.currentTimeMillis() + ",收到消息:" + s);
//    }
//
//    @Override
//    public void onClose(int i, String s, boolean b) {
//        System.out.println("关闭链接");
//    }
//
//    @Override
//    public void onError(Exception e) {
//        System.out.println("发生错误:" + e);
//    }
//}
