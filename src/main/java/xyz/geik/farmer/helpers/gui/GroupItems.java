package xyz.geik.farmer.helpers.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.model.user.User;

import java.util.stream.Collectors;

public class GroupItems {

    public static ItemStack getGroupItem(FarmerItem farmerItem, long capacity, double tax) {
        ItemStack result;
        if (farmerItem.getName().contains("-")) {
            result = new ItemStack(Material.getMaterial(farmerItem.getName().split("-")[0].toUpperCase()));
            result.setDurability(Short.parseShort(farmerItem.getName().split("-")[1]));
        }
        else
            result = new ItemStack(Material.getMaterial(farmerItem.getName().toUpperCase()));
        ItemMeta meta = result.getItemMeta();
        long stock = farmerItem.getAmount();
        double price = farmerItem.getPrice();
        int percent = (int) (100*stock/capacity);
        String color = selectFillColor(percent);
        meta.setLore(Main.getLangFile().getTextList("Gui.groupItem.lore").stream().map(key -> {
            return key.replace("{stock}", color + stock)
                    .replace("{maxstock}", String.valueOf(capacity))
                    .replace("{price}", String.valueOf(price))
                    .replace("{bar}", getFilledProgressBar(percent, stock, capacity, color))
                    .replace("{percent}", color + percent)
                    .replace("{stack_price}", String.valueOf((price*64)))
                    .replace("{tax}", String.valueOf(tax));
        }).collect(Collectors.toList()));
        result.setItemMeta(meta);
        return result;
    }

    public static ItemStack getUserItem(User user) {
        ItemStack result = null;
        if (Main.isOldVersion())
            result = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (short) 3);
        else result = new ItemStack(Material.getMaterial("PLAYER_HEAD"), 1);

        SkullMeta meta = (SkullMeta) result.getItemMeta();
        OfflinePlayer userPlayer = Bukkit.getOfflinePlayer(user.getUuid());
        if (Main.isOldVersion())
            meta.setOwner(userPlayer.getName());
        else
            meta.setOwningPlayer(userPlayer);

        meta.setDisplayName(Main.color("&b" + user.getName()));
        meta.setLore(Main.getLangFile().getTextList("usersGui.user.lore").stream().map(key -> {
            return key.replace("{role}", user.getPerm().name());
        }).collect(Collectors.toList()));
        result.setItemMeta(meta);
        return result;
    }

    private static String getFilledProgressBar(int percent, long stock, long capacity, String color) {
        final String barFull = Main.getLangFile().getString("percentBar");
        final String barSymbol = String.valueOf(barFull.charAt(0));
        String builder = "&7";
        if (stock == capacity)
            builder = "&c" + barFull;
        else if (stock == 0)
            builder += barFull;
        else {
            int filled = percent/(100/barFull.length());
            int empty = barFull.length()-filled;
            builder += color;
            for (int i = 1; i <= filled ; i++)
                builder += barSymbol;

            builder += "&7";
            for (int i = 1; i <= empty ; i++)
                builder += barSymbol;
        }
        return Main.color(builder);
    }

    private static String selectFillColor(int percent) {
        String result;
        if (percent >= 0 && percent <= 19)
            result = "&a";
        else if (percent >= 20 && percent <= 39)
            result = "&2";
        else if (percent >= 40 && percent <= 59)
            result = "&e";
        else if (percent >= 60 && percent <= 79)
            result = "&6";
        else result ="&c";
        return Main.color(result);
    }
}
