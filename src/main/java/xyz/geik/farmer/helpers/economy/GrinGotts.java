package xyz.geik.farmer.helpers.economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.gestern.gringotts.Gringotts;
import org.gestern.gringotts.api.Eco;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.EconomyManager;

/**
 * GrinGotts economy integration class
 *
 * @author Amowny
 * @since v6-b003
 */
public class GrinGotts extends EconomyManager {

    private Eco economy = null;

    /**
     * Constructor register event of super class
     * @param plugin
     */
    public GrinGotts(Main plugin) {
        super(plugin);
        setupEconomy();
    }

    private boolean setupEconomy() {
        if (!Bukkit.getPluginManager().isPluginEnabled("GrinGotts"))
            return false;
        this.economy = Gringotts.instance.getEco();
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
            this.economy.player(player.getUniqueId()).withdraw(price);
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
            this.economy.player(player.getUniqueId()).withdraw(price);
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
            this.economy.player(player.getUniqueId()).deposit(price);
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
            this.economy.player(offlinePlayer.getUniqueId()).deposit(price);
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
            this.economy.player(player.getUniqueId()).deposit(price);
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
            this.economy.player(offlinePlayer.getUniqueId()).deposit(price);
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
            return this.economy.player(player.getUniqueId()).balance();
        }
        return 0;
    }
}
