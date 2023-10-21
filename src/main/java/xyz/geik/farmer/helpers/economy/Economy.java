package xyz.geik.farmer.helpers.economy;

import org.bukkit.OfflinePlayer;

/**
 * Interface class for economy hook
 * Which getting player balance or withdraw/deposit money
 * for necessary sections.
 *
 * @author poyrazinan, Amowny
 * @since v6-b003
 */
public interface Economy {

    /**
     * Withdraws money from player
     * @param player target player
     * @param price amount of money
     */
    double withdrawPlayer(OfflinePlayer player, long price);

    /**
     * Deposits money to offline player
     * @param player target player
     * @param price amount of money
     */
    double depositPlayer(OfflinePlayer player, double price);

    /**
     * Gets player balance
     * @param player target player
     * @return player balance
     */
    double getBalance(OfflinePlayer player);
}
