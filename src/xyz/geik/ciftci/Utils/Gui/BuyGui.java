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
import xyz.geik.ciftci.Utils.Items.Item;

public class BuyGui {
	
	public static void createGui(Player player)
	{
		
		Inventory gui = Bukkit.getServer().createInventory(player, 27, Manager.getText("lang", "buyFarmerGui.guiName"));
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
			
			fillGui(gui);
			
			List<String> lore = new ArrayList<String>();
			
			for (String loreKey : Manager.getLore("lang", "buyFarmerGui.buyFarmer.lore"))
				lore.add(loreKey.replace("{price}", String.valueOf(Main.instance.getConfig().getInt("Settings.buyFarmer.price"))));
			
			gui.setItem(13, Item.getSkulledItem("buyFarmerGui.buyFarmer", lore, null));
			
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
			
			for (int i = 0; i < 27; i++)
			{
				
				if (i != 13)
					gui.setItem(i, fill);
				
				else continue;
				
			}
			
		}
		
	}

}
