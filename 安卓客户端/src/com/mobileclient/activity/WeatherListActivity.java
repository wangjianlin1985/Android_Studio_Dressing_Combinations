package com.mobileclient.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobileclient.app.Declare;
import com.mobileclient.domain.Weather;
import com.mobileclient.service.WeatherService;
import com.mobileclient.util.ActivityUtils;import com.mobileclient.util.WeatherSimpleAdapter;
import com.mobileclient.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class WeatherListActivity extends Activity {
	WeatherSimpleAdapter adapter;
	ListView lv; 
	List<Map<String, Object>> list;
	int weatherId;
	/* 天气预报操作业务逻辑层对象 */
	WeatherService weatherService = new WeatherService();
	/*保存查询参数条件的天气预报对象*/
	private Weather queryConditionWeather;

	private MyProgressDialog dialog; //进度条	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.weather_list);
		dialog = MyProgressDialog.getInstance(this);
		Declare declare = (Declare) getApplicationContext();
		String username = declare.getUserName();
		//标题栏控件
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WeatherListActivity.this, WeatherQueryActivity.class);
				startActivityForResult(intent,ActivityUtils.QUERY_CODE);//此处的requestCode应与下面结果处理函中调用的requestCode一致
			}
		});
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("天气预报查询列表");
		ImageView add_btn = (ImageView) this.findViewById(R.id.add_btn);
		add_btn.setOnClickListener(new android.view.View.OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(WeatherListActivity.this, WeatherAddActivity.class);
				startActivityForResult(intent,ActivityUtils.ADD_CODE);
			}
		});
		
		if(declare.getIdentify().equals("user")) {
			add_btn.setVisibility(View.GONE);
		}
		setViews();
	}

	//结果处理函数，当从secondActivity中返回时调用此函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ActivityUtils.QUERY_CODE && resultCode==RESULT_OK){
        	Bundle extras = data.getExtras();
        	if(extras != null)
        		queryConditionWeather = (Weather)extras.getSerializable("queryConditionWeather");
        	setViews();
        }
        if(requestCode==ActivityUtils.EDIT_CODE && resultCode==RESULT_OK){
        	setViews();
        }
        if(requestCode == ActivityUtils.ADD_CODE && resultCode == RESULT_OK) {
        	queryConditionWeather = null;
        	setViews();
        }
    }

	private void setViews() {
		lv = (ListView) findViewById(R.id.h_list_view);
		dialog.show();
		final Handler handler = new Handler();
		new Thread(){
			@Override
			public void run() {
				//在子线程中进行下载数据操作
				list = getDatas();
				//发送消失到handler，通知主线程下载完成
				handler.post(new Runnable() {
					@Override
					public void run() {
						dialog.cancel();
						adapter = new WeatherSimpleAdapter(WeatherListActivity.this, list,
	        					R.layout.weather_list_item,
	        					new String[] { "weatherId","areaObj","weatherDate","weatherDataObj","weatherImage","temperature","airQuality" },
	        					new int[] { R.id.tv_weatherId,R.id.tv_areaObj,R.id.tv_weatherDate,R.id.tv_weatherDataObj,R.id.iv_weatherImage,R.id.tv_temperature,R.id.tv_airQuality,},lv);
	        			lv.setAdapter(adapter);
					}
				});
			}
		}.start(); 

		// 添加长按点击
		lv.setOnCreateContextMenuListener(weatherListItemListener);
		lv.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            	int weatherId = Integer.parseInt(list.get(arg2).get("weatherId").toString());
            	Intent intent = new Intent();
            	intent.setClass(WeatherListActivity.this, WeatherDetailActivity.class);
            	Bundle bundle = new Bundle();
            	bundle.putInt("weatherId", weatherId);
            	intent.putExtras(bundle);
            	startActivity(intent);
            }
        });
	}
	private OnCreateContextMenuListener weatherListItemListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
			Declare declare = (Declare) WeatherListActivity.this.getApplication();
			if(declare.getIdentify().equals("admin")) {
				menu.add(0, 0, 0, "编辑天气预报信息"); 
				menu.add(0, 1, 0, "删除天气预报信息");
			}
			
		}
	};

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {  //编辑天气预报信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取天气id
			weatherId = Integer.parseInt(list.get(position).get("weatherId").toString());
			Intent intent = new Intent();
			intent.setClass(WeatherListActivity.this, WeatherEditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("weatherId", weatherId);
			intent.putExtras(bundle);
			startActivityForResult(intent,ActivityUtils.EDIT_CODE);
		} else if (item.getItemId() == 1) {// 删除天气预报信息
			ContextMenuInfo info = item.getMenuInfo();
			AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo) info;
			// 获取选中行位置
			int position = contextMenuInfo.position;
			// 获取天气id
			weatherId = Integer.parseInt(list.get(position).get("weatherId").toString());
			dialog();
		}
		return super.onContextItemSelected(item);
	}

	// 删除
	protected void dialog() {
		Builder builder = new Builder(WeatherListActivity.this);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String result = weatherService.DeleteWeather(weatherId);
				Toast.makeText(getApplicationContext(), result, 1).show();
				setViews();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private List<Map<String, Object>> getDatas() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			/* 查询天气预报信息 */
			List<Weather> weatherList = weatherService.QueryWeather(queryConditionWeather);
			for (int i = 0; i < weatherList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("weatherId", weatherList.get(i).getWeatherId());
				map.put("areaObj", weatherList.get(i).getAreaObj());
				map.put("weatherDate", weatherList.get(i).getWeatherDate());
				map.put("weatherDataObj", weatherList.get(i).getWeatherDataObj());
				/*byte[] weatherImage_data = ImageService.getImage(HttpUtil.BASE_URL+ weatherList.get(i).getWeatherImage());// 获取图片数据
				BitmapFactory.Options weatherImage_opts = new BitmapFactory.Options();  
				weatherImage_opts.inJustDecodeBounds = true;  
				BitmapFactory.decodeByteArray(weatherImage_data, 0, weatherImage_data.length, weatherImage_opts); 
				weatherImage_opts.inSampleSize = photoListActivity.computeSampleSize(weatherImage_opts, -1, 100*100); 
				weatherImage_opts.inJustDecodeBounds = false; 
				try {
					Bitmap weatherImage = BitmapFactory.decodeByteArray(weatherImage_data, 0, weatherImage_data.length, weatherImage_opts);
					map.put("weatherImage", weatherImage);
				} catch (OutOfMemoryError err) { }*/
				map.put("weatherImage", HttpUtil.BASE_URL+ weatherList.get(i).getWeatherImage());
				map.put("temperature", weatherList.get(i).getTemperature());
				map.put("airQuality", weatherList.get(i).getAirQuality());
				list.add(map);
			}
		} catch (Exception e) { 
			Toast.makeText(getApplicationContext(), "", 1).show();
		}
		return list;
	}

}
