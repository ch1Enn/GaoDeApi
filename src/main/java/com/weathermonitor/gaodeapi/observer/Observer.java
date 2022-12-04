package com.weathermonitor.gaodeapi.observer;

import javax.websocket.Session;
import java.util.Map;

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/11/30 14:42
 */
public abstract class Observer {
    private String cityCode;
    public Observer(String cityCode){
        this.cityCode = cityCode;
    }
    public String getCityCode() { return cityCode; }
    public void setCityCode(String cityCode) { this.cityCode = cityCode; }
    public abstract void sendAllMessage(String message, Map<String , Session> clientMap);
}
