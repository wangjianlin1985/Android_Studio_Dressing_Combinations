package com.mobileclient.util;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * ��������������activity֮����ת�Ĺ�����  
 */
public class ActivityUtils {
	public static final int QUERY_CODE = 1000;
	public static final int EDIT_CODE = 1001;
	public static final int ADD_CODE = 1002;

	private static ActivityUtils activityTransitionUtils;
	public static List<Activity> activityList = new ArrayList<Activity>();

	public static ActivityUtils getInstance() {
		if (activityTransitionUtils == null) {
			activityTransitionUtils = new ActivityUtils();
		}
		return activityTransitionUtils;
	}

	public void pushActivity(Activity activity) {
		activityList.add(activity);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 *  Ӧ�ó���
	 *
	 * @param context
	 *            �������Ķ���
	 */
	public void exitClient(Context context) {
		for (Activity ac : activityList) {
			ac.finish();
		}
//		ActivityManager activityManager = (ActivityManager) context
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		activityManager.killBackgroundProcesses("com.roboo.template");
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}

	public void ConfirmExit(final Context context) {// �˳�ȷ��
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("��   ��");
		builder.setMessage("�Ƿ��˳����?");
		builder.setPositiveButton("ȷ  ��",
				new DialogInterface.OnClickListener() {// �˳���ť
					@Override
					public void onClick(DialogInterface dialog, int i) {
						/**********************************************/
						// �˳�ʱ��������Ƶ������ΪĬ��ֵ����ҳ����
						/**********************************************/
						exitClient(context);
					}
				});
		builder.setNegativeButton("ȡ  ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						// ���˳�����ִ���κβ���
					}
				});
		builder.show();// ��ʾ�Ի���
	}
	public void setScreenOrientation(Activity activity) {
		activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	}

	public void setPortpaitScreenOrientation(Activity activity) {
		activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		activity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // �Q��
	}

	// ȫ����״̬��
	public void setFullScreenOrientation(Activity activity) {
		Window window = activity.getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);

	}

	/**
	 * ���ܣ�ȫ��������??
	 *
	 * @param activity
	 */
	public void setFullScreen(Activity activity) {
		Window window = activity.getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public void setFullScreenHasTitle(Activity activity, String title) {
		Window window = activity.getWindow();
		if (null != title) {
			activity.setTitle(title);
		}
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	// ��ʼ���汾����mainfest����??
	public void initVersion(Context context, TextView welcome_version) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo("zekezang.org",
							PackageManager.GET_CONFIGURATIONS);
			welcome_version.setText(packageInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ��ȡ��Ļ���
	public int[] getWidthHeight(Activity ac) {
		DisplayMetrics dm = new DisplayMetrics();
		ac.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return new int[] { dm.widthPixels, dm.heightPixels };
	}

	public void setBrightness(Activity activity, boolean isnormal) {

		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

		if (isnormal) {
			// lp.screenBrightness = 200 / 255.0f;
			lp.screenBrightness = -1.0f;
		} else {
			// lp.screenBrightness = 1 / 255.0f;
			lp.screenBrightness = 0.045f;
		}
		activity.getWindow().setAttributes(lp);
	}

	/**
	 * activity֮�����??
	 *
	 * @param activity
	 *            ����ǰactivity
	 * @param mIntent
	 *            ��Intent����
	 */
	public void skipActivity(Activity activity, Intent mIntent) {
		activity.startActivity(mIntent);
		// ���activity֮���л�����
		activity.overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}
	public static boolean validateFileSize(File file,int sizeM){
		boolean result = false;
		long size = file.length();
		float maxSize = sizeM+0.00f;
		if(div(size, 1048576, 2)<=maxSize){
			result = true;
		}
		return result;
	}
	private static float div(long v1, long v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Long.toString(v1));
		BigDecimal b2 = new BigDecimal(Long.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	public static boolean checkText(String str) {
		// TODO Auto-generated method stub
		return TextUtils.isEmpty(str);
	}
}
