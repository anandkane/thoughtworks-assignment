package com.thoughtworks.assignment.model;

import com.thoughtworks.assignment.allocation.AllocationStrategy;
import com.thoughtworks.assignment.model.visitor.ConferenceVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Conference extends AbstractModel {
	private List<Track> tracks = new ArrayList<>();
	private AllocationStrategy allocationStrategy;

	public Conference(String title, AllocationStrategy allocationStrategy) {
		super(title);
		this.allocationStrategy = allocationStrategy;
		this.allocationStrategy.getTrackSelectionStrategy().initialize(getTracks());
	}

	public boolean addTrack(Track track) {
		tracks.add(track);
		return true;
	}

	public void allocate(List<Talk> talks) {
		allocationStrategy.allocate(talks);
	}

	@Override
	public void accept(ConferenceVisitor visitor) {
		visitor.visitConference(this);
	}

	public List<Track> getTracks() {
		return Collections.unmodifiableList(tracks);
	}
}
