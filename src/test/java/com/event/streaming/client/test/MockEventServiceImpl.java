package com.event.streaming.client.test;

import java.util.LinkedHashSet;
import java.util.Map;

import com.event.api.streaming.client.common.Utils;
import com.event.api.streaming.client.dto.EventResponse;
import com.event.api.streaming.client.model.Stream;
import com.event.api.streaming.client.service.EventServiceImpl;

public class MockEventServiceImpl extends EventServiceImpl {

	
	
	public EventResponse generateStreamResponse(Map<String, LinkedHashSet<Stream>> streams) {

		EventResponse response = new EventResponse();
		response.setResult("[accepted]");

		setDurationsDTO(response);

		response.setUserEventCount(Utils.eventCounter);

		/****************/
		runScenarios(response, streams);
		/****************/

		setShows2020DTO(response);
		setplatformEventsDTO(response);
		
		setUserEventsDTO(response);
		setShowsDTO(response);

		styFlixStartedEventsRatioCalculation(response);
		setSuccessfullStreamingEventsDTO(response);

		return response;
	}
	
}
