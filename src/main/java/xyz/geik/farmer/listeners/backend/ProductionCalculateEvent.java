package xyz.geik.farmer.listeners.backend;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerMainGuiOpenEvent;
import xyz.geik.farmer.api.handlers.FarmerItemCollectEvent;
import xyz.geik.farmer.api.handlers.FarmerItemProductionEvent;
import xyz.geik.farmer.model.AverageProduction;
import xyz.geik.farmer.model.inventory.FarmerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Minutely, Hourly and Daily Production Calculation
 *
 * @author Geik
 */
public class ProductionCalculateEvent implements Listener {

    /**
     * Listens FarmerGuiOpenEvent for recalculate cache
     *
     * @param event
     */
    @EventHandler
    public void onProductionCalculateEvent(FarmerMainGuiOpenEvent event) {
        // If production is not calculated average production
        // cache will be created and calculated
        if (!event.getFarmer().getInv().isProductionCalculated()
                && event.getFarmer().getInv().getAverageProductions().isEmpty()) {
            // Cache creation and calculation
            List<AverageProduction> averageProductions = new ArrayList<>();
            event.getFarmer().getInv().getItems().stream().filter(FarmerItem::isProductionCalculation).forEach(item -> {
                // Adds cache
                AverageProduction averageProduction = new AverageProduction(event.getFarmer(), item.getMaterial(), event.getGui());
                averageProductions.add(averageProduction);
            });
            event.getFarmer().getInv().setAverageProductions(averageProductions);
        }
    }

    /**
     * Loads generation data when item is collected
     * if all generation items wait for calculation
     * then it will remove cache
     *
     * @param event
     */
    @EventHandler
    public void productionLoadEvent(@NotNull FarmerItemCollectEvent event) {
        List<AverageProduction> averageProductions = event.getFarmer().getInv().getAverageProductions();
        if (averageProductions != null && !averageProductions.isEmpty()) {
            // When all generation items is calculated
            // Removes cache after 15 minutes
            if (averageProductions.stream().noneMatch(averageProduction -> averageProduction.isCalculating())) {
                // Calls event because of remove cache of production
                FarmerItemProductionEvent productionEvent = new FarmerItemProductionEvent(event.getFarmer());
                Bukkit.getPluginManager().callEvent(productionEvent);
            } else
                averageProductions.stream().filter(averageProduction -> (averageProduction.isCalculating() && averageProduction.getMaterial().isSimilar(event.getItem())))
                        .forEach(averageProduction -> averageProduction.setLastInput(averageProduction.getLastInput() + event.getCollectAmount()));
        }
    }

    /**
     * Removes cache after 15 minutes
     * @param event
     */
    @EventHandler
    public void productionFlushEvent(@NotNull FarmerItemProductionEvent event) {
        // Sets data is cached
        event.getFarmer().getInv().setProductionCalculated(true);
        // Removes cache after 15 minutes
        // And sets production calculated to false
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            event.getFarmer().getInv().setAverageProductions(new ArrayList<>());
            event.getFarmer().getInv().setProductionCalculated(false);
        }, 20L*60L*15L);
    }
}
