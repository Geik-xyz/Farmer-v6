package xyz.geik.farmer.helpers.gui;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.shades.storage.Config;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.shades.inventorygui.GuiElement;
import xyz.geik.glib.shades.inventorygui.GuiPageElement;
import xyz.geik.glib.shades.inventorygui.StaticGuiElement;
import xyz.geik.glib.shades.xseries.XMaterial;
import xyz.geik.glib.shades.xseries.profiles.builder.XSkull;
import xyz.geik.glib.shades.xseries.profiles.objects.Profileable;
import xyz.geik.glib.shades.xseries.reflection.parser.ReflectionParser;
import xyz.geik.glib.utils.ItemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  All guis using this method for
 *  items and all items located here.
 *
 * @author Geik
 */
public class GuiHelper {

    /**
     * Constructor of class
     */
    public GuiHelper() {}

    /**
     * Filler item of guis.
     * Filler item basically fills empty slots of gui
     *
     * @param player for placeholder
     * @return ItemStack of filler
     */
    public static ItemStack getFiller(OfflinePlayer player) {
        ItemStack item;
        if (Main.getConfigFile().getGui().getGlobalItems().getFillerItem().isUseFiller()) {
            String name = "";
            List<String> lore = new ArrayList<>();
            int modelData = Main.getConfigFile().getGui().getGlobalItems().getFillerItem().getModelData();
            String material = Main.getConfigFile().getGui().getGlobalItems().getFillerItem().getMaterial();
            boolean hasGlow = Main.getConfigFile().getGui().getGlobalItems().getFillerItem().isHasGlow();
            item = getItem(name, lore, modelData, material, hasGlow, player);
        }
        else
            item = new ItemStack(Material.AIR);
        return item;
    }

    /**
     * If item has skull it gets item as head with
     * custom head data. Otherwise, check for material and
     * get item with a material.
     * @param name item name
     * @param lore lore of item
     * @param modelData modeldata of item
     * @param material material of item string
     * @param hasGlow is item has glow
     * @param player placeholder player
     * @return ItemStack of destination item
     */
    public static @NotNull ItemStack getItem(String name, List<String> lore, int modelData, String material, boolean hasGlow, OfflinePlayer player) {
        return ItemUtil.getItem(name, lore, modelData, material, hasGlow, player);
    }

    /**
     * GuiElement creator simple way
     * for templates
     *
     * @param item item of element
     * @param key to be in gui
     * @return GuiElement is finalized element
     */
    @Contract("_, _ -> new")
    public static @NotNull GuiElement createGuiElement(ItemStack item, char key) {
        return new StaticGuiElement(key,
                item,
                1,
                click -> true);
    }

    /**
     * Previous page item creator
     *
     * @param player for placeholder
     * @return GuiElement of previous menu icon
     */
    @Contract(" -> new")
    public static @NotNull GuiElement createPreviousPage(OfflinePlayer player) {
        ItemStack previousPageItem;
        String name = Main.getLangFile().getGui().getPreviousPage().getName();
        List<String> lore = new ArrayList<>();
        int modelData = Main.getConfigFile().getGui().getGlobalItems().getPreviousPage().getModelData();
        String material = Main.getConfigFile().getGui().getGlobalItems().getPreviousPage().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getGlobalItems().getPreviousPage().isHasGlow();
        previousPageItem = getItem(name, lore, modelData, material, hasGlow, player);
        return new GuiPageElement('p',
                previousPageItem,
                GuiPageElement.PageAction.PREVIOUS,
                name
        );
    }

    /**
     * Next page item creator
     *
     * @param player for placeholder
     * @return GuiElement of next menu icon
     */
    @Contract(" -> new")
    public static @NotNull GuiElement createNextPage(OfflinePlayer player) {
        ItemStack nextPageItem;
        String name = Main.getLangFile().getGui().getNextPage().getName();
        List<String> lore = new ArrayList<>();
        int modelData = Main.getConfigFile().getGui().getGlobalItems().getNextPage().getModelData();
        String material = Main.getConfigFile().getGui().getGlobalItems().getNextPage().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getGlobalItems().getNextPage().isHasGlow();
        nextPageItem = getItem(name, lore, modelData, material, hasGlow, player);
        return new GuiPageElement('n',
                nextPageItem,
                GuiPageElement.PageAction.NEXT,
                name
        );
    }

    /**
     * Manage gui item which located on
     * farmer main gui.
     *
     * @param farmer of region
     * @param player for placeholders
     * @return ItemStack manage icon
     */
    public static @NotNull ItemStack getManageItemOnMain(Farmer farmer, OfflinePlayer player) {
        ItemStack manage;
        String name = Main.getLangFile().getGui().getFarmerGui().getItems().getManage().getName();
        List<String> lore = Main.getLangFile().getGui().getFarmerGui().getItems().getManage().getLore();
        int modelData = Main.getConfigFile().getGui().getFarmerGuiItems().getManage().getModelData();
        String material = Main.getConfigFile().getGui().getFarmerGuiItems().getManage().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getFarmerGuiItems().getManage().isHasGlow();
        manage = getItem(name, lore, modelData, material, hasGlow, player);
        ItemMeta manageMeta = manage.getItemMeta();
        manageMeta.setLore(manageMeta.getLore().stream().map(key -> key.replace("{level}", String.valueOf(FarmerLevel.getAllLevels().indexOf(farmer.getLevel()) +1))
                .replace("{capacity}", String.valueOf(farmer.getLevel().getCapacity()))
                .replace("{tax}", String.valueOf(farmer.getLevel().getTax()))).collect(Collectors.toList()));
        manage.setItemMeta(manageMeta);
        return manage;
    }

    /**
     * Lore replacer for manager menu status item.
     * Changing status to toggleON or toggleOFF value.
     *
     * @param status of farmer collection
     * @param player for placeholders
     * @return ItemStack of status icon
     */
    public static @NotNull ItemStack getStatusItem(int status, OfflinePlayer player) {
        ItemStack statusItem;
        String name = Main.getLangFile().getGui().getManageGui().getItems().getCloseFarmer().getName();
        List<String> lore = Main.getLangFile().getGui().getManageGui().getItems().getCloseFarmer().getLore();
        int modelData = Main.getConfigFile().getGui().getManageGuiItems().getCloseFarmer().getModelData();
        String material = Main.getConfigFile().getGui().getManageGuiItems().getCloseFarmer().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getManageGuiItems().getCloseFarmer().isHasGlow();
        statusItem = getItem(name, lore, modelData, material, hasGlow, player);
        ItemMeta meta = statusItem.getItemMeta();
        meta.setLore(meta.getLore().stream().map(key -> key.replace("{status}",
                (status == 1) ? ChatUtils.color(Main.getLangFile().getVarious().getToggleOn())
                        : ChatUtils.color(Main.getLangFile().getVarious().getToggleOff()))).collect(Collectors.toList()));
        statusItem.setItemMeta(meta);
        return statusItem;
    }

    /**
     * Creates level item which is can be in max level.
     * So crates it with checking if farmer in max level,
     * or can be upgradeable and also replacing placeholder keys
     *
     * @param farmer of region
     * @param player for placeholders
     * @return ItemStack of level icon
     */
    public static @NotNull ItemStack getLevelItem(@NotNull Farmer farmer, OfflinePlayer player){
        int level = FarmerLevel.getAllLevels().indexOf(farmer.getLevel())+1;
        long capacity = farmer.getLevel().getCapacity();
        boolean isMax = FarmerLevel.getAllLevels().indexOf(farmer.getLevel()) == FarmerLevel.getAllLevels().size()-1;
        ItemStack result;
        FarmerLevel nextLevel;
        // In max level
        if (isMax) {
            String name = Main.getLangFile().getGui().getManageGui().getItems().getMaxLevel().getName();
            List<String> lore = Main.getLangFile().getGui().getManageGui().getItems().getMaxLevel().getLore();
            int modelData = Main.getConfigFile().getGui().getManageGuiItems().getMaxLevel().getModelData();
            String material = Main.getConfigFile().getGui().getManageGuiItems().getMaxLevel().getMaterial();
            boolean hasGlow = Main.getConfigFile().getGui().getManageGuiItems().getMaxLevel().isHasGlow();
            result = getItem(name, lore, modelData, material, hasGlow, player);
            nextLevel = FarmerLevel.getAllLevels().get(level-1);
        }
        // Can upgradeable
        else {
            String name = Main.getLangFile().getGui().getManageGui().getItems().getUpgradeNext().getName();
            List<String> lore = Main.getLangFile().getGui().getManageGui().getItems().getUpgradeNext().getLore();
            int modelData = Main.getConfigFile().getGui().getManageGuiItems().getUpgradeNext().getModelData();
            String material = Main.getConfigFile().getGui().getManageGuiItems().getUpgradeNext().getMaterial();
            boolean hasGlow = Main.getConfigFile().getGui().getManageGuiItems().getUpgradeNext().isHasGlow();
            result = getItem(name, lore, modelData, material, hasGlow, player);
            nextLevel = FarmerLevel.getAllLevels().get(level);
        }

        ItemMeta meta = result.getItemMeta();
        // Max level meta lore update
        if (isMax) {
            meta.setLore(meta.getLore().stream().map(key -> key.replace("{level}", String.valueOf(level))
                    .replace("{capacity}", String.valueOf(capacity))).collect(Collectors.toList()));
        }
        // Upgradeable lore update
        else {
            meta.setLore(meta.getLore().stream().map(key -> key.replace("{level}", String.valueOf(level))
                    .replace("{capacity}", String.valueOf(capacity))
                    .replace("{max_level}", String.valueOf(FarmerLevel.getAllLevels().size()))
                    .replace("{next_level}", String.valueOf(level+1))
                    .replace("{next_capacity}", String.valueOf(nextLevel.getCapacity()))
                    .replace("{req_money}", String.valueOf(nextLevel.getReqMoney()))).collect(Collectors.toList()));
        }
        meta.setDisplayName(meta.getDisplayName().replace("{level}", String.valueOf(level)));
        result.setItemMeta(meta);
        return result;
    }

    /**
     * Buy item in BuyGui
     *
     * @param player player for placeholder
     * @return ItemStack of buy farmer icon
     */
    public static @NotNull ItemStack getBuyItem(OfflinePlayer player) {
        ItemStack result;
        String name = Main.getLangFile().getGui().getBuyGui().getItems().getBuyItem().getName();
        List<String> lore = Main.getLangFile().getGui().getBuyGui().getItems().getBuyItem().getLore();
        int modelData = Main.getConfigFile().getGui().getBuyGuiItems().getBuyItem().getModelData();
        String material = Main.getConfigFile().getGui().getBuyGuiItems().getBuyItem().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getBuyGuiItems().getBuyItem().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, player);
        ItemMeta meta = result.getItemMeta();
        meta.setLore(meta.getLore().stream().map(key -> key.replace("{price}", String.valueOf(Main.getConfigFile().getSettings().getFarmerPrice()))).collect(Collectors.toList()));
        result.setItemMeta(meta);
        return result;
    }

    /**
     * Help item for main gui
     *
     * @param player player for placeholder
     * @return ItemStack of help icon
     */
    public static @NotNull ItemStack getHelpItemForMain(OfflinePlayer player) {
        ItemStack result;
        String name = Main.getLangFile().getGui().getFarmerGui().getItems().getHelp().getName();
        List<String> lore = Main.getLangFile().getGui().getFarmerGui().getItems().getHelp().getLore();
        int modelData = Main.getConfigFile().getGui().getFarmerGuiItems().getHelp().getModelData();
        String material = Main.getConfigFile().getGui().getFarmerGuiItems().getHelp().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getFarmerGuiItems().getHelp().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, player);
        return result;
    }

    /**
     * Gets the sellAll item
     *
     * @param player player for placeholder
     * @return ItemStack of sell all item
     */
    public static @NotNull ItemStack getSellAll(OfflinePlayer player) {
        ItemStack result;
        String name = Main.getLangFile().getGui().getFarmerGui().getItems().getSellAll().getName();
        List<String> lore = Main.getLangFile().getGui().getFarmerGui().getItems().getSellAll().getLore();
        int modelData = Main.getConfigFile().getGui().getFarmerGuiItems().getSellAll().getModelData();
        String material = Main.getConfigFile().getGui().getFarmerGuiItems().getSellAll().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getFarmerGuiItems().getSellAll().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, player);
        return result;
    }

    /**
     * Help item for users gui
     *
     * @param player player for placeholder
     * @return ItemStack of help icon
     */
    public static @NotNull ItemStack getHelpItemForUsers(OfflinePlayer player) {
        ItemStack result;
        String name = Main.getLangFile().getGui().getUsersGui().getItems().getHelp().getName();
        List<String> lore = Main.getLangFile().getGui().getUsersGui().getItems().getHelp().getLore();
        int modelData = Main.getConfigFile().getGui().getUsersGuiItems().getHelp().getModelData();
        String material = Main.getConfigFile().getGui().getUsersGuiItems().getHelp().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getUsersGuiItems().getHelp().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, player);
        return result;
    }

    /**
     * User category item in manage menu
     *
     * @param player player for placeholder
     * @return ItemStack of usercategory icon
     */
    public static @NotNull ItemStack getUserCategory(OfflinePlayer player) {
        ItemStack result;
        String name = Main.getLangFile().getGui().getManageGui().getItems().getUsers().getName();
        List<String> lore = Main.getLangFile().getGui().getManageGui().getItems().getUsers().getLore();
        int modelData = Main.getConfigFile().getGui().getManageGuiItems().getUsers().getModelData();
        String material = Main.getConfigFile().getGui().getManageGuiItems().getUsers().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getManageGuiItems().getUsers().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, player);
        return result;
    }

    /**
     * moduleGuiItem in manage menu
     *
     * @param player player for placeholder
     * @return ItemStack of moduleGui icon
     */
    public static @NotNull ItemStack getModuleGuiItem(OfflinePlayer player) {
        ItemStack result;
        String name = Main.getLangFile().getGui().getManageGui().getItems().getModules().getName();
        List<String> lore = Main.getLangFile().getGui().getManageGui().getItems().getModules().getLore();
        int modelData = Main.getConfigFile().getGui().getManageGuiItems().getModules().getModelData();
        String material = Main.getConfigFile().getGui().getManageGuiItems().getModules().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getManageGuiItems().getModules().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, player);
        return result;
    }

    /**
     * addUserItem in manage menu
     *
     * @param player player for placeholder
     * @return ItemStack of adduser icon
     */
    public static @NotNull ItemStack getAddUserItem(OfflinePlayer player) {
        ItemStack result;
        String name = Main.getLangFile().getGui().getUsersGui().getItems().getAddUser().getName();
        List<String> lore = Main.getLangFile().getGui().getUsersGui().getItems().getAddUser().getLore();
        int modelData = Main.getConfigFile().getGui().getUsersGuiItems().getAddUser().getModelData();
        String material = Main.getConfigFile().getGui().getUsersGuiItems().getAddUser().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getUsersGuiItems().getAddUser().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, player);
        return result;
    }

    /**
     * leftClickItem in geyser menu
     *
     * @return ItemStack of leftClick icon
     */
    public static @NotNull ItemStack getGeyserGuiLeftItem() {
        ItemStack result;
        String name = Main.getLangFile().getGui().getGeyserGui().getItems().getLeftClick().getName();
        List<String> lore = Main.getLangFile().getGui().getGeyserGui().getItems().getLeftClick().getLore();
        int modelData = Main.getConfigFile().getGui().getGeyserGuiItems().getLeftClick().getModelData();
        String material = Main.getConfigFile().getGui().getGeyserGuiItems().getLeftClick().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getGeyserGuiItems().getLeftClick().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, null);
        return result;
    }

    /**
     * RightClickItem in geyser menu
     *
     * @return ItemStack of rightClick icon
     */
    public static @NotNull ItemStack getGeyserGuiRightItem() {
        ItemStack result;
        String name = Main.getLangFile().getGui().getGeyserGui().getItems().getRightClick().getName();
        List<String> lore = Main.getLangFile().getGui().getGeyserGui().getItems().getRightClick().getLore();
        int modelData = Main.getConfigFile().getGui().getGeyserGuiItems().getRightClick().getModelData();
        String material = Main.getConfigFile().getGui().getGeyserGuiItems().getRightClick().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getGeyserGuiItems().getRightClick().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, null);
        return result;
    }

    /**
     * shiftRightClickItem in geyser menu
     *
     * @return ItemStack of shiftRightClick icon
     */
    public static @NotNull ItemStack getGeyserGuiShiftRightItem() {
        ItemStack result;
        String name = Main.getLangFile().getGui().getGeyserGui().getItems().getShiftRightClick().getName();
        List<String> lore = Main.getLangFile().getGui().getGeyserGui().getItems().getShiftRightClick().getLore();
        int modelData = Main.getConfigFile().getGui().getGeyserGuiItems().getShiftRightClick().getModelData();
        String material = Main.getConfigFile().getGui().getGeyserGuiItems().getShiftRightClick().getMaterial();
        boolean hasGlow = Main.getConfigFile().getGui().getGeyserGuiItems().getShiftRightClick().isHasGlow();
        result = getItem(name, lore, modelData, material, hasGlow, null);
        return result;
    }

    /**
     * TODO OLD REMOVE
     * If item has skull it gets item as head with
     * custom head data. Otherwise, check for material and
     * get item with a material.
     * @param path path of item (e. Items.storage)
     * @param file file of item (e. items.yml)
     * @return ItemStack of destination item
     */
    public static @NotNull ItemStack getItem(String path, @NotNull Config file) {
        ItemStack result;
        // If item is skull instead of material based item
        if (file.contains(path + ".skull")) {
            try {
                result = XMaterial.PLAYER_HEAD.parseItem();
                XSkull.of(result).profile(Profileable.detect(file.getString(path + ".skull"))).apply();

            } catch (Exception e) {
                result = new ItemStack(Material.STONE, 1);
            }
        }
        // If item is material based something
        else
            result = XMaterial.matchXMaterial(file.getString(path + ".material")).get().parseItem();

        ItemMeta meta = result.getItemMeta();
        if (file.contains(path + ".lore"))
            meta.setLore(file.getTextList(path + ".lore"));
        meta.setDisplayName(file.getText(path + ".name"));
        result.setItemMeta(meta);
        return result;
    }
}