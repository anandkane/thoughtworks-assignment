package com.thoughtworks.assignment.allocation;

import com.thoughtworks.assignment.model.Talk;
import com.thoughtworks.assignment.model.Track;

import java.util.List;

/**
 * Implementation of {@link TrackSelectionStrategy} that attempts to exhaust a {@link Track}'s capacity before
 * allocating slots from another {@link Track}. However, this behaviour is not guaranteed and is controlled in part by
 * {@link AllocationStrategy} used for allocation. For example, the {@link LongestFirstStrategy} implementation of
 * {@link AllocationStrategy} attempts to allocate slots to longest {@link Talk}s first. In the event when there are
 * slots available in current {@link Track}, but not long enough accomodate a talk, next {@link Track} will be used for
 * allocation. At later stage when a {@link Talk} with duration lesser than or equal to the current {@link Track}'s
 * remaining duration comes in, it will be allocated time from the current {@link Track}.
 */
public class ExhaustiveStrategy implements TrackSelectionStrategy {
	private List<Track> tracks;

	@Override
	public void initialize(List<Track> tracks) {
		this.tracks = tracks;
	}

	@Override
	public boolean allocate(Talk talk) {
		for (Track track : tracks) {
			if (track.allocate(talk)) return true;
		}

		return false;
	}
}
