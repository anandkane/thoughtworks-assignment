package com.thoughtworks.assignment.model;

import com.thoughtworks.assignment.model.visitor.ConferenceVisitor;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Session extends AbstractModel {
	private SessionType type;
	private LocalTime startsAt;
	private LocalTime endsAt;
	private Duration duration;
	private Duration durationUsed;
	private List<TalkAssignment> talks = new ArrayList<>();
	private int extensibility;
	private int delayMargin;
	private Session next;

	public Session(SessionType type, String title, String startsAt, int duration) {
		super(title);
		this.type = type;
		this.startsAt = LocalTime.parse(startsAt, DateTimeFormatter.ISO_TIME);
		this.duration = Duration.ofMinutes(duration);
		this.endsAt = this.startsAt.plusMinutes(duration);
		this.durationUsed = Duration.ofMinutes(0);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("type=").append(type);
		sb.append(", title=").append(getTitle());
		sb.append(", startsAt=").append(startsAt);
		sb.append(", endsAt=").append(endsAt);
		sb.append(", duration=").append(duration);
		sb.append(", durationUsed=").append(durationUsed);
		sb.append(", extensibility=").append(extensibility);
		sb.append(", delayMargin=").append(delayMargin);
		sb.append('}');
		return sb.toString();
	}

	public boolean allocate(Talk talk) {
		switch (type) {
			case LUNCH:
			case NETWORKING:
				return false;
		}

		Duration remaining = getDuration().minus(getDurationUsed());
		Duration diff = remaining.minusMinutes(talk.getDuration());
		if (!diff.isNegative()) {
			addTalkAssignment(talk);
			return true;
		}

		boolean extend = extend((int) diff.toMinutes() * -1);
		if (extend) {
			addTalkAssignment(talk);
		}
		return extend;
	}

	private void addTalkAssignment(Talk talk) {
		LocalTime talkAt = startsAt.plusMinutes(durationUsed.toMinutes());
		talks.add(new TalkAssignment(talk, talkAt));
		durationUsed = durationUsed.plusMinutes(talk.getDuration());
	}

	public boolean extend(int extendBy) {
		boolean extend = isExtensible(extendBy) && (getNext() == null || getNext().delay(extendBy));
		if (extend) {
			this.duration = this.duration.plusMinutes(extendBy);
			this.endsAt = this.endsAt.plusMinutes(extendBy);
		}
		return extend;
	}

	public boolean delay(int delayBy) {
		boolean can = delayMargin >= delayBy;
		Session next = getNext();
		if (can && next != null) {
			LocalTime time = next.getStartsAt().minusMinutes(delayBy);
			if (time.compareTo(getEndsAt()) >= 0) can = true;
			else can = next.delay(delayBy);
		}

		if (can) {
			startsAt = startsAt.plusMinutes(delayBy);
			endsAt = endsAt.plusMinutes(delayBy);
			delayMargin -= delayBy;
		}

		return can;
	}

	@Override
	public void accept(ConferenceVisitor visitor) {
		visitor.visitSession(this);
	}

	public boolean isExtensible(int minutes) {
		return extensibility >= minutes;
	}

	public void setExtensibility(int extensibility) {
		this.extensibility = extensibility;
	}

	public void setDelayMargin(int delayMargin) {
		this.delayMargin = delayMargin;
	}

	public Session getNext() {
		return next;
	}

	public Duration getDuration() {
		return duration;
	}

	public Duration getDurationUsed() {
		return durationUsed;
	}

	public Duration getRemainingDuration() {
		return duration.minus(durationUsed);
	}

	public int getDelayMargin() {
		return delayMargin;
	}

	public void setNext(Session next) {
		this.next = next;
	}

	public LocalTime getStartsAt() {
		return startsAt;
	}

	public LocalTime getEndsAt() {
		return endsAt;
	}

	public List<TalkAssignment> getTalks() {
		return Collections.unmodifiableList(talks);
	}

	public SessionType getType() {
		return type;
	}
}
