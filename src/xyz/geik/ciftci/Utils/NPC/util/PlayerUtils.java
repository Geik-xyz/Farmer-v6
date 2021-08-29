package xyz.geik.ciftci.Utils.NPC.util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static Player getRandomOnlinePlayer() {
        return Bukkit.getOnlinePlayers().toArray(new Player[0])[new Random().nextInt(Bukkit.getOnlinePlayers().size())];
    }

    public static Player getRandomOnlinePlayer(Player exempt) {
        if (Bukkit.getOnlinePlayers().size() <= 1) return exempt;
        Player p = exempt;
        while (p == exempt) p = Bukkit.getOnlinePlayers().toArray(new Player[0])[new Random().nextInt(Bukkit.getOnlinePlayers().size())];
        return p;
    }
}