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

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/11/29 16:40
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {
    /**
    * 成员属性
    * 天气实体类
     * 提供获取天气实时数据的方法
    */
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
    public JSONObject getWeather(@RequestParam String adcode){
        Map<String, String> params = new HashMap<>();
        params.put("adcode", adcode);
        logger.info("开始请求天气数据,请求的参数为:{}", params);
        JSONObject map = weatherUtil.getCurrent(params);
        return map;
    }
}
