package com.revature.aspect;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Assessment;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.controller.EmailController;
import com.revature.caliber.dao.AssessmentDAO;
import com.revature.caliber.dao.BatchDAO;
import com.revature.caliber.dao.GradeDAO;
import com.revature.caliber.dao.TraineeDAO;
import com.revature.caliber.dao.TrainerDAO;
import com.revature.caliber.email.FlagAlertMailer;
import com.revature.caliber.services.EmailService;
import com.revature.caliber.services.FlagEmailService;

@Component("logging-aspect")
@Aspect
public class Logging {

	private static final Logger AssessmentDAOlog = Logger.getLogger(AssessmentDAO.class);
	private static final Logger BatchDAOlog = Logger.getLogger(BatchDAO.class);
	private static final Logger GradeDAOlog = Logger.getLogger(GradeDAO.class);
	private static final Logger TraineeDAOlog = Logger.getLogger(TraineeDAO.class);
	private static final Logger TrainerDAOlog = Logger.getLogger(TrainerDAO.class);
	private static final Logger EmailServicelog = Logger.getLogger(EmailService.class);
	private static final Logger FlagEmailServicelog = Logger.getLogger(FlagEmailService.class);
	private static final Logger FlagAlertMailerlog = Logger.getLogger(FlagAlertMailer.class);
	private static final Logger EmailControllerlog = Logger.getLogger(EmailController.class); 
	
	/*
	 * Beginning of AssessmentDAO logs
	 */
	@AfterReturning("execution(* com.revature.caliber.dao.AssessmentDAO.save(..))")
	public void AssessmentDAOSave(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Assessment obj = (Assessment) argList[0];
		AssessmentDAOlog.debug("Saving assessment " + obj);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.AssessmentDAO.findOne(..))")
	public void AssessmentDAOFindOne(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		long id = (long) argList[0];
		AssessmentDAOlog.debug("Finding one assessment " + id);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.AssessmentDAO.findAll(..))")
	public void AssessmentDAOFindAll(JoinPoint point)
	{
		AssessmentDAOlog.debug("Find all assessments");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.AssessmentDAO.findByWeek(..))")
	public void AssessmentDAOFindByWeek(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer batchId = (Integer) argList[0];
		Integer week = (Integer) argList[1];
		AssessmentDAOlog.debug("Find assessment by week number " + week + " for batch " + batchId + " ");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.AssessmentDAO.findByBatchId(..))")
	public void AssessmentDAOFindByBatchId(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer batchId = (Integer) argList[0];
		AssessmentDAOlog.debug("Find assessment by batchId" + batchId + " ");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.AssessmentDAO.update(..))")
	public void AssessmentDAOUpdate(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Assessment assessment = (Assessment) argList[0];
		AssessmentDAOlog.debug("Updating assessment " + assessment);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.AssessmentDAO.delete(..))")
	public void AssessmentDAODelete(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Assessment assessment = (Assessment) argList[0];
		AssessmentDAOlog.debug("Deleting assessment " + assessment);
	}
	
	
	/*
	 * Beginning of BatchDAO logs
	 */
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.save(..))")
	public void BatchDAOSave(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Batch batch = (Batch) argList[0];
		BatchDAOlog.debug("Saving Batch " + batch);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findAll(..))")
	public void BatchDAOFindAll(JoinPoint point)
	{
		BatchDAOlog.debug("Fetching all batches");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findAllByTrainer(..))")
	public void BatchDAOFindAllByTrainer(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer trainerId = (Integer) argList[0];
		BatchDAOlog.debug("Fetching all batches for trainer: " + trainerId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findAllCurrent(..))")
	public void BatchDAOFindAllCurrent(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer trainerId = (Integer) argList[0];
		BatchDAOlog.debug("Fetching all current batches for trainer: " + trainerId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findAllCurrentWithNotesAndTrainees(..))")
	public void BatchDAOFindAllCurrentWithNotesAndTrainees(JoinPoint point)
	{
		BatchDAOlog.debug("Fetching all current batches with trainees, grades and notes");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findAllCurrentWithNotes(..))")
	public void BatchDAOFindAllCurrentWithNotes(JoinPoint point)
	{
		BatchDAOlog.debug("Fetching all current batches with trainees, grades and notes");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findAllCurrentWithTrainees(..))")
	public void BatchDAOFindAllCurrentWithTrainees(JoinPoint point)
	{
		BatchDAOlog.debug("Fetching all current batches with trainees, grades");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findAllCurrent())")
	public void BatchDAOFindAllCurrentNoArgs(JoinPoint point)
	{
		BatchDAOlog.debug("Fetching all current batches with active trainees");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findOne(..))")
	public void BatchDAOFindOne(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer batchId = (Integer) argList[0];
		BatchDAOlog.debug("Fetching batch: " + batchId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findOneWithDroppedTrainees(..))")
	public void BatchDAOFindOneWithDroppedTrainees(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer batchId = (Integer) argList[0];
		BatchDAOlog.debug("Fetching batch: " + batchId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findOneWithTraineesAndGrades(..))")
	public void BatchDAOFindOneWithTraineesAndGrades(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer batchId = (Integer) argList[0];
		BatchDAOlog.debug("Fetching batch with trainees: " + batchId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.update(..))")
	public void BatchDAOUpdate(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Batch batch = (Batch) argList[0];
		BatchDAOlog.debug("Updating batch: " + batch + " Trainer: " + batch.getTrainer() + " Cotrainer: " + batch.getCoTrainer());
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.delete(..))")
	public void BatchDAODelete(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Batch batch = (Batch) argList[0];
		BatchDAOlog.debug("Deleting Batch " + batch);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.BatchDAO.findAllAfterDate(..))")
	public void BatchDAOFindAllAfterDate(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer month = (Integer) argList[0];
		Integer day = (Integer) argList[1];
		Integer year = (Integer) argList[2];
		Calendar startDate = Calendar.getInstance();
		startDate.set(year, month, day);
		BatchDAOlog.debug("Fetching all current batches since: " + startDate.getTime().toString());
	}
	
	/*
	 * Beginning of GradeDAO logs
	 */
	@AfterReturning("execution(* com.revature.caliber.dao.GradeDAO.save(..))")
	public void GradeDAOSave(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Grade grade = (Grade) argList[0];
		GradeDAOlog.debug("Saving grade " + grade);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.GradeDAO.update(..))")
	public void GradeDAOUpdate(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Grade grade = (Grade) argList[0];
		GradeDAOlog.debug("Updating grade " + grade);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.GradeDAO.findAll(..))")
	public void GradeDAOFindAll(JoinPoint point)
	{
		GradeDAOlog.debug("Finding all grades");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.GradeDAO.findByAssessment(..))")
	public void GradeDAOFindByAssessment(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Long assessmentId = (Long) argList[0];
		GradeDAOlog.debug("Finding grades for assessment: " + assessmentId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.GradeDAO.findByTrainee(..))")
	public void GradeDAOFindByTrainee(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer traineeId = (Integer) argList[0];
		GradeDAOlog.debug("Finding all grades for trainee: " + traineeId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.GradeDAO.findByBatch(..))")
	public void GradeDAOFindByBatch(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer batchId = (Integer) argList[0];
		GradeDAOlog.debug("Finding all grades for batch: " + batchId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.GradeDAO.findByCategory(..))")
	public void GradeDAOFindByCategory(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer categoryId = (Integer) argList[0];
		GradeDAOlog.debug("Finding all grades for category: " + categoryId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.GradeDAO.findByWeek(..))")
	public void GradeDAOFindByWeek(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer batchId = (Integer) argList[0];
		Integer week = (Integer) argList[1];
		GradeDAOlog.debug("Finding week " + week + " grades for batch: " + batchId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.GradeDAO.findByTrainer(..))")
	public void GradeDAOFindByTrainer(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer trainerId = (Integer) argList[0];
		GradeDAOlog.debug("Finding all grades for trainer: " + trainerId);
	}
	
	/*
	 * Beginning of TraineeDAO logs
	 */
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.save(..))")
	public void TraineeDAOSave(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Trainee trainee = (Trainee) argList[0];
		TraineeDAOlog.debug("Saving trainee " + trainee);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.update(..))")
	public void TraineeDAOUpdate(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Trainee trainee = (Trainee) argList[0];
		TraineeDAOlog.debug("Updating trainee " + trainee);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.delete(..))")
	public void TraineeDAODelete(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Trainee trainee = (Trainee) argList[0];
		TraineeDAOlog.debug("Deleting trainee " + trainee);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findAll(..))")
	public void TraineeDAOFindAll(JoinPoint point)
	{
		TraineeDAOlog.debug("Fetching all trainees");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findAllNotDropped(..))")
	public void TraineeDAOFindAllNotDropped(JoinPoint point)
	{
		TraineeDAOlog.debug("Fetching all trainees");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findAllByBatch(..))")
	public void TraineeDAOFindAllByBatch(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer batchId = (Integer) argList[0];
		TraineeDAOlog.debug("Fetching all Active trainees by batch: " + batchId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findAllDroppedByBatch(..))")
	public void TraineeDAOFindAllDroppedByBatch(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer batchId = (Integer) argList[0];
		TraineeDAOlog.debug("Fetching all Dropped trainees by batch: " + batchId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findAllByTrainer(..))")
	public void TraineeDAOFindAllByTrainer(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer trainerId = (Integer) argList[0];
		TraineeDAOlog.debug("Fetch all trainees by trainer: " + trainerId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findOne(..))")
	public void TraineeDAOFindOne(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer traineeId = (Integer) argList[0];
		TraineeDAOlog.debug("Fetch trainee by id: " + traineeId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findByEmail(..))")
	public void TraineeDAOFindByEmail(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		String email = (String) argList[0];
		TraineeDAOlog.debug("Fetch trainee by email address: " + email);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findByName(..))")
	public void TraineeDAOFindByName(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		String name = (String) argList[0];
		TraineeDAOlog.debug("Fetch trainee by name: " + name);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findByResourceId(..))")
	public void TraineeDAOFindByResourceId(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		String resourceId = (String) argList[0];
		TraineeDAOlog.debug("Fetch trainee by resource id: " + resourceId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TraineeDAO.findBySkypeId(..))")
	public void TraineeDAOFindBySkypeId(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		String skypeId = (String) argList[0];
		TraineeDAOlog.debug("Fetch trainee by skype id: " + skypeId);
	}
	
	/*
	 * Beginning of TrainerDAO logs 
	 */
	@AfterReturning("execution(* com.revature.caliber.dao.TrainerDAO.findAllTrainerTitles(..))")
	public void TrainerDAOFindAllTrainerTitles(JoinPoint point)
	{
		TrainerDAOlog.debug("Get all trainer titles");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TrainerDAO.findByEmail(..))")
	public void TrainerDAOFindByEmail(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Trainer trainer = (Trainer) argList[0];
		TrainerDAOlog.debug("DAO found trainer by email " + trainer);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TrainerDAO.findAll(..))")
	public void TrainerDAOFindAll(JoinPoint point)
	{
		TrainerDAOlog.debug("Finding all trainers");
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TrainerDAO.save(..))")
	public void TrainerDAOSave(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Trainer trainer = (Trainer) argList[0];
		TrainerDAOlog.debug("Save trainer " + trainer);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TrainerDAO.findOne(..))")
	public void TrainerDAOFindOne(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer trainerId = (Integer) argList[0];
		TrainerDAOlog.debug("Find trainer by id: " + trainerId);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TrainerDAO.update(..))")
	public void TrainerDAOUpdate(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Trainer trainer = (Trainer) argList[0];
		TrainerDAOlog.debug("Update trainer " + trainer);
	}
	
	@AfterReturning("execution(* com.revature.caliber.dao.TrainerDAO.delete(..))")
	public void TrainerDAODelete(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Trainer trainer = (Trainer) argList[0];
		TrainerDAOlog.debug("Delete trainer " + trainer);
	}
	
	/*
	 * Beginning of EmailService log
	 */
	@AfterReturning("execution(* com.revature.caliber.services.EmailService.startReminderJob(..))")
	public void EmailServiceStartReminderJob(JoinPoint point)
	{	
		EmailServicelog.info("startReminderJob()");
	}
	
	/*
	 * Beginning of FlagEmailService log
	 */
	@AfterReturning("execution(* com.revature.caliber.services.FlagEmailService.startReminderJob(..))")
	public void FlagEmailServiceStartReminderJob(JoinPoint point)
	{	
		FlagEmailServicelog.info("startReminderJob()");
	}
	
	/*
	 * Beginning of FlagAlertMailer log
	 */
	@AfterReturning("execution(* com.revature.caliber.email.FlagAlertMailer.getVPs(..))")
	public void FlagAlertMailerGetVPs(JoinPoint point)
	{
		FlagAlertMailerlog.info("Getting VP's from trainer's table");
	}
	
	/*
	 * Beginning of EmailController logs
	 */
	@AfterReturning("execution(* com.revature.caliber.controller.EmailController.getObj(..))")
	public void EmailControllerGetObjReturning(JoinPoint point)
	{
		EmailControllerlog.debug("JSON message created");
	}
	
	@AfterThrowing("execution(* com.revature.caliber.controller.EmailController.getObj(..))")
	public void EmailControllerGetObjThrowing(JoinPoint point)
	{
		EmailControllerlog.error("failure creating JSON message");
	}
	
	@AfterReturning("execution(* com.revature.caliber.controller.EmailController.sendStatusEmail(..))")
	public void EmailControllerSendStatusEmailReturning(JoinPoint point)
	{
		EmailControllerlog.debug("Status email successfully send");
	}
	
	@AfterThrowing("execution(* com.revature.caliber.controller.EmailController.sendStatusEmail(..))")
	public void EmailControllerSendStatusEmailThrowing(JoinPoint point)
	{
		EmailControllerlog.error("failure sending status email");
	}
	
	@AfterReturning("execution(* com.revature.caliber.controller.EmailController.sendReminderEmail(..))")
	public void EmailControllerSendReminderEmailReturning(JoinPoint point)
	{
		EmailControllerlog.debug("Reminder email successfully send");
	}
	
	@AfterThrowing("execution(* com.revature.caliber.controller.EmailController.sendReminderEmail(..))")
	public void EmailControllerSendReminderEmailThrowing(JoinPoint point)
	{
		EmailControllerlog.error("Reminder sending status email");
	}
	
	@After("execution(* com.revature.caliber.controller.EmailController.handleGetTrainers(..))")
	public void EmailControllerHandleGetTrainers(JoinPoint point)
	{
		EmailControllerlog.debug("Requested URI: /emails/getTrainers");
	}
	
	@After("execution(* com.revature.caliber.controller.EmailController.handleGetScheduleEmail(..))")
	public void EmailControllerHandleGetScheduleEmail(JoinPoint point)
	{
		EmailControllerlog.debug("Requested URI: /emails/getSchedule");
	}
	
	@After("execution(* com.revature.caliber.controller.EmailController.handleScheduleEmail(..))")
	public void EmailControllerHandleScheduleEmail(JoinPoint point)
	{
		EmailControllerlog.debug("Requested URI: /emails/startSchedule");
	}
	
	@After("execution(* com.revature.caliber.controller.EmailController.handleEmailRequests(..))")
	public void EmailControllerHandleEmailRequests(JoinPoint point)
	{
		Object [] argList;
		argList = point.getArgs();
		Integer trainerId = (Integer) argList[0];
		EmailControllerlog.debug("Requested URI: /emails/send/{id}, with id: " + trainerId);
	}
}
