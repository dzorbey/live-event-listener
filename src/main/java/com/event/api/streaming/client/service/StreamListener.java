package com.event.api.streaming.client.service;

import java.io.IOException;
import java.io.InputStream;

public interface StreamListener {
	
	void subscribeStream(String platform) throws IOException, InterruptedException;
	
	String readEvent(InputStream stream) throws IOException;
	
	void populateEvents(String platform, String stream);
}
