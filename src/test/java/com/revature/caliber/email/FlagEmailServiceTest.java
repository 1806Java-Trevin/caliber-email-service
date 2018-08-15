package com.revature.caliber.email;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.revature.caliber.services.FlagEmailService;

public class FlagEmailServiceTest {
	
	FlagEmailService eServ = new FlagEmailService();
	
	@Test
	public void testGetInterval() {
		int delay = 30;
		int interval = 10;
//		eServ.startReminderJob(delay, interval);
		assertEquals(0, eServ.getInterval());
	}
	
	@Test
	public void testDelay() {
		assertEquals(0, eServ.getDelay());
	}
	
	/*@Test
	public void testReminderJob() {
		EmailService eServ = new EmailService();
		eServ.startReminderJob(100, 300);
	}*/
	
	@Test
	public void testAfterSetProperties() throws Exception {
		eServ.afterPropertiesSet();
	}

}
