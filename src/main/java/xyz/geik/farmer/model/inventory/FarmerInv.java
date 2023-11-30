package xyz.geik.farmer.model.inventory;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.modules.production.ProductionModel;
import xyz.geik.glib.shades.xseries.XMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Farmer inventory which contains items.
 * Farmer inventory is a list of items which farmer can take.
 *
 * @author Geik
 */
@Setter
@Getter
public class FarmerInv {

    /**
     * default items which farmer can take
     */
    public static List<FarmerItem> defaultItems = new ArrayList<>();

    /**
     * stocked items farmer has
     */
    private List<FarmerItem> items = new ArrayList<>();

    /**
     * Generation cache of farmer
     * Auto flush in 15 minutes
     * Loads cache if someone open farmer inventory
     */
    private List<ProductionModel> productionModels = new ArrayList<>();

    /**
     * Checks if average production is calculated
     */
    private boolean isProductionCalculated = false;

    /**
     * Farmer stock capacity
     */
    private long capacity;

    /**
     * Farmer inv which contains items set.
     * @param items item list of inventory
     * @param capacity capacity of inventory item can hold
     */
    public FarmerInv(List<FarmerItem> items, long capacity) {
        this.items = items;
        this.capacity = capacity;
    }

    /**
     * Farmer inv which don't have any item in it.
     * Creating with default item set.
     */
    public FarmerInv() {
        items.addAll(defaultItems.stream().map(FarmerItem::clone).collect(Collectors.toList()));
        capacity = FarmerLevel.getAllLevels().get(0).getCapacity();
    }

    /**
     * Gets item from farmer inv.
     *
     * @param material
     * @return
     */
    public FarmerItem getStockedItem(XMaterial material) {
        return items.stream().filter(item -> (item.getMaterial().isSimilar(material.parseItem()))).findFirst().get();
    }

    /**
     * Gets item from default items.
     *
     * @param material
     * @return
     */
    public static @NotNull FarmerItem getDefaultItem(XMaterial material) {
        return defaultItems.stream().filter(item -> (item.getMaterial().isSimilar(material.parseItem()))).findFirst().get();
    }

    /**
     * Checks if item is in default items.
     *
     * @param itemStack
     * @return
     */
    public static boolean checkMaterial(ItemStack itemStack) {
        return defaultItems.stream().anyMatch(item -> (item.getMaterial().isSimilar(itemStack)));
    }

    /**
     * Adding item amount to stock.
     * Respects capacity and if it above capacity
     * return additional amount.
     * @param material xmaterial of item
     * @param collectedItem item of collected item
     * @return long left amount of item
     */
    public long sumItemAmount(XMaterial material, @NotNull Item collectedItem) {
        FarmerItem item = getStockedItem(material);
        long amount = collectedItem.getItemStack().getAmount();
        if (Bukkit.getPluginManager().isPluginEnabled("WildStacker"))
            amount = WildStackerAPI.getItemAmount(collectedItem);
        long canTake = capacity - item.getAmount();
        if (canTake < amount) {
            setItemAmount(material, capacity);
            return amount - canTake;
        }
        else {
            item.sumAmount(amount);
            return 0L;
        }
    }

    /**
     * Forces adding item amount to stock.
     * Doesn't respect capacity.
     *
     * @param material
     * @param amount
     */
    public void forceSumItem(XMaterial material, long amount) {
        getStockedItem(material).sumAmount(amount);
    }

    /**
     * Removing x amount of item from stock.
     * Respects 0 if amount bigger than stock.
     * And return the abs of negative number.
     * @param material xmaterial of item
     * @param amount amount of negate
     * @return long amount of abs
     */
    public long negateItemAmount(XMaterial material, long amount) {
        FarmerItem item = getStockedItem(material);
        long negated = getStockedItem(material).getAmount() - amount;
        if (negated < 0) {
            setItemAmount(material, 0);
            return Math.abs(negated);
        }
        else {
            item.negateAmount(amount);
            return 0L;
        }
    }

    /**
     * Update item amount in stock. Doesn't respect capacity.
     *
     * @param material
     * @param amount
     */
    public void setItemAmount(XMaterial material, long amount) {
        getStockedItem(material).setAmount(amount);
    }
}