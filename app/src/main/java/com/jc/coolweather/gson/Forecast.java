package com.jc.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * HeWeather和风天气的daily_forecast部分（稍特殊）：
 * "daily_forecast":[
 *     {
 *         "date":"2016-08-08",
 *         "cond":{
 *             "txt_d":"阵雨"
 *         }，
 *         "tmp":{
 *             "max":"34",
 *             "min":"27"
 *         }
 *     },
 *     {
 *         "date":"2016-08-09",
 *         "cond":{
 *             "txt_d":"多云"
 *         }，
 *         "tmp":{
 *             "max":"35",
 *             "min":"29"
 *         }
 *     }
 * ]
 * 数组中的每一项都代表未来一天的天气信息，因此这里定义单日天气的实体类
 */
public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;
    @SerializedName("cond")
    public More more;

    public class Temperature {
        public String max;
        public String min;
    }

    public class More {
        @SerializedName("txt_d")
        public String info;
    }
}
