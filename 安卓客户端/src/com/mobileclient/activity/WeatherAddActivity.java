package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mobileclient.util.HttpUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.domain.Weather;
import com.mobileclient.service.WeatherService;
import com.mobileclient.domain.Area;
import com.mobileclient.service.AreaService;
import com.mobileclient.domain.WeatherData;
import com.mobileclient.service.WeatherDataService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class WeatherAddActivity extends Activity {
	// ����ȷ����Ӱ�ť
	private Button btnAdd;
	// ��������������
	private Spinner spinner_areaObj;
	private ArrayAdapter<String> areaObj_adapter;
	private static  String[] areaObj_ShowText  = null;
	private List<Area> areaList = null;
	/*��������ҵ���߼���*/
	private AreaService areaService = new AreaService();
	// �����������ڿؼ�
	private DatePicker dp_weatherDate;
	// ��������������
	private Spinner spinner_weatherDataObj;
	private ArrayAdapter<String> weatherDataObj_adapter;
	private static  String[] weatherDataObj_ShowText  = null;
	private List<WeatherData> weatherDataList = null;
	/*��������ҵ���߼���*/
	private WeatherDataService weatherDataService = new WeatherDataService();
	// ��������ͼ��ͼƬ��ؼ�
	private ImageView iv_weatherImage;
	private Button btn_weatherImage;
	protected int REQ_CODE_SELECT_IMAGE_weatherImage = 1;
	private int REQ_CODE_CAMERA_weatherImage = 2;
	// �����¶������
	private EditText ET_temperature;
	// �����������������
	private EditText ET_airQuality;
	protected String carmera_path;
	/*Ҫ���������Ԥ����Ϣ*/
	Weather weather = new Weather();
	/*����Ԥ������ҵ���߼���*/
	private WeatherService weatherService = new WeatherService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.weather_add); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�������Ԥ��");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		spinner_areaObj = (Spinner) findViewById(R.id.Spinner_areaObj);
		// ��ȡ���еĵ���
		try {
			areaList = areaService.QueryArea(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int areaCount = areaList.size();
		areaObj_ShowText = new String[areaCount];
		for(int i=0;i<areaCount;i++) { 
			areaObj_ShowText[i] = areaList.get(i).getAreaName();
		}
		// ����ѡ������ArrayAdapter��������
		areaObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, areaObj_ShowText);
		// ���������б�ķ��
		areaObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_areaObj.setAdapter(areaObj_adapter);
		// ����¼�Spinner�¼�����
		spinner_areaObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				weather.setAreaObj(areaList.get(arg2).getAreaId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_areaObj.setVisibility(View.VISIBLE);
		dp_weatherDate = (DatePicker)this.findViewById(R.id.dp_weatherDate);
		spinner_weatherDataObj = (Spinner) findViewById(R.id.Spinner_weatherDataObj);
		// ��ȡ���е�����
		try {
			weatherDataList = weatherDataService.QueryWeatherData(null);
		} catch (Exception e1) { 
			e1.printStackTrace(); 
		}
		int weatherDataCount = weatherDataList.size();
		weatherDataObj_ShowText = new String[weatherDataCount];
		for(int i=0;i<weatherDataCount;i++) { 
			weatherDataObj_ShowText[i] = weatherDataList.get(i).getWeatherDataName();
		}
		// ����ѡ������ArrayAdapter��������
		weatherDataObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, weatherDataObj_ShowText);
		// ���������б�ķ��
		weatherDataObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_weatherDataObj.setAdapter(weatherDataObj_adapter);
		// ����¼�Spinner�¼�����
		spinner_weatherDataObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				weather.setWeatherDataObj(weatherDataList.get(arg2).getWeatherDataId()); 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// ����Ĭ��ֵ
		spinner_weatherDataObj.setVisibility(View.VISIBLE);
		iv_weatherImage = (ImageView) findViewById(R.id.iv_weatherImage);
		/*����ͼƬ��ʾ�ؼ�ʱ����ͼƬ��ѡ��*/
		iv_weatherImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WeatherAddActivity.this,photoListActivity.class);
				startActivityForResult(intent,REQ_CODE_SELECT_IMAGE_weatherImage);
			}
		});
		btn_weatherImage = (Button) findViewById(R.id.btn_weatherImage);
		btn_weatherImage.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				carmera_path = HttpUtil.FILE_PATH + "/carmera_weatherImage.bmp";
				File out = new File(carmera_path); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); 
				startActivityForResult(intent, REQ_CODE_CAMERA_weatherImage);  
			}
		});
		ET_temperature = (EditText) findViewById(R.id.ET_temperature);
		ET_airQuality = (EditText) findViewById(R.id.ET_airQuality);
		btnAdd = (Button) findViewById(R.id.BtnAdd);
		/*�����������Ԥ����ť*/
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��������*/
					Date weatherDate = new Date(dp_weatherDate.getYear()-1900,dp_weatherDate.getMonth(),dp_weatherDate.getDayOfMonth());
					weather.setWeatherDate(new Timestamp(weatherDate.getTime()));
					if(weather.getWeatherImage() != null) {
						//���ͼƬ��ַ��Ϊ�գ�˵���û�ѡ����ͼƬ����ʱ��Ҫ���ӷ������ϴ�ͼƬ
						WeatherAddActivity.this.setTitle("�����ϴ�ͼƬ���Ե�...");
						String weatherImage = HttpUtil.uploadFile(weather.getWeatherImage());
						WeatherAddActivity.this.setTitle("ͼƬ�ϴ���ϣ�");
						weather.setWeatherImage(weatherImage);
					} else {
						weather.setWeatherImage("upload/noimage.jpg");
					}
					/*��֤��ȡ�¶�*/ 
					if(ET_temperature.getText().toString().equals("")) {
						Toast.makeText(WeatherAddActivity.this, "�¶����벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_temperature.setFocusable(true);
						ET_temperature.requestFocus();
						return;	
					}
					weather.setTemperature(ET_temperature.getText().toString());
					/*��֤��ȡ��������*/ 
					if(ET_airQuality.getText().toString().equals("")) {
						Toast.makeText(WeatherAddActivity.this, "�����������벻��Ϊ��!", Toast.LENGTH_LONG).show();
						ET_airQuality.setFocusable(true);
						ET_airQuality.requestFocus();
						return;	
					}
					weather.setAirQuality(ET_airQuality.getText().toString());
					/*����ҵ���߼����ϴ�����Ԥ����Ϣ*/
					WeatherAddActivity.this.setTitle("�����ϴ�����Ԥ����Ϣ���Ե�...");
					String result = weatherService.AddWeather(weather);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_CAMERA_weatherImage  && resultCode == Activity.RESULT_OK) {
			carmera_path = HttpUtil.FILE_PATH + "/carmera_weatherImage.bmp"; 
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(carmera_path, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 300*300);
			opts.inJustDecodeBounds = false;
			try {
				Bitmap booImageBm = BitmapFactory.decodeFile(carmera_path, opts);
				String jpgFileName = "carmera_weatherImage.jpg";
				String jpgFilePath =  HttpUtil.FILE_PATH + "/" + jpgFileName;
				try {
					FileOutputStream jpgOutputStream = new FileOutputStream(jpgFilePath);
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// ������д���ļ� 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_weatherImage.setImageBitmap(booImageBm);
				this.iv_weatherImage.setScaleType(ScaleType.FIT_CENTER);
				this.weather.setWeatherImage(jpgFileName);
			} catch (OutOfMemoryError err) {  }
		}

		if(requestCode == REQ_CODE_SELECT_IMAGE_weatherImage && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String filename =  bundle.getString("fileName");
			String filepath = HttpUtil.FILE_PATH + "/" + filename;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true; 
			BitmapFactory.decodeFile(filepath, opts); 
			opts.inSampleSize = photoListActivity.computeSampleSize(opts, -1, 128*128);
			opts.inJustDecodeBounds = false; 
			try { 
				Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
				this.iv_weatherImage.setImageBitmap(bm); 
				this.iv_weatherImage.setScaleType(ScaleType.FIT_CENTER); 
			} catch (OutOfMemoryError err) {  } 
			weather.setWeatherImage(filename); 
		}
	}
}
