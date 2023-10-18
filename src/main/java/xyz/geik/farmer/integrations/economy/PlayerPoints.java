package xyz.geik.farmer.integrations.economy;

import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.EconomyIntegrations;

/**
 * PlayerPoints economy integration class
 *
 * @author Amowny
 * @since v6-b003
 */
public class PlayerPoints extends EconomyIntegrations {

    private PlayerPointsAPI economy = null;

    /**
     * Constructor register event of super class
     * @param plugin
     */
    public PlayerPoints(Main plugin) {
        super(plugin);
        setupEconomy();
    }

    private boolean setupEconomy() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlayerPoints"))
            return false;
        this.economy = org.black_ixx.playerpoints.PlayerPoints.getInstance().getAPI();
        return (this.economy != null);
    }

    /**
     * Withdraw player balance
     * @param player
     * @param price
     * @return price
     */
    @Override
    public double withdrawPlayer(Player player, int price) {
        if (this.economy != null) {
            this.economy.take(player.getUniqueId(), price);
        }
        return price;
    }

    /**
     * Withdraw player balance
     * @param player
     * @param price
     * @return price
     */
    @Override
    public double withdrawPlayer(Player player, long price) {
        if (this.economy != null) {
            this.economy.take(player.getUniqueId(), (int) price);
        }
        return price;
    }

    /**
     * Deposit player balance
     * @param player
     * @param price
     * @return price
     */
    @Override
    public double depositPlayer(Player player, int price) {
        if (this.economy != null) {
            this.economy.give(player.getUniqueId(), price);
        }
        return price;
    }

    /**
     * Deposit player balance
     * @param offlinePlayer
     * @param price
     * @return price
     */
    @Override
    public double depositPlayer(OfflinePlayer offlinePlayer, int price) {
        if (this.economy != null) {
            this.economy.give(offlinePlayer.getUniqueId(), price);
        }
        return price;
    }

    /**
     * Deposit player balance
     * @param player
     * @param price
     * @return price
     */
    @Override
    public double depositPlayer(Player player, double price) {
        if (this.economy != null) {
            this.economy.give(player.getUniqueId(), (int) price);
        }
        return price;
    }

    /**
     * Deposit player balance
     * @param offlinePlayer
     * @param price
     * @return price
     */
    @Override
    public double depositPlayer(OfflinePlayer offlinePlayer, double price) {
        if (this.economy != null) {
            this.economy.give(offlinePlayer.getUniqueId(), (int) price);
        }
        return price;
    }

    /**
     * Get player balance
     * @param player
     * @return player balance
     */
    @Override
    public double getBalance(Player player) {
        if (this.economy != null) {
            return this.economy.look(player.getUniqueId());
        }
        return 0;
    }
}
