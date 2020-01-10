package com.tal.life400.pages;

import org.testng.Assert;
import org.testng.ITestContext;

import com.ibm.eNetwork.ECL.ECLErr;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.utilities.mainframe.Mainframe_PageBase;

public class Life400Master extends Mainframe_PageBase {
	private int HeaderP = 30;
	private int HeaderL = 19;
	private int CommandP = 1532;
	private String HeaderT= "Life400 Master Menu";
	
	public Life400Master(ITestContext testContext) {
		super(testContext);	
	}
	
	public void navigateToS0017Life400Page() throws ECLErr {
		// Check pre-condition:
		Assert.assertTrue(verifyPageExist());
		
		// Do Steps:
		super.writeAtPosition("A[enter]", CommandP);
		super.waitUntilInputTimeOut(5000);
		
		// Check post condition:
		S0017_Life400 s0017life400 = new S0017_Life400(testContext);
		Assert.assertTrue(s0017life400.verifyPageExist());
		
		// Report and Take Screenshot:
		super.writeExtentCommentWithScreenshot("Navigated to S0017 Life400 page.", LogStatus.PASS);
	}
	
	public boolean verifyPageExist() throws ECLErr {
		return super.readText(HeaderP, HeaderL).equals(HeaderT);
	}
	
}
