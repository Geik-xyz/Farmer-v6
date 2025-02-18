package xyz.geik.farmer.listeners.backend;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FastPlayerLookup implements Listener {
    private static final Map<String, UUID> onlinePlayers = new ConcurrentHashMap<>();
    private static final Map<String, UUID> recentPlayers = new ConcurrentHashMap<>();
    private static final int RECENT_CACHE_SIZE = 100;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        onlinePlayers.put(player.getName().toLowerCase(), player.getUniqueId());
        recentPlayers.put(player.getName().toLowerCase(), player.getUniqueId());
        trimRecentCache();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        onlinePlayers.remove(event.getPlayer().getName().toLowerCase());
    }

    public static UUID lookupPlayer(String name) {
        String lowercaseName = name.toLowerCase();
        UUID onlineUUID = onlinePlayers.get(lowercaseName);
        if (onlineUUID != null) {
            return onlineUUID;
        }
        UUID recentUUID = recentPlayers.get(lowercaseName);
        if (recentUUID != null) {
            return recentUUID;
        }
        Player player = Bukkit.getPlayerExact(name);
        if (player != null) {
            UUID uuid = player.getUniqueId();
            recentPlayers.put(lowercaseName, uuid);
            trimRecentCache();
            return uuid;
        }

        return null;
    }

    private static void trimRecentCache() {
        if (recentPlayers.size() > RECENT_CACHE_SIZE) {
            recentPlayers.clear();
        }
    }

    public static void addToRecentCache(String name, UUID uuid) {
        recentPlayers.put(name.toLowerCase(), uuid);
        trimRecentCache();
    }
}