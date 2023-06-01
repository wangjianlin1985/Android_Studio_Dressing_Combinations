package com.mobileclient.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mobileclient.util.HttpUtil;
import com.mobileclient.util.ImageService;
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
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

public class WeatherDataEditActivity extends Activity {
	// 声明确定添加按钮
	private Button btnUpdate;
	// 声明记录idTextView
	private TextView TV_weatherDataId;
	// 声明天气数据名称输入框
	private EditText ET_weatherDataName;
	// 声明天气数据图像图片框控件
	private ImageView iv_weatherImage;
	private Button btn_weatherImage;
	protected int REQ_CODE_SELECT_IMAGE_weatherImage = 1;
	private int REQ_CODE_CAMERA_weatherImage = 2;
	protected String carmera_path;
	/*要保存的天气数据信息*/
	WeatherData weatherData = new WeatherData();
	/*天气数据管理业务逻辑层*/
	private WeatherDataService weatherDataService = new WeatherDataService();

	private int weatherDataId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.weatherdata_edit); 
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("编辑天气数据信息");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		TV_weatherDataId = (TextView) findViewById(R.id.TV_weatherDataId);
		ET_weatherDataName = (EditText) findViewById(R.id.ET_weatherDataName);
		iv_weatherImage = (ImageView) findViewById(R.id.iv_weatherImage);
		/*单击图片显示控件时进行图片的选择*/
		iv_weatherImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WeatherDataEditActivity.this,photoListActivity.class);
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
		btnUpdate = (Button) findViewById(R.id.BtnUpdate);
		Bundle extras = this.getIntent().getExtras();
		weatherDataId = extras.getInt("weatherDataId");
		/*单击修改天气数据按钮*/
		btnUpdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*验证获取天气数据名称*/ 
					if(ET_weatherDataName.getText().toString().equals("")) {
						Toast.makeText(WeatherDataEditActivity.this, "天气数据名称输入不能为空!", Toast.LENGTH_LONG).show();
						ET_weatherDataName.setFocusable(true);
						ET_weatherDataName.requestFocus();
						return;	
					}
					weatherData.setWeatherDataName(ET_weatherDataName.getText().toString());
					if (!weatherData.getWeatherImage().startsWith("upload/")) {
						//如果图片地址不为空，说明用户选择了图片，这时需要连接服务器上传图片
						WeatherDataEditActivity.this.setTitle("正在上传图片，稍等...");
						String weatherImage = HttpUtil.uploadFile(weatherData.getWeatherImage());
						WeatherDataEditActivity.this.setTitle("图片上传完毕！");
						weatherData.setWeatherImage(weatherImage);
					} 
					/*调用业务逻辑层上传天气数据信息*/
					WeatherDataEditActivity.this.setTitle("正在更新天气数据信息，稍等...");
					String result = weatherDataService.UpdateWeatherData(weatherData);
					Toast.makeText(getApplicationContext(), result, 1).show(); 
					Intent intent = getIntent();
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
		});
		initViewData();
	}

	/* 初始化显示编辑界面的数据 */
	private void initViewData() {
	    weatherData = weatherDataService.GetWeatherData(weatherDataId);
		this.TV_weatherDataId.setText(weatherDataId+"");
		this.ET_weatherDataName.setText(weatherData.getWeatherDataName());
		byte[] weatherImage_data = null;
		try {
			// 获取图片数据
			weatherImage_data = ImageService.getImage(HttpUtil.BASE_URL + weatherData.getWeatherImage());
			Bitmap weatherImage = BitmapFactory.decodeByteArray(weatherImage_data, 0, weatherImage_data.length);
			this.iv_weatherImage.setImageBitmap(weatherImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					booImageBm.compress(Bitmap.CompressFormat.JPEG, 30, jpgOutputStream);// 把数据写入文件 
					File bmpFile = new File(carmera_path);
					bmpFile.delete();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
				this.iv_weatherImage.setImageBitmap(booImageBm);
				this.iv_weatherImage.setScaleType(ScaleType.FIT_CENTER);
				this.weatherData.setWeatherImage(jpgFileName);
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
			weatherData.setWeatherImage(filename); 
		}
	}
}
