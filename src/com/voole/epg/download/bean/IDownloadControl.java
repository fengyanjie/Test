package com.voole.epg.download.bean;


public interface IDownloadControl {
	/**创建一个下载任务*/
	public  FilmDownLoad4k DLCreate(String playUrl);
	/**创建一个下载任务*/
	public  boolean DlContinue(FilmDownLoad4k film);
	/**暂停一个下载任务*/
	public  boolean DLStop(FilmDownLoad4k film);
	/**获取当前下载任务*/
	public  FilmDownLoad4k getDLstaus(FilmDownLoad4k film);
	/**后台下载 设置最大带宽*/
	public  boolean setMaxBand(int max);
	/**删除下载任务*/
	public  boolean DLdelete(FilmDownLoad4k film);
	
	/**获取本地播放链接*/
	public String getDLPlayUrl(FilmDownLoad4k film);
	
	/**任务是否下载*/
	public boolean isDownLoad(FilmDownLoad4k film);
	
	/**获得下载错误对象*/
	public DLError getDlError();
	
	/**暂停下载*/
	public boolean DLDownPause();
	/**继续下载 */
	public boolean DLDownContinue();
	
}
