package com.event.api.streaming.client.enums;

import io.swagger.annotations.ApiModel;

@ApiModel
public enum StreamEvent {

	STREAM_STARTED("stream-started"), STREAM_FINISHED("stream-finished"), SHOW_LIKED("show-liked"),
	STREAM_INTERRUPTED("stream-interrupted");

	private String value;

	StreamEvent(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}