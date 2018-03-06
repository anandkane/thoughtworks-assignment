package com.thoughtworks.assignment.util;

import com.thoughtworks.assignment.allocation.AllocationStrategy;
import com.thoughtworks.assignment.allocation.TrackSelectionStrategy;
import com.thoughtworks.assignment.validation.TalkValidationStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyStore {
	private static Properties properties = new Properties();
	private static String inputFile;
	private static int talkLengthMin;
	private static int talkLengthMax;
	private static AllocationStrategy allocationStrategy;
	private static TrackSelectionStrategy trackSelectionStrategy;
	private static TalkValidationStrategy talkValidationStrategy;
	private static TalkValidationStrategy shortTalkLengthValidationStrategy;
	private static String timeFormat;
	private static int trackCount;

	private PropertyStore() {
	}

	public static synchronized void initialize(InputStream stream) throws IOException {
		properties.load(stream);
		inputFile = properties.getProperty("input.filename");
		timeFormat = properties.getProperty("format.time.pattern", "HH:mm");
		talkLengthMin = Integer.parseInt(properties.getProperty("talk.length.min", "5"));
		talkLengthMax = Integer.parseInt(properties.getProperty("talk.length.max", "240"));
		trackCount = Integer.parseInt(properties.getProperty("conf.track.count", "2"));

		String className;
		try {
			className = properties.getProperty("allocation.strategy",
					"com.thoughtworks.assignment.allocation.LongestFirstStrategy");
			allocationStrategy = (AllocationStrategy) Class.forName(className).newInstance();

			className = properties.getProperty("allocation.trackselection.strategy",
					"com.thoughtworks.assignment.allocation.ExhaustiveStrategy");
			trackSelectionStrategy = (TrackSelectionStrategy) Class.forName(className).newInstance();

			className = properties.getProperty("validation.strategy",
					"com.thoughtworks.assignment.validation.LogAndIgnoreValidationStrategy");
			talkValidationStrategy = (TalkValidationStrategy) Class.forName(className).newInstance();

			className = properties.getProperty("validation.length.short.strategy",
					"com.thoughtworks.assignment.validation.ExtendDurationStrategy");
			shortTalkLengthValidationStrategy = (TalkValidationStrategy) Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException("Failed to read properties.", e);
		}
	}

	public static String getInputFile() {
		return inputFile;
	}

	public static int getTalkLengthMin() {
		return talkLengthMin;
	}

	public static int getTalkLengthMax() {
		return talkLengthMax;
	}

	public static AllocationStrategy getAllocationStrategy() {
		return allocationStrategy;
	}

	public static TrackSelectionStrategy getTrackSelectionStrategy() {
		return trackSelectionStrategy;
	}

	public static TalkValidationStrategy getTalkValidationStrategy() {
		return talkValidationStrategy;
	}

	public static TalkValidationStrategy getShortTalkLengthValidationStrategy() {
		return shortTalkLengthValidationStrategy;
	}

	public static String getTimeFormat() {
		return timeFormat;
	}

	public static int getTrackCount() {
		return trackCount;
	}
}
