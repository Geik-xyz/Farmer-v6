package xyz.geik.farmer.modules.voucher;

import com.cryptomorin.xseries.XSound;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.handlers.FarmerRemoveEvent;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.guis.MainGui;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;

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
        Player player = event.getPlayer();
        int voucherLevel = NBT.get(event.getItem(), voucher -> (voucher.getInteger("farmerLevel")));
        ItemStack voucherBase = VoucherItem.getVoucherItem(voucherLevel);
        voucherBase.setAmount(event.getItem().getAmount());
        if (!voucherBase.equals(event.getItem()))
            return;
        event.setCancelled(true);
        if (!Settings.isWorldAllowed(player.getWorld().getName())) {
            player.sendMessage(Voucher.getInstance().getInstance().getLang().getText("wrongWorld"));
            return;
        }
        if (!Main.getIntegration().getOwnerUUID(player.getLocation()).equals(player.getUniqueId())) {
            player.sendMessage(Main.getLangFile().getText("notOwner"));
            return;
        }
        if (FarmerManager.getFarmers().containsKey(Main.getIntegration().getRegionID(player.getLocation()))) {
            if (Voucher.getInstance().isOverrideFarmer()) {
                Farmer farmer = FarmerManager.getFarmers().get(Main.getIntegration().getRegionID(player.getLocation()));
                if ((voucherLevel-1) > FarmerLevel.getAllLevels().indexOf(farmer.getLevel())) {
                    farmer.setLevel(FarmerLevel.getAllLevels().get(voucherLevel-1));
                    player.sendMessage(Voucher.getInstance().getLang().getText("changedLevel")
                            .replace("%level%", voucherLevel+""));
                    XSound.ENTITY_PLAYER_LEVELUP.play(player);
                    descentVoucher(player, event.getItem());
                    return;
                }
                else
                    player.sendMessage(Voucher.getInstance().getLang().getText("levelHigher"));
            }
            else
                player.sendMessage(Voucher.getInstance().getLang().getText("alreadyHaveFarmer"));
            return;
        }
        // Creates new farmer
        Farmer farmer = new Farmer(Main.getIntegration()
                .getRegionID(player.getLocation()), Main.getIntegration().getOwnerUUID(player.getLocation()), voucherLevel-1);
        // Opens farmer gui to buyer
        MainGui.showGui(player, farmer);
        // Sends message to player
        player.sendMessage(Main.getLangFile().getText("boughtFarmer"));
        XSound.ENTITY_PLAYER_LEVELUP.play(player);
        descentVoucher(player, event.getItem());
    }

    /**
     * Gives voucher when farmer is removed to owner
     *
     * @param event
     */
    @EventHandler
    public void onFarmerRemoveEvent(FarmerRemoveEvent event) {
        if (Voucher.getInstance().isGiveVoucherWhenRemove()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(event.getFarmer().getOwnerUUID());
            if (offlinePlayer.isOnline()) {
                Player player = offlinePlayer.getPlayer();
                int level = FarmerLevel.getAllLevels().indexOf(event.getFarmer().getLevel())+1;
                player.getInventory().addItem(VoucherItem.getVoucherItem(level));
                player.sendMessage(Voucher.getInstance().getLang().getText("voucherReceived")
                        .replace("%level%", level+""));
            }
        }
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
