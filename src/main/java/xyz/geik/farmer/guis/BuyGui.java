package xyz.geik.farmer.guis;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.chat.Placeholder;
import xyz.geik.glib.shades.inventorygui.InventoryGui;
import xyz.geik.glib.shades.inventorygui.StaticGuiElement;
import xyz.geik.glib.shades.xseries.XSound;

/**
 * Buy Gui which execute if region don't have farmer
 */
public class BuyGui {

    /**
     * Constructor of class
     */
    public BuyGui() {}

    /**
     * Opens gui to player
     * @param player to show gui
     */
    public static void showGui(Player player) {
        // Gui template as array
        String[] buyGui = Main.getConfigFile().getGui().getBuyFarmerLayout().toArray(new String[0]);
        // Inventory object
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, PlaceholderAPI.setPlaceholders(null, ChatUtils.color(Main.getLangFile().getGui().getBuyGui().getGuiName())), buyGui);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller(player));
        // Buy item placer
        gui.addElement(new StaticGuiElement('b',
                // Item here
                GuiHelper.getBuyItem(player),
                1,
                // Event written on bottom
                click -> {
                    double farmerPrice = Main.getConfigFile().getSettings().getFarmerPrice();
                    // If player has enough money to buy farmer
                    if (Main.getEconomy().getBalance(player) >= farmerPrice) {
                        // Removes farmer money from player
                        Main.getEconomy().withdrawPlayer(player, farmerPrice);
                        // Creates new farmer
                        Farmer farmer = new Farmer(Main.getIntegration()
                                .getRegionID(player.getLocation()), 0);
                        XSound.ENTITY_PLAYER_LEVELUP.play(player);
                        // Opens farmer gui to buyer
                        MainGui.showGui(player, farmer);
                        // Sends message to player
                        ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getBoughtFarmer());
                    }
                    // If player don't have enough money to buy send message
                    else
                        ChatUtils.sendMessage(player, ChatUtils.replacePlaceholders(
                                Main.getLangFile().getMessages().getNotEnoughMoney(),
                                new Placeholder("{req_money}", farmerPrice+"")));
                    // After all close gui.
                    gui.close();
                    return true;
                })
        );
        gui.show(player);
    }
}
