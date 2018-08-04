package com.revature.caliber.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.dao.TraineeDAO;
import com.revature.caliber.dao.TrainerDAO;

/**
 * Provides logic concerning trainer and trainee data. Application logic has no
 * business being in a DAO nor in a Controller. This is the ideal place for
 * calculations
 *
 * @author Patrick Walsh
 *
 */
@Service
public class TrainingService {

	
	@Autowired
	private TrainerDAO trainerDao;
	
	@Autowired
	private TraineeDAO traineeDao;
	
	
	public Trainer getTrainerById(int trainerId) {
		return trainerDao.findOne(trainerId);
	}
	
	public List<Trainee> getAllTrainees() {
		return traineeDao.findAll();
	}
}