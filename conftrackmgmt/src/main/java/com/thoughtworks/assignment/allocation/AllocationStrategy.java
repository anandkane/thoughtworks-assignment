package com.thoughtworks.assignment.allocation;

import com.thoughtworks.assignment.model.Talk;

import java.util.List;

/**
 * Defines the strategy used to allocate slots for tasks. For example, the default implementation
 * {@link LongestFirstStrategy} allocates slots in the decreasing order of {@link Talk} durations. Another possible
 * strategy is to allocate slots based on First Come First Served principle.
 *
 * Implement this interface to provide custom {@link AllocationStrategy}.
 */
public interface AllocationStrategy {

	void setTrackSelectionStrategy(TrackSelectionStrategy trackSelectionStrategy);

	TrackSelectionStrategy getTrackSelectionStrategy();

	void allocate(List<Talk> talks);
}
