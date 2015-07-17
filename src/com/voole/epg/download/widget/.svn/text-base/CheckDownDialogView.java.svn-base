package com.voole.epg.download.widget;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcl.os.storage.TStorageManager;
import com.voole.epg.download.base.BaseLinearLayout;
import com.voole.epg.download.bean.DownloadResourceManager;
import com.voole.epg.f4k.R;

public class CheckDownDialogView extends BaseLinearLayout {
	private Context context;
	private Map<String, String> labMap;
	public static final String DISKNAME = "磁盘";
	public CheckDownDialogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
//		initView();
	}

	public CheckDownDialogView(Context context) {
		super(context);
		this.context = context;
//		initView();
	}

	public CheckDownDialogView(Context context, View view) {
		super(context);
		this.context = context;
//		initView();
	}
	
	public void choiceUsbDialog(){
		AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		View view = View.inflate(context, R.layout.f4k_checkdown_dialog,
				null);
		
		Window window = dlg.getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = (int) (context.getResources().getDisplayMetrics().widthPixels / 2.1f);
		layoutParams.height = (int) (context.getResources().getDisplayMetrics().heightPixels / 2.4);
//		window.setAttributes(layoutParams);
//		Window dialogWindow = dlg.getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		lp.height = (int) (dialogWindow.getWindowManager()
//				.getDefaultDisplay().getHeight() * 0.39); // 高度设置为屏幕的0.4
//		lp.width = (int) (dialogWindow.getWindowManager()
//				.getDefaultDisplay().getHeight() * 0.8); // 宽度设置为屏幕的0.8
//		dialogWindow.setAttributes(lp);
		dlg.addContentView(view, layoutParams);
		initWidget(view, dlg);
		
	}

	private void initWidget(View view, final AlertDialog dlg) {
		List<TextView> labtextviews = new ArrayList<TextView>();
		List<ImageButton> imbtns = new ArrayList<ImageButton>();
		DetectionDisk();

		int countUsb = labMap.size();
		Set<Entry<String, String>> entrySet = labMap.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		Entry<String, String> next = null;
		LinearLayout linearImg = (LinearLayout) view.findViewById(R.id.linearLayout1);
		int linearCount2 = linearImg.getChildCount();
		for(int i=0; i<linearCount2; i++){
			ImageButton imgbtns = (ImageButton) linearImg.getChildAt(i);
			imbtns.add(imgbtns);
		}
		LinearLayout linearText = (LinearLayout) view.findViewById(R.id.linearLayout2);
		int countImg = linearText.getChildCount();
		for(int i=0; i<countImg; i++){
			TextView textViews = (TextView) linearText.getChildAt(i);
			labtextviews.add(textViews);
		}
		for (int i = 0; i < countUsb; i++) {
			ImageButton imageButton = imbtns.get(i);
			final TextView labTextView = labtextviews.get(i);
			imageButton.setVisibility(View.VISIBLE);
			labTextView.setVisibility(View.VISIBLE);
			next = iterator.next();
			labTextView.setText(next.getValue());
			imageButton.setTag(next.getKey());
			String dlPath = getDownPathname();
			if(dlPath.equalsIgnoreCase(next.getKey())){
				imageButton.requestFocus(); //获取焦点
			}
			//回调
			imageButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dlg.cancel();
					
					if(listener != null){
						listener.onClick(v);
					}
				}
			});
			}
		}

	public Map<String, String> DetectionDisk() {
		labMap = new IdentityHashMap<String, String>(){
			@Override
			public String get(Object key) {
				Set<java.util.Map.Entry<String, String>> entrySet = this.entrySet();
				Iterator<java.util.Map.Entry<String, String>> iterator = entrySet.iterator();
				while (iterator.hasNext()) {
					java.util.Map.Entry<String, String> next = iterator.next();
					if (key.toString().equalsIgnoreCase(next.getKey())) {
						return next.getValue();
					}
					
				}
				return null;
			}
			
		};
		String oemType = com.voole.util.prop.PropertiesUtil.getProperty(context, "oemType");
		if (!TextUtils.isEmpty(oemType)&&"TCL_4K".equalsIgnoreCase(oemType)) { //TCL 获取硬盘名称
			TStorageManager ts = TStorageManager.getInstance(context);
			List<String> paths = DownloadResourceManager.getInstance().getFileDir();
			if(paths == null){
				return null;
			}
			int defaultName = 0;
			for (String checkPath : paths) {
				com.voole.epg.download.utils.Log.e(checkPath);
	//			com.voole.epg.f4kdownload.utils.Log.e(ts.getVolumeLabel(checkPath));
				if(ts.getVolumeLabel(checkPath) != null){
					labMap.put(checkPath,ts.getVolumeLabel(checkPath));
	//						labMap.put(ts.getVolumeLabel(checkPath), checkPath);
				} else {
					defaultName ++;
					labMap.put(checkPath, CheckDownDialogView.DISKNAME+defaultName);
				}
			}
		}else{ //其他 截取Android路径 显示名称
			List<String> paths = DownloadResourceManager.getInstance().getFileDir();
			if (paths==null) {
				return labMap;
			}
			for (String string : paths) {
				labMap.put(string, string.substring(string.lastIndexOf("/")+1, string.length()));
			}
		}
		
		return labMap;
	}
	//获取路径
	public String getDownPathname() {
//		Map<String, String> labMapPath  = DetectionDisk();
		String present_edit = DownloadResourceManager
				.getInstance().getDownLoadPath();
		com.voole.epg.download.utils.Log.e("present_edit++++++++++++++"+present_edit);
		if (!TextUtils.isEmpty(present_edit)) {
			present_edit = present_edit.replace("/voole_video", "").trim();
		}
//		return labMapPath.get(present_edit);
		return present_edit;
	}
	
	public PathChoiceClickListener listener;

	public interface PathChoiceClickListener{
		public void onClick(View v);
	}

	public PathChoiceClickListener getListener() {
		return listener;
	}

	public void setListener(PathChoiceClickListener listener) {
		this.listener = listener;
	}
	
}
