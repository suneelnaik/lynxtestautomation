package com.tal.utilities.web;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public abstract class Web_PageBase {
	protected WebElement webelement;
	protected List<WebElement> webelements = null;
	protected WebDriver driver = null;
	protected int defaultBrowserTimeOut = 30;
	protected List<String> windowHandlers;
	protected ITestContext testContext;
	protected ExtentTest extentTest = null;
	protected String testName = null;
	
	protected Web_PageBase(ITestContext testContext) {
		this.testContext = testContext;
		this.extentTest = (ExtentTest)testContext.getAttribute("ExtentTest");	
		this.testName = testContext.getName();
		this.driver=(WebDriver) testContext.getAttribute("driver");
	}
	
	public void writeExtentComment(String comment, LogStatus Extentlogstatus) {
		extentTest.log(Extentlogstatus, comment);				
	}
	
	public void writeExtentCommentWithScreenshot(String comment, LogStatus Extentlogstatus) {
		String stepname=Thread.currentThread().getStackTrace()[2].getMethodName();
		String Base64Image = this.createScreenshot();
		extentTest.log(Extentlogstatus, stepname, comment + extentTest.addBase64ScreenShot("data:image/png;base64," +Base64Image));
		
	}
	public String createScreenshot() {
		String	Base64Image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		return Base64Image;
	}
	
	@SuppressWarnings("deprecation")
	public WebDriver startBrowser(String browserName, boolean FirefoxBrowserProxy)
			throws UnknownHostException {

		if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir") + "/src/main/java/com/tal/resources/geckodriver.exe");		
			if(FirefoxBrowserProxy)
			{
					//driver = proxySetting(); 
				
			}
			else
					driver = new FirefoxDriver();
			} else if (browserName.equalsIgnoreCase("iexplorer")) {
					System.setProperty("webdriver.ie.driver",
					System.getProperty("user.dir") + "/src/main/java/com/tal/resources/IEDriverServer.exe");
					DesiredCapabilities capabilities = DesiredCapabilities
					.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
					capabilities.setCapability("ignoreZoomSetting", true);
					driver = new InternetExplorerDriver(capabilities);
			} else if (browserName.equalsIgnoreCase("chrome")) {
						System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "/src/main/java/com/tal/resources/chromedriver.exe");
					driver = new ChromeDriver();
			}

					driver.manage().timeouts()
					.implicitlyWait(defaultBrowserTimeOut, TimeUnit.SECONDS);
					driver.manage().deleteAllCookies();
		
		if (browserName.equalsIgnoreCase("iexplorer"))
			this.SwitchAlert();

		if (windowHandlers == null)
			windowHandlers = new LinkedList<String>();
		else
			windowHandlers.clear();

		windowHandlers.add(driver.getWindowHandle());
		driver.manage().window().maximize();
		return driver;
	
	}

	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: SwitchAlert
	 * @Description : Switch to alert and return True if switched 
	 * else false
	 ************************************************************/
	public boolean SwitchAlert() {
		boolean Flag = false;
		try {
			if (driver.switchTo().alert() != null) {
				driver.switchTo().alert().accept();
				Flag = true;
			}
		}
		catch (NoAlertPresentException e) {
			
		}
		return Flag;
	}

		
	public WebDriver getWebDriver() {
		return driver;
	}
	
	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: deleteTempFile
	 * @Description : delete all the files in temporary directory defined by
	 * java temp directory property
	 ************************************************************/
	public void deleteTempFile() throws UnknownHostException {
		String property = "java.io.tmpdir";
		String temp = System.getProperty(property);
		File directory = new File(temp);
		try {
			delete(directory);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: delete
	 * @Description : delete all the files in directory defined by
	 * file parameter
	 ************************************************************/
	public void delete(File file) throws IOException {
		if (file.isDirectory()) { // directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					delete(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					System.out.println("Directory is deleted : "
							+ file.getAbsolutePath());
				}
			}
		} else {
			// if file, then delete it
			file.delete();
		}
	}

	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: highlightElement
	 * @Description : Highlights element with javascript executor
	 * with 2pixel DeepPink border.
	 ************************************************************/
	public void highlightElement(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
				element, "border: 2px solid DeepPink;");
	}

	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: doubleClick
	 * @Description : doubleClick the web element
	 ************************************************************/
	public void doubleClick(WebElement element) {
		if ((driver != null) && (element != null))
			(new Actions(driver)).doubleClick(element).build().perform();
	}


	public List<WebElement> findElements(String locator) {

		if (locator != null) {
			String[] arrLocator = locator.split("==");
			String locatorTag = arrLocator[0].trim();
			String objectLocator = arrLocator[1].trim();

			if (locatorTag.equalsIgnoreCase("id")) {
				webelements = driver.findElements(By.id(objectLocator));
			} else if (locatorTag.equalsIgnoreCase("name")) {
				webelements = driver.findElements(By.name(objectLocator));
			} else if (locatorTag.equalsIgnoreCase("xpath")) {
				webelements = driver.findElements(By.xpath(objectLocator));
			} else if (locatorTag.equalsIgnoreCase("linkText")) {
				webelements = driver.findElements(By.linkText(objectLocator));
			} else if (locatorTag.equalsIgnoreCase("class")) {
				webelements = driver.findElements(By.className(objectLocator));
			} else {
				System.out.println("Please Check the Locator Syntax Given :"
						+ locator);
				return null;
			}
		}
		return webelements;
	}

	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: hoverMouse
	 * @Description : hoverMouse over  the web element
	 ************************************************************/
	public void hoverMouse(WebElement Element) {
			Actions builder = new Actions(driver);  // Configure the Action    
		 Action mouseOver =builder.moveToElement(Element).build(); // Get the action    
		 mouseOver.perform(); // Execute the Action 
	}
	
	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: PressShiftTab
	 * @Description : PressShiftTab in the current focused window
	 * to switch focus on the previous  element
	 ************************************************************/
	public void PressShiftTab() throws AWTException {
		Robot robot = new Robot();
		robot.delay(3000);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB); 
		robot.keyRelease(KeyEvent.VK_SHIFT);
	}
	
	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: PressTab
	 * @Description : PressTab in the current focused window
	 * to switch focus on the next element
	 ************************************************************/
	public void PressTab() throws AWTException {
		Robot robot = new Robot();
		robot.delay(3000);

		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB); 
		
	}

	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: getAttribute
	 * @Description : returns the value of the attribute for the element
	 ************************************************************/
	public String getAttribute(WebElement element, String attributeName) {
		String attributeValue = null;
		try {
			if (element != null)
				attributeValue = element.getAttribute(attributeName);
			element = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return attributeValue;
	}
	
}