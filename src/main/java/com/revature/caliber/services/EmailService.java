package com.revature.caliber.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
	
	//private static final Logger logger = Logger.getLogger(EmailService.class);

	@Autowired
	private Mailer mailer;
	
	private ScheduledFuture<?> mailHandle;
	private int mailInterval;
	
	/**
	 * Used to schedule the actual firing of emails
	 */
	private static final ScheduledExecutorService scheduler =
		Executors.newSingleThreadScheduledExecutor();

	/**
	 * Starts the ScheduledThreadExecutor upon application startup/bean initialization
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * Begins the scheduler task of firing emails to trainers who have not submitted their grades
	 * The task is scheduled to first fire at HOUR_TO_FIRE:MINUTE_TO_FIRE with respect to TIME_ZONE
	 * The task is then repeated every TIME_UNITS_BETWEEN_EMAILS TIME_UNITS
	 * ZonedDateTime takes care of daylight savings so there is no issue with that
	 * There is a strange bug that causes this method to be called twice upon application startup
	 * The 'started' boolean is a hacky way of stopping this issue
	 */
	public synchronized void startReminderJob(int delay, int interval) {
		mailInterval = interval;
		//logger.info("startReminderJob()");
		if(mailHandle != null) {
			mailHandle.cancel(true);
			
			mailHandle = scheduler.scheduleAtFixedRate(mailer, delay, interval, TimeUnit.SECONDS );
		}
		else {
			mailHandle = scheduler.scheduleAtFixedRate(mailer, delay, interval, TimeUnit.SECONDS );
		}
	}
	
	public void cancelMail() {
		if( mailHandle != null ) {
			mailHandle.cancel(true);
			mailInterval = 0;
		}
	}
	public int  getDelay() {
		if(mailInterval == 0) {
			return 0;
		}
		return (int)mailHandle.getDelay(TimeUnit.SECONDS);
	}
	public int getInterval() {
		return mailInterval;
	}

}