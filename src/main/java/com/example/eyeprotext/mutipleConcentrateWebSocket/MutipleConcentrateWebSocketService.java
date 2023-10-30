package com.example.eyeprotext.mutipleConcentrateWebSocket;

import com.example.eyeprotext.InviteRoomWebSocket.InviteRoomWebSocketService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@Service
@ServerEndpoint("/api/mutipleConcentrateWebSocketService/{sid}")
public class MutipleConcentrateWebSocketService {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。

    private static CopyOnWriteArraySet<MutipleConcentrateWebSocketService> webSocketSet = new CopyOnWriteArraySet<MutipleConcentrateWebSocketService>();


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
        String inviteRoomId = sid.substring(0,36);
        String accountId = sid.substring(44,80);

//            openSendInfo(accountId + " 進來了 " + inviteRoomId + "房間", accountId, inviteRoomId);
            log.info("有新窗口开始监听:" + sid + ",当前在线人数为:" + getOnlineCount());


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
                closeSendInfo( accountId + " 離開了 " + inviteRoomId + " 房間", accountId, inviteRoomId);
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
        String inviteRoomId = sid.substring(0,36);
        String accountId = sid.substring(44,80);

        // inviteRoomId + " 進入專注模式，人數： "+ PersonCount，確認有沒有成員沒進專注模式
        if (message.contains("進入專注模式")) {
            int personCount = Integer.valueOf(message.substring(48));
            int count = 0;
            final boolean[] breakTask = {false};

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    breakTask[0] = true;
                }
            }, 60000);
            List<String> alreadyInMember = new ArrayList<>();
            while (true) {
                for (MutipleConcentrateWebSocketService item : webSocketSet) {
                    if (item.sid.contains(inviteRoomId) && !item.sid.contains(accountId) && !alreadyInMember.contains(item.sid)) {
                        count++;
                        alreadyInMember.add(item.sid);
                        log.info("房號" + accountId + " 有" + count + "位成員已加入");
                    }
                }
                if (count >= personCount) {
                    timer.cancel();
                    for (MutipleConcentrateWebSocketService item : webSocketSet) {
                        try {
                            //这里可以设定只推送给这个sid的，为null则全部推送
                            if (item.sid.contains(inviteRoomId)) {
                                log.info("帳號" + accountId + "，推送内容:" + "所有成員已進入專注模式");
                                item.sendMessage("所有成員已進入專注模式");
                            } else if (item.sid.equals(inviteRoomId + accountId)) {
                                item.sendMessage("所有成員已進入專注模式");
                            }
                        } catch (IOException e) {
                            continue;
                        }
                    }
                    break;
                } else if (breakTask[0]) {
                    timer.cancel();
                    for (MutipleConcentrateWebSocketService item : webSocketSet) {
                        try {
                            //这里可以设定只推送给这个sid的，为null则全部推送
                            if (item.sid.contains(inviteRoomId)) {
                                log.info("帳號" + accountId + "，推送内容:" + "有成員沒有進入專注模式");
                                item.sendMessage("有成員沒有進入專注模式");
                            } else if (item.sid.equals(inviteRoomId + accountId)) {
                                item.sendMessage("有成員沒有進入專注模式");
                            }
                        } catch (IOException e) {
                            continue;
                        }
                    }
                    break;
                }
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

        for (MutipleConcentrateWebSocketService item : webSocketSet) {
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

        for (MutipleConcentrateWebSocketService item : webSocketSet) {
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
        MutipleConcentrateWebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MutipleConcentrateWebSocketService.onlineCount--;
    }

    public static CopyOnWriteArraySet<MutipleConcentrateWebSocketService> getWebSocketSet() {
        return webSocketSet;
    }
}

