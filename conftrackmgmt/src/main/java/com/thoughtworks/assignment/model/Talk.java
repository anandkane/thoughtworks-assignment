package com.thoughtworks.assignment.model;

import com.thoughtworks.assignment.model.visitor.ConferenceVisitor;

public class Talk extends AbstractModel implements Comparable<Talk> {
	private int duration;
	private int entryNumber;

	public Talk(String title, int duration, int entryNumber) {
		super(title);
		this.duration = duration;
		this.entryNumber = entryNumber;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("title='").append(title).append('\'').append(", duration=").append(duration).append('}');
		return sb.toString();
	}

	@Override
	public int compareTo(Talk that) {
		if (that == null) return -1;

		int compare = Integer.compare(this.duration, that.duration);
		return compare == 0 ? Integer.compare(this.entryNumber, that.entryNumber) : compare;
	}

	public int getDuration() {
		return duration;
	}

	public void extendBy(int duration) {
		this.duration += duration;
	}

	@Override
	public void accept(ConferenceVisitor visitor) {

	}
}
