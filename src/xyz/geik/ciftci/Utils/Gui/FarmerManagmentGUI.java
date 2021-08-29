package xyz.geik.ciftci.Utils.Gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;


public class FarmerManagmentGUI {
	
	public static void createGui(Player player, boolean isCommand)
	{
		
		Inventory gui = Bukkit.getServer().createInventory(player, 27, Manager.getText("lang", "ManagmentGui.guiName"));
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
			
			fillGui(gui);
			
			List<String> levelValue = new ArrayList<String>(Main.instance.getConfig().getConfigurationSection("FarmerLevels").getKeys(false));
	  		
	  		String uuid = ApiFun.getIslandOwnerUUID(player.getLocation()).toString();
	  		
			int level = FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel();
			
			int nextLevel = level+1;
			
			int maxLevl = Integer.valueOf(  levelValue.get  (  levelValue.size()-1  )  );
			
			int currentCapacity = Main.instance.getConfig().getInt("FarmerLevels." + level + ".Capacity");
			
			int nextCapacity = Main.instance.getConfig().getInt("FarmerLevels." + nextLevel + ".Capacity");
			
			int nextReqMoney = Main.instance.getConfig().getInt("FarmerLevels." + level + ".nextRankMoney");
			
			double playerMoney = onEnableShortcut.econ.getBalance(player);
			
			int paymentMethods = FarmerManager.farmerCache.get(uuid).getStorage().getSellingStatus();
			
			int paymentMethod = isLeaderChoose(paymentMethods);
			
			String nextLevelName = getName("upgradeNext", level, nextLevel);
			List<String> nextLevelLore = upgradeLore("upgradeNext", Manager.roundDouble(playerMoney, 2), nextLevel, level, maxLevl, currentCapacity, nextCapacity, nextReqMoney);
			
			String maxLevelName = getName("inMaxLevel", level, nextLevel);
			List<String> maxLevelLore = upgradeLore("inMaxLevel", Manager.roundDouble(playerMoney, 2), nextLevel, level, maxLevl, currentCapacity, nextCapacity, nextReqMoney);
			
			if (level == maxLevl)
				gui.setItem(Manager.getInt("lang", "ManagmentGui.inMaxLevel.slot"), GuiManager.maxLevelItem(maxLevelName, maxLevelLore));
			
			else
				gui.setItem(Manager.getInt("lang", "ManagmentGui.upgradeNext.slot"), GuiManager.nextLevelItem(nextLevelName, nextLevelLore));
			
			gui.setItem(Manager.getInt("lang", "ManagmentGui.reCordinate.slot"), GuiManager.reCoordinate());
			
			gui.setItem(Manager.getInt("lang", "ManagmentGui.sellingMode.slot"), GuiManager.sellingModeItem(paymentMethod));
			
			gui.setItem(Manager.getInt("lang", "ManagmentGui.closeFarmer.slot"), GuiManager.closeFarmerFeature(  toggleLore(player, uuid)  ));
			
			
		});
		
		Bukkit.getScheduler().runTask(Main.instance, () ->
		{
			
			player.openInventory(gui);
			
		});
		
	}
	
	private static void fillGui(Inventory gui)
	{
		
		if (Manager.getBoolean("lang", "Gui.fillItem.fill")
				&& FarmerManager.fillItem != null)
		{
			
			ItemStack fill = FarmerManager.fillItem;
			
			List<Integer> slotList = new ArrayList<Integer>();
			
			for (String data : Manager.getConfigurationSection("lang", "ManagmentGui")) {
				
				if (data.equalsIgnoreCase("guiName"))
					continue;
				
				int slot = Manager.getInt("lang", "ManagmentGui." + data + ".slot");
				
				if (!slotList.contains(slot))
					slotList.add(slot);
			
			}
			
			for (int i = 0; i < 27; i++)
			{
				
				if (!slotList.contains(i) || 
						(!Main.autoCollector && !Main.autoSell && !Main.spawnerKiller && Manager.getInt("lang", "ManagmentGui.addons.slot") == i))
					gui.setItem(i, fill);
				
				else continue;
				
			}
			
			if (Main.autoCollector || Main.autoSell || Main.spawnerKiller)
				gui.setItem(Manager.getInt("lang", "ManagmentGui.addons.slot"), GuiManager.getAddonsItem());
			
		}
		
	}
	
	private static String getName(String path, int level, int nextLevel)
	{
		return Manager.getText("lang", "ManagmentGui." + path + ".name")
				.replace("{level}", String.valueOf(level))
				.replace("{next_level}", String.valueOf(nextLevel));
	}
	
	private static List<String> upgradeLore(String path, String playerMoney, int nextLevel, int level, int maxLevl, int currentCapacity, int nextCapacity, int nextReqMoney)
	{
		
		List<String> lore = new ArrayList<String>();
		int next_tax = Main.instance.getConfig().getInt("tax.taxRate");
		
		if (Main.instance.getConfig().isSet("FarmerLevels." + nextLevel + ".taxRate"))
			next_tax = Main.instance.getConfig().getInt("FarmerLevels." + nextLevel + ".taxRate");
			
		for (String s : Manager.getLore("lang", "ManagmentGui." + path + ".lore"))
			lore.add(Main.color(s.replace("{money}", playerMoney)
					.replace("{next_level}", String.valueOf(nextLevel))
					.replace("{current_level}", String.valueOf(level))
					.replace("{max_level}", String.valueOf(maxLevl))
					.replace("{current_capacity}", String.valueOf(currentCapacity)) 
					.replace("{next_capacity}", String.valueOf(nextCapacity))
					.replace("{req_money}", String.valueOf(nextReqMoney))
					.replace("{next_tax}", String.valueOf(next_tax))	));
		
		return lore;
		
	}
	
	private static List<String> toggleLore(Player player, String uuid)
	{
		
		List<String> lore = new ArrayList<String>();
		
		if (FarmerManager.farmerExclude.contains(uuid))
			for (String s : Manager.getLore("lang", "ManagmentGui.closeFarmer.lore"))
				lore.add(Main.color(s.replace("{status}", Manager.getText("lang", "toggleOFF"))));
		
		else 
			for (String s : Manager.getLore("lang", "ManagmentGui.closeFarmer.lore"))
				lore.add(Main.color(s.replace("{status}", Manager.getText("lang", "toggleON"))));
		
		return lore;
		
	}
	
	public static int isLeaderChoose(int paymentMethod)
	{
		
		if (Main.instance.getConfig().getBoolean("Settings.depositMethod.leaderChoose"))
			return paymentMethod;
		
		else
		{
			
			if (Main.instance.getConfig().getBoolean("Settings.depositMethod.defaultDepositLeader")) 
				return 1;
			
			else
				return 0;
			
		}
		
	}
}
