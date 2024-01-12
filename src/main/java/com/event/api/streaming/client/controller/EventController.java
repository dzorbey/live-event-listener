package com.event.api.streaming.client.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.event.api.streaming.client.dto.EventResponse;
import com.event.api.streaming.client.service.EventService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/listener", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Streaming Events Client")
public class EventController {

	@Autowired
	private EventService eventService;
	
	

	@ResponseStatus(code = HttpStatus.ACCEPTED)
	@RequestMapping(method = RequestMethod.POST, value = "/restart")
	@ApiOperation(value = "Restart event listener", response = EventResponse.class, nickname = "restart")
	@ApiResponses({
			@ApiResponse(code = 200, message = "Listener Restarted", response = EventResponse.class) })
	public ResponseEntity<EventResponse> eventListenerRestart(
			@ApiParam(required = false, value = "duration") @RequestHeader("duration") Integer duration,
			@ApiParam(required = false, value = "event-user-limit") @RequestHeader("event-user-limit") Integer eventUserLimit
			) throws IOException {
		
		eventService.restartListener(duration, eventUserLimit);
		
		return ResponseEntity.status(200).build();
	}	
	

	@ResponseStatus(code = HttpStatus.ACCEPTED)
	@RequestMapping(method = RequestMethod.GET, value = "/results")
	@ApiOperation(value = "Generate Event Results from Streaming Server", response = EventResponse.class, nickname = "events")
	@ApiResponses({
			@ApiResponse(code = 200, message = "EventResponse returned", response = EventResponse.class)})
	public ResponseEntity<EventResponse> eventListenerResults() throws IOException {
		
		EventResponse response =  eventService.generateStreamResponse();
		
		return ResponseEntity.status(200).body(response);
	}	
}
