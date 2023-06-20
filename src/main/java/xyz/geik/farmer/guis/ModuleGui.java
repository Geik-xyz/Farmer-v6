package xyz.geik.farmer.guis;

import de.themoep.inventorygui.InventoryGui;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerModuleGuiCreateEvent;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;

/**
 * Module Gui class
 * @author poyraz
 * @since 1.0.0
 */
public class ModuleGui {

    /**
     * @param player player to show
     * @param farmer farmer object
     */
    public static void showGui(Player player, Farmer farmer) {
        // Gui template as array
        String[] moduleGui = Main.getLangFile().getStringList("moduleGui.interface").toArray(new String[0]);
        // Inventory object
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, Main.getLangFile().getText("moduleGui.guiName"), moduleGui);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller());
        // Addons placer
        FarmerModuleGuiCreateEvent event = new FarmerModuleGuiCreateEvent(player, gui, farmer);
        Main.getInstance().getServer().getPluginManager().callEvent(event);
        // Shows gui to player
        if (!event.isCancelled())
            gui.show(player);
    }
}
