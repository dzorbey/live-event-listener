package com.event.api.streaming.client.dto;

import java.io.Serializable;
import java.util.Set;

public class PlatformEventDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String platform;
	private Set<EventDTO> events;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Set<EventDTO> getEvents() {
		return events;
	}

	public void setEvents(Set<EventDTO> events) {
		this.events = events;
	}

}