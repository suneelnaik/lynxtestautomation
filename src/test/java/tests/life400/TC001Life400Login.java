package tests.life400;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.ibm.eNetwork.ECL.ECLErr;
import com.tal.life400.pages.Life400Master;
import com.tal.life400.pages.LoginScreen;
import com.tal.life400.pages.PDM;
import com.tal.life400.pages.S0017_Life400;
import com.tal.life400.pages.S0018_EnquiriesMaster;
import com.tal.life400.pages.S6363_ContractEnquiry;
import com.tal.life400.pages.S6838_ContractDetails;
import com.tal.life400.pages.SignOn;
import com.tal.utilities.common.ExcelUtils;

import tests.common.Mainframe_TestBase;

public class TC001Life400Login extends Mainframe_TestBase {

	@Test(dataProvider = "Authentication")
	public void validLogin1(String sUserName, String sPassword, String sContract, String sStatus, ITestContext testContext) throws InterruptedException, ECLErr, AssertionError {
		// What pages are being used?
		LoginScreen loginScreen = new LoginScreen(testContext);
		SignOn signon = new SignOn(testContext);
		PDM pdm = new PDM(testContext);
		Life400Master life400master = new Life400Master(testContext);
		S0017_Life400 s0017life400 = new S0017_Life400(testContext);
		S0018_EnquiriesMaster s0018enquiriesmaster = new S0018_EnquiriesMaster(testContext);
		S6363_ContractEnquiry s6363contractenquiry = new S6363_ContractEnquiry(testContext);
		S6838_ContractDetails s6838contractdetails = new S6838_ContractDetails(testContext);
		
		// What are the steps?
		loginScreen.LoginAsUser(sUserName, sPassword);
		signon.navigateToPDMPage();
		pdm.navigateToLife400MasterPage();
		life400master.navigateToS0017Life400Page();
		Assert.assertTrue(false);
		s0017life400.navigateToS0018EnquiriesMasterPage();
		s0018enquiriesmaster.navigateToS6363ContractEnquiryPage();
		s6363contractenquiry.getContractDetails(sContract);
		s6838contractdetails.verifyPremiumStatus(sStatus);
	}

	@DataProvider
	public Object[][] Authentication() throws Exception {
		System.out.println("data provider Method");
		// Get the data file
		String file = System.getProperty("user.dir") + "/TestData.xlsx";
		
		// Set the sheet
		String sheet = "Authentication";
		String currenttest = this.toString();

		// Fetch an object containing all the data rows specific to this test case
		ExcelUtils.setExcelFile(file, sheet);
		String sTestCaseName = ExcelUtils.getTestCaseName(currenttest);
		int iTestCaseRow = ExcelUtils.getRowContains(sTestCaseName, 0);
		Object[][] testObjArray = ExcelUtils.getTableArray(file, sheet, iTestCaseRow,4);

		return (testObjArray);
	}
}
