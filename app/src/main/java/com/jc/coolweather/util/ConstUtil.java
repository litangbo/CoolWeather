package com.jc.coolweather.util;

public class ConstUtil {
    /**省市县数据查询地址前缀，完整路径为：prefix+"/"+provinceCode+"/"+cityCode*/
    public static final String URL_REGION = "http://guolin.tech/api/china";
    /**天气查询地址，{weatherId}为占位符*/
    public static final String URL_WEATHER = "http://guolin.tech/api/weather?cityid={weatherId}&key=bc0418b57b2d4918819d3974ac1285d9";
    /**请求必应图片地址*/
    public static final String URL_BING_PIC = "http://guolin.tech/api/bing_pic";

    /**天气缓存key*/
    public static final String KEY_WEATHER = "weather";
    /**天气id缓存key*/
    public static final String KEY_WEATHER_ID = "weather_id";
    /**必应图片缓存key*/
    public static final String KEY_BING_PIC = "bing_pic";
}
