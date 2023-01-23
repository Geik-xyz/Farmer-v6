package xyz.geik.farmer.guis;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;

public class BuyGui {

    public static void showGui(Player player) {
        String[] buyGui = Main.getLangFile().getList("buyGui.interface").toArray(String[]::new);
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, Main.getLangFile().getText("buyGui.guiName"), buyGui);
        gui.setFiller(GuiHelper.getFiller());
        // Close Farmer Icon
        gui.addElement(new StaticGuiElement('b',
                GuiHelper.getBuyItem(),
                1,
                click -> {
                    if (Main.getEcon().getBalance(player) >= Settings.farmerPrice) {
                        Main.getEcon().withdrawPlayer(player, Settings.farmerPrice);
                        Farmer farmer = new Farmer(Main.getIntegration()
                                .getRegionID(player.getLocation()), Main.getIntegration().getOwnerUUID(player.getLocation()));
                        MainGui.showGui(player, farmer);
                        player.sendMessage(Main.getLangFile().getText("boughtFarmer"));
                    }
                    else
                        player.sendMessage(Main.getLangFile().getText("notEnoughMoney")
                                .replace("{req_money}", String.valueOf(Settings.farmerPrice)));
                    gui.close();
                    return true;
                })
        );
        gui.show(player);
    }
}
