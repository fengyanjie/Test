package com.voole.epg.download.bean;


import java.io.Serializable;
import java.text.NumberFormat;

public class FilmDownLoad4k /*extends Film*/ implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	/**
	 * 未下载            	UNDOWN    		<br/>
	 * 下载中		DOWNLOADING		<br/>
	 * 等待下载	WAITING			<br/>
	 * 下载完成	FINISH			<br/>
	 * 用户暂停	PAUSE			<br/>
	 * 用户继续	CONTINUE	    <br/>
	 *
	 */
	public  enum DownType{
		
		UNDOWN,DOWNLOADING,WAITING,FINISH,PAUSE/*,CONTINUE*/;
	}
	/**totalsize：电影总大小*/
	public double totalsize;
	/**currentsize：当前已下载*/
	public double currentsize;
	/**cryptsize: 当前已加密大小*/
	public double cryptsize;
	/**currentstatus：当前状态，是暂停还是正在下载     */
	@Deprecated
	public String currentstatus;
	/**平均速度  bit*/
	public double averspeed;
	/**即时速度  bit*/
	public double realspeed;
	
	/**当前的下载状态*/
	public DownType downType = DownType.UNDOWN;
	
	/**当前4k电影的下载fid*/
	public String fid_download;
	
	/**当前4K电影的epg的fid*/
	public String fid_epg;
	
	public String playUrl;
	
	public String Mid;
	public String Sid;
	
	public String FilmName;
	
	public String getDownPercent(){
		if (totalsize>0) {
			NumberFormat numberFormat = NumberFormat.getPercentInstance();
			return numberFormat.format(currentsize/totalsize);
		}
		return "";
	}
	
}
