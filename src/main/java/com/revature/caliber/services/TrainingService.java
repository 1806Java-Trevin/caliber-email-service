package com.revature.caliber.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.dao.BatchDAO;
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
	
	@Autowired
	private BatchDAO batchDAO;
	
	
	public Trainer getTrainerById(int trainerId) {
		return trainerDao.findOne(trainerId);
	}
	
	public List<Trainer> findAllTrainers(){
		return trainerDao.findAll();
	}
	
	public List<Trainee> findAllTrainees() {
		return traineeDao.findAll();
	}
	
	/**
	 * FIND ALL BATCHES
	 *
	 * @return
	 */
	public List<Batch> findAllBatches() {
		return batchDAO.findAll();
	}
	
}