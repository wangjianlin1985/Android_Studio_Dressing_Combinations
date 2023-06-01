package com.mobileclient.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.mobileclient.activity.photoListActivity;
import com.mobileclient.app.Declare;

public class HttpUtil {
	// ����URL
	public static final String BASE_URL = "http://192.168.1.102:8080/JavaWebProject/";
	public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobileclient";
	public static final String Cach_Dir =  Declare.context.getCacheDir().getAbsolutePath();

	// ���Get�������request
	public static HttpGet getHttpGet(String url) { 
		HttpGet request = new HttpGet(url);
		return request;
	}

	// ���Post�������request
	public static HttpPost getHttpPost(String url) {
		HttpPost request = new HttpPost(url);
		return request;
	}

	// ������������Ӧ����response
	public static HttpResponse getHttpResponse(HttpGet request)
			throws ClientProtocolException, IOException {
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}

	// ������������Ӧ����response
	public static HttpResponse getHttpResponse(HttpPost request)
			throws ClientProtocolException, IOException {
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}

	// ����Post���󣬻����Ӧ��ѯ���
	public static String queryStringForPost(String url) {
		// ����url���HttpPost����
		HttpPost request = HttpUtil.getHttpPost(url);
		String result = null;
		try {
			// �����Ӧ����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// �ж��Ƿ�����ɹ�
			if (response.getStatusLine().getStatusCode() == 200) {
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		}
		return null;
	}

	// �����Ӧ��ѯ���
	public static String queryStringForPost(HttpPost request) {
		String result = null;
		try {
			// �����Ӧ����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// �ж��Ƿ�����ɹ�
			if (response.getStatusLine().getStatusCode() == 200) {
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		}
		return null;
	}

	// ����Get���󣬻����Ӧ��ѯ���
	public static String queryStringForGet(String url) {
		// ���HttpGet����
		HttpGet request = HttpUtil.getHttpGet(url);
		String result = null;
		try {
			// �����Ӧ����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			// �ж��Ƿ�����ɹ�
			if (response.getStatusLine().getStatusCode() == 200) {
				// �����Ӧ
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			result = "�����쳣��";
			return result;
		}
		return null;
	}

	public static boolean sendXML(String path, String xml) throws Exception {
		byte[] data = xml.getBytes();
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);// ���ͨ��post�ύ���ݣ�����������������������
		conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		outStream.close();
		if (conn.getResponseCode() == 200) {
			return true;
		}
		return false;
	}

	public static byte[] sendGetRequest(String path,
			Map<String, String> params, String enc) throws Exception {
		StringBuilder sb = new StringBuilder(path);
		sb.append('?');
		// ?method=save&title=435435435&timelength=89&
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue(), enc))
					.append('&');
		}
		sb.deleteCharAt(sb.length() - 1);

		URL url = new URL(sb.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);

		if (conn.getResponseCode() == 200) {
			return readStream(conn.getInputStream());
		}
		return null;
	}

	public static boolean sendPostRequest(String path,
			Map<String, String> params, String enc) throws Exception {
		// title=dsfdsf&timelength=23&method=save
		StringBuilder sb = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append('=')
						.append(URLEncoder.encode(entry.getValue(), enc))
						.append('&');
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		byte[] entitydata = sb.toString().getBytes();// �õ�ʵ��Ķ���������
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);// ���ͨ��post�ύ���ݣ�����������������������
		// Content-Type: application/x-www-form-urlencoded
		// Content-Length: 38
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(entitydata.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(entitydata);
		outStream.flush();
		outStream.close();
		if (conn.getResponseCode() == 200) {
			return true;
		}
		return false;
	}

	public static byte[] SendPostRequest(String path,
			Map<String, String> params, String enc) throws Exception {
		// title=dsfdsf&timelength=23&method=save
		StringBuilder sb = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append('=')
						.append(URLEncoder.encode(entry.getValue(), enc))
						.append('&');
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		byte[] entitydata = sb.toString().getBytes();// �õ�ʵ��Ķ���������
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);// ���ͨ��post�ύ���ݣ�����������������������
		// Content-Type: application/x-www-form-urlencoded
		// Content-Length: 38
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				String.valueOf(entitydata.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(entitydata);
		outStream.flush();
		outStream.close();
		if (conn.getResponseCode() == 200) {
			return readStream(conn.getInputStream());
		}
		return null;
	}

	// SSL HTTPS Cookie
	public static boolean sendRequestFromHttpClient(String path,
			Map<String, String> params, String enc) throws Exception {
		List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramPairs.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
		}
		UrlEncodedFormEntity entitydata = new UrlEncodedFormEntity(paramPairs,
				enc);// �õ�������������ʵ������
		HttpPost post = new HttpPost(path); // form
		post.setEntity(entitydata);
		DefaultHttpClient client = new DefaultHttpClient(); // �����
		HttpResponse response = client.execute(post);// ִ������
		if (response.getStatusLine().getStatusCode() == 200) {
			return true;
		}
		return false;
	}

	/**
	 * ��ȡ��
	 * 
	 * @param inStream
	 * @return �ֽ�����
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	/* �ϴ��ļ���Server�ķ��� */
	public static String uploadFile(String filename) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		try {
			URL url = new URL(HttpUtil.BASE_URL + "UpPhotoServlet");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* ����Input��Output����ʹ��Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* ���ô��͵�method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* ����DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + filename + "\"" + end);
			ds.writeBytes(end);
			/* ȡ���ļ���FileInputStream */
			File file = new File(HttpUtil.FILE_PATH + "/" + filename);
			FileInputStream fStream = new FileInputStream(file);
			/* ����ÿ��д��1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* ���ļ���ȡ������������ */
			while ((length = fStream.read(buffer)) != -1) {
				/* ������д��DataOutputStream�� */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* ȡ��Response���� */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			/* ��Response��ʾ��Dialog */
			/* �ر�DataOutputStream */
			ds.close();
			return b.toString();
		} catch (Exception e) {
			return "";
		}

	}
	
	
	
	private static final String[][] MIME_MapTable={  
            //{��׺���� MIME����}  
            {".3gp",    "video/3gpp"},  
            {".apk",    "application/vnd.android.package-archive"},  
            {".asf",    "video/x-ms-asf"},  
            {".avi",    "video/x-msvideo"},  
            {".bin",    "application/octet-stream"},  
            {".bmp",    "image/bmp"},  
            {".c",  "text/plain"},  
            {".class",  "application/octet-stream"},  
            {".conf",   "text/plain"},  
            {".cpp",    "text/plain"},  
            {".doc",    "application/msword"},  
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},  
            {".xls",    "application/vnd.ms-excel"},   
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},  
            {".exe",    "application/octet-stream"},  
            {".gif",    "image/gif"},  
            {".gtar",   "application/x-gtar"},  
            {".gz", "application/x-gzip"},  
            {".h",  "text/plain"},  
            {".htm",    "text/html"},  
            {".html",   "text/html"},  
            {".jar",    "application/java-archive"},  
            {".java",   "text/plain"},  
            {".jpeg",   "image/jpeg"},  
            {".jpg",    "image/jpeg"},  
            {".js", "application/x-javascript"},  
            {".log",    "text/plain"},  
            {".m3u",    "audio/x-mpegurl"},  
            {".m4a",    "audio/mp4a-latm"},  
            {".m4b",    "audio/mp4a-latm"},  
            {".m4p",    "audio/mp4a-latm"},  
            {".m4u",    "video/vnd.mpegurl"},  
            {".m4v",    "video/x-m4v"},   
            {".mov",    "video/quicktime"},  
            {".mp2",    "audio/x-mpeg"},  
            {".mp3",    "audio/x-mpeg"},  
            {".mp4",    "video/mp4"},  
            {".mpc",    "application/vnd.mpohun.certificate"},        
            {".mpe",    "video/mpeg"},    
            {".mpeg",   "video/mpeg"},    
            {".mpg",    "video/mpeg"},    
            {".mpg4",   "video/mp4"},     
            {".mpga",   "audio/mpeg"},  
            {".msg",    "application/vnd.ms-outlook"},  
            {".ogg",    "audio/ogg"},  
            {".pdf",    "application/pdf"},  
            {".png",    "image/png"},  
            {".pps",    "application/vnd.ms-powerpoint"},  
            {".ppt",    "application/vnd.ms-powerpoint"},  
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},  
            {".prop",   "text/plain"},  
            {".rc", "text/plain"},  
            {".rmvb",   "audio/x-pn-realaudio"},  
            {".rtf",    "application/rtf"},  
            {".sh", "text/plain"},  
            {".tar",    "application/x-tar"},     
            {".tgz",    "application/x-compressed"},   
            {".txt",    "text/plain"},  
            {".wav",    "audio/x-wav"},  
            {".wma",    "audio/x-ms-wma"},  
            {".wmv",    "audio/x-ms-wmv"},  
            {".wps",    "application/vnd.ms-works"},  
            {".xml",    "text/plain"},  
            {".z",  "application/x-compress"},  
            {".zip",    "application/x-zip-compressed"},  
            {"",        "*/*"}    
	};
	
	
	
	
	
	//���ط�������upload�ļ�Ŀ¼���ļ�
	public static void downloadFile(String uploadPath) {
		//Ҫ���ص��ļ�·��
		String urlDownload = "";
		//urlDownload =  "http://192.168.3.39/text.txt";
		urlDownload = HttpUtil.BASE_URL + uploadPath;
		// ��ô洢��·�������� �����ļ���Ŀ��·��  
		File f = new File(HttpUtil.FILE_PATH);
		if(!f.exists())  f.mkdir(); 
		
		File f2 = new File(HttpUtil.FILE_PATH + "/upload");
		if(!f2.exists())  f2.mkdir(); 
		
		//׼��ƴ���µ��ļ����������ڴ洢������ļ�����
		String newFilename = HttpUtil.FILE_PATH + "/" + uploadPath; 
		File file = new File(newFilename);
		//���Ŀ���ļ��Ѿ����ڣ���ɾ�����������Ǿ��ļ���Ч��
		if(file.exists())  file.delete(); 
		try {
		         // ����URL   
		         URL url = new URL(urlDownload);   
		         // ������   
		         URLConnection con = url.openConnection();
		         //����ļ��ĳ���
		         int contentLength = con.getContentLength();
		         System.out.println("���� :"+contentLength);
		         // ������   
		         InputStream is = con.getInputStream();  
		         // 1K�����ݻ���   
		         byte[] bs = new byte[1024];   
		         // ��ȡ�������ݳ���   
		         int len;   
		         // ������ļ���   
		         OutputStream os = new FileOutputStream(newFilename);   
		         // ��ʼ��ȡ   
		         while ((len = is.read(bs)) != -1) {   
		             os.write(bs, 0, len);   
		         }  
		         // ��ϣ��ر���������   
		         os.close();  
		         is.close(); 
		         //openFile(file);   
		} catch (Exception e) {
		        e.printStackTrace();
		} 
	}
	
	
	/** 
	 * ���ļ� 
	 * @param file 
	 */  
	private static void openFile(File file){
	    Intent intent = new Intent();  
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	    //����intent��Action����  
	    intent.setAction(Intent.ACTION_VIEW);  
	    //��ȡ�ļ�file��MIME����  
	    String type = getMIMEType(file);  
	    //����intent��data��Type���ԡ�  
	    intent.setDataAndType(/*uri*/Uri.fromFile(file), type);  
	    //��ת  
	    Declare.context.startActivity(intent);    
	      
	}  
	  
	/** 
	 * �����ļ���׺����ö�Ӧ��MIME���͡� 
	 * @param file 
	 */  
	private static String getMIMEType(File file) {  
	      
	    String type="*/*";  
	    String fName = file.getName();  
	    //��ȡ��׺��ǰ�ķָ���"."��fName�е�λ�á�  
	    int dotIndex = fName.lastIndexOf(".");  
	    if(dotIndex < 0){  
	        return type;  
	    }  
	    /* ��ȡ�ļ��ĺ�׺�� */  
	    String end=fName.substring(dotIndex,fName.length()).toLowerCase();  
	    if(end=="")return type;  
	    //��MIME���ļ����͵�ƥ������ҵ���Ӧ��MIME���͡�  
	    for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??��������һ�������ʣ����MIME_MapTable��ʲô��  
	        if(end.equals(MIME_MapTable[i][0]))  
	            type = MIME_MapTable[i][1];  
	    }         
	    return type;  
	}  
	 

}
