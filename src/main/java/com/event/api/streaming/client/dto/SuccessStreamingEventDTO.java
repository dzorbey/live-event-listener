package com.event.api.streaming.client.dto;

import java.util.Set;

public class SuccessStreamingEventDTO {

	private Long userId;
	private Integer successCount;
	private Set<SuccessfullUserEventDTO> successFullEvents;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Set<SuccessfullUserEventDTO> getSuccessFullEvents() {
		return successFullEvents;
	}

	public void setSuccessFullEvents(Set<SuccessfullUserEventDTO> successFullEvents) {
		this.successFullEvents = successFullEvents;
	}

}
