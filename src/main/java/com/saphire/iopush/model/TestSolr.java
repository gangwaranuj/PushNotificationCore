package com.saphire.iopush.model;

public class TestSolr {


	/**
	 * A Java MySQL PreparedStatement INSERT example.
	 * Demonstrates the use of a SQL INSERT statement against a
	 * MySQL database, called from a Java program, using a
	 * Java PreparedStatement.
	 * 
	 * Created by Alvin Alexander, http://devdaily.com
	 */
	

	  public static void hello()
	  {
	    try
	    {
	      // create a mysql database connection
	     /* String myDriver = "com.mysql.jdbc.Driver";
	      String myUrl = "jdbc:mysql://localhost:3306/test";
	      Class.forName(myDriver);
	      Connection conn = DriverManager.getConnection(myUrl, "test", "root");
	    
	      // create a sql date object so we can use it in our INSERT statement
	      Calendar calendar = Calendar.getInstance();
	      java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

	      // the mysql insert statement
	      String query = " insert into weather_country_list (country_id,country_code,country_name,creation_time)"
	        + " values (?, ?, ?,?)";

	      // create the mysql insert preparedstatement
	      PreparedStatement preparedStmt = conn.prepareStatement(query);
	      preparedStmt.setInt(1, 263);
	      preparedStmt.setString (2, "ST3");
	      preparedStmt.setString   (3, "SAKET");
	      preparedStmt.setDate (4, startDate);
	      // execute the preparedstatement
	      preparedStmt.execute();
	      
	      
	      String query2 = " delimiter //CREATE TRIGGER upd_check AFTER INSERT ON weather_country_listFOR EACH ROWWHERE creation_time < now() END;//delimiter ;";
	      PreparedStatement preparedStmt2 = conn.prepareStatement(query2);
	      
	      preparedStmt2.execute();
	      conn.close();*/
	    	System.out.println("Hello world");
	    	
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception!");
	      System.err.println(e.getMessage());
	    }
	  }
	
  
}





