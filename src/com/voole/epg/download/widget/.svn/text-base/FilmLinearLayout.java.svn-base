package com.voole.epg.download.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.voole.epg.download.DisplayManager;
import com.voole.epg.download.base.BaseLinearLayout;
import com.voole.epg.download.bean.FilmDownLoad4k;
import com.voole.epg.f4k.R;

public class FilmLinearLayout extends BaseLinearLayout{
	/**分割线的高度*/
	public static  int DIVIDER_HEIGHT = 3;
	/**每页条数*/
	public static  int PAGESIZE = 6;
	private Context context;
//	private Adapter adapter;
	private List<FilmDownLoad4k> films;
	private int pageCount;
	private int devider;
	public static final int ADAPTERLINEARLAYOUT_ID = 0x01101;
	private int currentPage = 1;
	/**当前页面开始位置*/
	private int startIndex;
	private List<AdapterItemView> itemViews;
	public FilmLinearLayout(Context context) {
		super(context);
		this.context = context;
		
		init();
	}
	private void init() {
		
		int height = DisplayManager.getInstance(context).getScreenHeight();
		if (height>=2160) {
			PAGESIZE = 10;
		}else if(height>=1080){
			PAGESIZE = 8;
		}
		
		itemViews = new ArrayList<AdapterItemView>();
		this.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < PAGESIZE; i++) {
			AdapterItemView itemview = new AdapterItemView(context);
			itemViews.add(itemview);
			LayoutParams itemParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,1);
			this.addView(itemview,itemParams);
			LinearLayout.LayoutParams  deviderParams = new LayoutParams(LayoutParams.MATCH_PARENT, DIVIDER_HEIGHT);
			this.addView(getDevider(context),deviderParams);
		}
		
	}
	/**
	 * 获取当前页面
	 * @return
	 */
	public int getCurrentPage(){
		return currentPage;
	}
	/**
	 * 获取总页数
	 * @return
	 */
	public int getTotalPage(){
		return pageCount==0?1:pageCount;
	}
	
	
	/**
	 * 获取当前显示的起始条目
	 * @return
	 */
	public int getCurrentPagerIndex(){
		return startIndex;
	}
	
	
	private View getDevider(Context context) {
		View deviderView = new View(context);
		if (devider==0) {
			deviderView.setBackgroundResource(R.drawable.download_devider_line);
		}else{
			deviderView.setBackgroundResource(devider);
		}
		return deviderView;
	}
	
	public void setChildViews(LinkedHashMap<String, FilmDownLoad4k> downStatus){
		setChildViews(setData(downStatus));
	}
	
	public List<FilmDownLoad4k>  setData(LinkedHashMap<String, FilmDownLoad4k> downStatus){
		ArrayList<FilmDownLoad4k> arrayList = null;
		if(downStatus!=null){
			Collection<FilmDownLoad4k> values = downStatus.values();
			arrayList = new ArrayList<FilmDownLoad4k>();
			for (FilmDownLoad4k filmDownLoad4k : values) {
				arrayList.add(filmDownLoad4k);
			}
		}
		setData(arrayList);
		return arrayList;
	}
	
	public void setChildViews(List<FilmDownLoad4k> films){
		
		setData(films);
		
		addToParent(films);
		
	}
	private void setData(List<FilmDownLoad4k> films) {
		this.films = films;
		if (films==null||films.size()==0) {
			pageCount = currentPage = 1;
		}else{
			int count = films.size();
			if (count%PAGESIZE==0) {
				pageCount = count/PAGESIZE;
			}else{
				pageCount = (count/PAGESIZE)+1;
			}
		}
		if (currentPage>=pageCount) {
			currentPage = pageCount;
		}
	}

	private void addToParent(List<FilmDownLoad4k> films) {
		int count = films==null?0:films.size();
		addChildViews(films, count,0);
	}

	
	public int getTotalChildCount(){
		return films==null?0:films.size();
	}

	/**
	 * 更新子view
	 * @param adapter
	 * @param count         显示的数量
	 * @param startIndex	开始的编号
	 */
	private void addChildViews(List<FilmDownLoad4k> films, int count,int startIndex) {
		this.startIndex = startIndex;
		for (int i = 0; i < PAGESIZE; i++) {
			if (i<count&&films!=null){
				this.itemViews.get(i).setTag(films.get(startIndex+i));
			}else{
				this.itemViews.get(i).setTag(null);
			}
		}
		
		for (int i = 0; i < itemViews.size(); i++) {
			itemViews.get(i).notifyDataChange();
		}
		
	}
	
	public void showNextPage(){
		if (films!=null&&films.size()>0) {
			int count = films.size();
			if (currentPage*PAGESIZE < count) {
				currentPage++;
				if ((currentPage+1)*PAGESIZE>count) {
					addChildViews(films, count%PAGESIZE,(currentPage-1)*PAGESIZE);
				}else{
					addChildViews(films, PAGESIZE,(currentPage-1)*PAGESIZE);
				}
			}else{
				Toast.makeText(mContext, "已经是最后一页啦..", 0).show();
			}
		}else{
			Toast.makeText(mContext, "没有后一页..", 0).show();
		}
	}
	public void showPrePage(){
		if (films!=null) {
			if (currentPage>1) {
				addChildViews(films, PAGESIZE,(currentPage-2)*PAGESIZE);
				currentPage--;
			}else{
				Toast.makeText(mContext, "没有上一页", 0).show();
			}
		}
	}
	
	/**
	 * 设置间隔图片资源
	 * @param drawable
	 */
	public void setDevider(int drawable){
		this.devider = drawable;
	}
	
	/**
	 * 更新界面
	 */
	public void notifyDataChange(){
		int count = films==null?0:films.size();
		if (currentPage*PAGESIZE>count) {
			addChildViews(films, count%PAGESIZE,(currentPage-1)*PAGESIZE);
		}else{
			addChildViews(films, PAGESIZE,(currentPage-1)*PAGESIZE);
		}
	}
	
	public List<FilmDownLoad4k> getAdapter(){
		return films;
	}
}