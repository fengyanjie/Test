package com.voole.epg.download.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voole.epg.download.DisplayManager;
import com.voole.epg.download.base.BaseLinearLayout;
import com.voole.epg.download.bean.DownloadResourceManager;
import com.voole.epg.download.bean.FilmDownLoad4k;
import com.voole.epg.f4k.R;

public class AdapterItemView extends BaseLinearLayout{

	private Context context;
	public static final int LEFTBUTTONSTART_ID = 0x11111;
	public static final int RIGHTBUTTONSTART_ID = 0x11112;
	public static final int PROGRESSVIEW_ID = 0x11113;
	private int textSize = DisplayManager.TEXTSIZE+2;
//	public AdaterItemView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		initView(context);
//	}
//
//	public AdaterItemView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		initView(context);
//	}
	private Button buttonleft;
	private Button buttonRight;
	private TextView progressSpeedTextView;
	private ProgressView progressTextView;
	private TextView filmNameTextView;
	private TextView sizeTextView;

	public AdapterItemView(Context context) {
		super(context);
		this.context = context;
	}
	public void setButtonClickListener(View.OnClickListener listener){
		if (listener!=null) {
			buttonleft.setOnClickListener(listener);
			buttonRight.setOnClickListener(listener);
		}
	}
//	public void setHeight(int height){
//		LayoutParams itemParams = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, height,1);
//		this.setOrientation(LinearLayout.HORIZONTAL);
//		this.setVerticalGravity(Gravity.CENTER_VERTICAL);
//		this.setLayoutParams(itemParams);
//	}
	
	private void initView(FilmDownLoad4k filmDownLoad4k) {
		if (filmDownLoad4k!=null) {
//			LinearLayout itemLayout = new LinearLayout(context);
			this.setOrientation(LinearLayout.HORIZONTAL);
			this.setVerticalGravity(Gravity.CENTER_VERTICAL);
			
			filmNameTextView = new TextView(context);
			filmNameTextView.setText(filmDownLoad4k.FilmName==null?"":filmDownLoad4k.FilmName);
			filmNameTextView.setGravity(Gravity.CENTER);
			filmNameTextView.setTextColor(Color.WHITE);
			filmNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			LayoutParams filmNameTextViewParams = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT, 3);
			filmNameTextView.setLayoutParams(filmNameTextViewParams);
			this.addView(filmNameTextView);
			
			progressTextView = new ProgressView(context);
			LayoutParams progressTextViewParams = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT, 4);
			progressTextView.setLayoutParams(progressTextViewParams);
			this.addView(progressTextView);
			progressTextView.setId(PROGRESSVIEW_ID);
			progressTextView.setMax(filmDownLoad4k.totalsize);
			progressTextView.setProgress(filmDownLoad4k.currentsize);
			
			
			progressSpeedTextView = new TextView(context);
			progressSpeedTextView.setText(DownloadResourceManager.formetFileSize(filmDownLoad4k.realspeed,"")+"/S");
			progressSpeedTextView.setGravity(Gravity.CENTER);
			progressSpeedTextView.setTextColor(Color.parseColor("#888888"));
			progressSpeedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			LayoutParams progressSpeedTextViewParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 2);
			progressSpeedTextView.setLayoutParams(progressSpeedTextViewParams);
			this.addView(progressSpeedTextView);
			
			sizeTextView = new TextView(context);
			sizeTextView.setText(DownloadResourceManager.formetFileSize(filmDownLoad4k.totalsize,""));
			sizeTextView.setGravity(Gravity.CENTER);
			sizeTextView.setTextColor(Color.parseColor("#888888"));
			sizeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			LayoutParams sizeTextViewParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
			sizeTextView.setLayoutParams(sizeTextViewParams);
			this.addView(sizeTextView);
			
			buttonleft = new FocusAbleButton(context);
			buttonleft.setId(LEFTBUTTONSTART_ID);
			buttonleft.setTag(filmDownLoad4k);
			switch (filmDownLoad4k.downType) {
			case DOWNLOADING:
				buttonleft.setText(R.string.pause);
				progressSpeedTextView.setVisibility(View.VISIBLE);
				break;
			case WAITING:
				buttonleft.setText(R.string.waiting);
				progressSpeedTextView.setVisibility(View.INVISIBLE);
				break;
			case PAUSE:
				buttonleft.setText(R.string.start);
				progressSpeedTextView.setVisibility(View.INVISIBLE);
				break;
			case FINISH:
				progressSpeedTextView.setVisibility(View.INVISIBLE);
				buttonleft.setText(R.string.play);
				break;
			default:
				break;
			}
			buttonleft.setTextColor(Color.WHITE);
			buttonleft.setGravity(Gravity.CENTER);
			int pading = 5;
			buttonleft.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			LayoutParams buttonleftViewParams = new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT, 1);
			buttonleft.setLayoutParams(buttonleftViewParams);
			this.addView(buttonleft);
			buttonleft.setPadding(pading, 0, pading, 0);
			
			
			buttonRight = new FocusAbleButton(context);
			buttonRight.setText(R.string.delete);
			buttonRight.setTag(filmDownLoad4k);
			buttonRight.setId(RIGHTBUTTONSTART_ID);
			buttonRight.setGravity(Gravity.CENTER);
			
			buttonRight.setTextColor(Color.WHITE);
			
			buttonRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			LayoutParams buttonRightViewParams = new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT, 1);
			buttonRight.setLayoutParams(buttonRightViewParams);
			this.addView(buttonRight);
			buttonRightViewParams.setMargins(5, 0, 30, 0);
			buttonRight.setPadding(pading, 0, pading, 0);
		}

	}
	
	
	public void notifyDataChange(){
		if (this.getTag() instanceof FilmDownLoad4k) {
			this.setVisibility(View.VISIBLE);
			FilmDownLoad4k filmDownLoad4k = (FilmDownLoad4k) this.getTag();
			if (progressSpeedTextView==null) {
				initView(filmDownLoad4k);
			}
			
			buttonleft.setTag(filmDownLoad4k);
			buttonRight.setTag(filmDownLoad4k);
			filmNameTextView.setText(filmDownLoad4k.FilmName);
			progressTextView.setMax(filmDownLoad4k.totalsize);
			progressTextView.setProgress(filmDownLoad4k.currentsize);
			sizeTextView.setText(DownloadResourceManager.formetFileSize(filmDownLoad4k.totalsize,""));
			System.out.println(filmDownLoad4k.currentsize+"    --current--");
			System.out.println(filmDownLoad4k.totalsize+"    ---total---");
			//System.out.println(filmDownLoad4k.currentsize/filmDownLoad4k.totalsize+"    ---progress / max---");
			progressSpeedTextView.setText(DownloadResourceManager.formetFileSize(filmDownLoad4k.realspeed,"")+"/S");
			switch (filmDownLoad4k.downType) {
			case DOWNLOADING:
				buttonleft.setText(R.string.pause); 
				progressSpeedTextView.setVisibility(View.VISIBLE);
				break;
			case WAITING:
				buttonleft.setText(R.string.waiting);
				progressSpeedTextView.setVisibility(View.INVISIBLE);
				break;
			case PAUSE:
				buttonleft.setText(R.string.start);
				progressSpeedTextView.setVisibility(View.INVISIBLE);
				break;
			case FINISH:
				buttonleft.setText(R.string.play);
				progressSpeedTextView.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
		}else{
			this.setVisibility(View.INVISIBLE);
		}
	}
	
}
