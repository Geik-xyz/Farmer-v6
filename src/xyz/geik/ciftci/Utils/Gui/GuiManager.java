package xyz.geik.ciftci.Utils.Gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.Items.Item;

public class GuiManager
{
	
	public static ItemStack addonDisabled()
	{
		if (Manager.isSet("lang", "addonGui.addonDisabled.skull"))
			return Item.getSkulledItem("addonGui.addonDisabled", null, null);
		else
		{
			if (FarmerManager.fillItem == null)
				FarmerManager.fillItemUpdater();
			
			return FarmerManager.fillItem;
			
		}
	}
	
	public static ItemStack getAddonsItem()
	{
		return Item.getSkulledItem("ManagmentGui.addons", null, null);
	}
	
	public static ItemStack spawnerKillItem(boolean spawnerKill)
	{
		
		List<String> lore = new ArrayList<String>();
		
		if (spawnerKill)
			for (String s : Manager.getLore("lang", "addonGui.spawnerKill.lore"))
				lore.add(s.replace("{status}", Manager.getText("lang", "toggleON")));
		
		else
			for (String s : Manager.getLore("lang", "addonGui.spawnerKill.lore"))
				lore.add(s.replace("{status}", Manager.getText("lang", "toggleOFF")));
		
		String name = Manager.getText("lang", "addonGui.spawnerKill.name");
		
		ItemStack spawnerKillItem = Item.playerSkull("addonGui.spawnerKill");
		
		ItemMeta meta = spawnerKillItem.getItemMeta();
		
		meta.setDisplayName(name);
		
		meta.setLore(lore);
		
		spawnerKillItem.setItemMeta(meta);
		
		return spawnerKillItem;
		
	}
	
	public static ItemStack autoSellModeItem(boolean autoSell)
	{
		
		List<String> lore = new ArrayList<String>();
		
		if (autoSell)
			for (String s : Manager.getLore("lang", "addonGui.autoSell.lore"))
				lore.add(s.replace("{status}", Manager.getText("lang", "toggleON")));
		
		else
			for (String s : Manager.getLore("lang", "addonGui.autoSell.lore"))
				lore.add(s.replace("{status}", Manager.getText("lang", "toggleOFF")));
		
		String name = Manager.getText("lang", "addonGui.autoSell.name");
		
		ItemStack autoSellItem = Item.playerSkull("addonGui.autoSell");
		
		ItemMeta meta = autoSellItem.getItemMeta();
		
		meta.setDisplayName(name);
		
		meta.setLore(lore);
		
		autoSellItem.setItemMeta(meta);
		
		return autoSellItem;
		
	}
	
	public static ItemStack autoCollectModeItem(boolean autoCollect)
	{
		
		List<String> lore = new ArrayList<String>();
		
		if (autoCollect)
			for (String s : Manager.getLore("lang", "addonGui.autoCollect.lore"))
				lore.add(s.replace("{status}", Manager.getText("lang", "toggleON")));
		
		else
			for (String s : Manager.getLore("lang", "addonGui.autoCollect.lore"))
				lore.add(s.replace("{status}", Manager.getText("lang", "toggleOFF")));
		
		String name = Manager.getText("lang", "addonGui.autoCollect.name");

		ItemStack autoCollectItem = Item.playerSkull("addonGui.autoCollect");
		
		ItemMeta meta = autoCollectItem.getItemMeta();
		
		meta.setDisplayName(name);
		
		meta.setLore(lore);
		
		autoCollectItem.setItemMeta(meta);
		
		return autoCollectItem;
		
	}
	
	public static ItemStack sellingModeItem(int paymentMethod)
	{
		
		List<String> lore = new ArrayList<String>();
		
		String sellingModeName;
		
		if (paymentMethod == 1)
		{
			
			sellingModeName = Manager.getText("lang", "ManagmentGui.sellingMode.name").replace("{current_mode}", Manager.getText("lang", "sellingModeLeader"));
			
			for (String s : Manager.getLore("lang", "ManagmentGui.sellingMode.lore"))
				lore.add(Main.color(s.replace("{current_mode}", Manager.getText("lang", "sellingModeLeader"))));
			
		}
		else 
		{
			
			sellingModeName = Manager.getText("lang", "ManagmentGui.sellingMode.name").replace("{current_mode}", Manager.getText("lang", "sellingModeMember"));
			
			for (String s : Manager.getLore("lang", "ManagmentGui.sellingMode.lore")) 
				lore.add(Main.color(s.replace("{current_mode}", Manager.getText("lang", "sellingModeMember"))));
			
		}
		
		ItemStack sellingModeItem = Item.playerSkull("ManagmentGui.sellingMode");
		
		ItemMeta meta = sellingModeItem.getItemMeta();
		
		meta.setDisplayName(sellingModeName);
		
		meta.setLore(lore);
		
		sellingModeItem.setItemMeta(meta);
		
		return sellingModeItem;
		
	}
	
	public static ItemStack reCoordinate()
	{
		return Item.getSkulledItem("ManagmentGui.reCordinate", null, null);
	}
	
	public static ItemStack nextLevelItem(String name, List<String> lore)
	{
		return Item.getSkulledItem("ManagmentGui.upgradeNext", lore, name);
	}
	
	public static ItemStack maxLevelItem(String name, List<String> lore)
	{
		return Item.getSkulledItem("ManagmentGui.inMaxLevel", lore, name);
	}
	
	public static ItemStack closeFarmerFeature(List<String> lore)
	{
		return Item.getSkulledItem("ManagmentGui.closeFarmer", lore, null);
	}
	
	public static ItemStack farmerHelp()
	{
		return Item.defaultItem(Manager.getText("lang", "Gui.help.name"),
				Manager.getLore("lang", "Gui.help.lore"),
				Material.getMaterial(Manager.getText("lang", "Gui.help.material")));
	}
	
	public static ItemStack nextPage()
	{
		return Item.defaultItem(Manager.getText("lang", "Gui.nextPage.name"),
				Manager.getLore("lang", "Gui.nextPage.lore"),
				Material.getMaterial(Manager.getText("lang", "Gui.nextPage.material")));
	}
	
	public static ItemStack previousPage()
	{
		return Item.defaultItem(Manager.getText("lang", "Gui.previousPage.name"),
				Manager.getLore("lang", "Gui.previousPage.lore"),
				Material.getMaterial(Manager.getText("lang", "Gui.previousPage.material")));
	}
	
	public static ItemStack emptyMenu()
	{
		return Item.defaultItem(Main.color("&4Burası Boş Görünüyor"), new ArrayList<String>(), Material.BARRIER);
	}
	
	public static ItemStack managmentIcon(int paymentMethod, int capacity, int farmerLevel)
	{
		
		List<String>  lore = new ArrayList<String>();
		
		if (paymentMethod == 1)
		{
			
			for (String s : Manager.getLore("lang", "Gui.managment.lore"))
				lore.add(Main.color(s.replace("{selling_status}", Manager.getText("lang", "sellingModeLeader"))
						.replace("{capacity}", String.valueOf(capacity))
						.replace("{level}", String.valueOf(farmerLevel))
						));
			
		}
		else 
		{
			
			for (String s : Manager.getLore("lang", "Gui.managment.lore")) 
				lore.add(Main.color(s.replace("{selling_status}", Manager.getText("lang", "sellingModeMember"))
						.replace("{capacity}", String.valueOf(capacity))
						.replace("{level}", String.valueOf(farmerLevel))
						));
			
		}
		
		return Item.getSkulledItem("Gui.managment", lore, null);
	}
	
	public static List<String> getConfigItemLore(List<String> lore, int percent, int capacity, int stok, double price, int farmerLevel)
	{
		String fillColor = GuiManager.fillColor(percent);
		
		String percentText = GuiManager.getPercent(fillColor, percent);
		
		String maxStockText = GuiManager.getMaxStock(fillColor, capacity);
		
		String bar = GuiManager.getProgressBar(fillColor, percent, stok, capacity);
		
		String stockText = GuiManager.getStock(fillColor, stok);
		
		String taxAmount = String.valueOf(Main.instance.getConfig().getInt("tax.taxRate"));
		
		if (Main.instance.getConfig().isSet("FarmerLevels." + farmerLevel + ".taxRate"))
			taxAmount = String.valueOf(Main.instance.getConfig().getInt("FarmerLevels." + farmerLevel + ".taxRate"));
		
		List<String> newList = new ArrayList<String>();
		
    	for (String string : lore)
    		newList.add(string.replace("&", "§")
    				.replace("{stock}", Main.color(  stockText  ) )
    				.replace("{maxstock}", Main.color(  maxStockText  ))
    				.replace("{level}", String.valueOf(farmerLevel))
    				.replace("{taxRate}", taxAmount)
    				.replace("{progress_percent}", Main.color( percentText ))
    				.replace("{price}", String.valueOf(price))
    				.replace("{stack_price}", String.valueOf(price*64))
    				.replace("{progress_bar}", bar));
    	
    	return newList;
    	
	}
	
	private static String getProgressBar(String color, int percent, int stock, int capacity)
	{
		
		String barFull = Manager.getText("lang", "progressSymbol");
		
		String barSymbol = String.valueOf(barFull.charAt(0));
		
		String barText = Main.color("&7");
		
		int bar = percent/(100/barFull.length());
		
		int toEmpty = barFull.length()-bar;
		
		if (stock == capacity)
		{
			
			barText = Main.color("&c");
			
			for (int i = 0; i <= Manager.getText("lang", "progressSymbol").length(); i++) barText = barText + barSymbol;	
			
		}
		
		else
		{
			
			if (bar > 0)
			{
				
				barText = Main.color(color);
				
				for (int i = 1; i <= bar ; i++)
					barText = barText + Main.color(barSymbol);
				
				
				for (int i = 1; i <= toEmpty ; i++)
					barText = barText + Main.color("&7"+barSymbol);
				
			}
			
			else
			{
				
				for (int i = 1; i <= toEmpty ; i++) 
					barText = barText + Main.color(barSymbol);
				
			}
			
		}
		
		return barText;
		
	}
	
	private static String getPercent(String color, int percent)
	{
		
		if (color == null) return String.valueOf(percent);
		
		else return color + String.valueOf(percent);
		
	}
	
	private static String getStock(String color, int stock)
	{
		
		if (color == null) return String.valueOf(stock);
		
		else return color + String.valueOf(stock);
		
	}
	
	private static String getMaxStock(String color, int maxStock)
	{
		
		if (color == null) return String.valueOf(maxStock);
		
		else return color + String.valueOf(maxStock);
		
	}
	
	private static String fillColor(int percent)
	{
		
		String fillColor = "&a";
			
		if (percent >= 0 && percent <= 19) fillColor = "&a";
		
		else if (percent >= 20 && percent <= 39) fillColor = "&2";
		
		else if (percent >= 40 && percent <= 59) fillColor = "&e";
		
		else if (percent >= 60 && percent <= 79) fillColor = "&6";
		
		else fillColor = "&c";
		
		return fillColor;
		
	}
	

}
