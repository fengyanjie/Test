package com.voole.epg.download.base;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.voole.epg.download.utils.Config;
import com.voole.epg.f4k.BuildConfig;
import com.voole.epg.f4k.R;
/**
 * 
 * 监听下载过程中出现的问题的广播
 *
 */
public class DLStateReceiver extends BroadcastReceiver {

	private AlertDialog alertDialog = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (context == null) {
			return;
		}
		/**
		 * 判断是否连接外网的方法： 接收Intent(ConnectivityManager.CONNECTIVITY_ACTION); 判断
		 * intent.putExtra(ConnectivityManager.EXTRA_INET_CONDITION, isConnected
		 * ? 100:0 );----------连接外网值为100
		 * 取值intent.putExtra(ConnectivityManager.EXTRA_NETWORK_INFO, new
		 * NetworkInfo(info));，，，，知道是对应哪个网络。
		 */

		if (BuildConfig.DEBUG)
			Toast.makeText(context, "互联网连接异常...", 0).show();
		String action = intent.getAction();
		showDialog(context, action);

		// if(!isNetWorkAvailable(context)){
		// showDialog(context);
		// }else{
		// cancelDialog();
		// }
		/*
		 * ConnectivityManager mConnectivityManager = (ConnectivityManager)
		 * context .getSystemService(Context.CONNECTIVITY_SERVICE); NetworkInfo
		 * mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); if
		 * (mNetworkInfo != null && mNetworkInfo.isAvailable()) { //
		 * mNetworkInfo.isAvailable(); Toast.makeText(context, "网络连接有异常，请检测网络",
		 * Toast.LENGTH_LONG).show(); if (!isShwoDialog) { new
		 * AlertDialog.Builder(context) .setTitle("提示")
		 * .setMessage("网络连接有异常，请检测网络") .setPositiveButton("确定", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * isShwoDialog = false; } }).show(); isShwoDialog = true; } }
		 */
		/*
		 * if(intent.getAction().equals("ETH_STATE")){ // int ethState =
		 * intent.getIntExtra("dhcp_state", 0); new AlertDialog.Builder(context)
		 * .setTitle("提示") .setMessage("网络连接有异常，请检测网络") .setPositiveButton("确定",
		 * new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * 
		 * } }).show(); }
		 */
	}

	public static boolean isNetWorkAvailable(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connec == null)
			return false;

		NetworkInfo[] allinfo = connec.getAllNetworkInfo();
		if (allinfo != null) {
			for (int i = 0; i < allinfo.length; i++) {
				if (allinfo[i].isAvailable() && allinfo[i].isConnected()) {
					return true;
				}
			}
		}
		return false;

	}

	private void showDialog(Context context,String action) {
		if (alertDialog == null) {
			alertDialog = new AlertDialog.Builder(context)
			.setTitle("网络异常，请检查网络连接！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).create();

		}
		
		if (Config.WWW_ERROR_ACTION.equalsIgnoreCase(action)) {
			alertDialog.setTitle(context.getString(R.string.common_net_error));
		}else if(Config.DISK_NOT_ENOUGH_ACTION.equalsIgnoreCase(action)){
			alertDialog.setTitle(context.getString(R.string.disk_freespace_not_enough));
		}
		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}

	}

	private void cancelDialog() {
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
			alertDialog = null;
		}
	}

}
