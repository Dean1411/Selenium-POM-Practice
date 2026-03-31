package reporting;

import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import Utilities.CaptureScreenshots;
import base.Base;
import baseTest.baseTest;


public class TestListener extends baseTest implements ITestListener{
	
	public static ExtentTest test;
	public static Utilities.CaptureScreenshots utils;
	
	
	@Override
	public void onTestStart(ITestResult result) {
		
		test = baseTest.extent.createTest(result.getMethod().getMethodName());
		
		extentManager.setTest(test);
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		test.log(Status.PASS, "Test Passed");
	}
	
	@Override
	public void onTestFailure(ITestResult result) {
		
	    WebDriver driver = Base.getDriver();
	    
	    String screenshot = CaptureScreenshots.captureScreenshot(driver, result.getName());


	    extentManager.getTest().fail(result.getThrowable())
	                 .addScreenCaptureFromPath(screenshot);
	}
	
	@Override
	public void onTestSkipped(ITestResult result) {
		test.log(Status.SKIP, "Test Skipped");
	}
	
	

}
