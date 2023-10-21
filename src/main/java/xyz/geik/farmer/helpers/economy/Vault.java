package xyz.geik.farmer.helpers.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.EconomyManager;

/**
 * Vault economy integration class
 *
 * @author geik
 * @since b000
 */
public class Vault extends EconomyManager {

    private Economy economy = null;

    /**
     * Constructor register event of super class
     * @param plugin
     */
    public Vault(Main plugin) {
        super(plugin);
        setupEconomy();
    }

    private void setupEconomy() {
        if (Main.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = Main.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
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
            return this.economy.withdrawPlayer(player, price).amount;
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
            return this.economy.withdrawPlayer(player, price).amount;
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
            return this.economy.depositPlayer(player, price).amount;
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
            return this.economy.depositPlayer(offlinePlayer, price).amount;
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
            return this.economy.depositPlayer(player, price).amount;
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
            return this.economy.depositPlayer(offlinePlayer, price).amount;
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
            return this.economy.getBalance(player);
        }
        return 0;
    }
}
