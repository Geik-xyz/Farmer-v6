package xyz.geik.farmer.modules.spawnerkiller;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.handlers.FarmerModuleGuiCreateEvent;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;

import java.util.stream.Collectors;

/**
 * Gui create event for spawner killer module
 * @author Geik
 */
public class SpawnerKillerGuiCreateEvent implements Listener {

    // TODO Description
    @EventHandler
    public void onGuiCreateEvent(@NotNull FarmerModuleGuiCreateEvent e) {
        char icon = SpawnerKiller.getInstance()
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
                            if (!e.getPlayer().hasPermission(SpawnerKiller.getInstance().getCustomPerm()))
                                return true;
                            // Change attribute
                            FarmerAPI.getModuleManager().changeAttribute("spawnerkiller", e.getFarmer());
                            e.getGui().draw();
                            return true;
                    })
                )
        );
    }

    // TODO Description
    @Contract(" -> new")
    private @NotNull ItemStack getGuiItem(Farmer farmer) {
        ItemStack item = GuiHelper.getItem("moduleGui.icon", SpawnerKiller.getInstance().getLang());
        ItemMeta meta = item.getItemMeta();
        String status = FarmerAPI.getModuleManager().getAttributeStatus("spawnerkiller", farmer) ?
                SpawnerKiller.getInstance().getLang().getText("enabled") :
                SpawnerKiller.getInstance().getLang().getText("disabled");
        meta.setLore(meta.getLore().stream().map(line -> line.replace("{status}", status))
                .collect(Collectors.toList()));
        item.setItemMeta(meta);
        return item;
    }
}
