package xyz.geik.farmer.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.PlaceholderHelper;
import xyz.geik.farmer.helpers.gui.GroupItems;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.listeners.backend.ChatEvent;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.chat.Placeholder;
import xyz.geik.glib.shades.inventorygui.DynamicGuiElement;
import xyz.geik.glib.shades.inventorygui.GuiElementGroup;
import xyz.geik.glib.shades.inventorygui.InventoryGui;
import xyz.geik.glib.shades.inventorygui.StaticGuiElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gui of user list
 * Users can be listed in this gui
 * Also promote demote user here
 * and add new user here
 */
public class UsersGui {

    /**
     * Constructor of class
     */
    public UsersGui() {}

    /**
     * Opens gui of users
     *
     * @param player to show gui
     * @param farmer of region
     */
    public static void showGui(Player player, @NotNull Farmer farmer) {
        // Gui interface array
        String[] userGui = Main.getConfigFile().getGui().getUsersLayout().toArray(new String[0]);
        // Inventory object
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, PlaceholderHelper.parsePlaceholders(player, ChatUtils.color(Main.getLangFile().getGui().getUsersGui().getGuiName())), userGui);
        // Filler fills empty slots
        gui.setFiller(GuiHelper.getFiller(player));
        // Help icon show basic information about gui
        gui.addElement(GuiHelper.createGuiElement(GuiHelper.getHelpItemForUsers(player), 'h'));
        // Both next and previous page items
        // Shown if there is another page
        // Otherwise they fill by gui filler
        // Next page item
        gui.addElement(GuiHelper.createNextPage(player));
        // Previous page item
        gui.addElement(GuiHelper.createPreviousPage(player));
        // Add user icon
        gui.addElement(new StaticGuiElement('a',
                // Adduser item
                GuiHelper.getAddUserItem(player),
                1,
                // Click event of item
                click -> {
                    // Checks if owner can add more user to farmer
                    if (User.getUserAmount(player) <= farmer.getUsers().size()) {
                        ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getReachedMaxUser());
                        return true;
                    }
                    // Adding player to a list for catching with ChatEvent
                    if (!ChatEvent.getPlayers().containsKey(player.getName()))
                        ChatEvent.getPlayers().put(player.getName(), farmer.getRegionID());
                    ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getWaitingInput(),
                            new Placeholder("{cancel}", Main.getLangFile().getVarious().getInputCancelWord()));
                    // Removes player from cache of ChatEvent catcher after 6 seconds
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        if (ChatEvent.getPlayers().containsKey(player.getName())) {
                            ChatEvent.getPlayers().remove(player.getName());
                            ChatUtils.sendMessage(player, Main.getLangFile().getMessages().getInputCancel());
                        }
                    }, 120L);
                    gui.close();
                    return true;
                })
        );
        // User list group items
        List<User> users = new ArrayList<>(farmer.getUsers());
        // Sorts users by permissions from largest to smallest
        Collections.sort(users, (o1, o2)
                -> FarmerPerm.getRoleId(o2.getPerm()) - FarmerPerm.getRoleId(o1.getPerm()));
        // Group of users
        GuiElementGroup group = new GuiElementGroup('u');
        // foreach every user
        for (User user : users) {
            group.addElement(new DynamicGuiElement('d', (viewer) -> {
                return new StaticGuiElement('s',
                    // User head item
                    GroupItems.getUserItem(user),
                    1,
                    // Click player head event
                    click -> {
                        boolean response = false;
                        // Left or right click for demote or promote user role
                        if (click.getType().equals(ClickType.LEFT) || click.getType().equals(ClickType.RIGHT))
                            response = User.updateUserRole(user, farmer);
                        // Shift right click for remove user
                        else if (click.getType().equals(ClickType.SHIFT_RIGHT)) {
                            response = farmer.removeUser(user);
                            if (response) {
                                gui.destroy();
                                return true;
                            }
                        }
                        if (response)
                            gui.draw();
                        return true;
                    }
                );
            }));
        }
        gui.addElement(group);
        gui.show(player);
    }
}