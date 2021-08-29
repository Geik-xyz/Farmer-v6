package xyz.geik.ciftci.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.json.JSONObject;

import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Cache.Farmer;

public class mFarmerMigration {
	
	@SuppressWarnings("deprecation")
	public static HashMap<String, Farmer> getFarmerInformations()
	{
		
		String SQL_QUERY = "SELECT * FROM farmers";
		
		HashMap<String, Farmer> farmerList = new HashMap<String, Farmer>();
		
		try (Connection con = ConnectionPool.getMFarmerConnection())
	    {
	        	
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            
            ResultSet resultSet = pst.executeQuery();
            
            while (resultSet.next())
            {
            	
            	try {
            		
            		int farmerLevel = 1;
            		
            		int farmerID = 99999;
            		
            		int sellingStatus = 0;
            		
            		Location placedLocation = null;
            		
            		boolean autoSell = false;
            		
            		boolean autoCollect = false;
            		
            		boolean spawnerKill = false;
                	
                	placedLocation = getLocationFromString(resultSet.getString( "location" ));
                	
                	String uuid = ApiFun.getIslandOwnerUUID(placedLocation).toString();
                	
                	List<OfflinePlayer> players = ApiFun.getLandPlayers(  Bukkit.getOfflinePlayer(uuid), uuid  );
                	
                	Farmer storage = new Farmer(uuid, farmerLevel, farmerID, sellingStatus, placedLocation, players, Manager.getText("lang", "FarmerSkin"), autoSell, autoCollect, spawnerKill);
                	
                	if (farmerList.containsKey(uuid))
                		continue;
                	
                	else farmerList.put(uuid, storage);
            		
            	}
            	
            	catch (NullPointerException e1) { continue; }
            	
            }
            
            resultSet.close();
            
            pst.close();
            
            return farmerList;
	            
	     }  catch (SQLException e1) { e1.printStackTrace(); return null; }
		
	}
	
	private static List<String> getPlayerAllValues(String ownerUUID)
	{
		
		String SQL_QUERY = "SELECT location, farmerId FROM farmers";
		
		List<String> farmerList = new ArrayList<String>();
		
		try (Connection con = ConnectionPool.getMFarmerConnection())
	    {
	        	
            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
            
            ResultSet resultSet = pst.executeQuery();
            
            while (resultSet.next())
            {
            	
            	try {
            		
            		Location placedLocation = null;
            		
            		placedLocation = getLocationFromString(resultSet.getString( "location" ));
                	
                	String uuid = ApiFun.getIslandOwnerUUID(placedLocation).toString();
                	
                	if (uuid.equalsIgnoreCase(ownerUUID)) 
                		farmerList.add(resultSet.getString("farmerId"));
            		
            	}
            	
            	catch (NullPointerException e1) { continue; }
            	
            }
            
            resultSet.close();
            
            pst.close();
            
            return farmerList;
	            
	     }  catch (SQLException e1) { e1.printStackTrace(); return null; }
		
	}
	
	public static HashMap<String, Integer> getFarmerValues(String uuid)
	{
		
		HashMap<String, Integer> allStock = new HashMap<String, Integer>();
		
		List<HashMap<String, Integer>> list = new ArrayList<HashMap<String, Integer>>();
		
		for (String farmerId : getPlayerAllValues(uuid)) {
			
			String SQL_QUERY = "SELECT * FROM farmers WHERE farmerId = ?";
			
			HashMap<String, Integer> values = new HashMap<String, Integer>();
			
			try (Connection con = ConnectionPool.getMFarmerConnection())
		    {
		        	
	            PreparedStatement pst = con.prepareStatement(SQL_QUERY);
	            
	            pst.setString(1, farmerId);
	            
	            ResultSet resultSet = pst.executeQuery();
	            
	            if (resultSet.next())
	            {
	            	
	            	String stocks = resultSet.getString("stocks");
	            	
	            	JSONObject stockJson = new JSONObject(stocks);
	            	
	            	for (String key : stockJson.keySet()) {
	            		
	            		int amount = stockJson.getInt(key);
	            		
	            		values.put(key.toLowerCase(), amount);
	            		
	            	}
	            	
	            }
	            
	            resultSet.close();
	            
	            pst.close();
	            
	            if (values != null)
	            	list.add(values);
		            
		    }  catch (SQLException e1) { return null; }
			
		}
		
		for (HashMap<String, Integer> values : list) {
			
			for (String key : values.keySet()) {
				
				if (!allStock.keySet().contains(key))
					allStock.put(key, values.get(key));
				
				else
					allStock.replace(key, allStock.get(key)+values.get(key));
				
			}
			
		}
		
		return allStock;
		
	}
	
	/**
	 * Getting location from db
	 * 
	 * @param s
	 * @return
	 */
	private static Location getLocationFromString(String s)
	{
		
		  if (s == null || s.trim() == "")
		  {
			  
			  return null;
			  
		  }
		  
		  final String[] parts = s.split(",");
		  
		  if (parts.length == 4)
		  {
			  
			  World w = Bukkit.getServer().getWorld(parts[0]);
			  
			  double x = Integer.parseInt(parts[1]);
			  
			  double y = Integer.parseInt(parts[2]);
			  
			  double z = Integer.parseInt(parts[3]);
			  
			  float yaw = Float.parseFloat(String.valueOf(0));
			  
			  float pitch = Float.parseFloat(String.valueOf(0));
			  
			  return new Location(w, x, y, z, yaw, pitch);
			  
		  }
		  return null;
		  
	}

}
