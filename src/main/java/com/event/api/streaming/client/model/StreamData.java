package com.event.api.streaming.client.model;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class StreamData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Show show = null;
	private User user = null;

	@SerializedName("event_date")
	private String eventDate = null;

	public Show getShow() {
		return show;
	}

	public void setShow(Show show) {
		this.show = show;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;// Utils.toLocalDate(eventDate);
	}
}