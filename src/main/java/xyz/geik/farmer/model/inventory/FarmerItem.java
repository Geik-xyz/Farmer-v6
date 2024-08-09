package xyz.geik.farmer.model.inventory;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import xyz.geik.glib.shades.xseries.XMaterial;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Farmer item which contains item name, price, amount and material.
 * Farmer item is an item which farmer can store in his inventory.
 *
 * @author Geik
 */
@Setter
@Getter
public class FarmerItem {

	/**
	 * Name of item (Material name)
	 */
	private String name;

	/**
	 * Price of item
	 */
	private double price;

	/**
	 * Amount of item
	 */
	private long amount;

	/**
	 * XMaterial of item Calculated in constructor
	 */
	private XMaterial material;

	/**
	 * Constructor of FarmerItem
	 *
	 * @param name   of item
	 * @param price  of item
	 * @param amount of item
	 */
	public FarmerItem(String name, double price, long amount) {
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.material = XMaterial.matchXMaterial(name).get();
	}

	/**
	 * Clones FarmerItem
	 *
	 * @return FarmerItem
	 */
	public FarmerItem clone() {
		return new FarmerItem(this.name, this.price, this.amount);
	}

	/**
	 * Summing x to amount
	 *
	 * @param sum sum amount
	 */
	public void sumAmount(long sum) {
		this.amount += sum;
	}

	/**
	 * Negating x from amount
	 *
	 * @param negate negate amount
	 */
	public void negateAmount(long negate) {
		this.amount -= negate;
	}

	/**
	 * Serializing FarmerItem set to flat string
	 * Because it should save to database
	 *
	 * @param items list of item
	 * @return serialized string
	 */
	public static String serializeItems(@NotNull List<FarmerItem> items) {
		StringBuilder builder = new StringBuilder();
		for (FarmerItem item : items) {
			if (item.amount == 0)
				continue;
			builder.append(item.getName() + ":" + item.getAmount() + ",");
		}
		return (builder.length() > 0)
				? builder.substring(0, builder.length() - 1)
				: "";
	}

	/**
	 * Deserialize FarmerItem from flat string to a list
	 *
	 * @param items serialized item list
	 * @return List deserialized item list
	 */
	public static List<FarmerItem> deserializeItems(String items) {
		List<FarmerItem> result = new ArrayList<>();
		// Cloning default items to farmer inventory
		result.addAll(FarmerInv.defaultItems.stream().map(FarmerItem::clone).collect(Collectors.toList()));
		// Return default items if item list is null
		if (items == null)
			return result;

		HashMap<String, Long> tempItems = new LinkedHashMap<>();
		Arrays.stream(items.split(",")).forEach(key -> {
			String[] rawArr = key.split(":");
			tempItems.put(rawArr[0], Long.parseLong(rawArr[1]));
		});

		result = result.stream().map(farmerItem -> {
			if (tempItems.containsKey(farmerItem.getName()))
				farmerItem.setAmount(tempItems.get(farmerItem.getName()));
			return farmerItem;
		}).collect(Collectors.toList());
		return result;
	}

	@Override
	public String toString() {
		return "FarmerItem{" +
				"name='" + name + '\'' +
				", price=" + price +
				", amount=" + amount +
				", material=" + material +
				'}';
	}
}
