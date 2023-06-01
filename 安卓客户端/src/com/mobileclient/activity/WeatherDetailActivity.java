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
	// �������ذ�ť
	private Button btnReturn;
	// ��������id�ؼ�
	private TextView TV_weatherId;
	// ���������ؼ�
	private TextView TV_areaObj;
	// �����������ڿؼ�
	private TextView TV_weatherDate;
	// ���������ؼ�
	private TextView TV_weatherDataObj;
	// ��������ͼ��ͼƬ��
	private ImageView iv_weatherImage;
	// �����¶ȿؼ�
	private TextView TV_temperature;
	// �������������ؼ�
	private TextView TV_airQuality;
	/* Ҫ���������Ԥ����Ϣ */
	Weather weather = new Weather(); 
	/* ����Ԥ������ҵ���߼��� */
	private WeatherService weatherService = new WeatherService();
	private AreaService areaService = new AreaService();
	private WeatherDataService weatherDataService = new WeatherDataService();
	private int weatherId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.weather_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴����Ԥ������");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
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
	/* ��ʼ����ʾ������������ */
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
			// ��ȡͼƬ����
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
