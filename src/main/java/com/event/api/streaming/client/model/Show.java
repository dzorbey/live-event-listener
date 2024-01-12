package com.event.api.streaming.client.model;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class Show implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cast = null;
	private String country = null;
	private String description = null;
	private String director = null;
	private String duration = null;
	private String title = null;
	private String type = null;
	private String platform = null;
	private String rating = null;

	@SerializedName("show_id")
	private String showId = null;

	@SerializedName("date_added")
	private String dateAdded = null;

	@SerializedName("listed_in")
	private String listedIn = null;

	@SerializedName("release_year")
	private Integer releaseYear = null;

	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getListedIn() {
		return listedIn;
	}

	public void setListedIn(String listedIn) {
		this.listedIn = listedIn;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
}