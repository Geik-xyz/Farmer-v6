package xyz.geik.farmer.model.user;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserData {
    private final UUID uuid;
    private final String playerName;
    private OfflinePlayer offlinePlayer;

    public static final Map<String, UUID> allPlayers = new HashMap<>();

    public static final Map<UUID, UserData> players = new HashMap<>();

    public UserData(Player player) {
        this(player.getUniqueId(), player.getName());
    }

    public UserData(UUID uuid, String playerName) {
        this.offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        this.uuid = uuid;
        this.playerName = playerName;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public void setOfflinePlayer(OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
    }

    public OfflinePlayer getOfflinePlayer() {
        return this.offlinePlayer;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public static UserData getExistPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public static UserData getPlayer(Player player) {
        if (!players.containsKey(player.getUniqueId())) {
            UserData userData = new UserData(player);
            players.put(player.getUniqueId(), userData);
            allPlayers.put(player.getName(), player.getUniqueId());
            return userData;
        }
        return players.get(player.getUniqueId());
    }

    public static UserData getOfflinePlayer(String player) {
        if (!allPlayers.containsKey(player))
            return null;
        UUID uuid = allPlayers.get(player);
        return players.get(uuid);
    }

    public static UserData addOfflinePlayer(String uuid, String playerName) {
        UUID uuid1 = UUID.fromString(uuid);
        if (!players.containsKey(uuid1)) {
            UserData userData = new UserData(uuid1, playerName);
            players.put(uuid1, userData);
            return userData;
        }
        return players.get(uuid1);
    }

}
