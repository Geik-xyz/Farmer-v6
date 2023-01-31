package xyz.geik.farmer.model.inventory;

import lombok.Getter;
import lombok.Setter;
import xyz.geik.farmer.model.FarmerLevel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class FarmerInv {

    // default items which farmer can take
    public static List<FarmerItem> defaultItems = new ArrayList<>();

    // farmer items which stock of items
    private Set<FarmerItem> items;
    private long capacity;

    /**
     * Farmer inv which contains items set.
     */
    public FarmerInv(Set<FarmerItem> items, long capacity) {
        this.items = items;
        this.capacity = capacity;
    }

    /**
     * Farmer inv which don't have any item in it.
     * Creating with default item set.
     */
    public FarmerInv() {
        items = new LinkedHashSet<>(defaultItems);
        capacity = FarmerLevel.getAllLevels().get(0).getCapacity();
    }

    private FarmerItem getItemByData(String name) {
        return items.stream().filter(
                item -> (item.getName().equalsIgnoreCase(name))).findFirst().orElseGet(null);
    }

    public static double getPrice(String name) {
        return defaultItems.stream().filter(item -> (item.getName().equalsIgnoreCase(name)))
                .findFirst().orElseGet(null).getPrice();
    }

    /**
     * Adding item amount to stock.
     * Respects capacity and if it above capacity
     * return additional amount.
     */
    public long sumItemAmount(String name, long amount) {
        long summed = getItemByData(name).getAmount() + amount;
        if (summed > capacity) {
            setItemAmount(name, capacity);
            return summed-capacity;
        }
        else {
            getItemByData(name).sumAmount(amount);
            return 0L;
        }
    }

    /**
     * Removing x amount of item from stock.
     * Respects 0 if amount bigger then stock.
     * And return the abs of negative number.
     */
    public long negateItemAmount(String name, long amount) {
        long negated = getItemByData(name).getAmount() - amount;
        if (negated < 0) {
            setItemAmount(name, 0);
            return Math.abs(negated);
        }
        else {
            getItemByData(name).negateAmount(amount);
            return 0L;
        }
    }

    public void setItemAmount(String name, long amount) {
        getItemByData(name).setAmount(amount);
    }
}