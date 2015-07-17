package com.voole.epg.download.bean;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.voole.epg.corelib.model.movies.Film;
import com.voole.epg.download.bean.DownLoadFilm.DownListener;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;
import com.voole.epg.f4k.R;
import com.voole.epg.player.PlayItem;

public class BaseDownloadManager {

	
//	private static  final BaseDownloadManager INSTANCE = new BaseDownloadManager();
//	private FilmListActivity context;
	private static final int CREATE_TASK_FAIL = 0x0010;
	private static final int CREATE_TASK_SUCCESS = 0x0011;
	private static final int CREATE_STORE_NOT_ENOUGH = 0x0012;
	private static final int GET_DOWN_URL_FAIL = 0x0013;
	private static final int GET_AUTH_URL_FAIL = 0x0014;
	private static final int NOT_ORDERED = 0x0015;
	private static final int NOT_ORDERED_INFO = 0x0016;
	private static final int ORDERED_TIMEOUT = 0x0017;
	private static final int GET_PLAY_URL_FAIL = 0x0018;
	private static final int GET_PLAY_URL_ERROR = 0x0019;
	private static final int DISK_FREESPACE_NOT_ENOUGH_CODE = 0x0020;
	private static final int PLEASE_INSERT_DISK_AND_RETRY_CODE = 0x0021;
	private static final int SERVER_ERROR = 0x0022;
	private  Handler handler = new Handler(Looper.getMainLooper()){
	private FilmDownLoad4k filmf4K;
	private Film film;

		public void handleMessage(android.os.Message msg) {
			
			if (msg.what!=CREATE_TASK_SUCCESS) {
				if (downLoadCreateListener!=null) {
					downLoadCreateListener.onFiled(msg.what,(FilmDownLoad4k) msg.obj);
				}
			}
			
				switch (msg.what) {
				case CREATE_TASK_FAIL:
					Toast.makeText(context, R.string.create_task_fail, 0).show();
					break;
				case CREATE_TASK_SUCCESS:
					filmf4K = (FilmDownLoad4k) msg.obj;
					filmf4K.downType = DownType.DOWNLOADING;
					if (downLoadCreateListener!=null) {
						downLoadCreateListener.onSuccess(filmf4K);
					}
					break;
					
				case CREATE_STORE_NOT_ENOUGH:
		//			filmf4K = (FilmDownLoad4k) msg.obj;
		//			filmf4K.downType = DownType.UNDOWN;
		//			movieDetailView.setButtonStatus(filmf4K);
					Toast.makeText(context, R.string.create_task_fail_case_no_enough_space, 0).show();
					if(downLoadCreateListener!=null){
						downLoadCreateListener.onUnEnough();
					}
					break;
				case GET_DOWN_URL_FAIL:
					Toast.makeText(context, R.string.create_task_fail_case_get_downUrl_fail, 0).show();
//					context.cancelDialog();
					break;
				case GET_AUTH_URL_FAIL:
					Toast.makeText(context, R.string.create_task_fail_case_get_authUrl_fail, 0).show();
					break;
				case NOT_ORDERED:
					Toast.makeText(context, R.string.do_not_pay_this_film, 0).show();
					break;
				case NOT_ORDERED_INFO:
					Toast.makeText(context, R.string.can_not_get_film_playinfo, 0).show();
				case ORDERED_TIMEOUT:
					Toast.makeText(context, R.string.film_order_time_out, 1).show();
					break;
				case GET_PLAY_URL_ERROR:
					Toast.makeText(context, R.string.can_not_get_film_playinfo, 0).show();
					break;
				case GET_PLAY_URL_FAIL:
					Toast.makeText(context, R.string.get_playUrl_fail, 1).show();
					break;
				case DISK_FREESPACE_NOT_ENOUGH_CODE:
					Toast.makeText(context,R.string.disk_freespace_not_enough,0).show();
					break;
				case PLEASE_INSERT_DISK_AND_RETRY_CODE:
					Toast.makeText(context,R.string.please_insert_disk_and_retry,0).show();
					break;
				case SERVER_ERROR:
					Toast.makeText(context,"服务器异常",0).show();
					break;
				case IContent.DOWN_LOAD_MANAGER:
					Toast.makeText(context,"跳到下载管理",0).show();
				case IContent.GETFILM_FAIL:
					Toast.makeText(context,"获取影片信息出错...",0).show();
				default:
					break;
				}
//			context.cancelDialog();
		};
	};
	private DownLoadCreateListener downLoadCreateListener;
	private Context context;
	public BaseDownloadManager(){};
	
	public BaseDownloadManager createDownTask(Context context,final PlayItem item,DownListener listener){

//		handler.post(new Runnable() {
//			
//			@Override
//			public void run() {
//				if (downLoadCreateListener!=null) {
//					downLoadCreateListener.onStarted(item);
//				}
//			}
//		});
		
		this.context = context;
		DownLoadFilm df = new DownLoadFilm(context);
		df.onDownLoad(item,listener);
		return this;
	}
	
	
	
	public BaseDownloadManager createDownTask(Context context,PlayItem item){
		return createDownTask(context,item, new DownListener() {
			@Override
			public void onResult(int code, FilmDownLoad4k filmf4K) {
				Message msg = handler.obtainMessage();
				msg.obj = filmf4K;
				msg.what = code;
				handler.sendMessage(msg);
			}
		});
	}
	
	public interface DownLoadCreateListener{
		public void onSuccess(FilmDownLoad4k film);
		public void onFiled(int code,FilmDownLoad4k film);
//		public void onStarted(PlayItem item);
		public void onUnEnough();  
		
	}
	
	public BaseDownloadManager setDownLoadCreateListener(DownLoadCreateListener downLoadCreateListener){
		this.downLoadCreateListener = downLoadCreateListener;
		return this;
	}
}
