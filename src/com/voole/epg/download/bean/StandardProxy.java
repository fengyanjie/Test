package com.voole.epg.download.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.voole.epg.corelib.model.proxy.IProxy;
import com.voole.epg.corelib.model.utils.LogUtil;
import com.voole.tvutils.NetUtil;
import com.vooleglib.VooleGLib;

public class StandardProxy implements IProxy{
	
	private int pid = 0;
	private Context context;
	public StandardProxy(Context context){
		this.context = context;
	}
	@Override
	public String getProxyServer() {
		final String url = "http://127.0.0.1:" + DownloadResourceManager.getInstance().getProxyPort();
		return url; 
	}
	
	private String getDir(){
		return context.getFilesDir().getAbsolutePath();
	}
	
	@Override
	public void startProxy() {
		String modulepath = getDir() + "/" + DownloadResourceManager.getInstance().getPorxyName();
		if (!(new File(modulepath)).exists()) {
			LogUtil.d("StandardProxy-->startProxy-->copy proxy file");
			try {
				InputStream is = context.getAssets().open(DownloadResourceManager.getInstance().getPorxyName());
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
		
		 if(!isProxyRunning()){
			exitProxy();
			
			int connectTimes = 30;
			for(int i = 0; i < connectTimes; i++){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				VooleGLib.execute(modulepath);
				if(!isProxyRunning()){
					break;
				}
			}
			
		} 
		
		 /*if(!isProxyRunning()){
			exitProxy();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			VooleGLib.execute(modulepath);
			isProxyRunning();
		}*/
		
	}

	/*@Override
	public void startProxy() {
		String modulepath = getDir() + "/" + PROXY_FILE_NAME;
		if (!(new File(modulepath)).exists()) {
			LogUtil.d("StandardProxy-->startProxy-->copy proxy file");
			try {
				InputStream is = LauncherApplication.GetInstance().getApplicationContext().getAssets().open(Config.PROXY_FILE);
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

			LogUtil.d("StandardProxy-->startProxy-->chmod");
			String cmd = "chmod 777 " + modulepath;
			ProcessUtil pu = new ProcessUtil();
			pu.execCmd(cmd);
		}
		
		exitProxy();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		LogUtil.d("StandardProxy-->startProxy-->start cmd");
		ProcessUtil pu1 = new ProcessUtil();
		pu1.execCmd(modulepath);
	}

	@Override
	public void exitProxy() {
		LogUtil.d("ProxyManager--->exitProxy");
		String str_pid = getProxyPid();
		if(str_pid != null && !str_pid.equals("")){
			String cmd = "kill -9 " + str_pid;
			ProcessUtil pu = new ProcessUtil();
			pu.execCmd(cmd);
		}
	}
	
	private int getAgentPid() {
		try {
			StringBuffer buffer = new StringBuffer();
			String httpMessage = "";
			if (NetUtil.connectServer(getProxyServer() + "/getpid", buffer)) {
				httpMessage = buffer.toString().trim();
				if(httpMessage != null){
					int id = Integer.parseInt(httpMessage.trim());
					return id;
				}
			}
		} catch (Exception e) {
			return -1;
		}
		return 0;
	}
	
	private String getProxyPid(){
		String modulepath = LauncherApplication.GetInstance().getFilePath() + "/" + PROXY_FILE_NAME;
		String pid = DeviceUtil.findPidOfAgent(modulepath);
		LogUtil.d("ProxyManager--->getProxyPid-->" + pid);
		return pid;
	}

	@Override
	public boolean isProxyRunning() {
		LogUtil.d("ProxyManager--->isProxyRunning");
		int pid = getAgentPid();
		if(pid > 0){
			return true;
		}else{
			return false;
		}
	}*/
	
	@Override
	public void exitProxy() {
		LogUtil.d("ProxyManager--->exitProxy");
//		VooleGLib.killExe(PROXY_FILE_NAME);
		/* if(pid > 0){
			android.os.Process.killProcess(pid);
			pid = -1;
		} */
		NetUtil.connectServer("http://127.0.0.1:5655/exit", new StringBuffer(), 1, 3);
	}
	
	private int getAgentPid() {
		try {
			StringBuffer buffer = new StringBuffer();
			String httpMessage = "";
			if (NetUtil.connectServer(getProxyServer() + "/getpid", buffer, 1, 6)) {
				httpMessage = buffer.toString().trim();
				if(httpMessage != null){
					int id = Integer.parseInt(httpMessage.trim());
					return id;
				}
			}
		} catch (Exception e) {
			return -1;
		}
		return 0;
	}

	@Override
	public boolean isProxyRunning() {
		pid = getAgentPid();
		if(pid<=0){
			return false;
		}else{
			return true;
		}
		/*int status = VooleGLib.isExeRunning(PROXY_FILE_NAME);
		if(status < 0){
			LogUtil.d("ProxyManager--->isProxyRunning--->true");
			return true;
		}else{
			LogUtil.d("ProxyManager--->isProxyRunning--->false");
			return false;
		}*/
	}

	@Override
	public void deleteProxyFiles() {
		String modulepath = getDir() + "/" + DownloadResourceManager.getInstance().getPorxyName();
		File f = new File(modulepath);
		if (f.exists()) {
			f.delete();
		}
	}

	@Override
	public void finishPlay() {
		LogUtil.d("ProxyManager--->finishPlay-->start");
		String url = getProxyServer() + "/finish";
		NetUtil.connectServer(url, new StringBuffer(), 1, 4);
		LogUtil.d("ProxyManager--->finishPlay-->end");
		
	}
	
}
