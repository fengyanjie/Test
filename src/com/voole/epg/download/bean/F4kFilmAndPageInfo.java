package com.voole.epg.download.bean;

import java.util.List;

import com.voole.epg.corelib.model.movies.Film;
import com.voole.epg.corelib.model.movies.FilmAndPageInfo;

public class F4kFilmAndPageInfo extends FilmAndPageInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7262329028733235330L;
	
	private List<Film> filmListLowRate;

	public List<Film> getFilmListLowRate() {
		return filmListLowRate;
	}

	public void setFilmListLowRate(List<Film> filmListLowRate) {
		this.filmListLowRate = filmListLowRate;
	}

	@Override
	public String toString() {
		return "F4kFilmAndPageInfo [filmListLowRate=" + filmListLowRate
				+ ", getFilmListLowRate()=" + getFilmListLowRate()
				+ ", getName()=" + getName() + ", getImageUrl()="
				+ getImageUrl() + ", getFilmList()=" + getFilmList()
				+ ", getPageIndex()=" + getPageIndex() + ", getPageCount()="
				+ getPageCount() + ", getFilmCount()=" + getFilmCount()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
}
