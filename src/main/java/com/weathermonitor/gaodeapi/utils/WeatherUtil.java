package com.weathermonitor.gaodeapi.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.net.URI;

/**
 * @author chieun Liang
 * @version 1.0
 * @create 2022/11/29 16:41
 */
@Component
@PropertySource("classpath:application.properties")
public class WeatherUtil {
    /**日志对象*/
    private static final Logger logger = LoggerFactory.getLogger(WeatherUtil.class);

    private final String key = "95b9e8a6bbe5bfb920704b9a17ef9c41";

    public final String WEATHER_URL = "https://restapi.amap.com/v3/weather/weatherInfo?";

    /**
     * 拼接请求的url
     * 发送get请求
     * @param params
     * @return
     */
    public JSONObject getCurrent(Map<String, String> params){

        JSONObject jsonObject = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建URI对象，并且设置请求参数
        try {
            URI uri = getBuilderCurrent(WEATHER_URL, params);
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            // 判断返回状态是否为200
            jsonObject = getRouteCurrent(response);
            httpclient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * 根据不同的路径规划获取距离
     * @param response
     * @return
     */
    private static JSONObject getRouteCurrent(CloseableHttpResponse response) throws Exception{
        JSONObject live = null;
        // 判断返回状态是否为200
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.info("调用高德地图接口返回的结果为:{}",content);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(content);
            JSONArray lives;
            lives = (JSONArray) jsonObject.get("lives");
            live = (JSONObject) lives.get(0);

            logger.info("返回的结果为:{}",JSONObject.toJSONString(live));
        }
        return live;
    }

    /**
     * 封装URI
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    private URI getBuilderCurrent(String url, Map<String, String> params) throws Exception{
        // 城市编码，高德地图提供
        String adcode = params.get("adcode");

        URIBuilder uriBuilder = new URIBuilder(url);
        // 公共参数
        uriBuilder.setParameter("key", key);
        uriBuilder.setParameter("city", adcode);

        logger.info("请求的参数key为:{}, cityCode为:{}", key, adcode);
        URI uri = uriBuilder.build();
        return uri;
    }

    /**
     * 查询未来的
     * 发送get请求
     * @return
     */
    public JSONObject sendGetFuture(Map<String, String> params){

        JSONObject jsonObject = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建URI对象，并且设置请求参数
        try {
            URI uri = getBuilderFuture(WEATHER_URL, params);
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            // 判断返回状态是否为200
            jsonObject = getRouteFuture(response);
            httpclient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * 封装URI
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    private URI getBuilderFuture(String url, Map<String, String> params) throws Exception{
        // 城市编码，高德地图提供
        String adcode = params.get("adcode");

        URIBuilder uriBuilder = new URIBuilder(url);
        // 公共参数
        uriBuilder.setParameter("key", key);
        uriBuilder.setParameter("city", adcode);
        uriBuilder.setParameter("extensions", "all");

        logger.info("请求的参数key为:{}, cityCode为:{}", key, adcode);
        URI uri = uriBuilder.build();
        return uri;
    }

    /**
     * 根据不同的路径规划获取距离
     * @param response
     * @return
     */
    private static JSONObject getRouteFuture(CloseableHttpResponse response) throws Exception{
        JSONObject live = null;
        // 判断返回状态是否为200
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.info("调用高德地图接口返回的结果为:{}",content);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(content);
            JSONArray forecast = (JSONArray) jsonObject.get("forecasts");
            live = (JSONObject) forecast.get(0);

            logger.info("返回的结果为:{}",JSONObject.toJSONString(live));
        }
        return live;
    }
}
