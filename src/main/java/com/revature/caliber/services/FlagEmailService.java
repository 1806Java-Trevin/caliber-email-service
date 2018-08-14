package com.revature.caliber.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.email.FlagAlertMailer;

@Service
public class FlagEmailService implements InitializingBean {

		//private static final Logger logger = Logger.getLogger(FlagEmailService.class);

		@Autowired
		private FlagAlertMailer mailer;
		
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
		public void afterPropertiesSet(){
		}

		
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
			mailHandle.cancel(true);
			mailInterval = 0;
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
