package xyz.geik.farmer.listeners.backend;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.guis.UsersGui;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.glib.chat.ChatUtils;
import xyz.geik.glib.chat.Placeholder;

import java.util.HashMap;

/**
 * PlayerChatEvent used for add user to farmer
 */
public class ChatEvent implements Listener {

    /**
     * Constructor of class
     */
    public ChatEvent() {}

    /**
     * Contains who currently adding someone to farmer
     * Which cancel player text on chat
     */
    @Getter
    private static HashMap<String, String> players = new HashMap<>();

    /**
     * Chat event listener
     *
     * @param e Listener event
     */
    @EventHandler
    public void chatEvent(@NotNull AsyncPlayerChatEvent e) {
        if (getPlayers().containsKey(e.getPlayer().getName())) {
            String msg = e.getMessage();
            e.setCancelled(true);
            // if player enter cancel word then it cancel await state.
            if (msg.equalsIgnoreCase(Main.getLangFile().getVarious().getInputCancelWord())) {
                getPlayers().remove(e.getPlayer().getName());
                ChatUtils.sendMessage(e.getPlayer(), Main.getLangFile().getMessages().getInputCancel());
                return;
            }
            Farmer farmer = FarmerManager.getFarmers().get(Main.getIntegration().getRegionID(e.getPlayer().getLocation()));
            try {
                OfflinePlayer target = Bukkit.getOfflinePlayer(msg);
                if (farmer.getUsers().stream().noneMatch(user -> user.getUuid().equals(target.getUniqueId()))) {
                    farmer.addUser(target.getUniqueId(), msg);
                    ChatUtils.sendMessage(e.getPlayer(), Main.getLangFile().getMessages().getUserAdded(),
                            new Placeholder("{player}", msg));
                }
                else
                    ChatUtils.sendMessage(e.getPlayer(), Main.getLangFile().getMessages().getUserAlreadyExist(),
                            new Placeholder("{player}", msg));
            }
            catch (NullPointerException e1) {
                ChatUtils.sendMessage(e.getPlayer(), Main.getLangFile().getMessages().getUserCouldntFound());
            }
            // Sync opens gui because this event is async
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                UsersGui.showGui(e.getPlayer(), farmer);
            });
            getPlayers().remove(e.getPlayer().getName());
        }
    }
}
