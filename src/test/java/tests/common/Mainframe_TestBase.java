package tests.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
// Standard Java Libraries
import java.util.Properties;

import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

// HACL Libraries
import com.ibm.eNetwork.ECL.ECLErr;
import com.ibm.eNetwork.ECL.ECLSession;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.report.Database;
//Extent Reports Libraries
import com.tal.report.ExtentReport;
import com.tal.utilities.common.Environment;
import com.tal.utilities.common.PropertyFileRead;

public abstract class Mainframe_TestBase {

	protected ECLSession session = null;
	protected Properties session_properties = new Properties();
	protected String SuiteName;
	protected int SuiteID;
	protected String Doc;
	protected Timestamp StartTime;
	protected Timestamp EndTime;
	protected String Environment;
	protected String BuildLink;
	protected Database db = new Database();
	protected Environment e=new Environment();
	protected PropertyFileRead prop=new PropertyFileRead();
	protected Boolean dbReporting=Boolean.parseBoolean(prop.FileRead("Framework.properties", "DatabaseReporting"));

	protected Mainframe_TestBase() {

	}

	protected void Connect(String HostName) throws ECLErr {
		this.session_properties.put(ECLSession.SESSION_HOST, HostName);
		this.session_properties.put(ECLSession.SESSION_AUTOCONNECT, "true");
		this.session = new ECLSession(session_properties);
		this.session.StartCommunication();

	}

	protected void Disconnect() {
		if (this.session != null) {
			this.session.disconnect();
		}
			
	}
	
	@BeforeSuite
	protected void BeforeSuite(ITestContext testContext) {
		SuiteName=testContext.getSuite().getName();
		Doc=testContext.getSuite().getParameter("Documentation");
		StartTime=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		EndTime=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		Environment=e.FileRead("Name");
		BuildLink=testContext.getSuite().getParameter("BuildLink");
		if(dbReporting)
			SuiteID=db.UpsertSuite(SuiteName, 0, Doc, StartTime, EndTime, Environment, BuildLink);
		ExtentReport.startReport(SuiteID,SuiteName,Doc,Environment);
		testContext.getSuite().setAttribute("SuiteId",SuiteID);
	}

	
	@BeforeMethod
	protected void BeforeMethod(ITestContext testContext) throws ECLErr, InterruptedException {
		this.Connect(e.FileRead("Life400Hostname"));
		testContext.setAttribute("Session", session);
		String[] categories=testContext.getCurrentXmlTest().getParameter("TestType").split(",");
		ExtentTest extentTest = ExtentReport.startTest(testContext.getName(),categories );
		extentTest.setDescription(testContext.getCurrentXmlTest().getParameter("Documentation"));
		testContext.setAttribute("ExtentTest", extentTest);	
		testContext.setAttribute("TestTech", "Mainframe");
	}
	
		
	@AfterMethod
	protected void AfterMethod(ITestContext testContext) {
		ExtentTest extentTest = (ExtentTest) testContext.getAttribute("ExtentTest");
		if (extentTest.getRunStatus().equals(LogStatus.UNKNOWN))
		{
			extentTest.log(LogStatus.SKIP, "This test method is skipped");
		}
		ExtentReport.endTest(extentTest);
		this.Disconnect();
	}

	@AfterSuite
	protected void AfterSuite(ITestContext ctx) {
		ExtentReport.endReport();
		EndTime=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		if(dbReporting)
			db.UpsertSuite(SuiteName, SuiteID, Doc, StartTime, EndTime, Environment, BuildLink);
		ctx.getSuite().removeAttribute("SuiteId");
	} 

}
