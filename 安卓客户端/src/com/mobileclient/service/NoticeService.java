package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Notice;
import com.mobileclient.util.HttpUtil;

/*������Ϣ����ҵ���߼���*/
public class NoticeService {
	/* ���������Ϣ */
	public String AddNotice(Notice notice) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("noticeId", notice.getNoticeId() + "");
		params.put("title", notice.getTitle());
		params.put("content", notice.getContent());
		params.put("videoFile", notice.getVideoFile());
		params.put("publishDate", notice.getPublishDate());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NoticeServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ��ѯ������Ϣ */
	public List<Notice> QueryNotice(Notice queryConditionNotice) throws Exception {
		String urlString = HttpUtil.BASE_URL + "NoticeServlet?action=query";
		if(queryConditionNotice != null) {
			urlString += "&title=" + URLEncoder.encode(queryConditionNotice.getTitle(), "UTF-8") + "";
			urlString += "&publishDate=" + URLEncoder.encode(queryConditionNotice.getPublishDate(), "UTF-8") + "";
		}

		/* 2�����ݽ�����������һ������SAXParser����xml�ļ���ʽ
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		NoticeListHandler noticeListHander = new NoticeListHandler();
		xr.setContentHandler(noticeListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Notice> noticeList = noticeListHander.getNoticeList();
		return noticeList;*/
		//��2���ǻ���json���ݸ�ʽ���������ǲ��õ��ǵ�2��
		List<Notice> noticeList = new ArrayList<Notice>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Notice notice = new Notice();
				notice.setNoticeId(object.getInt("noticeId"));
				notice.setTitle(object.getString("title"));
				notice.setContent(object.getString("content"));
				notice.setVideoFile(object.getString("videoFile"));
				notice.setPublishDate(object.getString("publishDate"));
				noticeList.add(notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticeList;
	}

	/* ����������Ϣ */
	public String UpdateNotice(Notice notice) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("noticeId", notice.getNoticeId() + "");
		params.put("title", notice.getTitle());
		params.put("content", notice.getContent());
		params.put("videoFile", notice.getVideoFile());
		params.put("publishDate", notice.getPublishDate());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NoticeServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ɾ��������Ϣ */
	public String DeleteNotice(int noticeId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("noticeId", noticeId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NoticeServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "������Ϣ��Ϣɾ��ʧ��!";
		}
	}

	/* ������Ϣid��ȡ������Ϣ���� */
	public Notice GetNotice(int noticeId)  {
		List<Notice> noticeList = new ArrayList<Notice>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("noticeId", noticeId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NoticeServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Notice notice = new Notice();
				notice.setNoticeId(object.getInt("noticeId"));
				notice.setTitle(object.getString("title"));
				notice.setContent(object.getString("content"));
				notice.setVideoFile(object.getString("videoFile"));
				notice.setPublishDate(object.getString("publishDate"));
				noticeList.add(notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = noticeList.size();
		if(size>0) return noticeList.get(0); 
		else return null; 
	}
}
