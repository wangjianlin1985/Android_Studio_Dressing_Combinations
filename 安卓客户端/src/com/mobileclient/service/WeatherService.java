package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Weather;
import com.mobileclient.util.HttpUtil;

/*����Ԥ������ҵ���߼���*/
public class WeatherService {
	/* �������Ԥ�� */
	public String AddWeather(Weather weather) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("weatherId", weather.getWeatherId() + "");
		params.put("areaObj", weather.getAreaObj() + "");
		params.put("weatherDate", weather.getWeatherDate().toString());
		params.put("weatherDataObj", weather.getWeatherDataObj() + "");
		params.put("weatherImage", weather.getWeatherImage());
		params.put("temperature", weather.getTemperature());
		params.put("airQuality", weather.getAirQuality());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WeatherServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ��ѯ����Ԥ�� */
	public List<Weather> QueryWeather(Weather queryConditionWeather) throws Exception {
		String urlString = HttpUtil.BASE_URL + "WeatherServlet?action=query";
		if(queryConditionWeather != null) {
			urlString += "&areaObj=" + queryConditionWeather.getAreaObj();
			if(queryConditionWeather.getWeatherDate() != null) {
				urlString += "&weatherDate=" + URLEncoder.encode(queryConditionWeather.getWeatherDate().toString(), "UTF-8");
			}
			urlString += "&weatherDataObj=" + queryConditionWeather.getWeatherDataObj();
		}

		/* 2�����ݽ�����������һ������SAXParser����xml�ļ���ʽ
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		WeatherListHandler weatherListHander = new WeatherListHandler();
		xr.setContentHandler(weatherListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Weather> weatherList = weatherListHander.getWeatherList();
		return weatherList;*/
		//��2���ǻ���json���ݸ�ʽ���������ǲ��õ��ǵ�2��
		List<Weather> weatherList = new ArrayList<Weather>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Weather weather = new Weather();
				weather.setWeatherId(object.getInt("weatherId"));
				weather.setAreaObj(object.getInt("areaObj"));
				weather.setWeatherDate(Timestamp.valueOf(object.getString("weatherDate")));
				weather.setWeatherDataObj(object.getInt("weatherDataObj"));
				weather.setWeatherImage(object.getString("weatherImage"));
				weather.setTemperature(object.getString("temperature"));
				weather.setAirQuality(object.getString("airQuality"));
				weatherList.add(weather);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weatherList;
	}

	/* ��������Ԥ�� */
	public String UpdateWeather(Weather weather) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("weatherId", weather.getWeatherId() + "");
		params.put("areaObj", weather.getAreaObj() + "");
		params.put("weatherDate", weather.getWeatherDate().toString());
		params.put("weatherDataObj", weather.getWeatherDataObj() + "");
		params.put("weatherImage", weather.getWeatherImage());
		params.put("temperature", weather.getTemperature());
		params.put("airQuality", weather.getAirQuality());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WeatherServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ɾ������Ԥ�� */
	public String DeleteWeather(int weatherId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("weatherId", weatherId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WeatherServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "����Ԥ����Ϣɾ��ʧ��!";
		}
	}

	/* ��������id��ȡ����Ԥ������ */
	public Weather GetWeather(int weatherId)  {
		List<Weather> weatherList = new ArrayList<Weather>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("weatherId", weatherId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WeatherServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Weather weather = new Weather();
				weather.setWeatherId(object.getInt("weatherId"));
				weather.setAreaObj(object.getInt("areaObj"));
				weather.setWeatherDate(Timestamp.valueOf(object.getString("weatherDate")));
				weather.setWeatherDataObj(object.getInt("weatherDataObj"));
				weather.setWeatherImage(object.getString("weatherImage"));
				weather.setTemperature(object.getString("temperature"));
				weather.setAirQuality(object.getString("airQuality"));
				weatherList.add(weather);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = weatherList.size();
		if(size>0) return weatherList.get(0); 
		else return null; 
	}
}
