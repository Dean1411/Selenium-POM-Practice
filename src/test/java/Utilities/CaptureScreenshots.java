package Utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class CaptureScreenshots {
	
	public static String captureScreenshot(WebDriver driver, String testName) {
		
		TakesScreenshot ts = (TakesScreenshot) driver;
		File sources = ts.getScreenshotAs(OutputType.FILE);
		
		String path =  System.getProperty("user.dir") + "/screenshots/" + testName + ".png";
		
		File destination = new File(path);
		destination.getParentFile().mkdirs();
		
		try {
			FileUtils.copyFile(sources, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return path;
	}

}
