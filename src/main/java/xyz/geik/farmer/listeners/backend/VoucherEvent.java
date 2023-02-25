package xyz.geik.farmer.listeners.backend;

import com.cryptomorin.xseries.XSound;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.guis.MainGui;
import xyz.geik.farmer.helpers.ItemsLoader;
import xyz.geik.farmer.model.Farmer;

/**
 * @author Geyik
 */
public class VoucherEvent implements Listener {

    /**
     * TODO Description
     *
     * @param event
     */
    @EventHandler
    public void onRightClickEvent(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if (event.getItem().getItemMeta().getDisplayName() == null) return;
        Player player = event.getPlayer();
        int farmerLevel = NBT.get(event.getItem(), voucher -> (voucher.getInteger("farmerLevel")));
        ItemStack voucherBase = ItemsLoader.getVoucherItem(farmerLevel);
        voucherBase.setAmount(event.getItem().getAmount());
        if (!voucherBase.equals(event.getItem()))
            return;
        event.setCancelled(true);
        if (!Farmer.farmerWorldCheck(event.getPlayer())) {
            player.sendMessage(Main.getLangFile().getText("wrongWorld"));
            return;
        }
        if (!Main.getIntegration().getOwnerUUID(player.getLocation()).equals(player.getUniqueId())) {
            player.sendMessage(Main.getLangFile().getText("notOwner"));
            return;
        }
        if (Main.getFarmers().containsKey(Main.getIntegration().getRegionID(player.getLocation()))) {
            player.sendMessage(Main.getLangFile().getText("alreadyHaveFarmer"));
            return;
        }
        // Creates new farmer
        Farmer farmer = new Farmer(Main.getIntegration()
                .getRegionID(player.getLocation()), Main.getIntegration().getOwnerUUID(player.getLocation()), farmerLevel-1);
        // Opens farmer gui to buyer
        MainGui.showGui(player, farmer);
        // Sends message to player
        player.sendMessage(Main.getLangFile().getText("boughtFarmer"));
        XSound.ENTITY_PLAYER_LEVELUP.play(player);
        descentVoucher(player, event.getItem());
    }

    /**
     * TODO Description
     *
     * @param player
     * @param voucher
     */
    private void descentVoucher(Player player, @NotNull ItemStack voucher) {
        if (voucher.getAmount() == 1)
            player.getInventory().remove(voucher);
        else
            voucher.setAmount(voucher.getAmount() - 1);
    }
}
