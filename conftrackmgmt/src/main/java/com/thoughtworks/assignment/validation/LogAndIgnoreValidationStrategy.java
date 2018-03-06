package com.thoughtworks.assignment.validation;

import com.thoughtworks.assignment.model.Talk;
import com.thoughtworks.assignment.util.PropertyStore;

import java.util.Iterator;
import java.util.List;

public class LogAndIgnoreValidationStrategy implements TalkValidationStrategy {
	private final int maxDuration;

	public LogAndIgnoreValidationStrategy() {
		maxDuration = PropertyStore.getTalkLengthMax();
	}

	@Override
	public void validate(List<Talk> talks) {
		Iterator<Talk> iterator = talks.iterator();
		while (iterator.hasNext()) {
			Talk next = iterator.next();
			if (next.getDuration() > maxDuration) {
				iterator.remove();
			}
		}
	}
}
