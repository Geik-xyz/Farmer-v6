package xyz.geik.ciftci.Listeners.GuiListeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;

public class BuyGuiListener implements Listener {

	public BuyGuiListener(Main plugin) {
	}

	/**
	 * Buy gui listener
	 * 
	 * @param e
	 */
	@EventHandler
	public void buyGuiListener(InventoryClickEvent e) {

		if (e.getView().getTitle().contains(Manager.getText("lang", "buyFarmerGui.guiName"))) {

			Player player = (Player) e.getWhoClicked();

			e.setCancelled(true);

			if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
				return;

			if (e.getClickedInventory().getType() == InventoryType.CHEST) {

				if (e.getSlot() == 13) {

					player.closeInventory();

					if (Manager.invFull(player) == false) {

						if (Main.instance.getConfig().getBoolean("Settings.buyFarmer.feature")) {

							if (onEnableShortcut.econ.getBalance(player) >= Main.instance.getConfig()
									.getInt("Settings.buyFarmer.price")) {

								if (Main.instance.getConfig().isSet("Settings.levelBasedFarmer")
										&& Main.instance.getConfig().getBoolean("Settings.levelBasedFarmer")) {

									ItemStack egg = new ItemStack(FarmerManager.SPAWN_EGG);

									ItemMeta meta = egg.getItemMeta();

									meta.setDisplayName(Main.color(meta.getDisplayName().replace("{level}", "1")));

									egg.setItemMeta(meta);

									player.getInventory().addItem(egg);

								}

								else
									player.getInventory().addItem(FarmerManager.SPAWN_EGG);

								player.sendMessage(Manager.getText("lang", "farmerBuySuccess"));

								onEnableShortcut.econ.withdrawPlayer(player,
										Main.instance.getConfig().getInt("Settings.buyFarmer.price"));

							}

							else
								player.sendMessage(Manager.getText("lang", "notEnoughMoney")
										.replace("{money}",
												String.valueOf((int) onEnableShortcut.econ.getBalance(player)))
										.replace("{req_money}", String.valueOf(
												Main.instance.getConfig().getInt("Settings.buyFarmer.price"))));

						}

						else
							player.sendMessage(Manager.getText("lang", "thisFeatureDisabled"));

					}

					else
						player.sendMessage(Manager.getText("lang", "inventoryFull"));

				}

			}

		}
	}

}
