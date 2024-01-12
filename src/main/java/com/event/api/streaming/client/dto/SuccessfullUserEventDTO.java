package com.event.api.streaming.client.dto;

public class SuccessfullUserEventDTO {

	private String eventId;
	private String nextEventId;


	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getNextEventId() {
		return nextEventId;
	}

	public void setNextEventId(String nextEventId) {
		this.nextEventId = nextEventId;
	}
}
