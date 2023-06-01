package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

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

public class WeatherDataSimpleAdapter extends SimpleAdapter { 
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

    public WeatherDataSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,ListView listView) { 
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
	  if (convertView == null) convertView = mInflater.inflate(R.layout.weatherdata_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*�󶨸�view�����ؼ�*/
	  holder.tv_weatherDataId = (TextView)convertView.findViewById(R.id.tv_weatherDataId);
	  holder.tv_weatherDataName = (TextView)convertView.findViewById(R.id.tv_weatherDataName);
	  holder.iv_weatherImage = (ImageView)convertView.findViewById(R.id.iv_weatherImage);
	  /*���ø����ؼ���չʾ����*/
	  holder.tv_weatherDataId.setText("��¼id��" + mData.get(position).get("weatherDataId").toString());
	  holder.tv_weatherDataName.setText("�����������ƣ�" + mData.get(position).get("weatherDataName").toString());
	  holder.iv_weatherImage.setImageResource(R.drawable.default_photo);
	  ImageLoadListener weatherImageLoadListener = new ImageLoadListener(mListView,R.id.iv_weatherImage);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("weatherImage"),weatherImageLoadListener);  
	  /*�����޸ĺõ�view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_weatherDataId;
    	TextView tv_weatherDataName;
    	ImageView iv_weatherImage;
    }
} 
