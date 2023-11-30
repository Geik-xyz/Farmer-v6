package xyz.geik.farmer.guis;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.chat.Placeholder;
import xyz.geik.glib.shades.inventorygui.DynamicGuiElement;
import xyz.geik.glib.shades.inventorygui.InventoryGui;
import xyz.geik.glib.shades.inventorygui.StaticGuiElement;

/**
 * Manage gui can be openable
 * by owner or administrator only
 */
public class ManageGui {

    /**
     * Constructor of class
     */
    public ManageGui() {}

    /**
     * Shows gui to player also contains event of it
     *
     * @param player to show gui
     * @param farmer of region
     */
    public static void showGui(Player player, Farmer farmer) {
        // Gui interface array
        String[] guiSetup = Main.getConfigFile().getGui().getManageLayout().toArray(new String[0]);
        // Inventory object
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, PlaceholderAPI.setPlaceholders(null, ChatUtils.color(Main.getLangFile().getGui().getManageGui().getGuiName())), guiSetup);
        // Filler for empty slots
        gui.setFiller(GuiHelper.getFiller(player));
        // Change state of Farmer Icon
        gui.addElement(new DynamicGuiElement('t', (viewer) -> {
            return new StaticGuiElement('t',
                // Placing item depending on state of farmer (Collecting or not)
                GuiHelper.getStatusItem(farmer.getState(), player),
                1,
                // Event of status change
                click -> {
                    if (farmer.getState() == 0)
                      farmer.setState(1);
                    else
                      farmer.setState(0);
                    gui.draw();
                    ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getToggleFarmer(),
                            new Placeholder("{status}", (farmer.getState() == 0) ?
                                    ChatUtils.color(Main.getLangFile().getVarious().getToggleOff()) :
                                    ChatUtils.color(Main.getLangFile().getVarious().getToggleOn())));
                    return true;
                });
        }));
        // Users gui opener
        gui.addElement(new StaticGuiElement('u',
                GuiHelper.getUserCategory(player),
                1,
                click -> {
                    UsersGui.showGui(player, farmer);
                    return true;
                })
        );
        // Level icon
        gui.addElement(new DynamicGuiElement('l', (viewer) -> {
            return new StaticGuiElement('l',
                // Level item
                GuiHelper.getLevelItem(farmer, player),
                1,
                // Event of level item click
                click -> {
                    int nextLevelIndex = FarmerLevel.getAllLevels().indexOf(farmer.getLevel())+1;
                    if (!(FarmerLevel.getAllLevels().size()-1 < nextLevelIndex)) {
                        FarmerLevel nextLevel = FarmerLevel.getAllLevels()
                                .get(nextLevelIndex);
                        if (Main.getEconomy().getBalance(player) >= nextLevel.getReqMoney()) {
                            if (nextLevel.getPerm() != null && !player.hasPermission(nextLevel.getPerm()))
                                ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNoPerm());
                            else {
                                Main.getEconomy().withdrawPlayer(player, nextLevel.getReqMoney());
                                farmer.setLevel(nextLevel);
                                farmer.getInv().setCapacity(nextLevel.getCapacity());
                                ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getLevelUpgraded(),
                                        new Placeholder("{level}", String.valueOf(nextLevelIndex+1)),
                                        new Placeholder("{capacity}", nextLevel.getCapacity()+""));
                            }
                        }
                        else
                            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNotEnoughMoney(),
                                    new Placeholder("{req_money}", nextLevel.getReqMoney()+""));
                        gui.draw();
                    }
                    return true;
                });
            })
        );
        // Module icon
        if (FarmerAPI.getModuleManager().isModulesUseGui())
            gui.addElement(new StaticGuiElement('m',
                    GuiHelper.getModuleGuiItem(player),
                    1,
                    click -> {
                        ModuleGui.showGui(player, farmer);
                        return true;
                    })
            );
        gui.show(player);
    }

}
