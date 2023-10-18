package xyz.geik.farmer.integrations;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.economy.*;

/**
 * Abstract class for EconomyIntegrations hook
 * Which getting player balance or withdraw/deposit money
 * for necessary sections.
 *
 * @author Amowny
 */
public abstract class EconomyIntegrations {

    /**
     * Constructor register event of super class
     */
    protected final Main plugin;

    /**
     * Constructor register event of super class
     * @param plugin Main Plugin class of integration
     */
    public EconomyIntegrations(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Withdraw money from player
     * @param player
     * @param price
     */
    public abstract double withdrawPlayer(Player player, int price);

    /**
     * Withdraw money from player
     * @param player
     * @param price
     */
    public abstract double withdrawPlayer(Player player, long price);

    /**
     * Deposit money from player
     * @param player
     * @param price
     */
    public abstract double depositPlayer(Player player, int price);

    /**
     * Deposit money from offline player
     * @param offlinePlayer
     * @param price
     */
    public abstract double depositPlayer(OfflinePlayer offlinePlayer, int price);

    /**
     * Deposit money from player
     * @param player
     * @param price
     */
    public abstract double depositPlayer(Player player, double price);

    /**
     * Deposit money from offline player
     * @param offlinePlayer
     * @param price
     */
    public abstract double depositPlayer(OfflinePlayer offlinePlayer, double price);

    /**
     * Getting player balance
     * @param player
     * @return player balance
     */
    public abstract double getBalance(Player player);

    /**
     * Catches plugin that server uses
     * and loads integration class of it.
     */
    public static void registerIntegrations() {
        if (Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("vault") || Bukkit.getPluginManager().isPluginEnabled("Vault") && Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("auto"))
            Main.setEconomyIntegrations(new Vault(Main.getInstance()));
        else if (Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("royaleeconomy") || Bukkit.getPluginManager().isPluginEnabled("RoyaleEconomy") && Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("auto"))
            Main.setEconomyIntegrations(new RoyaleEconomy(Main.getInstance()));
        else if (Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("playerpoints") || Bukkit.getPluginManager().isPluginEnabled("PlayerPoints") && Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("auto"))
            Main.setEconomyIntegrations(new PlayerPoints(Main.getInstance()));
        else if (Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("gringotts") || Bukkit.getPluginManager().isPluginEnabled("GrinGotts") && Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("auto"))
            Main.setEconomyIntegrations(new GrinGotts(Main.getInstance()));
        else if(Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("elementalgems") || Bukkit.getPluginManager().isPluginEnabled("ElementalGems") && Main.getConfigFile().getString("settings.economy").equalsIgnoreCase("auto"))
            Main.setEconomyIntegrations(new ElementalGems(Main.getInstance()));
    }
}
