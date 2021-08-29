package xyz.geik.ciftci.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.geik.ciftci.Main;

public class Manager {
	
	
	public static void saveLang(String file)
	{
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/" + file + ".yml");
		
		FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
		
		try {
			lang.save(langFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param file
	 * @param title
	 * @return
	 */
	public static boolean getBoolean(String file, String title)
	{
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/" + file + ".yml");
		
		FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
		
		return lang.getBoolean(title);
		
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param file
	 * @param title
	 * @return
	 */
	public static String getText(String file, String title)
	{
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/" + file + ".yml");
		
		FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
		
		return Main.color(lang.getString(title));
		
		
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param file
	 * @param title
	 * @return
	 */
	public static int getInt(String file, String title)
	{
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/" + file + ".yml");
		
		FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
		
		return lang.getInt(title);
		
		
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param file
	 * @param title
	 * @return
	 */
	public static double getDouble(String file, String title)
	{
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/" + file + ".yml");
		
		FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
		
		return lang.getDouble(title);
		
		
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param file
	 * @param title
	 * @return
	 */
	public static List<String> getLore(String file, String title)
	{
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/" + file + ".yml");
		
		FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
		
		List<String> list = new ArrayList<String>();
		
		for (String s : lang.getStringList(title))
		{
			list.add(Main.color(s));
		}
		
		return list;
		
	}
	
	public static Set<String> getConfigurationSection(String file, String title)
	{
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/" + file + ".yml");
		
		FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
		
		Set<String> list = lang.getConfigurationSection(title).getKeys(false);
		
		return list;
		
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param file
	 * @param fileName
	 */
	public static void FileChecker(String fileName)
	{
		
		try
		{
			
			File c = new File("plugins/" + Main.instance.getDescription().getName() + "/" + fileName + ".yml");
			
			if (!c.exists())
			{
				Main.instance.saveResource(fileName + ".yml", false);
			}
			
		}
		
		catch(NullPointerException e1) {  Main.instance.saveResource(fileName + ".yml", false);  }
		
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param file
	 * @param title
	 * @return
	 */
	public static boolean isSet(String file, String title)
	{
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/" + file + ".yml");
		
		FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
		
		return lang.isSet(title);
		
	}
	
	/**
	 * @author Geik
	 * @since 1.2.0
	 * @param path
	 * @return
	 */
	public static String getStringFromLocation(Location loc)
	{
		return loc.getWorld().getName() + "/" + loc.getX() + "/" + loc.getY() + "/" + loc.getZ() + "/" + loc.getYaw() + "/" + loc.getPitch() ;
	}
	
	/**
	 * @author Geik
	 * @since 1.2.0
	 * @param s
	 * @return
	 */
	public static Location getLocationFromString(String s)
	{
		
		  if (s == null || s.trim() == "")
		  {
			  
			  return null;
			  
		  }
		  
		  final String[] parts = s.split("/");
		  
		  if (parts.length == 6)
		  {
			  
			  World w = Bukkit.getServer().getWorld(parts[0]);
			  
			  double x = Double.parseDouble(parts[1]);
			  
			  double y = Double.parseDouble(parts[2]);
			  
			  double z = Double.parseDouble(parts[3]);
			  
			  float yaw = Float.parseFloat(parts[4]);
			  
			  float pitch = Float.parseFloat(parts[5]);
			  
			  return new Location(w, x, y, z, yaw, pitch);
			  
		  }
		  return null;
		  
	}
	
	public static Location getLocationFromStringKFarmer(String s)
	{
		
		  if (s == null || s.trim() == "")
		  {
			  
			  return null;
			  
		  }
		  
		  final String[] parts = s.split(";");
		  
		  if (parts.length == 6)
		  {
			  
			  World w = Bukkit.getServer().getWorld(parts[0]);
			  
			  double x = Double.parseDouble(parts[1]);
			  
			  double y = Double.parseDouble(parts[2]);
			  
			  double z = Double.parseDouble(parts[3]);
			  
			  float yaw = Float.parseFloat(parts[4]);
			  
			  float pitch = Float.parseFloat(parts[5]);
			  
			  return new Location(w, x, y, z, yaw, pitch);
			  
		  }
		  return null;
		  
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param strNum
	 * @return
	 */
	public static boolean isNumeric(String strNum)
	{
		
	    try
	    {
	        Integer.parseInt(strNum);
	    }
	    
	    catch (NumberFormatException nfe)
	    {
	        return false;
	    }
	    
	    return true;
	    
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param player
	 */
	@SuppressWarnings("deprecation")
	public static void descentItemAmountMainHand(Player player)
	{
		
		Bukkit.getScheduler().runTask(Main.instance, () -> {
			
			ItemStack handItem = player.getInventory().getItemInHand();
			
			int itemCount = handItem.getAmount();
			
			if (itemCount == 1) handItem.setType(Material.AIR);
			
			else handItem.setAmount(itemCount-1);
			
			player.getInventory().setItemInHand(handItem);
			
			player.updateInventory();
			
		});
		
	}
	
	/**
	 * @author Geik
	 * @param p
	 * @return
	 */
	public static boolean invFull(Player p)
	{
		return p.getInventory().firstEmpty() == -1;
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @param value
	 * @param places
	 * @return
	 */
	public static String roundDouble(double value, int places)
	{
		
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    
	    value = value * factor;
	    
	    long tmp = Math.round(value);
	    
	    double result = (double) tmp / factor;
	    
	    return String.valueOf( result );
	    
	}
	
	/**
	 * @author Geik
	 * @return
	 * @since 1.1.0
	 */
	public static String getNMSVersion()
	{
		
        String v = Bukkit.getServer().getClass().getPackage().getName();
        
        return v.substring(v.lastIndexOf('.') + 1);
        
    }


}
