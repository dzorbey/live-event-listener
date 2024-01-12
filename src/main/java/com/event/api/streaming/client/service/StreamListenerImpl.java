package com.event.api.streaming.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;

import com.event.api.streaming.client.common.Utils;
import com.event.api.streaming.client.config.SecurityConfig;
import com.event.api.streaming.client.enums.StreamPlatform;
import com.event.api.streaming.client.model.Stream;
import com.event.api.streaming.client.model.StreamData;

import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.annotation.PostConstruct;

@Service
public class StreamListenerImpl implements Runnable, StreamListener {
	private static final Logger logger = LoggerFactory.getLogger(StreamListenerImpl.class);

	private static Map<String, Thread> threads = new HashMap<>();

	private String streamPlatform = null;
	private static Integer waitMaxSeconds;
	private static Integer userCountLimit;
	private static Date startDate = null;

	public StreamListenerImpl platform(String Platform, Integer duration, Integer limit) {
		streamPlatform = Platform;
		waitMaxSeconds = duration * 1000;
		userCountLimit = limit;
		return this;
	}

	
	public static void initilize() {
		Utils.durations.clear();
		Utils.eventsMap.clear();

		Utils.outputStreams = new HashMap<String, PipedOutputStream>();
		Utils.inputStreams = new HashMap<String, PipedInputStream>();
		Utils.eventCounter = 0;

		Arrays.asList(StreamPlatform.values()).stream().forEach((Platform) -> {

			LinkedHashSet<Stream> streamSet = new LinkedHashSet<>();
			Collections.synchronizedSet(streamSet);

			Utils.eventsMap.put(Platform.value(), streamSet);
			Utils.inputStreams.put(Platform.value(), new PipedInputStream());
			Utils.outputStreams.put(Platform.value(), new PipedOutputStream());
		});
	}
	
	
	@PostConstruct
	public void start() {
		initilize();
		startThreads(20, 3);
	}
	
	
	public void restart(Integer duration, Integer userCountLimit) {
		initilize();
		startThreads(duration, userCountLimit);
	}
	
	
	
	public static void startThreads(Integer duration, Integer userCountLimit) {
		
		Arrays.asList(StreamPlatform.values()).stream().forEach((Platform) -> {
			
			Thread thread = new Thread(new StreamListenerImpl().platform(Platform.value(), duration, userCountLimit));
			threads.put(Platform.value(), thread);
			thread.start();
		});
	}
	
	

	public void subscribeStream(String platform) throws IOException, InterruptedException {
		startDate = new Date();
		
		try {
			Utils.inputStreams.get(platform).connect(Utils.outputStreams.get(platform));						
		}catch(Exception e) {
			logger.info("Exception:" + e.getMessage());
		}

		Flux<DataBuffer> messageBody = SecurityConfig.webClient.get().uri("/" + platform).accept(MediaType.ALL)
				.header(org.apache.http.HttpHeaders.AUTHORIZATION, SecurityConfig.getAuthenticatonHeader())
				.exchangeToFlux(clientResponse -> {
					return clientResponse.body(BodyExtractors.toDataBuffers());
				}).onErrorContinue(Exception.class, (error, obj) -> logger.info("error:{}, obj:{}", error, obj))
				.doFinally(s -> {
					try {
						closeStreams(platform);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

		DataBufferUtils.write(messageBody, Utils.outputStreams.get(platform)).subscribe(obj -> {

			try {
				boolean condition = (Utils.eventCounter >= userCountLimit);
				checkElapsedTime(platform, condition);

				String stream = readEvent(Utils.inputStreams.get(platform));
				populateEvents(platform, stream);

				checkElapsedTime(platform, condition);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		Utils.inputStreams.get(platform).readAllBytes();
	}

	public String readEvent(InputStream stream) throws IOException {
		StringBuffer buffer = new StringBuffer();

		byte[] availableBytes = new byte[stream.available()];
		stream.read(availableBytes, 0, availableBytes.length);

		buffer.append(new String(availableBytes));
		return String.valueOf(buffer);
	}

	private static void closeStreams(String platform) throws IOException {
		Utils.outputStreams.get(platform).close();
		Utils.inputStreams.get(platform).close();
	}
	
	public void populateEvents(String platform, String rawStream) {

		try {
			String uuid = Utils.matchUUID(rawStream);
			String body = Utils.matchPrefix("data:", rawStream);
			String event = Utils.matchPrefix("event:", rawStream);

			if (uuid != null && event != null) {
				try {
					Stream stream = new Stream();
					StreamData streamData = Utils.gson.fromJson(body, StreamData.class);
					stream.id(uuid).event(event).data(streamData).platform(platform);

					if (streamData.getUser().getFirstName().equals(Utils.SYTAC)) {
						Utils.eventCounter++;
					}
					Utils.getStreams().get(platform).add(stream);
				} catch (Exception e) {
					logger.info("mallformed object:{}", e.getMessage());
				}
			}
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			logger.info("out of bounds:{}", rawStream);
		}
	}

	private static void checkElapsedTime(String platform, Boolean condition) throws IOException {
		Long currentMilis = Calendar.getInstance().getTimeInMillis();
		Long elapsedTime = ((currentMilis - startDate.getTime()));
		if ((elapsedTime >= waitMaxSeconds) || condition) {
			Utils.durations.put(platform, elapsedTime.toString());
			closeStreams(platform);
			threads.get(platform).interrupt();
		}
	}

	@Override
	public void run() {
		try {
			subscribeStream(streamPlatform);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}