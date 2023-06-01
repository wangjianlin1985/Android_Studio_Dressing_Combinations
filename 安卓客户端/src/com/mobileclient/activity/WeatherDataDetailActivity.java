package com.mobileclient.activity;

import java.util.Date;
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
public class WeatherDataDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// ������¼id�ؼ�
	private TextView TV_weatherDataId;
	// ���������������ƿؼ�
	private TextView TV_weatherDataName;
	// ������������ͼ��ͼƬ��
	private ImageView iv_weatherImage;
	/* Ҫ���������������Ϣ */
	WeatherData weatherData = new WeatherData(); 
	/* �������ݹ���ҵ���߼��� */
	private WeatherDataService weatherDataService = new WeatherDataService();
	private int weatherDataId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.weatherdata_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴������������");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_weatherDataId = (TextView) findViewById(R.id.TV_weatherDataId);
		TV_weatherDataName = (TextView) findViewById(R.id.TV_weatherDataName);
		iv_weatherImage = (ImageView) findViewById(R.id.iv_weatherImage); 
		Bundle extras = this.getIntent().getExtras();
		weatherDataId = extras.getInt("weatherDataId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				WeatherDataDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    weatherData = weatherDataService.GetWeatherData(weatherDataId); 
		this.TV_weatherDataId.setText(weatherData.getWeatherDataId() + "");
		this.TV_weatherDataName.setText(weatherData.getWeatherDataName());
		byte[] weatherImage_data = null;
		try {
			// ��ȡͼƬ����
			weatherImage_data = ImageService.getImage(HttpUtil.BASE_URL + weatherData.getWeatherImage());
			Bitmap weatherImage = BitmapFactory.decodeByteArray(weatherImage_data, 0,weatherImage_data.length);
			this.iv_weatherImage.setImageBitmap(weatherImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}
