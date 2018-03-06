package com.thoughtworks.assignment.allocation;

import com.thoughtworks.assignment.model.Talk;
import com.thoughtworks.assignment.model.Track;

import java.util.List;

/**
 * Defines a strategy used to decide the manner in which {@link Track}s are picked for slot allocations. For example,
 * {@link ExhaustiveStrategy} attempts to use a {@link Track} to it's capacity before picking another. Refer
 * {@link ExhaustiveStrategy} for more details. Another possible distribution strategy can be Round Robin Distribution.
 *
 * Implement this interface to provide custom {@link TrackSelectionStrategy}.
 */
public interface TrackSelectionStrategy {
	void initialize(List<Track> tracks);

	boolean allocate(Talk talk);
}
