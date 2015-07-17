package com.voole.epg.download.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.voole.epg.corelib.model.movies.Film;
import com.voole.epg.download.DisplayManager;
import com.voole.epg.download.base.BaseLinearLayout;
import com.voole.epg.f4k.R;
import com.voole.tvutils.ImageManager;
import com.voole.tvutils.ImageManager.ImageListener;
import com.voole.tvutils.ImageUtil;

public class MovieItemView extends BaseLinearLayout{
	private static final int UNFOCUS_MARGIN = DisplayManager.TEXTSIZE-10;
	private static final int UNFOCUS_PADDING = UNFOCUS_MARGIN/2;
	private AlwaysMarqueeTextView textView = null;
	private PosterView posterView = null;
	private Film data = null;
	private boolean isFocus = false;
	private boolean isEditable = false;
	private boolean isSelected = false;
	private boolean isShowPurgeTV = false;
	private int textSize=DisplayManager.TEXTSIZE+4;
	private int imgWeight=16;
	private int textWeight=84;
	

	public MovieItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MovieItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MovieItemView(Context context) {
		super(context);
		init(context);
	}
	
	public MovieItemView(Context context,int textSize,int imgWeight,int textWeight) {
		super(context);
		this.textSize = textSize;
		this.imgWeight = imgWeight;
		this.textWeight = textWeight;
		init(context);
	}
	
	
	
	private void init(Context context){
		setOrientation(VERTICAL);
		initPoster(context);
		initText(context);
	}
	
	private void initPoster(Context context){
		posterView = new PosterView(context);
		LinearLayout.LayoutParams param_poster = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, imgWeight);
		param_poster.setMargins(UNFOCUS_MARGIN, UNFOCUS_MARGIN, UNFOCUS_MARGIN, UNFOCUS_MARGIN);
		posterView.setLayoutParams(param_poster);
		posterView.setImageResource(R.drawable.default_img);
		addView(posterView);
	}
	
	private void initText(Context context){
		textView = new AlwaysMarqueeTextView(context);
		LinearLayout.LayoutParams param_text = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, textWeight);
		param_text.setMargins(UNFOCUS_MARGIN, 0, UNFOCUS_MARGIN, UNFOCUS_MARGIN);
		textView.setLayoutParams(param_text);
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundColor(Color.TRANSPARENT);
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(textSize);
		textView.setMarquee(false);
		addView(textView);
	}
	
	public void setData(Film film){
		this.data = film;
		ImageManager.GetInstance().displayImage(this.data.getImgUrl(), posterView);
//		ImageManager.GetInstance().displayImage(this.data.getImgUrl(), posterView, new ImageListener() {
//			@Override
//			public void onLoadingStarted(String uri, View view) {
//				posterView.setImageResource(R.drawable.default_img);
//			}
//			@Override
//			public void onLoadingFailed(String uri, View view) {
//				
//			}
//			@Override
//			public void onLoadingComplete(String uri, View view, Bitmap bitmap) {
//				if(bitmap==null){
//					posterView.setImageResource(R.drawable.default_img);
//				} else{
//					posterView.setImageBitmap(bitmap);
//				}
//			}
//		});
		textView.setText(this.data.getFilmName());
	}
	
	public void setShowPursuerTV(boolean isShowPursuerTV){
		isShowPurgeTV = isShowPursuerTV;
	}
	
	public void setEditable(boolean isEditable){
		this.isEditable = isEditable;
		updateUI();
	}
	
	public void setSelectedItem(boolean isSelected){
		this.isSelected = isSelected;
		updateUI();
	}
	
	public boolean isSelectedItem(){
		return isSelected;
	}
	
	public void setTextSize(int textSize){
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	}
	
	public void setFocusedItem(boolean isFocus){
		this.isFocus = isFocus;
		updateUI();
	}
	
	private void updateUI(){
		if(isEditable){
		}else{
			if(isFocus){
				setBackgroundResource(R.drawable.cs_movie_item_seleted_bg);
				textView.setMarquee(true);
				posterView.setPadding(0, 0, 0, 0);
			}else{
				setBackgroundResource(0);
				textView.setMarquee(false);
//				posterView.setPadding(0, 0, UNFOCUS_PADDING * 2, UNFOCUS_PADDING * 2);
			}
		}
		posterView.invalidate();
	}
	
	
	
	public class PosterView extends ImageView{
		private Paint paint = new Paint();
		private Bitmap priceBg = null;
		private Bitmap editSelected = null;
		private Bitmap editUnselected = null;
		private NinePatch ninePatchBg_selected = null;
		private Rect rect;
		public PosterView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initPosterView(context);
		}

		public PosterView(Context context, AttributeSet attrs) {
			super(context, attrs);
			initPosterView(context);
		}

		public PosterView(Context context) {
			super(context);
			initPosterView(context);
		}
		
		private void initPosterView(Context context){
			setScaleType(ScaleType.FIT_XY);
			setBackgroundResource(R.drawable.cs_movie_item_unseleted_bg);
//			setPadding(0, 0, UNFOCUS_PADDING * 2, UNFOCUS_PADDING * 2);
			setImageResource(R.drawable.default_img);
		    paint.setStrokeWidth(0);  
		    paint.setStyle(Paint.Style.STROKE);  
			paint.setColor(Color.WHITE);
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(24);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (rect==null) {
				rect = canvas.getClipBounds(); 
			}
			if(isShowPurgeTV && data != null && data.getEpisodCount() != null){
				if(Integer.parseInt(data.getEpisodCount())>1){
					Rect imgRect = new Rect();
					if(isFocus){
						imgRect.left = rect.left;
						imgRect.top = rect.bottom - rect.height() / 5;
						imgRect.right = rect.right;
						imgRect.bottom = rect.bottom;
					}else{
						imgRect.left = rect.left + UNFOCUS_PADDING;
						imgRect.top = rect.bottom - rect.height() / 5;
						imgRect.right = rect.right - UNFOCUS_PADDING * 2;
						imgRect.bottom = rect.bottom - UNFOCUS_PADDING * 2;
					}
					paint.setColor(Color.BLACK);
					paint.setStyle(Paint.Style.FILL);
					canvas.drawRect(imgRect, paint);
					paint.setTextAlign(Align.CENTER);
					paint.setTextSize(22);
					paint.setStyle(Paint.Style.STROKE);  
					paint.setColor(Color.WHITE);
					FontMetrics fontMetrics = paint.getFontMetrics(); 
		        	float fontHeight = fontMetrics.bottom - fontMetrics.top; 
		        	float textBaseY = imgRect.bottom - (imgRect.height() - fontHeight) / 2 - fontMetrics.bottom; 
		        	if(data.getEpisodCount().equals( data.getCurrentEpisodeCount() )){
						canvas.drawText("更新完毕" , imgRect.width() / 2, textBaseY, paint);
		        	}else{
						canvas.drawText("更新至"+data.getCurrentEpisodeCount()+"集" , imgRect.width() / 2, textBaseY, paint);
		        	}
				}
			}
			
			if (data != null && data.getMoviePrice() != null) {
				int moviePrice = Integer.parseInt(data.getMoviePrice()) / 100;
				if (moviePrice >0 ) {
					if(priceBg == null){
						priceBg = ImageUtil.getResourceBitmap(mContext, R.drawable.cs_movie_item_pricetag_bg);
					}
					Rect imgRect = new Rect();
					imgRect.left = rect.left;
					imgRect.top = rect.bottom - rect.height() / 5-10;
					imgRect.right = rect.right *43/100;
					imgRect.bottom = rect.bottom-20;
		        	canvas.drawBitmap(priceBg, null, imgRect, null);
		        	FontMetrics fontMetrics = paint.getFontMetrics(); 
		        	float fontHeight = fontMetrics.bottom - fontMetrics.top; 
		        	float textBaseY = imgRect.bottom - (imgRect.height() - fontHeight) / 2 - fontMetrics.bottom-4; 
					canvas.drawText("￥" + moviePrice , imgRect.width() / 2-4, textBaseY, paint);
				}
			}
		}
		
	}
	
}
