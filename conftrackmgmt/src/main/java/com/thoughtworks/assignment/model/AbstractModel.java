package com.thoughtworks.assignment.model;

import com.thoughtworks.assignment.model.visitor.ConferenceVisitor;

public abstract class AbstractModel {
	protected String title;

	public AbstractModel(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	public abstract void accept(ConferenceVisitor visitor);
}
