package xyz.geik.farmer.model.inventory;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
public class FarmerItem {

    private String name;
    private double price;
    private long amount;

    public FarmerItem(String name, double price, long amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public void sumAmount(long sum) {
        this.amount += sum;
    }

    public void negateAmount(long negate) {
        this.amount -= negate;
    }

    public static String serializeItems(Set<FarmerItem> items) {
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
