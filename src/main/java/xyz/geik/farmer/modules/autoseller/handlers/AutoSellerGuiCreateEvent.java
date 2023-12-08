package xyz.geik.farmer.modules.autoseller;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.api.handlers.FarmerModuleGuiCreateEvent;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.glib.shades.inventorygui.DynamicGuiElement;
import xyz.geik.glib.shades.inventorygui.StaticGuiElement;

import java.util.stream.Collectors;

/**
 * Auto Seller Gui listener and events
 */
public class AutoSellerGuiCreateEvent implements Listener {

    /**
     * Constructor of class
     */
    public AutoSellerGuiCreateEvent() {}

    /**
     * Creates the GUI element for the module and adds it to the GUI
     *
     * @param e of module gui create event
     */
    @EventHandler
    public void onGuiCreateEvent(@NotNull FarmerModuleGuiCreateEvent e) {
        char icon = AutoSeller.getInstance()
                .getLang().getString("moduleGui.icon.guiInterface").charAt(0);
        e.getGui().addElement(
                new DynamicGuiElement(icon, (viewer) ->
                        new StaticGuiElement(
                                icon,
                                // Item here
                                getGuiItem(e.getFarmer()),
                                1,
                                // Event written bottom
                                click -> {
                                    // If player don't have permission do nothing
                                    if (!e.getPlayer().hasPermission(AutoSeller.getInstance().getCustomPerm()))
                                        return true;
                                    // Change attribute
                                    e.getFarmer().changeAttribute("autoseller");
                                    e.getGui().draw();
                                    return true;
                                })
                )
        );
    }

    /**
     * Get the item for the GUI
     *
     * @param farmer of region
     * @return item stack of module gui
     */
    private @NotNull ItemStack getGuiItem(@NotNull Farmer farmer) {
        ItemStack item = GuiHelper.getItem("moduleGui.icon", AutoSeller.getInstance().getLang());
        ItemMeta meta = item.getItemMeta();
        String status = farmer.getAttributeStatus("autoseller") ?
                AutoSeller.getInstance().getLang().getText("enabled") :
                AutoSeller.getInstance().getLang().getText("disabled");
        meta.setLore(meta.getLore().stream().map(line -> line.replace("{status}", status))
                .collect(Collectors.toList()));
        item.setItemMeta(meta);
        return item;
    }
}
