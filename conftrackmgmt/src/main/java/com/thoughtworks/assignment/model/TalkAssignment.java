package com.thoughtworks.assignment.model;

import com.thoughtworks.assignment.model.visitor.ConferenceVisitor;

import java.time.LocalTime;

public class TalkAssignment {
	private Talk talk;
	private LocalTime startsAt;

	public TalkAssignment(Talk talk, LocalTime startsAt) {
		this.talk = talk;
		this.startsAt = startsAt;
	}

	public void accept(ConferenceVisitor visitor) {
		visitor.visitTalk(this);
	}


	public LocalTime getStartsAt() {
		return startsAt;
	}

	public LocalTime getEndsAt() {
		return startsAt.plusMinutes(talk.getDuration());
	}

	public Talk getTalk() {
		return talk;
	}

	public long getDuration() {
		return getTalk().getDuration();
	}

	public String getTitle() {
		return getTalk().getTitle();
	}
}
