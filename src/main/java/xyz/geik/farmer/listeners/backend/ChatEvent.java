package xyz.geik.farmer.listeners.backend;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.guis.UsersGui;
import xyz.geik.farmer.model.Farmer;

import java.util.HashMap;

public class ChatEvent implements Listener {

    private static HashMap<String, String> players = new HashMap<>();
    public static HashMap<String, String> getPlayers() { return players; }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e) {
        // TODO PERM CHECK FOR USER AMOUNT
        if (getPlayers().keySet().contains(e.getPlayer().getName())) {
            String msg = e.getMessage();
            e.setCancelled(true);
            // Cancel Task
            if (msg.equalsIgnoreCase(Main.getLangFile().getText("inputCancelWord"))) {
                getPlayers().remove(e.getPlayer().getName());
                e.getPlayer().sendMessage(Main.getLangFile().getText("inputCancel"));
                return;
            }
            Farmer farmer = Main.getFarmers().get(getPlayers().get(e.getPlayer().getName()));
            try {
                OfflinePlayer target = Bukkit.getOfflinePlayer(msg);
                if (!farmer.getUsers().stream().anyMatch(user -> {
                    return user.getUuid().equals(target.getUniqueId());})) {
                    farmer.addUser(target.getUniqueId(), msg);
                    e.getPlayer().sendMessage(Main.getLangFile().getText("userAdded")
                            .replace("{player}", msg));
                }
                else
                    e.getPlayer().sendMessage(Main.getLangFile().getText("userAlreadyExist")
                            .replace("{player}", msg));
            }
            catch (NullPointerException e1) {
                e.getPlayer().sendMessage(Main.getLangFile().getText("userCouldntFound"));
            }
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                UsersGui.showGui(e.getPlayer(), farmer);
            });
            getPlayers().remove(e.getPlayer().getName());
        }
    }
}
