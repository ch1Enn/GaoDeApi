package com.weathermonitor.gaodeapi.observer;
import com.weathermonitor.gaodeapi.utils.WeatherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.websocket.*;
import java.util.Map;
import java.util.Set;

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/11/30 21:57
 */
public class WebSocketObserver extends Observer {
    private static final Logger logger = LoggerFactory.getLogger(WeatherUtil.class);
    public WebSocketObserver(String cityCode){
        super(cityCode);
    }

    /**
     * 服务端向客户端发送气象数据
     *
     * @param message
     * @author chieun Liang
     */
    @Override
    public void sendAllMessage(String message, Map<String , Session> clientMap) {
        Set<String> sessionIdSet = clientMap.keySet();
        //广播操作发送消息
        for(String sessionId : sessionIdSet){
            Session session = clientMap.get(sessionId);
            //发送消息到客户端
            if (session != null && session.isOpen()) {
                try {
                    synchronized (session){
                        logger.info("{} 发送给 {} 客户端 数据为 {}",this, session.getId(), message);
                        session.getAsyncRemote().sendText(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
