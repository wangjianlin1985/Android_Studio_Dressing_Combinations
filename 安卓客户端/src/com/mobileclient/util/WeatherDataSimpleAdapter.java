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
	/*需要绑定的控件资源id*/
    private int[] mTo;
    /*map集合关键字数组*/
    private String[] mFrom;
/*需要绑定的数据*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    private ListView mListView;
    //图片异步缓存加载类,带内存缓存和文件缓存
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
	  ///*第一次装载这个view时=null,就新建一个调用inflate渲染一个view*/
	  if (convertView == null) convertView = mInflater.inflate(R.layout.weatherdata_list_item, null);
	  convertView.setTag("listViewTAG" + position);
	  holder = new ViewHolder(); 
	  /*绑定该view各个控件*/
	  holder.tv_weatherDataId = (TextView)convertView.findViewById(R.id.tv_weatherDataId);
	  holder.tv_weatherDataName = (TextView)convertView.findViewById(R.id.tv_weatherDataName);
	  holder.iv_weatherImage = (ImageView)convertView.findViewById(R.id.iv_weatherImage);
	  /*设置各个控件的展示内容*/
	  holder.tv_weatherDataId.setText("记录id：" + mData.get(position).get("weatherDataId").toString());
	  holder.tv_weatherDataName.setText("天气数据名称：" + mData.get(position).get("weatherDataName").toString());
	  holder.iv_weatherImage.setImageResource(R.drawable.default_photo);
	  ImageLoadListener weatherImageLoadListener = new ImageLoadListener(mListView,R.id.iv_weatherImage);
	  syncImageLoader.loadImage(position,(String)mData.get(position).get("weatherImage"),weatherImageLoadListener);  
	  /*返回修改好的view*/
	  return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_weatherDataId;
    	TextView tv_weatherDataName;
    	ImageView iv_weatherImage;
    }
} 
