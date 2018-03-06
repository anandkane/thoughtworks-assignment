package com.thoughtworks.assignment.input;

import com.thoughtworks.assignment.model.Talk;

import java.util.ArrayList;

/**
 * Defines logic to translate the input into list of {@link Talk}s. Implement this interface to process custom input
 * formats.
 */
@FunctionalInterface
public interface InputProcessor {
	ArrayList<Talk> processInput();
}
