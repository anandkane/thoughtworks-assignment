package com.thoughtworks.assignment.validation;

import com.thoughtworks.assignment.model.Talk;
import com.thoughtworks.assignment.util.PropertyStore;

import java.util.List;

public class ExtendDurationStrategy implements TalkValidationStrategy {
	private final int minDuration;

	public ExtendDurationStrategy() {
		minDuration = PropertyStore.getTalkLengthMin();
	}

	@Override
	public void validate(List<Talk> talks) {
		talks.forEach(talk -> {
			int duration = talk.getDuration();
			if (duration < minDuration) {
				talk.extendBy(minDuration - duration);
			}
		});
	}
}
