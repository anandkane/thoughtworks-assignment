package com.thoughtworks.assignment.model;

import com.thoughtworks.assignment.util.SessionBuilder;
import org.junit.Test;

import java.time.LocalTime;

import static com.thoughtworks.assignment.model.SessionType.TALK;
import static org.junit.Assert.assertEquals;

public class SessionExtensibilityTest {

	public static final LocalTime startsAt = LocalTime.parse("13:00");

	@Test
	public void inextensible() {
		SessionBuilder builder = new SessionBuilder();
		Session session = builder.setType(TALK).setTitle("Session1").setStartAt("13:00").setDuration(180).build();
		session.allocate(new Talk("", 150, 1));
		boolean allocate = session.allocate(new Talk("", 60, 3));
		validateSession(session, allocate, false, startsAt, startsAt.plusMinutes(180), 180, 30);
	}

	@Test
	public void nextNull() {
		SessionBuilder builder = new SessionBuilder();
		Session session = builder.setType(TALK).setTitle("Session1").setStartAt("13:00").setDuration(180).setExtensibility(60).build();

		assertEquals(LocalTime.parse("16:00"), session.getEndsAt());
		session.allocate(new Talk("", 150, 1));
		boolean allocate = session.allocate(new Talk("", 60, 3));
		validateSession(session, allocate, true, startsAt, startsAt.plusMinutes(210), 210, 0);
	}

	@Test
	public void extendMoreThanAllowed() {
		SessionBuilder builder = new SessionBuilder();
		Session session = builder.setType(TALK).setTitle("Session1").setStartAt("13:00").setDuration(180).setExtensibility(60).build();

		assertEquals(LocalTime.parse("16:00"), session.getEndsAt());
		session.allocate(new Talk("", 150, 1));
		boolean allocate = session.allocate(new Talk("", 91, 3));
		validateSession(session, allocate, false, startsAt, startsAt.plusMinutes(180), 180, 30);

	}

	@Test
	public void nextDelayable() {
		SessionBuilder builder = new SessionBuilder();
		Session session = builder.setType(TALK).setTitle("Session1").setStartAt("13:00").setDuration(180).setExtensibility(60).build();

		Session next = builder.clear().setTitle("Session2").setStartAt("16:00").setDuration(60).setDelayMargin(60).build();
		session.setNext(next);

		session.allocate(new Talk("", 150, 1));
		session.allocate(new Talk("", 60, 3));

		assertEquals(LocalTime.parse("13:00"), session.getStartsAt());
		assertEquals(LocalTime.parse("16:30"), session.getEndsAt());

		assertEquals(210, session.getDuration().toMinutes());
		assertEquals(0, session.getRemainingDuration().toMinutes());

		assertEquals(LocalTime.parse("16:30"), next.getStartsAt());
		assertEquals(LocalTime.parse("17:30"), next.getEndsAt());

		assertEquals(60, next.getDuration().toMinutes());
		assertEquals(60, next.getRemainingDuration().toMinutes());
		assertEquals(30, next.getDelayMargin());

	}

	@Test
	public void exactExtensibility() {
		SessionBuilder builder = new SessionBuilder();
		Session session = builder.setType(TALK).setTitle("Session1").setStartAt("13:00").setDuration(180).setExtensibility(60).build();

		Session next = builder.clear().setTitle("Session2").setStartAt("16:00").setDuration(60).setDelayMargin(60).build();
		session.setNext(next);

		session.allocate(new Talk("", 150, 1));
		session.allocate(new Talk("", 90, 3));

		assertEquals(LocalTime.parse("13:00"), session.getStartsAt());
		assertEquals(LocalTime.parse("17:00"), session.getEndsAt());

		assertEquals(240, session.getDuration().toMinutes());
		assertEquals(0, session.getRemainingDuration().toMinutes());

		assertEquals(LocalTime.parse("17:00"), next.getStartsAt());
		assertEquals(LocalTime.parse("18:00"), next.getEndsAt());

		assertEquals(60, next.getDuration().toMinutes());
		assertEquals(60, next.getRemainingDuration().toMinutes());
		assertEquals(0, next.getDelayMargin());
	}

	@Test
	public void higherExtensibility() {
		SessionBuilder builder = new SessionBuilder();
		Session session = builder.setType(TALK).setTitle("Session1").setStartAt("13:00").setDuration(180).setExtensibility(60).build();

		Session next = builder.clear().setTitle("Session2").setStartAt("16:00").setDuration(60).setDelayMargin(60).build();
		session.setNext(next);

		session.allocate(new Talk("", 150, 1));
		session.allocate(new Talk("", 91, 3));

		assertEquals(LocalTime.parse("13:00"), session.getStartsAt());
		assertEquals(LocalTime.parse("16:00"), session.getEndsAt());

		assertEquals(180, session.getDuration().toMinutes());
		assertEquals(30, session.getRemainingDuration().toMinutes());

		assertEquals(LocalTime.parse("16:00"), next.getStartsAt());
		assertEquals(LocalTime.parse("17:00"), next.getEndsAt());

		assertEquals(60, next.getDuration().toMinutes());
		assertEquals(60, next.getRemainingDuration().toMinutes());
	}


	private void validateSession(Session session, boolean allocated, boolean allocate, LocalTime startsAt,
								 LocalTime endsAt, int duration, int remaining) {
		assertEquals(allocate, allocated);

		assertEquals(startsAt, session.getStartsAt());
		assertEquals(endsAt, session.getEndsAt());

		assertEquals(duration, session.getDuration().toMinutes());
		assertEquals(remaining, session.getRemainingDuration().toMinutes());
	}
}
