package com.revature.caliber.email;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TraineeFlag;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.beans.TrainingStatus;
import com.revature.caliber.controller.EmailController;
import com.revature.caliber.services.TrainingService;

@Component
public class FlagAlertMailer implements Runnable{
	
	//private static final Logger logger = Logger.getLogger(FlagAlertMailer.class);

	// TODO refactor to REST calls
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private EmailController emailController;

	/**
	 * Called by the scheduledThreadExecutor when the time is right based on the
	 * constants in EmailService Simply calls send(), which finds vps to be
	 * emailed and emails them
	 * 
	 * @precondition None.
	 * @param None.
	 * @postcondition Email thread is running on server
	 */
	@Override
	public void run() {
		emailController.runFlagEmail();
	}

	/**
	 * Returns a Set of Trainers who have the role of "ROLE_VP"
	 * 
	 * @precondition None.
	 * @param None.
	 * @return Set of VP Trainers
	 */
	public Set<Trainer> getVPs() {
		List<Trainer> trainers = trainingService.findAllTrainers();
		//logger.info(trainers.toString());
		Set<Trainer> vps = new HashSet<>();
		for (Trainer trainer : trainers) {
			if (trainer.getTier() == TrainerRole.ROLE_VP) {
				vps.add(trainer);
			}
		}
		return vps;
	}

	/**
	 * Returns a String of trainees with red flags formatted in an HTML table
	 * 
	 * @precondition None.
	 * @param None.
	 * @return String of red flagged trainees
	 */
	public String redFlagHTML() {
		List<Trainee> trainees = trainingService.findAllTrainees();
		String redFlagHTML = "";
		for (Trainee trainee : trainees) {
			if (trainee.getFlagStatus().equals(TraineeFlag.RED)) {
				TrainingStatus ts = trainee.getTrainingStatus();
				if (ts.equals(TrainingStatus.Training) || ts.equals(TrainingStatus.Marketing)) {
					redFlagHTML += "<tr><td>" + trainee.getName() + "</td><td>" + trainee.getFlagNotes() + "</td></tr>";
				}
			}
		}
		return redFlagHTML;
	}

	/**
	 * Returns a String of trainees with green flags formatted in an HTML table
	 * 
	 * @precondition None.
	 * @param None.
	 * @return String of green flagged trainees
	 */
	public String greenFlagHTML() {
		List<Trainee> trainees = trainingService.findAllTrainees();
		String greenFlagHTML = "";
		for (Trainee trainee : trainees) {
			if (trainee.getFlagStatus() == TraineeFlag.GREEN) {
				greenFlagHTML += "<tr><td>" + trainee.getName() + "</td><td>" + trainee.getFlagNotes() + "</td></tr>";
			}

		}
		return greenFlagHTML;
	}
}