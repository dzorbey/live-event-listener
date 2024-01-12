package com.event.api.streaming.client.dto;

import java.io.Serializable;
import java.util.Set;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id = null;
	private Integer age = null;
	private String firstName = null;
	private String lastName = null;

	private Set<String> executedEventTypes = null;
	private Set<EventDTO> executedEvents = null;

	public Set<String> getExecutedEventTypes() {
		return executedEventTypes;
	}

	public void setExecutedEventTypes(Set<String> executedEventTypes) {
		this.executedEventTypes = executedEventTypes;
	}

	public Set<EventDTO> getExecutedEvents() {
		return executedEvents;
	}

	public void setExecutedEvents(Set<EventDTO> executedEvents) {
		this.executedEvents = executedEvents;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}