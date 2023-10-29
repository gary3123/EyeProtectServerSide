package com.example.eyeprotext.APNsPushy;

import com.alibaba.fastjson.JSON;
import com.eatthepath.pushy.apns.*;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class APNsPushNotification {
    private static ApnsClient apnsClient = null;
    private static final String p12Password = "";

    /**
     * APNs推送
     *
     * @param deviceToken IOS等终端设备注册后返回的DeviceToken
     * @param topic       这是你的主题，大多数情况是bundleId
     * @param alert       json格式内容-发送的消息体
     * @param badge       app提醒消息圆点数
     * @param time        有效时间
     * @param priority    发送策略 apns-priority 10为立即 5为省电
     * @param pushType    推送方式，主要有alert，background，voip，complication，fileprovider，mdm
     * @return response
     * @throws ExecutionException
     * @throws InterruptedException
     */

    public static PushNotificationResponse<SimpleApnsPushNotification> ApplePush(String deviceToken,
                                                                                 String topic,
                                                                                 String alert,
                                                                                 int badge,
                                                                                 Long time,
                                                                                 DeliveryPriority priority,
                                                                                 PushType pushType) throws ExecutionException, InterruptedException {
        // 有效时间
        Date invalidationTime = new Date(System.currentTimeMillis() + time);
        Instant instant = invalidationTime.toInstant();
        // 构造一个APNs的推送消息实体
        Map<String,Object> body = new HashMap<>();
        body.put("alert",alert);
        body.put("badge",badge);
        body.put("sound","default");
        Map<String,Object> apns = new HashMap<>();
        apns.put("aps",body);
        String payload = JSON.toJSONString(apns);

        SimpleApnsPushNotification msg = new SimpleApnsPushNotification(deviceToken, topic, payload, instant, priority, pushType);
        // 开始推送
        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>> future = getAPNSConnect().sendNotification(msg);
        return future.get();
    }

    public static ApnsClient getAPNSConnect() {

        if (apnsClient == null) {
            try {
                // 四个线程
                EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
                String resourceLocation = null;
                resourceLocation = "src/main/resources/APNsPushNotificationCer/PushNotificationCer.p12";
                File file = ResourceUtils.getFile(resourceLocation);
                if (file.exists()) {
                    log.info("文件存在");
                }
                apnsClient = new ApnsClientBuilder()
                        //APNS生产IP地址
                        .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                        //P12配置文件时注册密码
                        .setClientCredentials(file, p12Password)
                        //用于设置服务器与苹果服务器建立几个链接通道，这里是建立了四个，链接通道并不是越多越好的，具体百度
                        .setConcurrentConnections(3)
                        //的作用是建立几个线程来处理，说白了就是多线程，我这里设置的都是4，相当于16个线程同时处理。
                        .setEventLoopGroup(eventLoopGroup).build();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Get iOS Connect Exception, Please Check:{}", e.getMessage());
            }
        }
        return apnsClient;
    }

    public static void sendIosMsg(String deviceToken, String msgBody, int badge) {

        PushNotificationResponse<SimpleApnsPushNotification> response = null;
        try {
            response = APNsPushNotification.ApplePush(deviceToken, "com.imac.eyesapp", msgBody,badge,60 * 1000L, DeliveryPriority.IMMEDIATE, PushType.ALERT);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Ios Send Msg Fail{}",e.getMessage());
            sendIosMsg(deviceToken, msgBody, badge);
            return;
        }

        log.info("执行结果:{}",response.getRejectionReason());
        // 如果返回的消息中success为true那么成功，否则失败！
        log.info("如果返回的消息中success为true那么成功，否则失败！{}", JSON.toJSONString(response));
    }

}
