package tests.common;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
// Standard Java Libraries
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeMethod;

//Extent Reports Libraries
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.tal.report.Database;
import com.tal.report.ExtentReport;
import com.tal.utilities.common.Environment;
import com.tal.utilities.common.PropertyFileRead;

import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class Web_TestBase {
	protected WebDriver driver;
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

	protected Web_TestBase() {
	
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
	protected void BeforeMethod(ITestContext testContext) {
		String[] categories=testContext.getCurrentXmlTest().getParameter("TestType").split(",");
		ExtentTest extentTest = ExtentReport.startTest(testContext.getName(),categories );
		testContext.setAttribute("ExtentTest", extentTest);
		testContext.setAttribute("TestTech", "Web");
		this.startBrowser(testContext);
	}
	

		
	@AfterMethod
	protected void AfterMethod(ITestContext testContext) {
		ExtentTest extentTest = (ExtentTest) testContext.getAttribute("ExtentTest");
		if (extentTest.getRunStatus().equals(LogStatus.UNKNOWN))
		{
			extentTest.log(LogStatus.SKIP, "This test method is skipped");
		}
		ExtentReport.endTest(extentTest);
		this.stopBrowser(testContext);
	}

	@AfterSuite
	protected void AfterSuite(ITestContext ctx) {
		ExtentReport.endReport();
		EndTime=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		if(dbReporting)
			db.UpsertSuite(SuiteName, SuiteID, Doc, StartTime, EndTime, Environment, BuildLink);
		ctx.getSuite().removeAttribute("SuiteId");
	}
	
	protected void startBrowser(ITestContext ctx) {
		String browser = (String) ctx.getCurrentXmlTest().getParameter("Browser");
		String url=(String) ctx.getCurrentXmlTest().getParameter("URL");
		if(browser.equalsIgnoreCase("ie")) {
			WebDriverManager.iedriver().proxyUser(prop.FileRead("Framework.properties", "ProxyDomainAndUser")).proxyPass(prop.FileRead("Framework.properties", "ProxyPassword")).proxy(prop.FileRead("Framework.properties", "ProxyHostAndPort")).setup();
			driver = new InternetExplorerDriver();
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        driver.get(url);
		} else if(browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().proxyUser(prop.FileRead("Framework.properties", "ProxyDomainAndUser")).proxyPass(prop.FileRead("Framework.properties", "ProxyPassword")).proxy(prop.FileRead("Framework.properties", "ProxyHostAndPort")).setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("start-maximized"); // open Browser in maximized mode
			options.addArguments("disable-infobars"); // disabling infobars
			options.addArguments("--disable-extensions"); // disabling extensions
			options.addArguments("--disable-gpu"); // applicable to windows os only
			options.addArguments("--remote-debugging-port=9222");			
			options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
			options.addArguments("--no-sandbox"); // Bypass OS security model
			driver = new ChromeDriver(options);
	        driver.get(url);
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}else if(browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().proxyUser(prop.FileRead("Framework.properties", "ProxyDomainAndUser")).proxyPass(prop.FileRead("Framework.properties", "ProxyPassword")).proxy(prop.FileRead("Framework.properties", "ProxyHostAndPort")).setup();
			driver = new FirefoxDriver();
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        driver.get(url);
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}else if(browser.equalsIgnoreCase("phantom")) {
			WebDriverManager.phantomjs().proxyUser(prop.FileRead("Framework.properties", "ProxyDomainAndUser")).proxyPass(prop.FileRead("Framework.properties", "ProxyPassword")).proxy(prop.FileRead("Framework.properties", "ProxyHostAndPort")).setup();
			driver = new PhantomJSDriver();
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        driver.get(url);
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		ctx.setAttribute("driver", driver);
	}
	
	protected void stopBrowser(ITestContext ctx) {
		driver.close();
		try {
			Runtime.getRuntime().exec("taskkill /im chromedriver.exe /f");
			Runtime.getRuntime().exec("taskkill /im geckodriver.exe /f");
			Runtime.getRuntime().exec("taskkill /im IEDriverServer.exe /f");
		} catch (IOException e) {
			
		}
	}

}
