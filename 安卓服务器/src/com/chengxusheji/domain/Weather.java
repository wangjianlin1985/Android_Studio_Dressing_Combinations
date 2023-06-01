package com.chengxusheji.domain;

import java.sql.Timestamp;
public class Weather {
    /*����id*/
    private int weatherId;
    public int getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    /*����*/
    private Area areaObj;
    public Area getAreaObj() {
        return areaObj;
    }
    public void setAreaObj(Area areaObj) {
        this.areaObj = areaObj;
    }

    /*��������*/
    private Timestamp weatherDate;
    public Timestamp getWeatherDate() {
        return weatherDate;
    }
    public void setWeatherDate(Timestamp weatherDate) {
        this.weatherDate = weatherDate;
    }

    /*����*/
    private WeatherData weatherDataObj;
    public WeatherData getWeatherDataObj() {
        return weatherDataObj;
    }
    public void setWeatherDataObj(WeatherData weatherDataObj) {
        this.weatherDataObj = weatherDataObj;
    }

    /*����ͼ��*/
    private String weatherImage;
    public String getWeatherImage() {
        return weatherImage;
    }
    public void setWeatherImage(String weatherImage) {
        this.weatherImage = weatherImage;
    }

    /*�¶�*/
    private String temperature;
    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    /*��������*/
    private String airQuality;
    public String getAirQuality() {
        return airQuality;
    }
    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

}