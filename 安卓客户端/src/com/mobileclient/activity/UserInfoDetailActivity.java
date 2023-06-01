package com.mobileclient.activity;

import java.util.Date;
import com.mobileclient.domain.UserInfo;
import com.mobileclient.service.UserInfoService;
import com.mobileclient.domain.Area;
import com.mobileclient.service.AreaService;
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
public class UserInfoDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// �����û����ؼ�
	private TextView TV_user_name;
	// ������¼����ؼ�
	private TextView TV_password;
	// �������ڵ����ؼ�
	private TextView TV_areaObj;
	// ���������ؼ�
	private TextView TV_name;
	// �����Ա�ؼ�
	private TextView TV_gender;
	// �����������ڿؼ�
	private TextView TV_birthDate;
	// �����û���ƬͼƬ��
	private ImageView iv_userPhoto;
	// ������ϵ�绰�ؼ�
	private TextView TV_telephone;
	// ��������ؼ�
	private TextView TV_email;
	// ������ͥ��ַ�ؼ�
	private TextView TV_address;
	// ����ע��ʱ��ؼ�
	private TextView TV_regTime;
	/* Ҫ������û���Ϣ */
	UserInfo userInfo = new UserInfo(); 
	/* �û�����ҵ���߼��� */
	private UserInfoService userInfoService = new UserInfoService();
	private AreaService areaService = new AreaService();
	private String user_name;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.userinfo_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴�û�����");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_user_name = (TextView) findViewById(R.id.TV_user_name);
		TV_password = (TextView) findViewById(R.id.TV_password);
		TV_areaObj = (TextView) findViewById(R.id.TV_areaObj);
		TV_name = (TextView) findViewById(R.id.TV_name);
		TV_gender = (TextView) findViewById(R.id.TV_gender);
		TV_birthDate = (TextView) findViewById(R.id.TV_birthDate);
		iv_userPhoto = (ImageView) findViewById(R.id.iv_userPhoto); 
		TV_telephone = (TextView) findViewById(R.id.TV_telephone);
		TV_email = (TextView) findViewById(R.id.TV_email);
		TV_address = (TextView) findViewById(R.id.TV_address);
		TV_regTime = (TextView) findViewById(R.id.TV_regTime);
		Bundle extras = this.getIntent().getExtras();
		user_name = extras.getString("user_name");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UserInfoDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    userInfo = userInfoService.GetUserInfo(user_name); 
		this.TV_user_name.setText(userInfo.getUser_name());
		this.TV_password.setText(userInfo.getPassword());
		Area areaObj = areaService.GetArea(userInfo.getAreaObj());
		this.TV_areaObj.setText(areaObj.getAreaName());
		this.TV_name.setText(userInfo.getName());
		this.TV_gender.setText(userInfo.getGender());
		Date birthDate = new Date(userInfo.getBirthDate().getTime());
		String birthDateStr = (birthDate.getYear() + 1900) + "-" + (birthDate.getMonth()+1) + "-" + birthDate.getDate();
		this.TV_birthDate.setText(birthDateStr);
		byte[] userPhoto_data = null;
		try {
			// ��ȡͼƬ����
			userPhoto_data = ImageService.getImage(HttpUtil.BASE_URL + userInfo.getUserPhoto());
			Bitmap userPhoto = BitmapFactory.decodeByteArray(userPhoto_data, 0,userPhoto_data.length);
			this.iv_userPhoto.setImageBitmap(userPhoto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.TV_telephone.setText(userInfo.getTelephone());
		this.TV_email.setText(userInfo.getEmail());
		this.TV_address.setText(userInfo.getAddress());
		this.TV_regTime.setText(userInfo.getRegTime());
	} 
}
