package com.caozaolin.mreactfloatview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.caozaolin.mreactfloatview.listener.FvItemClickListener;
import com.caozaolin.mreactfloatview.view.GameAssistApi;

public class ZSDK {

	private static final int REQUEST_CODE = 1;
	private GameAssistApi mGameAssistApi;

	private static ZSDK instance;
	private Activity context;
	private FvItemClickListener mListener;

	private ZSDK() {}

	public static ZSDK getInstance() {
		if (instance == null) {
			instance = new ZSDK();
		}
		return instance;
	}

			/**
			 * 初始化ZSDK类，悬浮窗权限申请也在这一步进行
			 * @param context
			 */
		public void init(Activity context, FvItemClickListener listener) {
			this.context = context;
			this.mListener = listener;
//			if(mGameAssistApi == null) {
//				mGameAssistApi = new GameAssistApi(context, mListener);
//			}
			askforPermission();
		}

		public Activity getContext() {
			return this.context;
		}

		/**
		 * 显示悬浮窗
		 */
		public void onResume() {
			if(mGameAssistApi != null){
				mGameAssistApi.onResume();
			}
		}

		/**
		 * 隐藏悬浮窗
		 */
		public void onPause() {

			if(mGameAssistApi != null){
				mGameAssistApi.onPause();

			}
		}

	/**
	 * 隐藏悬浮窗
	 */
	public void onDestroy() {
		onPause();
		mGameAssistApi = null;
		instance = null;
	}




		/**
		 * 处理返回Activity状态码的情况
		 * @param requestCode
		 * @param resultCode
		 * @param data
		 */
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == REQUEST_CODE) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					Log.d("eeeee", "canDrawOverlaysZsdk=" + Settings.canDrawOverlays(context));



					AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
					int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context.getPackageName());
					Log.d("eeeee", "android:system_alert_window: mode=" + mode);



					if (Settings.canDrawOverlays(context) || mode == 0 || mode == 1) {
						// 如果已经授权
						// 以下两句代码务必调用
						if(mGameAssistApi == null){
							Log.d("eeeee", "mGameAssistApi == null");
							mGameAssistApi = new GameAssistApi(context, mListener);
						}
					}
				}
			}
		}

		/**
		 * 权限申请
		 */
		public void askforPermission(){
			// 进行权限申请


//			if (!SettingsCompat.canDrawOverlays(context)){
//				Log.d("eeeee", "111111111111canDrawOverlays");
//									AlertDialog.Builder builder = new AlertDialog.Builder(context);
//					builder.setTitle("提示");
//					builder.setMessage("需要申请系统悬浮窗权限！");
//					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//							SettingsCompat.manageDrawOverlays(context);
////							Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
////							intent.setData(Uri.parse("package:" + context.getPackageName()));
////							context.startActivityForResult(intent, REQUEST_CODE);
//						}
//					});
//					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.dismiss();
//						}
//					});
//					builder.show();
//
//			}


			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
				// 如果是api23以上版本，则进行权限申请
				if(!Settings.canDrawOverlays(context)){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("提示");
					builder.setMessage("需要申请系统悬浮窗权限！");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
							intent.setData(Uri.parse("package:" + context.getPackageName()));
							context.startActivityForResult(intent, REQUEST_CODE);
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					builder.show();
				} else {
					Toast.makeText(context, "已经申请过权限了", Toast.LENGTH_SHORT).show();
					if(mGameAssistApi == null){
						mGameAssistApi = new GameAssistApi(context, mListener);
					}
				}

			} else {
				// 如果是api23以下版本，不需要申请权限
				Toast.makeText(context, "不需要申请权限", Toast.LENGTH_SHORT).show();
				// ZSDK.getInstance().init(context);
				if(mGameAssistApi == null){
					mGameAssistApi = new GameAssistApi(context, mListener);
				}
			}
		}


	}