package com.voole.epg.download.base;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.voole.epg.corelib.model.auth.AuthManager;
import com.voole.epg.corelib.model.proxy.ProxyManager;
import com.voole.epg.download.DownLoadService;
import com.voole.epg.download.TVProgressDialog;
import com.voole.epg.download.base.TVAlertDialog.Builder;
import com.voole.epg.download.bean.DownloadResourceManager;
import com.voole.epg.download.bean.FilmDownLoad4k;
import com.voole.epg.download.bean.StandardAuth;
import com.voole.epg.download.bean.StandardProxy;
import com.voole.epg.download.utils.Config;
import com.voole.epg.f4k.R;
import com.voole.tvutils.ImageManager;
import com.voole.tvutils.NetUtil;

public abstract class BaseActivity extends Activity {
	private static final int TIMEOUT = 4000;
	protected static final int ACCESS_NET_FAIL = 0x10000001;
	protected static final int ACCESS_WWW_FAIL = 0x20000001;
	private TVProgressDialog progressDialog = null;

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			if (BaseActivity.this.isFinishing()) {
				return;
			}
			if (!checkNetWork()) {
				return;
			}
			if (msg.what == ACCESS_WWW_FAIL) {
				if (netErrorDialog==null||!netErrorDialog.isShowing()) {
					netErrorDialog = getNetErrorDialog();
					netErrorDialog.show();
				}
				return;
			}
			
			cancelDialog();
			if (msg.what == ACCESS_NET_FAIL) {
				new TVAlertDialog.Builder(BaseActivity.this)
						.setCancelable(false)
						.setTitle(R.string.common_access_net_fail)
						.setPositiveButton(R.string.common_ok_button,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										BaseActivity.this.setResult(800);
										BaseActivity.this.finish();
									}
								}).create().show();
			} else {
				doHandleMessage(msg.what, msg.obj);
//				Toast.makeText(getApplicationContext(), "ACCESS_WWW_FAIL-----------www is good", 0).show();
				if(netErrorDialog!=null&&netErrorDialog.isShowing()){
					netErrorDialog.cancel();
					netErrorDialog = null;
				}
			}
		}
	};
	private TVAlertDialog netErrorDialog;
//	private UsbStateReceiver receiver;
	private DLStateReceiver mNetStateReceiver;

	protected abstract void doHandleMessage(int what, Object obj);
	/**
	 * 测试互联网是否联通
	 * @param string
	 */
	protected void checkNetwork(final String string) {
		new Thread(){
			public void run() {
				HttpURLConnection conn = null;
				try {
					URL url = new URL(string);
					conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(TIMEOUT);
					conn.setConnectTimeout(TIMEOUT);
					int code =  conn.getResponseCode();
					Log.e(this.getClass().getName(),code+"         httpcode");
					if (code==-1) {
						handler.sendEmptyMessage(ACCESS_WWW_FAIL);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(ACCESS_WWW_FAIL);
				}finally{
					if (conn!=null) {
						conn.disconnect();
					}
				}
			};
		}.start();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DownloadResourceManager.getInstance().initContext(this);
		ImageManager.GetInstance().init(this);
		new Thread(){
			public void run() {
				try{
					DownloadResourceManager.getInstance().initAuthConfigPath();
					if (AuthManager.GetInstance()==null) {
						AuthManager.GetInstance().init(new StandardAuth(getApplicationContext()));
					}
					if (!AuthManager.GetInstance().isAuthRunning()) {
						AuthManager.GetInstance().startAuth();
					}
				}catch(NullPointerException e){
					Log.e(BaseActivity.this.getClass().getName(), "NullPointerException 没有初始化StandardAuth",e);
					AuthManager.GetInstance().init(new StandardAuth(getApplicationContext()));
				}
				
				try{
					if (ProxyManager.GetInstance()==null) {
						ProxyManager.GetInstance().init(new StandardProxy(getApplicationContext()));
					}
					if (!ProxyManager.GetInstance().isProxyRunning()) {
						ProxyManager.GetInstance().startProxy();
					}
				}catch(NullPointerException e){
					ProxyManager.GetInstance().init(new StandardProxy(getApplicationContext()));
					Log.e(BaseActivity.this.getClass().getName(), "NullPointerException 没有初始化StandardProxy",e);
				}
				List<String> paths = DownloadResourceManager.getInstance().getFileDir();
				if(paths == null){
					return ;
				}
				LinkedHashMap<String, FilmDownLoad4k> downStatus = DownloadResourceManager.getInstance().getDLStatus();
				if(downStatus == null){
					File path = new File(DownloadResourceManager.getInstance().getDownLoadPath());
					if (TextUtils.isEmpty(DownloadResourceManager.getInstance()
							.getDownLoadPath())
					|| !path.getParentFile().exists() || !path.exists()){//路径不存在
						for (String checkPath : paths) { //初始化一个存在voole.db的路径 
							com.voole.epg.download.utils.Log.e(checkPath);
							File filePath =  new File(checkPath+"/voole_video");
							//获得当前路径下的所有文件和文件夹
					        File[] allFiles = filePath.listFiles();
					     	if(allFiles == null){
								return;
							}
					         //循环所有路径
					         for(int i = 0;i < allFiles.length;i++){
					        	 	//如果是文件
					               if(allFiles[i].isFile()){ 
				                    //执行操作，例如输出文件名
				                    System.out.println("--->"+allFiles[i].getName());
				                    if("voole.db".equalsIgnoreCase(allFiles[i].getName())){
				                    	DownloadResourceManager.getInstance().initDownPath(checkPath);
				                    	break;
				                    }
					              }
					         }
							
						}
						
					}
				}
				
			};
		}.start();
		
		Log.e(this.getClass().getName(), "onCreate");
		// showMessage("完全自定义Toast完全自定义Toast完全自定义Toast完全自定义Toast完全自定义Toast完全自定义Toast");
	}

	public void showDialog() {
		
		checkNetwork("http://www.baidu.com");
		
		if (progressDialog == null) {
			progressDialog = new TVProgressDialog(this);
			progressDialog.show();
		}
	}

	public void cancelDialog() {
		if (progressDialog != null) {
			progressDialog.setCancelable(true);
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	protected void sendMessage(int what) {
		Message message = Message.obtain();
		message.what = what;
		handler.sendMessage(message);
	}

	protected void showMessage(String msg) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.f4k_message, null);
		TextView textView = (TextView) layout.findViewById(R.id.msg);
		textView.setText(msg);
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		TVToast.show(toast, 5000);
	}

	@Override
	protected void onResume() {
		Log.e(this.getClass().getName(), "onResume");
		startDLService();
		new Thread(){
			public void run(){
				DownloadResourceManager.getInstance().getDownloadController().DLDownContinue();
			}
		}.start();

		super.onResume();
	}

	@Override
	protected void onStop() {
		Log.e(this.getClass().getName(), "onStop");
		unRegisterNetReceiver();
		super.onStop();
	}

	
	@Override
	protected void onStart() {
		Log.e(this.getClass().getName(), "onStart");
		registerNetReceiver();
		super.onStart();
	}
	
	
	@Override
	protected void onRestart() {
		Log.e(this.getClass().getName(), "onRestart");
		super.onRestart();
	}
	
	
	
	@Override
	protected void onDestroy() {
		Log.e(this.getClass().getName(), "onDestroy");
		super.onDestroy();
	}
	private void unRegisterNetReceiver(){
    	if (mNetStateReceiver!=null) {
    		this.unregisterReceiver(mNetStateReceiver);
    		mNetStateReceiver = null;
		}
    }
	
	
	/**
	 * 检测内网环境是否联通
	 * @return
	 */
	private boolean checkNetWork(){
    	if(!NetUtil.isNetWorkAvailable(this)){
    		if (netErrorDialog!=null&&netErrorDialog.isShowing()) {
				return false;
			}else{
				netErrorDialog = getNetErrorDialog();
				netErrorDialog.show();
			}
    		return false;
    	}
    	return true;
    }

	private TVAlertDialog getNetErrorDialog() {
		Builder netErrorBuilder = new TVAlertDialog.Builder(BaseActivity.this);
		return netErrorBuilder.setCancelable(false)
		.setTitle(R.string.common_net_error)
		.setPositiveButton(R.string.common_ok_button,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						BaseActivity.this.setResult(800);
						BaseActivity.this.finish();
					}
			}).create();
	}
	public TVAlertDialog getSimpleDialog(String msg) {
		Builder netErrorBuilder = new TVAlertDialog.Builder(BaseActivity.this);
		return netErrorBuilder.setCancelable(false)
				.setTitle(msg)
				.setPositiveButton(R.string.common_ok_button,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						BaseActivity.this.setResult(800);
						BaseActivity.this.finish();
					}
				}).create();
	}
	
	
	
	
	
	public TVAlertDialog getOrderDialog(String msg) {
		com.voole.epg.download.base.TVAlertDialog.Builder netErrorBuilder = new TVAlertDialog.Builder(BaseActivity.this);
		return netErrorBuilder.setCancelable(false)
		.setTitle(msg)
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
					}
			}).setPositiveButton(R.string.order, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,int which) {
					boolean flag = false;
					try{
						Intent mIntent = new Intent("android.intent.action.MAIN"); 
						mIntent.putExtra("tz","3");
						ComponentName comp = new ComponentName("com.voole.webepg", "com.voole.webepg.NewStartup");
						mIntent.setComponent(comp);
						mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mIntent.addCategory("android.intent.category.LAUNCHER");
						BaseActivity.this.startActivity(mIntent);
					}catch(Exception e){
						try{
						Intent consumerRechargeIntent = new Intent();
						if (BaseActivity.this.getPackageName().equalsIgnoreCase("com.voole.epgfor4k")) {
							consumerRechargeIntent.setAction("com.voole.epg.action.myvoole.comsumer.Recharge_tcl");
						}else{
							consumerRechargeIntent.setAction("com.voole.epg.action.myvoole.comsumer.Recharge");
						}
						//com.voole.epg.action.myvoole.comsumer.Recharge
						BaseActivity.this.startActivity(consumerRechargeIntent);
						flag = true;
						}catch(Exception e1){
							Toast.makeText(BaseActivity.this, R.string.jump_recharge_fail_c, 0).show();
						}
						if (!flag) {
							Toast.makeText(BaseActivity.this, R.string.jump_recharge_fail_b, 0).show();
						}
						
					}
				}
			}).create();
	}
	private void registerNetReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Config.WWW_ERROR_ACTION);
		filter.addAction(Config.DISK_NOT_ENOUGH_ACTION);
        mNetStateReceiver = new DLStateReceiver();
        this.registerReceiver(mNetStateReceiver, filter);
    }
	
	public void setProgressDialogCancelable(boolean cancelable) {
		if (progressDialog!=null) {
			progressDialog.setCancelable(cancelable);
		}
	}
	
	private void startDLService() {
		if (DownloadResourceManager.getInstance().getDownLoadFlag()==null||!DownloadResourceManager.getInstance().getDownLoadFlag().equals("1")) {
			Log.e(this.getClass().getName(), "未打开下载开关");
			return;
		}
		Intent service = new Intent();
		service.setClass(this, DownLoadService.class);
		this.startService(service);
	}
	
}
