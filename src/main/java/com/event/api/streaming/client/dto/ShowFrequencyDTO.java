package com.event.api.streaming.client.dto;


public class ShowFrequencyDTO {

	private String event;
	private Integer frequency;
	
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

}
