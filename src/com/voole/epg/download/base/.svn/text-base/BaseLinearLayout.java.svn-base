package com.voole.epg.download.base;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class BaseLinearLayout extends LinearLayout{
	protected Context mContext;

	public BaseLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public BaseLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BaseLinearLayout(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		mContext = context;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.e(this.getClass().getName(), "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.e(this.getClass().getName(), "onLayout");
		super.onLayout(changed, l, t, r, b);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Log.e(this.getClass().getName(), "onDraw");
		super.onDraw(canvas);
	}
}
