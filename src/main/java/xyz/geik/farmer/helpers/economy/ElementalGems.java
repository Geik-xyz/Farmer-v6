package xyz.geik.farmer.helpers.economy;

import me.elementalgaming.ElementalGems.GemAPI;
import org.bukkit.OfflinePlayer;
import xyz.geik.farmer.Main;

/**
 * ElementalGems economy integration class
 *
 * @author Amowny
 * @since v6-b003
 */
public class ElementalGems implements Economy {

    private boolean economy;

    /**
     * Constructor register event of super class
     */
    public ElementalGems() {
        this.economy = true;
        if (!setupEconomy()) {
            this.economy = false;
        }
    }

    private boolean setupEconomy() {
        return (Main.getInstance().getServer().getPluginManager().getPlugin("ElementalGems") != null);
    }

    /**
     * Withdraw player balance
     * @param player
     * @param price
     * @return price
     */
    @Override
    public double withdrawPlayer(OfflinePlayer player, long price) {
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
    public double depositPlayer(OfflinePlayer player, double price) {
        if (this.economy) {
            GemAPI gemAPI = new GemAPI();
            gemAPI.addGems(player.getUniqueId(), price);
        }
        return price;
    }

    /**
     * Get player balance
     * @param player
     * @return player balance
     */
    public double getBalance(OfflinePlayer player) {
        if (this.economy) {
            GemAPI gemAPI = new GemAPI();
            return gemAPI.getGems(player.getUniqueId());
        }
        return 0;
    }
}
