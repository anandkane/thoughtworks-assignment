package com.thoughtworks.assignment.model.visitor;

import com.thoughtworks.assignment.model.*;

/**
 * A visitor pattern implementation to allow externalizing operations to be performed on various conference components.
 */
public interface ConferenceVisitor {
	void visitConference(Conference conference);

	void visitTrack(Track track);

	void visitSession(Session session);

	void visitTalk(TalkAssignment talk);
}
