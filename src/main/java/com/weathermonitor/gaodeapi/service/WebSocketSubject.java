package com.weathermonitor.gaodeapi.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weathermonitor.gaodeapi.controller.WeatherController;
import com.weathermonitor.gaodeapi.observer.Observer;
import com.weathermonitor.gaodeapi.observer.WebSocketObserver;
import com.weathermonitor.gaodeapi.utils.WeatherUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/12/1 13:01
 */
@Component
@Slf4j
@ServerEndpoint("/websocket")
public class WebSocketSubject{
    /**
    * 成员属性
    * 定时器，定时执行访问api任务
    */
    private Timer timer = new Timer();
    /**
    * 成员属性
    * 定时器任务
    */
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {

        }
    };
    //private Boolean timerCancelFlag = false;
    /**
    * 成员属性
    * 城市天气观察者列表
     * 天气数据更新后通过观察者的 sendAllMessage 方法向前台发送消息
    */
    private ArrayList<Observer> observers = new ArrayList<>();

    /**
    * 成员属性
    * 保存前台请求过的城市的编码
    */
    private ArrayList<String> cityCodes = new ArrayList<>();


    private static final Logger logger = LoggerFactory.getLogger(WeatherUtil.class);

    /**
     * 成员属性
     * 用来存储服务连接对象
     */
    private static Map<String , Session> clientMap = new ConcurrentHashMap<>();

    public static Map<String, Session> getClientMap() {
        return clientMap;
    }

    public static void setClientMap(Map<String, Session> clientMap) {
        WebSocketSubject.clientMap = clientMap;
    }

    public void attach(Observer observer) {
        this.observers.add(observer);
    }


    public void detach(Observer observer) {
        this.observers.remove(observer);
    }


    public void notifyObserver(String message, Map<String , Session> clientMap, String cityCode) {
        //订阅的 observer 执行更新函数
        for(Observer o : observers){
            if(cityCode.equals(o.getCityCode())){
                o.sendAllMessage(message, clientMap);
            }
        }
    }

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
        logger.info("WebSocket连接建立成功，{}", session.getId());
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
        logger.info("WebSocket连接断开，{}", session.getId());
        task.cancel();
        timer.cancel();
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
    public void onMsg(Session session, String message) {
        /*
         * message 为前台传输过来的城市代码，根据它来查询城市气象数据
         * 查询方式为轮询 高德api
         * */
        logger.info("服务端收到的消息为：{}", message);
        //取消之前的定时任务
        task.cancel();
        timer.cancel();
        //若后台请求的为新的一个城市
        //将发送过来的城市代码存储起来，并创建对应的观察者
        if(!cityCodes.contains(message)){
            cityCodes.add(message);
            //添加一个observer
            Observer webSocketObserver = new WebSocketObserver(message);
            attach(webSocketObserver);
        }
        logger.info("当前的城市即观察者列表：{}",cityCodes);
        //WeatherController 获取高德api提供的气象数据
        WeatherController weatherController = new WeatherController();
        final ArrayList<JSONObject> cityWeathers = new ArrayList<>();
        logger.info("城市列表容量：{}", cityCodes.size());
        //存储获取的气象数据
        for (int i = 0; i <= cityCodes.size(); i++){
            cityWeathers.add(null);
        }
        logger.info("容量：{}",cityWeathers.size());

        //轮询访问高德天气api
        //由于计时事件执行在Timer线程中，您必须确保访问Timer线程中任务中使用的任何数据项的正确同步。
        task = new TimerTask() {
            @Override
            public void run() {
                JSONObject[] live = {null};

                for(int i = 0; i < cityCodes.size(); i++){
                    live[0] = weatherController.getWeather(cityCodes.get(i));

                    if(cityWeathers.get(i) == null){
                        logger.info("天气更新了！");
                        cityWeathers.set(i, live[0]);
                        logger.info("live {}", live[0]);
                        notifyObserver(live[0].toString(),getClientMap(), message);
                    }else if(live[0].get("reporttime").equals(cityWeathers.get(i).get("reporttime"))){
                        //最新获取的数据没有变化，数据没有更新
                        logger.info("天气还未更新！live {}", live[0]);
                        notifyObserver("null",getClientMap(), message);
                    }else if(!live[0].get("reporttime").equals(cityWeathers.get(i).get("reporttime"))){
                        //数据更新了
                        cityWeathers.set(i, live[0]);
                        logger.info("天气更新了！ live {}", live[0]);
                        notifyObserver(live[0].toString(),getClientMap(), message);
                    }
                }
            }
        };

        timer = new Timer();
        timer.schedule(task,0,60*1000*45);
    }
}