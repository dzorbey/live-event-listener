package com.event.streaming.client.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.event.api.streaming.client.dto.EventResponse;
import com.event.api.streaming.client.dto.PlatformEventDTO;
import com.event.api.streaming.client.dto.ShowFrequencyDTO;
import com.event.api.streaming.client.dto.UserDTO;
import com.event.api.streaming.client.enums.StreamEvent;
import com.event.api.streaming.client.enums.StreamPlatform;
import com.event.api.streaming.client.model.Stream;

@Component
@Profile("test")
class EventListenerApplicationTests {

	public static Map<String, LinkedHashSet<Stream>> eventsMap = new ConcurrentHashMap<String, LinkedHashSet<Stream>>();
	
	void initilize() {
		Arrays.asList(StreamPlatform.values()).stream().forEach((platform) -> {
			LinkedHashSet<Stream> streamSet = new LinkedHashSet<>();
			eventsMap.put(platform.value(), streamSet);
		});
	}
	
	private static MockEventServiceImpl mockEventServiceImpl = new MockEventServiceImpl();

	
	@Test
	void testCounts() {
		initilize();
		mockEventServiceImpl.initilize();
		
		LinkedHashSet<Stream> streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s12", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s12", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 13L, "s12", 2015, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 13L, "s15", 2015, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 15L, "s15", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 16L, "s15", 2015, StreamEvent.SHOW_LIKED.value()));
		eventsMap.put(StreamPlatform.SYTFLIX.value(), streams);
		
		streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 11L, "s15", 2015, StreamEvent.STREAM_INTERRUPTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 18L, "s16", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 18L, "s20", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 19L, "s21", 2015, StreamEvent.SHOW_LIKED.value()));
		eventsMap.put(StreamPlatform.SYSNEY.value(), streams);
		
		EventResponse response = 
				 mockEventServiceImpl.generateStreamResponse(eventsMap);
	
		assertTrue(response.getDistinctShowCount() == 5);
		assertTrue(response.getDistinctUserCount() == 6);
		assertTrue(response.getEventCount() == 10);
	}
	
	
	
	
	@Test
	void testSytFlixStartedEventsRatio() {
		initilize();
		mockEventServiceImpl.initilize();
		
		LinkedHashSet<Stream> streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s12", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 12L, "s13", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 13L, "s14", 2015, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2015, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 15L, "s16", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 16L, "s17", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 17L, "s18", 2015, StreamEvent.STREAM_INTERRUPTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 18L, "s19", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 19L, "s20", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 19L, "s21", 2015, StreamEvent.SHOW_LIKED.value()));
		
		eventsMap.put(StreamPlatform.SYTFLIX.value(), streams);
		EventResponse response = 
				 mockEventServiceImpl.generateStreamResponse(eventsMap);
	
		assertTrue(response.getSytFlixStartedEventsRatio().equals(new BigDecimal(20)));
	}
	
	
	
	
	@Test
	void testShowsCount2020Onwards() {
		initilize();
		mockEventServiceImpl.initilize();
		
		LinkedHashSet<Stream> streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s12", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 12L, "s13", 2022, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 13L, "s14", 2020, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_FINISHED.value()));
		
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_FINISHED.value()));
		
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 15L, "s16", 2015, StreamEvent.STREAM_STARTED.value()));
		eventsMap.put(StreamPlatform.SYTFLIX.value(), streams);
		
		streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 16L, "s17", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 17L, "s18", 2023, StreamEvent.STREAM_INTERRUPTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 18L, "s19", 2015, StreamEvent.SHOW_LIKED.value()));
		eventsMap.put(StreamPlatform.SYSNEY.value(), streams);
		
		
		streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 19L, "s20", 1985, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 20L, "s21", 2014, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 21L, "s22", 1990, StreamEvent.STREAM_INTERRUPTED.value()));
		
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_FINISHED.value()));
		
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 22L, "s23", 2022, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 23L, "s24", 2015, StreamEvent.STREAM_STARTED.value()));
		eventsMap.put(StreamPlatform.SYTAZON.value(), streams);
		
		EventResponse response = 
				 mockEventServiceImpl.generateStreamResponse(eventsMap);
		assertTrue(response.getShowsOnwards2020().size() == 4);
		
		for(ShowFrequencyDTO dto :response.getShowsOnwards2020()) {
			if(dto.getEvent().equals(StreamEvent.STREAM_FINISHED.value()))
				assertTrue(dto.getFrequency() == 3);
			else if(dto.getEvent().equals(StreamEvent.SHOW_LIKED.value()))
				assertTrue(dto.getFrequency() == 2);
			else if(dto.getEvent().equals(StreamEvent.STREAM_INTERRUPTED.value()))
				assertTrue(dto.getFrequency() == 1);
			else if(dto.getEvent().equals(StreamEvent.STREAM_STARTED.value()))
				assertTrue(dto.getFrequency() == 1);
			else
				assertTrue(false);
		}
	}
	
	
	@Test
	void testUserEventTypes() {
		initilize();
		mockEventServiceImpl.initilize();
		
		LinkedHashSet<Stream> streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s12", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s13", 2022, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s14", 2020, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s16", 2015, StreamEvent.STREAM_STARTED.value()));
		eventsMap.put(StreamPlatform.SYTFLIX.value(), streams);
		
		streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 16L, "s17", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 11L, "s18", 2023, StreamEvent.STREAM_INTERRUPTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 18L, "s19", 2015, StreamEvent.SHOW_LIKED.value()));
		eventsMap.put(StreamPlatform.SYSNEY.value(), streams);
		
		
		streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 18L, "s20", 1985, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 20L, "s21", 2014, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 21L, "s22", 1990, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 21L, "s23", 2022, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 20L, "s24", 2015, StreamEvent.STREAM_STARTED.value()));
		eventsMap.put(StreamPlatform.SYTAZON.value(), streams);
		
		EventResponse response = 
				 mockEventServiceImpl.generateStreamResponse(eventsMap);
		assertTrue(response.getUsers().size() == 6);
		
		for(UserDTO dto :response.getUsers()) {
			if(dto.getId().equals(11L))
				assertTrue(dto.getExecutedEventTypes().size() == 3);
			else if(dto.getId().equals(14L))
				assertTrue(dto.getExecutedEventTypes().size() == 2);
			else if(dto.getId().equals(16L))
				assertTrue(dto.getExecutedEventTypes().size() == 1);
			else if(dto.getId().equals(18L))
				assertTrue(dto.getExecutedEventTypes().size() == 1);
			else if(dto.getId().equals(20L))
				assertTrue(dto.getExecutedEventTypes().size() == 2);
			else if(dto.getId().equals(21L))
				assertTrue(dto.getExecutedEventTypes().size() == 1);
			else
				assertTrue(false);
		}
		
	   for(UserDTO dto :response.getUsers()) {
			if(dto.getId().equals(11L))
				assertTrue(dto.getExecutedEvents().size() == 4);
			else if(dto.getId().equals(14L))
				assertTrue(dto.getExecutedEvents().size() == 2);
			else if(dto.getId().equals(16L))
				assertTrue(dto.getExecutedEvents().size() == 1);
			else if(dto.getId().equals(18L))
				assertTrue(dto.getExecutedEvents().size() == 2);
			else if(dto.getId().equals(20L))
				assertTrue(dto.getExecutedEvents().size() == 2);
			else if(dto.getId().equals(21L))
				assertTrue(dto.getExecutedEvents().size() == 2);
			else
				assertTrue(false);
	   }
	}
	
	
	
	@Test
	void testPlatformEvents() {
		initilize();
		mockEventServiceImpl.initilize();
		
		LinkedHashSet<Stream> streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s12", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s13", 2022, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s14", 2020, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s16", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s12", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s13", 2022, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s14", 2020, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s16", 2015, StreamEvent.STREAM_STARTED.value()));
		eventsMap.put(StreamPlatform.SYTFLIX.value(), streams);
		
		streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 16L, "s17", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 11L, "s18", 2023, StreamEvent.STREAM_INTERRUPTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 18L, "s19", 2015, StreamEvent.SHOW_LIKED.value()));
		eventsMap.put(StreamPlatform.SYSNEY.value(), streams);
		
		
		streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 18L, "s20", 1985, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 20L, "s21", 2014, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 21L, "s22", 1990, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 21L, "s23", 2022, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 20L, "s24", 2015, StreamEvent.STREAM_STARTED.value()));
		eventsMap.put(StreamPlatform.SYTAZON.value(), streams);
		
		EventResponse response = 
				 mockEventServiceImpl.generateStreamResponse(eventsMap);
		
		for(PlatformEventDTO dto :response.getPlatformEvents()) {
			if(dto.getPlatform().equals(StreamPlatform.SYTFLIX.value()))
				assertTrue(dto.getEvents().size() == 10);
			else if(dto.getPlatform().equals(StreamPlatform.SYSNEY.value()))
				assertTrue(dto.getEvents().size() == 3);
			else if(dto.getPlatform().equals(StreamPlatform.SYTAZON.value()))
				assertTrue(dto.getEvents().size() == 5);
			else
				assertTrue(false);
		}
	}
	
	
	
	@Test
	void testSuccesfullStreamingEvents() {
		initilize();
		mockEventServiceImpl.initilize();
		
		LinkedHashSet<Stream> streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s12", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s13", 2022, StreamEvent.SHOW_LIKED.value()));
		
		/****/
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s14", 2020, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s14", 2021, StreamEvent.STREAM_FINISHED.value()));
		/****/
		
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s16", 2015, StreamEvent.STREAM_STARTED.value()));
		
		/****/
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s22", 2015, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s22", 2022, StreamEvent.STREAM_FINISHED.value()));
		/****/
		
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s14", 2020, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 14L, "s15", 2021, StreamEvent.STREAM_FINISHED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTFLIX.value(), 11L, "s16", 2015, StreamEvent.STREAM_STARTED.value()));
		eventsMap.put(StreamPlatform.SYTFLIX.value(), streams);
		
		streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 16L, "s17", 2015, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 11L, "s18", 2023, StreamEvent.STREAM_INTERRUPTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYSNEY.value(), 18L, "s19", 2015, StreamEvent.SHOW_LIKED.value()));
		eventsMap.put(StreamPlatform.SYSNEY.value(), streams);
		
		
		streams = new LinkedHashSet<>();
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 18L, "s20", 1985, StreamEvent.SHOW_LIKED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 20L, "s21", 2014, StreamEvent.SHOW_LIKED.value()));
		
		/****/
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 21L, "s32", 1990, StreamEvent.STREAM_STARTED.value()));
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 22L, "s32", 2022, StreamEvent.STREAM_FINISHED.value()));
		/****/
		
		streams.add(Utils.createStream(StreamPlatform.SYTAZON.value(), 20L, "s24", 2015, StreamEvent.STREAM_STARTED.value()));
		eventsMap.put(StreamPlatform.SYTAZON.value(), streams);
		
		EventResponse response = 
				 mockEventServiceImpl.generateStreamResponse(eventsMap);
		
		//Users:11L and 21L
		assertTrue(response.getSuccessfullStreamingEventsPerUser().size() == 2);
	}
}
