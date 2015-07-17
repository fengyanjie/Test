package com.voole.epg.download;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.voole.epg.download.bean.DownloadResourceManager;

public class UsbStateReceiver extends BroadcastReceiver {

	private static final String TAG = "UsbStateReceiver";
	public static final int USB_STATE_MSG = 0x00020;
	public static final int USB_STATE_ON = 0x00021;
	public static final int USB_STATE_OFF = 0x00022;
	public static final String USB_STATE = "USB_STATE";
	public static final String USB_PATH = "USB_PATH";
	private static Toast toast;
	Context context;
	private static String usbStr;
	private Handler handler  = new Handler(Looper.getMainLooper());
	
	@SuppressLint("ShowToast")
	public void registerReceiver(Context context) {
		this.context = context;
		if (toast == null) {
			toast = Toast.makeText(context, usbStr, 0);
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_CHECKING);
		filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);//直接拔出 sd卡广播
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
//		String oemType = com.voole.util.prop.PropertiesUtil.getProperty(context, "oemType");
//		if("TCL_4K".equalsIgnoreCase(oemType)){
			//TCL系统拔出磁盘Intent.ACTION_MEDIA_EJECT不触发。
			//挂载 插入拔出都调用
//			filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
//		}
		// 必须要有此行，否则无法收到广播
		filter.addDataScheme("file");
		context.registerReceiver(this, filter);
		
	}
	static Runnable tostRunnable = new Runnable() {
		@Override
		public void run() {
			if(usbStr != null){
				toast.show();
			}
		}
	};

	@Override
	public void onReceive(final Context context, final Intent intent) {
		Log.v(TAG, "usb action = " + intent.getAction());
		final File path = new File(DownloadResourceManager.getInstance().getDownLoadPath());
		final String action = intent.getAction();
		// if(!path.getParentFile().exists()||path.getParentFile().length()<=0){
		// if(BuildConfig.DEBUG)Toast.makeText(context,
		// "!path.exists()"+!path.exists(), 0).show();
		// DownloadResourceManager.getInstance().clearDLData();
		// }
		toast.setText("优盘 已经拔出......");
		new Thread() {
			public void run() {
				
					if (action.equals(Intent.ACTION_MEDIA_MOUNTED)
							|| action.equals(Intent.ACTION_MEDIA_CHECKING)
							) {
						usbStr = null;
					} else {
						usbStr = "";
					}
				
					
					if (path.getParent() == null) {
						DownloadResourceManager.getInstance().clearDLData();
						Log.e(TAG, "下载路径不可用...");
						return;
					}
					SystemClock.sleep(1000);
//					Log.e("path.getParent().toString() ++++++++++++++", path.getParent().toString()+"++"+!DownloadResourceManager.getInstance().isDownRootExists(path.getParent().toString()));
					if (!DownloadResourceManager.getInstance().isDownRootExists(path.getParent().toString())) {
						Log.e(TAG, "文件夹 被移除...................");
						DownloadResourceManager.getInstance().clearDLData();
						handler.post(tostRunnable);
					}else{
						handler.removeCallbacks(tostRunnable);
					}
			};
		}.start();

	}

}
