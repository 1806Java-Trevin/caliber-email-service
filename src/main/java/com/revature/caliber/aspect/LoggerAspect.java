package com.revature.caliber.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component("LogAspect")
@Aspect
public class LoggerAspect {
	final static Logger logger = Logger.getLogger(LoggerAspect.class);
	
	/*
	 * Mailer
	 * sendEmails()
	 */
	@AfterReturning("execution(* com.revature.caliber.email.Mailer.sendEmails(..))")
	public void successEmailSend() {
		
	}
	
	@AfterThrowing("execution(* com.revature.caliber.email.Mailer.sendEmails(..))")
	public void failEmailSend() {
		
	}
	
	/*
	 * Mailer
	 * getEmailString()
	 */
	@AfterReturning("execution(* com.revature.caliber.email.Mailer.getEmailString(..))")
	public void successGetEmailString() {
		
	}
	
	@AfterThrowing("execution(* com.revature.caliber.email.Mailer.getEmailString(..))")
	public void failGetEmailString() {
		
	}
	
	/*
	 * FlagAlertMailer
	 * sendEmails()
	 */
	@AfterReturning("execution(* com.revature.caliber.email.FlagAlertEmailer.sendEmails(..))")
	public void successFlagEmailSend() {
		
	}
	
	@AfterThrowing("execution(* com.revature.caliber.email.FlagAlertEmailer.sendEmails(..))")
	public void failFlagEmailSend() {
		
	}
	
	/*
	 * FlagAlerMailer
	 * getFlagEmailString()
	 */
	@AfterReturning("execution(* com.revature.caliber.email.FlagAlertEmailer.getFlagEmailString(..))")
	public void successGetFlagEmailString() {
		
	}
	
	@AfterThrowing("execution(* com.revature.caliber.email.FlagAlertEmailer.getFlagEmailString(..))")
	public void failGetFlagEmailString() {
		
	}	
}
