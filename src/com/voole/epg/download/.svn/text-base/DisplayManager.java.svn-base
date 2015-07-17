package com.voole.epg.download;

import android.content.Context;

public class DisplayManager {

	private static  DisplayManager manager;
	private Context context;
	public static final int TEXTSIZE = 20;
	private static int screenWidth;
	private static int screenHeight;
	private DisplayManager(Context context){
		this.context = context;
	};
	
	public static DisplayManager getInstance(Context context){
		if (manager==null) {
			manager = new DisplayManager(context);
		}
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		screenHeight = context.getResources().getDisplayMetrics().heightPixels;
		
		return manager;
	}
	
	
	public int getScreenWidth(){
		return screenWidth;
	}
	
	public int getScreenHeight(){
		return screenHeight;
	}
	
}
