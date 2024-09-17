package xyz.geik.farmer.guis;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerItemSellEvent;
import xyz.geik.farmer.api.handlers.FarmerMainGuiOpenEvent;
import xyz.geik.farmer.helpers.gui.GroupItems;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.modules.geyser.gui.GeyserGui;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.module.ModuleManager;
import xyz.geik.glib.shades.inventorygui.DynamicGuiElement;
import xyz.geik.glib.shades.inventorygui.GuiElementGroup;
import xyz.geik.glib.shades.inventorygui.InventoryGui;
import xyz.geik.glib.shades.inventorygui.StaticGuiElement;
import xyz.geik.glib.shades.xseries.XMaterial;

/**
 * Main gui of farmer
 * Player can sell, take and can open
 * manage gui in this gui if they have
 * permission to do.
 */
public class MainGui {

    /**
     * Constructor of class
     */
    public MainGui() {}

    /**
     * Gui main command
     *
     * @param player to show gui
     * @param farmer of region
     */
    public static void showGui(Player player, Farmer farmer) {
        // Array of gui interface
        String[] guiSetup = Main.getConfigFile().getGui().getFarmerLayout().toArray(new String[0]);
        // Gui object
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, PlaceholderAPI.setPlaceholders(null, Main.getLangFile().getGui().getFarmerGui().getGuiName()), guiSetup);
        // Fills empty spaces on  gui
        gui.setFiller(GuiHelper.getFiller(player));
        // Manage Icon element
        gui.addElement(new StaticGuiElement('m',
                // Manage item
                GuiHelper.getManageItemOnMain(farmer, player),
                1,
                // Event
                click -> {
                    // If player has admin perm or owner of farmer
                    if (player.hasPermission("farmer.admin")
                            || farmer.getOwnerUUID().equals(player.getUniqueId()))
                        ManageGui.showGui(player, farmer);
                    return true;
                })
        );
        // Help item
        gui.addElement(GuiHelper.createGuiElement(GuiHelper.getHelpItemForMain(player), 'h'));

        // Item group which farmer collects
        GuiElementGroup group = new GuiElementGroup('g');
        // Foreach item list
        for (FarmerItem item : farmer.getInv().getItems()) {
            // Element of grup there can x amount of i
            group.addElement(new DynamicGuiElement('i', (viewer) -> new StaticGuiElement('i',
                    GroupItems.getGroupItem(farmer, item),
                    1,
                    click -> {
                        // If player has admin perm or member of farmer
                        // Because member can take item or sell from farmer
                        // Otherwise if user has coop role then they can only
                        // Look inventory of farmer
                        if (player.hasPermission("farmer.admin") ||
                                farmer.getUsers().stream().anyMatch(user -> (
                                !user.getPerm().equals(FarmerPerm.COOP)
                                        && user.getName().equalsIgnoreCase(player.getName())))) {
                            // XMaterial check for old version
                            ItemStack cursorItem;
                            try {
                                cursorItem = click.getRawEvent().getInventory().getItem(click.getSlot());
                            }
                            catch (Exception ignored) {cursorItem = null;}
                            assert cursorItem != null;
                            XMaterial material = XMaterial.matchXMaterial(cursorItem);
                            FarmerItem slotItem = farmer.getInv().getStockedItem(material);
                            // Sells all stock of an item
                            if (click.getType().equals(ClickType.SHIFT_RIGHT)) {
                                if (slotItem.getPrice() < 0)
                                    return true;
                                // Calls FarmerItemSellEvent
                                FarmerItemSellEvent itemSellEvent = new FarmerItemSellEvent(farmer, slotItem, player);
                                Bukkit.getPluginManager().callEvent(itemSellEvent);
                            }
                            // Withdraw item
                            else {
                                // If inventory full returns
                                if (invFull(player)) {
                                    ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getInventoryFull());
                                    return true;
                                }
                                long count;
                                // Left click can only have one stack of item
                                // But if there is less than one stack
                                // Then overriding this amount to count.
                                if (click.getType().equals(ClickType.LEFT))
                                    count = (slotItem.getAmount() >= cursorItem.getMaxStackSize()) ? cursorItem.getMaxStackSize() : slotItem.getAmount();

                                // Withdraws max player can take from stocked amount
                                else if (click.getType().equals(ClickType.RIGHT)) {
                                    int playerEmpty = getEmptySlots(player) * cursorItem.getMaxStackSize();
                                    count = (slotItem.getAmount() >= playerEmpty)
                                            ? playerEmpty
                                            : slotItem.getAmount();
                                }
                                else return true;

                                if (count == 0)
                                    return true;
                                ItemStack returnItem = material.parseItem();

                                // This part was changed because in version 1.21.1, Pufferfish broke the max stack size in the addItem(stack) method
                                // and IllegalStackRemover flagged this as an illegal action.
                                int removed = 0;
                                // Store the maximum stack size in a variable to avoid calling the method multiple times
                                assert returnItem != null;
                                int maxStackSize = returnItem.getMaxStackSize();

                                // If the item does not have a stack size of 64 (for special items or custom stacks)
                                if (maxStackSize != 64) {
                                    long additional = count % maxStackSize;
                                    int fullStacks = (int) (count / maxStackSize);

                                    for (int i = 0; i < fullStacks; i++) {
                                        returnItem.setAmount(maxStackSize);
                                        player.getInventory().addItem(returnItem);
                                    }

                                    if (additional > 0) {
                                        returnItem.setAmount((int) additional);
                                        player.getInventory().addItem(returnItem);
                                    }
                                } else {
                                    // If the item has a stack size of 64 (standard for most items in Minecraft)
                                    int countReplaced = (int) count;


                                    // Keep adding stacks until either we run out of items or the inventory has no empty slots
                                    while (countReplaced > 0 && !invFull(player)) {
                                        // Determine how many items we can add in this iteration (either a full stack or the remaining amount)
                                        int removeAmount = Math.min(countReplaced, maxStackSize);
                                        countReplaced -= removeAmount; // Decrease the number of items that need to be added
                                        removed += removeAmount; // Track how many items have been successfully added

                                        // Set the current item's amount and add it to the inventory
                                        returnItem.setAmount(removeAmount);
                                        player.getInventory().addItem(returnItem);
                                    }
                                }
                                // After adding items, adjust the amount of the item in the original slot
                                if (removed > 0) {
                                    // If we successfully added any items, reduce the slot item by the amount removed
                                    slotItem.negateAmount(removed);
                                } else {
                                    // Otherwise, reduce the slot item by the entire count
                                    slotItem.negateAmount(count);
                                }
                            }
                            gui.draw();
                        }
                        return true;
                    })));
        }
        // Adding everything to gui and opening
        gui.addElement(group);
        gui.addElement(GuiHelper.createNextPage(player));
        gui.addElement(GuiHelper.createPreviousPage(player));
        FarmerMainGuiOpenEvent guiOpenEvent = new FarmerMainGuiOpenEvent(player, farmer, gui);
        Bukkit.getPluginManager().callEvent(guiOpenEvent);
        if (!guiOpenEvent.isCancelled())
            gui.show(player);
    }

    /**
     * Checks if player has slot in inventory
     *
     * @param player to be checked
     * @return boolean status of inventory can take item
     */
    private static boolean invFull(@NotNull Player player) {
        return player.getInventory().firstEmpty() == -1;
    }

    /**
     * Gets all the empty slots
     *
     * @param player to be checked
     * @return int of empty slots
     */
    private static int getEmptySlots(@NotNull Player player) {
        int count = 0;
        for (int i = 0; i <= 35; i++) {
            if (player.getInventory().getItem(i) == null
                    || player.getInventory().getItem(i).getType().equals(Material.AIR)) {
                count++;
            } else
                continue;
        }
        return count;
    }
}