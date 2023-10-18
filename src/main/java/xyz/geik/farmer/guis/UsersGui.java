package xyz.geik.farmer.guis;

import de.themoep.inventorygui.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.gui.GroupItems;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.listeners.backend.ChatEvent;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;

import java.util.*;

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
        String[] userGui = Main.getLangFile().getStringList("usersGui.interface").toArray(new String[0]);
        // Inventory object
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, PlaceholderAPI.setPlaceholders(null, Main.getLangFile().getText("usersGui.guiName")), userGui);
        // Filler fills empty slots
        gui.setFiller(GuiHelper.getFiller());
        // Help icon show basic information about gui
        gui.addElement(GuiHelper.createGuiElement("usersGui.help", 'h'));
        // Both next and previous page items
        // Shown if there is another page
        // Otherwise they fill by gui filler
        // Next page item
        gui.addElement(GuiHelper.createNextPage());
        // Previous page item
        gui.addElement(GuiHelper.createPreviousPage());
        // Add user icon
        gui.addElement(new StaticGuiElement('a',
                // Adduser item
                GuiHelper.getItem("usersGui.addUser"),
                1,
                // Click event of item
                click -> {
                    // Checks if owner can add more user to farmer
                    if (User.getUserAmount(player) <= farmer.getUsers().size()) {
                        player.sendMessage(Main.getLangFile().getText("reachedMaxUser"));
                        return true;
                    }
                    // Adding player to a list for catching with ChatEvent
                    if (!ChatEvent.getPlayers().containsKey(player.getName()))
                        ChatEvent.getPlayers().put(player.getName(), farmer.getRegionID());
                    player.sendMessage(Main.getLangFile().getText("waitingInput").replace("{cancel}",
                            Main.getLangFile().getText("inputCancelWord")));
                    // Removes player from cache of ChatEvent catcher after 6 seconds
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        if (ChatEvent.getPlayers().containsKey(player.getName())) {
                            ChatEvent.getPlayers().remove(player.getName());
                            player.sendMessage(Main.getLangFile().getText("inputCancel"));
                        }
                    }, 120L);
                    gui.close();
                    return true;
                })
        );
        // User list group items
        List<User> users = new ArrayList<>(farmer.getUsers());
        // Sorts users by permissions from largest to smallest
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return FarmerPerm.getRoleId(o2.getPerm()) - FarmerPerm.getRoleId(o1.getPerm());
            }
        });
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