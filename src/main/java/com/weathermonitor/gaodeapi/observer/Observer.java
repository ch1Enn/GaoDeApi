package com.weathermonitor.gaodeapi.observer;

import javax.websocket.Session;
import java.util.Map;

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/11/30 14:42
 */
public abstract class Observer {
    /**
    * 成员属性
    * 城市观察者的标识信息
    */
    private String cityCode;
    public Observer(String cityCode){
        this.cityCode = cityCode;
    }
    public String getCityCode() { return cityCode; }
    public void setCityCode(String cityCode) { this.cityCode = cityCode; }

    /**
     * 调用Session方法，向前台推送数据
     *
     * @param message,clientMap
     * @return
     * @author chieun Liang
     */
    public abstract void sendAllMessage(String message, Map<String , Session> clientMap);
}
