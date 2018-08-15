/**
 * 
 */
package com.revature.caliber.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.services.TrainingService;

/**
 * @author Jerry Affricot
 *
 *	Test the training service class
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations= {"classpath*:integration-test.xml"})
@SpringBootTest
public class TrainingServiceTest {
	
	@Autowired
	TrainingService trainingService;
	
	/*
	 * get trainer by id test method
	 * get a trainer from the database and compare
	 * the name of the returned trainer
	 */
	@Test
	@Transactional
	public void getTrainberByIdTest() {
		Trainer trainer = trainingService.getTrainerById(1);
		
		assertEquals("Patrick Walsh", trainer.getName());
	}
	
	/**
	 * Test if the list of trainers is not null
	 */
	@Test
	@Transactional
	public void findAllTrainersTest() {
		assertNotEquals(0,trainingService.findAllTrainers().size());
	}
	
	/**
	 * Test if the list of trainees is not null
	 */
	@Test
	@Transactional
	public void findAllTraineesTest() {
		assertNotEquals(0,trainingService.findAllTrainees().size());
	}
	
	/**
	 * Test if the list of batches is not null
	 */
	@Test
	@Transactional
	public void findAllBatchesTest() {
		assertNotEquals(0,trainingService.findAllBatches().size());
	}
	
	
	

}
