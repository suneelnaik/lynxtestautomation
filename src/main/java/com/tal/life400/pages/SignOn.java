package com.tal.life400.pages;

import org.testng.Assert;
import org.testng.ITestContext;

import com.ibm.eNetwork.ECL.ECLErr;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.utilities.mainframe.Mainframe_PageBase;

public class SignOn extends Mainframe_PageBase{
	private int HeaderP = 31;
	private int HeaderL = 19;
	private int CommandP = 1;
	private String HeaderT = "Sign-on Information";
	
	public SignOn(ITestContext testContext) {
		super(testContext);
	}
	
	
	public void navigateToPDMPage() throws ECLErr {
		// Check pre-condition:
		Assert.assertTrue(verifyPageExist());
		
		// Do Steps:
		super.writeAtPosition("[enter]", CommandP);
		super.waitUntilInputTimeOut(5000);
		
		if(super.searchAnywhere("Display Program Messages")>0) {
			super.writeAtCurrentPosition("[enter]");
			super.waitUntilInputTimeOut(5000);
		}
		
		// Check post condition:
		PDM pdm = new PDM(testContext);
		Assert.assertTrue(pdm.verifyPageExist());
		
		// Report and Take Screenshot:
		super.writeExtentCommentWithScreenshot("Navigated to PDM page.", LogStatus.PASS);
	}	
	
	public boolean verifyPageExist() throws ECLErr {
		return super.readText(HeaderP, HeaderL).equals(HeaderT);
	}
	
}
