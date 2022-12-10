package com.weathermonitor.gaodeapi.service;

import com.weathermonitor.gaodeapi.observer.Observer;

import javax.websocket.Session;
import java.util.Map;

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/11/30 14:37
 */
public abstract class Subject {
    /**
     * 添加一个观察者
     *
     * @param observer
     * @author chieun Liang
     */
    public abstract void attach(Observer observer);

    /**
     * 移除一个观察者
     *
     * @param observer
     * @author chieun Liang
     */
    public abstract void detach(Observer observer);

    /**
     * 通知观察者
     * @param message, clientMap
     * @author chieun Liang
     */
    public abstract void notifyObserver(String message, Map<String , Session> clientMap);
}
