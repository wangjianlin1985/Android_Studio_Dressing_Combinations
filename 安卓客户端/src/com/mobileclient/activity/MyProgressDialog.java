package com.mobileclient.activity;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author ���� :Duncan Wei
 * @version ����ʱ�䣺2013-12-9 ����01:43:42
 * ��˵��
 */

public class MyProgressDialog extends ProgressDialog {

	public MyProgressDialog(Context context) {
		super(context);
	}
	public static MyProgressDialog getInstance(Context context)
	{
		MyProgressDialog dialog=new MyProgressDialog(context);
			dialog.setMessage("���Ժ�...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
		return dialog;
	}

}
