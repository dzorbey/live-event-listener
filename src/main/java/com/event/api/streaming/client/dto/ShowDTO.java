package com.event.api.streaming.client.dto;

import java.io.Serializable;

public class ShowDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String showId = null;
	private String title = null;
	private String firstCast = null;
	

	public String getFirstCast() {
		return firstCast;
	}

	public void setFirstCast(String firstCast) {
		this.firstCast = firstCast;
	}

	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}