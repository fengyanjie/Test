package com.voole.epg.download.widget;

import com.voole.epg.corelib.model.movies.Film;

public interface MovieViewListener {
	public void onItemSelected(Film item,int index);
	public void onPlay(Film item);
	public void onGotoPage(int pageNo);
}
