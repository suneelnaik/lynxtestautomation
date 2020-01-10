package com.tal.listener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

public class MethodInterceptorListener implements IMethodInterceptor {

	Set<Pattern> testpatterns;
	Set<Pattern> Apppatterns;
	
	private boolean includeTest(String testsToInclude,String currentTestName) {
		boolean result = false;
		if(testpatterns==null) {
			testpatterns = new HashSet<>();
			String[] testPatterns = testsToInclude.split(",");
			for(String testPattern:testPatterns) {
				testpatterns.add(Pattern.compile(testPattern, Pattern.CASE_INSENSITIVE));
			}
		}
		
		for(Pattern pattern:testpatterns) {
			if(pattern.matcher(currentTestName).find()) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	private boolean includeApp(String Appname,String Application) {
		boolean result = false;
		if(Apppatterns==null) {
			Apppatterns = new HashSet<>();
			String[] testPatterns = Appname.split(",");
			for(String testPattern:testPatterns) {
				Apppatterns.add(Pattern.compile(testPattern, Pattern.CASE_INSENSITIVE));
			}
		}
		
		for(Pattern pattern:Apppatterns) {
			if(pattern.matcher(Application).find()) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {		
		
		String testNames = System.getProperty("TestType");	
		String testtype= context.getCurrentXmlTest().getParameter("TestType");
		String AppName = System.getProperty("Application");	
		String Application= context.getCurrentXmlTest().getParameter("Application");
		boolean testtypematch=includeTest(testNames,testtype);
		boolean applicationmatch=includeApp(AppName,Application);
		
		if(testNames==null || testNames.trim().isEmpty()) {
			return methods;
		}else {
			if(testtypematch && applicationmatch) {
				
				return methods;
			}else {
				return new ArrayList<IMethodInstance>();
			} 
		}
	}
}
