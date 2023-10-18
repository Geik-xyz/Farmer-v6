package xyz.geik.farmer.integrations.economy;

import me.qKing12.RoyaleEconomy.API.APIHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.EconomyIntegrations;

/**
 * RoyaleEconomy economy integration class
 *
 * @author Amowny
 * @since v6-b003
 */
public class RoyaleEconomy extends EconomyIntegrations {

    private APIHandler economy = null;

    /**
     * Constructor register event of super class
     * @param plugin
     */
    public RoyaleEconomy(Main plugin) {
        super(plugin);
        setupEconomy();
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("RoyaleEconomy") == null) {
            return false;
        }
        economy = me.qKing12.RoyaleEconomy.RoyaleEconomy.apiHandler;
        return economy != null;
    }

    /**
     * Withdraw player balance
     * @param player
     * @param price
     * @return price
     */
    @Override
    public double withdrawPlayer(Player player, int price) {
        if (economy != null) {
            this.economy.balance.removeBalance(player.getUniqueId().toString(), price);
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
        if (economy != null) {
            this.economy.balance.removeBalance(player.getUniqueId().toString(), price);
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
        if (economy != null) {
            this.economy.balance.addBalance(player.getUniqueId().toString(), price);
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
        if (economy != null) {
            this.economy.balance.addBalance(offlinePlayer.getUniqueId().toString(), price);
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
        if (economy != null) {
            this.economy.balance.addBalance(player.getUniqueId().toString(), price);
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
        if (economy != null) {
            this.economy.balance.addBalance(offlinePlayer.getUniqueId().toString(), price);
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
        if (economy != null) {
            return this.economy.balance.getBalance(player.getUniqueId().toString());
        }
        return 0;
    }
}
