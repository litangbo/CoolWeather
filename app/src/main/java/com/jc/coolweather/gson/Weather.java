package com.jc.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 和风天气返回的数据结构大致如下：
 * {
 *     "HeWeather":[
 *         {
 *             "status":"ok",
 *             "basic":{},
 *             "aqi":{},
 *             "now":{},
 *             "suggestion"{}，
 *             "daily_forecast":[]
 *         }
 *     ]
 * }
 */
public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
