package xyz.geik.farmer.modules.voucher;

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
import xyz.geik.farmer.model.Farmer;

public class VoucherEvent implements Listener {

    /**
     * Uses voucher to farmer and opens farmer gui
     *
     * @param event
     */
    @EventHandler
    public void onVoucherUseEvent(@NotNull PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if (event.getItem().getItemMeta().getDisplayName() == null) return;
        Player player = event.getPlayer();
        int farmerLevel = NBT.get(event.getItem(), voucher -> (voucher.getInteger("farmerLevel")));
        ItemStack voucherBase = VoucherItem.getVoucherItem(farmerLevel);
        voucherBase.setAmount(event.getItem().getAmount());
        if (!voucherBase.equals(event.getItem()))
            return;
        event.setCancelled(true);
        if (!Farmer.farmerWorldCheck(event.getPlayer())) {
            player.sendMessage(Voucher.getInstance().getInstance().getConfig().getText("wrongWorld"));
            return;
        }
        if (!Main.getIntegration().getOwnerUUID(player.getLocation()).equals(player.getUniqueId())) {
            player.sendMessage(Voucher.getInstance().getConfig().getText("notOwner"));
            return;
        }
        if (Main.getFarmers().containsKey(Main.getIntegration().getRegionID(player.getLocation()))) {
            player.sendMessage(Voucher.getInstance().getConfig().getText("alreadyHaveFarmer"));
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
     * Descent voucher amount in inventory
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
