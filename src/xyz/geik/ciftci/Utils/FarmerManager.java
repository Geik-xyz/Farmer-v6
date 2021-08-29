package xyz.geik.ciftci.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Listeners.BackEndListeners.npcClickEvent;
import xyz.geik.ciftci.Listeners.BackEndListeners.onJoinEvent;
import xyz.geik.ciftci.Listeners.BackEndListeners.onQuitEvent;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.API.FarmItemType;
import xyz.geik.ciftci.Utils.Cache.ConfigItems;
import xyz.geik.ciftci.Utils.Cache.Farmer;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;
import xyz.geik.ciftci.Utils.Items.Item;
import xyz.geik.ciftci.Utils.NPC.listener.MoveListener;
import xyz.geik.ciftci.Utils.NPC.npc.NPC;
import xyz.geik.ciftci.Utils.NPC.npc.NPCEventHandler;
import xyz.geik.ciftci.Utils.NPC.npc.NPCInventory.EquipmentSlot;
import xyz.geik.ciftci.Utils.NPC.npc.impl.NPCImpl;
import xyz.geik.ciftci.Utils.NPC.skin.NPCTextures;

public class FarmerManager
{
	
	/**
	 * @apiNote SpawnEgg update onEnable
	 */
	public static ItemStack SPAWN_EGG;
	
	public static ItemStack fillItem;
	
	/**
	 * @apiNote Stored Items
	 */
	public static List<ItemStack> CONFIG_TO_GUI = new ArrayList<ItemStack>();
	
	public static HashMap<ItemStack, ConfigItems> STORED_ITEMS = new HashMap<ItemStack, ConfigItems>();
	
	public static HashMap<String, StorageAndValues> farmerCache = new HashMap<String, StorageAndValues>();
	
	public static HashMap<FarmItemType, ItemStack> autoCollectMaterials = new HashMap<FarmItemType, ItemStack>();
	
	public static List<String> farmerIdMap = new ArrayList<String>();
	
	public static List<String> WORLDS;
	
	public static List<String> farmerExclude = new ArrayList<String>();
	
	public static List<Integer> NPCList = new ArrayList<Integer>();
	
	
	
	@SuppressWarnings("deprecation")
	public static void sellModifier(String uuid, Player player, double money, HashMap<ConfigItems, Integer> values, ConfigItems configItem, double taxMoney, boolean autoSell, String owner)
	{
		
		if (autoSell)
		{
			
			onEnableShortcut.econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(owner)), money);
			
			if (Main.instance.getConfig().isSet("Settings.autoSellMessage") &&
					Main.instance.getConfig().getBoolean("Settings.autoSellMessage") &&
					Bukkit.getOfflinePlayer(UUID.fromString(owner)).isOnline())
				Bukkit.getPlayer(UUID.fromString(owner)).sendMessage(
						Manager.getText("lang", "sellComplete").replace("{tax}", Manager.roundDouble(taxMoney, 2)).replace("{money}", Manager.roundDouble(money, 2)));
				
		}
		
		else 
		{
			
			if (!Main.instance.getConfig().getBoolean("Settings.depositMethod.leaderChoose"))
			{
			 
				if (Main.instance.getConfig().getBoolean("Settings.depositMethod.defaultDepositLeader")) 
					onEnableShortcut.econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(owner)), money);
			 
				else 
					onEnableShortcut.econ.depositPlayer(player, money);
			 
			}
			
			else
			{
			 
				int method = FarmerManager.farmerCache.get(uuid).getStorage().getSellingStatus();
			 
				if (method == 1) onEnableShortcut.econ.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(owner)), money);
			 
				else onEnableShortcut.econ.depositPlayer(player, money);
			 
			}
			
		}
 
		values.replace(configItem, 0);
 
		FarmerManager.farmerCache.get(uuid).setItemValues(values);
 
		if (Main.instance.getConfig().getBoolean("tax.depositToAcc") && taxMoney > 0) 
			onEnableShortcut.econ.depositPlayer(Bukkit.getOfflinePlayer(Main.instance.getConfig().getString("tax.taxAcc")), taxMoney);
		
	}
	
	public static void joinHandler(String ownerUUID, Player joiner)
	{
		
		try
		{
			
			if (farmerCache.containsKey(ownerUUID))
				return;
			
			StorageAndValues values = DatabaseQueries.getAllValuesOfPlayer(ownerUUID);
				
			if (values.getStorage().getFarmerID() != 96456)
			{
				
				if (values.getStorage().getNpcLocation() != null) {
					
					NPCImpl npc = createFarmer(values);
					
					values.setNPC(npc);
					
					values.getStorage().setFarmerID(npc.getEntityId());
					
				}
				
				else {
					
					values.setNPC(null);
					
					values.getStorage().setFarmerID(new Random().nextInt());
					
				}
				
				insertCookie(ownerUUID, values);
				
				if (farmerIdMap.contains(values.getStorage().getOwnerUUID())
						&& !values.getStorage().getAutoCollect())
					farmerIdMap.remove(values.getStorage().getOwnerUUID());
				
			}
			
			else return;
			
		}
		
		catch (NullPointerException e1) {  }
		
	}
	
	public static void leaveHandler(String ownerUUID, StorageAndValues values)
	{
		
		try
		{
			
			if (!farmerCache.containsKey(ownerUUID)) return;
				
			Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
			{
				
				DatabaseQueries.leaveEvent(ownerUUID, values);
				
				removeCookie(ownerUUID);
				
			});
			
		}
		
		catch(NullPointerException e1) {  }
		
	}
	
	@SuppressWarnings("deprecation")
	public static void fillItemUpdater()
	{
		
		Material material = null;
		
		String nms = Manager.getNMSVersion();
		
		String mat = null;
		
		try
		{
			
			if (Manager.isSet("lang", "Gui.fillItem.material"))
			{
				
				if (Material.getMaterial(Manager.getText("lang", "Gui.fillItem.material").toUpperCase()) != null)
				{
					material = Material.getMaterial(Manager.getText("lang", "Gui.fillItem.material").toUpperCase());
				}
				
				else mat = "STAINED_GLASS_PANE";
				
			}
			
			else mat = "STAINED_GLASS_PANE";
			
		}
		catch (NullPointerException e1){}
		
		if (mat != null)
		{
			
			if (Material.getMaterial(mat) != null)
				material = Material.getMaterial(mat);
			
			else
				material = Material.GRAY_STAINED_GLASS_PANE;
			
		}
		
		if (material == null) {
			fillItem = null;
			return;
		}
		
		String name = Main.color("&7");
		
		ItemStack toStore = Item.defaultItem(name, new ArrayList<String>(), material);
		
		if (Manager.isSet("lang", "Gui.fillItem.damage") 
				&& !(nms.contains("1_13") || nms.contains("1_14") || nms.contains("1_15") || nms.contains("1_16")
						|| nms.contains("1_17")))
		{
			
			int damage = Integer.valueOf(Manager.getText("lang", "Gui.fillItem.damage"));
			
			toStore.setDurability((short) damage);
			
		}
		
		else if (!Manager.isSet("lang", "Gui.fillItem.damage") 
				&& !(nms.equalsIgnoreCase("1_13") || nms.equalsIgnoreCase("1_14") || nms.equalsIgnoreCase("1_15") || nms.equalsIgnoreCase("1_16")
						|| nms.equalsIgnoreCase("1_17")))
		{
			
			toStore.setDurability((short) 7);
			
		}
		
		fillItem = toStore;	
		
	}
	
	
	@SuppressWarnings("deprecation")
	public static void updateSpawnEgg()
	{
		
		Material material;
		
		try
		{
			
			if (Manager.isSet("lang", "SpawnEgg.material"))
			{
				
				if (Material.getMaterial(Manager.getText("lang", "SpawnEgg.material").toUpperCase()) != null)
				{
					material = Material.getMaterial(Manager.getText("lang", "SpawnEgg.material").toUpperCase());
				}
				
				else material = Material.EGG;
				
			}
			
			else material = Material.EGG;
			
		}
		
		catch (NullPointerException e1) {material = Material.EGG;}
		
		ItemStack toStore = Item.defaultItem(Manager.getText("lang", "SpawnEgg.Name"), Manager.getLore("lang", "SpawnEgg.Lore"), material);
		
		if (Manager.isSet("lang", "SpawnEgg.damage"))
		{
			
			int damage = Integer.valueOf(Manager.getText("lang", "SpawnEgg.damage"));
			
			toStore.setDurability((short) damage);
			
		}
		
		ItemMeta meta = toStore.getItemMeta();
		
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		toStore.setItemMeta(meta);
		
		SPAWN_EGG = toStore;
		
	}
	
	public static StorageAndValues createFreshFarmer(Location location, int farmerLevel, Player putter)
	{
		
		String owner = ApiFun.getIslandOwnerUUID(location);
		
		insertFarmerDataYML(owner);
		
		StorageAndValues values = DatabaseQueries.registerIslandDatabase(owner, location, farmerLevel);
		
		NPCImpl npc = createFarmer(values);
		
		values.setNPC(npc);
		
		values.getStorage().setFarmerID(npc.getEntityId());
		
		if (onEnableShortcut.autoCollectWithoutFarmer)
			values.getStorage().setAutoCollect(true);
		
		if (onEnableShortcut.spawnerKillerWithoutFarmer)
			values.getStorage().setSpawnerKill(true);
		
		insertCookie(owner, values);
		
        return values;
		
	}
	
	public static void insertCookie(String owner, StorageAndValues cookie)
	{
		
		if (!farmerCache.containsKey(owner)) {
			farmerCache.put(owner, cookie);
			cookie.getNPC().setSpawned(true);
		}
		
	}
	
	public static void removeCookie(String owner)
	{
		
		try {
			if (farmerCache.get(owner) != null
					&& farmerCache.get(owner).getNPC() != null
					& farmerCache.get(owner).getNPC().isSpawned())
				farmerCache.get(owner).getNPC().setSpawned(false);
		}
		
		catch (NullPointerException e1) {}
		
		if (farmerCache.containsKey(owner))
			farmerCache.remove(owner);
	}
	
	@SuppressWarnings("deprecation")
	public static NPCImpl createFarmer(StorageAndValues values)
	{
		
		Farmer farmer = values.getStorage();
		NPCImpl npc = new NPCImpl(Main.instance);
		
		String name = Manager.getText("lang", "FarmerName");
		
		if (Main.multiplier > 1)
			name = Main.color(Manager.getText("lang", "FarmerName") + " &6x" + Main.multiplier);
		
		if (name.length() >= 16)
			name = Manager.getText("lang", "FarmerName");
		
		npc.setName(  name  );
		if (farmer.getSkinName() != null)
			npc.skin = farmer.getSkinName();
		if (!farmer.getSkinName().equalsIgnoreCase(Manager.getText("lang", "FarmerSkin")))
			npc.updateProperty();
		else
			npc.property = Main.DEFAULT_SKIN;
		
		npc.setUuid(UUID.randomUUID());
		npc.setLocation(values.getStorage().getNpcLocation());
		
		// ID Calculator
		File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
		FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
		
		int id = 0;
		if (data.isSet("npc"))
			if (data.getInt("npc") >= 2000)
				id = 0;
			else
				id = data.getInt("npc");
		id++;
		
		data.set("npc", id);
		try {data.save(dataFile);} catch (IOException e) {}
		
		npc.setEntityId(id);
		npc.setTextures(new NPCTextures());
		npc.setSneaking(false);
		
		// NMS Itemstack
		String nms = Manager.getNMSVersion();
		if (nms.contains("1_8") || nms.contains("1_12") | nms.contains("1_16"))
			npc.setEquipmentSlot(EquipmentSlot.HAND, new ItemStack(Material.WHEAT));
		
		// Events
		npc.addEventHandler(new NPCEventHandler() {
			
		    @Override
		    public void onInteract(NPC npc, Player player, InteractType type)
		    {
		    	
		    	npcClickEvent.NpcClick(npc, player);
		    	
		    }
		});
		
		// Owner adder
		if (onEnableShortcut.USE_OWNER) {
			
			if (ApiFun.getOwnerViaID(farmer.getOwnerUUID()).isOnline()) 
				npc.addPlayer(ApiFun.getOwnerViaID(farmer.getOwnerUUID()).getPlayer()); 
			else
				npc.addOfflinePlayer(ApiFun.getOwnerViaID(farmer.getOwnerUUID()).getUniqueId());
			
		} else {
			
			if (Bukkit.getOfflinePlayer(UUID.fromString(farmer.getOwnerUUID())).isOnline()) 
				npc.addPlayer(Bukkit.getPlayer(UUID.fromString(farmer.getOwnerUUID()))); 
			else
				npc.addOfflinePlayer(UUID.fromString(farmer.getOwnerUUID()));
			
		}
		
		// Members adder
		if (data.contains("data." + farmer.getOwnerUUID()) 
				&& data.getStringList("data." + farmer.getOwnerUUID()) != null
				&& !data.getStringList("data." + farmer.getOwnerUUID()).isEmpty())
			for (String member : data.getStringList("data." + farmer.getOwnerUUID()))
				if (Bukkit.getOfflinePlayer(member).isOnline()) 
					npc.addPlayer(Bukkit.getPlayer(member));
				else
					npc.addOfflinePlayer(Bukkit.getOfflinePlayer(member).getUniqueId());
		
		if (!NPCList.contains(id))
			NPCList.add(id);
		
		return npc;
		
	}
	
	public static void insertFarmerDataYML(String owner)
	{
		
		File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
		
		FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
		OfflinePlayer uuid = null;
		
		if (onEnableShortcut.USE_OWNER)
			uuid = ApiFun.getOwnerViaID(owner);
		
		else
			uuid = Bukkit.getOfflinePlayer(UUID.fromString(owner));
		
		// OWNER
		List<String> areas = new ArrayList<String>();
		if (data.isSet("players." + uuid.getName() + ".areas"))
		{
			
			if (!data.getStringList("players." + uuid.getName() + ".areas").isEmpty())
				areas = data.getStringList("players." + uuid.getName() + ".areas");
			
		}
		
		if (!areas.contains(owner)) {
			areas.add(owner);
			data.set("players." + uuid.getName() + ".areas", areas);
		}
		//
		
		List<String> members = new ArrayList<String>();
		// Members
		for (OfflinePlayer member : ApiFun.getLandPlayers(  uuid, owner  ))
			if (member.getUniqueId().toString().equalsIgnoreCase(uuid.getUniqueId().toString()))
				continue;
			else {
				List<String> memberAreas = new ArrayList<String>();
				if (data.isSet("players." + member.getName() + ".areas")
						&& !data.getStringList("players." + member.getName() + ".areas").isEmpty())
					memberAreas = data.getStringList("players." + member.getName() + ".areas");
				
				memberAreas.add(owner);
				data.set("players." + member.getName() + ".areas", memberAreas);
				members.add(member.getName());
			}
		//
		
		data.set("data." + owner, members);
		
		try {	data.save(dataFile);	}
		
		catch (IOException e) {}
		
	}
	
	public static boolean removeFarmerDataYML(String owner)
	{
		
		File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
		
		FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
		
		OfflinePlayer uuid = null;
		if (onEnableShortcut.USE_OWNER)
			uuid = ApiFun.getOwnerViaID(owner);
		else
			uuid = Bukkit.getOfflinePlayer(UUID.fromString(owner));
		
		if (data.isSet("players." + uuid.getName() + ".areas")
				&&	!data.getStringList("players." + uuid.getName() + ".areas").isEmpty()) {
				
				List<String> areas = data.getStringList("players." + uuid.getName() + ".areas");
				
				if (areas.size() == 1 && areas.contains(owner))
					data.set("players." + uuid.getName(), null);
				
				else if (areas.size() > 1 && areas.contains(owner))
				{
					areas.remove(owner);
					data.set("players." + uuid.getName() + ".areas", areas);
				}
				
		}
		
		if (data.isSet("data." + owner))
		{
			
			if (!data.getStringList("data." + owner).isEmpty())
				for (String member : data.getStringList("data." + owner))
				{
					if (data.isSet("players." + member + ".areas")
							&&	!data.getStringList("players." + member + ".areas").isEmpty()) {
							
							List<String> areas = data.getStringList("players." + member + ".areas");
							
							if (areas.size() == 1 && areas.contains(owner))
								data.set("players." + member, null);
							
							else if (areas.size() > 1 && areas.contains(owner))
							{
								areas.remove(owner);
								data.set("players." + member + ".areas", areas);
							}
							
					}
					
				}
			
			data.set("data." + owner, null);
			
			try {	data.save(dataFile);	}
			
			catch (IOException e) {}
			
			return true;
			
		}
		
		else return false;
		
	}
	
	public static void addMember(String owner, Player player)
	{
		
		try {
			
			if (Manager.isSet("data", "data." + owner) && !Manager.getLore("data", "data." + owner).contains(player.getName())
					&& !owner.equalsIgnoreCase(player.getUniqueId().toString()))  {
				
				if (FarmerManager.farmerCache.containsKey(owner)
						&& !FarmerManager.farmerCache.get(owner).getStorage().getMemberList().contains(player)
						&& player.isOnline())
				{
					
					FarmerManager.farmerCache.get(owner).getStorage().getMemberList().add(player);
					if (!FarmerManager.farmerCache.get(owner).getNPC().players.contains(player))
						FarmerManager.farmerCache.get(owner).getNPC().addPlayer(player);
					
					if (MoveListener.npcCache.containsKey(player.getName()))
						MoveListener.npcCache.get(player.getName()).add(owner);
					
					else {
						List<String> farmers = new ArrayList<String>();
						farmers.add(owner);
						MoveListener.npcCache.put(player.getName(), farmers);
					}
					
				}
				
				Bukkit.getScheduler().runTask(Main.instance, () ->
				{
					
					try
					{
						
						File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
						FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
						
						// PLAYERS
						List<String> areas = new ArrayList<String>();
						if (data.isSet("players." + player.getName() + ".areas") &&
								!data.getStringList("players." + player.getName() + ".areas").isEmpty())
							areas = data.getStringList("players." + player.getName() + ".areas");
						
						if (!areas.contains(owner)) {
							areas.add(owner);
							data.set("players." + player.getName() + ".areas", areas);
						}
						//
						
						// DATA
						List<String> players = new ArrayList<String>();
						if (data.getStringList("data." + owner) != null &&
								!data.getStringList("data." + owner).isEmpty())
							players = data.getStringList("data." + owner);
						
						if (!players.contains(player.getName())) {
							players.add(player.getName());
							data.set("data." + owner, players);	
						}
						//
						
						try {	data.save(dataFile);	}
						
						catch (IOException e) {}
						
					}
					
					catch (NullPointerException e1) {}
					
				});
				
			}
			
			else return;
			
		}
		
		catch (NullPointerException e1) {}
		
	}
	
	public static void removeMember(String owner, Player player)
	{
		
		onQuitEvent.leaveEvent(player);
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
			
			File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
			
			FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
			
			try
			{

				if (data.isSet("data." + owner))
				{
					
					//Players
					if (data.isSet("players." + player.getName() + ".areas")
							&&	!data.getStringList("players." + player.getName() + ".areas").isEmpty()) {
							
						List<String> areas = data.getStringList("players." + player.getName() + ".areas");
						
						if (areas.size() == 1 && areas.contains(owner))
							data.set("players." + player.getName(), null);
						
						else if (areas.size() > 1 && areas.contains(owner))
						{
							areas.remove(owner);
							data.set("players." + player.getName() + ".areas", areas);
						}
							
					}
					//
					
					List<String> list = data.getStringList("data." + owner);
					
					if (list == null || list.isEmpty())
						return;
					
					if (list.contains(player.getName()))
					{
						
						list.remove(player.getName());
						
						data.set("data." + owner, list);
						
						try {	data.save(dataFile);	}
						
						catch (IOException e) {}
						
					}
					
				}
				
			}
			
			catch (NullPointerException e1) {}
			
		});
		
	}
	
	@SuppressWarnings("deprecation")
	public static void changeFarmerOwner(String oldOwner, String newOwner)
	{
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
			
			if ( FarmerManager.farmerCache.containsKey(oldOwner) )
			{
				
				StorageAndValues storage = FarmerManager.farmerCache.get(oldOwner);
				
				if (storage.getStorage().getAutoSell() && Main.autoSell)
					storage.getStorage().setAutoSell(false);
				
				if (storage.getStorage().getSpawnerKill() && Main.spawnerKiller)
					storage.getStorage().setSpawnerKill(false);
				
				if (storage.getStorage().getAutoCollect() && Main.autoCollector)
					storage.getStorage().setAutoCollect(false);
				
				FarmerManager.farmerCache.put(newOwner, storage);
				
				FarmerManager.farmerCache.get(newOwner).getStorage().getMemberList().add(Bukkit.getOfflinePlayer(UUID.fromString(oldOwner)));
				
				FarmerManager.farmerCache.remove(oldOwner);
				
			}
			
			DatabaseQueries.updateOwner(oldOwner, newOwner);
			
			File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
			FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
			
			List<String> members = data.getStringList("data." + oldOwner);
			
			if (MoveListener.npcCache.containsKey(Bukkit.getPlayer(UUID.fromString(oldOwner)).getName()))
				MoveListener.npcCache.remove(Bukkit.getPlayer(UUID.fromString(oldOwner)).getName());
			
			if (members.contains(Bukkit.getPlayer(UUID.fromString(newOwner)).getName()))
				members.remove(Bukkit.getPlayer(UUID.fromString(newOwner)).getName());
			
			members.add(Bukkit.getPlayer(UUID.fromString(oldOwner)).getName());
			
			data.set("data." + newOwner, members);
			
			data.set("data." + oldOwner, null);
			
			for (String member : members) {
				if (MoveListener.npcCache.containsKey(member))
					MoveListener.npcCache.remove(member);
				
				List<String> areas = data.getStringList("players." + member + ".areas");
				areas.remove(oldOwner);
				if (!areas.contains(newOwner))
					areas.add(newOwner);
				
				data.set("players." + member + ".areas", areas);
				
			}
			
			List<String> areas = data.getStringList("players." + Bukkit.getOfflinePlayer(UUID.fromString(newOwner)).getName() + ".areas");
			areas.remove(oldOwner);
			if (!areas.contains(newOwner))
				areas.add(newOwner);
			
			data.set("players." + Bukkit.getOfflinePlayer(UUID.fromString(newOwner)).getName() + ".areas", areas);
			
			try {data.save(dataFile);} catch (IOException e) {}
			
			for (String member : members)
				if (Bukkit.getOfflinePlayer(member).isOnline())
					onJoinEvent.loginEvent(Bukkit.getPlayer(member));
			
		});
		
	}
	
	public static FarmItemType farmItemTypeCalculator(String toCheck)
	{
		
		if (toCheck.contains("WHEAT"))
			return FarmItemType.WHEAT;
		
		else if (toCheck.contains("POTATO"))
			return FarmItemType.POTATO;
		
		else if (toCheck.contains("CARROT"))
			return FarmItemType.CARROT;
		
		else if (toCheck.contains("SUGAR_CANE"))
			return FarmItemType.SUGAR_CANE;
		
		else if (toCheck.contains("CACTUS"))
			return FarmItemType.CACTUS;
		
		else if (toCheck.contains("PUMPKIN"))
			return FarmItemType.PUMPKIN;
		
		else if (toCheck.contains("MELON"))
			return FarmItemType.MELON;
		
		else if (toCheck.contains("COCOA") || toCheck.contains("INK_SACK"))
			return FarmItemType.COCOA;
		
		else if (toCheck.contains("NETHER_WART") || toCheck.contains("NETHER_STALK"))
			return FarmItemType.NETHER_WART;
		
		else if (toCheck.contains("BEETROOT"))
			return FarmItemType.BEETROOT;
		
		else if (toCheck.contains("BERRIES"))
			return FarmItemType.BERRIES;
		
		else if (toCheck.contains("KELP"))
			return FarmItemType.KELP;
		
		else if (toCheck.contains("BAMBOO"))
			return FarmItemType.BAMBOO;
		
		else 
			return FarmItemType.NULL;
		
	}
	
	public static void setItem(String owner, int amount, ConfigItems toCheck, HashMap<ConfigItems, Integer> values)
	{
			
		values.replace(toCheck, values.get(toCheck)+amount);
		
		FarmerManager.farmerCache.get(owner).setItemValues(values);
			
	}
	
	public static void giveEggToPlayer(Player player, int level)
	{
		
		if (Main.instance.getConfig().isSet("Settings.levelBasedFarmer")
				&& Main.instance.getConfig().getBoolean("Settings.levelBasedFarmer"))
		{
			
			ItemStack egg = new ItemStack(FarmerManager.SPAWN_EGG);
			
			ItemMeta meta = egg.getItemMeta();
			
			meta.setDisplayName(  Main.color(meta.getDisplayName().replace("{level}", String.valueOf(level)))  );
			
			egg.setItemMeta(meta);
			
			syncItemAdderToPlayer(player, egg);
			
		}
		
		else syncItemAdderToPlayer(player, FarmerManager.SPAWN_EGG);
		
	}
	
	private static void syncItemAdderToPlayer(Player player, ItemStack item)
	{
		
		Bukkit.getScheduler().runTask(Main.instance, () ->
		{
			
			player.getInventory().addItem(item);
			
		});
		
	}

}
