package com.weathermonitor.gaodeapi.utils;

import lombok.extern.slf4j.Slf4j;
import netscape.security.UserTarget;
import org.apache.tomcat.websocket.WsRemoteEndpointAsync;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/11/29 21:25
 */
@Component
@Slf4j
@ServerEndpoint("/websocket")
public class WebSocket {
    /**
    * 成员属性
    * 用来存储服务连接对象
    */
    private static Map<String , Session> clientMap = new ConcurrentHashMap<>();

    /**
     * 客户端与服务端连接
     * @param session
     * @author chieun Liang
     */
    @OnOpen
    public void onOpen(Session session){
        /*
        * something to do for open
        * */
        clientMap.put(session.getId(), session);
    }

    /**
     * 客户端与服务端关闭
     * @param session
     * @author chieun Liang
     */
    @OnClose
    public void onClose(Session session){
        /*
         * something to do for close
         * */
        clientMap.remove(session.getId());
    }

    /**
     * 客户端与服务端连接异常
     * @param error
     * @param session

     * @author chieun Liang
     */
    @OnError
    public void onError(Throwable error, Session session){
        /*
         * something to do for error
         * */
        error.printStackTrace();
    }

    /**
     * 客户端向服务端发送消息
     * @param message
     * @param session
     * @throws IOException
     * @author chieun Liang
     */
    @OnMessage
    public void onMsg(Session session, String message) throws IOException{
        /*
         * something to do for onMessage
         * */
        sendAllMessage(message);
    }

    private void sendAllMessage(String message){
        Set<String> sessionIdSet = clientMap.keySet();
        //广播操作发送消息
        for(String sessionId : sessionIdSet){
            Session session = clientMap.get(sessionId);
            //发送消息到客户端
            session.getAsyncRemote().sendText(message);
        }
    }
}
