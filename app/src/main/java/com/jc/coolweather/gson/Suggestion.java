package com.jc.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * HeWeather和风天气的Suggestion部分：
 * "suggestion":{
 *     "comf":{
 *         "text":"白天天气较热，虽然有雨，但仍然无法削弱较高气温给人们带来的署意，这种天气会让您感到不很舒适"
 *     },
 *     "cw":{
 *         "text":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车"
 *     },
 *     "sport":{
 *         "text":"有降水，且风力较强，推荐您在室内进行低强度运动；若坚持户外运动，请选择避雨防风的地点"
 *     }
 * }
 */
public class Suggestion {
    /**舒适度*/
    @SerializedName("comf")
    public Comfort comfort;
    /**洗车*/
    @SerializedName("cw")
    public CarWash carWash;
    /**运动*/
    public Sport sport;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
