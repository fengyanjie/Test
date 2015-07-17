package com.voole.epg.download.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.voole.epg.download.DisplayManager;
import com.voole.epg.download.base.BaseLinearLayout;
import com.voole.epg.f4k.R;

public class ExitDialogContinueView extends BaseLinearLayout{

	private Context context;
	private OnClickListener okListener;
	private OnClickListener cancellistener;
	private OnKeyListener okKeyLister;
	private OnKeyListener cancelKeyLister;
	
	public void setContext(Context context) {
		this.context = context;
	}
	public void setOkClickListener(OnClickListener listener) {
		this.okListener = listener;
	}
	public void setCancelClicklistener(OnClickListener listener) {
		this.cancellistener = listener;
	}
	public void setOkKeyLister(OnKeyListener keyLister) {
		this.okKeyLister = keyLister;
	}
	public void setCancelKeyLister(OnKeyListener keyLister) {
		this.cancelKeyLister = keyLister;
	}
	
	public ExitDialogContinueView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ExitDialogContinueView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
		this.setGravity(Gravity.CENTER);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundResource(R.drawable.bg_exitview);

		LinearLayout managerTopLayout = new LinearLayout(context);
		managerTopLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams paramtop = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,0,3);
		managerTopLayout.setLayoutParams(paramtop);
		
		LinearLayout managertvLayout = new LinearLayout(context);
		managertvLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams paramtv = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,0,9);
		managertvLayout.setLayoutParams(paramtv);
		managertvLayout.setPadding(10, 0, 10, 0);
//		managertvLayout.setGravity(Gravity.CENTER);
		
		TextView tvinfo = new TextView(context);
		tvinfo.setText(R.string.exit_isdown_film_info);
		LinearLayout.LayoutParams tvInfoParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		tvinfo.setTextColor(Color.WHITE);
		tvinfo.setGravity(Gravity.CENTER_HORIZONTAL);
		tvinfo.setTextSize(DisplayManager.TEXTSIZE -4);
		tvinfo.setLayoutParams(tvInfoParams);
		managertvLayout.addView(tvinfo);
		
		TextView tvmsg = new TextView(context);
		tvmsg.setText(R.string.exit_isdown_film_msg);
		LinearLayout.LayoutParams tvMsgParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		tvMsgParams.gravity = Gravity.CENTER_HORIZONTAL;
		tvmsg.setTextColor(Color.WHITE);
		tvmsg.setGravity(Gravity.CENTER_HORIZONTAL);
		tvmsg.setTextSize(DisplayManager.TEXTSIZE - 4);
		tvmsg.setLayoutParams(tvMsgParams);
		managertvLayout.addView(tvmsg);
		
		LinearLayout managerCenterLayout = new LinearLayout(context);
		managerCenterLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams paramcenter = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,0,3);
		managerCenterLayout.setLayoutParams(paramcenter);
		
		LinearLayout managerBtnLayout = new LinearLayout(context);
		managerBtnLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams parambtn = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 5);
		managerBtnLayout.setLayoutParams(parambtn);
		managerBtnLayout.setPadding(60, 0, 60, 0);
		
		LinearLayout okBtnLayout = new LinearLayout(context);
		LinearLayout.LayoutParams paramokBtn = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 7);
		okBtnLayout.setLayoutParams(paramokBtn);
		okBtnLayout.setGravity(Gravity.CENTER);
		
		Button okButton = new FocusAbleButton(context);
		okButton.setText(context.getString(R.string.exit_continue_down_ok));
		LayoutParams okBtnParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		okButton.setTextColor(Color.WHITE);
		okButton.setGravity(Gravity.CENTER);
		okButton.setTextSize(DisplayManager.TEXTSIZE - 3);
		okButton.setLayoutParams(okBtnParam);
		okBtnLayout.addView(okButton);
		managerBtnLayout.addView(okBtnLayout);
		
		LinearLayout centerBtnLayout = new LinearLayout(context);
		LinearLayout.LayoutParams paramcenterBtn = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 5);
		centerBtnLayout.setLayoutParams(paramcenterBtn);
		
		View centerbtnView = new View(context);
		LayoutParams centerBtnParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		centerbtnView.setLayoutParams(centerBtnParam);
		centerBtnLayout.addView(centerbtnView);
		managerBtnLayout.addView(centerBtnLayout);
		
		LinearLayout cancelBtnLayout = new LinearLayout(context);
		LinearLayout.LayoutParams paramcancelBtn = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 7);
		cancelBtnLayout.setLayoutParams(paramcancelBtn);
		cancelBtnLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		Button cancelButton = new FocusAbleButton(context);
		cancelButton.setText(context.getString(R.string.exit_continue_down_cancel));
		LayoutParams cancelBtnParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		cancelButton.setTextColor(Color.WHITE);
		cancelButton.setGravity(Gravity.CENTER);
		cancelButton.setTextSize(DisplayManager.TEXTSIZE -3);
		cancelButton.setLayoutParams(cancelBtnParams);
		cancelBtnLayout.addView(cancelButton);
		managerBtnLayout.addView(cancelBtnLayout);
		
		LinearLayout managerBottomLayout = new LinearLayout(context);
		managerBottomLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams parambottom = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,0,5);
		managerBottomLayout.setLayoutParams(parambottom);
		
		LinearLayout.LayoutParams paramView = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, DisplayManager.TEXTSIZE+4);
		paramView.gravity = Gravity.CENTER;
		View view  = new View(context);
		view.setBackgroundColor(Color.TRANSPARENT);
		view.setLayoutParams(paramView);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (okListener!=null) {
					okListener.onClick(v);
				}
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cancellistener!=null) {
					cancellistener.onClick(v);
				}
			}
		});
		this.addView(view);
		this.addView(managerTopLayout);
		this.addView(managertvLayout);
		this.addView(managerCenterLayout);
		this.addView(managerBtnLayout);
		this.addView(managerBottomLayout);
	}

}
