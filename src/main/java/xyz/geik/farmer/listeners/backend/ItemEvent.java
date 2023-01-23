package xyz.geik.farmer.listeners.backend;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerInv;

public class ItemEvent implements Listener {

    @EventHandler(priority= EventPriority.HIGHEST)
    public void itemSpawnEvent(ItemSpawnEvent e) {
        /**
         * Has Item in farmer
         * Item don't have meta
         * Checked player drop
         * Checked World
         * Checked if has farmer on location
         * Checked if farmer closed
         */
        if (!Settings.allowedWorlds.contains(e.getLocation().getWorld().getName()))
            return;
        if (Settings.ignorePlayerDrop && e.getEntity().getPickupDelay() >= 39)
            return;

        ItemStack item = new ItemStack(e.getEntity().getItemStack());
        if (item.hasItemMeta())
            return;

        if (!(FarmerInv.defaultItems.stream().anyMatch(itm -> (itm.getName().equalsIgnoreCase(item.getType().name())))
                || Main.isOldVersion() && FarmerInv.defaultItems.stream().anyMatch(itm -> (
                        itm.getName().contains("-")
                                && itm.getName().split("-")[0].equalsIgnoreCase(item.getType().name())
                                    && itm.getName().split("-")[1].equalsIgnoreCase(String.valueOf(item.getDurability()))))))
            return;

        String regionID = Main.getIntegration().getRegionID(e.getLocation());
        if (regionID == null || !Main.getFarmers().containsKey(regionID))
            return;

        Farmer farmer = Main.getFarmers().get(regionID);
        if (farmer.getState() == 0)
            return;

        long left = -1;

        int data = 0;
        if (Main.isOldVersion()) {
            data = item.getDurability();
            if (data != 0)
                left = farmer.getInv().sumItemAmount(item.getType().name() + "-" + data, item.getAmount());
        }

        if (left == -1)
            left = farmer.getInv().sumItemAmount(item.getType().name(), item.getAmount());

        if (left != 0)
            item.setAmount((int) left);

        else
            e.setCancelled(true);
    }
}
