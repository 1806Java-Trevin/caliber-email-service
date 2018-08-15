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

import com.revature.caliber.beans.Assessment;
import com.revature.caliber.beans.AssessmentType;
import com.revature.caliber.beans.Batch;
import com.revature.caliber.beans.Category;
import com.revature.caliber.dao.AssessmentDAO;
import com.revature.caliber.dao.BatchDAO;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations= {"classpath*:integration-test.xml"})
@SpringBootTest
public class AssessmentDAOTest {
	
	@Autowired
	AssessmentDAO aDao;
	
	@Autowired
	BatchDAO bDao;
	
	
	
	@Test
	@Transactional
	public void getAssessmentByIdTest() {
		
		long expected = 40;
		
		System.out.println("All: " + aDao.findOne(3062).getRawScore());
		long actual = aDao.findOne(3062).getRawScore();
		
		assertEquals(expected,actual);
	}
	
	@Test
	@Transactional
	public void getAllAssessmentsTest() {
		assertNotEquals(0, aDao.findAll().size());
	}
	
	@Test
	@Transactional
	public void saveTest() {
		Batch batch = bDao.findOne(2201);
		Category cg = aDao.findOne(3062).getCategory();
		Assessment t = new Assessment("Dan Pickles2", batch, 69, AssessmentType.Exam, 1, cg);
		aDao.save(t);
		
		assertEquals(true, aDao.findByBatchId(2201).contains(t));
//		System.out.println(bDao.findAll());
	}
	
	@Test
	@Transactional
	public void updateTest() {
		Assessment t = aDao.findOne(3062);
		System.out.println("bt = " + t);
		t.setRawScore(252002);
		aDao.update(t);
		
		
		
		t = aDao.findOne(3062);
		
		
		assertEquals(252002, t.getRawScore());
	}
	
	@Test
	@Transactional
	public void deleteTest() {
		Assessment t = aDao.findOne(3062);
		aDao.delete(t);
		
		assertEquals(null, aDao.findOne(3062));
	}
}
