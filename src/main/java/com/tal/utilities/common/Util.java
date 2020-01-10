package com.tal.utilities.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
public class Util {
		public static String getTS() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			return sdf.format(timestamp);
		}
		
		
		@SuppressWarnings("unused")
		public static String getMethodName() { 
	        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
	        for (int i=1; i<stElements.length; i++) {
	            StackTraceElement ste = stElements[i];
	            return ste.getMethodName().toUpperCase();
	        }
	        return null;
	     }
		
}
