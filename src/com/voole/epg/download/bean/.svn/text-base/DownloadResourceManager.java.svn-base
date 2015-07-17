package com.voole.epg.download.bean;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.voole.epg.corelib.model.auth.AuthManager;
import com.voole.epg.corelib.model.auth.User;
import com.voole.epg.corelib.model.proxy.ProxyManager;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;
import com.voole.epg.download.tools.ReadCopyWriteObject;
import com.voole.epg.download.utils.Config;
import com.voole.epg.player.PlayItem;
import com.voole.epg.player.VoolePlayer;
import com.voole.util.exec.ExecUtil;
import com.voole.util.prop.PropertiesUtil;

/**
 * 
 * 下载资源的管理类
 *
 */
public class DownloadResourceManager {

	private  final String VOOLE_DB = "voole.db";
	private  String authPort = "8080";
	private  String proxyPort = "5656";
//	private  String proxyLocalPort = proxyPort;
//	private  final String BASEURL_CONTROL = "http://127.0.0.1:"+proxyPort+"/dlcontrol";
//	private  final String BASEURL_DOWNLOAD = "http://127.0.0.1:"+proxyPort+"/downloadm3u8";
//	private  final String BASEURL_PLAY = "http://127.0.0.1:"+proxyPort+"/playlocal";
//	private  final String BASEURL_DOWN_STATUS = "http://127.0.0.1:"+proxyPort+"/getdlstatus";
//	private  final String BASEURL = "http://127.0.0.1:"+proxyPort;
	private  final String TAG = "DownloadResourceManager";
	private  LinkedHashMap<String,FilmDownLoad4k> downStatus;
	private  String path = "/data/data/com.voole.epg/files/"+Config.AUTH_RT_CONF;
	private  Context context;
	private  IDownloadControl controller;
	
	private DownloadResourceManager(){}
	
	private  static DownloadResourceManager INSTANCE = null;
	
	public static  DownloadResourceManager getInstance(){
		synchronized (DownloadResourceManager.class) {
			if (INSTANCE==null) {
				INSTANCE = new DownloadResourceManager();
			}
			return INSTANCE;
		}
	}
	
	public  void initdb(LinkedHashMap<String,FilmDownLoad4k> db){
		if(downStatus==null){
			downStatus = db;
		}
	}
	
	
	public  synchronized void setDownloadControl(IDownloadControl controller){
		this.controller = controller;
	}
	
	public  synchronized IDownloadControl getDownloadController(){
		return controller;
	}
	/**
	 * 暂停下载
	 * @param fid_download
	 * @return
	 */
	public  synchronized boolean DLPause(FilmDownLoad4k film){
		return controller.DLStop(film);
	}
	
	/**
	 * 继续/开始下载
	 * @param fid_download
	 * @return
	 */
	public  synchronized boolean DlContinue(FilmDownLoad4k film){
		com.voole.epg.download.utils.Log.d("DlContinue");
		return controller.DlContinue(film);
	}
	
	/**
	 * 删除下载
	 * @param fid_download
	 * @return
	 */
	public  synchronized boolean DlDel(FilmDownLoad4k film){
		com.voole.epg.download.utils.Log.d("DlDel");
		return controller.DLdelete(film);
				
	}
	
	
	
	
	/**
	 * http://127.0.0.1:5656/downloadm3u8?url='http://121.10.173.122/skyts01.m3u8'
		 其中http://121.10.173.122/skyts01.m3u8是epg返给你的播放串。epg还会返给你电影名称等其它细节。
		 
		下载任务创建成功与否，会给你以http response形式返回给你如下xml：
		<?xml version="1.0"?>
		<status>
		<value>0</value>
		<fid>1234567890abcdef1234567890abcdef</fid>
		<description>create download task OK!</description>
		</status>

	 */
	/**
	 * 创建一个下载,
	 * @param playUrl 为epg给定的播放地址
	 * @return
	 */
	public  synchronized FilmDownLoad4k DlCreate(String playUrl){
		com.voole.epg.download.utils.Log.d("DlCreate");
		return controller.DLCreate(playUrl);
	}
	
	/**
	 * 设置最大带宽 
	 * @param 
	 * @return
	 */
	public synchronized boolean setMaxBand(int realspeed){
		return controller.setMaxBand(realspeed);
	}
	
	public  synchronized String getDLPlayUrl(FilmDownLoad4k film){
		return controller.getDLPlayUrl(film);
	}
	
	/**
	 * 另外，需要增加开关在vooleauth.conf中以控制下载，我已经增加了如下开关：
		write_ts_to_disk=1
		udisk_root_dir=/mnt/usb/usb_sda1/voole_video

	 */
	
	public  void initAuthConfigPath(){
		if (context==null) {
			Log.e(TAG, "context is null please call the  initContext() first");
			return;
		}
		path = "/data/data/"+context.getPackageName()+"/files/"+Config.AUTH_RT_CONF;
//		path2 = "/data/data/"+context.getPackageName()+"/files/"+Config.AUTH_CONF;
		Config.MAX_TASK = TextUtils.isEmpty(PropertiesUtil.getProperty(context, "max_down_task"))?Config.MAX_TASK:Integer.parseInt(PropertiesUtil.getProperty(context, "max_down_task"));
		proxyPort = getProxyPort();
		authPort = getAuthPort();
//		proxyLocalPort = getProxyLocalPort();
		ExecUtil.VOOLE_AUTH = getAuthName();
		ExecUtil.VOOLE_PROXY = getPorxyName();
		initDownController(9);
	}
	
	/**
	 * 初始化老代理   
	 * 小于8 则为老代理
	 */
	public  void initDownController(int proxyVersion) {
		if (proxyVersion>8) {
			controller = new DownController_new(proxyPort);
		}else{
			controller = new DownController(proxyPort);
		}
	}
	

	/**
	 * 获取本地proxy播放的端口
	 * @param key  local_http_port 
	 * @return 
	 */
	public  String getProxyLocalPort(){
		String path = getDlPropFromKey("local_agent_http_play_local_port");
		if (TextUtils.isEmpty(path)) {
			return proxyPort;
		}
		path = trimQuotationMark(path);
		return path;
	}

	
//	public  void exec(String cmd){
//		Process exec;
//		try {
//			exec = Runtime.getRuntime().exec(cmd);
//			InputStream in = exec.getInputStream();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	public  void initDownPath(String path) {
//		String maxPath = getDownLoadPath();
//		try {
//			Process exec = Runtime.getRuntime().exec("df");
//			in = exec.getInputStream();
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//			String str = null;
//			while (!TextUtils.isEmpty(str = bufferedReader.readLine())) {
//				str = str.split(" ")[0];
//				if (str.startsWith("/mnt/")) {
//					if (new File(str).getUsableSpace()>new File(maxPath).getUsableSpace()) {
//						maxPath = str;
//						com.voole.epg.download.utils.Log.e(str);
//					}
//				}
//			}
//			in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally{
//			if (in!=null) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		BufferedReader bufferedReader = null;
//		try {
//			String mountsPath = "/proc/mounts";
//			File mountsFile = new File(mountsPath);
//		    bufferedReader = new BufferedReader(new FileReader(mountsFile));
//			String str = null;
//			while (!TextUtils.isEmpty(str = bufferedReader.readLine())) {
//			if (!str.contains("/mnt")) {
//				continue;
//			}
//			str = str.split(" ")[1];
//			if (str.startsWith("/mnt/")) {
//				if (new File(str).getUsableSpace()>new File(maxPath).getUsableSpace()) { //分区已用空间
//					maxPath = str;
//					com.voole.epg.download.utils.Log.e(str);
//				}
//			}
//		}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch(NullPointerException e){
//			e.printStackTrace();
//		}finally{
//			if (bufferedReader!=null) {
//				try {
//					bufferedReader.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		
//		
//		if (!TextUtils.isEmpty(maxPath)) {
//			maxPath = maxPath.trim();
//			if (!maxPath.contains("voole_video")) {
//				maxPath = maxPath.concat("/voole_video");
//				if (!maxPath.contains("mnt/asec")&&!maxPath.contains("mnt/secure")&&!maxPath.contains("mnt/obb")&&!maxPath.contains("mnt/private")&&!maxPath.contains("mnt/sdcard")&&!maxPath.contains(Environment.getExternalStorageDirectory().toString())) {
//					setDownLoadPath(maxPath);
//					new File(maxPath).mkdirs();
//					new Thread(){
//						public void run() {
//							ProxyManager.GetInstance().exitProxy();
//							SystemClock.sleep(1000);
//							ProxyManager.GetInstance().startProxy();
//						};
//					}.start();
//				}
//			}
//		}
		if (!TextUtils.isEmpty(path)) {
			path = path.trim();
			if (!path.contains("voole_video")) {
				path = path.concat("/voole_video");
				if (!path.contains("mnt/asec")&&!path.contains("mnt/secure")&&!path.contains("mnt/obb")&&!path.contains("mnt/private")&&!path.contains("mnt/sdcard")&&!path.contains(Environment.getExternalStorageDirectory().toString())) {
					setDownLoadPath(path);
					com.voole.epg.download.utils.Log.e(path);
					new File(path).mkdirs();
					new Thread(){
						public void run() {
							ProxyManager.GetInstance().exitProxy();
							SystemClock.sleep(100);
							ProxyManager.GetInstance().startProxy();
						};
					}.start();
				}
				
			}
		}
	}
	
//	 private List<String> items = null;//存放名称  
	/**
	 * 获得指定目录文件
	 * @param filePath
	 */
	 @SuppressWarnings("resource")
	public List<String> getFileDir() {
		 List<String> paths = null;//存放路径  
		 
		 BufferedReader bufferedReader = null;
		 String mountsPath = "/proc/mounts";
		 String str = null;
		 String subStr = null;
		 File mountsFile = new File(mountsPath);
		 try {
			bufferedReader = new BufferedReader(new FileReader(mountsFile));
			while (!TextUtils.isEmpty(str = bufferedReader.readLine())) {
				if (!str.contains("/mnt")) {
					continue;
				}
				subStr = str.split(" ")[1];
				if (subStr.startsWith("/mnt/")) {
					if (!subStr.contains("mnt/shell")&&!subStr.contains("mnt/asec")&&!subStr.contains("mnt/secure")&&!subStr.contains("mnt/obb")&&!subStr.contains("mnt/private")&&!subStr.contains("mnt/sdcard")&&!subStr.contains(Environment.getExternalStorageDirectory().toString())) {
						if(new File(subStr).getTotalSpace() > 2f*1024*1024*1024){//磁盘小于2G排除
							com.voole.epg.download.utils.Log.e(subStr);
							if (paths==null) {
								paths = new ArrayList<String>();
							}
							paths.add(subStr);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} 
		return paths;  
    }  
	
	public  Properties getDlProp(){
		File propFile = new File(path);
		FileInputStream fin = null;
		if (propFile.exists()&&propFile.length()>0) {
			Properties prop = new Properties();
			try {
				fin = new FileInputStream(propFile);
				prop.load(fin);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				try {
					if (context!=null) {
						prop = new Properties();
						prop.load(context.getAssets().open("voolert.conf"));
					}
					return prop;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (fin!=null) {
					try {
						fin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return prop;
		}else{
			try {
				Properties prop = null;
				if (context!=null) {
					prop = new Properties();
					prop.load(context.getAssets().open("voolert.conf"));
				}
				return prop;
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public  Properties getDlProp(String path){
		File propFile = new File(path );
		FileInputStream fin = null;
		if (propFile.exists()&&propFile.length()>0) {
			Properties prop = new Properties();
			try {
				fin = new FileInputStream(propFile);
				prop.load(fin);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (fin!=null) {
					try {
						fin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return prop;
		}
		return null;
	}
	
	/**
	 * 获取vooleauth.conf中的值
	 * @param key
	 * @return
	 */
	public synchronized  String getDlPropFromKey(String key){
		if (getDlProp()!=null) {
			Properties prop = getDlProp();
			return (String) prop.get(key);
		}
		return null;
	}
	
	/**
	 * 设置是否可以下载
	 * @param value  write_ts_to_disk
	 * @return
	 */
	public  boolean setDownLoadFlag(String value){
		
		return setDlProp("write_ts_to_disk", value);
	}
	/**
	 * 从配制文件获取是否可以下载
	 * @param key  write_ts_to_disk 
	 * @return 1是可以
	 */
	public  String getDownLoadFlag(){
		String flag = getDlPropFromKey("write_ts_to_disk");
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		flag = trimQuotationMark(flag);
		return flag;
	}
	/**
	 * 从配制文件获取下载路径
	 * @param key  udisk_root_dir 
	 * @return 设置的下载路径
	 */
	public  String getDownLoadPath(){
		String path = getDlPropFromKey("udisk_root_dir");
		if (TextUtils.isEmpty(path)) {
			return "";
		}
		path = trimQuotationMark(path);
		return path;
	}
	/**
	 * 获取下载auth的端口
	 * @param key  local_http_port 
	 * @return 
	 */
	public  String getAuthPort(){
		String path = getDlPropFromKey("local_http_port");
		if (TextUtils.isEmpty(path)) {
			return authPort;
		}
		path = trimQuotationMark(path);
		return path;
	}


	/**
	 * 去掉双引号
	 * @param path
	 * @return
	 */
	private  String trimQuotationMark(String path) {
		if (TextUtils.isEmpty(path)) {
			return path;
		}
		
//		if ("\"".equals(path.substring(0,1))) {
//			path = path.trim();
//			path = path.substring(1, path.length()-1);
//		}
		if (path.contains("\"")) {
			path  = path.replace("\"", "");
			if (!TextUtils.isEmpty(path)) {
				path = path.trim();
			}
		}
		return path;
	}
	/**
	 * 获取下载proxy的端口
	 * @param key  local_http_port 
	 * @return 
	 */
	public  String getProxyPort(){
		String path = getDlPropFromKey("local_agent_http_port");
		if (TextUtils.isEmpty(path)) {
			return proxyPort;
		}
		path = trimQuotationMark(path);
		return path;
	}
	/**
	 * 设置是否可以下载
	 * @param value  write_ts_to_disk
	 * @return
	 */
	public  boolean setDownLoadPath(String value){
		return setDlProp("udisk_root_dir", "\""+value+"\"");
	}
	
	
	
	
	public  boolean setDlProp(String key,String value){
		if (getDlProp()!=null) {
			try {
				Properties prop = getDlProp();
				prop.setProperty(key, value);
				prop.store(new FileOutputStream(new File(path)), value);
//				Properties prop2 = getDlProp(path2);
//				if (prop2!=null) {
//					prop2.setProperty(key, value);
//					prop2.store(new FileOutputStream(new File(path2)), value);
//					
//				}
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
		
	}
	
	
	public  synchronized FilmDownLoad4k getDLstaus(FilmDownLoad4k film){
		return controller.getDLstaus(film);
	}
	
	public  synchronized boolean isDLDownding(FilmDownLoad4k film){
		return controller.isDownLoad(film);
	}
	
	/**
	 * 查看是否曾经添加到下载列表
	 * @param fid_epg
	 * @return
	 */
	public  boolean checkifDownload(String fid_epg){
		downStatus = getDLStatus();
		if (downStatus!=null) {
			return downStatus.containsKey(fid_epg);
		}
		return false;
	}
	/**
	 * 根据epg_fid获取下载信息
	 * @param fid_epg
	 * @return
	 */
	public  synchronized FilmDownLoad4k getDLstaus(String fid_epg){
		downStatus = getDLStatus();
		if (downStatus != null) {
			return downStatus.get(fid_epg);
		}
		return null;
	}
	
	
	/**
	 * 从数据库里删除
	 * @return
	 */
	public  synchronized boolean delDLstatus(FilmDownLoad4k film){
		downStatus = getDLStatus();
		if (downStatus!=null && !TextUtils.isEmpty(film.fid_epg)) {
			FilmDownLoad4k remove = downStatus.remove(film.fid_epg);
			if(DlDel(film)){
				if (downStatus!=null) {
					downStatus.remove(film.fid_epg);
					return true;
				}
			}else{
				downStatus.put(remove.fid_epg, remove);
			}
		}
		return false;
	}
	
	
	/**
	 * 序列化各个状态
	 * @param downs
	 * @return
	 */
	public  synchronized boolean writeDownStatus(LinkedHashMap<String,FilmDownLoad4k> downs){
		com.voole.epg.download.utils.Log.e("writeDownStatus");
		ObjectOutputStream objout = null;
		try {
			String downPath = getDownLoadPath();
			File dbTmpFile = new File(downPath,VOOLE_DB+".tmp");
			objout = new ObjectOutputStream(new FileOutputStream(dbTmpFile));
			objout.writeObject(downs);
			objout.flush();
			File dbFile = new File(downPath,VOOLE_DB);
			downStatus = downs;
			return dbTmpFile.renameTo(dbFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (objout!=null) {
				try {
					objout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 从磁盘读去各个下载
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  synchronized LinkedHashMap<String,FilmDownLoad4k> getDLStatus(){
		/**
		 * a map 
		 * 
		 * key   fid_epg
		 * 
		 * value  FilmDownLoad4k
		 */
		
		
		LinkedHashMap<String,FilmDownLoad4k> downs = null;
		ObjectInputStream objIn = null;
		if (downStatus!=null) {
			return downStatus ;
		}
		try {
			String downPath = getDownLoadPath();
			if (!TextUtils.isEmpty(downPath)) {
				File file = new File(downPath,VOOLE_DB);
				
				if (file.exists()) {
					objIn = new ObjectInputStream(new FileInputStream(file));
					if (objIn != null) {
						downs  = (LinkedHashMap<String,FilmDownLoad4k>) objIn.readObject();
					}
				}else{
					Log.e(TAG, "voole.db does not exists==>" +file.getAbsolutePath());
				}
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			LinkedHashMap<String,com.voole.epg.f4k_download.domain.FilmDownLoad4k> oldDowns = null;
			ObjectInputStream oldObjIn = null;
			if (downStatus!=null) {
				return downStatus ;
			}
				String downPath = getDownLoadPath();
				if (!TextUtils.isEmpty(downPath)) {
					File file = new File(downPath,VOOLE_DB);
					
					if (file.exists()) {
						try {
							oldObjIn = new ObjectInputStream(new FileInputStream(file));
							if (oldObjIn != null) {
									oldDowns  = (LinkedHashMap<String,com.voole.epg.f4k_download.domain.FilmDownLoad4k>)oldObjIn.readObject();
							}
						} catch (StreamCorruptedException e1) {
							e1.printStackTrace();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}else{
						Log.e(TAG, "voole.db does not exists==>" +file.getAbsolutePath());
					}
				}
				if(oldDowns != null){
					downs = new LinkedHashMap<String, FilmDownLoad4k>();
					Set<Entry<String, com.voole.epg.f4k_download.domain.FilmDownLoad4k>> entrySet2 = oldDowns.entrySet();
					for (Entry<String, com.voole.epg.f4k_download.domain.FilmDownLoad4k> entry : entrySet2) {
						FilmDownLoad4k dest = new FilmDownLoad4k();
						dest = ReadCopyWriteObject.readCopyWrite(dest, entry.getValue());
						downs.put(dest.fid_epg, dest);
					}
				}
			
			e.printStackTrace();
		} finally{
			if (objIn!=null) {
				try {
					objIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		downStatus = downs;
		return downs;
		
	}
	
	
	/**
	 * bit 转换成其他进制单位
	 * bit 转换成KB
	 * @param size
	 * @return
	 */
	   public static String formetFileSize(double size, String isKB) {//转换文件大小
	        DecimalFormat df = new DecimalFormat("#.0");
	        String fileSizeString = "";
	        if("isKB".equalsIgnoreCase(isKB)){ //转换KB留整   40%
	        	 DecimalFormat dfKB = new DecimalFormat("0");
	        	 fileSizeString = dfKB.format((size / 1024) * 0.4);
	        } else {
	        	if (size < 1024) {
		            fileSizeString = df.format(size) + "B";
		        } else if (size < 1048576) {
		            fileSizeString = df.format(size / 1024) + "K";
		        } else if (size < 1073741824) {
		            fileSizeString = df.format(size / 1048576) + "M";
		        } else {
		            fileSizeString = df.format(size / 1073741824) + "G";
		        }
	        }
	        
	        return fileSizeString;
	    }
	   	
	   
	   public  void playFromLoacal(FilmDownLoad4k film,Activity activity){
		   if(!ProxyManager.GetInstance().isProxyRunning()){
				ProxyManager.GetInstance().startProxy();
			}
		   	String dlPlayUrl = getDLPlayUrl(film);
			List<PlayItem> playItems = new ArrayList<PlayItem>();
			PlayItem item = new PlayItem();
			item.setMid(film.Mid);
			item.setSid(film.Sid);
			item.setFid(film.fid_download);
			item.setFilmName(film.FilmName);
			item.setPlayUrl(dlPlayUrl);
			playItems.add(item);
			User user = AuthManager.GetInstance().getUser();
			if (user!=null) {
				VoolePlayer.GetInstance().startPlay(activity, playItems, 0, null, user.getOemid(), user.getUid(), user.getHid());
			}else{
				VoolePlayer.GetInstance().startPlay(activity, playItems, 0);
			}
			
	   }
	   
	   /**
	    * 清除缓冲的下载数据
	    */
	   public  synchronized void clearDLData(){
		   if (downStatus!=null) {
			   downStatus.clear();
			   downStatus = null;
		   }
	   }
	   
	   
	   /**
	    * 将其存入列表 并 限制同时下载条数
	    * @param film
	    */
	   public  synchronized void putFilm(FilmDownLoad4k film,boolean isUpdate){
		   if(getDLStatus()!=null){
			   if (isUpdate) {
				   if(getDLStatus().get(film.fid_epg) != null){
					   getDLStatus().get(film.fid_epg).averspeed = film.averspeed;
					   getDLStatus().get(film.fid_epg).cryptsize = film.cryptsize;
					   getDLStatus().get(film.fid_epg).currentsize = film.currentsize;
//					   getDLStatus().get(film.fid_epg).currentstatus = film.currentstatus;
					   getDLStatus().get(film.fid_epg).downType = film.downType;
					   getDLStatus().get(film.fid_epg).fid_download = film.fid_download;
					   getDLStatus().get(film.fid_epg).fid_epg = film.fid_epg;
					   getDLStatus().get(film.fid_epg).averspeed = film.averspeed;
					   getDLStatus().get(film.fid_epg).FilmName = film.FilmName;
					   getDLStatus().get(film.fid_epg).Mid = film.Mid;
					   getDLStatus().get(film.fid_epg).playUrl = film.playUrl;
					   getDLStatus().get(film.fid_epg).realspeed = film.realspeed;
					   getDLStatus().get(film.fid_epg).Sid = film.Sid;
					   getDLStatus().get(film.fid_epg).totalsize = film.totalsize;
				   }
			   }else{
				   getDLStatus().put(film.fid_epg, film);
			   }
			   
			   
			   
			   LinkedHashMap<String, FilmDownLoad4k> dlStatus = getDLStatus();
			   Set<Entry<String, FilmDownLoad4k>> entrySet = dlStatus.entrySet();
			   int i = 0;
			   for (Entry<String, FilmDownLoad4k> entry : entrySet) {
					if (entry.getValue().downType==DownType.DOWNLOADING) {
						i++;
					}
			   }
			  
			   
			   if (i>Config.MAX_TASK) {
				   for (Entry<String, FilmDownLoad4k> entry : entrySet) {
						if (entry.getValue().downType==DownType.DOWNLOADING&&!entry.getValue().fid_epg.equalsIgnoreCase(film==null?"":film.fid_epg)) {
							entry.getValue().downType = DownType.WAITING;
							DLPause(entry.getValue());
							i--;
							if (i==Config.MAX_TASK) {
								break;
							}
						}
				   }
			   }else{
				   for (Entry<String, FilmDownLoad4k> entry : entrySet) {
						if (entry.getValue().downType==DownType.WAITING) {
							if (i==Config.MAX_TASK) {
								break;
							}
							entry.getValue().downType = DownType.DOWNLOADING;
							i++;
						}
				   }
			   }
//			   if (film==null) {
//				return;
//			   }
//			   if(getDLStatus().get(film.fid_epg) != null&&isUpdate){
//				   getDLStatus().get(film.fid_epg).averspeed = film.averspeed;
//				   getDLStatus().get(film.fid_epg).cryptsize = film.cryptsize;
//				   getDLStatus().get(film.fid_epg).currentsize = film.currentsize;
//				   getDLStatus().get(film.fid_epg).currentstatus = film.currentstatus;
//				   getDLStatus().get(film.fid_epg).downType = film.downType;
//				   getDLStatus().get(film.fid_epg).fid_download = film.fid_download;
//				   getDLStatus().get(film.fid_epg).fid_epg = film.fid_epg;
//				   getDLStatus().get(film.fid_epg).averspeed = film.averspeed;
//				   getDLStatus().get(film.fid_epg).FilmName = film.FilmName;
//				   getDLStatus().get(film.fid_epg).Mid = film.Mid;
//				   getDLStatus().get(film.fid_epg).playUrl = film.playUrl;
//				   getDLStatus().get(film.fid_epg).realspeed = film.realspeed;
//				   getDLStatus().get(film.fid_epg).Sid = film.Sid;
//				   getDLStatus().get(film.fid_epg).totalsize = film.totalsize;
//			   }else{
//				   getDLStatus().put(film.fid_epg, film);
//			   }
			   
			
		   }
	   }
	   
	   
	   
	   /**
	    * 异步 启动全部下载
	    */
	   public  void startAllDownload(){
		   final LinkedHashMap<String, FilmDownLoad4k> downStatus = getDLStatus();
			if (downStatus==null) {
				return ;
			}
			new Thread(){
				public void run() {
					Set<Entry<String, FilmDownLoad4k>> entrySet = downStatus.entrySet();
					for (Entry<String, FilmDownLoad4k> entry : entrySet) {
						if(DlContinue(entry.getValue())){
							entry.getValue().downType = DownType.DOWNLOADING;
						}else{
							entry.getValue().downType = DownType.WAITING;
						}
					}
				};
			}.start();
		   
	   }
	   /**
	    * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
	    * 
	    * @return 应用程序是/否获取Root权限
	    */
	   public  boolean upgradeRootPermission(String pkgCodePath) {
	       Process process = null;
	       DataOutputStream os = null;
	       try {
	           String cmd="chmod 777 " + pkgCodePath;
	           process = Runtime.getRuntime().exec("su"); //切换到root帐号
	           os = new DataOutputStream(process.getOutputStream());
	           os.writeBytes(cmd + "\n");
	           os.writeBytes("exit\n");
	           os.flush();
	           process.waitFor();
	       } catch (Exception e) {
	           return false;
	       } finally {
	           try {
	               if (os != null) {
	                   os.close();
	               }
	               process.destroy();
	           } catch (Exception e) {
	           }
	       }
	       return true;
	   }
	   
	  public  void gcContext(){
		  context = null;
	  }
	  
	  
	  
	  public  String getPorxyName(){
		  if (getDlProp()!=null) {
			  String proxyName = getDlPropFromKey("proxy_name");
			  if (!TextUtils.isEmpty(proxyName)) {
				  proxyName = trimQuotationMark(proxyName);
				  Log.e(TAG, "proxy_name="+proxyName);
				return proxyName;
			  }
		  }
		return "videodaemon";
	  }
	  
	  public  String getAuthName(){
		  if (getDlProp()!=null) {
			  String authName = getDlPropFromKey("auth_name");
			  if (!TextUtils.isEmpty(authName)) {
				  authName = trimQuotationMark(authName);
				  Log.e(TAG, "auth_name="+authName);
				  return authName;
			  }
		  }
		  return "vooleauthd";
	  }
	  
	  public  boolean isDownRootExists(String root){
		  boolean flag = false;
		  if (TextUtils.isEmpty(root)) {
			return flag;
		  }
//		  try {
//			  	Process exec = Runtime.getRuntime().exec("df");
//				InputStream in = exec.getInputStream();
//				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//				String str = null;
//				while (!TextUtils.isEmpty(str = bufferedReader.readLine())) {
//					str = str.split(" ")[0];
//					com.voole.epg.download.utils.Log.e(str);
//					if(str.contains(root)){
//						flag = true;
//						break;
//					}
//				}
//				in.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		  
		   String mountsPath = "/proc/mounts";
			File mountsFile = new File(mountsPath);
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(mountsFile));
				String str = null;
				while (!TextUtils.isEmpty(str = bufferedReader.readLine())) {
					if (!str.contains("/mnt")) {
						continue;
					}
					str = str.split(" ")[1];
					com.voole.epg.download.utils.Log.e(str);
					if(str.contains(root)){
						flag = true;
						break;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (NullPointerException e){
				e.printStackTrace();
			}finally{
				if (bufferedReader!=null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		return flag;
	  }


	public  void initContext(Context context) {
		if (this.context==null) {
		 this.context = context;
		}
	}
	
	public  long getTotalDLSize(){
		LinkedHashMap<String, FilmDownLoad4k> dlStatus = getDLStatus();
		long totalsize = 0;
		if (dlStatus!=null) {
			Set<Entry<String, FilmDownLoad4k>> entrySet = dlStatus.entrySet();
			for (Entry<String, FilmDownLoad4k> entry : entrySet) {
				totalsize = (long) (totalsize+entry.getValue().totalsize);
			}
		}
		return totalsize;
	}

}
