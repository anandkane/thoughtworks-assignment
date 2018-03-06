package com.thoughtworks.assignment.model;

import com.thoughtworks.assignment.model.visitor.ConferenceVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Track extends AbstractModel {
	private List<Session> sessions = new ArrayList<>();

	public Track(String title) {
		super(title);
	}

	public boolean addSession(Session session) {
		sessions.add(session);
		return true;
	}

	@Override
	public void accept(ConferenceVisitor visitor) {
		visitor.visitTrack(this);
	}

	public List<Session> getSessions() {
		return Collections.unmodifiableList(sessions);
	}

	public boolean allocate(Talk talk) {
		for (Session session : sessions) {
			if (session.allocate(talk)) return true;
		}

		return false;
	}
}
