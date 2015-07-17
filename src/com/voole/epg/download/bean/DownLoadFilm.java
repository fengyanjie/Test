package com.voole.epg.download.bean;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.voole.epg.download.DownManagerActivity;
import com.voole.epg.download.FilmListActivity;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;
import com.voole.epg.player.PlayItem;
import com.voole.epg.player.ad.vo.ADResponse;

public class DownLoadFilm {
	private Context context;
	private FilmDownLoad4k filmf4K;
	protected static final String TAG = "DownLoadFilm";

	public DownLoadFilm(Context context) {
		this.context = context;
	}

	public interface DownListener {
		/**
		 * @param code
		 * 
		 *            {@link FilmListActivity
		 *             @link IContent
		 *            }
		 * @param film
		 */
		void onResult(int code, FilmDownLoad4k film);
	}

	public void onDownLoad(final PlayItem playItem, final DownListener listener) {
		if(playItem == null){
			return;
		}
		String fid = playItem.getFid();

		if (!TextUtils.isEmpty(fid)) {

			if (DownloadResourceManager.getInstance().getDownLoadFlag() == null
					|| !"1".equals(DownloadResourceManager.getInstance().getDownLoadFlag())) {
//				Toast.makeText(context, R.string.down_flag_is_not_open, 0).show();
				if (listener!=null) {
					listener.onResult(IContent.CREATE_TASK_FAIL, null);
				}
				return;
			}

			filmf4K = DownloadResourceManager.getInstance().getDLstaus(fid);

			if (filmf4K == null) {// 立即下载
				new Thread() {
					File path = new File(DownloadResourceManager.getInstance().getDownLoadPath());

					public void run() {
						if (TextUtils.isEmpty(DownloadResourceManager.getInstance()
								.getDownLoadPath())/***/
						|| !path.getParentFile().exists() || !path.exists()) {
//							DownloadResourceManager.getInstance().initDownPath();
							return;
						}

						path = new File(DownloadResourceManager.getInstance().getDownLoadPath());
						if (!DownloadResourceManager.getInstance().isDownRootExists(path.getParent())) {
							listener.onResult(IContent.PLEASE_INSERT_DISK_AND_RETRY_CODE, null);
							return;
						}
						if (path.getParentFile().getFreeSpace() < 2f * 1024 * 1024 * 1024) {// 如果小于2G
																							// 则提示空间不足
							listener.onResult(IContent.DISK_FREESPACE_NOT_ENOUGH_CODE, filmf4K);
							return;
						}

						LinkedHashMap<String, FilmDownLoad4k> downStatus = DownloadResourceManager.getInstance().getDLStatus();
						if (downStatus == null) {// 如果没有创建下载列表,则创建
							downStatus = new LinkedHashMap<String, FilmDownLoad4k>();
						}
							if (playItem.getAdXml() != null) {
								try {
									ADResponse adResponse = new ADResponse(playItem.getAdXml());
									String playUrl = adResponse.play_url;
									if (!TextUtils.isEmpty(playUrl)) {
										creatDownload(playItem, path,downStatus,playUrl);
									} else {
										listener.onResult(IContent.GET_DOWN_URL_FAIL, filmf4K);
									}
								} catch (XmlPullParserException e) {
									Log.e(TAG, "XmlPullParserException", e);
								} catch (IOException e) {
									Log.e(TAG, "IOException", e);
								}
							} else {
								listener.onResult(IContent.CREATE_TASK_FAIL, filmf4K);
							}
				}

					private void creatDownload(final PlayItem playItem,final File path,LinkedHashMap<String, FilmDownLoad4k> downStatus,String playUrl) {
						Log.e(TAG, "adResponse.List<Product> products,play_url--> " + playUrl);
						final FilmDownLoad4k filmDownLoad4k = DownloadResourceManager.getInstance().DlCreate(playUrl); // 创建下载
						if (filmDownLoad4k != null) {
							filmDownLoad4k.fid_epg = playItem.getFid();
							filmDownLoad4k.FilmName = playItem.getFilmName();
							filmDownLoad4k.Mid = playItem.getMid();
							filmDownLoad4k.Sid = playItem.getSid();
							filmDownLoad4k.downType=DownType.DOWNLOADING;
							filmDownLoad4k.playUrl = playUrl;
							
							if (path.getFreeSpace()- DownloadResourceManager.getInstance().getTotalDLSize() > filmDownLoad4k.totalsize) {
								
								DownloadResourceManager.getInstance().initdb(downStatus);
								DownloadResourceManager.getInstance().putFilm(filmDownLoad4k,false);
								if (DownloadResourceManager.getInstance().writeDownStatus(downStatus)) {
									listener.onResult(IContent.CREATE_TASK_SUCCESS, filmDownLoad4k);
								} else {
									filmDownLoad4k.downType = DownType.UNDOWN;
									DownloadResourceManager.getInstance().delDLstatus(filmDownLoad4k);
									listener.onResult(IContent.CREATE_TASK_FAIL, filmDownLoad4k);
								}
							} else {
								DownloadResourceManager.getInstance().delDLstatus(filmDownLoad4k);
								listener.onResult(IContent.CREATE_STORE_NOT_ENOUGH, filmDownLoad4k);
								return;
							}
						} else {
							Log.e(TAG, "创建下载失败....");
							DownloadResourceManager.getInstance().DLPause(new FilmDownLoad4k());
							listener.onResult(IContent.CREATE_TASK_FAIL, filmDownLoad4k);
						}
					};
				}.start();
			} else {// 跳至下载管理
//				if (BuildConfig.DEBUG)
//					Toast.makeText(context, "跳到下载管理", 0).show();
				listener.onResult(IContent.DOWN_LOAD_MANAGER, null);
				Intent intent = new Intent();
				intent.setClass(context, DownManagerActivity.class);
				context.startActivity(intent);
			}
		} else {
//			if (BuildConfig.DEBUG)
//				Toast.makeText(context, "获取影片信息出错...", 0).show();
			listener.onResult(IContent.GETFILM_FAIL, null);
		}

	}
	
	
	
	



}
