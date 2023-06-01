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

/*��������ҵ���߼���*/
public class AreaService {
	/* ��ӵ��� */
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

	/* ��ѯ���� */
	public List<Area> QueryArea(Area queryConditionArea) throws Exception {
		String urlString = HttpUtil.BASE_URL + "AreaServlet?action=query";
		if(queryConditionArea != null) {
		}

		/* 2�����ݽ�����������һ������SAXParser����xml�ļ���ʽ
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
		//��2���ǻ���json���ݸ�ʽ���������ǲ��õ��ǵ�2��
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

	/* ���µ��� */
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

	/* ɾ������ */
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
			return "������Ϣɾ��ʧ��!";
		}
	}

	/* ���ݵ���id��ȡ�������� */
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
