package xyz.geik.farmer.helpers.gui;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.model.user.User;

import java.util.stream.Collectors;

/**
 * Main gui helper methods
 * can be seen here.
 */
public class GroupItems {

    /**
     * Farmer stock item which can be anything in items.yml
     * It also calculates stock and percent for display stock
     * Stock can be seen by color of lore or bar in lore
     *
     * @param farmerItem
     * @param capacity
     * @param tax
     * @return
     */
    public static @NotNull ItemStack getGroupItem(@NotNull FarmerItem farmerItem, long capacity, double tax) {
        ItemStack result = farmerItem.getMaterial().parseItem();
        ItemMeta meta = result.getItemMeta();
        // Stock amount of farmer
        long stock = farmerItem.getAmount();
        // Price of item
        double price = farmerItem.getPrice();
        // Calculates percent of stocked item
        int percent = (int) (100*stock/capacity);
        // Select color of stock capacity
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

    /**
     * User item which display in UserGui
     * It always player head.
     *
     * @param user
     * @return
     */
    public static @NotNull ItemStack getUserItem(@NotNull User user) {
        // Player head collector
        ItemStack result = SkullUtils.getSkull(user.getUuid());
        SkullMeta meta = (SkullMeta) result.getItemMeta();
        meta.setDisplayName(Main.color("&b" + user.getName()));
        meta.setLore(Main.getLangFile().getTextList("usersGui.user.lore").stream().map(key -> {
            return key.replace("{role}", user.getPerm().getName());
        }).collect(Collectors.toList()));
        result.setItemMeta(meta);
        return result;
    }

    /**
     * Progress bar uses in GroupItems#getGroupItem()
     * Which located here and percent bar shown in lore of group item
     *
     * @param percent
     * @param stock
     * @param capacity
     * @param color
     * @return
     */
    private static @NotNull String getFilledProgressBar(int percent, long stock, long capacity, String color) {
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

    /**
     * FillColor uses in GroupItems#getGroupItem()
     * Which located here and fill color calculates color of stock
     *
     * @param percent
     * @return
     */
    private static @NotNull String selectFillColor(int percent) {
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
