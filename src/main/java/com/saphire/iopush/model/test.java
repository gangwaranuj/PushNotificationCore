package com.saphire.iopush.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.Utility;

public class test {
   public static void main(String[] args) {
      // Prints "Hello, World" in the terminal window.
      System.out.println("Hello, World");
      try {
		Date nextday = Utility.addMin(1440, Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
		SimpleDateFormat format1 = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_yyyy_MM_DD_T_HH_MM_SS);
		
		String date = format1.format(new Date());
	
		System.out.println("nextday=="+date.toString());
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
   }
}