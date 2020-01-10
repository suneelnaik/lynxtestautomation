package com.tal.life400.pages;

import org.testng.Assert;
import org.testng.ITestContext;

import com.ibm.eNetwork.ECL.ECLErr;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.utilities.mainframe.Mainframe_PageBase;

public class S0017_Life400 extends Mainframe_PageBase {
	private int HeaderP = 36;
	private int HeaderL = 8;
	private int ConEnqSubCmdP = 970;
	private String HeaderT= "LIFE/400";
	
	public S0017_Life400(ITestContext testContext) {
		super(testContext);	
	}
	
	public void navigateToS0018EnquiriesMasterPage() throws ECLErr {
		// Check pre-condition:
		Assert.assertTrue(verifyPageExist());
		
		// Do Steps:
		super.writeAtPosition("[enter]", ConEnqSubCmdP);
		super.waitUntilInputTimeOut(5000);
		
		// Check post condition:
		S0018_EnquiriesMaster s0018enquiriesmaster = new S0018_EnquiriesMaster(testContext);
		Assert.assertTrue(s0018enquiriesmaster.verifyPageExist());
		
		// Report and Take Screenshot:
		super.writeExtentCommentWithScreenshot("Navigated to S0018 Enquiries Master menu page.", LogStatus.PASS);
	}
	
	
	public boolean verifyPageExist() throws ECLErr {
		return super.readText(HeaderP, HeaderL).equals(HeaderT);
	}
}
