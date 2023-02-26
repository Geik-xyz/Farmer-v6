package xyz.geik.farmer.modules.voucher;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

/**
 * VoucherItem class
 *
 * @author
 * @version 1.0.0
 * @since 1.0.0
 */
public class VoucherItem {

    /**
     * Get voucher item with level
     *
     * @param level
     * @return
     */
    public static @NotNull ItemStack getVoucherItem(int level) {
        // Item loader
        ItemStack voucher = getItem("voucher");
        // NBT loader
        NBT.modify(voucher, nbt -> {
            nbt.setInteger("farmerLevel", level);
        });
        // ItemMeta loader
        ItemMeta meta = voucher.getItemMeta();
        // Changes level placeholder to level
        meta.setLore(meta.getLore().stream().map(key -> {
            if (key.contains("{level}"))
                return key.replace("{level}", String.valueOf(level));
            return key;
        }).collect(Collectors.toList()));
        // Changes level placeholder to level
        meta.setDisplayName(meta.getDisplayName().replace("{level}", String.valueOf(level)));
        // Saves meta
        voucher.setItemMeta(meta);
        return voucher;
    }

    /**
     * If item has skull it gets item as head with
     * custom head data. Otherwise, check for material and
     * get item with a material.
     */
    public static @NotNull ItemStack getItem(String path) {
        ItemStack result;
        // If item is skull instead of material based item
        if (Voucher.getInstance().getConfig().contains(path + ".skull")) {
            result = XMaterial.matchXMaterial("PLAYER_HEAD").get().parseItem();
            try {
                SkullMeta meta = (SkullMeta) result.getItemMeta();
                assert meta != null;
                // GameProfile, Filed etc. used mojang lib for catch player skull
                SkullUtils.applySkin(meta, Voucher.getInstance().getConfig().getString(path + ".skull"));
                result.setItemMeta(meta);
            } catch (Exception e) {
                result = new ItemStack(Material.STONE, 1);
            }
        }
        // If item is material based something
        else
            result = XMaterial.matchXMaterial(Voucher.getInstance().getConfig().getString(path + ".material")).get().parseItem();

        ItemMeta meta = result.getItemMeta();
        if (Voucher.getInstance().getConfig().contains(path + ".lore"))
            meta.setLore(Voucher.getInstance().getConfig().getTextList(path + ".lore"));
        meta.setDisplayName(Voucher.getInstance().getConfig().getText(path + ".name"));
        result.setItemMeta(meta);
        return result;
    }



}
