package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Weather;
public class WeatherListHandler extends DefaultHandler {
	private List<Weather> weatherList = null;
	private Weather weather;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (weather != null) { 
            String valueString = new String(ch, start, length); 
            if ("weatherId".equals(tempString)) 
            	weather.setWeatherId(new Integer(valueString).intValue());
            else if ("areaObj".equals(tempString)) 
            	weather.setAreaObj(new Integer(valueString).intValue());
            else if ("weatherDate".equals(tempString)) 
            	weather.setWeatherDate(Timestamp.valueOf(valueString));
            else if ("weatherDataObj".equals(tempString)) 
            	weather.setWeatherDataObj(new Integer(valueString).intValue());
            else if ("weatherImage".equals(tempString)) 
            	weather.setWeatherImage(valueString); 
            else if ("temperature".equals(tempString)) 
            	weather.setTemperature(valueString); 
            else if ("airQuality".equals(tempString)) 
            	weather.setAirQuality(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Weather".equals(localName)&&weather!=null){
			weatherList.add(weather);
			weather = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		weatherList = new ArrayList<Weather>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Weather".equals(localName)) {
            weather = new Weather(); 
        }
        tempString = localName; 
	}

	public List<Weather> getWeatherList() {
		return this.weatherList;
	}
}
