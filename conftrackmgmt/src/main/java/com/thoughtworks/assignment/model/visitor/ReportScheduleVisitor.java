package com.thoughtworks.assignment.model.visitor;

import com.thoughtworks.assignment.model.Conference;
import com.thoughtworks.assignment.model.Session;
import com.thoughtworks.assignment.model.TalkAssignment;
import com.thoughtworks.assignment.model.Track;
import com.thoughtworks.assignment.util.PropertyStore;

import java.time.format.DateTimeFormatter;

import static java.lang.System.lineSeparator;

public class ReportScheduleVisitor implements ConferenceVisitor {
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(PropertyStore.getTimeFormat());
	private StringBuilder builder = new StringBuilder();
	private boolean firstTrack = true;

	@Override
	public void visitConference(Conference conference) {
		builder.append(conference.getTitle()).append(lineSeparator());
		for (Track track : conference.getTracks()) track.accept(this);
	}

	@Override
	public void visitTrack(Track track) {
		if (!firstTrack) {
			builder.append(lineSeparator());
		}
		firstTrack = false;
		builder.append(track.getTitle()).append(lineSeparator());

		for (Session session : track.getSessions()) session.accept(this);
	}

	@Override
	public void visitSession(Session session) {
		switch (session.getType()) {
			case LUNCH:
			case NETWORKING:
				builder.append(session.getStartsAt()).append(" ").append(session.getTitle()).append(lineSeparator());
		}
		for (TalkAssignment talk : session.getTalks()) {
			talk.accept(this);
		}
	}

	@Override
	public void visitTalk(TalkAssignment talk) {
		builder.append(talk.getStartsAt().format(TIME_FORMATTER)).append(" ").append(talk.getTitle())
				.append(" ").append(talk.getDuration()).append(lineSeparator());
	}

	public String getReport() {
		return builder.toString();
	}
}
