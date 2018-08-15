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
import com.revature.caliber.beans.TrainerRole;
import com.revature.caliber.dao.TrainerDAO;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations= {"classpath*:integration-test.xml"})
@SpringBootTest
public class TrainingDaoTest {

	@Autowired
	TrainerDAO tDao;
	
	/**
	 * Test get trainer titles
	 */
	@Test
	@Transactional
	public void getAllTrainerTitlesTest() {
		assertNotEquals(0, tDao.findAllTrainerTitles().size());
	}
	
	/**
	 * Test get trainer by email
	 */
	@Test
	@Transactional
	public void getByEmailTest() {
		assertEquals("Dan Pickles",tDao.findByEmail("pjw6193@hotmail.com").getName());
	}
	
	/**
	 * 	
	 */
	@Test
	@Transactional
	public void getAllTrainersTest() {
		assertNotEquals(0, tDao.findAll().size());
	}
	
	@Test
	@Transactional
	public void getOneTrainerTest() {
		String expected = "Dan Pickles";
		
		String actual = tDao.findOne(51).getName();
		
		assertEquals(expected,actual);
	}
	
	@Test
	@Transactional
	public void saveTest() {
		Trainer t = new Trainer("Dan Pickles2", "The Best", "dp@mail.com", TrainerRole.ROLE_INACTIVE);
		tDao.save(t);
		
		assertEquals("Dan Pickles2", tDao.findByEmail("dp@mail.com").getName());
	}
	
	@Test
	@Transactional
	public void updateTest() {
		Trainer t = tDao.findOne(51);
		t.setName("Dan Pickles 2");
		tDao.update(t);
		
		assertEquals("Dan Pickles 2", tDao.findOne(51).getName());
	}
	
	@Test
	@Transactional
	public void deleteTest() {
		Trainer t = tDao.findOne(51);
		tDao.delete(t);
		
		assertEquals(null, tDao.findOne(51));
	}

}
