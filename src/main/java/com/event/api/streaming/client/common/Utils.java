package com.event.api.streaming.client.common;

import com.event.api.streaming.client.enums.StreamPlatform;
import com.event.api.streaming.client.model.Stream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.*;
import com.ibm.icu.util.TimeZone;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.Type;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	
	public static final String UUID_REGEX = "\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}";
	public static final String SYTAC = "Sytac";

	public static final MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
	
	public static final Pattern pairRegex = Pattern.compile(UUID_REGEX);
	public static Integer eventCounter = 0;

	public static final ZoneId localZoneId = Calendar.getInstance().getTimeZone().toZoneId();
	
	public static final LocalDate currentDate = LocalDate.ofInstant(Calendar.getInstance().toInstant(), ZoneId.systemDefault());
	
	public static final DateTimeFormatter formatterWithZone = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS z");
	public static final DateTimeFormatter basicFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public static final DateFormat simplDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
	
	public static ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

	public static Map<String, PipedInputStream> inputStreams = new HashMap<String, PipedInputStream>();
	public static Map<String, PipedOutputStream> outputStreams = new HashMap<String, PipedOutputStream>();
	
	public static Map<String, String> durations = new ConcurrentHashMap<String, String>();
	
	public static Map<String, LinkedHashSet<Stream>> eventsMap = new ConcurrentHashMap<String, LinkedHashSet<Stream>>();
	
	static {
		Arrays.asList(StreamPlatform.values()).stream().forEach((platform) -> {
			
			LinkedHashSet<Stream> streamSet = new LinkedHashSet<>();
			Collections.synchronizedSet(streamSet);

			eventsMap.put(platform.value(), streamSet);
			inputStreams.put(platform.value(), new PipedInputStream());
			outputStreams.put(platform.value(), new PipedOutputStream());
		});
	}

	public static LocalDate toLocalDate(String eventDate, DateTimeFormatter formatter) {
		return LocalDate.parse(eventDate, formatter);
	}

	public static Map<String, LinkedHashSet<Stream>> getStreams() {
		return eventsMap;
	}

	public static Gson gson = new GsonBuilder()
			.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
				@Override
				public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
						throws JsonParseException {
					return LocalDateTime.parse(json.getAsString());
				}
			}).registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
				@Override
				public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
						throws JsonParseException {
					return LocalDate.parse(json.getAsString());
				}
			}).create();

	
	public static String matchUUID(String source) {
		Pattern pairRegex = Pattern.compile(UUID_REGEX);
	    Matcher matcher = pairRegex.matcher(source);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return null;
	}
	
	public static String matchPrefix(String prefix, String source) {
	
		String regex = ".*?" + prefix + "(.*)";
		Pattern pairRegex = Pattern.compile(regex);
	    Matcher matcher = pairRegex.matcher(source);
	    while (matcher.find()) {
	        return matcher.group(1);
	    }
		return null;
	}
	
	
	public static String parseEventDate(String eventDate, String country) {
		
		String[] ids = TimeZone.getAvailableIDs(country);
		for(String id : ids) {
			try {
				ZonedDateTime zonedDateTime = ZonedDateTime.parse(eventDate + " " + ZoneId.of(id), formatterWithZone);
				Date eventLocalTime = Date.from(zonedDateTime.toInstant());
				
				return (simplDateFormat.format(eventLocalTime) + " " + localZoneId);
			}catch(Exception e) {
				//logger.error("unknown:" + id + ", country: " + country);
			}
		}
		return eventDate;
	}
	
	public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }
	
	public static int getUserAge(String birthDate) {
		return currentDate.getYear() - toLocalDate(birthDate, basicFormatter).getYear();
	}
}
