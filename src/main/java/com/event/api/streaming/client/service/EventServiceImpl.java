package com.event.api.streaming.client.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.event.api.streaming.client.common.Utils;
import com.event.api.streaming.client.dto.DurationDTO;
import com.event.api.streaming.client.dto.EventDTO;
import com.event.api.streaming.client.dto.EventResponse;
import com.event.api.streaming.client.dto.PlatformEventDTO;
import com.event.api.streaming.client.dto.ShowDTO;
import com.event.api.streaming.client.dto.ShowFrequencyDTO;
import com.event.api.streaming.client.dto.SuccessStreamingEventDTO;
import com.event.api.streaming.client.dto.SuccessfullUserEventDTO;
import com.event.api.streaming.client.dto.UserDTO;
import com.event.api.streaming.client.enums.StreamEvent;
import com.event.api.streaming.client.enums.StreamPlatform;
import com.event.api.streaming.client.model.Show;
import com.event.api.streaming.client.model.Stream;
import com.event.api.streaming.client.model.User;

@Service
public class EventServiceImpl implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	private Map<Long, Set<String>> userEventTypesMap;

	private Map<Long, LinkedHashSet<EventDTO>> userExecutedEventsMap;

	private Map<Long, UserDTO> eventUsersMap = new ConcurrentHashMap<Long, UserDTO>();

	private static Map<String, LinkedHashSet<EventDTO>> platformEventsMap;

	private static Map<Long, LinkedHashSet<SuccessfullUserEventDTO>> successFullEventsMap;

	private Map<String, Set<String>> eventShows2020Map;

	private Map<String, ShowDTO> showsMap;

	private static BigDecimal totalSytFlixEvents;
	private static BigDecimal totalSytFlixStartedEvents;

	static StreamListenerImpl listenerImpl = new StreamListenerImpl();


	@PostConstruct
	public void initilize() {
		userEventTypesMap = new ConcurrentHashMap<Long, Set<String>>();
		userExecutedEventsMap = new ConcurrentHashMap<Long, LinkedHashSet<EventDTO>>();
		eventUsersMap = new ConcurrentHashMap<Long, UserDTO>();
		platformEventsMap = new ConcurrentHashMap<String, LinkedHashSet<EventDTO>>();
		eventShows2020Map =  new ConcurrentHashMap<String, Set<String>>();
		
		showsMap = new ConcurrentHashMap<String, ShowDTO>();
		successFullEventsMap = new ConcurrentHashMap<Long, LinkedHashSet<SuccessfullUserEventDTO>>();

		totalSytFlixEvents = BigDecimal.ZERO;
		totalSytFlixStartedEvents = BigDecimal.ZERO;

		Arrays.asList(StreamPlatform.values()).stream().forEach((platform) -> {

			LinkedHashSet<EventDTO> eventsSet = new LinkedHashSet<>();
			Collections.synchronizedSet(eventsSet);
			platformEventsMap.put(platform.value(), eventsSet);
		});
		
		Arrays.asList(StreamEvent.values()).stream().forEach((eventEnum) -> {
			if(!eventShows2020Map.containsKey(eventEnum.value())) {
				LinkedHashSet<String> eventShowsSet = new LinkedHashSet<>();
				eventShows2020Map.put(eventEnum.value(), eventShowsSet);
			}
		});	
	}

	public void restartListener(Integer duration, Integer userCountLimit) {
		logger.info("Listener restarted..");
		initilize();
		listenerImpl.restart(duration, userCountLimit);
	}

	public EventResponse generateStreamResponse() {

		EventResponse response = new EventResponse();
		response.setResult("[accepted]");

		setDurationsDTO(response);

		response.setUserEventCount(Utils.eventCounter);

		/****************/
		runScenarios(response, Utils.getStreams());
		/****************/

		setShows2020DTO(response);
		setplatformEventsDTO(response);
		
		setUserEventsDTO(response);
		setShowsDTO(response);

		styFlixStartedEventsRatioCalculation(response);
		setSuccessfullStreamingEventsDTO(response);

		return response;
	}
	
	
	

	public void setShowsDTO(EventResponse response) {
		List<ShowDTO> shows = new ArrayList<>();
		showsMap.values().stream().forEach((show) -> {
			shows.add(SerializationUtils.clone((ShowDTO) show));
		});
		response.setShows(shows);
		response.setDistinctShowCount(shows.size());
	}
	

	public void setUserEventsDTO(EventResponse response) {

		List<UserDTO> eventUsers = new ArrayList<>();
		eventUsersMap.values().stream().forEach((eventUser) -> {
			eventUsers.add(SerializationUtils.clone((UserDTO) eventUser));
		});
		eventUsers.stream().forEach((eventUser) -> {

			if (userEventTypesMap.containsKey(eventUser.getId())) {
				Set<String> userExecutedEventTypes = userEventTypesMap.get(eventUser.getId());
				eventUser.setExecutedEventTypes(userExecutedEventTypes);
			}

			if (userExecutedEventsMap.containsKey(eventUser.getId())) {
				Set<EventDTO> userExecutedEvents = userExecutedEventsMap.get(eventUser.getId());
				eventUser.setExecutedEvents(userExecutedEvents);
			}
		});
		response.setUsers(eventUsers);
		response.setDistinctUserCount(eventUsers.size());
	}

	public void setSuccessfullStreamingEventsDTO(EventResponse response) {
		List<SuccessStreamingEventDTO> eventDTOs = new ArrayList<>();
		for (Entry<Long, LinkedHashSet<SuccessfullUserEventDTO>> currentEntry : successFullEventsMap.entrySet()) {

			SuccessStreamingEventDTO successfullEventDTO = new SuccessStreamingEventDTO();
			successfullEventDTO.setUserId(currentEntry.getKey());
			successfullEventDTO.setSuccessCount(currentEntry.getValue().size());
			successfullEventDTO.setSuccessFullEvents(currentEntry.getValue());
			eventDTOs.add(successfullEventDTO);
		}
		response.setSuccessfullStreamingEventsPerUser(eventDTOs);
	}

	public void setShows2020DTO(EventResponse response) {
		List<ShowFrequencyDTO> showsOnwards2020 = new ArrayList<>();
		for (Entry<String, Set<String>> entry : eventShows2020Map.entrySet()) {
			ShowFrequencyDTO eventShows = new ShowFrequencyDTO();
			eventShows.setEvent(entry.getKey());
			eventShows.setFrequency(entry.getValue().size());
			showsOnwards2020.add(eventShows);
		}
		response.setShowsOnwards2020(showsOnwards2020);
	}

	public void setplatformEventsDTO(EventResponse response) {
		List<PlatformEventDTO> platformEvents = new ArrayList<>();
		for (Entry<String, LinkedHashSet<EventDTO>> currentEntry : platformEventsMap.entrySet()) {

			PlatformEventDTO platformEvent = new PlatformEventDTO();
			platformEvent.setPlatform(currentEntry.getKey());
			platformEvent.setEvents(currentEntry.getValue());
			platformEvents.add(platformEvent);
		}
		response.setPlatformEvents(platformEvents);
	}

	public void setDurationsDTO(EventResponse response) {
		List<DurationDTO> durations = new ArrayList<>();
		for (Entry<String, String> entry : Utils.durations.entrySet()) {
			DurationDTO duration = new DurationDTO();
			duration.setPlatform(entry.getKey());
			duration.setTimeSpent(entry.getValue());
			durations.add(duration);
		}
		response.setDurations(durations);
	}

	public void runScenarios(EventResponse response, Map<String, LinkedHashSet<Stream>> streams) {

		response.setEventCount(0);
		streams.values().stream().forEach((data) -> {
			response.setEventCount(response.getEventCount() + data.size());

			LinkedHashSet<Stream> platformStreams = (LinkedHashSet<Stream>) data;

			succesfullStreams(platformStreams);
			platformStreams.stream().forEach((stream) -> {

				String event = stream.getEvent();
				User user = stream.getData().getUser();
				Show show = stream.getData().getShow();

				findUserEvents(stream, event);
				findUserEventTypes(user, event);
				processUserData(user);
				shows2020Onwards(show, event);

				processPlatformEvents(stream, event);
				processShowData(show);

				sytFlixStartedEventsRatio(stream.getPlatform(), event);
			});
		});
	}

	public void styFlixStartedEventsRatioCalculation(EventResponse response) {

		if (totalSytFlixEvents.intValue() > 0) {
			BigDecimal sytFlixStartedEventsRatio = totalSytFlixStartedEvents.multiply(new BigDecimal(100))
					.divide(totalSytFlixEvents, Utils.mc);

			response.setSytFlixStartedEventsRatio(sytFlixStartedEventsRatio);
		}
	}

	public void shows2020Onwards(Show show, String event) {
		
		if (show.getReleaseYear() >= 2020) {
			String showId = show.getShowId();
			
			if (!eventShows2020Map.values().contains(showId)) {
				eventShows2020Map.get(event).add(showId);	
			}
		}
	}

	public void findUserEventTypes(User user, String event) {
		Long userId = user.getId();
		try {

			if (!userEventTypesMap.containsKey(userId)) {
				Set<String> set = new LinkedHashSet<>();
				Collections.synchronizedSet(set);
				set.add(event);

				userEventTypesMap.put(userId, set);
			} else {
				if (!userEventTypesMap.values().contains(event)) {
					userEventTypesMap.get(userId).add(event);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void findUserEvents(Stream stream, String event) {

		Long userId = stream.getData().getUser().getId();
		String eventDate = stream.getData().getEventDate();

		EventDTO currentEvent = new EventDTO();
		currentEvent.setEvent(event);
		currentEvent.setId(stream.getId());
		currentEvent.setShowId(stream.getData().getShow().getShowId());
		currentEvent.setEventDate(Utils.parseEventDate(eventDate, stream.getData().getUser().getCountry()));

		if (!userExecutedEventsMap.containsKey(userId)) {
			LinkedHashSet<EventDTO> events = new LinkedHashSet<>();
			Collections.synchronizedSet(events);
			events.add(currentEvent);
			userExecutedEventsMap.put(userId, events);
		} else {
			userExecutedEventsMap.get(userId).add(currentEvent);
		}
	}

	public void succesfullStreams(LinkedHashSet<Stream> platformStreams) {

		Iterator<Stream> it = platformStreams.iterator();
		if (it.hasNext()) {
			Stream currentStream = it.next();

			while (it.hasNext()) {
				Stream nextStream = it.next();

				String currentEvent = currentStream.getEvent();
				String nextEvent = nextStream.getEvent();

				// bug in the code, or intentionally wrong documentation,
				// there is no [stream-ended] event, instead [stream-finished]
				if (currentEvent.equals(StreamEvent.STREAM_STARTED.value())
						&& nextEvent.equals(StreamEvent.STREAM_FINISHED.value())) {

					String currentShowId = currentStream.getData().getShow().getShowId();
					String nextShowId = nextStream.getData().getShow().getShowId();

					String currentplatform = currentStream.getPlatform();
					String nextplatform = nextStream.getPlatform();

					if (currentplatform.equals(nextplatform) && currentShowId.equals(nextShowId)) {
						// successful event by definiton ..,
						// yet, when the currentEvent user is not the same as the next eventUser, is
						// this still a success?
						Long currentUserId = currentStream.getData().getUser().getId();

						SuccessfullUserEventDTO userEventDTO = new SuccessfullUserEventDTO();
						userEventDTO.setEventId(currentStream.getId());
						userEventDTO.setNextEventId(nextStream.getId());

						if (!successFullEventsMap.containsKey(currentUserId)) {
							LinkedHashSet<SuccessfullUserEventDTO> successfullEvents = new LinkedHashSet<>();
							successfullEvents.add(userEventDTO);
							successFullEventsMap.put(currentUserId, successfullEvents);
						} else {
							successFullEventsMap.get(currentUserId).add(userEventDTO);
						}
						/*
						 * logger.info("currentUserId:" + currentStream.getData().getUser().getId() +
						 * ", nextUserId:" + nextStream.getData().getUser().getId());
						 * 
						 * logger.info("userId:" + currentStream.getData().getUser().getId() +
						 * ", currentId:" + currentStream.getId() + ", nextId:" + nextStream.getId());
						 */
					}
				}
				currentStream = nextStream;
			}
		}
	}

	public void processUserData(User user) {
		Long userId = user.getId();

		if (!eventUsersMap.containsKey(userId)) {
			UserDTO eventUser = new UserDTO();
			eventUser.setId(userId);
			eventUser.setAge(Utils.getUserAge(user.getDateOfBirth()));
			eventUser.setFirstName(user.getFirstName());
			eventUser.setLastName(user.getLastName());

			eventUsersMap.put(userId, eventUser);
		}
	}

	public void processPlatformEvents(Stream stream, String event) {

		String eventDate = stream.getData().getEventDate();

		EventDTO currentEvent = new EventDTO();
		currentEvent.setEvent(event);
		currentEvent.setId(stream.getId());
		currentEvent.setShowId(stream.getData().getShow().getShowId());
		currentEvent.setEventDate(Utils.parseEventDate(eventDate, stream.getData().getUser().getCountry()));

		platformEventsMap.get(stream.getPlatform()).add(currentEvent);
	}

	public static String findCast(String source) {
		int index = source.indexOf(",");
		if (index == -1) {
			return source;
		} else {
			return source.substring(0, index);
		}
	}

	public void processShowData(Show show) {

		if (!showsMap.containsKey(show.getShowId())) {
			ShowDTO showDTO = new ShowDTO();

			showDTO.setShowId(show.getShowId());
			showDTO.setTitle(show.getTitle());

			if (show.getCast() != null) {
				String firstCast = findCast(show.getCast());
				showDTO.setFirstCast(firstCast);
			}
			showsMap.put(show.getShowId(), showDTO);
		}
	}

	public void sytFlixStartedEventsRatio(String platform, String event) {

		if (platform.equals(StreamPlatform.SYTFLIX.value())) {
			totalSytFlixEvents = totalSytFlixEvents.add(new BigDecimal(1));

			if (event.equals(StreamEvent.STREAM_STARTED.value())) {
				totalSytFlixStartedEvents = totalSytFlixStartedEvents.add(new BigDecimal(1));
			}
		}
	}
}
