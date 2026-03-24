package base;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.log4testng.Logger;

import com.aventstack.extentreports.ExtentReports;

import io.github.bonigarcia.wdm.WebDriverManager;


public class Base {
	
	protected static WebDriver driver;
	public static FileReader fr;
	public static Properties prop = new Properties();
	public static WebDriverWait wait;
	public static Actions act;
	public static String browser;
	public static Logger log = Logger.getLogger(Base.class);
	public static ExtentReports report;
	
	
	public static WebDriver getDriver() {
		return driver;
	}
	
	public WebElement fluentWait(By locator) {

	    Wait<WebDriver> fluentWait = new FluentWait<>(driver)
	            .withTimeout(Duration.ofSeconds(20))
	            .pollingEvery(Duration.ofSeconds(2))
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class);

	    return fluentWait.until(driver -> driver.findElement(locator));
	}
		
	
	@BeforeMethod
	public void setup() throws IOException {
		
		if(driver == null) {				
				fr = new FileReader("src/test/resources/config/config.properties");	
				prop.load(fr);
				log.info("file successfully loaded");
		}
		
		browser = prop.getProperty("browser");
		
		switch(browser) {
		case "chrome":
			ChromeOptions opt = new ChromeOptions();
			opt.addArguments("--incognito");
			opt.addArguments("--disable-notifications");
			opt.addArguments("--start-maximized");
			//opt.addExtensions(new File("./Extensions/AdBlock.crx"));

			Map<String, Object> prefs = new HashMap<>();
			prefs.put("profile.default_content_setting_values.notifications", 2);
			opt.setExperimentalOption("prefs", prefs);
			
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(opt);
			driver.manage().window().maximize();
			driver.get(prop.getProperty("url"));
			driver.manage().deleteAllCookies();
			wait = new WebDriverWait(driver, Duration.ofSeconds(15));
			act = new Actions(driver);
			break;
		default:
			log.warn("Incorrect driver selected.");	
			break;
		}
	}
	
		
	@AfterMethod
	public void tearDown() {
		
		if(driver != null) {
			driver.quit();
			driver = null;
		}
	}
}
