package baseTest;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;

import base.Base;

public class baseTest extends Base {
	
	public static ExtentReports extent;
	
	@BeforeSuite
	public void setUpReport() {
		
		if(extent == null) {
			extent = reporting.report.getInstance();
		}
	}
	
	public WebElement find(By element) {
		
		return driver.findElement(element);
	}
	
	public List<WebElement> findAll(By element){
		
		return wait.until(ExpectedConditions.refreshed(
		        ExpectedConditions.visibilityOfAllElementsLocatedBy(element)
				));
	}
	
	public void selectMenuItem(By element,String txt) {
		removeGoogleAds();
		List<WebElement> menu = findAll(element);
		
		try {
			
			for(WebElement item: menu) {
				if(item.getText().trim().contains(txt)) {
					click(item);
					System.out.println(item.getText() + " clicked");
					break;
				}
			}
		}catch (StaleElementReferenceException ex) {
			System.out.println("Select Menu Item ex: " + ex.getCause());
		}
	}
	
	public boolean isLoggedIn(By element,String txt) {
		
		List<WebElement> menu = findAll(element);
		
		try {
			for(WebElement item: menu) {
				if(item.getText().trim().contains(txt)) {
					System.out.println("User logged in: " + item.getText());
					return true;
				}
			}
		}catch (Exception ex) {
			System.out.println("Is loggedin Exception: " + ex.getMessage());
		}
		
		return false;
	}
	
	public WebElement findByText(By locator, String txt) {

	    for (WebElement el : findAll(locator)) {
	        if (el.getText().trim().equalsIgnoreCase(txt)) {
	            return el;
	        }
	    }

	    throw new RuntimeException("Element not found in find by text: " + txt);
	}
	
	public String getPageTitle() {
		
		String title = driver.getTitle();
		System.out.println("Title: " + title);
		return title;
		
	}
	
	public void click(WebElement item) {
		
		try {
			wait.until(ExpectedConditions.
					elementToBeClickable(item)).click();
		}catch(ElementClickInterceptedException ex) {
			
	        System.out.println("Click intercepted — removing ads...");

	        removeGoogleAds();

	        wait.until(ExpectedConditions
	                .elementToBeClickable(item)).click();
		}

	}
	
//	public void click(By element) {
//		wait.until(ExpectedConditions.
//				elementToBeClickable(element)).click();
//	}
	
	public void click(By element) {

	    try {
	        wait.until(ExpectedConditions
	                .elementToBeClickable(element)).click();

	    } catch (ElementClickInterceptedException e) {

	        System.out.println("Click intercepted — removing ads...");

	        removeGoogleAds();

	        wait.until(ExpectedConditions
	                .elementToBeClickable(element)).click();
	    }
	}
	
	public void type(By element, String txt) {
		
		try {
			WebElement el = wait.until(ExpectedConditions.
					visibilityOfElementLocated(element));
			
			el.clear();
			el.sendKeys(txt);
		}catch(IllegalArgumentException ex) {
			log.warn(ex);
		}
	}
	
	public void selectByIndex(WebElement element, int num) {
		
		try {
			Select select = new Select(element);
			select.selectByIndex(num);
		}catch(NoSuchElementException ex) {
			log.warn(ex);
		}
	}
	
	public void selectByText(By locator, String txt) {		
		
		try {
			WebElement options = wait.until(
					ExpectedConditions.visibilityOfElementLocated(locator));
			
			Select select = new Select(options);
			select.selectByVisibleText(txt);
		}catch(NoSuchElementException ex) {
			log.warn(ex);
		}
	}
	
	public String getText(By locator) {
		
		String txt = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
		System.out.println("Text: " + txt);
		return txt;
	}
	
	public boolean isDisplayed(By locator) {
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		}catch(Exception ex) {
			log.warn("Exception: ", ex);
			return false;
		}
		return true;
	}
	
	public void acceptAlert() {
		
		driver.switchTo().alert().accept();
	}
	
	public void declineAlert() {
		driver.switchTo().alert().dismiss();
	}
	
	public void removeGoogleAds() {

	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    js.executeScript(
	        "document.querySelectorAll('a[href*=\"googleadservices\"],' +" +
	        "'a[href*=\"doubleclick\"]').forEach(el => el.remove());"
	    );
	}
	
	public void scrollAndClick(By locator) {

	    WebElement element = wait.until(
	        ExpectedConditions.presenceOfElementLocated(locator)
	    );

	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block: 'center'})",
	        element
	    );

	    wait.until(ExpectedConditions.elementToBeClickable(element)).click();
	}
	
	@AfterSuite
	public void tearDownReport() {
		extent.flush();
		
	    try {
	        File file = new File(System.getProperty("user.dir")
	                + "/src/test/resources/reports/extentReport.html");

	        Desktop.getDesktop().browse(file.toURI());
	    } 
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
