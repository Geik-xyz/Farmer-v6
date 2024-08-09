package xyz.geik.farmer.modules.production.handlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerItemCollectEvent;
import xyz.geik.farmer.api.handlers.FarmerItemProductionEvent;
import xyz.geik.farmer.api.handlers.FarmerMainGuiOpenEvent;
import xyz.geik.farmer.modules.production.Production;
import xyz.geik.farmer.modules.production.model.ProductionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Minutely, Hourly and Daily Production Calculation
 *
 * @author Geik
 */
public class ProductionCalculateEvent implements Listener {

    /**
     * Constructor of class
     */
    public ProductionCalculateEvent() {}

    /**
     * Listens FarmerGuiOpenEvent for recalculate cache
     *
     * @param event of gui open event
     */
    @EventHandler
    public void onProductionCalculateEvent(@NotNull FarmerMainGuiOpenEvent event) {
        // If production is not calculated average production
        // cache will be created and calculated
        if (!event.getFarmer().getInv().isProductionCalculated()
                && event.getFarmer().getInv().getProductionModels().isEmpty()) {
            // Cache creation and calculation
            List<ProductionModel> productionModels = new ArrayList<>();
            event.getFarmer().getInv().getItems().stream().filter(Production::isCalculateItem).forEach(item -> {
                // Adds cache
                ProductionModel productionModel = new ProductionModel(event.getFarmer(), item.getMaterial(), event.getGui());
                productionModels.add(productionModel);
            });
            event.getFarmer().getInv().setProductionModels(productionModels);
        }
    }

    /**
     * Loads generation data when item is collected
     * if all generation items wait for calculation
     * then it will remove cache
     *
     * @param event of collection
     */
    @EventHandler
    public void productionLoadEvent(@NotNull FarmerItemCollectEvent event) {
        List<ProductionModel> productionModels = event.getFarmer().getInv().getProductionModels();
        if (productionModels != null && !productionModels.isEmpty()) {
            // When all generation items is calculated
            // Removes cache after 15 minutes
            if (productionModels.stream().noneMatch(production -> production.isCalculating())) {
                // Calls event because of remove cache of production
                FarmerItemProductionEvent productionEvent = new FarmerItemProductionEvent(event.getFarmer());
                Bukkit.getPluginManager().callEvent(productionEvent);
            } else
                productionModels.stream().filter(production -> (production.isCalculating() && production.getMaterial().isSimilar(event.getItem())))
                        .forEach(averageProduction -> averageProduction.setLastInput(averageProduction.getLastInput() + event.getCollectAmount()));
        }
    }

    /**
     * Removes cache after 15 minutes
     * @param event production event
     */
    @EventHandler
    public void productionFlushEvent(@NotNull FarmerItemProductionEvent event) {
        // Sets data is cached
        event.getFarmer().getInv().setProductionCalculated(true);
        // Removes cache after 15 minutes
        // And sets production calculated to false
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            event.getFarmer().getInv().setProductionModels(new ArrayList<>());
            event.getFarmer().getInv().setProductionCalculated(false);
        }, 20L*60L*Production.getInstance().getReCalculate());
    }
}
