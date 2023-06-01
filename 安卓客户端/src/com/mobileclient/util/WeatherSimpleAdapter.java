package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.AreaService;
import com.mobileclient.service.WeatherDataService;
import com.mobileclient.activity.R;
import com.mobileclient.imgCache.ImageLoadListener;
import com.mobileclient.imgCache.ListViewOnScrollListener;
import com.mobileclient.imgCache.SyncImageLoader;
import android.content.Context;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.ListView;
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class WeatherSimpleAdapter extends SimpleAdapter { 
	/*��Ҫ�󶨵Ŀؼ���Դid*/
    private int[] mTo;
    /*map���Ϲؼ�������*/
    private String[] mFrom;
/*��Ҫ�󶨵�����*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //ͼƬ�첽���������,���ڴ滺����ļ�����
    private SyncImageLoader syncImageLoader;

    public WeatherSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
        mListView = listView; 
        syncImageLoader = SyncImageLoader.getInstance();
        ListViewOnScrollListener onScrollListener = new ListViewOnScrollListener(syncImageLoader,listView,getCount());
        mListView.setOnScrollListener(onScrollListener);
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
	  ViewHolder holder = null;
	  ///*��һ��װ�����viewʱ=null,���½�һ������inflate��Ⱦһ��view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.weather_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*�󶨸�view�����ؼ�*/
	  holder.tv_weatherId = (TextView)convertView.findViewById(R.id.tv_weatherId);
	  holder.tv_areaObj = (TextView)convertView.findViewById(R.id.tv_areaObj);
	  holder.tv_weatherDate = (TextView)convertView.findViewById(R.id.tv_weatherDate);
	  holder.tv_weatherDataObj = (TextView)convertView.findViewById(R.id.tv_weatherDataObj);
	  holder.iv_weatherImage = (ImageView)convertView.findViewById(R.id.iv_weatherImage);
	  holder.tv_temperature = (TextView)convertView.findViewById(R.id.tv_temperature);
	  holder.tv_airQuality = (TextView)convertView.findViewById(R.id.tv_airQuality);
	  /*���ø����ؼ���չʾ����*/
	  holder.tv_weatherId.setText("����id��" + mData.get(position).get("weatherId").toString());
	  holder.tv_areaObj.setText("������" + (new AreaService()).GetArea(Integer.parseInt(mData.get(position).get("areaObj").toString())).getAreaName());
	  try {holder.tv_weatherDate.setText("�������ڣ�" + mData.get(position).get("weatherDate").toString().substring(0, 10));} catch(Exception ex){}
	  holder.tv_weatherDataObj.setText("������" + (new WeatherDataService()).GetWeatherData(Integer.parseInt(mData.get(position).get("weatherDataObj").toString())).getWeatherDataName());
	  holder.iv_weatherImage.setImageResource(R.drawable.default_photo);
	  ImageLoadListener weatherImageLoadListener = new ImageLoadListener(mListView,R.id.iv_weatherImage);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("weatherImage"),weatherImageLoadListener);  
	  holder.tv_temperature.setText("�¶ȣ�" + mData.get(position).get("temperature").toString());
	  holder.tv_airQuality.setText("����������" + mData.get(position).get("airQuality").toString());
	  /*�����޸ĺõ�view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_weatherId;
    	TextView tv_areaObj;
    	TextView tv_weatherDate;
    	TextView tv_weatherDataObj;
    	ImageView iv_weatherImage;
    	TextView tv_temperature;
    	TextView tv_airQuality;
    }
} 
