package at.ticketline.test;

import java.sql.*;

public class HSQLDBHandler 
{
	protected Connection con = null;
	
	   protected HSQLDBHandler() {}
	 
	   private static class SingletonHolder 
	   {
		   private static HSQLDBHandler instance = new HSQLDBHandler();
	   } 
	 
	   public static HSQLDBHandler getInstance() 
	   {
		   return SingletonHolder.instance;
	   }
	   
	   protected void openConnection() throws ClassNotFoundException, SQLException
	   {
		   Class.forName("org.hsqldb.jdbcDriver");
		   con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/ticketline", "sa", "");
	   }
		 	   
	   public Connection getConnection() throws ClassNotFoundException, SQLException
	   {
		   if(con == null || con.isClosed())
		   {
			 openConnection();
			 return con;
		   }
		   else
			   return con;
	   }
	   
	   public void closeConnection() throws SQLException
	   {
		   	   con.close();
	   }
}