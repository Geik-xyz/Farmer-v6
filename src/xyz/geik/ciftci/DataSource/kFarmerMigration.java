package xyz.geik.ciftci.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Cache.Farmer;

public class kFarmerMigration {
	
	
	@SuppressWarnings("deprecation")
	public static HashMap<String, Farmer> getFarmerInformations()
	{
		
		String SQL_QUERY = "SELECT * FROM farmers";
		
		HashMap<String, Farmer> farmerList = new HashMap<String, Farmer>();
		
		try (Connection con = ConnectionPool.getKFarmerConnection())
	    {
	        	
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            
            ResultSet resultSet = pst.executeQuery();
            
            while (resultSet.next())
            {
            	
            	int farmerLevel = 0;
        		
        		int farmerID = 99999999;
        		
        		int sellingStatus = 0;
        		
        		Location placedLocation = null;
        		
        		boolean autoSell = false;
        		
        		boolean autoCollect = false;
        		
        		boolean spawnerKill = false;
            	
            	String uuid = resultSet.getString("owner");
            	
            	List<OfflinePlayer> players = ApiFun.getLandPlayers(  Bukkit.getOfflinePlayer(uuid), uuid  );
            	
            	farmerLevel = resultSet.getInt("level");
            	
            	placedLocation = Manager.getLocationFromStringKFarmer(resultSet.getString( "location" ));
            	
            	Farmer storage = new Farmer(uuid, farmerLevel, farmerID, sellingStatus, placedLocation, players, Manager.getText("lang", "FarmerSkin"), autoSell, autoCollect, spawnerKill);
            	
            	if (farmerList.containsKey(uuid))
            	{
            		
            		if (farmerList.get(uuid).getFarmerLevel() < farmerLevel)
            		{
            			
            			farmerList.replace(uuid, storage);
            			
            		}
            		
            	}
            	
            	else farmerList.put(uuid, storage);
            	
            }
            
            resultSet.close();
            
            pst.close();
            
            return farmerList;
	            
	     }  catch (SQLException e1) { e1.printStackTrace(); return null; }
		
	}
	
	public static HashMap<String, Integer> getFarmerValues(String uuid)
	{
		
		String SQL_QUERY = "SELECT Inv.* FROM farmers Frm INNER JOIN inventory Inv ON Frm.claimID = Inv.claimID WHERE Frm.owner = ?";
		
		HashMap<String, Integer> values = new HashMap<String, Integer>();
		
		try (Connection con = ConnectionPool.getKFarmerConnection())
	    {
	        	
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            
            pst.setString(1, uuid);
            
            ResultSet resultSet = pst.executeQuery();
            
            while (resultSet.next())
            {
            	
            	String material = resultSet.getString("material");
            	
            	int amount = resultSet.getInt("amount");
            	
            	if (values.containsKey(material))
            	{
            		
            		int newValue = values.get(material)+amount;
            		
            		values.remove(material);
            		
            		values.put(material.toLowerCase(), newValue);
            		
            	}
            	
            	else values.put(material.toLowerCase(), amount);
            	
            }
            
            resultSet.close();
            
            pst.close();
            
            return values;
	            
	    }  catch (SQLException e1) { return null; }
		
	}

}
