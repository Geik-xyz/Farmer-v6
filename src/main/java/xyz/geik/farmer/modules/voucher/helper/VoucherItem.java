package xyz.geik.farmer.modules.voucher.helper;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.modules.voucher.Voucher;
import xyz.geik.farmer.shades.nbtapi.NBT;
import xyz.geik.glib.shades.xseries.XMaterial;
import xyz.geik.glib.shades.xseries.profiles.builder.XSkull;
import xyz.geik.glib.shades.xseries.profiles.objects.Profileable;

import java.util.stream.Collectors;

/**
 * VoucherItem class
 *
 * @author Geik
 * @version 1.0.0
 * @since 1.0.0
 */
public class VoucherItem {

    /**
     * Constructor of class
     */
    public VoucherItem() {}

    /**
     * Get voucher item with level
     *
     * @param level Integer level of voucher
     * @return ItemStack Item of voucher
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
            if (key.contains("%level%"))
                return key.replace("%level%", String.valueOf(level));
            return key;
        }).collect(Collectors.toList()));
        // Changes level placeholder to level
        meta.setDisplayName(meta.getDisplayName().replace("%level%", String.valueOf(level)));
        // Saves meta
        voucher.setItemMeta(meta);
        return voucher;
    }

    /**
     * If item has skull it gets item as head with
     * custom head data. Otherwise, check for material and
     * get item with a material.
     * @param path item path
     * @return ItemStack of destination item
     */
    public static @NotNull ItemStack getItem(String path) {
        ItemStack result;
        // If item is skull instead of material based item
        if (Voucher.getInstance().getLang().contains(path + ".skull")) {
            result = XMaterial.matchXMaterial("PLAYER_HEAD").get().parseItem();
            try {
                SkullMeta meta = (SkullMeta) result.getItemMeta();
                assert meta != null;
                // GameProfile, Filed etc. used mojang lib for catch player skull
                Profileable requestedProfile = Profileable
                        .detect(Voucher.getInstance().getLang().getString(path + ".skull"));
                Profileable defaultProfile = Profileable.detect("Steve");
                meta = (SkullMeta) XSkull.of(meta)
                        .profile(requestedProfile)
                        .fallback(defaultProfile).applyAsync();
                result.setItemMeta(meta);
            } catch (Exception e) {
                result = new ItemStack(Material.STONE, 1);
            }
        }
        // If item is material based something
        else
            result = XMaterial.matchXMaterial(Voucher.getInstance().getLang().getString(path + ".material")).get().parseItem();

        ItemMeta meta = result.getItemMeta();
        if (Voucher.getInstance().getLang().contains(path + ".lore"))
            meta.setLore(Voucher.getInstance().getLang().getTextList(path + ".lore"));
        meta.setDisplayName(Voucher.getInstance().getLang().getText(path + ".name"));
        if (Voucher.getInstance().getLang().getBoolean(path + ".glow")) {
            meta.addEnchant(Enchantment.getByName("DAMAGE_ALL"), 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        result.setItemMeta(meta);
        return result;
    }



}
