package tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Utilities.JsonUtils;
import baseTest.baseTest;
import io.qameta.allure.Description;
import pages.home;
import reporting.extentManager;

public class ValidLoginTest extends baseTest {
	
	private home homePage;
	public static JSONArray userLogin;
	public static JSONArray userSignup;
	
	@BeforeClass
	public void initPage() throws JSONException, Exception {
		
		homePage = new home();
		userLogin = JsonUtils.getLoginUsers();
	}
	
	@Test
	@Description("Login User with correct email and password")
	public void loginWithRegisteredUserTest() throws InterruptedException {
			    
	    JSONObject loginUser = userLogin.getJSONObject(0);
	    String email = loginUser.getString("email");
	    String password = loginUser.getString("password");
	    String expectedUser = "Valid";
		
		extentManager.getTest().info("Click sign-up/login.");
		homePage.clickMenuItem();
		extentManager.getTest().info("enter username and password and click login.");
		homePage.login(email,password);
		extentManager.getTest().info("Get signed in user.");
		homePage.verifyUser(expectedUser);
		
		extentManager.getTest().info("Assert if correct user is logged in.");
		Assert.assertEquals(expectedUser, "Dean");

	}

}
