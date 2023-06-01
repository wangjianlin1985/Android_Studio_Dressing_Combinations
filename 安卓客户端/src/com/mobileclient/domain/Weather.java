package com.mobileclient.domain;

import java.io.Serializable;

public class Weather implements Serializable {
    /*天气id*/
    private int weatherId;
    public int getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    /*地区*/
    private int areaObj;
    public int getAreaObj() {
        return areaObj;
    }
    public void setAreaObj(int areaObj) {
        this.areaObj = areaObj;
    }

    /*天气日期*/
    private java.sql.Timestamp weatherDate;
    public java.sql.Timestamp getWeatherDate() {
        return weatherDate;
    }
    public void setWeatherDate(java.sql.Timestamp weatherDate) {
        this.weatherDate = weatherDate;
    }

    /*天气*/
    private int weatherDataObj;
    public int getWeatherDataObj() {
        return weatherDataObj;
    }
    public void setWeatherDataObj(int weatherDataObj) {
        this.weatherDataObj = weatherDataObj;
    }

    /*天气图像*/
    private String weatherImage;
    public String getWeatherImage() {
        return weatherImage;
    }
    public void setWeatherImage(String weatherImage) {
        this.weatherImage = weatherImage;
    }

    /*温度*/
    private String temperature;
    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /*空气质量*/
    private String airQuality;
    public String getAirQuality() {
        return airQuality;
    }
    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

}