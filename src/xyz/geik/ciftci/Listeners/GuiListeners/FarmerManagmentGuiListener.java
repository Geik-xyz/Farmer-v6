package xyz.geik.ciftci.Listeners.GuiListeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.API.Events.FarmerTransportEvent;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Cache.Farmer;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;
import xyz.geik.ciftci.Utils.Gui.AddonGui;
import xyz.geik.ciftci.Utils.Gui.FarmerManagmentGUI;

public class FarmerManagmentGuiListener implements Listener {

	public FarmerManagmentGuiListener(Main plugin) {
	}

	public static HashMap<String, Long> cooldown = new HashMap<String, Long>();

	/**
	 * Farmer Managment gui listener
	 * 
	 * @param e
	 */
	@EventHandler
	public void managmentGui(InventoryClickEvent e) {

		if (e.getView().getTitle().contains(Manager.getText("lang", "ManagmentGui.guiName"))) {

			Player player = (Player) e.getWhoClicked();

			e.setCancelled(true);

			if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
				return;

			if (e.getClickedInventory().getType() == InventoryType.CHEST) {

				String uuid = ApiFun.getIslandOwnerUUID(player.getLocation()).toString();

				List<String> levelValue = new ArrayList<String>(
						Main.instance.getConfig().getConfigurationSection("FarmerLevels").getKeys(false));

				Farmer farmer = FarmerManager.farmerCache.get(uuid).getStorage();

				int maxLevel = Integer.valueOf(levelValue.get(levelValue.size() - 1));

				int nextLevel = farmer.getFarmerLevel() + 1;

				int nextCapacity = Main.instance.getConfig().getInt("FarmerLevels." + nextLevel + ".Capacity");

				int nextReqMoney = Main.instance.getConfig()
						.getInt("FarmerLevels." + farmer.getFarmerLevel() + ".nextRankMoney");

				int playerMoney = (int) onEnableShortcut.econ.getBalance(player);

				int paymentMethod = FarmerManagmentGUI.isLeaderChoose(farmer.getSellingStatus());

				int farmerID = farmer.getFarmerID();
				
				boolean addonStatus = false;
				if (Main.instance.getConfig().getBoolean("AddonSettings.autoSell.feature") 
						|| Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.toggle")
						|| Main.instance.getConfig().getBoolean("AddonSettings.spawnerKiller.toggle"))
					addonStatus = true;

				// UPGRADE
				if (e.getSlot() == Manager.getInt("lang", "ManagmentGui.upgradeNext.slot")
						&& farmer.getFarmerLevel() < maxLevel) {

					if (playerMoney >= nextReqMoney) {

						if (Main.instance.getConfig().isSet("FarmerLevels." + nextLevel + ".permission")
								&& !player.hasPermission(Main.instance.getConfig()
										.getString("FarmerLevels." + nextLevel + ".permission"))) {

							player.closeInventory();

							player.sendMessage(Manager.getText("lang", "dontHavePermission"));

							return;

						}

						onEnableShortcut.econ.withdrawPlayer(player, nextReqMoney);

						farmer.setFarmerLevel(farmer.getFarmerLevel() + 1);

						FarmerManager.farmerCache.get(uuid).updateFarmerStorage(farmer);

						player.sendMessage(Manager.getText("lang", "levelUpgraded")
								.replace("{level}", String.valueOf(farmer.getFarmerLevel()))
								.replace("{capacity}", String.valueOf(nextCapacity)));

					}

					else
						player.sendMessage(Manager.getText("lang", "notEnoughMoney")
								.replace("{money}", String.valueOf((int) onEnableShortcut.econ.getBalance(player)))
								.replace("{req_money}", String.valueOf(nextReqMoney)));

				}

				// MaxLevel
				else if (e.getSlot() == Manager.getInt("lang", "ManagmentGui.inMaxLevel.slot")
						&& farmer.getFarmerLevel() == maxLevel)
					return;

				// RECOORDINATE
				else if (e.getSlot() == Manager.getInt("lang", "ManagmentGui.reCordinate.slot")) {

					if (cooldown.containsKey(player.getName())) {

						long secondsLeft = ((cooldown.get(player.getName()) / 1000) + 30)
								- (System.currentTimeMillis() / 1000);

						if (secondsLeft > 0) {
							String message = Main.color(
									" &6Çiftçi &8▸ &cBu kadar çabuk bu işlemi yapamazsın! (" + secondsLeft + "sn)");
							if (Manager.isSet("lang", "inCooldown"))
								message = Manager.getText("lang", "inCooldown").replace("{time}",
										String.valueOf(secondsLeft));

							player.sendMessage(message);
							return;
						}

						else
							cooldown.remove(player.getName());

					}

					if (Manager.invFull(player)) {

						player.sendMessage(Manager.getText("lang", "inventoryFull"));

						return;

					}

					if (farmerID != 96456) {

						StorageAndValues values = FarmerManager.farmerCache.get(uuid);

						FarmerTransportEvent event = new FarmerTransportEvent(uuid, values);

						Bukkit.getPluginManager().callEvent(event);

						if (!event.isCancelled()) {

							final int level = values.getStorage().getFarmerLevel();

							values.getStorage().setFarmerID(96456);

							DatabaseQueries.leaveEvent(uuid, values);

							cooldown.put(player.getName(), System.currentTimeMillis());

							FarmerManager.removeFarmerDataYML(uuid);

							FarmerManager.giveEggToPlayer(player, level);

							FarmerManager.removeCookie(uuid);

							player.sendMessage(Manager.getText("lang", "reCordinate"));

						}

					}

					else
						player.sendMessage(Manager.getText("lang", "alreadyWaitingFarmer"));

				}

				// SELLING MODE
				else if (e.getSlot() == Manager.getInt("lang", "ManagmentGui.sellingMode.slot")) {

					if (!Main.instance.getConfig().getBoolean("Settings.depositMethod.leaderChoose"))
						player.sendMessage(Manager.getText("lang", "featureDisabled"));

					else if (paymentMethod == 1) {

						FarmerManager.farmerCache.get(uuid).getStorage().setSellingStatus(0);

						player.sendMessage(Manager.getText("lang", "sellingMethodChanged").replace("{current_mode}",
								Manager.getText("lang", "sellingModeMember")));

					}

					else {

						FarmerManager.farmerCache.get(uuid).getStorage().setSellingStatus(1);

						player.sendMessage(Manager.getText("lang", "sellingMethodChanged").replace("{current_mode}",
								Manager.getText("lang", "sellingModeLeader")));

					}

				}

				// CLOSE FARMER LOOT
				else if (e.getSlot() == Manager.getInt("lang", "ManagmentGui.closeFarmer.slot")) {

					if (FarmerManager.farmerExclude.contains(uuid)) {

						FarmerManager.farmerExclude.remove(uuid);

						player.sendMessage(Manager.getText("lang", "toggleFarmer").replace("{status}",
								Manager.getText("lang", "toggleON")));

					}

					else {

						FarmerManager.farmerExclude.add(uuid);

						player.sendMessage(Manager.getText("lang", "toggleFarmer").replace("{status}",
								Manager.getText("lang", "toggleOFF")));

					}

				}

				// AutoSell Toggle
				else if (addonStatus && e.getSlot() == Manager.getInt("lang", "ManagmentGui.addons.slot")) {

					player.closeInventory();

					AddonGui.createGui(player);

				}

				else
					return;

				player.closeInventory();

			}

		}

	}

}
