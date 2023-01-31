package xyz.geik.farmer.model.inventory;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Farmer Inventory has Set of FarmerItem
 * Because farmer has multiple items.
 */
@Setter
@Getter
public class FarmerItem {

    // Name of item in config
    // Old versions this can be INK_SACK-3
    // -3 shows data id of it.
    private String name;

    // Price of item
    private double price;

    // Amount of item
    private long amount;

    /**
     * Constructor of FarmerItem
     *
     * @param name
     * @param price
     * @param amount
     */
    public FarmerItem(String name, double price, long amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    // Summing x to amount
    public void sumAmount(long sum) {
        this.amount += sum;
    }

    // Negating x from amount
    public void negateAmount(long negate) {
        this.amount -= negate;
    }

    /**
     * Serializing FarmerItem set to flat string
     * Because it should save to database
     *
     * @param items
     * @return
     */
    public static String serializeItems(@NotNull Set<FarmerItem> items) {
        StringBuilder builder = new StringBuilder();
        for (FarmerItem item : items) {
            if (item.amount == 0)
                continue;
            builder.append(item.getName() + ":" + item.getAmount() + ",");
        }
        return (builder.length() > 0)
                ? builder.substring(0, builder.length()-1)
                : "";
    }

    /**
     * Deserializeing FarmerItem from flat string to Set<FarmerItem>
     *
     * @param items
     * @return
     */
    public static Set<FarmerItem> deserializeItems(String items) {
        if (items == null)
            return new LinkedHashSet<>(FarmerInv.defaultItems);
        HashMap<String, Long> tempItems = new LinkedHashMap<>();
        Arrays.stream(items.split(",")).forEach(key -> {
            String[] rawArr = key.split(":");
            tempItems.put(rawArr[0], Long.parseLong(rawArr[1]));
        });
        Set<FarmerItem> result = new LinkedHashSet<>(FarmerInv.defaultItems);
        result = result.stream().map(farmerItem -> {
            if (tempItems.containsKey(farmerItem.getName()))
                farmerItem.setAmount(tempItems.get(farmerItem.getName()));
            return farmerItem;
        }).collect(Collectors.toSet());
        return result;
    }
}
