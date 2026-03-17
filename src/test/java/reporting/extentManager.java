package reporting;

import com.aventstack.extentreports.ExtentTest;

public class extentManager {
	
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	
	public static ExtentTest getTest() {
		return test.get();
	}
	
	public static void setTest(ExtentTest extentTest) {
		test.set(extentTest);
	}

}
