package com.revature.caliber.email;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.revature.caliber.beans.Trainer;

public class MailerTest {
	
	@Test
	public void testGetTrainersWhoNeedToSubmitGrades() {
		Set<Trainer> trainersToSubmitGrades = new HashSet<Trainer>();
		trainersToSubmitGrades = new Mailer().getTrainersWhoNeedToSubmitGrades();
		
		assertEquals(false, trainersToSubmitGrades.isEmpty());
	}
}
