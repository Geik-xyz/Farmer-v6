package xyz.geik.ciftci.Commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.DataSource.kFarmerMigration;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Cache.ConfigItems;
import xyz.geik.ciftci.Utils.Cache.Farmer;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;

public class kFarmerMigrationCmd {
	
	
	/**
	 * @author Geik
	 * @since
	 * Migration of KFarmer
	 */
	public static void migrate()
	{
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6&lÇİFTÇİ &aConfig ayarlanıyor..."));
		
		// ÇİFTÇİ
		
		File itemsFile = new File("plugins/" + Main.instance.getDescription().getName() + "/items.yml");
		
		FileConfiguration items = YamlConfiguration.loadConfiguration(itemsFile);
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/lang.yml");
		
		FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
		
		// KFARMER
		
		File kFarmerConfig = new File("plugins/KFarmer/config.yml");
		
		FileConfiguration cfgK = YamlConfiguration.loadConfiguration(kFarmerConfig);
		
		Main.instance.getConfig().set("FarmerLevels", null);
		
		for (String level : cfgK.getConfigurationSection("level").getKeys(false))
		{
			
			if (cfgK.isSet("level." + level + ".requirements"))
				for (String req : cfgK.getConfigurationSection("level." + level + ".requirements").getKeys(false))
				{
					
					String type = cfgK.getString("level." + level + ".requirements." + req + ".type");
					
					String value = cfgK.getString("level." + level + ".requirements." + req + ".value");
					
					if (type.equalsIgnoreCase("PERMISSION"))
					{
						
						Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".Capacity", Integer.valueOf(level)*1000);
						
						Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".nextRankMoney", Integer.valueOf(1));
						
						Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".permission", value);
						
					}
					
					else
					{
						
						Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".Capacity", Integer.valueOf(level)*1000);
						
						Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".nextRankMoney", Double.valueOf(value));
						
					}
					
				}
			
			else
			{
				
				Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".Capacity", Integer.valueOf(level)*1000);
				
				Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".nextRankMoney", Integer.valueOf(1));
				
			}
			
			int taxRate = (int) cfgK.getDouble("level." + level + ".tax");
			
			Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".taxRate", taxRate);
			
		}
		
		Main.instance.getConfig().set("Settings.levelBasedFarmer", true);
		
		Main.instance.getConfig().set("Settings.depositMethod.defaultDepositLeader", true);
		
		Main.instance.saveConfig();
		
		Main.instance.reloadConfig();
		
		lang.set("SpawnEgg.Name", lang.getString("SpawnEgg.Name") + " Sv.{level}");
		
		try {lang.save(langFile);} catch (IOException e1) {}
		
		// ITEMS
		
		items.set("Items", null);
		
		for (String key : cfgK.getConfigurationSection("price").getKeys(false))
		{
			
			items.set("Items." + key.toLowerCase() + ".material", key.toLowerCase());
			
			items.set("Items." + key.toLowerCase() + ".price", cfgK.getDouble("price." + key));
			
		}
		
		try {items.save(itemsFile);} catch (IOException e) {}
		
		Commands.reloadCommand(Bukkit.getConsoleSender());
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6&lÇİFTÇİ &aOyuncu verileri çekiliyor..."));
		
		Bukkit.getScheduler().runTaskLater(Main.instance, () ->
		{
			
			for (Farmer farmer : kFarmerMigration.getFarmerInformations().values())
			{
				
				DatabaseQueries.registerIslandDatabase(farmer.getOwnerUUID(), farmer.getNpcLocation(), 1);
				
				FarmerManager.insertFarmerDataYML(farmer.getOwnerUUID());
				
				HashMap<String, Integer> count = kFarmerMigration.getFarmerValues(farmer.getOwnerUUID());
				
				HashMap<ConfigItems, Integer> itemValues = new HashMap<ConfigItems, Integer>();
				
				for (ConfigItems configItems : FarmerManager.STORED_ITEMS.values())
				{
					
					if (count.containsKey(configItems.getDataName().toLowerCase()))
					{
						
						itemValues.put(configItems, count.get(configItems.getDataName().toLowerCase()));
						
					}
					
					else itemValues.put(configItems, 0);
					
				}
				
				StorageAndValues toStore = new StorageAndValues(farmer, itemValues, null);
				
				DatabaseQueries.leaveEvent(farmer.getOwnerUUID(), toStore);
				
			}
			
			Bukkit.getConsoleSender().sendMessage(Main.color("&6&lÇİFTÇİ &aİşlem tamamlandı sunucuyu kapatıp açın!"));
			
		}, 100L);
		
	}

}
