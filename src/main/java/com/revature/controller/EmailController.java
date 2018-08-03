package com.revature.controller;

import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.services.TrainingService;

/**
 * Used for assessment CRUD operations. Includes both Trainer and QC assessments
 * 
 * @author Me
 *
 */
@RestController
//@PreAuthorize("isAuthenticated()")
//@CrossOrigin(origins = "http://ec2-54-163-132-124.compute-1.amazonaws.com")
public class EmailController {

	
	@Autowired
	TrainingService trainingService;
	
	
	@RequestMapping(params= {"email_type"}, value = "/emails/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
//	@PreAuthorize("hasAnyRole('VP', 'TRAINER')")
	public ResponseEntity<Void> createAssessment( @PathVariable int trainerId, @RequestParam String email_type ) {
//		log.debug("Creating assessment: " + assessment);
//		assessmentService.save(assessment);
		
		Trainer t = trainingService.getTrainerById(trainerId);
		if(t==null) {
//			log.info("Invalid trainer ID");
			System.out.println("Invalid trainer ID");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(email_type.equals("trainerGradeReiminder!?!@#")) {
			sendTrainingReminder(t);
		} else if (email_type.equals("vpBatchStatusReport!?!@#")) {
			sendStatusReport(t);
			
		} else {
			return new ResponseEntity<>(HttpStatus.IM_USED);
		}
		
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	private void sendTrainingReminder(Trainer trainerToBeSentToWithAGoodReminderEmail) {
		Properties properties = setProperties();
		Session session = getSession(properties);
		sendReminderEmail(session, trainerToBeSentToWithAGoodReminderEmail);
	}
	private void sendStatusReport(Trainer trainerToBeSentToWithAGoodStatusEmail) {
		Properties properties = setProperties();
		Session session = getSession(properties);
//		sendStatusEmail(session, trainerToBeSentToWithAGoodStatusEmail);
	}
	
	

	/**
	 * Sets up the properties for the sending of emails
	 * We use gmail's SMTP server
	 * @return The properties for our email sending procedure
	 */
	private Properties setProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "587");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		return properties;
	}

	/**
	 * Creates an email Session that can be used to send emails
	 * @param properties The configuration for this session
	 * @return A session used to send emails
	 */
	private Session getSession(Properties properties) {
		return Session.getDefaultInstance(properties, authenticator);
	}

	
	
	
	private void sendReminderEmail(Session session, Trainer trainerToSubmitGrades) {
		logger.info("Trainer being sent emails: "+ trainerToSubmitGrades);
		String emailTemplate = getEmailString();
		if (emailTemplate == null) {
			logger.warn("Unable to load email template, exiting sendEmails()");
			return;
		}	
		try {
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(trainer.getEmail()));
			
			message.setSubject("Submit Grades Reminder");
			
			// Parametrize the email to contain the name of the trainer being emailed
			String emailStr = emailTemplate.replace(EMAIL_TEMPLATE_NAME_TOKEN, trainer.getName());
			message.setContent(emailStr, "text/html");
			
			Transport.send(message);
			logger.info("Email sent");
		} catch (MessagingException e) {
			logger.warn(e);
			logger.warn("Email exception");
		}
	}
	
	private void sendStatusEmail(Trainer t) {
		
	}
	
}
