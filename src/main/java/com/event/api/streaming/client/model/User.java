package com.event.api.streaming.client.model;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id = null;
	private String country = null;
	private String gender = null;
	private String email = null;

	@SerializedName("date_of_birth")
	private String dateOfBirth = null;

	@SerializedName("first_name")
	private String firstName = null;

	@SerializedName("last_name")
	private String lastName = null;

	@SerializedName("ip_address")
	private String ipAddress = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}