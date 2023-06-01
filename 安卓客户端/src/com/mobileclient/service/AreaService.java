package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Area;
import com.mobileclient.util.HttpUtil;

/*地区管理业务逻辑层*/
public class AreaService {
	/* 添加地区 */
	public String AddArea(Area area) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("areaId", area.getAreaId() + "");
		params.put("areaName", area.getAreaName());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "AreaServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询地区 */
	public List<Area> QueryArea(Area queryConditionArea) throws Exception {
		String urlString = HttpUtil.BASE_URL + "AreaServlet?action=query";
		if(queryConditionArea != null) {
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		AreaListHandler areaListHander = new AreaListHandler();
		xr.setContentHandler(areaListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Area> areaList = areaListHander.getAreaList();
		return areaList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Area> areaList = new ArrayList<Area>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Area area = new Area();
				area.setAreaId(object.getInt("areaId"));
				area.setAreaName(object.getString("areaName"));
				areaList.add(area);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return areaList;
	}

	/* 更新地区 */
	public String UpdateArea(Area area) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("areaId", area.getAreaId() + "");
		params.put("areaName", area.getAreaName());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "AreaServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除地区 */
	public String DeleteArea(int areaId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("areaId", areaId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "AreaServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "地区信息删除失败!";
		}
	}

	/* 根据地区id获取地区对象 */
	public Area GetArea(int areaId)  {
		List<Area> areaList = new ArrayList<Area>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("areaId", areaId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "AreaServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Area area = new Area();
				area.setAreaId(object.getInt("areaId"));
				area.setAreaName(object.getString("areaName"));
				areaList.add(area);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = areaList.size();
		if(size>0) return areaList.get(0); 
		else return null; 
	}
}
