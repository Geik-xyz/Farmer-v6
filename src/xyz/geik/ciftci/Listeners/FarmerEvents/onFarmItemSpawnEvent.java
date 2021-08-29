package xyz.geik.ciftci.Listeners.FarmerEvents;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.songoda.ultimatestacker.UltimateStacker;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.API.Events.FarmItemSpawnEvent;
import xyz.geik.ciftci.API.Events.FarmerAutoSellEvent;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Cache.ConfigItems;

public class onFarmItemSpawnEvent implements Listener {

	public Main plugin;

	public onFarmItemSpawnEvent(Main plugin) {
		this.plugin = plugin;
	}

	/**
	 * Farm item spawn event
	 * 
	 * @param e
	 */
	@EventHandler
	public void onFarmItemSpawn(ItemSpawnEvent e) {

		try {

			if (Main.isShutdowned)
				return;

			if (onEnableShortcut.playerDropCancel && e.getEntity().getPickupDelay() >= 39)
				return;

			if (FarmerManager.WORLDS.contains(e.getEntity().getWorld().getName())) {

				ItemStack item = new ItemStack(e.getEntity().getItemStack());

				item.setAmount(1);

				if (FarmerManager.STORED_ITEMS.keySet().contains(item)) {

					ConfigItems configItem = FarmerManager.STORED_ITEMS.get(item);

					String owner = ApiFun.getIslandOwnerUUID(e.getLocation()).toString();

					if (owner == null)
						return;

					if (!FarmerManager.farmerExclude.contains(owner)) {

						HashMap<ConfigItems, Integer> values = FarmerManager.farmerCache.get(owner).getItemValues();

						int level = FarmerManager.farmerCache.get(owner).getStorage().getFarmerLevel();

						int capacity = Main.instance.getConfig().getInt("FarmerLevels." + level + ".Capacity");

						int farmerID = FarmerManager.farmerCache.get(owner).getStorage().getFarmerID();

						int newAmount = values.get(configItem) + e.getEntity().getItemStack().getAmount();

						if (values.get(configItem) != null && newAmount <= capacity && farmerID != 96456) {

							FarmItemSpawnEvent event = new FarmItemSpawnEvent(owner,
									FarmerManager.farmerCache.get(owner), e.getEntity().getItemStack());

							Bukkit.getPluginManager().callEvent(event);

							if (!event.isCancelled()) {

								e.setCancelled(true);

								boolean isNatural = e.getEntity().getPickupDelay() >= 39;

								setItem(owner, e.getEntity().getItemStack().getAmount(), configItem, values,
										e.getEntity(), isNatural);

							}

						}

						else if (newAmount >= capacity && farmerID != 96456 && Main.autoSell) {

							if (FarmerManager.farmerCache.get(owner).getStorage().getAutoSell()
									&& Main.instance.getConfig().getBoolean("AddonSettings.autoSell.feature")) {

								String uuid = owner;

								if (onEnableShortcut.USE_OWNER)
									uuid = ApiFun.getOwnerViaID(owner).getUniqueId().toString();

								double price = configItem.getPrice();

								if (price <= 0)
									return;

								int taxRate = Main.instance.getConfig().getInt("tax.taxRate");

								if (Main.instance.getConfig().isSet("FarmerLevels." + level + ".taxRate"))
									taxRate = Main.instance.getConfig().getInt("FarmerLevels." + level + ".taxRate");

								double taxMoney = 0;

								if (taxRate > 0)
									taxMoney = ((newAmount * price) * taxRate) / 100;

								double money = (((double) newAmount) * price) - taxMoney;

								FarmerAutoSellEvent event = new FarmerAutoSellEvent(owner, configItem, taxMoney, money,
										((double) newAmount) * price, FarmerManager.farmerCache.get(owner));

								Bukkit.getPluginManager().callEvent(event);

								if (!event.isCancelled()) {

									e.setCancelled(true);

									FarmerManager.sellModifier(owner, null, money, values, configItem, taxMoney, true,
											uuid);

								}

							}

						}

					}

				}

				else
					return;

			}

		}

		catch (NullPointerException e1) {
		}

	}

	private static void setItem(String owner, int amount, ConfigItems toCheck, HashMap<ConfigItems, Integer> values,
			Item entity, boolean playerDrop) {

		if (Bukkit.getPluginManager().getPlugin("UltimateStacker") != null) {

			if (!playerDrop)
				values.replace(toCheck,
						values.get(toCheck) + (UltimateStacker.getActualItemAmount(entity) * Main.multiplier));
			else
				values.replace(toCheck, values.get(toCheck) + UltimateStacker.getActualItemAmount(entity));

			FarmerManager.farmerCache.get(owner).setItemValues(values);

		}

		else if (Bukkit.getPluginManager().getPlugin("WildStacker") != null) {

			if (!playerDrop)
				values.replace(toCheck, values.get(toCheck) + (WildStackerAPI.getItemAmount(entity) * Main.multiplier));
			else
				values.replace(toCheck, values.get(toCheck) + WildStackerAPI.getItemAmount(entity));

			FarmerManager.farmerCache.get(owner).setItemValues(values);

		}

		else {

			if (!playerDrop)
				values.replace(toCheck, values.get(toCheck) + (amount * Main.multiplier));

			else
				values.replace(toCheck, values.get(toCheck) + amount);

			FarmerManager.farmerCache.get(owner).setItemValues(values);

		}

	}

}
