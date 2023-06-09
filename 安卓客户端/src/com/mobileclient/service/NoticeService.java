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

/*生活信息管理业务逻辑层*/
public class NoticeService {
	/* 添加生活信息 */
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

	/* 查询生活信息 */
	public List<Notice> QueryNotice(Notice queryConditionNotice) throws Exception {
		String urlString = HttpUtil.BASE_URL + "NoticeServlet?action=query";
		if(queryConditionNotice != null) {
			urlString += "&title=" + URLEncoder.encode(queryConditionNotice.getTitle(), "UTF-8") + "";
			urlString += "&publishDate=" + URLEncoder.encode(queryConditionNotice.getPublishDate(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
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
		//第2种是基于json数据格式解析，我们采用的是第2种
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

	/* 更新生活信息 */
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

	/* 删除生活信息 */
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
			return "生活信息信息删除失败!";
		}
	}

	/* 根据信息id获取生活信息对象 */
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
