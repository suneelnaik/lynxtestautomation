package com.tal.utilities.mainframe;

import java.io.IOException;
import org.testng.ITestContext;
import com.ibm.eNetwork.ECL.ECLErr;
import com.ibm.eNetwork.ECL.ECLPS;
import com.ibm.eNetwork.ECL.ECLSession;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public abstract class Mainframe_PageBase {

	protected ECLSession session = null;
	protected ExtentTest extentTest = null;
	protected String testName = null;
	protected ITestContext testContext;

	protected Mainframe_PageBase(ITestContext testContext) {
		this.testContext = testContext;
		this.session = (ECLSession)testContext.getAttribute("Session");
		this.extentTest = (ExtentTest)testContext.getAttribute("ExtentTest");	
		this.testName = testContext.getName();
	}

	/* Common operations */

	/*************************************************************
	 * @Description : This method returns char for input type as 'A' = Alphanumeric,
	 *              '0' is Numeric and 'N' could not determine
	 * @author :Gaurav.Kakkar@tal.com.au
	 ***********************************************************/
	public char inputType() {

		if (session.GetOIA().IsAlphanumeric())
			return 'A';
		else if (session.GetOIA().IsNumeric())
			return '0';
		else
			return 'N';
	}

	/*************************************************************
	 * @Description : This method returns char for input type as 'A' = Alphanumeric,
	 *              '0' is Numeric and 'N' could not determine
	 * @author :Gaurav.Kakkar@tal.com.au
	 ***********************************************************/
	public String inputStatus() {

		int status = session.GetOIA().InputInhibited();
		String stat = "";
		switch (status) {
		case 0:
			stat = "Input Not Inhibited";
			break;

		case 1:
			stat = "System Wait";
			break;

		case 2:
			stat = "Communication Check";
			break;

		case 3:
			stat = "Program Check";
			break;

		case 4:
			stat = "Machine Check";
			break;

		case 5:
			stat = "Other Inhibited";
			break;

		}
		return stat;
	}

	/*************************************************************
	 * @Description : This method return different status from the operator
	 *              information area for current session.
	 * @author :Gaurav.Kakkar@tal.com.au
	 ***********************************************************/
	public String getSystemStatus() {
		int status = session.GetOIA().GetStatusFlags();
		String stat = "";
		switch (status) {
		case 1:
			stat = "Controller ready";
			break;

		case 2:
			stat = "Online with non-SNA connection";
			break;

		case 4:
			stat = "Connected to a host application";
			break;

		case 8:
			stat = "Connected to SSCP (SNA)";
			break;

		case 16:
			stat = "Not connected (SNA)";
			break;

		case 32:
			stat = "Keyboard inhibited";
			break;

		case 64:
			stat = "System lock after AID key pressed";
			break;

		case 128:
			stat = "Communication check";
			break;

		case 256:
			stat = "Program check (error in data stream)";
			break;

		case 512:
			stat = "Keystroke in wrong place on the screen, cursor must be moved";
			break;

		case 1024:
			stat = "Function currently not available";
			break;

		case 2048:
			stat = "Keystroke invalid at this time";
			break;

		case 4096:
			stat = "Too many characters entered into field";
			break;

		case 8192:
			stat = "Symbol entered is not available";
			break;

		case 16384:
			stat = "Operator input error (5250)";
			break;

		case 32736:
			stat = "Do no enter mask";
			break;

		case 32768:
			stat = "ECL Insert state";
			break;

		case 262144:
			stat = "Graphic Cursor state";
			break;

		case 2097152:
			stat = "Communications error reminder";
			break;

		case 4194304:
			stat = "Message waiting indicator (5250)";
			break;

		default:
			stat = "Encrypted session indicator";
		}
		return stat;
	}

	/**
	 * returns a String containing all characters in  Text Plane Presentation Space
	 * @author Gaurav.Kakkar@tal.com.au
	 */
	public String readText(int position, int length) throws ECLErr{
		char[] buffer=new char[length+1];
		session.GetPS().GetScreen(buffer,length+1,position, length, ECLPS.TEXT_PLANE);
		return (new String(buffer).trim());
	}

	/* 
	 * creates a screenshot of the current session and stores it as a PNG using the filename specified 
	 * */
	public String createScreenshot() {
			
		String Base64Image = "";
		Mainframe_Screenshot mfScreenshot = new Mainframe_Screenshot();		
		try {
			Base64Image = mfScreenshot.createScreenshot(session);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Base64Image;
	}

	/**
	 * Search for text on current presentation space in Upwards Direction 
	 * @author Gaurav.Kakkar@tal.com.au
	 */
	public void searchUpwards(String SearchText){
		
		session.GetPS().SearchText(SearchText, ECLPS.SEARCH_BACKWARD );
	}
	
	/**
	 * Search for text on current presentation space in Downward Direction 
	 * @author Gaurav.Kakkar@tal.com.au
	 */
	public void searchDownwards(String SearchText) {
			
			session.GetPS().SearchText(SearchText, ECLPS.SEARCH_FORWARD );
	}
	
	/* 
	 * searches for a text match on any part of the screen. Returns the position of the text if the text is found.
	 */
	public int searchAnywhere(String SearchText) throws ECLErr{
		session.GetPS().SetCursorPos(1);
		int pos = session.GetPS().SearchText(SearchText, ECLPS.SEARCH_FORWARD );
		return pos;
	}
		
	/**
	 * returns a String containing all characters in  Text Plane Presentation Space
	 * @author Gaurav.Kakkar@tal.com.au
	 */
	public String readTextPlaneScreen() throws ECLErr{
		char[] buffer=new char[1920];
		session.GetPS().GetScreen(buffer,1920,ECLPS.TEXT_PLANE);
		return (new String(buffer));
	}
				
	/**
	 * returns a String containing all characters in  Field Plane Presentation Space
	 * @author Gaurav.Kakkar@tal.com.au
	 */
	public String readFieldPlaneScreen() throws ECLErr{
		char[] buffer=new char[1920];
		session.GetPS().GetScreen(buffer,1920,ECLPS.FIELD_PLANE);
		return (new String(buffer));
	}
		
	/**
	 * returns a String containing all characters in  Color Plane Presentation Space
	 * @author Gaurav.Kakkar@tal.com.au
	 */
	public String readColorPlaneScreen() throws ECLErr{
		char[] buffer=new char[1920];
		session.GetPS().GetScreen(buffer,1920,ECLPS.COLOR_PLANE	);
		return (new String(buffer));
	}
	
	/**
	 * returns a String containing all characters in  ExField Plane Presentation Space
	 * @author Gaurav.Kakkar@tal.com.au
	 */
	public String readExFieldPlaneScreen() throws ECLErr{
		char[] buffer=new char[1920];
		session.GetPS().GetScreen(buffer,1920,ECLPS.EXFIELD_PLANE);
		return (new String(buffer));
	}
	
	/**
	 * returns a String containing all characters in  DBCS Plane Presentation Space
	 * @author Gaurav.Kakkar@tal.com.au
	 */
	public String readDBCSPlaneScreen() throws ECLErr{
		char[] buffer=new char[1920];
		session.GetPS().GetScreen(buffer,1920,ECLPS.DBCS_PLANE);
		return (new String(buffer));
	}
	
	/**
	 * returns a String containing all characters in  DBCS Grid Plane Presentation Space
	 * @author Gaurav.Kakkar@tal.com.au
	 */
	public String readDBCSGridPlaneScreen() throws ECLErr{
		char[] buffer=new char[1920];
		session.GetPS().GetScreen(buffer,1920,ECLPS.GRID_PLANE);
		return (new String(buffer));
	}
	
	/*************************************************************
	 * @Description : This will allow test to wait until the text 
	 * appears at given position for given time in the current
	 *  presentation space .
	 * @author :Gaurav.Kakkar@tal.com.au
	 * ***********************************************************/
	public void waitCaseSensTextAtPosForTime(String text, int pos, long time) throws ECLErr, InterruptedException {
		
		ECLPS ps=session.GetPS();
		int row= ps.ConvertPosToRow(pos);
		int col= ps.ConvertPosToCol(pos);
		ps.WaitForString(text,row,col, time, false, true);
		
	}
		
	/*************************************************************
	 * @Description : This will allow test to wait until the
	 * current screen allows to enter the Input within timeout.
	 * @author :Gaurav.Kakkar@tal.com.au
	 * ***********************************************************/
	public void waitUntilInputTimeOut(long timeout) {
		
		session.GetOIA().WaitForInput(timeout);
		
	}

	/* 
	 * input Text at a specific position
	 */
	public void writeAtPosition(String Text, int Position) {
		
		try {
			session.GetPS().SendKeys(Text, Position);
		} catch (Exception e) {
			System.out.println("Unable to write on mainframe screen.");
			e.printStackTrace();
		}
		
	}

	/*
	 * input Text at current position
	 */
	public void writeAtCurrentPosition(String Text)  {
			
		try {
			session.GetPS().SendKeys(Text);
		} catch (Exception e) {
			System.out.println("Unable to write on mainframe screen.");
			e.printStackTrace();
		}
			
	}
	
	/* 
	 * input Text at a row/column position 
	 * */
	public void atRowCol(String Text, int Row, int Col){
		
		try {
			session.GetPS().SendKeys(Text, Row, Col);
		} catch (Exception e) {
			System.out.println("Unable to write on mainframe screen.");
			e.printStackTrace();
		}
		
	}

	public void writeExtentComment(String comment, LogStatus Extentlogstatus) {
		extentTest.log(Extentlogstatus, comment);				
	}
	
	public void writeExtentCommentWithScreenshot(String comment, LogStatus Extentlogstatus) {
		String stepname=Thread.currentThread().getStackTrace()[2].getMethodName();
		String Base64Image = this.createScreenshot();
		extentTest.log(Extentlogstatus, stepname, comment + extentTest.addBase64ScreenShot("data:image/png;base64," +Base64Image));
		
	}
	
}
