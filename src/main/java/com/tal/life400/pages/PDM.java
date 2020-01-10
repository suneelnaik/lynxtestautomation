package com.tal.life400.pages;

import org.testng.Assert;
import org.testng.ITestContext;

import com.ibm.eNetwork.ECL.ECLErr;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.utilities.mainframe.Mainframe_PageBase;

public class PDM extends Mainframe_PageBase{
	private int HeaderP = 26;
	private int HeaderL = 37;
	private int CommandP = 1527;
	private String HeaderT= "Programming Development Manager (PDM)";
	
	public PDM(ITestContext testContext) {
		super(testContext);	
	}
	
	public void navigateToLife400MasterPage() throws ECLErr {
		// Check pre-condition:
		Assert.assertTrue(verifyPageExist());
		
		// Do Steps:
		super.writeAtPosition("menu[enter]", CommandP);
		super.waitUntilInputTimeOut(5000);
		
		// Check post condition:
		Life400Master life400master = new Life400Master(testContext);
		Assert.assertTrue(life400master.verifyPageExist());
		
		// Report and Take Screenshot:
		super.writeExtentCommentWithScreenshot("Navigated to Life400 Master menu page.", LogStatus.PASS);
	}
	
	public boolean verifyPageExist() throws ECLErr {
		return super.readText(HeaderP, HeaderL).equals(HeaderT);
	}
	
}
