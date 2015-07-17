package com.voole.epg.download.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.voole.epg.corelib.model.auth.IAuth;
import com.voole.epg.corelib.model.utils.LogUtil;
import com.voole.epg.download.utils.Config;
import com.vooleglib.VooleGLib;

public class StandardAuth implements IAuth{

	@Override
	public String getAuthServer() {
		final String url = "http://127.0.0.1:" + DownloadResourceManager.getInstance().getAuthPort();
		return url;
	}
	private Context context;
	public StandardAuth(Context context){
		this.context = context;
	}
	
	private String getDir(){
		return context.getFilesDir().getAbsolutePath();
	}
	
	@Override
	public void startAuth() {
		String moduleConfig = getDir() + "/" + Config.AUTH_CONF;
		if (!(new File(moduleConfig)).exists()) {
			LogUtil.d("StandardAuth-->startAuth-->copy config file");
			try {
				InputStream is = context.getAssets().open(Config.AUTH_CONF);
				byte[] buffer = new byte[1024 * 8];
				int count = 0;
				FileOutputStream fos = new FileOutputStream(moduleConfig);
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String rtConfig = getDir() + "/" + Config.AUTH_RT_CONF;
		if (!(new File(rtConfig)).exists()) {
			LogUtil.d("StandardAuth-->startAuth-->copy rtconfig file");
			try {
				String[] files = context.getAssets().list("");
				for (int i = 0; i < files.length; i++) {
					if (Config.AUTH_RT_CONF.equals(files[i])) {
						LogUtil.d("StandardAuth---->assets---filesList-->" +files[i] );
						InputStream is = context.getAssets().open(Config.AUTH_RT_CONF);
						byte[] buffer = new byte[1024 * 8];
						int count = 0;
						FileOutputStream fos = new FileOutputStream(rtConfig);
						while ((count = is.read(buffer)) > 0) {
							fos.write(buffer, 0, count);
						}
						fos.close();
						is.close();
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String modulepath = getDir() + "/" + DownloadResourceManager.getInstance().getAuthName();
		if (!(new File(modulepath)).exists()) {
			LogUtil.d("StandardAuth-->startAuth-->copy auth file");
			try {
				InputStream is = context.getAssets().open(DownloadResourceManager.getInstance().getAuthName());
				byte[] buffer = new byte[1024 * 8];
				int count = 0;
				FileOutputStream fos = new FileOutputStream(modulepath);
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(!isAuthRunning()){
			VooleGLib.execute(modulepath);
		}
	}

	/*@Override
	public void startAuth() {
		String moduleConfig = getDir() + "/" + AUTH_CONF_NAME;
		if (!(new File(moduleConfig)).exists()) {
			LogUtil.d("StandardAuth-->startAuth-->copy config file");
			try {
				InputStream is = LauncherApplication.GetInstance().getApplicationContext().getAssets().open(Config.AUTH_CONF);
				byte[] buffer = new byte[1024 * 8];
				int count = 0;
				FileOutputStream fos = new FileOutputStream(moduleConfig);
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String modulepath = getDir() + "/" + AUTH_FILE_NAME;
		if (!(new File(modulepath)).exists()) {
			LogUtil.d("StandardAuth-->startAuth-->copy auth file");
			try {
				InputStream is = LauncherApplication.GetInstance().getApplicationContext().getAssets().open(Config.AUTH_FILE);
				byte[] buffer = new byte[1024 * 8];
				int count = 0;
				FileOutputStream fos = new FileOutputStream(modulepath);
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			LogUtil.d("StandardAuth-->startAuth-->chmod");
			String cmd = "chmod 777 " + modulepath;
			ProcessUtil pu = new ProcessUtil();
			pu.execCmd(cmd);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		exitAuth();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		LogUtil.d("StandardAuth-->startAuth-->start cmd");
		ProcessUtil pu1 = new ProcessUtil();
		pu1.execCmd(modulepath);
	}*/
	
	@Override
	public void exitAuth() {
		LogUtil.d("AuthManager--->exitAuth");
		VooleGLib.killExe(DownloadResourceManager.getInstance().getAuthName());
	}

	/*@Override
	public void exitAuth() {
		LogUtil.d("AuthManager--->exitAuth");
		String str_pid = getAuthPid();
		if(str_pid != null && !str_pid.equals("")){
			String cmd = "kill -9 " + str_pid;
			ProcessUtil pu = new ProcessUtil();
			pu.execCmd(cmd);
		}
		LogUtil.d("StandardAuth-->exitAuth-->end");
	}
	
	private int getAgentPid() {
		try {
			URL url = new URL("http://127.0.0.1:" + Config.AUTH_PORT + "/getpid");
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			int len = conn.getContentLength();
			InputStream is = conn.getInputStream();
			byte[] data = new byte[len];
			int bytesRead = 0;
			int offset = 0;
			while (offset < len) {
				bytesRead = is.read(data, offset, data.length - offset);
				if (bytesRead == -1) {
					break;
				}
				offset += bytesRead;
			}
			is.close();
			if (offset != len) {
				return -1;
			}

			StringBuffer str = new StringBuffer("");
			for (int i = 0; i < len; i++) {
				str.append(data[i] - '0');
			}

			return Integer.parseInt(str.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private String getAuthPid(){
		String modulepath = getDir() + "/" + AUTH_FILE_NAME;
		String pid = DeviceUtil.findPidOfAgent(modulepath);
		LogUtil.d("AuthManager--->getAuthPid-->" + pid);
		return pid;
	}*/
	
	
	@Override
	public void deleteAuthFiles() {
		LogUtil.d("AuthManager--->deleteAuthFiles");
		String moduleConfig = getDir() + "/" + Config.AUTH_CONF;
		File config = new File(moduleConfig);
		if (config.exists()) {
			config.delete();
		}
		String modulepath = getDir() + "/" + DownloadResourceManager.getInstance().getAuthName();
		File auth = new File(modulepath);
		if (auth.exists()) {
			auth.delete();
		}
	}
	
	@Override
	public boolean isAuthRunning() {
		int status = VooleGLib.isExeRunning(DownloadResourceManager.getInstance().getAuthName());
		String modulepath = getDir() + "/" + DownloadResourceManager.getInstance().getAuthName();
		File auth = new File(modulepath);
		if(status < 0){
			LogUtil.d("AuthManager--->isAuthRunning--->true");
			if(!auth.exists()){
				exitAuth();
				return false;
			}else{
				return true;
			}
		}else{
			LogUtil.d("AuthManager--->isAuthRunning--->false");
			return false;
		}
	}

	/*@Override
	public boolean isAuthRunning() {
		LogUtil.d("AuthManager--->isAuthRunning");
		int pid = getAgentPid();
		if(pid > 0){
			return true;
		}else{
			return false;
		}
	}*/

}
