package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import base.Base;

public class report extends Base {
	
	public static ExtentReports extent;
	
	public static ExtentReports getInstance() {
		
	    ExtentSparkReporter spark = new ExtentSparkReporter("src/test/resources/reports/extentReport.html");
	    
        spark.config().setReportName("Automation Report");
        spark.config().setDocumentTitle("Test Results");
        
	    extent = new ExtentReports();
	    extent.attachReporter(spark);
	    
	    extent.setSystemInfo("Tester", "Dean Aschendorf");
	    extent.setSystemInfo("Environment", "Development");
	    extent.setSystemInfo("Browser", Base.getBrowser());
	    
		return extent;
	}

}
