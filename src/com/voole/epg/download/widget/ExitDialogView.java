package com.voole.epg.download.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.voole.epg.download.DisplayManager;
import com.voole.epg.download.base.BaseLinearLayout;
import com.voole.epg.f4k.R;

public class ExitDialogView extends BaseLinearLayout{

	private Context context;
	private OnClickListener bListener;
	private OnClickListener tListener;
	private OnKeyListener tKeyLister;
	private OnKeyListener bKeyLister;
	public void setTKeyListener(OnKeyListener keyLister){
		this.tKeyLister = keyLister;
	}
	public void setBKeyListener(OnKeyListener keyLister){
		this.bKeyLister = keyLister;
	}
	public ExitDialogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ExitDialogView(Context context) {
		super(context);
		initView(context);
	}
	
	public void setTClickListener(OnClickListener listener){
		this.tListener = listener;
	}
	public void setBClickListener(OnClickListener listener){
		this.bListener = listener;
	}

	private void initView(Context context) {
		this.context = context;
		this.setGravity(Gravity.CENTER);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundResource(R.drawable.bg_exitview);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.gravity = Gravity.CENTER;
		Button tButton = new FocusAbleButton(context);
		tButton.setText("\r\r\r\r\r\r\r\r\r"+context.getString(R.string.exit_watch)+"\r\r\r\r\r\r\r\r\r");
		tButton.setTextColor(Color.WHITE);
		tButton.setTextSize(DisplayManager.TEXTSIZE);
		Button bButton = new FocusAbleButton(context);
		bButton.setText(R.string.watch_others);
		bButton.setTextColor(Color.WHITE);
		bButton.setTextSize(DisplayManager.TEXTSIZE);
		LinearLayout.LayoutParams paramView = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, DisplayManager.TEXTSIZE+4);
		paramView.gravity = Gravity.CENTER;
		View view  = new View(context);
		view.setBackgroundColor(Color.TRANSPARENT);
		view.setLayoutParams(paramView);
		tButton.setLayoutParams(param);
		bButton.setLayoutParams(param);
		tButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tListener!=null) {
					tListener.onClick(v);
				}
			}
		});
		bButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bListener!=null) {
					bListener.onClick(v);
				}
			}
		});
		this.addView(tButton);
		this.addView(view);
		this.addView(bButton);
	}
	
	

}
