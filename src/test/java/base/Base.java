package base;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.beust.jcommander.Parameter;

import io.github.bonigarcia.wdm.WebDriverManager;


public class Base {
	
	protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	protected static ThreadLocal<String> browser = new ThreadLocal<>();
	protected static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();
	protected static ThreadLocal<Actions> act = new ThreadLocal<>();
	public static final Properties prop = new Properties();
	public static FileReader fr;
	public static final Logger log = LogManager.getLogger(Base.class);
	public static ExtentReports report;
	
	
	
	public static WebDriver getDriver() {
		return driver.get();
	}
	
	public static WebDriverWait getWait() {
		return wait.get();
	}
	
	public static Actions getActions() {
		return act.get();
	}
	
	public static String getBrowser() {
		return browser.get();
	}
	
	public WebElement fluentWait(By locator) {

	    Wait<WebDriver> fluentWait = new FluentWait<>(getDriver())
	            .withTimeout(Duration.ofSeconds(10))
	            .pollingEvery(Duration.ofSeconds(3))
	            .ignoring(NoSuchElementException.class)
	            .ignoring(StaleElementReferenceException.class);

	    return fluentWait.until(d -> d.findElement(locator));
	}
		
	
	@BeforeMethod
	@Parameters({"browser","OS"})
	public void setup(@Optional("chrome") String browserName, String osName) throws IOException {
		
		try {
			browser.set(browserName);
			
			// FIXED: Synchronized block ensures only ONE thread loads the properties file at a time
			// and the FileReader 'fr' is isolated locally within this method scope.
			synchronized (prop) {
				if(prop.isEmpty()) {				
					try (FileReader fr = new FileReader("src/test/resources/config/config.properties")) {
						prop.load(fr);
						log.info("Properties file successfully loaded");
					}
				}
			}
			
			String hubUrl = prop.getProperty("huburl");
			
			// Remote or Grid Test Env options		
			if(prop.getProperty("test_env").equalsIgnoreCase("remote")) {
				
				DesiredCapabilities cap = new DesiredCapabilities();
				
				//OS
				if(osName.equalsIgnoreCase("linux")) {
					cap.setPlatform(Platform.LINUX);
				}else if (osName.equalsIgnoreCase("mac")) {
					cap.setPlatform(Platform.MAC);
				}else if (osName.equalsIgnoreCase("windows")) {
					cap.setPlatform(Platform.WINDOWS);
				}else {
					log.info("OS not matching");
				}
				
				//Browser
				switch(browserName.toLowerCase()) {
					case "chrome":
						cap.setBrowserName("chrome");
						break;
					case "edge":					
						cap.setBrowserName("edge");
					    break;
					case "firefox":
						cap.setBrowserName("firefox");	
					    break;
					default:
						throw new IllegalArgumentException("Incorrect remote driver selected: " + browserName);
					}
				
					driver.set(new RemoteWebDriver(new URL(hubUrl), cap));		
					
				    getDriver().manage().deleteAllCookies();
				    getDriver().manage().window().maximize();
				    getDriver().get(prop.getProperty("url"));

				    wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(20)));
				    act.set(new Actions(getDriver()));
			}
			
			//Local test environment
			if(prop.getProperty("test_env").equalsIgnoreCase("local")) {
				
				switch(browserName) {
				case "chrome":
					ChromeOptions opt = new ChromeOptions();
					opt.addArguments("--incognito");
					opt.addArguments("--disable-notifications");
					opt.addArguments("--start-maximized");

					Map<String, Object> prefs = new HashMap<>();
					prefs.put("profile.default_content_setting_values.notifications", 2);
					opt.setExperimentalOption("prefs", prefs);
					
					WebDriverManager.chromedriver().setup();
					driver.set(new ChromeDriver(opt));
					getDriver().manage().window().maximize();
					getDriver().get(prop.getProperty("url"));
					getDriver().manage().deleteAllCookies();
					wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(20)));
					act.set(new Actions(getDriver()));
					break;
				case "chrome-headless":
				    ChromeOptions chromeHeadless = new ChromeOptions();
				    chromeHeadless.addArguments("--headless=new");
				    chromeHeadless.addArguments("--disable-gpu");
				    chromeHeadless.addArguments("--window-size=1920,1080");
				    chromeHeadless.addArguments("--disable-notifications");

				    WebDriverManager.chromedriver().setup();
				    driver.set(new ChromeDriver(chromeHeadless));
				    
				    getDriver().manage().deleteAllCookies();
				    getDriver().get(prop.getProperty("url"));

				    wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(15)));
				    act.set(new Actions(getDriver()));
					break;
				case "edge":

				    EdgeOptions edgeOptions = new EdgeOptions();
				    edgeOptions.addArguments("--start-maximized");

				    WebDriverManager.edgedriver().setup();
				    driver.set(new EdgeDriver(edgeOptions));
				    
				    getDriver().manage().deleteAllCookies();
				    getDriver().get(prop.getProperty("url"));

				    wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(15)));
				    act.set(new Actions(getDriver()));

				    break;
				case "edge-headless":

				    EdgeOptions edgeHeadless = new EdgeOptions();
				    edgeHeadless.addArguments("--headless=new");
				    edgeHeadless.addArguments("--window-size=1920,1080");

				    WebDriverManager.edgedriver().setup();
				    driver.set(new EdgeDriver(edgeHeadless));
				    
				    getDriver().manage().deleteAllCookies();
				    getDriver().get(prop.getProperty("url"));

				    wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(15)));
				    act.set(new Actions(getDriver()));

				    break;
				case "firefox":

				    FirefoxOptions firefoxOptions = new FirefoxOptions();
				    firefoxOptions.addArguments("--start-maximized");

				    WebDriverManager.firefoxdriver().setup();
				    driver.set(new FirefoxDriver(firefoxOptions));
				    
				    getDriver().manage().deleteAllCookies();
				    getDriver().get(prop.getProperty("url"));

				    wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(15)));
				    act.set(new Actions(getDriver()));

				    break;
				case "firefox-headless":

				    FirefoxOptions firefoxHeadless = new FirefoxOptions();
				    firefoxHeadless.addArguments("--headless");

				    WebDriverManager.firefoxdriver().setup();
				    driver.set(new FirefoxDriver(firefoxHeadless));
				    
				    getDriver().manage().deleteAllCookies();
				    getDriver().get(prop.getProperty("url"));

				    wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(15)));
				    act.set(new Actions(getDriver()));

				    break;
				default:
					log.warn("Incorrect driver selected.");	
					break;
				}
			}
		}catch(Throwable t) {
			
	        System.err.println("!!! SETUP FAILED FOR BROWSER: " + browserName + " !!!");
	        t.printStackTrace();
	        throw new RuntimeException(t);
		}
			
	}
					
	@AfterMethod
	public void tearDown() {
		
		if(getDriver() != null) {
			getDriver().quit();
			driver.remove();
			browser.remove();
			wait.remove();
			act.remove();
		}
	}
}
