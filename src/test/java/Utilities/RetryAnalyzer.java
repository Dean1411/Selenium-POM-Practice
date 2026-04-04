package Utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
	
	int retryCount = 0;
	int max_retries = 2;
	
	public boolean retry(ITestResult result) {
		
		if(retryCount < max_retries) {
			retryCount++;
			return true;
		}
		
		return false;
	}

}
