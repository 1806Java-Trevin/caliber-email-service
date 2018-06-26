package com.revature.caliber.email;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Assessment;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.data.AssessmentRepository;
import com.revature.caliber.services.EvaluationService;
import com.revature.caliber.services.TrainingService;

/**
 * This class sends reminder emails to trainers who have not submitted all grades for their batch.
 * @author Will Underwood
 * @author Andrew Bonds
 * @author Vladimir Yevseenko
 *
 */
@Component
public class Mailer implements Runnable {
	
	private static final Logger logger = Logger.getLogger(Mailer.class);

	@Autowired
	private AssessmentRepository assessmentRepository;

	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private EmailAuthenticator authenticator;

	/**
	 * The EMAIL_TEMPLATE_NAME_TOKEN is the token that is in the HTML file that will be replaced
	 * with the actual name of the trainer for the email to be trainer specific
	 */
	private static final String EMAIL_TEMPLATE_NAME_TOKEN = "$TRAINER_NAME";
	
	/**
	 * The path to the email template
	 */
	private static final String EMAIL_TEMPLATE_PATH = "emailTemplate.html";

	/**
	 * Called by the scheduledThreadExecutor when the time is right based on the constants in EmailService
	 * Simply calls send(), which calculates which trainers need to be emailed and emails them
	 * @precondition None.
	 * @param None.
	 * @postcondition Email thread is running on server
	 */
	@Override
	public void run() {
		send();
	}

	/**
	 * Sets up the properties and session in order to send emails
	 * then simply calls the sendEmails() method which does email sending
	 * given the trainers who need to submit grades
	 */
	private void send() {
		Properties properties = setProperties();
		Session session = getSession(properties);
		sendEmails(session, getTrainersWhoNeedToSubmitGrades());
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

	/**
	 * Iterates over trainersToSubmitGrades and emails each person individually
	 * that they need to submit their grades
	 * @param session The email session used to send emails
	 * @param trainersToSubmitGrades The trainers who need to be emailed reminders
	 */
	private void sendEmails(Session session, Set<Trainer> trainersToSubmitGrades) {
		logger.info("Trainers being sent emails: "+ trainersToSubmitGrades);
		String emailTemplate = getEmailString();
		if (emailTemplate == null) {
			logger.warn("Unable to load email template, exiting sendEmails()");
			return;
		}
		for (Trainer trainer : trainersToSubmitGrades) {		
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
	}
	
	/**
	 * Reads the email template located at EMAIL_TEMPLATE_PATH and returns a String
	 * containing the contents of it
	 * @return A String containing the contents of the email template html file
	 */
	private String getEmailString() {
		try {
			String emailStr = null;
			ClassLoader classLoader = getClass().getClassLoader();
			emailStr = IOUtils.toString(classLoader.getResourceAsStream(EMAIL_TEMPLATE_PATH));
			logger.info("loaded email template");
			return emailStr;
		} catch (IOException e) {
			logger.warn("Unable to read email template");
			logger.warn(e);
			return null;
		}
	}
	
	/**
	 * Returns a Set of Trainers who have not submitted all grades for their batch's assessments.
	 * Only considers current batches.
	 * Also grabs trainers who have not created a single assessment.
	 * @precondition None.
	 * @param None.
	 * @return A Set of Trainers who need to submit grades
	 */
	public Set<Trainer> getTrainersWhoNeedToSubmitGrades() {
		Set<Trainer> trainersToSubmitGrades = new HashSet<Trainer>();
		List<Batch> batches = getBatches();
		for (Batch batch : batches) {
			Set<Trainee> trainees = batch.getTrainees();
			//The following removes all dropped trainees from the Trainee Set
			trainees = trainees.stream().filter(trainee -> !trainee.getTrainingStatus().equals(TrainingStatus.Dropped)).collect(Collectors.toSet());
			List<Assessment> assessments = getAssessments(batch.getBatchId());
			//Checking for trainers who haven't created a single assessment for their batch
			if(assessments.isEmpty()) {
				trainersToSubmitGrades.add(batch.getTrainer());
			}
			int expectedNumberOfGrades = trainees.size() * assessments.size();
			int actualNumberOfGrades = 0;
			actualNumberOfGrades = getActualNumberOfGrades(assessments, batch.getBatchId());
			if (actualNumberOfGrades < expectedNumberOfGrades) {
				trainersToSubmitGrades.add(batch.getTrainer());
			}
		}
		return trainersToSubmitGrades;
	}
	
	private List<Batch> getBatches(){
		return this.trainingService.findAllBatches();
	}
	
	private List<Assessment> getAssessments(int batchID) {
		return this.assessmentRepository.findByBatchBatchId(batchID);
	}
	
	private int getActualNumberOfGrades(List<Assessment> expectedAssessments, int batchId){
		List<Grade> allGrades = evaluationService.findByBatch(batchId);
		int gradeCounter = 0;
		for(Grade grade: allGrades) {
			for(Assessment assessment: expectedAssessments) {
				if(grade.getAssessment().getAssessmentId() == assessment.getAssessmentId()) {
					gradeCounter++;
				}
			}
		}
		return gradeCounter;
	}
}
