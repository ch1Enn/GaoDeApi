package com.weathermonitor.gaodeapi.controller;

import com.weathermonitor.gaodeapi.utils.WeatherUtil;
import jdk.nashorn.internal.scripts.JS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/11/29 16:40
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {
    private static WeatherUtil weatherUtil = new WeatherUtil();

    /**日志对象*/
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    /**
     * http://localhost:2080/weather/getCurrent?adcode=140200
     * 获取当前的天气预报
     * @param adcode
     * @return
     */
    //@GetMapping("/getCurrent")
    public static JSONObject getWeather(@RequestParam String adcode){
        Map<String, String> params = new HashMap<>();
        params.put("adcode", adcode);
        logger.info("获取当前的天气预报,请求的参数为:{}", params);
        JSONObject map = weatherUtil.getCurrent(params);
        logger.info("获取当前的气象数据,返回的请求结果为:{}", map);
        return map;
    }

    @GetMapping("/monitorWeather")
    public static JSONObject timer1(@RequestParam String adcode) {
        Timer t = new Timer();
        final JSONObject[] live = {null};
        final JSONObject[] temp = { null };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                temp[0] = getWeather(adcode);
                logger.info("此次的轮询结果为：{}",temp[0]);
                if (live[0] == null || live[0].get("reporttime") == temp[0].get("reporttime")){
                    live[0] = temp[0];
                    logger.info("天气更新了 {}   {}",temp[0].get("reporttime"), live[0].get("reporttime"));
                    logger.info("{}",live[0].get("reporttime") != temp[0].get("reporttime"));
                }else{
                    logger.info("天气还未更新");
                }
            }
        };
        t.schedule(task,0, 300);
        return temp[0];
    }
}
