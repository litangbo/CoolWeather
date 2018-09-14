package com.jc.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * HeWeather和风天气的Now部分：
 * "now":{
 *     "tmp":"29",
 *     "cond":{
 *         "text":"阵雨"
 *     }
 * }
 */
public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
