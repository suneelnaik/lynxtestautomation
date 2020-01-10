package tests.life400;

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
import com.tal.report.Database;

import tests.common.Mainframe_TestBase;

public class TC003DBDataProvider extends Mainframe_TestBase {

	@Test(dataProvider = "MySQL")
	public void validLogin3(String sUserName, String sPassword, String sContract, String sStatus, ITestContext testContext) throws InterruptedException, ECLErr, AssertionError {
		dummy();
		
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
		s0017life400.navigateToS0018EnquiriesMasterPage();
		s0018enquiriesmaster.navigateToS6363ContractEnquiryPage();
		s6363contractenquiry.getContractDetails(sContract);
		s6838contractdetails.verifyPremiumStatus(sStatus);
	}

	@DataProvider
	public String[][] MySQL()
	{	Database db = new Database();
		return db.readDB("select data1, data2, data3, data4 from testdata where testname=\"TC003DBDataProvider\" LIMIT 1");
		
	}
	
	public void  dummy()
	{	
		System.out.println("dummy method");
	}
	
}
