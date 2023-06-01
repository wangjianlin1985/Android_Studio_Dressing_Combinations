package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.Weather;
import com.mobileclient.service.WeatherService;
import com.mobileclient.domain.Area;
import com.mobileclient.service.AreaService;
import com.mobileclient.domain.WeatherData;
import com.mobileclient.service.WeatherDataService;
import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
public class WeatherDetailActivity extends Activity {
	// 声明返回按钮
	private Button btnReturn;
	// 声明天气id控件
	private TextView TV_weatherId;
	// 声明地区控件
	private TextView TV_areaObj;
	// 声明天气日期控件
	private TextView TV_weatherDate;
	// 声明天气控件
	private TextView TV_weatherDataObj;
	// 声明天气图像图片框
	private ImageView iv_weatherImage;
	// 声明温度控件
	private TextView TV_temperature;
	// 声明空气质量控件
	private TextView TV_airQuality;
	/* 要保存的天气预报信息 */
	Weather weather = new Weather(); 
	/* 天气预报管理业务逻辑层 */
	private WeatherService weatherService = new WeatherService();
	private AreaService areaService = new AreaService();
	private WeatherDataService weatherDataService = new WeatherDataService();
	private int weatherId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// 设置当前Activity界面布局
		setContentView(R.layout.weather_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("查看天气预报详情");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// 通过findViewById方法实例化组件
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_weatherId = (TextView) findViewById(R.id.TV_weatherId);
		TV_areaObj = (TextView) findViewById(R.id.TV_areaObj);
		TV_weatherDate = (TextView) findViewById(R.id.TV_weatherDate);
		TV_weatherDataObj = (TextView) findViewById(R.id.TV_weatherDataObj);
		iv_weatherImage = (ImageView) findViewById(R.id.iv_weatherImage); 
		TV_temperature = (TextView) findViewById(R.id.TV_temperature);
		TV_airQuality = (TextView) findViewById(R.id.TV_airQuality);
		Bundle extras = this.getIntent().getExtras();
		weatherId = extras.getInt("weatherId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				WeatherDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* 初始化显示详情界面的数据 */
	private void initViewData() {
	    weather = weatherService.GetWeather(weatherId); 
		this.TV_weatherId.setText(weather.getWeatherId() + "");
		Area areaObj = areaService.GetArea(weather.getAreaObj());
		this.TV_areaObj.setText(areaObj.getAreaName());
		Date weatherDate = new Date(weather.getWeatherDate().getTime());
		String weatherDateStr = (weatherDate.getYear() + 1900) + "-" + (weatherDate.getMonth()+1) + "-" + weatherDate.getDate();
		this.TV_weatherDate.setText(weatherDateStr);
		WeatherData weatherDataObj = weatherDataService.GetWeatherData(weather.getWeatherDataObj());
		this.TV_weatherDataObj.setText(weatherDataObj.getWeatherDataName());
		byte[] weatherImage_data = null;
		try {
			// 获取图片数据
			weatherImage_data = ImageService.getImage(HttpUtil.BASE_URL + weather.getWeatherImage());
			Bitmap weatherImage = BitmapFactory.decodeByteArray(weatherImage_data, 0,weatherImage_data.length);
			this.iv_weatherImage.setImageBitmap(weatherImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.TV_temperature.setText(weather.getTemperature());
		this.TV_airQuality.setText(weather.getAirQuality());
	} 
}
