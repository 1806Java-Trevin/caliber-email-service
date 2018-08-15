package com.revature.caliber.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.revature.caliber.dao.AssessmentDAO;
import com.revature.caliber.dao.BatchDAO;
import com.revature.caliber.dao.GradeDAO;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations= {"classpath*:integration-test.xml"})
@SpringBootTest
public class GradeDAOTest {
	
	@Autowired
	GradeDAO gDao;
	
	@Autowired
	BatchDAO bDao;
	
	@Autowired
	AssessmentDAO aDao;
	
	@Test
	@Transactional
	public void getAllGradesTest() {
		assertNotEquals(0, gDao.findAll().size());
	}
	
	@Test
	@Transactional
	public void getByAssessmentTest() {
		
		long id = 2087;

		assertEquals(false, gDao.findByAssessment(id).isEmpty());
	}
	
	@Test
	@Transactional
	public void getByTraineeTest() {
		
		//long id = 5506;

		assertEquals(false, gDao.findByTrainee(5506).isEmpty());
	}
	
	@Test
	@Transactional
	public void getByBatchTest() {
		
		//long id = 5506;

		assertEquals(false, gDao.findByBatch(2201).isEmpty());
	}
	
	@Test
	@Transactional
	public void getByCategoryTest() {
		
		//long id = 5506;

		assertEquals(false, gDao.findByCategory(1).isEmpty());
	}
	
	@Test
	@Transactional
	public void getByWeekTest() {
		
		//long id = 5506;

		assertEquals(true, gDao.findByWeek(2201, 7).isEmpty());
	}
	
	@Test
	@Transactional
	public void getByTrainerTest() {
		
		//long id = 5506;

		assertEquals(true, gDao.findByTrainer(51).isEmpty());
	}

}
