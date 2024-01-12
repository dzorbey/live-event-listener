package com.event.api.streaming.client.enums;

public enum StreamPlatform {

	SYTFLIX("sytflix"), SYTAZON("sytazon"), SYSNEY("sysney");

	private String value;

	StreamPlatform(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}