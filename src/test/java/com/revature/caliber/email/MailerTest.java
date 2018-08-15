package com.revature.caliber.email;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.caliber.beans.Trainer;
import com.revature.caliber.dao.BatchDAO;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations= {"classpath*:integration-test.xml"})
@SpringBootTest
public class MailerTest {
	
	@Autowired
	Mailer mailer;
	
	@Autowired
	BatchDAO bDao;
	
	@Test
	@Transactional
	public void testGetTrainersWhoNeedToSubmitGrades() {
		Set<Trainer> trainersToSubmitGrades = new HashSet<Trainer>();
		trainersToSubmitGrades = mailer.getTrainersWhoNeedToSubmitGrades();
		
		assertEquals(true, trainersToSubmitGrades.isEmpty());
		//System.out.println(bDao.findAll());
	}
}
