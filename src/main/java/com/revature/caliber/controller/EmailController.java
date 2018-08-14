package com.revature.caliber.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	/**
	 * Returns a set of trainers based on the type of email requester intends to send out
	 * The email_type should refer to either the TRAINER_GRADE_REMINDER or VP_BATCH_STATUS_REPORT
	 * final variable.
	 * 
	 * This method will a status error code if the email type requested is not one of the
	 * accepted values.
	 * 
	 * @param email_type a string used to determine what trainer set to grab
	 * @return A ResponseEntity containing the trainer set required
	 */
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
	
	/**
	 * Returns a map consisting of two elements, the delay timer for initial email trigger
	 * and the interval between additional email triggers after the initial. This allows for
	 * automated email reminders and alerts to be sent to trainers/VPs
	 * 
	 * @param email_type a string used to determine whether to get timers from email or flag
	 * @return	A map consisting of 2 elements, one for delay, one for interval
	 */
	@RequestMapping(value = "/emails/getSchedule", method = RequestMethod.GET)
	public ResponseEntity<HashMap<String, Integer>> handleGetScheduleEmail(
			@RequestParam("email_type") String email_type) {

		if (email_type == null) {
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

	/**
	 * FOR ACTUAL APPLICATION:
	 * 
	 * Schedules an email to be sent based on data from an input form. Gets the email type string,
	 * interval integer, and delay integer from the request. Then, depending on what email type was
	 * requested in the form, schedules either reminders for trainers who have not completed the
	 * grading of their trainees OR a status report of all batches to the VPs.
	 * 
	 * Integer values equal to or lesser than 0 for interval will result in an email cancellation.
	 * 
	 * @param req The request containing the form data to be read into local variables
	 * @return A response entity containing either the delay and interval OR a status code in case of error
	 */
	@RequestMapping(value = "/emails/startSchedule", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
	public ResponseEntity<HashMap<String, Integer>> handleScheduleEmail(HttpServletRequest req) {

		JSONObject obj = getObj(req);
		String email_type = obj.getString("email_type");
		int interval = obj.getInt("interval");
		int delay = obj.getInt("delay");

		HashMap<String, Integer> map =  new HashMap<String, Integer>();
		switch (email_type) {
		case TRAINER_GRADE_REMINDER:
			if(interval <= 0) {
				emailService.cancelMail();
			}
			else {
				emailService.startReminderJob(delay, interval);
			}
			map.put("delay", emailService.getDelay());
			map.put("interval", emailService.getInterval());
			break;
		case VP_BATCH_STATUS_REPORT:
			if(interval <= 0) {
				flagService.cancelMail();
			}
			else {
				flagService.startReminderJob(delay, interval);
			}
			map.put("delay", flagService.getDelay());
			map.put("interval", flagService.getInterval());
			break;
		default:
			return new ResponseEntity<HashMap<String, Integer>>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<HashMap<String, Integer>>(map, HttpStatus.CREATED);
	}

	/**
	 * FOR TESTING PURPOSES:
	 * 
	 * Schedules an email to be sent based on data from an input form. Gets the email type string,
	 * interval integer, and delay integer from the request. Then, depending on what email type was
	 * requested in the form, schedules either reminders for trainers who have not completed the
	 * grading of their trainees OR a status report of all batches to the VPs.
	 * 
	 * Integer values equal to or lesser than 0 for interval will result in an email cancellation.
	 * 
	 * @param req The request containing the form data to be read into local variables
	 * @return A response entity containing either the delay and interval OR a status code in case of error
	 */
	@RequestMapping(value = "/emails/startScheduleForm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<HashMap<String, Integer>> handleScheduleEmailForm(@RequestParam Map<String,String> req) {
		// @RequestBody MultiValueMap<String, String> formData
		System.out.println("I reached inside handle scheudle email form");

		if( !req.containsKey("email_type") || !req.containsKey("delay") || !req.containsKey("interval")  ) {
			return new ResponseEntity<HashMap<String, Integer>>(HttpStatus.BAD_REQUEST);
		}
		
		String email_type = req.get("email_type");
		System.out.println(email_type);
		int interval = Integer.parseInt(req.get("interval"));
		int delay = Integer.parseInt(req.get("delay"));
		System.out.println(email_type + " " + interval + " " + delay);

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		switch (email_type) {
		case TRAINER_GRADE_REMINDER:
			if (interval <= 0) {
				emailService.cancelMail();
			} else {
				emailService.startReminderJob(delay, interval);
			}
			map.put("delay", emailService.getDelay());
			map.put("interval", emailService.getInterval());
			break;
		case VP_BATCH_STATUS_REPORT:
			if (interval <= 0) {
				flagService.cancelMail();
			} else {
				flagService.startReminderJob(delay, interval);
			}
			map.put("delay", flagService.getDelay());
			map.put("interval", flagService.getInterval());
			break;
		default:
			return new ResponseEntity<HashMap<String, Integer>>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<HashMap<String, Integer>>(map, HttpStatus.CREATED);
	}

	/**
	 * Grabs the trainer id and email type from the request and calls a function to either
	 * A) Send an email to remind a trainer to grade their associate
	 * B) Report to VP on batch status
	 * 
	 * Returns a bad request if params are invalid.
	 * 
	 * @param trainerId Id of the trainer to send email too
	 * @param email_type Type of email to send, between A or B
	 * @return Returns a response code detailing if the request was successful
	 */
	@RequestMapping(params = { "email_type" }, value = "/emails/send/{id}", method = RequestMethod.POST)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public ResponseEntity<Void> handleEmailRequests(@PathVariable("id") int trainerId,
			@RequestParam("email_type") String email_type) {
		Trainer trainerRecipient = trainingService.getTrainerById(trainerId);

		if (trainerRecipient == null) {
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

	/**
	 * Loops over set of trainers who have not submitted grades for their batch, and sends
	 * reminder emails to those trainers via the handleEmailRequests method.
	 */
	public void runReminderEmail() {
		Set<Trainer> trainersToMail = mailer.getTrainersWhoNeedToSubmitGrades();
		for (Trainer t : trainersToMail) {
			handleEmailRequests(t.getTrainerId(), TRAINER_GRADE_REMINDER);
		}
	}
	
	/**
	 * Sends email reports on flags for batches to all VPs, using the handleEmailRequests method.
	 */
	public void runFlagEmail() {
		Set<Trainer> trainersToMail = flagMailer.getVPs();
		for (Trainer t : trainersToMail) {
			handleEmailRequests(t.getTrainerId(), VP_BATCH_STATUS_REPORT); 
		}
	}
	
	/**
	 * Sets up the properties for the sending of emails; we use gmail's SMTP server
	 * 
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
	
	/**
	 * Builds and returns an email message for the trainer given as a parameter, reminding
	 * them to submit grades for their batch.
	 * 
	 * The message starts with the subject line, followed by the trainer's name. 
	 * 
	 * @param trainerRecipient the trainer to be emailed
	 * @return the message to be sent in that particular email
	 * @throws IOException
	 * @throws MessagingException
	 */
	private Message buildTrainerReminderEmail(Trainer trainerRecipient) throws IOException, MessagingException {
		Session session = Session.getDefaultInstance(getProperties(), authenticator);
		
		String emailContents = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "\\src\\main\\resources\\emailTemplate.html")),
				StandardCharsets.UTF_8);
		MimeMessage message = new MimeMessage(session);
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(trainerRecipient.getEmail()));

		message.setSubject("Reminder: Submit Grades");

		String emailStr = emailContents.replace("$TRAINER_NAME", trainerRecipient.getName());
		message.setContent(emailStr, "text/html");
		return message;
	}
	
	/**
	 * Builds and returns an email message for a VP, displaying batch statuses.
	 * 
	 * Starts with subject line, followed by placing the name of the VP to be emailed at the top.
	 * Then, the list of trainees with green flags is appended, followed by the list of trainees with
	 * red flags. Finally the content type is set to HTML and the message is returned.
	 * 
	 * @param trainerRecipient the VP to be emailed
	 * @return the message to be sent in the email
	 * @throws IOException
	 * @throws MessagingException
	 */
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
	
	/**
	 * Generates and returns a string consisting of all trainees, along with their flags and
	 * any notes attached.
	 * 
	 * @param flag 
	 * @param trainees
	 * @return
	 */
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
	
	/**
	 * Calls the buildTrainerReminderEmail method to get the email object, then sends the email
	 * built by that object.
	 * 
	 * @param trainerToSubmitGrades the trainer to receive an email.
	 */
	private void sendReminderEmail(Trainer trainerToSubmitGrades) {
		try {
			Message email = buildTrainerReminderEmail(trainerToSubmitGrades);
			Transport.send(email);
		} catch (IOException e) {
			//unable to find html template
		} catch (MessagingException e) {
			//failure to send email
		}
	}
	
	/**
	 * Calls the buildStatusEmail method to get the email object, then sends the email built
	 * by that object.
	 * 
	 * @param trainerRecipient the VP to receive the email.
	 */
	private void sendStatusEmail(Trainer trainerRecipient) {
		try {
			Message email = buildStatusEmail(trainerRecipient);
			Transport.send(email);
		} catch (IOException e) {
			//unable to find html template
		} catch (MessagingException e) {
			//failure sending email
		}
	}
	
	/**
	 * Renders request parameters as JSON object for ease of parsing.
	 * 
	 * @param req the request sent by the client
	 * @return A JSONObject version of the passed in request.
	 */
	public JSONObject getObj(HttpServletRequest req) {

		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			//failure to build StringBuffer
		}

		JSONObject obj = null;
		try {
			obj = new JSONObject(jb.toString());
		} catch (Exception e) {
			//failure to marshal StringBuffer
		}
		return obj;
	}
}
