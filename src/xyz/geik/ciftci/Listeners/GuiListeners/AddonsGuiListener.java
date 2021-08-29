package xyz.geik.ciftci.Listeners.GuiListeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Cache.Farmer;

public class AddonsGuiListener implements Listener {

	public AddonsGuiListener(Main plugin) {
	}

	/**
	 * Addon gui listener
	 * 
	 * @param e
	 */
	@EventHandler
	public void addonGuiListener(InventoryClickEvent e) {

		if (e.getView().getTitle().contains(Manager.getText("lang", "addonGui.guiName"))) {

			Player player = (Player) e.getWhoClicked();

			e.setCancelled(true);

			if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
				return;

			if (e.getClickedInventory().getType() == InventoryType.CHEST) {

				String uuid = ApiFun.getIslandOwnerUUID(player.getLocation());

				Farmer farmer = FarmerManager.farmerCache.get(uuid).getStorage();

				if (e.getSlot() == 10 && Main.instance.getConfig().getBoolean("AddonSettings.autoSell.feature")
						&& Main.autoSell) {

					if (e.getWhoClicked().hasPermission("ciftci.autosell")) {

						boolean status = farmer.getAutoSell();

						if (status)
							status = false;

						else
							status = true;

						DatabaseQueries.toggleAutoSell(uuid, status);

						farmer.setAutoSell(status);

						FarmerManager.farmerCache.get(uuid).updateFarmerStorage(farmer);

						String stat = Manager.getText("lang", "toggleON");

						if (!status)
							stat = Manager.getText("lang", "toggleOFF");

						player.sendMessage(Manager.getText("lang", "autoSellToggle").replace("{status}", stat));

					}

					else {

						player.closeInventory();

						player.sendMessage(Manager.getText("lang", "dontHavePermission"));

						return;

					}

				}

				// AutoCollect toggle
				else if (e.getSlot() == 16 && Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.feature")
						&& Main.autoCollector) {

					if (e.getWhoClicked().hasPermission("ciftci.autocollect")) {

						boolean status = farmer.getAutoCollect();

						if (status)
							status = false;

						else
							status = true;

						DatabaseQueries.toggleAutoCollect(uuid, status);

						farmer.setAutoCollect(status);

						FarmerManager.farmerCache.get(uuid).updateFarmerStorage(farmer);

						String stat = Manager.getText("lang", "toggleON");

						if (!status)
							stat = Manager.getText("lang", "toggleOFF");

						player.sendMessage(Manager.getText("lang", "autoCollectToggle").replace("{status}", stat));

						if (status && !FarmerManager.farmerIdMap.contains(farmer.getOwnerUUID()))
							FarmerManager.farmerIdMap.add(farmer.getOwnerUUID());

						else if (!status && FarmerManager.farmerIdMap.contains(farmer.getOwnerUUID()))
							FarmerManager.farmerIdMap.remove(farmer.getOwnerUUID());

					}

					else {

						player.closeInventory();

						player.sendMessage(Manager.getText("lang", "dontHavePermission"));

						return;

					}

				}

				// SpawnerKiller
				else if (Main.spawnerKiller && e.getSlot() == 13) {

					if (e.getWhoClicked().hasPermission("ciftci.spawnerkill")) {

						boolean status = farmer.getSpawnerKill();

						if (status)
							status = false;

						else
							status = true;

						DatabaseQueries.toggleSpawnerKiller(uuid, status);

						farmer.setSpawnerKill(status);

						FarmerManager.farmerCache.get(uuid).updateFarmerStorage(farmer);

						String stat = Manager.getText("lang", "toggleON");

						if (!status)
							stat = Manager.getText("lang", "toggleOFF");

						player.sendMessage(Manager.getText("lang", "spawnerKillerToggle").replace("{status}", stat));

					}

					else {

						player.closeInventory();

						player.sendMessage(Manager.getText("lang", "dontHavePermission"));

						return;

					}

				}

				else
					return;

				player.closeInventory();

			}

		}
	}

}
