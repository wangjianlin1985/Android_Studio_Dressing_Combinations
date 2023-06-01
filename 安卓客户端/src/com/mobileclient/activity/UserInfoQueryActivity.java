package com.mobileclient.activity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.domain.Area;
import com.mobileclient.service.AreaService;

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
public class UserInfoQueryActivity extends Activity {
	// 声明查询按钮
	private Button btnQuery;
	// 声明用户名输入框
	private EditText ET_user_name;
	// 声明所在地区下拉框
	private Spinner spinner_areaObj;
	private ArrayAdapter<String> areaObj_adapter;
	private static  String[] areaObj_ShowText  = null;
	private List<Area> areaList = null; 
	/*地区管理业务逻辑层*/
	private AreaService areaService = new AreaService();
	// 声明姓名输入框
	private EditText ET_name;
	// 出生日期控件
	private DatePicker dp_birthDate;
	private CheckBox cb_birthDate;
	// 声明联系电话输入框
	private EditText ET_telephone;
	/*查询过滤条件保存到这个对象中*/
	private UserInfo queryConditionUserInfo = new UserInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去除title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		// 设置当前Activity界面布局
		setContentView(R.layout.userinfo_query);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("设置用户查询条件");
		ImageView back_btn = (ImageView) this.findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnQuery = (Button) findViewById(R.id.btnQuery);
		ET_user_name = (EditText) findViewById(R.id.ET_user_name);
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
		// 设置所在地区下拉列表的风格
		areaObj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_areaObj.setAdapter(areaObj_adapter);
		// 添加事件Spinner事件监听
		spinner_areaObj.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2 != 0)
					queryConditionUserInfo.setAreaObj(areaList.get(arg2-1).getAreaId()); 
				else
					queryConditionUserInfo.setAreaObj(0);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		// 设置默认值
		spinner_areaObj.setVisibility(View.VISIBLE);
		ET_name = (EditText) findViewById(R.id.ET_name);
		dp_birthDate = (DatePicker) findViewById(R.id.dp_birthDate);
		cb_birthDate = (CheckBox) findViewById(R.id.cb_birthDate);
		ET_telephone = (EditText) findViewById(R.id.ET_telephone);
		/*单击查询按钮*/
		btnQuery.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					/*获取查询参数*/
					queryConditionUserInfo.setUser_name(ET_user_name.getText().toString());
					queryConditionUserInfo.setName(ET_name.getText().toString());
					if(cb_birthDate.isChecked()) {
						/*获取出生日期*/
						Date birthDate = new Date(dp_birthDate.getYear()-1900,dp_birthDate.getMonth(),dp_birthDate.getDayOfMonth());
						queryConditionUserInfo.setBirthDate(new Timestamp(birthDate.getTime()));
					} else {
						queryConditionUserInfo.setBirthDate(null);
					} 
					queryConditionUserInfo.setTelephone(ET_telephone.getText().toString());
					Intent intent = getIntent();
					//这里使用bundle绷带来传输数据
					Bundle bundle =new Bundle();
					//传输的内容仍然是键值对的形式
					bundle.putSerializable("queryConditionUserInfo", queryConditionUserInfo);
					intent.putExtras(bundle);
					setResult(RESULT_OK,intent);
					finish();
				} catch (Exception e) {}
			}
			});
	}
}
