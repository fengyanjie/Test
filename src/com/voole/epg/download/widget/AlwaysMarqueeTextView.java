package com.voole.epg.download.widget;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class AlwaysMarqueeTextView extends TextView {

	public AlwaysMarqueeTextView(Context context) {
		super(context);
		init(context);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		setSingleLine(true);
		setMarquee(false);
	}
	
	public void setMarquee(boolean isMarquee){
		if(isMarquee){
			setHorizontallyScrolling(true);
			setEllipsize(TruncateAt.MARQUEE);
			setMarqueeRepeatLimit(-1);
		}else{
			setEllipsize(TruncateAt.END);
		}
	}

	@Override
	public boolean isFocused() {
		return true;
	}

}
