package com.tal.report;

import java.io.ByteArrayInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import org.apache.commons.codec.binary.Base64;

import com.tal.utilities.common.PropertyFileRead;


public class Database {
	protected String jdbcUrl;
	protected String username;
	protected String password;
	
	
	public Database() {
		PropertyFileRead pr = new PropertyFileRead();
		jdbcUrl = pr.FileRead("Framework.properties", "jdbcDriverUrl");
		username = pr.FileRead("Framework.properties", "DatabaseUsername");
		password = pr.FileRead("Framework.properties", "DatabasePassword");
	}
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: UpsertSuite
	 * @Description : Function to upsert suite data in MySQL DB
	 ************************************************************/
	public int UpsertSuite(String SuiteName,int SuiteID,String Doc,Timestamp StartTime,Timestamp EndTime,String Environment,String BuildLink) {
		 
	     
	      String sql = "{call UpsertSuite_v1(?,?,?,?,?,?,?)}";
	      int suiteId=0;
	      try{
	    	  Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	    	  Connection conn = DriverManager.getConnection(jdbcUrl, username, password); 
	          CallableStatement stmt=conn.prepareCall(sql);
	         
	        //Set IN parameter
	          stmt.setString(1,SuiteName);
	          
	          if(SuiteID==0)
	        	  stmt.setNull(2, Types.INTEGER);
	          else
	        	  stmt.setInt(2, SuiteID);
	          
	          stmt.setString(3,Doc);
	          stmt.setTimestamp(4,StartTime);
	          stmt.setTimestamp(5,EndTime);
	          stmt.setString(6,Environment);
	          stmt.setString(7,BuildLink);
	         
	        //Get suite ID parameters
	         ResultSet resultSet = stmt.executeQuery();
	         resultSet.next();
	         suiteId=resultSet.getInt(1);
	          
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      
	      return suiteId;
    }
	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: Upsert Test
	 * @Description : Function to upsert test results data in MySQL DB
	 ************************************************************/
	public int UpsertTest(String SuiteName,int SuiteID,String Documentation,Timestamp StartTime,
			Timestamp EndTime,String ShortName,String Message,String Status,String Tags,String Environment,
			String Portfolio,String Application,String FunctionalArea,String TestType,String Feature,String Keywords) {
		 
	     
	      String sql = "{call UpsertTestResult_v1(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	      int testId=0;
	      try{
	    	  Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	    	  Connection conn = DriverManager.getConnection(jdbcUrl, username, password); 
	          CallableStatement stmt=conn.prepareCall(sql);
	         
	        //Set IN parameter
	          stmt.setString(1,SuiteName);
	          stmt.setInt(2,SuiteID);
	          stmt.setString(3,Documentation);
	          stmt.setTimestamp(4,StartTime);
	          stmt.setTimestamp(5,EndTime);
	          stmt.setString(6,ShortName);
	          stmt.setString(7,Message);
	          stmt.setString(8,Status);
	          stmt.setString(9,Tags);
	          stmt.setString(10,Environment);
	          stmt.setString(11,Portfolio);
	          stmt.setString(12,Application);
	          stmt.setString(13,FunctionalArea);
	          stmt.setString(14,TestType);
	          stmt.setString(15,Feature);
	          stmt.setString(16,Keywords);

	         //Execute and get test ID parameters
	         ResultSet resultSet = stmt.executeQuery();
	         resultSet.next();
	         testId=resultSet.getInt(2);
	               
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      
	      return testId;
    }
	
	/*************************************************************
	 * @author :Gaurav.Kakkar@tal.com.au
	 * @Method_Name: Upsert Test
	 * @Description : Function to upsert test results data in MySQL DB
	 ************************************************************/
	@SuppressWarnings("static-access")
	public void UpsertScreenshot(String Base64Image,int TestID) {
		  String sql = "{call UpsertScreenshot_v1(?,?)}";
	      
	      try{
	    	  Base64 decoder = new Base64();
	    	  byte[] imageByte = decoder.decodeBase64(Base64Image);
	            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
	    	  
	    	  Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	    	  Connection conn = DriverManager.getConnection(jdbcUrl, username, password); 
	          CallableStatement stmt=conn.prepareCall(sql);
	         
	        //Set IN parameter
	          stmt.setBinaryStream(1,bis);
	          stmt.setInt(2,TestID);
	       
	         //Execute procedure to upload BLOB
	         stmt.executeQuery();
	                   
	      } catch (Exception e) {
	         e.printStackTrace();
	      }	      
    }
	
	public String[][] readDB(String SQL ){
		Connection con;
		String[][] inputArr = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try {

				//Create a connection to DB by passing Url,Username,Password as parameters
				con = DriverManager.getConnection(jdbcUrl, username, password);
				Statement stmt=con.createStatement();
				
				//Executing the Queries
				ResultSet rs = stmt.executeQuery(SQL);
				rs.last();
				int rows = rs.getRow();
				 
				ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
				int cols = rsmd.getColumnCount();
				inputArr= new String[rows][cols];
 
				int i =0;
				rs.beforeFirst();
				//Iterating the data in the Table
				while (rs.next())
				{
					for(int j=0;j<cols;j++)
					{
						inputArr[i][j]=rs.getString(j+1);
					}
					i++;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				System.out.println("Failed to create the database connection."); 
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			System.out.println("Driver not found."); 
		}
		return inputArr;
	}
	
}
