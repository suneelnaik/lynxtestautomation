package com.tal.report;

import java.io.File;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class ExtentReport {
	
	private static ExtentReports extent;
	//private static ExtentTest test;
	
	public static void startReport(int SuiteId, String SuiteName, String SuiteDoc, String Environement) {
		String suitefilename=SuiteName.replaceAll("\\s+","");
		extent = new ExtentReports(System.getProperty("user.dir") + "/target/Extent/ExtentReport_"+suitefilename+".html", true);
		extent.loadConfig(new File("ReportConfiguration.xml"));
		extent.addSystemInfo("Suite Number", String.valueOf(SuiteId));
		extent.addSystemInfo("Suite Name", SuiteName);
		extent.addSystemInfo("Suite Details", SuiteDoc);
		extent.addSystemInfo("Environement", Environement);
	}
	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: endReport
	 * @Description : Function to end and Flush the extent report
	 ************************************************************/
	public static void endReport() {
		
		extent.flush();
		extent.close();
		
	}
	
	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: startTest
	 * @Description : Function to start the extent report
	 ************************************************************/
	public static ExtentTest startTest(String testname, String[] groups) {
		ExtentTest test;
		test = extent.startTest(testname);
		for (String group : groups)
			test.assignCategory(group);
		
		return test;
	}
	
	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: endTest
	 * @Description : Function to end the extent logger test.
	 ************************************************************/
	public static void endTest(ExtentTest test) {
		extent.endTest(test);
	}
		
}
