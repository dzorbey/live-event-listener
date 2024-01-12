package com.event.api.streaming.client.model;

import java.io.Serializable;

public class Stream implements Serializable {

	private static final long serialVersionUID = 1L;

	private String platform = null;
	private String id = null;
	private String event = null;
	private StreamData data = null;

	public String getPlatform() {
		return platform;
	}

	public Stream platform(String platform) {
		this.platform = platform;
		return this;
	}

	public String getId() {
		return id;
	}

	public Stream id(String id) {
		this.id = id;
		return this;
	}

	public String getEvent() {
		return event;
	}

	public Stream event(String event) {
		this.event = event;
		return this;
	}

	public StreamData getData() {
		return data;
	}

	public Stream data(StreamData data) {
		this.data = data;
		return this;
	}
}