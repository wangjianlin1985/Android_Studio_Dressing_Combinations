package com.mobileclient.activity;

import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileclient.app.Declare;
import com.mobileclient.util.HttpUtil;

  
/**
 * 
 * @author 
 *��¼
 */
public class LoginActivity extends Activity {
	private TextView title,register;
	private Button btn_login;
	private ImageView back_btn;
	private EditText et_username;
	private EditText et_pwd;
	//�û����ѡ��������
	private Spinner Spinner_identify;
	private ArrayAdapter<String> identify_adapter;
	private static  String[] identify_ShowText  = null;

	private ImageView search; 
	private MyProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title   
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);  
					
		setContentView(R.layout.user_login); 
		dialog = MyProgressDialog.getInstance(this);
		title = (TextView) findViewById(R.id.title);
		et_username = (EditText)findViewById(R.id.et_username);
		et_pwd = (EditText)findViewById(R.id.et_pwd);
		title.setText("�û���¼");
		search = (ImageView)findViewById(R.id.search);
		search.setVisibility(View.GONE);
		register = (TextView)findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) { 
				Intent intent = new Intent(LoginActivity.this,UserInfoAddActivity.class);
				startActivity(intent);
			} 
		});
		back_btn = (ImageView) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) { 
				finish();
			} 
		});
		
		this.Spinner_identify = (Spinner) findViewById(R.id.Spinner_identify);
		identify_ShowText = new String[] {"�û�","����Ա"};
		// ����ѡ������ArrayAdapter��������
		identify_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, identify_ShowText);
		// ���������б�ķ��
		identify_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// ��adapter ��ӵ�spinner��
		Spinner_identify.setAdapter(identify_adapter);
		// ����¼�Spinner�¼�����
		Spinner_identify.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				Declare declare = (Declare)LoginActivity.this.getApplication();
				switch(arg2) {
					case 0:
						declare.setIdentify("user");
						break;
					case 1:
						declare.setIdentify("admin");
						break;  
					default:
						break;
				} 
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
			
			});
			// ����Ĭ��ֵ
		Spinner_identify.setVisibility(View.VISIBLE);
		 
		
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				if("".equals(et_username.getText().toString())){
					Toast.makeText(LoginActivity.this, "�û�������", 0).show();
					return;
				}
				if("".equals(et_pwd.getText().toString())){
					Toast.makeText(LoginActivity.this, "�������", 0).show();
					return;
				}
				dialog.show();
				ExecutorService e = Executors.newCachedThreadPool();
				e.execute(new Runnable() {
					@Override
					public void run() {
						Declare declare = (Declare) LoginActivity.this.getApplication();
						try {
							String url = HttpUtil.BASE_URL
									+ "LoginServlet?userName="
									+ URLEncoder.encode(
											URLEncoder.encode(et_username.getText().toString(), "UTF-8"), "UTF-8")+"&password="
											+ URLEncoder.encode(
											URLEncoder.encode(et_pwd.getText().toString(), "UTF-8"), "UTF-8")+ "&identify=" + declare.getIdentify();
							// ��ѯ���ؽ��
							String result = HttpUtil.queryStringForPost(url);
							System.out.println("=========================  "+result);
							if(!result.equals("0")){
								System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@");
								declare.setUserName(et_username.getText().toString());
								handler.sendEmptyMessage(1);
							}else{
								handler.sendEmptyMessage(0); 
							}
						} catch (Exception e) {
							// TODO: handle exception
							Log.i("LoginActivity",e.toString());
						}
					}
				});
			}
			
		});

	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Toast.makeText(LoginActivity.this, "��½�ɹ�", 0).show();
				Intent intent = new Intent();
				Declare declare = (Declare) LoginActivity.this.getApplication();
				if(declare.getIdentify().equals("admin"))
					intent.setClass(LoginActivity.this,MainActivity.class);
				else
					intent.setClass(LoginActivity.this,MainUserActivity.class);
 
				startActivity(intent);
				LoginActivity.this.finish(); 
				break;
			case 0:
				Toast.makeText(LoginActivity.this, "��¼ʧ�ܣ��û������벻ƥ��", 0).show();
				break;

			default:
				break;
			}
			dialog.cancel();
		}
	};
	
	 
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{// ��׽���ؼ�
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			setResult(RESULT_CANCELED);
			finish();
		}
		return true;
	}
}
