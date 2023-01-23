package xyz.geik.farmer.guis;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;

public class ManageGui {

    public static void showGui(Player player, Farmer farmer) {
        String[] guiSetup = Main.getLangFile().getList("manageGui.interface").toArray(String[]::new);
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, Main.getLangFile().getText("manageGui.guiName"), guiSetup);
        gui.setFiller(GuiHelper.getFiller());
        // Close Farmer Icon
        gui.addElement(new DynamicGuiElement('t', (viewer) -> {
            return new StaticGuiElement('t',
                GuiHelper.getStatusItem(farmer.getState()),
                1,
                click -> {
                    if (farmer.getState() == 0)
                      farmer.setState(1);
                    else
                      farmer.setState(0);
                    gui.draw();
                    player.sendMessage(Main.getLangFile().getText("toggleFarmer")
                          .replace("{status}", (farmer.getState() == 0) ?
                                  Main.getLangFile().getText("toggleOFF") :
                                  Main.getLangFile().getText("toggleON")));
                    return true;
                });
        }));
        // Users gui opener
        gui.addElement(new StaticGuiElement('u',
                GuiHelper.getItem("manageGui.users"),
                1,
                click -> {
                    UsersGui.showGui(player, farmer);
                    return true;
                })
        );
        // level up
        gui.addElement(new DynamicGuiElement('l', (viewer) -> {
            return new StaticGuiElement('l',
                GuiHelper.getLevelItem(farmer),
                1,
                click -> {
                    int nextLevelIndex = FarmerLevel.getAllLevels().indexOf(farmer.getLevel())+1;
                    if (!(FarmerLevel.getAllLevels().size()-1 < nextLevelIndex)) {
                        FarmerLevel nextLevel = FarmerLevel.getAllLevels()
                                .get(nextLevelIndex);
                        if (Main.getEcon().getBalance(player) >= nextLevel.getReqMoney()) {
                            Main.getEcon().withdrawPlayer(player, nextLevel.getReqMoney());
                            farmer.setLevel(nextLevel);
                            farmer.getInv().setCapacity(nextLevel.getCapacity());
                            player.sendMessage(Main.getLangFile().getText("levelUpgraded")
                                    .replace("{level}", String.valueOf(nextLevelIndex+1))
                                    .replace("{capacity}", String.valueOf(nextLevel.getCapacity())));
                        }
                        else
                            player.sendMessage(Main.getLangFile().getText("notEnoughMoney")
                                    .replace("{req_money}", String.valueOf(nextLevel.getReqMoney())));
                        gui.draw();
                    }
                    return true;
                });
            })
        );
        gui.show(player);
    }

}
