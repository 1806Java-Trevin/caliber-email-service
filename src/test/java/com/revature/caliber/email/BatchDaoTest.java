package com.revature.caliber.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.caliber.beans.Assessment;
import com.revature.caliber.beans.AssessmentType;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Category;
import com.revature.caliber.beans.Trainer;
import com.revature.caliber.dao.AssessmentDAO;
import com.revature.caliber.dao.BatchDAO;
import com.revature.caliber.dao.TrainerDAO;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations= {"classpath*:integration-test.xml"})
@SpringBootTest
public class BatchDaoTest {

	@Autowired
	AssessmentDAO aDao;
	
	@Autowired
	BatchDAO bDao;
	
	@Autowired
	TrainerDAO tDao;
	
	@Test
	@Transactional
	public void findOneTest() {
		
		String expected = "Tech Incubator at Queens College, 65-30 Kissena Blvd, CEP Hall 2, Queens, NY 11367";
		
		System.out.println(bDao.findAll());
		
		String actual = bDao.findOne(2201).getLocation();
		
		assertEquals(expected,actual);
	}
	
	@Test
	@Transactional
	public void findOneWithDroppedTraineesTest() {
		
		String expected = "Tech Incubator at Queens College, 65-30 Kissena Blvd, CEP Hall 2, Queens, NY 11367";
		
		
		String actual = bDao.findOneWithDroppedTrainees(2201).getLocation();
		
		assertEquals(expected,actual);
	}
	
	@Test
	@Transactional
	public void findOneWithTraineesAndGradesTest() {
		
		String expected = "Tech Incubator at Queens College, 65-30 Kissena Blvd, CEP Hall 2, Queens, NY 11367";
		
		
		String actual = bDao.findOneWithTraineesAndGrades(2201).getLocation();
		
		assertEquals(expected,actual);
	}
	
	@Test
	@Transactional
	public void findAllTest() {
		assertNotEquals(0, bDao.findAll().size());
	}
	
	@Test
	@Transactional
	public void findAllByTrainerTest() {
		assertNotEquals(0, bDao.findAllByTrainer(tDao.findOne(1).getTrainerId()).size());
	}
	
	@Test
	@Transactional
	public void findAllByCurrentTest() {
		assertNotEquals(0, bDao.findAllCurrent(tDao.findOne(1).getTrainerId()).size());
	}
	
	@Test
	@Transactional
	public void findAllCurrentWithNotesAndTraineesTest() {
		assertNotEquals(0, bDao.findAllCurrentWithNotesAndTrainees().size());
	}
	
	@Test
	@Transactional
	public void findAllCurrentWithNotesTest() {
		assertNotEquals(0, bDao.findAllCurrentWithNotes().size());
	}
	
	@Test
	@Transactional
	public void findAllCurrentWithTraineesTest() {
		assertNotEquals(0, bDao.findAllCurrentWithTrainees().size());
	}
	
	@Test
	@Transactional
	public void findAllCurrentTest() {
		assertNotEquals(0, bDao.findAllCurrent().size());
	}
	
	@Test
	@Transactional
	public void findAllAfterDateTest() {
		assertNotEquals(0, bDao.findAllAfterDate(11,07,1994).size());
	}
	
//	@Test
//	@Transactional
//	public void saveTest() {
//	
//		Batch b = new Batch("Person", new Trainer(), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), "location");
//		
//		bDao.save(b);
//		
//		assertEquals(5000, bDao.findOne(5000).getBatchId());
//	}
	
	@Test
	@Transactional
	public void updateTest() {
		Batch t = bDao.findOne(2201);
		System.out.println("bt = " + t);
		t.setLocation("Miami");
		bDao.update(t);
		
		t = bDao.findOne(2201);
		
		assertEquals("Miami", t.getLocation());
	}
		
}
