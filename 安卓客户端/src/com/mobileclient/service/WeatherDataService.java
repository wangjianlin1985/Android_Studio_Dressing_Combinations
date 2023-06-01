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

/*天气数据管理业务逻辑层*/
public class WeatherDataService {
	/* 添加天气数据 */
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

	/* 查询天气数据 */
	public List<WeatherData> QueryWeatherData(WeatherData queryConditionWeatherData) throws Exception {
		String urlString = HttpUtil.BASE_URL + "WeatherDataServlet?action=query";
		if(queryConditionWeatherData != null) {
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
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
		//第2种是基于json数据格式解析，我们采用的是第2种
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

	/* 更新天气数据 */
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

	/* 删除天气数据 */
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
			return "天气数据信息删除失败!";
		}
	}

	/* 根据记录id获取天气数据对象 */
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
