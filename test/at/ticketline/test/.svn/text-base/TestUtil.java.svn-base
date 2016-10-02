package at.ticketline.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class TestUtil {
    private static Logger log = Logger.getLogger(TestUtil.class);

    private static String ticketlineRCPBasePath = System
	    .getProperty("user.dir") + File.separator;
    private static String dbToolsPath = ticketlineRCPBasePath + "db-tools";
    private static String dropDDL = "dropDDL.jdbc";
    private static String createDDL = "createDDL.jdbc";
    private static String clearTables = "clearDDL.jdbc";

    public static void executeScript(String pathToScript) {
	Connection con = null;
	BufferedReader in = null;
	Statement stmt = null;

	try {
	    con = HSQLDBHandler.getInstance().getConnection();
	    in = new BufferedReader(new FileReader(pathToScript));
	    stmt = con.createStatement();

	    while (in.ready()) {
		String nextStmt = in.readLine();
		stmt.executeUpdate(nextStmt);
	    }
	    con.commit();
	} 
	catch(Exception e) {
	    log.error("Error while clearing tables!");
	    try {
		con.rollback();
	    } catch (SQLException sqle) {
		sqle.printStackTrace();
	    }
	}
	finally {
	    try {
		if (stmt != null)
		    stmt.close();
		if (con != null)
		    con.close();
		if (in != null)
		    in.close();
	    }
	    catch(Exception e) {
		log.error("Error while clearing tables!");
		e.printStackTrace();
	    }
	}
    }

    public static void createTables() {
	executeScript(ticketlineRCPBasePath + createDDL);
    }

    public static void dropTables() {
	executeScript(ticketlineRCPBasePath + dropDDL);
    }
    
    public static void clearTables() {
	executeScript(ticketlineRCPBasePath + clearTables);
    }
/*
    public static void clearTables() {
	File dir = new File(dbToolsPath);
	try {
	    Runtime.getRuntime()
		    .exec(dbToolsPath + File.separator + "resetAllData.sh",
			    null, dir);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
*/
    public static void restoreTestData() {

	File dir = new File(dbToolsPath);
	try {
	    Runtime.getRuntime()
		    .exec(dbToolsPath + File.separator + "resetAllData.sh",
			    null, dir);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	try {
	    Runtime.getRuntime().exec(
		    dbToolsPath + File.separator + "insertTestData.sh", null,
		    dir);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
