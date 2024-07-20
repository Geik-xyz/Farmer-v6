package xyz.geik.farmer.modules.voucher.handlers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerRemoveEvent;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.guis.MainGui;
import xyz.geik.farmer.helpers.WorldHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.voucher.Voucher;
import xyz.geik.farmer.modules.voucher.helper.VoucherItem;
import xyz.geik.farmer.shades.nbtapi.NBT;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.shades.xseries.XSound;

/**
 * Voucher Event Listener class
 * @author poyraz
 * @since 1.0.0
 */
public class VoucherEvent implements Listener {

    /**
     * Constructor of class
     */
    public VoucherEvent() {}

    /**
     * Uses voucher to farmer and opens farmer gui
     *
     * @param event of player interact event
     */
    @EventHandler
    public void onVoucherUseEvent(@NotNull PlayerInteractEvent event) {
        if (event.getItem() == null)
            return;
        if (event.getItem().getItemMeta() == null)
            return;
        Player player = event.getPlayer();
        int voucherLevel;
        try {
            voucherLevel = NBT.get(event.getItem(), voucher -> (voucher.getInteger("farmerLevel")));
        }
        catch (Exception e) {
            return;
        }
        ItemStack voucherBase = VoucherItem.getVoucherItem(voucherLevel);
        voucherBase.setAmount(event.getItem().getAmount());
        if (!voucherBase.equals(event.getItem()))
            return;
        event.setCancelled(true);
        if (!WorldHelper.isFarmerAllowed(player.getWorld().getName())) {
            player.sendMessage(Voucher.getInstance().getLang().getText("wrongWorld"));
            return;
        }
        if (!Main.getIntegration().getOwnerUUID(player.getLocation()).equals(player.getUniqueId())) {
            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getNotOwner());
            return;
        }
        if (FarmerManager.getFarmers().containsKey(Main.getIntegration().getRegionID(player.getLocation()))) {
            if (Main.getModulesFile().getVoucher().isUseWhenFarmerExist()) {
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
                .getRegionID(player.getLocation()), voucherLevel-1);
        // Opens farmer gui to buyer
        MainGui.showGui(player, farmer);
        // Sends message to player
        ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getBoughtFarmer());
        XSound.ENTITY_PLAYER_LEVELUP.play(player);
        descentVoucher(player, event.getItem());
    }

    /**
     * Gives voucher when farmer is removed to owner
     *
     * @param event of farmer remove event
     */
    @EventHandler
    public void onFarmerRemoveEvent(FarmerRemoveEvent event) {
        if (Main.getModulesFile().getVoucher().isGiveVoucherWhenRemove()) {
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
     * @param player to remove item
     * @param voucher item of voucher
     */
    private void descentVoucher(Player player, @NotNull ItemStack voucher) {
        if (voucher.getAmount() == 1)
            player.getInventory().remove(voucher);
        else
            voucher.setAmount(voucher.getAmount() - 1);
    }
}
