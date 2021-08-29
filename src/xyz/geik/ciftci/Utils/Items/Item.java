package xyz.geik.ciftci.Utils.Items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.Manager;

public class Item {
	
	/**
	 * @author Geik
	 * @param name
	 * @param lore
	 * @param itemMaterial
	 * @return
	 */
	public static ItemStack defaultItem(String name, List<String> lore, Material itemMaterial) {
		try {
			List<String> newList = new ArrayList<String>();
	    	for (String string : lore) {
	    		newList.add(string.replace("&", "§"));}
	    	ItemStack item = new ItemStack(itemMaterial);
	    	ItemMeta meta = item.getItemMeta();
	    	meta.setDisplayName(name.replace("&", "§"));
	    	meta.setLore(newList);
	    	item.setItemMeta(meta);
	    	return item;
		} catch(NullPointerException e1) {Bukkit.getConsoleSender().sendMessage(Main.color("&6Çiftçi &cMateryal hatası bulundu. &b" + name));return null;}
	}
	
	public static ItemStack getSkulledItem(String path, List<String> lore, String name) {
		
		ItemStack reCordinateItem = playerSkull(path);
		
		ItemMeta meta = reCordinateItem.getItemMeta();
		
		if (name == null)
			meta.setDisplayName(Manager.getText("lang", path + ".name"));
		else
			meta.setDisplayName(name);
		
		if (lore == null)
			meta.setLore(Manager.getLore("lang", path + ".lore"));
		else meta.setLore(lore);
		
		reCordinateItem.setItemMeta(meta);
		
		return reCordinateItem;
		
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack playerSkull(String path) {
		
		String nms = Manager.getNMSVersion();
		
		Material mat;
		
		if (nms.contains("1_8") || nms.contains("1_9") || nms.contains("1_10")
				|| nms.contains("1_11") || nms.contains("1_12"))
			mat = Material.getMaterial("SKULL_ITEM");
		
		else mat = Material.PLAYER_HEAD;
		
        ItemStack skull = new ItemStack(mat);
        
        if (nms.contains("1_8") || nms.contains("1_9") || nms.contains("1_10")
				|| nms.contains("1_11") || nms.contains("1_12"))
        	skull.setDurability((short) 3);
        
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        assert meta != null;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", Manager.getText("lang", path + ".skull")));
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(meta);
        return skull;
    }

}
