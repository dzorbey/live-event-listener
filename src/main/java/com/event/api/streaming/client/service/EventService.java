package com.event.api.streaming.client.service;

import java.util.LinkedHashSet;

import com.event.api.streaming.client.dto.EventResponse;
import com.event.api.streaming.client.model.Show;
import com.event.api.streaming.client.model.Stream;
import com.event.api.streaming.client.model.User;

public interface EventService {
	
	void restartListener(Integer duration, Integer userCountLimit);
	
	EventResponse generateStreamResponse();

	void shows2020Onwards(Show show, String event);

	void processUserData(User user);

	void processPlatformEvents(Stream stream, String event);

	void processShowData(Show show);

	void findUserEventTypes(User user, String event);
	
	void findUserEvents(Stream stream, String event);
	
	void succesfullStreams(LinkedHashSet<Stream> platformStreams);
}
