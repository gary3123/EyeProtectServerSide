package com.example.eyeprotext.InviteRoomWebSocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@Service
@ServerEndpoint("/api/websocket/{sid}")
public class WebSocketService {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。

    private static CopyOnWriteArraySet<WebSocketService> webSocketSet = new CopyOnWriteArraySet<WebSocketService>();


    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private String sid = "";


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        this.sid = sid;
        addOnlineCount();           //在线数加1
        try {
            String inviteRoomId = sid.substring(0,36);
            String accountId = sid.substring(44,80);
            openSendInfo(accountId + " 進來了 " + inviteRoomId + "房間", accountId, inviteRoomId);
            log.info("有新窗口开始监听:" + sid + ",当前在线人数为:" + getOnlineCount());
        } catch (IOException e) {
            log.error("websocket IO Exception");
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        //断开连接情况下，更新主板占用情况为释放
        log.info("释放的sid为："+sid);
        //这里写你 释放的时候，要处理的业务
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());

        String inviteRoomId = sid.substring(0,36);
        String accountId = sid.substring(44,80);
        try {
            if (sid.contains("HostAT")) {
                closeSendInfo("房主" + accountId + " 離開了 " + inviteRoomId + " 房間", accountId, inviteRoomId);
            } else {
                closeSendInfo(accountId + " 離開了 " + inviteRoomId + " 房間", accountId, inviteRoomId);
            }
        } catch (IOException e) {
            log.error("websocket IO Exception");
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
        //群发消息
        for (WebSocketService item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void openSendInfo(String message, @PathParam("accountId") String accountId, String inviteRoomId) throws IOException {

        for (WebSocketService item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (item.sid.contains(inviteRoomId) && !item.sid.contains(accountId)) {
                    log.info("帳號" + accountId + "，推送内容:" + message);
                    item.sendMessage(message);
                } else if (item.sid.equals(inviteRoomId + accountId)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static void closeSendInfo(String message, @PathParam("accountId") String accountId, String inviteRoomId) throws IOException {

        for (WebSocketService item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (item.sid.contains(inviteRoomId) && !item.sid.contains(accountId)) {
                    log.info("帳號" + accountId + "，推送内容:" + message);
                    item.sendMessage(message);
                } else if (item.sid.equals(inviteRoomId + accountId)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }

    public static CopyOnWriteArraySet<WebSocketService> getWebSocketSet() {
        return webSocketSet;
    }
}
