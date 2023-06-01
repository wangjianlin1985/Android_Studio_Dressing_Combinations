package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.Weather;
import com.mobileclient.domain.Area;
import com.mobileclient.service.AreaService;
import com.mobileclient.domain.WeatherData;
import com.mobileclient.service.WeatherDataService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.ImageView;
import android.widget.TextView;
public class WeatherQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明地区下拉框
	private Spinner spinner_areaObj;
	private ArrayAdapter<String> areaObj_adapter;
	private static  String[] areaObj_ShowText  = null;
	private List<Area> areaList = null; 
	/*地区管理业务逻辑层*/
	private AreaService areaService = new AreaService();
	// 天气日期控件
	private DatePicker dp_weatherDate;
	private CheckBox cb_weatherDate;
	// 声明天气下拉框
	private Spinner spinner_weatherDataObj;
	private ArrayAdapter<String> weatherDataObj_adapter;
	private static  String[] weatherDataObj_ShowText  = null;
	private List<WeatherData> weatherDataList = null; 
	/*天气数据管理业务逻辑层*/
	private WeatherDataService weatherDataService = new WeatherDataService();
	/*查询过滤条件保存到这个对象中*/
	private Weather queryConditionWeather = new Weather();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.weather_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置天气预报查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_areaObj = (Spinner) findViewById(R.id.Spinner_areaObj);
		// 获取所有的地区
		try {
			areaList = areaService.QueryArea(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int areaCount = areaList.size();
		areaObj_ShowText = new String[areaCount+1];
		areaObj_ShowText[0] = "不限制";
		for(int i=1;i<=areaCount;i++) { 
			areaObj_ShowText[i] = areaList.get(i-1).getAreaName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		areaObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, areaObj_ShowText);
		// 设置地区下拉列表的风格
		areaObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_areaObj.setAdapter(areaObj_adapter);
		// 添加事件Spinner事件监听
		spinner_areaObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionWeather.setAreaObj(areaList.get(arg2-1).getAreaId()); 
				else
					queryConditionWeather.setAreaObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_areaObj.setVisibility(View.VISIBLE);
		dp_weatherDate = (DatePicker) findViewById(R.id.dp_weatherDate);
		cb_weatherDate = (CheckBox) findViewById(R.id.cb_weatherDate);
		spinner_weatherDataObj = (Spinner) findViewById(R.id.Spinner_weatherDataObj);
		// 获取所有的天气数据
		try {
			weatherDataList = weatherDataService.QueryWeatherData(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int weatherDataCount = weatherDataList.size();
		weatherDataObj_ShowText = new String[weatherDataCount+1];
		weatherDataObj_ShowText[0] = "不限制";
		for(int i=1;i<=weatherDataCount;i++) { 
			weatherDataObj_ShowText[i] = weatherDataList.get(i-1).getWeatherDataName();
		} 
		// 将可选内容与ArrayAdapter连接起来
		weatherDataObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, weatherDataObj_ShowText);
		// 设置天气下拉列表的风格
		weatherDataObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_weatherDataObj.setAdapter(weatherDataObj_adapter);
		// 添加事件Spinner事件监听
		spinner_weatherDataObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionWeather.setWeatherDataObj(weatherDataList.get(arg2-1).getWeatherDataId()); 
				else
					queryConditionWeather.setWeatherDataObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_weatherDataObj.setVisibility(View.VISIBLE);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					if(cb_weatherDate.isChecked()) {
						/*获取天气日期*/
						Date weatherDate = new Date(dp_weatherDate.getYear()-1900,dp_weatherDate.getMonth(),dp_weatherDate.getDayOfMonth());
						queryConditionWeather.setWeatherDate(new Timestamp(weatherDate.getTime()));
					} else {
						queryConditionWeather.setWeatherDate(null);
					} 
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionWeather", queryConditionWeather);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
