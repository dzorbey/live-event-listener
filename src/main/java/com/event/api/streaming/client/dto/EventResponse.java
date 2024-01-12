package com.event.api.streaming.client.dto;

import java.math.BigDecimal;
import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class EventResponse {

	@ApiModelProperty(example = "[accepted]")
	private String result = null;

	@ApiModelProperty
	private int userEventCount;

	@ApiModelProperty
	private int distinctUserCount;

	@ApiModelProperty
	private int distinctShowCount;

	@ApiModelProperty
	private int eventCount;
	
	@ApiModelProperty
	private BigDecimal sytFlixStartedEventsRatio;

	@ApiModelProperty
	private List<DurationDTO> durations = null;

	@ApiModelProperty
	private List<ShowFrequencyDTO> showsOnwards2020 = null;

	@ApiModelProperty
	private List<UserDTO> users = null;

	@ApiModelProperty
	private List<PlatformEventDTO> platformEvents = null;

	@ApiModelProperty
	private List<ShowDTO> shows = null;

	@ApiModelProperty
	private List<SuccessStreamingEventDTO> successfullStreamingEventsPerUser = null;
	



	public BigDecimal getSytFlixStartedEventsRatio() {
		return sytFlixStartedEventsRatio;
	}

	public void setSytFlixStartedEventsRatio(BigDecimal sytFlixStartedEventsRatio) {
		this.sytFlixStartedEventsRatio = sytFlixStartedEventsRatio;
	}

	public int getDistinctShowCount() {
		return distinctShowCount;
	}

	public void setDistinctShowCount(int distinctShowCount) {
		this.distinctShowCount = distinctShowCount;
	}

	public List<ShowDTO> getShows() {
		return shows;
	}

	public void setShows(List<ShowDTO> shows) {
		this.shows = shows;
	}


	public List<PlatformEventDTO> getPlatformEvents() {
		return platformEvents;
	}

	public void setPlatformEvents(List<PlatformEventDTO> platformEvents) {
		this.platformEvents = platformEvents;
	}

	public List<SuccessStreamingEventDTO> getSuccessfullStreamingEventsPerUser() {
		return successfullStreamingEventsPerUser;
	}

	public void setSuccessfullStreamingEventsPerUser(List<SuccessStreamingEventDTO> successfullStreamingEventsPerUser) {
		this.successfullStreamingEventsPerUser = successfullStreamingEventsPerUser;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getUserEventCount() {
		return userEventCount;
	}

	public void setUserEventCount(int userEventCount) {
		this.userEventCount = userEventCount;
	}

	public int getDistinctUserCount() {
		return distinctUserCount;
	}

	public void setDistinctUserCount(int distinctUserCount) {
		this.distinctUserCount = distinctUserCount;
	}

	public int getEventCount() {
		return eventCount;
	}

	public void setEventCount(int eventCount) {
		this.eventCount = eventCount;
	}

	public List<DurationDTO> getDurations() {
		return durations;
	}

	public void setDurations(List<DurationDTO> durations) {
		this.durations = durations;
	}

	public List<ShowFrequencyDTO> getShowsOnwards2020() {
		return showsOnwards2020;
	}

	public void setShowsOnwards2020(List<ShowFrequencyDTO> showsOnwards2020) {
		this.showsOnwards2020 = showsOnwards2020;
	}

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}
}