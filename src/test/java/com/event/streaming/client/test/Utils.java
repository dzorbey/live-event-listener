package com.event.streaming.client.test;

import java.util.UUID;

import com.event.api.streaming.client.model.Show;
import com.event.api.streaming.client.model.Stream;
import com.event.api.streaming.client.model.StreamData;
import com.event.api.streaming.client.model.User;

public class Utils {
	
	public static Stream createStream(String platform, Long userId, String showId, Integer releaseYear, String event) {
		Stream stream = new Stream();
		
		stream.data(createStreamData(platform, userId, showId, releaseYear));
		stream.id(UUID.randomUUID().toString());
		stream.event(event);
		stream.platform(platform);
		
		return stream;
	}
	
	public static StreamData createStreamData(String platform, Long userId, String showId, Integer releaseYear) {	
		StreamData streamData = new StreamData();

		User user = new User();
		user.setCountry("NL");
		user.setId(userId);
		user.setDateOfBirth("31/10/1970");
		streamData.setUser(user);

		Show show = new Show();
		show.setReleaseYear(releaseYear);
		show.setShowId(showId);
		show.setCast("Delikan, Gokyildiz");
		show.setPlatform(platform);

		streamData.setShow(show);
		streamData.setEventDate("27-02-2023 03:20:17.111");
		
		return streamData;
	}
}
