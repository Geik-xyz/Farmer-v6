package xyz.geik.ciftci.Listeners.BackEndListeners;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Crops;
import org.bukkit.material.CocoaPlant.CocoaPlantSize;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.API.FarmItemType;

@SuppressWarnings("deprecation")
public class onBlockGrowEvent implements Listener {

	public onBlockGrowEvent(Main plugin) {
	}

	/**
	 * AutoCollect listener
	 * 
	 * @param e
	 */
	@EventHandler
	public void blockGrowEvent(BlockGrowEvent e) {

		try {

			if (!Main.autoCollector)
				return;

			if (!onEnableShortcut.autoCollectWithoutFarmer && Main.isShutdowned)
				return;

			String uuid = ApiFun.getIslandOwnerUUID(e.getBlock().getLocation()).toString();

			if (uuid == null)
				return;

			int capacity = 0;

			boolean canCollect = false;
			boolean cancel = false;

			if (onEnableShortcut.autoCollectWithoutFarmer)
				canCollect = true;

			if (FarmerManager.farmerCache.containsKey(uuid) && !canCollect) {

				capacity = Main.instance.getConfig().getInt("FarmerLevels."
						+ FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel() + ".Capacity");

				canCollect = FarmerManager.farmerCache.get(uuid).getStorage().getAutoCollect();

			}

			else if (FarmerManager.farmerIdMap.contains(uuid) && !canCollect) {
				cancel = true;
				canCollect = true;
			}

			String nms = Manager.getNMSVersion();

			if (canCollect) {

				if (e.getNewState().getData() instanceof Crops
						&& !e.getNewState().getBlock().getType().name().contains("BEETROOTS")
						&& !e.getNewState().getBlock().getType().name().equalsIgnoreCase("CARROT")
						&& !e.getNewState().getBlock().getType().name().equalsIgnoreCase("POTATO")) {

					Crops c = (Crops) e.getNewState().getData();

					if (c.getState() == CropState.RIPE
							&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.WHEAT)) {

						if (cancel) {
							e.setCancelled(true);
							return;
						}

						if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
								&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
								&& !onEnableShortcut.autoCollectWithoutFarmer
								&& FarmerManager.farmerCache.get(uuid).getItemValues()
										.get(FarmerManager.STORED_ITEMS
												.get(FarmerManager.autoCollectMaterials.get(FarmItemType.WHEAT)))
										+ 1 > capacity)
							return;

						e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
								FarmerManager.autoCollectMaterials.get(FarmItemType.WHEAT));

						c.setState(CropState.GERMINATED);

						e.getNewState().setRawData(c.getData());

					}

				}

				else if (e.getNewState().getType() == Material.CACTUS
						&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.CACTUS)) {

					if (cancel) {
						e.setCancelled(true);
						return;
					}

					if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
							&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
							&& !onEnableShortcut.autoCollectWithoutFarmer
							&& FarmerManager.farmerCache.get(uuid).getItemValues()
									.get(FarmerManager.STORED_ITEMS
											.get(FarmerManager.autoCollectMaterials.get(FarmItemType.CACTUS)))
									+ 1 > capacity)
						return;

					e.setCancelled(true);

					e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
							FarmerManager.autoCollectMaterials.get(FarmItemType.CACTUS));

				}

				else if (e.getNewState().getType().name().contains("SUGAR_CANE")
						&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.SUGAR_CANE)) {

					if (cancel) {
						e.setCancelled(true);
						return;
					}

					if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
							&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
							&& !onEnableShortcut.autoCollectWithoutFarmer
							&& FarmerManager.farmerCache.get(uuid).getItemValues()
									.get(FarmerManager.STORED_ITEMS
											.get(FarmerManager.autoCollectMaterials.get(FarmItemType.SUGAR_CANE)))
									+ 1 > capacity)
						return;

					if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.requirePiston")
							&& Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.requirePiston")) {

						boolean hasPiston = pistonCheck(e.getNewState());

						if (hasPiston) {

							e.setCancelled(true);

							e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
									FarmerManager.autoCollectMaterials.get(FarmItemType.SUGAR_CANE));

						}

						else
							return;

					}

					else {

						e.setCancelled(true);

						e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
								FarmerManager.autoCollectMaterials.get(FarmItemType.SUGAR_CANE));

					}

				}

				else if (e.getNewState().getType().name().contains("COCOA") && e.getNewState().getRawData() >= 8
						&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.COCOA)) {

					if (cancel) {
						e.setCancelled(true);
						return;
					}

					if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
							&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
							&& !onEnableShortcut.autoCollectWithoutFarmer
							&& FarmerManager.farmerCache.get(uuid).getItemValues()
									.get(FarmerManager.STORED_ITEMS
											.get(FarmerManager.autoCollectMaterials.get(FarmItemType.COCOA)))
									+ 1 > capacity)
						return;

					e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
							FarmerManager.autoCollectMaterials.get(FarmItemType.COCOA));

					CocoaPlant cp = (CocoaPlant) e.getNewState().getData();

					cp.setSize(CocoaPlantSize.SMALL);

					e.getNewState().setRawData(cp.getData());

				}

				else if (e.getNewState().getType().name().contains("NETHER_WART") && e.getNewState().getRawData() == 3
						&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.NETHER_WART)) {

					if (cancel) {
						e.setCancelled(true);
						return;
					}

					if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
							&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
							&& !onEnableShortcut.autoCollectWithoutFarmer
							&& FarmerManager.farmerCache.get(uuid).getItemValues()
									.get(FarmerManager.STORED_ITEMS
											.get(FarmerManager.autoCollectMaterials.get(FarmItemType.NETHER_WART)))
									+ 1 > capacity)
						return;

					e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
							FarmerManager.autoCollectMaterials.get(FarmItemType.NETHER_WART));

					e.getNewState().setRawData((byte) 0);

				}

				else if ((((e.getNewState().getType() == Material.CARROT
						|| e.getNewState().getBlock().getType().name().equalsIgnoreCase("CARROT"))
						&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.CARROT))
						|| ((e.getNewState().getType() == Material.POTATO
								|| e.getNewState().getBlock().getType().name().equalsIgnoreCase("POTATO"))
								&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.POTATO)))
						&& e.getNewState().getRawData() == 7) {

					if (cancel) {
						e.setCancelled(true);
						return;
					}

					if (e.getNewState().getType() == Material.CARROT
							|| e.getNewState().getBlock().getType().name().equalsIgnoreCase("CARROT")) {

						if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
								&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
								&& !onEnableShortcut.autoCollectWithoutFarmer
								&& FarmerManager.farmerCache.get(uuid).getItemValues()
										.get(FarmerManager.STORED_ITEMS
												.get(FarmerManager.autoCollectMaterials.get(FarmItemType.CARROT)))
										+ 1 > capacity)
							return;

						e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
								FarmerManager.autoCollectMaterials.get(FarmItemType.CARROT));

						e.getNewState().setRawData((byte) 0);

					}

					else {

						if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
								&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
								&& !onEnableShortcut.autoCollectWithoutFarmer
								&& FarmerManager.farmerCache.get(uuid).getItemValues()
										.get(FarmerManager.STORED_ITEMS
												.get(FarmerManager.autoCollectMaterials.get(FarmItemType.POTATO)))
										+ 1 > capacity)
							return;

						e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
								FarmerManager.autoCollectMaterials.get(FarmItemType.POTATO));

						e.getNewState().setRawData((byte) 0);

					}

				}

				else if (e.getNewState().getType().name().contains("MELON")
						&& !e.getNewState().getType().name().contains("SEED")
						&& !e.getNewState().getType().name().contains("STEM")
						&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.MELON)) {

					if (cancel) {
						e.setCancelled(true);
						return;
					}

					int amount = 1;

					if (nms.contains("1_16") || nms.contains("1_15") || nms.contains("1_14") || nms.contains("1_13")) {

						if (FarmerManager.autoCollectMaterials.get(FarmItemType.MELON).getType()
								.equals(Material.MELON_SLICE))
							amount = 3;

					}

					else {

						if (FarmerManager.autoCollectMaterials.get(FarmItemType.MELON).getType().toString()
								.equalsIgnoreCase("melon"))
							amount = 3;

					}

					if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
							&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
							&& !onEnableShortcut.autoCollectWithoutFarmer
							&& FarmerManager.farmerCache.get(uuid).getItemValues()
									.get(FarmerManager.STORED_ITEMS
											.get(FarmerManager.autoCollectMaterials.get(FarmItemType.MELON)))
									+ 1 > capacity)
						return;

					if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.requirePiston")
							&& Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.requirePiston")) {

						final boolean hasPiston = pistonCheck(e.getNewState());

						if (hasPiston) {

							e.setCancelled(true);

							ItemStack toDrop = FarmerManager.autoCollectMaterials.get(FarmItemType.MELON).clone();

							toDrop.setAmount(amount);

							e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
									toDrop);

						}

						else
							return;

					}

					else {

						e.setCancelled(true);

						e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
								FarmerManager.autoCollectMaterials.get(FarmItemType.MELON));

					}

				}

				else if (e.getNewState().getType() == Material.PUMPKIN
						&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.PUMPKIN)) {

					if (cancel) {
						e.setCancelled(true);
						return;
					}

					if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
							&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
							&& !onEnableShortcut.autoCollectWithoutFarmer
							&& FarmerManager.farmerCache.get(uuid).getItemValues()
									.get(FarmerManager.STORED_ITEMS
											.get(FarmerManager.autoCollectMaterials.get(FarmItemType.PUMPKIN)))
									+ 1 > capacity)
						return;

					if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.requirePiston")
							&& Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.requirePiston")) {

						final boolean hasPiston = pistonCheck(e.getNewState());

						if (hasPiston) {

							e.setCancelled(true);

							e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
									FarmerManager.autoCollectMaterials.get(FarmItemType.PUMPKIN));

						}

						else
							return;

					}

					else {

						e.setCancelled(true);

						e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
								FarmerManager.autoCollectMaterials.get(FarmItemType.PUMPKIN));

					}

				}

				if (nms.contains("1_16") || nms.contains("1_15") || nms.contains("1_14") || nms.contains("1_13")
						|| nms.contains("1_12")) {

					if (nms.contains("1_14") || nms.contains("1_15") || nms.contains("1_16")) {

						if (e.getNewState().getType() == Material.SWEET_BERRY_BUSH
								&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.BERRIES)) {

							if (cancel) {
								e.setCancelled(true);
								return;
							}

							if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
									&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
									&& !onEnableShortcut.autoCollectWithoutFarmer
									&& FarmerManager.farmerCache.get(uuid).getItemValues()
											.get(FarmerManager.STORED_ITEMS
													.get(FarmerManager.autoCollectMaterials.get(FarmItemType.WHEAT)))
											+ 1 > capacity)
								return;

							e.setCancelled(true);

							e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
									FarmerManager.autoCollectMaterials.get(FarmItemType.BERRIES));

							return;

						}

					}

					if (e.getNewState().getData() instanceof Crops
							&& e.getNewState().getBlock().getType().name().contains("BEETROOTS")
							&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.BEETROOT)) {

						if (cancel) {
							e.setCancelled(true);
							return;
						}

						if (Main.instance.getConfig().isSet("AddonSettings.autoCollect.ignoreStock")
								&& !Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.ignoreStock")
								&& !onEnableShortcut.autoCollectWithoutFarmer
								&& FarmerManager.farmerCache.get(uuid).getItemValues()
										.get(FarmerManager.STORED_ITEMS
												.get(FarmerManager.autoCollectMaterials.get(FarmItemType.BEETROOT)))
										+ 1 > capacity)
							return;

						Crops c = (Crops) e.getNewState().getData();

						if (c.getState() == CropState.RIPE
								&& FarmerManager.autoCollectMaterials.keySet().contains(FarmItemType.WHEAT)
								&& FarmerManager.farmerCache.get(uuid).getItemValues()
										.get(FarmerManager.STORED_ITEMS
												.get(FarmerManager.autoCollectMaterials.get(FarmItemType.WHEAT)))
										+ 1 <= capacity) {

							e.getNewState().getLocation().getWorld().dropItemNaturally(e.getNewState().getLocation(),
									FarmerManager.autoCollectMaterials.get(FarmItemType.BEETROOT));

							c.setState(CropState.GERMINATED);

							e.getNewState().setRawData(c.getData());
						}

					}

				}

			}

			else if (cancel && FarmerManager.autoCollectMaterials.values().stream()
					.anyMatch(farmItem -> farmItem.getType() == e.getNewState().getBlock().getType())) {
				e.setCancelled(true);
				return;
			}

		}

		catch (NullPointerException e1) {
		}

	}

	private boolean pistonCheck(BlockState state) {

		Location loc;

		loc = state.getLocation().clone().add(0, 1, 0);
		if (loc.getBlock().getType().name().contains("PISTON_HEAD")
				|| loc.getBlock().getType().name().contains("PISTON_BASE")
				|| loc.getBlock().getType().name().contains("PISTON"))
			return true;

		else
			return false;

	}

}
