package com.event.api.streaming.client.dto;

import java.util.List;

public class StreamResponse {

	List<DurationDTO> durations;

	public List<DurationDTO> getDurations() {
		return durations;
	}

	public void setDurations(List<DurationDTO> durations) {
		this.durations = durations;
	}
}
