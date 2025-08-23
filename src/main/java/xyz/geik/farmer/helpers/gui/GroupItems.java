package xyz.geik.farmer.helpers.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.model.user.User;
import xyz.geik.farmer.modules.production.model.ProductionModel;
import xyz.geik.glib.chat.ChatUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Main gui helper methods
 * can be seen here.
 */
public class GroupItems {

    private static final Pattern MODULE_P = Pattern.compile("\\{module_(.*?)\\}");

    /**
     * Constructor of class
     */
    public GroupItems() {}

    /**
     * Farmer stock item which can be anything in items.yml
     * It also calculates stock and percent for display stock.
     * Stock can be seen by color of lore or bar in lore
     *
     * @param farmer farmer object
     * @param farmerItem item of farmer
     * @return ItemStack of farmer held
     */
    public static @NotNull ItemStack getGroupItem(@NotNull Farmer farmer, @NotNull FarmerItem farmerItem) {
        long capacity = farmer.getLevel().getCapacity();
        double tax = farmer.getLevel().getTax();
        ItemStack result = farmerItem.getMaterial().parseItem();
        assert result != null;
        ItemMeta meta = result.getItemMeta();
        // Stock amount of farmer
        long stock = farmerItem.getAmount();
        // Price of item
        double price = farmerItem.getPrice();
        // Calculates percent of stocked item
        int percent = (int) (100*stock/capacity);
        // Select color of stock capacity
        String color = selectFillColor(percent);
        // Average production of item cache if it's null
        // it won't be displayed because there is no calculation required
        ProductionModel productionModel = farmer.getInv().getProductionModels().stream()
                .filter(g -> g.getMaterial().equals(farmerItem.getMaterial()))
                .findFirst().orElse(null);
        // Lore map
        meta.setLore(ChatUtils.color(Main.getLangFile().getGui().getFarmerGui().getItems().getGroupItems().getLore().stream()
                .map(key -> {
            // If key contains {prod_ it will be replaced with average production data
            // If there is no data then makes it null
            key = key.replace("%item%", farmerItem.getMaterial().name());
            Pattern pattern = Pattern.compile("\\{module_(.*?)\\}");
            Matcher matcher = pattern.matcher(key);
            while (matcher.find()) {
                String value = matcher.group(1);              // örn: "autoseller_CACTUS"
                String[] parts = value.split("_", 2);         // ["autoseller", "CACTUS"]
                String moduleName = parts[0];                 // "autoseller"
                String itemName   = parts.length > 1 ? parts[1] : ""; // "CACTUS"
                boolean defaultItemStatus = Farmer.getGlobalAttributes()
                        .getOrDefault(moduleName + "_item_default", false);
                String status = farmer.getAttributeStatus(moduleName)
                        && farmer.getAttributeStatus(value, defaultItemStatus) ?
                        Main.getLangFile().getVarious().getToggleOn() :
                        Main.getLangFile().getVarious().getToggleOff();
                key = key.replace("{module_" + value + "}", status);
            }

            if (key.contains("{prod_"))
                return ProductionModel.updateLore(productionModel, key);
            // Default replace
            else if (key.contains("{price}") && price < 0)
                return null;
            else
                return key.replace("{stock}", color + stock)
                    .replace("{maxstock}", capacity +"")
                    .replace("{price}", price +"")
                    .replace("{bar}", getFilledProgressBar(percent, stock, capacity, color))
                    .replace("{percent}", color + percent)
                    .replace("{stack_price}", (price*64) +"")
                    .replace("{tax}", tax +"");
        // Filters null values
        }).filter(Objects::nonNull)
                .collect(Collectors.toList())));
        result.setItemMeta(meta);
        return result;
    }

    /**
     * User item which display in UserGui
     * It always player head.
     *
     * @param user of farmer
     * @return ItemStack of user head
     */
    public static @NotNull ItemStack getUserItem(@NotNull User user) {
        // Player head collector
        ItemStack item;
        String name = Main.getLangFile().getGui().getUsersGui().getItems().getUser().getName();
        List<String> lore = Main.getLangFile().getGui().getUsersGui().getItems().getUser().getLore();
        String material = Main.getConfigFile().getGui().getUsersGuiItems().getUserHeadMaterial();
        if (material == null || material.isEmpty() || material.equalsIgnoreCase("PLAYER_HEAD"))
            material = user.getUuid().toString();
        item = GuiHelper.getItem(name, lore, 0, material, false, Bukkit.getOfflinePlayer(user.getUuid()));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatUtils.color("&b" + user.getName()));
        meta.setLore(meta.getLore().stream()
                .map(key -> key.replace("{role}", ChatUtils.color(user.getPerm().getName()))).collect(Collectors.toList()));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Progress bar uses in GroupItems#getGroupItem()
     * Which located here and percent bar shown in lore of group item
     *
     * @param percent of progress
     * @param stock of farmer
     * @param capacity of farmer
     * @param color of bar
     * @return String of finalized bar
     */
    private static @NotNull String getFilledProgressBar(int percent, long stock, long capacity, String color) {
        final String barFull = Main.getLangFile().getVarious().getPercentBar();
        final String barSymbol = String.valueOf(barFull.charAt(0));
        StringBuilder builder = new StringBuilder("&7");
        if (stock == capacity)
            builder = new StringBuilder("&c" + barFull);
        else if (stock == 0)
            builder.append(barFull);
        else {
            int filled = percent/(100/barFull.length());
            int empty = barFull.length()-filled;
            builder.append(color);
            for (int i = 1; i <= filled ; i++)
                builder.append(barSymbol);

            builder.append("&7");
            for (int i = 1; i <= empty ; i++)
                builder.append(barSymbol);
        }
        return ChatUtils.color(builder.toString());
    }

    /**
     * FillColor uses in GroupItems#getGroupItem()
     * Which located here and fill color calculates color of stock
     *
     * @param percent of storage
     * @return String color
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
        return ChatUtils.color(result);
    }
}
