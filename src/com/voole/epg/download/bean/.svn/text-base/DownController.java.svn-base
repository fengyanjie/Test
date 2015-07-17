package com.voole.epg.download.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.text.TextUtils;
import android.util.Log;

import com.voole.epg.corelib.model.proxy.ProxyManager;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;
import com.voole.util.net.HttpDownloaderUtil;

@Deprecated
public class DownController implements IDownloadControl {

	private static final String TAG = "DownController";
	private String proxyPort;

	public DownController(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * http://127.0.0.1:5656/downloadm3u8?url=
	 * 'http://121.10.173.122/skyts01.m3u8'
	 * 其中http://121.10.173.122/skyts01.m3u8是epg返给你的播放串。epg还会返给你电影名称等其它细节。
	 * 
	 * 下载任务创建成功与否，会给你以http response形式返回给你如下xml： <?xml version="1.0"?> <status>
	 * <value>0</value> <fid>1234567890abcdef1234567890abcdef</fid>
	 * <description>create download task OK!</description> </status>
	 */
	/**
	 * 创建一个下载,
	 * 
	 * @param playUrl
	 *            为epg给定的播放地址
	 * @return
	 */
	@Override
	public FilmDownLoad4k DLCreate(String playUrl) {
		if (!ProxyManager.GetInstance().isProxyRunning()) {
			ProxyManager.GetInstance().startProxy();
		}
		String url = "http://127.0.0.1:" + proxyPort + "/downloadm3u8";
		url += "?";
		url += "url=";
		url += "'" + playUrl + "'";
		Log.d(TAG, url);
		String result = HttpDownloaderUtil.readString(url);
		Log.d(TAG, "DlCreate-->" + result);
		FilmDownLoad4k film = null;
		if (!TextUtils.isEmpty(result) && result.contains("<value>0</value>")) {
			try {
				XmlPullParserFactory parserFactory = XmlPullParserFactory
						.newInstance();
				XmlPullParser parser = parserFactory.newPullParser();
				parser.setInput(new ByteArrayInputStream(result.getBytes()),
						"UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {
						film = new FilmDownLoad4k();
					} else if (eventType == XmlPullParser.START_TAG) {
						String tagname = parser.getName();
						if ("length".equalsIgnoreCase(tagname)) {
							String totalSize = parser.nextText();
							film.totalsize = totalSize == null ? 0 : Double.parseDouble(totalSize);
							if (film.totalsize>0) {
								film.downType = DownType.WAITING;
							}else{
								film.downType = DownType.UNDOWN;
							}
						}
						if ("fid".equalsIgnoreCase(tagname)) {
							film.fid_download = parser.nextText();
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
		}
		if (film!=null&&DlContinue(film)) {
			return film;
		}else{
			return null;
		}
	}

	@Override
	public boolean DLStop(FilmDownLoad4k film) {
		if (film == null) {
			LinkedHashMap<String, FilmDownLoad4k> downStatus = DownloadResourceManager.getInstance().getDLStatus();
			if (downStatus == null) {
				return true;
			}
			Set<Entry<String, FilmDownLoad4k>> entrySet = downStatus.entrySet();
			for (Entry<String, FilmDownLoad4k> entry : entrySet) {
				DLControl(entry.getValue().fid_download, 0);
			}
			return true;
		}

		return DLControl(film.fid_download, 0);
	}

	/**
	 * 
	 * 
	 * <b>获取当前的下载状态</b></br> </br>
	 * http://127.0.0.1:5656/getdlstatus?fid=1234567890abcdef1234567890abcdef
	 * 返回接口： <?xml version="1.0"?> <status>
	 * <fid>1234567890abcdef1234567890abcdef</fid>
	 * <totalsize>1357924680</totalsize> <currentsize>1133557799</currentsize>
	 * <cryptsize>0</cryptsize> <averspeed>111.666</averspeed>
	 * <realspeed>213.455</realspeed> </status>
	 */
	@Override
	public FilmDownLoad4k getDLstaus(FilmDownLoad4k film) {
		if (film == null) {
			return null;
		}
		String url = "http://127.0.0.1:" + proxyPort + "/getdlstatus";
		url += "?";
		url += "fid=";
		url += film.fid_download;
		Log.d(TAG, url);

		String result = HttpDownloaderUtil.readString(url);
		Log.d(TAG, "getDLstaus-->" + result);
		if (TextUtils.isEmpty(result)) {
			if (!ProxyManager.GetInstance().isProxyRunning()) {
				ProxyManager.GetInstance().startProxy();
			}
			return null;
		}
		try {
			XmlPullParserFactory parserFactory = XmlPullParserFactory
					.newInstance();
			XmlPullParser parser = parserFactory.newPullParser();
			parser.setInput(new ByteArrayInputStream(result.getBytes()),
					"UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {
					String tagname = parser.getName();
					// if ("totalsize".equalsIgnoreCase(tagname)) {
					// String totalSize = parser.nextText();
					// film.totalsize =
					// totalSize==null?0:Double.parseDouble(totalSize);
					// }
					if ("currentsize".equalsIgnoreCase(tagname)) {
						String currentsize = parser.nextText();
						film.currentsize = currentsize == null ? 0 : Double
								.parseDouble(currentsize);
					}
					if ("averspeed".equalsIgnoreCase(tagname)) {
						String averspeed = parser.nextText();
						film.averspeed = averspeed == null ? 0 : Double
								.parseDouble(averspeed) * 1024;
					}
					if ("realspeed".equalsIgnoreCase(tagname)) {
						String realspeed = parser.nextText();
						film.realspeed = realspeed == null ? 0f : Double
								.parseDouble(realspeed) * 1024;
					}
					if ("cryptsize".equalsIgnoreCase(tagname)) {
						String cryptsize = parser.nextText();
						film.cryptsize = cryptsize == null ? 0 : Double
								.parseDouble(cryptsize);
					}

					/**
					 * 未下载 UNDOWN <br/>
					 * 下载中 DOWNLOADING <br/>
					 * 等待下载 WAITING <br/>
					 * 下载完成 FINISH <br/>
					 * 
					 */
					if (film.totalsize > 0) {
						if (film.totalsize <= film.currentsize) {
							film.downType = DownType.FINISH;
						} else {
//							if (film.realspeed > 0) {
//								film.downType = DownType.DOWNLOADING;
//							} else {
								// if (film.averspeed<=0) {
								// film.downType = DownType.WAITING;
								// }
//							}
						}
					} else {
						film.downType = DownType.UNDOWN;
					}
					// downStatus.put(film.fid_epg, film);
					// writeDownStatus(downStatus);
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

	
	
	@Override
	public boolean setMaxBand(int max) {
		String url = "http://127.0.0.1:"+proxyPort+"/localm3u8";
		url+="?";
		url+="type=downloadsetmaxbandwidth";
		url+="&bandwidth=";
		url+=max;
		Log.d(TAG, url);
		String result = HttpDownloaderUtil.readString(url);
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
	 * 
	 * <b>暂停、继续下载接口</b></br>
	 * http://127.0.0.1:5656/dlcontrol?type=1&fid=1234567890
	 * abcdef1234567890abcdef type: 1为继续下载，0为暂停下载, 2为删除下载，3为限制带宽，其他为未定义 <?xml
	 * version="1.0"?> <status> <value>ret_value</value> // value为0，代表成功，其它代表失败
	 * <fid>1234567890abcdef1234567890abcdef</fid> <type>1</type>
	 * <description>pause download task OK! </description> </status>
	 */
	private synchronized boolean DLControl(String fid, int type) {
		String url = "http://127.0.0.1:" + proxyPort + "/dlcontrol";
		url += "?";
		url += "fid=";
		url += fid;
		url += "&type=";
		url += type;
		String result = HttpDownloaderUtil.readString(url);
		Log.d(TAG, "DLControl-->" + url);
		Log.d(TAG, "DLControl-->" + result);
		if (!TextUtils.isEmpty(result)) {
			result.replace(" ", "");
			return result.contains("<value>0</value>");
		} else {
			if (type==0) {
				return true;
			}
			new Thread() {
				public void run() {
					if (!ProxyManager.GetInstance().isProxyRunning()) {
						ProxyManager.GetInstance().startProxy();
					}
				};
			}.start();
		}
		return false;

	}

	@Override
	public boolean DLdelete(FilmDownLoad4k film) {
		return DLControl(film.fid_download, 2);
	}

	@Override
	public boolean DlContinue(FilmDownLoad4k film) {
		return DLControl(film.fid_download, 1);
	}

	@Override
	public String getDLPlayUrl(FilmDownLoad4k film) {
		String url = "http://127.0.0.1:" + proxyPort + "/playlocal";
		// String url = "http://127.0.0.1:5654/playlocal";
		url += "?";
		url += "fid=";
		url += film.fid_download;
		Log.d(TAG, url);
		return url;
	}

	@Override
	public boolean isDownLoad(FilmDownLoad4k film) {
		if (film!=null) {
			return film.averspeed>0;
		}
		return false;
	}

	@Override
	public DLError getDlError() {
		// TODO Auto-generated method stub
		return null;
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
