package xyz.geik.farmer.model;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.InventoryGui;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;

/**
 * @author Geik
 */
@Getter
@Setter
public class AverageProduction {

    // Result of generation handle data in minutes
    // lastInput is the last input of the farmer collection
    private int result, lastInput;

    // regionId is the region id of the farmer
    private String regionId;

    // material is the material of the item
    private XMaterial material;

    // isCalculating is the boolean to check if the calculation is done or not
    private boolean isCalculating;

    /**
     * AverageProduction constructor
     *
     * @param farmer
     * @param material
     * @param gui
     */
    public AverageProduction(@NotNull Farmer farmer, XMaterial material, InventoryGui gui) {
        // It will clear when x time passed
        this.isCalculating = true;
        this.regionId = farmer.getRegionID();
        this.material = material;
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            setResult(getLastInput()*12);
            this.setCalculating(false);
            gui.draw();
        }, 5*20L);
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
        return (getResult()*60);
    }

    /**
     * Get the result in days
     *
     * @return result
     */
    public int getDay() {
        return (getResult()*60*24);
    }
}
