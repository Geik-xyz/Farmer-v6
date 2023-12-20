package xyz.geik.farmer.modules.geyser.gui;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerItemSellEvent;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.shades.inventorygui.InventoryGui;
import xyz.geik.glib.shades.inventorygui.StaticGuiElement;
import xyz.geik.glib.shades.xseries.XMaterial;

/**
 * GeyserGui which execute if player is geyser player
 */
public class GeyserGui {

    /**
     * Constructor of class
     */
    public GeyserGui() {}

    /**
     * Opens gui to player
     * @param player to show gui
     */
    public static void showGui(Player player, ItemStack cursorItem, XMaterial cursorItemMaterial, Farmer farmer, FarmerItem slotItem) {
        // Gui template as array
        String[] geyserGui = Main.getConfigFile().getGui().getGeyserLayout().toArray(new String[0]);
        // Inventory object
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, PlaceholderAPI.setPlaceholders(null, ChatUtils.color(Main.getLangFile().getGui().getGeyserGui().getGuiName())), geyserGui);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller(player));
        // Left item placer

        gui.addElement(new StaticGuiElement('l',
                // Item here
                GuiHelper.getGeyserGuiLeftItem(),
                1,
                click -> {
                    // If inventory full returns
                    if (invFull(player)) {
                        ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getInventoryFull());
                        return true;
                    }
                    long count = (slotItem.getAmount() >= cursorItem.getMaxStackSize()) ? cursorItem.getMaxStackSize() : slotItem.getAmount();
                    if (count == 0)
                        return true;
                    ItemStack returnItem = cursorItemMaterial.parseItem();
                    // Give item separately for != 64 amount of item
                    // Because bukkit library forces item to max stack amount 64
                    assert returnItem != null;
                    if (returnItem.getMaxStackSize() != 64) {
                        returnItem.setAmount(returnItem.getMaxStackSize());
                        long additional = count % returnItem.getMaxStackSize();
                        for (int i = 1; i <= count/returnItem.getMaxStackSize() ; i++)
                            player.getInventory().addItem(returnItem);
                        if (additional != 0) {
                            returnItem.setAmount((int) additional);
                            player.getInventory().addItem(returnItem);
                        }
                    }
                    // if max stack amount equals 64 does another method
                    else {
                        returnItem.setAmount((int) count);
                        player.getInventory().addItem(returnItem);
                    }
                    slotItem.negateAmount(count);
                    return true;
                }
        ));

        // Right item placer

        gui.addElement(new StaticGuiElement('r',
                // Item here
                GuiHelper.getGeyserGuiRightItem(),
                1,
                click -> {
                    // If inventory full returns
                    if (invFull(player)) {
                        ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getInventoryFull());
                        return true;
                    }
                    int playerEmpty = getEmptySlots(player) * cursorItem.getMaxStackSize();
                    long count = (slotItem.getAmount() >= playerEmpty)
                            ? playerEmpty
                            : slotItem.getAmount();
                    if (count == 0)
                        return true;
                    ItemStack returnItem = cursorItemMaterial.parseItem();
                    // Give item separately for != 64 amount of item
                    // Because bukkit library forces item to max stack amount 64
                    assert returnItem != null;
                    if (returnItem.getMaxStackSize() != 64) {
                        returnItem.setAmount(returnItem.getMaxStackSize());
                        long additional = count % returnItem.getMaxStackSize();
                        for (int i = 1; i <= count/returnItem.getMaxStackSize() ; i++)
                            player.getInventory().addItem(returnItem);
                        if (additional != 0) {
                            returnItem.setAmount((int) additional);
                            player.getInventory().addItem(returnItem);
                        }
                    }
                    // if max stack amount equals 64 does another method
                    else {
                        returnItem.setAmount((int) count);
                        player.getInventory().addItem(returnItem);
                    }
                    slotItem.negateAmount(count);
                    return true;
                }
        ));

        // Shift Right item placer

        gui.addElement(new StaticGuiElement('s',
                // Item here
                GuiHelper.getGeyserGuiShiftRightItem(),
                1,
                click -> {
                    if (slotItem.getPrice() < 0)
                        return true;
                    // Calls FarmerItemSellEvent
                    FarmerItemSellEvent itemSellEvent = new FarmerItemSellEvent(farmer, slotItem, player);
                    Bukkit.getPluginManager().callEvent(itemSellEvent);
                    return true;
                }
        ));

        gui.show(player);
    }

    /**
     * Checks if player has slot in inventory
     *
     * @param player to be checked
     * @return boolean status of inventory can take item
     */
    private static boolean invFull(@NotNull Player player) {
        return player.getInventory().firstEmpty() == -1;
    }

    /**
     * Gets all the empty slots
     *
     * @param player to be checked
     * @return int of empty slots
     */
    private static int getEmptySlots(@NotNull Player player) {
        int count = 0;
        for (int i = 0; i <= 35; i++) {
            if (player.getInventory().getItem(i) == null
                    || player.getInventory().getItem(i).getType().equals(Material.AIR)) {
                count++;
            } else
                continue;
        }
        return count;
    }
}