package xyz.geik.farmer.modules.production.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.modules.production.Production;
import xyz.geik.glib.shades.inventorygui.InventoryGui;
import xyz.geik.glib.shades.xseries.XMaterial;

/**
 * Production model object
 * @author poyraz
 * @since 1.0.0
 */
@Getter
@Setter
public class ProductionModel {

    /**
     * Result of generation handle data in minutes
     * lastInput is the last input of the farmer collection
     */
    private int result, lastInput;

    /**
     * regionId is the region id of the farmer
     */
    private String regionId;

    /**
     * material is the material of the item
     */
    private XMaterial material;

    /**
     * isCalculating is the boolean to check if the calculation is done or not
     */
    private boolean isCalculating;

    /**
     * AverageProduction constructor
     *
     * @param farmer of production
     * @param material to calculate
     * @param gui of farmer
     */
    public ProductionModel(@NotNull Farmer farmer, XMaterial material, InventoryGui gui) {
        // It will clear when x time passed
        this.isCalculating = true;
        this.regionId = farmer.getRegionID();
        this.material = material;
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            setResult(getLastInput() * 12);
            this.setCalculating(false);
            gui.draw();
        }, 5 * 20L);
    }

    /**
     * Get the result in minutes
     *
     * @return result
     */
    public int getMin() {
        return getResult();
    }

    /**
     * Get the result in hours
     *
     * @return result
     */
    public int getHour() {
        return (getResult() * 60);
    }

    /**
     * Get the result in days
     *
     * @return result
     */
    public int getDay() {
        return (getResult() * 60 * 24);
    }

    /**
     * Updates lore of an item
     *
     * @param productionModel model of production
     * @param key key of production placeholders
     * @return String of updated lore
     */
    public static String updateLore(ProductionModel productionModel, String key) {
        if (productionModel == null || !Production.getInstance().isEnabled())
            return null;
        else {
            // If it's calculating then it will be replaced with calculating
            String calculating = Production.getInstance().getLang().getText("calculatingGeneration");
            String min = productionModel.isCalculating() ? calculating : coolFormat(productionModel.getMin());
            String hour = productionModel.isCalculating() ? calculating : coolFormat(productionModel.getHour());
            String day = productionModel.isCalculating() ? calculating : coolFormat(productionModel.getDay());
            return key.replace("{prod_min}", min)
                    .replace("{prod_hour}", hour)
                    .replace("{prod_day}", day)
                    .replace("{prod_blank}", "");
        }
    }

    /**
     * Format the number
     *
     * @param iteration integer
     * @return String of format
     */
    @Contract(pure = true)
    private static @NotNull String coolFormat(int iteration) {
        return coolFormat(iteration, 0);
    }

    /**
     * Format the number with iteration
     *
     * @param n double
     * @param iteration integer
     * @return String of format
     */
    @Contract(pure = true)
    private static @NotNull String coolFormat(double n, int iteration) {
        double d = ((double) (long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        //this determines the class, i.e. 'k', 'm' etc
        //this decides whether to trim the decimals
        // (int) d * 10 / 10 drops the decimal
        return d < 1000? //this determines the class, i.e. 'k', 'm' etc
                (d > 99.9 || isRound || d > 9.99 ? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + Production.getInstance().getNumberFormat()[iteration]
                : coolFormat(d, iteration+1);

    }
}
