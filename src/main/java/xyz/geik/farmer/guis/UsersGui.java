package xyz.geik.farmer.guis;

import de.themoep.inventorygui.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.permissions.PermissionAttachmentInfo;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.gui.GroupItems;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.listeners.backend.ChatEvent;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;

import java.util.*;

public class UsersGui {

    public static void showGui(Player player, Farmer farmer) {
        String[] userGui = Main.getLangFile().getList("usersGui.interface").toArray(String[]::new);
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, Main.getLangFile().getText("usersGui.guiName"), userGui);
        gui.setFiller(GuiHelper.getFiller());
        // Help icon
        gui.addElement(GuiHelper.createGuiElement("usersGui.help", 'h'));
        gui.addElement(GuiHelper.createNextPage());
        gui.addElement(GuiHelper.createPreviosPage());
        // Add user icon
        gui.addElement(new StaticGuiElement('a',
                GuiHelper.getItem("usersGui.addUser"),
                1,
                click -> {
                    if (User.getUserAmount(player) <= farmer.getUsers().size()) {
                        player.sendMessage(Main.getLangFile().getText("reachedMaxUser"));
                        return true;
                    }
                    if (!ChatEvent.getPlayers().containsKey(player.getName()))
                        ChatEvent.getPlayers().put(player.getName(), farmer.getRegionID());
                    player.sendMessage(Main.getLangFile().getText("waitingInput").replace("{cancel}",
                            Main.getLangFile().getText("inputCancelWord")));
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        if (ChatEvent.getPlayers().containsKey(player.getName())) {
                            ChatEvent.getPlayers().remove(player.getName());
                            player.sendMessage(Main.getLangFile().getText("inputCancel"));
                        }
                    }, 200L);
                    gui.close();
                    return true;
                })
        );
        // User list group items
        List<User> users = new ArrayList<>(farmer.getUsers());
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return FarmerPerm.getRoleId(o2.getPerm()) - FarmerPerm.getRoleId(o1.getPerm());
            }
        });
        GuiElementGroup group = new GuiElementGroup('u');
        for (User user : users) {
            group.addElement(new DynamicGuiElement('d', (viewer) -> {
                return new StaticGuiElement('s',
                    GroupItems.getUserItem(user),
                    1,
                    click -> {
                        boolean response = false;
                        if (click.getType().equals(ClickType.LEFT) || click.getType().equals(ClickType.RIGHT))
                            response = User.updateUserRole(user, farmer);
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