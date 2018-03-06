package com.thoughtworks.assignment.util;

import com.thoughtworks.assignment.model.Session;
import com.thoughtworks.assignment.model.SessionType;

public class SessionBuilder {
	private String title;
	private String startAt;
	private int duration;
	private int extensibility;
	private int delayMargin;
	private Session next;
	private SessionType type;

	public SessionBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public SessionBuilder setStartAt(String startAt) {
		this.startAt = startAt;
		return this;
	}

	public SessionBuilder setDuration(int duration) {
		this.duration = duration;
		return this;
	}

	public SessionBuilder setExtensibility(int extensibility) {
		this.extensibility = extensibility;
		return this;
	}

	public SessionBuilder setDelayMargin(int delayMargin) {
		this.delayMargin = delayMargin;
		return this;
	}

	public SessionBuilder setNext(Session next) {
		this.next = next;
		return this;
	}

	public SessionBuilder setType(SessionType type) {
		this.type = type;
		return this;
	}

	public Session build() {
		Session session = new Session(type, title, startAt, duration);
		session.setExtensibility(extensibility);
		session.setDelayMargin(delayMargin);
		session.setNext(next);
		return session;
	}

	public SessionBuilder clear() {
		setTitle("");
		setDuration(0);
		setExtensibility(0);
		setDelayMargin(0);
		setStartAt("");
		setNext(null);

		return this;
	}
}
