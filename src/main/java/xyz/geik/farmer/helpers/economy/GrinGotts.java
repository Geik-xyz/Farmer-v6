package xyz.geik.farmer.helpers.economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.gestern.gringotts.Gringotts;
import org.gestern.gringotts.api.Eco;

/**
 * GrinGotts economy integration class
 *
 * @author Amowny
 * @since v6-b003
 */
public class GrinGotts implements Economy {

    private Eco economy = null;

    /**
     * Constructor register event of super class
     */
    public GrinGotts() {
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
    public double withdrawPlayer(OfflinePlayer player, long price) {
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
    public double depositPlayer(OfflinePlayer player, double price) {
        if (this.economy != null) {
            this.economy.player(player.getUniqueId()).deposit(price);
        }
        return price;
    }

    /**
     * Get player balance
     * @param player
     * @return player balance
     */
    public double getBalance(OfflinePlayer player) {
        if (this.economy != null) {
            return this.economy.player(player.getUniqueId()).balance();
        }
        return 0;
    }
}
