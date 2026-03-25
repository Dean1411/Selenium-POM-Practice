package tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import baseTest.baseTest;
import io.qameta.allure.Description;
import pages.home;
import reporting.extentManager;



public class RegisterUserTest extends baseTest {
	
	private home homePage;
	
	@BeforeClass
	public void initPage() {
	    homePage = new home();
	}
	
	@Test
	@Description("Sign up a user and delete it again.")
	public void registerUserTest() throws InterruptedException {
		extentManager.getTest().info("Click sign-up/login.");
		homePage.clickMenuItem();
		extentManager.getTest().info("Type name of user to sign up.");
		homePage.signUp();
		extentManager.getTest().info("Fill form.");
		homePage.fillForm();
		extentManager.getTest().info("Verify if user is created");
		homePage.verifyUser();
		extentManager.getTest().info("Delete created user");
		homePage.deleteAc();
	}
}
