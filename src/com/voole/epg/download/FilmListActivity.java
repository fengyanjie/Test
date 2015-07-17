package com.voole.epg.download;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.voole.epg.corelib.model.auth.AuthManager;
import com.voole.epg.corelib.model.auth.UrlList;
import com.voole.epg.corelib.model.auth.User;
import com.voole.epg.corelib.model.movies.Film;
import com.voole.epg.corelib.model.movies.FilmAndPageInfo;
import com.voole.epg.corelib.model.movies.MovieManager;
import com.voole.epg.corelib.model.navigation.FilmClass;
import com.voole.epg.download.base.BaseActivity;
import com.voole.epg.download.base.RegistManager;
import com.voole.epg.download.base.TVAlertDialog;
import com.voole.epg.download.bean.DownloadResourceManager;
import com.voole.epg.download.bean.F4kFilmAndPageInfo;
import com.voole.epg.download.bean.FilmDownLoad4k;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;
import com.voole.epg.download.widget.ExitDialogContinueView;
import com.voole.epg.download.widget.ExitDialogView;
import com.voole.epg.download.widget.MovieViewFocusListener;
import com.voole.epg.download.widget.MovieViewListener;
import com.voole.epg.download.widget.SingleLineMovieView;
import com.voole.epg.f4k.R;
import com.voole.tvutils.ImageManager;
import com.voole.util.net.HttpDownloaderUtil;
import com.voole.util.prop.PropertiesUtil;

public class FilmListActivity extends BaseActivity implements MovieViewListener, OnClickListener, MovieViewFocusListener {
	private static final int GET_DATA_SUCCESS = 1;
	private static final int GET_DATA_FAILURE = 2;
	protected static final String TAG = "F4KListActivity";
	private static final int SET_BG = 0x111111;
	private SingleLineMovieView movieView = null;
	private FilmAndPageInfo filmAndPageInfo = null;
	private String topicUrl=null;
	private LinearLayout mainLayout = null;
	private Button downMangerButton;
	private String topicBigUrl;
	private List<Film> priceFilmList;
	private int pageNo = 1;
	private int ITEM_SIZE = 6;
	private String mid;
	private String epg4kInterfaceUrl;
	private String accountUrl;
	private String playAuth;
	private FilmDetailDialog detailDialogf4k;
	private LinearLayout movieViewLy;
	private ImageView f4k_im_logo;
	private int selceted;
	private boolean isDownBackground = false;
	private boolean isDownLoding = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DownloadResourceManager.getInstance().initContext(this);
		setContentView(R.layout.f4k_fk_film_list);
		init();
		getData();
		
//		DownloadResourceManager.getInstance().upgradeRootPermission(getPackageCodePath());
	}



	private void init() {
		this.setResult(Activity.RESULT_OK);
		mainLayout = (LinearLayout)findViewById(R.id.bg);
		movieView = new SingleLineMovieView(this, ITEM_SIZE, 8, 1, DisplayManager.TEXTSIZE+4);
		LinearLayout.LayoutParams movieViewParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		movieView.setLayoutParams(movieViewParams);
		epg4kInterfaceUrl = PropertiesUtil.getProperty(this, "epg4kInterfaceUrl");
		movieViewLy = (LinearLayout) findViewById(R.id.topic_list_layout);
		movieViewLy.addView(movieView);
		downMangerButton = (Button) findViewById(R.id.ck_fk_button_downManger);
		if (DownloadResourceManager.getInstance().getDownLoadFlag()==null||!DownloadResourceManager.getInstance().getDownLoadFlag().equals("1")) {
			downMangerButton.setVisibility(View.INVISIBLE);
		}
		f4k_im_logo=(ImageView) findViewById(R.id.f4k_im_logo);
		movieView.setMovieViewListener(this);
		downMangerButton.setOnClickListener(this);
		movieView.setMovieViewFocusListener(this);
		
		
	}

	private void getData() {
		
		
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			
			FilmClass filmClass = (FilmClass) bundle.getSerializable("navigation");
			String showLogo=bundle.getString("showLogo");
			
			if("0".equals(showLogo)){
				f4k_im_logo.setVisibility(View.INVISIBLE);
			}
			
			if (filmClass!=null) {//导航跳转
				topicUrl = filmClass.getFilmClassUrl();
				if (topicUrl==null) {
					Toast.makeText(this, "topicUrl is null", 0).show();
					this.finish();
					return;
				}
				getFilmList(1);
				
			}else{
				topicUrl = bundle.getString("topicUrl");
				topicBigUrl = bundle.getString("topicBigUrl");
				pageNo = bundle.getInt("pageNo")==0?1:bundle.getInt("pageNo");
				mid = bundle.getString("mid");
				
				if(topicUrl==null||"".equals(topicUrl)){
					String id=topicUrl = bundle.getString("id");
					topicUrl=AuthManager.GetInstance().getUrlList().getPayList()+"&ctype=3&column="+id;
				} 
				getFilmList(pageNo);
				if(topicBigUrl!=null&&!"".equals(topicBigUrl)){
					setBg(topicBigUrl);
				}
			}
			
		}else{
			getFilmList(1);
		}
	}
	
	

	
	private void getFilmList(final int pageNo){
		showDialog();
		new Thread(){
			public void run() {
				
				Bundle bundle = getIntent().getExtras();
				if(bundle != null){
					accountUrl = bundle.getString("accountUrl");
					playAuth = bundle.getString("playAuth");
						if (!TextUtils.isEmpty(accountUrl)) {
							UrlList urlList = AuthManager.GetInstance().getUrlList();
							if (urlList!=null) {
								urlList.setAccount(accountUrl);
							}
						}
						if (!TextUtils.isEmpty(playAuth)) {
							UrlList urlList = AuthManager.GetInstance().getUrlList();
							if (urlList!=null) {
								urlList.setPlayAuth(playAuth);
							}
						}
					}else{
						//http://account.voole.com/tv/playauth.php?spid=20120629&epgid=100500&oemid=797&hid=ACDBDA00977D&uid=103052205
						
						User user = AuthManager.GetInstance().getUser();
						if (user!=null) {
							appendIds(epg4kInterfaceUrl, user);
							String jsonExtras = HttpDownloaderUtil.readString(epg4kInterfaceUrl);
							JSONObject jsonObject = null;
							if (TextUtils.isEmpty(jsonExtras)) {
								Log.e(TAG, "获取播放列表出错: ->>"+epg4kInterfaceUrl);
								sendMessage(GET_DATA_FAILURE);
								return;
							}
							try {
								jsonObject = new JSONObject(jsonExtras);
								if(!jsonObject.isNull("topicUrl")){
									topicUrl = jsonObject.getString("topicUrl");
								}
								if(!jsonObject.isNull("accountUrl")){
									accountUrl = jsonObject.getString("accountUrl");
								}
								if (!jsonObject.isNull("playAuth")) {
									playAuth = jsonObject.getString("playAuth");
									initAuth(playAuth);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (!TextUtils.isEmpty(accountUrl)) {
								UrlList urlList = AuthManager.GetInstance().getUrlList();
								if (urlList!=null) {
									urlList.setAccount(accountUrl);
								}
							}
							if (!TextUtils.isEmpty(playAuth)) {
								UrlList urlList = AuthManager.GetInstance().getUrlList();
								if (urlList!=null) {
									urlList.setPlayAuth(playAuth);
								}
							}
						}else{
							FilmListActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									sendMessage(ACCESS_NET_FAIL);
								}
							});
							return;
						}
					}
				FilmAndPageInfo pageInfo=null;
//				if (filmAndPageInfo!=null&&!(filmAndPageInfo instanceof F4kFilmAndPageInfo)) {
//					filmAndPageInfo = MovieManager.GetInstance().getFilmsOfTopic(topicUrl, pageNo, ITEM_SIZE);
//				}else
//				filmAndPageInfo = MovieManager.GetInstance().getFilmsOfTopic(topicUrl, pageNo, ITEM_SIZE*2);
				pageInfo = MovieManager.GetInstance().getFilmsOfTopic(topicUrl, pageNo, ITEM_SIZE);
				
				if(pageInfo == null){
					sendMessage(ACCESS_NET_FAIL);
				}else{
					
					F4kFilmAndPageInfo lowerRateFilmAndPageInfo = getLowerRateFilmAndPageInfo(pageInfo);
					if (lowerRateFilmAndPageInfo!=null) {
						pageInfo = lowerRateFilmAndPageInfo;
					}else{
						FilmAndPageInfo info = MovieManager.GetInstance().getFilmsOfTopic(topicUrl, pageNo, ITEM_SIZE);
						if (info!=null) {//重置页码
							pageInfo.setPageCount(info.getPageCount());
							pageInfo.setPageIndex(info.getPageIndex());
						}
					}
					
					
					if(pageInfo.getFilmList()!=null&&pageInfo.getFilmList().size()>0){
						priceFilmList = MovieManager.GetInstance().getMoviesListForPrice(pageInfo.getFilmList());
						if(priceFilmList != null && priceFilmList.size()>0){
							filmAndPageInfo = pageInfo;
							sendMessage(GET_DATA_SUCCESS);
						}else{
							sendMessage(GET_DATA_FAILURE);
						}
					}else{
						sendMessage(GET_DATA_FAILURE);
					}
				}
			};
		}.start();
	}
	

	protected F4kFilmAndPageInfo getLowerRateFilmAndPageInfo(FilmAndPageInfo filmInfo) {
		List<Film> filmList = filmInfo.getFilmList();
		F4kFilmAndPageInfo  info = null;
		ArrayList<Film> lowRateFilms = null;
		if (filmList!=null&&filmList.size()>1) {
			Film filmFirst = filmInfo.getFilmList().get(0);//第一个  规则: 12341234 类似名字格式
			lowRateFilms = new ArrayList<Film>();
			int index = 0;
			for (int i = 1; i < filmList.size(); i++) {
//				if(TextUtils.isEmpty(filmList.get(i).getFilmName())&&filmList.get(i).getFilmName().startsWith(filmFirst.getFilmName().substring(0, filmFirst.getFilmName().lastIndexOf("-")==-1?0:filmFirst.getFilmName().lastIndexOf("-")))){
//					index = i;
//				}
				if (!TextUtils.isEmpty(filmFirst.getFilmName())&&!TextUtils.isEmpty(filmList.get(i).getFilmName())) {
					String[] first= filmFirst.getFilmName().trim().split("-");
					String[] next= filmList.get(i).getFilmName().trim().split("-");
					if (first.length>0&&next.length>0) {
						if (first[0].equalsIgnoreCase(next[0])) {
							index = i;
							com.voole.epg.download.utils.Log.e("match index = "+i);
						}
					}
				}
				if (index!=0) {
					if (info==null) {
						info = new F4kFilmAndPageInfo();
					}
					lowRateFilms.add(filmList.get(i));
				}
			}
		}
		if (info!=null) {
			filmList.removeAll(lowRateFilms);
			info.setFilmList(filmList);
			info.setPageIndex(filmInfo.getPageIndex());
			info.setPageCount(filmInfo.getPageCount());
			info.setFilmCount(filmList.size());
			info.setName(filmInfo.getName());
			info.setImageUrl(filmInfo.getImageUrl());
			info.setFilmListLowRate(lowRateFilms);
		}
		return info;
	}

	protected String appendIds(String url, User user) {
		//http://account.voole.com/tv/playauth.php?spid=20120629&epgid=100500&oemid=797&hid=ACDBDA00977D&uid=103052205
		if (user==null) {
			return url;
		}
		if (TextUtils.isEmpty(url)) {
			return AuthManager.GetInstance().getUrlList().getPlayAuth();
		}
		if (url.contains("uid")) {
			return url;
		}
		if (!url.contains("?")) {
			url+="?";
			url+="oemid="+user.getOemid();
		}else{
			url+="&oemid="+user.getOemid();
		}
		url+="&uid="+user.getUid();
		url+="&hid="+user.getHid();
		url+="&spid="+user.getSid();
		url+="&sid="+user.getSid();
		return url;
	}

	protected void initAuth(String playAuth2) {
		// TODO Auto-generated method stub
		
	}

	private void setBg(String imgUrl){
		com.voole.epg.download.utils.Log.e(imgUrl);
		ImageManager.GetInstance().displayImage(imgUrl, mainLayout);
	}
	
	@Override
	protected void doHandleMessage(int what, Object obj) {
		switch (what) {
		case GET_DATA_SUCCESS:
			cancelDialog();
			if (handler.hasMessages(SET_BG)) {
				handler.removeMessages(SET_BG);
			}
			movieView.setData(filmAndPageInfo.getFilmList());
			selceted = 0;
			
			movieView.setPageInfo(filmAndPageInfo.getPageIndex(), filmAndPageInfo.getPageCount());
			movieView.requestFocus();
			
			setBg(filmAndPageInfo.getFilmList().get(0).getImgUrlB());
			
			if (filmAndPageInfo!=null&&filmAndPageInfo.getClass()!=null) {
				List<Film> filmList = filmAndPageInfo.getFilmList();
				for (int i = 0; i <ITEM_SIZE; i++) {
					if (i<filmList.size()) {
						if (filmList.get(i).getMid().equals(mid)) {
							movieView.setFocusedItem(i);
							
							if (detailDialogf4k==null) {
								detailDialogf4k = new FilmDetailDialog(this);
							}
							if (!this.isFinishing()&&!detailDialogf4k.isShowing()) {
								detailDialogf4k.show();
							}
							mid = "";
							detailDialogf4k.setFilm(filmAndPageInfo,i);
							setBg(filmAndPageInfo.getFilmList().get(i).getImgUrlB());
							break;
						}
					}
				}
			}
			
			
			break;
		case GET_DATA_FAILURE:
			new TVAlertDialog.Builder(FilmListActivity.this)
			.setCancelable(false)
			.setTitle(R.string.no_film_at_this_topic)
			.setPositiveButton(R.string.common_ok_button,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							finish();
						}
					}).create().show();
			break;
			
		case SET_BG:
			
			setBg(filmAndPageInfo.getFilmList().get(selceted>=filmAndPageInfo.getFilmList().size()?filmAndPageInfo.getFilmList().size()-1:selceted).getImgUrlB());
			
			
			break;
		default:
			break;
		}
	}

	
	
	
	@Override
	public void onItemSelected(Film item, int index) {
		//选择确定某一个
		if (detailDialogf4k==null) {
			detailDialogf4k = new FilmDetailDialog(this);
		}
		if (!this.isFinishing()&&!detailDialogf4k.isShowing()) {
			detailDialogf4k.show();
		}
		detailDialogf4k.setFilm(filmAndPageInfo,index);
		
//		detailView.setDetailData(null, item);
//		detailView.setVisibility(View.GONE);
		
	}

	@Override
	public void onPlay(Film item) {
		//do nothing
		
	}

	@Override
	public void onGotoPage(int pageNo) {

		getFilmList(pageNo);
		
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (getIntent().getIntExtra("is4K", 0)==1) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				final Dialog dialog = new Dialog(this,R.style.alertDialog);
				LayoutParams params =new LayoutParams(500, 300);
				ExitDialogView view  = new ExitDialogView(this);
				view.setTClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.cancel();
						}
						isDownLoding = false;
						LinkedHashMap<String, FilmDownLoad4k> downStatus = DownloadResourceManager.getInstance().getDLStatus();
						if(downStatus != null){
							Set<Entry<String, FilmDownLoad4k>> entrySet = downStatus.entrySet();
							for(Entry<String, FilmDownLoad4k> entry : entrySet){
								if(entry.getValue().downType == DownType.DOWNLOADING){ //下载中
									isDownLoding = true;
								}
							}
						}
						String isDownback = PropertiesUtil.getProperty(FilmListActivity.this, "isDownBackground");
						isDownBackground = Boolean.parseBoolean(isDownback);
						if(isDownBackground && isDownLoding){ //下载中
							exitDialog();
						} else {
							FilmListActivity.this.setResult(800);//退出
							FilmListActivity.this.onBackPressed();
							ExitDialog.getInstance(FilmListActivity.this).showDialogWithMessage(R.string.download_exit_msg);
						}
					}

				});
				view.setBClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (dialog.isShowing()) {
							dialog.cancel();
						}
						FilmListActivity.this.setResult(600);//继续观看
						FilmListActivity.this.finish();
					}
				});
				
				dialog.setContentView(view, params);
				dialog.show();
				return true;
			default:
				break;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	public void exitDialog() {
		final Dialog dia = new Dialog(this,R.style.alertDialog);
		LayoutParams params =new LayoutParams(500, 300);
		
		ExitDialogContinueView diaView  = new ExitDialogContinueView(FilmListActivity.this);
		diaView.setOkClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dia.isShowing()){
					dia.cancel();
					//点击按钮实现home键的功能
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);           
					startActivity(intent);
					
					if(TextUtils.isEmpty(PropertiesUtil.getProperty(FilmListActivity.this, "isDownBackground")) || !isDownBackground){
						FilmListActivity.this.setResult(800);//退出
						FilmListActivity.this.onBackPressed();
						isDownBackground = false;
					} else { //后台下载
						isDownBackground = true;
						new Thread(){
							@Override
							public void run() {
								String maxBand = DownloadResourceManager.formetFileSize(DownLoadService.realspeed,"isKB");
								DownloadResourceManager.getInstance().setMaxBand(Integer.parseInt(maxBand));//后台下载
							}
						}.start();
					}
//					F4KListActivity.this.setResult(400);//退出
//					F4KListActivity.this.onBackPressed();
				}
			}
		});
		diaView.setCancelClicklistener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(dia.isShowing()){
					dia.cancel();
					FilmListActivity.this.setResult(800);//退出
					FilmListActivity.this.onBackPressed();
				}
			}
		});
		
		dia.setContentView(diaView, params);
		dia.show();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.ck_fk_button_downManger){
			Intent intent = new Intent();
			intent.setClass(this, DownManagerActivity.class);
			this.startActivity(intent);
		}
		
	}

	@Override
	public void onItemFocusChange(int selceted) {
		this.selceted = selceted;
		if (filmAndPageInfo!=null&&filmAndPageInfo.getFilmList()!=null) {
			if (handler.hasMessages(SET_BG)) {
				handler.removeMessages(SET_BG);
			}
			handler.sendEmptyMessageDelayed(SET_BG, 500);
		}
	}

	
	public String getAuthUrl(){

//"playAuth":"http://userauth.voole.com/Agent.do?spid=20120629&epgid=100500&oemid=797&hid=ACDBDA00977D&uid=103052205"
 

		if (AuthManager.GetInstance().getUser()==null) {
			return null;
		}
		return appendIds(playAuth, AuthManager.GetInstance().getUser());
	}
	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
//		if(VoolePlayerUtil.isHome(this)){
//			this.setResult(800);
//			this.finish();
//		}
	}
	
	@Override
	protected void onDestroy() {
		com.voole.epg.download.utils.Log.e("onDestroy");
		DownloadResourceManager.getInstance().gcContext();
//		if (TextUtils.isEmpty(PropertiesUtil.getProperty(this, "isDownBackground"))||!Boolean.parseBoolean(PropertiesUtil.getProperty(this, "isDownBackground"))) {
//			stopDLService();
//			DownloadResourceManager.getInstance().clearDLData();
//		}
		if(!isDownBackground){
			stopDLService();
			DownloadResourceManager.getInstance().clearDLData();
		} 
		super.onDestroy();
	}
	
	public void stopDLService() {
		if (DownloadResourceManager.getInstance().getDownLoadFlag()==null||!DownloadResourceManager.getInstance().getDownLoadFlag().equals("1")) {
			Log.e(this.getClass().getName(), "未打开下载开关");
			return;
		}
		
		
		Intent service = new Intent();
		service.setClass(this, DownLoadService.class);
		this.stopService(service);
		
	}

	@Override
	protected void onResume() {
		if(RegistManager.getInstance().getRegister()!=null){
			RegistManager.getInstance().getRegister().hasRegist(this);
		}
		super.onResume();
	}
	
	
	
	
	
}
