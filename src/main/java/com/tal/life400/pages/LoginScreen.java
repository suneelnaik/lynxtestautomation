package com.tal.life400.pages;

import org.testng.Assert;
import org.testng.ITestContext;

import com.ibm.eNetwork.ECL.ECLErr;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.utilities.mainframe.Mainframe_PageBase;

public class LoginScreen extends Mainframe_PageBase {
	private int HeaderP = 150;
	private int HeaderL = 6;
	private String HeaderT= "MERLIN";
	private int UsernameP = 1618;
	private int PasswordP = 1646;

	public LoginScreen(ITestContext testContext) {
		super(testContext);	
	}

	public void LoginAsUser(String UserName, String Password) throws InterruptedException, ECLErr {

		// Check pre-condition:
		super.waitUntilInputTimeOut(5000);
		Assert.assertTrue(verifyPageExist());
		
		// Do Steps:
		super.waitUntilInputTimeOut(5000);
		super.writeAtPosition(UserName, UsernameP);
		super.writeAtPosition(Password, PasswordP);
		super.writeAtCurrentPosition("[enter]");
		super.waitUntilInputTimeOut(5000);
		
			
		// Check post condition:
		SignOn signon = new SignOn(testContext);
		Assert.assertTrue(signon.verifyPageExist());
				
		// Report and Take Screenshot:
		super.writeExtentCommentWithScreenshot("Logged in with User " + UserName, LogStatus.PASS);
		
		
	}
	
	public boolean verifyPageExist() throws ECLErr {
		return super.readText(HeaderP, HeaderL).equals(HeaderT);
	}
	
}
