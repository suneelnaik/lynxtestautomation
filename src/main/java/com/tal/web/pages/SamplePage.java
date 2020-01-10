package com.tal.web.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;

import com.relevantcodes.extentreports.LogStatus;
import com.tal.utilities.web.Web_PageBase;

public class SamplePage extends Web_PageBase {
	WebDriver driver;

    @FindBy(id="txtUsername")
    WebElement username;

    @FindBy(id="txtPassword")
    WebElement password;    

    @FindBy(id="btnLogin")
    WebElement login;

	public SamplePage(ITestContext testContext) {
		super(testContext);
		this.driver = (WebDriver) testContext.getAttribute("driver");

        //This initElements method will create all WebElements
        PageFactory.initElements(driver, this);
	}
	
	//Set user name in textbox
    public void setUserName(String sUserName){
    	username.sendKeys(sUserName);   
    }

    //Set password in password textbox
    public void setPassword(String sPassword){
    	password.sendKeys(sPassword);
    }

    //Click on login button
    public void clickLogin(){
    	login.click();
    }
    
    //wait for Login button
    public void waitForElementVisible(WebElement element){
    	WebDriverWait wait = new WebDriverWait(driver, 20);
    	wait.until(ExpectedConditions.visibilityOf(element));
    }
	
    public void loginToOCMSAdmin(String strUserName,String strPasword){
        //Fill user name
    	//waitForElementVisible(username);
        this.setUserName(strUserName);

        //Fill password
        waitForElementVisible(password);
        this.setPassword(strPasword);

        //Click Login button
        this.clickLogin(); 
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        super.writeExtentCommentWithScreenshot("Clicked on Login.", LogStatus.PASS);
    }
}
