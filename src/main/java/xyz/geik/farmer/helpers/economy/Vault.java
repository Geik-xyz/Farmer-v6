package xyz.geik.farmer.helpers.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.geik.farmer.Main;

/**
 * Vault economy integration class
 *
 * @author geik
 * @since b000
 */
public class Vault implements xyz.geik.farmer.helpers.economy.Economy {

    private Economy economy = null;

    /**
     * Constructor register event of super class
     */
    public Vault() {
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
    public double withdrawPlayer(OfflinePlayer player, long price) {
        if (this.economy != null) {
            this.economy.withdrawPlayer(player, price);
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
            this.economy.depositPlayer(player, price);
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
            this.economy.getBalance(player);
        }
        return 0;
    }
}
