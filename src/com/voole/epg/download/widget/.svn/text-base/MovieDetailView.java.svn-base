package com.voole.epg.download.widget;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.voole.epg.corelib.model.account.Product;
import com.voole.epg.corelib.model.movies.Detail;
import com.voole.epg.corelib.model.movies.Film;
import com.voole.epg.corelib.model.movies.FilmAndPageInfo;
import com.voole.epg.corelib.model.movies.MovieManager;
import com.voole.epg.download.DisplayManager;
import com.voole.epg.download.FilmListActivity;
import com.voole.epg.download.base.BaseLinearLayout;
import com.voole.epg.download.base.ID;
import com.voole.epg.download.bean.DownloadResourceManager;
import com.voole.epg.download.bean.F4kFilmAndPageInfo;
import com.voole.epg.download.bean.FilmDownLoad4k;
import com.voole.epg.download.bean.FilmDownLoad4k.DownType;
import com.voole.epg.download.utils.Log;
import com.voole.epg.f4k.R;
import com.voole.util.prop.PropertiesUtil;

public class MovieDetailView extends BaseLinearLayout{
	private FilmListActivity context;
	private static final int layout_love_id = 1999;
	private static final int price_id = 2001;
	private static final int price_tv_id = 2002;
	private static final int bargin_id = 2003;
	private static final int bargin_tv_id = 2004;
	
	private static final int rightBtn_id = 10072;
	
	private static final int middleBtn_id = 10073;
	
	private static final int layout_btn_id = 1007;
	
	private static final int text_order_id = 1006;
	
	private static final int text_actor_id = 1005;
	
	private static final int text_director_id = 1004;
	
	private static final int text_movie_class_id = 1000;
	
	private static final int text_year_mins_area_type_id = 1003;
	
	private static final int text_filmName_id = 1002;
	
	private static final int detail_left_btn_id = 2005;


	private static final int text_introduce_id = 0x001111;

	
	//msg
	protected static final int GET_DOWN_SUCCESS = 0x001010;
	protected static final int GET_DOWN_FAIL = 0x001011;
	protected static final int GET_DETAIL_SUCCESS = 0x001012;
	
	
	
//	private TextView text_price = null;
	private TextView text_barginPrice = null;
	private AlwaysMarqueeTextView text_filmName = null;
	private TextView text_year_mins_area_type = null;
	private TextView text_introduce = null;
	private AlwaysMarqueeTextView text_director = null;
	private AlwaysMarqueeTextView text_actor = null;
	private Button leftBtn = null;
	private Button leftBtn_playlow = null;
	private Button middleBtn = null;
	private Button rightBtn = null;
	
	private RelativeLayout layout_btn=null;
	
	
	
	private final int textSize = DisplayManager.TEXTSIZE+4;

	
	
	private Film film = null;

	private DetailButtonListener listener = null;

	public void setDetailButtonListener(DetailButtonListener listener) {
		this.listener = listener;
	}


	private TextView text_dl_info;
	private FilmDownLoad4k download;

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DOWN_SUCCESS:
				setButtonStatus((FilmDownLoad4k) msg.obj);
				break;
			case GET_DOWN_FAIL:
				setButtonStatus(new FilmDownLoad4k());
				break;
			case GET_DETAIL_SUCCESS:
				Detail detail = (Detail) msg.obj;
				setProductsData(detail.getProductList());
				break;
			default:
				break;
			}
		};
	};
	private TextView barginPrice;
	private TextView price;
	private int index;
	private FilmAndPageInfo info;
	
	
	public MovieDetailView(Context context) {
		super(context);
		init(context);
	}

	public MovieDetailView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MovieDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * left : right --- 1:3
	 * 
	 * @param context
	 */
	private void init(Context context) {
		setOrientation(VERTICAL);
		initRight(context);
	}



	private void initRight(Context context) {
		RelativeLayout layout_right = new RelativeLayout(context);
		LinearLayout.LayoutParams param_layout_right = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		int margin = 35;
		param_layout_right.setMargins(margin, margin, margin, margin); 
		layout_right.setLayoutParams(param_layout_right);


		text_filmName = new AlwaysMarqueeTextView(context);
		text_filmName.setId(text_filmName_id);
		text_filmName.setMarquee(true);
		text_filmName.setTextColor(Color.WHITE);
		text_filmName.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize+8);
		RelativeLayout.LayoutParams param_text_filmName = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param_text_filmName.addRule(RelativeLayout.CENTER_HORIZONTAL);
		param_text_filmName.bottomMargin = 5;
		text_filmName.setLayoutParams(param_text_filmName);
		layout_right.addView(text_filmName);

		text_year_mins_area_type = new TextView(context);
		text_year_mins_area_type.setId(text_year_mins_area_type_id);
		text_year_mins_area_type.setTextColor(Color.WHITE);
		text_year_mins_area_type.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		RelativeLayout.LayoutParams param_text_year_mins_area_type = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		param_text_year_mins_area_type.addRule(RelativeLayout.BELOW, text_filmName_id);
		param_text_year_mins_area_type.bottomMargin = 5 ;
		text_year_mins_area_type
				.setLayoutParams(param_text_year_mins_area_type);
		layout_right.addView(text_year_mins_area_type);
		
		TextView text_movie_class  = new TextView(context);//电影分类
		text_movie_class.setId(text_movie_class_id);
		
		
		barginPrice = new TextView(context);
		barginPrice.append("￥0");
		barginPrice.setTextColor(Color.parseColor("#00c1ea"));
		barginPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize+5);
		RelativeLayout.LayoutParams barginPriceParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		barginPriceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		barginPriceParams.addRule(RelativeLayout.ALIGN_BOTTOM, text_year_mins_area_type_id);
		barginPrice.setLayoutParams(barginPriceParams);
		barginPrice.setId(price_id);
		layout_right.addView(barginPrice);
		
		TextView barginPriceTextView = new TextView(context);
		barginPriceTextView.setText(" 促销价: ");
		barginPriceTextView.setTextColor(Color.GRAY);
		barginPriceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		RelativeLayout.LayoutParams param_price = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param_price.addRule(RelativeLayout.LEFT_OF, price_id);
		param_price.addRule(RelativeLayout.ALIGN_BOTTOM, price_id);
		barginPriceTextView.setLayoutParams(param_price);
		barginPriceTextView.setId(price_tv_id);
		layout_right.addView(barginPriceTextView);
		

		
		price = new TextView(context);
		price.setText("￥0 ");
		price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		price.setId(10012);
		price.setTextColor(Color.GRAY);
		price.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		price.setId(bargin_id);
		RelativeLayout.LayoutParams priceParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		priceParams.addRule(RelativeLayout.ALIGN_BOTTOM, price_id);
		priceParams.addRule(RelativeLayout.LEFT_OF, price_tv_id);
		price.setLayoutParams(priceParams);
		layout_right.addView(price);
		
		TextView priceTextView = new TextView(context);
		priceTextView.setText("原价:");
		priceTextView.setId(10012);
		priceTextView.setTextColor(Color.GRAY);
		priceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		RelativeLayout.LayoutParams param_barginPrice = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		param_barginPrice.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		param_barginPrice.addRule(RelativeLayout.ALIGN_BOTTOM, price_id);
		param_barginPrice.addRule(RelativeLayout.LEFT_OF, bargin_id);
		priceTextView.setLayoutParams(param_barginPrice);
		layout_right.addView(priceTextView);
		
		
		
		
		
		

		text_introduce = new TextView(context);
		text_introduce.setTextColor(Color.WHITE);
		text_introduce.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		text_introduce.setEllipsize(TruncateAt.END);
		text_introduce.setId(text_introduce_id);
		text_introduce.setMaxLines(3);
		text_introduce.setLineSpacing(3f, 1f);
		RelativeLayout.LayoutParams param_text_introduce = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		param_text_introduce.addRule(RelativeLayout.BELOW, text_year_mins_area_type_id);
		text_introduce.setLayoutParams(param_text_introduce);
		layout_right.addView(text_introduce);

		text_director = new AlwaysMarqueeTextView(context);
		text_director.setId(text_director_id);
		text_director.setTextColor(Color.WHITE);
		text_director.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		text_director.setMarquee(false);
		RelativeLayout.LayoutParams param_text_director = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		param_text_director.addRule(RelativeLayout.BELOW, text_introduce_id);
		param_text_director.topMargin = 4;
		text_director.setLayoutParams(param_text_director);
		layout_right.addView(text_director);

		text_actor = new AlwaysMarqueeTextView(context);
		text_actor.setId(text_actor_id);
		text_actor.setTextColor(Color.WHITE);
		text_actor.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
		text_director.setMarquee(false);
		RelativeLayout.LayoutParams param_text_actor = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		param_text_actor.addRule(RelativeLayout.BELOW, text_director_id);
		param_text_actor.topMargin = 4;
		text_actor.setLayoutParams(param_text_actor);
		layout_right.addView(text_actor);

		text_dl_info = new TextView(context);
		text_dl_info.setText(R.string.watch_worning_for4k);
		text_dl_info.setTextColor(Color.parseColor("#FFC125"));
		text_dl_info.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize+2);
		RelativeLayout.LayoutParams param_text_dl_info = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		param_text_dl_info.addRule(RelativeLayout.BELOW, text_actor_id);
		param_text_dl_info.topMargin = textSize-10;
		text_dl_info.setLayoutParams(param_text_dl_info);
		layout_right.addView(text_dl_info);
		if (DownloadResourceManager.getInstance().getDownLoadFlag()==null||!DownloadResourceManager.getInstance().getDownLoadFlag().equals("1")) {
			text_dl_info.setVisibility(View.INVISIBLE);
		}

		layout_btn = new RelativeLayout(context);
		layout_btn.setId(layout_btn_id);
		RelativeLayout.LayoutParams param_layout_btn = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param_layout_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		param_layout_btn.addRule(RelativeLayout.CENTER_HORIZONTAL);
		param_layout_btn.bottomMargin = 10;
		layout_btn.setLayoutParams(param_layout_btn);

		leftBtn = new FocusAbleButton(context);
		leftBtn.setTextSize(textSize);
		leftBtn.setTextColor(Color.WHITE);
		leftBtn.setGravity(Gravity.CENTER);
		leftBtn.setTextSize(textSize);
		leftBtn.setId(detail_left_btn_id);
		
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener!=null) {
						listener.onPlay(film);
				}
				
			}
		});
		RelativeLayout.LayoutParams param_leftBtn = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param_leftBtn.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		param_leftBtn.rightMargin = 45;
		leftBtn.setLayoutParams(param_leftBtn);
		layout_btn.addView(leftBtn);
		
		
		
		
		leftBtn_playlow = new FocusAbleButton(context);
		leftBtn_playlow.setTextSize(textSize);
		leftBtn_playlow.setTextColor(Color.WHITE);
		leftBtn_playlow.setGravity(Gravity.CENTER);
		leftBtn_playlow.setTextSize(textSize);
		leftBtn_playlow.setId(ID.MovieDetaiView.DETAIL_LEFT_BTN_ID);
		
		leftBtn_playlow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener!=null) {
					if (info instanceof  F4kFilmAndPageInfo) {
						F4kFilmAndPageInfo info4k = (F4kFilmAndPageInfo) info;
						if (info4k.getFilmListLowRate().size()>index) {
							listener.onPlay(((F4kFilmAndPageInfo) info).getFilmListLowRate().get(index));
						}else{
							Log.d("can not play l080p case no fid for it");
						}
					}
				}
				
			}
		});
		RelativeLayout.LayoutParams leftBtn_playlowBtn = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftBtn_playlowBtn.addRule(RelativeLayout.RIGHT_OF, detail_left_btn_id);
		leftBtn_playlowBtn.rightMargin = 45;
		leftBtn_playlow.setLayoutParams(leftBtn_playlowBtn);
		layout_btn.addView(leftBtn_playlow);

		
		
		middleBtn = new FocusAbleButton(context);
		middleBtn.setTextColor(Color.WHITE);
		middleBtn.setGravity(Gravity.CENTER);
		middleBtn.setTextSize(textSize);
		middleBtn.setId(middleBtn_id);
		middleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null && film != null) {
					listener.onDownload(film);
				}
			}
		});
		RelativeLayout.LayoutParams param_middleBtn = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param_middleBtn.addRule(RelativeLayout.RIGHT_OF, ID.MovieDetaiView.DETAIL_LEFT_BTN_ID );
		middleBtn.setLayoutParams(param_middleBtn);
		layout_btn.addView(middleBtn);
		
		rightBtn = new FocusAbleButton(context);
		rightBtn.setTextColor(Color.WHITE);
		rightBtn.setGravity(Gravity.CENTER);
		rightBtn.setTextSize(textSize);
		rightBtn.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				if (listener != null && film != null) {
					listener.onCancel(film);
				}
			}
		});
		rightBtn.setId(rightBtn_id);
		RelativeLayout.LayoutParams param_rightBtn = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param_rightBtn.leftMargin = 45;
		param_rightBtn.addRule(RelativeLayout.RIGHT_OF, middleBtn_id);
		rightBtn.setLayoutParams(param_rightBtn);
		layout_btn.addView(rightBtn);
		layout_right.addView(layout_btn);

		RelativeLayout layout_love = new RelativeLayout(context);
		layout_love.setId(layout_love_id);
		RelativeLayout.LayoutParams param_layout_lov = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		param_layout_lov.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout_love.setLayoutParams(param_layout_lov);
		layout_right.addView(layout_love);


		addView(layout_right);
		leftBtn.setPadding(textSize, textSize-10, textSize, textSize-10);
		leftBtn_playlow.setPadding(textSize, textSize-10, textSize, textSize-10);
		middleBtn.setPadding(textSize, textSize-10, textSize, textSize-10);
		rightBtn.setPadding(textSize, textSize-10, textSize, textSize-10);
	}

	public void setDetailData(FilmAndPageInfo info, int index) {
		if (info==null) {
			return;
		}
		if (info.getFilmList()==null) {
			return;
		}
		this.index = index;
		this.info = info;
		this.film = info.getFilmList().get(index);
		leftBtn.requestFocus();
		text_filmName.setText(this.film.getFilmName());
		String str_year="";
		if(!this.film.getYear().contains("不")){
			str_year=this.film.getYear()+"年";
		}
		text_year_mins_area_type.setText(str_year + "  \t"
				+ this.film.getLongTime() + "分钟" + "  \t" + this.film.getArea());
		text_introduce.setText(this.film.getIntroduction()
				+ this.film.getDescription());
		text_director.setText("导演:" + this.film.getDirector());
		text_actor.setText("主演:" + this.film.getActor());
		
		text_dl_info.setText(R.string.get_film_info);
		
		leftBtn.setText(R.string.watch_online);
		middleBtn.setVisibility(View.VISIBLE);
		middleBtn.setText(R.string.download_at_once);
		rightBtn.setText(R.string.cancel);
		setLeftPlayLowRateInfo(info);
		
		
		new Thread(){
			public void run() {
				download  = DownloadResourceManager.getInstance().getDLstaus(film.getFilmID());
				String localPlay=PropertiesUtil.getProperty(context, "local_play");
				if(Boolean.parseBoolean(localPlay)){
					///////////////测试用
				download = new FilmDownLoad4k();
				download.downType = DownType.FINISH;
					///////////////测试用
					if (download!=null) {
						Message msg = new Message();
						msg.what = GET_DOWN_SUCCESS;
						msg.obj = download;
						handler.sendMessage(msg);
					}else{
						handler.sendEmptyMessage(GET_DOWN_FAIL);
					}
				}else{
					if (download!=null) {
						Message msg = new Message();
						msg.what = GET_DOWN_SUCCESS;
						msg.obj = download;
						handler.sendMessage(msg);
					}else{
						handler.sendEmptyMessage(GET_DOWN_FAIL);
					}
					if (download!=null) {
						download = DownloadResourceManager.getInstance().getDLstaus(download);
						Message msg = new Message();
						msg.what = GET_DOWN_SUCCESS;
						msg.obj = download;
						handler.sendMessage(msg);
					}
				}
				
				
				Detail detail = MovieManager.GetInstance().getDetailFromSrcUrl(film.getSourceUrl());
				if (detail!=null) {
					Message msg = new Message();
					msg.what = GET_DETAIL_SUCCESS;
					msg.obj = detail;
					handler.sendMessage(msg);
				}else{
					Log.d("get detail fail "+film.getFilmName());
				}
				
				
			};
		}.start();
		
		
		
	}

	private void setLeftPlayLowRateInfo(FilmAndPageInfo info) {
		if (info instanceof F4kFilmAndPageInfo) {
			leftBtn_playlow.setText(R.string.play_online_1080p);
			leftBtn_playlow.setVisibility(View.GONE);
			text_dl_info.setText(R.string.watch_worning_for4k_band);
		}else{
			text_dl_info.setText(R.string.watch_worning_for4k);
			leftBtn_playlow.setVisibility(View.GONE);
		}
	}
	
	public void setProductsData(List<Product> products) {
		if (products != null && products.size() > 0) {
			String costFee = products.get(0).getCostFee();
			String fee = products.get(0).getFee();
			if (!"".equals(costFee)) {
				price.setText("￥" + Integer.parseInt(costFee) / 100);
			} else {
				price.setText("￥" + "0");
			}
			if (!"".equals(fee)) {
				barginPrice.setText("￥" + Integer.parseInt(fee) / 100);
			} else {
				barginPrice.setText("￥" + "0");
			}
		}
	}

	public void setButtonStatus(FilmDownLoad4k film){
		if (film==null) {
			return;
		}
		switch (film.downType) {
		case UNDOWN:
			leftBtn.setText(R.string.watch_online);
			setLeftPlayLowRateInfo(info);
			middleBtn.setVisibility(View.VISIBLE);
			middleBtn.setText(R.string.download_at_once);
			rightBtn.setText(R.string.cancel);
			break;
		case WAITING:
		case PAUSE:
			leftBtn.setText(R.string.watch_online);
			setLeftPlayLowRateInfo(info);
			middleBtn.setText(R.string.download_manager);
			middleBtn.setVisibility(View.VISIBLE);
			rightBtn.setText(R.string.cancel);
			text_dl_info.setText(R.string.pause_go_manager_see_detail);
			break;
		case FINISH:
			leftBtn.setText(R.string.play_at_once);
//			middleBtn.setText("下载管理");
			middleBtn.setVisibility(View.GONE);
			leftBtn_playlow.setVisibility(View.GONE);
			rightBtn.setText(R.string.cancel);
			text_dl_info.setText(R.string.down_play_at_once);
			break;
		case DOWNLOADING:
			leftBtn.setText(R.string.watch_online);
			setLeftPlayLowRateInfo(info);
			middleBtn.setText(R.string.download_manager);
			middleBtn.setVisibility(View.VISIBLE);
			rightBtn.setText(R.string.cancel);
			text_dl_info.setText(getContext().getString(R.string.film_has_down)+film.getDownPercent()+ getContext().getString(R.string.go_manager_see_detail));
			break;
		default:
			break;
		}
		if (DownloadResourceManager.getInstance().getDownLoadFlag()==null||!DownloadResourceManager.getInstance().getDownLoadFlag().equals("1")) {
			  middleBtn.setVisibility(View.INVISIBLE);
		}
		
		
	}
	

	
	public interface DetailButtonListener {
		/**
		 * 在线播放
		 * 
		 * @param film
		 */
		public void onPlay(Film film);

		/**
		 * 下载/下载管理
		 */
		public void onDownload(Film film);

		/**
		 * 取消
		 * @param film 
		 */
		public void onCancel(Film film);
	}

	

}
