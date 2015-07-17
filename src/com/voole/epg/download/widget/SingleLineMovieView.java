package com.voole.epg.download.widget;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.voole.epg.corelib.model.movies.Film;
import com.voole.epg.download.base.BaseRelativeLayout;
import com.voole.epg.download.base.ID;
import com.voole.epg.f4k.R;
import com.voole.tvutils.ImageUtil;

public class SingleLineMovieView extends BaseRelativeLayout{
//	public static final int ITEM_SIZE = 10;
	
	private int ITEM_SIZE = 10;
	public int getITEM_SIZE() {
		return ITEM_SIZE;
	}

	public void setITEM_SIZE(int iTEM_SIZE) {
		ITEM_SIZE = iTEM_SIZE;
	}

	private MovieItemView[] itemViews = null;
	private List<Film> films = null;
	
	private int selectedItemIndex = 0;
	private int currentPageNo = -1;
	private int totalPageSize = -1;
	
	private boolean isDisplayArrow = true;
	private int textSize=0;
	private int imgWeight=0;
	private int textWeight=0;
	
	private ImageView leftArrow = null;
	private ImageView rightArrow = null;
	
	private MovieViewListener movieViewListener = null;
	private MovieViewFocusListener movieViewFocusListener = null;
	
	public void setMovieViewListener(MovieViewListener l){
		movieViewListener = l;
	}
	public void setMovieViewFocusListener(MovieViewFocusListener l){
		movieViewFocusListener = l;
	}
	
	public SingleLineMovieView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MovieView4k);
		int itemSize = a.getInt(R.styleable.MovieView4k_itemSize4k, ITEM_SIZE);
		ITEM_SIZE = itemSize;
		isDisplayArrow = a.getBoolean(R.styleable.MovieView4k_displayArrow4k, true);
		textSize= a.getInt(R.styleable.MovieView4k_textSize4k, com.voole.epg.download.DisplayManager.TEXTSIZE-4);
		imgWeight= a.getInt(R.styleable.MovieView4k_imgWeight4k, 0);
		textWeight= a.getInt(R.styleable.MovieView4k_textWeight4k, 0);
		a.recycle();
		init(context);
	}

	public SingleLineMovieView(Context context) {
		super(context);
		init(context);
	}

	public SingleLineMovieView(Context context, int itemSize) {
		super(context);
		this.ITEM_SIZE = itemSize;
		init(context);
	}
	
	public SingleLineMovieView(Context context, int itemSize, int imgWeight, int textWeight,int textSize) {
		super(context);
		this.ITEM_SIZE = itemSize;
		this.imgWeight = imgWeight;
		this.textWeight = textWeight;
		this.textSize = textSize;
		init(context);
	}
	
	private void init(Context context){
		setFocusable(true);
		setFocusableInTouchMode(true);
		setClickable(true);
		itemViews = new MovieItemView[ITEM_SIZE];
		initMovies(context);
	}
	
	private void initMovies(Context context){
		// left arrow
		leftArrow = new ImageView(context);
		leftArrow.setId(ID.SingleLineMovieView.LEFT_ARROW_ID);
		RelativeLayout.LayoutParams param_arrow_left = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param_arrow_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		param_arrow_left.addRule(RelativeLayout.CENTER_VERTICAL);
		param_arrow_left.leftMargin = 20;
		leftArrow.setLayoutParams(param_arrow_left);
		leftArrow.setImageBitmap(ImageUtil.getResourceBitmap(context, R.drawable.cs_movie_left_arrow));
		leftArrow.setScaleType(ScaleType.FIT_XY);
		addView(leftArrow);
		if(!isDisplayArrow){
			leftArrow.setVisibility(View.INVISIBLE);
		}
		
		// movie layout
		LinearLayout layout_middle = new LinearLayout(context);
		RelativeLayout.LayoutParams layout_middle_param = new RelativeLayout.LayoutParams
		(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout_middle_param.addRule(RelativeLayout.RIGHT_OF, ID.SingleLineMovieView.LEFT_ARROW_ID);
		layout_middle_param.addRule(RelativeLayout.LEFT_OF, ID.SingleLineMovieView.RIGHT_ARROW_ID);
		layout_middle.setLayoutParams(layout_middle_param);
		
		LinearLayout.LayoutParams layout_middle_item_param = new LinearLayout.LayoutParams
		(0, LayoutParams.MATCH_PARENT, 1);
		layout_middle_item_param.setMargins(0, 0, 0, 0);
		for(int i = 0; i < ITEM_SIZE; i++){
			itemViews[i] = new MovieItemView(context,textSize,imgWeight,textWeight);
			itemViews[i].setLayoutParams(layout_middle_item_param);
			layout_middle.addView(itemViews[i]);
		}
		addView(layout_middle);
		
		//right arrow
		rightArrow = new ImageView(context);
		rightArrow.setId(ID.SingleLineMovieView.RIGHT_ARROW_ID);
		RelativeLayout.LayoutParams param_arrow_right = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param_arrow_right.addRule(RelativeLayout.CENTER_VERTICAL);
		param_arrow_right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		param_arrow_right.rightMargin = 45;
		rightArrow.setLayoutParams(param_arrow_right);
		rightArrow.setImageBitmap(ImageUtil.getResourceBitmap(context, R.drawable.cs_movie_right_arrow));
		rightArrow.setScaleType(ScaleType.FIT_XY);
		addView(rightArrow);
		if(!isDisplayArrow){
			rightArrow.setVisibility(View.INVISIBLE);
		}
	}
	
	
	public void setData(List<Film> films){
		this.films = films;
		int endIndex = ITEM_SIZE;
		if(films.size() < ITEM_SIZE){
			endIndex = films.size();
		}
		for(int i = 0; i < endIndex; i++){
			itemViews[i].setData(films.get(i));
			itemViews[i].setVisibility(View.VISIBLE);
		}
		
		for(int i=endIndex; i<ITEM_SIZE; i++){
			itemViews[i].setVisibility(View.INVISIBLE);
		}
		if(this.isFocused()){
			itemViews[selectedItemIndex].setFocusedItem(false);
		}
		selectedItemIndex = 0;
		if(this.isFocused()){
			itemViews[selectedItemIndex].setFocusedItem(true);
			if (movieViewFocusListener!=null) {
				movieViewFocusListener.onItemFocusChange(selectedItemIndex);
			}
		}
	}
	
	public void setPageInfo(int pageNo, int totalPageSize){
		this.currentPageNo = pageNo;
		this.totalPageSize = totalPageSize;
	}
	
	public void setDisplayArrow(boolean isShow){
		if (isShow) {
			leftArrow.setVisibility(VISIBLE);
			rightArrow.setVisibility(VISIBLE);
		}else {
			leftArrow.setVisibility(INVISIBLE);
			rightArrow.setVisibility(INVISIBLE);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if(selectedItemIndex > 0){
					itemViews[selectedItemIndex].setFocusedItem(false);
					itemViews[--selectedItemIndex].setFocusedItem(true);
					if (movieViewFocusListener!=null) {
						movieViewFocusListener.onItemFocusChange(selectedItemIndex);
					}
				}else{
					previousPage();
				}
				
				return true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if(films != null && selectedItemIndex < films.size() - 1 && selectedItemIndex+1 < ITEM_SIZE){
					itemViews[selectedItemIndex].setFocusedItem(false);
					itemViews[++selectedItemIndex].setFocusedItem(true);
					if (movieViewFocusListener!=null) {
						movieViewFocusListener.onItemFocusChange(selectedItemIndex);
					}
				}else{
					nextPage();
				}
				return true;
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				if(movieViewListener != null && films != null){
					movieViewListener.onItemSelected(films.get(selectedItemIndex),selectedItemIndex);
					return true;
				}
				break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	public void setFocusedItem(int selectIndex){
		if(selectIndex > 0){
			itemViews[selectedItemIndex].setFocusedItem(false);
			itemViews[selectIndex].setFocusedItem(true);
			if (movieViewFocusListener!=null) {
				movieViewFocusListener.onItemFocusChange(selectedItemIndex);
			}
			selectedItemIndex = selectIndex;
		}
	}
	private void nextPage(){
		if(currentPageNo < totalPageSize){
			currentPageNo ++;
			gotoPage(currentPageNo);
		}
	}
	
	private void previousPage(){
		if(currentPageNo > 1){
			currentPageNo --;
			gotoPage(currentPageNo);
		}
	}
	
	private void gotoPage(int pageNo) {
		if(this.movieViewListener != null){
			movieViewListener.onGotoPage(pageNo);
		}
	}
	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		if (films == null || films.size() <= 0) {
			return;
		}
		if(gainFocus){
			itemViews[selectedItemIndex].setFocusedItem(true);
		}else{
			itemViews[selectedItemIndex].setFocusedItem(false);
		}
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}
	
}
