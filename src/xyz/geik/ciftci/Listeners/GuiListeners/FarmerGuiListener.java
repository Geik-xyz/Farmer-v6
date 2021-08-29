package xyz.geik.ciftci.Listeners.GuiListeners;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Cache.ConfigItems;
import xyz.geik.ciftci.Utils.Gui.FarmerInventoryGUI;
import xyz.geik.ciftci.Utils.Gui.FarmerManagmentGUI;
import xyz.geik.ciftci.Utils.Gui.GuiManager;

public class FarmerGuiListener implements Listener {

	public FarmerGuiListener(Main plugin) {
	}

	/**
	 * Farmer gui listener
	 * 
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void guiListener(InventoryClickEvent e) {

		if (e.getView().getTitle().contains(Manager.getText("lang", "Gui.guiName"))) {

			Player player = (Player) e.getWhoClicked();

			e.setCancelled(true);

			if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR))
				return;

			if (e.getClickedInventory().getType() == InventoryType.CHEST) {

				List<ItemStack> items = FarmerManager.CONFIG_TO_GUI;

				final String uuid = String.valueOf(ApiFun.getIslandOwnerUUID(player.getLocation()));

				String owner = uuid;

				if (onEnableShortcut.USE_OWNER)
					owner = ApiFun.getOwnerViaID(uuid).getUniqueId().toString();

				String[] pageShortcut = e.getView().getTitle().split(" ");

				String page = pageShortcut[pageShortcut.length - 1].substring(1,
						pageShortcut[pageShortcut.length - 1].length());

				if (Manager.isNumeric(page)) {

					if (items.size() == 0)
						player.closeInventory();

					else {

						if ((String.valueOf(player.getUniqueId()).equalsIgnoreCase(owner) || player.isOp())
								&& e.getSlot() == 4)
							Bukkit.getScheduler().runTask(Main.instance, () -> {
								FarmerManagmentGUI.createGui(player, false);
							});

						if (e.getSlot() >= 10 && e.getSlot() <= 43) {

							int click = getClickSlot(e.getSlot(), page);

							ConfigItems configItem = FarmerManager.STORED_ITEMS.get(items.get(click));

							ItemStack item = new ItemStack(items.get(click));

							int damage = configItem.getDamage();

							HashMap<ConfigItems, Integer> values = FarmerManager.farmerCache.get(uuid).getItemValues();

							int count = values.get(configItem);

							double price = configItem.getPrice();

							if (damage != 0)
								item.setDurability((short) damage);

							item.setAmount(item.getMaxStackSize());

							int farmerLevel = FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel();
							int taxRate = Main.instance.getConfig().getInt("tax.taxRate");
							if (Main.instance.getConfig().isSet("FarmerLevels." + farmerLevel + ".taxRate"))
								taxRate = Main.instance.getConfig().getInt("FarmerLevels." + farmerLevel + ".taxRate");

							if (Main.instance.getConfig().isSet("DetailedSettings.taxOnTake")
									&& Main.instance.getConfig().getBoolean("DetailedSettings.taxOnTake")
									&& (e.getClick().equals(ClickType.RIGHT) || e.getClick().equals(ClickType.LEFT)))
								count = count - (count * (taxRate / 100));

							// 1 Stack al
							if (count > 0) {

								if (e.getClick().equals(ClickType.LEFT)) {

									if (Manager.invFull(player) == true) {

										player.sendMessage(Manager.getText("lang", "inventoryFull"));

										player.closeInventory();

										return;

									}

									if (count >= item.getMaxStackSize())
										count = count - item.getMaxStackSize();

									else {

										item.setAmount(count);

										count = 0;
									}

									if (Main.instance.getConfig().isSet("DetailedSettings.taxOnTake")
											&& Main.instance.getConfig().getBoolean("DetailedSettings.taxOnTake"))
										item.setAmount(item.getAmount() - ((item.getAmount() * taxRate)) / 100);

									syncItemAdderToPlayer(player, item);

									values.replace(configItem, count);

									FarmerManager.farmerCache.get(uuid).setItemValues(values);

								}

								// Hepsini al
								else if (e.getClick().equals(ClickType.RIGHT)) {

									if (Manager.invFull(player) == true) {

										player.sendMessage(Manager.getText("lang", "inventoryFull"));

										player.closeInventory();

										return;

									}

									int playerSpace = getEmptySlotsAmount(player);

									int newCount = count;

									if (playerSpace * item.getMaxStackSize() < count) {

										newCount = playerSpace * item.getMaxStackSize();

										count = count - playerSpace * item.getMaxStackSize();

										addItemToPlayer(newCount, item, player, taxRate);

									}

									else
										count = 0;

									addItemToPlayer(newCount, item, player, taxRate);

									values.replace(configItem, count);

									FarmerManager.farmerCache.get(uuid).setItemValues(values);

								}

								// Hepsini sat
								else if (e.getClick().equals(ClickType.SHIFT_RIGHT)) {

									if (price <= 0)
										return;

									double taxMoney = 0;

									if (taxRate > 0)
										taxMoney = ((count * price) * taxRate) / 100;

									double money = (((double) count) * price) - taxMoney;

									player.sendMessage(Manager.getText("lang", "sellComplete")
											.replace("{tax}", Manager.roundDouble(taxMoney, 2))
											.replace("{money}", Manager.roundDouble(money, 2)));

									FarmerManager.sellModifier(uuid, player, money, values, configItem, taxMoney, false,
											owner);

								}

							}

							player.closeInventory();

						}

						else if (e.getSlot() == 45 && e.getCurrentItem().equals(GuiManager.previousPage())) {

							player.closeInventory();

							FarmerInventoryGUI.createGui(player, Integer.valueOf(page) - 1, uuid);

						}

						else if (e.getSlot() == 53 && e.getCurrentItem().equals(GuiManager.nextPage())) {

							player.closeInventory();

							FarmerInventoryGUI.createGui(player, Integer.valueOf(page) + 1, uuid);

						}

					}

				}

				else
					return;

			}

		}

	}

	private void syncItemAdderToPlayer(Player player, ItemStack item) {
		player.getInventory().addItem(item);
	}

	public static void addItemToPlayer(int amount, ItemStack item, Player player, int taxRate) {

		int calculatedAmount = amount;

		int maxAmount = item.getMaxStackSize();

		if (amount > maxAmount) {

			if (Main.instance.getConfig().isSet("DetailedSettings.taxOnTake")
					&& Main.instance.getConfig().getBoolean("DetailedSettings.taxOnTake"))
				item.setAmount(maxAmount - ((maxAmount * taxRate) / 100));

			else
				item.setAmount(maxAmount);

			for (int i = 0; i <= amount / maxAmount; i++) {

				if (calculatedAmount > maxAmount) {

					if (Main.instance.getConfig().isSet("DetailedSettings.taxOnTake")
							&& Main.instance.getConfig().getBoolean("DetailedSettings.taxOnTake"))
						item.setAmount(maxAmount - ((maxAmount * taxRate) / 100));

					else
						item.setAmount(maxAmount);

					player.getInventory().addItem(item);

					calculatedAmount = calculatedAmount - maxAmount;

				}

				else {

					if (Main.instance.getConfig().isSet("DetailedSettings.taxOnTake")
							&& Main.instance.getConfig().getBoolean("DetailedSettings.taxOnTake"))
						item.setAmount(calculatedAmount - ((calculatedAmount * taxRate) / 100));

					else
						item.setAmount(calculatedAmount);

					player.getInventory().addItem(item);

					calculatedAmount = 0;

					break;

				}

			}

		}

		else {

			if (Main.instance.getConfig().isSet("DetailedSettings.taxOnTake")
					&& Main.instance.getConfig().getBoolean("DetailedSettings.taxOnTake"))
				item.setAmount(calculatedAmount - ((calculatedAmount * taxRate) / 100));

			else
				item.setAmount(calculatedAmount);

			player.getInventory().addItem(item);

		}

	}

	public static int getEmptySlotsAmount(Player player) {
		int count = 0;
		for (ItemStack i : player.getInventory()) {
			if (i == null) {
				count++;
			} else
				continue;
		}
		return count;
	}

	private int getClickSlot(int slot, String page) {

		int click = 0;

		if (slot >= 10 && slot <= 16)
			click = slot - 10;

		else if (slot >= 19 && slot <= 25)
			click = slot - 12; // 7

		else if (slot >= 28 && slot <= 34)
			click = slot - 14; // 14

		else if (slot >= 37 && slot <= 43)
			click = slot - 16; // 21

		else
			return 0;

		return ((Integer.valueOf(page) * 28) - 28) + click;

	}

}
