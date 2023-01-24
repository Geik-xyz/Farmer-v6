package xyz.geik.farmer.guis;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;

/**
 * Buy Gui which execute if region don't have farmer
 */
public class BuyGui {

    /**
     * Opens gui to player
     * @param player
     */
    public static void showGui(Player player) {
        // Gui template as array
        String[] buyGui = Main.getLangFile().getList("buyGui.interface").toArray(String[]::new);
        // Inventory object
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, Main.getLangFile().getText("buyGui.guiName"), buyGui);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller());
        // Buy item placer
        gui.addElement(new StaticGuiElement('b',
                // Item here
                GuiHelper.getBuyItem(),
                1,
                // Event written on bottom
                click -> {
                    // If player has enough money to buy farmer
                    if (Main.getEcon().getBalance(player) >= Settings.farmerPrice) {
                        // Removes farmer money from player
                        Main.getEcon().withdrawPlayer(player, Settings.farmerPrice);
                        // Creates new farmer
                        Farmer farmer = new Farmer(Main.getIntegration()
                                .getRegionID(player.getLocation()), Main.getIntegration().getOwnerUUID(player.getLocation()));
                        // Opens farmer gui to buyer
                        MainGui.showGui(player, farmer);
                        // Sends message to player
                        player.sendMessage(Main.getLangFile().getText("boughtFarmer"));
                    }
                    // If player don't have enough money to buy send message
                    else
                        player.sendMessage(Main.getLangFile().getText("notEnoughMoney")
                                .replace("{req_money}", String.valueOf(Settings.farmerPrice)));
                    // After all close gui.
                    gui.close();
                    return true;
                })
        );
        gui.show(player);
    }
}
