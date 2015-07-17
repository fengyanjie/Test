package com.voole.epg.download.base;

import android.app.Activity;

public class RegistManager {
	
	private RegistManager(){};
	private static RegistManager instance;
	private IRegister regist;
	public static RegistManager getInstance(){
		if (instance==null) {
			instance = new RegistManager();
		}
		return instance;
	}
	

	public void setRegistListener(IRegister regist){
		instance.regist = regist;
	}
	public IRegister getRegister(){
		return regist;
	}
	
	public interface IRegister {
		/** 判断是否注册*/
		public boolean hasRegist(Activity context);
	}
}
