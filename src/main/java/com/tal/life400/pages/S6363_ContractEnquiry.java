package com.tal.life400.pages;

import org.testng.Assert;
import org.testng.ITestContext;

import com.ibm.eNetwork.ECL.ECLErr;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.utilities.mainframe.Mainframe_PageBase;

public class S6363_ContractEnquiry extends Mainframe_PageBase {
	private int HeaderP = 32;
	private int HeaderL = 24;
	private int ContractNoP = 844;
	private int ActionCmdP = 1724;
	private String HeaderT= "Contract Enquiry Submenu";
	
	public S6363_ContractEnquiry(ITestContext testContext) {
		super(testContext);	
	}
	
	public void getContractDetails(String ContractId) throws ECLErr {
		// Check pre-condition:
		Assert.assertTrue(verifyPageExist());
		
		// Do Steps:
		super.writeAtPosition(ContractId, ContractNoP);
		super.writeAtPosition("A[enter]", ActionCmdP);
		super.waitUntilInputTimeOut(5000);
		
		// Check post condition:
		S6838_ContractDetails s6838contractdetails = new S6838_ContractDetails(testContext);
		Assert.assertTrue(s6838contractdetails.verifyPageExist());
		
		// Report and Take Screenshot:
		super.writeExtentCommentWithScreenshot("Navigated to S6838 Contract Details page.", LogStatus.PASS);
	}
	
	public boolean verifyPageExist() throws ECLErr {
		return super.readText(HeaderP, HeaderL).equals(HeaderT);
	}
}
