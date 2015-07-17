package com.voole.epg.download.base;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.voole.epg.download.DisplayManager;
import com.voole.epg.f4k.R;

public class TVAlertDialog extends BaseDialog {

	private static int  TEXT_SIZE=28;
	private Builder choiceUsbDialog;
	public TVAlertDialog(Context context) {
		super(context);
	}
	
	public TVAlertDialog(Context context, int theme) {
		super(context, theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels / 2.1f);
		layoutParams.height = (int) (mContext.getResources().getDisplayMetrics().heightPixels / 2.4);
		window.setAttributes(layoutParams);
	}

	public static class Builder{
		private Context context;
		private String title;
		private boolean flag = true;
		private String positiveButtonText;
		private String negativeButtonText;
		
		private DialogInterface.OnClickListener
		                        positiveButtonClickListener,
		                        negativeButtonClickListener;
		private int textSize = DisplayManager.TEXTSIZE+4;
		
		public Builder(Context context){
			this.context = context;
		}
		
		public Builder setTitle(String title){
			this.title = title;
			return this;
		}
		
		public Builder setTitle(int resId){
			this.title = (String)context.getText(resId);
			return this;
		}
		
		public Builder setCancelable(boolean flag){
			this.flag = flag;
			return this;
		}
		
		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener){
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}
		
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener){
			this.positiveButtonText = (String)context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}
		
		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener){
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}
		
		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener){
			this.negativeButtonText = (String)context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}
		
		public TVAlertDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final TVAlertDialog epgDialog = new TVAlertDialog(context,R.style.alertDialog);
			View layout = inflater.inflate(R.layout.f4k_alert_dialog, null);
			TextView title = (TextView)layout.findViewById(R.id.title);
			title.setText(this.title);
			title.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE);
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(this.positiveButtonText);
				((Button) layout.findViewById(R.id.positiveButton)).setTextColor(Color.WHITE);
				((Button) layout.findViewById(R.id.positiveButton)).setTextSize(DisplayManager.TEXTSIZE+4);
				((Button) layout.findViewById(R.id.positiveButton)).setPadding(textSize, textSize-10, textSize, textSize-10);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									epgDialog.dismiss();
									positiveButtonClickListener.onClick(
											epgDialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.bt1).setVisibility(
						View.GONE);
			}
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(this.negativeButtonText);
				((Button) layout.findViewById(R.id.negativeButton)).setTextColor(Color.WHITE);
				((Button) layout.findViewById(R.id.negativeButton)).setTextSize(DisplayManager.TEXTSIZE+4);
				((Button) layout.findViewById(R.id.negativeButton)).setPadding(textSize , textSize-10, textSize, textSize-10);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									epgDialog.dismiss();
									negativeButtonClickListener.onClick(
											epgDialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.bt2).setVisibility(
						View.GONE);
			}
			epgDialog.setContentView(layout);
			epgDialog.setCancelable(flag);
			return epgDialog;
		}
	}
	//选择U盘
	public void showInspectDialog(Context context) {
		choiceUsbDialog = new com.voole.epg.download.base.TVAlertDialog.Builder(
				context);
		choiceUsbDialog.setCancelable(false);
		choiceUsbDialog.setTitle(R.string.choice_disk);
		choiceUsbDialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		choiceUsbDialog.create().show();
	}

}
