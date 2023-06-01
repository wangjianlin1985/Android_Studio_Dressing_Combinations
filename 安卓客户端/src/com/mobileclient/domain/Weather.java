package com.mobileclient.domain;

import java.io.Serializable;

public class Weather implements Serializable {
    /*����id*/
    private int weatherId;
    public int getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    /*����*/
    private int areaObj;
    public int getAreaObj() {
        return areaObj;
    }
    public void setAreaObj(int areaObj) {
        this.areaObj = areaObj;
    }

    /*��������*/
    private java.sql.Timestamp weatherDate;
    public java.sql.Timestamp getWeatherDate() {
        return weatherDate;
    }
    public void setWeatherDate(java.sql.Timestamp weatherDate) {
        this.weatherDate = weatherDate;
    }

    /*����*/
    private int weatherDataObj;
    public int getWeatherDataObj() {
        return weatherDataObj;
    }
    public void setWeatherDataObj(int weatherDataObj) {
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