package xyz.geik.farmer.integrations.economy;

import me.elementalgaming.ElementalGems.GemAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.integrations.EconomyIntegrations;

/**
 * ElementalGems economy integration class
 *
 * @author Amowny
 * @since v6-b003
 */
public class ElementalGems extends EconomyIntegrations {

    private boolean economy;

    /**
     * Constructor register event of super class
     * @param plugin
     */
    public ElementalGems(Main plugin) {
        super(plugin);
        this.economy = true;
        if (!setupEconomy()) {
            this.economy = false;
        }
    }

    private boolean setupEconomy() {
        return (this.plugin.getServer().getPluginManager().getPlugin("ElementalGems") != null);
    }

    /**
     * Withdraw player balance
     * @param player
     * @param price
     * @return price
     */
    @Override
    public double withdrawPlayer(Player player, int price) {
        if (this.economy) {
            GemAPI gemAPI = new GemAPI();
            gemAPI.removeGems(player.getUniqueId(), price);
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
        if (this.economy) {
            GemAPI gemAPI = new GemAPI();
            gemAPI.removeGems(player.getUniqueId(), price);
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
        if (this.economy) {
            GemAPI gemAPI = new GemAPI();
            gemAPI.addGems(player.getUniqueId(), price);
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
        if (this.economy) {
            GemAPI gemAPI = new GemAPI();
            gemAPI.addGems(offlinePlayer.getUniqueId(), price);
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
        if (this.economy) {
            GemAPI gemAPI = new GemAPI();
            gemAPI.addGems(player.getUniqueId(), price);
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
        if (this.economy) {
            GemAPI gemAPI = new GemAPI();
            gemAPI.addGems(offlinePlayer.getUniqueId(), price);
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
        if (this.economy) {
            GemAPI gemAPI = new GemAPI();
            return gemAPI.getGems(player.getUniqueId());
        }
        return 0;
    }
}
