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
    public abstract void attach(Observer observer);

    public abstract void detach(Observer observer);

    public abstract void notifyObserver(String message, Map<String , Session> clientMap);
}
