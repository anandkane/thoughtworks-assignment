package com.thoughtworks.assignment.validation;

import com.thoughtworks.assignment.model.Talk;

import java.util.List;

public interface TalkValidationStrategy {
	void validate(List<Talk> talks);
}
