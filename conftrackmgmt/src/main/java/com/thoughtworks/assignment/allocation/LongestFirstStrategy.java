package com.thoughtworks.assignment.allocation;

import com.thoughtworks.assignment.model.Talk;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link AllocationStrategy} that allocates slots to {@link Talk}s in the decreasing order of their
 * durations. In case of {@link Talk}s with equal durations, the one that was entered first is allocated slots first.
 */
public class LongestFirstStrategy implements AllocationStrategy {
	private TrackSelectionStrategy trackSelectionStrategy;

	@Override
	public void setTrackSelectionStrategy(TrackSelectionStrategy trackSelectionStrategy) {
		this.trackSelectionStrategy = trackSelectionStrategy;
	}

	@Override
	public TrackSelectionStrategy getTrackSelectionStrategy() {
		return trackSelectionStrategy;
	}

	@Override
	public void allocate(List<Talk> talks) {
		Collections.sort(talks, Collections.reverseOrder());
		talks.forEach(talk -> trackSelectionStrategy.allocate(talk));
	}
}
