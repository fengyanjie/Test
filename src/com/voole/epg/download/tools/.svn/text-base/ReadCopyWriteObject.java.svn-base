package com.voole.epg.download.tools;

import com.voole.epg.download.bean.FilmDownLoad4k;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;

public class ReadCopyWriteObject {
	/**
	 * 从磁盘读取下载写入新的对象
	 * 
	 */
	public static FilmDownLoad4k readCopyWrite(com.voole.epg.download.bean.FilmDownLoad4k mDestDownLoad4k,com.voole.epg.f4k_download.domain.FilmDownLoad4k mSrcFilmFor4k) {
		mDestDownLoad4k.averspeed = mSrcFilmFor4k.averspeed;
		mDestDownLoad4k.cryptsize = mSrcFilmFor4k.cryptsize;
		mDestDownLoad4k.currentsize = mSrcFilmFor4k.currentsize;
		mDestDownLoad4k.currentstatus = mSrcFilmFor4k.currentstatus;
		switch (mSrcFilmFor4k.downType) {
		case UNDOWN:
			mDestDownLoad4k.downType = DownType.UNDOWN;
			break;
		case DOWNLOADING:
			mDestDownLoad4k.downType = DownType.DOWNLOADING;
			break;
		case WAITING:
			mDestDownLoad4k.downType = DownType.WAITING;
			break;
		case PAUSE:
			mDestDownLoad4k.downType = DownType.PAUSE;
			break;
		case FINISH:
			mDestDownLoad4k.downType = DownType.FINISH;
			break;
		default:
			break;
		}
		
		mDestDownLoad4k.fid_download = mSrcFilmFor4k.fid_download;
		mDestDownLoad4k.fid_epg = mSrcFilmFor4k.fid_epg;
		mDestDownLoad4k.FilmName = mSrcFilmFor4k.FilmName;
		mDestDownLoad4k.Mid = mSrcFilmFor4k.Mid;
		mDestDownLoad4k.playUrl = mSrcFilmFor4k.playUrl;
		mDestDownLoad4k.realspeed = mSrcFilmFor4k.realspeed;
		mDestDownLoad4k.Sid = mSrcFilmFor4k.Sid;
		mDestDownLoad4k.totalsize = mSrcFilmFor4k.totalsize;
		return mDestDownLoad4k;
	}
	

	
}
