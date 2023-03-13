package xyz.geik.farmer.listeners.backend;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerItemSellEvent;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;

public class ItemSellEvent implements Listener {

    /**
     * Sell item event
     *
     * @param event
     * @return
     */
    @EventHandler
    public void sellItemEvent(@NotNull FarmerItemSellEvent event) {
        FarmerItem slotItem = event.getFarmerItem();
        Farmer farmer = event.getFarmer();
        if (slotItem.getAmount() == 0)
            return;
        // Calculating tax, profit and selling price
        double sellPrice = slotItem.getPrice() * slotItem.getAmount();
        double profit = (farmer.getLevel().getTax() > 0)
                ? sellPrice-(sellPrice*farmer.getLevel().getTax()/100)
                : sellPrice;
        double tax = (sellPrice == profit) ? 0 : sellPrice*farmer.getLevel().getTax()/100;
        // If configuration has deposit tax to
        // defined player then it will deposit it
        // to player.
        if (Settings.depositTax)
            Main.getEcon()
                    .depositPlayer(Settings.taxUser, tax);
        Main.getEcon().depositPlayer(event.getPlayer(), profit);
        slotItem.setAmount(0);
        if (event.getPlayer().isOnline())
            event.getPlayer().sendMessage(Main.getLangFile().getText("sellComplete")
                .replace("{money}", roundDouble(profit))
                .replace("{tax}", roundDouble(tax)));
    }

    /**
     * Rounds double for display good.
     * It shown as #.## but if this isn't exist
     * It shown as #.######## something like that.
     *
     * @param value
     * @return
     */
    private static @NotNull String roundDouble(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        double result = (double) tmp / factor;
        return String.valueOf( result );
    }
}
