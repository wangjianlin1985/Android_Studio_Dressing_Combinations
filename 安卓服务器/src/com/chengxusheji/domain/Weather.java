package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Weather {
    /*天气id*/
    private int weatherId;
    public int getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    /*地区*/
    private Area areaObj;
    public Area getAreaObj() {
        return areaObj;
    }
    public void setAreaObj(Area areaObj) {
        this.areaObj = areaObj;
    }

    /*天气日期*/
    private Timestamp weatherDate;
    public Timestamp getWeatherDate() {
        return weatherDate;
    }
    public void setWeatherDate(Timestamp weatherDate) {
        this.weatherDate = weatherDate;
    }

    /*天气*/
    private WeatherData weatherDataObj;
    public WeatherData getWeatherDataObj() {
        return weatherDataObj;
    }
    public void setWeatherDataObj(WeatherData weatherDataObj) {
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