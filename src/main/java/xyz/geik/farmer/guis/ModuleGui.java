package xyz.geik.farmer.guis;

import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerModuleGuiCreateEvent;
import xyz.geik.farmer.helpers.PlaceholderHelper;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.shades.inventorygui.InventoryGui;

/**
 * Module Gui class
 * @author poyraz
 * @since 1.0.0
 */
public class ModuleGui {

    /**
     * Constructor of class
     */
    public ModuleGui() {}

    /**
     * Shows gui to player
     *
     * @param player player to show
     * @param farmer farmer object
     */
    public static void showGui(Player player, Farmer farmer) {
        // Gui template as array
        String[] moduleGui = Main.getConfigFile().getGui().getModuleLayout().toArray(new String[0]);
        // Inventory object
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, PlaceholderHelper.parsePlaceholders(player, ChatUtils.color(Main.getLangFile().getGui().getModuleGui().getGuiName())), moduleGui);
        // Filler item for empty slots
        gui.setFiller(GuiHelper.getFiller(player));
        // Addons placer
        FarmerModuleGuiCreateEvent event = new FarmerModuleGuiCreateEvent(player, gui, farmer);
        Main.getInstance().getServer().getPluginManager().callEvent(event);
        // Shows gui to player
        if (!event.isCancelled())
            gui.show(player);
    }
}
