package com.revature.caliber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Trainer;
import com.revature.dao.TrainerDAO;

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
	
	
	public Trainer getTrainerById(int trainerId) {
		return trainerDao.findOne(trainerId);
	}
}