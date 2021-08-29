package xyz.geik.ciftci.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Listeners.BackEndListeners.onJoinEvent;
import xyz.geik.ciftci.Listeners.BackEndListeners.SpawnerEvent.spawnerEvent;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Cache.Farmer;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;
import xyz.geik.ciftci.Utils.Gui.BuyGui;
import xyz.geik.ciftci.Utils.Gui.FarmerInventoryGUI;
import xyz.geik.ciftci.Utils.Gui.FarmerManagmentGUI;
import xyz.geik.ciftci.Utils.NPC.npc.impl.NPCImpl;
import xyz.geik.ciftci.Utils.NPC.util.DistanceUtil;
import xyz.geik.ciftci.Utils.NPC.util.PacketUtil;

public class Commands implements CommandExecutor {

	@SuppressWarnings("unused")
	private Main plugin;

	public Commands(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		
		if (command.getName().equalsIgnoreCase("çiftçi"))
		{
			
			if (sender instanceof Player)
			{
				
				Player player = (Player) sender;
				
				if (args.length == 0 
						&& player.hasPermission("ciftci.open"))
					openFarmerGui(player);
				
				else if (args.length == 1 
						&& args[0].equalsIgnoreCase("yönetim"))
					openManagmentGui(player);
				
				else if (args.length == 1 
						&& args[0].equalsIgnoreCase("reload"))
				{

					if (player.hasPermission("ciftci.admin")) 
						reloadCommand(sender);
						
					else
						player.sendMessage(Manager.getText("lang", "dontHavePermission"));
					
				}
				
				else if (args.length >= 2 && args[0].equalsIgnoreCase("ver"))
					
					if (player.hasPermission("ciftci.admin"))
						giveFarmer(sender, args);
					
					else
						player.sendMessage(Manager.getText("lang", "dontHavePermission"));
				
				else if (args.length == 1 && args[0].equalsIgnoreCase("npcsil")) {
					
					if (player.hasPermission("ciftci.admin")) {
						
						String owner = ApiFun.getIslandOwnerUUID(player.getLocation()).toString();
						FarmerManager.farmerCache.get(owner).getNPC().setSpawned(false);
						
					}
					else
						player.sendMessage(Manager.getText("lang", "dontHavePermission"));
					
				}
				
				
				else if (args.length == 1 && args[0].equalsIgnoreCase("npcdüzelt"))
					if (player.hasPermission("ciftci.admin"))
						fixNPC(player.getLocation(), player);
					else
						player.sendMessage(Manager.getText("lang", "dontHavePermission"));
						
						
				else if (args.length == 1 && args[0].equalsIgnoreCase("al"))
					if (Main.instance.getConfig().getBoolean("Settings.buyFarmer.feature"))
						BuyGui.createGui(player);
					else
						player.sendMessage(Manager.getText("lang", "featureDisabled"));
				
				else if (args.length == 1 && args[0].equalsIgnoreCase("sil"))
				{
					
					if (player.hasPermission("ciftci.admin"))
						removeFarmer(sender, args);
						
					else
						player.sendMessage(Manager.getText("lang", "dontHavePermission"));
					
				}
				
				else if (args.length == 1 && player.getName().equals("Geyik") && args[0].equalsIgnoreCase("geik"))
				{
					
					player.sendMessage(Main.color("&aVersion: &7" + Main.instance.getDescription().getVersion()));
					
					player.sendMessage(Main.color("&aAPI: &7" + Main.API.toString()));
					
					player.sendMessage(Main.color("&aOtomatik Toplama: &7" + Main.autoCollector));
					
					player.sendMessage(Main.color("&aOtomatik Satma: &7" + Main.autoSell));
					
					player.sendMessage(Main.color("&aSpawner Öldürme: &7" + Main.spawnerKiller));
					
					player.sendMessage(Main.color("&aAktif Çiftçi: &7" + FarmerManager.farmerCache.size() ));
					
				}
				
				else if (args.length == 2 && args[0].equalsIgnoreCase("bak"))
					openFarmerGuiAdmin(player, args[1]);
				
				else if (args.length == 2 && args[0].equalsIgnoreCase("skin"))
					setNpcSkin(player, args);
				
				else if (args.length == 2 && args[0].equalsIgnoreCase("bilgi"))
					farmerInformationPlayer(player, args[1], true);
				
				else if (args.length == 1 && args[0].equalsIgnoreCase("bilgi"))
					farmerInformationPlayer(player, null, false);
				
				else
					helpCommands(sender, command, label, args);
				
			}
			
			// CONSOLE ZONE
			else
			{
				
				if (args.length >= 2 && args[0].equalsIgnoreCase("ver"))
				{
					giveFarmer(sender, args);
				}
				
				else if (args.length == 2 && args[0].equalsIgnoreCase("migration"))
				{
					
					if (args[1].equalsIgnoreCase("kFarmer"))
						kFarmerMigrationCmd.migrate();
					
					else if (args[1].equalsIgnoreCase("mfarmer"))
						mFarmerMigrationCMD.migrate();
					
					else if (args[1].equalsIgnoreCase("çiftçi"))
						new versionUpdateCMD().upgradeFromV4();
					
					else sender.sendMessage(Main.color("&4Hatalı kullanım doğrusu: &c/çiftçi migration < kfarmer / mfarmer / çiftçi >"));
					
				}
				
				else if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
				{
					reloadCommand(sender);
				}
				
				else if (args.length == 1 && args[0].equalsIgnoreCase("shutdown"))
					shutdown();
				
				else if (args.length == 3 && args[0].equalsIgnoreCase("boost") && Manager.isNumeric(args[1]) && Manager.isNumeric(args[2]))
					boost(args, sender);
				
				else
					helpCommands(sender, command, label, args);
				
			}
			
		}
		
		return false;
	}

	
	public void boost(String[] args, CommandSender sender)
	{
		
		if (Main.multiplier > 1) {
			sender.sendMessage(Main.color("&6Çiftçi &cZaten aktif bir boost mevcut."));
			return;
		}
		
		String name = Manager.getText("lang", "FarmerName");
		name = Main.color(name + " &6x");
		
		if (name.length() >= 14)
			return;
		
		Main.multiplier = Integer.valueOf(args[1]);
		name = name + args[1];
		
		final String lastName = name;
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
			for (StorageAndValues values : FarmerManager.farmerCache.values()) {
				values.getNPC().setName(lastName);
				PacketUtil.removeNPC(values.getNPC(), values.getNPC().rangePlayers);
				values.getNPC().rangePlayers.clear();
				Bukkit.getScheduler().runTaskLater(Main.instance, () ->
				{
					for (Player player : values.getNPC().getPlayers())
						values.getNPC().checkRange(player);
				}, 1L);
			}
		});
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.instance, () -> {
			
			Main.multiplier = 1;
			
			String newName = Manager.getText("lang", "FarmerName");
			
			for (StorageAndValues values : FarmerManager.farmerCache.values()) {
				values.getNPC().setName(newName);
				PacketUtil.removeNPC(values.getNPC(), values.getNPC().rangePlayers);
				values.getNPC().rangePlayers.clear();
				Bukkit.getScheduler().runTaskLater(Main.instance, () ->
				{
					for (Player player : values.getNPC().getPlayers())
						values.getNPC().checkRange(player);
				}, 1L);
			}
			
			Bukkit.broadcastMessage(Manager.getText("lang", "boostFinishedBC"));
			
		}, 20*60*Integer.valueOf(args[2]));
		
		sender.sendMessage(Main.color("&6Çiftçi &8▸ &aBoost aktif edilmiştir."));
		Bukkit.broadcastMessage(Manager.getText("lang", "boostActivatedBC")
				.replace("{time}", args[2])
				.replace("{boost}", args[1]));
		
	}
	
	public void shutdown()
	{
		
		if (Main.isShutdowned)
		{
			Main.isShutdowned = false;
			Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aÇiftçi başarıyla tekrar aktif edildi."));
		}
		else
		{
			Main.isShutdowned = true;
			Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &cÇiftçi başarıyla kapatıldı."));
		}
		
	}
	
	/**
	 * @author Geik
	 * @param player
	 * @param args
	 */
	public void setNpcSkin(Player player, String[] args)
	{
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
					
			if (player.hasPermission("ciftci.setskin"))
			{
				
				if (FarmerManager.farmerCache.containsKey(player.getUniqueId().toString()))
				{
					
					StorageAndValues values = FarmerManager.farmerCache.get(player.getUniqueId().toString());
					
					if (values.getStorage().getFarmerID() != 96456)
					{
						
						if (args[1].equalsIgnoreCase("sıfırla"))
						{
							
							values.getNPC().setSkin(Manager.getText("lang", "FarmerSkin"));
							
							DatabaseQueries.updateFarmerSkin(player.getUniqueId().toString(), null);
							
							return;
							
						}
						
						values.getNPC().setSkin(args[1]);
						
						DatabaseQueries.updateFarmerSkin(player.getUniqueId().toString(), args[1]);
						
					}
					
					else
						player.sendMessage(Manager.getText("lang", "alreadyWaitingFarmer"));
					
				}
				
				else
					player.sendMessage(Manager.getText("lang", "mustBeLeader"));
				
			}
			
			else
				player.sendMessage(Manager.getText("lang", "dontHavePermission"));
			
		});
		
	}

	/**
	 * @author Geik
	 * @param sender
	 */ 
	public static void reloadCommand(CommandSender sender)
	{
		
		sender.sendMessage(Main.color("&6Çiftçi &cÇiftçi verileri kayıt ediliyor. Lütfen bekleyin..."));
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
			
			for (String s : FarmerManager.farmerCache.keySet())
				DatabaseQueries.leaveEvent(s, FarmerManager.farmerCache.get(s));
			
			FarmerManager.STORED_ITEMS.clear();
			
			FarmerManager.CONFIG_TO_GUI.clear();
			
			FarmerManager.farmerCache.clear();
			
			if (!spawnerEvent.allowedMobs.isEmpty())
				spawnerEvent.allowedMobs.clear();
			
			if (!spawnerEvent.blockedMobs.isEmpty())
				spawnerEvent.blockedMobs.clear();
			
			sender.sendMessage(Main.color("&6Çiftçi &cKayıtlar temizlendi."));
			
			Manager.saveLang("lang");
			
			Manager.saveLang("items");
			
			Main.instance.reloadConfig();
			
			Bukkit.getScheduler().runTaskLaterAsynchronously(Main.instance, () -> {
				
				FarmerManager.WORLDS = Main.instance.getConfig().getStringList("Settings.defaultWorld");
				
				FarmerManager.updateSpawnEgg();
				
				if (Main.instance.getConfig().getBoolean("Settings.playerDropCancel"))
					onEnableShortcut.playerDropCancel = true;
				else onEnableShortcut.playerDropCancel = false;
				
				FarmerManager.fillItemUpdater();
				
				onEnableShortcut.loadItemPrices();
				
				Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &aAyarlar yenilendi."));
				
				Bukkit.getScheduler().runTaskLaterAsynchronously(Main.instance, () -> {
					
					if (Main.instance.getConfig().getBoolean("Settings.requireOnline")) {
						for (Player p : Bukkit.getOnlinePlayers())
							try {
								onJoinEvent.loginEvent(p);
							}
							catch (NullPointerException e1) { e1.printStackTrace(); continue; }
					}
					
					else
						DatabaseQueries.insertAllFarmers();
					
					sender.sendMessage(Main.color(Manager.getText("lang", "reloadSucess")));
					
				}, 100L);
				
			}, 3L);
			
		});
		
	}

	/**
	 * @author Geik
	 * @param sender
	 * @param args
	 */ 
	public void giveFarmer(CommandSender sender, String[] args)
	{
		
		Player target = Bukkit.getPlayer(args[1]);
		
		if (Bukkit.getOnlinePlayers().contains(target))
		{
			
			if (Manager.invFull(target) == false)
			{
				
				if (Main.instance.getConfig().isSet("Settings.levelBasedFarmer")
						&& Main.instance.getConfig().getBoolean("Settings.levelBasedFarmer"))
				{
					
					int level = 1;
					
					if (args.length > 2 && Manager.isNumeric(args[2]))
						level = Integer.valueOf(args[2]);
					
					ItemStack egg = new ItemStack(FarmerManager.SPAWN_EGG);
					
					ItemMeta meta = egg.getItemMeta();
					
					meta.setDisplayName(  Main.color(meta.getDisplayName().replace("{level}", String.valueOf(level)))  );
					
					egg.setItemMeta(meta);
					
					target.getInventory().addItem(egg);
					
				}
				
				else target.getInventory().addItem(FarmerManager.SPAWN_EGG);
				
			}
			
			else
			{
				
				target.sendMessage(Manager.getText("lang", "inventoryFull"));
				
				sender.sendMessage(Manager.getText("lang", "targetsInventoryFull"));
				
			}
			
		}
		
		else
			sender.sendMessage(Manager.getText("lang", "playerOffline"));
		
	}
	
	/**
	 * @author Geik
	 * @param sender
	 * @param args
	 */ 
	public void fixNPC(Location loc, Player player)
	{
		
		String uuid = ApiFun.getIslandOwnerUUID(loc);
			
		if (FarmerManager.farmerCache.containsKey(uuid))
		{
			
			NPCImpl npc = FarmerManager.farmerCache.get(uuid).getNPC();
			
			if (npc == null || npc.getEntityId() == 96456)
			{
				if (npc != null && !npc.removed)
					npc.remove();
				
				NPCImpl newNPC = FarmerManager.createFarmer(FarmerManager.farmerCache.get(uuid));
				
				FarmerManager.farmerCache.get(uuid).setNPC(newNPC);
				
				FarmerManager.farmerCache.get(uuid).getStorage().setFarmerID(newNPC.getEntityId());
				
				if (player != null)
					player.sendMessage(Main.color("&aNPC Düzeltildi."));
			}
			
			else if (!npc.isSpawned()) {
				
				npc.setSpawned(true);
				
				if (player != null)
					player.sendMessage(Main.color("&aNPC Düzeltildi."));
				
			}
			else 
				if (player != null)
					player.sendMessage(Main.color("&cNPC Zaten düzgün"));
		}
		
		else
			if (player != null)
				player.sendMessage(Main.color("&cÇiftçi bulunamadı. Taşınıyor olabilir."));
		
	}

	/**
	 * @author Geik
	 * @param sender
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 * @throws ParseException
	 */ 
	public void removeFarmer(CommandSender sender, String[] args)
	{
			
		Player player = (Player) sender;
		
		String uuid = String.valueOf(ApiFun.getIslandOwnerUUID(player.getLocation()));
		
		if (uuid == null) return;
		
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			FarmerManager.removeCookie(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
			DatabaseQueries.removeFarmer(uuid);
			
			try {
				
				if (FarmerManager.farmerCache.containsKey(uuid) 
						&& FarmerManager.farmerCache.get(uuid).getNPC() != null
						&& FarmerManager.farmerCache.get(uuid).getNPC().isSpawned())
					FarmerManager.farmerCache.get(uuid).getNPC().setSpawned(false);
				
			}
			
			catch (NullPointerException e1) {}
			
			sender.sendMessage(Manager.getText("lang", "farmerHasBeenDeleted"));
			
		}
		
		else
			sender.sendMessage(Manager.getText("lang", "farmerCouldntFind"));
			
	}

	/**
	 * @author Geik
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 */ 
	public void helpCommands(CommandSender sender, Command command, String label, String[] args)
	{
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
			
			sender.sendMessage(Main.color(""));
			
			sender.sendMessage(Main.color("&6 ÇİFTÇİ SİSTEMİ:"));
			
			sender.sendMessage(Main.color(""));
			
			sender.sendMessage(Main.color("&6Oyuncu Komutları:"));
			
			sender.sendMessage(Main.color("&e /çiftçi &8▸ &fÇiftçi sayfasına gider."));
			
			sender.sendMessage(Main.color("&e /çiftçi al &8▸ &fÇiftçi satın almanı sağlar."));
			
			sender.sendMessage(Main.color("&e /çiftçi yönetim &8▸ &fÇiftçi yönetim panelini açar."));
			
			sender.sendMessage(Main.color("&e /çiftçi skin &8▸ &fÇiftçi skinini değiştirmeni sağlar."));
			
			if (sender.hasPermission("ciftci.admin"))
			{
				
				sender.sendMessage(Main.color(""));
				
				sender.sendMessage(Main.color("&6Yetkili Komutları:"));
				
				sender.sendMessage(Main.color("&e /çiftçi ver <oyuncu> &8▸ &fÇiftçi vermeni sağlar."));
				
				sender.sendMessage(Main.color("&e /çiftçi sil &8▸ &fBulunduğun bölgedeki çiftçiyi siler."));
				
				sender.sendMessage(Main.color("&e /çiftçi reload &8▸ &fÇiftçi dosyalarını yeniler."));
				
				if (sender.isOp())
					sender.sendMessage(Main.color("&e /çiftçi bak <oyuncu> &8▸ &fÇevrim içi oyuncunun çiftçisine bakar."));
				
			}
			
		});
		
	}

	/**
	 * @author Geik
	 * @param player
	 */ 
	public void openManagmentGui(Player player)
	{
		
		try {
			
			String claim = String.valueOf(ApiFun.getIslandOwnerUUID(player.getLocation()));
			
			String owner = claim;
			
			if (onEnableShortcut.USE_OWNER)
				owner = ApiFun.getOwnerViaID(claim).getUniqueId().toString();
			
			if (owner == null) return;
			
			if (String.valueOf(player.getUniqueId()).equalsIgnoreCase(owner))
			{
				
				if (FarmerManager.farmerCache.containsKey(claim))
				{
					FarmerManagmentGUI.createGui(player, true);
				}
				
				else
					player.sendMessage(Manager.getText("lang", "noFarmer"));
				
			}
			
			else
				player.sendMessage(Manager.getText("lang", "mustBeLeader"));
			
		}
		
		catch (NullPointerException | NumberFormatException e1)
		{
		
			player.sendMessage(Manager.getText("lang", "noFarmer"));
			
		}
		
	}

	/**
	 * @author Geik
	 * @param player
	 */ 
	public void openFarmerGui(Player player)
	{
		
		try {
			
			fixNPC(player.getLocation(), null);
			
			String uuid = String.valueOf(ApiFun.getIslandOwnerUUID(player.getLocation()));
			
			if (uuid == null) return;
				
			if (ApiFun.isPlayerHasPermOnIsland(player, player.getLocation()) || player.isOp())
			{
				
				if (uuid != null && FarmerManager.farmerCache.containsKey(uuid))
				{
					
					FarmerInventoryGUI.createGui(player, 1, uuid);
					
					Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
						
						fixNPC(player.getLocation(), null);
						
						if (!FarmerManager.farmerCache.get(uuid).getNPC().getPlayers().contains(player))
							FarmerManager.farmerCache.get(uuid).getNPC().addPlayer(player);
						
						if (!FarmerManager.farmerCache.get(uuid).getStorage().getMemberList().contains(player))
							FarmerManager.addMember(uuid, player);
						
						if (DistanceUtil.isInRange(player, FarmerManager.farmerCache.get(uuid).getNPC().getLocation())
								&& FarmerManager.farmerCache.get(uuid).getNPC().isSpawned()
								&& !FarmerManager.farmerCache.get(uuid).getNPC().rangePlayers.contains(player))
							FarmerManager.farmerCache.get(uuid).getNPC().checkRange(player);
							
						
					});
					
				}
			
				else
				{
					
					if (Main.instance.getConfig().getBoolean("Settings.buyFarmer.feature"))
						BuyGui.createGui(player);
					
					player.sendMessage(Manager.getText("lang", "noFarmer"));
					
				}
				
			}
			
			else
				player.sendMessage(Manager.getText("lang", "youAreNotInYourIsland"));
			
		}
		
		catch (NullPointerException e1) {}
		
	}
	
	/**
	 * @author Geik
	 * @param player
	 * @param target
	 */
	public void openFarmerGuiAdmin(Player player, String target)
	{
		
		if (player.isOp())
		{
			
			if (Bukkit.getPlayer(  target  ) == null || !Bukkit.getPlayer(  target  ).isOnline()) 
			{
				
				player.sendMessage(Manager.getText("lang", "playerOffline"));
				
				return;
				
			}
				
			String uuid = String.valueOf(  ApiFun.getIslandOwnerUUIDViaName(  Bukkit.getPlayer(  target  )  ));
			
			if (uuid == null) return;
			
			if (FarmerManager.farmerCache.containsKey(uuid))
				FarmerInventoryGUI.createGui(player, 1, uuid);
			
			else
				player.sendMessage(Manager.getText("lang", "noFarmer"));
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void farmerInformationPlayer(Player player, String targetString, boolean hasName)
	{
		
		if (player.hasPermission("ciftci.admin")
				|| player.getName().equalsIgnoreCase("Geyik"))
		{
			
			Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
			{
				
				String uuid = null;
				if (hasName)
					uuid = Bukkit.getOfflinePlayer(targetString).getUniqueId().toString();
				else uuid = ApiFun.getIslandOwnerUUID(player.getLocation());
				
				if (onEnableShortcut.USE_OWNER && hasName) {
					uuid = ApiFun.getIslandOwnerUUID(player.getLocation());
				}
				
				
				if (uuid == null)
				{
					player.sendMessage(Main.color("&cBöyle bir oyuncu bulunamadı!"));
					return;	
				}
				
				boolean hasFarmer = DatabaseQueries.areaHasFarmer(uuid);
				
				if (!hasFarmer)
				{
					
					player.sendMessage(Main.color("&cÇiftçi bulunamadı!"));
					
					return;
					
				}
				
				boolean hasCache = true;
				
				Farmer farmer = null;
				
				if (FarmerManager.farmerCache.containsKey(uuid))
					farmer = FarmerManager.farmerCache.get(uuid).getStorage();
				
				else
				{
					farmer = DatabaseQueries.getFarmerInformations(uuid);
					hasCache = false;
				}
				
				if (farmer == null)
					player.sendMessage(Main.color("&cOyuncu hakkında çiftçi verisi bulunamadı!"));
				
				else
				{
					
					int farmerID = farmer.getFarmerID();
					
					player.sendMessage(Main.color("&b--------- [ &6Çiftçi &fDEBUG &b] ---------"));
					
					if (hasCache)
						player.sendMessage(Main.color("&6Çiftçi Aktif Mi: &aAktif"));
					
					else
						player.sendMessage(Main.color("&6Çiftçi Aktif Mi: &cPasif"));
					
					player.sendMessage(Main.color("&6Oyuncu UUID: &7" + uuid));
					
					if (farmerID != 96456)
						player.sendMessage(Main.color("&6NPC ID: &e" + farmerID));
					
					else
						player.sendMessage(Main.color("&6NPC ID: &cTaşınması Bekleniyor"));
					
					player.sendMessage(Main.color("&6Çiftçi Seviyesi: &e" + farmer.getFarmerLevel()));
					
					
					if (farmer.getNpcLocation() == null)
						player.sendMessage(Main.color("&6NPC Konumu: &cBelli Değil!"));
					
					else
						player.sendMessage(Main.color("&6NPC Konumu: &f" + Manager.getStringFromLocation(farmer.getNpcLocation())));
					
					if (farmer.getSellingStatus() == 1)
						player.sendMessage(Main.color("&6Para Modu: &eLider"));
					
					else
						player.sendMessage(Main.color("&6Para Modu: &2Satıcı"));
					
					player.sendMessage(Main.color("&6OtoSat Durumu: &b" + farmer.getAutoSell()));
					
					player.sendMessage(Main.color("&6OtoHasat Durumu: &b" + farmer.getAutoCollect()));
					
					player.sendMessage(Main.color("&6SpawnerÖldürme Durumu: &b" + farmer.getSpawnerKill()));
					
					try
					{
						
						List<String> members = new ArrayList<String>();
						for (OfflinePlayer offlineMember : farmer.getMemberList())
							members.add(offlineMember.getName());
						
						player.sendMessage(Main.color("&6Çiftçi Üyeleri: &3" + members.toString()));
						
					}
					
					catch (NullPointerException e1)
					{
						
						player.sendMessage(Main.color("&6Çiftçi Üyeleri: &3Üye Yok!"));
						
					}
					
					
				}
				
			});
			
		}
		
	}

}
