package xyz.geik.ciftci.Commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.DataSource.mFarmerMigration;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Cache.ConfigItems;
import xyz.geik.ciftci.Utils.Cache.Farmer;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;

public class mFarmerMigrationCMD {
	
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
		
		// MFARMER
		
		File mFarmerConfig = new File("plugins/Farmer/config.yml");
		
		FileConfiguration cfgM = YamlConfiguration.loadConfiguration(mFarmerConfig);
		
		Main.instance.getConfig().set("FarmerLevels", null);
		
		if (cfgM.getBoolean("level.enabled"))
			for (String level : cfgM.getConfigurationSection("level.levels").getKeys(false))
			{
					
				Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".Capacity", Integer.valueOf(cfgM.getInt("level.levels." + level + ".capacity")));
				
				Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".nextRankMoney", Integer.valueOf(cfgM.getInt("level.levels." + level + ".price")));
				
			}
		
		Main.instance.getConfig().set("Settings.levelBasedFarmer", true);
		
		Main.instance.getConfig().set("Settings.depositMethod.defaultDepositLeader", true);
		
		Main.instance.getConfig().set("tax.taxRate", cfgM.getInt("tax.default"));
		
		Main.instance.saveConfig();
		
		Main.instance.reloadConfig();
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aCitizens çiftçi npcleri siliniyor..."));
		
		File npcSaves = new File("plugins/Citizens/saves.yml");
		FileConfiguration npc = YamlConfiguration.loadConfiguration(npcSaves);
		
		String ciftciName = Main.color(cfgM.getString("NPC.name"));
		
		List<String> npcList = new ArrayList<String>();
		
		for (String id : npc.getConfigurationSection("npc").getKeys(false))
			if (Main.color(npc.getString("npc." + id + ".name")).equalsIgnoreCase(ciftciName) )
				npcList.add(id);
			else continue;
		
		for (String ids : npcList)
			npc.set("npc." + ids, null);
		
		try {
			npc.save(npcSaves);
		} catch (IOException e1) {}
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aCitizens verileri silindi..."));
		
		lang.set("SpawnEgg.Name", lang.getString("SpawnEgg.Name") + " Sv.&e{level}");
		
		try {lang.save(langFile);} catch (IOException e1) {}
		
		// ITEMS
		
		items.set("Items", null);
		
		for (String key : cfgM.getConfigurationSection("Items").getKeys(false))
		{
			
			String material = cfgM.getString("Items." + key + ".material").toLowerCase();
			
			items.set("Items." + material + ".material", material);
			
			items.set("Items." + material + ".price", cfgM.getDouble("Items." + key + ".salePrice"));
			
			if (cfgM.getInt("Items." + key + ".data") != 0)
				items.set("Items." + material + ".damage", cfgM.getInt("Items." + key + ".data"));
			
		}
		
		try {items.save(itemsFile);} catch (IOException e) {}
		
		Commands.reloadCommand(Bukkit.getConsoleSender());
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6&lÇİFTÇİ &aOyuncu verileri çekiliyor..."));
		
		Bukkit.getScheduler().runTaskLater(Main.instance, () ->
		{
			
			for (Farmer farmer : mFarmerMigration.getFarmerInformations().values())
			{
				
				DatabaseQueries.registerIslandDatabase(farmer.getOwnerUUID(), farmer.getNpcLocation(), 1);
				
				FarmerManager.insertFarmerDataYML(farmer.getOwnerUUID());
				
				HashMap<String, Integer> count = mFarmerMigration.getFarmerValues(farmer.getOwnerUUID());
				
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
