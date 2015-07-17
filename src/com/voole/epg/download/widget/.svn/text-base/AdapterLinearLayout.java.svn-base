package com.voole.epg.download.widget;
//package com.voole.epg.f4k_download.widget;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.Adapter;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.voole.epg.f4k.R;
//import com.voole.epg.f4k_download.base.BaseLinearLayout;
//
//public class AdapterLinearLayout extends BaseLinearLayout{
//	/**分割线的高度*/
//	public static final int DIVIDER_HEIGHT = 3;
//	/**每页条数*/
//	public static final  int PAGESIZE = 6;
//	private Context context;
//	private Adapter adapter;
//	private int pageCount;
//	private int devider;
//	public static final int ADAPTERLINEARLAYOUT_ID = 0x01101;
//	private int currentPage = 1;
//	/**当前页面开始位置*/
//	private int startIndex;
//	public AdapterLinearLayout(Context context) {
//		super(context);
//		this.context = context;
//		this.setOrientation(LinearLayout.VERTICAL);
//		LinearLayout.LayoutParams  params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		this.setLayoutParams(params);
//	}
//	/**
//	 * 获取当前页面
//	 * @return
//	 */
//	public int getCurrentPage(){
//		return currentPage;
//	}
//	/**
//	 * 获取总页数
//	 * @return
//	 */
//	public int getTotalPage(){
//		return pageCount==0?1:pageCount;
//	}
//	
//	
//	public int getResetTotalPage(){
//		if (adapter==null) {
//			return 0;
//		}
//		int count = adapter.getCount();
//		if (count<=0) {
//			return 0;
//		}
//		if (count%PAGESIZE==0) {
//			pageCount = count/PAGESIZE;
//		}else{
//			pageCount = (count/PAGESIZE)+1;
//		}
//		return pageCount;
//	}
//	/**
//	 * 获取当前显示的起始条目
//	 * @return
//	 */
//	public int getCurrentPagerIndex(){
//		return startIndex;
//	}
//	
//	
//	private View getDevider(Context context) {
//		View deviderView = new View(context);
//		if (devider==0) {
//			deviderView.setBackgroundResource(R.drawable.download_devider_line);
//		}else{
//			deviderView.setBackgroundResource(devider);
//		}
//		LinearLayout.LayoutParams  params = new LayoutParams(LayoutParams.MATCH_PARENT, DIVIDER_HEIGHT);
//		deviderView.setLayoutParams(params);
//		return deviderView;
//	}
//	
//	
//	
//	public void setChildViewsAdapter(Adapter adapter){
//		this.removeAllViews();
//		if (adapter==null) {
//			return;
//		}
//		int count = adapter.getCount();
//		if (count<=0) {
//			return;
//		}
//		this.adapter = adapter;
//		if (count%PAGESIZE==0) {
//			pageCount = count/PAGESIZE;
//		}else{
//			pageCount = (count/PAGESIZE)+1;
//		}
//		
//		addToParent(adapter);
//		
//	}
//
//	private void addToParent(Adapter adapter) {
//		int count = adapter.getCount();
//		addChildViews(adapter, count,0);
//	}
//
//
//	/**
//	 * 更新子view
//	 * @param adapter
//	 * @param count         显示的数量
//	 * @param startIndex	开始的编号
//	 */
//	private void addChildViews(Adapter adapter, int count,int startIndex) {
//		this.adapter = adapter;
//		this.removeAllViews();
//		this.startIndex = startIndex;
//		if (adapter.getCount()>0) {
//			if (count<PAGESIZE) {
//				for (int i = 0; i < count; i++) {
//					this.addView(adapter.getView(startIndex+i, null, this));
//					this.addView(getDevider(context));
//				}
//			}else{
//				for (int i = 0; i < PAGESIZE; i++) {
//					this.addView(adapter.getView(startIndex+i, null, this));
//					this.addView(getDevider(context));
//				}
//			}
//		}
//	}
//	
//	public void showNextPage(){
//		if (adapter!=null) {
//			int count = adapter.getCount();
//			if (currentPage*PAGESIZE < count) {
//				currentPage++;
//				if ((currentPage+1)*PAGESIZE>count) {
//					addChildViews(adapter, count%PAGESIZE,(currentPage-1)*PAGESIZE);
//				}else{
//					addChildViews(adapter, PAGESIZE,(currentPage-1)*PAGESIZE);
//				}
//			}else{
//				Toast.makeText(mContext, "已经是最后一页啦..", 0).show();
//			}
//		}
//	}
//	public void showPrePage(){
//		if (adapter!=null) {
//			if (currentPage>1) {
//				addChildViews(adapter, PAGESIZE,(currentPage-2)*PAGESIZE);
//				currentPage--;
//			}else{
//				Toast.makeText(mContext, "没有上一页", 0).show();
//			}
//		}
//	}
//	
//	/**
//	 * 设置间隔图片资源
//	 * @param drawable
//	 */
//	public void setDevider(int drawable){
//		this.devider = drawable;
//	}
//	
//	/**
//	 * 更新界面
//	 */
//	public void notifyDataChange(){
//		int count = adapter.getCount();
//		if (currentPage*PAGESIZE>count) {
//			addChildViews(adapter, count%PAGESIZE,(currentPage-1)*PAGESIZE);
//		}else{
//			addChildViews(adapter, PAGESIZE,(currentPage-1)*PAGESIZE);
//		}
//	}
//	
//	public Adapter getAdapter(){
//		return adapter;
//	}
//}