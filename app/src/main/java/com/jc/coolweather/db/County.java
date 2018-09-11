package com.jc.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 县的实体类，必须继承DataSupport类
 */
public class County extends DataSupport{
    /**id，只支持int和long类型*/
    private int id;
    /**县的名称*/
    private String countyName;
    /**县所对应的天气id*/
    private String weatherId;
    /**所属市*/
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
