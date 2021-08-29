package xyz.geik.ciftci.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.angeschossen.lands.api.integration.LandsIntegration;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Metrics;
import xyz.geik.ciftci.Commands.Commands;
import xyz.geik.ciftci.Commands.versionUpdateCMD;
import xyz.geik.ciftci.DataSource.ConnectionPool;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Listeners.ListenerRegister;
import xyz.geik.ciftci.Listeners.BackEndListeners.onJoinEvent;
import xyz.geik.ciftci.Listeners.BackEndListeners.SpawnerEvent.spawnerEvent;
import xyz.geik.ciftci.Utils.API.ApiType;
import xyz.geik.ciftci.Utils.Cache.ConfigItems;
import xyz.geik.ciftci.Utils.NPC.skin.NPCTextures;

public class onEnableShortcut
{
	
	public static Economy econ = null;
	
	private static Permission perms = null;
	
	public static boolean autoCollectWithoutFarmer = false;
	
	public static boolean spawnerKillerWithoutFarmer = false;
	
	public static boolean USE_OWNER = false;
	
	public static boolean playerDropCancel = false;
	
	public static boolean scanOnlyPlayer = false;
	
	public static LandsIntegration lands;
	
	public static List<String> farmerIdMap = new ArrayList<String>();
	
	public onEnableShortcut()
	{
		
		Main.instance.saveDefaultConfig();
		
		Main.instance.getCommand("çiftçi").setExecutor(new Commands(Main.instance));
		
		Manager.FileChecker("lang");
		
		Manager.FileChecker("data");
		
		Manager.FileChecker("items");
		
		ConnectionPool.initsqlite();
		
		DatabaseQueries.createTable();
		
		//UPDATE
		DatabaseQueries.updateDataBaseAlter("farmerLocation", "varchar(64) DEFAULT null");
		
		DatabaseQueries.updateDataBaseAlter("farmerSkin", "varchar(30) DEFAULT null");
		
		DatabaseQueries.updateDataBaseAlter("autoSell", "int DEFAULT 0");
	
		DatabaseQueries.updateDataBaseAlter("autoCollect", "int DEFAULT 0");
		
		DatabaseQueries.updateDataBaseAlter("spawnerKill", "int DEFAULT 0");
		
		if (Main.instance.getConfig().isSet("Settings.despawnNPC")) {
			new versionUpdateCMD().upgradeFromV4();
			return;
		}
		
		if (Main.instance.getConfig().isSet("Settings.useClaims")
				&& Main.instance.getConfig().getBoolean("Settings.useClaims"))
			USE_OWNER = true;
		
		if (!Manager.isSet("data", "players"))
			new versionUpdateCMD().updateData();
		//
		
		if (Main.instance.getConfig().isSet("DetailedSettings.scanOnlyAdded")
				&& Main.instance.getConfig().getBoolean("DetailedSettings.scanOnlyAdded"))
			scanOnlyPlayer = true;
		
		if (Main.instance.getConfig().isSet("DetailedSettings.shutDown")
						&& Main.instance.getConfig().getBoolean("DetailedSettings.shutDown"))
			Main.isShutdowned = true;
		
		if (Main.instance.getConfig().getBoolean("Settings.playerDropCancel"))
			playerDropCancel = true;
		
		setupEconomy();
		
		FarmerManager.WORLDS = Main.instance.getConfig().getStringList("Settings.defaultWorld");
		
		FarmerManager.updateSpawnEgg();
		
		FarmerManager.fillItemUpdater();
		
		setupPermissions();
		
		Main.DEFAULT_SKIN = NPCTextures.toProperty(Manager.getText("lang", "FarmerSkin"));
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
			loadItemPrices();
		});
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.instance, () ->
		{
			
			joinPlayers();
			
		}, 200L);
		
		Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
			
			new ListenerRegister();
			
			customChartedMetric();
			
		}, 40L);
		
	}
	
	public static void getWorkingPlugin()
	{
		
		Bukkit.getScheduler().runTask(Main.instance, () ->
		{
				
				if (Bukkit.getPluginManager().getPlugin("ASkyBlock") != null) Main.API = ApiType.ASkyBlock;
				else if (Bukkit.getPluginManager().getPlugin("FabledSkyBlock") != null) Main.API = ApiType.FabledSkyblock;
				else if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) Main.API = ApiType.GriefPrevention;
				else if (Bukkit.getPluginManager().getPlugin("SuperiorSkyblock2") != null) Main.API = ApiType.SuperiorSkyblock;
				else if (Bukkit.getPluginManager().getPlugin("IridiumSkyblock") != null) Main.API = ApiType.IridiumSkyblock;
				else if (Bukkit.getPluginManager().getPlugin("BentoBox") != null) Main.API = ApiType.BentoBox;
				else if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null) Main.API = ApiType.PlotSquared;
				else if (Bukkit.getPluginManager().getPlugin("UltimateClaims") != null) Main.API = ApiType.UltimateClaims;
				else if (Bukkit.getPluginManager().getPlugin("hClaims") != null) Main.API = ApiType.hClaims;
				else if (Bukkit.getPluginManager().getPlugin("InfClaim") != null) Main.API = ApiType.InfClaim;
				else if (Bukkit.getPluginManager().getPlugin("Lands") != null)
				{
					
					lands = new LandsIntegration(Main.instance);
					Main.API = ApiType.Lands;
					
				}
				else Main.API = ApiType.NULL;
			
		});
		
	}
	
	public static Permission getPermissions() {
        return perms;
    }
	
	private boolean setupPermissions()
	{
		
        RegisteredServiceProvider<Permission> rsp = Main.instance.getServer().getServicesManager().getRegistration(Permission.class);
        
        perms = rsp.getProvider();
        
        return perms != null;
        
    }
	
	private boolean setupEconomy()
	{
		
        if (Main.instance.getServer().getPluginManager().getPlugin("Vault") == null)
        {
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = Main.instance.getServer().getServicesManager().getRegistration(Economy.class);
        
        if (rsp == null)
        {
            return false;
        }
        
        econ = rsp.getProvider();
        
        return econ != null;
        
    }
    
    public static Economy getEconomy()
    {
        return econ;
    }
    
    @SuppressWarnings("deprecation")
	public static void loadItemPrices()
    {
				
		Set<String> configValue = Manager.getConfigurationSection("items", "Items");
		
		for (String dataName : configValue)
		{
			
			if (Material.getMaterial(Manager.getText("items", "Items." + dataName + ".material").toUpperCase()) != null)
			{
				
				int damage = 0;
				
				if (Manager.isSet("items", "Items." + dataName + ".damage")) damage = Manager.getInt("items", "Items." + dataName + ".damage");
				
				double price = Manager.getDouble("items", "Items." + dataName + ".price");
				
				String material = Manager.getText("items", "Items." + dataName + ".material");
				
				boolean autoCollect = false;
				
				ItemStack item = new ItemStack(Material.getMaterial(material.toUpperCase()));
				
				if (damage != 0)
				{
					DatabaseQueries.registerProduct( (Manager.getText("items", "Items." + dataName + ".material") + "_" + damage).toLowerCase() );
					
					item.setDurability((short) damage);
				}
				
				else
				{
					DatabaseQueries.registerProduct(Manager.getText("items", "Items." + dataName + ".material").toLowerCase());
				}
				
				if (Manager.isSet("items", "Items." + dataName + ".autoCollect") 
						&& Manager.getBoolean("items", "Items." + dataName + ".autoCollect"))
				{
					
					autoCollect = true;
					
					FarmerManager.autoCollectMaterials.put(FarmerManager.farmItemTypeCalculator(material.toUpperCase()), item);
					
				}
				
				ConfigItems configItems = new ConfigItems(dataName, material, damage, price, autoCollect, FarmerManager.farmItemTypeCalculator(material.toUpperCase()));
				
				FarmerManager.STORED_ITEMS.put(item, configItems);
				
				FarmerManager.CONFIG_TO_GUI.add(item);
				
			}
			
			else Bukkit.getConsoleSender().sendMessage(Main.color("&4&lHATA &b" + dataName + ">"
					+ Manager.getText("items", "Items." + dataName + ".material") + " &cDiye bir materyal bulunamadı! Geçiliyor..."));
			
		}
		
		if (Main.instance.getConfig().isSet("AddonSettings.spawnerKiller.blacklist"))
		{
			
			spawnerEvent.blockedMobs.addAll(Main.instance.getConfig().getStringList("AddonSettings.spawnerKiller.blacklist"));
			
		}
		
		if (Main.instance.getConfig().isSet("AddonSettings.spawnerKiller.whitelist"))
		{
			
			spawnerEvent.allowedMobs.addAll(Main.instance.getConfig().getStringList("AddonSettings.spawnerKiller.whitelist"));
			
		}
		
		if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.withoutFarmer"))
			autoCollectWithoutFarmer = Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.withoutFarmer");
		
		if (Main.instance.getConfig().isSet("AddonSettings.spawnerKiller.withoutFarmer"))
			spawnerKillerWithoutFarmer = Main.instance.getConfig().getBoolean("AddonSettings.spawnerKiller.withoutFarmer");
		
	}
    
    public static void joinPlayers()
    {
    	
    	if (!Main.instance.getConfig().getBoolean("Settings.requireOnline"))
    		DatabaseQueries.insertAllFarmers();
    	
    	else
    	{
    		
    		DatabaseQueries.autoCollectHandler();
    		
    		for (Player player : Bukkit.getOnlinePlayers())
    			onJoinEvent.loginEvent(player);
    		
    	}
    	
    }

    private void customChartedMetric() {
    	
    	Metrics metrics = new Metrics(Main.instance, 9646);
    	metrics.addCustomChart(new Metrics.SingleLineChart("ciftci_sayisi", new Callable<Integer>() {
    		@Override
            public Integer call() throws Exception {
                return FarmerManager.farmerCache.size();
            }
        }));
    	
    	metrics.addCustomChart(new Metrics.SimplePie("api_eklentisi", new Callable<String>() {
			@Override
			public String call() throws Exception {
				return Main.API.name();
			}
        }));
    	
    }
    
}
