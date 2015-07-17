package com.voole.epg.download;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;

import com.voole.epg.corelib.model.auth.AuthManager;
import com.voole.epg.corelib.model.proxy.ProxyManager;
import com.voole.epg.download.bean.DLError;
import com.voole.epg.download.bean.DownloadResourceManager;
import com.voole.epg.download.bean.FilmDownLoad4k;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;
import com.voole.epg.download.bean.StandardAuth;
import com.voole.epg.download.bean.StandardProxy;
import com.voole.epg.download.utils.Config;
import com.voole.epg.download.utils.Log;
import com.voole.epg.f4k.BuildConfig;
import com.voole.util.net.HttpDownloaderUtil;
import com.voole.util.prop.PropertiesUtil;

public class DownLoadService extends Service {
	private static final String TAG = "DownLoadService";
	private static final int TIMEOUT = 4000;
	private boolean loop;
	private  Runnable dataThread;
	private int time  = 0;
	public static double realspeed;
	private int timecount  = 8;
	private  String proxyVersion = null;
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	BroadcastReceiver homeReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			if (Config.TCL_HOME_ACTION.equalsIgnoreCase(intent.getAction())) {
				Log.d("android.intent.action.ENTER_HOME is received msg");
				if(TextUtils.isEmpty(PropertiesUtil.getProperty(context, "isDownBackground")) || !Boolean.parseBoolean(PropertiesUtil.getProperty(context, "isDownBackground"))){
					stopSelf();
				} else { // 后台下载
					boolean isDownLoding = false;
					LinkedHashMap<String, FilmDownLoad4k> downStatus = DownloadResourceManager.getInstance().getDLStatus();
					if(downStatus != null ){
						Set<Entry<String,FilmDownLoad4k>> entrySet = downStatus.entrySet();
						for(Entry<String,FilmDownLoad4k> entry : entrySet){
							if(entry.getValue().downType == DownType.DOWNLOADING){
								isDownLoding = true;
							}
						}
					}
					if(isDownLoding){ //下载中
						new Thread(){
							public void run() {
								com.voole.epg.download.utils.Log.d("setMaxBand"+realspeed);
								String maxBand = DownloadResourceManager.formetFileSize(realspeed,"isKB");
								DownloadResourceManager.getInstance().setMaxBand(Integer.parseInt(maxBand));
							};
						}.start();
					} else {
						stopSelf();
					}
				}
			}
		}

		private void stopSelf() {
			loop = false;
			DownLoadService.this.stopSelf();
			LinkedHashMap<String, FilmDownLoad4k> downStatus = DownloadResourceManager.getInstance().getDLStatus();
			if(downStatus != null){
				DownloadResourceManager.getInstance().writeDownStatus(downStatus);
			}
		};
	};
	private UsbStateReceiver usbReceiver;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		registStopReceiver();
		registUsbReceiver();
		doGetDLstatus();
		super.onCreate();
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("onStartCommand++++++++++++++++++++++++++++++++++++++++++");
		new Thread(){
			public void run() {
				DownloadResourceManager.getInstance().initContext(getApplicationContext());
				DownloadResourceManager.getInstance().initAuthConfigPath();
				DownloadResourceManager.getInstance().setMaxBand(0);
			};
		}.start();
		return super.onStartCommand(intent, flags, startId);
	}

	private void doGetDLstatus() {
		loop =true;
		Log.d(TAG, this+"  onStartCommand  "+"开始获取状态");
			dataThread = new Runnable() {
				@Override
				public void run() {
					while (loop) {
						
//						checkNetwork("http://www.baidu.com", DownLoadService.this);
						
						checkAuthandProxy();
						if (BuildConfig.DEBUG)Log.d("proxyVersion: "+proxyVersion+"  "+Thread.currentThread().getId());
						SystemClock.sleep(Config.UPDATA_TIME);
						
						LinkedHashMap<String, FilmDownLoad4k> downStatus = DownloadResourceManager.getInstance().getDLStatus();
						if (downStatus == null) {
							SystemClock.sleep(Config.UPDATA_TIME);
							continue;
						}
						Set<Entry<String, FilmDownLoad4k>> entrySet = downStatus.entrySet();
						try {
							for (Entry<String, FilmDownLoad4k> entry : entrySet) {
								if (entry.getValue().downType==DownType.FINISH) {
									continue;
								}
								if (entry.getValue().downType == DownType.PAUSE) {
									continue;
								}
								if (entry.getValue().downType==DownType.WAITING) {
									DownloadResourceManager.getInstance().putFilm(entry.getValue(),true);
									continue;
								}
								
								FilmDownLoad4k film = DownloadResourceManager.getInstance().getDLstaus(entry.getValue());
								if (timecount>0&&timecount<=8) { // 记录开始前10s下载速度
									if(realspeed < film.realspeed){
										realspeed = film.realspeed;
									}
									timecount--;
								}
								
								
								if (film != null) {
									DownloadResourceManager.getInstance().putFilm(film,true);
								} else {
									checkDLError();
									DownloadResourceManager.getInstance().DlContinue(entry.getValue());
								}
								
								if (!DownloadResourceManager.getInstance().isDLDownding(entry.getValue())&&entry.getValue().downType==DownType.DOWNLOADING) {
									DownloadResourceManager.getInstance().DlContinue(entry.getValue());
								}
								
							}
						} catch (Exception e) {
//							Log.e(DownLoadService.this.getClass().getName(),e.getLocalizedMessage(), e);
							SystemClock.sleep(Config.UPDATA_TIME);
						}
					}
					DownloadResourceManager.getInstance().DLPause(null);
					
				}
			};
		
			exec.execute(dataThread);
	}

	private void registUsbReceiver() {
		usbReceiver = new UsbStateReceiver();
		usbReceiver.registerReceiver(this);
	}
	
	private void unRegistUsbReceiver(){
		if (usbReceiver!=null) {
			this.unregisterReceiver(usbReceiver);
			usbReceiver = null;
		}
	}

	private void registStopReceiver() {
		IntentFilter filter = new IntentFilter(Config.TCL_HOME_ACTION);
		this.registerReceiver(homeReceiver, filter);
	}
	
	private void unRegistStopReceiver(){
		this.unregisterReceiver(homeReceiver);
	}
	
	/**
	 * 测试互联网是否联通
	 * @param string
	 */
	protected void checkNetwork(final String string,final Context context) {
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
						time++;
					}else{
						time = 0;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					time++;
				}finally{
					if (conn!=null) {
						conn.disconnect();
					}
					if (time>=3) {
						Intent intent = new Intent();
						intent.setAction(Config.WWW_ERROR_ACTION);
						context.sendBroadcast(intent);
						time = 0;
					}
				}
			};
		}.start();
		
	}
	
	
	@Override
	public void onDestroy() {
		Log.e("onDestroy");
		loop = false;
		proxyVersion = null;
		if (exec!=null) {
			exec.shutdownNow();
			exec = null;
		}
		unRegistStopReceiver();
		unRegistUsbReceiver();
		super.onDestroy();
	}
	
	
	
	private void getProxyVersion() {
		if (TextUtils.isEmpty(proxyVersion)) {
			if (!TextUtils.isEmpty(ProxyManager.GetInstance().getProxyServer())) {
				String urlStr = ProxyManager.GetInstance().getProxyServer()+"/info";
				String result = HttpDownloaderUtil.readString(urlStr);
				if (!TextUtils.isEmpty(result)) {
					Log.d(result);
					try {
						XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
						XmlPullParser parser = parserFactory.newPullParser();
						parser.setInput(new ByteArrayInputStream(result.getBytes()),"UTF-8");
						int eventType = parser.getEventType();
						while (eventType != XmlPullParser.END_DOCUMENT) {
							String tagname = parser.getName();
							if ("version".equalsIgnoreCase(tagname)) {
								proxyVersion = parser.nextText();
								if (!TextUtils.isEmpty(proxyVersion)) {
									proxyVersion = proxyVersion.trim();
									String[] split = proxyVersion.split(" ");
									proxyVersion = split[0];
									int version = TextUtils.isEmpty(proxyVersion)?9:Integer.parseInt(proxyVersion);//默认使用最新的代理
									DownloadResourceManager.getInstance().initDownController(version);
								}
							}
							eventType = parser.next();
						}
						
						
					} catch (XmlPullParserException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
	}
	
	/**
	 * 检查是否含有可处理的下载错误
	 */
	private void checkDLError() {
		if (DownloadResourceManager.getInstance().getDownloadController()!=null) {
			DLError dlError = DownloadResourceManager.getInstance().getDownloadController().getDlError();
			if (dlError!=null) {
				if (dlError.value==1006) {
					Intent intent = new Intent();
					intent.setAction(Config.DISK_NOT_ENOUGH_ACTION);
					DownLoadService.this.sendBroadcast(intent);
				}
				Log.d(dlError.toString());
			}
		}
	}
	
	private void checkAuthandProxy() {
		try{
			if (AuthManager.GetInstance()==null) {
				AuthManager.GetInstance().init(new StandardAuth(getApplicationContext()));
			}
			if (!AuthManager.GetInstance().isAuthRunning()) {
				AuthManager.GetInstance().startAuth();
			}
		}catch(NullPointerException e){
			Log.e(DownLoadService.this.getClass().getName(), "NullPointerException 没有初始化StandardAuth",e);
			AuthManager.GetInstance().init(new StandardAuth(getApplicationContext()));
		}
		
		try{
			if (ProxyManager.GetInstance()==null) {
				ProxyManager.GetInstance().init(new StandardProxy(getApplicationContext()));
			}
			if (!ProxyManager.GetInstance().isProxyRunning()) {
				ProxyManager.GetInstance().startProxy();
			}
			
			getProxyVersion();
			
			
			
		}catch(NullPointerException e){
			ProxyManager.GetInstance().init(new StandardProxy(getApplicationContext()));
			Log.e(DownLoadService.this.getClass().getName(), "NullPointerException 没有初始化StandardProxy",e);
		}
	}
}
