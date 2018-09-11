package com.jc.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省的实体类，用于存放省的数据信息
 * LitePal要求每个实体类都必须继承DataSupport类
 */
public class Province extends DataSupport{
    /**id，只支持int和long类型*/
    private int id;
    /**省的名称*/
    private String provinceName;
    /**省的代号*/
    private int provinceCode;

    @Override
    public String toString() {
        return super.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
