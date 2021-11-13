package xyz.geik.ciftci.Listeners.FarmerEvents;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.API.Events.FarmerPlaceEvent;
import xyz.geik.ciftci.API.Events.FarmerPlacedEvent;
import xyz.geik.ciftci.API.Events.FarmerTransportedEvent;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;
import xyz.geik.ciftci.Utils.NPC.npc.impl.NPCImpl;

public class onPlaceEvent implements Listener {

	public Main plugin;

	public onPlaceEvent(Main plugin) {
		this.plugin = plugin;
	}

	/**
	 * Place event
	 * 
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlace(PlayerInteractEvent e) {

		try {

			Player player = e.getPlayer();

			ItemStack handItem = new ItemStack(player.getInventory().getItemInHand());

			if (handItem == null || handItem.getType() == null || handItem.getType() == Material.AIR
					|| !handItem.getType().equals(FarmerManager.SPAWN_EGG.getType()))
				return;

			handItem.setAmount(1);

			int farmerLevel = 0;

			if (Main.instance.getConfig().isSet("Settings.levelBasedFarmer")
					&& Main.instance.getConfig().getBoolean("Settings.levelBasedFarmer"))
				farmerLevel = Integer.valueOf(
						StringUtils.difference(Main.color(FarmerManager.SPAWN_EGG.getItemMeta().getDisplayName()),
								Main.color(e.getItem().getItemMeta().getDisplayName())).split(" ")[0]);

			if (handItem.hasItemMeta() && handItem.getItemMeta().hasDisplayName() &&
					(Main.color(handItem.getItemMeta().getDisplayName())
					.contains(Main.color(FarmerManager.SPAWN_EGG.getItemMeta().getDisplayName().replace("{level}",
							String.valueOf(farmerLevel))))
					|| Main.color(handItem.getItemMeta().getDisplayName())
							.equalsIgnoreCase(Main.color(FarmerManager.SPAWN_EGG.getItemMeta().getDisplayName())))) {

				e.setCancelled(true);

				if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

					if (FarmerManager.WORLDS.contains(player.getWorld().getName())) {

						if (ApiFun.isPlayerHasPermOnIsland(player, e.getClickedBlock().getLocation())
								|| player.isOp()) {

							String uuid = ApiFun.getIslandOwnerUUID(e.getClickedBlock().getLocation());

							if (uuid == null)
								return;

							boolean hasFarmer = DatabaseQueries.areaHasFarmer(uuid);

							Location loc = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);

							// Under Transporting
							if (!FarmerManager.farmerCache.containsKey(uuid) && hasFarmer) {

								StorageAndValues storage = DatabaseQueries.getAllValuesOfPlayer(uuid);

								if (farmerLevel != 0)
									storage.getStorage().setFarmerLevel(farmerLevel);

								FarmerTransportedEvent event = new FarmerTransportedEvent(uuid, storage,
										e.getClickedBlock().getLocation(), e.getPlayer());

								Bukkit.getPluginManager().callEvent(event);

								if (!event.isCancelled()) {

									try {

										storage.getStorage().setFarmerLocation(loc);

										Manager.descentItemAmountMainHand(e.getPlayer());

										FarmerManager.insertFarmerDataYML(uuid);

										NPCImpl npc = FarmerManager.createFarmer(storage);

										DatabaseQueries.updateFarmerLocation(uuid, loc);

										storage.getStorage().setFarmerID(npc.getEntityId());

										storage.setNPC(npc);

										FarmerManager.insertCookie(uuid, storage);

									}

									catch (NullPointerException e1) {
										e1.printStackTrace();
									}

								}

							}

							else if (!FarmerManager.farmerCache.containsKey(uuid) && !hasFarmer) {

								FarmerPlaceEvent event = new FarmerPlaceEvent(uuid, e.getClickedBlock().getLocation());

								Bukkit.getPluginManager().callEvent(event);

								if (!event.isCancelled()) {

									Manager.descentItemAmountMainHand(e.getPlayer());

									StorageAndValues values = FarmerManager.createFreshFarmer(loc, farmerLevel, player);

									FarmerPlacedEvent placed = new FarmerPlacedEvent(uuid, values, loc);

									Bukkit.getPluginManager().callEvent(placed);

								}

							}

							else {

								if (FarmerManager.farmerCache.containsKey(uuid) && !hasFarmer) {

									FarmerManager.farmerCache.remove(uuid);

									return;

								}

								player.sendMessage(Manager.getText("lang", "haveFarmerError"));

								return;

							}

							player.sendMessage(Manager.getText("lang", "placementSuccess"));

						}

						else
							player.sendMessage(Manager.getText("lang", "youAreNotInYourIsland"));

					}

					else
						player.sendMessage(Manager.getText("lang", "notInCiftciWorld"));

				}

				else if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {

					e.setCancelled(true);

					e.setUseItemInHand(Result.DENY);

				}

			}

		}

		catch (NullPointerException | NumberFormatException e1) {
			return;
		}

	}

}
