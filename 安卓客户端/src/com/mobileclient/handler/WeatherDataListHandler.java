package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.WeatherData;
public class WeatherDataListHandler extends DefaultHandler {
	private List<WeatherData> weatherDataList = null;
	private WeatherData weatherData;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (weatherData != null) { 
            String valueString = new String(ch, start, length); 
            if ("weatherDataId".equals(tempString)) 
            	weatherData.setWeatherDataId(new Integer(valueString).intValue());
            else if ("weatherDataName".equals(tempString)) 
            	weatherData.setWeatherDataName(valueString); 
            else if ("weatherImage".equals(tempString)) 
            	weatherData.setWeatherImage(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("WeatherData".equals(localName)&&weatherData!=null){
			weatherDataList.add(weatherData);
			weatherData = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		weatherDataList = new ArrayList<WeatherData>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("WeatherData".equals(localName)) {
            weatherData = new WeatherData(); 
        }
        tempString = localName; 
	}

	public List<WeatherData> getWeatherDataList() {
		return this.weatherDataList;
	}
}
