package xyz.geik.farmer.model.inventory;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import xyz.geik.farmer.model.FarmerLevel;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class FarmerInv {

    // default items which farmer can take
    public static List<FarmerItem> defaultItems = new ArrayList<>();

    // stocked items farmer has
    private List<FarmerItem> items;
    private long capacity;

    /**
     * Farmer inv which contains items set.
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
        items = new ArrayList<>(defaultItems);
        capacity = FarmerLevel.getAllLevels().get(0).getCapacity();
    }

    // TODO Description
    public FarmerItem getStockedItem(XMaterial material) {
        return items.stream().filter(item -> (item.getMaterial().isSimilar(material.parseItem()))).findFirst().get();
    }

    // TODO Description
    public static FarmerItem getDefaultItem(XMaterial material) {
        return defaultItems.stream().filter(item -> (item.getMaterial().isSimilar(material.parseItem()))).findFirst().get();
    }

    // TODO Description
    public static boolean checkMaterial(ItemStack itemStack) {
        return defaultItems.stream().anyMatch(item -> (item.getMaterial().isSimilar(itemStack)));
    }

    /**
     * Adding item amount to stock.
     * Respects capacity and if it above capacity
     * return additional amount.
     */
    public long sumItemAmount(XMaterial material, long amount) {
        FarmerItem item = getStockedItem(material);
        long summed = item.getAmount() + amount;
        if (summed > capacity) {
            setItemAmount(material, capacity);
            return summed-capacity;
        }
        else {
            item.sumAmount(amount);
            return 0L;
        }
    }

    // TODO Description
    public void forceSumItem(XMaterial material, long amount) {
        getStockedItem(material).sumAmount(amount);
    }

    /**
     * Removing x amount of item from stock.
     * Respects 0 if amount bigger then stock.
     * And return the abs of negative number.
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

    // TODO Description
    public void setItemAmount(XMaterial material, long amount) {
        getStockedItem(material).setAmount(amount);
    }
}