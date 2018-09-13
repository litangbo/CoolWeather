package com.jc.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * HeWeather和风天气的Basic部分：
 * "basic":{
 *     "city":"苏州",
 *     "id":"CN101190401",
 *     "update":{
 *         "loc":"2016-08-08 21:58"
 *     }
 * }
 */
public class Basic {
    /**城市名*/
    @SerializedName("city")
    public String cityName;
    /**城市对应的天气id*/
    @SerializedName("id")
    public String weatherId;

    public class Update {
        /**天气的更新时间*/
        @SerializedName("loc")
        public String updateTime;
    }
}
