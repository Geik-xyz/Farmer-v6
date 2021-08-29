package xyz.geik.ciftci.Utils.Gui;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.Cache.ConfigItems;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;

public class FarmerInventoryGUI {
	
	public static void createGui(Player player, int page, String uuid)
	{
		
		try
		{
			
			Inventory gui = Bukkit.getServer().createInventory(player, 54, Manager.getText("lang", "Gui.guiName") + " #" + page);
			
			List<ItemStack> rawItems = FarmerManager.CONFIG_TO_GUI;
			
			if (uuid == null) return;
			
			fillItem(gui, player);
			
			if (!Manager.isSet("lang", "Gui.help.show")
					|| (Manager.isSet("lang", "Gui.help.show") && Manager.getBoolean("lang", "Gui.help.show")))
				gui.setItem(Manager.getInt("lang", "Gui.help.slot"), GuiManager.farmerHelp());
			
			if (rawItems == null || rawItems.size() == 0) 
				gui.setItem(22, GuiManager.emptyMenu());
			
			else
			{
				
				int itemCount = rawItems.size();
				
				List<String> loreTemplate = Manager.getLore("lang", "Gui.farmItemTemplate.lore");
				
				StorageAndValues values = FarmerManager.farmerCache.get(uuid);
			  	
			  	final int farmerLevel = values.getStorage().getFarmerLevel();
			  	
			  	final int capacity = Main.instance.getConfig().getInt("FarmerLevels." + farmerLevel + ".Capacity");
			  	
			  	final int paymentMethods = FarmerManager.farmerCache.get(uuid).getStorage().getSellingStatus();
				
				final int paymentMethod = FarmerManagmentGUI.isLeaderChoose(paymentMethods);
				
				gui.setItem(4, GuiManager.managmentIcon(paymentMethod, capacity, farmerLevel));
				
				if (itemCount <= 28 && itemCount > 0)
				{
					
					Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
						
						for (int itemSlot = 0; itemSlot <= itemCount-1 ; itemSlot++)
					  	{
							
							ConfigItems configItem = FarmerManager.STORED_ITEMS.get(  rawItems.get(itemSlot)  );
							
							ItemStack item = new ItemStack(  rawItems.get(  itemSlot  )  );
							
							ItemMeta meta = item.getItemMeta();
								
							double price = configItem.getPrice();
							
							int stok = values.getItemValues().get(configItem);
							
							int percent = 100*stok/capacity;
							
							meta.setLore(GuiManager.getConfigItemLore(loreTemplate, percent, capacity, stok, price, farmerLevel));
							
							item.setItemMeta(meta);
							
							if (itemSlot <= 6)
								gui.setItem(10+itemSlot, item);
					  		
					  		else if (itemSlot <= 13 && itemSlot > 6)
					  			gui.setItem(12+itemSlot, item);
					  		
					  		else if (itemSlot <= 20 && itemSlot > 13)
					  			gui.setItem(14+itemSlot, item);
					  		
					  		else if (itemSlot == 28)
					  			return;
					  		
					  		else
					  			gui.setItem(16+itemSlot, item);
							
					  	}
					
					});
					
				}
				
				else if (itemCount > 0)
				{
				
					Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
						
						int pageCount = 0;
						
						int startingValue = 28*page;
					  	
					  	for (int itemSlot = startingValue-28; itemSlot <= itemCount-1 ; itemSlot++)
					  	{
					  		
					  		if (pageCount > (itemCount-1)-(startingValue-28)) break;
							
							ItemStack item =  new ItemStack(  rawItems.get(  itemSlot  )  );
							
							ConfigItems configItem = FarmerManager.STORED_ITEMS.get(  item  );
							
							ItemMeta meta = item.getItemMeta();
								
							double price = configItem.getPrice();
							
							int stok = values.getItemValues().get(  configItem  );
							
							int percent = 100*stok/capacity;
							
							meta.setLore(GuiManager.getConfigItemLore(loreTemplate, percent, capacity, stok, price, farmerLevel));
							
							item.setItemMeta(meta);
							
							if (pageCount <= 6)
								gui.setItem(10+pageCount, item);
					  		
					  		else if (pageCount <= 13 && pageCount > 6)
					  			gui.setItem(12+pageCount, item);
					  		
					  		else if (pageCount <= 20 && pageCount > 13)
					  			gui.setItem(14+pageCount, item);
					  		
					  		else if (pageCount == 28)
					  			return;
					  		
					  		else
					  			gui.setItem(16+pageCount, item);
							
							pageCount++;
							
					  	}
					
					});
					
					double maxPageCounter = (itemCount/28);
				  	
				  	int maxPage = (int) (maxPageCounter+1);
				  	
				  	if ((maxPageCounter % 1) != 0)
				  		maxPage = (int) maxPageCounter;
				  	
				  	if (page == 1 && maxPage > 1) {
				  		gui.setItem(53, GuiManager.nextPage());
				  		setFillItemToSlot(45, gui);
				  	}
				  	
				  	else if (page == maxPage && maxPage > 1) {
				  		gui.setItem(45, GuiManager.previousPage());
				  		setFillItemToSlot(53, gui);
				  	}
				  	
				  	else if (page < maxPage && maxPage > 1) 
				  	{
				  		
				  		gui.setItem(53, GuiManager.nextPage());
				  		
				  		gui.setItem(45, GuiManager.previousPage());
				  		
				  	}
					
				}
				
				else
				{
					
					gui.setItem(22, GuiManager.emptyMenu());
					
				}
				
			}
			
			Bukkit.getScheduler().runTask(Main.instance, () -> {
				
				player.openInventory(gui);
				
			});
			
		}
		
		catch(NullPointerException e1) { e1.printStackTrace();
		player.sendMessage(Main.color("&cÇiftçi açılırken hata oluştu. Lütfen çıkıp giriniz."));}
		
	}
	
	public static void setFillItemToSlot(int slot, Inventory gui)
	{
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
			
			if (Manager.getBoolean("lang", "Gui.fillItem.fill")
					&& FarmerManager.fillItem != null)
			{
				
				ItemStack fill = FarmerManager.fillItem;
				
				gui.setItem(slot, fill);
				
			}
			
		});
		
	}
	
	@SuppressWarnings({ "deprecation" })
	private static void fillItem(Inventory gui, Player player)
	{
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
			
			final int help = Manager.getInt("lang", "Gui.help.slot");
			
			if (Manager.getBoolean("lang", "Gui.fillItem.fill")
					&& FarmerManager.fillItem != null)
			{
				
				ItemStack fill = FarmerManager.fillItem;
				
				fill.setDurability((short) 7);
				
				for (int i = 0; i < 24; i++)
				{
					
					if (i <= 9)
					{
						
						if (i == 4)
							continue;
						
						else
							gui.setItem(i, fill);
						
					}
					
					else if (i == 10)
						gui.setItem(17, fill);
					
					else if (i == 11)
						gui.setItem(18, fill);
					
					else if (i == 12)
						gui.setItem(26, fill);
					
					else if (i == 13)
						gui.setItem(27, fill);
					
					else if (i == 14)
						gui.setItem(35, fill);
					
					else if (i == 15)
						gui.setItem(36, fill);
					
					else if (i == 16)
						gui.setItem(44, fill);
					
					else if (i > 16)
					{
						
						if (29+i != help)
							gui.setItem(29+i, fill);
						else continue;
						
					}
					
				}
				
				int itemCount = FarmerManager.CONFIG_TO_GUI.size();
				
				if (itemCount <= 28 && itemCount > 0)
				{
					
					setFillItemToSlot(45, gui);
					
					setFillItemToSlot(53, gui);
					
				}
				
			}
			
		});
		
	}

}
