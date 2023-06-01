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
	// ������ѯ��ť
	private Button btnQuery;
	// ��������������
	private Spinner spinner_areaObj;
	private ArrayAdapter<String> areaObj_adapter;
	private static  String[] areaObj_ShowText  = null;
	private List<Area> areaList = null; 
	/*��������ҵ���߼���*/
	private AreaService areaService = new AreaService();
	// �������ڿؼ�
	private DatePicker dp_weatherDate;
	private CheckBox cb_weatherDate;
	// ��������������
	private Spinner spinner_weatherDataObj;
	private ArrayAdapter<String> weatherDataObj_adapter;
	private static  String[] weatherDataObj_ShowText  = null;
	private List<WeatherData> weatherDataList = null; 
	/*�������ݹ���ҵ���߼���*/
	private WeatherDataService weatherDataService = new WeatherDataService();
	/*��ѯ�����������浽���������*/
	private Weather queryConditionWeather = new Weather();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.weather_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("��������Ԥ����ѯ����");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		spinner_areaObj = (Spinner) findViewById(R.id.Spinner_areaObj);
		// ��ȡ���еĵ���
		try {
			areaList = areaService.QueryArea(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int areaCount = areaList.size();
		areaObj_ShowText = new String[areaCount+1];
		areaObj_ShowText[0] = "������";
		for(int i=1;i<=areaCount;i++) { 
			areaObj_ShowText[i] = areaList.get(i-1).getAreaName();
		} 
		// ����ѡ������ArrayAdapter��������
		areaObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, areaObj_ShowText);
		// ���õ��������б�ķ��
		areaObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_areaObj.setAdapter(areaObj_adapter);
		// ����¼�Spinner�¼�����
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
		// ����Ĭ��ֵ
		spinner_areaObj.setVisibility(View.VISIBLE);
		dp_weatherDate = (DatePicker) findViewById(R.id.dp_weatherDate);
		cb_weatherDate = (CheckBox) findViewById(R.id.cb_weatherDate);
		spinner_weatherDataObj = (Spinner) findViewById(R.id.Spinner_weatherDataObj);
		// ��ȡ���е���������
		try {
			weatherDataList = weatherDataService.QueryWeatherData(null);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int weatherDataCount = weatherDataList.size();
		weatherDataObj_ShowText = new String[weatherDataCount+1];
		weatherDataObj_ShowText[0] = "������";
		for(int i=1;i<=weatherDataCount;i++) { 
			weatherDataObj_ShowText[i] = weatherDataList.get(i-1).getWeatherDataName();
		} 
		// ����ѡ������ArrayAdapter��������
		weatherDataObj_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, weatherDataObj_ShowText);
		// �������������б�ķ��
		weatherDataObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		spinner_weatherDataObj.setAdapter(weatherDataObj_adapter);
		// ����¼�Spinner�¼�����
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
		// ����Ĭ��ֵ
		spinner_weatherDataObj.setVisibility(View.VISIBLE);
		/*������ѯ��ť*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*��ȡ��ѯ����*/
					if(cb_weatherDate.isChecked()) {
						/*��ȡ��������*/
						Date weatherDate = new Date(dp_weatherDate.getYear()-1900,dp_weatherDate.getMonth(),dp_weatherDate.getDayOfMonth());
						queryConditionWeather.setWeatherDate(new Timestamp(weatherDate.getTime()));
					} else {
						queryConditionWeather.setWeatherDate(null);
					} 
					Intent intent = getIntent();
					//����ʹ��bundle��������������
					Bundle bundle =new Bundle();
					//�����������Ȼ�Ǽ�ֵ�Ե���ʽ
					bundle.putSerializable("queryConditionWeather", queryConditionWeather);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
