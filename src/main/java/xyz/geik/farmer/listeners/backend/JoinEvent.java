package xyz.geik.farmer.listeners.backend;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.model.user.UserData;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoinEvent(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UserData userData = UserData.getPlayer(player);
        userData.setOfflinePlayer(player);
    }

}
