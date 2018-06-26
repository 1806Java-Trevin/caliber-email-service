package com.revature.caliber.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.email.Mailer;

/**
 * Starts the email job
 * @author Andrew Bonds
 * @author Will Underwood
 * @author Vladimir Yevseenko
 */
@Service
public class EmailService implements InitializingBean {
	
	private static final Logger logger = Logger.getLogger(EmailService.class);

	@Autowired
	private Mailer mailer;
	
	/**
	 * Used to schedule the actual firing of emails
	 */
	private static final ScheduledExecutorService scheduler =
		Executors.newSingleThreadScheduledExecutor();
	
	/**
	 * The time zone with respect to which emails will be sent 
	 */
	private static final ZoneId TIME_ZONE = ZoneId.of("America/New_York");

	/**
	 * The day of the week during which emails should fire
	 */
	private static final DayOfWeek DAY_OF_WEEK_TO_FIRE = DayOfWeek.WEDNESDAY;
	
	/**
	 * The hour of the day during DAY_OF_WEEK_TO_FIRE at which to fire
	 */
	private static final int HOUR_TO_FIRE = 20; // hours go 0-23
	
	/**
	 * The minute of the HOUR_TO_FIRE to fire
	 */
	private static final int MINUTE_TO_FIRE = 00; // minutes go 0-59
	
	/**
	 * Number of days between emails, likely to stay 1 week/7 days
	 * Not directly used in the code but only for setting up TIME_UNITS_BETWEEN_EMAILS
	 */
	private static final int DAYS_BETWEEN_EMAILS = 7;
	
	/**
	 * The time units that will be used for scheduleAtFixedRate()
	 */
	private static final TimeUnit TIME_UNITS = TimeUnit.SECONDS;
	
	/**
	 * Used to set the delay between emails for scheduleAtFixedRate()
	 */
	private static final long TIME_UNITS_BETWEEN_EMAILS = TimeUnit.DAYS.toSeconds(DAYS_BETWEEN_EMAILS);
	

	/**
	 * Starts the ScheduledThreadExecutor upon application startup/bean initialization
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		startReminderJob();
	}

	/**
	 * Begins the scheduler task of firing emails to trainers who have not submitted their grades
	 * The task is scheduled to first fire at HOUR_TO_FIRE:MINUTE_TO_FIRE with respect to TIME_ZONE
	 * The task is then repeated every TIME_UNITS_BETWEEN_EMAILS TIME_UNITS
	 * ZonedDateTime takes care of daylight savings so there is no issue with that
	 * There is a strange bug that causes this method to be called twice upon application startup
	 * The 'started' boolean is a hacky way of stopping this issue
	 */
	private static boolean started = false;
	private synchronized void startReminderJob() {
		/* 
		 * Simply exit if we have already started the scheduled email job
		 * The method is synchronized for this reason, to prevent an edge case of it firing twice anyway
		 * The issue stems from run() in Mailer being called twice, not sure why 
		 */
		if (started)
			return;
		started = true;
		
		logger.info("startReminderJob()");
		
		// First we get the time that the emails will start to fire
		LocalTime timeToFireDate = LocalTime.of(HOUR_TO_FIRE, MINUTE_TO_FIRE);
		LocalDate timeToFireTime = LocalDate.now().with(TemporalAdjusters.nextOrSame(DAY_OF_WEEK_TO_FIRE));
		ZonedDateTime timeToFire = ZonedDateTime.of(timeToFireTime, timeToFireDate, TIME_ZONE);
		
		// Then the current time in order to get an initial delay for scheduleAtFixedRate()
		ZonedDateTime now = ZonedDateTime.of(LocalDate.now(), LocalTime.now(), TIME_ZONE);
		
		long delayInUnits = timeToFire.toEpochSecond() - now.toEpochSecond();
	
		logger.info("Emails will start firing at: " + timeToFire);
		
		/*
		 * Mailer's run() will be called after delayInUnits TIME_UNITS with TIME_UNITS_BETWEEN_EMAILS TIME_UNITS
		 * until the next call to run()
		 */
		scheduler.scheduleAtFixedRate(mailer, delayInUnits, TIME_UNITS_BETWEEN_EMAILS, TIME_UNITS);
	}

}
