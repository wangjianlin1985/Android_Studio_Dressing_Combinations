package com.chengxusheji.domain;

import java.sql.Timestamp;
public class WeatherData {
    /*��¼id*/
    private int weatherDataId;
    public int getWeatherDataId() {
        return weatherDataId;
    }
    public void setWeatherDataId(int weatherDataId) {
        this.weatherDataId = weatherDataId;
    }

    /*������������*/
    private String weatherDataName;
    public String getWeatherDataName() {
        return weatherDataName;
    }
    public void setWeatherDataName(String weatherDataName) {
        this.weatherDataName = weatherDataName;
    }

    /*��������ͼ��*/
    private String weatherImage;
    public String getWeatherImage() {
        return weatherImage;
    }
    public void setWeatherImage(String weatherImage) {
        this.weatherImage = weatherImage;
    }

}