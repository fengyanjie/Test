package com.voole.epg.download.base;

import android.app.Dialog;
import android.content.Context;

public class BaseDialog extends Dialog{
	protected Context mContext = null;

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	protected BaseDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public BaseDialog(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		this.mContext = context;
	}

}
