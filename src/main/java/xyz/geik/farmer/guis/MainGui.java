package xyz.geik.farmer.guis;

import de.themoep.inventorygui.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.helpers.gui.GuiHelper;
import xyz.geik.farmer.helpers.gui.GroupItems;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.model.user.FarmerPerm;

public class MainGui {

    public static void showGui(Player player, Farmer farmer) {
        String[] guiSetup = Main.getLangFile().getList("Gui.interface").toArray(String[]::new);
        InventoryGui gui = new InventoryGui(Main.getInstance(), null, Main.getLangFile().getText("Gui.guiName"), guiSetup);
        gui.setFiller(GuiHelper.getFiller());
        // Manage Icon
        gui.addElement(new StaticGuiElement('m',
                GuiHelper.getManageItemOnMain(farmer),
                1,
                click -> {
                    if (player.hasPermission("farmer.admin")
                            || farmer.getOwnerUUID().equals(player.getUniqueId()))
                        ManageGui.showGui(player, farmer);
                    return true;
                })
        );
        // Help item
        gui.addElement(GuiHelper.createGuiElement("Gui.help", 'h'));

        // Item group
        GuiElementGroup group = new GuiElementGroup('g');
        for (FarmerItem item : farmer.getInv().getItems()) {
            group.addElement(new DynamicGuiElement('i', (viewer) -> {
                return new StaticGuiElement('i',
                        GroupItems.getGroupItem(item, farmer.getLevel().getCapacity(), farmer.getLevel().getTax()),
                        1,
                        click -> {
                            if (player.hasPermission("farmer.admin") ||
                                    farmer.getUsers().stream().anyMatch(user -> (
                                    !user.getPerm().equals(FarmerPerm.COOP)
                                            && user.getName().equalsIgnoreCase(player.getName())))) {
                                Material material = click.getEvent().getCurrentItem().getType();
                                String materialName;
                                if (Main.isOldVersion())
                                    materialName = material.name() + "-" + click.getEvent().getCurrentItem().getDurability();
                                else
                                    materialName = material.name();
                                FarmerItem slotItem = farmer.getInv().getItems().stream()
                                        .filter(farmerItem -> (farmerItem.getName().equalsIgnoreCase(
                                                materialName)))
                                        .findFirst()
                                        .get();
                                if (click.getType().equals(ClickType.SHIFT_RIGHT)) {
                                    if (slotItem.getAmount() == 0)
                                        return true;
                                    double sellPrice = slotItem.getPrice()*slotItem.getAmount();
                                    double profit = (farmer.getLevel().getTax() > 0)
                                            ? sellPrice-(sellPrice*farmer.getLevel().getTax()/100)
                                            : sellPrice;
                                    double tax = (sellPrice == profit) ? 0 : sellPrice*farmer.getLevel().getTax()/100;
                                    Main.getEcon().depositPlayer(player, profit);
                                    slotItem.setAmount(0);

                                    if (Settings.depositTax)
                                        Main.getEcon()
                                                .depositPlayer(Settings.taxUser, tax);

                                    player.sendMessage(Main.getLangFile().getText("sellComplete")
                                        .replace("{money}", roundDouble(profit))
                                        .replace("{tax}", roundDouble(tax)));
                                }
                                else {
                                    if (invFull(player)) {
                                        player.sendMessage(Main.getLangFile().getText("inventoryFull"));
                                        return true;
                                    }
                                    long count;
                                    if (click.getType().equals(ClickType.LEFT))
                                        count = (slotItem.getAmount() >= 64) ? 64 : slotItem.getAmount();

                                    else if (click.getType().equals(ClickType.RIGHT)) {
                                        int playerEmpty = getEmptySlots(player) * material.getMaxStackSize();
                                        count = (slotItem.getAmount() >= playerEmpty)
                                                ? playerEmpty
                                                : slotItem.getAmount();
                                    }
                                    else return true;

                                    if (count == 0)
                                        return true;
                                    ItemStack returnItem;
                                    if (materialName.contains("-"))
                                        returnItem = new ItemStack(Material.getMaterial(materialName.split("-")[0]),
                                                (int) count, Short.parseShort(materialName.split("-")[1]));
                                    else
                                        returnItem = new ItemStack(material, (int) count);
                                    player.getInventory().addItem(returnItem);
                                    slotItem.negateAmount(count);
                                }
                                gui.draw();
                            }
                            return true;
                        });
            }));
        }
        gui.addElement(group);
        gui.addElement(GuiHelper.createNextPage());
        gui.addElement(GuiHelper.createPreviosPage());
        gui.show(player);
    }

    private static boolean invFull(Player p) {
        return p.getInventory().firstEmpty() == -1;
    }

    private static int getEmptySlots(Player player) {
        int count = 0;
        for (ItemStack i : player.getInventory()) {
            if (i == null) {
                count++;
            } else
                continue;
        }
        return count;
    }

    private static String roundDouble(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        double result = (double) tmp / factor;
        return String.valueOf( result );
    }
}