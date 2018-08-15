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

import com.revature.caliber.dao.TraineeDAO;

/**
 * @author Jerry Affricot
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations= {"classpath*:integration-test.xml"})
@SpringBootTest
public class TraineeDaoTest {
	
	@Autowired
	TraineeDAO traineeDao;
	
	/**
	 * Test if the list of trainees is not null
	 */
	@Test
	@Transactional
	public void findAllTest() {
		assertNotEquals(0,traineeDao.findAll().size());
	}
	
	
	/**
	 * 
	 */
	@Test
	@Transactional
	public void findAllNotDroppedTest() {
		assertNotEquals(0,traineeDao.findAllNotDropped().size());
	}
	
	/**
	 * 
	 */
	@Test
	@Transactional
	public void findAllByBatchTest() {
		assertNotEquals(0,traineeDao.findAllByBatch(2201));
	}
	
	/**
	 * 
	 */
	@Test
	@Transactional
	public void findAllDroppedByBatchTest() {
		assertNotEquals(0,traineeDao.findAllDroppedByBatch(2201));
	}
	
	
	/**
	 * 
	 */
	@Test
	@Transactional
	public void findOneTest() {
		assertEquals("osher.y.cohen@gmail.com",traineeDao.findOne(5503).getEmail());
	}
	
	
	@Test
	@Transactional
	public void findBySkypeIdTest() {
		assertNotEquals(0,traineeDao.findBySkypeId("mcart5566").size());
	}
	
	
	@Test
	@Transactional
	public void findByEmailTest() {
		assertEquals(true,traineeDao.findByEmail("howard.johnson@hotmail.com").isEmpty());
	}
	
	
	@Test
	@Transactional
	public void findByNameTest() {
		assertEquals(true,traineeDao.findByName("Howard Johnson").isEmpty());
	}
	
	
	
	

}
