package com.tal.life400.pages;

import org.testng.Assert;
import org.testng.ITestContext;

import com.ibm.eNetwork.ECL.ECLErr;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.utilities.mainframe.Mainframe_PageBase;

public class S0018_EnquiriesMaster extends Mainframe_PageBase {
	private int HeaderP = 32;
	private int HeaderL = 21;
	private static String HeaderT= "Enquiries Master Menu";
	private static int ContractDetailsCmd = 653;
	
	public S0018_EnquiriesMaster(ITestContext testContext) {
		super(testContext);	
	}
	
	public void navigateToS6363ContractEnquiryPage() throws ECLErr {
		// Check pre-condition:
		Assert.assertTrue(verifyPageExist());
		
		// Do Steps:
		super.writeAtPosition("[enter]", ContractDetailsCmd);
		super.waitUntilInputTimeOut(5000);
		
		// Check post condition:
		S6363_ContractEnquiry s6363contractenquiry = new S6363_ContractEnquiry(testContext);
		Assert.assertTrue(s6363contractenquiry.verifyPageExist());
		
		// Report and Take Screenshot:
		super.writeExtentCommentWithScreenshot("Navigated to S6363 Contract Enquiry page.", LogStatus.PASS);
	}
		
	public boolean verifyPageExist() throws ECLErr {
		return super.readText(HeaderP, HeaderL).equals(HeaderT);
	}
}
