package com.thoughtworks.assignment;

import com.thoughtworks.assignment.allocation.AllocationStrategy;
import com.thoughtworks.assignment.allocation.TrackSelectionStrategy;
import com.thoughtworks.assignment.input.InputProcessor;
import com.thoughtworks.assignment.model.Conference;
import com.thoughtworks.assignment.model.Session;
import com.thoughtworks.assignment.model.Talk;
import com.thoughtworks.assignment.model.Track;
import com.thoughtworks.assignment.model.visitor.ReportScheduleVisitor;
import com.thoughtworks.assignment.util.PropertyStore;
import com.thoughtworks.assignment.util.SessionBuilder;
import com.thoughtworks.assignment.validation.TalkValidationStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thoughtworks.assignment.model.SessionType.*;

public class Application {

	public static final String LIGHTNING = "lightning";

	public static void main(String[] args) throws IOException {
		initializeProperties(args);

		String inputFileName = PropertyStore.getInputFile();
		if (inputFileName == null && args.length > 1) inputFileName = args[1];

		List<Talk> talks = processInput(inputFileName);
		validateTalks(talks);

		Conference conference = setupConference();
		conference.allocate(talks);

		ReportScheduleVisitor visitor = new ReportScheduleVisitor();
		conference.accept(visitor);
		System.out.println(visitor.getReport());
	}

	private static Conference setupConference() {
		// TODO Read from properties
		AllocationStrategy allocationStrategy = PropertyStore.getAllocationStrategy();
		TrackSelectionStrategy trackSelectionStrategy = PropertyStore.getTrackSelectionStrategy();
		allocationStrategy.setTrackSelectionStrategy(trackSelectionStrategy);
		Conference conference = new Conference("World Programming Conference", allocationStrategy);
		int trackCount = PropertyStore.getTrackCount();
		SessionBuilder builder = new SessionBuilder();
		for (int i = 1; i <= trackCount; i++) {
			Track track = new Track("Track-" + i);
			Session morning = builder.clear().setType(TALK).setTitle("Morning session").setStartAt("09:00")
					.setDuration(180).build();
			Session lunch = builder.clear().setType(LUNCH).setTitle("Lunch").setStartAt("12:00").setDuration(60).build();
			Session noon = builder.clear().setType(TALK).setTitle("Afternoon session").setStartAt("13:00").setDuration(180)
					.setExtensibility(60).build();
			Session networking = builder.clear().setType(NETWORKING).setTitle("Networking").setStartAt("16:00").setDuration(60)
					.setDelayMargin(60).build();

			morning.setNext(lunch);
			lunch.setNext(noon);
			noon.setNext(networking);

			track.addSession(morning);
			track.addSession(lunch);
			track.addSession(noon);
			track.addSession(networking);

			conference.addTrack(track);
		}

		return conference;
	}

	private static void validateTalks(List<Talk> talks) {
		// TODO externalize in properties
		TalkValidationStrategy[] strategies = new TalkValidationStrategy[]
				{PropertyStore.getTalkValidationStrategy(), PropertyStore.getShortTalkLengthValidationStrategy()};
		for (TalkValidationStrategy strategy : strategies) {
			strategy.validate(talks);
		}
	}

	private static void initializeProperties(String[] args) throws IOException {
		String path = "";
		if (args.length > 0) {
			path = args[0];
		}
		InputStream inputStream;
		if (path != "") inputStream = new FileInputStream(new File(path));
		else inputStream = Application.class.getResourceAsStream("application.properties");

		PropertyStore.initialize(inputStream);
	}

	private static List<Talk> processInput(String inputFileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(new File(inputFileName)));
		ArrayList<Talk> talks = new ArrayList<>();
		Pattern patter = Pattern.compile("[0-9]+");
		InputProcessor processor = () -> {
			int entryNum = 1;
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				int index = line.lastIndexOf(' ');
				String title = line.substring(0, index);
				String strDuration = line.substring(index + 1);
				int duration = 0;
				if (strDuration.equals(LIGHTNING)) {
					duration = PropertyStore.getTalkLengthMin();
				} else {
					Matcher matcher = patter.matcher(strDuration);
					if (matcher.find()) {
						strDuration = matcher.group();
						duration = Integer.parseInt(strDuration);
					}
				}
				talks.add(new Talk(title, duration, entryNum++));
			}
			return talks;
		};

		return processor.processInput();
	}
}
