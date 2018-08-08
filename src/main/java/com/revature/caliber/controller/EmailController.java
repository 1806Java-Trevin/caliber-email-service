package com.revature.caliber.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TraineeFlag;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.email.EmailAuthenticator;
import com.revature.caliber.email.FlagAlertMailer;
import com.revature.caliber.email.Mailer;
import com.revature.caliber.services.EmailService;
import com.revature.caliber.services.FlagEmailService;
import com.revature.caliber.services.TrainingService;

/**
 * Used for assessment CRUD operations. Includes both Trainer and QC assessments
 * 
 * @author Me
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
//@PreAuthorize("isAuthenticated()")
//@CrossOrigin(origins = "http://ec2-54-163-132-124.compute-1.amazonaws.com")
public class EmailController {
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private EmailAuthenticator authenticator;
	
	@Autowired
	private Mailer mailer;

	@Autowired
	private FlagAlertMailer flagMailer;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FlagEmailService flagService;

//	@Autowired
//	private HttpServletResponse servletResponse;
//	
//	private void allowCrossDomainAccess() {
//	    if (servletResponse != null) {
//	        servletResponse.setHeader("Access-Control-Allow-Origin", "true");
//	    }
//	}
	/*
	 * email types below:
	 * the email type maps to a template and each type is handled
	 * individually by a method of this class
	 * 
	 * example:  /emails/99?email_type=vpBatchStatusReport
	 *  		99 is the trainer id to send the email to
	 *  		email_type could be 'trainerGradeReminder' or 'vpBatchStatusReport'
	 */
	private static final String TRAINER_GRADE_REMINDER = "trainerGradeReminder";
	
	private static final String VP_BATCH_STATUS_REPORT = "vpBatchStatusReport";
	
	@RequestMapping( value = "/emails/getTrainers" ,method=RequestMethod.GET)
	public ResponseEntity<Set<Trainer>> handleGetTrainers(@RequestParam("email_type") String email_type){
		
		switch (email_type) {
		case TRAINER_GRADE_REMINDER:
				Set<Trainer> trainers = mailer.getTrainersWhoNeedToSubmitGrades();
				return new ResponseEntity<Set<Trainer>>(trainers, HttpStatus.CREATED);
		case VP_BATCH_STATUS_REPORT:
				Set<Trainer> trainers1 = flagMailer.getVPs();
				return new ResponseEntity<Set<Trainer>>(trainers1, HttpStatus.CREATED);
		default:
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	@RequestMapping( value = "/emails/getSchedule" ,method=RequestMethod.GET)
	public ResponseEntity<HashMap<String, Integer>> handleGetScheduleEmail(@RequestParam("email_type") String email_type) {

//		 @RequestParam("email_type") String email_type,
//			@RequestParam("delay") String delay, @RequestParam("interval") String interval,
		
		
		if(email_type == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		switch (email_type) {
		case TRAINER_GRADE_REMINDER:
			
			map.put("delay",  emailService.getDelay());

			map.put("interval", emailService.getInterval());
			return new ResponseEntity<>(map, HttpStatus.CREATED);
		case VP_BATCH_STATUS_REPORT:

			map.put("delay",  flagService.getDelay());

			map.put("interval", flagService.getInterval());
			return new ResponseEntity<>(map, HttpStatus.CREATED);
		default:
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	//delay is in seconds?
	//interval is in interval units?
	@RequestMapping( value = "/emails/startSchedule" ,method=RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<Set<Trainer>> handleScheduleEmail(
			@RequestBody MultiValueMap<String, String> formData) {
		
//		 @RequestParam("email_type") String email_type,
//			@RequestParam("delay") String delay, @RequestParam("interval") String interval,
		
		for(String key: formData.keySet()) {
			System.out.println(formData.get(key));
		}
		
		String email_type = formData.getFirst("email_type");
		if(email_type == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(formData.getFirst("delay") == null || formData.getFirst("interval") == null ) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		int delay;
		int interval;
		try {
			delay = Integer.parseInt(formData.getFirst("delay"));
			interval = Integer.parseInt(formData.getFirst("interval"));
		}
		catch(NumberFormatException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		switch (email_type) {
		case TRAINER_GRADE_REMINDER:
			if(interval <= 0) {
				emailService.cancelMail();
			}
			else {
				emailService.startReminderJob(delay, interval);
			}
			break;
		case VP_BATCH_STATUS_REPORT:
			if(interval <= 0) {
				flagService.cancelMail();
			}
			else {
				flagService.startReminderJob(delay, interval);
			}
			break;
		default:
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	
	
	@RequestMapping(params= {"email_type"}, value = "/emails/send/{id}", method = RequestMethod.POST)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
//	@PreAuthorize("hasAnyRole('VP', 'TRAINER')")
	public ResponseEntity<Void> handleEmailRequests( @PathVariable("id") int trainerId, @RequestParam("email_type") String email_type ) {
		Trainer trainerRecipient = trainingService.getTrainerById(trainerId);
		
		//trainerRecipient = new Trainer("haha", "title", "kevinqkh@gmail.com", TrainerRole.ROLE_QC);
		if(trainerRecipient == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		switch (email_type) {
		case TRAINER_GRADE_REMINDER:
			sendReminderEmail(trainerRecipient);
			break;
		case VP_BATCH_STATUS_REPORT:
			sendStatusEmail(trainerRecipient);
			break;
		default:
			return new ResponseEntity<>(HttpStatus.IM_USED);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	public void runReminderEmail() {
		System.out.println("I am sending reminder email now");
		Set<Trainer> trainersToMail = mailer.getTrainersWhoNeedToSubmitGrades();
		for(Trainer t: trainersToMail) {
			System.out.println(t);
			System.out.println(t.getEmail());
			handleEmailRequests(t.getTrainerId(), TRAINER_GRADE_REMINDER);  // real one
			// handleEmailRequests(99, TRAINER_GRADE_REMINDER);  // this line for testing only
		}
		//loop
//		sendReminderEmail(trainerRecipient);
	}
	
	public void runFlagEmail() {
		System.out.println("I am sending flag email now");
		Set<Trainer> trainersToMail = flagMailer.getVPs();
		for(Trainer t: trainersToMail) {
			System.out.println(t);
			System.out.println(t.getEmail());
			handleEmailRequests(t.getTrainerId(), VP_BATCH_STATUS_REPORT);  // real one
			// handleEmailRequests(99, VP_BATCH_STATUS_REPORT);  // this line for testing only
			
		}
	}
	
	
	/**
	 * Sets up the properties for the sending of emails
	 * We use gmail's SMTP server
	 * @return The properties for our email sending procedure
	 */
	private Properties getProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "587");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		return properties;
	}
	
	private Message buildTrainerReminderEmail(Trainer trainerRecipient) throws IOException, MessagingException {
		Session session = Session.getDefaultInstance(getProperties(), authenticator);
		String emailContents = new String(Files.readAllBytes(Paths.get("/Users/kevinqkh/Revature/Caliber/caliber-email-service/src/main/resources/emailTemplate.html")),StandardCharsets.UTF_8);

		MimeMessage message = new MimeMessage(session);
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(trainerRecipient.getEmail()));

		message.setSubject("Reminder: Submit Grades");

		String emailStr = emailContents.replace("$TRAINER_NAME", trainerRecipient.getName());
		message.setContent(emailStr, "text/html");

		return message;
	}
	
	private Message buildStatusEmail(Trainer trainerRecipient) throws IOException, MessagingException {
		Session session = Session.getDefaultInstance(getProperties(), authenticator);
		String emailContents = new String(Files.readAllBytes(Paths.get("/Users/kevinqkh/Revature/Caliber/caliber-email-service/src/main/resources/flagEmailTemplate.html")),StandardCharsets.UTF_8);

		MimeMessage message = new MimeMessage(session);
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(trainerRecipient.getEmail()));

		message.setSubject("Batch Status");

		String emailStr = emailContents.replace("$VP_NAME", trainerRecipient.getName());
		List<Trainee> trainees = trainingService.findAllTrainees();
		emailStr = emailStr.replace("$GREEN_FLAG_TRAINEES", getHTMLFlags(TraineeFlag.GREEN, trainees));
		emailStr = emailStr.replace("$RED_FLAG_TRAINEES", getHTMLFlags(TraineeFlag.RED, trainees));
		message.setContent(emailStr, "text/html");
		

		return message;
	}
	
	public String getHTMLFlags(TraineeFlag flag, List<Trainee> trainees) {
		String flagHTML = "";
		for (Trainee trainee : trainees) {
			if (trainee.getFlagStatus().equals(flag)) {
				TrainingStatus ts = trainee.getTrainingStatus();
				if (ts.equals(TrainingStatus.Training) || ts.equals(TrainingStatus.Marketing)) {
					String flagNote = (trainee.getFlagNotes()==null)? "N/A" : trainee.getFlagNotes(); //replace null with N/A
					flagHTML += "<tr><td>" + trainee.getName() + "</td><td>" + flagNote + "</td></tr>";
				}
			}
		}
		return flagHTML;
	}
	
	private void sendReminderEmail(Trainer trainerToSubmitGrades) {
		try {
			Message email = buildTrainerReminderEmail(trainerToSubmitGrades);
			System.out.println(email);
			Transport.send(email);
		}catch(IOException e) {
			System.out.println("IOex");
		}catch(MessagingException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	private void sendStatusEmail(Trainer trainerRecipient) {
		try {
			Message email = buildStatusEmail(trainerRecipient);
			System.out.println(email);
			Transport.send(email);
		}catch(IOException e) {
			System.out.println("IOex");
		}catch(MessagingException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
