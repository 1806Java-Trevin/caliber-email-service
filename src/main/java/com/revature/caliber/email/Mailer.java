package com.revature.caliber.email;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Assessment;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Grade;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.controller.EmailController;
import com.revature.caliber.dao.AssessmentDAO;
import com.revature.caliber.dao.GradeDAO;
import com.revature.caliber.services.TrainingService;

/**
 * This class sends reminder emails to trainers who have not submitted all grades for their batch.
 * @author Will Underwood
 * @author Andrew Bonds
 * @author Vladimir Yevseenko
 *
 */
@Component
public class Mailer implements Runnable{
	
	@Autowired
	private EmailController emailController;
	@Autowired
	private AssessmentDAO assessmentRepository;
//
	@Autowired
	private TrainingService trainingService;
//	
	@Autowired
	private GradeDAO evaluationService;


	/**
	 * Called by the scheduledThreadExecutor when the time is right based on the constants in EmailService
	 * Simply calls send(), which calculates which trainers need to be emailed and emails them
	 * @precondition None.
	 * @param None.
	 * @postcondition Email thread is running on server
	 */
	@Override
	public void run() {
		emailController.runReminderEmail();
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
		List<Batch> batches = this.trainingService.findAllBatches();
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

	private List<Assessment> getAssessments(int batchID) {
		return this.assessmentRepository.findByBatchId(batchID);
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
