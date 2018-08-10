package com.revature.caliber.email;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.caliber.beans.Trainee;
import com.revature.caliber.beans.TraineeFlag;
import com.revature.caliber.controller.EmailController;

public class ControllerTest {
	private static EmailController emailBoss;

	@BeforeClass
	public static void setBoss() {
		emailBoss = new EmailController();
	}

	@Test
	public void getHTMLFlagTest() {
		assertEquals("Cannot find a trainee flagged GREEN", "", emailBoss.getHTMLFlags(TraineeFlag.GREEN, new ArrayList<Trainee>()));
	}
	
}
