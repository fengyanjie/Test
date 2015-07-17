package com.voole.epg.download;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.voole.epg.corelib.model.account.Product;
import com.voole.epg.corelib.model.auth.AuthManager;
import com.voole.epg.corelib.model.auth.User;
import com.voole.epg.corelib.model.movies.Content;
import com.voole.epg.corelib.model.movies.Detail;
import com.voole.epg.corelib.model.movies.Film;
import com.voole.epg.corelib.model.movies.FilmAndPageInfo;
import com.voole.epg.corelib.model.movies.MovieManager;
import com.voole.epg.corelib.model.play.Ad;
import com.voole.epg.corelib.model.play.BasePlayManager;
import com.voole.epg.corelib.model.play.PlayCheckInfo;
import com.voole.epg.download.base.BaseDialog;
import com.voole.epg.download.base.TVAlertDialog;
import com.voole.epg.download.base.TVAlertDialog.Builder;
import com.voole.epg.download.bean.BaseDownloadManager;
import com.voole.epg.download.bean.BaseDownloadManager.DownLoadCreateListener;
import com.voole.epg.download.bean.DownloadResourceManager;
import com.voole.epg.download.bean.FilmDownLoad4k;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;
import com.voole.epg.download.bean.IContent;
import com.voole.epg.download.widget.CheckDownDialogView;
import com.voole.epg.download.widget.CheckDownDialogView.PathChoiceClickListener;
import com.voole.epg.download.widget.FilmLinearLayout;
import com.voole.epg.download.widget.MovieDetailView;
import com.voole.epg.download.widget.MovieDetailView.DetailButtonListener;
import com.voole.epg.f4k.BuildConfig;
import com.voole.epg.f4k.R;
import com.voole.epg.player.PlayItem;
import com.voole.epg.player.VoolePlayer;
import com.voole.util.prop.PropertiesUtil;

public class FilmDetailDialog extends BaseDialog implements DetailButtonListener{
	protected static final String TAG = "DetailDialogf4k";

	public static interface DetailDialogf4kListener<T> {
		public void onPlay(Activity activity,T t);
	}

	public static DetailDialogf4kListener<String> listener = null;

	public static void setListener(DetailDialogf4kListener<String> listener) {
		FilmDetailDialog.listener = listener;
	} 
	private MovieDetailView movieDetailView;
	private FilmListActivity context;
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
	private static final int linearLayout1_ID = 0x11111;
	private FilmLinearLayout adapterLinearLayout;
	private CheckDownDialogView checkView;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CREATE_TASK_FAIL:
				Toast.makeText(context, R.string.create_task_fail, 0).show();
				break;
			case CREATE_TASK_SUCCESS:
				filmf4K = (FilmDownLoad4k) msg.obj;
				if (film!=null&&film.getFilmID().equalsIgnoreCase(filmf4K.fid_epg)) {
					movieDetailView.setButtonStatus(filmf4K);
					final Toast makeText = Toast.makeText(context, R.string.task_create_success, Toast.LENGTH_SHORT);
					this.postDelayed(new Runnable() {
						@Override
						public void run() {
							makeText.setText(R.string.three_task_allow_at_one_time);
							makeText.setDuration(2000);
							makeText.show();
						}
					},1000);
				}
				break;
				
			case CREATE_STORE_NOT_ENOUGH:
				filmf4K = (FilmDownLoad4k) msg.obj;
				filmf4K.downType = DownType.UNDOWN;
				movieDetailView.setButtonStatus(filmf4K);
				showChoiceDialog();
				Toast.makeText(context, R.string.create_task_fail_case_no_enough_space, 0).show();
				break;
			case GET_DOWN_URL_FAIL:
				Toast.makeText(context, R.string.create_task_fail_case_get_downUrl_fail, 0).show();
				context.cancelDialog();
				break;
			case GET_AUTH_URL_FAIL:
				Toast.makeText(context, R.string.create_task_fail_case_get_authUrl_fail, 0).show();
				break;
			case NOT_ORDERED:
				context.getOrderDialog(getContext().getString(R.string.do_not_pay_this_film)).show();
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
			default:
				break;
			}
			context.cancelDialog();
		};
	};
	private FilmDownLoad4k filmf4K;
	private Film film;
	private Builder choiceUsbDialog;
	public FilmDetailDialog(Activity context, int theme) {
		super(context, theme);
		initData();
	}

	protected FilmDetailDialog(Activity context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initData();
	}

	public FilmDetailDialog(Activity context) {
		this(context,R.style.alertDialog);
		initData();
	}
	
	private void initData() {
		this.context = (FilmListActivity) mContext;
		movieDetailView  = new MovieDetailView(mContext);
		movieDetailView.setDetailButtonListener(this);
//		movieDetailView.setBackgroundColor(Color.parseColor("#ee000000"));
		movieDetailView.setBackgroundResource(R.drawable.bg_4k_download_detail);
//		this.setContentView(movieDetailView);
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int height = DisplayManager.getInstance(context).getScreenHeight()-h/3;
		int width = DisplayManager.getInstance(context).getScreenWidth()-w/5;
		LayoutParams params =new LayoutParams(width, height);
		this.setContentView(movieDetailView, params);
		
//		this.handler = DownLoadHandler.getInstance().getHandler();
//		DownLoadHandler.getInstance().setParameter(context,film);
	}

	public void setFilm(FilmAndPageInfo info,int index){
		this.film = info.getFilmList().get(index);
		movieDetailView.setDetailData(info, index);
	}

	@Override
	public void onPlay(final Film film) {
			
		
			String localPlay=PropertiesUtil.getProperty(context, "local_play");
			if(Boolean.parseBoolean(localPlay)){
	                //////////////测试用
			FilmDownLoad4k dLstaus = new FilmDownLoad4k();
			dLstaus.downType = DownType.FINISH;
	

			String videoName = PlayListLocal.getinstance(context).getPingyi(film.getFilmName())+".mp4";
			//String local_play_list = PropertiesUtil.getProperty(context, "local_play_list");
			String local_play_list=DownloadResourceManager.getInstance().getDownLoadPath();
			Toast makeText = Toast.makeText(context, "目录不存在", 0);
			File localFile = new File(local_play_list);
			Uri uri = null;
			if (localFile.isDirectory()) {
				File[] listFiles = localFile.listFiles();
				if (listFiles!=null) {
					for (File file : listFiles) {
					if (file.getName().endsWith(videoName)) {
						uri = Uri.fromFile(file);
						break;
					}
				}
				}
			}else{
				makeText.show();
			}
			if (uri==null) {
				makeText.setText("没有找到播放介质...");
				makeText.show();
				return;
			}else{
				List<PlayItem> playItems = new ArrayList<PlayItem>();
				PlayItem item = new PlayItem();
				item.setMid(film.getMid());
				item.setSid(film.getSourceID());
				item.setFid(film.getFilmID());
				item.setFilmName(film.getFilmName());
				item.setPlayUrl(uri.toString());
				playItems.add(item);
				User user = AuthManager.GetInstance().getUser();
				if (user!=null) {
					VoolePlayer.GetInstance().startPlay(context, playItems, 0, null, user.getOemid(), user.getUid(), user.getHid());
				}else{
					VoolePlayer.GetInstance().startPlay(context, playItems, 0);
				}
			}  
		////////////////测试用
		}else{
//			final FilmDownLoad4k dLstaus = DownloadResourceManager.getInstance().getDLstaus(film.getFilmID());
//			if (dLstaus != null && dLstaus.downType == DownType.FINISH) {
//				context.showDialog();
//				context.setProgressDialogCancelable(false);
//				new Thread(){
//					@Override
//					public void run() {
//							Detail detail = MovieManager.GetInstance().getDetailFromMid(dLstaus.Mid);
//							if (detail==null) {
//								handler.sendEmptyMessage(NOT_ORDERED_INFO);
//								Log.e(TAG, "没有订购信息, 只能预览  不可以下载");
//								return;
//							}
//							List<Content> contents = detail.getContentList();
//							List<Product> products = detail.getProductList();
//							if (contents!=null&&contents.size()>0&&products!=null&&products.size()>0) {
//				
//								Ad ad = BasePlayManager.GetInstance().getPlayUrl(film.getMid(),
//										contents.get(0).getContentIndex(),
//										contents.get(0).getFid(),
//										products.get(0).getPtype(),
//										contents.get(0).getDownUrl());
//								
//								
//								if(ad == null){
//									handler.sendEmptyMessage(NOT_ORDERED_INFO);
//									Log.e(TAG, "没有订购, 只能预览  不可以下载");
//									return;
//								}
//								if("0".equals(ad.getPriview())||("1".equals(ad.getPriview()) && "0".equals(ad.getPriviewTime()))){
//									if (ad.getAdxml()!=null) {
//										try {
//											ADResponse adResponse = new ADResponse(ad.getAdxml());
//											String playUrl = adResponse.play_url;
//											if (!TextUtils.isEmpty(playUrl)) {
//												dLstaus.playUrl  = playUrl;
//												handler.post(new Runnable() {
//													@Override
//													public void run() {
//														context.cancelDialog();
//														DownloadResourceManager.getInstance().playFromLoacal(dLstaus, context);
//													}
//												});
//											}else if(Integer.parseInt(ad.getStatus())<0){
//												handler.sendEmptyMessage(GET_PLAY_URL_ERROR);
//												Log.e(TAG, "没有订购, 只能预览  不可以下载 ad.getStatus()"+ad.getStatus());
//												return;
//											}else{
//												handler.sendEmptyMessage(GET_PLAY_URL_FAIL);
//											}
//											Log.e(TAG, "adResponse.play_url--> "+adResponse.play_url);
//										} catch (XmlPullParserException e) {
//											Log.e(TAG, "XmlPullParserException",e);
//										} catch (IOException e) {
//											Log.e(TAG, "IOException",e);
//										}
//									}else{
//										handler.sendEmptyMessage(GET_PLAY_URL_FAIL);
//									}
//								}else{
//									handler.sendEmptyMessage(ORDERED_TIMEOUT);
//									Log.e(TAG, getContext().getString(
//											R.string.film_order_time_out));
//									return;
//								}
//							}
//						}
//				}.start();
//			} else {
//				if (listener != null) {
//					listener.onPlay(context,film.getFilmID());
//				}else{
//					Toast.makeText(context, R.string.the_net_not_support, 0).show();
//				}
//
//			}
			if (listener != null) {
				context.stopDLService();
				new Thread(){
					public void run(){
						DownloadResourceManager.getInstance().getDownloadController().DLDownPause();
					}
				}.start();
				listener.onPlay(context,film.getMid());
			}else{
				Toast.makeText(context, R.string.the_net_not_support, 0).show();
			}
		}
		
 	}
	@Override
	public void onDownload(final Film film) {
		String fid = film.getFilmID();
		filmf4K = DownloadResourceManager.getInstance().getDLstaus(fid);
		if (filmf4K==null) {
			File path = new File(DownloadResourceManager.getInstance().getDownLoadPath());
			if (TextUtils.isEmpty(DownloadResourceManager.getInstance()
					.getDownLoadPath())
			|| !path.getParentFile().exists() || !path.exists()){
				checkDownPath();
			} else {
				//下载
				context.showDialog();
				getPlayItemAndDown(film);
			}
//			context.setProgressDialogCancelable(false);
		}else{
			if (BuildConfig.DEBUG)
			Toast.makeText(context, "跳到下载管理", 0).show();
			Intent intent = new Intent();
			intent.setClass(context, DownManagerActivity.class);
			context.startActivity(intent);
			FilmDetailDialog.this.dismiss();
		}

	}

	private void getPlayItemAndDown(final Film film) {
		new Thread(){
			@Override
			public void run() {
				PlayItem playItem = getPlayItem(film);
				onCreateDownload(film,playItem);
			}
			
		}.start();
	}
	private void checkDownPath() {
			List<String> pathsStr = DownloadResourceManager.getInstance().getFileDir();
			if(pathsStr != null){
				checkView = new CheckDownDialogView(context);
				checkView.choiceUsbDialog();
				checkView.setListener(new PathChoiceClickListener() { //回调
					@Override
					public void onClick(View v) {
						String path = (String) v.getTag();
						DownloadResourceManager.getInstance().clearDLData();
						DownloadResourceManager.getInstance().initDownPath(path);
						context.showDialog();
						getPlayItemAndDown(film);
					}
				});
			}else{
				// 没插U盘给对话框提示
				TVAlertDialog  tvDialog= new TVAlertDialog(context);
				tvDialog.showInspectDialog(context);
			}
	}

	private void onCreateDownload(final Film film, PlayItem playItem) {
		String fid = film.getFilmID();
		filmf4K = DownloadResourceManager.getInstance().getDLstaus(fid);
//		DownLoadFilm df = new DownLoadFilm(context);
//		df.onDownLoad(playItem,new DownListener() {
//			@Override
//			public void onResult(int code, FilmDownLoad4k filmf4K) {
//				Message msg = handler.obtainMessage();
//				msg.obj = filmf4K;
//				msg.what = code;
//				handler.sendMessage(msg);
//			}
//		});
//		handler.post(new Runnable() {
//			@Override
//			public void run() {
//				context.showDialog();
//				
//			}
//		});
		new BaseDownloadManager().setDownLoadCreateListener(new DownLoadCreateListener() {
			@Override
			public void onSuccess(FilmDownLoad4k filmf4K) {
				if (film!=null&&film.getFilmID().equalsIgnoreCase(filmf4K.fid_epg)) {
					movieDetailView.setButtonStatus(filmf4K);
					final Toast makeText = Toast.makeText(context, R.string.task_create_success, Toast.LENGTH_SHORT);
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							makeText.setText(R.string.three_task_allow_at_one_time);
							makeText.setDuration(2000);
							makeText.show();
						}
						
					},1000);
				}
				context.cancelDialog();
			}
			@Override
			public void onFiled(int code, FilmDownLoad4k film) {
				if (code==IContent.NOT_ORDERED) {
					context.getOrderDialog(context.getString(R.string.do_not_pay_this_film)).show();
				}
				context.cancelDialog();
				
			}
//			@Override
//			public void onStarted(PlayItem item) {
//				context.showDialog();
//			}
			@Override
			public void onUnEnough() {
				showChoiceDialog();
			}
		}).createDownTask(context,playItem);
	}


	@Override
	public void onCancel(Film film) {
		this.cancel();
	}
	private String getCheckInfoResult(PlayCheckInfo playCheckInfo,List<Product> productList) {
		if ("0".equals(playCheckInfo.getOrder())) {
			int productSize = productList.size();
			for (int i = 0; i < productSize; i++) {
				if (playCheckInfo.getPid().equals(productList.get(i).getPid())) {
					String stopTime = playCheckInfo.getEndtime();
					return "您已订购" + productList.get(i).getName() + "，可在"
							+ stopTime + "前免费观看";
				}
			}
		}
		if ("0".equals(playCheckInfo.getViewed())) {
			String endTime = playCheckInfo.getEndtime();
			return "您已看过该影片，可在" + endTime + "前免费观看";
		}
		return "";
	}
	
	//磁盘满了选择其他盘
	public void showChoiceDialog(){
		choiceUsbDialog = new com.voole.epg.download.base.TVAlertDialog.Builder(this.context);
		choiceUsbDialog.setCancelable(false);
		choiceUsbDialog.setTitle(R.string.disk_freespace_not_enough);
		choiceUsbDialog.setPositiveButton(R.string.present_sele, new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
//				context.showDialog();
				CheckDownDialogView checkDown = new CheckDownDialogView(context);
				checkDown.choiceUsbDialog( );
				checkDown.setListener(new PathChoiceClickListener() { //回调
					@Override
					public void onClick(View v) {
						String path = (String) v.getTag();
						DownloadResourceManager.getInstance().clearDLData();
						DownloadResourceManager.getInstance().initDownPath(path);
						context.showDialog();
						getPlayItemAndDown(film);
//						System.out.println(path + "路径==================================");
//						LinkedHashMap<String, FilmDownLoad4k> dlStatus = DownloadResourceManager.getInstance().getDLStatus();
////						Log.e(dlStatus +"   "+dlStatus.size());
//						adapterLinearLayout = new FilmLinearLayout(context);
//						adapterLinearLayout.setData(dlStatus);
//						adapterLinearLayout.notifyDataChange();
//						DownLoadMangerView dv = new DownLoadMangerView(context);
//						dv.initPage();
//						DownManagerActivity.this.cancelDialog();
					}
				});
				
			}
		});
		choiceUsbDialog.setNegativeButton(R.string.common_cancel_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.setProgressDialogCancelable(false);
				dialog.dismiss();
			}
		});
		choiceUsbDialog.create().show();
		
		
	}
	

	public PlayItem getPlayItem(final Film film) {
		
		PlayItem playItem = null;
		Detail detail = MovieManager.GetInstance().getDetailFromSrcUrl(film.getSourceUrl());
		if (detail == null) {
			handler.sendEmptyMessage(SERVER_ERROR);
			return null;
		}
		List<Content> contents = detail.getContentList();
		List<Product> products = detail.getProductList();
		if (contents != null && contents.size() >0&& products != null && products.size() > 0) {

			Ad ad = BasePlayManager.GetInstance().getPlayUrl(
					film.getMid(),
					contents.get(0).getContentIndex(),
					contents.get(0).getFid(),
					products.get(0).getPtype(),
					contents.get(0).getDownUrl());

			if (ad == null) {
				handler.sendEmptyMessage(NOT_ORDERED_INFO);
				Log.e(TAG, "没有订购, 只能预览  不可以下载");
				return null;
			}

			if ("0".equals(ad.getPriview())|| ("1".equals(ad.getPriview()) && "0".equals(ad.getPriviewTime()))) {
				if (ad.getAdxml()!=null) {
					playItem = new PlayItem();
					playItem.setAdXml(ad.getAdxml());
					playItem.setFid(film.getFilmID());
					playItem.setMid(products.get(0).getMid());
					playItem.setSid(products.get(0).getSid());
					playItem.setFilmName(film.getFilmName());
					return playItem;
				}else{
					handler.sendEmptyMessage(CREATE_TASK_FAIL);
				}
			}else if (Integer.parseInt(ad.getStatus()) < 0) {
				handler.sendEmptyMessage(GET_DOWN_URL_FAIL);
				Log.e(TAG, "没有订购, 只能预览  不可以下载 ad.getStatus()"
						+ ad.getStatus());
				return null;
			} else {
				Log.e(TAG, "没有订购, 只能预览  不可以下载");
				handler.sendEmptyMessage(NOT_ORDERED);
				return null;
			}
		}else{
			handler.sendEmptyMessage(CREATE_TASK_FAIL);
		}
		
		return playItem;
	}
	
}
