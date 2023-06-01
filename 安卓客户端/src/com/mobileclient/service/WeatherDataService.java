package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.WeatherData;
import com.mobileclient.util.HttpUtil;

/*�������ݹ���ҵ���߼���*/
public class WeatherDataService {
	/* ����������� */
	public String AddWeatherData(WeatherData weatherData) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("weatherDataId", weatherData.getWeatherDataId() + "");
		params.put("weatherDataName", weatherData.getWeatherDataName());
		params.put("weatherImage", weatherData.getWeatherImage());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WeatherDataServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ��ѯ�������� */
	public List<WeatherData> QueryWeatherData(WeatherData queryConditionWeatherData) throws Exception {
		String urlString = HttpUtil.BASE_URL + "WeatherDataServlet?action=query";
		if(queryConditionWeatherData != null) {
		}

		/* 2�����ݽ�����������һ������SAXParser����xml�ļ���ʽ
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		WeatherDataListHandler weatherDataListHander = new WeatherDataListHandler();
		xr.setContentHandler(weatherDataListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<WeatherData> weatherDataList = weatherDataListHander.getWeatherDataList();
		return weatherDataList;*/
		//��2���ǻ���json���ݸ�ʽ���������ǲ��õ��ǵ�2��
		List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WeatherData weatherData = new WeatherData();
				weatherData.setWeatherDataId(object.getInt("weatherDataId"));
				weatherData.setWeatherDataName(object.getString("weatherDataName"));
				weatherData.setWeatherImage(object.getString("weatherImage"));
				weatherDataList.add(weatherData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weatherDataList;
	}

	/* ������������ */
	public String UpdateWeatherData(WeatherData weatherData) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("weatherDataId", weatherData.getWeatherDataId() + "");
		params.put("weatherDataName", weatherData.getWeatherDataName());
		params.put("weatherImage", weatherData.getWeatherImage());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WeatherDataServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ɾ���������� */
	public String DeleteWeatherData(int weatherDataId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("weatherDataId", weatherDataId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WeatherDataServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "����������Ϣɾ��ʧ��!";
		}
	}

	/* ���ݼ�¼id��ȡ�������ݶ��� */
	public WeatherData GetWeatherData(int weatherDataId)  {
		List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("weatherDataId", weatherDataId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "WeatherDataServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				WeatherData weatherData = new WeatherData();
				weatherData.setWeatherDataId(object.getInt("weatherDataId"));
				weatherData.setWeatherDataName(object.getString("weatherDataName"));
				weatherData.setWeatherImage(object.getString("weatherImage"));
				weatherDataList.add(weatherData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = weatherDataList.size();
		if(size>0) return weatherDataList.get(0); 
		else return null; 
	}
}
