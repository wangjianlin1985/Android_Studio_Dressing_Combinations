package com.chengxusheji.domain;

import java.sql.Timestamp;
public class WeatherData {
    /*记录id*/
    private int weatherDataId;
    public int getWeatherDataId() {
        return weatherDataId;
    }
    public void setWeatherDataId(int weatherDataId) {
        this.weatherDataId = weatherDataId;
    }

    /*天气数据名称*/
    private String weatherDataName;
    public String getWeatherDataName() {
        return weatherDataName;
    }
    public void setWeatherDataName(String weatherDataName) {
        this.weatherDataName = weatherDataName;
    }

    /*天气数据图像*/
    private String weatherImage;
    public String getWeatherImage() {
        return weatherImage;
    }
    public void setWeatherImage(String weatherImage) {
        this.weatherImage = weatherImage;
    }

}