package com.tal.life400.pages;

import org.testng.Assert;
import org.testng.ITestContext;

import com.ibm.eNetwork.ECL.ECLErr;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.utilities.mainframe.Mainframe_PageBase;

public class S6838_ContractDetails extends Mainframe_PageBase {
	private int HeaderP = 33;
	private int HeaderL = 16;
	private String HeaderT= "Contract Details";
	private int PremiumStatusP = 209;
	private int PremiumStatusL = 15;
	
	public S6838_ContractDetails(ITestContext testContext) {
		super(testContext);	
	}
	
	public void verifyPremiumStatus(String status) throws ECLErr {
		// Check pre-condition:
		Assert.assertTrue(verifyPageExist());
		
		// Do Steps:
		Assert.assertTrue(super.readText(PremiumStatusP, PremiumStatusL).equals(status));
		
		// Check post condition:
		
		
		// Report and Take Screenshot:
		super.writeExtentCommentWithScreenshot("Premium Status verified as : "+super.readText(PremiumStatusP, PremiumStatusL), LogStatus.PASS);
	}
	
	public boolean verifyPageExist() throws ECLErr {
		return super.readText(HeaderP, HeaderL).equals(HeaderT);
	}
}
