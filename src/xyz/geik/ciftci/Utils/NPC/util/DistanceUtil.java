package xyz.geik.ciftci.Utils.NPC.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import xyz.geik.ciftci.Main;

public class DistanceUtil {
    private DistanceUtil() {}

    public static boolean isInRange(Player player, Location location) {
        if(location == null || player.getWorld() != location.getWorld()) {
            return false;
        }
        return !(location.distance(player.getLocation()) > Main.NPC_RADIUS);
    }
}
