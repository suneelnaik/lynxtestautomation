package com.tal.utilities.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyFileRead {

	Properties PropDetails = new Properties();
	InputStream input = null;
	OutputStream output = null;
	
	public PropertyFileRead(){
		
	}
	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: FileRead
	 * @Description : Property File Read
	 * @param: FileName- Complete path of file
	 * @param: ObName- Object to be read from the file
	 * @return: ObValue - Value of the Object read from file
	 * ***********************************************************/
	public String FileRead(String FileName, String ObName)
	{
		try {
			 	InputStream input = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/com/tal/properties/common/"+FileName);
				 
				// load a properties file
				PropDetails.load(input);
			} catch (IOException ex) {
			ex.printStackTrace();
			}
		String ObValue = PropDetails.getProperty(ObName);
		return ObValue;
	}
}
	
	

