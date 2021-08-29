package xyz.geik.ciftci.DataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import xyz.geik.ciftci.Main;

public class ConnectionPool {

	public static Connection conn = null;
	
	public static Connection getConnection() {
		try {
			
			Class.forName("org.sqlite.JDBC");
			File databaseFile = new File("plugins/" + Main.instance.getDescription().getName() + "/Database.db");
	        return DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getParentFile().getAbsolutePath() + "/Database.db");
	        
		} catch (ClassNotFoundException | SQLException e) { return null; }
    }

    public static void closePool() {
    	
        try {
        	
			if (conn != null && !conn.isClosed()) {
				
			    conn.close();
			    
			}
			
        } catch (SQLException e) {}
        
    }
	
	public static void initsqlite() {
		File databaseFile = new File("plugins/" + Main.instance.getDescription().getName() + "/Database.db");
		if (!databaseFile.exists()) {
	    	try {
	    		Class.forName("org.sqlite.JDBC");
	    		String url = "jdbc:sqlite:" + databaseFile.getParentFile().getAbsolutePath() + "/Database.db";
	    		conn = DriverManager.getConnection(url);
	    		
	    	} catch(SQLException | ClassNotFoundException e) {
	    		e.printStackTrace();
	    	} finally {
	    		try {
	    			if (conn != null) {
	    				conn.close();
	    			}
	    		} catch(SQLException ex) {
	    			ex.printStackTrace();
	    		}
	    	}
		}
    }
	
	public static Connection getKFarmerConnection() {
		try {
			
			Class.forName("org.sqlite.JDBC");
			File databaseFile = new File("plugins/KFarmer/data.db");
	        return DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
	        
		} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); return null; }
    }
	
	public static Connection getMFarmerConnection() {
		try {
			
			Class.forName("org.sqlite.JDBC");
			File databaseFile = new File("plugins/Farmer/farmers.db");
	        return DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
	        
		} catch (ClassNotFoundException | SQLException e) { e.printStackTrace(); return null; }
    }

}
