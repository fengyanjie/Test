package com.voole.epg.download.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.voole.epg.f4k.R;

public class FocusAbleButton extends Button{

	private Context context;
	public FocusAbleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public FocusAbleButton(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	
	
	
	public FocusAbleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	private void init() {
		this.setBackgroundDrawable(getButtonGg());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	private StateListDrawable getButtonGg() {
		return addStateDrawable(context,
				R.drawable.bg_button_4k_dl_manager_normal,
				R.drawable.bg_button_4k_dl_manager_focus,
				R.drawable.bg_button_4k_dl_manager_focus);
	}

	private StateListDrawable addStateDrawable(Context context, int idNormal,
			int idPressed, int idFocused) {
		StateListDrawable sd = new StateListDrawable();
		Drawable normal = idNormal == -1 ? null : context.getResources()
				.getDrawable(idNormal);
		Drawable pressed = idPressed == -1 ? null : context.getResources()
				.getDrawable(idPressed);
		Drawable focus = idFocused == -1 ? null : context.getResources()
				.getDrawable(idFocused);
		// 注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
		// 所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
		sd.addState(new int[] { android.R.attr.state_enabled,
				android.R.attr.state_focused }, focus);
		sd.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressed);
		sd.addState(new int[] { android.R.attr.state_focused }, focus);
		sd.addState(new int[] { android.R.attr.state_pressed }, pressed);
		sd.addState(new int[] { android.R.attr.state_enabled }, normal);
		sd.addState(new int[] {}, normal);
		return sd;
	}
	

}
