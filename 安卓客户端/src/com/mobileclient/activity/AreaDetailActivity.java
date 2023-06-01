package com.mobileclient.activity;

import java.util.Date;
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
public class AreaDetailActivity extends Activity {
	// �������ذ�ť
	private Button btnReturn;
	// ��������id�ؼ�
	private TextView TV_areaId;
	// �����������ƿؼ�
	private TextView TV_areaName;
	/* Ҫ����ĵ�����Ϣ */
	Area area = new Area(); 
	/* ��������ҵ���߼��� */
	private AreaService areaService = new AreaService();
	private int areaId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//ȥ��title 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//ȥ��Activity�����״̬��
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		// ���õ�ǰActivity���沼��
		setContentView(R.layout.area_detail);
		ImageView search = (ImageView) this.findViewById(R.id.search);
		search.setVisibility(View.GONE);
		TextView title = (TextView) this.findViewById(R.id.title);
		title.setText("�鿴��������");
		ImageView back = (ImageView) this.findViewById(R.id.back_btn);
		back.setOnClickListener(new OnClickListener(){ 
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		// ͨ��findViewById����ʵ�������
		btnReturn = (Button) findViewById(R.id.btnReturn);
		TV_areaId = (TextView) findViewById(R.id.TV_areaId);
		TV_areaName = (TextView) findViewById(R.id.TV_areaName);
		Bundle extras = this.getIntent().getExtras();
		areaId = extras.getInt("areaId");
		btnReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AreaDetailActivity.this.finish();
			}
		}); 
		initViewData();
	}
	/* ��ʼ����ʾ������������ */
	private void initViewData() {
	    area = areaService.GetArea(areaId); 
		this.TV_areaId.setText(area.getAreaId() + "");
		this.TV_areaName.setText(area.getAreaName());
	} 
}
