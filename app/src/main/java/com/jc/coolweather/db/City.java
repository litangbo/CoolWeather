package com.jc.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 市的实体类，必须继承DataSupport类
 */
public class City extends DataSupport{
    /**id，只支持int和long类型*/
    private int id;
    /**市的名称*/
    private String cityName;
    /**市的代号*/
    private int cityCode;
    /**所属省的id*/
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
