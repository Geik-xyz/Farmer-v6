package xyz.geik.farmer.modules.autoharvest.handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.helpers.WorldHelper;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.modules.autoharvest.AutoHarvest;
import xyz.geik.glib.shades.xseries.XMaterial;

/**
 * Auto Harvest Listener class
 *
 * @author poyraz
 * @since 1.0.0
 */
public class AutoHarvestEvent implements Listener {

    /**
     * Constructor of class
     */
    public AutoHarvestEvent() {}

    /**
     * Main event of auto harvest
     *
     * @param event of block grow
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onHarvestGrowEvent(@NotNull BlockGrowEvent event) {
        Block block = event.getNewState().getBlock();
        XMaterial material = parseMaterial(XMaterial.matchXMaterial(event.getNewState().getType()));
        // Checks world suitable for farmer
        if (!WorldHelper.isFarmerAllowed(block.getWorld().getName()))
            return;

        // Checks auto harvest, harvests this block.
        if (!AutoHarvest.checkCrop(material))
            return;

        if (!AutoHarvest.getInstance().isWithoutFarmer()) {
            // Checks item dropped in region of a player
            // And checks region owner has a farmer
            try {
                String regionID = Main.getIntegration().getRegionID(block.getLocation());
                if (regionID == null || !FarmerManager.getFarmers().containsKey(regionID))
                    return;

                Farmer farmer = FarmerManager.getFarmers().get(regionID);

                // Checks farmer can auto harvest
                if (!farmer.getAttributeStatus("autoharvest"))
                    return;

                if (!hasStock(farmer, material)) {
                    event.setCancelled(true);
                    return;
                }
            }
            catch (Exception ignored) {}
        }

        // Checks piston
        if (AutoHarvest.getInstance().isRequirePiston()) {
            if (!pistonCheck(event.getBlock().getLocation()))
                return;
        }

        // Harvests crops
        if (harvestCrops(event.getNewState(), material))
            return;

        else if (harvestCocoa(event.getNewState(), material))
            return;

        else if (harvestBlocks(event.getNewState(), material, event))
            return;

        else return;
    }

    /**
     * Checks if block harvestable or not.
     *
     * @param farmer of region
     * @param material of farmer
     * @return status of has stock
     */
    private boolean hasStock(Farmer farmer, XMaterial material) {
        if (AutoHarvest.getInstance().isCheckStock()) {
            // Checks if farmer has autoseller module and is enabled for this farmer
            if (farmer.getAttributeStatus("autoseller"))
                return true;
            long capacity = farmer.getInv().getCapacity();
            FarmerItem item = farmer.getInv()
                    .getStockedItem(material);
            if (item.getAmount() == capacity)
                return false;

            // Seed stock check
            XMaterial seed = hasSeed(material);
            if (!seed.equals(XMaterial.AIR)) {
                if (FarmerInv.checkMaterial(seed.parseItem()))
                    return hasStock(farmer, seed);
            }
        }
        return true;
    }

    /**
     * Harvest block typed crops which remove after harvest
     *
     * @param state of block
     * @param material of block
     * @return of harvest
     */
    private boolean harvestBlocks(BlockState state, @NotNull XMaterial material, BlockGrowEvent event) {
        if (isBlockHarvestable(material)) {
            event.setCancelled(true);
            ItemStack item = material.parseItem();
            assert item != null;
            if (material.equals(XMaterial.valueOf("MELON_SLICE")))
                item.setAmount(4);
            else if (material.equals(XMaterial.valueOf("SWEET_BERRIES")))
                item.setAmount(3);
            state.getWorld().dropItemNaturally(state.getLocation(), item);
            state.setType(Material.AIR);
            return true;
        }
        else return false;
    }

    /**
     * Checks if grown crop type of block
     * @param material of block
     * @return status of block harvestable
     */
    private boolean isBlockHarvestable(@NotNull XMaterial material) {
        return material.equals(XMaterial.valueOf("SUGAR_CANE"))
                || material.equals(XMaterial.valueOf("MELON_SLICE"))
                || material.equals(XMaterial.valueOf("PUMPKIN"))
                || material.equals(XMaterial.valueOf("CACTUS"))
                || material.equals(XMaterial.valueOf("CHORUS_FLOWER"))
                || material.equals(XMaterial.valueOf("CHORUS_PLANT"));
    }

    /**
     * Checks if grown crop type of ageable crop
     * @param material of block
     * @return status of block harvestable
     */
    private boolean isCropsHarvestable(@NotNull XMaterial material) {
        boolean status = material.equals(XMaterial.valueOf("WHEAT"))
                || material.equals(XMaterial.valueOf("CARROT"))
                || material.equals(XMaterial.valueOf("POTATO"))
                || material.equals(XMaterial.valueOf("BEETROOT"))
                || material.equals(XMaterial.valueOf("SWEET_BERRIES"))
                || material.equals(XMaterial.valueOf("NETHER_WART"));
        return status;
    }

    /**
     * Checks if crop has seed for stock check
     * @param material of block
     * @return status of has seed
     */
    private XMaterial hasSeed(@NotNull XMaterial material) {
        if (material.equals(XMaterial.valueOf("WHEAT")))
            return XMaterial.valueOf("WHEAT_SEEDS");
        else if (material.equals(XMaterial.valueOf("BEETROOT")))
            return XMaterial.valueOf("BEETROOT_SEEDS");
        else return XMaterial.AIR;
    }

    /**
     * Harvests crop which age-able and makes age of it 0
     *
     * @param state of block
     * @param material of block
     * @return is harvesting succeed
     */
    private boolean harvestCrops(@NotNull BlockState state, @NotNull XMaterial material) {
        if (isCropsHarvestable(material)) {
            MaterialData data = state.getData();
            // Other crops
            if (data.getData() == 7
                    || ((material.equals(XMaterial.valueOf("NETHER_WART"))
                        || material.equals(XMaterial.valueOf("BEETROOT"))
                        || material.equals(XMaterial.valueOf("SWEET_BERRIES")))
                            && data.getData() == 3)) {
                ItemStack item = material.parseItem();
                // Checks if stock is not full then drops item
                // item of crop
                state.getWorld().dropItemNaturally(state.getLocation(), item);
                // Item of seed
                state.getBlock().getDrops().forEach(seed -> state.getWorld().dropItemNaturally(state.getLocation(), seed));
                // Makes crop to be zero age
                data.setData((byte) 0);
                state.setData(data);
            }
            return true;
        }
        else return false;
    }

    /**
     * Parses item stack of crops
     */
    private XMaterial parseMaterial(XMaterial material) {
        if (material.equals(XMaterial.valueOf("BEETROOTS")))
            material = XMaterial.valueOf("BEETROOT");
        else if (material.equals(XMaterial.valueOf("POTATOES")))
            material = XMaterial.valueOf("POTATO");
        else if (material.equals(XMaterial.valueOf("CARROTS")))
            material = XMaterial.valueOf("CARROT");
        else if (material.equals(XMaterial.valueOf("SWEET_BERRY_BUSH")))
            material = XMaterial.valueOf("SWEET_BERRIES");
        else if (material.equals(XMaterial.valueOf("MELON")))
            material = XMaterial.valueOf("MELON_SLICE");
        else if (material.equals(XMaterial.valueOf("COCOA")))
            material = XMaterial.valueOf("COCOA_BEANS");
        return material;
    }

    /**
     * Used in harvestCrops for item drops and age of crop
     * @param state of block
     * @param material of block
     */
    private boolean harvestCocoa(BlockState state, @NotNull XMaterial material) {
        if (material.equals(XMaterial.valueOf("COCOA_BEANS"))) {
            CocoaPlant data = (CocoaPlant) state.getData();
            if (data.getSize().equals(CocoaPlant.CocoaPlantSize.LARGE)) {
                ItemStack item = material.parseItem();
                item.setAmount(3);
                state.getWorld().dropItemNaturally(state.getLocation(), item);

                data.setSize(CocoaPlant.CocoaPlantSize.SMALL);
                state.setRawData(data.getData());
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if piston is near the block
     *
     * @param location of block to check
     * @return is piston check pass or not
     */
    private boolean pistonCheck(@NotNull Location location) {
        if (AutoHarvest.getInstance().isCheckAllDirections()) {
            Location loc1 = location.clone().add(-1, 0, 0);
            Location loc2 = location.clone().add(1, 0, 0);
            Location loc3 = location.clone().add(0, 0, -1);
            Location loc4 = location.clone().add(0, 0, 1);
            if (loc1.getBlock().getType().name().contains("PISTON"))
                return true;
            else if (loc2.getBlock().getType().name().contains("PISTON"))
                return true;
            else if (loc3.getBlock().getType().name().contains("PISTON"))
                return true;
            else if (loc4.getBlock().getType().name().contains("PISTON"))
                return true;
        }
        Location loc = location.clone().add(0, 1, 0);
        if (loc.getBlock().getType().name().contains("PISTON"))
            return true;
        else
            return false;
    }
}
