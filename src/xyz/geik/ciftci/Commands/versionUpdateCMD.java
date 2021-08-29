package xyz.geik.ciftci.Commands;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;

public class versionUpdateCMD {
	
	public versionUpdateCMD() {}
	
	public void upgradeFromV4() {
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aEklenti &bv5 &asürümüne güncelleniyor!"));
		
		if (Bukkit.getPluginManager().getPlugin("Citizens") != null)
			Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("Citizens"));
		
		File cfgFile = new File("plugins/" + Main.instance.getDescription().getName() + "/config.yml");
		FileConfiguration cfgOld = YamlConfiguration.loadConfiguration(cfgFile);
		
		// AutoCollect
		boolean autoCollect = false;
		if (cfgOld.isSet("Settings.autoCollect"))
			autoCollect = cfgOld.getBoolean("Settings.autoCollect");
		
		boolean requirePiston = false;
		if (cfgOld.isSet("Settings.autoCollectWithPiston"))
			requirePiston = cfgOld.getBoolean("Settings.autoCollectWithPiston");
		
		boolean autoCollectWhenFull = false;
		if (cfgOld.isSet("Settings.autoCollectWhenFarmerFull"))
			autoCollectWhenFull = cfgOld.getBoolean("Settings.autoCollectWhenFarmerFull");
		
		boolean autoCollectWithoutFarmer = false;
		if (cfgOld.isSet("Settings.autoCollectWithoutFarmer"))
			autoCollectWithoutFarmer = cfgOld.getBoolean("Settings.autoCollectWithoutFarmer");
		
		boolean autoCollectToggle = false;
		if (cfgOld.isSet("Settings.autoCollectCanToggle"))
			autoCollectToggle = cfgOld.getBoolean("Settings.autoCollectCanToggle");
		
		// SpawnerKiller
		boolean killerWithoutFarmer = false;
		if (cfgOld.isSet("Settings.spawnerKillerWithoutFarmer"))
			killerWithoutFarmer = cfgOld.getBoolean("Settings.spawnerKillerWithoutFarmer");
		
		boolean killerToggle = false;
		if (cfgOld.isSet("Settings.toggleSpawnerKiller"))
			killerToggle = cfgOld.getBoolean("Settings.toggleSpawnerKiller");
		
		List<String> blackList = new ArrayList<String>();
		if (cfgOld.isSet("Settings.spawnerBlackList"))
			blackList = cfgOld.getStringList("Settings.spawnerBlackList");
		List<String> whiteList = new ArrayList<String>();
		if (cfgOld.isSet("Settings.spawnerWhiteList"))
			blackList = cfgOld.getStringList("Settings.spawnerWhiteList");
		
		// AutoSell
		boolean autoSell = false;
		if (cfgOld.isSet("Settings.autoSell"))
			autoSell = cfgOld.getBoolean("Settings.autoSell");
		
		boolean autoSellMessage = false;
		if (cfgOld.isSet("Settings.autoSellMessage"))
			autoSellMessage = cfgOld.getBoolean("Settings.autoSellMessage");
		
		// Others
		boolean onlyLeader = false;
		if (cfgOld.isSet("Settings.onlyLeader"))
			onlyLeader = cfgOld.getBoolean("Settings.onlyLeader");
		
		boolean levelBasedFarmer = false;
		if (cfgOld.isSet("Settings.onlyLeader"))
			onlyLeader = cfgOld.getBoolean("Settings.onlyLeader");
		
		// Buy Farmer
		final boolean buyFarmer = cfgOld.getBoolean("Settings.buyFarmer.feature");
		final int price = cfgOld.getInt("Settings.buyFarmer.price");
		final List<String> allowedWorlds = cfgOld.getStringList("Settings.defaultWorld");
		final int taxRate = cfgOld.getInt("tax.taxRate");
		final boolean depositToAcc = cfgOld.getBoolean("tax.depositToAcc");
		final String taxAcc = cfgOld.getString("tax.taxAcc");
		
		SimpleDateFormat formatter = new SimpleDateFormat("hhMMssyyyymmdd");
		String oldConfigName = "config4-" + formatter.format(new Date());
		cfgFile.renameTo(new File("plugins/" + Main.instance.getDescription().getName() + "/" + oldConfigName + ".yml"));
		
		File cfgFilex = new File("plugins/" + Main.instance.getDescription().getName() + "/config.yml");
		cfgFilex.delete();
		
		Main.instance.saveDefaultConfig();
		Main.instance.reloadConfig();
		
		FileConfiguration cfg = Main.instance.getConfig();
		
		// AutoCollect
		cfg.set("AddonSettings.autoCollect.feature", autoCollect);
		cfg.set("AddonSettings.autoCollect.withoutFarmer", autoCollectWithoutFarmer);
		cfg.set("AddonSettings.autoCollect.toggle", autoCollectToggle);
		cfg.set("AddonSettings.autoCollect.requirePiston", requirePiston);
		cfg.set("AddonSettings.autoCollect.ignoreStock", autoCollectWhenFull);
		
		// Others
		cfg.set("Settings.onlyLeader", onlyLeader);
		cfg.set("Settings.levelBasedFarmer", levelBasedFarmer);
		cfg.set("Settings.buyFarmer.feature", buyFarmer);
		cfg.set("Settings.buyFarmer.price", price);
		cfg.set("Settings.defaultWorld", allowedWorlds);
		
		cfg.set("tax.taxRate", taxRate);
		cfg.set("tax.depositToAcc", depositToAcc);
		cfg.set("tax.taxAcc", taxAcc);
		
		// AutoSell
		cfg.set("AddonSettings.autoSell.feature", autoSell);
		cfg.set("AddonSettings.autoSell.sendMessage", autoSellMessage);
		
		// Spawner Killer
		cfg.set("AddonSettings.spawnerKiller.withoutFarmer", killerWithoutFarmer);
		cfg.set("AddonSettings.spawnerKiller.toggle", killerToggle);
		if (!blackList.isEmpty())
			cfg.set("AddonSettings.spawnerKiller.blacklist", blackList);
		if (!whiteList.isEmpty())
			cfg.set("AddonSettings.spawnerKiller.whiteList", whiteList);
		
		File cfgLevelFile = new File("plugins/" + Main.instance.getDescription().getName() + "/" + oldConfigName + ".yml");
		FileConfiguration cfgLevel = YamlConfiguration.loadConfiguration(cfgLevelFile);
		
		for (String level : cfgLevel.getConfigurationSection("FarmerLevels").getKeys(false))
		{
			
			if (cfgLevel.isSet("FarmerLevels." + Integer.valueOf(level) + ".permission")) 
				Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".permission", 
						cfgLevel.getString("FarmerLevels." + Integer.valueOf(level) + ".permission"));
			
			if (cfgLevel.isSet("FarmerLevels." + Integer.valueOf(level) + ".taxRate")) 
				Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".taxRate", 
						cfgLevel.getInt("FarmerLevels." + Integer.valueOf(level) + ".taxRate"));
				
			Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".Capacity",
					cfgLevel.getInt("FarmerLevels." + Integer.valueOf(level) + ".Capacity"));
			
			Main.instance.getConfig().set("FarmerLevels." + Integer.valueOf(level) + ".nextRankMoney",
					cfgLevel.getInt("FarmerLevels." + Integer.valueOf(level) + ".nextRankMoney"));
			
		}
		
		Main.instance.saveConfig();
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aCitizens çiftçi npcleri siliniyor..."));
		
		File npcSaves = new File("plugins/Citizens/saves.yml");
		FileConfiguration npc = YamlConfiguration.loadConfiguration(npcSaves);
		
		String ciftciName = Main.color(Manager.getText("lang", "FarmerName"));
		
		List<String> npcList = new ArrayList<String>();
		
		for (String id : npc.getConfigurationSection("npc").getKeys(false))
			if (Main.color(npc.getString("npc." + id + ".name")).equalsIgnoreCase(ciftciName) )
				npcList.add(id);
			else continue;
		
		for (String ids : npcList)
			npc.set("npc." + ids, null);
		
		try {
			npc.save(npcSaves);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aCitizens verileri silindi..."));
		Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aLang dosyası yeniden oluşturuluyor..."));
		
		
		File langFile = new File("plugins/" + Main.instance.getDescription().getName() + "/lang.yml");
		
		String oldLangName = "langv4-" + formatter.format(new Date());
		langFile.renameTo(new File("plugins/" + Main.instance.getDescription().getName() + "/" + oldLangName + ".yml"));
		
		File langFilex = new File("plugins/" + Main.instance.getDescription().getName() + "/lang.yml");
		langFilex.delete();
		
		Manager.FileChecker("lang");
		
		if (levelBasedFarmer) {
			
			File newLangFile = new File("plugins/" + Main.instance.getDescription().getName() + "/lang.yml");
			FileConfiguration langNew = YamlConfiguration.loadConfiguration(newLangFile);
			
			langNew.set("SpawnEgg.Name", langNew.getString("SpawnEgg.Name") + " Sv.&e{level}");
			
			try {
				langNew.save(langFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aLang dosyası yeniden oluşturuldu..."));
		
		Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aSunucuyu kapatıp açın."));
		
		Bukkit.getPluginManager().disablePlugin(Main.instance);
		
	}
	
	public void updateData() {
		
		try {
			
			File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
			FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
			
			if (data.getConfigurationSection("data").getKeys(false) != null
					&& !data.getConfigurationSection("data").getKeys(false).isEmpty()) {
				
				if (!data.isSet("players")
						|| data.getConfigurationSection("players").getKeys(false) == null
						|| data.getConfigurationSection("players").getKeys(false).isEmpty()) {
					
					for (String id : data.getConfigurationSection("data").getKeys(false)) {
						
						List<String> players = new ArrayList<String>();
						if (!data.getStringList("data." + id).isEmpty())
							for (String player : data.getStringList("data." + id))
								if (!players.contains(player))
									players.add(player);
						
						String owner = id;
						if (onEnableShortcut.USE_OWNER)
							owner = ApiFun.getOwnerViaID(id).getName();
						else owner = Bukkit.getOfflinePlayer(UUID.fromString(id)).getName();
						
						if (!players.contains(owner))
							players.add(owner);
						
						for (String player : players) {
							List<String> areas = new ArrayList<String>();
							if (data.isSet("players." + player + ".areas"))
								areas = data.getStringList("players." + player + ".areas");
							areas.add(id);
							data.set("players." + player + ".areas", areas);
						}
						
					}
					
				}
				
				try {
					data.save(dataFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Bukkit.getConsoleSender().sendMessage(Main.color("&aData verisi başarıyla güncellendi."));
				
			}
			
		}
		
		catch (NullPointerException e1) {return;}
		
	}

}
