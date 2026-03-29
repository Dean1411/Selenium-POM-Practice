package tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.home;
import reporting.extentManager;

public class RegisterAndLoginWithUserTest {
	
	private home homePage;
	
	@BeforeClass
	public void initPage() {
		
		homePage = new home();
	}
	
	@Test (description="Register a user, then log in with the same user.")
	public void loginWithRegisteredUserTest() throws InterruptedException {
		
		extentManager.getTest().info("Click sign-up/login.");
		homePage.clickMenuItem();
		extentManager.getTest().info("Type name of user to sign up");
		homePage.signUp();
		extentManager.getTest().info("Fill form.");
		homePage.fillForm();
	}

}
