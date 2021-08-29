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
import xyz.geik.ciftci.Utils.API.ApiFun;

public class AddonGui
{
	
	public static void createGui(Player player)
	{
		
		Inventory gui = Bukkit.getServer().createInventory(player, 27, Manager.getText("lang", "addonGui.guiName"));
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
			
			fillGui(gui);
	  		
	  		String uuid = ApiFun.getIslandOwnerUUID(player.getLocation()).toString();
			
			if (Main.autoSell 
					&& Main.instance.getConfig().getBoolean("AddonSettings.autoSell.feature")) {
				gui.setItem(10, 
						GuiManager.autoSellModeItem(FarmerManager.farmerCache.get(uuid).getStorage().getAutoSell()));
				
			}
			else gui.setItem(10, GuiManager.addonDisabled());
			
			if (Main.autoCollector
					&& Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.feature")
					&& Main.instance.getConfig().isSet("AddonSettings.autoCollect.toggle")
					&& Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.toggle"))
			{
						
				gui.setItem(16, 
						GuiManager.autoCollectModeItem(FarmerManager.farmerCache.get(uuid).getStorage().getAutoCollect()));
				
			}
			else gui.setItem(16, GuiManager.addonDisabled());
			
			if (Main.spawnerKiller
					&& Main.instance.getConfig().isSet("AddonSettings.spawnerKiller.toggle")
					&& Main.instance.getConfig().getBoolean("AddonSettings.spawnerKiller.toggle"))
			{
				
				gui.setItem(13, 
						GuiManager.spawnerKillItem(FarmerManager.farmerCache.get(uuid).getStorage().getSpawnerKill()));
				
			}
			else gui.setItem(13, GuiManager.addonDisabled());
			
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
			
			slotList.add(16);
			slotList.add(13);
			slotList.add(10);
			
			for (int i = 0; i < 27; i++)
			{
				
				if (!slotList.contains(i))
					gui.setItem(i, fill);
				
				else continue;
				
			}
			
		}
		
	}

}
