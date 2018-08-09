package com.revature.caliber.email;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.caliber.Application;



public class MainTest {
	static Application app;
	@BeforeClass
	public static void createSpring() {
		app = new Application();
	}

	@Test
	public void testGetTest() {
		assertEquals("This is a passing test", "Test", app.getTest());
	}
	
//	@Test
//	public void testMain() {
//		// assert("Testing main", void, app.main(new String[] {"Test"}));
//		try {
//			app.main(new String[] {"Test"});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	@AfterClass
	public static void deleteSpring() {
		app = null;
	}
}
