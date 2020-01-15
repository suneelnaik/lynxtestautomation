package tests.sample;

import java.net.UnknownHostException;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.tal.web.pages.SamplePage;

import tests.common.Web_TestBase;

public class sampleTest  {

	@Test
	public void loginOCMS(ITestContext testContext) throws UnknownHostException, InterruptedException{
		// What pages are being used?
		//SamplePage sample= new SamplePage(testContext);
		
		//steps
		//sample.loginToOCMSAdmin("opensourcecms", "opensourcecms");
		System.out.println("Hello World! Test Passed");
	}
}
