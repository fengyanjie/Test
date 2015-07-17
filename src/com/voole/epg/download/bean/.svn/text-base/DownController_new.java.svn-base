package com.voole.epg.download.bean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.text.TextUtils;

import com.voole.epg.corelib.model.proxy.ProxyManager;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;
import com.voole.epg.download.utils.Log;
import com.voole.util.net.HttpDownloaderUtil;

public class DownController_new implements IDownloadControl{

	private String proxyPort;
	private DLError error;
	public DownController_new(String proxyPort){
		this.proxyPort = proxyPort;
	}
	private static final String TAG = "DownController_new";

	
	/**
	 * 
	 * <b>暂停、继续下载接口</b></br>
	 *http://127.0.0.1:5656/localm3u8?type=downloadstart&url=''
	 *
		<?xml version="1.0"?>
		<status>
		<value>0</value>
		<fid>9c2218c78a3f1a559e2f3e808eb6722b</fid>
		<description>create download task OK!</description>
		</status>

	 */
	@Override
	public FilmDownLoad4k DLCreate(String playUrl) {
		
		if(!ProxyManager.GetInstance().isProxyRunning()){
			ProxyManager.GetInstance().startProxy();
		}
		String url = "http://127.0.0.1:"+proxyPort+"/localm3u8";
		url+="?";
		url+="type=downloadstart";
		url+="&url=";
		url+="'"+playUrl+"'";
		Log.d(TAG, url);
		String result = HttpDownloaderUtil.readString(url);
		Log.d(TAG, "DlCreate-->"+result);
		FilmDownLoad4k  film = null;
		if (!TextUtils.isEmpty(result)&&result.contains("<value>0</value>")) {
			try {
				XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
				XmlPullParser parser = parserFactory.newPullParser();
				parser.setInput(new ByteArrayInputStream(result.getBytes()), "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {
						film = new FilmDownLoad4k();
					} else if (eventType == XmlPullParser.START_TAG) {
						String tagname = parser.getName();
						if ("totalsize".equalsIgnoreCase(tagname)) {
							String totalSize = parser.nextText();
							film.totalsize = totalSize==null?0:Double.parseDouble(totalSize);
						}
						if ("currentsize".equalsIgnoreCase(tagname)) {
							String currentsize = parser.nextText();
							film.currentsize = currentsize==null?0:Double.parseDouble(currentsize);
						}
						if ("fid".equalsIgnoreCase(tagname)) {
							film.fid_download = parser.nextText();
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						String tagname = parser.getName();
						Log.d("End tag " + tagname);
					} 
					eventType = parser.next();
				}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		return film;
	}

	
	/**
	 * 停止下载
	 * http://127.0.0.1:5656/localm3u8?type=downloadstop
	 * @return
	 */
	@Override
	public boolean DLStop(FilmDownLoad4k film) {
		String url = "http://127.0.0.1:"+proxyPort+"/localm3u8";
		url+="?";
		url+="type=downloadstop";
		Log.d(TAG, url);
		String result = HttpDownloaderUtil.readString(url);
		Log.d(TAG, result);
		if (!TextUtils.isEmpty(result)) {
			result.replace(" ", "");
			return result.contains("<value>0</value>");
		}else{
//			new Thread(){
//				public void run() {
//					if(!ProxyManager.GetInstance().isProxyRunning()){
//						ProxyManager.GetInstance().startProxy();
//					}
//				};
//			}.start();
		}
		return false;
	}

	
	
	/**
	 * 
	 * 
	 * <b>获取当前的下载状态</b></br>
	 * </br>
	 * http://127.0.0.1:5656/localm3u8?type=downloadspeed
		返回接口：
		<?xml version="1.0"?>
		<status>
		<fid>1234567890abcdef1234567890abcdef</fid>
		<totalsize>1357924680</totalsize>
		<currentsize>1133557799</currentsize>
		<cryptsize>0</cryptsize> 
		<averspeed>111.666</averspeed> 
		<realspeed>213.455</realspeed> 
		</status>

	 */
	@Override
	public FilmDownLoad4k getDLstaus(FilmDownLoad4k film) {
		String downLoadPath = DownloadResourceManager.getInstance().getDownLoadPath();
		if(film != null){
		if (new File(downLoadPath,film.fid_download).exists()) {
			delectOldFolder(film);
		}
		}
		
		String url = "http://127.0.0.1:"+proxyPort+"/localm3u8";
		url+="?";
		url+="type=downloadstatus";
		if (film!=null) {
			url+="&fid=";
			url+=film.fid_download;
		}else{
			film = new FilmDownLoad4k();
		}
		Log.d(TAG, url);
		String result = HttpDownloaderUtil.readString(url);
		Log.d(TAG, "getDLstaus-->"+result);
		if (TextUtils.isEmpty(result)) {
			if(!ProxyManager.GetInstance().isProxyRunning()){
				ProxyManager.GetInstance().startProxy();
			}
			return null;
		}else{
			result.replace(" ", "");
			if(!result.contains("<value>0</value>")){
				parserErrorInfo(result);
				return null;
			}else{
				error = null;
			}
		}
		
		
		try {
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			parser.setInput(new ByteArrayInputStream(result.getBytes()), "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {
					String tagname = parser.getName();
					if ("totalsize".equalsIgnoreCase(tagname)) {
						String totalSize = parser.nextText();
						film.totalsize = totalSize==null?0:Double.parseDouble(totalSize);
					}
					if ("currentsize".equalsIgnoreCase(tagname)) {
						String currentsize = parser.nextText();
						film.currentsize = currentsize==null?0:Double.parseDouble(currentsize);
					}
					if ("averspeed".equalsIgnoreCase(tagname)) {
						String averspeed = parser.nextText();
						film.averspeed = averspeed==null?0:Double.parseDouble(averspeed)*1024;
					}
					if ("realspeed".equalsIgnoreCase(tagname)) {
						String realspeed = parser.nextText();
						film.realspeed = realspeed==null?0f:Double.parseDouble(realspeed)*1024;
					}
					if ("cryptsize".equalsIgnoreCase(tagname)) {
						String cryptsize = parser.nextText();
						film.cryptsize = cryptsize==null?0:Double.parseDouble(cryptsize);
					}
					
					/**
					 * 未下载            	UNDOWN    		<br/>
					 * 下载中		DOWNLOADING		<br/>
					 * 等待下载	WAITING			<br/>
					 * 下载完成	FINISH			<br/>
					 *
					 */
					if (film.totalsize>0) {
						if (film.totalsize<=film.currentsize) {
							film.downType = DownType.FINISH;
						}else{
//							if (film.realspeed>0) {
//								film.downType = DownType.DOWNLOADING;
//							}else{
//								
//							}
						}
					}else{
						film.downType = DownType.UNDOWN;
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					String tagname = parser.getName();
				} 
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return film;
	}


	/**
	 * 解析错误消息
	 * @param result
	 */
	private void parserErrorInfo(String result) {
		try {
			XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			parser.setInput(new ByteArrayInputStream(result.getBytes()), "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String tagname = parser.getName();
					if (error==null) {
						error = new DLError();
					}
					if ("value".equalsIgnoreCase(tagname)) {
						error.value = Integer.parseInt(parser.nextText());
					}
					if ("description".equalsIgnoreCase(tagname)) {
						error.desception = parser.nextText();
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

	
	
	/**
	 *  后台下载设置最大带宽
	 * 
	 * http://127.0.0.1:5656/localm3u8?type=downloadsetmaxbandwidth&bandwidth=200
		
		返回
		<?xml version="1.0"?>
		<status>
		<value>0</value>
		<description>set max bandwidth OK!</description>
		</status>

	 * @return
	 */
	@Override
	public boolean setMaxBand(int max) {
		String url = "http://127.0.0.1:"+proxyPort+"/localm3u8";
		url+="?";
		url+="type=downloadsetmaxbandwidth";
		url+="&bandwidth=";
		url+=max;
		Log.d(TAG, "++++++++++++"+url);
		String result = HttpDownloaderUtil.readString(url);
		Log.d(TAG,  "-----------"+result);
		if (!TextUtils.isEmpty(result)) {
			result.replace(" ", "");
			return result.contains("<value>0</value>");
		}else{
			new Thread(){
				public void run() {
					if(!ProxyManager.GetInstance().isProxyRunning()){
						ProxyManager.GetInstance().startProxy();
					}
				};
			}.start();
		}
		return false;
	}

	
	
	/**
	 * 删除下载
	 * 
	 * http://127.0.0.1:5656/localm3u8?type=filedelete&fid=9c2218c78a3f1a559e2f3e808eb6722b
	 * @return
	 */
	@Override
	public boolean DLdelete(FilmDownLoad4k film) {
		
		String url = "http://127.0.0.1:"+proxyPort+"/localm3u8";
		url+="?";
		url+="type=filedelete";
		url+="&fid=";
		url+=film.fid_download;
		Log.d(TAG, url);
		String result = HttpDownloaderUtil.readString(url);
		Log.d(TAG, result);
		if (!TextUtils.isEmpty(result)) {
			result.replace(" ", "");
			return delectOldFolder(film)|result.contains("<value>0</value>");
		}else{
			new Thread(){
				public void run() {
					if(!ProxyManager.GetInstance().isProxyRunning()){
						ProxyManager.GetInstance().startProxy();
					}
				};
			}.start();
		}
		return false;
	}


	/**
	 * 删除老代理下载的文件
	 * @param film
	 */
	private boolean delectOldFolder(FilmDownLoad4k film) {
		String downLoadPath = DownloadResourceManager.getInstance().getDownLoadPath();
		if (!TextUtils.isEmpty(downLoadPath)) {
			File file = new File(downLoadPath);
			if (file.isDirectory()) {
				String[] list = file.list();
				if (list!=null) {
					for (String string : list) {
						if (string.startsWith(film.fid_download)) {
							 return deleteDir(new File(string));
						}
					}
				}
			}
		}
		return false;
	}


	@Override
	public boolean DlContinue(FilmDownLoad4k film) {
		return DLCreate(film.playUrl)!=null;
	}


	@Override
	public String getDLPlayUrl(FilmDownLoad4k film) {
		
		return film.playUrl;
	}

	
    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private  boolean deleteDir(File dir) {
    	 if (dir.isDirectory()) {
             File[] children = dir.listFiles();
             //递归删除目录中的子目录下
             for (int i=0; i<children.length;i++) {
             	 Log.e(TAG, children[i].getAbsolutePath());
             	boolean success =  deleteDir(children[i]);
             	if (!success) {
 					return false;
 				}
             }
         }
         // 目录此时为空，可以删除
         return dir.delete();
    }

	@Override
	public boolean isDownLoad(FilmDownLoad4k film) {
		return getDLstaus(null)!=null;
	}


	@Override
	public DLError getDlError() {
		return error;
	}
	
	/**
	 * <b>暂停下载</b></br>
	 * http://127.0.0.1:5656/localm3u8?type=downloadpause
	 *	成功
		<status>
		 <value>0</value>
		 <description>pause OK!</description>
		</status>
		失败
		<status>
		 <value>-1</value>
		 <description>no download task!</description>
		</status>
	 */
	public boolean DLDownPause(){
		String url = "http://127.0.0.1:"+proxyPort+"/localm3u8?type=downloadpause";
		HttpDownloaderUtil.readString(url);
		Log.e(TAG,url);
		return true ;	
	}
	
	/**
	 * <b>继续下载</b></br>
	 * http://127.0.0.1:5656/localm3u8?type=downloadpause
	*/
	public boolean DLDownContinue(){
		String url = "http://127.0.0.1:"+proxyPort+"/localm3u8?type=downloadresume";
		HttpDownloaderUtil.readString(url);
		Log.e(TAG,url);
		return true ;	
	}

}
