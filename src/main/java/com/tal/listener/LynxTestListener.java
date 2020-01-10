package com.tal.listener;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.ibm.eNetwork.ECL.ECLSession;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.report.Database;
import com.tal.utilities.common.Environment;
import com.tal.utilities.common.PropertyFileRead;
import com.tal.utilities.mainframe.Mainframe_Screenshot;

public class LynxTestListener extends TestListenerAdapter {

	Database db = new Database();
	PropertyFileRead prop=new PropertyFileRead();
	Boolean dbReporting=Boolean.parseBoolean(prop.FileRead("Framework.properties", "DatabaseReporting"));

	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: MainframeExtentListener
	 * @Description : Constructor Calling Parent Class Constructor
	 ************************************************************/
	public LynxTestListener() {
		super();
	}

	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: onStart
	 * @Description : Overriding the onStart Method for Loading the Property files
	 *              and Database connection
	 ************************************************************/
	public void onStart(ITestContext iTestContext) {
		
	}

	@Override
	public void onTestStart(ITestResult result) {
		
	}

	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: onTestFinish
	 * @Description : Overriding the onTestFinish Method for closing any browser
	 *              instance and connection objects
	 ************************************************************/
	
	public void onTestFinish(ITestResult iTestResult) {

	}

	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: onFinish
	 * @Description : Overriding the onFinish Method for closing any browser
	 *              instance and connection objects
	 ************************************************************/
	public void onFinish(ITestContext iTestContext) {
				
	}
	
	@Override
	public void onTestFailure(ITestResult iTestResult) {
		// Get the test Context
		ITestContext testContext = iTestResult.getTestContext();
		ExtentTest extentTest = (ExtentTest) testContext.getAttribute("ExtentTest");
		String Base64Image = "";
		
		if(testContext.getAttribute("TestTech").toString().equals("Mainframe")) {
			ECLSession session = (ECLSession) testContext.getAttribute("Session");
			
			// Get Screenshot on the last available screen	
			try {
				Mainframe_Screenshot mfScreenshot = new Mainframe_Screenshot();
				Base64Image = mfScreenshot.createScreenshot(session);
			} catch (IOException e) {
				System.out.println("There was an error trying to capture the screenshot.");
				e.printStackTrace();
			}

		}else if(testContext.getAttribute("TestTech").toString().equals("Web")) {
			WebDriver driver = (WebDriver) testContext.getAttribute("driver");
			Base64Image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		}
		

		// Construct the comments
		String testName = testContext.getName();
		if (!(iTestResult.getThrowable() == null)) {
			extentTest.log(LogStatus.FAIL, "<html><body><font color=\"red\">Failure Cause</font></body></html>", iTestResult.getThrowable());
		} else {
			extentTest.log(LogStatus.FAIL, testName, "No error message avaialble !!");
		}
		
		if(dbReporting) {
			int testID=updateDB(iTestResult, "FAIL", ExceptionUtils.getStackTrace(iTestResult.getThrowable()));
			db.UpsertScreenshot(Base64Image, testID);
		}
		
		extentTest.log(LogStatus.FAIL, "<html><body><font color=\"red\">Failure Screenshot</font></body></html>", 
				extentTest.addBase64ScreenShot("data:image/png;base64," + Base64Image));
	}
	
	@Override
	public void onTestSuccess(ITestResult result) {
		if(dbReporting) {
			String Base64Image = "";
			if(result.getTestContext().getAttribute("TestTech").toString().equals("Mainframe")){
				ECLSession session = (ECLSession) result.getTestContext().getAttribute("Session");
				
					// Get Screenshot on the last available screen	
					try {
						Mainframe_Screenshot mfScreenshot = new Mainframe_Screenshot();
						Base64Image = mfScreenshot.createScreenshot(session);			
					} catch (IOException e) {
						System.out.println("There was an error trying to capture the screenshot.");
						e.printStackTrace();
					}
				} else if(result.getTestContext().getAttribute("TestTech").toString().equals("Web")){
					WebDriver driver = (WebDriver) result.getTestContext().getAttribute("driver");
					Base64Image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
				}
			int testID=updateDB(result, "PASS", "Test Successfully Exceuted");
			db.UpsertScreenshot(Base64Image, testID);
		}
	}
	
	//Computes and updates suite and test meta data
		public int updateDB(ITestResult result, String status, String message) {
			Environment e=new Environment();
			String SuiteName=result.getTestContext().getSuite().getParameter("SuiteName");
			int SuiteID =(int)result.getTestContext().getSuite().getAttribute("SuiteId");
			String Documentation=result.getTestContext().getCurrentXmlTest().getParameter("Documentation");
			Timestamp StartTime=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Timestamp(result.getStartMillis())));
			Timestamp EndTime=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Timestamp(result.getEndMillis())));
			String ShortName=result.getTestContext().getCurrentXmlTest().getParameter("ShortName");
			String Message=message;
			String Status=status;
			String Tags=result.getTestContext().getCurrentXmlTest().getParameter("Tags");
			String Environment=e.FileRead("Name");
			String Portfolio=result.getTestContext().getCurrentXmlTest().getParameter("Portfolio");
			String Application=result.getTestContext().getCurrentXmlTest().getParameter("Application");
			String FunctionalArea=result.getTestContext().getCurrentXmlTest().getParameter("FunctionalArea");
			String TestType=result.getTestContext().getCurrentXmlTest().getParameter("TestType");
			String Feature=result.getTestContext().getCurrentXmlTest().getParameter("Feature");
			String Keywords=result.getTestContext().getCurrentXmlTest().getParameter("Keywords");
			
			//Upsert Passed Test to DB			
			int testID=db.UpsertTest(SuiteName, SuiteID, Documentation, StartTime, EndTime, ShortName, Message,
					Status, Tags, Environment, Portfolio, Application, FunctionalArea, TestType, Feature, Keywords);
			
			return testID;
		}
}
